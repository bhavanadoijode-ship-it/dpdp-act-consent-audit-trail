import os
from flask import Flask
from dotenv import load_dotenv
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address

load_dotenv()

def create_app():
    app = Flask(__name__)
    app.config.from_prefixed_env()

    # Rate limiting
    limiter = Limiter(get_remote_address, app=app, default_limits=["30 per minute"])

    # Register blueprints (routes)
    from routes.describe import describe_bp
    from routes.recommend import recommend_bp
    from routes.generate_report import generate_report_bp
    app.register_blueprint(describe_bp)
    app.register_blueprint(recommend_bp)
    app.register_blueprint(generate_report_bp)

    return app

app = create_app()

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
