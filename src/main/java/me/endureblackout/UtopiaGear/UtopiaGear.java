
package me.endureblackout.UtopiaGear;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class UtopiaGear extends JavaPlugin {

	public void onEnable() {
		if (!getDataFolder().exists()) {
			getConfig().options().copyDefaults(true);
			saveConfig();
		}

		YamlConfiguration config = (YamlConfiguration) getConfig();
		
		@SuppressWarnings("unused")
		ShopGUIManager manager = new ShopGUIManager(config);

		getServer().getPluginManager().registerEvents(new GearHandler(this, config), this);
		getServer().getPluginManager().registerEvents(new WeaponHandler(this, config), this);
		getServer().getPluginManager().registerEvents(new CommandHandler(this, config), this);
		getCommand("ug").setExecutor(new CommandHandler(this, config));
	}
}
