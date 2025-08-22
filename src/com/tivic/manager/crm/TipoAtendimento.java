package com.tivic.manager.crm;

public class TipoAtendimento {

	private int cdTipoAtendimento;
	private String nmTipoAtendimento;
	private String txtTipoAtendimento;
	private int tpClassificacao;
	private int nrHorasPrevisaoResp;

	public TipoAtendimento(){}
	
	public TipoAtendimento(int cdTipoAtendimento,
			String nmTipoAtendimento,
			String txtTipoAtendimento,
			int tpClassificacao,
			int nrHorasPrevisaoResp){
		setCdTipoAtendimento(cdTipoAtendimento);
		setNmTipoAtendimento(nmTipoAtendimento);
		setTxtTipoAtendimento(txtTipoAtendimento);
		setTpClassificacao(tpClassificacao);
		setNrHorasPrevisaoResp(nrHorasPrevisaoResp);
	}
	public void setCdTipoAtendimento(int cdTipoAtendimento){
		this.cdTipoAtendimento=cdTipoAtendimento;
	}
	public int getCdTipoAtendimento(){
		return this.cdTipoAtendimento;
	}
	public void setNmTipoAtendimento(String nmTipoAtendimento){
		this.nmTipoAtendimento=nmTipoAtendimento;
	}
	public String getNmTipoAtendimento(){
		return this.nmTipoAtendimento;
	}
	public void setTxtTipoAtendimento(String txtTipoAtendimento){
		this.txtTipoAtendimento=txtTipoAtendimento;
	}
	public String getTxtTipoAtendimento(){
		return this.txtTipoAtendimento;
	}
	public void setTpClassificacao(int tpClassificacao){
		this.tpClassificacao=tpClassificacao;
	}
	public int getTpClassificacao(){
		return this.tpClassificacao;
	}
	public void setNrHorasPrevisaoResp(int nrHorasPrevisaoResp){
		this.nrHorasPrevisaoResp=nrHorasPrevisaoResp;
	}
	public int getNrHorasPrevisaoResp(){
		return this.nrHorasPrevisaoResp;
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
