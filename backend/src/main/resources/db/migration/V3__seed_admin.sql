-- ============================================================
-- V3 — Seed default admin user
-- Password is BCrypt of 'Admin@1234'
-- Change immediately after first login
-- ============================================================

INSERT INTO users (
    full_name,
    email,
    password,
    enabled
)
VALUES (
    'System Admin',
    'admin@internship.com',
    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
    TRUE
);

INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_ADMIN'
FROM   users
WHERE  email = 'admin@internship.com';