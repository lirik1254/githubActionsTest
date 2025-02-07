CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password TEXT NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       profile_picture TEXT
);

CREATE TABLE documents (
                           document_id SERIAL PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           file_url TEXT NOT NULL,
                           category VARCHAR(100),
                           upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           author_id INTEGER,
                           CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE SET NULL
);

insert into "users"(name, email, password, role, profile_picture)
values ('Кирилл', 'kirill_shulzhik@mail.ru', 'mypass', 'student', 'myimage.jpg'),
       ('Ольга', 'olga_aue@mail.ru', 'myolgapass', 'student', 'myolgaimage.jpg'),
       ('Олег', 'oleg_gazmanov@yandex.ru', 'olegpass', 'student', 'olegimage.jpg'),
       ('Руслан', 'ruslan_nahimov@rambler.com', 'ruslanpass', 'student', 'ruslanimage.png');

insert into "documents"(title, file_url, category, upload_date, author_id)
values ('Инженерный документ1', 'EngineeringDoc1.img', 'engineering',
        '2020-10-10', 1),
       ('Инженерный документ2', 'EngineeringDoc2.img', 'engineering',
        '2021-10-10', 2),
       ('Инженерный документ3', 'EngineeringDoc3.img', 'engineering',
        '2022-10-10', 3),
       ('Инженерный документ4', 'EngineeringDoc4.img', 'engineering',
        '2023-10-10', 4)