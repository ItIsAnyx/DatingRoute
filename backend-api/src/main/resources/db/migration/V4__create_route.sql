CREATE TYPE route_type AS ENUM('PEDESTRIAN');

CREATE TABLE IF NOT EXISTS routes(
    id UUID PRIMARY KEY,
    chat_id BIGINT NOT NULL UNIQUE,
    places JSONB NOT NULL,
    updated_at TIMESTAMP,
    route_type route_type NOT NULL;
    CONSTRAINT fk_routes__chat_id_chat FOREIGN KEY (chat_id) REFERENCES chats(id) ON CASCADE DELETE
);