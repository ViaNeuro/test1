CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    full_name VARCHAR(160) NOT NULL,
    role VARCHAR(40) NOT NULL CHECK (role IN ('USER','IT_SPECIALIST','ADMIN')),
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE ticket (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(180) NOT NULL,
    category VARCHAR(80) NOT NULL,
    description TEXT NOT NULL,
    priority VARCHAR(20) NOT NULL CHECK (priority IN ('LOW','MEDIUM','HIGH','CRITICAL')),
    status VARCHAR(30) NOT NULL CHECK (status IN ('NEW','IN_PROGRESS','WAITING_USER','RESOLVED','CLOSED')),
    requester_id BIGINT NOT NULL REFERENCES app_user(id),
    assignee_id BIGINT REFERENCES app_user(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE ticket_comment (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL REFERENCES ticket(id) ON DELETE CASCADE,
    author_id BIGINT NOT NULL REFERENCES app_user(id),
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE ticket_history (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL REFERENCES ticket(id) ON DELETE CASCADE,
    actor_id BIGINT NOT NULL REFERENCES app_user(id),
    field_name VARCHAR(60) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    created_at TIMESTAMP NOT NULL
);

INSERT INTO app_user(username, password, full_name, role) VALUES
('user', '{noop}password', 'Обычный пользователь', 'USER'),
('it', '{noop}password', 'IT специалист', 'IT_SPECIALIST'),
('admin', '{noop}password', 'Администратор', 'ADMIN');
