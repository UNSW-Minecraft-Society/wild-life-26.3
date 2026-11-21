# starts wildcard, runs after initialise
# be sure to switch your flag for whether game has started

tellraw @a {"text": "Example wildcard has started", "color": "gold"}

scoreboard players set started RulesForFools 1

function rulesforfools:extra/joining_graceperiod

tag @a add rulesforfools_player

execute store result score Rules rulesforfools_RNG run random roll 1..3

execute if score Rules rulesforfools_RNG matches 1 run function rulesforfools:extra/rulesets/1
execute if score Rules rulesforfools_RNG matches 2 run function rulesforfools:extra/rulesets/2
execute if score Rules rulesforfools_RNG matches 3 run function rulesforfools:extra/rulesets/3