# ends game, run after this event has finished
# remove side effects on players and other event specific things, otherwise feel free to leave this empty

tellraw @a {"text": "Example wildcard has ended", "color": "gold"}

scoreboard players set started RulesForFools 0

tag @a remove rulesforfools_player

schedule clear rulesforfools:extra/joining_graceperiod
schedule clear rulesforfools:extra/loop_sneakingrule
schedule clear rulesforfools:extra/loop_sunlight
schedule clear rulesforfools:extra/schedule_cakeeater
schedule clear rulesforfools:extra/trigger_sunlight