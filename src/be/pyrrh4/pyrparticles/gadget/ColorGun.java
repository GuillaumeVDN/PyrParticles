package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import be.pyrrh4.pyrcore.lib.gui.ItemData;
import be.pyrrh4.pyrcore.lib.material.Mat;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.util.ChangedBlock;

public class ColorGun extends AbstractGadget implements Listener {

	// static fields
	private static final ItemData gunItem = new ItemData("color_gun", -1, Mat.GOLDEN_HORSE_ARMOR, 1, "§6" + Utils.capitalizeFirstLetter(Gadget.COLOR_GUN.getName()), null);

	// fields and constructor
	private List<Snowball> snowballs = new ArrayList<Snowball>();
	private ItemStack previousItem;

	public ColorGun(Player player) {
		super(Gadget.COLOR_GUN, player);
	}

	// start
	@Override
	public void start() {
		// give him the gun
		previousItem = player.getInventory().getItem(PyrParticles.inst().getColorGunHotbarSlot());
		player.getInventory().setItem(PyrParticles.inst().getColorGunHotbarSlot(), gunItem.getItemStack());
		player.updateInventory();
		// register events
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.inst());
		// create stop task
		new BukkitRunnable() {
			@Override
			public void run() {
				stop();
			}
		}.runTaskLater(PyrParticles.inst(), (long) (20 * getType().getDuration()));
	}

	// stop
	@Override
	public void stop() {
		// delete snowballs
		for (Snowball snowball : snowballs) {
			snowball.remove();
		}
		snowballs.clear();
		// reset item
		player.getInventory().setItem(PyrParticles.inst().getColorGunHotbarSlot(), previousItem);
		player.updateInventory();
		// unregister events
		HandlerList.unregisterAll(this);
		// unregister gadget
		PyrParticles.inst().getRunningGadgets().remove(this);
	}

	// events
	@EventHandler
	public void event(PlayerInteractEvent event) {
		if (event.getAction().toString().contains("RIGHT_CLICK") && gunItem.isSimilar(event.getItem())) {
			Player player = event.getPlayer();
			Snowball snowball = player.launchProjectile(Snowball.class, null);// TODO : custom velocity in config
			snowballs.add(snowball);
		}
	}

	@EventHandler
	public void event(ProjectileHitEvent event) {
		Projectile proj = event.getEntity();
		if (snowballs.contains(proj)) {
			snowballs.remove(proj);
			Location location = proj.getLocation();
			Mat typeNormal = AbstractGadget.RANDOM_WOOL.next();
			Mat typeCarpet = AbstractGadget.RANDOM_CARPET.next();
			for (Block block : Utils.getBlocksRound(location, PyrParticles.inst().getColorGunRadius()).keySet()) {
				if (block.getType().isSolid()) {
					// remove previous blocks there first
					PyrParticles.inst().removeColorGunBlocksAt(block.getLocation());
					// send block change
					Mat next = block.getType().toString().contains("CARPET") ? typeCarpet : typeNormal;
					next.setBlockChange(block, block.getWorld().getPlayers());
					// add it to previous blocks so it'll be restored later
					PyrParticles.inst().getColorGunBlocks().add(new ChangedBlock(block, Utils.asList(block.getWorld().getPlayers())));
				}
			}
		}
	}

}
