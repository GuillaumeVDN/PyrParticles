package be.pyrrh4.pyrparticles.particle;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import be.pyrrh4.core.User;
import be.pyrrh4.core.material.Mat;
import be.pyrrh4.core.messenger.Locale;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.core.versioncompat.particle.ParticleManager.Type;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.PyrParticlesUser;

public enum ParticleEffect {

	WATER(Type.DRIP_WATER, Mat.WATER_BUCKET),
	BUBBLE(Type.WATER_BUBBLE, Mat.OAK_BOAT),
	LAVA(Type.WATER_WAKE, Mat.LAVA_BUCKET),
	MAGMA(Type.LAVA, Mat.BLAZE_POWDER),
	FIRE(Type.FLAME, Mat.FIRE_CHARGE),
	SMOKE(Type.SMOKE_NORMAL, Mat.FURNACE),
	MAGIC(Type.SPELL_WITCH, Mat.CAULDRON),
	COLOR(Type.SPELL_MOB, Mat.LIME_DYE),
	SPELL(Type.SPELL_INSTANT, Mat.SUGAR),
	SPARKS(Type.FIREWORKS_SPARK, Mat.NETHER_STAR),
	LOVE(Type.HEART, -0.15D, Mat.ROSE_RED),
	MUSIC(Type.NOTE, -0.15D, Mat.JUKEBOX),
	HAPPY(Type.VILLAGER_HAPPY, Mat.EMERALD),
	ANGRY(Type.VILLAGER_ANGRY, -0.45D, Mat.ANVIL),
	ENCHANTMENT(Type.ENCHANTMENT_TABLE, Mat.ENCHANTING_TABLE),
	SUSPENDED(Type.SUSPENDED_DEPTH, Mat.STONE),
	CLOUD(Type.CLOUD, Mat.BONE_MEAL),
	ENDER(Type.PORTAL, Mat.ENDER_EYE),
	REDSTONE(Type.REDSTONE, Mat.REDSTONE),
	SLIME(Type.SLIME, Mat.SLIME_BALL),
	SNOWBALL(Type.SNOWBALL, Mat.SNOWBALL),
	SHOVEL(Type.SNOW_SHOVEL, Mat.DIAMOND_SHOVEL),
	RANDOM(null, Mat.DRAGON_EGG);

	// constructor
	private Type particleType;
	private double verticalCompensation;
	private Mat guiItemType;
	private int guiItemData;

	private ParticleEffect(Type particleType, Mat guiItemType) {
		this(particleType, 0.0D, guiItemType);
	}

	private ParticleEffect(Type particleType, Mat guiItemType, int guiItemData) {
		this(particleType, 0.0D, guiItemType, guiItemData);
	}

	private ParticleEffect(Type particleType, double verticalCompensation, Mat guiItemType) {
		this(particleType, verticalCompensation, guiItemType, 0);
	}

	private ParticleEffect(Type particleType, double verticalCompensation, Mat guiItemType, int guiItemData) {
		this.particleType = particleType;
		this.verticalCompensation = verticalCompensation;
		this.guiItemType = guiItemType;
		this.guiItemData = guiItemData;
	}

	// methods
	public Type getParticleType() {
		return particleType;
	}

	public double getVerticalCompensation() {
		return verticalCompensation;
	}

	public Type getNextParticleType() {
		if (particleType == null) {
			ArrayList<ParticleEffect> values = Utils.asList(values());
			for (int i = 0; i < 15; i++) {
				ParticleEffect particleEffect = Utils.random(values);
				if (particleEffect.getParticleType() != null) {
					return particleEffect.getParticleType();
				}
			}
			return WATER.getNextParticleType();
		} else {
			return particleType;
		}
	}

	public Mat getGuiItemType() {
		return guiItemType;
	}

	public int getGuiItemData() {
		return guiItemData;
	}

	public String getName() {
		return Utils.valueOfOrNull(Locale.class, "MISC_PYRPARTICLES_PARTICLE" + name().replace("_", "")).getActive().getLine();
	}

	public boolean hasPermission(Player player) {
		return player.isOp() || player.hasPermission("pyrparticles.particle.*") || player.hasPermission("pyrparticles.particle." + toString().toLowerCase());
	}

	public void start(Player player) {
		// permission
		if (!hasPermission(player)) {
			Locale.MSG_GENERIC_NOPERMISSION.getActive().send(player, "{plugin}", PyrParticles.instance());
			return;
		}
		// start
		PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
		data.setParticleEffect(this);
		Locale.MSG_PYRPARTICLES_PARTICLEENABLE.getActive().send(player, "{particle}", getName());
	}

	// stop
	public static void stop(Player player) {
		PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
		data.setParticleEffect(null);
		Locale.MSG_PYRPARTICLES_PARTICLEDISABLE.getActive().send(player);
	}

}
