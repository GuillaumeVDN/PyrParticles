package be.pyrrh4.pyrparticles.util;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import be.pyrrh4.core.util.Utils;

public class RandomMaterial {

	// fields and constructor
	private Material type;
	private int maxData;

	public RandomMaterial(Material type, int maxData) {
		this.type = type;
		this.maxData = maxData;
	}

	// getters
	public Material getType() {
		return type;
	}

	public int getMaxData() {
		return maxData;
	}

	public MaterialData getNext() {
		return new MaterialData(type, (byte) (maxData == 0 ? 0 : Utils.random(maxData)));
	}

}
