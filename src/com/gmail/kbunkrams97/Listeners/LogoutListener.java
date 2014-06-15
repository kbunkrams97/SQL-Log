package com.gmail.kbunkrams97.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.kbunkrams97.SQLMain;

public class LogoutListener implements Listener {

	public static SQLMain plugin = SQLMain.getInstance();
	
	@EventHandler
	public void onLogoutEvent(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		plugin.updatePlayerLocation(player);
	}
}
