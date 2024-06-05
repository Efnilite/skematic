```yaml
command /pos1:
    permission: skematic.pos1
    executable by: players
    trigger:
        set {pos1::%player's uuid%} to player's location

command /pos2:
    permission: skematic.pos2
    executable by: players
    trigger:
        set {pos2::%player's uuid%} to player's location

command /save <string>:
    permission: skematic.save
    executable by: players
    trigger:
        save {pos1::%player's uuid%} and {pos2::%player's uuid%} to schematic arg-1

command /paste <string>:
    permission: skematic.paste
    executable by: players
    trigger:
        paste schematic arg-1 at player's location ignoring air

on schematic paste of "test":
    set {_x} to event-schematic

    broadcast "sick! %schematic% was pasted!"

command /data <string>:
    permission: skematic.data
    executable by: players
    trigger:
        send schematic minecraft version of arg-1 to player
        send schematic data version of arg-1 to player
        send schematic dimensions of arg-1 to player
```