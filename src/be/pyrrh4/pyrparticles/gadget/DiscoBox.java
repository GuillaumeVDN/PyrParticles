package be.pyrrh4.pyrparticles.gadget;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrcore.lib.versioncompat.particle.ParticleManager;
import be.pyrrh4.pyrcore.lib.versioncompat.particle.ParticleManager.Type;
import be.pyrrh4.pyrparticles.PyrParticles;

public class DiscoBox extends AbstractGadget implements Listener {

	// fields and constructor
	private Location location;
	private Block lightBlock, jukeboxBlock;
	private Mat lightBlockMat, jukeboxBlockMat;
	private Map<Block, Mat> borders = new HashMap<Block, Mat>();
	private int taskId = -1;
	private boolean toggleLight = false;

	public DiscoBox(Player player) {
		super(Gadget.DISCO_BOX, player);
	}

	// start
	@Override
	public void start() {
		// pre-calculate blocks
		location = player.getLocation().clone().subtract(0, 1, 0);
		lightBlock = location.clone().add(0, 4, 0).getBlock();
		jukeboxBlock = location.getBlock();
		final int radius = PyrParticles.inst().getDiscoBoxRadius();
		for (int x : Utils.asList(radius, -radius)) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = 0; y <= 4; y++) {
					Block block = location.clone().add(x, y, z).getBlock();
					borders.put(block, Mat.from(block));
				}
			}
		}
		for (int z : Utils.asList(radius, -radius)) {
			for (int x = -radius; x <= radius; x++) {
				for (int y = 0; y <= 4; y++) {
					Block block = location.clone().add(x, y, z).getBlock();
					borders.put(block, Mat.from(block));
				}
			}
		}
		for (int y : Utils.asList(0, 4)) {
			for (int x = -(radius - 1); x <= (radius - 1); x++) {
				for (int z = -(radius - 1); z <= (radius - 1); z++) {
					if (x == 0 && z == 0) continue;// don't add center top and bottom
					Block block = location.clone().add(x, y, z).getBlock();
					borders.put(block, Mat.from(block));
				}
			}
		}
		// set misc
		lightBlockMat = Mat.from(lightBlock);
		Mat.GLOWSTONE.setBlock(lightBlock);
		jukeboxBlockMat= Mat.from(jukeboxBlock);
		Mat.JUKEBOX.setBlock(jukeboxBlock);
		// play disc
		location.getWorld().playEffect(lightBlock.getLocation(), Effect.RECORD_PLAY, AbstractGadget.RANDOM_DISK.next());
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
				// blocks
				updateBlocks();
				// particles
				for (int i = 0; i < 15; i++) {
					ParticleManager.INSTANCE.sendColor(Type.SPELL_MOB, location.clone().add(Utils.randomDouble(-radius, radius), Utils.randomDouble(0.0D, 4.0D), Utils.randomDouble(-radius, radius)), 1.0F, 1, Utils.getRandomBukkitColor(), Utils.asList(location.getWorld().getPlayers()));
				}
			}
		}.runTaskTimer(PyrParticles.inst(), 0L, PyrParticles.inst().getDiscoBoxTicks()).getTaskId();
		// register listeners
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.inst());
	}

	// stop
	@Override
	public void stop() {
		// delete blocks
		for (Block block : borders.keySet()) {
			borders.get(block).setBlock(block);
		}
		lightBlockMat.setBlock(lightBlock);
		jukeboxBlockMat.setBlock(jukeboxBlock);
		// stop music
		location.getWorld().playEffect(lightBlock.getLocation(), Effect.RECORD_PLAY, 0);
		// cancel task
		Bukkit.getScheduler().cancelTask(taskId);
		// unregister gadget
		PyrParticles.inst().getRunningGadgets().remove(this);
		// unregister events
		HandlerList.unregisterAll(this);
	}

	// update blocks
	private void updateBlocks() {
		// borders
		for (Block block : borders.keySet()) {
			AbstractGadget.RANDOM_DISK.next().setBlock(block);
		}
		// light
		if (toggleLight) {
			Mat.GLOWSTONE.setBlock(lightBlock);
		} else {
			AbstractGadget.RANDOM_DISK.next().setBlock(lightBlock);
		}
		toggleLight = !toggleLight;
	}

	@EventHandler
	public void event(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (borders.containsKey(block) || block.equals(lightBlock) || block.equals(jukeboxBlock)) {
			event.setCancelled(true);
		}
	}

}
