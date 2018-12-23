package be.pyrrh4.pyrparticles.gadget;

import org.bukkit.entity.Player;

import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrparticles.util.RandomMat;

public abstract class AbstractGadget {

	// fields and constructors
	public static final RandomMat RANDOM_WOOL = new RandomMat(Mat.WHITE_WOOL, Mat.BLACK_WOOL, Mat.BLUE_WOOL, Mat.BROWN_WOOL, Mat.CYAN_WOOL, Mat.GRAY_WOOL, Mat.GREEN_WOOL, Mat.LIGHT_BLUE_WOOL, Mat.LIGHT_GRAY_WOOL, Mat.LIME_WOOL, Mat.MAGENTA_WOOL, Mat.ORANGE_WOOL, Mat.PINK_WOOL, Mat.PURPLE_WOOL, Mat.RED_WOOL, Mat.YELLOW_WOOL);
	public static final RandomMat RANDOM_CARPET = new RandomMat(Mat.WHITE_CARPET, Mat.BLACK_CARPET, Mat.BLUE_CARPET, Mat.BROWN_CARPET, Mat.CYAN_CARPET, Mat.GRAY_CARPET, Mat.GREEN_CARPET, Mat.LIGHT_BLUE_CARPET, Mat.LIGHT_GRAY_CARPET, Mat.LIME_CARPET, Mat.MAGENTA_CARPET, Mat.ORANGE_CARPET, Mat.PINK_CARPET, Mat.PURPLE_CARPET, Mat.RED_CARPET, Mat.YELLOW_CARPET);
	public static final RandomMat RANDOM_STAINED_GLASS = new RandomMat(Mat.WHITE_STAINED_GLASS, Mat.BLACK_STAINED_GLASS, Mat.BLUE_STAINED_GLASS, Mat.BROWN_STAINED_GLASS, Mat.CYAN_STAINED_GLASS, Mat.GRAY_STAINED_GLASS, Mat.GREEN_STAINED_GLASS, Mat.LIGHT_BLUE_STAINED_GLASS, Mat.LIGHT_GRAY_STAINED_GLASS, Mat.LIME_STAINED_GLASS, Mat.MAGENTA_STAINED_GLASS, Mat.ORANGE_STAINED_GLASS, Mat.PINK_STAINED_GLASS, Mat.PURPLE_STAINED_GLASS, Mat.RED_STAINED_GLASS, Mat.YELLOW_STAINED_GLASS);
	public static final RandomMat RANDOM_DISK = new RandomMat(Mat.MUSIC_DISC_11, Mat.MUSIC_DISC_13, Mat.MUSIC_DISC_BLOCKS, Mat.MUSIC_DISC_BLOCKS, Mat.MUSIC_DISC_CAT, Mat.MUSIC_DISC_CHIRP, Mat.MUSIC_DISC_FAR, Mat.MUSIC_DISC_MALL, Mat.MUSIC_DISC_MELLOHI, Mat.MUSIC_DISC_STAL, Mat.MUSIC_DISC_STRAD, Mat.MUSIC_DISC_WAIT, Mat.MUSIC_DISC_WARD);

	// fields and constructor
	protected Gadget type;
	protected Player player;

	protected AbstractGadget(Gadget type, Player player) {
		this.type = type;
		this.player = player;
	}

	// methods
	public Gadget getType() {
		return type;
	}

	public Player getPlayer() {
		return player;
	}

	// abstract methods
	public abstract void start();
	public abstract void stop();

}
