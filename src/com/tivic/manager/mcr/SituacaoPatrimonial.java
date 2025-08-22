package com.tivic.manager.mcr;

public class SituacaoPatrimonial {

	private int cdEmpreendimento;
	private float vlCaixa;
	private float vlReceber;
	private float vlEstoque;
	private float vlCredito;
	private float vlTerreno;
	private float vlImovel;
	private float vlMaquina;
	private float vlVeiculo;
	private float vlMoveis;
	private float vlOutrosAtivo;
	private float vlFornecedor;
	private float vlImposto;
	private float vlEmprestimo;
	private float vlCreditoProprietarios;
	private float vlDivida;
	private float vlAdiantamento;
	private int lgContaEmpresarial;
	private String nrContaCorrente;
	private String nmBancoConta;
	private String nrAgenciaConta;
	private float vlSaldoMedio;
	private float vlLimiteCheque;
	private String nrContaPoupanca;
	private String nmBancoPoupanca;
	private String nrAgenciaPoupanca;
	private float vlSaldoAplicacao;
	private String nmCredor;
	private float vlPrestacao;
	private int nrParcela;

	public SituacaoPatrimonial(int cdEmpreendimento,
			float vlCaixa,
			float vlReceber,
			float vlEstoque,
			float vlCredito,
			float vlTerreno,
			float vlImovel,
			float vlMaquina,
			float vlVeiculo,
			float vlMoveis,
			float vlOutrosAtivo,
			float vlFornecedor,
			float vlImposto,
			float vlEmprestimo,
			float vlCreditoProprietarios,
			float vlDivida,
			float vlAdiantamento,
			int lgContaEmpresarial,
			String nrContaCorrente,
			String nmBancoConta,
			String nrAgenciaConta,
			float vlSaldoMedio,
			float vlLimiteCheque,
			String nrContaPoupanca,
			String nmBancoPoupanca,
			String nrAgenciaPoupanca,
			float vlSaldoAplicacao,
			String nmCredor,
			float vlPrestacao,
			int nrParcela){
		setCdEmpreendimento(cdEmpreendimento);
		setVlCaixa(vlCaixa);
		setVlReceber(vlReceber);
		setVlEstoque(vlEstoque);
		setVlCredito(vlCredito);
		setVlTerreno(vlTerreno);
		setVlImovel(vlImovel);
		setVlMaquina(vlMaquina);
		setVlVeiculo(vlVeiculo);
		setVlMoveis(vlMoveis);
		setVlOutrosAtivo(vlOutrosAtivo);
		setVlFornecedor(vlFornecedor);
		setVlImposto(vlImposto);
		setVlEmprestimo(vlEmprestimo);
		setVlCreditoProprietarios(vlCreditoProprietarios);
		setVlDivida(vlDivida);
		setVlAdiantamento(vlAdiantamento);
		setLgContaEmpresarial(lgContaEmpresarial);
		setNrContaCorrente(nrContaCorrente);
		setNmBancoConta(nmBancoConta);
		setNrAgenciaConta(nrAgenciaConta);
		setVlSaldoMedio(vlSaldoMedio);
		setVlLimiteCheque(vlLimiteCheque);
		setNrContaPoupanca(nrContaPoupanca);
		setNmBancoPoupanca(nmBancoPoupanca);
		setNrAgenciaPoupanca(nrAgenciaPoupanca);
		setVlSaldoAplicacao(vlSaldoAplicacao);
		setNmCredor(nmCredor);
		setVlPrestacao(vlPrestacao);
		setNrParcela(nrParcela);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setVlCaixa(float vlCaixa){
		this.vlCaixa=vlCaixa;
	}
	public float getVlCaixa(){
		return this.vlCaixa;
	}
	public void setVlReceber(float vlReceber){
		this.vlReceber=vlReceber;
	}
	public float getVlReceber(){
		return this.vlReceber;
	}
	public void setVlEstoque(float vlEstoque){
		this.vlEstoque=vlEstoque;
	}
	public float getVlEstoque(){
		return this.vlEstoque;
	}
	public void setVlCredito(float vlCredito){
		this.vlCredito=vlCredito;
	}
	public float getVlCredito(){
		return this.vlCredito;
	}
	public void setVlTerreno(float vlTerreno){
		this.vlTerreno=vlTerreno;
	}
	public float getVlTerreno(){
		return this.vlTerreno;
	}
	public void setVlImovel(float vlImovel){
		this.vlImovel=vlImovel;
	}
	public float getVlImovel(){
		return this.vlImovel;
	}
	public void setVlMaquina(float vlMaquina){
		this.vlMaquina=vlMaquina;
	}
	public float getVlMaquina(){
		return this.vlMaquina;
	}
	public void setVlVeiculo(float vlVeiculo){
		this.vlVeiculo=vlVeiculo;
	}
	public float getVlVeiculo(){
		return this.vlVeiculo;
	}
	public void setVlMoveis(float vlMoveis){
		this.vlMoveis=vlMoveis;
	}
	public float getVlMoveis(){
		return this.vlMoveis;
	}
	public void setVlOutrosAtivo(float vlOutrosAtivo){
		this.vlOutrosAtivo=vlOutrosAtivo;
	}
	public float getVlOutrosAtivo(){
		return this.vlOutrosAtivo;
	}
	public void setVlFornecedor(float vlFornecedor){
		this.vlFornecedor=vlFornecedor;
	}
	public float getVlFornecedor(){
		return this.vlFornecedor;
	}
	public void setVlImposto(float vlImposto){
		this.vlImposto=vlImposto;
	}
	public float getVlImposto(){
		return this.vlImposto;
	}
	public void setVlEmprestimo(float vlEmprestimo){
		this.vlEmprestimo=vlEmprestimo;
	}
	public float getVlEmprestimo(){
		return this.vlEmprestimo;
	}
	public void setVlCreditoProprietarios(float vlCreditoProprietarios){
		this.vlCreditoProprietarios=vlCreditoProprietarios;
	}
	public float getVlCreditoProprietarios(){
		return this.vlCreditoProprietarios;
	}
	public void setVlDivida(float vlDivida){
		this.vlDivida=vlDivida;
	}
	public float getVlDivida(){
		return this.vlDivida;
	}
	public void setVlAdiantamento(float vlAdiantamento){
		this.vlAdiantamento=vlAdiantamento;
	}
	public float getVlAdiantamento(){
		return this.vlAdiantamento;
	}
	public void setLgContaEmpresarial(int lgContaEmpresarial){
		this.lgContaEmpresarial=lgContaEmpresarial;
	}
	public int getLgContaEmpresarial(){
		return this.lgContaEmpresarial;
	}
	public void setNrContaCorrente(String nrContaCorrente){
		this.nrContaCorrente=nrContaCorrente;
	}
	public String getNrContaCorrente(){
		return this.nrContaCorrente;
	}
	public void setNmBancoConta(String nmBancoConta){
		this.nmBancoConta=nmBancoConta;
	}
	public String getNmBancoConta(){
		return this.nmBancoConta;
	}
	public void setNrAgenciaConta(String nrAgenciaConta){
		this.nrAgenciaConta=nrAgenciaConta;
	}
	public String getNrAgenciaConta(){
		return this.nrAgenciaConta;
	}
	public void setVlSaldoMedio(float vlSaldoMedio){
		this.vlSaldoMedio=vlSaldoMedio;
	}
	public float getVlSaldoMedio(){
		return this.vlSaldoMedio;
	}
	public void setVlLimiteCheque(float vlLimiteCheque){
		this.vlLimiteCheque=vlLimiteCheque;
	}
	public float getVlLimiteCheque(){
		return this.vlLimiteCheque;
	}
	public void setNrContaPoupanca(String nrContaPoupanca){
		this.nrContaPoupanca=nrContaPoupanca;
	}
	public String getNrContaPoupanca(){
		return this.nrContaPoupanca;
	}
	public void setNmBancoPoupanca(String nmBancoPoupanca){
		this.nmBancoPoupanca=nmBancoPoupanca;
	}
	public String getNmBancoPoupanca(){
		return this.nmBancoPoupanca;
	}
	public void setNrAgenciaPoupanca(String nrAgenciaPoupanca){
		this.nrAgenciaPoupanca=nrAgenciaPoupanca;
	}
	public String getNrAgenciaPoupanca(){
		return this.nrAgenciaPoupanca;
	}
	public void setVlSaldoAplicacao(float vlSaldoAplicacao){
		this.vlSaldoAplicacao=vlSaldoAplicacao;
	}
	public float getVlSaldoAplicacao(){
		return this.vlSaldoAplicacao;
	}
	public void setNmCredor(String nmCredor){
		this.nmCredor=nmCredor;
	}
	public String getNmCredor(){
		return this.nmCredor;
	}
	public void setVlPrestacao(float vlPrestacao){
		this.vlPrestacao=vlPrestacao;
	}
	public float getVlPrestacao(){
		return this.vlPrestacao;
	}
	public void setNrParcela(int nrParcela){
		this.nrParcela=nrParcela;
	}
	public int getNrParcela(){
		return this.nrParcela;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", vlCaixa: " +  getVlCaixa();
		valueToString += ", vlReceber: " +  getVlReceber();
		valueToString += ", vlEstoque: " +  getVlEstoque();
		valueToString += ", vlCredito: " +  getVlCredito();
		valueToString += ", vlTerreno: " +  getVlTerreno();
		valueToString += ", vlImovel: " +  getVlImovel();
		valueToString += ", vlMaquina: " +  getVlMaquina();
		valueToString += ", vlVeiculo: " +  getVlVeiculo();
		valueToString += ", vlMoveis: " +  getVlMoveis();
		valueToString += ", vlOutrosAtivo: " +  getVlOutrosAtivo();
		valueToString += ", vlFornecedor: " +  getVlFornecedor();
		valueToString += ", vlImposto: " +  getVlImposto();
		valueToString += ", vlEmprestimo: " +  getVlEmprestimo();
		valueToString += ", vlCreditoProprietarios: " +  getVlCreditoProprietarios();
		valueToString += ", vlDivida: " +  getVlDivida();
		valueToString += ", vlAdiantamento: " +  getVlAdiantamento();
		valueToString += ", lgContaEmpresarial: " +  getLgContaEmpresarial();
		valueToString += ", nrContaCorrente: " +  getNrContaCorrente();
		valueToString += ", nmBancoConta: " +  getNmBancoConta();
		valueToString += ", nrAgenciaConta: " +  getNrAgenciaConta();
		valueToString += ", vlSaldoMedio: " +  getVlSaldoMedio();
		valueToString += ", vlLimiteCheque: " +  getVlLimiteCheque();
		valueToString += ", nrContaPoupanca: " +  getNrContaPoupanca();
		valueToString += ", nmBancoPoupanca: " +  getNmBancoPoupanca();
		valueToString += ", nrAgenciaPoupanca: " +  getNrAgenciaPoupanca();
		valueToString += ", vlSaldoAplicacao: " +  getVlSaldoAplicacao();
		valueToString += ", nmCredor: " +  getNmCredor();
		valueToString += ", vlPrestacao: " +  getVlPrestacao();
		valueToString += ", nrParcela: " +  getNrParcela();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SituacaoPatrimonial(cdEmpreendimento,
			vlCaixa,
			vlReceber,
			vlEstoque,
			vlCredito,
			vlTerreno,
			vlImovel,
			vlMaquina,
			vlVeiculo,
			vlMoveis,
			vlOutrosAtivo,
			vlFornecedor,
			vlImposto,
			vlEmprestimo,
			vlCreditoProprietarios,
			vlDivida,
			vlAdiantamento,
			lgContaEmpresarial,
			nrContaCorrente,
			nmBancoConta,
			nrAgenciaConta,
			vlSaldoMedio,
			vlLimiteCheque,
			nrContaPoupanca,
			nmBancoPoupanca,
			nrAgenciaPoupanca,
			vlSaldoAplicacao,
			nmCredor,
			vlPrestacao,
			nrParcela);
	}

}