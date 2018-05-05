package be.pyrrh4.pyrparticles.commands;

import java.util.ArrayList;

import be.pyrrh4.core.command.CommandCall;
import be.pyrrh4.core.command.CommandPattern;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.trail.Trail;

public class CommandTrailList extends CommandPattern {

	private final String list;

	public CommandTrailList() {
		super("trail list", "trails list", "pp.command.trail.list", false);
		// settings
		ArrayList<String> trails = Utils.emptyList();
		for (Trail trail : Trail.values()) {
			trails.add(trail.toString().toLowerCase());
		}
		this.list = Utils.asNiceString(trails, true);
	}

	@Override
	public void perform(CommandCall call) {
		PyrParticles.instance().getLocale().getMessage("trail_list").send(call.getSender(), "$LIST", list);
	}

}
