package com.tivic.manager.seg.usuario.repositories;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;


public class UsuarioRepositoryDAO implements IUsuarioRepository {

	@Override
	public void insert(Usuario usuario, CustomConnection customConnection) throws Exception {
		int cod = UsuarioDAO.insert(usuario, customConnection.getConnection());
		if (cod <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar usuário");
	}

	@Override
	public void update(Usuario usuario, CustomConnection customConnection) throws Exception {
		int cod = UsuarioDAO.update(usuario, customConnection.getConnection());
		if (cod <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar usuário");
	}

	@Override
	public Usuario get(int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.finishConnection();
			Usuario usuario = get(cdUsuario, customConnection);
			return usuario;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Usuario get(int cdUsuario, CustomConnection customConnection) throws Exception {
		Usuario usuario = UsuarioDAO.get(cdUsuario, customConnection.getConnection());
		return usuario;
	}

	@Override
	public List<Usuario> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Usuario> usuarioList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return usuarioList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Usuario> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Usuario> search = new SearchBuilder<Usuario>("seg_usuario")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Usuario.class);
	}

	@Override
	public void delete(int cdUsuario, CustomConnection customConnection) throws Exception {
		int cod = UsuarioDAO.delete(cdUsuario, customConnection.getConnection());
		if(cod <= 0) {
			throw new Exception("Erro ao deletar usuário.");
		}
	}
	

	@Override
	public Usuario getByLogin(String nmLogin) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_login", nmLogin);
		try {
			customConnection.finishConnection();
			List<Usuario> usuarioList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return usuarioList.get(0);
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Usuario getByLogin(String nmLogin, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_login", nmLogin);
		List<Usuario> usuarioList = find(searchCriterios, customConnection);
		return usuarioList.get(0);
	}
}
