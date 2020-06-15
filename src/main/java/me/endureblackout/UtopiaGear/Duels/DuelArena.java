package me.endureblackout.UtopiaGear.Duels;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import me.endureblackout.UtopiaGear.UtopiaGear;

public class DuelArena {
	UtopiaGear core;
	private Location waitPos1 = null;
	private Location waitPos2 = null;
	private Location spawnPoint1 = null;
	private Location spawnPoint2 = null;
	private Location homeLocation = null;
	private File dataFolder = Bukkit.getPluginManager().getPlugin("MysticalGear").getDataFolder();
	
	public DuelArena(UtopiaGear core, Location wait1, Location wait2, Location spawnPoint1, Location spawnPoint2, Location homeLocation) {
		this.core = core;
		this.waitPos1 = wait1;
		this.waitPos2 = wait2;
		this.spawnPoint1 = spawnPoint1;
		this.spawnPoint2 = spawnPoint2;
		this.setHomeLocation(homeLocation);
	}

	public DuelArena() { }
	
	public DuelArena(Location spawn1, Location spawn2, UtopiaGear core) {
		this.spawnPoint1 = spawn1;
		this.spawnPoint2 = spawn2;
		this.core = core;
	}
	
	public Location getWaitPos1() {
		return waitPos1;
	}

	public void setWaitPos1(Location waitPos1) {
		this.waitPos1 = waitPos1;
	}

	public Location getWaitPos2() {
		return waitPos2;
	}

	public void setWaitPos2(Location waitPos2) {
		this.waitPos2 = waitPos2;
	}

	public Location getSpawnPoint1() {
		return spawnPoint1;
	}

	public void setSpawnPoint1(Location spawnPoint1) {
		this.spawnPoint1 = spawnPoint1;
	}

	public Location getSpawnPoint2() {
		return spawnPoint2;
	}

	public void setSpawnPoint2(Location spawnPoint2) {
		this.spawnPoint2 = spawnPoint2;
	}
	
	public Location getHomeLocation() {
		return homeLocation;
	}

	public void setHomeLocation(Location homeLocation) {
		this.homeLocation = homeLocation;
	}
	
	public void saveArena() {
		String uid = UUID.randomUUID().toString();
		File arenaFile = new File(dataFolder, "arenas.yml");
		
		if(!arenaFile.exists()) {
			YamlConfiguration arenasYaml = YamlConfiguration.loadConfiguration(arenaFile);
			
			arenasYaml.createSection("Arenas");
			arenasYaml.createSection(uid);
			
			arenasYaml.set(uid + ".world", this.waitPos1.getWorld().getName());
			
			arenasYaml.set(uid + ".wait1.x", this.waitPos1.getBlockX());
			arenasYaml.set(uid + ".wait1.y", this.waitPos1.getBlockY());
			arenasYaml.set(uid + ".wait1.z", this.waitPos1.getBlockZ());
			
			arenasYaml.set(uid + ".wait2.x", this.waitPos2.getBlockX());
			arenasYaml.set(uid + ".wait2.y", this.waitPos2.getBlockY());
			arenasYaml.set(uid + ".wait2.z", this.waitPos2.getBlockZ());
			
			arenasYaml.set(uid + ".spawn1.x", this.spawnPoint1.getBlockX());
			arenasYaml.set(uid + ".spawn1.y", this.spawnPoint1.getBlockY());
			arenasYaml.set(uid + ".spawn1.z", this.spawnPoint1.getBlockZ());
			
			arenasYaml.set(uid + ".spawn2.x", this.spawnPoint2.getBlockX());
			arenasYaml.set(uid + ".spawn2.y", this.spawnPoint2.getBlockY());
			arenasYaml.set(uid + ".spawn2.z", this.spawnPoint2.getBlockZ());
			
			arenasYaml.set(uid + ".home.x", this.homeLocation.getBlockX());
			arenasYaml.set(uid + ".home.y", this.homeLocation.getBlockY());
			arenasYaml.set(uid + ".home.z", this.homeLocation.getBlockZ());
			
			try {
				arenasYaml.save(arenaFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			YamlConfiguration arenasYaml = YamlConfiguration.loadConfiguration(arenaFile);
			
			arenasYaml.createSection(uid);
			
			arenasYaml.set(uid + ".world", this.waitPos1.getWorld().getName());
			
			arenasYaml.set(uid + ".wait1.x", this.waitPos1.getBlockX());
			arenasYaml.set(uid + ".wait1.y", this.waitPos1.getBlockY());
			arenasYaml.set(uid + ".wait1.z", this.waitPos1.getBlockZ());
			
			arenasYaml.set(uid + ".wait2.x", this.waitPos2.getBlockX());
			arenasYaml.set(uid + ".wait2.y", this.waitPos2.getBlockY());
			arenasYaml.set(uid + ".wait2.z", this.waitPos2.getBlockZ());
			
			arenasYaml.set(uid + ".spawn1.x", this.spawnPoint1.getBlockX());
			arenasYaml.set(uid + ".spawn1.y", this.spawnPoint1.getBlockY());
			arenasYaml.set(uid + ".spawn1.z", this.spawnPoint1.getBlockZ());
			
			arenasYaml.set(uid + ".spawn2.x", this.spawnPoint2.getBlockX());
			arenasYaml.set(uid + ".spawn2.y", this.spawnPoint2.getBlockY());
			arenasYaml.set(uid + ".spawn2.z", this.spawnPoint2.getBlockZ());
			
			arenasYaml.set(uid + ".home.x", this.homeLocation.getBlockX());
			arenasYaml.set(uid + ".home.y", this.homeLocation.getBlockY());
			arenasYaml.set(uid + ".home.z", this.homeLocation.getBlockZ());
			
			try {
				arenasYaml.save(arenaFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
