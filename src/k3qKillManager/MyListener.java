package k3qKillManager;


import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.md_5.bungee.api.ChatColor;

public class MyListener implements Listener{
	
	FileConfiguration config;
	PlayerDataManager plrdatamanager;
	TopGui tpgrmanager;
	MyListener (FileConfiguration config, PlayerDataManager plrdatamanager, TopGui tpgrmanager) {
		this.config = config;
		this.plrdatamanager = plrdatamanager;
		this.tpgrmanager = tpgrmanager;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		try {
			if (event.getEntity().getPlayer() != null && event.getEntity().getKiller() != null && event.getEntity().getKiller().getPlayer() != null) {
				Player victim = event.getEntity().getPlayer();
				Player killer = event.getEntity().getKiller().getPlayer();
				if (config.getBoolean("lightning") == true) {
					victim.getWorld().strikeLightningEffect(victim.getLocation());
				}
				this.plrdatamanager.KillInteraction(killer, victim);
				String message = String.format(ChatColor.DARK_RED + config.getString("killmessage"), plrdatamanager.getPlayerPoints(victim.getUniqueId().toString()), victim.getDisplayName(), plrdatamanager.getPlayerPoints(killer.getUniqueId().toString()), killer.getDisplayName());
				Bukkit.broadcastMessage(message);
				tpgrmanager.reloadInventory();
			}
		}
		catch (NullPointerException x) {
			
		}
	}
}
