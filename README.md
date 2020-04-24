<div align="center">
<br>
<img
    alt="AuthMod"
    src="./src/main/resources/logo.png"
    width=200px
/>
<br/>
<h1>Authmod</h1>
<strong>Server side mod which allows to safely accept demo versions on your minecraft server.</strong>
</div>
<br/>
<p align="center">
<a href="https://www.curseforge.com/minecraft/mc-mods/authmod">
    <img src="https://img.shields.io/badge/curseforge-authmod-blueviolet" alt="curseforge page"/>
</a>
<a href="https://img.shields.io/badge/forge%20version-1.15.1-blue.svg">
    <img src="https://img.shields.io/badge/forge%20version-1.15.1-blue.svg" alt="forge version"/>
</a>
<a href="https://img.shields.io/badge/java-1.8-blue.svg">
    <img src="https://img.shields.io/badge/java-1.8-blue.svg" alt="java version" />
</a>
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
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=code_smells" alt="code smells"/>
</a>
<a href="https://sonarcloud.io/dashboard?id=authmod">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=coverage" alt="code coverage" />
</a>
<a href="https://sonarcloud.io/dashboard?id=authmod">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=duplicated_lines_density" alt="duplicated lines" />
</a>
<a href="https://sonarcloud.io/dashboard?id=authmod">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=sqale_rating" alt="maintainability" />
</a>
<a href="https://sonarcloud.io/dashboard?id=authmod">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=authmod&metric=vulnerabilities" alt="vulnerabilities" />
</a>
</p>


## Table of contents

- [Authmod](#authmod)
- [Installation](#installation)
- [Limits](#limits)
- [Getting started for developers](#getting-started-for-developers)
  - [Building the mod](#building-the-mod)
  - [SQL snippets](#sql-snippets)
- [Issues](#issues)
- [Contact](#contact)
- [Contributors](#contributors)


# Authmod

AuthMod is a server side Minecraft mod allowing you to accept premium or demo minecraft accounts safely. What is important to remind with this mod is **the mojang authentication cannot be used**. So if you rely on this, this mod is maybe not a good solution for you. Authmod proposes a set of interesting features:

- Enable or disable the registration on the server.
- Enable or disable the authentication on the server.
- Register a list of allowed users.
- Ban a registered player.
- Registration/authentication can require an identifier (email for instance).
- Exclude a player if he's not logged after a certain delay.

All the data related to the registration, the authentication... are stored either in a SQL database or a file.

| Features       | File strategy | Database strategy |
| -------------- | :-----------: | :---------------: |
| Registration   |     **✔**     |       **✔**       |
| Authentication |     **✔**     |       **✔**       |

**Authmod** adds a set of commands on the minecraft server:
```bash
# Allow the user to authenticate on the server
/login email@example.com password

# Allow the user to register on the server
/register email@example.com password password

# Tell to the user whether authenticated
/logged
# Change password
/changepassword old_password new_password new_password
```


## Installation

1. Stop your server.
2. Add `authmod-X.X.jar` in the `mods/` directory:

```bash
# This command downloads the latest version of authmod.
cd mods/
curl -s https://api.github.com/repos/chocorean/authmod/releases/latest \
| grep "browser_download_url.*jar" \
| cut -d : -f 2,3 \
| tr -d \" \
| wget -qi -
```
3. Now, we need to configure authmod. Run the server once to generate config file `world/serverconfig/authmod-server.toml`. This file looks like [this](./src/main/resources/authmod-server.toml).
4. Edit the `authmod-server.toml` file depending on your needs.
5. Restart the server and you're all set!


## Limits
It is important to understand that **authmod is not perfect**. During the development, we detected hacks that can cause hassle for players. Here the list of these problems:
 - If two players connect with the same username, no matter if one of them is authenticated or not, the first that came on the server will be automatically disconnected. We don't know yet if this issue can be solved.


## Getting started for developers

### Building the mod

```bash
./gradlew build
ls ./build/libs/authmod-X.jar
```


### SQL snippets

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


## Issues

[Right here](https://github.com/Chocorean/authmod/issues).


## Contact

- [Mail](mailto:baptiste.chocot@gmail.com)
- Discord: `Sunser#7808`


## Contributors

- Baptiste Chocot ([@Chocorean](https://www.github.com/Chocorean/))
- Yann Prono ([@Mcdostone](https://www.github.com/Mcdostone/))
