package be.pyrrh4.pyrparticles.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import be.pyrrh4.core.gui.ClickTolerance;
import be.pyrrh4.core.gui.ClickeableItem;
import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.gui.ItemData;
import be.pyrrh4.core.messenger.Locale;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.gadget.Gadget;

public class GadgetsGUI extends GUI {

	// fields and constructor
	public GadgetsGUI(Player player) {
		super(PyrParticles.instance(), Locale.GUI_PYRPARTICLES_GADGETSNAME.getActive().getLine(), 27, 26, true, ClickTolerance.ONLY_TOP);
		// preload
		int slot = -1;
		for (final Gadget gadget : Gadget.values()) {
			setRegularItem(new ClickeableItem(new ItemData("gadget_" + gadget.toString(), ++slot, gadget.getGuiItemType(), 1, "§6" + Utils.capitalizeFirstLetter(gadget.getName()), Utils.valueOfOrNull(Locale.class, "MISC_PYRPARTICLES_" + (gadget.hasPermission(player) ? "UNLOCKED" : "LOCKED")).getActive().getLines())) {
				@Override
				public boolean onClick(Player player, ClickType clickType, GUI gui, int pageIndex) {
					gadget.startOrGiveItem(player);
					player.closeInventory();
					return true;
				}
			});
		}
		// item previous
		setPersistentItem(new ClickeableItem(MainGUI.ITEM_PREVIOUS) {
			@Override
			public boolean onClick(Player player, ClickType clickType, GUI gui, int pageIndex) {
				PyrParticles.instance().getMainGUI().open(player);
				return true;
			}
		});
	}

}
