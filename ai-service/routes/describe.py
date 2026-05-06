from flask import Blueprint, request, jsonify
from services.groq_client import generate_response

describe_bp = Blueprint("describe", __name__)

@describe_bp.route("/describe", methods=["POST"])
def describe():
    data = request.get_json()
    text = data.get("text")

    ai_response = generate_response(text)

    return jsonify({
        "generated_at": "now",
        "message": ai_response
    })