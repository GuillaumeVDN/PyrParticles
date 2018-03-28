package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import be.pyrrh4.core.compat.sound.Sound;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;

public class CocoaBomb extends AbstractGadget implements Listener {

	// static fields
	private static final ItemStack cocoaItem = new ItemStack(351, 1, (short) 0, (byte) 3);

	// fields and constructor
	private Block bomb;
	private ArrayList<Item> items = new ArrayList<Item>();

	public CocoaBomb(Player player) {
		super(Gadget.COCOA_BOMB, player);
	}

	// start
	@Override
	public void start() {
		// spawn bomb
		bomb = player.getLocation().getBlock();
		bomb.setTypeIdAndData(Material.WOOL.getId(), (byte) 12, false);
		Sound.FIZZ.play(bomb.getLocation());
		// start task
		new BukkitRunnable() {
			private long remaining = 20L * 5L;
			private boolean toggleType = false;
			@Override
			public void run() {
				// explode
				remaining -= 7L;
				if (remaining <= 0L) {
					// explode bomb
					bomb.setType(Material.AIR);
					Sound.EXPLODE.play(bomb.getLocation());
					for (int i = 0; i < 25; i++) {
						Item item = bomb.getWorld().dropItem(bomb.getLocation(), cocoaItem);
						item.setPickupDelay(Integer.MAX_VALUE);
						item.setVelocity(new Vector(Utils.randomDouble(-1.0D, 1.0D), Utils.randomDouble(0.0D, 1.0D), Utils.randomDouble(-1.0D, 1.0D)));
						items.add(item);
					}
					// stop later
					new BukkitRunnable() {
						@Override
						public void run() {
							stop();
						}
					}.runTaskLater(PyrParticles.instance(), 20L * 5L);
					// cancel
					cancel();
				}
				// will explode
				else {
					// change bomb type
					if (toggleType) {
						bomb.setTypeIdAndData(Material.WOOL.getId(), (byte) 12, false);
					} else {
						bomb.setTypeIdAndData(Material.WOOL.getId(), (byte) 0, false);
					}
					toggleType = !toggleType;
					// sound
					Sound.NOTE_STICKS.play(bomb.getLocation());
				}
			}
		}.runTaskTimer(PyrParticles.instance(), 7L, 7L);
		// register events
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.instance());
	}

	// stop
	@Override
	public void stop() {
		// remove items
		for (Item item : items) {
			item.remove();
		}
		items.clear();
		// unregister events
		HandlerList.unregisterAll(this);
		// unregister gadget
		PyrParticles.instance().getRunningGadgets().remove(this);
	}

	// events

	@EventHandler
	public void onHopperHop(InventoryPickupItemEvent event) {
		if (items.contains(event.getItem())) {
			event.setCancelled(true);
		}
	}

}
