INSERT into users_test(id,email,first_name,last_name,password,username)
VALUES (1, 'pestrikv1337@gmail.com', 'Anton', 'Pestrikov', 'password', 'username'),
       (2, 'anton1337@gmail.com', 'Thony', 'Goikolov', 'password', 'another_username');

INSERT INTO role_test(id, name)
VALUES (1, 'USER'),
       (2, 'ADMIN'),
       (3, 'PREMIUM');

INSERT INTO user_role_test(user_id,role_id)
VALUES (1,2),
       (2,1);

INSERT INTO task_test(id, created, data, deadline, modified, user_id, description, status)
VALUES (1, '2023-05-07 14:42:16.553606', 'New', '2023-07-07 14:42:16.553606', '2023-05-07 15:42:16.553606', 1, 'description', 'BACKLOG');


INSERT INTO device_test(id, created, device_token, last_login, user_id)
VALUES (1, '2023-05-03 14:42:16.553606', 'f45b2dac-ee52-11ed-a05b-0242ac120003', '2023-07-03 14:42:16.553606', 1),
    (2, '2023-06-03 14:42:16.553606', 'a700584d-8485-40dd-96b8-483b2c4d8601', '2023-08-03 14:42:16.553606', 2);

INSERT INTO refresh_token_test(id, token, device_id, user_id)
VALUES (1, '3b68d4f6-ee53-11ed-a05b-0242ac120003',1,1),
       (2, '3e438216-eea8-4de1-a8a3-078dbf21fe1e',2,2);



