<div align="center">
<br>
<img
    alt="AuthMod"
    src="./src/main/resources/logo.png"
    width=200px
/>
<br/>
<h1>Authmod</h1>
<strong>Server side mod which allows to register and authenticate players your Minecraft server.</strong>
</div>
<br/>
<p align="center">
<a href="https://www.curseforge.com/minecraft/mc-mods/authmod" target="_blank">
    <img src="https://cf.way2muchnoise.eu/full_authmod_downloads(555-FF4C05-FFF-00000000-FFF).svg" alt="curseforge page"/>
</a>
<a href="https://img.shields.io/badge/forge%20version-1.15.2-blue.svg" target="_blank">
    <img src="https://img.shields.io/badge/forge%20version-1.16.5-blue.svg" alt="forge version"/>
</a>
<a href="https://img.shields.io/badge/java-1.8-blue.svg" target="_blank">
    <img src="https://img.shields.io/badge/java-1.8-blue.svg" alt="java version" />
</a>
<a href="https://github.com/Chocorean/authmod/actions" target="_blank">
    <img src="https://github.com/Chocorean/authmod/workflows/build/badge.svg?branch=master" alt="build status"/>
</a>
<a href="https://sonarcloud.io/dashboard?id=Chocorean_authmod-core" target="_blank">
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

AuthMod is a server-side Minecraft mod that adds commands to register and authenticate players joining your server. The mod is an alternative to the Mojang authentication. Authmod may be a good solution if you want to authorize demo Minecraft accounts on your server. **When a player joins the server, all his actions are disabled**. He has to authenticate via the `/login <password>` command to play.

**Authmod registers 4 commands to your server**. Each one can be turned [on/off](./src/main/resources/authmod-server.toml):
 - `/register <password> <confirmation>` to register the player.
 - `/login <password>` to log into the server.
 - `/logged` to know whether you're logged in.
 - `/changepassword <old> <new> <comfirmation>` to change your password once logged.


Player data are stored in either a [SQL database](./docker/init.sql) or a CSV file.

You may require players to provide an identifier at registration/login time (`/login <email> <password>`). By default this option is disabled and Minecraft usernames are used instead.



## Installation

1. Stop your Minecraft server.
1. Go to releases and select the jar depend
1. Add `authmod-<version>.jar` in the `mods/` directory:
```bash
curl -L "https://github.com/chocorean/authmod/releases/download/v1.16.5-1.0.0/authmod-1.16.5-1.0.0.jar" --output path/mods/authmod.jar
```
4. Now, we need to configure the mod. Run the server once to generate the configuration file at [`world/serverconfig/authmod-server.toml`](./src/main/resources/authmod-server.toml).
5. Edit [`authmod-server.toml`](./src/main/resources/authmod-server.toml) depending on your needs.
6. Restart the server and you're all set!


## For developers

Please refer to the [Forge documentation](https://mcforge.readthedocs.io/en/latest/gettingstarted/) for setting your development environment.
```bash
./gradlew build
# The jar file includes authmod, authmod-core, jbcrypt and the JDBC mariaDB driver
ls ./build/libs/authmod-*.jar
```

### Internationalization

Pull requests for adding i18n are more than welcomed. See [authmod-core](https://github.com/Chocorean/authmod-core), section [Internationalisation](https://github.com/Chocorean/authmod-core#internationalization) for more details.


### Releasing a new version
1. Apply the changes on the branch whose name corresponds to the forge version (*let's say you're on branch [`1.16.5`](https://github.com/Chocorean/authmod/tree/1.16.5)*).
1. We're going to create a git tag. Its name must follow the pattern `v<version-of-forge>-<version-of-authmod>`.
1. Create the git tag: `git tag -a v1.16.5-1.0.0 -m "Release a new version for forge 1.16.5"`
1. Push the changes `git push`
1. And then push the tag: `git push --tags`



## Contributors

- Baptiste Chocot ([@Chocorean](https://www.github.com/Chocorean/)) or `Sunser#7808` (Discord)
- Yann Prono ([@Mcdostone](https://www.github.com/Mcdostone/))
- [weffermiguel](https://www.curseforge.com/members/weffermiguel) for spanish i18n
