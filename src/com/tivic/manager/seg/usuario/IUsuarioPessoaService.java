package com.tivic.manager.seg.usuario;

import java.util.List;

import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioPessoaDTO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IUsuarioPessoaService {
	public UsuarioPessoaDTO insert(UsuarioPessoaDTO usuarioPessoaDto) throws Exception;
	public UsuarioPessoaDTO insert(UsuarioPessoaDTO usuarioPessoaDto, CustomConnection customConnection) throws Exception;
	public UsuarioPessoaDTO update(UsuarioPessoaDTO usuarioPessoaDto) throws Exception;
	public UsuarioPessoaDTO update(UsuarioPessoaDTO usuarioPessoaDto, CustomConnection customConnection) throws Exception;
	public UsuarioPessoaDTO get(int cdUsuario) throws Exception;
	public UsuarioPessoaDTO get(int cdUsuario, CustomConnection customConnection) throws Exception;
	public List<UsuarioPessoaDTO> find(SearchCriterios searchCriterios) throws Exception;
	public List<UsuarioPessoaDTO> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<UsuarioPessoaDTO> findPessoas( String nmPessoa) throws Exception;
	public List<UsuarioPessoaDTO> findPessoas( String nmPessoa, CustomConnection customConnection) throws Exception;
	public List<Usuario> findUsuario(int cdUsuario) throws Exception;
	public List<Usuario> findUsuario(int cdUsuario, CustomConnection customConnection) throws Exception;
}
