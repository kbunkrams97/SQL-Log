package com.gmail.kbunkrams97.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.gmail.kbunkrams97.SQLMain;

public class LoginListener implements Listener {

	public static SQLMain plugin = SQLMain.getInstance();
	
	@EventHandler
	public void onLoginEvent(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		
		plugin.updatePlayerLocation(player);
	}
}
