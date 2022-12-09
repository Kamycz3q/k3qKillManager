package k3qKillManager;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TopGui implements Listener {
	private final Inventory inv;
	private DbConnection dbconn;
	private String tablename;
	
	public TopGui(DbConnection dbObj, String tablename) {
		inv = Bukkit.createInventory(null, 9, "Top 9");
		this.dbconn = dbObj;
		this.tablename = tablename;
		init();

		
	}
	
	private void init() {
		reloadInventory();
	}
	public void reloadInventory() {
		inv.clear();
		try {
			Connection conn = dbconn.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM `" + this.tablename + "` ORDER BY points DESC LIMIT 9");
			ps.executeQuery();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String plrName = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uuid"))).getName();
				ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		        ItemMeta meta = item.getItemMeta();
		        meta.setDisplayName(ChatColor.LIGHT_PURPLE + plrName);
		        List<String> dataList = new ArrayList<String>();
		        dataList.add(ChatColor.DARK_PURPLE + rs.getString("points"));
		        dataList.add(ChatColor.DARK_PURPLE + String.format("%s/%s", rs.getString("kills"), rs.getString("deaths")));
		        meta.setLore(dataList);
		        item.setItemMeta(meta);
		        inv.addItem(item);
				
			
			}
	        ps.close();

			conn.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	public void openInventoryForPlayer(HumanEntity ent) {
		ent.openInventory(inv);
	}
	
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		if (e.getInventory().equals(inv)) {
			e.setCancelled(true);
		}
	}
	
}
