package com.tivic.manager.mob.lotes.impressao.pix.repository;

import java.util.List;

import com.tivic.manager.mob.lotes.impressao.pix.dao.AitPixDAO;
import com.tivic.manager.mob.lotes.impressao.pix.model.AitPix;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitPixRepositoryDAO implements AitPixRepository {
	@Override
	public AitPix insert(AitPix AitPix, CustomConnection customConnection) throws Exception {
		int CdPix = AitPixDAO.insert(AitPix, customConnection.getConnection());
		if (CdPix <= 0)
			throw new ValidacaoException("Erro ao inserir lote");
		AitPix.setCdPix(CdPix);
		return AitPix;
	}

	@Override
	public AitPix update(AitPix AitPix, CustomConnection customConnection) throws Exception {
		int codigoRetorno = AitPixDAO.update(AitPix, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar AitPix");
		return AitPix;
	}

	@Override
	public AitPix delete(AitPix AitPix, CustomConnection customConnection) throws Exception {
		int codigoRetorno = AitPixDAO.delete(AitPix.getCdPix(), customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar AitPix");
		return AitPix;
	}

	@Override
	public AitPix get(int cdPix, CustomConnection customConnection) throws Exception {
		return AitPixDAO.get(cdPix, customConnection.getConnection());
	}

	@Override
	public List<AitPix> getAll(CustomConnection customConnection) throws Exception {
		ResultSetMapper<AitPix> rsm = new ResultSetMapper<AitPix>(AitPixDAO.getAll(), AitPix.class);
		return rsm.toList();
	}
	
	@Override
	public List<AitPix> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitPix> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitPix> search = new SearchBuilder<AitPix>("mob_ait_pix")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(AitPix.class);
	} 

}
