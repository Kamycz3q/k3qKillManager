package k3qKillManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class CommandsListener implements CommandExecutor  {
	TopGui tpgmanager;
	CommandsListener(TopGui tpgmanager) {
		this.tpgmanager = tpgmanager;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
        	Player plr = (Player) sender;
        	tpgmanager.openInventoryForPlayer((HumanEntity)Bukkit.getEntity(plr.getUniqueId()));
        	return true;
        } else {
        	return false;
        }
       }
	
}
