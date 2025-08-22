package com.tivic.manager.ptc.protocolosv3.jari;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.sol.search.SearchCriterios;

public class JariSearchBuilder {
	
	private SearchCriterios searchCriterios;

	public JariSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public JariSearchBuilder setTpStatus() {
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.RECURSO_JARI.getKey());
		return this;
	}
	
	public JariSearchBuilder setDtProtocoloInicial(String dataInicial) {
		searchCriterios.addCriteriosGreaterDate("D.dt_protocolo", getDataInicial(dataInicial));
		return this;
	}
	
	public JariSearchBuilder setDtProtocoloFinal(String dataFinal) {
		searchCriterios.addCriteriosMinorDate("D.dt_protocolo", getDataFinal(dataFinal));
		return this;
	}
	
	public JariSearchBuilder setCdSituacaoDocumento(int situacaoDocumento) {
		searchCriterios.addCriteriosEqualInteger("D.cd_situacao_documento", situacaoDocumento, situacaoDocumento > 0);
		return this;
	}
	
	private String getDataInicial(String data) {
		LocalDate dataInicial = (data == null || data.isEmpty())
		        ? LocalDate.now().withDayOfYear(1).withMonth(1)  
		        : LocalDate.parse(data, DateTimeFormatter.ISO_DATE);
		return dataInicial.toString();
		
	}
	
	private String getDataFinal(String data) {
		LocalDate dataFinal = (data == null || data.isEmpty())
		        ? LocalDate.now()  
		        : LocalDate.parse(data, DateTimeFormatter.ISO_DATE);
		return dataFinal.toString();
	}
	
	public SearchCriterios build() {
		return searchCriterios;
	}
}
