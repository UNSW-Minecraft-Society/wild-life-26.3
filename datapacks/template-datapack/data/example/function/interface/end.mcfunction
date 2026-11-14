# ends game, run after this event has finished
# remove side effects on players and other event specific things, otherwise feel free to leave this empty

tellraw @a {"text": "Example wildcard has ended", "color": "gold"}

scoreboard players set started example 0

