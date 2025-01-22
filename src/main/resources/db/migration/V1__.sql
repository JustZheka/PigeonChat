CREATE TABLE accounts
(
    login      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    profile_id UUID,
    CONSTRAINT pk_accounts PRIMARY KEY (login)
);

CREATE TABLE chats
(
    id     UUID NOT NULL,
    title  VARCHAR(255),
    avatar VARCHAR(255),
    CONSTRAINT pk_chats PRIMARY KEY (id)
);

CREATE TABLE chats_messages
(
    chat_id     UUID NOT NULL,
    messages_id UUID NOT NULL
);

CREATE TABLE chats_profiles
(
    chat_id     UUID NOT NULL,
    profiles_id UUID NOT NULL
);

CREATE TABLE messages
(
    id         UUID NOT NULL,
    text       VARCHAR(255),
    chat_id    UUID,
    profile_id UUID,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE profile
(
    id                UUID                        NOT NULL,
    username          VARCHAR(255)                NOT NULL,
    birthday          TIMESTAMP WITHOUT TIME ZONE,
    registration_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description       VARCHAR(255),
    avatar            VARCHAR(255)                NOT NULL,
    email             VARCHAR(255),
    user_role         VARCHAR(255),
    CONSTRAINT pk_profile PRIMARY KEY (id)
);

CREATE TABLE profile_chats
(
    profile_id UUID NOT NULL,
    chats_id   UUID NOT NULL
);

ALTER TABLE chats_messages
    ADD CONSTRAINT uc_chats_messages_messages UNIQUE (messages_id);

ALTER TABLE accounts
    ADD CONSTRAINT FK_ACCOUNTS_ON_PROFILE FOREIGN KEY (profile_id) REFERENCES profile (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_CHAT FOREIGN KEY (chat_id) REFERENCES chats (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_PROFILE FOREIGN KEY (profile_id) REFERENCES profile (id);

ALTER TABLE chats_messages
    ADD CONSTRAINT fk_chames_on_chat FOREIGN KEY (chat_id) REFERENCES chats (id);

ALTER TABLE chats_messages
    ADD CONSTRAINT fk_chames_on_message FOREIGN KEY (messages_id) REFERENCES messages (id);

ALTER TABLE chats_profiles
    ADD CONSTRAINT fk_chapro_on_chat FOREIGN KEY (chat_id) REFERENCES chats (id);

ALTER TABLE chats_profiles
    ADD CONSTRAINT fk_chapro_on_profile FOREIGN KEY (profiles_id) REFERENCES profile (id);

ALTER TABLE profile_chats
    ADD CONSTRAINT fk_procha_on_chat FOREIGN KEY (chats_id) REFERENCES chats (id);

ALTER TABLE profile_chats
    ADD CONSTRAINT fk_procha_on_profile FOREIGN KEY (profile_id) REFERENCES profile (id);