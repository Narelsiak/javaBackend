CREATE TABLE options (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         text TEXT NOT NULL,
                         question_id BIGINT NOT NULL,
                         is_correct TINYINT(1),
                         FOREIGN KEY (question_id) REFERENCES questions(id)
);
