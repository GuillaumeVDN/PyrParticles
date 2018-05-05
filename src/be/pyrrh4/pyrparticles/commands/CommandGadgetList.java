package be.pyrrh4.pyrparticles.commands;

import java.util.ArrayList;

import be.pyrrh4.core.command.CommandCall;
import be.pyrrh4.core.command.CommandPattern;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.gadget.Gadget;

public class CommandGadgetList extends CommandPattern {

	private final String list;

	public CommandGadgetList() {
		super("gadget list", "gadgets list", "pp.command.gadget.list", false);
		// settings
		ArrayList<String> gadgets = Utils.emptyList();
		for (Gadget gadget : Gadget.values()) {
			gadgets.add(gadget.toString().toLowerCase());
		}
		this.list = Utils.asNiceString(gadgets, true);
	}

	@Override
	public void perform(CommandCall call) {
		PyrParticles.instance().getLocale().getMessage("gadget_list").send(call.getSender(), "$LIST", list);
	}

}
