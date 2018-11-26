package be.pyrrh4.pyrparticles;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import be.pyrrh4.core.Core;
import be.pyrrh4.core.Perm;
import be.pyrrh4.core.PyrPlugin;
import be.pyrrh4.core.User;
import be.pyrrh4.core.command.CommandCall;
import be.pyrrh4.core.command.CommandRoot;
import be.pyrrh4.core.gui.ItemData;
import be.pyrrh4.core.material.Mat;
import be.pyrrh4.core.messenger.Locale;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.commands.CommandGadget;
import be.pyrrh4.pyrparticles.commands.CommandParticle;
import be.pyrrh4.pyrparticles.commands.CommandTrail;
import be.pyrrh4.pyrparticles.gadget.AbstractGadget;
import be.pyrrh4.pyrparticles.gadget.Gadget;
import be.pyrrh4.pyrparticles.gui.MainGUI;
import be.pyrrh4.pyrparticles.particle.ParticlesRunnable;
import be.pyrrh4.pyrparticles.trail.TrailsRunnable;
import be.pyrrh4.pyrparticles.util.ChangedBlock;

public class PyrParticles extends PyrPlugin implements Listener {

	// ----------------------------------------------------------------------
	// Instance
	// ----------------------------------------------------------------------

	private static PyrParticles instance;

	public PyrParticles() {
		instance = this;
	}

	public static PyrParticles instance() {
		return instance;
	}

	// ----------------------------------------------------------------------
	// Fields
	// ----------------------------------------------------------------------

	// misc
	private ArrayList<Integer> tasksIds = new ArrayList<Integer>();
	private MainGUI mainGUI;
	private ItemData hotbarItem;

	public MainGUI getMainGUI() {
		return mainGUI;
	}

	public ItemData getHotbarItem() {
		return hotbarItem;
	}

	// tracking
	private ArrayList<ChangedBlock> trailBlocks = new ArrayList<ChangedBlock>(), colorGunBlocks = new ArrayList<ChangedBlock>();
	private ArrayList<AbstractGadget> runningGadgets = new ArrayList<AbstractGadget>();

	public ArrayList<ChangedBlock> getTrailBlocks() {
		return trailBlocks;
	}

	public void removeTrailBlocksAt(Location location) {
		for (ChangedBlock tb : Utils.asList(trailBlocks)) {
			if (Utils.coordsEquals(tb.getBlock().getLocation(), location)) {
				trailBlocks.remove(tb);
			}
		}
	}

	public ArrayList<ChangedBlock> getColorGunBlocks() {
		return trailBlocks;
	}

	public void removeColorGunBlocksAt(Location location) {
		for (ChangedBlock tb : Utils.asList(colorGunBlocks)) {
			if (Utils.coordsEquals(tb.getBlock().getLocation(), location)) {
				colorGunBlocks.remove(tb);
			}
		}
	}

	public ArrayList<AbstractGadget> getRunningGadgets() {
		return runningGadgets;
	}

	public AbstractGadget getRunningGadget(Player player) {
		for (AbstractGadget gadget : runningGadgets) {
			if (gadget.getPlayer().equals(player)) {
				return gadget;
			}
		}
		return null;
	}

	// settings
	private int particlesAmount, trailsPersistence, colorGunHotbarSlot, colorGunPersistence, discoBoxRadius, hotbarItemSlot, hotbarGadgetSlot, gadgetsCooldown;
	private long particlesTicks, trailsTicks, discoBoxTicks, discoSheepTicks, mobDanceTicks;
	private double colorGunRadius;
	private ArrayList<String> enabledWorlds = Utils.emptyList();

	public int getParticlesAmount() {
		return particlesAmount;
	}

	public int getTrailsPersistence() {
		return trailsPersistence;
	}

	public int getColorGunHotbarSlot() {
		return colorGunHotbarSlot;
	}

	public double getColorGunRadius() {
		return colorGunRadius;
	}

