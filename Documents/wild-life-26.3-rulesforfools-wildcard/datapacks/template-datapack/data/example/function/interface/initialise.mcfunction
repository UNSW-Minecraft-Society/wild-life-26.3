# setup scoreboard, teams, and other important things that your datapacks needs to work
# do not run anything from here, the plugin will automatically do this

# for testing use your terminate function, then run this, then start

tellraw @a {"text": "Example wildcard has initialised", "color": "gold"}

scoreboard objectives add example dummy
scoreboard players set started example 0

