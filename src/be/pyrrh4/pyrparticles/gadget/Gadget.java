package be.pyrrh4.pyrparticles.gadget;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import be.pyrrh4.pyrcore.PCLocale;
import be.pyrrh4.pyrcore.lib.gui.ItemData;
import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.messenger.Text;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PPLocale;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.data.PPUser;

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
	private String name;

	private Gadget(Class<? extends AbstractGadget> gadgetClass, Mat guiItemType) {
		this.gadgetClass = gadgetClass;
		this.guiItemType = guiItemType;
		Text text = Text.valueOf("MISC_PYRPARTICLES_GADGET" + name().replace("_", ""));
		this.name = text == null ? name() : text.getLine();
	}

	// getters
	public Mat getGuiItemType() {
		return guiItemType;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return PyrParticles.inst().getConfiguration().getInt("settings.gadget_duration." + name().toLowerCase(), -1);
	}

	public boolean hasPermission(Player player) {
		return player.isOp() || player.hasPermission("pyrparticles.gadget.*") || player.hasPermission("pyrparticles.gadget." + toString().toLowerCase());
	}

	public void startOrGiveItem(Player player) {
		// permission
		if (!hasPermission(player)) {
			PCLocale.MSG_GENERIC_NOPERMISSION.send(player, "{plugin}", PyrParticles.inst().getName());
			return;
		}
		// give in hotbar
		if (PyrParticles.inst().getHotbarGadgetSlot() != -1) {
			player.getInventory().setItem(PyrParticles.inst().getHotbarGadgetSlot(), gadgetsToItems.get(this));
			player.updateInventory();
		}
		// execute it
		else {
			start(player);
		}
	}

	public void start(Player player) {
		// delay
		PPUser user = PyrParticles.inst().getData().getUsers().getElement(player);
		if (PyrParticles.inst().getGadgetsCooldown() != -1) {
			long diff = System.currentTimeMillis() - user.getLastGadgetUsed(), cooldown = PyrParticles.inst().getGadgetsCooldown() * 1000;
			if (diff < cooldown) {
				PCLocale.MSG_GENERIC_COOLDOWN.send(player, "{plugin}", PyrParticles.inst().getName(), "{time}", Utils.formatDurationMillis(cooldown - diff));
				return;
			}
		}
		user.setLastGadgetUsed(System.currentTimeMillis());
		// start
		AbstractGadget gadg = createGadget(player);
		if (gadg != null) {
			PPLocale.MSG_PYRPARTICLES_GADGETENABLE.send(player, "{gadget}", getName());
			gadg.start();
			PyrParticles.inst().getRunningGadgets().add(gadg);
		}
	}

	public AbstractGadget createGadget(Player player) {
		try {
			return gadgetClass.getConstructor(Player.class).newInstance(player);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
			PyrParticles.inst().error("Could not initialize gadget " + gadgetClass + " :");
			exception.printStackTrace();
			return null;
		}
	}

	// associate GUI items with gadgets
	private static Map<Gadget, ItemStack> gadgetsToItems = new HashMap<Gadget, ItemStack>();

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
