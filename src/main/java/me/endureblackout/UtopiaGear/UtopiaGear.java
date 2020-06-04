package me.endureblackout.UtopiaGear;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class UtopiaGear extends JavaPlugin implements Listener {


	public UtopiaGear() {

	}
	
	

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
		getServer().getPluginManager().registerEvents(new StopBrewEvent(this), this);
		getServer().getPluginManager().registerEvents(new FullDrawHandler(this), this);
		getServer().getPluginManager().registerEvents(new GearEnchantHandler(this, config), this);
		getServer().getPluginManager().registerEvents(new TridentEnchantHandler(this), this);
		getCommand("mg").setExecutor(new CommandHandler(this, config));

		Bukkit.getPluginManager().registerEvents(this, this);
		this.makeClockAndChangingTimers();
	}

	public int r, g, b = 20;
	public int time = 59;

	public void makeClockAndChangingTimers() {
		YamlConfiguration config = (YamlConfiguration) getConfig();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				switch (time) {
				case 59:
					r = 255;
					g = 0;
					b = 0;
					time -= 1;
					break;
				case 58:
					r = 255;
					g = 68;
					b = 0;
					time -= 1;
					break;
				case 57:
					r = 255;
					g = 111;
					b = 0;
					time -= 1;
					break;
				case 56:
					r = 255;
					g = 171;
					b = 0;
					time -= 1;
					break;
				case 55:
					r = 255;
					g = 255;
					b = 0;
					time -= 1;
					break;
				case 54:
					r = 188;
					g = 255;
					b = 0;
					time -= 1;
					break;
				case 53:
					r = 128;
					g = 255;
					b = 0;
					time -= 1;
					break;
				case 52:
					r = 43;
					g = 255;
					b = 0;
					time -= 1;
					break;
				case 51:
					r = 0;
					g = 255;
					b = 9;
					time -= 1;
					break;
				case 50:
					r = 0;
					g = 255;
					b = 51;
					time -= 1;
					break;
				case 49:
					r = 0;
					g = 255;
					b = 111;
					time -= 1;
					break;
				case 48:
					r = 0;
					g = 255;
					b = 162;
					time -= 1;
					break;
				case 47:
					r = 0;
					g = 255;
					b = 230;
					time -= 1;
					break;
				case 46:
					r = 0;
					g = 239;
					b = 255;
					time -= 1;
					break;
				case 45:
					r = 0;
					g = 196;
					b = 255;
					time -= 1;
					break;
				case 44:
					r = 0;
					g = 173;
					b = 255;
					time -= 1;
					break;
				case 43:
					r = 0;
					g = 162;
					b = 255;
					time -= 1;
					break;
				case 42:
					r = 0;
					g = 137;
					b = 255;
					time -= 1;
					break;
				case 41:
					r = 0;
					g = 100;
					b = 255;
					time -= 1;
					break;
				case 40:
					r = 0;
					g = 77;
					b = 255;
					time -= 1;
					break;
				case 39:
					r = 0;
					g = 34;
					b = 255;
					time -= 1;
					break;
				case 38:
					r = 17;
					g = 0;
					b = 255;
					time -= 1;
					break;
				case 37:
					r = 37;
					g = 0;
					b = 255;
					time -= 1;
					break;
				case 36:
					r = 68;
					g = 0;
					b = 255;
					time -= 1;
					break;
				case 35:
					r = 89;
					g = 0;
					b = 255;
					time -= 1;
					break;
				case 34:
					r = 102;
					g = 0;
					b = 255;
					time -= 1;
					break;
				case 33:
					r = 124;
					g = 0;
					b = 255;
					time -= 1;
					break;
				case 32:
					r = 154;
					g = 0;
					b = 255;
					time -= 1;
					break;
				case 31:
					r = 222;
					g = 0;
					b = 255;
					time -= 1;
					break;
				case 30:
					r = 255;
					g = 0;
					b = 247;
					time -= 1;
					break;
				case 29:
					r = 255;
					g = 0;
					b = 179;
					time -= 1;
					break;
				case 28:
					r = 255;
					g = 0;
					b = 128;
					time = 59;
					break;
				}
				Color c = Color.fromRGB(r, g, b);
				ConfigurationSection gearSec = config.getConfigurationSection("Gear");
				Set<String> gear = gearSec.getKeys(false);

				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					ItemStack helmet = p.getInventory().getHelmet();
					ItemStack chest = p.getInventory().getChestplate();
					ItemStack legs = p.getInventory().getLeggings();
					ItemStack boots = p.getInventory().getBoots();

					for (String k : gear) {
						ConfigurationSection specGear = gearSec.getConfigurationSection(k);
						String name = ChatColor.translateAlternateColorCodes('&', specGear.getString("Name"));

						if (specGear.contains("rainbow") && specGear.getString("rainbow").equalsIgnoreCase("true")) {
							if (helmet != null && helmet.getType() == Material.LEATHER_HELMET
									&& helmet.getItemMeta().getDisplayName()
											.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
								p.getInventory().setHelmet(getColorArmor(helmet, c));

							}

							if (chest != null && chest.getType() == Material.LEATHER_CHESTPLATE
									&& chest.getItemMeta().getDisplayName()
											.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
								p.getInventory().setChestplate(getColorArmor(chest, c));
							}

							if (legs != null && legs.getType() == Material.LEATHER_LEGGINGS
									&& legs.getItemMeta().getDisplayName()
											.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
								p.getInventory().setLeggings(getColorArmor(legs, c));
							}

							if (boots != null && boots.getType() == Material.LEATHER_BOOTS
									&& boots.getItemMeta().getDisplayName()
											.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
								p.getInventory().setBoots(getColorArmor(boots, c));
							}
						}
					}
				}

			}
		}, 0, 2);
	}

	public ItemStack getColorArmor(ItemStack m, Color c) {
		LeatherArmorMeta meta = (LeatherArmorMeta) m.getItemMeta();
		meta.setColor(c);
		m.setItemMeta(meta);
		return m;
	}
}
