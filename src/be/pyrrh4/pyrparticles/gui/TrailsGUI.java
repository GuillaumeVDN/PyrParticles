package be.pyrrh4.pyrparticles.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.gui.ItemData;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.trail.Trail;

public class TrailsGUI extends GUI {

	// fields and constructor
	public TrailsGUI(Player player) {
		super(PyrParticles.instance(), PyrParticles.instance().getLocale().getMessage("gui_trails_name").getLines().get(0), 27, 26, false, true, DuplicateTolerance.DISALLOW);
		// preload
		int slot = -1;
		for (Trail trail : Trail.values()) {
			addItem(ItemData.create("trail_" + trail.toString(), ++slot, -1, trail.getGuiItemType(), trail.getGuiItemData(), 1, "§6" + Utils.capitalizeFirstLetter(trail.getName()), PyrParticles.instance().getLocale().getMessage(trail.hasPermission(player) ? "unlocked" : "locked").getLines()));
		}
		addItem(MainGUI.ITEM_PREVIOUS);
	}

	// on click
	@Override
	protected void onClick(InventoryClickEvent event, Player player, InventoryClickType clickType, ItemStack clickedStack, ItemData clickedData, ItemStack cursorStack, ItemData cursorData) {
		if (clickedData == null || clickedData.getId() == null) {
			return;
		}
		// clicked on particles
		if (clickedData.getId().startsWith("trail_")) {
			Trail trail = Utils.valueOfOrNull(Trail.class, clickedData.getId().substring(6));
			if (trail != null) {
				trail.start(player);
				player.closeInventory();
			}
		}
		// clicked on previous
		else if (clickedData.getId().equals(MainGUI.ITEM_PREVIOUS.getId())) {
			PyrParticles.instance().getMainGUI().open(player, false);
		}
	}

}
