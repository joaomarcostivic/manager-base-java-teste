package com.tivic.manager.seg.usuario.repositories;

import java.util.List;
import com.tivic.manager.seg.Usuario;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IUsuarioRepository {
	void insert(Usuario usuario, CustomConnection customConnection) throws Exception;
	void update(Usuario usuario, CustomConnection customConnection) throws Exception;
	Usuario get(int cdUsuario) throws Exception;
	Usuario get(int cdUsuario, CustomConnection customConnection) throws Exception;
	List<Usuario> find(SearchCriterios searchCriterios) throws Exception;
	List<Usuario> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	void delete(int cdUsuario, CustomConnection customConnection) throws Exception;
	Usuario getByLogin(String nmLogin) throws Exception;
	Usuario getByLogin(String nmLogin, CustomConnection customConnection) throws Exception;
}
