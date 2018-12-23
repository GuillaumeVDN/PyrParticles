package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrcore.lib.versioncompat.sound.Sound;
import be.pyrrh4.pyrparticles.PyrParticles;

@SuppressWarnings("deprecation")
public class MobDance extends AbstractGadget implements Listener {

	// static fields
	private static final List<Mat> itemsTypes = Utils.asList(Mat.BONE_MEAL, Mat.ROSE_RED, Mat.CREEPER_SPAWN_EGG, Mat.CREEPER_HEAD, Mat.GUNPOWDER, Mat.BONE);
	private static final List<EntityType> mobTypes = Utils.asList(EntityType.CREEPER, EntityType.SKELETON, EntityType.ZOMBIE, EntityType.PIG_ZOMBIE, EntityType.VILLAGER);
	private static final List<Mat> records = Utils.asList(Mat.MUSIC_DISC_CHIRP, Mat.MUSIC_DISC_MELLOHI);

	// fields and constructor
	private Location location;
	private List<Item> items = new ArrayList<Item>();
	private List<Entity> mobs = new ArrayList<Entity>();
	private List<TNTPrimed> tnts = new ArrayList<TNTPrimed>();
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
		location.getWorld().playEffect(location, Effect.RECORD_PLAY, Utils.random(records).getCurrentMaterial().getId());
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
						Item item = location.getWorld().dropItem(location, Utils.random(itemsTypes).getNewCurrentStack());
						item.setPickupDelay(Integer.MAX_VALUE);
						item.setVelocity(new Vector(Utils.randomDouble(-1.0D, 1.0D), Utils.randomDouble(0.0D, 1.0D), Utils.randomDouble(-1.0D, 1.0D)));
						items.add(item);
					}
				}
			}
		}.runTaskTimer(PyrParticles.inst(), 0L, PyrParticles.inst().getMobDanceTicks()).getTaskId();
		// register events
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.inst());
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
		PyrParticles.inst().getRunningGadgets().remove(this);
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
				Item item = tnt.getWorld().dropItem(tnt.getLocation(), Utils.random(itemsTypes).getNewCurrentStack());
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
