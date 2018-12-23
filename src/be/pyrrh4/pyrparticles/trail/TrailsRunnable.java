package be.pyrrh4.pyrparticles.trail;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.data.PPUser;
import be.pyrrh4.pyrparticles.util.ChangedBlock;

public class TrailsRunnable implements Runnable {

	@Override
	public void run() {
		// for every player
		for (Player player : Utils.getOnlinePlayers()) {
			PPUser user = PyrParticles.inst().getData().getUsers().getElement(player);
			// that has a trail
			if (user.getTrail() != null) {
				// if world is allowed
				if (PyrParticles.inst().getEnabledWorlds().isEmpty() || PyrParticles.inst().getEnabledWorlds().contains(player.getWorld().getName())) {
					// check if can apply trail
					Trail trail = user.getTrail();
					Block block = trail.isUpperBlock() ? player.getLocation().getBlock() : player.getLocation().getBlock().getRelative(BlockFace.DOWN);
					if (player.isOnGround() && (trail.isUpperBlock() ? Mat.AIR.isMat(block) && block.getRelative(BlockFace.DOWN).getType().isSolid() : block.getType().isSolid())) {
						// remove previous blocks there first
						PyrParticles.inst().removeTrailBlocksAt(block.getLocation());
						// send block change
						Mat next = trail.nextType();
						List<Player> affectedPlayers = Utils.asList(block.getWorld().getPlayers());
						next.setBlockChange(block, affectedPlayers);
						// add it to previous blocks so it'll be restored later
						PyrParticles.inst().getTrailBlocks().add(new ChangedBlock(block, affectedPlayers));
					}
				}
			}
		}
	}

}
