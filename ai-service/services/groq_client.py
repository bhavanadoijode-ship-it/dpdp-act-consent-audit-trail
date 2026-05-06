import os
from groq import Groq
from dotenv import load_dotenv   

load_dotenv()                

client = Groq(api_key=os.getenv("GROQ_API_KEY"))

def generate_response(text):
    response = client.chat.completions.create(
    model="meta-llama/llama-4-scout-17b-16e-instruct",
    messages=[
        {"role": "user", "content": text}
    ]
)
    return response.choices[0].message.content