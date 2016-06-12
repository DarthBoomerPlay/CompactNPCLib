package org.inventivetalent.npclib;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.apihelper.APIManager;
import org.inventivetalent.npclib.command.SpawnCommands;
import org.inventivetalent.npclib.registry.NPCRegistry;
import org.inventivetalent.pluginannotations.PluginAnnotations;

public class NPCLibPlugin extends JavaPlugin implements Listener {

	private NPCLib npcLibInstance = new NPCLib();
	private NPCRegistry pluginNpcRegistry;

	@Override
	public void onLoad() {
		APIManager.registerAPI(npcLibInstance);
	}

	@Override
	public void onEnable() {
		NPCLib.logger = getLogger();

		APIManager.initAPI(NPCLib.class);
		pluginNpcRegistry = NPCLib.createRegistry(this);

		Bukkit.getPluginManager().registerEvents(this, this);
		PluginAnnotations.COMMAND.load(this, new SpawnCommands(this));
	}

	@Override
	public void onDisable() {
		APIManager.disableAPI(NPCLib.class);
	}

	public NPCRegistry getPluginNpcRegistry() {
		return pluginNpcRegistry;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(CreatureSpawnEvent event) {
		System.out.println(event);
		System.out.println(event.isCancelled());
		event.setCancelled(false);
	}

}
