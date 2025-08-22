	package com.tivic.manager.wsdl.detran.mg.recursodefesa;

import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class RecursoDefesaDadosEntrada extends DadosEntradaMG {

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
	
	public String getNumeroProcessoDefesa() {
		return itens.get("numero_processo_defesa").getValor();
	}
	
	public void setNumeroProcessoDefesa(String numeroProcessoDefesa) {
		itens.put("numero_processo_defesa", new DadosItem(numeroProcessoDefesa, 17, false));
	}

	public GregorianCalendar getDataEntradaDefesa() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_entrada_defesa").getValor());
	}
	
	public void setDataEntradaDefesa(GregorianCalendar dataEntradaDefesa) {
		itens.put("data_entrada_defesa", new DadosItem(Util.formatDate(dataEntradaDefesa, "yyyyMMdd"), 8, false));
	}

	public GregorianCalendar getDataEncerramentoDefesa() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_encerramento_defesa").getValor());
	}
	
	public void setDataEncerramentoDefesa(GregorianCalendar dataEncerramentoDefesa) {
		itens.put("data_encerramento_defesa", new DadosItem(Util.formatDate(dataEncerramentoDefesa, "yyyyMMdd"), 8, false));
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
	
	public void setNumeroDocumento(int numeroDocumento) {
		itens.put("numero_documento", new DadosItem(String.valueOf(numeroDocumento), 10, false));
	}
}
