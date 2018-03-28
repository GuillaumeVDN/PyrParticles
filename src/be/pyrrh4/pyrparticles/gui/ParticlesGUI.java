package be.pyrrh4.pyrparticles.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.gui.ItemData;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;

public class ParticlesGUI extends GUI {

	// fields and constructor
	public ParticlesGUI(Player player) {
		super(PyrParticles.instance(), PyrParticles.instance().getLocale().getMessage("gui_particles_name").getLines().get(0), 27, 26, false, true, DuplicateTolerance.DISALLOW);
		// preload
		int slot = -1;
		for (ParticleEffect effect : ParticleEffect.values()) {
			addItem(ItemData.create("particle_" + effect.toString(), ++slot, -1, effect.getGuiItemType(), effect.getGuiItemData(), 1, "§6" + Utils.capitalizeFirstLetter(effect.getName()), PyrParticles.instance().getLocale().getMessage(effect.hasPermission(player) ? "unlocked" : "locked").getLines()));
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
		if (clickedData.getId().startsWith("particle_")) {
			ParticleEffect effect = Utils.valueOfOrNull(ParticleEffect.class, clickedData.getId().substring(9));
			if (effect != null) {
				effect.start(player);
				player.closeInventory();
			}
		}
		// clicked on previous
		else if (clickedData.getId().equals(MainGUI.ITEM_PREVIOUS.getId())) {
			PyrParticles.instance().getMainGUI().open(player, false);
		}
	}

}
