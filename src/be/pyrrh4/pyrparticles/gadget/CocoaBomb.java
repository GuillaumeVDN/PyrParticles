package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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

import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrcore.lib.versioncompat.sound.Sound;
import be.pyrrh4.pyrparticles.PyrParticles;

public class CocoaBomb extends AbstractGadget implements Listener {

	// static fields
	private static final ItemStack cocoaItem = Mat.COCOA_BEANS.getNewCurrentStack();

	// fields and constructor
	private Block bomb;
	private List<Item> items = new ArrayList<Item>();

	public CocoaBomb(Player player) {
		super(Gadget.COCOA_BOMB, player);
	}

	// start
	@Override
	public void start() {
		// spawn bomb
		bomb = player.getLocation().getBlock();
		Mat.BROWN_WOOL.setBlock(bomb);
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
					Mat.AIR.setBlock(bomb);
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
					}.runTaskLater(PyrParticles.inst(), 20L * 5L);
					// cancel
					cancel();
				}
				// will explode
				else {
					// change bomb type
					if (toggleType) {
						Mat.BROWN_WOOL.setBlock(bomb);
					} else {
						Mat.WHITE_WOOL.setBlock(bomb);
					}
					toggleType = !toggleType;
					// sound
					Sound.NOTE_STICKS.play(bomb.getLocation());
				}
			}
		}.runTaskTimer(PyrParticles.inst(), 7L, 7L);
		// register events
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.inst());
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
		PyrParticles.inst().getRunningGadgets().remove(this);
	}

	// events

	@EventHandler
	public void onHopperHop(InventoryPickupItemEvent event) {
		if (items.contains(event.getItem())) {
			event.setCancelled(true);
		}
	}

}
