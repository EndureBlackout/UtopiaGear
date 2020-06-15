package me.endureblackout.UtopiaGear.Duels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.endureblackout.UtopiaGear.UtopiaGear;

public class DuelCommandHandler implements CommandExecutor, Listener {
	UtopiaGear core;
	YamlConfiguration config;
	YamlConfiguration arenaConfig;

	public static HashMap<Player, DuelArena> CreatingArena = new HashMap<Player, DuelArena>();
	public static List<Player> waitingPlayers = new ArrayList<>();
	public static List<Duel> activeDuels = new ArrayList<>();

	public DuelCommandHandler(UtopiaGear core, YamlConfiguration config, YamlConfiguration arenaConfig) {
		this.core = core;
		this.config = config;
		this.arenaConfig = arenaConfig;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("duel")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("create")) {
						if (CreatingArena.containsKey(p)) {
							p.sendMessage(ChatColor.RED + "You are already creating an arena.");
						} else {
							CreatingArena.put(p, new DuelArena());
							p.sendMessage(ChatColor.GREEN + "You are now creating an arena");
						}
					}
					
					if(args[0].equalsIgnoreCase("leave")) {
						if(waitingPlayers.size() == 1 && waitingPlayers.contains(p)) {
							waitingPlayers.clear();
							
							p.sendMessage(ChatColor.GREEN + "You have left the duel queue.");
						}
					}
				}

				if (args.length == 0) {
					if(activeDuels.size() > 0) {
						p.sendMessage(ChatColor.RED + "You must wait until the current duel is done to queue for a duel.");
					}
					
					if(waitingPlayers.contains(p)) {
						p.sendMessage(ChatColor.RED + "You cannot duel yourself.");
					}
					
					if (waitingPlayers.size() == 1 && !waitingPlayers.contains(p) && activeDuels.size() == 0) {
						for (String sect : arenaConfig.getKeys(false)) {
							Player waiter = waitingPlayers.get(0);
							waitingPlayers.clear();

							ConfigurationSection arenaSect = arenaConfig.getConfigurationSection(sect);

							Location wait2 = getWait(arenaSect, "wait2");

							p.teleport(wait2);

							waiter.sendMessage(ChatColor.GREEN + "You are now dueling with " + p.getName());
							p.sendMessage(ChatColor.GREEN + "You are now dueling with " + waiter.getName());

							Duel duel = new Duel(waiter, p, getDuelArena(sect),this.core);
							activeDuels.add(duel);

							duel.setupDuel();
						}
					} else if (waitingPlayers.size() == 0 && activeDuels.size() == 0) {
						waitingPlayers.add(p);

						for (String sect : arenaConfig.getKeys(false)) {
							ConfigurationSection arenaSect = arenaConfig.getConfigurationSection(sect);

							Location wait1 = getWait(arenaSect, "wait1");

							p.teleport(wait1);
							p.sendMessage(ChatColor.RED + "You are now waiting for someone to duel with you.");
						}
					}
				}
			}
		}

		return true;
	}

	public Location getWait(ConfigurationSection configSect, String specific) {
		Location loc = null;

		try {
			String world = configSect.getString("world");
			int x = configSect.getInt(specific + ".x");
			int y = configSect.getInt(specific + ".y");
			int z = configSect.getInt(specific + ".z");

			loc = new Location(core.getServer().getWorld(world), x, y, z);

			return loc;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public DuelArena getDuelArena(String uid) {
		ConfigurationSection arenaSect = arenaConfig.getConfigurationSection(uid);
		
		World world = Bukkit.getWorld(arenaSect.getString("world"));
		
		int s1X = arenaSect.getInt("spawn1.x");
		int s1Y = arenaSect.getInt("spawn1.y");
		int s1Z = arenaSect.getInt("spawn1.z");
		Location spawn1 = new Location(world, s1X, s1Y, s1Z);
		
		int s2X = arenaSect.getInt("spawn2.x");
		int s2Y = arenaSect.getInt("spawn2.y");
		int s2Z = arenaSect.getInt("spawn2.z");
		Location spawn2 = new Location(world, s2X, s2Y, s2Z);
		
		return new DuelArena(spawn1, spawn2, core);
	}
}
