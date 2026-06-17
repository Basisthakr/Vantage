# Vantage

A Spring Boot REST service that evaluates how well a resume matches a job description.
It uses Spring AI to call an LLM and return a structured fit assessment -- score,
matched skills, missing skills, and a one-line recommendation. The Spring AI
abstraction layer means the underlying model is swappable with no code changes.

---

## Setup

**Required environment variable (default provider):**
```
OPENAI_API_KEY=sk-...
```

**Run:**
```bash
./mvnw spring-boot:run
```

**Alternate provider (Groq, no code change):**
```bash
GROQ_API_KEY=gsk_... ./mvnw spring-boot:run -Dspring.profiles.active=groq
```
This activates `application-groq.properties`, which points the OpenAI-compatible
Spring AI starter at Groq's API and switches the model to llama3-8b-8192.
The service layer is unaware of the swap -- same code, different model.

---

## Example

```bash
curl -s -X POST http://localhost:8080/api/v1/fit-assessment \
  -H "Content-Type: application/json" \
  -d '{
    "resumeText": "Java developer, 5 years Spring Boot, REST APIs, PostgreSQL, AWS EC2, Docker.",
    "jobDescriptionText": "Backend engineer needed. Must have Java, Spring Boot, AWS. Kubernetes preferred."
  }' | jq
```

**Response:**
```json
{
  "score": 78,
  "matchedSkills": ["Java", "Spring Boot", "AWS", "Docker"],
  "missingSkills": ["Kubernetes"],
  "recommendation": "Strong match -- apply, but expect to address gaps in Kubernetes"
}
```

**Validation error (400):**
```json
{
  "message": "Validation failed",
  "details": ["resumeText: Resume text must not be blank"]
}
```

---

## Spring AI features used

**ChatClient and ChatClient.Builder**
The OpenAI starter auto-configures a `ChatClient.Builder` bean. The service
injects the Builder, calls `.build()` once at startup, and reuses the `ChatClient`
across requests. Injecting the Builder (not `ChatClient` directly) is the Spring AI
idiom -- it lets you attach defaults like system messages or advisors before building.

**PromptTemplate with a resource file**
The prompt lives in `src/main/resources/prompts/fit-assessment-prompt.txt`.
`PromptTemplate` loads it from the classpath and substitutes `{resume}` and
`{jobDescription}` at request time. Keeping the prompt in a file separates
content from code: you can tune wording without recompiling.

**Structured output via `.entity(FitAssessment.class)`**
This is the core Spring AI feature. Internally, `BeanOutputConverter` reflects
on the `FitAssessment` record, generates a JSON schema, appends format instructions
to the prompt, calls the model, and deserializes the JSON response into a
`FitAssessment` instance via Jackson. If the model returns malformed JSON or
omits a field, Jackson throws and the service catches it as a 502 error.

**Provider abstraction**
The service depends on `ChatClient` only -- no OpenAI-specific types anywhere
in the business logic. Swapping providers is a pom.xml + application.properties change.
The Groq profile (`-Dspring.profiles.active=groq`) demonstrates this: same code,
different model, just a different base URL and API key in config.
