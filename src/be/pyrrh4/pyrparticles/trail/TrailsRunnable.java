package be.pyrrh4.pyrparticles.trail;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import be.pyrrh4.core.User;
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
					if (player.isOnGround() && (trail.isUpperBlock() ? block.getTypeId() == 0 && block.getRelative(BlockFace.DOWN).getType().isSolid() : block.getType().isSolid())) {
						// remove previous blocks there first
						PyrParticles.instance().removeTrailBlocksAt(block.getLocation());
						// send block change
						MaterialData next = trail.getNextType();
						for (Player pl : block.getWorld().getPlayers()) {
							pl.sendBlockChange(block.getLocation(), next.getItemTypeId(), next.getData());
						}
						// add it to previous blocks so it'll be restored later
						PyrParticles.instance().getTrailBlocks().add(new ChangedBlock(block, Utils.asList(block.getWorld().getPlayers())));
					}
				}
			}
		}
	}

}
