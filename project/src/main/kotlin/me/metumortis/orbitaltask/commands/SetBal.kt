package me.metumortis.orbitaltask.commands

import me.metumortis.orbitaltask.Main
import me.metumortis.orbitaltask.functions.translateColors
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class SetBal(private val plugin: JavaPlugin) : CommandExecutor {
    @Suppress("Deprecation")
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!sender.isOp) {
            sender.sendMessage(plugin.config.getString("messages.no-permission")!!.let(::translateColors))
            return true
        }
        else if(args.size != 2) {
            sender.sendMessage(plugin.config.getString("messages.setbal-usage")!!.let(::translateColors))
            return true
        }
        else if(args[1].toIntOrNull() == null || args[1].toInt() < 0) {
            sender.sendMessage(plugin.config.getString("messages.setbal-usage")!!.let(::translateColors))
            return true
        }
        else if(!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore() && Bukkit.getPlayer(args[0]) == null){
            sender.sendMessage(plugin.config.getString("messages.player-not-found")!!.let(::translateColors))
            return true
        }
        else {
            val player = Bukkit.getOfflinePlayer(args[0])
            setBalanceOfThePlayer(player, args[1].toInt())
            sender.sendMessage(plugin.config.getString("messages.setbal")!!.replace("%player%", args[0]).replace("%balance%", args[1]).let(::translateColors))
            return true
        }
    }
    companion object{
        fun setBalanceOfThePlayer(player: OfflinePlayer, amount: Int){
            val statement = Main.connection.createStatement()
            val rs = statement.executeQuery("SELECT * from balances WHERE uuid = '${player.uniqueId}'")
            if(!rs.next()) statement.executeUpdate("INSERT INTO balances (uuid, balance) VALUES('${player.uniqueId}',$amount)")
            else statement.executeUpdate("UPDATE balances SET balance = $amount WHERE uuid = '${player.uniqueId}'")
            statement.close()
        }
    }
}