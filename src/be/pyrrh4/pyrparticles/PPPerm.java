package be.pyrrh4.pyrparticles;

import be.pyrrh4.pyrcore.lib.Perm;

public class PPPerm {

	public static final Perm PYRPARTICLES_ROOT = new Perm(null, "pyrparticles.*");
	public static final Perm PYRPARTICLES_ADMIN = new Perm(PYRPARTICLES_ROOT, "pyrparticles.admin");

	public static final Perm PYRPARTICLES_COMMAND_ROOT = new Perm(PYRPARTICLES_ROOT, "pyrparticles.command.*");

	public static final Perm PYRPARTICLES_COMMAND_GADGET = new Perm(PYRPARTICLES_COMMAND_ROOT, "pyrparticles.command.gadget");
	public static final Perm PYRPARTICLES_COMMAND_GADGET_OTHERS = new Perm(PYRPARTICLES_COMMAND_GADGET, "pyrparticles.command.gadget.others");

	public static final Perm PYRPARTICLES_COMMAND_PARTICLE = new Perm(PYRPARTICLES_COMMAND_ROOT, "pyrparticles.command.particle");
	public static final Perm PYRPARTICLES_COMMAND_PARTICLE_OTHERS = new Perm(PYRPARTICLES_COMMAND_PARTICLE, "pyrparticles.command.particle.others");

	public static final Perm PYRPARTICLES_COMMAND_TRAIL = new Perm(PYRPARTICLES_COMMAND_ROOT, "pyrparticles.command.trail");
	public static final Perm PYRPARTICLES_COMMAND_TRAIL_OTHERS = new Perm(PYRPARTICLES_COMMAND_TRAIL, "pyrparticles.command.trail.others");

}
