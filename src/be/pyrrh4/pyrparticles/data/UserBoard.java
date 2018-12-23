package be.pyrrh4.pyrparticles.data;

import java.io.File;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import be.pyrrh4.pyrcore.PyrCore;
import be.pyrrh4.pyrcore.data.PCUser;
import be.pyrrh4.pyrcore.lib.data.DataBoard;
import be.pyrrh4.pyrcore.lib.data.mysql.Query;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;

public class UserBoard extends DataBoard<PPUser> {

	// fields
	Map<UUID, PPUser> online = new HashMap<UUID, PPUser>();

	// methods
	public Map<UUID, PPUser> getAll() {
		return Collections.unmodifiableMap(online);
	}

	@Override
	public PPUser getElement(Object param) {
		if (param instanceof OfflinePlayer) {
			return online.get(((OfflinePlayer) param).getUniqueId());
		} else if (param instanceof UUID) {
			return online.get((UUID) param);
		} else if (param instanceof PCUser) {
			PCUser user = (PCUser) param;
			return user.isCurrentProfile() ? online.get(user.getUniqueId()) : null;
		}
		throw new IllegalArgumentException("param type " + param.getClass() + " isn't allowed");
	}

	public void pullOnline() {
		for (Player pl : Utils.getOnlinePlayers()) {
			UUID uuid = pl.getUniqueId();
			if (!online.containsKey(uuid)) {
				online.put(uuid, new PPUser(new PCUser(pl.getUniqueId())));
			}
		}
		pullAsync(online.values(), null);
	}

	// data
	@Override
	public PPDataManager getDataManager() {
		return PyrParticles.inst().getData();
	}

	@Override
	protected final File getJsonFile(PPUser element) {
		return new File(PyrCore.inst().getUserDataRootFolder() + "/" + element.getDataId() + "/pyrparticles_user.json");
	}

	@Override
	protected final void jsonPull() {
		throw new UnsupportedOperationException();// can't pull the whole user board
	}

	@Override
	protected final void jsonDelete() {
		File root = PyrCore.inst().getUserDataRootFolder();
		if (root.exists() && root.isDirectory()) {
			for (File userRoot : root.listFiles()) {
				if (userRoot.exists() && userRoot.isDirectory()) {
					File user = new File(userRoot.getPath() + "/pyrparticles_user.json");
					if (user.exists()) {
						user.delete();
					}
				}
			}
		}
	}

	// MySQL
	@Override
	protected final String getMySQLTable() {
		return "pyrparticles_user";
	}

	@Override
	protected final Query getMySQLInitQuery() {
		return new Query("CREATE TABLE IF NOT EXISTS `" + getMySQLTable() + "`("
				+ "id VARCHAR(100) NOT NULL,"
				+ "particle_effect TINYTEXT NOT NULL,"
				+ "particle_displayer TINYTEXT NOT NULL,"
				+ "trail TINYTEXT NOT NULL,"
				+ "last_gadget_used BIGINT NOT NULL,"
				+ "PRIMARY KEY(id)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=?;", "utf8");
	}

	@Override
	protected final void mysqlPull() throws SQLException {
		throw new UnsupportedOperationException();// can't pull the whole user board
	}

	@Override
	protected final void mysqlDelete() {
		getDataManager().performMySQLUpdateQuery(new Query("DELETE FROM `" + getMySQLTable() + "`;"));
	}

}
