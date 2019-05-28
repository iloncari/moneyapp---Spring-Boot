create table if not exists users(
  id INT auto_increment PRIMARY KEY not null,
  username varchar(20) not null,
  password varchar(200) not null,
  enabled bit not null
);

create table if not exists authorities(
    id INT auto_increment PRIMARY KEY not null,
    username varchar(20) not null,
    authority varchar(20) not null
);

create table if not exists wallet(
    id INT auto_increment primary key not null,
    created_date timestamp,
    name varchar(50) not null,
    type varchar(50) not null,
    userId INT,
    foreign key (userId) REFERENCES users(id)
);

create table if not exists expense(
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    created_date TIMESTAMP,
    name varchar(50) NOT NULL,
    price double precision not null,
    type varchar(50) not null,
    walletid INT,
    FOREIGN KEY (walletid) references wallet(id)
);
