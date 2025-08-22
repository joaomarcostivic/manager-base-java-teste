package com.tivic.manager.mob.correios;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosEtiquetaDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class CorreiosEtiquetaRepositoryDAO implements ICorreiosEtiquetaRepository {

	@Override
	public void update(CorreiosEtiqueta correiosEtiqueta, CustomConnection customConnection) throws Exception {
		int retorno = CorreiosEtiquetaDAO.update(correiosEtiqueta, customConnection.getConnection());
		if (retorno <= 0) {
			throw new BadRequestException("Erro ao inserir etiqueta");
		}
	}

	@Override
	public CorreiosEtiqueta get(int cdEtiqueta, CustomConnection customConnection) throws Exception {
		return CorreiosEtiquetaDAO.get(cdEtiqueta, customConnection.getConnection());
	}

	@Override
	public void insert(CorreiosEtiqueta correiosEtiqueta, CustomConnection customConnection) throws Exception {
		int retorno = CorreiosEtiquetaDAO.insert(correiosEtiqueta, customConnection.getConnection());
		if (retorno <= 0) {
			throw new BadRequestException("Erro ao inserir etiqueta");
		}
	}
	
	@Override
	public List<CorreiosEtiqueta> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception {
		ResultSetMapper<CorreiosEtiqueta> rsm = new ResultSetMapper<CorreiosEtiqueta>(CorreiosEtiquetaDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), CorreiosEtiqueta.class);
		return rsm.toList();
	}

	@Override
	public void delete(int cdEtiqueta, CustomConnection customConnection) throws Exception {
		int codigoRetorno = CorreiosEtiquetaDAO.delete(cdEtiqueta, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar lote");
	}
}
