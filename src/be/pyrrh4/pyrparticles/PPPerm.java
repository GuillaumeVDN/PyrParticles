package be.pyrrh4.pyrparticles;

import be.pyrrh4.pyrcore.lib.Perm;

public class PPPerm {

	public static final Perm PYRPARTICLES_ROOT = new Perm(null, "pyrparticles.*");
	public static final Perm PYRPARTICLES_ADMIN = new Perm(PYRPARTICLES_ROOT, "pyrparticles.admin");
	public static final Perm PYRPARTICLES_COMMAND_ROOT = new Perm(PYRPARTICLES_ROOT, "pyrparticles.command.*");
	public static final Perm PYRPARTICLES_COMMAND_GADGET = new Perm(PYRPARTICLES_COMMAND_ROOT, "pyrparticles.command.gadget");
	public static final Perm PYRPARTICLES_COMMAND_PARTICLE = new Perm(PYRPARTICLES_COMMAND_ROOT, "pyrparticles.command.particle");
	public static final Perm PYRPARTICLES_COMMAND_TRAIL = new Perm(PYRPARTICLES_COMMAND_ROOT, "pyrparticles.command.trail");

}
