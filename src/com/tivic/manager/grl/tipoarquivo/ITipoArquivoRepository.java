package com.tivic.manager.grl.tipoarquivo;

import java.util.List;

import com.tivic.manager.grl.TipoArquivo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ITipoArquivoRepository {

	public TipoArquivo insert(TipoArquivo tipoArquivo, CustomConnection customConnection) throws Exception;
	public TipoArquivo update(TipoArquivo tipoArquivo, CustomConnection customConnection) throws Exception;
	public void delete(Integer cdTipoArquivo, CustomConnection customConnection) throws Exception;
	public TipoArquivo get(int cdTipoArquivo) throws Exception;
	public TipoArquivo get(int cdTipoArquivo, CustomConnection customConnection) throws Exception;
	public List<TipoArquivo> find(SearchCriterios searchCriterios) throws IllegalArgumentException, Exception;
	public List<TipoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception;

}
