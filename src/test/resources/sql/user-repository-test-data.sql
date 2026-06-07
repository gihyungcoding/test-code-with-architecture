insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (1, 'gihyung.coding@gmail.com', 'gihyunglee', 'Seoul', 'aaaaaa-aaaa-aaaa-aaaa-aaaaaa', 'ACTIVE', 0),
       (2, 'gihyung.coding2@gmail.com', 'gihyunglee2', 'Seoul', 'aaaaaa-aaaa-aaaa-aaaa-aaaaaa', 'PENDING', 0),
       (3, 'gihyung.coding3@gmail.com', 'gihyunglee3', 'Seoul', 'aaaaaa-aaaa-aaaa-aaaa-aaaaaa', 'INACTIVE', 0);
ALTER TABLE users ALTER COLUMN id RESTART WITH 4;