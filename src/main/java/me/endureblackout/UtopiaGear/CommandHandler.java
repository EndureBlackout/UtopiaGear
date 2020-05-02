
package me.endureblackout.UtopiaGear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandHandler implements CommandExecutor, Listener {

	UtopiaGear core;
	YamlConfiguration config;

	public CommandHandler(UtopiaGear instance, YamlConfiguration config) {
		this.core = instance;
		this.config = config;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("mg")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("shop")) {
						p.openInventory(ShopGUIManager.mainGUI);
					}
					if (args[0].equalsIgnoreCase("reload")) {
						core.reloadConfig();
						p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "MysticalGear" + ChatColor.GRAY + "] "
								+ ChatColor.GREEN + "Config reloaded!");
					}
					if (args[0].equalsIgnoreCase("hidden") && p.hasPermission("utopiagear.admin")) {
						p.openInventory(ShopGUIManager.hiddenGUI);
					}
					if (args[0].equalsIgnoreCase("token") && p.hasPermission("utopiagear.admin")) {
						ItemStack tokens = new ItemStack(Material.getMaterial(config.getString("Tokens.Item")));
						tokens.setAmount(64);

						ItemMeta tokenMeta = tokens.getItemMeta();

						tokenMeta.setDisplayName(
								ChatColor.translateAlternateColorCodes('&', config.getString("Tokens.Name")));

						List<String> lore = config.getStringList("Tokens.Lore");
						List<String> loreList = new ArrayList<String>();

						if (!(lore.size() == 0) && !(lore == null))
							for (int i = 0; i < lore.size(); i++) {
								loreList.add(ChatColor.translateAlternateColorCodes('&', lore.get(i)));
							}

						tokenMeta.setLore(loreList);

						tokens.setItemMeta(tokenMeta);

						p.getInventory().addItem(tokens);
					}
				}
			}
		}

		return true;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		ConfigurationSection gearSec = config.getConfigurationSection("Gear");
		Set<String> gear = gearSec.getKeys(false);

		if (!(e.getCurrentItem() == null) && !e.getCurrentItem().getType().equals(Material.AIR)) {
			if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Mystical Gear")) {
				e.setCancelled(true);

				if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("armor")) {
					p.closeInventory();

					new BukkitRunnable() {

						@Override
						public void run() {
							p.openInventory(ShopGUIManager.armorGUI);
						}
					}.runTaskLater(core, 1);
				} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())
						.equalsIgnoreCase("weapons")) {
					p.closeInventory();

					new BukkitRunnable() {

						public void run() {
							p.openInventory(ShopGUIManager.weaponGUI);
						}
					}.runTaskLater(core, 1);
				}
			} else if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Armor")) {
				e.setCancelled(true);
				for (String k : gear) {
					ItemStack clickedItem = e.getCurrentItem();

					UtopiaArmor createdArmor = new UtopiaArmor(config);
					createdArmor.setArmor(k);

					if (clickedItem.equals(createdArmor.createArmor(clickedItem.getType().name()))) {
						p.closeInventory();

						new BukkitRunnable() {

							public void run() {
								p.openInventory(ShopGUIManager.specArmorGUI.get(k));
							}
						}.runTaskLater(this.core, 1);
					}
				}
			} else if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("HIDDEN MENU")) {
				e.setCancelled(true);
				if (e.getCurrentItem().getType().equals(Material.LEATHER_CHESTPLATE)) {
					for (String k : gear) {
						ItemStack clickedItem = e.getCurrentItem();

						UtopiaArmor createdArmor = new UtopiaArmor(config);
						createdArmor.setArmor(k);

						if (clickedItem.equals(createdArmor.createArmor(clickedItem.getType().name()))) {
							p.closeInventory();

							new BukkitRunnable() {

								public void run() {
									p.openInventory(ShopGUIManager.specArmorGUI.get(k));
								}
							}.runTaskLater(this.core, 1);
						}
					}
				} else {
					ItemStack clickedItem = e.getCurrentItem();

					ConfigurationSection weapSec = config.getConfigurationSection("Weapons");
					Set<String> weap = weapSec.getKeys(false);

					for (String k : weap) {

						ConfigurationSection weapon = weapSec.getConfigurationSection(k);

						UtopiaWeapon createdWeap = new UtopiaWeapon(config);
						createdWeap.setWeapon(k);

						if (clickedItem.equals(createdWeap.createWeapon(clickedItem.getType().name()))) {
							e.setCancelled(true);

							new BukkitRunnable() {
								public void run() {
									p.closeInventory();

									if (checkTokens(p, weapon.getInt("Price"))) {
										p.getInventory().addItem(createdWeap.createWeapon(weapon.getString("Type")));
										p.sendMessage(ChatColor.GREEN + "You bought a " + ChatColor
												.translateAlternateColorCodes('&', weapon.getString("Name")));
									} else {
										p.sendMessage(ChatColor.RED + "Sorry, but you don't have enough to buy that!");
									}
								}
							}.runTaskLater(this.core, 1);
						}
					}
				}
			} else if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Weapons")) {
				ItemStack clickedItem = e.getCurrentItem();

				ConfigurationSection weapSec = config.getConfigurationSection("Weapons");
				Set<String> weap = weapSec.getKeys(false);

				for (String k : weap) {

					ConfigurationSection weapon = weapSec.getConfigurationSection(k);

					UtopiaWeapon createdWeap = new UtopiaWeapon(config);
					createdWeap.setWeapon(k);

					if (clickedItem.equals(createdWeap.createWeapon(clickedItem.getType().name()))) {
						e.setCancelled(true);

						new BukkitRunnable() {
							public void run() {
								p.closeInventory();

								if (checkTokens(p, weapon.getInt("Price"))) {
									p.getInventory().addItem(createdWeap.createWeapon(weapon.getString("Type")));
									p.sendMessage(ChatColor.GREEN + "You bought a "
											+ ChatColor.translateAlternateColorCodes('&', weapon.getString("Name")));
								} else {
									p.sendMessage(ChatColor.RED + "Sorry, but you don't have enough to buy that!");
								}
							}
						}.runTaskLater(this.core, 1);
					}
				}
			} else {
				for (String k : gear) {
					ConfigurationSection armor = gearSec.getConfigurationSection(k);

					UtopiaArmor createdArmor = new UtopiaArmor(config);
					createdArmor.setArmor(k);

					ItemStack clickedItem = e.getCurrentItem();

					if (e.getView().getTitle()
							.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', armor.getString("Name")))) {
						if (clickedItem.equals(createdArmor.createArmor(clickedItem.getType().name()))) {
							e.setCancelled(true);

							new BukkitRunnable() {
								public void run() {
									p.closeInventory();

									if (checkTokens(p, armor.getInt("price"))) {
										ItemStack armorItem = createdArmor.createArmor(clickedItem.getType().name());
										LeatherArmorMeta armorMeta = (LeatherArmorMeta) armorItem.getItemMeta();
										armorMeta.setUnbreakable(true);
										armorItem.setItemMeta(armorMeta);

										p.getInventory().addItem(armorItem);
										p.sendMessage(ChatColor.GREEN + "You bought "
												+ ChatColor.translateAlternateColorCodes('&', armor.getString("Name")));
									} else {
										p.sendMessage(ChatColor.RED + "Sorry, but you don't have enough to buy that!");
									}
								}
							}.runTaskLater(this.core, 1);
						}
					}
				}
			}
		}
	}

	public List<ItemStack> allArmorOption(String name, String lore, Color color) {
		ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta hMeta = (LeatherArmorMeta) helm.getItemMeta();

		hMeta.setColor(color);
		hMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		hMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore)));

		helm.setItemMeta(hMeta);

		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta cMeta = (LeatherArmorMeta) chest.getItemMeta();

		cMeta.setColor(color);
		cMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		cMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore)));

		chest.setItemMeta(cMeta);

		ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta lMeta = (LeatherArmorMeta) legs.getItemMeta();

		lMeta.setColor(color);
		lMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		lMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore)));

		legs.setItemMeta(lMeta);

		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta bMeta = (LeatherArmorMeta) boots.getItemMeta();

		bMeta.setColor(color);
		bMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		bMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore)));

		boots.setItemMeta(bMeta);

		List<ItemStack> items = new ArrayList<ItemStack>();
		items.add(helm);
		items.add(chest);
		items.add(legs);
		items.add(boots);

		return items;
	}

	public ItemStack armorOption(String name, String lore, Color color) {
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta cMeta = (LeatherArmorMeta) chest.getItemMeta();

		cMeta.setColor(color);
		cMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		cMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore)));

		chest.setItemMeta(cMeta);

		return chest;
	}

	@SuppressWarnings("deprecation")
	public ItemStack weaponOption(String type, String name, String lore, List<String> enchants) {
		ItemStack weapon = new ItemStack(Material.getMaterial(type.toUpperCase()));
		ItemMeta weapMeta = weapon.getItemMeta();

		weapMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		weapMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore)));

		for (String e : enchants) {
			String[] splitup = e.split(",");

			String enchant = splitup[0];
			int level = Integer.parseInt(splitup[1]);

			weapMeta.addEnchant(Enchantment.getByName(enchant.toUpperCase()), level, true);
		}

		weapMeta.setUnbreakable(true);
		weapon.setItemMeta(weapMeta);

		return weapon;
	}

	public void giveArmor(Player p, String type, String name, String lore, Color color) {
		ItemStack item = new ItemStack(Material.getMaterial(type.toUpperCase()));
		LeatherArmorMeta iMeta = (LeatherArmorMeta) item.getItemMeta();

		iMeta.setColor(color);

		iMeta.setDisplayName(name);

		iMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore)));

		iMeta.setUnbreakable(true);
		item.setItemMeta(iMeta);

		p.getInventory().addItem(item);
	}

	public boolean checkTokens(Player p, Integer price) {
		ConfigurationSection tokens = config.getConfigurationSection("Tokens");
		PlayerInventory pInv = p.getInventory();
		ItemStack token = new ItemStack(Material.getMaterial(tokens.getString("Item")));

		if (pInv.firstEmpty() != -1) {
			if (!p.hasPermission("utopiagear.admin")) {
				for (ItemStack i : pInv.getContents()) {
					if (!(i == null)) {
						if (i.isSimilar(token)) {
							if (i.getAmount() == price) {
								pInv.remove(i);
								return true;
							} else if (i.getAmount() > price) {
								i.setAmount(i.getAmount() - price);
								return true;
							} else {
								p.sendMessage(ChatColor.RED + "Not enough tokens for that!");
								return false;
							}
						}
					} else {
						return false;
					}
				}
				return false;

			} else {
				return true;
			}
		} else {
			p.sendMessage(
					ChatColor.RED + "Your inventory was full and we couldn't give you the item. You were not charged!");
			p.closeInventory();
			p.closeInventory();
			p.closeInventory();
			p.closeInventory();
			return false;
		}
	}
}
