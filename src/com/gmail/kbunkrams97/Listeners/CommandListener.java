package com.gmail.kbunkrams97.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.gmail.kbunkrams97.SQLMain;

public class CommandListener implements Listener {

	public static SQLMain plugin = SQLMain.getInstance();
	
	@EventHandler
	public void onCommandEvent(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
			
		plugin.updatePlayerCommand(player, event.getMessage());
	}
}
