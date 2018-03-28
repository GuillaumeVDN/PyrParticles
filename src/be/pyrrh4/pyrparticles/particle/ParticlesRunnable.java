package be.pyrrh4.pyrparticles.particle;

import org.bukkit.entity.Player;

import be.pyrrh4.core.User;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.PyrParticlesUser;

public class ParticlesRunnable implements Runnable {

	@Override
	public void run() {
		// for every player
		for (Player player : Utils.getOnlinePlayers()) {
			PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
			// that has particles
			if (data.getParticleEffect() != null) {
				// if world is allowed
				if (PyrParticles.instance().getEnabledWorlds().isEmpty() || PyrParticles.instance().getEnabledWorlds().contains(player.getWorld().getName())) {
					// display particle
					data.getParticleDisplayer().display(player, data.getParticleEffect());
				}
			}
		}
	}

}
