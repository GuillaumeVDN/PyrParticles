package be.pyrrh4.pyrparticles.commands;

import be.pyrrh4.core.command.CommandCall;
import be.pyrrh4.core.command.CommandPattern;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;

public class CommandParticleRemove extends CommandPattern {

	public CommandParticleRemove() {
		super("particle remove", "remove particles effect", "pp.command.particle.remove", true);
	}

	@Override
	public void perform(CommandCall call) {
		ParticleEffect.stop(call.getSenderAsPlayer());
	}

}
