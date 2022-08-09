package me.metumortis.orbitaltask.commands

import me.metumortis.orbitaltask.Main.Companion.connection
import me.metumortis.orbitaltask.functions.translateColors
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Bal(private val plugin: JavaPlugin) : CommandExecutor {
    @Suppress("Deprecation")
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(args.isEmpty()){
            if(sender !is Player){
                sender.sendMessage(plugin.config.getString("messages.players-only")!!.let(::translateColors))
                return true
            }
            else {
                sender.sendMessage(plugin.config.getString("messages.balance")!!.let(::translateColors).replace("%balance%", getBalanceOfThePlayer(sender).toString()))
                return true
            }
        }
        else if(Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore() || Bukkit.getPlayer(args[0]) != null){
            sender.sendMessage(plugin.config.getString("messages.balance-others")!!.let(::translateColors).replace("%balance%", getBalanceOfThePlayer(Bukkit.getOfflinePlayer(args[0])).toString()))
            return true
        }
        else {
            sender.sendMessage(plugin.config.getString("messages.player-not-found")!!.let(::translateColors))
            return true
        }
    }
    companion object{
        fun getBalanceOfThePlayer(player: OfflinePlayer): Int{
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM balances WHERE uuid = '${player.uniqueId}'")
            statement.close()
            return if (resultSet.next()) resultSet.getInt("balance") else 0
        }
    }
}