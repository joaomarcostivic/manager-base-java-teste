package com.tivic.manager.mob.aitmovimentodocumento;

import java.util.List;

import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.AitMovimentoDocumentoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import javax.ws.rs.BadRequestException;

public class AitMovimentoDocumentoRepositoryDAO implements AitMovimentoDocumentoRepository{


	@Override
	public void insert(AitMovimentoDocumento aitMovimentoDocumento, CustomConnection customConnection) throws Exception {
		int ret = AitMovimentoDocumentoDAO.insert(aitMovimentoDocumento, customConnection.getConnection());
		if (ret < 0)
			throw new Exception("Erro ao inserir AitMovimentoDocumento.");
	}

	@Override
	public void update(AitMovimentoDocumento aitMovimentoDocumento, CustomConnection customConnection) throws Exception {
		int ret = AitMovimentoDocumentoDAO.update(aitMovimentoDocumento, customConnection.getConnection());
		if(ret < 0)
			throw new Exception("Erro ao atualizar AitMovimentoDocumento.");
	}

	@Override
	public AitMovimentoDocumento get(int cdAit, int cdMovimento, int cdDocumento) throws Exception {
		return get(cdAit, cdMovimento, cdDocumento, new CustomConnection());
	}

	@Override
	public AitMovimentoDocumento get(int cdAit, int cdMovimento, int cdDocumento, CustomConnection customConnection) throws Exception {
		return AitMovimentoDocumentoDAO.get(cdAit, cdMovimento, cdDocumento, customConnection.getConnection());
	}

	@Override
	public List<AitMovimentoDocumento> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitMovimentoDocumento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitMovimentoDocumento> search = new SearchBuilder<AitMovimentoDocumento>("mob_ait_movimento_documento")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(AitMovimentoDocumento.class);
	}


	@Override
	public AitMovimentoDocumento getAit(int cdAit, int cdMovimento) throws BadRequestException, Exception {
		return getAit(cdAit, cdMovimento, new CustomConnection());
	}
	
	public AitMovimentoDocumento getAit(int cdAit, int cdMovimento, CustomConnection customConnection) throws BadRequestException, Exception {
		AitMovimentoDocumento aitMovimentoDocumento = AitMovimentoDocumentoDAO.getAit(cdAit, cdMovimento, customConnection.getConnection());

		if (aitMovimentoDocumento == null) {
			throw new BadRequestException("Nenhuma Movimento de AIT encontrado");
		}

		return aitMovimentoDocumento;
	}

}
