# NoChatLag

[Forge Version](https://github.com/NoahvdAa/NoChatLag) | [Plugin Version](https://github.com/NoahvdAa/NoChatLagServer)

Downloads: [Modrinth](https://modrinth.com/mod/nochatlag) | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/nochatlag/) | [GitHub Releases](https://github.com/NoahvdAa/NoChatLag/releases/tag/1.0.0)

A couple of days ago, something changed in Mojang's blocked users API, causing the client to not properly load the list of blocked users on start. Because of this, the client will attempt to fetch the blocklist every two minutes when the chat is being rendered. This loading is done on the same thread as the rendering, causing the game to freeze until Mojang's API returns a response. Since the requests takes a while to complete, this will cause a noticeable lag spike.

This issue is being tracked in Mojira issue [WEB-5587](https://bugs.mojang.com/browse/WEB-5587).

You are currently viewing the 1.16.5 branch. Check out the *master* branch for the 1.17/1.18 version.