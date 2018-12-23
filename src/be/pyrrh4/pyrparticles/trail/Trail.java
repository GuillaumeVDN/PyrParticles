package be.pyrrh4.pyrparticles.trail;

import java.util.List;

import org.bukkit.entity.Player;

import be.pyrrh4.pyrcore.PCLocale;
import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.messenger.Text;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PPLocale;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.data.PPUser;
import be.pyrrh4.pyrparticles.util.RandomMat;

public enum Trail {

	CARPET(true, Mat.CYAN_CARPET, new RandomMat(Mat.WHITE_CARPET, Mat.BLACK_CARPET, Mat.BLUE_CARPET, Mat.BROWN_CARPET, Mat.CYAN_CARPET, Mat.GRAY_CARPET, Mat.GREEN_CARPET, Mat.LIGHT_BLUE_CARPET, Mat.LIGHT_GRAY_CARPET, Mat.LIME_CARPET, Mat.MAGENTA_CARPET, Mat.ORANGE_CARPET, Mat.PINK_CARPET, Mat.PURPLE_CARPET, Mat.RED_CARPET, Mat.YELLOW_CARPET)),
	WOOL(Mat.PURPLE_WOOL, new RandomMat(Mat.WHITE_WOOL, Mat.BLACK_WOOL, Mat.BLUE_WOOL, Mat.BROWN_WOOL, Mat.CYAN_WOOL, Mat.GRAY_WOOL, Mat.GREEN_WOOL, Mat.LIGHT_BLUE_WOOL, Mat.LIGHT_GRAY_WOOL, Mat.LIME_WOOL, Mat.MAGENTA_WOOL, Mat.ORANGE_WOOL, Mat.PINK_WOOL, Mat.PURPLE_WOOL, Mat.RED_WOOL, Mat.YELLOW_WOOL)),
	GLASS(Mat.ORANGE_STAINED_GLASS, new RandomMat(Mat.WHITE_STAINED_GLASS, Mat.BLACK_STAINED_GLASS, Mat.BLUE_STAINED_GLASS, Mat.BROWN_STAINED_GLASS, Mat.CYAN_STAINED_GLASS, Mat.GRAY_STAINED_GLASS, Mat.GREEN_STAINED_GLASS, Mat.LIGHT_BLUE_STAINED_GLASS, Mat.LIGHT_GRAY_STAINED_GLASS, Mat.LIME_STAINED_GLASS, Mat.MAGENTA_STAINED_GLASS, Mat.ORANGE_STAINED_GLASS, Mat.PINK_STAINED_GLASS, Mat.PURPLE_STAINED_GLASS, Mat.RED_STAINED_GLASS, Mat.YELLOW_STAINED_GLASS)),
	CLAY(Mat.YELLOW_CONCRETE, new RandomMat(Mat.WHITE_CONCRETE, Mat.BLACK_CONCRETE, Mat.BLUE_CONCRETE, Mat.BROWN_CONCRETE, Mat.CYAN_CONCRETE, Mat.GRAY_CONCRETE, Mat.GREEN_CONCRETE, Mat.LIGHT_BLUE_CONCRETE, Mat.LIGHT_GRAY_CONCRETE, Mat.LIME_CONCRETE, Mat.MAGENTA_CONCRETE, Mat.ORANGE_CONCRETE, Mat.PINK_CONCRETE, Mat.PURPLE_CONCRETE, Mat.RED_CONCRETE, Mat.YELLOW_CONCRETE)),
	FROST(Mat.SNOW_BLOCK, new RandomMat(Mat.PACKED_ICE)),
	PODZOL(Mat.DIRT, new RandomMat(Mat.DIRT, Mat.COARSE_DIRT)),
	POLISHED(Mat.STONE, new RandomMat(Mat.STONE, Mat.DIORITE, Mat.ANDESITE)),
	WOOD(Mat.OAK_WOOD, new RandomMat(Mat.ACACIA_LOG, Mat.BIRCH_LOG, Mat.DARK_OAK_LOG, Mat.JUNGLE_LOG, Mat.OAK_LOG, Mat.SPRUCE_LOG)),
	BEDROCK(Mat.BEDROCK, new RandomMat(Mat.BEDROCK)),
	SEA(Mat.SPONGE, new RandomMat(Mat.SEA_LANTERN)),
	RICHES(Mat.GOLD_BLOCK, new RandomMat(Mat.REDSTONE_BLOCK)),
	MINE(Mat.OBSIDIAN, new RandomMat(Mat.COAL_BLOCK)),
	NETHER(Mat.NETHERRACK, new RandomMat(Mat.NETHER_BRICK)),
	SANDSTONE(Mat.SANDSTONE, new RandomMat(Mat.RED_SANDSTONE));

	// constructor
	private List<RandomMat> types;
	private boolean upperBlock;
	private Mat guiItemType;
	private String name;

	private Trail(Mat guiItemType, RandomMat... types) {
		this(false, guiItemType, types);
	}

	private Trail(boolean upperBlock, Mat guiItemType, RandomMat... types) {
		this.upperBlock = upperBlock;
		this.guiItemType = guiItemType;
		this.types = Utils.asList(types);
		Text text = Text.valueOf("MISC_PYRPARTICLES_TRAIL" + name().replace("_", ""));
		this.name = text == null ? name() : text.getLine();
	}

	// getters
	public Mat nextType() {
		return Utils.random(types).next();
	}

	public Mat getGuiItemType() {
		return guiItemType;
	}

	public boolean isUpperBlock() {
		return upperBlock;
	}

	public String getName() {
		return name;
	}

	public boolean hasPermission(Player player) {
		return player.isOp() || player.hasPermission("pyrparticles.trail.*") || player.hasPermission("pyrparticles.trail." + toString().toLowerCase());
	}

	public void start(Player player) {
		// permission
		if (!hasPermission(player)) {
			PCLocale.MSG_GENERIC_NOPERMISSION.send(player, "{plugin}", PyrParticles.inst().getName());
			return;
		}
		// start
		PPUser user = PyrParticles.inst().getData().getUsers().getElement(player);
		user.setTrail(this);
		PPLocale.MSG_PYRPARTICLES_TRAILENABLE.send(player, "{trail}", getName());
	}

	// stop
	public static void stop(Player player) {
		PPUser user = PyrParticles.inst().getData().getUsers().getElement(player);
		user.setTrail(null);
		PPLocale.MSG_PYRPARTICLES_TRAILDISABLE.send(player);
	}

}
