package com.tivic.manager.relatorios.estatisticasaits;

import java.util.List;

import com.tivic.sol.search.SearchCriterios;

public interface IGeraRelatorioEstatisticasAits {
	public byte[] gerar(List<RelatorioEstatisticasAitsDTO> itemsList, SearchCriterios searchCriterios) throws Exception;
	public byte[] gerarGrafico(List<RelatorioEstatisticasAitsDTO> itemsList, SearchCriterios searchCriterios) throws Exception;
}
