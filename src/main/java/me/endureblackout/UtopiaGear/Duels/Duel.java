package me.endureblackout.UtopiaGear.Duels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Duel {
	private Player firstPlayer;
	private Player secondPlayer;
	private DuelArena duelArena;

	protected final Plugin plugin;

	public Duel(Player firstPlayer, Player secondPlayer, Plugin plugin) {
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		this.plugin = plugin;
	}

	public void setFirstPlayer(Player p) {
		this.firstPlayer = p;
	}

	public void setSecondPlayer(Player p) {
		this.secondPlayer = p;
	}

	public Player getFirstPlayer() {
		return this.firstPlayer;
	}

	public Player getSecondPlayer() {
		return this.secondPlayer;
	}

	public DuelArena getArena() {
		return this.duelArena;
	}

	public void setArena(DuelArena arena) {
		this.duelArena = arena;
	}

	public boolean firstPlayerSet() {
		return firstPlayer != null ? true : false;
	}

	public boolean secondPlayerSet() {
		return secondPlayer != null ? true : false;
	}

	public void setupDuel() {
		if (firstPlayerSet() && secondPlayerSet()) {
			firstPlayer.sendMessage(ChatColor.GREEN + "The duel will start in 10 seconds!");
			secondPlayer.sendMessage(ChatColor.GREEN + "The duel will start in 10 seconds!");

			new Countdown(10, plugin) {

				@Override
				public void count(int current) {
					if (current <= 5) {
						firstPlayer.sendMessage(ChatColor.GREEN + "Starting in " + current);
						secondPlayer.sendMessage(ChatColor.GREEN + "Starting in " + current);
					}
					else if (current <= 0) {
						firstPlayer.teleport(duelArena.getSpawnPoint1());
						secondPlayer.teleport(duelArena.getSpawnPoint2());
						
						firstPlayer.sendMessage(ChatColor.DARK_GREEN + "FIGHT!!!!");
						secondPlayer.sendMessage(ChatColor.DARK_GREEN + "FIGHT!!!!");
					}
				}

			}.start();
		} else {
			if (firstPlayerSet()) {
				firstPlayer.sendMessage(
						ChatColor.RED + "There is no second player to duel with sending you back to spawn.");
				firstPlayer.teleport(Bukkit.getWorld("Spawn").getSpawnLocation());
			} else {
				secondPlayer.sendMessage(
						ChatColor.RED + "There is no second player to duel with sending you back to spawn.");
				secondPlayer.teleport(Bukkit.getWorld("Spawn").getSpawnLocation());
			}
		}
	}
}
