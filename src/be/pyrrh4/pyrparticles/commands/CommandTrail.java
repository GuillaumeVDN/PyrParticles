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
import be.pyrrh4.pyrparticles.trail.Trail;

public class CommandTrail extends CommandArgument {

	private static final Param paramList = new Param(Utils.asList("list", "l"), null, PPPerm.PYRPARTICLES_COMMAND_TRAIL, false, false);
	private static final Param paramStop = new Param(Utils.asList("stop", "cancel"), null, PPPerm.PYRPARTICLES_COMMAND_TRAIL, false, false);
	private static final Param paramTrail = new Param(Utils.asList("trail", "t"), "id", PPPerm.PYRPARTICLES_COMMAND_TRAIL, false, false);
	private static final Param paramPlayer = new Param(Utils.asList("player", "p"), "name", PPPerm.PYRPARTICLES_COMMAND_TRAIL_OTHERS, false, false);
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
		super(PyrParticles.inst(), Utils.asList("trail"), "trail manipulation", PPPerm.PYRPARTICLES_COMMAND_TRAIL, false, paramList, paramStop, paramTrail);
		// initialize list
		List<String> trails = Utils.emptyList();
		for (Trail effect : Trail.values()) {
			trails.add(effect.toString().toLowerCase());
		}
		this.list = Utils.asNiceString(trails, true);
	}

	@Override
	public void perform(CommandCall call) {
		CommandSender sender = call.getSender();
		// list
		if (paramList.has(call)) {
			PPLocale.MSG_PYRPARTICLES_TRAILLIST.send(sender, "{list}", list);
		}
		// stop
		else if (paramStop.has(call)) {
			Player target = paramPlayer.has(call) ? paramPlayer.getPlayer(call, true) : (sender instanceof Player ? (Player) sender : null);
			if (target != null) {
				Trail.stop(target);
			}
		}
		// gadget
		else if (paramTrail.has(call)) {
			Trail effect = paramTrail.get(call, TRAIL_PARSER);
			if (effect != null) {
				Player target = paramPlayer.has(call) ? paramPlayer.getPlayer(call, true) : (sender instanceof Player ? (Player) sender : null);
				if (target != null) {
					effect.start(target);
				}
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
				PPLocale.MSG_PYRPARTICLES_INVALIDTRAILPARAM.send(sender, "{parameter}", "-" + parameter.toString() + (parameter.getDescription() == null ? "" : ":" + value));
				return null;
			}
			// found trail
			return effect;
		}
	};

}
