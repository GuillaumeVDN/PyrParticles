package be.pyrrh4.pyrparticles.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import be.pyrrh4.core.gui.ClickeableItem;
import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.gui.ItemData;
import be.pyrrh4.core.messenger.Locale;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;

public class ParticlesGUI extends GUI {

	// fields and constructor
	public ParticlesGUI(Player player) {
		super(PyrParticles.instance(), Locale.GUI_PYRPARTICLES_PARTICLESNAME.getActive().getLine(), 27, 26);
		// preload
		int slot = -1;
		for (final ParticleEffect effect : ParticleEffect.values()) {
			setRegularItem(new ClickeableItem(new ItemData("particle_" + effect.toString(), ++slot, effect.getGuiItemType(), 1, "§6" + Utils.capitalizeFirstLetter(effect.getName()), Utils.valueOfOrNull(Locale.class, "MISC_PYRPARTICLES_" + (effect.hasPermission(player) ? "UNLOCKED" : "LOCKED")).getActive().getLines())) {
				@Override
				public boolean onClick(Player player, ClickType clickType, GUI gui, int pageIndex) {
					effect.start(player);
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
