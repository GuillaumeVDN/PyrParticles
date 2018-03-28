package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import be.pyrrh4.core.compat.sound.Sound;
import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;

public class MobDance extends AbstractGadget implements Listener {

	// static fields
	private static final ArrayList<ItemStack> itemsTypes = Utils.asList(
			GUI.createItem(new MaterialData(Material.INK_SACK, (byte) 15)),
			GUI.createItem(new MaterialData(Material.INK_SACK, (byte) 1)),
			GUI.createItem(new MaterialData(Material.MONSTER_EGG, (byte) 50)),
			GUI.createItem(new MaterialData(Material.SKULL_ITEM, (byte) 2)),
			GUI.createItem(new MaterialData(Material.CONCRETE_POWDER)),
			GUI.createItem(new MaterialData(Material.BONE))
			);
	private static final ArrayList<EntityType> mobTypes = Utils.asList(EntityType.CREEPER, EntityType.SKELETON, EntityType.ZOMBIE, EntityType.PIG_ZOMBIE, EntityType.VILLAGER);
	private static final ArrayList<Material> records = Utils.asList(Material.RECORD_7, Material.RECORD_10);

	// fields and constructor
	private Location location;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<Entity> mobs = new ArrayList<Entity>();
	private ArrayList<TNTPrimed> tnts = new ArrayList<TNTPrimed>();
	private int taskId;

	public MobDance(Player player) {
		super(Gadget.MOB_DANCE, player);
	}

	// start
	@Override
	public void start() {
		location = player.getLocation();
		// spawn mobs
		double a = (360.0D / 15.0D) * Math.PI / 180.0D, r = 3.0D, at = 0.0D;
		for (int i = 0; i < 15; i++) {
			double x = Math.cos(at) * r, z = Math.sin(at) * r;
			at += a;
			Entity mob = location.getWorld().spawnEntity(location.clone().add(x, 0.0D, z), Utils.random(mobTypes));
			if (mob.getType().equals(EntityType.CREEPER) && Utils.RANDOM.nextBoolean()) {
				((Creeper) mob).setPowered(true);
			}
			mobs.add(mob);
		}
		// play disc
		location.getWorld().playEffect(location, Effect.RECORD_PLAY, Utils.random(records).getId());
		// start task
		taskId = new BukkitRunnable() {
			private long end = System.currentTimeMillis() + (long) (getType().getDuration() * 1000);
			private double t = 0.0D, r = 3.5D;
			@Override
			public void run() {
				// stop
				if (System.currentTimeMillis() > end) {
					stop();
					return;
				}
				// rotate mobs
				double a = (360.0 / (double) mobs.size()) * Math.PI / 180.0;
				double at = t;
				t += 0.3;

				for (Entity entity : mobs) {
					double x = Math.cos(at) * r;
					double z = Math.sin(at) * r;
					entity.teleport(location.clone().add(x, 0, z));
					at += a;
				}
				// tnt
				if (Utils.random(1, 25) == 1) {
					TNTPrimed tnt = (TNTPrimed) location.getWorld().spawnEntity(Utils.random(mobs).getLocation(), EntityType.PRIMED_TNT);
					tnt.setVelocity(new Vector(Utils.randomDouble(-1.5D, 1.5D), Utils.randomDouble(0.0D, 1.5D), Utils.randomDouble(-1.5D, 1.5D)));
					tnts.add(tnt);
				}
				// items
				if (Utils.random(1, 25) == 1) {
					for (int i = 0; i < 25; i++) {
						Item item = location.getWorld().dropItem(location, Utils.random(itemsTypes));
						item.setPickupDelay(Integer.MAX_VALUE);
						item.setVelocity(new Vector(Utils.randomDouble(-1.0D, 1.0D), Utils.randomDouble(0.0D, 1.0D), Utils.randomDouble(-1.0D, 1.0D)));
						items.add(item);
					}
				}
			}
		}.runTaskTimer(PyrParticles.instance(), 0L, PyrParticles.instance().getMobDanceTicks()).getTaskId();
		// register events
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.instance());
	}

	// stop
	@Override
	public void stop() {
		// delete mobs
		for (Entity mob : mobs) {
			mob.remove();
		}
		mobs.clear();
		// delete TNTs
		for (TNTPrimed tnt : tnts) {
			tnt.remove();
		}
		tnts.clear();
		// delete items
		for (Item item : items) {
			item.remove();
		}
		items.clear();
		// stop music
		location.getWorld().playEffect(location, Effect.RECORD_PLAY, 0);
		// cancel task
		Bukkit.getScheduler().cancelTask(taskId);
		// unregister events
		HandlerList.unregisterAll(this);
		// unregister gadget
		PyrParticles.instance().getRunningGadgets().remove(this);
	}

	// events
	@EventHandler
	public void event(EntityDamageEvent event) {
		if (mobs.contains(event.getEntity())) {
			event.setDamage(0.0D);
		}
	}

	@EventHandler
	public void event(EntityCombustEvent event) {
		if (mobs.contains(event.getEntity())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void event(InventoryPickupItemEvent event) {
		if (items.contains(event.getItem())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void event(EntityDamageByEntityEvent event) {
		if (tnts.contains(event.getDamager())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void event(EntityExplodeEvent event) {
		if (mobs.contains(event.getEntity())) {
			event.setCancelled(true);
		} else if (tnts.contains(event.getEntity())) {
			event.setCancelled(true);
			// remove from list
			TNTPrimed tnt = (TNTPrimed) event.getEntity();
			tnts.remove(tnt);
			tnt.remove();
			// sound
			Sound.EXPLODE.play(tnt.getLocation());
			// items
			for (int i = 0; i < 25; i++) {
				Item item = tnt.getWorld().dropItem(tnt.getLocation(), Utils.random(itemsTypes));
				item.setPickupDelay(Integer.MAX_VALUE);
				item.setVelocity(new Vector(Utils.randomDouble(-1.0D, 1.0D), Utils.randomDouble(0.0D, 1.0D), Utils.randomDouble(-1.0D, 1.0D)));
				items.add(item);
			}
		}
	}

	@EventHandler
	public void event(EntityTargetEvent event) {
		if (mobs.contains(event.getEntity())) {
			event.setCancelled(true);
		}
	}

}
