package com.tivic.manager.grl.tipoarquivo;

import java.util.List;

import com.tivic.manager.grl.TipoArquivo;
import com.tivic.manager.grl.TipoArquivoDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class TipoArquivoRepositoryDAO implements ITipoArquivoRepository{
	
	@Override
	public TipoArquivo insert(TipoArquivo tipoArquivo, CustomConnection customConnection) throws Exception {
		int cdTipoArquivo = TipoArquivoDAO.insert(tipoArquivo, customConnection.getConnection());
		if (cdTipoArquivo <= 0) 
			throw new ValidacaoException("Erro ao inserir tipo arquivo.");
		tipoArquivo.setCdTipoArquivo(cdTipoArquivo);
		return tipoArquivo;
	}

	@Override
	public TipoArquivo update(TipoArquivo tipoArquivo, CustomConnection customConnection) throws Exception {
		int codigoRetorno = TipoArquivoDAO.update(tipoArquivo, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar tipo arquivo.");
		return tipoArquivo;
	}
	
	@Override
	public void delete(Integer cdTipoArquivo, CustomConnection customConnection) throws Exception {
		int codigoRetorno = TipoArquivoDAO.delete(cdTipoArquivo, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao remover tipo arquivo.");
	}
	
	@Override
	public TipoArquivo get(int cdTipoArquivo) throws Exception {
		return get(cdTipoArquivo, new CustomConnection());
	}

	@Override
	public TipoArquivo get(int cdTipoArquivo, CustomConnection customConnection) throws Exception {
		return TipoArquivoDAO.get(cdTipoArquivo, customConnection.getConnection());
	}
	
	@Override
	public List<TipoArquivo> find(SearchCriterios searchCriterios) throws IllegalArgumentException, Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<TipoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception {
		ResultSetMapper<TipoArquivo> rsm = new ResultSetMapper<TipoArquivo>(TipoArquivoDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), TipoArquivo.class);
		return rsm.toList();
	}

}
