
package me.endureblackout.UtopiaGear;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UtopiaWeapon {

	YamlConfiguration config;

	public String name = "";
	public List<String> lore = new ArrayList<String>();
	public String type = "";
	public List<String> enchants = new ArrayList<String>();
	public int price = 0;
	public boolean hidden = false;

	public UtopiaWeapon(YamlConfiguration config) {
		this.config = config;
	}

	public void setWeapon(String name) {
		ConfigurationSection gearSec = config.getConfigurationSection("Weapons");
		Set<String> gear = gearSec.getKeys(false);

		for (String k : gear) {
			ConfigurationSection weapon = gearSec.getConfigurationSection(k);

			if (k.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
				this.name = weapon.getString("Name");
				this.lore = weapon.getStringList("Lore");
				this.type = weapon.getString("Type");
				this.price = weapon.getInt("Price");
				this.hidden = weapon.getBoolean("oponly");
				this.enchants = weapon.getStringList("enchants");
			}
		}
	}

	public String getName() {
		return this.name;
	}

	public List<String> getLore() {
		return this.lore;
	}

	public String getType() {
		return this.type;
	}

	public List<String> getEnchants() {
		return this.enchants;
	}

	public int getPrice() {
		return this.price;
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public ItemStack createWeapon(String type) {
		ItemStack weapon = new ItemStack(Material.getMaterial(getType().toUpperCase()));
		ItemMeta weaponMeta = weapon.getItemMeta();

		weaponMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));

		ArrayList<String> loreList = new ArrayList<String>();

		if (!(lore.size() == 0) && !(lore == null)) {
			for (int i = 0; i < lore.size(); i++) {
				loreList.add(ChatColor.translateAlternateColorCodes('&', lore.get(i)));
			}
		}

		for (int i = 0; i < enchants.size(); i++) {
			String[] enchantString = enchants.get(i).split(",");

			String enchant = enchantString[0];
			int level = Integer.parseInt(enchantString[1]);
			weaponMeta.addEnchant(Enchantment.getByName(enchant.toUpperCase()), level, true);
		}

		weaponMeta.setLore(loreList);
		weapon.setItemMeta(weaponMeta);
		return weapon;
	}
	
	public ItemStack createShopWeapon(String type) {
		ItemStack weapon = new ItemStack(Material.getMaterial(getType().toUpperCase()));
		ItemMeta weaponMeta = weapon.getItemMeta();

		weaponMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));

		ArrayList<String> loreList = new ArrayList<String>();

		if (!(lore.size() == 0) && !(lore == null)) {
			for (int i = 0; i < lore.size(); i++) {
				loreList.add(ChatColor.translateAlternateColorCodes('&', lore.get(i)));
			}
		}
		
		loreList.add(ChatColor.GOLD + "Price: " + getPrice());

		for (int i = 0; i < enchants.size(); i++) {
			String[] enchantString = enchants.get(i).split(",");

			String enchant = enchantString[0];
			int level = Integer.parseInt(enchantString[1]);
			weaponMeta.addEnchant(Enchantment.getByName(enchant.toUpperCase()), level, true);
		}

		weaponMeta.setLore(loreList);
		weapon.setItemMeta(weaponMeta);
		return weapon;
	}
}
