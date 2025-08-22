package com.tivic.manager.ord;

public class TipoAtendimento extends com.tivic.manager.crm.TipoAtendimento {


	public TipoAtendimento(){ 
		super();
	}

	public TipoAtendimento(int cdTipoAtendimento,
			String nmTipoAtendimento,
			String txtTipoAtendimento,
			int tpClassificacao,
			int nrHorasPrevisaoResp){
		super(cdTipoAtendimento,
			nmTipoAtendimento,
			txtTipoAtendimento,
			tpClassificacao,
			nrHorasPrevisaoResp);
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoAtendimento: " +  getCdTipoAtendimento();
		valueToString += ", nmTipoAtendimento: " +  getNmTipoAtendimento();
		valueToString += ", txtTipoAtendimento: " +  getTxtTipoAtendimento();
		valueToString += ", tpClassificacao: " +  getTpClassificacao();
		valueToString += ", nrHorasPrevisaoResp: " +  getNrHorasPrevisaoResp();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoAtendimento(getCdTipoAtendimento(),
			getNmTipoAtendimento(),
			getTxtTipoAtendimento(),
			getTpClassificacao(),
			getNrHorasPrevisaoResp());
	}

}
