create table T_USER
(
    id         bigint(20) NOT NULL auto_increment,
    name       varchar(50),
    age        int(3),
    address_id bigint(20),
    password   varchar(50),
    isAdmin    boolean
);
create table T_PHONE
(
    id      bigint(20) NOT NULL auto_increment,
    user_id bigint(20),
    number  varchar(50)
);
create table T_ADDRESS
(
    id      bigint(20) NOT NULL auto_increment,
    user_id bigint(20),
    street  varchar(50)
);

create sequence hibernate_sequence start with 1 increment by 1;