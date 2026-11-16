execute as @a[tag=!rulesforfools_player] run scoreboard players add @s rulesforfools_graceperiod 1
execute as @a[tag=!rulesforfools_player] if score @s rulesforfools_graceperiod matches 2 run tellraw @s "You have been given a rule book."
execute as @a[tag=!rulesforfools_player] if score @s rulesforfools_graceperiod matches 2 run title @s title "Rules For Fools"
execute as @a[tag=!rulesforfools_player] if score @s rulesforfools_graceperiod matches 2 run function rulesforfools:extra/rulesets/give_rulebook


execute as @a[tag=!rulesforfools_player] if score @s rulesforfools_graceperiod matches 300.. run tellraw @s "Grace Period is Over!"
execute as @a[tag=!rulesforfools_player] if score @s rulesforfools_graceperiod matches 300.. run tag @s add rulesforfools_player

schedule function rulesforfools:extra/joining_graceperiod 1s