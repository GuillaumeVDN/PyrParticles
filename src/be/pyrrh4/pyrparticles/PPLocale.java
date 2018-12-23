package be.pyrrh4.pyrparticles;

import java.io.File;

import be.pyrrh4.pyrcore.lib.messenger.Text;
import be.pyrrh4.pyrcore.lib.util.Utils;

public class PPLocale {

	// ----------------------------------------------------------------------
	// Fields
	// ----------------------------------------------------------------------

	public static final File file = new File(PyrParticles.inst().getDataFolder() + "/texts.yml");

	// ----------------------------------------------------------------------
	// Messages
	// ----------------------------------------------------------------------

	public static final Text MSG_PYRPARTICLES_INVALIDGADGETPARAM = new Text(
			"MSG_PYRPARTICLES_INVALIDGADGETPARAM", file,
			"en_US", "&6PyrParticles >> &7Parameter &c{parameter} [{value}] &7should be an existing gadget.",
			"fr_FR", "&6PyrParticles >> &7Le paramètre &c{parameter} [{value}] &7devrait être un gadget existant."
			);

	public static final Text MSG_PYRPARTICLES_INVALIDPARTICLEPARAM = new Text(
			"MSG_PYRPARTICLES_INVALIDPARTICLEPARAM", file,
			"en_US", "&6PyrParticles >> &7Parameter &c{parameter} [{value}] &7should be an existing particle effect.",
			"fr_FR", "&6PyrParticles >> &7Le paramètre &c{parameter} [{value}] &7devrait être un effet de particule existant."
			);

	public static final Text MSG_PYRPARTICLES_INVALIDTRAILPARAM = new Text(
			"MSG_PYRPARTICLES_INVALIDTRAILPARAM", file,
			"en_US", "&6PyrParticles >> &7Parameter &c{parameter} [{value}] &7should be an existing trail.",
			"fr_FR", "&6PyrParticles >> &7Le paramètre &c{parameter} [{value}] &7devrait être une trainée existante."
			);

	public static final Text MSG_PYRPARTICLES_GADGETLIST = new Text(
			"MSG_PYRPARTICLES_GADGETLIST", file,
			"en_US", "&6PyrParticles >> &7Available gadgets : &a{list}",
			"fr_FR", "&6PyrParticles >> &7Gadgets disponibles : &a{list}"
			);

	public static final Text MSG_PYRPARTICLES_PARTICLELIST = new Text(
			"MSG_PYRPARTICLES_PARTICLELIST", file,
			"en_US", "&6PyrParticles >> &7Available particle effects : &a{list}",
			"fr_FR", "&6PyrParticles >> &7Effets de particules disponibles : &a{list}"
			);

	public static final Text MSG_PYRPARTICLES_TRAILLIST = new Text(
			"MSG_PYRPARTICLES_TRAILLIST", file,
			"en_US", "&6PyrParticles >> &7Available trails : &a{list}",
			"fr_FR", "&6PyrParticles >> &7Trainées disponibles : &a{list}"
			);

	public static final Text MSG_PYRPARTICLES_GADGETENABLE = new Text(
			"MSG_PYRPARTICLES_GADGETENABLE", file,
			"en_US", "&6PyrParticles >> &7You activated gadget &a{gadget}&7.",
			"fr_FR", "&6PyrParticles >> &7Vous avez utilisé le gadget &a{gadget}&7."
			);

	public static final Text MSG_PYRPARTICLES_PARTICLEENABLE = new Text(
			"MSG_PYRPARTICLES_PARTICLEENABLE", file,
			"en_US", "&6PyrParticles >> &7You enabled particle &a{particle}&7.",
			"fr_FR", "&6PyrParticles >> &7Vous avez activé l'effet de particules &a{particle}&7."
			);

	public static final Text MSG_PYRPARTICLES_PARTICLEDISABLE = new Text(
			"MSG_PYRPARTICLES_PARTICLEDISABLE", file,
			"en_US", "&6PyrParticles >> &7You disabled particles.",
			"fr_FR", "&6PyrParticles >> &7Vous avez désactivé les effets de particules."
			);

	public static final Text MSG_PYRPARTICLES_TRAILENABLE = new Text(
			"MSG_PYRPARTICLES_TRAILENABLE", file,
			"en_US", "&6PyrParticles >> &7You enabled trail &a{trail}&7.",
			"fr_FR", "&6PyrParticles >> &7Vous avez activé la trainée &a{trail}&7."
			);

