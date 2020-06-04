package me.endureblackout.UtopiaGear;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class GearEnchantHandler implements Listener {
	UtopiaGear core;
	YamlConfiguration config;

	public GearEnchantHandler(UtopiaGear core, YamlConfiguration config) {
		this.core = core;
		this.config = config;
	}

	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		ItemStack item = e.getItem();

		if (item.getType().equals(Material.LEATHER_HELMET) || item.getType().equals(Material.LEATHER_CHESTPLATE)
				|| item.getType().equals(Material.LEATHER_LEGGINGS) || item.getType().equals(Material.LEATHER_BOOTS)) {
			LeatherArmorMeta armorMeta = (LeatherArmorMeta) item.getItemMeta();

			ConfigurationSection gearSec = config.getConfigurationSection("Gear");
			Set<String> gear = gearSec.getKeys(false);

			for (String k : gear) {
				ConfigurationSection specGear = gearSec.getConfigurationSection(k);
				UtopiaArmor armor = new UtopiaArmor(config);
				armor.setArmor(k);
				
				ItemStack armorStack = armor.createArmor(item.getType().toString());

				if (armorMeta.getDisplayName()
						.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', specGear.getString("Name")))) {
					if(armorMeta.getLore().equals(armorStack.getItemMeta().getLore())) {
						e.setCancelled(true);
						e.getEnchanter().sendMessage(ChatColor.RED + "You are not allowed to enchant Mystical Gear.");
					}					
				}
			}
		} else  {
			ItemMeta weapMeta = item.getItemMeta();
			ConfigurationSection weapSec = config.getConfigurationSection("Weapons");
			Set<String> weaps = weapSec.getKeys(false);
			
			for(String k : weaps) {
				ConfigurationSection weap = weapSec.getConfigurationSection(k);
				UtopiaWeapon weapon = new UtopiaWeapon(config);
				weapon.setWeapon(k);
				ItemStack weapStack = weapon.createWeapon(weap.getString("Type"));
				
				if(item.getType().toString().equalsIgnoreCase(weapStack.getType().toString())) {
					if(weapMeta.getLore().equals(weapStack.getItemMeta().getLore())) {
						e.setCancelled(true);
						e.getEnchanter().sendMessage(ChatColor.RED + "You are not allowed the enchant Mystical Gear.");
					}
				}
			}
		}
	}
}
