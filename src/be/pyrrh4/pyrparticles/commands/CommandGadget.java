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
import be.pyrrh4.pyrparticles.gadget.Gadget;

// TODO : (for this and also commandparticle and commandtrail) specify target player
public class CommandGadget extends CommandArgument {

	private static final Param paramList = new Param(Utils.asList("list", "l"), null, Perm.PYRPARTICLES_COMMAND_GADGET, true);
	private static final Param paramGadget = new Param(Utils.asList("gadget", "g"), "id", Perm.PYRPARTICLES_COMMAND_GADGET, true);
	private String list = "";

	static {
		paramList.setIncompatibleWith(paramGadget);
		paramGadget.setIncompatibleWith(paramList);
	}

	public CommandGadget() {
		super(PyrParticles.instance(), Utils.asList("gadget"), "gadgets manipulation", Perm.PYRPARTICLES_COMMAND_GADGET, true, paramList, paramGadget);
		// initialize list
		ArrayList<String> gadgets = Utils.emptyList();
		for (Gadget gadget : Gadget.values()) {
			gadgets.add(gadget.toString().toLowerCase());
		}
		this.list = Utils.asNiceString(gadgets, true);
	}

	@Override
	public void perform(CommandCall call) {
		Player player = call.getSenderAsPlayer();
		// list
		if (paramList.has(call)) {
			Locale.MSG_PYRPARTICLES_GADGETLIST.getActive().send(call.getSender(), "{list}", list);
		}
		// gadget
		else if (paramGadget.has(call)) {
			Gadget gadget = paramGadget.get(call, GADGET_PARSER);
			if (gadget != null) {
				gadget.startOrGiveItem(player);
			}
		}
		// unknown
		else {
			showHelp(call.getSender());
		}
	}

	public static final ParamParser<Gadget> GADGET_PARSER = new ParamParser<Gadget>() {
		@Override
		public Gadget parse(CommandSender sender, Param parameter, String value) {
			// unknown gadget
			Gadget gadget = Utils.valueOfOrNull(Gadget.class, value.toUpperCase());
			if (gadget == null) {
				Locale.MSG_PYRPARTICLES_INVALIDGADGETPARAM.getActive().send(sender, "{parameter}", parameter.toString(), "{value}", value);
				return null;
			}
			// found gadget
			return gadget;
		}
	};

}
