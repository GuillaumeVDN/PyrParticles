package be.pyrrh4.pyrparticles.trail;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import be.pyrrh4.core.User;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.PyrParticlesUser;
import be.pyrrh4.pyrparticles.util.RandomMaterial;

public enum Trail {

	CARPET(true, Material.CARPET, 9, new RandomMaterial("CARPET", 15)),
	WOOL(Material.WOOL, 10, new RandomMaterial("WOOL", 15)),
	GLASS(Material.STAINED_GLASS, 1, new RandomMaterial("STAINED_GLASS", 15)),
	CLAY(Material.STAINED_CLAY, 4, new RandomMaterial("STAINED_CLAY", 15)),
	FROST(Material.SNOW_BLOCK, new RandomMaterial("SNOW_BLOCK", 0), new RandomMaterial("PACKED_ICE", 0)),
	PODZOL(Material.DIRT, 2, new RandomMaterial("DIRT", 2)),
	POLISHED(Material.STONE, 4, new RandomMaterial("STONE", 6)),
	WOOD(Material.WOOD, new RandomMaterial("WOOD", 4), new RandomMaterial("LOG", 3)),
	BEDROCK(Material.BEDROCK, new RandomMaterial("BEDROCK", 0)),
	SEA(Material.SPONGE, new RandomMaterial("SPONGE", 0), new RandomMaterial("PRISMARINE", 0), new RandomMaterial("SEA_LANTERN", 0)),
	RICHES(Material.GOLD_BLOCK, new RandomMaterial("GOLD_BLOCK", 0), new RandomMaterial("DIAMOND_BLOCK", 0), new RandomMaterial("LAPIS_BLOCK", 0), new RandomMaterial("REDSTONE_BLOCK", 0)),
	MINE(Material.OBSIDIAN, new RandomMaterial("OBSIDIAN", 0), new RandomMaterial("COAL_BLOCK", 0)),
	NETHER(Material.NETHERRACK, new RandomMaterial("NETHERRACK", 0), new RandomMaterial("NETHER_BRICK", 0)),
	SANDSTONE(Material.SANDSTONE, new RandomMaterial("SANDSTONE", 0), new RandomMaterial("RED_SANDSTONE", 0));

	// constructor
	private ArrayList<RandomMaterial> types;
	private boolean upperBlock;
	private Material guiItemType;
	private int guiItemData;

	private Trail(Material guiItemType, RandomMaterial... types) {
		this(false, guiItemType, 0, types);
	}

	private Trail(boolean upperBlock, Material guiItemType, RandomMaterial... types) {
		this(upperBlock, guiItemType, 0, types);
	}

	private Trail(Material guiItemType, int guiItemData, RandomMaterial... types) {
		this(false, guiItemType, guiItemData, types);
	}

	private Trail(boolean upperBlock, Material guiItemType, int guiItemData, RandomMaterial... types) {
		this.upperBlock = upperBlock;
		this.guiItemType = guiItemType;
		this.guiItemData = guiItemData;
		this.types = Utils.asList(types);
	}

	// getters

	public boolean canUse() {
		for (RandomMaterial type : types) {
			if (type.exists()) {
				return true;
			}
		}
		return false;
	}

	public MaterialData nextType() {
		return Utils.random(types).next();
	}

	public Material getGuiItemType() {
		return guiItemType;
	}

	public int getGuiItemData() {
		return guiItemData;
	}

	public boolean isUpperBlock() {
		return upperBlock;
	}

	public String getName() {
		return PyrParticles.instance().getLocale().getMessage("trail_" + toString().toLowerCase()).getLines().get(0);
	}

	public boolean hasPermission(Player player) {
		return player.isOp() || player.hasPermission("pp.trail.*") || player.hasPermission("pp.trail." + toString().toLowerCase());
	}

	public void start(Player player) {
		// permission
		if (!hasPermission(player)) {
			PyrParticles.instance().getLocale().getMessage("locked").send(player);
			return;
		}
		// start
		PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
		data.setTrail(this);
		data.save();
		PyrParticles.instance().getLocale().getMessage("trail_enable").send(player, "$TRAIL", getName());
	}

	// stop
	public static void stop(Player player) {
		PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
		data.setTrail(null);
		data.save();
		PyrParticles.instance().getLocale().getMessage("trail_disable").send(player);
	}

}
