package k3qKillManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerDataManager {
	
	DbConnection conn;
	String tableName;
	Integer deathpoints;
	Integer killpoints;
	PlayerDataManager (DbConnection conn, String tablename, Integer deathpoints, Integer killpoints) {
		this.conn = conn;
		this.tableName = tablename;
		this.deathpoints = deathpoints;
		this.killpoints = killpoints;
	}
	
	public void KillInteraction(Player killer, Player victim) {
		//check for killer and victim
		//killer
		Connection dbconn = null;
		try {
			dbconn = conn.getConnection();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		if (dbconn == null) {
			Bukkit.getConsoleSender().sendMessage("MySQL Problem with Kill Interaction");
			return;
		}
		try {
			
			PreparedStatement ps = dbconn.prepareStatement("Select uuid FROM `" + this.tableName + "` WHERE uuid=?");
			ps.setString(1, killer.getUniqueId().toString());
			ResultSet res = ps.executeQuery();
			if (!res.next()) {
				PreparedStatement psInsert = dbconn.prepareStatement("INSERT INTO `" + this.tableName + "` (uuid) VALUES (?)");
				psInsert.setString(1, killer.getUniqueId().toString());
				psInsert.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//victim
		try {
			PreparedStatement ps = dbconn.prepareStatement("Select uuid FROM `" + this.tableName + "` WHERE uuid=?");
			ps.setString(1, victim.getUniqueId().toString());
			ResultSet res = ps.executeQuery();
			if (!res.next()) {
				PreparedStatement psInsert = dbconn.prepareStatement("INSERT INTO `" + this.tableName + "` (uuid) VALUES (?)");
				psInsert.setString(1, victim.getUniqueId().toString());
				psInsert.executeUpdate();
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		//end
		
		//add kills for killer / deaths for victim
		try {
			PreparedStatement ps = dbconn.prepareStatement("UPDATE `" + this.tableName + "` SET kills = kills + 1,points = points + ?  WHERE uuid=?");
			ps.setInt(1, this.killpoints);
			ps.setString(2, killer.getUniqueId().toString());
			ps.executeUpdate();
		
			ps = dbconn.prepareStatement("UPDATE `" + this.tableName + "` SET deaths = deaths + 1, points = points - ? WHERE uuid=?"); 
			ps.setInt(1, this.deathpoints);
			ps.setString(2, victim.getUniqueId().toString());
			ps.executeUpdate();
			ps.close();

		}  catch (SQLException e) {
			e.printStackTrace();
			
		}
		try {
			dbconn.close();

		}
		 catch (SQLException e) {
			e.printStackTrace();
				
		}


		
	}
	public Integer getPlayerPoints(String uuid) {
		Connection dbconn = null;
		try {
			dbconn = conn.getConnection();
			
			PreparedStatement ps =  dbconn.prepareStatement("Select points FROM `" + this.tableName + "` WHERE uuid=?");
			Bukkit.getConsoleSender().sendMessage("SELECT points FROM `" + this.tableName + "` WHERE uuid=?" + uuid);
			ps.setString(1, uuid);
			ps.executeQuery();
			ResultSet rs = ps.getResultSet();
			rs.next();
			Integer points = Integer.parseInt(rs.getString("points"));
			rs.close();
			ps.close();
			return points;
			
			
		}
		catch (SQLException e) {
			e.printStackTrace();
				
		}

		
		
		try {
			dbconn.close();

		}
		 catch (SQLException e) {
			e.printStackTrace();
				
		}
		return 0;
	}
}
