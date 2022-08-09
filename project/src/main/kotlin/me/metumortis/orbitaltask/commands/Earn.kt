package me.metumortis.orbitaltask.commands

import me.metumortis.orbitaltask.commands.Bal.Companion.getBalanceOfThePlayer
import me.metumortis.orbitaltask.commands.SetBal.Companion.setBalanceOfThePlayer
import me.metumortis.orbitaltask.functions.translateColors
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class Earn(private val plugin: JavaPlugin) : CommandExecutor {
    companion object{
        private var lastUseMap: HashMap<Player, Long> = HashMap()
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) {
            sender.sendMessage(plugin.config.getString("messages.players-only")!!.let(::translateColors))
            return true
        }
        else if(!lastUseMap.containsKey(sender) || (lastUseMap[sender]!! + plugin.config.getInt("earning-cooldown",0) *60*1000) < System.currentTimeMillis()) {
            lastUseMap[sender] = System.currentTimeMillis()
            val randomEarning = Random.nextInt(plugin.config.getInt("earning-range.min"), plugin.config.getInt("earning-range.max")+1)
            val currentBalance = getBalanceOfThePlayer(sender)
            setBalanceOfThePlayer(sender, currentBalance + randomEarning)
            sender.sendMessage(plugin.config.getString("messages.earned")!!.replace("%amount%", randomEarning.toString()).let(::translateColors))
            return true
        } else {
            val remainingSeconds = (((lastUseMap[sender]!! + plugin.config.getInt("earning-cooldown",0)*60*1000) - System.currentTimeMillis())/1000)
            sender.sendMessage(plugin.config.getString("messages.earning-cooldown")!!.replace("%seconds%", remainingSeconds.toString()).let(::translateColors))
            return true
        }
    }
}