package be.pyrrh4.pyrparticles;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import be.pyrrh4.core.command.Arguments;
import be.pyrrh4.core.command.Arguments.Performer;
import be.pyrrh4.core.command.CallInfo;
import be.pyrrh4.core.command.Command;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.gadget.Gadget;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;
import be.pyrrh4.pyrparticles.trail.Trail;

public class Commands {

	private static String particles = "", trails = "", gadgets = "";

	static {
		// particles effects
		ArrayList<String> particles = Utils.emptyList();
		for (ParticleEffect part : ParticleEffect.values()) {
			particles.add(part.toString().toLowerCase());
		}
		Commands.particles = Utils.asNiceString(particles, true);
		// trails
		ArrayList<String> trails = Utils.emptyList();
		for (Trail trail : Trail.values()) {
			trails.add(trail.toString().toLowerCase());
		}
		Commands.trails = Utils.asNiceString(trails, true);
		// gadgets
		ArrayList<String> gadgets = Utils.emptyList();
		for (Gadget gadget : Gadget.values()) {
			gadgets.add(gadget.toString().toLowerCase());
		}
		Commands.gadgets = Utils.asNiceString(gadgets, true);
	}

	static void registerCommands(Command command) {

		// particles effect
		command.addArguments(new Arguments("particle list", "particle list", "particles effects list", "pp.command.particle.list", true, new Performer() {
			@Override
			public void perform(CallInfo call) {
				Player player = call.getSenderAsPlayer();
				PyrParticles.instance().getLocale().getMessage("particle_list").send(player, "$LIST", particles);
			}
		}));

		command.addArguments(new Arguments("particle remove", "particle remove", "remove particles effect", "pp.command.particle.remove", true, new Performer() {
			@Override
			public void perform(CallInfo call) {
				Player player = call.getSenderAsPlayer();
				ParticleEffect.stop(player);
			}
		}));

		command.addArguments(new Arguments("particle [string]", "particle [name]", "enable a particles effect", "pp.command.particle", true, new Performer() {
			@Override
			public void perform(CallInfo call) {
				Player player = call.getSenderAsPlayer();
				ParticleEffect effect = Utils.valueOfOrNull(ParticleEffect.class, call.getArgAsString(1).toUpperCase());
				if (effect == null) {
					PyrParticles.instance().getLocale().getMessage("unknown_particle").send(player);
				} else {
					effect.start(player);
				}
			}
		}));

		// trail
		command.addArguments(new Arguments("trail list", "trail list", "trails list", "pp.command.trail.list", true, new Performer() {
			@Override
			public void perform(CallInfo call) {
				Player player = call.getSenderAsPlayer();
				PyrParticles.instance().getLocale().getMessage("trail_list").send(player, "$LIST", trails);
			}
		}));

		command.addArguments(new Arguments("trail remove", "trail remove", "remove trail", "pp.command.trail.remove", true, new Performer() {
			@Override
			public void perform(CallInfo call) {
				Player player = call.getSenderAsPlayer();
				Trail.stop(player);
			}
		}));

		command.addArguments(new Arguments("trail [string]", "trail [name]", "enable a trail", "pp.command.trail", true, new Performer() {
			@Override
			public void perform(CallInfo call) {
				Player player = call.getSenderAsPlayer();
				Trail trail = Utils.valueOfOrNull(Trail.class, call.getArgAsString(1).toUpperCase());
				if (trail == null) {
					PyrParticles.instance().getLocale().getMessage("unknown_trail").send(player);
				} else {
					trail.start(player);
				}
			}
		}));

		// gadget
		command.addArguments(new Arguments("gadget list", "gadget list", "gadgets effects list", "pp.command.gadget.list", true, new Performer() {
			@Override
			public void perform(CallInfo call) {
				Player player = call.getSenderAsPlayer();
				PyrParticles.instance().getLocale().getMessage("gadget_list").send(player, "$LIST", gadgets);
			}
		}));

		command.addArguments(new Arguments("gadget [string]", "gadget [name]", "activate a gadget", "pp.command.gadget", true, new Performer() {
			@Override
			public void perform(CallInfo call) {
				Player player = call.getSenderAsPlayer();
				Gadget gadget = Utils.valueOfOrNull(Gadget.class, call.getArgAsString(1).toUpperCase());
				if (gadget == null) {
					PyrParticles.instance().getLocale().getMessage("unknown_gadget").send(player);
				} else {
					gadget.startOrGiveItem(player);
				}
			}
		}));

	}

}
