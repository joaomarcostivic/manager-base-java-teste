package com.tivic.manager.wsdl.detran.mg.consultainfracoes;

import java.util.GregorianCalendar;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class ConsultarInfracoesDadosEntrada extends DadosEntradaMG {

	public int getTipoConsulta() {
		return Integer.parseInt(itens.get("tipo_consulta").getValor());
	}
	
	public void setTipoConsulta(int tipoConsulta) {
		itens.put("tipo_consulta", new DadosItem(String.valueOf(tipoConsulta), 1, true));
	}
	
	public String getCnh() {
		return itens.get("numero_cnh").getValor();
	}
	
	public void setCnh(String cnh) {
		itens.put("numero_cnh", new DadosItem(cnh, 11, true));
	}

	public String getCpf() {
		return itens.get("numero_cpf").getValor();
	}
	
	public void setCpf(String cpf) {
		itens.put("numero_cpf", new DadosItem(cpf, 11, true));
	}

	public GregorianCalendar getDataInicioConsulta() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_inicio_consulta").getValor());
	}
	
	public void setDataInicioConsulta(GregorianCalendar dataInicioConsulta) {
		itens.put("data_inicio_consulta", new DadosItem(Util.formatDate(dataInicioConsulta, "yyyyMMdd"), 8, true));
	}

	public GregorianCalendar getDataFinalConsulta() {
		return Util.convStringSemFormatacaoReverseSToGregorianCalendar(itens.get("data_fim_consulta").getValor());
	}
	
	public void setDataFinalConsulta(GregorianCalendar dataFinalConsulta) {
		itens.put("data_fim_consulta", new DadosItem(Util.formatDate(dataFinalConsulta, "yyyyMMdd"), 8, true));
	}
	
}
