package me.endureblackout.UtopiaGear;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;

public class StopBrewEvent implements Listener {
	UtopiaGear core;
	
	public StopBrewEvent(UtopiaGear core) {
		this.core = core;
	}
	
	public void onBrewEvent(BrewEvent e) {
		BrewerInventory bi = e.getContents();
		
		if(bi.contains(Material.BLAZE_POWDER)) {
			for(int i = 0; i < bi.getSize(); i++) {
				if(bi.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("Awkward Potion")) {
					e.setCancelled(true);
					e.getContents().clear(i);
				}
			}
		}
	}
}
