package com.tivic.manager.seg.usuario;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IUsuarioService {
	public void delete (int cdUsuario) throws Exception;
	public void delete (int cdUsuario, CustomConnection customConnection) throws Exception;
	PagedResponse<UsuarioLoginDTO> findLogins(SearchCriterios searchCriterios) throws Exception;
	Search<UsuarioLoginDTO> findLogins(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	Search<UsuarioLoginDTO> findLoginsBaseAntiga(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	void logoutUsuario(int cdUsuario) throws Exception;
	void logoutUsuario(int cdUsuario, CustomConnection customConnection) throws Exception;
}
