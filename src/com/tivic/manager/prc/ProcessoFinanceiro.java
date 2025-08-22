package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class ProcessoFinanceiro {

	private int cdProcesso;
	private int cdEventoFinanceiro;
	private int cdProdutoServico;
	private int cdAndamento;
	private int cdPessoa;
	private int tpNaturezaEvento;
	private GregorianCalendar dtEventoFinanceiro;
	private Double vlEventoFinanceiro;
	private GregorianCalendar dtLancamento;
	private int cdContaPagar;
	private int cdContaReceber;
	private int cdUsuario;
	private int cdArquivo;
	private GregorianCalendar dtRevisao;
	private int cdUsuarioRevisao;
	private int tpSegmento;
	private int tpInstancia;
	private int cdEstado;
	private int cdAgendaItem;
	private int cdRegraFaturamento;
	private int cdEventoFinanceiroOrigem;
	private String nrReferencia;

	public ProcessoFinanceiro(){ }

	public ProcessoFinanceiro(int cdProcesso,
			int cdEventoFinanceiro,
			int cdProdutoServico,
			int cdAndamento,
			int cdPessoa,
			int tpNaturezaEvento,
			GregorianCalendar dtEventoFinanceiro,
			Double vlEventoFinanceiro,
			GregorianCalendar dtLancamento,
			int cdContaPagar,
			int cdContaReceber,
			int cdUsuario,
			int cdArquivo,
			GregorianCalendar dtRevisao,
			int cdUsuarioRevisao,
			int tpSegmento,
			int tpInstancia,
			int cdEstado,
			int cdAgendaItem,
			int cdRegraFaturamento,
			int cdEventoFinanceiroOrigem,
			String nrReferencia){
		setCdProcesso(cdProcesso);
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setCdProdutoServico(cdProdutoServico);
		setCdAndamento(cdAndamento);
		setCdPessoa(cdPessoa);
		setTpNaturezaEvento(tpNaturezaEvento);
		setDtEventoFinanceiro(dtEventoFinanceiro);
		setVlEventoFinanceiro(vlEventoFinanceiro);
		setDtLancamento(dtLancamento);
		setCdContaPagar(cdContaPagar);
		setCdContaReceber(cdContaReceber);
		setCdUsuario(cdUsuario);
		setCdArquivo(cdArquivo);
		setDtRevisao(dtRevisao);
		setCdUsuarioRevisao(cdUsuarioRevisao);
		setTpSegmento(tpSegmento);
		setTpInstancia(tpInstancia);
		setCdEstado(cdEstado);
		setCdAgendaItem(cdAgendaItem);
		setCdRegraFaturamento(cdRegraFaturamento);
		setCdEventoFinanceiroOrigem(cdEventoFinanceiroOrigem);
		setNrReferencia(nrReferencia);
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdAndamento(int cdAndamento){
		this.cdAndamento=cdAndamento;
	}
	public int getCdAndamento(){
		return this.cdAndamento;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpNaturezaEvento(int tpNaturezaEvento){
		this.tpNaturezaEvento=tpNaturezaEvento;
	}
	public int getTpNaturezaEvento(){
		return this.tpNaturezaEvento;
	}
	public void setDtEventoFinanceiro(GregorianCalendar dtEventoFinanceiro){
		this.dtEventoFinanceiro=dtEventoFinanceiro;
	}
	public GregorianCalendar getDtEventoFinanceiro(){
		return this.dtEventoFinanceiro;
	}
	public void setVlEventoFinanceiro(Double vlEventoFinanceiro){
		this.vlEventoFinanceiro=vlEventoFinanceiro;
	}
	public Double getVlEventoFinanceiro(){
		return this.vlEventoFinanceiro;
	}
	public void setDtLancamento(GregorianCalendar dtLancamento){
		this.dtLancamento=dtLancamento;
	}
	public GregorianCalendar getDtLancamento(){
		return this.dtLancamento;
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setDtRevisao(GregorianCalendar dtRevisao){
		this.dtRevisao=dtRevisao;
	}
	public GregorianCalendar getDtRevisao(){
		return this.dtRevisao;
	}
	public void setCdUsuarioRevisao(int cdUsuarioRevisao){
		this.cdUsuarioRevisao=cdUsuarioRevisao;
	}
	public int getCdUsuarioRevisao(){
		return this.cdUsuarioRevisao;
	}
	public void setTpSegmento(int tpSegmento){
		this.tpSegmento=tpSegmento;
	}
	public int getTpSegmento(){
		return this.tpSegmento;
	}
	public void setTpInstancia(int tpInstancia){
		this.tpInstancia=tpInstancia;
	}
	public int getTpInstancia(){
		return this.tpInstancia;
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setCdAgendaItem(int cdAgendaItem){
		this.cdAgendaItem=cdAgendaItem;
	}
	public int getCdAgendaItem(){
		return this.cdAgendaItem;
	}
	public void setCdRegraFaturamento(int cdRegraFaturamento){
		this.cdRegraFaturamento=cdRegraFaturamento;
	}
	public int getCdRegraFaturamento(){
		return this.cdRegraFaturamento;
	}
	public void setCdEventoFinanceiroOrigem(int cdEventoFinanceiroOrigem){
		this.cdEventoFinanceiroOrigem=cdEventoFinanceiroOrigem;
	}
	public int getCdEventoFinanceiroOrigem(){
		return this.cdEventoFinanceiroOrigem;
	}
	public void setNrReferencia(String nrReferencia){
		this.nrReferencia=nrReferencia;
	}
	public String getNrReferencia(){
		return this.nrReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProcesso: " +  getCdProcesso();
		valueToString += ", cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdAndamento: " +  getCdAndamento();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", tpNaturezaEvento: " +  getTpNaturezaEvento();
		valueToString += ", dtEventoFinanceiro: " +  sol.util.Util.formatDateTime(getDtEventoFinanceiro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlEventoFinanceiro: " +  getVlEventoFinanceiro();
		valueToString += ", dtLancamento: " +  sol.util.Util.formatDateTime(getDtLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", dtRevisao: " +  sol.util.Util.formatDateTime(getDtRevisao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuarioRevisao: " +  getCdUsuarioRevisao();
		valueToString += ", tpSegmento: " +  getTpSegmento();
		valueToString += ", tpInstancia: " +  getTpInstancia();
		valueToString += ", cdEstado: " +  getCdEstado();
		valueToString += ", cdAgendaItem: " +  getCdAgendaItem();
		valueToString += ", cdRegraFaturamento: " +  getCdRegraFaturamento();
		valueToString += ", cdEventoFinanceiroOrigem: " +  getCdEventoFinanceiroOrigem();
		valueToString += ", nrReferencia: " + getNrReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProcessoFinanceiro(getCdProcesso(),
			getCdEventoFinanceiro(),
			getCdProdutoServico(),
			getCdAndamento(),
			getCdPessoa(),
			getTpNaturezaEvento(),
			getDtEventoFinanceiro()==null ? null : (GregorianCalendar)getDtEventoFinanceiro().clone(),
			getVlEventoFinanceiro(),
			getDtLancamento()==null ? null : (GregorianCalendar)getDtLancamento().clone(),
			getCdContaPagar(),
			getCdContaReceber(),
			getCdUsuario(),
			getCdArquivo(),
			getDtRevisao()==null ? null : (GregorianCalendar)getDtRevisao().clone(),
			getCdUsuarioRevisao(),
			getTpSegmento(),
			getTpInstancia(),
			getCdEstado(),
			getCdAgendaItem(),
			getCdRegraFaturamento(),
			getCdEventoFinanceiroOrigem(),
			getNrReferencia());
	}

}