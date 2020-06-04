package me.endureblackout.UtopiaGear;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

public class TridentEnchantHandler implements Listener {
	UtopiaGear core;
	
	public TridentEnchantHandler(UtopiaGear core) {
		this.core = core;
	}
	
	@EventHandler
	public void onTridenEnchant(EnchantItemEvent e) {
		ItemStack item = e.getItem();
		
		if(item.getType().equals(Material.TRIDENT)) {
			e.setCancelled(true);
			
			e.getEnchanter().sendMessage(ChatColor.RED + "Sorry, but you are not allowed to enchant this!");
		}
	}
}
