title @a times 0s 1s 0s
scoreboard objectives add wildcardtitles dummy "Titles"
scoreboard players add WildCard wildcardtitles 1

execute if score WildCard wildcardtitles matches 1 run title @a title "W"
execute if score WildCard wildcardtitles matches 2 run title @a title "Wi"
execute if score WildCard wildcardtitles matches 3 run title @a title "Wil"
execute if score WildCard wildcardtitles matches 4 run title @a title "Wild"
execute if score WildCard wildcardtitles matches 5 run title @a title "Wildc"
execute if score WildCard wildcardtitles matches 6 run title @a title "Wildca"
execute if score WildCard wildcardtitles matches 7 run title @a title "Wildcar"
execute if score WildCard wildcardtitles matches 8 run title @a title "Wildcard"
execute if score WildCard wildcardtitles matches 9 run title @a title "Wildcard A"
execute if score WildCard wildcardtitles matches 10 run title @a title "Wildcard Ac"
execute if score WildCard wildcardtitles matches 11 run title @a title "Wildcard Act"
execute if score WildCard wildcardtitles matches 12 run title @a title "Wildcard Acti"
execute if score WildCard wildcardtitles matches 13 run title @a title "Wildcard Activ"
execute if score WildCard wildcardtitles matches 14 run title @a title "Wildcard Activa"
execute if score WildCard wildcardtitles matches 15 run title @a title "Wildcard Activat"
execute if score WildCard wildcardtitles matches 16 run title @a title "Wildcard Activate"
execute if score WildCard wildcardtitles matches 17 run title @a title "Wildcard Activated"
execute if score WildCard wildcardtitles matches 18 run title @a title "Wildcard Activated!"

schedule function wildlife:general/showtitle 5t
execute if score WildCard wildcardtitles matches 18 run schedule clear wildlife:general/showtitle 
execute if score WildCard wildcardtitles matches 18 run scoreboard players set WildCard wildcardtitles 0
