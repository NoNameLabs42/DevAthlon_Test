package de.NoNameLabs.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class TestPlugin extends JavaPlugin {
	
	public static TestPlugin INSTANCE;
	
	Logger log = this.getLogger();
	
	List<Player> player_music = new ArrayList<Player>();
	Map<Player, Effect> player_effects = new HashMap<Player, Effect>();
	
	Note[][] music = {
		{Note.D},		{Note.F},		{Note.D2},		{}, 	{},		{},		{Note.D},		{Note.F},
		{Note.D2},		{},		{},		{},		{Note.E2},		{},		{},		{Note.F2},
		{Note.E2},		{Note.F2},		{Note.E2},		{Note.C2}, 	{Note.A2},		{},		{},		{},
		{Note.A2},		{},		{Note.D},		{},		{Note.F},		{Note.G2},		{Note.A2},		{},
		{},		{},		{},		{}, 	{Note.A2},		{},		{Note.D},		{},
		{Note.F},		{Note.G2},		{Note.E},		{}, 	{},		{},		{},		{},
		{Note.D},		{Note.F},		{Note.D2},		{}, 	{},		{},		{Note.D},		{Note.F},
		{Note.D2},		{},		{},		{},		{Note.E2},		{},		{},		{Note.F2},
		{Note.E2},		{Note.F2},		{Note.E2},		{Note.C2}, 	{Note.A2},		{},		{},		{},
		{Note.A2},		{},		{Note.D},		{},		{Note.F},		{Note.G2},		{Note.A2},		{},
		{},		{},		{Note.A2},		{}, 	{Note.D},		{},		{},		{},
		{},		{},	{},		{},	{},		{},	{},		{},	
	};
	
    public void onEnable(){ 
    	INSTANCE = this;
        log.info("Plugin gestartet");
        
        getServer().getPluginManager().registerEvents(new TestListener(), this);
        
        music();
        effects();
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
    			counter = (counter + 1) % music.length;
    			synchronized(player_music) {
    				for (Player p: player_music) {
    					for (Note n: music[counter]) {
    						p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, n.getPitch());
    					}
    				}
    			}
    		}
    	}, 0L, 5L);
    }
    
    public void effects() {
    	this.getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
    		public void run() {
    			synchronized (player_effects) {
    				for (Player p: player_effects.keySet()) {
        				p.getWorld().spigot().playEffect(p.getLocation(), player_effects.get(p), 0, 0, (float) (Math.random() / 4), 1, (float) (Math.random() / 4), 0.2f, 5, 1);
        			}
    			}
    		}
    	}, 0L, 2L);
    }
}
