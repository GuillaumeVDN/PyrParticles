package be.pyrrh4.pyrparticles;

import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.util.ChangedBlock;

public class MainRunnable implements Runnable {

	@Override
	public void run() {
		// restore trail blocks
		for (ChangedBlock tb : Utils.asList(PyrParticles.instance().getTrailBlocks())) {
			if (System.currentTimeMillis() - tb.getDate() >= (long) (PyrParticles.instance().getTrailsPersistence() * 1000)) {
				tb.restore();
				PyrParticles.instance().getTrailBlocks().remove(tb);
			}
		}

		// restore colorgun blocks
		for (ChangedBlock tb : Utils.asList(PyrParticles.instance().getColorGunBlocks())) {
			if (System.currentTimeMillis() - tb.getDate() >= (long) (PyrParticles.instance().getColorGunPersistence() * 1000)) {
				tb.restore();
				PyrParticles.instance().getColorGunBlocks().remove(tb);
			}
		}
	}

}
