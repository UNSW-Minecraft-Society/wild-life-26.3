execute as @a[tag=rulesforfools_player] if predicate rulesforfools:exposed_to_sun run scoreboard players add @s rulesforfools_sunexposure 1
execute as @a[tag=rulesforfools_player] if score @s rulesforfools_sunexposure matches 900 run tellraw @s "You have been exposed to the sun for over 15 minutes now!"

schedule function rulesforfools:extra/loop_sunlight 1s