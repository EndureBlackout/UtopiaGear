
package me.endureblackout.UtopiaGear;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WeaponHandler implements Listener {

	UtopiaGear			core;
	YamlConfiguration	config;

	public WeaponHandler(UtopiaGear core, YamlConfiguration config) {
		this.core = core;
		this.config = config;
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player p = (Player) e.getDamager();
			Player d = (Player) e.getEntity();

			ConfigurationSection weapSec = config.getConfigurationSection("Weapons");
			Set<String> weaps = weapSec.getKeys(false);
			
			UtopiaWeapon utopiaWeap = new UtopiaWeapon(config);

			for (String k : weaps) {
				ConfigurationSection weap = weapSec.getConfigurationSection(k);
				
				utopiaWeap.setWeapon(k);
				ItemStack weaponStack = utopiaWeap.createWeapon(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
				ItemStack holdingWeap = p.getInventory().getItemInMainHand();
				
				
				if (!(p.getInventory().getItemInMainHand().getType().equals(Material.AIR))) {
					if (holdingWeap.getItemMeta().getDisplayName().equalsIgnoreCase(weaponStack.getItemMeta().getDisplayName())) {
						if (holdingWeap.getItemMeta().getLore().equals(weaponStack.getItemMeta().getLore())) {
							List<String> goodeffects = weap.getStringList("goodeffects");
							List<String> badeffects = weap.getStringList("badeffects");

							if (!(goodeffects.size() == 0)) {
								for (String t : goodeffects) {
									String[] list = t.split(",");

									String effect = list[0];
									int duration = Integer.parseInt(list[2]);
									double chance = Double.parseDouble(list[1]);
									int level = Integer.parseInt(list[3]);

									if (Math.random() <= chance) {
										p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect.toUpperCase()), duration * 20, level));
										p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "UtopiaGear" + ChatColor.GRAY + "] " + ChatColor.GREEN + "You have received " + ChatColor.WHITE + effect + " " + (level + 1) + ChatColor.GREEN + " for "
											+ ChatColor.GREEN + duration);
									}
								}
							}

							if (!(badeffects.size() == 0)) {
								for (String t : badeffects) {
									String[] list = t.split(",");

									String effect = list[0];
									int duration = Integer.parseInt(list[2]);
									double chance = Double.parseDouble(list[1]);
									int level = Integer.parseInt(list[3]);

									if (Math.random() <= chance) {
										d.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect.toUpperCase()), duration * 20, level));

										if (d instanceof Player) {
											d.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "UtopiaGear" + ChatColor.GRAY + "] " + ChatColor.GREEN + "You have received " + ChatColor.WHITE + effect + " " + (level + 1) + ChatColor.GREEN + " for "
												+ ChatColor.GREEN + duration);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
