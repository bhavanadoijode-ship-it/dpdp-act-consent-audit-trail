# AI Service — Consent Audit Trail

This is the AI microservice for Tool-94 (Consent Audit Trail). It exposes three endpoints: `/describe`, `/recommend`, and `/generate-report`.

## Setup

1. Clone the repository and navigate to `ai-service/`.
2. Create a `.env` file (see `.env.example`).
3. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```
4. Run the service:
   ```bash
   flask run --host=0.0.0.0 --port=5000
   ```

## Environment Variables

| Variable         | Description                |
|------------------|---------------------------|
| GROQ_API_KEY     | Groq API key              |
| ...              | Add more as needed        |

## Endpoints

### POST `/describe`
- **Input:** JSON with required fields
- **Output:** Structured JSON with `generated_at`

### POST `/recommend`
- **Input:** JSON with required fields
- **Output:** Array of recommendations (action_type, description, priority)

### POST `/generate-report`
- **Input:** JSON with required fields
- **Output:** Structured report JSON (title, summary, overview, key items, recommendations)

## Docker

Build and run with Docker:
```bash
docker build -t ai-service .
docker run -p 5000:5000 --env-file .env ai-service
```

## TODO
- Implement Groq API integration
- Add input validation and error handling
- Complete prompt templates
- Write unit tests
