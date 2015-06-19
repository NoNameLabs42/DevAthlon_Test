package de.NoNameLabs.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {
	
	Logger log = this.getLogger();
	
	List<Player> player_music = new ArrayList<Player>();
	
	
	
    public void onEnable(){ 
        log.info("Plugin gestartet");
        
        getServer().getPluginManager().registerEvents(new TestListener(), this);
        
        music();
    }
     
    public void onDisable(){ 
    	log.info("Plugin gestoppt");
    }
    
    public boolean onCommand(CommandSender sender, Command command, String commandlabel, String[] args) {
    	Player p = null;
    	if (sender instanceof Player) p = (Player) sender;
    	
    	if (commandlabel.equalsIgnoreCase("startmusic")) {
    		
    		if (p != null) {
    			if (player_music.contains(p)) sender.sendMessage(ChatColor.RED + "Du hast bereits die Musik aktiviert!");
    			else {
    				synchronized (player_music) {
    					player_music.add(p);	
					}
    				sender.sendMessage(ChatColor.GREEN + "Die Musik wurde aktiviert!");
    			}
    		} else sender.sendMessage(ChatColor.RED + "Error: Du bist kein Spieler!");
    		
    		return true;
    		
    	} else if (commandlabel.equalsIgnoreCase("stopmusic")) {
    		
    		if (p != null) {
    			if (!player_music.contains(p)) sender.sendMessage(ChatColor.RED + "Du hast bereits die Musik deaktiviert!");
    			else {
    				synchronized (player_music) {
    					player_music.remove(p);
    				}
    				sender.sendMessage(ChatColor.GREEN + "Die Musik wurde deaktiviert!");
    			}
    		} else sender.sendMessage(ChatColor.RED + "Error: Du bist kein Spieler!");
    		
    		return true;
    		
    	}
    	return false;
    }
    
    public void music() {
    	this.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
    		int counter = 0;
    		public void run() {
    			counter = (counter + 1)%10;
    			synchronized(player_music) {
    				for (Player p: player_music) {
    					p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 0);
    				}
    			}
    		}
    	}, 0L, 20L);
    }
}
