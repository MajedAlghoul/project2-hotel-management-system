USE hms;
insert into role (name) values ("ROLE_ADMIN");
insert into role (name) values ("ROLE_CUSTOMER");
insert into role (name) values ("ROLE_EMPLOYEE");
insert into _user (dtype, email, name, password_hash) values ("Employee", "ahmad@email.com", "Ahmad", "$2a$10$p9Ib.UcUK18pMPCIcuabxe1GXJd2kEeTWtWcGX8C7sIL4oKsgZGMG");
insert into user_role (role_id, user_id) values (1, 1);
insert into user_role (role_id, user_id) values (3, 1)