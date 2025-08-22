package com.tivic.manager.adm;

public class TipoOcorrencia extends com.tivic.manager.grl.TipoOcorrencia {


	public TipoOcorrencia(int cdTipoOcorrencia,
			String nmTipoOcorrencia){
		super(cdTipoOcorrencia,
			nmTipoOcorrencia);
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoOcorrencia(getCdTipoOcorrencia(),
			getNmTipoOcorrencia());
	}

}
