package me.endureblackout.UtopiaGear.Duels;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class DuelArena {
	private Location waitPos1;
	private Location waitPos2;
	private Location spawnPoint1;
	private Location spawnPoint2;
	private File dataFolder = Bukkit.getPluginManager().getPlugin("MysticalGear").getDataFolder();
	
	public DuelArena(Location wait1, Location wait2, Location spawnPoint1, Location spawnPoint2) {
		this.setWaitPos1(wait1);
		this.setWaitPos2(wait2);
		this.setSpawnPoint1(spawnPoint1);
		this.setSpawnPoint2(spawnPoint2);
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
	
	public void saveArena() {
		String uid = UUID.randomUUID().toString();
		File arenaFile = new File(dataFolder, "arenas.yml");
		
		if(!arenaFile.exists()) {
			YamlConfiguration arenasYaml = YamlConfiguration.loadConfiguration(arenaFile);
			
			arenasYaml.createSection("Arenas");
			arenasYaml.createSection(uid);
			arenasYaml.set(uid + ".wait1.x", this.waitPos1.getX());
			arenasYaml.set(uid + ".wait1.y", this.waitPos1.getY());
			arenasYaml.set(uid + ".wait1.z", this.waitPos1.getZ());
			
			arenasYaml.set(uid + ".wait2.x", this.waitPos2.getX());
			arenasYaml.set(uid + ".wait2.y", this.waitPos2.getY());
			arenasYaml.set(uid + ".wait2.z", this.waitPos2.getZ());
			
			arenasYaml.set(uid + ".wait1.x", this.spawnPoint1.getX());
			arenasYaml.set(uid + ".wait1.y", this.spawnPoint1.getY());
			arenasYaml.set(uid + ".wait1.z", this.spawnPoint1.getZ());
			
			arenasYaml.set(uid + ".wait1.x", this.spawnPoint2.getX());
			arenasYaml.set(uid + ".wait1.y", this.spawnPoint2.getY());
			arenasYaml.set(uid + ".wait1.z", this.spawnPoint2.getZ());
			
			try {
				arenasYaml.save(arenaFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			YamlConfiguration arenasYaml = YamlConfiguration.loadConfiguration(arenaFile);
			
			arenasYaml.createSection(uid);
			arenasYaml.set(uid + ".wait1.x", this.waitPos1.getX());
			arenasYaml.set(uid + ".wait1.y", this.waitPos1.getY());
			arenasYaml.set(uid + ".wait1.z", this.waitPos1.getZ());
			
			arenasYaml.set(uid + ".wait2.x", this.waitPos2.getX());
			arenasYaml.set(uid + ".wait2.y", this.waitPos2.getY());
			arenasYaml.set(uid + ".wait2.z", this.waitPos2.getZ());
			
			arenasYaml.set(uid + ".wait1.x", this.spawnPoint1.getX());
			arenasYaml.set(uid + ".wait1.y", this.spawnPoint1.getY());
			arenasYaml.set(uid + ".wait1.z", this.spawnPoint1.getZ());
			
			arenasYaml.set(uid + ".wait1.x", this.spawnPoint2.getX());
			arenasYaml.set(uid + ".wait1.y", this.spawnPoint2.getY());
			arenasYaml.set(uid + ".wait1.z", this.spawnPoint2.getZ());
			
			try {
				arenasYaml.save(arenaFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
