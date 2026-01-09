CREATE TABLE tasks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    statement VARCHAR(255) NOT NULL,
    order_number INT NOT NULL,
    type VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_task PRIMARY KEY (id),
    CONSTRAINT fk_task_course FOREIGN KEY (course_id)
         REFERENCES course(id),
    CONSTRAINT uq_task_statement UNIQUE (course_id, statement),
    CONSTRAINT uq_task_order UNIQUE (course_id, order_number)
);