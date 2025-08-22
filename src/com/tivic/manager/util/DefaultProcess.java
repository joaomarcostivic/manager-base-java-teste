package com.tivic.manager.util;

import java.util.HashMap;

public class DefaultProcess implements sol.admin.Process {

	protected String caption;
	protected String id;
	protected int code;
	protected float maxProgress;
	protected float minProgress;
	protected float progress;
	protected boolean started;

	public HashMap<String,Object> getInfo() {
		HashMap<String,Object> info = new HashMap<String,Object>();
		info.put("CODE", new Integer(code));
		info.put("MAX", new Float(maxProgress));
		info.put("MIN", new Float(minProgress));
		info.put("PROGRESS", new Float(progress));
		info.put("ID", id);
		info.put("CAPTION", caption);
		info.put("PERCENT", new Float(this.maxProgress - this.minProgress == 0 ? 100 :
									  (this.progress - this.minProgress) * 100 / (this.maxProgress - this.minProgress)));
		return info;
	}

	public DefaultProcess(String id) {
		this(id, 0, 100, 0);
	}

	public DefaultProcess(String id, float minProgress, float maxProgress, float initialProgress) {
		this.code = ProcessManager.getNewCodeProcess();
		this.id = id;
		this.minProgress = minProgress;
		this.maxProgress = maxProgress;
		this.progress = initialProgress;
	}

	public String getCaption() {
		return caption;
	}

	public int getCode() {
		return code;
	}

	public String getId() {
		return id;
	}

	public float getMaxProgress() {
		return maxProgress;
	}

	public float getMinProgress() {
		return minProgress;
	}

	public float getProgress() {
		return progress;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean isStarted) {
		if (this.started)
			return;
		this.started = isStarted;
	}

	public void run() {
	}

}