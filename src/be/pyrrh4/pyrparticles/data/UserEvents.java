package be.pyrrh4.pyrparticles.data;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import be.pyrrh4.pyrcore.data.PCUser;
import be.pyrrh4.pyrcore.lib.event.UserDataProfileChangedEvent;
import be.pyrrh4.pyrparticles.PyrParticles;

public class UserEvents implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(PlayerJoinEvent event) {
		joinUser(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(UserDataProfileChangedEvent event) {
		PCUser user = event.getUser();
		Player player = user.getPlayer();
		if (player != null) {
			// unregister user
			quitUser(player);
			// register user
			joinUser(player);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerQuitEvent event) {
		quitUser(event.getPlayer());
	}

	private void joinUser(final Player player) {
		PPUser user = new PPUser(new PCUser(player));
		PyrParticles.inst().getData().getUsers().online.put(player.getUniqueId(), user);
		user.pullAsync(null);
	}

	private void quitUser(final Player player) {
		if (PyrParticles.inst().getData().getUsers().online.containsKey(player.getUniqueId())) {
			PyrParticles.inst().getData().getUsers().online.remove(player.getUniqueId());
		}
	}

}
