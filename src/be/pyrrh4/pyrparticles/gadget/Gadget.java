package be.pyrrh4.pyrparticles.gadget;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import be.pyrrh4.core.User;
import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.PyrParticlesUser;

// TODO : config, add the possibility to disable some particles/trails/gadgets
public enum Gadget {

	COLOR_GUN(ColorGun.class, Material.GOLD_BARDING),
	COCOA_BOMB(CocoaBomb.class, Material.INK_SACK, 3),
	DISCO_BOX(DiscoBox.class, Material.STAINED_GLASS, 4),
	DISCO_SHEEP(DiscoSheep.class, Material.WOOL, 11),
	MOB_DANCE(MobDance.class, Material.MONSTER_EGG),
	PIG_FOUNTAIN(PigFountain.class, Material.PORK),
	PYROMANIAC(Pyromaniac.class, Material.FLINT_AND_STEEL)
	;

	// constructor
	private Class<? extends AbstractGadget> gadgetClass;
	private Material guiItemType;
	private int guiItemData;

	private Gadget(Class<? extends AbstractGadget> gadgetClass, Material guiItemType) {
		this(gadgetClass, guiItemType, 0);
	}

	private Gadget(Class<? extends AbstractGadget> gadgetClass, Material guiItemType, int guiItemData) {
		this.gadgetClass = gadgetClass;
		this.guiItemType = guiItemType;
		this.guiItemData = guiItemData;
	}

	// getters
	public Material getGuiItemType() {
		return guiItemType;
	}

	public int getGuiItemData() {
		return guiItemData;
	}

	public String getName() {
		return PyrParticles.instance().getLocale().getMessage("gadget_" + toString().toLowerCase()).getLines().get(0);
	}

	public int getDuration() {
		return PyrParticles.instance().getConfiguration().getInt("settings.gadget_duration." + toString().toLowerCase(), -1);
	}

	public boolean hasPermission(Player player) {
		return player.isOp() || player.hasPermission("pp.gadget.*") || player.hasPermission("pp.gadget." + toString().toLowerCase());
	}

	public void startOrGiveItem(Player player) {
		// permission
		if (!hasPermission(player)) {
			PyrParticles.instance().getLocale().getMessage("locked").send(player);
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
				PyrParticles.instance().getLocale().getMessage("cooldown").send(player, "$COOLDOWN", Utils.formatDurationMillis(cooldown - diff));
				return;
			}
		}
		data.setLastGadgetUsed(System.currentTimeMillis());
		data.save();
		// start
		AbstractGadget gadg = createGadget(player);
		if (gadg != null) {
			PyrParticles.instance().getLocale().getMessage("gadget_enable").send(player, "$GADGET", getName());
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
			gadgetsToItems.put(gadget, GUI.createItem(gadget.getGuiItemType(), gadget.getGuiItemData(), 1, "§6" + Utils.capitalizeFirstLetter(gadget.getName()), null));
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
