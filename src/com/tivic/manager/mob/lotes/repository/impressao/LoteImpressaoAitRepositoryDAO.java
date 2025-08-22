package com.tivic.manager.mob.lotes.repository.impressao;

import java.util.List;

import com.tivic.manager.mob.lotes.dao.impressao.LoteImpressaoAitDAO;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class LoteImpressaoAitRepositoryDAO implements ILoteImpressaoAitRepository {
	
	@Override
	public void insert(LoteImpressaoAit loteImpressao, CustomConnection customConnection) throws Exception {
		int cdLoteImpressao = LoteImpressaoAitDAO.insert(loteImpressao, customConnection.getConnection());
		if (cdLoteImpressao <= 0)
			throw new Exception("Erro ao inserir LoteImpressaoAit.");
		loteImpressao.setCdLoteImpressao(cdLoteImpressao);
	}

	@Override
	public void update(LoteImpressaoAit loteImpressao, CustomConnection customConnection) throws Exception {
		LoteImpressaoAitDAO.update(loteImpressao, customConnection.getConnection());
	}
	
	@Override
	public LoteImpressaoAit delete(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoAitDAO.delete(loteImpressaoAit.getCdLoteImpressao(),  loteImpressaoAit.getCdAit(), customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar AIT do lote");
		return loteImpressaoAit;
	}

	@Override
	public LoteImpressaoAit get(int cdLoteImpressao, int cdAit, CustomConnection customConnection) throws Exception {
		return LoteImpressaoAitDAO.get(cdLoteImpressao, cdAit, customConnection.getConnection());
	}

	@Override
	public List<LoteImpressaoAit> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<LoteImpressaoAit> search = new SearchBuilder<LoteImpressaoAit>("mob_lote_impressao_ait")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(LoteImpressaoAit.class);
	}
	
	@Override
	public List<LoteImpressaoAit> findByCdLoteImpressao(int cdLoteImpressao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLoteImpressao);
		Search<LoteImpressaoAit> search = new SearchBuilder<LoteImpressaoAit>("mob_lote_impressao_ait A")
				.searchCriterios(searchCriterios)
				.build();
		return search.getList(LoteImpressaoAit.class);
	}
}
