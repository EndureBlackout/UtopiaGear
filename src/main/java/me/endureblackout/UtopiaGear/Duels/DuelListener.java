package me.endureblackout.UtopiaGear.Duels;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.endureblackout.UtopiaGear.UtopiaGear;

public class DuelListener implements Listener {
	UtopiaGear core;
	YamlConfiguration arenaConfig;

	public DuelListener(UtopiaGear core, YamlConfiguration arenaConfig) {
		this.core = core;
		this.arenaConfig = arenaConfig;
	}

//	TODO: Possibly implement this way of doing duels.
//	(Player steps on pressure plate and it adds them to the queue).
//	@EventHandler
//	public void platePressEvent(PlayerInteractEvent e) {
//		if (e.getAction() == Action.PHYSICAL) {
//			if (e.getClickedBlock().getType().equals(Material.STONE_PRESSURE_PLATE)) {
//				Location pLoc = e.getPlayer().getLocation();
//				int pX = pLoc.getBlockX();
//				int pY = pLoc.getBlockY();
//				int pZ = pLoc.getBlockZ();
//				
//				System.out.println(pX);
//
//				for (String sect : arenaConfig.getKeys(true)) {
//					System.out.println(sect);
//					ConfigurationSection configSect = arenaConfig.getConfigurationSection(sect);
//
//					Location wait1 = getWait(configSect, "wait1");
//					Location wait2 = getWait(configSect, "wait2");
//
//					if (wait1 != null && pX == wait1.getX() && pY == wait1.getY() && pZ == wait1.getZ()) {
//						if (wait2 != null) {
//							for (Entry<Player, Location> waiter : waitingPlayers.entrySet()) {
//								if ((waiter.getValue().getBlockX() == wait1.getBlockX()
//										&& waiter.getValue().getBlockY() == wait1.getBlockY()
//										&& waiter.getValue().getBlockZ() == wait1.getBlockZ())
//										|| (waiter.getValue().getBlockX() == wait2.getBlockX()
//												&& waiter.getValue().getBlockY() == wait2.getBlockY()
//												&& waiter.getValue().getBlockZ() == wait2.getBlockZ())) {
//									Duel duel = new Duel(e.getPlayer(), waiter.getKey(), core);
//									duel.setupDuel();
//									activeDuels.add(duel);
//									return;
//								}
//							}
//
//							if (pX == wait2.getBlockX() && pY == wait2.getBlockY() && pZ == wait2.getBlockZ()) {
//								waitingPlayers.put(e.getPlayer(), wait2);
//								e.getPlayer()
//										.sendMessage(ChatColor.RED + "You are waiting to duel with another person.");
//								return;
//							}
//						}
//
//						waitingPlayers.put(e.getPlayer(), wait1);
//						e.getPlayer().sendMessage(ChatColor.RED + "You are waiting to duel with another person.");
//					}
//				}
//			}
//		}
//	}
	
	@EventHandler
	public void onDuelerDeath(PlayerDeathEvent e) {
		if(DuelCommandHandler.activeDuels.size() == 1) {
			Duel duel = DuelCommandHandler.activeDuels.get(0);
			
			if(e.getEntity().equals(duel.getFirstPlayer())) {
				Player secondPlayer = duel.getSecondPlayer();
				secondPlayer.sendMessage(ChatColor.GREEN + "You have won the duel! Quick, collect your items, you have 20 seconds!");
				
				DuelCommandHandler.activeDuels.clear();
			} else if (e.getEntity().equals(duel.getSecondPlayer())) {
				Player firstPlayer = duel.getFirstPlayer();
				firstPlayer.sendMessage(ChatColor.GREEN + "You have won the duel! Quick, collect your items, you have 20 seconds!");
				
				DuelCommandHandler.activeDuels.clear();
			}
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (DuelCommandHandler.CreatingArena.containsKey(e.getPlayer())) {
			Player p = e.getPlayer();

			switch (e.getMessage()) {
			case "wait1":
				e.setCancelled(true);
				DuelCommandHandler.CreatingArena.get(p).setWaitPos1(p.getLocation());
				p.sendMessage(ChatColor.GREEN + "You have set the first wait point.");
				break;
			case "wait2":
				e.setCancelled(true);
				DuelCommandHandler.CreatingArena.get(p).setWaitPos2(p.getLocation());
				p.sendMessage(ChatColor.GREEN + "You have set the second wait point.");
				break;
			case "spawn1":
				e.setCancelled(true);
				DuelCommandHandler.CreatingArena.get(p).setSpawnPoint1(p.getLocation());
				p.sendMessage(ChatColor.GREEN + "You have set the first spawn point.");
				break;
			case "spawn2":
				e.setCancelled(true);
				DuelCommandHandler.CreatingArena.get(p).setSpawnPoint2(p.getLocation());
				p.sendMessage(ChatColor.GREEN + "You have set the second spawn point.");
				break;
			case "home":
				e.setCancelled(true);
				DuelCommandHandler.CreatingArena.get(p).setHomeLocation(p.getLocation());
				p.sendMessage(ChatColor.GREEN + "You have set the home location.");
				break;
			case "finish":
				e.setCancelled(true);
				DuelCommandHandler.CreatingArena.get(p).saveArena();
				DuelCommandHandler.CreatingArena.remove(p);
				p.sendMessage(ChatColor.GREEN + "You have finished creating your duel arena.");
				break;
			default:
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "Please finish creating the arena before using commands or chatting");
			}
		}
	}
}
