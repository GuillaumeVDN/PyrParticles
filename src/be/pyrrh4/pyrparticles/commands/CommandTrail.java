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
import be.pyrrh4.pyrparticles.trail.Trail;

public class CommandTrail extends CommandArgument {

	private static final Param paramList = new Param(Utils.asList("list", "l"), null, Perm.PYRPARTICLES_COMMAND_TRAIL, true);
	private static final Param paramStop = new Param(Utils.asList("stop", "cancel"), null, Perm.PYRPARTICLES_COMMAND_TRAIL, true);
	private static final Param paramTrail = new Param(Utils.asList("trail", "t"), "id", Perm.PYRPARTICLES_COMMAND_TRAIL, true);
	private String list = "";

	static {
		paramList.setIncompatibleWith(paramStop);
		paramList.setIncompatibleWith(paramTrail);

		paramStop.setIncompatibleWith(paramList);
		paramStop.setIncompatibleWith(paramTrail);

		paramTrail.setIncompatibleWith(paramList);
		paramStop.setIncompatibleWith(paramStop);
	}

	public CommandTrail() {
		super(PyrParticles.instance(), Utils.asList("trail"), "trail manipulation", Perm.PYRPARTICLES_COMMAND_TRAIL, true, paramList, paramStop, paramTrail);
		// initialize list
		ArrayList<String> trails = Utils.emptyList();
		for (Trail effect : Trail.values()) {
			trails.add(effect.toString().toLowerCase());
		}
		this.list = Utils.asNiceString(trails, true);
	}

	@Override
	public void perform(CommandCall call) {
		Player player = call.getSenderAsPlayer();
		// list
		if (paramList.has(call)) {
			Locale.MSG_PYRPARTICLES_TRAILLIST.getActive().send(call.getSender(), "{list}", list);
		}
		// stop
		else if (paramStop.has(call)) {
			Trail.stop(call.getSenderAsPlayer());
		}
		// gadget
		else if (paramTrail.has(call)) {
			Trail effect = paramTrail.get(call, TRAIL_PARSER);
			if (effect != null) {
				effect.start(player);
			}
		}
		// unknown
		else {
			showHelp(call.getSender());
		}
	}

	public static final ParamParser<Trail> TRAIL_PARSER = new ParamParser<Trail>() {
		@Override
		public Trail parse(CommandSender sender, Param parameter, String value) {
			// unknown trail
			Trail effect = Utils.valueOfOrNull(Trail.class, value.toUpperCase());
			if (effect == null) {
				Locale.MSG_PYRPARTICLES_INVALIDTRAILPARAM.getActive().send(sender, "{parameter}", parameter.toString(), "{value}", value);
				return null;
			}
			// found trail
			return effect;
		}
	};

}
