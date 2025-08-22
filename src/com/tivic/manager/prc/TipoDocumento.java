package com.tivic.manager.prc;

public class TipoDocumento extends com.tivic.manager.gpn.TipoDocumento {

	private int cdTipoAndamento;

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
			String nrExterno,
			int cdTipoAndamento){
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
		setCdTipoAndamento(cdTipoAndamento);
	}
	public void setCdTipoAndamento(int cdTipoAndamento){
		this.cdTipoAndamento=cdTipoAndamento;
	}
	public int getCdTipoAndamento(){
		return this.cdTipoAndamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", cdTipoAndamento: " +  getCdTipoAndamento();
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
			getNrExterno(),
			getCdTipoAndamento());
	}

}
