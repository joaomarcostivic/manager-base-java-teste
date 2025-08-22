package com.tivic.manager.prc;

public class TipoPrazo {

	private int cdTipoPrazo;
	private String nmTipoPrazo;
	private int cdTipoAndamento;
	private int tpAgendaItem;
	private int cdTipoAndamentoCumprimento;
	private int lgDocumentoObrigatorio;
	private int lgUtilizaModelo;
	private int cdModelo;
	private int lgEmail;
	private int cdEmpresa;
	private int cdTipoDocumentoCumprimento;
	private int cdPessoaPadrao;
	private int cdGrupoTrabalhoPadrao;
	private int lgAgendaEditavel;
	private int stTipoPrazo;

	public TipoPrazo() { }

	public TipoPrazo(int cdTipoPrazo,
			String nmTipoPrazo,
			int cdTipoAndamento,
			int tpAgendaItem,
			int cdTipoAndamentoCumprimento,
			int lgDocumentoObrigatorio,
			int lgUtilizaModelo,
			int cdModelo,
			int lgEmail,
			int cdEmpresa,
			int cdTipoDocumentoCumprimento,
			int cdPessoaPadrao,
			int cdGrupoTrabalhoPadrao,
			int lgAgendaEditavel,
			int stTipoPrazo) {
		setCdTipoPrazo(cdTipoPrazo);
		setNmTipoPrazo(nmTipoPrazo);
		setCdTipoAndamento(cdTipoAndamento);
		setTpAgendaItem(tpAgendaItem);
		setCdTipoAndamentoCumprimento(cdTipoAndamentoCumprimento);
		setLgDocumentoObrigatorio(lgDocumentoObrigatorio);
		setLgUtilizaModelo(lgUtilizaModelo);
		setCdModelo(cdModelo);
		setLgEmail(lgEmail);
		setCdEmpresa(cdEmpresa);
		setCdTipoDocumentoCumprimento(cdTipoDocumentoCumprimento);
		setCdPessoaPadrao(cdPessoaPadrao);
		setCdGrupoTrabalhoPadrao(cdGrupoTrabalhoPadrao);
		setLgAgendaEditavel(lgAgendaEditavel);
		setStTipoPrazo(stTipoPrazo);
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
	}
	public void setNmTipoPrazo(String nmTipoPrazo){
		this.nmTipoPrazo=nmTipoPrazo;
	}
	public String getNmTipoPrazo(){
		return this.nmTipoPrazo;
	}
	public void setCdTipoAndamento(int cdTipoAndamento){
		this.cdTipoAndamento=cdTipoAndamento;
	}
	public int getCdTipoAndamento(){
		return this.cdTipoAndamento;
	}
	public void setTpAgendaItem(int tpAgendaItem){
		this.tpAgendaItem=tpAgendaItem;
	}
	public int getTpAgendaItem(){
		return this.tpAgendaItem;
	}
	public void setCdTipoAndamentoCumprimento(int cdTipoAndamentoCumprimento){
		this.cdTipoAndamentoCumprimento=cdTipoAndamentoCumprimento;
	}
	public int getCdTipoAndamentoCumprimento(){
		return this.cdTipoAndamentoCumprimento;
	}
	public void setLgDocumentoObrigatorio(int lgDocumentoObrigatorio){
		this.lgDocumentoObrigatorio=lgDocumentoObrigatorio;
	}
	public int getLgDocumentoObrigatorio(){
		return this.lgDocumentoObrigatorio;
	}
	public void setLgUtilizaModelo(int lgUtilizaModelo){
		this.lgUtilizaModelo=lgUtilizaModelo;
	}
	public int getLgUtilizaModelo(){
		return this.lgUtilizaModelo;
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setLgEmail(int lgEmail){
		this.lgEmail=lgEmail;
	}
	public int getLgEmail(){
		return this.lgEmail;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdTipoDocumentoCumprimento(int cdTipoDocumentoCumprimento){
		this.cdTipoDocumentoCumprimento=cdTipoDocumentoCumprimento;
	}
	public int getCdTipoDocumentoCumprimento(){
		return this.cdTipoDocumentoCumprimento;
	}
	public void setCdPessoaPadrao(int cdPessoaPadrao){
		this.cdPessoaPadrao=cdPessoaPadrao;
	}
	public int getCdPessoaPadrao(){
		return this.cdPessoaPadrao;
	}
	public void setCdGrupoTrabalhoPadrao(int cdGrupoTrabalhoPadrao){
		this.cdGrupoTrabalhoPadrao=cdGrupoTrabalhoPadrao;
	}
	public int getCdGrupoTrabalhoPadrao(){
		return this.cdGrupoTrabalhoPadrao;
	}
	public void setLgAgendaEditavel(int lgAgendaEditavel){
		this.lgAgendaEditavel=lgAgendaEditavel;
	}
	public int getLgAgendaEditavel(){
		return this.lgAgendaEditavel;
	}
	public void setStTipoPrazo(int stTipoPrazo){
		this.stTipoPrazo=stTipoPrazo;
	}
	public int getStTipoPrazo(){
		return this.stTipoPrazo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoPrazo: " +  getCdTipoPrazo();
		valueToString += ", nmTipoPrazo: " +  getNmTipoPrazo();
		valueToString += ", cdTipoAndamento: " +  getCdTipoAndamento();
		valueToString += ", tpAgendaItem: " +  getTpAgendaItem();
		valueToString += ", cdTipoAndamentoCumprimento: " +  getCdTipoAndamentoCumprimento();
		valueToString += ", lgDocumentoObrigatorio: " +  getLgDocumentoObrigatorio();
		valueToString += ", lgUtilizaModelo: " +  getLgUtilizaModelo();
		valueToString += ", cdModelo: " +  getCdModelo();
		valueToString += ", lgEmail: " +  getLgEmail();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdTipoDocumentoCumprimento: " +  getCdTipoDocumentoCumprimento();
		valueToString += ", cdPessoaPadrao: " +  getCdPessoaPadrao();
		valueToString += ", cdGrupoTrabalhoPadrao: " +  getCdGrupoTrabalhoPadrao();
		valueToString += ", lgAgendaEditavel: " +  getLgAgendaEditavel();
		valueToString += ", stTipoPrazo: " +  getStTipoPrazo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoPrazo(getCdTipoPrazo(),
			getNmTipoPrazo(),
			getCdTipoAndamento(),
			getTpAgendaItem(),
			getCdTipoAndamentoCumprimento(),
			getLgDocumentoObrigatorio(),
			getLgUtilizaModelo(),
			getCdModelo(),
			getLgEmail(),
			getCdEmpresa(),
			getCdTipoDocumentoCumprimento(),
			getCdPessoaPadrao(),
			getCdGrupoTrabalhoPadrao(),
			getLgAgendaEditavel(),
			getStTipoPrazo());
	}

}