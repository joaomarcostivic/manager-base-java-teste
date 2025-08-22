package com.tivic.manager.adapter.base.antiga.usuario;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.usuario.repositories.IUsuarioRepository;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class UsuarioRepositoryOldDAO implements IUsuarioRepository {
	
	private IAdapterService<UsuarioOld, Usuario> adapterService;
	
	public UsuarioRepositoryOldDAO() throws Exception {
		this.adapterService = new AdapterUsuarioService();
	}

	@Override
	public void insert(Usuario usuario, CustomConnection customConnection) throws Exception {
		UsuarioOld usuarioOld = this.adapterService.toBaseAntiga(usuario);
		int codRetorno = UsuarioOldDAO.insert(usuarioOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar usuário");
		usuario.setCdUsuario(codRetorno);
	}

	@Override
	public void update(Usuario usuario, CustomConnection customConnection) throws Exception {
		UsuarioOld usuarioOld = this.adapterService.toBaseAntiga(usuario);
		int codRetorno = UsuarioOldDAO.update(usuarioOld, customConnection.getConnection());
		if (codRetorno <= 0)
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
		UsuarioOld usuarioOld = UsuarioOldDAO.get(cdUsuario, customConnection.getConnection());
		return this.adapterService.toBaseNova(usuarioOld);
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
		List<Usuario> usuarios = new ArrayList<Usuario>();
		ResultSetMapper<UsuarioOld> rsm = new ResultSetMapper<UsuarioOld>(UsuarioOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), UsuarioOld.class);
		List<UsuarioOld> UsuarioOldList = rsm.toList();
		for (UsuarioOld usuarioOld : UsuarioOldList) {
			Usuario usuario = this.adapterService.toBaseNova(usuarioOld);
			usuarios.add(usuario);
		}
		return usuarios;
	}

	@Override
	public void delete(int cdUsuario, CustomConnection customConnection) throws Exception {
		int cod = UsuarioOldDAO.delete(cdUsuario, customConnection.getConnection());
		if(cod <= 0) {
			throw new Exception("Erro ao deletar usuário.");
		}
	}
	
	@Override
	public Usuario getByLogin(String nmNick) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_nick", nmNick);
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
	public Usuario getByLogin(String nmNick, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_nick", nmNick);
		List<Usuario> usuarioList = find(searchCriterios, customConnection);
		return usuarioList.get(0);
	}
}
