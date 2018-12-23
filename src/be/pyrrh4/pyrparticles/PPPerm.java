package be.pyrrh4.pyrparticles;

import be.pyrrh4.pyrcore.lib.Perm;

public class PPPerm {

	public static final Perm PYRPARTICLES_ROOT = new Perm("PYRPARTICLES_ROOT", null, "pyrparticles");
	public static final Perm PYRPARTICLES_ADMIN = new Perm("PYRPARTICLES_ADMIN", PYRPARTICLES_ROOT, "admin");
	public static final Perm PYRPARTICLES_COMMAND_ROOT = new Perm("PYRPARTICLES_COMMAND_ROOT", PYRPARTICLES_ROOT, "command");
	public static final Perm PYRPARTICLES_COMMAND_GADGET = new Perm("PYRPARTICLES_COMMAND_GADGET", PYRPARTICLES_COMMAND_ROOT, "gadget");
	public static final Perm PYRPARTICLES_COMMAND_PARTICLE = new Perm("PYRPARTICLES_COMMAND_PARTICLE", PYRPARTICLES_COMMAND_ROOT, "particle");
	public static final Perm PYRPARTICLES_COMMAND_TRAIL = new Perm("PYRPARTICLES_COMMAND_TRAIL", PYRPARTICLES_COMMAND_ROOT, "trail");

}
