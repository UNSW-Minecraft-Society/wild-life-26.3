execute as @a[tag=rulesforfools_player] if predicate rulesforfools:sneaking run damage @s[tag=rulesforfools_player] 1000 minecraft:bad_respawn_point

schedule function rulesforfools:extra/loop_sneakingrule 5t