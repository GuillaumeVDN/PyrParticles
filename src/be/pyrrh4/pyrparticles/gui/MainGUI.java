package be.pyrrh4.pyrparticles.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.gui.ItemData;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;
import be.pyrrh4.pyrparticles.trail.Trail;

public class MainGUI extends GUI {

	// static fields
	public static final ItemData ITEM_PREVIOUS = ItemData.create("previous_menu", 26, -1, Material.REDSTONE_TORCH_ON, 0, 1, "§7Previous menu", null);

	// fields and constructor
	public MainGUI() {
		super(PyrParticles.instance(), PyrParticles.instance().getLocale().getMessage("gui_main_name").getLines().get(0), 27, 26, false, false, DuplicateTolerance.DISALLOW);
		// preload
		addItem(getMenuItem("particles", 10, Material.BLAZE_POWDER, 0, "gui_main_particles_name", "gui_main_particles_lore"));
		addItem(getMenuItem("trails", 11, Material.INK_SACK, 15, "gui_main_trails_name", "gui_main_trails_lore"));
		addItem(getMenuItem("gadgets", 12, Material.NETHER_STAR, 0, "gui_main_gadgets_name", "gui_main_gadgets_lore"));
		addItem(ItemData.create("particles_clear", 15, -1, Material.WOOD_DOOR, 0, 1, PyrParticles.instance().getLocale().getMessage("gui_main_particles_clear_name").getLines().get(0), null));
		addItem(ItemData.create("trail_clear", 16, -1, Material.IRON_DOOR, 0, 1, PyrParticles.instance().getLocale().getMessage("gui_main_trail_clear_name").getLines().get(0), null));
	}

	private ItemData getMenuItem(String id, int slot, Material type, int data, String name, String lore) {
		return ItemData.create(id, slot, -1, type, data, 1, PyrParticles.instance().getLocale().getMessage(name).getLines().get(0), PyrParticles.instance().getLocale().getMessage(lore).getLines());
	}

	// on click
	@Override
	protected void onClick(InventoryClickEvent event, Player player, InventoryClickType clickType, ItemStack clickedStack, ItemData clickedData, ItemStack cursorStack, ItemData cursorData) {
		if (clickedData == null || clickedData.getId() == null) {
			return;
		}
		// clicked on particles
		if (clickedData.getId().equals("particles")) {
			new ParticlesGUI(player).open(player, false);
		}
		// clicked on trails
		else if (clickedData.getId().equals("trails")) {
			new TrailsGUI(player).open(player, false);
		}
		// clicked on gadgets
		else if (clickedData.getId().equals("gadgets")) {
			new GadgetsGUI(player).open(player, false);
		}
		// clicked on clear particles
		else if (clickedData.getId().equals("particles_clear")) {
			ParticleEffect.stop(player);
			player.closeInventory();
		}
		// clicked on clear trail
		else if (clickedData.getId().equals("trail_clear")) {
			Trail.stop(player);
			player.closeInventory();
		}
	}

}
