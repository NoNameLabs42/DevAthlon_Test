package de.NoNameLabs.Main;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TestListener implements Listener {	
	public static Inventory particle_inventory = Bukkit.createInventory(null, 9, ChatColor.RED + "Partikel auswählen");
	
	static {
		particle_inventory.setItem(0, new ItemStack(Material.FLINT_AND_STEEL, 1));
		particle_inventory.setItem(1, new ItemStack(Material.WATER_BUCKET, 1));
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		event.getPlayer().sendMessage(ChatColor.YELLOW + "OH NEIN!!!! Du hast " + event.getItemDrop().getItemStack().getAmount() + " " + event.getItemDrop().getItemStack().getType().name() + " gedroppt!!!");
		//Print a message on player dropping an item
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		event.getPlayer().spigot().playEffect(event.getPlayer().getLocation(), Effect.CLOUD, 0 /*?*/, 0 /*?*/, 0 /*x*/, 0 /*y*/, 0 /*z*/, 0.1f /* SPEED */, 50  /* PARTICLE COUNT*/, 30 /*SIZE*/); //Play Effect only to the one player
		event.getPlayer().getWorld().playEffect(event.getPlayer().getLocation(), Effect.FLAME, 0 /*DATA ?*/, 50/*SIZE*/); //Play Effect to the WORLD (ALL PLAYERS)!!!
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		TestPlugin.INSTANCE.getServer().getScheduler().runTaskLater(TestPlugin.INSTANCE, new Runnable() {
    		public void run() {
    			event.getEntity().spigot().respawn(); //Auto respawns the player after 1 second (There must be a delay so that items stay at death point)
    		}
    	}, 20L);

	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) { // On Interact
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK  && event.getPlayer().getItemInHand().getType() == Material.SKULL_ITEM && event.getPlayer().getItemInHand().getDurability() == (short)4) { // On Right Click Block and Item in hand is Creeper Skull
			event.setCancelled(true);
			event.getPlayer().getWorld().spawnEntity(event.getClickedBlock().getLocation().add(0.5, 1, 0.5), EntityType.CREEPER).setCustomName("Klaus"); //Spawn a Creeper
		}
		
		//On Rightclick with NetherStar open Particle Inventory
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null) {
			if (event.getItem().equals(getPartikelItem())) {
				event.setCancelled(true);
				event.getPlayer().openInventory(particle_inventory);
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		//Give the player a Nether Star named "Partikel"
		event.getPlayer().getInventory().setItem(8, getPartikelItem());
	}
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory() == null || event.getCurrentItem() == null) {
			return;
		}
		
		if (event.getInventory().getName().equals(particle_inventory.getName())) {
			event.setCancelled(true);
			event.getWhoClicked().closeInventory();
			
			ItemStack clicked_item = event.getCurrentItem();
			
			if (clicked_item.getType() == Material.FLINT_AND_STEEL) {
				synchronized (TestPlugin.INSTANCE.player_effects) {
					TestPlugin.INSTANCE.player_effects.put((Player) event.getWhoClicked(), Effect.FLAME);
				}
			} else if (clicked_item.getType() == Material.WATER_BUCKET) {
				synchronized (TestPlugin.INSTANCE.player_effects) {
					TestPlugin.INSTANCE.player_effects.put((Player) event.getWhoClicked(), Effect.NOTE);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if (TestPlugin.INSTANCE.player_effects.containsKey(event.getPlayer())) {
			TestPlugin.INSTANCE.player_effects.remove(event.getPlayer());
		}
	}
	
	public static ItemStack getPartikelItem() {
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "Öffnet das Partikel-Menu");
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + "Partikel");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
}
