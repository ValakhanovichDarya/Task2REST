create table if not exists authors(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(40));
create table if not exists books(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(40), numberOfPages int, authorId BIGINT NOT NULL, FOREIGN KEY (authorId) REFERENCES authors(id));

