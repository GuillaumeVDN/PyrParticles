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

	CARPET(true, Material.CARPET, 9, new RandomMaterial(Material.CARPET, 15)),
	WOOL(Material.WOOL, 10, new RandomMaterial(Material.WOOL, 15)),
	GLASS(Material.STAINED_GLASS, 1, new RandomMaterial(Material.STAINED_GLASS, 15)),
	CLAY(Material.STAINED_CLAY, 4, new RandomMaterial(Material.STAINED_CLAY, 15)),
	FROST(Material.SNOW_BLOCK, new RandomMaterial(Material.SNOW_BLOCK, 0), new RandomMaterial(Material.PACKED_ICE, 0)),
	PODZOL(Material.DIRT, 2, new RandomMaterial(Material.DIRT, 2)),
	POLISHED(Material.STONE, 4, new RandomMaterial(Material.STONE, 6)),
	WOOD(Material.WOOD, new RandomMaterial(Material.WOOD, 4), new RandomMaterial(Material.LOG, 3)),
	BEDROCK(Material.BEDROCK, new RandomMaterial(Material.BEDROCK, 0)),
	SEA(Material.SPONGE, new RandomMaterial(Material.SPONGE, 0), new RandomMaterial(Material.PRISMARINE, 0), new RandomMaterial(Material.SEA_LANTERN, 0)),
	RICHES(Material.GOLD_BLOCK, new RandomMaterial(Material.GOLD_BLOCK, 0), new RandomMaterial(Material.DIAMOND_BLOCK, 0), new RandomMaterial(Material.LAPIS_BLOCK, 0), new RandomMaterial(Material.REDSTONE_BLOCK, 0)),
	MINE(Material.OBSIDIAN, new RandomMaterial(Material.OBSIDIAN, 0), new RandomMaterial(Material.COAL_BLOCK, 0)),
	NETHER(Material.NETHERRACK, new RandomMaterial(Material.NETHERRACK, 0), new RandomMaterial(Material.NETHER_BRICK, 0)),
	SANDSTONE(Material.SANDSTONE, new RandomMaterial(Material.SANDSTONE, 0), new RandomMaterial(Material.RED_SANDSTONE, 0));

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

	public MaterialData getNextType() {
		return Utils.random(types).getNext();
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
