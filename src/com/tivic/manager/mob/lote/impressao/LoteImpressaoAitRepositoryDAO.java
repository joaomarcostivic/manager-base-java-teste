package com.tivic.manager.mob.lote.impressao;

import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitDAO;
import com.tivic.manager.util.ResultSetMapper;

import javax.ws.rs.core.NoContentException;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;

public class LoteImpressaoAitRepositoryDAO implements ILoteImpressaoAitRepository {

	@Override
	public LoteImpressaoAit insert(LoteImpressaoAit objeto, CustomConnection customConnection) throws Exception {
		int cdLoteImpressaoAit = LoteImpressaoAitDAO.insert(objeto, customConnection.getConnection());
		if (cdLoteImpressaoAit <= 0)
			throw new ValidacaoException("Erro ao inserir AIT no lote");
		return objeto;
	}

	@Override
	public LoteImpressaoAit update(LoteImpressaoAit objeto, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoAitDAO.update(objeto, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar AIT do lote");
		return objeto;
	}

	@Override
	public LoteImpressaoAit delete(LoteImpressaoAit objeto, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteImpressaoAitDAO.delete(objeto.getCdLoteImpressao(),  objeto.getCdAit(), customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar AIT do lote");
		return objeto;
	}

	@Override
	public LoteImpressaoAit get(int cdLoteImpressao, int cdAit, CustomConnection customConnection) throws Exception {
		return LoteImpressaoAitDAO.get(cdLoteImpressao, cdAit, customConnection.getConnection());
	}

	@Override
	public List<LoteImpressaoAit> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ResultSetMapper<LoteImpressaoAit> rsm = new ResultSetMapper<LoteImpressaoAit>(LoteImpressaoAitDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), LoteImpressaoAit.class);
		return rsm.toList();
	}
	
	@Override
	public LoteImpressaoAitDTO getDTO(int cdAit, int tpDocumento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("B.tp_documento", tpDocumento);
		Search<LoteImpressaoAitDTO> search = new SearchBuilder<LoteImpressaoAitDTO>("mob_lote_impressao_ait A")
				.addJoinTable("LEFT OUTER JOIN mob_lote_impressao B ON (A.cd_lote_impressao = B.cd_lote_impressao)")
        		.searchCriterios(searchCriterios)
				.count()
				.customConnection(customConnection)
				.build();
		List<LoteImpressaoAitDTO> list = search.getList(LoteImpressaoAitDTO.class);
		if (list.isEmpty()) {
			throw new NoContentException("Não foi encontrado o lote desse AIT.");
		}
		return list.get(0);
	}
}