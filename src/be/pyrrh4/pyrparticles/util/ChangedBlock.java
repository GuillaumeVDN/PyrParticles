package be.pyrrh4.pyrparticles.util;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ChangedBlock {

	// fields and constructor
	private Block block;
	private ArrayList<Player> affected;
	private long changed;

	public ChangedBlock(Block block, ArrayList<Player> affected) {
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

	public void restore() {
		for (Player pl : affected) {
			if (pl.isOnline()) {
				pl.sendBlockChange(block.getLocation(), block.getTypeId(), block.getData());
			}
		}
	}
}