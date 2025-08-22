package com.tivic.manager.gpn;

public class WorkflowAcao {

	private int cdAcao;
	private int cdRegra;
	private int tpAcao;
	private int lgSugestao;
	private int nrDias;
	private Double vlServico;
	private int cdTipoAndamento;
	private int cdTipoPrazo;
	private int cdTipoSituacao;
	private int cdProdutoServico;
	private int cdModelo;
	private int cdPessoa;
	private int cdGrupoTrabalho;
	private int tpResponsavelAgenda;
	private int tpContagemPrazo;
	private int cdTipoDocumento;

	public WorkflowAcao() { }

	public WorkflowAcao(int cdAcao,
			int cdRegra,
			int tpAcao,
			int lgSugestao,
			int nrDias,
			Double vlServico,
			int cdTipoAndamento,
			int cdTipoPrazo,
			int cdTipoSituacao,
			int cdProdutoServico,
			int cdModelo,
			int cdPessoa,
			int cdGrupoTrabalho,
			int tpResponsavelAgenda,
			int tpContagemPrazo,
			int cdTipoDocumento) {
		setCdAcao(cdAcao);
		setCdRegra(cdRegra);
		setTpAcao(tpAcao);
		setLgSugestao(lgSugestao);
		setNrDias(nrDias);
		setVlServico(vlServico);
		setCdTipoAndamento(cdTipoAndamento);
		setCdTipoPrazo(cdTipoPrazo);
		setCdTipoSituacao(cdTipoSituacao);
		setCdProdutoServico(cdProdutoServico);
		setCdModelo(cdModelo);
		setCdPessoa(cdPessoa);
		setCdGrupoTrabalho(cdGrupoTrabalho);
		setTpResponsavelAgenda(tpResponsavelAgenda);
		setTpContagemPrazo(tpContagemPrazo);
		setCdTipoDocumento(cdTipoDocumento);
	}
	public void setCdAcao(int cdAcao){
		this.cdAcao=cdAcao;
	}
	public int getCdAcao(){
		return this.cdAcao;
	}
	public void setCdRegra(int cdRegra){
		this.cdRegra=cdRegra;
	}
	public int getCdRegra(){
		return this.cdRegra;
	}
	public void setTpAcao(int tpAcao){
		this.tpAcao=tpAcao;
	}
	public int getTpAcao(){
		return this.tpAcao;
	}
	public void setLgSugestao(int lgSugestao){
		this.lgSugestao=lgSugestao;
	}
	public int getLgSugestao(){
		return this.lgSugestao;
	}
	public void setNrDias(int nrDias){
		this.nrDias=nrDias;
	}
	public int getNrDias(){
		return this.nrDias;
	}
	public void setVlServico(Double vlServico){
		this.vlServico=vlServico;
	}
	public Double getVlServico(){
		return this.vlServico;
	}
	public void setCdTipoAndamento(int cdTipoAndamento){
		this.cdTipoAndamento=cdTipoAndamento;
	}
	public int getCdTipoAndamento(){
		return this.cdTipoAndamento;
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
	}
	public void setCdTipoSituacao(int cdTipoSituacao){
		this.cdTipoSituacao=cdTipoSituacao;
	}
	public int getCdTipoSituacao(){
		return this.cdTipoSituacao;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdGrupoTrabalho(int cdGrupoTrabalho){
		this.cdGrupoTrabalho=cdGrupoTrabalho;
	}
	public int getCdGrupoTrabalho(){
		return this.cdGrupoTrabalho;
	}
	public void setTpResponsavelAgenda(int tpResponsavelAgenda){
		this.tpResponsavelAgenda=tpResponsavelAgenda;
	}
	public int getTpResponsavelAgenda(){
		return this.tpResponsavelAgenda;
	}
	public void setTpContagemPrazo(int tpContagemPrazo){
		this.tpContagemPrazo=tpContagemPrazo;
	}
	public int getTpContagemPrazo(){
		return this.tpContagemPrazo;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAcao: " +  getCdAcao();
		valueToString += ", cdRegra: " +  getCdRegra();
		valueToString += ", tpAcao: " +  getTpAcao();
		valueToString += ", lgSugestao: " +  getLgSugestao();
		valueToString += ", nrDias: " +  getNrDias();
		valueToString += ", vlServico: " +  getVlServico();
		valueToString += ", cdTipoAndamento: " +  getCdTipoAndamento();
		valueToString += ", cdTipoPrazo: " +  getCdTipoPrazo();
		valueToString += ", cdTipoSituacao: " +  getCdTipoSituacao();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdModelo: " +  getCdModelo();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdGrupoTrabalho: " +  getCdGrupoTrabalho();
		valueToString += ", tpResponsavelAgenda: " +  getTpResponsavelAgenda();
		valueToString += ", tpContagemPrazo: " +  getTpContagemPrazo();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new WorkflowAcao(getCdAcao(),
			getCdRegra(),
			getTpAcao(),
			getLgSugestao(),
			getNrDias(),
			getVlServico(),
			getCdTipoAndamento(),
			getCdTipoPrazo(),
			getCdTipoSituacao(),
			getCdProdutoServico(),
			getCdModelo(),
			getCdPessoa(),
			getCdGrupoTrabalho(),
			getTpResponsavelAgenda(),
			getTpContagemPrazo(),
			getCdTipoDocumento());
	}

}