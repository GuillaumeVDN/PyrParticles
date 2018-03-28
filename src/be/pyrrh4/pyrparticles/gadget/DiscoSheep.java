package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import be.pyrrh4.core.compat.sound.Sound;
import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.util.RandomMaterial;

public class DiscoSheep extends AbstractGadget implements Listener {

	// static fields
	private static final RandomMaterial type = new RandomMaterial(Material.WOOL, 15);

	// fields and constructor
	private Sheep sheep;
	private ArrayList<Item> items = new ArrayList<Item>();
	private int taskId;

	public DiscoSheep(Player player) {
		super(Gadget.DISCO_SHEEP, player);
	}

	// start
	@Override
	public void start() {
		// spawn sheep
		sheep = (Sheep) player.getWorld().spawnEntity(player.getLocation(), EntityType.SHEEP);
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
				// sheep color
				sheep.setColor(Utils.getRandomDyeColor());
				// items
				Item item = sheep.getWorld().dropItem(sheep.getLocation().clone().add(0.0D, 1.0D, 0.0D), GUI.createItem(type.getNext(), 1));
				item.setPickupDelay(Integer.MAX_VALUE);
				item.setVelocity(new Vector(Utils.randomDouble(-0.6D, 0.6D), Utils.randomDouble(0.0D, 0.6D), Utils.randomDouble(-0.6D, 0.6D)));
				items.add(item);
				// sound
				Sound.NOTE_BASS_DRUM.play(sheep.getLocation());
			}
		}.runTaskTimer(PyrParticles.instance(), 0L, PyrParticles.instance().getDiscoSheepTicks()).getTaskId();
		// register events
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.instance());
	}

	// stop
	@Override
	public void stop() {
		// delete sheep
		sheep.remove();
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
		PyrParticles.instance().getRunningGadgets().remove(this);
	}

	// events
	@EventHandler
	public void event(EntityDamageEvent event) {
		if (event.getEntity().equals(sheep)) {
			event.setDamage(0.0D);
		}
	}

	@EventHandler
	public void event(EntityCombustEvent event) {
		if (event.getEntity().equals(sheep)) {
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
