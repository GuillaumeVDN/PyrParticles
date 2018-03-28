package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import be.pyrrh4.core.User;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.PyrParticlesUser;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;
import be.pyrrh4.pyrparticles.trail.Trail;
import be.pyrrh4.pyrparticles.util.ChangedBlock;

public class Pyromaniac extends AbstractGadget implements Listener {

	// fields and constructor
	private ArrayList<ChangedBlock> changedBlocks = new ArrayList<ChangedBlock>();
	private ParticleEffect previousEffect;
	private Trail previousTrail;
	private int taskId;

	public Pyromaniac(Player player) {
		super(Gadget.PYROMANIAC, player);
	}

	// start
	@Override
	public void start() {
		// save effects
		PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
		previousEffect = data.getParticleEffect();
		previousTrail = data.getTrail();
		// change effect
		data.setParticleEffect(ParticleEffect.MAGMA);
		data.setTrail(null);
		data.save();
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
			}
		}.runTaskTimer(PyrParticles.instance(), 0L, 20L).getTaskId();
		// register events
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.instance());
	}

	// stop
	@Override
	public void stop() {
		// reset effects
		PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
		data.setParticleEffect(previousEffect);
		data.setTrail(previousTrail);
		data.save();
		// reset blocks
		for (ChangedBlock block : changedBlocks) {
			block.restore();
		}
		changedBlocks.clear();
		// cancel task
		Bukkit.getScheduler().cancelTask(taskId);
		// unregister events
		HandlerList.unregisterAll(this);
		// unregister gadget
		PyrParticles.instance().getRunningGadgets().remove(this);
	}

	// events
	@EventHandler
	public void event(PlayerMoveEvent event) {
		if (!event.getPlayer().equals(player) || Utils.coordsEquals(event.getFrom(), event.getTo())) {
			return;
		}
		// change block
		Block block = player.getLocation().getBlock();
		ArrayList<Player> affected = Utils.asList(player.getWorld().getPlayers());
		for (Player pl : affected) {
			pl.sendBlockChange(block.getLocation(), Material.FIRE, (byte) 0);
		}
		// save
		changedBlocks.add(new ChangedBlock(block, affected));
	}

}
