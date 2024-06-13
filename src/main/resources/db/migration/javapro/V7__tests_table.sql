CREATE TABLE tests (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       type VARCHAR(50) NOT NULL,
                       visibility BOOLEAN NOT NULL,
                       random_question BOOLEAN NOT NULL DEFAULT 0,
                       max_attempts INT NOT NULL,
                       time_limit INT NOT NULL
);
