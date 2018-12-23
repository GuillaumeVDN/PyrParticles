package be.pyrrh4.pyrparticles.data;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import be.pyrrh4.pyrcore.data.PCUser;
import be.pyrrh4.pyrcore.lib.data.DataElement;
import be.pyrrh4.pyrcore.lib.data.mysql.Query;
import be.pyrrh4.pyrcore.lib.util.Utils;
import be.pyrrh4.pyrparticles.PyrParticles;
import be.pyrrh4.pyrparticles.particle.ParticleDisplayer;
import be.pyrrh4.pyrparticles.particle.ParticleEffect;
import be.pyrrh4.pyrparticles.trail.Trail;

public class PPUser extends DataElement {

	// fields and constructor
	private PCUser user;
	private ParticleEffect particleEffect = null;
	private ParticleDisplayer particleDisplayer = ParticleDisplayer.AROUND;
	private Trail trail = null;
	private long lastGadgetUsed = 0L;

	public PPUser(PCUser user) {
		this.user = user;
	}

	// getters
	public PCUser getUser() {
		return user;
	}

	public ParticleEffect getParticleEffect() {
		return particleEffect;
	}

	public void setParticleEffect(ParticleEffect particleEffect) {
		this.particleEffect = particleEffect;
		pushAsync();
	}

	public ParticleDisplayer getParticleDisplayer() {
		return particleDisplayer;
	}

	public void setParticleDisplayer(ParticleDisplayer particleDisplayer) {
		this.particleDisplayer = particleDisplayer;
		pushAsync();
	}

	public Trail getTrail() {
		return trail;
	}

	public void setTrail(Trail trail) {
		this.trail = trail;
		pushAsync();
	}

	public void setParticleEffetAndTrail(ParticleEffect particleEffect, Trail trail) {
		this.particleEffect = particleEffect;
		this.trail = trail;
		pushAsync();
	}

	public long getLastGadgetUsed() {
		return lastGadgetUsed;
	}

	public void setLastGadgetUsed(long lastGadgetUsed) {
		this.lastGadgetUsed = lastGadgetUsed;
		pushAsync();
	}

	// data
	private static class JsonData {
		private final ParticleEffect particleEffect;
		private final ParticleDisplayer particleDisplayer;
		private final Trail trail;
		private final long lastGadgetUsed;
		private JsonData(PPUser user) {
			this.particleEffect = user.particleEffect;
			this.particleDisplayer = user.particleDisplayer;
			this.trail = user.trail;
			this.lastGadgetUsed = user.lastGadgetUsed;
		}
	}

	@Override
	protected final UserBoard getBoard() {
		return PyrParticles.inst().getData().getUsers();
	}

	@Override
	protected final String getDataId() {
		return user.toString();
	}

	@Override
	protected final void jsonPull() {
		File file = getBoard().getJsonFile(this);
		JsonData data = Utils.loadFromGson(JsonData.class, file, true);
		if (data != null) {
			this.particleEffect = data.particleEffect;
			this.particleDisplayer = data.particleDisplayer;
			this.trail = data.trail;
			this.lastGadgetUsed = data.lastGadgetUsed;
		}
	}

	@Override
	protected final void jsonPush() {
		File file = getBoard().getJsonFile(this);
		Utils.saveToGson(new JsonData(this), file);
	}

	@Override
	protected final void jsonDelete() {
		File file = getBoard().getJsonFile(this);
		if (file.exists()) {
			file.delete();
		}
	}

	@Override
	protected final void mysqlPull(ResultSet set) throws SQLException {
		this.particleEffect = Utils.valueOfOrNull(ParticleEffect.class, set.getString("particle_effect"));
		this.particleDisplayer = Utils.valueOfOrNull(ParticleDisplayer.class, set.getString("particle_displayer"));
		this.trail = Utils.valueOfOrNull(Trail.class, set.getString("trail"));
		this.lastGadgetUsed = set.getLong("last_gadget_used");
	}

	@Override
	protected Query getMySQLPullQuery() {
		return new Query("SELECT * FROM `" + getBoard().getMySQLTable() + "` WHERE `id`=?;", getDataId());
	}

	@Override
	protected final Query getMySQLPushQuery() {
		String particleEffect = this.particleEffect == null ? "null" : this.particleEffect.name();
		String particleDisplayer = this.particleDisplayer == null ? "null" : this.particleDisplayer.name();
		String trail = this.trail == null ? "null" : this.trail.name();
		return new Query("REPLACE INTO `" + getBoard().getMySQLTable() + "`(`id`,`particle_effect`,`particle_displayer`,`trail`,`last_gadget_used`) VALUES(?,?,?,?," + lastGadgetUsed + ");",
				getDataId(), particleEffect, particleDisplayer, trail);
	}

	@Override
	protected final Query getMySQLDeleteQuery() {
		return new Query("DELETE FROM `" + getBoard().getMySQLTable() + "` WHERE `id`=?;", getDataId());
	}

}
