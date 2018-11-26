package be.pyrrh4.pyrparticles.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import be.pyrrh4.core.gui.ClickeableItem;
import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.gui.ItemData;
import be.pyrrh4.core.material.Mat;
import be.pyrrh4.core.messenger.Locale;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;
import be.pyrrh4.pyrparticles.trail.Trail;

public class MainGUI extends GUI {

	// static fields
	public static final ItemData ITEM_PREVIOUS = new ItemData("previous_menu", 26, Mat.REDSTONE_TORCH, 1, "§7Previous menu", null);

	// fields and constructor
	public MainGUI() {
		super(PyrParticles.instance(), Locale.GUI_PYRPARTICLES_MAINNAME.getActive().getLine(), 27, 26);
		// preload
		setRegularItem(new ClickeableItem(getMenuItem("particles", 10, Mat.BLAZE_POWDER, Locale.GUI_PYRPARTICLES_PARTICLESNAME, Locale.GUI_PYRPARTICLES_MAINPARTICLESLORE)) {
			@Override
			public boolean onClick(Player player, ClickType clickType, GUI gui, int pageIndex) {
				new ParticlesGUI(player).open(player);
				return true;
			}
		});
		setRegularItem(new ClickeableItem(getMenuItem("trails", 11, Mat.BONE_MEAL, Locale.GUI_PYRPARTICLES_MAINTRAILSNAME, Locale.GUI_PYRPARTICLES_MAINTRAILSLORE)) {
			@Override
			public boolean onClick(Player player, ClickType clickType, GUI gui, int pageIndex) {
				new TrailsGUI(player).open(player);
				return true;
			}
		});
		setRegularItem(new ClickeableItem(getMenuItem("gadgets", 12, Mat.NETHER_STAR, Locale.GUI_PYRPARTICLES_MAINGADGETSNAME, Locale.GUI_PYRPARTICLES_MAINGADGETSLORE)) {
			@Override
			public boolean onClick(Player player, ClickType clickType, GUI gui, int pageIndex) {
				new GadgetsGUI(player).open(player);
				return true;
			}
		});
		setRegularItem(new ClickeableItem(new ItemData("particles_clear", 15, Mat.OAK_DOOR, 1, Locale.GUI_PYRPARTICLES_MAINPARTICLESCLEARNAME.getActive().getLine(), null)) {
			@Override
			public boolean onClick(Player player, ClickType clickType, GUI gui, int pageIndex) {
				ParticleEffect.stop(player);
				player.closeInventory();
				return true;
			}
		});
		setRegularItem(new ClickeableItem(new ItemData("trail_clear", 16, Mat.IRON_DOOR, 1, Locale.GUI_PYRPARTICLES_MAINTRAILCLEARNAME.getActive().getLine(), null)) {
			@Override
			public boolean onClick(Player player, ClickType clickType, GUI gui, int pageIndex) {
				Trail.stop(player);
				player.closeInventory();
				return true;
			}
		});
	}

	private ItemData getMenuItem(String id, int slot, Mat type, Locale name, Locale lore) {
		return new ItemData(id, slot, type, 1, name.getActive().getLine(), lore.getActive().getLines());
	}

}
