package k3qKillManager;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
    FileConfiguration config = getConfig();
    DbConnection dbconn;
    PlayerDataManager plrdatamanager;
    TopGui tpgrManager;
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("k3qKillManager: Initialized");
		
				
		
		config.addDefault("killmessage", "[%d]%s Zosta³ zabity przez [%d]%s");
		config.addDefault("lightning", true);
		config.addDefault("sql_host", "localhost");
		config.addDefault("sql_user", "root");
		config.addDefault("sql_password", "");
		config.addDefault("sql_db", "mcdb");
		config.addDefault("sql_tablename", "k3qkillmanager-rankingdata");
		config.addDefault("killpoints", 15);
		config.addDefault("deathpoints", 15);
		config.options().copyDefaults(true);
		//config intialize
		saveConfig();

		
		
		//db initialize
		dbconn = new DbConnection(config.getString("sql_host"), config.getString("sql_user"), config.getString("sql_password"), config.getString("sql_db"));
		try {
			if (dbconn.getConnection() != null) {
				Bukkit.getConsoleSender().sendMessage(this.getName() + ": Connected to mysql");
				plrdatamanager = new PlayerDataManager(dbconn, config.getString("sql_tablename"), config.getInt("killpoints"), config.getInt("deathpoints"));
			} else {
				Bukkit.getConsoleSender().sendMessage(this.getName() + ": Problem with connecting to mysql / import sql file!");
				this.getPluginLoader().disablePlugin(this);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.getPluginLoader().disablePlugin(this);

		}
		tpgrManager = new TopGui(dbconn, config.getString("sql_tablename"));
		
		Bukkit.getServer().getPluginManager().registerEvents(new MyListener(config, plrdatamanager, tpgrManager), this);
		Bukkit.getServer().getPluginManager().registerEvents(tpgrManager, this);
		this.getCommand("toprank").setExecutor(new CommandsListener(tpgrManager));


		
	}
	
}
