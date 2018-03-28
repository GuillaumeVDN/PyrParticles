package be.pyrrh4.pyrparticles.particle;

import org.bukkit.entity.Player;

import be.pyrrh4.pyrparticles.PyrParticles;

public enum ParticleDisplayer {

	// TODO : adding more than one animation might be a good thing to do + how to select them
	AROUND(ParticleDisplayerAround.class);

	private AbstractParticleDisplayer displayer;

	private ParticleDisplayer(Class<? extends AbstractParticleDisplayer> displayerClass) {
		try {
			displayer = displayerClass.newInstance();
		} catch (InstantiationException | IllegalAccessException exception) {
			PyrParticles.instance().error("Could not initialize particle displayer " + displayerClass + " :");
			exception.printStackTrace();
			displayer = null;
		}
	}

	public void display(Player player, ParticleEffect effect) {
		if (displayer != null) {
			displayer.display(player, effect);
		}
	}

	// displayer

	public static interface AbstractParticleDisplayer {
		public void display(Player player, ParticleEffect effect);
	}

}
