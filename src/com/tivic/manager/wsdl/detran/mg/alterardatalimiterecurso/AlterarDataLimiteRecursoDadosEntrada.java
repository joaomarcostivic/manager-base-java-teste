package com.tivic.manager.wsdl.detran.mg.alterardatalimiterecurso;

import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;
import com.tivic.sol.util.date.DateUtil;

public class AlterarDataLimiteRecursoDadosEntrada extends DadosEntradaMG {
	
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
	
	public int getCodigoSistema() {
		return Integer.parseInt(itens.get("codigo_sistema").getValor());
	}
	
	public void setCodigoSistema(int codigoSistema) {
		itens.put("codigo_sistema", new DadosItem(String.valueOf(codigoSistema), 1, true));
	}

	public String getOperacao() {
		return itens.get("operacao").getValor();
	}
	
	public void setOperacao(String operacao) {
		itens.put("operacao", new DadosItem(String.valueOf(operacao), 1, true));
	}

	public GregorianCalendar getNovaDataLimiteRecurso() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("nova_data_limite_recurso").getValor());
	}

	public void setNovaDataLimiteRecurso(GregorianCalendar novaDataLimiteRecurso) {
		itens.put("nova_data_limite_recurso", new DadosItem(DateUtil.formatDate(novaDataLimiteRecurso, "yyyyMMdd"), 8, false));
	}
	
}
