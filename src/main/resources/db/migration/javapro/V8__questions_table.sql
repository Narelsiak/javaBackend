CREATE TABLE questions (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           text TEXT NOT NULL,
                           image_path TEXT DEFAULT NULL,
                           test_id BIGINT NOT NULL,
                           FOREIGN KEY (test_id) REFERENCES tests(id)
);