package com.tivic.manager.agd;

public class TipoOcorrencia extends com.tivic.manager.grl.TipoOcorrencia {


	public TipoOcorrencia(){ }

	public TipoOcorrencia(int cdTipoOcorrencia,
			String nmTipoOcorrencia,
			String idTipoOcorrencia,
			int lgEmail){
		super(cdTipoOcorrencia,
			nmTipoOcorrencia,
			idTipoOcorrencia,
			lgEmail);
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoOcorrencia(getCdTipoOcorrencia(),
			getNmTipoOcorrencia(),
			getIdTipoOcorrencia(),
			getLgEmail());
	}

}
