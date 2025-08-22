package com.tivic.manager.seg.usuario;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.usuario.enums.UsuarioSituacaoEnum;
import com.tivic.manager.seg.usuario.repositories.IUsuarioRepository;
import com.tivic.manager.seg.usuario.validators.DeleteUserValidators;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class UsuarioService implements IUsuarioService {
	private IUsuarioRepository usuarioRepository;
	private boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	
	public UsuarioService() throws Exception {
		usuarioRepository = (IUsuarioRepository) BeansFactory.get(IUsuarioRepository.class);
	}

	@Override
	public void delete(int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new DeleteUserValidators(cdUsuario).validate(customConnection);
			delete(cdUsuario, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void delete(int cdUsuario, CustomConnection customConnection) throws Exception {
		usuarioRepository.delete(cdUsuario, customConnection);
	}

	@Override
	public PagedResponse<UsuarioLoginDTO> findLogins(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<UsuarioLoginDTO> usuarioSearch = lgBaseAntiga ? findLoginsBaseAntiga(searchCriterios, customConnection) : findLogins(searchCriterios, customConnection);
			List<UsuarioLoginDTO> usuarioList = new ArrayList<UsuarioLoginDTO>(usuarioSearch.getList(UsuarioLoginDTO.class));
			customConnection.finishConnection();
			return new PagedResponse<UsuarioLoginDTO>(usuarioList, usuarioSearch.getRsm().getTotal());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<UsuarioLoginDTO> findLogins(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<UsuarioLoginDTO> usuario = new SearchBuilder<UsuarioLoginDTO>("seg_usuario A")
				.fields("A.cd_usuario, A.nm_login, A.st_usuario, A.st_login, "+
						"B.nm_pessoa, A.cd_equipamento, C.nm_equipamento, C.st_equipamento")
				.addJoinTable("LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)")
				.addJoinTable("LEFT OUTER JOIN grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento)")
				.orderBy(" TRIM(B.nm_pessoa)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.count()
			.build();
		return usuario;
	}

	@Override
	public Search<UsuarioLoginDTO> findLoginsBaseAntiga(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<UsuarioLoginDTO> usuario = new SearchBuilder<UsuarioLoginDTO>("usuario A")
				.fields("A.cod_usuario AS cd_usuario, A.nm_nick AS nm_login, A.st_usuario, A.st_login, "+
						"B.nm_pessoa, A.cd_equipamento, C.nm_equipamento, C.st_equipamento")
				.addJoinTable("LEFT OUTER JOIN grl_pessoa B ON (A.cod_usuario = B.cd_pessoa)")
				.addJoinTable("LEFT OUTER JOIN grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento)")
				.orderBy(" TRIM(B.nm_pessoa)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.count()
			.build();
		return usuario;
	}
	
	@Override
	public void logoutUsuario(int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			logoutUsuario(cdUsuario, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void logoutUsuario(int cdUsuario, CustomConnection customConnection) throws Exception {
		Usuario usuarioLogado = getUsuarioLogado(cdUsuario);
		if(usuarioLogado.getCdEquipamento() > 0) {
			usuarioLogado.setCdEquipamento(0);
			usuarioLogado.setStLogin(UsuarioSituacaoEnum.ST_INATIVO.getKey());
			usuarioRepository.update(usuarioLogado, customConnection);
		}
	}
	
	private Usuario getUsuarioLogado(int cdUsuario) throws Exception {
		Usuario usuario = usuarioRepository.get(cdUsuario);
		if(usuario == null) {
			throw new Exception("Usúario não encontrado");
		}
		return usuario;		
	}
}
