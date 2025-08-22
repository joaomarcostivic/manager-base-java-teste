package com.tivic.manager.mob.relatorioestatisticas;

import java.util.List;

import com.tivic.sol.search.SearchCriterios;

public interface IGerarRelatorioEstatistico {
	public byte[] gerar(List<RelatorioEstatisticasDTO> itemsList, SearchCriterios searchCriterios) throws Exception;
	public byte[] gerarRelatorioNips(List<RelatorioEstatisticasNipDTO> itemsList, SearchCriterios searchCriterios) throws Exception;
	public byte[] gerarGraficoNais(List<RelatorioEstatisticasDTO> itemsList, SearchCriterios searchCriterios) throws Exception;
	public byte[] gerarGraficoNips(List<RelatorioEstatisticasNipDTO> itemsList, SearchCriterios searchCriterios) throws Exception;
}
