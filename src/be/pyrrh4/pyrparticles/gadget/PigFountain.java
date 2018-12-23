package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrcore.lib.versioncompat.sound.Sound;
import be.pyrrh4.pyrparticles.PyrParticles;

public class PigFountain extends AbstractGadget implements Listener {

	// static fields
	private static final List<Mat> itemsTypes = Utils.asList(Mat.BONE_MEAL, Mat.ROSE_RED);

	// fields and constructor
	private List<Pig> pigs = new ArrayList<Pig>();
	private List<Item> items = new ArrayList<Item>();
	private Location location;
	private int taskId;

	public PigFountain(Player player) {
		super(Gadget.PIG_FOUNTAIN, player);
	}

	// start
	@Override
	public void start() {
		location = player.getLocation();
		// start task
		taskId = new BukkitRunnable() {
			private long end = System.currentTimeMillis() + (long) (getType().getDuration() * 1000);
			@Override
			public void run() {
				// stop
				if (System.currentTimeMillis() > end) {
					stop();
					return;
				}
				// spawn pig
				Pig pig = (Pig) location.getWorld().spawnEntity(location, EntityType.PIG);
				pig.setVelocity(new Vector(Utils.randomDouble(-2.0D, 2.0D), Utils.randomDouble(0.0D, 2.0D), Utils.randomDouble(-2.0D, 2.0D)));
				pigs.add(pig);
				// sound
				Sound.PIG_IDLE.play(location);
			}
		}.runTaskTimer(PyrParticles.inst(), 0L, 20L).getTaskId();
		// register events
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.inst());
	}

	// stop
	@Override
	public void stop() {
		// delete pigs
		for (Pig pig : pigs) {
			pig.remove();
		}
		pigs.clear();
		// delete items
		for (Item item : items) {
			item.remove();
		}
		items.clear();
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
		if (pigs.contains(event.getEntity())) {
			event.setDamage(0.0D);
			if (event.getCause().equals(DamageCause.FALL)) {
				Pig pig = (Pig) event.getEntity();
				// sound
				Sound.NOTE_PLING.play(pig.getLocation());
				// items
				for (int i = 0; i < 25; i++) {
					Item item = pig.getWorld().dropItem(pig.getLocation(), Utils.random(itemsTypes).getNewCurrentStack());
					item.setPickupDelay(Integer.MAX_VALUE);
					item.setVelocity(new Vector(Utils.randomDouble(-1.0D, 1.0D), Utils.randomDouble(0.0D, 1.0D), Utils.randomDouble(-1.0D, 1.0D)));
					items.add(item);
				}
				// remove entity
				pig.remove();
				pigs.remove(pig);
			}
		}
	}

	@EventHandler
	public void event(EntityCombustEvent event) {
		if (pigs.contains(event.getEntity())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onHopperHop(InventoryPickupItemEvent event) {
		if (items.contains(event.getItem())) {
			event.setCancelled(true);
		}
	}

}
