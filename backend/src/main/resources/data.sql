INSERT INTO tb_users (username, password, role)
VALUES ('admin', '$2a$10$6XiU1hJ0W9TWGcEFyDWxee2Jh8vxDb.5wBB3YElFHQJKS0Ppu6k4q', 1)
ON CONFLICT (username) DO NOTHING;