	public static final Text MSG_PYRPARTICLES_TRAILDISABLE = new Text(
			"MSG_PYRPARTICLES_TRAILDISABLE", file,
			"en_US", "&6PyrParticles >> &7You disabled trail.",
			"fr_FR", "&6PyrParticles >> &7Vous avez désactivé la trainée."
			);

	// ------------------------------------------------------------
	// MISC
	// ------------------------------------------------------------

	public static final Text GUI_PYRPARTICLES_MAINNAME = new Text(
			"GUI_PYRPARTICLES_MAINNAME", file,
			"en_US", "PyrParticles"
			);

	public static final Text GUI_PYRPARTICLES_MAINGADGETSNAME = new Text(
			"GUI_PYRPARTICLES_MAINGADGETSNAME", file,
			"en_US", "&6Gadgets"
			);

	public static final Text GUI_PYRPARTICLES_MAINPARTICLESNAME = new Text(
			"GUI_PYRPARTICLES_MAINPARTICLESNAME", file,
			"en_US", "&6Particle effects",
			"fr_FR", "&6Effets de particules"
			);

	public static final Text GUI_PYRPARTICLES_MAINPARTICLESLORE = new Text(
			"GUI_PYRPARTICLES_MAINPARTICLESLORE", file,
			"en_US", Utils.asList("&7Click to open"),
			"fr_FR", Utils.asList("&7Cliquez pour ouvrir")
			);

	public static final Text GUI_PYRPARTICLES_MAINGADGETSLORE = new Text(
			"GUI_PYRPARTICLES_MAINGADGETSLORE", file,
			"en_US", Utils.asList("&7Click to select"),
			"fr_FR", Utils.asList("&7Cliquez pour sélectionner")
			);

	public static final Text GUI_PYRPARTICLES_MAINPARTICLESCLEARNAME = new Text(
			"GUI_PYRPARTICLES_MAINPARTICLESCLEARNAME", file,
			"en_US", "&cClear particles effect",
			"fr_FR", "&cSupprimer l'effet de particules"
			);

	public static final Text GUI_PYRPARTICLES_MAINTRAILSNAME = new Text(
			"GUI_PYRPARTICLES_MAINTRAILSNAME", file,
			"en_US", "&6Trails",
			"fr_FR", "&6Trainées"
			);

	public static final Text GUI_PYRPARTICLES_MAINTRAILSLORE = new Text(
			"GUI_PYRPARTICLES_MAINTRAILSLORE", file,
			"en_US", Utils.asList("&7Click to open"),
			"fr_FR", Utils.asList("&7Cliquez pour ouvrir")
			);

	public static final Text GUI_PYRPARTICLES_MAINTRAILCLEARNAME = new Text(
			"GUI_PYRPARTICLES_MAINTRAILCLEARNAME", file,
			"en_US", "&cClear trail",
			"fr_FR", "&cSupprimer la traînée"
			);

	public static final Text GUI_PYRPARTICLES_GADGETSNAME = new Text(
			"GUI_PYRPARTICLES_GADGETSNAME", file,
			"en_US", "PyrParticles/Gadgets",
			"fr_FR", "PyrParticles/Gadgets"
			);

	public static final Text GUI_PYRPARTICLES_PARTICLESNAME = new Text(
			"GUI_PYRPARTICLES_PARTICLESNAME", file,
			"en_US", "PyrParticles/Particles",
			"fr_FR", "PyrParticles/Particules"
			);

	public static final Text GUI_PYRPARTICLES_TRAILSNAME = new Text(
			"GUI_PYRPARTICLES_TRAILSNAME", file,
			"en_US", "PyrParticles/Trails",
			"fr_FR", "PyrParticles/Trainées"
			);

	// ------------------------------------------------------------
	// GUI
	// ------------------------------------------------------------

	public static final Text MISC_PYRPARTICLES_HOTBARITEMNAME = new Text(
			"MISC_PYRPARTICLES_HOTBARITEMNAME", file,
			"en_US", "&6PyrParticles",
			"fr_FR", "&6PyrParticles"
			);

	public static final Text MISC_PYRPARTICLES_HOTBARITEMLORE = new Text(
			"MISC_PYRPARTICLES_HOTBARITEMLORE", file,
			"en_US", Utils.asList("&7Click to open"),
			"fr_FR", Utils.asList("&7Cliquez pour ouvrir")
			);

	public static final Text MISC_PYRPARTICLES_LOCKED = new Text(
			"MISC_PYRPARTICLES_LOCKED", file,
			"en_US", "&c&lLocked",
			"fr_FR", "&c&lBloqué"
			);

