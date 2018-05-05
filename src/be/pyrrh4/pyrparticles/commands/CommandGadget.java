package be.pyrrh4.pyrparticles.commands;

import org.bukkit.entity.Player;

import be.pyrrh4.core.command.CommandCall;
import be.pyrrh4.core.command.CommandPattern;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.gadget.Gadget;

public class CommandGadget extends CommandPattern {

	public CommandGadget() {
		super("gadget [string]%gadget_name", "activate a gadget", "pp.command.gadget", true);
	}

	@Override
	public void perform(CommandCall call) {
		Player player = call.getSenderAsPlayer();
		Gadget gadget = Utils.valueOfOrNull(Gadget.class, call.getArgAsString(this, 1).toUpperCase());
		if (gadget == null) {
			PyrParticles.instance().getLocale().getMessage("unknown_gadget").send(player);
		} else {
			gadget.startOrGiveItem(player);
		}
	}

}
