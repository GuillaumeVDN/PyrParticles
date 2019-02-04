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
import be.pyrrh4.pyrparticles.gadget.Gadget;

// TODO : (for this and also commandparticle and commandtrail) specify target player
public class CommandGadget extends CommandArgument {

	private static final Param paramList = new Param(Utils.asList("list", "l"), null, null, false, false);
	private static final Param paramGadget = new Param(Utils.asList("gadget", "g"), "id", null, false, false);
	private static final Param paramPlayer = new Param(Utils.asList("player", "p"), "name", PPPerm.PYRPARTICLES_COMMAND_GADGET_OTHERS, false, false);
	private String list = "";

	static {
		paramList.setIncompatibleWith(paramGadget);
		paramGadget.setIncompatibleWith(paramList);
	}

	public CommandGadget() {
		super(PyrParticles.inst(), Utils.asList("gadget"), "gadgets manipulation", PPPerm.PYRPARTICLES_COMMAND_GADGET, false, paramList, paramGadget, paramPlayer);
		// initialize list
		List<String> gadgets = Utils.emptyList();
		for (Gadget gadget : Gadget.values()) {
			gadgets.add(gadget.toString().toLowerCase());
		}
		this.list = Utils.asNiceString(gadgets, true);
	}

	@Override
	public void perform(CommandCall call) {
		CommandSender sender = call.getSender();
		// list
		if (paramList.has(call)) {
			PPLocale.MSG_PYRPARTICLES_GADGETLIST.send(sender, "{list}", list);
		}
		// gadget
		else if (paramGadget.has(call)) {
			Gadget gadget = paramGadget.get(call, GADGET_PARSER);
			if (gadget != null) {
				Player target = paramPlayer.has(call) ? paramPlayer.getPlayer(call, true) : (sender instanceof Player ? (Player) sender : null);
				if (target != null) {
					gadget.startOrGiveItem(target);
				}
			}
		}
		// unknown
		else {
			showHelp(sender);
		}
	}

	public static final ParamParser<Gadget> GADGET_PARSER = new ParamParser<Gadget>() {
		@Override
		public Gadget parse(CommandSender sender, Param parameter, String value) {
			// unknown gadget
			Gadget gadget = Utils.valueOfOrNull(Gadget.class, value.toUpperCase());
			if (gadget == null) {
				PPLocale.MSG_PYRPARTICLES_INVALIDGADGETPARAM.send(sender, "{parameter}", "-" + parameter.toString() + (parameter.getDescription() == null ? "" : ":" + value));
				return null;
			}
			// found gadget
			return gadget;
		}
	};

}