	public int getColorGunPersistence() {
		return colorGunPersistence;
	}

	public int getDiscoBoxRadius() {
		return discoBoxRadius;
	}

	public long getDiscoBoxTicks() {
		return discoBoxTicks;
	}

	public long getDiscoSheepTicks() {
		return discoSheepTicks;
	}

	public long getMobDanceTicks() {
		return mobDanceTicks;
	}

	public int getHotbarItemSlot() {
		return hotbarItemSlot;
	}

	public int getHotbarGadgetSlot() {
		return hotbarGadgetSlot;
	}

	public int getGadgetsCooldown() {
		return gadgetsCooldown;
	}

	public ArrayList<String> getEnabledWorlds() {
		return enabledWorlds;
	}

	// ----------------------------------------------------------------------
	// Plugin data
	// ----------------------------------------------------------------------

	@Override
	protected void loadStorage() {
	}

	@Override
	protected void saveStorage() {
	}

	@Override
	protected void closeUserData() {
		for (User user : Core.instance().getUsers().values()) {
			user.closePluginData(PyrParticlesUser.class);
		}
	}

	// ----------------------------------------------------------------------
	// Pre enable
	// ----------------------------------------------------------------------

	@Override
	protected boolean preEnable() {
		this.spigotResourceId = 10225;
		return true;
	}

	// ----------------------------------------------------------------------
	// Reload
	// ----------------------------------------------------------------------

