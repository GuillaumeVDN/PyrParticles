package be.pyrrh4.pyrparticles.data;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import be.pyrrh4.pyrcore.lib.data.DataManager;
import be.pyrrh4.pyrparticles.PyrParticles;

public class PPDataManager extends DataManager {

	// constructor
	private UserBoard userBoard = null;
	private UserEvents userEvents = null;

	public PPDataManager(BackEnd backend) {
		super(PyrParticles.inst(), backend);
	}

	// getters
	public UserBoard getUsers() {
		return userBoard;
	}

	// methods
	@Override
	protected void innerEnable() {
		// users
		this.userBoard = new UserBoard();
		userBoard.initAsync(new Callback() { @Override public void callback() {
			userBoard.pullOnline();
		}});
		// events
		Bukkit.getPluginManager().registerEvents(this.userEvents = new UserEvents(), getPlugin());
	}

	@Override
	protected void innerSynchronize() {
		userBoard.pullOnline();
	}

	@Override
	protected void innerReset() {
		userBoard.deleteAsync();
	}

	@Override
	protected void innerDisable() {
		this.userBoard = null;
		HandlerList.unregisterAll(userEvents);
		this.userEvents = null;
	}

}
