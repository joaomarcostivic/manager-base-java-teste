package com.tivic.manager.adm;

public class TabelaComissaoAplicacao {

	private int cdTabelaComissao;
	private int cdAplicacao;
	private int cdCategoriaEconomica;
	private int cdGrupo;
	private int cdPlanoPagamento;
	private int cdFormaPagamento;
	private int cdRegiao;
	private int cdAgente;
	private int cdCliente;
	private int cdTipoOperacao;
	private int cdProdutoServico;
	private int lgAtivo;

	public TabelaComissaoAplicacao(int cdTabelaComissao,
			int cdAplicacao,
			int cdCategoriaEconomica,
			int cdGrupo,
			int cdPlanoPagamento,
			int cdFormaPagamento,
			int cdRegiao,
			int cdAgente,
			int cdCliente,
			int cdTipoOperacao,
			int cdProdutoServico,
			int lgAtivo){
		setCdTabelaComissao(cdTabelaComissao);
		setCdAplicacao(cdAplicacao);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setCdGrupo(cdGrupo);
		setCdPlanoPagamento(cdPlanoPagamento);
		setCdFormaPagamento(cdFormaPagamento);
		setCdRegiao(cdRegiao);
		setCdAgente(cdAgente);
		setCdCliente(cdCliente);
		setCdTipoOperacao(cdTipoOperacao);
		setCdProdutoServico(cdProdutoServico);
		setLgAtivo(lgAtivo);
	}
	public void setCdTabelaComissao(int cdTabelaComissao){
		this.cdTabelaComissao=cdTabelaComissao;
	}
	public int getCdTabelaComissao(){
		return this.cdTabelaComissao;
	}
	public void setCdAplicacao(int cdAplicacao){
		this.cdAplicacao=cdAplicacao;
	}
	public int getCdAplicacao(){
		return this.cdAplicacao;
	}
	public void setCdCategoriaEconomica(int cdCategoriaEconomica){
		this.cdCategoriaEconomica=cdCategoriaEconomica;
	}
	public int getCdCategoriaEconomica(){
		return this.cdCategoriaEconomica;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setCdFormaPagamento(int cdFormaPagamento){
		this.cdFormaPagamento=cdFormaPagamento;
	}
	public int getCdFormaPagamento(){
		return this.cdFormaPagamento;
	}
	public void setCdRegiao(int cdRegiao){
		this.cdRegiao=cdRegiao;
	}
	public int getCdRegiao(){
		return this.cdRegiao;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setCdTipoOperacao(int cdTipoOperacao){
		this.cdTipoOperacao=cdTipoOperacao;
	}
	public int getCdTipoOperacao(){
		return this.cdTipoOperacao;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaComissao: " +  getCdTabelaComissao();
		valueToString += ", cdAplicacao: " +  getCdAplicacao();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", cdFormaPagamento: " +  getCdFormaPagamento();
		valueToString += ", cdRegiao: " +  getCdRegiao();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", cdTipoOperacao: " +  getCdTipoOperacao();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaComissaoAplicacao(getCdTabelaComissao(),
			getCdAplicacao(),
			getCdCategoriaEconomica(),
			getCdGrupo(),
			getCdPlanoPagamento(),
			getCdFormaPagamento(),
			getCdRegiao(),
			getCdAgente(),
			getCdCliente(),
			getCdTipoOperacao(),
			getCdProdutoServico(),
			getLgAtivo());
	}

}
