package com.tivic.manager.wsdl.detran.mg.notificacao;

import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class NotificacaoDadosEntrada extends DadosEntradaMG  {

	public String getAit() {
		return itens.get("ait").getValor();
	}
	
	public void setAit(String ait) {
		itens.put("ait", new DadosItem(ait, 10, true));
	}

	public String getNumeroProcessamento() {
		return itens.get("numero_processamento").getValor();
	}
	
	public void setNumeroProcessamento(String numeroProcessamento) {
		itens.put("numero_processamento", new DadosItem(numeroProcessamento, 9, true));
	}

	public String getCodigoRenainf() {
		return itens.get("codigo_renainf").getValor();
	}
	
	public void setCodigoRenainf(String codigoRenainf) {
		itens.put("codigo_renainf", new DadosItem(codigoRenainf, 11, false));
	}

	public String getCodigoInfracao() {
		return itens.get("codigo_infracao").getValor();
	}
	
	public void setCodigoInfracao(String codigoInfracao) {
		itens.put("codigo_infracao", new DadosItem(codigoInfracao, 4, true));
	}

	public String getCodigoDesdobramentoInfracao() {
		return itens.get("codigo_desdobramento_infracao").getValor();
	}
	
	public void setCodigoDesdobramentoInfracao(String codigoDesdobramentoInfracao) {
		itens.put("codigo_desdobramento_infracao", new DadosItem(codigoDesdobramentoInfracao, 2, true));
	}

	public int getCodigoMovimentacao() {
		return Integer.parseInt(itens.get("codigo_movimentacao").getValor());
	}
	
	public void setCodigoMovimentacao(int codigoMovimentacao) {
		itens.put("codigo_movimentacao", new DadosItem(Util.fillNum(codigoMovimentacao, 3), 3, true));
	}

	public GregorianCalendar getDataNotificacao() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_notificacao").getValor());
	}
	
	public void setDataNotificacao(GregorianCalendar dataInfracao) {
		itens.put("data_notificacao", new DadosItem(Util.formatDate(dataInfracao, "yyyyMMdd"), 8, false));
	}

	public String getNumeroNotificacao() {
		return itens.get("numero_notificacao").getValor();
	}
	
	public void setNumeroNotificacao(String numeroNotificacao) {
		itens.put("numero_notificacao", new DadosItem(numeroNotificacao, 10, false));
	}

	public int getIndicadorContas() {
		return Integer.parseInt(itens.get("indicador_contas").getValor());
	}
	
	public void setIndicadorContas(int indicadorContas) {
		itens.put("indicador_contas", new DadosItem(String.valueOf(indicadorContas), 2, false));
	}

	public String ArCorreio() {
		return itens.get("numero_ar_correio").getValor();
	}
	
	public void setNumeroArCorreio(String numeroArCorreio) {
		itens.put("numero_ar_correio", new DadosItem(numeroArCorreio, 9, false));
	}

	public GregorianCalendar getDataPostagemCorreio() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_postagem_correio").getValor());
	}
	
	public void setDataPostagemCorreio(GregorianCalendar dataPostagemCorreio) {
		itens.put("data_postagem_correio", new DadosItem(Util.formatDate(dataPostagemCorreio, "yyyyMMdd"), 8, false));
	}

	public GregorianCalendar getDataEntregaCorreio() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_entrega_correio").getValor());
	}
	
	public void setDataEntregaCorreio(GregorianCalendar dataEntregaCorreio) {
		itens.put("data_entrega_correio", new DadosItem(Util.formatDate(dataEntregaCorreio, "yyyyMMdd"), 8, false));
	}

	public int getCodigoRetornoCorreio() {
		return Integer.parseInt(itens.get("codigo_retorno_correio").getValor());
	}
	
	public void setCodigoRetornoCorreio(int codigoRetornoCorreio) {
		itens.put("codigo_retorno_correio", new DadosItem(String.valueOf(codigoRetornoCorreio), 2, false));
	}

	public GregorianCalendar getDataPublicacao() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_publicacao").getValor());
	}
	
	public void setDataPublicacao(GregorianCalendar dataPublicacao) {
		itens.put("data_publicacao", new DadosItem(Util.formatDate(dataPublicacao, "yyyyMMdd"), 8, false));
	}

	public GregorianCalendar getDataNovoPrazoFici() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_novo_prazo_fici").getValor());
	}
	
	public void setDataNovoPrazoFici(GregorianCalendar dataNovoPrazoFici) {
		itens.put("data_novo_prazo_fici", new DadosItem(Util.formatDate(dataNovoPrazoFici, "yyyyMMdd"), 8, false));
	}

	public int getIndicadorNotificacaoEdital() {
		return Integer.parseInt(itens.get("indicador_notificacao_edital").getValor());
	}
	
	public void setIndicadorNotificacaoEdital(int indicadorNotificacaoEdital) {
		itens.put("indicador_notificacao_edital", new DadosItem(String.valueOf(indicadorNotificacaoEdital), 1, true));
	}

	public GregorianCalendar getDataNotificacaoEdital() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_notificacao_edital").getValor());
	}
	
	public void setDataNotificacaoEdital(GregorianCalendar dataNotificacaoEdital) {
		itens.put("data_notificacao_edital", new DadosItem(Util.formatDate(dataNotificacaoEdital, "yyyyMMdd"), 8, false));
	}

	public int getTipoPenalidade() {
		return Integer.parseInt(itens.get("tipo_penalidade").getValor());
	}
	
	public void setTipoPenalidade(int codigoRetornoCorreio) {
		itens.put("tipo_penalidade", new DadosItem(String.valueOf(codigoRetornoCorreio), 3, false));
	}

}
