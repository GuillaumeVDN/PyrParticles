package be.pyrrh4.pyrparticles.commands;

import org.bukkit.entity.Player;

import be.pyrrh4.core.command.CommandCall;
import be.pyrrh4.core.command.CommandPattern;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;

public class CommandParticle extends CommandPattern {

	public CommandParticle() {
		super("particle [string]%particle_name", "enable a particles effect", "pp.command.particle", true);
	}

	@Override
	public void perform(CommandCall call) {
		Player player = call.getSenderAsPlayer();
		ParticleEffect effect = Utils.valueOfOrNull(ParticleEffect.class, call.getArgAsString(this, 1).toUpperCase());
		if (effect == null) {
			PyrParticles.instance().getLocale().getMessage("unknown_particle").send(player);
		} else {
			effect.start(player);
		}
	}

}
