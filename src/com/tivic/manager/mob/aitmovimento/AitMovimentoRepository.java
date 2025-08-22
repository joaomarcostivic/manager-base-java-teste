package com.tivic.manager.mob.aitmovimento;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ait.relatorios.quantitativo.RelatorioQuantitativoDTO;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface AitMovimentoRepository {
	public int insert(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public void update(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public AitMovimento get(int cdMovimento, int cdAit) throws Exception;
	public AitMovimento get(int cdMovimento,  int cdAit, CustomConnection customConnection) throws Exception;
	public List<AitMovimento> find(SearchCriterios searchCriterios) throws Exception;
	public List<AitMovimento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public AitMovimento getByStatus(int cdAit, int tpStatus) throws Exception;
	public AitMovimento getByStatus(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception;
	public Search<RelatorioQuantitativoDTO> findQuantitativoMovimentos(SearchCriterios searchCriterios) throws Exception;
	public Search<RelatorioQuantitativoDTO> findQuantitativoMovimentos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentoNip(SearchCriterios searchCriterios, int tpMovimentoQuantitativo) throws Exception;
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentoNip(SearchCriterios searchCriterios, int tpMovimentoQuantitativo, CustomConnection customConnection) throws Exception;
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentosRecebidos(SearchCriterios searchCriterios) throws Exception;
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentosRecebidos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}

