-- This migration adds a default admin user for testing purposes
-- Default password is 'admin123'
INSERT INTO users (first_name, last_name, username, email, password, created_at)
VALUES ('Admin', 'User', 'admin', 'admin@rupeexfer.com', 
        '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.','2023-01-01 00:00:00');

-- Assign admin role to the admin user
INSERT INTO user_roles (user_id, role_id)
VALUES (
    (SELECT id FROM users WHERE username = 'admin'),
    (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
);

-- Insert a test user
-- Default password is 'user123'
INSERT INTO users (first_name, last_name, username, email, password, created_at)
VALUES ('Test', 'User', 'testuser', 'test@rupeexfer.com', 
        '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.','2023-01-01 00:00:00');

-- Assign user role to the test user
INSERT INTO user_roles (user_id, role_id)
VALUES (
    (SELECT id FROM users WHERE username = 'testuser'),
    (SELECT id FROM roles WHERE name = 'ROLE_USER')
);
