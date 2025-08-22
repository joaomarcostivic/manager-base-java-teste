package com.tivic.manager.ptc.protocolosv3.recursos;

import java.util.List;

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class RecursoRepositoryDAO implements IRecursoRepository {

	@Override
	public int insert(Recurso recurso, CustomConnection customConnection) throws Exception {
		int retorno =  RecursoDAO.insert(recurso, customConnection.getConnection());
		if(retorno <= 0 ) 
			throw new Exception("Erro ao criar nova Ata");
		return retorno;
	}

	@Override
	public int update(Recurso recurso, CustomConnection customConnection) throws Exception {
		int retorno = RecursoDAO.update(recurso, customConnection.getConnection());
		if(retorno <= 0 ) 
			throw new Exception("Erro ao atualizar Ata");
		return retorno;
	}

	@Override
	public Recurso get(int cdDocumento, int cdAta, CustomConnection customConnection) throws Exception {
		Recurso recurso = RecursoDAO.get(cdDocumento, cdAta, customConnection.getConnection());
		return recurso;
	}

	@Override
	public List<Recurso> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ResultSetMapper<Recurso> rsm = new ResultSetMapper<Recurso>(RecursoDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), Recurso.class);
		return rsm.toList();
	}

	@Override
	public void delete(int cdDocumento, int cdAta, CustomConnection customConnection) throws Exception {
		int codigoRetorno = RecursoDAO.delete(cdDocumento, cdAta, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar recurso");
	}

}