	public static final Text MISC_PYRPARTICLES_UNLOCKED = new Text(
			"MISC_PYRPARTICLES_UNLOCKED", file,
			"en_US", "&a&lUnlocked",
			"fr_FR", "&a&lDébloqué"
			);

	public static final Text MISC_PYRPARTICLES_GADGETPIGFOUNTAIN = new Text(
			"MISC_PYRPARTICLES_GADGETPIGFOUNTAIN", file,
			"en_US", "pig fountain",
			"fr_FR", "fontaine de cochons"
			);

	public static final Text MISC_PYRPARTICLES_GADGETDISCOBOX = new Text(
			"MISC_PYRPARTICLES_GADGETDISCOBOX", file,
			"en_US", "disco box",
			"fr_FR", "boîte de nuit"
			);

	public static final Text MISC_PYRPARTICLES_GADGETCOLORGUN = new Text(
			"MISC_PYRPARTICLES_GADGETCOLORGUN", file,
			"en_US", "color gun",
			"fr_FR", "pistolet à couleur"
			);

	public static final Text MISC_PYRPARTICLES_GADGETDISCOSHEEP = new Text(
			"MISC_PYRPARTICLES_GADGETDISCOSHEEP", file,
			"en_US", "disco sheep",
			"fr_FR", "mouton disco"
			);

	public static final Text MISC_PYRPARTICLES_GADGETPYROMANIAC = new Text(
			"MISC_PYRPARTICLES_GADGETPYROMANIAC", file,
			"en_US", "pyromaniac",
			"fr_FR", "pyromane"
			);

	public static final Text MISC_PYRPARTICLES_GADGETMOBDANCE = new Text(
			"MISC_PYRPARTICLES_GADGETMOBDANCE", file,
			"en_US", "mob dance",
			"fr_FR", "danse des mobs"
			);

