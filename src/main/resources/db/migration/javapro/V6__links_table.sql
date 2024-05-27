CREATE TABLE IF NOT EXISTS links (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    link LONGTEXT NOT NULL,
    topic_id INT,
    FOREIGN KEY (topic_id) REFERENCES topics(id)
    );