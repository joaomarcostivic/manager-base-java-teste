package com.tivic.manager.mob.listagempagamentos;

import java.util.List;

import com.tivic.sol.search.SearchCriterios;

public interface IGeraRelatorioListagemPagamentos {
	public byte[] gerar(List<RelatorioPagamentoDTO> itemsList) throws Exception;
	public byte[] gerarGrafico(List<RelatorioPagamentoDTO> itemsList, SearchCriterios searchCriterios) throws Exception;
}
