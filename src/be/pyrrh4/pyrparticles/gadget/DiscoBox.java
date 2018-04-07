package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import be.pyrrh4.core.compat.particle.ParticleManager;
import be.pyrrh4.core.compat.particle.ParticleManager.Type;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.util.RandomMaterial;

public class DiscoBox extends AbstractGadget implements Listener {

	// static fields
	private static final RandomMaterial type = new RandomMaterial("STAINED_GLASS", 15);
	private static final ArrayList<Material> records = Utils.asList(Material.GREEN_RECORD, Material.RECORD_3, Material.RECORD_4, Material.RECORD_5, Material.RECORD_8, Material.RECORD_9, Material.RECORD_12);

	// fields and constructor
	private Location location;
	private Block lightBlock, jukeboxBlock;
	private MaterialData lightBlockData, jukeboxBlockData;
	private HashMap<Block, MaterialData> borders = new HashMap<Block, MaterialData>();
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
		final int radius = PyrParticles.instance().getDiscoBoxRadius();
		for (int x : Utils.asList(radius, -radius)) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = 0; y <= 4; y++) {
					Block block = location.clone().add(x, y, z).getBlock();
					borders.put(block, new MaterialData(block.getType(), block.getData()));
				}
			}
		}
		for (int z : Utils.asList(radius, -radius)) {
			for (int x = -radius; x <= radius; x++) {
				for (int y = 0; y <= 4; y++) {
					Block block = location.clone().add(x, y, z).getBlock();
					borders.put(block, new MaterialData(block.getType(), block.getData()));
				}
			}
		}
		for (int y : Utils.asList(0, 4)) {
			for (int x = -(radius - 1); x <= (radius - 1); x++) {
				for (int z = -(radius - 1); z <= (radius - 1); z++) {
					if (x == 0 && z == 0) continue;// don't add center top and bottom
					Block block = location.clone().add(x, y, z).getBlock();
					borders.put(block, new MaterialData(block.getType(), block.getData()));
				}
			}
		}
		// set misc
		lightBlockData = new MaterialData(lightBlock.getType(), lightBlock.getData());
		lightBlock.setType(Material.GLOWSTONE);
		jukeboxBlockData = new MaterialData(jukeboxBlock.getType(), jukeboxBlock.getData());
		jukeboxBlock.setType(Material.JUKEBOX);
		// play disc
		location.getWorld().playEffect(lightBlock.getLocation(), Effect.RECORD_PLAY, Utils.random(records).getId());
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
		}.runTaskTimer(PyrParticles.instance(), 0L, PyrParticles.instance().getDiscoBoxTicks()).getTaskId();
		// register listeners
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.instance());
	}

	// stop
	@Override
	public void stop() {
		// delete blocks
		for (Block block : borders.keySet()) {
			MaterialData data = borders.get(block);
			block.setTypeIdAndData(data.getItemTypeId(), data.getData(), false);
		}
		lightBlock.setTypeIdAndData(lightBlockData.getItemTypeId(), lightBlockData.getData(), false);
		jukeboxBlock.setTypeIdAndData(jukeboxBlockData.getItemTypeId(), jukeboxBlockData.getData(), false);
		// stop music
		location.getWorld().playEffect(lightBlock.getLocation(), Effect.RECORD_PLAY, 0);
		// cancel task
		Bukkit.getScheduler().cancelTask(taskId);
		// unregister gadget
		PyrParticles.instance().getRunningGadgets().remove(this);
		// unregister events
		HandlerList.unregisterAll(this);
	}

	// update blocks
	private void updateBlocks() {
		// borders
		for (Block block : borders.keySet()) {
			MaterialData next = type.next();
			block.setTypeIdAndData(next.getItemTypeId(), next.getData(), false);
		}
		// light
		if (toggleLight) {
			lightBlock.setType(Material.GLOWSTONE);
		} else {
			MaterialData next = type.next();
			lightBlock.setTypeIdAndData(next.getItemTypeId(), next.getData(), false);
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
