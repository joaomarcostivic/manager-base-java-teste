package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class DocumentoSaidaItem {

	private int cdDocumentoSaida;
	private int cdProdutoServico;
	private int cdEmpresa;
	private float qtSaida;
	private float vlUnitario;
	private float vlAcrescimo;
	private float vlDesconto;
	private GregorianCalendar dtEntregaPrevista;
	private int cdUnidadeMedida;
	private int cdTabelaPreco;
	private int cdItem;
	private int cdBico;
	private float vlDescontoGeral;
	private int cdNaturezaOperacao;
	private float vlPrecoTabela;
	private float qtEncerranteFinal;
	private int cdTipoContribuicaoSocial;
	
	public DocumentoSaidaItem(){}
	
	public DocumentoSaidaItem(int cdDocumentoSaida,
			int cdProdutoServico,
			int cdEmpresa,
			float qtSaida,
			float vlUnitario,
			float vlAcrescimo,
			float vlDesconto,
			GregorianCalendar dtEntregaPrevista,
			int cdUnidadeMedida,
			int cdTabelaPreco,
			int cdItem,
			int cdBico){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setQtSaida(qtSaida);
		setVlUnitario(vlUnitario);
		setVlAcrescimo(vlAcrescimo);
		setVlDesconto(vlDesconto);
		setDtEntregaPrevista(dtEntregaPrevista);
		setCdUnidadeMedida(cdUnidadeMedida);
		setCdTabelaPreco(cdTabelaPreco);
		setCdItem(cdItem);
		setCdBico(cdBico);
		setVlDescontoGeral(0);
	}
	
	public DocumentoSaidaItem(int cdDocumentoSaida,
			int cdProdutoServico,
			int cdEmpresa,
			float qtSaida,
			float vlUnitario,
			float vlAcrescimo,
			float vlDesconto,
			GregorianCalendar dtEntregaPrevista,
			int cdUnidadeMedida,
			int cdTabelaPreco,
			int cdItem,
			int cdBico,
			float qtEncerranteFinal){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setQtSaida(qtSaida);
		setVlUnitario(vlUnitario);
		setVlAcrescimo(vlAcrescimo);
		setVlDesconto(vlDesconto);
		setDtEntregaPrevista(dtEntregaPrevista);
		setCdUnidadeMedida(cdUnidadeMedida);
		setCdTabelaPreco(cdTabelaPreco);
		setCdItem(cdItem);
		setCdBico(cdBico);
		setVlDescontoGeral(0);
		setQtEncerranteFinal(qtEncerranteFinal);
	}
	
	public DocumentoSaidaItem(int cdDocumentoSaida,
			int cdProdutoServico,
			int cdEmpresa,
			float qtSaida,
			float vlUnitario,
			float vlAcrescimo,
			float vlDesconto,
			GregorianCalendar dtEntregaPrevista,
			int cdUnidadeMedida,
			int cdTabelaPreco,
			int cdItem,
			int cdBico,
			float vlDescontoGeral,
			int cdNaturezaOperacao,
			int cdTipoContribuicaoSocial){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setQtSaida(qtSaida);
		setVlUnitario(vlUnitario);
		setVlAcrescimo(vlAcrescimo);
		setVlDesconto(vlDesconto);
		setDtEntregaPrevista(dtEntregaPrevista);
		setCdUnidadeMedida(cdUnidadeMedida);
		setCdTabelaPreco(cdTabelaPreco);
		setCdItem(cdItem);
		setCdBico(cdBico);
		setVlDescontoGeral(vlDescontoGeral);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setCdTipoContribuicaoSocial(cdTipoContribuicaoSocial);
	}
	
	public DocumentoSaidaItem(int cdDocumentoSaida,
			int cdProdutoServico,
			int cdEmpresa,
			float qtSaida,
			float vlUnitario,
			float vlAcrescimo,
			float vlDesconto,
			GregorianCalendar dtEntregaPrevista,
			int cdUnidadeMedida,
			int cdTabelaPreco,
			int cdItem,
			int cdBico,
			float vlDescontoGeral,
			int cdNaturezaOperacao,
			float vlTabelaPreco,
			float qtEncerranteFinal,
			int cdTipoContribuicaoSocial){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setQtSaida(qtSaida);
		setVlUnitario(vlUnitario);
		setVlAcrescimo(vlAcrescimo);
		setVlDesconto(vlDesconto);
		setDtEntregaPrevista(dtEntregaPrevista);
		setCdUnidadeMedida(cdUnidadeMedida);
		setCdTabelaPreco(cdTabelaPreco);
		setCdItem(cdItem);
		setCdBico(cdBico);
		setVlDescontoGeral(vlDescontoGeral);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setVlPrecoTabela(vlTabelaPreco);
		setCdTipoContribuicaoSocial(cdTipoContribuicaoSocial);
		setQtEncerranteFinal(qtEncerranteFinal);
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
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
	public void setQtSaida(float qtSaida){
		this.qtSaida=qtSaida;
	}
	public float getQtSaida(){
		return this.qtSaida;
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
	public void setDtEntregaPrevista(GregorianCalendar dtEntregaPrevista){
		this.dtEntregaPrevista=dtEntregaPrevista;
	}
	public GregorianCalendar getDtEntregaPrevista(){
		return this.dtEntregaPrevista;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public void setCdBico(int cdBico){
		this.cdBico=cdBico;
	}
	public int getCdBico(){
		return this.cdBico;
	}
	public void setVlDescontoGeral(float vlDescontoGeral){
		this.vlDescontoGeral=vlDescontoGeral;
	}
	public float getVlDescontoGeral(){
		return this.vlDescontoGeral;
	}
	public void setCdNaturezaOperacao(int cdNaturezaOperacao){
		this.cdNaturezaOperacao=cdNaturezaOperacao;
	}
	public int getCdNaturezaOperacao(){
		return this.cdNaturezaOperacao;
	}
	public void setVlPrecoTabela(float vlPrecoTabela) {
		this.vlPrecoTabela = vlPrecoTabela;
	}
	public float getVlPrecoTabela() {
		return vlPrecoTabela;
	}
	public void setQtEncerranteFinal(float qtEncerranteFinal) {
		this.qtEncerranteFinal = qtEncerranteFinal;
	}
	public float getQtEncerranteFinal() {
		return qtEncerranteFinal;
	}
	public int getCdTipoContribuicaoSocial() {
		return cdTipoContribuicaoSocial;
	}
	public void setCdTipoContribuicaoSocial(int cdTipoContribuicaoSocial) {
		this.cdTipoContribuicaoSocial = cdTipoContribuicaoSocial;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", qtSaida: " +  getQtSaida();
		valueToString += ", vlUnitario: " +  getVlUnitario();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", dtEntregaPrevista: " +  sol.util.Util.formatDateTime(getDtEntregaPrevista(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", cdBico: " +  getCdBico();
		valueToString += ", vlDescontoGeral: " +  getVlDescontoGeral();
		valueToString += ", cdNaturezaOperacao: " +  getCdNaturezaOperacao();
		valueToString += ", vlPrecoTabela: " +  getVlPrecoTabela();
		valueToString += ", qtEncerranteFinal: " +  getQtEncerranteFinal();
		valueToString += ", cdTipoContribuicaoSocial: " +  getCdTipoContribuicaoSocial();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoSaidaItem(getCdDocumentoSaida(),
			getCdProdutoServico(),
			getCdEmpresa(),
			getQtSaida(),
			getVlUnitario(),
			getVlAcrescimo(),
			getVlDesconto(),
			getDtEntregaPrevista()==null ? null : (GregorianCalendar)getDtEntregaPrevista().clone(),
			getCdUnidadeMedida(),
			getCdTabelaPreco(),
			getCdItem(),
			getCdBico(),
			getVlDescontoGeral(),
			getCdNaturezaOperacao(),
			getVlPrecoTabela(),
			getQtEncerranteFinal(),
			getCdTipoContribuicaoSocial());
	}

}
