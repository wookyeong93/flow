CREATE TABLE custom_extension (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,

    CONSTRAINT ck_custom_extension_name_format
        CHECK (name ~ '^[a-z0-9]+$' AND name !~ '^[0-9]+$')
);
