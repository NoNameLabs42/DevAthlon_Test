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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {
	
	public static TestPlugin INSTANCE;
	
	public FileConfiguration config = getConfig();
	
	public Logger log = this.getLogger();
	
	public List<Player> player_music = new ArrayList<Player>();
	public Map<Player, Effect> player_effects = new HashMap<Player, Effect>();
	
	private int particle_speed, particles, particle_size;
	
	
	
	public static Note[][] music = {
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
        config();
    }
     
    public void onDisable(){ 
    	log.info("Plugin gestoppt");
    }
    
    public void config() { 
        config.addDefault("Partikel_Geschwindigkeit", 5);
        config.addDefault("Partikel_Menge", 5);
        config.addDefault("Partikel_Gr��e", 1);
        config.options().copyDefaults(true);
        
        particle_speed = config.getInt("Partikel_Geschwindigkeit");
        particles = config.getInt("Partikel_Menge");
        particle_size = config.getInt("Partikel_Gr��e");
        
        saveConfig();
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
        				p.getWorld().spigot().playEffect(p.getLocation(), player_effects.get(p), 0, 0, (float) (Math.random() / 4), 1, (float) (Math.random() / 4), 1f / particle_speed, particles, particle_size);
        			}
    			}
    		}
    	}, 0L, 2L);
    }
}
