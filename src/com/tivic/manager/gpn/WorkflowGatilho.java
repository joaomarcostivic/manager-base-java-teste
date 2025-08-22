package com.tivic.manager.gpn;

public class WorkflowGatilho {

	private int cdGatilho;
	private int cdRegra;
	private int tpGatilho;
	private int vlInicial;
	private int vlFinal;
	private int cdAtributo;
	private int cdEntidade;
	private int cdTipoPrazo;
	private int cdProdutoServico;
	private int cdTipoAndamento;
	private int cdPessoa;
	private int tpAgendaItem;

	public WorkflowGatilho() { }

	public WorkflowGatilho(int cdGatilho,
			int cdRegra,
			int tpGatilho,
			int vlInicial,
			int vlFinal,
			int cdAtributo,
			int cdEntidade,
			int cdTipoPrazo,
			int cdProdutoServico,
			int cdTipoAndamento,
			int cdPessoa,
			int tpAgendaItem) {
		setCdGatilho(cdGatilho);
		setCdRegra(cdRegra);
		setTpGatilho(tpGatilho);
		setVlInicial(vlInicial);
		setVlFinal(vlFinal);
		setCdAtributo(cdAtributo);
		setCdEntidade(cdEntidade);
		setCdTipoPrazo(cdTipoPrazo);
		setCdProdutoServico(cdProdutoServico);
		setCdTipoAndamento(cdTipoAndamento);
		setCdPessoa(cdPessoa);
		setTpAgendaItem(tpAgendaItem);
	}
	public void setCdGatilho(int cdGatilho){
		this.cdGatilho=cdGatilho;
	}
	public int getCdGatilho(){
		return this.cdGatilho;
	}
	public void setCdRegra(int cdRegra){
		this.cdRegra=cdRegra;
	}
	public int getCdRegra(){
		return this.cdRegra;
	}
	public void setTpGatilho(int tpGatilho){
		this.tpGatilho=tpGatilho;
	}
	public int getTpGatilho(){
		return this.tpGatilho;
	}
	public void setVlInicial(int vlInicial){
		this.vlInicial=vlInicial;
	}
	public int getVlInicial(){
		return this.vlInicial;
	}
	public void setVlFinal(int vlFinal){
		this.vlFinal=vlFinal;
	}
	public int getVlFinal(){
		return this.vlFinal;
	}
	public void setCdAtributo(int cdAtributo){
		this.cdAtributo=cdAtributo;
	}
	public int getCdAtributo(){
		return this.cdAtributo;
	}
	public void setCdEntidade(int cdEntidade){
		this.cdEntidade=cdEntidade;
	}
	public int getCdEntidade(){
		return this.cdEntidade;
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdTipoAndamento(int cdTipoAndamento){
		this.cdTipoAndamento=cdTipoAndamento;
	}
	public int getCdTipoAndamento(){
		return this.cdTipoAndamento;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpAgendaItem(int tpAgendaItem){
		this.tpAgendaItem=tpAgendaItem;
	}
	public int getTpAgendaItem(){
		return this.tpAgendaItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGatilho: " +  getCdGatilho();
		valueToString += ", cdRegra: " +  getCdRegra();
		valueToString += ", tpGatilho: " +  getTpGatilho();
		valueToString += ", vlInicial: " +  getVlInicial();
		valueToString += ", vlFinal: " +  getVlFinal();
		valueToString += ", cdAtributo: " +  getCdAtributo();
		valueToString += ", cdEntidade: " +  getCdEntidade();
		valueToString += ", cdTipoPrazo: " +  getCdTipoPrazo();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdTipoAndamento: " +  getCdTipoAndamento();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", tpAgendaItem: " +  getTpAgendaItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new WorkflowGatilho(getCdGatilho(),
			getCdRegra(),
			getTpGatilho(),
			getVlInicial(),
			getVlFinal(),
			getCdAtributo(),
			getCdEntidade(),
			getCdTipoPrazo(),
			getCdProdutoServico(),
			getCdTipoAndamento(),
			getCdPessoa(),
			getTpAgendaItem());
	}

}