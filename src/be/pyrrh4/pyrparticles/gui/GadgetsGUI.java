package be.pyrrh4.pyrparticles.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.gui.ItemData;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.gadget.Gadget;

public class GadgetsGUI extends GUI {

	// fields and constructor
	public GadgetsGUI(Player player) {
		super(PyrParticles.instance(), PyrParticles.instance().getLocale().getMessage("gui_gadgets_name").getLines().get(0), 27, 26, false, true, DuplicateTolerance.DISALLOW);
		// preload
		int slot = -1;
		for (Gadget gadget : Gadget.values()) {
			addItem(ItemData.create("gadget_" + gadget.toString(), ++slot, -1, gadget.getGuiItemType(), gadget.getGuiItemData(), 1, "§6" + Utils.capitalizeFirstLetter(gadget.getName()), PyrParticles.instance().getLocale().getMessage(gadget.hasPermission(player) ? "unlocked" : "locked").getLines()));
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
		if (clickedData.getId().startsWith("gadget_")) {
			Gadget gadget = Utils.valueOfOrNull(Gadget.class, clickedData.getId().substring(7));
			if (gadget != null) {
				gadget.startOrGiveItem(player);
				player.closeInventory();
			}
		}
		// clicked on previous
		else if (clickedData.getId().equals(MainGUI.ITEM_PREVIOUS.getId())) {
			PyrParticles.instance().getMainGUI().open(player, false);
		}
	}

}
