CREATE TABLE task_category (
    task_id   int NOT NULL ,
    category_id int NOT NULL,
    PRIMARY KEY (task_id, category_id),
    FOREIGN KEY (task_id) REFERENCES tasks (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);