package be.pyrrh4.pyrparticles;

import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.util.ChangedBlock;

public class MainRunnable implements Runnable {

	@Override
	public void run() {
		// restore trail blocks
		for (ChangedBlock tb : Utils.asList(PyrParticles.inst().getTrailBlocks())) {
			if (System.currentTimeMillis() - tb.getDate() >= (long) (PyrParticles.inst().getTrailsPersistence() * 1000)) {
				tb.restore();
				PyrParticles.inst().getTrailBlocks().remove(tb);
			}
		}

		// restore colorgun blocks
		for (ChangedBlock tb : Utils.asList(PyrParticles.inst().getColorGunBlocks())) {
			if (System.currentTimeMillis() - tb.getDate() >= (long) (PyrParticles.inst().getColorGunPersistence() * 1000)) {
				tb.restore();
				PyrParticles.inst().getColorGunBlocks().remove(tb);
			}
		}
	}

}
