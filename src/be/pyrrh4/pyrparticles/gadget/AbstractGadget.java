package be.pyrrh4.pyrparticles.gadget;

import org.bukkit.entity.Player;

public abstract class AbstractGadget {

	protected Gadget type;
	protected Player player;

	protected AbstractGadget(Gadget type, Player player) {
		this.type = type;
		this.player = player;
	}

	public Gadget getType() {
		return type;
	}

	public Player getPlayer() {
		return player;
	}

	public abstract void start();
	public abstract void stop();

}
