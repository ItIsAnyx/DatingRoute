CREATE TYPE route_type AS ENUM('PEDESTRIAN');

CREATE TABLE IF NOT EXISTS routes(
    id UUID PRIMARY KEY,
    chat_id BIGINT NOT NULL UNIQUE,
    updated_at TIMESTAMP NOT NULL,
    route_type route_type NOT NULL,
    CONSTRAINT fk_routes__chat_id_chat FOREIGN KEY (chat_id) REFERENCES chats(id)
);

CREATE TABLE IF NOT EXISTS route_points(
    id UUID PRIMARY KEY,
    route_id UUID NOT NULL,
    order_index INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_routes_points__route_id_route FOREIGN KEY (route_id) REFERENCES routes(id)
);