package be.pyrrh4.pyrparticles.particle;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import be.pyrrh4.core.User;
import be.pyrrh4.core.compat.particle.ParticleManager.Type;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.PyrParticlesUser;

public enum ParticleEffect {

	WATER(Type.DRIP_WATER, Material.WATER_BUCKET),
	BUBBLE(Type.WATER_BUBBLE, Material.BOAT),
	LAVA(Type.WATER_WAKE, Material.LAVA_BUCKET),
	MAGMA(Type.LAVA, Material.BLAZE_POWDER),
	FIRE(Type.FLAME, Material.FIREBALL),
	SMOKE(Type.SMOKE_NORMAL, Material.FURNACE),
	MAGIC(Type.SPELL_WITCH, Material.CAULDRON_ITEM),
	COLOR(Type.SPELL_MOB, Material.INK_SACK, 10),
	SPELL(Type.SPELL_INSTANT, Material.SUGAR),
	SPARKS(Type.FIREWORKS_SPARK, Material.NETHER_STAR),
	LOVE(Type.HEART, -0.15D, Material.RED_ROSE),
	MUSIC(Type.NOTE, -0.15D, Material.JUKEBOX),
	HAPPY(Type.VILLAGER_HAPPY, Material.EMERALD),
	ANGRY(Type.VILLAGER_ANGRY, -0.45D, Material.ANVIL),
	ENCHANTMENT(Type.ENCHANTMENT_TABLE, Material.ENCHANTMENT_TABLE),
	SUSPENDED(Type.SUSPENDED_DEPTH, Material.STONE),
	CLOUD(Type.CLOUD, Material.INK_SACK, 15),
	ENDER(Type.PORTAL, Material.EYE_OF_ENDER),
	REDSTONE(Type.REDSTONE, Material.REDSTONE),
	SLIME(Type.SLIME, Material.SLIME_BALL),
	SNOWBALL(Type.SNOWBALL, Material.SNOW_BALL),
	SHOVEL(Type.SNOW_SHOVEL, Material.DIAMOND_SPADE),
	RANDOM(null, Material.DRAGON_EGG);

	// constructor
	private Type particleType;
	private double verticalCompensation;
	private Material guiItemType;
	private int guiItemData;

	private ParticleEffect(Type particleType, Material guiItemType) {
		this(particleType, 0.0D, guiItemType);
	}

	private ParticleEffect(Type particleType, Material guiItemType, int guiItemData) {
		this(particleType, 0.0D, guiItemType, guiItemData);
	}

	private ParticleEffect(Type particleType, double verticalCompensation, Material guiItemType) {
		this(particleType, verticalCompensation, guiItemType, 0);
	}

	private ParticleEffect(Type particleType, double verticalCompensation, Material guiItemType, int guiItemData) {
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

	public Material getGuiItemType() {
		return guiItemType;
	}

	public int getGuiItemData() {
		return guiItemData;
	}

	public String getName() {
		return PyrParticles.instance().getLocale().getMessage("particle_" + toString().toLowerCase()).getLines().get(0);
	}

	public boolean hasPermission(Player player) {
		return player.isOp() || player.hasPermission("pp.particle.*") || player.hasPermission("pp.particle." + toString().toLowerCase());
	}

	public void start(Player player) {
		// permission
		if (!hasPermission(player)) {
			PyrParticles.instance().getLocale().getMessage("locked").send(player);
			return;
		}
		// start
		PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
		data.setParticleEffect(this);
		data.save();
		PyrParticles.instance().getLocale().getMessage("particle_enable").send(player, "$PARTICLE", getName());
	}

	// stop
	public static void stop(Player player) {
		PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
		data.setParticleEffect(null);
		data.save();
		PyrParticles.instance().getLocale().getMessage("particle_disable").send(player);
	}

}
