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
<a href="https://www.curseforge.com/minecraft/mc-mods/authmod" target="_blank">
    <img src="http://cf.way2muchnoise.eu/full_authmod_downloads(555-FF4C05-FFF-00000000-FFF).svg" alt="curseforge page"/>
</a>
<a href="https://img.shields.io/badge/forge%20version-1.15.2-blue.svg" target="_blank">
    <img src="https://img.shields.io/badge/forge%20version-1.15.2-blue.svg" alt="forge version"/>
</a>
<a href="https://img.shields.io/badge/java-1.8-blue.svg" target="_blank">
    <img src="https://img.shields.io/badge/java-1.8-blue.svg" alt="java version" />
</a>
<a href="https://github.com/Chocorean/authmod/actions" target="_blank">
    <img src="https://github.com/Chocorean/authmod/workflows/build/badge.svg?branch=master" alt="build status"/>
</a>
<a href="https://sonarcloud.io/dashboard?id=Chocorean_authmod" target="_blank">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=Chocorean_authmod&metric=alert_status" alt="build status on sonarcloud"/>
</a>
<a href="https://sonarcloud.io/dashboard?id=Chocorean_authmod" target="_blank">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=Chocorean_authmod&metric=bugs" alt="bugs"/>
</a>
<a href="https://sonarcloud.io/dashboard?id=Chocorean_authmod" target="_blank">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=Chocorean_authmod&metric=code_smells" alt="code smells"/>
</a>
<a href="https://sonarcloud.io/dashboard?id=Chocorean_authmod" target="_blank">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=Chocorean_authmod&metric=sqale_rating" alt="maintainability" />
</a>
<a href="https://sonarcloud.io/dashboard?id=Chocorean_authmod" target="_blank">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=Chocorean_authmod&metric=vulnerabilities" alt="vulnerabilities" />
</a>
<a href="https://lgtm.com/projects/g/Chocorean/authmod/alerts/" target="_blank">
    <img src="https://img.shields.io/lgtm/alerts/g/Chocorean/authmod.svg?logo=lgtm&logoWidth=18" alt="vulnerabilities" />
</a>
</p>


## Table of contents

- [Authmod](#authmod)
- [Installation](#installation)
- [Disabling in-game registration](#disabling-in-game-registration)
- [Getting started for developers](#getting-started-for-developers)
  - [Building the mod](#building-the-mod)
  - [SQL snippets](#sql-snippets)
- [Internationalization](#internationalization)
- [Contributors](#contributors)


# Authmod

AuthMod is a server side Minecraft mod allowing you to accept either premium and demo minecraft accounts safely. What is important to remind with this mod is **the mojang authentication cannot be used**. So if you rely on this, this mod is maybe not a good solution for you. Authmod proposes a set of interesting features:

- Enable or disable the registration on the server.
- Enable or disable the authentication on the server.
- Register a list of allowed users.
- Ban a registered player.
- Registration/authentication can require an identifier (email for instance).
- Exclude a player if he's not logged after a certain delay.

All the data related to the registration, the authentication... are stored in either a SQL database or a file.

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

## Disabling in-game registration

You might want to disallow players from registering themselves on your server. If you do that, you will have to find a way to fill your SQL database or registration file.

If you don't know it already, passwords are not stored in the database; what is stored is a fingerprint of the password, called an *hash*. This hash is computed with the [bcrypt algorithm](https://en.wikipedia.org/wiki/Bcrypt), and we will have to insert in the database the *hash of the passwords* instead of passwords themselves. You can use [BCrypt Calculator](https://www.dailycred.com/article/bcrypt-calculator) to compute your hashes.

## Getting started for developers

Please refer to the [Forge documentation](https://mcforge.readthedocs.io/en/latest/gettingstarted/) for setting your development environment.

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

## Internationalization

Pull requests for adding i18n are more than welcomed. Please make sure to:
- create ` src/main/resources/assets/authmod/lang/XX_YY.json`
- update `src/main/java/io/chocorean/authmod/config/AuthModConfig.java`
- update this `README.md` file with your pseudo (optional but strongly recommended!)

Thank you!

## Contributors

- Baptiste Chocot ([@Chocorean](https://www.github.com/Chocorean/)) or `Sunser#7808` (Discord)
- Yann Prono ([@Mcdostone](https://www.github.com/Mcdostone/))
- [weffermiguel](https://www.curseforge.com/members/weffermiguel) for spanish i18n