	@Override
	protected void reloadInner() {
		// reset variables
		particlesTicks = getConfiguration().getInt("settings.particles_ticks");
		particlesAmount = getConfiguration().getInt("settings.particles_amount");
		trailsTicks = getConfiguration().getInt("settings.trails_ticks");
		trailsPersistence = getConfiguration().getInt("settings.trails_persistence");
		colorGunHotbarSlot = getConfiguration().getInt("settings.hotbar_color_gun_item");
		colorGunRadius = getConfiguration().getDouble("settings.color_gun_radius");
		colorGunPersistence = getConfiguration().getInt("settings.color_gun_persistence");
		discoBoxRadius = getConfiguration().getInt("settings.disco_box_radius");
		discoBoxTicks = getConfiguration().getInt("settings.disco_box_ticks");
		discoSheepTicks = getConfiguration().getInt("settings.disco_sheep_ticks");
		mobDanceTicks = getConfiguration().getInt("settings.mob_dance_ticks");
		hotbarItemSlot = getConfiguration().getInt("settings.hotbar_menu_item");
		hotbarGadgetSlot = getConfiguration().getInt("settings.hotbar_gadget_item");
		gadgetsCooldown = getConfiguration().getInt("settings.gadgets_cooldown");
		if (particlesTicks < 1L) particlesTicks = 1L;
		if (particlesAmount < 1) particlesAmount = 1;
		if (trailsTicks < 1L) trailsTicks = 1L;
		if (trailsPersistence < 1) trailsPersistence = 1;
		if (colorGunHotbarSlot < 0 || colorGunHotbarSlot > 8) colorGunHotbarSlot = 4;
		if (colorGunRadius < 1.0D) colorGunRadius = 1.0D;
		if (colorGunPersistence < 1) colorGunPersistence = 1;
		if (discoBoxRadius < 1) discoBoxRadius = 1;
		if (discoBoxTicks < 1L) discoBoxTicks = 1L;
		if (discoSheepTicks < 1L) discoSheepTicks = 1L;
		if (mobDanceTicks < 1L) mobDanceTicks = 1L;
		if (hotbarItemSlot < 0 || hotbarItemSlot > 8) hotbarItemSlot = -1;
		if (hotbarGadgetSlot < 0 || hotbarGadgetSlot > 8) hotbarGadgetSlot = -1;
		if (hotbarItemSlot == hotbarGadgetSlot) hotbarGadgetSlot = -1;
		if (gadgetsCooldown < 1) gadgetsCooldown = -1;
		enabledWorlds.clear();
		enabledWorlds.addAll(getConfiguration().getList("settings.enabled_worlds"));
		hotbarItem = new ItemData("hotbar_item", -1, Mat.DIAMOND, 1, Locale.MISC_PYRPARTICLES_HOTBARITEMNAME.getActive().getLine(), Locale.MISC_PYRPARTICLES_HOTBARITEMLORE.getActive().getLines());

		// eventually cancel tasks
		for (int taskId : tasksIds) {
			Bukkit.getScheduler().cancelTask(taskId);
		}

		// register tasks
		tasksIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ParticlesRunnable(), 20L, particlesTicks));
		tasksIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TrailsRunnable(), 20L, trailsTicks));
		tasksIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MainRunnable(), 20L, 20L));

		// eventually unregister GUI
		if (mainGUI != null) {
			mainGUI.unregister();
		}
		mainGUI = new MainGUI();

		// update items
		if (hotbarItemSlot != -1) {
			for (Player pl : Utils.getOnlinePlayers()) {
				pl.getInventory().setItem(hotbarItemSlot, hotbarItem.getItemStack());
				pl.updateInventory();
			}
		}
	}

	// ----------------------------------------------------------------------
	// Enable
	// ----------------------------------------------------------------------

	@Override
	protected boolean enable() {
		// call reload
		reloadInner();
		// events
		Bukkit.getPluginManager().registerEvents(this, this);
		// commands
		CommandRoot root = new CommandRoot(this, Utils.asList("pyrparticles", "pp"), null, null, true) {
			@Override
			protected void perform(CommandCall call) {
				PyrParticles.instance().getMainGUI().open(call.getSenderAsPlayer());
			}
		};
		registerCommand(root, Perm.PYRPARTICLES_ADMIN);
		root.addChild(new CommandGadget());
		root.addChild(new CommandParticle());
		root.addChild(new CommandTrail());
		// return
		return true;
	}

	// ----------------------------------------------------------------------
	// Disable
	// ----------------------------------------------------------------------

	@Override
	protected void disable() {
		// cancel tasks
		for (int id : tasksIds) {
			Bukkit.getScheduler().cancelTask(id);
		}
		// restore changed blocks
		for (ChangedBlock cb : trailBlocks) {
			cb.restore();
		}
		for (ChangedBlock cb : colorGunBlocks) {
			cb.restore();
		}
		// stop gadgets
		for (AbstractGadget gadget : Utils.asList(runningGadgets)) {
			gadget.stop();
		}
	}

	// ----------------------------------------------------------------------
	// Listeners
	// ----------------------------------------------------------------------

	@EventHandler
	public void event(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (hotbarItemSlot != -1) {
			player.getInventory().setItem(hotbarItemSlot, hotbarItem.getItemStack());
			player.updateInventory();
		}
	}

	@EventHandler
	public void event(PlayerDropItemEvent event) {
		ItemStack item = event.getItemDrop().getItemStack();
		if (hotbarItem.isSimilar(item) || Gadget.isGadgetItem(item)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void event(InventoryClickEvent event) {
		if (hotbarItem.isSimilar(event.getCurrentItem())) {
			event.setCancelled(true);
			mainGUI.open((Player) event.getWhoClicked());
			return;
		}
		Gadget gadget = Gadget.getGadgetFromItem(event.getCurrentItem());
		if (gadget != null) {
			event.setCancelled(true);
			gadget.start((Player) event.getWhoClicked());
		}
	}

	@EventHandler
	public void event(PlayerInteractEvent event) {
		if (hotbarItem.isSimilar(event.getItem())) {
			event.setCancelled(true);
			mainGUI.open(event.getPlayer());
			return;
		}
		Gadget gadget = Gadget.getGadgetFromItem(event.getItem());
		if (gadget != null) {
			event.setCancelled(true);
			gadget.start(event.getPlayer());
		}
	}

}
