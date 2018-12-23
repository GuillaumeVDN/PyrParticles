package be.pyrrh4.pyrparticles.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.pyrrh4.pyrcore.lib.command.CommandArgument;
import be.pyrrh4.pyrcore.lib.command.CommandCall;
import be.pyrrh4.pyrcore.lib.command.Param;
import be.pyrrh4.pyrcore.lib.command.ParamParser;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PPLocale;
import be.pyrrh4.pyrparticles.PPPerm;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;

public class CommandParticle extends CommandArgument {

	private static final Param paramList = new Param(Utils.asList("list", "l"), null, PPPerm.PYRPARTICLES_COMMAND_PARTICLE, true);
	private static final Param paramStop = new Param(Utils.asList("stop", "cancel"), null, PPPerm.PYRPARTICLES_COMMAND_PARTICLE, true);
	private static final Param paramParticle = new Param(Utils.asList("particle", "p"), "id", PPPerm.PYRPARTICLES_COMMAND_PARTICLE, true);
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
		super(PyrParticles.inst(), Utils.asList("particle"), "particle manipulation", PPPerm.PYRPARTICLES_COMMAND_PARTICLE, true, paramList, paramStop, paramParticle);
		// initialize list
		List<String> particles = Utils.emptyList();
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
			PPLocale.MSG_PYRPARTICLES_PARTICLELIST.send(call.getSender(), "{list}", list);
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
				PPLocale.MSG_PYRPARTICLES_INVALIDPARTICLEPARAM.send(sender, "{parameter}", parameter.toString(), "{value}", value);
				return null;
			}
			// found particle
			return effect;
		}
	};

}
