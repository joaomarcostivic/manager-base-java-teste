package com.tivic.manager.crm;

public class TipoOcorrencia extends com.tivic.manager.grl.TipoOcorrencia {

	private int tpAcao;

	public TipoOcorrencia(int cdTipoOcorrencia,
			String nmTipoOcorrencia,
			int tpAcao){
		super(cdTipoOcorrencia,
			nmTipoOcorrencia);
		setTpAcao(tpAcao);
	}
	public void setTpAcao(int tpAcao){
		this.tpAcao=tpAcao;
	}
	public int getTpAcao(){
		return this.tpAcao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", tpAcao: " +  getTpAcao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoOcorrencia(getCdTipoOcorrencia(),
			getNmTipoOcorrencia(),
			getTpAcao());
	}

}
