CREATE TABLE task_options (
    id BIGINT NOT NULL AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    option_text VARCHAR(80) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_task_option PRIMARY KEY (id),
    CONSTRAINT fk_option_task FOREIGN KEY (task_id)
        REFERENCES tasks(id),

    CONSTRAINT uq_option_text UNIQUE (task_id, option_text)
);
