CREATE TABLE user_answers (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_test_id BIGINT NOT NULL,
                              question_id BIGINT NOT NULL,
                              user_response TEXT,
                              is_correct TINYINT(1),
                              answer_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_test_id) REFERENCES user_tests(id),
                              FOREIGN KEY (question_id) REFERENCES questions(id)
);
