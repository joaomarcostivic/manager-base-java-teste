package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class CotacaoPedidoItem {

	private int cdCotacaoPedidoItem;
	private int cdFornecedor;
	private int cdEmpresa;
	private int cdPedidoCompra;
	private int cdProdutoServico;
	private int cdPlanoPagamento;
	private int cdPessoaContato;
	private int cdPessoaJuridica;
	private int nrPosicao;
	private GregorianCalendar dtCotacao;
	private int qtDiasGarantia;
	private float qtDisponivel;
	private float vlCotacao;
	private float vlFrete;
	private float vlTributos;
	private float vlDesconto;
	private int tpResultado;
	private int lgAssistenciaTecnica;
	private int lgProntaEntrega;
	private String txtObservacao;
	private int qtDiasEntrega;
	private int qtDiasValidade;

	public CotacaoPedidoItem(int cdCotacaoPedidoItem,
			int cdFornecedor,
			int cdEmpresa,
			int cdPedidoCompra,
			int cdProdutoServico,
			int cdPlanoPagamento,
			int cdPessoaContato,
			int cdPessoaJuridica,
			int nrPosicao,
			GregorianCalendar dtCotacao,
			int qtDiasGarantia,
			float qtDisponivel,
			float vlCotacao,
			float vlFrete,
			float vlTributos,
			float vlDesconto,
			int tpResultado,
			int lgAssistenciaTecnica,
			int lgProntaEntrega,
			String txtObservacao,
			int qtDiasEntrega,
			int qtDiasValidade){
		setCdCotacaoPedidoItem(cdCotacaoPedidoItem);
		setCdFornecedor(cdFornecedor);
		setCdEmpresa(cdEmpresa);
		setCdPedidoCompra(cdPedidoCompra);
		setCdProdutoServico(cdProdutoServico);
		setCdPlanoPagamento(cdPlanoPagamento);
		setCdPessoaContato(cdPessoaContato);
		setCdPessoaJuridica(cdPessoaJuridica);
		setNrPosicao(nrPosicao);
		setDtCotacao(dtCotacao);
		setQtDiasGarantia(qtDiasGarantia);
		setQtDisponivel(qtDisponivel);
		setVlCotacao(vlCotacao);
		setVlFrete(vlFrete);
		setVlTributos(vlTributos);
		setVlDesconto(vlDesconto);
		setTpResultado(tpResultado);
		setLgAssistenciaTecnica(lgAssistenciaTecnica);
		setLgProntaEntrega(lgProntaEntrega);
		setTxtObservacao(txtObservacao);
		setQtDiasEntrega(qtDiasEntrega);
		setQtDiasValidade(qtDiasValidade);
	}
	public void setCdCotacaoPedidoItem(int cdCotacaoPedidoItem){
		this.cdCotacaoPedidoItem=cdCotacaoPedidoItem;
	}
	public int getCdCotacaoPedidoItem(){
		return this.cdCotacaoPedidoItem;
	}
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPedidoCompra(int cdPedidoCompra){
		this.cdPedidoCompra=cdPedidoCompra;
	}
	public int getCdPedidoCompra(){
		return this.cdPedidoCompra;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setCdPessoaContato(int cdPessoaContato){
		this.cdPessoaContato=cdPessoaContato;
	}
	public int getCdPessoaContato(){
		return this.cdPessoaContato;
	}
	public void setCdPessoaJuridica(int cdPessoaJuridica){
		this.cdPessoaJuridica=cdPessoaJuridica;
	}
	public int getCdPessoaJuridica(){
		return this.cdPessoaJuridica;
	}
	public void setNrPosicao(int nrPosicao){
		this.nrPosicao=nrPosicao;
	}
	public int getNrPosicao(){
		return this.nrPosicao;
	}
	public void setDtCotacao(GregorianCalendar dtCotacao){
		this.dtCotacao=dtCotacao;
	}
	public GregorianCalendar getDtCotacao(){
		return this.dtCotacao;
	}
	public void setQtDiasGarantia(int qtDiasGarantia){
		this.qtDiasGarantia=qtDiasGarantia;
	}
	public int getQtDiasGarantia(){
		return this.qtDiasGarantia;
	}
	public void setQtDisponivel(float qtDisponivel){
		this.qtDisponivel=qtDisponivel;
	}
	public float getQtDisponivel(){
		return this.qtDisponivel;
	}
	public void setVlCotacao(float vlCotacao){
		this.vlCotacao=vlCotacao;
	}
	public float getVlCotacao(){
		return this.vlCotacao;
	}
	public void setVlFrete(float vlFrete){
		this.vlFrete=vlFrete;
	}
	public float getVlFrete(){
		return this.vlFrete;
	}
	public void setVlTributos(float vlTributos){
		this.vlTributos=vlTributos;
	}
	public float getVlTributos(){
		return this.vlTributos;
	}
	public void setVlDesconto(float vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public float getVlDesconto(){
		return this.vlDesconto;
	}
	public void setTpResultado(int tpResultado){
		this.tpResultado=tpResultado;
	}
	public int getTpResultado(){
		return this.tpResultado;
	}
	public void setLgAssistenciaTecnica(int lgAssistenciaTecnica){
		this.lgAssistenciaTecnica=lgAssistenciaTecnica;
	}
	public int getLgAssistenciaTecnica(){
		return this.lgAssistenciaTecnica;
	}
	public void setLgProntaEntrega(int lgProntaEntrega){
		this.lgProntaEntrega=lgProntaEntrega;
	}
	public int getLgProntaEntrega(){
		return this.lgProntaEntrega;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setQtDiasEntrega(int qtDiasEntrega){
		this.qtDiasEntrega=qtDiasEntrega;
	}
	public int getQtDiasEntrega(){
		return this.qtDiasEntrega;
	}
	public void setQtDiasValidade(int qtDiasValidade){
		this.qtDiasValidade=qtDiasValidade;
	}
	public int getQtDiasValidade(){
		return this.qtDiasValidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCotacaoPedidoItem: " +  getCdCotacaoPedidoItem();
		valueToString += ", cdFornecedor: " +  getCdFornecedor();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPedidoCompra: " +  getCdPedidoCompra();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", cdPessoaContato: " +  getCdPessoaContato();
		valueToString += ", cdPessoaJuridica: " +  getCdPessoaJuridica();
		valueToString += ", nrPosicao: " +  getNrPosicao();
		valueToString += ", dtCotacao: " +  sol.util.Util.formatDateTime(getDtCotacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtDiasGarantia: " +  getQtDiasGarantia();
		valueToString += ", qtDisponivel: " +  getQtDisponivel();
		valueToString += ", vlCotacao: " +  getVlCotacao();
		valueToString += ", vlFrete: " +  getVlFrete();
		valueToString += ", vlTributos: " +  getVlTributos();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", tpResultado: " +  getTpResultado();
		valueToString += ", lgAssistenciaTecnica: " +  getLgAssistenciaTecnica();
		valueToString += ", lgProntaEntrega: " +  getLgProntaEntrega();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", qtDiasEntrega: " +  getQtDiasEntrega();
		valueToString += ", qtDiasValidade: " +  getQtDiasValidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CotacaoPedidoItem(getCdCotacaoPedidoItem(),
			getCdFornecedor(),
			getCdEmpresa(),
			getCdPedidoCompra(),
			getCdProdutoServico(),
			getCdPlanoPagamento(),
			getCdPessoaContato(),
			getCdPessoaJuridica(),
			getNrPosicao(),
			getDtCotacao()==null ? null : (GregorianCalendar)getDtCotacao().clone(),
			getQtDiasGarantia(),
			getQtDisponivel(),
			getVlCotacao(),
			getVlFrete(),
			getVlTributos(),
			getVlDesconto(),
			getTpResultado(),
			getLgAssistenciaTecnica(),
			getLgProntaEntrega(),
			getTxtObservacao(),
			getQtDiasEntrega(),
			getQtDiasValidade());
	}

}
