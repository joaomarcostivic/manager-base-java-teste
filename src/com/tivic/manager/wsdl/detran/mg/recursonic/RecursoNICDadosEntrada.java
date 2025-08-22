package com.tivic.manager.wsdl.detran.mg.recursonic;

import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class RecursoNICDadosEntrada extends DadosEntradaMG {
	
	public String getPlaca() {
		return itens.get("placa").getValor();
	}
	
	public void setPlaca(String placa) {
		itens.put("placa", new DadosItem(placa, 10, true));
	}

	public String getAit() {
		return itens.get("ait").getValor();
	}
	
	public void setAit(String ait) {
		itens.put("ait", new DadosItem(ait, 10, true));
	}

	public GregorianCalendar getDataInfracao() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_infracao").getValor());
	}
	
	public void setDataInfracao(GregorianCalendar dataDataInfracao) {
		itens.put("data_infracao", new DadosItem(Util.formatDate(dataDataInfracao, "yyyyMMdd"), 8, true));
	}
	
	public int getCodigoMovimentacao() {
		return Integer.parseInt(itens.get("codigo_movimentacao").getValor());
	}
	
	public void setCodigoMovimentacao(int codigoMovimentacao) {
		itens.put("codigo_movimentacao", new DadosItem(String.valueOf(codigoMovimentacao), 1, true));
	}
	
	public GregorianCalendar getDataMovimentacao() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_movimentacao").getValor());
	}
	
	public void setDataMovimentacao(GregorianCalendar dataMovimentacao) {
		itens.put("data_movimentacao", new DadosItem(Util.formatDate(dataMovimentacao, "yyyyMMdd"), 8, true));
	}
	
	public String getValorMulta() {
		return itens.get("valor_multa").getValor();
	}
	
	public void setValorMulta(double valorMulta) {
		itens.put("valor_multa", new DadosItem(String.valueOf(valorMulta), 11, true));
	}
	
	public String getNumeroProcessamento() {
		return itens.get("numero_processamento").getValor();
	}
	
	public void setNumeroProcessamento(int numeroProcessamento) {
		itens.put("numero_processamento", new DadosItem(String.valueOf(numeroProcessamento), 9, false));
	}
	
	public String getCodigoRenainf() {
		return itens.get("codigo_renainf").getValor();
	}
	
	public void setCodigoRenainf(String codigoRenainf) {
		itens.put("codigo_renainf", new DadosItem(codigoRenainf, 11, false));
	}
	
	public String getCodigoInfracaoGeradora() {
		return itens.get("codigo_infracao_geradora").getValor();
	}
	
	public void setCodigoInfracaoGeradora(String codigoInfracaoGeradora) {
		itens.put("codigo_infracao_geradora", new DadosItem(codigoInfracaoGeradora, 4, true));
	}
	
	public String getCodigoDesdobramentoInfracaoGeradora() {
		return itens.get("codigo_desdobramento_infracao_geradora").getValor();
	}
	
	public void setCodigoDesdobramentoInfracaoGeradora(String codigoDesdobramentoInfracaoGeradora) {
		itens.put("codigo_desdobramento_infracao_geradora", new DadosItem(codigoDesdobramentoInfracaoGeradora, 2, true));
	}
	
	public String getAitGeradora() {
		return itens.get("ait_geradora").getValor();
	}
	
	public void setAitGeradora(String aitGeradora) {
		itens.put("ait_geradora", new DadosItem(aitGeradora, 10, true));
	}
	
	public String getNumeroProcessamentoGeradora() {
		return itens.get("numero_processamento_geradora").getValor();
	}
	
	public void setNumeroProcessamentoGeradora(String numeroProcessamentoGeradora) {
		itens.put("numero_processamento_geradora", new DadosItem(numeroProcessamentoGeradora, 9, true));
	}

	public String getCodigoRenainfGeradora() {
		return itens.get("codigo_renainf_geradora").getValor();
	}
	
	public void setCodigoRenainfGeradora(String codigoRenainfGeradora) {
		itens.put("codigo_renainf_geradora", new DadosItem(codigoRenainfGeradora, 11, false));
	}
	
	public String getFatorMultiplicador() {
		return itens.get("fator_multiplicador").getValor();
	}
	
	public void setFatorMultiplicador(int fatorMultiplicador) {
		itens.put("fator_multiplicador", new DadosItem(String.valueOf(fatorMultiplicador), 2, true));
	}
	
	public String getCampoReservado() {
		return itens.get("campo_reservado").getValor();
	}
	
	public void setCampoReservado(String CampoReservado) {
		itens.put("campo_reservado", new DadosItem(CampoReservado, 89, false));
	}
}
