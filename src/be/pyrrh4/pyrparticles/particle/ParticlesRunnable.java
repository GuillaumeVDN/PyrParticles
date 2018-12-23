package be.pyrrh4.pyrparticles.particle;

import org.bukkit.entity.Player;

import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.data.PPUser;

public class ParticlesRunnable implements Runnable {

	@Override
	public void run() {
		// for every player
		for (Player player : Utils.getOnlinePlayers()) {
			PPUser user = PyrParticles.inst().getData().getUsers().getElement(player);
			// that has particles
			if (user.getParticleEffect() != null) {
				// if world is allowed
				if (PyrParticles.inst().getEnabledWorlds().isEmpty() || PyrParticles.inst().getEnabledWorlds().contains(player.getWorld().getName())) {
					// display particle
					user.getParticleDisplayer().display(player, user.getParticleEffect());
				}
			}
		}
	}

}
