
package me.endureblackout.UtopiaGear;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class GearHandler implements Listener {

	UtopiaGear core;
	YamlConfiguration config;
	WorldGuardPlugin wg;

	public GearHandler(UtopiaGear instance, YamlConfiguration config) {
		this.core = instance;
		this.config = config;
		wg = (WorldGuardPlugin) loadPlugin("WorldGuard");
	}

	@EventHandler
	public void playerHit(EntityDamageByEntityEvent e) throws NumberFormatException, NotRegisteredException {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			PlayerInventory pInv = p.getInventory();

			LocalPlayer lPlayer = wg.wrapPlayer(p);
			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionManager rMan = container.get(lPlayer.getWorld());
			RegionQuery query = container.createQuery();
			ApplicableRegionSet arset = query.getApplicableRegions(lPlayer.getLocation());
			Town town = null;

			if (!TownyUniverse.isWilderness(p.getLocation().getBlock())) {
				town = TownyUniverse.getDataSource().getTown(TownyUniverse.getTownName(p.getLocation()));
			}

			if (arset.size() == 0 && town == null || arset.testState(lPlayer, Flags.PVP) && town == null
					|| arset.testState(lPlayer, Flags.PVP) && town.isPVP() || arset.size() == 0 && town.isPVP()) {
				double damage = e.getDamage();

				ConfigurationSection gearSec = config.getConfigurationSection("Gear");
				Set<String> gear = gearSec.getKeys(false);

				ItemStack[] is = pInv.getArmorContents();

				// int eap = 0;
				// int high = 0;
				// int low = 0;
				double reduction = 0;

				if (e.getCause() == DamageCause.PROJECTILE && e.getDamager() instanceof Arrow) {
					Arrow arrow = (Arrow) e.getDamager();

					if (arrow.getShooter() instanceof Player) {
						Player shooter = (Player) arrow.getShooter();
						PlayerInventory sInv = shooter.getInventory();

						ItemStack[] ds = sInv.getArmorContents();

						for (int i = 0; i < ds.length; i++) {
							if (!(ds[i] == null) && !(ds[i].getType().equals(Material.AIR))) {
								for (String k : gear) {
									UtopiaArmor armor = new UtopiaArmor(config);
									armor.setArmor(k);

									if (ds[i].getType().equals(Material.LEATHER_BOOTS)
											|| ds[i].getType().equals(Material.LEATHER_CHESTPLATE)
											|| ds[i].getType().equals(Material.LEATHER_HELMET)
											|| ds[i].getType().equals(Material.LEATHER_LEGGINGS)) {

										ItemStack armorStack = armor.createArmor(ds[i].getType().name());
										ItemStack wearingArmor = ds[i];

										if (wearingArmor.getItemMeta().getLore()
												.equals(armorStack.getItemMeta().getLore())) {
											List<String> goodEffects = gearSec.getConfigurationSection(k)
													.getStringList("goodeffects");
											List<String> badEffects = gearSec.getConfigurationSection(k)
													.getStringList("badeffects");

											if (!(goodEffects.size() == 0)) {
												for (String t : goodEffects) {
													String[] list = t.split(",");

													String effect = list[0];
													int duration = Integer.parseInt(list[2]);
													double chance = Double.parseDouble(list[1]);
													int level = Integer.parseInt(list[3]);

													if (Math.random() <= chance) {
														shooter.addPotionEffect(new PotionEffect(
																PotionEffectType.getByName(effect.toUpperCase()),
																duration * 20, level));
														shooter.sendMessage(
																ChatColor.GRAY + "[" + ChatColor.BLUE + "MysticalGear"
																		+ ChatColor.GRAY + "] " + ChatColor.GREEN
																		+ "You have received " + ChatColor.WHITE
																		+ effect + " " + (level + 1) + ChatColor.GREEN
																		+ " for " + ChatColor.GREEN + duration);

													}
												}
											}

											if (!(badEffects.size() == 0)) {
												for (String t : badEffects) {
													String[] list = t.split(",");

													String effect = list[0];
													int duration = Integer.parseInt(list[2]);
													double chance = Double.parseDouble(list[1]);
													int level = Integer.parseInt(list[3]);

													if (Math.random() <= chance) {
														p.addPotionEffect(new PotionEffect(
																PotionEffectType.getByName(effect.toUpperCase()),
																duration * 20, level));
														p.sendMessage(
																ChatColor.GRAY + "[" + ChatColor.BLUE + "MysticalaGear"
																		+ ChatColor.GRAY + "] " + ChatColor.GREEN
																		+ "You have received " + ChatColor.WHITE
																		+ effect + " " + (level + 1) + ChatColor.GREEN
																		+ " for " + ChatColor.GREEN + duration);
													}
												}
											}
										}
									}
								}
							}
						}

						for (int i = 0; i < is.length; i++) {
							if (!(is[i] == null) && !(is[i].getType().equals(Material.AIR))) {
								for (String k : gear) {
									UtopiaArmor armor = new UtopiaArmor(config);
									armor.setArmor(k);

									if (is[i].getType().equals(Material.LEATHER_BOOTS)
											|| is[i].getType().equals(Material.LEATHER_CHESTPLATE)
											|| is[i].getType().equals(Material.LEATHER_HELMET)
											|| is[i].getType().equals(Material.LEATHER_LEGGINGS)) {
										ItemStack armorStack = armor.createArmor(is[i].getType().name());
										ItemStack wearingArmor = is[i];

										double pvpHighest = gearSec.getDouble(k + ".pvphighest");
										double pvpLowest = gearSec.getDouble(k + ".pvplowest");

										Random rand = new Random();
										double random = rand.nextDouble();
										double reductNum = pvpLowest + (random * (pvpHighest - pvpLowest));

										if (wearingArmor.getItemMeta().getLore()
												.equals(armorStack.getItemMeta().getLore())) {
											if (is[i].getType().equals(Material.LEATHER_HELMET)) {
												reduction += reductNum + 0.12;
												System.out.println("Helm");
											} else if (is[i].getType().equals(Material.LEATHER_CHESTPLATE)) {
												reduction += reductNum + 0.22;
												System.out.println("Chest");
											} else if (is[i].getType().equals(Material.LEATHER_LEGGINGS)) {
												reduction += reductNum + 0.17;
												System.out.println("Leggings");
											} else if (is[i].getType().equals(Material.LEATHER_BOOTS)) {
												reduction += reductNum + 0.12;
												System.out.println("Boots");
											}
										}
									}
								}
							}
						}
					}
					e.setDamage(calcPvpDamage(reduction, damage));
					return;
				}

				if (e.getDamager() instanceof Player) {
					Player damager = (Player) e.getDamager();
					Entity hit = e.getEntity();
					PlayerInventory dInv = damager.getInventory();
					ItemStack[] ds = dInv.getArmorContents();

					for (int i = 0; i < ds.length; i++) {
						if (!(ds[i] == null) && !(ds[i].getType() == Material.AIR)) {
							for (String k : gear) {
								UtopiaArmor armor = new UtopiaArmor(config);
								armor.setArmor(k);

								if (ds[i].getType().equals(Material.LEATHER_BOOTS)
										|| ds[i].getType().equals(Material.LEATHER_CHESTPLATE)
										|| ds[i].getType().equals(Material.LEATHER_HELMET)
										|| ds[i].getType().equals(Material.LEATHER_LEGGINGS)) {
									ItemStack armorStack = armor.createArmor(ds[i].getType().name());
									ItemStack wearingArmor = ds[i];

									if (wearingArmor.getItemMeta().getLore()
											.equals(armorStack.getItemMeta().getLore())) {
										List<String> goodEffects = gearSec.getConfigurationSection(k)
												.getStringList("goodeffects");
										List<String> badEffects = gearSec.getConfigurationSection(k)
												.getStringList("badeffects");

										if (!(goodEffects.size() == 0)) {
											for (String t : goodEffects) {
												String[] list = t.split(",");

												String effect = list[0];
												int duration = Integer.parseInt(list[2]);
												double chance = Double.parseDouble(list[1]);
												int level = Integer.parseInt(list[3]);

												if (Math.random() <= chance) {
													damager.addPotionEffect(new PotionEffect(
															PotionEffectType.getByName(effect.toUpperCase()),
															duration * 20, level));
													damager.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE
															+ "MysticalGear" + ChatColor.GRAY + "] " + ChatColor.GREEN
															+ "You have received " + ChatColor.WHITE + effect + " "
															+ (level + 1) + ChatColor.GREEN + " for " + ChatColor.GREEN
															+ duration);
												}
											}
										}

										if (!(badEffects.size() == 0)) {
											for (String t : badEffects) {
												String[] list = t.split(",");

												String effect = list[0];
												int duration = Integer.parseInt(list[2]);
												double chance = Double.parseDouble(list[1]);
												int level = Integer.parseInt(list[3]);

												if (Math.random() <= chance) {
													((LivingEntity) hit).addPotionEffect(new PotionEffect(
															PotionEffectType.getByName(effect.toUpperCase()),
															duration * 20, level));
													hit.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE
															+ "MysticalGear" + ChatColor.GRAY + "] " + ChatColor.GREEN
															+ "You have received " + ChatColor.WHITE + effect + " "
															+ (level + 1) + ChatColor.GREEN + " for " + ChatColor.GREEN
															+ duration);
												}
											}
										}
									}
								}
							}
						}
					}

					for (int i = 0; i < is.length; i++) {
						if (!(is[i] == null) && !(is[i].getType().equals(Material.AIR))
								&& (is[i].getType().equals(Material.LEATHER_BOOTS)
										|| is[i].getType().equals(Material.LEATHER_LEGGINGS)
										|| is[i].getType().equals(Material.LEATHER_CHESTPLATE)
										|| is[i].getType().equals(Material.LEATHER_HELMET))) {
							for (String k : gear) {
								UtopiaArmor armor = new UtopiaArmor(config);
								armor.setArmor(k);

								ItemStack armorStack = armor.createArmor(is[i].getType().name());
								ItemStack wearingArmor = is[i];

								double pvpHighest = gearSec.getDouble(k + ".pvphighest");
								double pvpLowest = gearSec.getDouble(k + ".pvplowest");

								Random rand = new Random();
								double random = rand.nextDouble();
								float reductNum = (float) (pvpLowest + (random * (pvpHighest - pvpLowest)));
								System.out.println("Reduct Num: " + reductNum);

								if (wearingArmor.getItemMeta().getLore().equals(armorStack.getItemMeta().getLore())) {

									if (is[i].getType().equals(Material.LEATHER_HELMET)) {
										reduction += reductNum + 0.12;
									} else if (is[i].getType().equals(Material.LEATHER_CHESTPLATE)) {
										reduction += reductNum + 0.22;
									} else if (is[i].getType().equals(Material.LEATHER_LEGGINGS)) {
										reduction += reductNum + 0.17;
									} else if (is[i].getType().equals(Material.LEATHER_BOOTS)) {
										reduction += reductNum + 0.12;
									}
								}

								System.out.println("Reduction: " + reduction);
							}
						}
					}
					e.setDamage(calcPvpDamage(reduction, damage));
					return;
				} else if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player)
						&& e.getDamager() instanceof Player) {
					LivingEntity hit = (LivingEntity) e.getEntity();
					Player damager = (Player) e.getDamager();
					PlayerInventory dInv = damager.getInventory();
					ItemStack[] ds = dInv.getArmorContents();

					for (int i = 0; i < ds.length; i++) {
						if (!(ds[i] == null) && !(ds[i].getType()).equals(Material.AIR)) {
							for (String k : gear) {
								UtopiaArmor armor = new UtopiaArmor(config);
								armor.setArmor(k);

								ItemStack armorStack = armor.createArmor(is[i].getType().name());
								ItemStack wearingArmor = is[i];

								double pvpHighest = gearSec.getDouble(k + ".pvphighest");
								double pvpLowest = gearSec.getDouble(k + ".pvplowest");

								Random rand = new Random();
								double random = rand.nextDouble();
								double reductNum = pvpLowest + (random * (pvpHighest - pvpLowest));

								if (wearingArmor.equals(armorStack)) {
									if (is[i].getType().equals(Material.LEATHER_HELMET)) {
										reduction += reductNum + 0.12;
									} else if (is[i].getType().equals(Material.LEATHER_CHESTPLATE)) {
										reduction += reductNum + 0.22;
									} else if (is[i].getType().equals(Material.LEATHER_LEGGINGS)) {
										reduction += reductNum + 0.17;
									} else if (is[i].getType().equals(Material.LEATHER_BOOTS)) {
										reduction += reductNum + 0.12;
									}

									List<String> goodEffects = gearSec.getConfigurationSection(k)
											.getStringList("goodeffects");
									List<String> badEffects = gearSec.getConfigurationSection(k)
											.getStringList("badeffects");

									for (String t : goodEffects) {
										String[] list = t.split(",");

										String effect = list[0];
										int duration = Integer.parseInt(list[2]);
										double chance = Double.parseDouble(list[1]);
										int level = Integer.parseInt(list[3]);

										if (Math.random() <= chance) {
											damager.addPotionEffect(
													new PotionEffect(PotionEffectType.getByName(effect.toUpperCase()),
															duration * 20, level));
											damager.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "MysticalGear"
													+ ChatColor.GRAY + "] " + ChatColor.GREEN + "You have received "
													+ ChatColor.WHITE + effect + " " + (level + 1) + ChatColor.GREEN
													+ " for " + ChatColor.GREEN + duration);
										}
									}

									for (String t : badEffects) {
										String[] list = t.split(",");

										String effect = list[0];
										int duration = Integer.parseInt(list[2]);
										double chance = Double.parseDouble(list[1]);
										int level = Integer.parseInt(list[3]);

										if (Math.random() <= chance) {
											hit.addPotionEffect(
													new PotionEffect(PotionEffectType.getByName(effect.toUpperCase()),
															duration * 20, level));
											hit.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "MysticalGear"
													+ ChatColor.GRAY + "] " + ChatColor.GREEN + "You have received "
													+ ChatColor.WHITE + effect + " " + (level + 1) + ChatColor.GREEN
													+ " for " + ChatColor.GREEN + duration);
										}
									}
								}
							}
						}
					}
					e.setDamage(calcPvpDamage(reduction, damage));
					return;
				}

				if (!(e.getDamager() instanceof Player) && e.getDamager() instanceof LivingEntity) {
					for (int i = 0; i < is.length; i++) {
						if (!(is[i].getType().equals(Material.AIR))) {
							for (String k : gear) {
								UtopiaArmor armor = new UtopiaArmor(config);
								armor.setArmor(k);

								ItemStack armorStack = armor.createArmor(is[i].getType().name());
								ItemStack wearingArmor = is[i];

								if (wearingArmor.getItemMeta().getLore().equals(armorStack.getItemMeta().getLore())) {
									if (is[i].getType().equals(Material.LEATHER_HELMET)) {
										reduction += 0.01 + 0.12;
									} else if (is[i].getType().equals(Material.LEATHER_CHESTPLATE)) {
										reduction += 0.02 + 0.32;
									} else if (is[i].getType().equals(Material.LEATHER_LEGGINGS)) {
										reduction += 0.02 + 0.24;
									} else if (is[i].getType().equals(Material.LEATHER_BOOTS)) {
										reduction += 0.01 + 0.12;
									}
								}
							}
						}
					}
				}
				e.setDamage(calcPvpDamage(reduction, damage));
				return;
			} else {
				e.setCancelled(true);
				return;
			}
		}

	}

	@SuppressWarnings({ "deprecation" })
	@EventHandler
	public void playerDamage(EntityDamageEvent e) throws NumberFormatException, NotRegisteredException {
		ConfigurationSection gearSec = config.getConfigurationSection("Gear");
		Set<String> gear = gearSec.getKeys(false);

		/*
		 * if (e.getCause() == DamageCause.PROJECTILE) { Player p = (Player)
		 * e.getEntity(); PlayerInventory pInv = p.getInventory();
		 * 
		 * if (e.getEntity() instanceof Arrow) { Arrow arrow = (Arrow) e.getEntity();
		 * 
		 * if (arrow.getShooter() instanceof Player) { Player shooter = (Player)
		 * arrow.getShooter(); PlayerInventory sInv = shooter.getInventory();
		 * ItemStack[] ds = sInv.getArmorContents();
		 * 
		 * for (int i = 0; i < ds.length; i++) { if
		 * (!(ds[i].getType().equals(Material.AIR))) { for (String k : gear) {
		 * UtopiaArmor armor = new UtopiaArmor(config); armor.setArmor(k);
		 * 
		 * ItemStack armorStack = armor.createArmor(ds[i].getType().name()); ItemStack
		 * wearingArmor = ds[i];
		 * 
		 * if
		 * (wearingArmor.getItemMeta().getLore().equals(armorStack.getItemMeta().getLore
		 * ())) { List<String> goodEffects = gearSec.getConfigurationSection(k)
		 * .getStringList("goodeffects"); List<String> badEffects =
		 * gearSec.getConfigurationSection(k) .getStringList("badeffects");
		 * 
		 * LocalPlayer lPlayer = wg.wrapPlayer(p); RegionContainer container =
		 * WorldGuard.getInstance().getPlatform() .getRegionContainer(); RegionManager
		 * rMan = container.get((World) p.getWorld()); RegionQuery query =
		 * container.createQuery(); ApplicableRegionSet arset =
		 * query.getApplicableRegions(lPlayer.getLocation());
		 * 
		 * if (arset.testState(lPlayer, Flags.PVP)) { if (!(goodEffects.size() == 0)) {
		 * for (String t : goodEffects) { String[] list = t.split(",");
		 * 
		 * String effect = list[0]; int duration = Integer.parseInt(list[2]); double
		 * chance = Double.parseDouble(list[1]); int level = Integer.parseInt(list[3]);
		 * 
		 * if (Math.random() <= chance) { shooter.addPotionEffect(new PotionEffect(
		 * PotionEffectType.getByName(effect.toUpperCase()), duration * 20, level));
		 * shooter.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "MysticalGear" +
		 * ChatColor.GRAY + "] " + ChatColor.GREEN + "You have received " +
		 * ChatColor.WHITE + effect + " " + (level + 1) + ChatColor.GREEN + " for " +
		 * ChatColor.GREEN + duration); } } }
		 * 
		 * if (!(badEffects.size() == 0)) { for (String t : badEffects) { String[] list
		 * = t.split(",");
		 * 
		 * String effect = list[0]; int duration = Integer.parseInt(list[2]); double
		 * chance = Double.parseDouble(list[1]); int level = Integer.parseInt(list[3]);
		 * 
		 * if (Math.random() <= chance) { p.addPotionEffect(new PotionEffect(
		 * PotionEffectType.getByName(effect.toUpperCase()), duration * 20, level)); } }
		 * } } } } } } }
		 * 
		 * ItemStack[] is = pInv.getArmorContents();
		 * 
		 * double damage = e.getDamage();
		 * 
		 * double reduction = 0;
		 * 
		 * for (int i = 0; i < is.length; i++) { if
		 * (!(is[i].getType().equals(Material.AIR))) { for (String k : gear) {
		 * UtopiaArmor armor = new UtopiaArmor(config); armor.setArmor(k);
		 * 
		 * ItemStack armorStack = armor.createArmor(is[i].getType().name()); ItemStack
		 * wearingArmor = is[i];
		 * 
		 * double pvpHighest = gearSec.getDouble(k + ".pvphighest"); double pvpLowest =
		 * gearSec.getDouble(k + ".pvplowest");
		 * 
		 * Random rand = new Random(); double random = rand.nextDouble(); double
		 * reductNum = pvpLowest + (random * (pvpHighest - pvpLowest));
		 * 
		 * if
		 * (wearingArmor.getItemMeta().getLore().equals(armorStack.getItemMeta().getLore
		 * ())) { if (is[i].getType().equals(Material.LEATHER_HELMET)) { reduction +=
		 * reductNum + 0.12; } else if
		 * (is[i].getType().equals(Material.LEATHER_CHESTPLATE)) { reduction +=
		 * reductNum + 0.22; } else if
		 * (is[i].getType().equals(Material.LEATHER_LEGGINGS)) { reduction += reductNum
		 * + 0.17; } else if (is[i].getType().equals(Material.LEATHER_BOOTS)) {
		 * reduction += reductNum + 0.12; } } } } } e.setDamage(calcPvpDamage(reduction,
		 * damage)); return; } }
		 */
		if (!(e.getCause() == DamageCause.ENTITY_ATTACK) && !(e.getCause() == DamageCause.PROJECTILE))

		{
			System.out.println("Umm... something is not quite right.");
			if (e.getEntity() instanceof Player) {

				Player p = (Player) e.getEntity();
				PlayerInventory pInv = p.getInventory();
				ItemStack[] is = pInv.getArmorContents();

				double damage = e.getDamage();

				double reduction = 0;

				for (int i = 0; i < is.length; i++) {
					if (!(is[i] == null) && !(is[i].getType().equals(Material.AIR))
							&& (is[i].getType().equals(Material.LEATHER_BOOTS)
									|| is[i].getType().equals(Material.LEATHER_LEGGINGS)
									|| is[i].getType().equals(Material.LEATHER_CHESTPLATE)
									|| is[i].getType().equals(Material.LEATHER_HELMET)))

					{
						for (String k : gear) {
							UtopiaArmor armor = new UtopiaArmor(config);
							armor.setArmor(k);

							ItemStack armorStack = armor.createArmor(is[i].getType().name());
							ItemStack wearingArmor = is[i];

							if (wearingArmor.getItemMeta().getLore().equals(armorStack.getItemMeta().getLore())) {
								if (is[i].getType().equals(Material.LEATHER_HELMET)) {
									reduction += 0.02 + 0.12;
								} else if (is[i].getType().equals(Material.LEATHER_CHESTPLATE)) {
									reduction += 0.03 + 0.32;
								} else if (is[i].getType().equals(Material.LEATHER_LEGGINGS)) {
									reduction += 0.03 + 0.24;
								} else if (is[i].getType().equals(Material.LEATHER_BOOTS)) {
									reduction += 0.02 + 0.12;
								}
							}
						}
					}

				}

				e.setDamage(calcPvpDamage(reduction, damage));
				return;
			}
		}
	}

	public double calcPvpDamage(double reduction, double damage) {
		// if (eap >= 1) {
		// System.out.println(damage);
		// Random rand = new Random();
		//
		// int diff = high - low > 0 ? high - low : 1;
		// System.out.println(diff);
		// int value = rand.nextInt(high) + diff;
		// value += buff;
		// System.out.println(value);
		// damage -= value;
		// System.out.println(buff);
		//
		// if (damage <= 0.0D) {
		// if (Math.random() <= 0.33D) {
		// damage = 1.0D;
		// }
		// }
		// }
		// System.out.println(damage);
		// return damage;
		double amountReduce = (damage * reduction);
		damage -= amountReduce;
		System.out.println(reduction);
		System.out.println(damage);
		return damage;
	}

	public double calcPveDamage(int high, int low, int eap, double damage) {
		if (eap >= 1) {
			Random rand = new Random();
			int value = rand.nextInt(low) + high;
			damage = damage - value;

			if (damage < 0.0D) {
				damage = 0.0D;
				return damage;
			} else if (damage == 0.0D) {
				if (Math.random() <= 0.33D) {
					damage = 1.0D;
					return damage;
				}
			}
		}

		return damage;
	}

	private Plugin loadPlugin(String pluginName) {
		Plugin plugin = core.getServer().getPluginManager().getPlugin(pluginName);
		if (plugin == null) {
			return null;
		}
		return plugin;
	}
}
