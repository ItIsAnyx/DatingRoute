CREATE TABLE IF NOT EXISTS chats (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    CONSTRAINT fk_chats__user_id__users FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_chats_user_id ON chats(user_id);

CREATE TYPE message_type AS ENUM('USER_MESSAGE', 'AI_MESSAGE', 'CONFIRMATION', 'ERROR');

CREATE TABLE IF NOT EXISTS messages (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    message_type message_type NOT NULL,
    send_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_messages__chat_id__chats FOREIGN KEY (chat_id) REFERENCES chats(id),
    CONSTRAINT fk_messages__user_id__users FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_messages_chat_id ON messages(chat_id);


