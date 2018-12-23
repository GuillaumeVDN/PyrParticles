package be.pyrrh4.pyrparticles.gui;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import be.pyrrh4.pyrcore.lib.gui.ClickeableItem;
import be.pyrrh4.pyrcore.lib.gui.GUI;
import be.pyrrh4.pyrcore.lib.gui.ItemData;
import be.pyrrh4.pyrcore.lib.messenger.Text;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PPLocale;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.trail.Trail;

public class TrailsGUI extends GUI {

	// fields and constructor
	public TrailsGUI(Player player) {
		super(PyrParticles.inst(), PPLocale.GUI_PYRPARTICLES_TRAILSNAME.getLine(), 27, 26);
		// preload
		int slot = -1;
		for (final Trail trail : Trail.values()) {
			Text text = Text.valueOf("MISC_PYRPARTICLES_" + (trail.hasPermission(player) ? "UNLOCKED" : "LOCKED"));
			List<String> lore = text == null ? Utils.asList(trail.hasPermission(player) ? "UNLOCKED" : "LOCKED") : text.getLines();
			setRegularItem(new ClickeableItem(new ItemData("trail_" + trail.toString(), ++slot, trail.getGuiItemType(), 1, "§6" + Utils.capitalizeFirstLetter(trail.getName()), lore)) {
				@Override
				public boolean onClick(Player player, ClickType clickType, GUI gui, int pageIndex) {
					trail.start(player);
					player.closeInventory();
					return true;
				}
			});
		}
		// item previous
		setPersistentItem(new ClickeableItem(MainGUI.ITEM_PREVIOUS) {
			@Override
			public boolean onClick(Player player, ClickType clickType, GUI gui, int pageIndex) {
				PyrParticles.inst().getMainGUI().open(player);
				return true;
			}
		});
	}

}
