#giving a rulebook is its own function as it is also triggered when players late-enter a game
execute as @a run function rulesforfools:extra/rulesets/give_rulebook

schedule function rulesforfools:extra/schedule_cakeeater 300s
schedule function rulesforfools:extra/loop_sneakingrule 300s
schedule function rulesforfools:extra/loop_sunlight 300s
schedule function rulesforfools:extra/trigger_sunlight 3900s