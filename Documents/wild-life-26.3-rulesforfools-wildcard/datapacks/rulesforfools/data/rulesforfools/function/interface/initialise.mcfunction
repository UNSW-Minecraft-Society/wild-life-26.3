# setup scoreboard, teams, and other important things that your datapacks needs to work
# do not run anything from here, the plugin will automatically do this

# for testing use your terminate function, then run this, then start

tellraw @a {"text": "Rules For Fools has initialised", "color": "gold"}

scoreboard objectives remove rulesforfools_eatcake
scoreboard objectives remove rulesforfools_eatcake
scoreboard objectives remove rulesforfools_sunexposure

scoreboard objectives remove rulesforfools_RNG
scoreboard objectives remove rulesforfools_graceperiod

scoreboard objectives add rulesforfools_eatcake minecraft.custom:minecraft.eat_cake_slice
scoreboard objectives add rulesforfools_eatcarrot minecraft.used:minecraft.carrot
scoreboard objectives add rulesforfools_sunexposure dummy

scoreboard objectives add rulesforfools_RNG dummy
scoreboard objectives add rulesforfools_graceperiod dummy


scoreboard players set RulesForFools example 0

