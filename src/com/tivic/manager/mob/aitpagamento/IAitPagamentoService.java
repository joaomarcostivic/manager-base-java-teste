package com.tivic.manager.mob.aitpagamento;

import java.util.List;

import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitDTO;
import com.tivic.manager.mob.restituicao.RestituicaoDTO;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IAitPagamentoService {
	public PagedResponse<AitPagamento> find(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<AitPagamento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public void cancelarPagamento(AitPagamento aitPagamento, int cdUsuario) throws Exception;
	public void cancelarPagamento(AitPagamento aitPagamento, int cdUsuario, CustomConnection customConnection) throws Exception;
	public PagedResponse<AitPagamentoRecebidoDTO> findValoresRecebidos(SearchCriterios searchCriterios) throws Exception;
	public Search<AitPagamentoRecebidoDTO> findValoresRecebidos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public PagedResponse<RestituicaoDTO> findRestituicao(SearchCriterios searchCriterios) throws Exception; 
	public PagedResponse<AitPagamentoRecebidoDTO> reportPeriodo(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<RelatorioAitDTO> detalhamento(SearchCriterios searchCriterios) throws Exception;
	public byte[] gerarRelatorio(List<AitPagamentoRecebidoDTO> itemsList, SearchCriterios searchCriterios) throws Exception;
}
