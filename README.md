# AuthMod

![Build status](https://travis-ci.org/Mcdostone/authmod.svg?branch=master)
![Build status](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean.authmod%3Aauthmod&metric=alert_status)
![Bugs](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean.authmod%3Aauthmod&metric=bugs)
![code smells](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean.authmod%3Aauthmod&metric=code_smells)
![duplicated](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean.authmod%3Aauthmod&metric=duplicated_lines_density)
![Maintanability](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean.authmod%3Aauthmod&metric=sqale_rating)
![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=io.chocorean.authmod%3Aauthmod&metric=vulnerabilities)


## Table of contents

- [General](#general)
- [Operation](#operation)
- [Getting started for developers](#Getting started for developers)
- [Contact](#contact)
- [License](#license)
- [Webpage](#webpage)
- [Install](#install)
- [Issues](#issues)


## General
AuthMod is a mod allowing you to accept players playing with the Minecraft demo (players who have not paid for the game) safely.

 - Possibility to choose the delay to automatically exclude not authenticated players.
 - Enable or disable the registration of players
 - Enable or disable the authentication of players (if disabled, everyone can join the server)
 
Features            | File strategy         | Database strategy        |
| ----------------- |:---------------------:|:------------------------:| 
| Registration      | **✖**                 | **✔**                   |
| authentication    | **✔**                 | **✔**                   |


## Operation

At each new connection to the server, the mod will ask the new player to enter a password. This password must be entered in order to play. Otherwise, the player will be unable to play and kicked after a while.


## Getting started for developers

### 1. Setup the database

I used mariadb but you can use whatever you want. Create a database named `minecraft`
```sql
CREATE DATABASE minecraft;
```

After that, create a new table `players`
```sql
CREATE TABLE IF NOT EXISTS `players` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `creationDate` datetime NOT NULL DEFAULT current_timestamp(),
  `updatedOn` datetime NOT NULL DEFAULT current_timestamp(),
  `avatar` varchar(255) DEFAULT NULL,
  `lastConnection` datetime DEFAULT current_timestamp(),
  `password` varchar(255) DEFAULT NULL,
  `isAdmin` tinyint(1) DEFAULT 0,
  `isBan` tinyint(1) DEFAULT 0,
  `uuid` varchar(255) DEFAULT '',
  `username` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
```

Here some data if you're interested in:
```sql
INSERT INTO players (id, firstname, lastname, email, creationDate, updatedOn, avatar, lastConnection, password, isAdmin, isBan, uuid, username) VALUES
	(1, 'Richard', 'Stallman', 'richard.stallman.gnu.org', '2018-06-12 22:59:09', '2018-06-12 22:59:09', NULL, '2018-06-12 22:59:09', NULL, 0, 0, '', ''),
	(2, 'Linus', 'Torvalds', 'linus.torvalds.linux.org', '2018-06-12 22:59:09', '2018-06-12 22:59:09', 'https://lh3.googleusercontent.com/SyqrxNLd6Eo4-AwTGXktIfMnx4dOBREcZCZvocEVue-GsuBB1dYDjJorgHviJeTHzHYfAKs4wiHmkDk=w1211-h1210-rw-no', '2018-06-12 22:59:09', NULL, 0, 0, '', ''),
	(3, 'Tony', 'Stark', 'tony.stark.stark-company.com', '2018-06-12 22:59:09', '2018-06-12 22:59:09', 'http://awakenthegreatnesswithin.com/wp-content/uploads/2017/12/Robert-Downey-Jr-Quotes-1.jpg', '2018-06-12 22:59:09', NULL, 0, 0, '', ''),
	(4, 'El', 'Profesor', 'el-profesor@cdp.com', '2018-06-12 22:59:09', '2018-06-12 22:59:09', 'https://pbs.twimg.com/profile_images/966755261619851264/_SReV3xm_400x400.jpg', '2018-06-12 22:59:09', NULL, 1, 0, '', ''),
	(5, 'coding', 'horror', 'coding-horror@sof.com', '2018-06-12 22:59:09', '2018-06-12 22:59:09', 'https://pbs.twimg.com/profile_images/632821853627678720/zPKK7jql_400x400.png', '2018-06-12 22:59:09', NULL, NULL, 0, '', ''),
	(6, 'Louis', 'De Funes', 'louis.de-funes@actor.fr', '2018-06-12 22:59:09', '2018-06-12 22:59:09', NULL, '2018-06-12 22:59:09', NULL, 0, 1, '', '');
```

you can use this [calculator](https://www.dailycred.com/article/bcrypt-calculator) to generate a password!

### 2. Configuring your IDE
Follow [this documentation](https://mcforge.readthedocs.io/en/latest/gettingstarted/) to have a functional environment with your preferred IDE!

### 3. Editing the *authmod.cfg* file

A example is available [here](https://github.com/Mcdostone/authmod/blob/master/src/main/resources/authmod.cfg).


## Contact

[Mail](mailto:baptiste.chocot@gmail.com)


## License

[GNU v3.0](https://www.gnu.org/licenses/gpl-3.0.fr.html)


## Webage

Further informations and downloads links are available on the [Curse project page](https://minecraft.curseforge.com/projects/authmod).


## Install

First make sure you have Forge installed on your server. Then, put AuthMod in the server's mods/ directory. Reload the server.


## Issues

[Right here](https://github.com/Chocorean/authmod/issues).
