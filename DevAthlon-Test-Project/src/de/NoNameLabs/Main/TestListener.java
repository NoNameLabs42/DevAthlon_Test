package de.NoNameLabs.Main;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class TestListener implements Listener {

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		event.getPlayer().sendMessage(ChatColor.YELLOW + "OH NEIN!!!! Du hast " + event.getItemDrop().getItemStack().getAmount() + " " + event.getItemDrop().getItemStack().getType().name() + " gedroppt!!!");
	}
	
}
