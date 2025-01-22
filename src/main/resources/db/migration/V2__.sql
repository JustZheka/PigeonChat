ALTER TABLE chats_messages
    DROP CONSTRAINT fk_chames_on_chat;

ALTER TABLE chats_messages
    DROP CONSTRAINT fk_chames_on_message;

DROP TABLE chats_messages CASCADE;