package be.pyrrh4.pyrparticles.util;

import java.util.ArrayList;

import be.pyrrh4.core.material.Mat;
import be.pyrrh4.core.util.Utils;

public class RandomMat {

	// fields and constructor
	private ArrayList<Mat> mats = new ArrayList<Mat>();

	public RandomMat(ArrayList<Mat> mats) {
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
