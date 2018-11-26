package be.pyrrh4.pyrparticles.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import be.pyrrh4.core.gui.ClickeableItem;
import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.gui.ItemData;
import be.pyrrh4.core.messenger.Locale;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.trail.Trail;

public class TrailsGUI extends GUI {

	// fields and constructor
	public TrailsGUI(Player player) {
		super(PyrParticles.instance(), Locale.GUI_PYRPARTICLES_TRAILSNAME.getActive().getLine(), 27, 26);
		// preload
		int slot = -1;
		for (final Trail trail : Trail.values()) {
			setRegularItem(new ClickeableItem(new ItemData("trail_" + trail.toString(), ++slot, trail.getGuiItemType(), 1, "§6" + Utils.capitalizeFirstLetter(trail.getName()), Utils.valueOfOrNull(Locale.class, "MISC_PYRPARTICLES_" + (trail.hasPermission(player) ? "unlocked" : "locked")).getActive().getLines())) {
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
				PyrParticles.instance().getMainGUI().open(player);
				return true;
			}
		});
	}

}
