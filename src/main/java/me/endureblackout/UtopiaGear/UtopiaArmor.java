
package me.endureblackout.UtopiaGear;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class UtopiaArmor {

	YamlConfiguration	config;

	public String		name	= "";
	public List<String>	lore	= new ArrayList<String>();
	public String		color	= "";
	public int			price	= 0;
	public boolean		hidden	= false;

	public UtopiaArmor(YamlConfiguration config) {
		this.config = config;
	}

	public void setArmor(String name) {
		ConfigurationSection gearSec = config.getConfigurationSection("Gear");
		Set<String> gear = gearSec.getKeys(false);

		for (String k : gear) {
			ConfigurationSection armor = gearSec.getConfigurationSection(k);

			if (k.equalsIgnoreCase(name)) {
				this.name = armor.getString("Name");
				this.lore = armor.getStringList("lore");
				this.color = armor.getString("Color");
				this.price = armor.getInt("price");
				this.hidden = armor.getBoolean("oponly");
			}
		}
	}

	public String getName() {
		return this.name;
	}

	public List<String> getLore() {
		return this.lore;
	}

	public Color getColor() {
		Colors color = new Colors(this.color);
		return color.getColor();
	}

	public Integer getPrice() {
		return this.price;
	}

	public ItemStack createArmor(String piece) {
		ItemStack armorPiece = new ItemStack(Material.getMaterial(piece));
		LeatherArmorMeta armorMeta = (LeatherArmorMeta) armorPiece.getItemMeta();

		armorMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));
		
		ArrayList<String> loreList = new ArrayList<String>();

		if (!(lore.size() == 0) && !(lore == null)) {
			for (int i = 0; i < lore.size(); i++) {
				loreList.add(ChatColor.translateAlternateColorCodes('&', lore.get(i)));
			}
		}

		armorMeta.setLore(loreList);
		armorMeta.setColor(getColor());
		armorPiece.setItemMeta(armorMeta);

		return armorPiece;
	}
	
	public ItemStack createShopItem(String piece) {
		ItemStack armorPiece = new ItemStack(Material.getMaterial(piece));
		LeatherArmorMeta armorMeta = (LeatherArmorMeta) armorPiece.getItemMeta();

		armorMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));
		
		ArrayList<String> loreList = new ArrayList<String>();

		if (!(lore.size() == 0) && !(lore == null)) {
			for (int i = 0; i < lore.size(); i++) {
				loreList.add(ChatColor.translateAlternateColorCodes('&', lore.get(i)));
			}
		}
		
		loreList.add(ChatColor.GOLD + "Price: " + getPrice());

		armorMeta.setLore(loreList);
		armorMeta.setColor(getColor());
		armorPiece.setItemMeta(armorMeta);

		return armorPiece;
	}
}
