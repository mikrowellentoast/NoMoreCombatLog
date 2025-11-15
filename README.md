# NoMoreCombatLog
A simple and configurable Paper plugin that prevents players from combat logging.

---

##  Features

### Combat Tag System
- Players are placed in combat when attacking or being attacked.
- The combat tag lasts for a configurable duration.
- Actionbar messages show remaining tag time.

### Combat Logging Prevention
- If a tagged player logs out before the timer ends, they are automatically killed.

### Retaliation Mode (optional)
- Tag only applies if the victim retaliates within a time window.
- Optional feature allowing more flexible PvP rules.

### Portal Restrictions
- Players can be prevented from using portals while in combat.

---

## Installation

1. Download the latest release of **NoMoreCombatLog.jar**.
2. Place it inside your server’s `/plugins/` directory.
3. Start or restart your Paper server.

---

## Configuration

Everything is controlled through the `config.yml` file:

```yaml
enabled: true                    # Enable or disable the plugin
combat-tag-duration: 30          # Duration (seconds) of the combat tag
enable-in-creative: false        # Allow tagging in creative mode
retaliationattack: false         # Enable retaliation-based tagging
retaliation-window: 10           # Time window for retaliation mode
set-attacker-on-combat: true     # Tag attacker in retaliation mode
allow-portal-teleport: false     # Allow portals during combat
```

## Commands
| Command        | Permission               | Description                      |
|----------------|---------------------------|----------------------------------|
| `/nmcl`        | `nomorecombatlog.use`     | Shows the plugin version.        |
| `/nmcl reload` | `nomorecombatlog.reload`  | Reloads the plugin configuration. |

## Permissions
| Permission                | Description                      |
|---------------------------|----------------------------------|
| `nomorecombatlog.use`     | Allows use of the `/nmcl` command. |
| `nomorecombatlog.reload`  | Allows use of the `/nmcl reload` command. |
| `nomorecombatlog.bypass` | Bypass combat logging            |

---

More features soon

---
## TODO
- Add customizable messages.
- don't kill players in combat because of server restart
- more punishments for combat logging
- support for other server types (Spigot)
- keep track of combat loggers across server restarts