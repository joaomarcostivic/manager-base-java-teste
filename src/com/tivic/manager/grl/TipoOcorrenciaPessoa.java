package com.tivic.manager.grl;

public class TipoOcorrenciaPessoa extends TipoOcorrencia {


	public TipoOcorrenciaPessoa() { }

	public TipoOcorrenciaPessoa(int cdTipoOcorrencia,
			String nmTipoOcorrencia,
			String idTipoOcorrencia,
			int lgEmail) {
		super(cdTipoOcorrencia,
			nmTipoOcorrencia,
			idTipoOcorrencia,
			lgEmail);
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", nmTipoOcorrencia: " +  getNmTipoOcorrencia();
		valueToString += ", idTipoOcorrencia: " +  getIdTipoOcorrencia();
		valueToString += ", lgEmail: " +  getLgEmail();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoOcorrenciaPessoa(getCdTipoOcorrencia(),
			getNmTipoOcorrencia(),
			getIdTipoOcorrencia(),
			getLgEmail());
	}

}