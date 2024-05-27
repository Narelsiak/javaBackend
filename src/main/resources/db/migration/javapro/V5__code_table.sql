CREATE TABLE IF NOT EXISTS sourcecodes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    code LONGTEXT NOT NULL,
    topic_id INT,
    FOREIGN KEY (topic_id) REFERENCES topics(id)
);