Portland Bus Alexa Skill written in Java

# Based on:
- https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/Developing-Your-First-Skill.html

# Usage

In the Lambda's handler field, set the handler to: 
```
com.merricklabs.portlandbus.PortlandBusStreamHandler
```

Set the `SKILL_ID` environment variable to the one belonging to your skill.

# Running locally

Use https://github.com/localstack/localstack to mock out the DynamoDB endpoints.
