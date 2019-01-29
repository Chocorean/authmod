<div align="center">
  <br>
  <img
    alt="DEV"
    src="./src/main/resources/logo.png"
    width=200px
  />
  <br/>
  <h1>Authmod</h1>
  <strong>Server side mod which allows to safely accept demo versions on your minecraft server.</strong>
</div>
<br/>
<p align="center">
  <a href="https://travis-ci.com/Chocorean/authmod">
    <img src="https://travis-ci.com/Chocorean/authmod.svg?branch=master" alt="build status"/>
  </a>
  <a href="https://sonarcloud.io/dashboard?id=authmod">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=alert_status" alt="build status on sonarcloud"/>
  </a>
  <a href="https://sonarcloud.io/dashboard?id=authmod">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=bugs" alt="bugs"/>
  </a>
  <a href="https://sonarcloud.io/dashboard?id=authmod">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=code_smells" />
  </a>
  <a href="https://sonarcloud.io/dashboard?id=authmod">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=duplicated_lines_density" />
  </a>
  <a href="https://sonarcloud.io/dashboard?id=authmod">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=sqale_rating" />
  </a>
  <a href="https://sonarcloud.io/dashboard?id=authmod">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=vulnerabilities" />
  </a>
  <a href="https://img.shields.io/badge/forge%20version-1.12.2-blue.svg">
    <img src="https://img.shields.io/badge/forge%20version-1.12.2-blue.svg" />
  </a>
  <a href="https://img.shields.io/badge/java-1.8-blue.svg">
    <img src="https://img.shields.io/badge/java-1.8-blue.svg" />
  </a>
</p>


# AuthMod

## Table of contents

