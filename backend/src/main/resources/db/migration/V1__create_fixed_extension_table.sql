CREATE TABLE fixed_extension (
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(20) NOT NULL UNIQUE,
    checked BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT ck_fixed_extension_name_format
        CHECK (name ~ '^[a-z0-9]+$' AND name !~ '^[0-9]+$')
);
