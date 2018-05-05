package be.pyrrh4.pyrparticles.commands;

import org.bukkit.entity.Player;

import be.pyrrh4.core.command.CommandCall;
import be.pyrrh4.core.command.CommandPattern;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.trail.Trail;

public class CommandTrail extends CommandPattern {

	public CommandTrail() {
		super("trail [string]%trail_name", "enable a trail", "pp.command.trail", true);
	}

	@Override
	public void perform(CommandCall call) {
		Player player = call.getSenderAsPlayer();
		Trail trail = Utils.valueOfOrNull(Trail.class, call.getArgAsString(this, 1).toUpperCase());
		if (trail == null) {
			PyrParticles.instance().getLocale().getMessage("unknown_trail").send(player);
		} else {
			trail.start(player);
		}
	}

}
