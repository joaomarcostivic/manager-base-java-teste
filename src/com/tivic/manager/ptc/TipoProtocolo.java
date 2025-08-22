package com.tivic.manager.ptc;

public class TipoProtocolo {
	
	private String nmTipoProtocolo;
	private int tpStatus;

	public TipoProtocolo() {}

	public String getNmTipoProtocolo() {
		return nmTipoProtocolo;
	}

	public void setNmTipoProtocolo(String nmTipoProtocolo) {
		this.nmTipoProtocolo = nmTipoProtocolo;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public TipoProtocolo(String nmTipoProtocolo, int tpStatus) {
		super();
		this.nmTipoProtocolo = nmTipoProtocolo;
		this.tpStatus = tpStatus;
	}
	
}
