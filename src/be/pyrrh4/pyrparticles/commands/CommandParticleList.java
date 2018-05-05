package be.pyrrh4.pyrparticles.commands;

import java.util.ArrayList;

import be.pyrrh4.core.command.CommandCall;
import be.pyrrh4.core.command.CommandPattern;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;

public class CommandParticleList extends CommandPattern {

	private final String list;

	public CommandParticleList() {
		super("particle list", "particles effects list", "pp.command.particle.list", false);
		// settings
		ArrayList<String> particles = Utils.emptyList();
		for (ParticleEffect part : ParticleEffect.values()) {
			particles.add(part.toString().toLowerCase());
		}
		this.list = Utils.asNiceString(particles, true);
	}

	@Override
	public void perform(CommandCall call) {
		PyrParticles.instance().getLocale().getMessage("particle_list").send(call.getSender(), "$LIST", list);
	}

}
