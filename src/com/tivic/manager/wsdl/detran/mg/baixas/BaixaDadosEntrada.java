package com.tivic.manager.wsdl.detran.mg.baixas;

import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class BaixaDadosEntrada extends DadosEntradaMG {
	
	public String getAit() {
		return itens.get("ait").getValor();
	}
	
	public void setAit(String ait) {
		itens.put("ait", new DadosItem(ait, 10, true));
	}
	
	public int getCodigoInfracao() {
		return Integer.parseInt(itens.get("codigo_infracao").getValor());
	}
	
	public void setCodigoInfracao(String codigoInfracao) {
		itens.put("codigo_infracao", new DadosItem(String.valueOf(codigoInfracao), 4, true));
	}
	
	public int getCodigoDesdobramentoInfracao() {
		return Integer.parseInt(itens.get("codigo_desdobramento_infracao").getValor());
	}
	
	public void setCodigoDesdobramentoInfracao(String codigoDesdobramentoInfracao) {
		itens.put("codigo_desdobramento_infracao", new DadosItem(String.valueOf(codigoDesdobramentoInfracao), 2, true));
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

	public int getCodigoMovimentacao() {
		return Integer.parseInt(itens.get("codigo_movimentacao").getValor());
	}
	
	public void setCodigoMovimentacao(int codigoMovimentacao) {
		itens.put("codigo_movimentacao", new DadosItem(String.valueOf(codigoMovimentacao), 3, true));
	}
	
	public GregorianCalendar getDataMovimentacao() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_movimentacao").getValor());
	}
	
	public void setDataMovimentacao(GregorianCalendar dataMovimentacao) {
		itens.put("data_movimentacao", new DadosItem(Util.formatDate(dataMovimentacao, "yyyyMMdd"), 8, true));
	}
	
	public String getNumeroDocumento() {
		return itens.get("numero_documento").getValor();
	}
	
	public void setNumeroDocumento(String numeroDocumento) {
		itens.put("numero_documento", new DadosItem(String.valueOf(numeroDocumento), 10, false));
	}
	
	public Double getValorPago() {
		return new Double(itens.get("valor_pago").getValor());
	}
	
	public void setValorPago(Double valor) {
		String str = Util.formatNumber(valor);
		str = str.replaceAll(",", "").replace(".", "");		
		str = Util.fill(str, 11, '0', 'E');
				
		itens.put("valor_pago", new DadosItem(str, 11, false));
	}
	
}