	public static final Text MISC_PYRPARTICLES_GADGETCOCOABOMB = new Text(
			"MISC_PYRPARTICLES_GADGETCOCOABOMB", file,
			"en_US", "cocoa bomb",
			"fr_FR", "bombe de cacao"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEMUSIC = new Text(
			"MISC_PYRPARTICLES_PARTICLEMUSIC", file,
			"en_US", "music",
			"fr_FR", "musique"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLESLIME = new Text(
			"MISC_PYRPARTICLES_PARTICLESLIME", file,
			"en_US", "slime",
			"fr_FR", "slime"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLESUSPENDED = new Text(
			"MISC_PYRPARTICLES_PARTICLESUSPENDED", file,
			"en_US", "suspended",
			"fr_FR", "en suspens"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEREDSTONE = new Text(
			"MISC_PYRPARTICLES_PARTICLEREDSTONE", file,
			"en_US", "redstone",
			"fr_FR", "redstone"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEHAPPY = new Text(
			"MISC_PYRPARTICLES_PARTICLEHAPPY", file,
			"en_US", "happy",
			"fr_FR", "joie"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEENDER = new Text(
			"MISC_PYRPARTICLES_PARTICLEENDER", file,
			"en_US", "ender",
			"fr_FR", "ender"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLESPARKS = new Text(
			"MISC_PYRPARTICLES_PARTICLESPARKS", file,
			"en_US", "sparks",
			"fr_FR", "étincelles"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEWATER = new Text(
			"MISC_PYRPARTICLES_PARTICLEWATER", file,
			"en_US", "water",
			"fr_FR", "eau"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLECOLOR = new Text(
			"MISC_PYRPARTICLES_PARTICLECOLOR", file,
			"en_US", "color",
			"fr_FR", "couleur"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLESNOWBALL = new Text(
			"MISC_PYRPARTICLES_PARTICLESNOWBALL", file,
			"en_US", "snowball",
			"fr_FR", "boule de neige"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEBUBBLE = new Text(
			"MISC_PYRPARTICLES_PARTICLEBUBBLE", file,
			"en_US", "bubble",
			"fr_FR", "bulles"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEFIRE = new Text(
			"MISC_PYRPARTICLES_PARTICLEFIRE", file,
			"en_US", "fire",
			"fr_FR", "feu"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEANGRY = new Text(
			"MISC_PYRPARTICLES_PARTICLEANGRY", file,
			"en_US", "angry",
			"fr_FR", "colère"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLECLOUD = new Text(
			"MISC_PYRPARTICLES_PARTICLECLOUD", file,
			"en_US", "cloud",
			"fr_FR", "nuage"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLESPELL = new Text(
			"MISC_PYRPARTICLES_PARTICLESPELL", file,
			"en_US", "spell",
			"fr_FR", "sorts"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLELAVA = new Text(
			"MISC_PYRPARTICLES_PARTICLELAVA", file,
			"en_US", "lava",
			"fr_FR", "lave"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLESHOVEL = new Text(
			"MISC_PYRPARTICLES_PARTICLESHOVEL", file,
			"en_US", "shovel",
			"fr_FR", "pelle"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLESMOKE = new Text(
			"MISC_PYRPARTICLES_PARTICLESMOKE", file,
			"en_US", "smoke",
			"fr_FR", "fumée"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEMAGMA = new Text(
			"MISC_PYRPARTICLES_PARTICLEMAGMA", file,
			"en_US", "magma",
			"fr_FR", "magma"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLELOVE = new Text(
			"MISC_PYRPARTICLES_PARTICLELOVE", file,
			"en_US", "love",
			"fr_FR", "coeurs"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEENCHANTMENT = new Text(
			"MISC_PYRPARTICLES_PARTICLEENCHANTMENT", file,
			"en_US", "enchantment",
			"fr_FR", "enchantement"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLEMAGIC = new Text(
			"MISC_PYRPARTICLES_PARTICLEMAGIC", file,
			"en_US", "magic",
			"fr_FR", "magie"
			);

	public static final Text MISC_PYRPARTICLES_PARTICLERANDOM = new Text(
			"MISC_PYRPARTICLES_PARTICLERANDOM", file,
			"en_US", "random",
			"fr_FR", "aléatoire"
			);

	public static final Text MISC_PYRPARTICLES_TRAILSEA = new Text(
			"MISC_PYRPARTICLES_TRAILSEA", file,
			"en_US", "sea",
			"fr_FR", "mer"
			);

	public static final Text MISC_PYRPARTICLES_TRAILCARPET = new Text(
			"MISC_PYRPARTICLES_TRAILCARPET", file,
			"en_US", "carpet",
			"fr_FR", "tapis"
			);

	public static final Text MISC_PYRPARTICLES_TRAILWOOD = new Text(
			"MISC_PYRPARTICLES_TRAILWOOD", file,
			"en_US", "wood",
			"fr_FR", "bois"
			);

	public static final Text MISC_PYRPARTICLES_TRAILWOOL = new Text(
			"MISC_PYRPARTICLES_TRAILWOOL", file,
			"en_US", "wool",
			"fr_FR", "laine"
			);

	public static final Text MISC_PYRPARTICLES_TRAILCLAY = new Text(
			"MISC_PYRPARTICLES_TRAILCLAY", file,
			"en_US", "clay",
			"fr_FR", "argile"
			);

	public static final Text MISC_PYRPARTICLES_TRAILFROST = new Text(
			"MISC_PYRPARTICLES_TRAILFROST", file,
			"en_US", "frost",
			"fr_FR", "froid"
			);

	public static final Text MISC_PYRPARTICLES_TRAILPOLISHED = new Text(
			"MISC_PYRPARTICLES_TRAILPOLISHED", file,
			"en_US", "polished",
			"fr_FR", "polis"
			);

	public static final Text MISC_PYRPARTICLES_TRAILBEDROCK = new Text(
			"MISC_PYRPARTICLES_TRAILBEDROCK", file,
			"en_US", "bedrock",
			"fr_FR", "bedrock"
			);

	public static final Text MISC_PYRPARTICLES_TRAILPODZOL = new Text(
			"MISC_PYRPARTICLES_TRAILPODZOL", file,
			"en_US", "podzol",
			"fr_FR", "podzol"
			);

	public static final Text MISC_PYRPARTICLES_TRAILRICHES = new Text(
			"MISC_PYRPARTICLES_TRAILRICHES", file,
			"en_US", "riches",
			"fr_FR", "richesse"
			);

	public static final Text MISC_PYRPARTICLES_TRAILMINE = new Text(
			"MISC_PYRPARTICLES_TRAILMINE", file,
			"en_US", "mine",
			"fr_FR", "mine"
			);

	public static final Text MISC_PYRPARTICLES_TRAILNETHER = new Text(
			"MISC_PYRPARTICLES_TRAILNETHER", file,
			"en_US", "nether",
			"fr_FR", "nether"
			);

	public static final Text MISC_PYRPARTICLES_TRAILSANDSTONE = new Text(
			"MISC_PYRPARTICLES_TRAILSANDSTONE", file,
			"en_US", "sandstone",
			"fr_FR", "grès"
			);

	public static final Text MISC_PYRPARTICLES_TRAILGLASS = new Text(
			"MISC_PYRPARTICLES_TRAILGLASS", file,
			"en_US", "glass",
			"fr_FR", "verre"
			);

}
