package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.data.PPUser;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;
import be.pyrrh4.pyrparticles.trail.Trail;
import be.pyrrh4.pyrparticles.util.ChangedBlock;

public class Pyromaniac extends AbstractGadget implements Listener {

	// fields and constructor
	private List<ChangedBlock> changedBlocks = new ArrayList<ChangedBlock>();
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
		PPUser user = PyrParticles.inst().getData().getUsers().getElement(player);
		previousEffect = user.getParticleEffect();
		previousTrail = user.getTrail();
		// change effect
		user.setParticleEffetAndTrail(ParticleEffect.MAGMA, Trail.NETHER);
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
		}.runTaskTimer(PyrParticles.inst(), 0L, 20L).getTaskId();
		// register events
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.inst());
	}

	// stop
	@Override
	public void stop() {
		// reset effects
		PPUser user = PyrParticles.inst().getData().getUsers().getElement(player);
		user.setParticleEffect(previousEffect);
		user.setTrail(previousTrail);
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
		PyrParticles.inst().getRunningGadgets().remove(this);
	}

	// events
	@EventHandler
	public void event(PlayerMoveEvent event) {
		if (!event.getPlayer().equals(player) || Utils.coordsEquals(event.getFrom(), event.getTo())) {
			return;
		}
		// change block
		Block block = player.getLocation().getBlock();
		List<Player> affected = Utils.asList(player.getWorld().getPlayers());
		Mat.FIRE.setBlockChange(player.getLocation().getBlock(), affected);
		// save
		changedBlocks.add(new ChangedBlock(block, affected));
	}

}
