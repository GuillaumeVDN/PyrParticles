package be.pyrrh4.pyrparticles.gui;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import be.pyrrh4.pyrcore.lib.gui.ClickTolerance;
import be.pyrrh4.pyrcore.lib.gui.ClickeableItem;
import be.pyrrh4.pyrcore.lib.gui.GUI;
import be.pyrrh4.pyrcore.lib.gui.ItemData;
import be.pyrrh4.pyrcore.lib.messenger.Text;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PPLocale;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.gadget.Gadget;

public class GadgetsGUI extends GUI {

	// fields and constructor
	public GadgetsGUI(Player player) {
		super(PyrParticles.inst(), PPLocale.GUI_PYRPARTICLES_GADGETSNAME.getLine(), 27, 26, true, ClickTolerance.ONLY_TOP);
		// preload
		int slot = -1;
		for (final Gadget gadget : Gadget.values()) {
			Text text = Text.valueOf("MISC_PYRPARTICLES_" + (gadget.hasPermission(player) ? "UNLOCKED" : "LOCKED"));
			List<String> lore = text == null ? Utils.asList(gadget.hasPermission(player) ? "UNLOCKED" : "LOCKED") : text.getLines();
			setRegularItem(new ClickeableItem(new ItemData("gadget_" + gadget.toString(), ++slot, gadget.getGuiItemType(), 1, "§6" + Utils.capitalizeFirstLetter(gadget.getName()), lore)) {
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
				new MainGUI().open(player);
				return true;
			}
		});
	}

}
