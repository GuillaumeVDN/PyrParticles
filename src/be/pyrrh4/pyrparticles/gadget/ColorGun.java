package be.pyrrh4.pyrparticles.gadget;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import be.pyrrh4.core.gui.GUI;
import be.pyrrh4.core.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.util.ChangedBlock;
import be.pyrrh4.pyrparticles.util.RandomMaterial;

public class ColorGun extends AbstractGadget implements Listener {

	// static fields
	private static final ItemStack gunItem = GUI.createItem(Material.GOLD_BARDING, 0, 1, "§6" + Utils.capitalizeFirstLetter(Gadget.COLOR_GUN.getName()), null);
	private static final RandomMaterial type = new RandomMaterial(Material.WOOL, 15), carpetType = new RandomMaterial(Material.CARPET, 15);

	// fields and constructor
	private ArrayList<Snowball> snowballs = new ArrayList<Snowball>();
	private ItemStack previousItem;

	public ColorGun(Player player) {
		super(Gadget.COLOR_GUN, player);
	}

	// start
	@Override
	public void start() {
		// give him the gun
		previousItem = player.getInventory().getItem(PyrParticles.instance().getColorGunHotbarSlot());
		player.getInventory().setItem(PyrParticles.instance().getColorGunHotbarSlot(), gunItem);
		player.updateInventory();
		// register events
		Bukkit.getPluginManager().registerEvents(this, PyrParticles.instance());
		// create stop task
		new BukkitRunnable() {
			@Override
			public void run() {
				stop();
			}
		}.runTaskLater(PyrParticles.instance(), (long) (20 * getType().getDuration()));
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
		player.getInventory().setItem(PyrParticles.instance().getColorGunHotbarSlot(), previousItem);
		player.updateInventory();
		// unregister events
		HandlerList.unregisterAll(this);
		// unregister gadget
		PyrParticles.instance().getRunningGadgets().remove(this);
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
			MaterialData typeNormal = type.getNext();
			MaterialData typeCarpet = carpetType.getNext();
			for (Block block : Utils.getBlocksRound(location, PyrParticles.instance().getColorGunRadius()).keySet()) {
				if (block.getType().isSolid()) {
					// remove previous blocks there first
					PyrParticles.instance().removeColorGunBlocksAt(block.getLocation());
					// send block change
					MaterialData next = block.getType().equals(Material.CARPET) ? typeCarpet : typeNormal;
					for (Player pl : block.getWorld().getPlayers()) {
						pl.sendBlockChange(block.getLocation(), next.getItemTypeId(), next.getData());
					}
					// add it to previous blocks so it'll be restored later
					PyrParticles.instance().getColorGunBlocks().add(new ChangedBlock(block, Utils.asList(block.getWorld().getPlayers())));
				}
			}
		}
	}

}
