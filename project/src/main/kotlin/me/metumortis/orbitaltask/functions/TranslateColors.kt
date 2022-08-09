package me.metumortis.orbitaltask.functions

import org.bukkit.ChatColor

fun translateColors(str: String): String {
    if("&#[0-9a-f]{6}".toRegex().containsMatchIn(str)){
        var parsedStr = str
        for (x in "&(#[0-9A-f]{6})".toRegex().findAll(str)){
            parsedStr = parsedStr.replaceFirst(x.value.toRegex(), net.md_5.bungee.api.ChatColor.of(x.value.slice(
                1 until x.value.length
            )).toString())
        }
        return ChatColor.translateAlternateColorCodes('&', parsedStr)
    }
    return ChatColor.translateAlternateColorCodes('&', str)
}