package com.tivic.manager.triagem.services;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.IPessoaRepository;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.usuario.repositories.IUsuarioRepository;
import com.tivic.manager.triagem.builders.UsuarioTriagemBuilder;
import com.tivic.manager.triagem.dtos.UsuarioTriagemDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class UsuarioTriagemService implements IUsuarioTriagem {
	
	private IPessoaRepository pessoaRepository;
	private IUsuarioRepository usuarioRepository;
	private ConversorBaseAntigaNovaFactory conversaoEventoFactory;
	
	public UsuarioTriagemService() throws Exception {
		pessoaRepository = (IPessoaRepository) BeansFactory.get(IPessoaRepository.class);
		this.usuarioRepository = (IUsuarioRepository) BeansFactory.get(IUsuarioRepository.class);
        this.conversaoEventoFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
	}

	@Override
	public UsuarioTriagemDTO getByEmail(String nmEmail) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			UsuarioTriagemDTO usuarioTriagemDTO = getByEmail(nmEmail, customConnection);
			customConnection.finishConnection();
			return usuarioTriagemDTO;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public UsuarioTriagemDTO getByEmail(String nmEmail, CustomConnection customConnection) throws Exception {
		Pessoa usuarioPessoa = buscarUsuarioPessoa(nmEmail, customConnection);
		Usuario usuario = buscarUsuario(usuarioPessoa.getCdPessoa(), customConnection);
		UsuarioTriagemDTO UsuarioTriagemDTO =  new UsuarioTriagemBuilder()
				.pessoa(usuarioPessoa)
				.usuario(usuario)
				.build();
		return UsuarioTriagemDTO;
	}
	
	private Pessoa buscarUsuarioPessoa(String nmEmail, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_email", nmEmail);
		List<Pessoa> pessoaList = pessoaRepository.find(searchCriterios, customConnection);
		if (pessoaList.isEmpty()) {
			throw new BadRequestException("Nenhum cadastro encontrado com o email: " + nmEmail);
		}
		return pessoaList.get(0);
	}
	
	private Usuario buscarUsuario(Integer cdPessoa, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_pessoa", cdPessoa);
		
		this.usuarioRepository = conversaoEventoFactory.getUsuarioRepository();
		List<Usuario> usuarios = this.usuarioRepository.find(searchCriterios);
		if (usuarios.isEmpty()) {
			throw new NoContentException("Usuário não encontrado.");
		}
		return usuarios.get(0);
	}
		
}
