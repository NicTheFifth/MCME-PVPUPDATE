# PVP Server Manual

1. [Commands](#1-commands)
   1. [Permissions in the plugin](#1i-permissions-in-the-plugin)
      1. [Role collections](#1ia-role-collections)
      2. [No permissions](#1ib-no-permissions)
      3. [canRun](#1ic-canrun)
      4. [pvp.mapEditor](#1id-mapeditor)
      5. [adminPermission](#1ie-adminpermission) 
   2. [Based on roles on the server](#1ii-based-on-roles-on-the-server)
      1. [Every player](#1iia-every-player)
      2. [Guide](#1iib-guide)
      3. [Valar](#1iic-valar)
2. [Maps](#2-maps)

## 1. Commands

### 1.i Permissions in the plugin

#### 1.i.a Role Collections

There are two roles in the PVP server, PVP Manager and PVP Admin. PVP Manager has the permission canRun, whilst PVP
Admin has all permissions in the plugin.

#### 1.i.b No permissions

The commands that players without any permissions can do:
```mermaid
graph LR
    A[pvp] --> rules --> B([Gamemode name])
    A      --> autojoin
    A      --> join
    A      --> leave
```

#### 1.i.c canRun

The commands that players with this permission can do in addition to [No permissions](#1ib-no-permissions):
```mermaid
graph LR
    A[pvp] --> B(create)
    B --> C1([Existing map]) --> D1([Existing gamemode]) --> E1([Timelimit in seconds]) --> F1([Scoregoal])
    D1                      --> F2([Scoregoal])
    A --> start
    A --> setgoal --> F3([Scoregoal])
    A --> settimelimiit --> E2([Timelimit in seconds])
    A --> stop
```
#### 1.i.d mapEditor

The commands that players with this permission can do in addition to [No permissions](#1ib-no-permissions):
```mermaid
graph LR
    A[mapedit] --> create --> B1([Nonexisting map name])
    A --> rename --> B2([Nonexisting map name])
    A --> select --> C([Existing map name])
    A --> setarea
    A --> setrp --> D([RP name])
    A --> setmapspawn
    A --> gamemode --> E1([Gamemode name])
    A --> setmax --> F([Max amount of players])
    A --> setkillheight
    A --> G([spawn]) --> AA[add]
          G          --> AB[delete]   --> H1([Index])
          G          --> AC[teleport] --> H2([Index])
          G          --> I([Spawn name]) --> AD[set]
                         I               --> ABA[delete]
                         I               --> AAA[add]      --> H3([Index])
                         I               --> ACA[teleport] --> H4([Index])
    A --> J([Special point name]) --> set
          J                       --> add      --> H5([Index])
          J                       --> remove   --> H6([Index])
          J                       --> teleport --> H7([Index])
    A --> info --> E2[Gamemode]
    A --> showpoints
    A --> hidepoints
```
#### 1.i.e adminPermission

The commands that players with this permission can include all of the above and are extended by:
```mermaid
graph LR
mapedit --> delete --> A([Existing map name]) --> B([Gamemode name])
```
### 1.ii Based on roles on the server
#### 1.ii.a Every player
The commands that every player can use are detailed in [No permissions](#1ib-no-permissions).
#### 1.ii.b Guide
The commands that guides can use are [No permissions](#1ib-no-permissions) and [canRun](#1ic-canrun)
#### 1.ii.c Valar
Valar can use all commands within the pvp plugin.
## 2. Maps