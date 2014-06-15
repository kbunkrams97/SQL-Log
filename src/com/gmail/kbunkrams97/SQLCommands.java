package com.gmail.kbunkrams97;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SQLCommands implements CommandExecutor{

	public static SQLMain plugin = SQLMain.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("sqllog"))
		{
			if(args.length == 0)
			{
				if(sender.isOp() || ((Player) sender).hasPermission("sqllog.*") || ((Player) sender).hasPermission("sqllog.update") || ((Player) sender).hasPermission("sqllog.purge") || ((Player) sender).hasPermission("sqllog.reload"))
				{
					sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log Menu" + ChatColor.WHITE + "]");
					sender.sendMessage(ChatColor.RED + "/sqllog help:" + ChatColor.GRAY + "This brings up the help menu.");
					sender.sendMessage(ChatColor.RED + "/sqllog update:" + ChatColor.GRAY + "Updates all players positions to the database.");
					sender.sendMessage(ChatColor.RED + "/sqllog purge:" + ChatColor.GRAY + "This purges the database for items older than three days.");
					sender.sendMessage(ChatColor.RED + "/sqllog reload:" + ChatColor.GRAY + "This reloads the plugin.");
				}
				else
				{
					sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log" + ChatColor.WHITE + "] " + ChatColor.RED + "You do not have permission to this command.");
				}
			}
			else if(args.length == 1)
			{
				if(args[0].equalsIgnoreCase("help"))
				{
					if(sender.isOp() || ((Player) sender).hasPermission("sqllog.*") || ((Player) sender).hasPermission("sqllog.update") || ((Player) sender).hasPermission("sqllog.purge") || ((Player) sender).hasPermission("sqllog.reload"))
					{
						sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log Menu" + ChatColor.WHITE + "]");
						sender.sendMessage(ChatColor.RED + "/sqllog help:" + ChatColor.GRAY + "This brings up the help menu.");
						sender.sendMessage(ChatColor.RED + "/sqllog update:" + ChatColor.GRAY + "Updates all players positions to the database.");
						sender.sendMessage(ChatColor.RED + "/sqllog purge:" + ChatColor.GRAY + "This purges the database for items older than three days.");
						sender.sendMessage(ChatColor.RED + "/sqllog reload:" + ChatColor.GRAY + "This reloads the plugin.");
					}
					else
					{
						sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log" + ChatColor.WHITE + "] " + ChatColor.RED + "You do not have permission to this command.");
					}
				}
				else if(args[0].equalsIgnoreCase("update"))
				{
					if(sender.isOp() || ((Player) sender).hasPermission("sqllog.*") || ((Player) sender).hasPermission("sqllog.update"))
					{
						int counter = 0;
						for(Player player : Bukkit.getOnlinePlayers())
						{
							plugin.updatePlayerLocation(player);
							counter++;
						}
						sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log" + ChatColor.WHITE + "] " + ChatColor.RED + "A total of " + counter + " player(s) have been updated.");
					}
					else
					{
						sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log" + ChatColor.WHITE + "] " + ChatColor.RED + "You do not have permission to this command.");
					}
				}
				else if(args[0].equalsIgnoreCase("purge"))
				{
					if(sender.isOp() || ((Player) sender).hasPermission("sqllog.*") || ((Player) sender).hasPermission("sqllog.purge"))
					{
						plugin.purgeDatabase();
						sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log" + ChatColor.WHITE + "] " + ChatColor.RED + "The database has been purged.");
					}
					else
					{
						sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log" + ChatColor.WHITE + "] " + ChatColor.RED + "You do not have permission to this command.");
					}
				}
				else if(args[0].equalsIgnoreCase("reload"))
				{
					if(sender.isOp() || ((Player) sender).hasPermission("sqllog.*") || ((Player) sender).hasPermission("sqllog.reload"))
					{
						plugin.reloadConfig();
						plugin.startRunnable();
						plugin.firstLoad = true;
						sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log" + ChatColor.WHITE + "] " + ChatColor.RED + "The config has been reloaded.");
					}
					else
					{
						sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log" + ChatColor.WHITE + "] " + ChatColor.RED + "You do not have permission to this command.");
					}
					
				}
				else
				{
					sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log" + ChatColor.WHITE + "] " + ChatColor.RED + "This is an unknown command.");
				}
			}
			else
			{
				sender.sendMessage("[" + ChatColor.GOLD + "SQL-Log" + ChatColor.WHITE + "]" + ChatColor.RED + "This is an unknown command.");
			}
		}
		return false;
	}

}
