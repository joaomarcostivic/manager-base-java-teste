package com.tivic.manager.mob.correios;

import java.util.List;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.CorreiosLoteDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class CorreiosLoteRepositoryDAO implements CorreiosLoteRepository {

	@Override
	public CorreiosLote update(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception {
		 int retorno = CorreiosLoteDAO.update(correiosLote, customConnection.getConnection());
		 if (retorno <= 0) {
			 throw new ValidacaoException("Erro ao atualizar lote.");
		 }
		 return correiosLote;
	}

	@Override
	public CorreiosLote insert(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception {
		int retorno = CorreiosLoteDAO.insert(correiosLote, customConnection.getConnection());
		if(retorno <= 0) {
			throw new ValidacaoException("Erro ao salvar novo lote");
		}
		return correiosLote;
	}

	@Override
	public CorreiosLote delete(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception {
		int retorno = CorreiosLoteDAO.delete(correiosLote.getCdLote(), customConnection.getConnection());
		if(retorno <= 0) {
			throw new ValidacaoException("Erro ao deletar lote");
		}
		return correiosLote;
	}
	
	@Override
	public CorreiosLote get(int cdLote, CustomConnection customConnection) throws Exception {
		CorreiosLote correiosLote = CorreiosLoteDAO.get(cdLote, customConnection.getConnection());
		if (correiosLote.getCdLote() <= 0) {
			throw new ValidacaoException("Lote não encontrado.");
		}
		return correiosLote;
	}

	@Override
	public List<CorreiosLote> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception {
		ResultSetMapper<CorreiosLote> rsm = new ResultSetMapper<CorreiosLote>(CorreiosLoteDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), CorreiosLote.class);
		return rsm.toList();
	}
}
