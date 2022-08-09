package me.metumortis.orbitaltask.commands

import me.metumortis.orbitaltask.commands.Bal.Companion.getBalanceOfThePlayer
import me.metumortis.orbitaltask.commands.SetBal.Companion.setBalanceOfThePlayer
import me.metumortis.orbitaltask.functions.translateColors
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Give(val plugin: JavaPlugin) : CommandExecutor {
    @Suppress("Deprecation")
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(args.size != 2){
            sender.sendMessage(plugin.config.getString("messages.give-usage")!!.let(::translateColors))
            return true
        }
        else if(!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore() && Bukkit.getPlayer(args[0]) == null){
            sender.sendMessage(plugin.config.getString("messages.player-not-found")!!.let(::translateColors))
            return true
        }
        else if(args[1].toIntOrNull() == null || args[1].toInt() < 0){
            sender.sendMessage(plugin.config.getString("messages.give-usage")!!.let(::translateColors))
            return true
        }
        else {
            val target = Bukkit.getOfflinePlayer(args[0])
            if(sender is ConsoleCommandSender){
                setBalanceOfThePlayer(target, args[1].toInt())
                sender.sendMessage(plugin.config.getString("messages.given")!!.replace("%player%", args[0]).replace("%balance%", args[1]).let(::translateColors))
            }
            else if(sender is Player){
                val senderBalance = getBalanceOfThePlayer(sender)
                val targetBalance = getBalanceOfThePlayer(target)
                if(senderBalance < args[1].toInt()){
                    sender.sendMessage(plugin.config.getString("messages.not-enough-money")!!.let(::translateColors))
                    return true
                }
                setBalanceOfThePlayer(target, targetBalance + args[1].toInt())
                setBalanceOfThePlayer(sender, senderBalance - args[1].toInt())
                sender.sendMessage(plugin.config.getString("messages.given")!!.replace("%player%", args[0]).replace("%balance%", args[1]).let(::translateColors))
            }
            return true
        }
    }
}