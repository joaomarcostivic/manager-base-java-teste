package com.tivic.manager.acd;

public class TipoInstrumentosPedagogicos {

	private int cdTipoInstrumentosPedagogicos;
	private String nmTipoInstrumentosPedagogicos;
	private String idTipoInstrumentosPedagogicos;
	private int stTipoInstrumentosPedagogicos;

	public TipoInstrumentosPedagogicos() { }

	public TipoInstrumentosPedagogicos(int cdTipoInstrumentosPedagogicos,
			String nmTipoInstrumentosPedagogicos,
			String idTipoInstrumentosPedagogicos,
			int stTipoInstrumentosPedagogicos) {
		setCdTipoInstrumentosPedagogicos(cdTipoInstrumentosPedagogicos);
		setNmTipoInstrumentosPedagogicos(nmTipoInstrumentosPedagogicos);
		setIdTipoInstrumentosPedagogicos(idTipoInstrumentosPedagogicos);
		setStTipoInstrumentosPedagogicos(stTipoInstrumentosPedagogicos);
	}
	public void setCdTipoInstrumentosPedagogicos(int cdTipoInstrumentosPedagogicos){
		this.cdTipoInstrumentosPedagogicos=cdTipoInstrumentosPedagogicos;
	}
	public int getCdTipoInstrumentosPedagogicos(){
		return this.cdTipoInstrumentosPedagogicos;
	}
	public void setNmTipoInstrumentosPedagogicos(String nmTipoInstrumentosPedagogicos){
		this.nmTipoInstrumentosPedagogicos=nmTipoInstrumentosPedagogicos;
	}
	public String getNmTipoInstrumentosPedagogicos(){
		return this.nmTipoInstrumentosPedagogicos;
	}
	public void setIdTipoInstrumentosPedagogicos(String idTipoInstrumentosPedagogicos){
		this.idTipoInstrumentosPedagogicos=idTipoInstrumentosPedagogicos;
	}
	public String getIdTipoInstrumentosPedagogicos(){
		return this.idTipoInstrumentosPedagogicos;
	}
	public void setStTipoInstrumentosPedagogicos(int stTipoInstrumentosPedagogicos){
		this.stTipoInstrumentosPedagogicos=stTipoInstrumentosPedagogicos;
	}
	public int getStTipoInstrumentosPedagogicos(){
		return this.stTipoInstrumentosPedagogicos;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoInstrumentosPedagogicos: " +  getCdTipoInstrumentosPedagogicos();
		valueToString += ", nmTipoInstrumentosPedagogicos: " +  getNmTipoInstrumentosPedagogicos();
		valueToString += ", idTipoInstrumentosPedagogicos: " +  getIdTipoInstrumentosPedagogicos();
		valueToString += ", stTipoInstrumentosPedagogicos: " +  getStTipoInstrumentosPedagogicos();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoInstrumentosPedagogicos(getCdTipoInstrumentosPedagogicos(),
			getNmTipoInstrumentosPedagogicos(),
			getIdTipoInstrumentosPedagogicos(),
			getStTipoInstrumentosPedagogicos());
	}

}