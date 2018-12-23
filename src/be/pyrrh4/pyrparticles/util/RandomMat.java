package be.pyrrh4.pyrparticles.util;

import java.util.ArrayList;
import java.util.List;

import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.util.Utils;

public class RandomMat {

	// fields and constructor
	private List<Mat> mats = new ArrayList<Mat>();

	public RandomMat(List<Mat> mats) {
		this.mats = mats;
	}

	public RandomMat(Mat... mats) {
		if (mats != null) {
			this.mats = Utils.asList(mats);
		}
	}

	// methods
	public Mat next() {
		return Utils.random(mats);
	}

}
