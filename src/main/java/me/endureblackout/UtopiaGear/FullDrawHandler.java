package me.endureblackout.UtopiaGear;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class FullDrawHandler implements Listener {
	UtopiaGear core;
	
	public FullDrawHandler(UtopiaGear core) {
		this.core = core;
	}
	
	@EventHandler
	public void onShootEvent(EntityShootBowEvent e) {
		if(e.getForce() != 1.0 && e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "Bow must be pulled fully back before you can shoot!");
		}
	}
}
