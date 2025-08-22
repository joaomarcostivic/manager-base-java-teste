package com.tivic.manager.wsdl.detran.mg.recursojari;

import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class RecursoJariDadosEntrada extends DadosEntradaMG {

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
		itens.put("codigo_movimentacao", new DadosItem(String.valueOf(codigoMovimentacao), 3, true));
	}

	public String getNumeroProcessoRecurso() {
		return itens.get("numero_processo_recurso").getValor();
	}
	
	public void setNumeroProcessoRecurso(String numeroProcessoRecurso) {
		itens.put("numero_processo_recurso", new DadosItem(numeroProcessoRecurso, 17, true));
	}

	public GregorianCalendar getDataEntradaRecurso() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_entrada_recurso").getValor());
	}
	
	public void setDataEntradaRecurso(GregorianCalendar dataEntradaRecurso) {
		itens.put("data_entrada_recurso", new DadosItem(Util.formatDate(dataEntradaRecurso, "yyyyMMdd"), 8, true));
	}

	public GregorianCalendar getDataEncerramentoRecurso() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_encerramento_recurso").getValor());
	}
	
	public void setDataEncerramentoRecurso(GregorianCalendar dataEncerramentoRecurso) {
		itens.put("data_encerramento_recurso", new DadosItem(Util.formatDate(dataEncerramentoRecurso, "yyyyMMdd"), 8, false));
	}

	public int getCodigoReclassificacaoInfracao() {
		return Integer.parseInt(itens.get("codigo_reclassificacao_infracao").getValor());
	}
	
	public void setCodigoReclassificacaoInfracao(int codigoReclassificacaoInfracao) {
		itens.put("codigo_reclassificacao_infracao", new DadosItem(String.valueOf(codigoReclassificacaoInfracao), 6, false));
	}

	public GregorianCalendar getDataMovimentacao() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_movimentacao").getValor());
	}
	
	public void setDataMovimentacao(GregorianCalendar dataMovimentacao) {
		itens.put("data_movimentacao", new DadosItem(Util.formatDate(dataMovimentacao, "yyyyMMdd"), 8, true));
	}

	public int getNumeroDocumento() {
		return Integer.parseInt(itens.get("numero_documento").getValor());
	}
	
	public void setNumeroDocumento(String numeroDocumento) {
		itens.put("numero_documento", new DadosItem(numeroDocumento, 10, false));
	}

	public GregorianCalendar getDataPublicacaoResultadoJari() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_publicacao_resultado_jari").getValor());
	}
	
	public void setDataPublicacaoResultadoJari(GregorianCalendar dataPublicacaoResultadoJari) {
		itens.put("data_publicacao_resultado_jari", new DadosItem(Util.formatDate(dataPublicacaoResultadoJari, "yyyyMMdd"), 8, false));
	}

}
