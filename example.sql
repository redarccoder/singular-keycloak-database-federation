CREATE TABLE users (
            "id" TEXT PRIMARY KEY,
            "username" TEXT NOT NULL,
            "email" TEXT NOT NULL,
            "firstName" TEXT NOT NULL,
            "lastName" TEXT NOT NULL,
            "cpf" TEXT NOT NULL,
            "fullName" TEXT NOT NULL,
            "hash_pwd" TEXT NOT NULL,
            "mailVerified" BOOLEAN NOT NULL DEFAULT true,
            "enabled" BOOLEAN NOT NULL DEFAULT true,
            "groups" TEXT NOT NULL DEFAULT "" -- comma-separated list of group names, for example 'group1,group2'
 );

-- password is 'admin' with SHA1
INSERT INTO users VALUES ('1111', 'admin', 'user1111@example.com', 'Some', 'User', '', 'Some User', 'd033e22ae348aeb5660fc2140aec35850c4da997');
