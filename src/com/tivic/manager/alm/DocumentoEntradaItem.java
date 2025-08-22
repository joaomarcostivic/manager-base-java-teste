package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class DocumentoEntradaItem {

	private int cdDocumentoEntrada;
	private int cdProdutoServico;
	private int cdEmpresa;
	private float qtEntrada;
	private float vlUnitario;
	private float vlAcrescimo;
	private float vlDesconto;
	private int cdUnidadeMedida;
	private GregorianCalendar dtEntregaPrevista;
	private int cdNaturezaOperacao;
	private int cdAdicao;
	private int cdItem;
	private float vlVucv;
	private float vlDescontoGeral;
	private int cdTipoCredito;

	public DocumentoEntradaItem() { }

	public DocumentoEntradaItem(int cdDocumentoEntrada,
			int cdProdutoServico,
			int cdEmpresa,
			float qtEntrada,
			float vlUnitario,
			float vlAcrescimo,
			float vlDesconto,
			int cdUnidadeMedida,
			GregorianCalendar dtEntregaPrevista,
			int cdNaturezaOperacao,
			int cdAdicao,
			int cdItem,
			float vlVucv,
			float vlDescontoGeral,
			int cdTipoCredito) {
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setQtEntrada(qtEntrada);
		setVlUnitario(vlUnitario);
		setVlAcrescimo(vlAcrescimo);
		setVlDesconto(vlDesconto);
		setCdUnidadeMedida(cdUnidadeMedida);
		setDtEntregaPrevista(dtEntregaPrevista);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setCdAdicao(cdAdicao);
		setCdItem(cdItem);
		setVlVucv(vlVucv);
		setVlDescontoGeral(vlDescontoGeral);
		setCdTipoCredito(cdTipoCredito);
	}
	
	@Deprecated
	public DocumentoEntradaItem(int cdDocumentoEntrada,
			int cdProdutoServico,
			int cdEmpresa,
			int cdItem,
			float qtEntrada,
			float vlUnitario,
			float vlAcrescimo,
			float vlDesconto,
			int cdUnidadeMedida,
			GregorianCalendar dtEntregaPrevista) {
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setCdItem(cdItem);
		setQtEntrada(qtEntrada);
		setVlUnitario(vlUnitario);
		setVlAcrescimo(vlAcrescimo);
		setVlDesconto(vlDesconto);
		setCdUnidadeMedida(cdUnidadeMedida);
		setDtEntregaPrevista(dtEntregaPrevista);
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setQtEntrada(float qtEntrada){
		this.qtEntrada=qtEntrada;
	}
	public float getQtEntrada(){
		return this.qtEntrada;
	}
	public void setVlUnitario(float vlUnitario){
		this.vlUnitario=vlUnitario;
	}
	public float getVlUnitario(){
		return this.vlUnitario;
	}
	public void setVlAcrescimo(float vlAcrescimo){
		this.vlAcrescimo=vlAcrescimo;
	}
	public float getVlAcrescimo(){
		return this.vlAcrescimo;
	}
	public void setVlDesconto(float vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public float getVlDesconto(){
		return this.vlDesconto;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public void setDtEntregaPrevista(GregorianCalendar dtEntregaPrevista){
		this.dtEntregaPrevista=dtEntregaPrevista;
	}
	public GregorianCalendar getDtEntregaPrevista(){
		return this.dtEntregaPrevista;
	}
	public void setCdNaturezaOperacao(int cdNaturezaOperacao){
		this.cdNaturezaOperacao=cdNaturezaOperacao;
	}
	public int getCdNaturezaOperacao(){
		return this.cdNaturezaOperacao;
	}
	public void setCdAdicao(int cdAdicao){
		this.cdAdicao=cdAdicao;
	}
	public int getCdAdicao(){
		return this.cdAdicao;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public void setVlVucv(float vlVucv){
		this.vlVucv=vlVucv;
	}
	public float getVlVucv(){
		return this.vlVucv;
	}
	public void setVlDescontoGeral(float vlDescontoGeral){
		this.vlDescontoGeral=vlDescontoGeral;
	}
	public float getVlDescontoGeral(){
		return this.vlDescontoGeral;
	}
	public void setCdTipoCredito(int cdTipoCredito){
		this.cdTipoCredito=cdTipoCredito;
	}
	public int getCdTipoCredito(){
		return this.cdTipoCredito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", qtEntrada: " +  getQtEntrada();
		valueToString += ", vlUnitario: " +  getVlUnitario();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		valueToString += ", dtEntregaPrevista: " +  sol.util.Util.formatDateTime(getDtEntregaPrevista(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdNaturezaOperacao: " +  getCdNaturezaOperacao();
		valueToString += ", cdAdicao: " +  getCdAdicao();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", vlVucv: " +  getVlVucv();
		valueToString += ", vlDescontoGeral: " +  getVlDescontoGeral();
		valueToString += ", cdTipoCredito: " +  getCdTipoCredito();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoEntradaItem(getCdDocumentoEntrada(),
			getCdProdutoServico(),
			getCdEmpresa(),
			getQtEntrada(),
			getVlUnitario(),
			getVlAcrescimo(),
			getVlDesconto(),
			getCdUnidadeMedida(),
			getDtEntregaPrevista()==null ? null : (GregorianCalendar)getDtEntregaPrevista().clone(),
			getCdNaturezaOperacao(),
			getCdAdicao(),
			getCdItem(),
			getVlVucv(),
			getVlDescontoGeral(),
			getCdTipoCredito());
	}

}