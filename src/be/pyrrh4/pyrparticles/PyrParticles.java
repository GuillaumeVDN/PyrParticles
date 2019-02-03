package be.pyrrh4.pyrparticles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

import be.pyrrh4.pyrcore.lib.PyrPlugin;
import be.pyrrh4.pyrcore.lib.command.CommandCall;
import be.pyrrh4.pyrcore.lib.command.CommandRoot;
import be.pyrrh4.pyrcore.lib.configuration.YMLConfiguration;
import be.pyrrh4.pyrcore.lib.data.DataManager.BackEnd;
import be.pyrrh4.pyrcore.lib.gui.ItemData;
import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.commands.CommandGadget;
import be.pyrrh4.pyrparticles.commands.CommandParticle;
import be.pyrrh4.pyrparticles.commands.CommandTrail;
import be.pyrrh4.pyrparticles.data.PPDataManager;
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

	public static PyrParticles inst() {
		return instance;
	}

	// ----------------------------------------------------------------------
	// Fields
	// ----------------------------------------------------------------------

	// misc
	private List<Integer> tasksIds = new ArrayList<Integer>();
	private ItemData hotbarItem;

	public ItemData getHotbarItem() {
		return hotbarItem;
	}

	// tracking
	private List<ChangedBlock> trailBlocks = new ArrayList<ChangedBlock>(), colorGunBlocks = new ArrayList<ChangedBlock>();
	private List<AbstractGadget> runningGadgets = new ArrayList<AbstractGadget>();

	public List<ChangedBlock> getTrailBlocks() {
		return trailBlocks;
	}

	public void removeTrailBlocksAt(Location location) {
		for (ChangedBlock tb : Utils.asList(trailBlocks)) {
			if (Utils.coordsEquals(tb.getBlock().getLocation(), location)) {
				trailBlocks.remove(tb);
			}
		}
	}

	public List<ChangedBlock> getColorGunBlocks() {
		return trailBlocks;
	}

	public void removeColorGunBlocksAt(Location location) {
		for (ChangedBlock tb : Utils.asList(colorGunBlocks)) {
			if (Utils.coordsEquals(tb.getBlock().getLocation(), location)) {
				colorGunBlocks.remove(tb);
			}
		}
	}

	public List<AbstractGadget> getRunningGadgets() {
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
	private List<String> enabledWorlds = Utils.emptyList();

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

	public List<String> getEnabledWorlds() {
		return enabledWorlds;
	}

	// ------------------------------------------------------------
	// Data and configuration
	// ------------------------------------------------------------

	private PPDataManager dataManager = null;
	private YMLConfiguration configuration = null;

	@Override
	public YMLConfiguration getConfiguration() {
		return configuration;
	}

	public PPDataManager getData() {
		return dataManager;
	}

	@Override
	protected void unregisterData() {
		dataManager.disable();
	}

	@Override
	public void resetData() {
		dataManager.reset();
	}

	// ----------------------------------------------------------------------
	// Activation
	// ----------------------------------------------------------------------

	@Override
	protected boolean preEnable() {
		// spigot resource id
		this.spigotResourceId = 10225;
		// success
		return true;
	}

	@Override
	protected boolean innerReload() {
		// configuration
		this.configuration = new YMLConfiguration(this, new File(getDataFolder() + "/config.yml"), "config.yml", false, true);

		// load locale file
		reloadLocale(PPLocale.file);

		// reset variables
		particlesTicks = getConfiguration().getInt("settings.particles_ticks", 5);
		particlesAmount = getConfiguration().getInt("settings.particles_amount", 2);
		trailsTicks = getConfiguration().getInt("settings.trails_ticks", 4);
		trailsPersistence = getConfiguration().getInt("settings.trails_persistence", 3);
		colorGunHotbarSlot = getConfiguration().getInt("settings.hotbar_color_gun_item", 5);
		colorGunRadius = getConfiguration().getDouble("settings.color_gun_radius", 3);
		colorGunPersistence = getConfiguration().getInt("settings.color_gun_persistence", 3);
		discoBoxRadius = getConfiguration().getInt("settings.disco_box_radius", 3);
		discoBoxTicks = getConfiguration().getInt("settings.disco_box_ticks", 5);
		discoSheepTicks = getConfiguration().getInt("settings.disco_sheep_ticks", 3);
		mobDanceTicks = getConfiguration().getInt("settings.mob_dance_ticks", 1);
		hotbarItemSlot = getConfiguration().getInt("settings.hotbar_menu_item", -1);
		hotbarGadgetSlot = getConfiguration().getInt("settings.hotbar_gadget_item", -1);
		gadgetsCooldown = getConfiguration().getInt("settings.gadgets_cooldown", 30);
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
		enabledWorlds.addAll(getConfiguration().getList("settings.enabled_worlds", Utils.asList("world")));
		hotbarItem = new ItemData("hotbar_item", -1, Mat.DIAMOND, 1, PPLocale.MISC_PYRPARTICLES_HOTBARITEMNAME.getLine(), PPLocale.MISC_PYRPARTICLES_HOTBARITEMLORE.getLines());

		// eventually cancel tasks
		for (int taskId : tasksIds) {
			Bukkit.getScheduler().cancelTask(taskId);
		}

		// register tasks
		tasksIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ParticlesRunnable(), 40L, particlesTicks));
		tasksIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TrailsRunnable(), 40L, trailsTicks));
		tasksIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MainRunnable(), 40L, 20L));

		// update items
		if (hotbarItemSlot != -1) {
			for (Player pl : Utils.getOnlinePlayers()) {
				pl.getInventory().setItem(hotbarItemSlot, hotbarItem.getItemStack());
				pl.updateInventory();
			}
		}

		// data manager
		if (dataManager == null) {
			BackEnd backend = getConfiguration().getEnumValue("data.backend", BackEnd.class, BackEnd.JSON);
			if (backend == null) {
				backend = BackEnd.JSON;
			}
			this.dataManager = new PPDataManager(backend);
			dataManager.enable();
		} else {
			dataManager.synchronize();
		}

		// success
		return true;
	}

	// ----------------------------------------------------------------------
	// Enable
	// ----------------------------------------------------------------------

	@Override
	protected boolean enable() {
		// call reload
		innerReload();

		// events
		Bukkit.getPluginManager().registerEvents(this, this);

		// commands
		CommandRoot root = new CommandRoot(this, Utils.asList("pyrparticles", "pp", "pparticles", "cosmetics"), null, null, true) {
			@Override
			protected void perform(CommandCall call) {
				new MainGUI().open(call.getSenderAsPlayer());
			}
		};
		registerCommand(root, PPPerm.PYRPARTICLES_ADMIN);
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
			new MainGUI().open((Player) event.getWhoClicked());
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
			new MainGUI().open(event.getPlayer());
			return;
		}
		Gadget gadget = Gadget.getGadgetFromItem(event.getItem());
		if (gadget != null) {
			event.setCancelled(true);
			gadget.start(event.getPlayer());
		}
	}

}
