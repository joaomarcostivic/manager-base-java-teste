package com.tivic.manager.ptc.tipodocumento;

import org.json.JSONObject;

public class TipoProtocolo {
	private String nmTipoProtocolo;
	private int tpStatus;
	private int tpDocumento;

	public TipoProtocolo() {}
	
	public TipoProtocolo(String nmTipoProtocolo, int tpStatus, int tpDocumento) {
		this.nmTipoProtocolo = nmTipoProtocolo;
		this.tpStatus = tpStatus;
		this.tpDocumento = tpDocumento;
	}

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

	public int getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
	
	@Override
	public String toString() {
		try {
			JSONObject json = new JSONObject();
			json.put("nmTipoProtocolo", getNmTipoProtocolo());
			json.put("tpStatus", getTpStatus());
			json.put("tpDocumento", getTpDocumento());
			return json.toString();
		} catch(Exception e) {
			return super.toString();
		}
	}
	
}
