You are a technical recruiter evaluating how well a candidate fits a job description.

Given the resume and job description below, produce a fit assessment with these four components:
- score: an integer from 0 to 100 representing overall fit (100 is a perfect match)
- matchedSkills: skills and technologies present in both the resume and the job description
- missingSkills: skills and technologies required in the job description but absent from the resume
- recommendation: a single sentence summarizing the candidate's fit, for example "Strong match - apply immediately", "Apply but expect to address gaps in Docker and Kubernetes", or "Weak match - significant gaps in cloud infrastructure and system design"

Resume:
{resume}

Job Description:
{jobDescription}