- [What is Authmod?](#what-is-authmod)
- [How it works](#how-it-works)
- [Getting started for developers](#getting-started-for-developers)
  - [Requirements](#requirements)
  - [Installation](#installation)
  - [Using the file strategy](#using-the-file-strategy)
  - [Using the database strategy](#using-the-database-strategy)
- [Getting started for administrators](#getting-started-for-administrators)
- [Issues](#issues)
- [Website](#website)
- [Resources](#resources)
- [Contact](#contact)
- [Contributors](#contributors)


## What is Authmod?

*We are 2 students from TELECOM Nancy wanted to create a minecraft server for our school. At the beginning, the server was opened to everyone. We wanted to  accept only students from our school while accepting students that didn't buy the game. We were lazy to build a custom launcher to do that. So we came up with this idea to build a mod adding a authentication layer that replaces the classic [mojang one](https://wiki.vg/Authentication).*


AuthMod is a server side Minecraft mod allowing you to accept premium or demo minecraft accounts safely. What is important to remind with this mod is **the mojang authentication cannot be used**. So if you rely on this, this mod is maybe not a good solution for you. Authmod proposes a set of interesting features:

 - Possibility to enable or disable the registration on the minecraft server. 
 - Possibility to enable or disable the authentication on the minecraft server.
 - Possibility to register a list of allowed users.
 - Possibility to exclude a player if he's not logged after a certain delay.
 
 All the data related to the registration, the authentication... are stored either in a SQL database or a file.

Features            | File strategy         | Database strategy        |
| ----------------- |:---------------------:|:------------------------:| 
| Registration      | **✔**                 | **✔**                    |
| Authentication    | **✔**                 | **✔**                    |


### How it works

The mod provides to the users a set of commands that can be used once connected on the server. Those commands are:
```bash
# Allow the user to authenticate on the server
/login email@example.com password

# Allow the user to register on the server
/register email@example.com password

# Tell to the user whether authenticated
/logged
```

For the `/login` command, once this command is entered by the user, the mod will check whether **the email address, the password and the username**  correspond to data stored in a database or a CSV file (it depends on the strategy you choose).

## Getting started for developers

### Requirements
 - [Gradle](https://gradle.org/): build tool used by the forge community
 - [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) installed 
 - Your prefered java IDE

### Installation

Here the steps to follow if you want to contribute or hack the project:

1. First step is to clone the repository:
```bash
git clone https://github.com/Chocorean/authmod
```
2. For this step, you just have to follow [this documentation](https://mcforge.readthedocs.io/en/latest/gettingstarted/) in order to setup you developer environment.

3. Run once the minecraft server in order to generate all the necessary files.
4. Accept the EULA agreement by modifing the file `run/eula.txt`.
5. Modify the `run/server.properties` and switch `online-mode` to `false`.
6. The last step is simply configure the `authmod.cfg`. An example is available [here](https://github.com/Chocorean/authmod/blob/master/src/main/resources/authmod.cfg). In a development environment, this file is located in `run/authmod.cfg`.

### Using the file strategy

The only thing to do is to modify the `authmod.cfg` file:
```bash
general {
  # ...
  # S:strategy=DATABASE
  S:strategy=FILE
}
```
And that's it! There is nothing particular to do. When using this strategy, the `authmod_players.csv` file will be created in the `config/` folder.
Each row is composed of 4 types of data:
 - The address email
 - The username of the player
 - The hashed password
 - `true` whether the player is banned


### Using the database strategy

If you want to test the `database` strategy, you need a database instance running on your machine. For those who are familiar with docker, there is a `docker-compose.yml` file available to setup everything with no worries. Otherwise, install one manually. We use by default [mariadb](https://mariadb.org/) but any other classic SQL database should be ok. 

Change the `authmod.cfg` configuration by modifying this:
```bash
general {
  # ...
  # S:strategy=FILE
  S:strategy=DATABASE
}
```
Don't forget to configure in this file all information related to the database (under the `database {...}` key).
 
The last step is to init the database and a table `players`:

```sql
/* Create the database */
CREATE OR REPLACE DATABASE minecraft;

/* Create the table containing the players data */
CREATE TABLE IF NOT EXISTS minecraft.players (
  id int(11) NOT NULL AUTO_INCREMENT,
  email varchar(255) NOT NULL,
  password varchar(255) DEFAULT NULL,
  uuid varchar(255) DEFAULT NULL,
  username varchar(255) NOT NULL,
  banned tinyint(1) DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY unique_email (email),
  UNIQUE KEY unique_uuid (uuid),
  UNIQUE KEY unique_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* Insert two players, passwords are not set*/
INSERT INTO minecraft.players (id, email, password, uuid, username, banned) VALUES
  (1, 'richard.stallman.gnu.org', NULL, NULL, 'stallman', 0),
  (2, 'linus.torvalds.linux.org', NULL, NULL, 'linux', 0);
```

## Getting started for administrators

1. stop your minecraft server.
2. Add `authmod-X.X.jar` in the `mods/` directory:
```bash
# This command downloads the latest version of authmod
# Execute it in the mods/ folder
curl -s https://api.github.com/repos/chocorean/authmod/releases/latest \
  | grep "browser_download_url.*jar" \
  | cut -d : -f 2,3 \
  | tr -d \" \
  | wget -qi -
```
3. Now, we configure authmod. Go under the `config` folder and download the cfg template file (you can also run the server once and the file will be generated):
```bash
# in the config/ folder
wget https://raw.githubusercontent.com/Chocorean/authmod/master/src/main/resources/authmod.cfg
```
4. Edit the `config/authmod.cfg` file depending on your needs.
5. Restart the server and everything should be ok!

## Issues

[Right here](https://github.com/Chocorean/authmod/issues).

## Website

Further informations and downloads links are available on the [Curse project page](https://minecraft.curseforge.com/projects/authmod).


## Resources

 - [bcrypt calculator](https://www.dailycred.com/article/bcrypt-calculator): useful to generate a hashed password using the bcrypt algorithm.


## Contact

- [Mail](mailto:baptiste.chocot@gmail.com)
- Discord: `Sunser#7808`

## Contributors

- Baptiste Chocot ([@Chocorean](https://www.github.com/Chocorean/))
- Yann Prono ([@Mcdostone](https://www.github.com/Mcdostone/))
