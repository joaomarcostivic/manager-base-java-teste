package com.tivic.manager.ptc;

public class TipoDocumento extends com.tivic.manager.gpn.TipoDocumento {


	public TipoDocumento(){ }

	public TipoDocumento(int cdTipoDocumento,
			String nmTipoDocumento,
			String idTipoDocumento,
			int stTipoDocumento,
			int cdEmpresa,
			int cdSetor,
			int cdFormulario,
			int tpNumeracao,
			String idPrefixoNumeracao,
			String dsMascaraNumeracao,
			int nrUltimaNumeracao,
			int cdTipoDocumentoSuperior,
			int lgNumeracaoSuperior,
			String nrExterno){
		super(cdTipoDocumento,
			nmTipoDocumento,
			idTipoDocumento,
			stTipoDocumento,
			cdEmpresa,
			cdSetor,
			cdFormulario,
			tpNumeracao,
			idPrefixoNumeracao,
			dsMascaraNumeracao,
			nrUltimaNumeracao,
			cdTipoDocumentoSuperior,
			lgNumeracaoSuperior,
			nrExterno);
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += "parent: " + super.toString();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDocumento(getCdTipoDocumento(),
			getNmTipoDocumento(),
			getIdTipoDocumento(),
			getStTipoDocumento(),
			getCdEmpresa(),
			getCdSetor(),
			getCdFormulario(),
			getTpNumeracao(),
			getIdPrefixoNumeracao(),
			getDsMascaraNumeracao(),
			getNrUltimaNumeracao(),
			getCdTipoDocumentoSuperior(),
			getLgNumeracaoSuperior(),
			getNrExterno());
	}

}
