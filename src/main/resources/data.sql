insert into users(id, username, password, enabled)
values ( 1, 'admin', '$2a$10$cxYLqzVuWNQo8PKUwYCaIu9bxozb7GVSDLcGm5NqLrokUVH9F7B.6', 1);
insert into users(id, username, password, enabled)
values ( 2, 'user', '$2a$10$Xs4Nh83G6xEejvL7wutSi.Bav9Xz/WPq4QgPd73Tgs7zk8yjze7d.', 1);

insert into authorities (id, username, authority)
values ( 1, 'admin', 'ADMIN');
insert into authorities (id, username, authority)
values ( 2, 'admin', 'USER');
insert into authorities (id, username, authority)
values ( 3, 'user', 'USER');


insert into wallet (id, created_date, name, type, userId)
values ( 1, CURRENT_TIMESTAMP(),'Adminov KUNSKI novčanik', 'KUNSKI', 1);

insert into expense (id, created_date, name, price, type, walletid)
values ( 1, CURRENT_TIMESTAMP(),'Adminov trošak 1', 15.43,'PIĆE', 1);

insert into expense (id, created_date, name, price, type, walletid)
values ( 2, CURRENT_TIMESTAMP(),'Adminov novi trošak 2', 2.43,'HRANA', 1);

insert into expense (id, created_date, name, price, type, walletid)
values ( 3, CURRENT_TIMESTAMP(),'Adminov novi trošak 3', 6666.43,'HRANA', 1);

insert into expense (id, created_date, name, price, type, walletid)
values ( 4, CURRENT_TIMESTAMP(),'Adminov trošak 4', 65.43,'DUĆAN', 1);

