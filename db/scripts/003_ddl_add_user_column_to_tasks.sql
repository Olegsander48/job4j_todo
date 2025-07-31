ALTER TABLE tasks
ADD user_id int REFERENCES todo_user(id);