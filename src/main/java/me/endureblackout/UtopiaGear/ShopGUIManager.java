
package me.endureblackout.UtopiaGear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ShopGUIManager {

	YamlConfiguration							config;

	public static Inventory						mainGUI;
	public static Inventory						armorGUI;
	public static HashMap<String, Inventory>	specArmorGUI	= new HashMap<String, Inventory>();
	public static Inventory						weaponGUI;

	public ShopGUIManager(YamlConfiguration config) {
		this.config = config;

		setupMainGUI();
		setupArmorGUI();

		ConfigurationSection gearSec = this.config.getConfigurationSection("Gear");
		Set<String> gear = gearSec.getKeys(false);

		for (String k : gear) {
			setupSpecArmorGUI(k);
		}

		setupWeaponGUI();
	}

	public void setupMainGUI() {
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta swordMeta = sword.getItemMeta();

		swordMeta.setDisplayName(ChatColor.DARK_BLUE + "Weapons");
		sword.setItemMeta(swordMeta);

		ItemStack tunic = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta tunicMeta = (LeatherArmorMeta) tunic.getItemMeta();

		tunicMeta.setColor(Color.RED);
		tunicMeta.setDisplayName(ChatColor.RED + "Armor");
		tunic.setItemMeta(tunicMeta);

		mainGUI = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "Mystical Gear");
		mainGUI.setItem(11, sword);
		mainGUI.setItem(15, tunic);
	}

	public void setupArmorGUI() {
		armorGUI = Bukkit.createInventory(null, 27, ChatColor.RED + "Armor");

		ConfigurationSection gearSec = config.getConfigurationSection("Gear");
		Set<String> gear = gearSec.getKeys(false);

		List<ItemStack> armor = new ArrayList<ItemStack>();

		for (String k : gear) {
			UtopiaArmor armorObject = new UtopiaArmor(this.config);
			armorObject.setArmor(k);

			if (!(armorObject.hidden)) {
				armor.add(armorObject.createArmor(Material.LEATHER_CHESTPLATE.name()));
			}
		}

		for (int i = 0; i < armor.size(); i++) {
			ItemStack piece = armor.get(i);
			System.out.println(piece.getItemMeta().getDisplayName());
			armorGUI.setItem(armorGUI.firstEmpty(), armor.get(i));
		}
	}

	public void setupSpecArmorGUI(String name) {
		ConfigurationSection gearSec = config.getConfigurationSection("Gear");

		UtopiaArmor armorObject = new UtopiaArmor(this.config);
		armorObject.setArmor(name);

		if (!(armorObject.hidden)) {
			specArmorGUI.put(name, Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', gearSec.getConfigurationSection(name).getString("Name"))));
			specArmorGUI.get(name).setItem(specArmorGUI.get(name).firstEmpty(), armorObject.createArmor(Material.LEATHER_HELMET.name()));
			specArmorGUI.get(name).setItem(specArmorGUI.get(name).firstEmpty(), armorObject.createArmor(Material.LEATHER_CHESTPLATE.name()));
			specArmorGUI.get(name).setItem(specArmorGUI.get(name).firstEmpty(), armorObject.createArmor(Material.LEATHER_LEGGINGS.name()));
			specArmorGUI.get(name).setItem(specArmorGUI.get(name).firstEmpty(), armorObject.createArmor(Material.LEATHER_BOOTS.name()));
		}
	}

	public void setupWeaponGUI() {
		weaponGUI = Bukkit.createInventory(null, 27, ChatColor.DARK_BLUE + "Weapons");

		ConfigurationSection gearSec = config.getConfigurationSection("Weapons");
		Set<String> gear = gearSec.getKeys(false);

		for (String k : gear) {
			UtopiaWeapon weaponObject = new UtopiaWeapon(this.config);

			weaponObject.setWeapon(k);

			ItemStack weapon = weaponObject.createWeapon(weaponObject.getType());

			if (!weaponObject.isHidden()) {
				weaponGUI.setItem(weaponGUI.firstEmpty(), weapon);
			}
		}
	}
}
