package be.pyrrh4.pyrparticles.util;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import be.pyrrh4.core.util.Utils;

public class RandomMaterial {

	// fields and constructor
	private String type;
	private int maxData;

	public RandomMaterial(String type, int maxData) {
		this.type = type;
		this.maxData = maxData;
	}

	// getters
	public Material type() {
		return Utils.valueOfOrNull(Material.class, type);
	}

	public int maxData() {
		return maxData;
	}

	public MaterialData next() {
		return new MaterialData(type(), (byte) (maxData == 0 ? 0 : Utils.random(maxData)));
	}

	public boolean exists() {
		return type != null;
	}

}
