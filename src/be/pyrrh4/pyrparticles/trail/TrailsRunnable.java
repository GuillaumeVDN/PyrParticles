package be.pyrrh4.pyrparticles.trail;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import be.pyrrh4.core.User;
import be.pyrrh4.core.material.Mat;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.PyrParticlesUser;
import be.pyrrh4.pyrparticles.util.ChangedBlock;

public class TrailsRunnable implements Runnable {

	@Override
	public void run() {
		// for every player
		for (Player player : Utils.getOnlinePlayers()) {
			PyrParticlesUser data = User.from(player).getPluginData(PyrParticlesUser.class);
			// that has a trail
			if (data.getTrail() != null) {
				// if world is allowed
				if (PyrParticles.instance().getEnabledWorlds().isEmpty() || PyrParticles.instance().getEnabledWorlds().contains(player.getWorld().getName())) {
					// check if can apply trail
					Trail trail = data.getTrail();
					Block block = trail.isUpperBlock() ? player.getLocation().getBlock() : player.getLocation().getBlock().getRelative(BlockFace.DOWN);
					if (player.isOnGround() && (trail.isUpperBlock() ? Mat.AIR.isMat(block) && block.getRelative(BlockFace.DOWN).getType().isSolid() : block.getType().isSolid())) {
						// remove previous blocks there first
						PyrParticles.instance().removeTrailBlocksAt(block.getLocation());
						// send block change
						Mat next = trail.nextType();
						ArrayList<Player> affectedPlayers = Utils.asList(block.getWorld().getPlayers());
						next.setBlockChange(block, affectedPlayers);
						// add it to previous blocks so it'll be restored later
						PyrParticles.instance().getTrailBlocks().add(new ChangedBlock(block, affectedPlayers));
					}
				}
			}
		}
	}

}
