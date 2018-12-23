package be.pyrrh4.pyrparticles.particle;

import java.util.List;

import org.bukkit.entity.Player;

import be.pyrrh4.pyrcore.PCLocale;
import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.messenger.Text;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrcore.lib.versioncompat.particle.ParticleManager.Type;
import be.pyrrh4.pyrparticles.PPLocale;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.data.PPUser;

public enum ParticleEffect {

	WATER(Type.DRIP_WATER, Mat.WATER_BUCKET),
	BUBBLE(Type.WATER_BUBBLE, Mat.OAK_BOAT),
	LAVA(Type.DRIP_LAVA, Mat.LAVA_BUCKET),
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
	private String name;

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
		Text text = Text.valueOf("MISC_PYRPARTICLES_PARTICLE" + name().replace("_", ""));
		this.name = text == null ? name() : text.getLine();
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
			List<ParticleEffect> values = Utils.asList(values());
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
		return name;
	}

	public boolean hasPermission(Player player) {
		return player.isOp() || player.hasPermission("pyrparticles.particle.*") || player.hasPermission("pyrparticles.particle." + toString().toLowerCase());
	}

	public void start(Player player) {
		// permission
		if (!hasPermission(player)) {
			PCLocale.MSG_GENERIC_NOPERMISSION.send(player, "{plugin}", PyrParticles.inst());
			return;
		}
		// start
		PPUser user = PyrParticles.inst().getData().getUsers().getElement(player);
		user.setParticleEffect(this);
		PPLocale.MSG_PYRPARTICLES_PARTICLEENABLE.send(player, "{particle}", getName());
	}

	// stop
	public static void stop(Player player) {
		PPUser user = PyrParticles.inst().getData().getUsers().getElement(player);
		user.setParticleEffect(null);
		PPLocale.MSG_PYRPARTICLES_PARTICLEDISABLE.send(player);
	}

}
