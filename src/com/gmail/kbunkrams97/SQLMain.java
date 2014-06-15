package com.gmail.kbunkrams97;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import com.gmail.kbunkrams97.DatabaseHandler.SQLSelection;
import com.gmail.kbunkrams97.Listeners.CommandListener;
import com.gmail.kbunkrams97.Listeners.LoginListener;
import com.gmail.kbunkrams97.Listeners.LogoutListener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class SQLMain extends JavaPlugin
{
	private static SQLMain instance;
	
	public Logger log = Logger.getLogger("Minecraft");
	
	public boolean firstLoad = true;
	
	public SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
	
	public BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	
	private volatile int counter = getConfig().getInt("Current-Timer-Progress");
	
	public void onEnable()
	{
		instance = this;
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		try {
			checkDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		firstLoad = false;
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CommandListener(), this);
		pm.registerEvents(new LoginListener(), this);
		pm.registerEvents(new LogoutListener(), this);
		
		getCommand("sqllog").setExecutor(new SQLCommands());
		
		startRunnable();
	}
	
	public void onDisable()
	{
		getConfig().set("Current-Timer-Progress", counter);
		saveConfig();
	}
	
	public void updatePlayerLocation(Player player)
	{
		Date date = new Date();
		Location loc = player.getLocation();
		
		try {
			checkDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		long unix = date.getTime();
		
		try {
			SQLSelection.getStatement().execute("INSERT INTO location(date, player, x, y, z, world) VALUES(" + unix + ", '" + player.getName() + "', " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", '" + loc.getWorld().getName() + "');");
			SQLSelection.getConnection().close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePlayerCommand(Player player, String command)
	{
		Date date = new Date();
		Location loc = player.getLocation();
		
		try {
			checkDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		long unix = date.getTime();
		
		try {
			SQLSelection.getStatement().execute("INSERT INTO command(date, player, x, y, z, world, command) VALUES(" + unix + ", '" + player.getName() + "', " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", '" + loc.getWorld().getName() + "', '" +  command + "');");
			SQLSelection.getConnection().close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void purgeDatabase()
	{
		Calendar calendar = Calendar.getInstance();
		String time = getConfig().getString("Remove-Data-Older-Than");
		
		try {
			checkDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		if(time.contains("d"))
		{
			String[] days = time.split("d");
			if(days[0] != "")
			{
				calendar.add(Calendar.DATE, -Integer.parseInt(days[0]));
			}
		}
		
		if(time.contains("h"))
		{
			String[] hours = time.split("h");
			if(hours[0].contains("d"))
			{
				hours = hours[0].split("d");
				if(hours[1] != "")
				{
					calendar.add(Calendar.HOUR_OF_DAY, -Integer.parseInt(hours[1]));
				}
			}
			else
			{
				calendar.add(Calendar.HOUR_OF_DAY, -Integer.parseInt(hours[0]));
			}
		}
		
		if(time.contains("m"))
		{
			String[] minutes = time.split("m");
			if(minutes[0].contains("d"))
			{
				minutes = minutes[0].split("d");
				if(minutes[1].contains("h"))
				{
					minutes = minutes[1].split("h");
					if(minutes[1] != "")
					{
						calendar.add(Calendar.MINUTE, -Integer.parseInt(minutes[1]));
					}
				}
				else
				{
					calendar.add(Calendar.MINUTE, -Integer.parseInt(minutes[1]));
				}
			}
			else
			{
				if(minutes[0].contains("h"))
				{
					minutes = minutes[0].split("h");
					if(minutes[1] != "")
					{
						calendar.add(Calendar.MINUTE, -Integer.parseInt(minutes[1]));
					}
				}
				else
				{
					calendar.add(Calendar.MINUTE, -Integer.parseInt(minutes[0]));
				}
			}
		}
		
		long unix = calendar.getTime().getTime();
		
		try {
			SQLSelection.getStatement().execute("DELETE FROM location WHERE date < " + unix + ";");
			SQLSelection.getStatement().execute("DELETE FROM command WHERE date < " + unix + ";");
			SQLSelection.getConnection().close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		log.info("[SQL-Log] Data older than " + getConfig().getString("Remove-Data-Older-Than") + " has been removed.");
	}
	
	public void checkDatabase() throws SQLException, ClassNotFoundException 
	{
        Connection con = null;
        con = SQLSelection.getConnection();
        con.createStatement().execute("CREATE TABLE IF NOT EXISTS location(date BIGINT, player VARCHAR(255), x INTEGER, y INTEGER, z INTEGER, world VARCHAR(255));");
        con.createStatement().execute("CREATE TABLE IF NOT EXISTS command(date BIGINT, player VARCHAR(255), x INTEGER, y INTEGER, z INTEGER, world VARCHAR(255), command VARCHAR(255));");
        con.close();
    }
	
	public void startRunnable()
	{
		scheduler.cancelAllTasks();
		
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run()
			{
				for(Player player : Bukkit.getOnlinePlayers())
				{
					updatePlayerLocation(player);
				}
			}
		}, 0L, getConfig().getInt("Update-Locations-Every")*20);
		
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() 
			{
				counter++;
				if(counter/getConfig().getInt("Remove-Old-Data-Ever") >= 1)
				{
					purgeDatabase();
					counter = 0;
				}
			}
		}, 0L, 1200;
	}
	
	public static SQLMain getInstance()
	{
		return instance;
	}
}
