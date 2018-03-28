package be.pyrrh4.pyrparticles.particle;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import be.pyrrh4.core.compat.particle.ParticleManager;
import be.pyrrh4.core.compat.particle.ParticleManager.Type;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.particle.ParticleDisplayer.AbstractParticleDisplayer;

public class ParticleDisplayerAround implements AbstractParticleDisplayer {

	private static final int RANGE = 2;

	@Override
	public void display(Player player, ParticleEffect particleEffect) {
		for (int i = 0; i < PyrParticles.instance().getParticlesAmount(); i++) {
			Location loc = player.getLocation().clone();
			// idk why the following but it was like that in the previous versions
			double x = Math.cos(Utils.random(1, 360)) * (Utils.random(1, RANGE));
			double z = Math.sin(Utils.random(1, 360)) * (Utils.random(1, RANGE));
			loc.add(x, Utils.randomDouble(-1.0D, 2.0D), z);
			// add vertical compensation
			if (particleEffect.getVerticalCompensation() != 0.0F) {
				loc.add(0.0D, particleEffect.getVerticalCompensation(), 0.0D);
			}
			// display particle
			Type type = particleEffect.getNextParticleType();
			if (type.hasColor()) {
				ParticleManager.INSTANCE.sendColor(type, loc, 0.0F, 1, Utils.getRandomBukkitColor(), loc.getWorld());
			} else {
				ParticleManager.INSTANCE.send(type, loc, 0.0F, 1, loc.getWorld());
			}
		}
	}

}
