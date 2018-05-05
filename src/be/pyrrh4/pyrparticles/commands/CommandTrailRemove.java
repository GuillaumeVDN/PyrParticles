package be.pyrrh4.pyrparticles.commands;

import be.pyrrh4.core.command.CommandCall;
import be.pyrrh4.core.command.CommandPattern;
import be.pyrrh4.pyrparticles.trail.Trail;

public class CommandTrailRemove extends CommandPattern {

	public CommandTrailRemove() {
		super("trail remove", "remove trail", "pp.command.trail.remove", true);
	}

	@Override
	public void perform(CommandCall call) {
		Trail.stop(call.getSenderAsPlayer());
	}

}
