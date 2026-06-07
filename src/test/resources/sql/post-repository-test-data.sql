insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (1, 'gihyung.coding@gmail.com', 'gihyunglee', 'Seoul', 'aaaaaa-aaaa-aaaa-aaaa-aaaaaa', 'ACTIVE', 0);
insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
values (1, 'hello world', 1780000000000, 0, 1);

ALTER TABLE posts ALTER COLUMN id RESTART WITH 2;