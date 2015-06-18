package de.NoNameLabs.Main;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {
	
	Logger log = this.getLogger();
	
    public void onEnable(){ 
        log.info("Plugin gestartet");
        
        //getServer().getPluginManager().registerEvents(new Listener(), this);
        
    }
     
    public void onDisable(){ 
    	log.info("Plugin gestoppt");
    }
}
