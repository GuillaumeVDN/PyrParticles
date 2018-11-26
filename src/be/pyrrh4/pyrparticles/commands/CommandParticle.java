package be.pyrrh4.pyrparticles.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.pyrrh4.core.Perm;
import be.pyrrh4.core.command.CommandArgument;
import be.pyrrh4.core.command.CommandCall;
import be.pyrrh4.core.command.Param;
import be.pyrrh4.core.command.ParamParser;
import be.pyrrh4.core.messenger.Locale;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;

public class CommandParticle extends CommandArgument {

	private static final Param paramList = new Param(Utils.asList("list", "l"), null, Perm.PYRPARTICLES_COMMAND_PARTICLE, true);
	private static final Param paramStop = new Param(Utils.asList("stop", "cancel"), null, Perm.PYRPARTICLES_COMMAND_PARTICLE, true);
	private static final Param paramParticle = new Param(Utils.asList("particle", "p"), "id", Perm.PYRPARTICLES_COMMAND_PARTICLE, true);
	private String list = "";

	static {
		paramList.setIncompatibleWith(paramStop);
		paramList.setIncompatibleWith(paramParticle);

		paramStop.setIncompatibleWith(paramList);
		paramStop.setIncompatibleWith(paramParticle);

		paramParticle.setIncompatibleWith(paramList);
		paramStop.setIncompatibleWith(paramStop);
	}

	public CommandParticle() {
		super(PyrParticles.instance(), Utils.asList("particle"), "particle manipulation", Perm.PYRPARTICLES_COMMAND_PARTICLE, true, paramList, paramStop, paramParticle);
		// initialize list
		ArrayList<String> particles = Utils.emptyList();
		for (ParticleEffect effect : ParticleEffect.values()) {
			particles.add(effect.toString().toLowerCase());
		}
		this.list = Utils.asNiceString(particles, true);
	}

	@Override
	public void perform(CommandCall call) {
		Player player = call.getSenderAsPlayer();
		// list
		if (paramList.has(call)) {
			Locale.MSG_PYRPARTICLES_PARTICLELIST.getActive().send(call.getSender(), "{list}", list);
		}
		// stop
		else if (paramStop.has(call)) {
			ParticleEffect.stop(call.getSenderAsPlayer());
		}
		// particle
		else if (paramParticle.has(call)) {
			ParticleEffect effect = paramParticle.get(call, PARTICLE_PARSER);
			if (effect != null) {
				effect.start(player);
			}
		}
		// unknown
		else {
			showHelp(call.getSender());
		}
	}

	public static final ParamParser<ParticleEffect> PARTICLE_PARSER = new ParamParser<ParticleEffect>() {
		@Override
		public ParticleEffect parse(CommandSender sender, Param parameter, String value) {
			// unknown particle
			ParticleEffect effect = Utils.valueOfOrNull(ParticleEffect.class, value.toUpperCase());
			if (effect == null) {
				Locale.MSG_PYRPARTICLES_INVALIDPARTICLEPARAM.getActive().send(sender, "{parameter}", parameter.toString(), "{value}", value);
				return null;
			}
			// found particle
			return effect;
		}
	};

}
