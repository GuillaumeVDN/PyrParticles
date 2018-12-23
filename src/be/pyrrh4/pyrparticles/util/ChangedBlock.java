package be.pyrrh4.pyrparticles.util;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ChangedBlock {

	// fields and constructor
	private Block block;
	private List<Player> affected;
	private long changed;

	public ChangedBlock(Block block, List<Player> affected) {
		this.block = block;
		this.affected = affected;
		this.changed = System.currentTimeMillis();
	}

	// methods
	public Block getBlock() {
		return block;
	}

	public long getDate() {
		return changed;
	}

	@SuppressWarnings("deprecation")
	public void restore() {
		for (Player pl : affected) {
			if (pl.isOnline()) {
				pl.sendBlockChange(block.getLocation(), block.getType(), block.getData());
			}
		}
	}
}