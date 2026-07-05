CREATE TABLE processed_messages (
    message_id VARCHAR(255) PRIMARY KEY,
    consumer_group VARCHAR(255) NOT NULL,
    processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_processed_messages_group ON processed_messages(consumer_group);