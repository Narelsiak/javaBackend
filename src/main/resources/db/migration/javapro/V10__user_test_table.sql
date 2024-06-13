CREATE TABLE user_tests (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            test_id BIGINT NOT NULL,
                            start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            end_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            is_completed TINYINT(1) DEFAULT 0,
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (test_id) REFERENCES tests(id)
);