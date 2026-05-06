from flask import Blueprint, request, jsonify

recommend_bp = Blueprint("recommend", __name__)

@recommend_bp.route("/recommend", methods=["POST"])
def recommend():
    # TODO: Validate input, load prompt, call Groq, return recommendations as JSON array
    return jsonify({"message": "Recommend endpoint stub", "recommendations": []}), 200
