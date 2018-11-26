package be.pyrrh4.pyrparticles.gadget;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import be.pyrrh4.core.User;
import be.pyrrh4.core.gui.ItemData;
import be.pyrrh4.core.material.Mat;
import be.pyrrh4.core.messenger.Locale;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.PyrParticlesUser;

// TODO : config, add the possibility to disable some particles/trails/gadgets
public enum Gadget {

	COLOR_GUN(ColorGun.class, Mat.GOLDEN_HORSE_ARMOR),
	COCOA_BOMB(CocoaBomb.class, Mat.CYAN_DYE),
	DISCO_BOX(DiscoBox.class, Mat.CYAN_STAINED_GLASS),
	DISCO_SHEEP(DiscoSheep.class, Mat.BLUE_WOOL),
	MOB_DANCE(MobDance.class, Mat.CREEPER_SPAWN_EGG),
	PIG_FOUNTAIN(PigFountain.class, Mat.COOKED_PORKCHOP),
	PYROMANIAC(Pyromaniac.class, Mat.FLINT_AND_STEEL)
	;

	// constructor
	private Class<? extends AbstractGadget> gadgetClass;
	private Mat guiItemType;

	private Gadget(Class<? extends AbstractGadget> gadgetClass, Mat guiItemType) {
		this.gadgetClass = gadgetClass;
		this.guiItemType = guiItemType;
	}

	// getters
	public Mat getGuiItemType() {
		return guiItemType;
	}

	public String getName() {
		return Utils.valueOfOrNull(Locale.class, "MISC_PYRPARTICLES_GADGET" + name().replace("_", "")).getActive().getLine();
	}

	public int getDuration() {
		return PyrParticles.instance().getConfiguration().getInt("settings.gadget_duration." + name().toLowerCase(), -1);
	}

	public boolean hasPermission(Player player) {
		return player.isOp() || player.hasPermission("pyrparticles.gadget.*") || player.hasPermission("pyrparticles.gadget." + toString().toLowerCase());
	}

	public void startOrGiveItem(Player player) {
		// permission
		if (!hasPermission(player)) {
			Locale.MSG_GENERIC_NOPERMISSION.getActive().send(player, "{plugin}", PyrParticles.instance().getName());
			return;
		}
		// give in hotbar
		if (PyrParticles.instance().getHotbarGadgetSlot() != -1) {
			player.getInventory().setItem(PyrParticles.instance().getHotbarGadgetSlot(), gadgetsToItems.get(this));
			player.updateInventory();
		}
		// execute it
		else {
			start(player);
		}
	}

	public void start(Player player) {
		// delay
		PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
		if (PyrParticles.instance().getGadgetsCooldown() != -1) {
			long diff = System.currentTimeMillis() - data.getLastGadgetUsed(), cooldown = PyrParticles.instance().getGadgetsCooldown() * 1000;
			if (diff < cooldown) {
				Locale.MSG_GENERIC_COOLDOWN.getActive().send(player, "{time}", Utils.formatDurationMillis(cooldown - diff));
				return;
			}
		}
		data.setLastGadgetUsed(System.currentTimeMillis());
		// start
		AbstractGadget gadg = createGadget(player);
		if (gadg != null) {
			Locale.MSG_PYRPARTICLES_GADGETENABLE.getActive().send(player, "{gadget}", getName());
			gadg.start();
			PyrParticles.instance().getRunningGadgets().add(gadg);
		}
	}

	public AbstractGadget createGadget(Player player) {
		try {
			return gadgetClass.getConstructor(Player.class).newInstance(player);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			PyrParticles.instance().error("Could not initialize gadget " + gadgetClass + " :");
			exception.printStackTrace();
			return null;
		}
	}

	// associate GUI items with gadgets
	private static HashMap<Gadget, ItemStack> gadgetsToItems = new HashMap<Gadget, ItemStack>();

	static {
		for (Gadget gadget : Gadget.values()) {
			gadgetsToItems.put(gadget, new ItemData("gadget_" + gadget.getName(), -1, gadget.getGuiItemType(), 1, "§6" + Utils.capitalizeFirstLetter(gadget.getName()), null).getItemStack());
		}
	}

	public static Gadget getGadgetFromItem(ItemStack item) {
		for (Gadget gadget : gadgetsToItems.keySet()) {
			if (gadgetsToItems.get(gadget).isSimilar(item)) {
				return gadget;
			}
		}
		return null;
	}

	public static boolean isGadgetItem(ItemStack item) {
		for (ItemStack it : gadgetsToItems.values()) {
			if (it.isSimilar(item)) {
				return true;
			}
		}
		return false;
	}

}
