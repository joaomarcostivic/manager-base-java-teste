package com.tivic.manager.seg.usuario;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.IPessoaRepository;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.pessoa.PessoaUsuarioBuilder;
import com.tivic.manager.grl.pessoaempresa.IPessoaEmpresaService;
import com.tivic.manager.seg.UsuarioPessoaDTO;
import com.tivic.sol.auth.usuario.IUsuarioService;
import com.tivic.sol.auth.usuario.Usuario;
import com.tivic.sol.auth.usuario.builder.UsuarioBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class UsuarioPessoaService implements IUsuarioPessoaService {

	private IPessoaRepository pessoaRepository;
	private IUsuarioService usuarioService;
	private IPessoaEmpresaService pessoaEmpresaService;
	
	public UsuarioPessoaService() throws Exception {
		this.pessoaRepository = (IPessoaRepository) BeansFactory.get(IPessoaRepository.class);
		this.usuarioService = (IUsuarioService) BeansFactory.get(IUsuarioService.class);
		this.pessoaEmpresaService = (IPessoaEmpresaService) BeansFactory.get(IPessoaEmpresaService.class);
	}
	
	public UsuarioPessoaDTO insert(UsuarioPessoaDTO usuarioPessoaDto) throws Exception {
		return insert(usuarioPessoaDto, new CustomConnection());
	}
	
	public UsuarioPessoaDTO insert(UsuarioPessoaDTO usuarioPessoaDto, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			Pessoa pessoa = new PessoaUsuarioBuilder(usuarioPessoaDto).build();
			this.pessoaRepository.insert(pessoa, customConnection);
			PessoaEmpresa pessoaEmpresa = new UsuarioPessoaEmpresaDirector().criar(pessoa.getCdPessoa(), usuarioPessoaDto.getVinculo().getCdVinculo());
			this.pessoaEmpresaService.insert(pessoaEmpresa, customConnection);
			Usuario usuario = this.usuarioService.insert(new UsuarioBuilder()
				.codigoPessoa(pessoa.getCdPessoa())
				.login(usuarioPessoaDto.getUsuario().getNmLogin())
				.senha(usuarioPessoaDto.getUsuario().getNmSenha())
				.tipo(usuarioPessoaDto.getUsuario().getTpUsuario())
				.situacao(usuarioPessoaDto.getUsuario().getStUsuario())
			.build(), customConnection);
			usuarioPessoaDto.getUsuario().setCdUsuario(usuario.getCdUsuario());
			usuarioPessoaDto.getPessoa().setCdPessoa(pessoa.getCdPessoa());
			customConnection.finishConnection();
			return usuarioPessoaDto;
		} finally {
			customConnection.closeConnection();
		}
	}

	public UsuarioPessoaDTO update(UsuarioPessoaDTO usuarioPessoaDto) throws Exception {
		return update(usuarioPessoaDto, new CustomConnection());
	}
	
	public UsuarioPessoaDTO update(UsuarioPessoaDTO usuarioPessoaDto, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			Pessoa pessoa = new PessoaUsuarioBuilder(usuarioPessoaDto)
				.codigo(usuarioPessoaDto.getPessoa().getCdPessoa())
			.build();
			if(pessoa.getCdPessoa() > 0)
				this.pessoaRepository.update(pessoa, customConnection);
			else {
				this.pessoaRepository.insert(pessoa, customConnection);
				usuarioPessoaDto.getPessoa().setCdPessoa(pessoa.getCdPessoa());
			}
			atualizarVinculo(usuarioPessoaDto, customConnection);
			this.usuarioService.update(new UsuarioBuilder()
				.codigo(usuarioPessoaDto.getUsuario().getCdUsuario())
				.codigoPessoa(usuarioPessoaDto.getPessoa().getCdPessoa())
				.login(usuarioPessoaDto.getUsuario().getNmLogin())
				.senha(usuarioPessoaDto.getUsuario().getNmSenha())
				.tipo(usuarioPessoaDto.getUsuario().getTpUsuario())
				.situacao(usuarioPessoaDto.getUsuario().getStUsuario())
			.build(), customConnection);
			customConnection.finishConnection();
			return usuarioPessoaDto;
		} finally {
			customConnection.closeConnection();
		}
	}

	private void atualizarVinculo(UsuarioPessoaDTO usuarioPessoaDto, CustomConnection customConnection) throws Exception {
		Empresa _empresa = EmpresaServices.getEmpresaById("JARI");
		
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_empresa", _empresa.getCdEmpresa());
		searchCriterios.addCriteriosEqualInteger("cd_pessoa", usuarioPessoaDto.getPessoa().getCdPessoa());
		List<PessoaEmpresa> listPessoaEmpresa = this.pessoaEmpresaService.find(searchCriterios, customConnection);
		for(PessoaEmpresa pessoaEmpresa : listPessoaEmpresa) {
			this.pessoaEmpresaService.delete(pessoaEmpresa.getCdEmpresa(), pessoaEmpresa.getCdPessoa(), pessoaEmpresa.getCdVinculo(), customConnection);
		} 
		PessoaEmpresa pessoaEmpresa = new UsuarioPessoaEmpresaDirector().criar(usuarioPessoaDto.getPessoa().getCdPessoa(), usuarioPessoaDto.getVinculo().getCdVinculo());
		this.pessoaEmpresaService.insert(pessoaEmpresa, customConnection);
	}

	@Override
	public UsuarioPessoaDTO get(int cdUsuario) throws Exception {
		return get(cdUsuario, new CustomConnection());
	}

	@Override
	public UsuarioPessoaDTO get(int cdUsuario, CustomConnection customConnection) throws Exception {
		Usuario usuario = this.usuarioService.get(cdUsuario, customConnection);
		return new UsuarioPessoaDTOBuilder()
			.usuario(usuario)
			.pessoa()
			.vinculo()
		.build();
	}

	@Override
	public List<UsuarioPessoaDTO> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<UsuarioPessoaDTO> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<UsuarioPessoaDTO> listUsuarioPessoa = new ArrayList<UsuarioPessoaDTO>();
		ItemComparator nmPessoaComparator = searchCriterios.getAndRemoveCriterio("nmPessoa");
		ItemComparator nmEmailComparator = searchCriterios.getAndRemoveCriterio("nmEmail");
		List<Usuario> usuarios = this.usuarioService.find(searchCriterios);
		for(Usuario usuario : usuarios) {
			listUsuarioPessoa.add(new UsuarioPessoaDTOBuilder()
				.usuario(usuario)
				.pessoa()
				.vinculo()
			.build());
		}
		filtrarPorNomePessoa(nmPessoaComparator, listUsuarioPessoa);
		filtrarPorNomeEmail(nmEmailComparator, listUsuarioPessoa);
		return listUsuarioPessoa;
	}

	private void filtrarPorNomePessoa(ItemComparator nmPessoaComparator, List<UsuarioPessoaDTO> listUsuarioPessoa) {
		if(nmPessoaComparator != null) {
			for(UsuarioPessoaDTO usuarioPessoaDto : listUsuarioPessoa) {
				if(usuarioPessoaDto.getPessoa() != null && nmPessoaComparator.getValue() != null && usuarioPessoaDto.getPessoa().getNmPessoa().startsWith(nmPessoaComparator.getValue())) {
					listUsuarioPessoa.remove(usuarioPessoaDto);
				}
			}
		}
	}

	private void filtrarPorNomeEmail(ItemComparator nmEmailComparator, List<UsuarioPessoaDTO> listUsuarioPessoa) {
		if(nmEmailComparator != null) {
			for(UsuarioPessoaDTO usuarioPessoaDto : listUsuarioPessoa) {
				if(usuarioPessoaDto.getPessoa() != null && nmEmailComparator.getValue() != null  && usuarioPessoaDto.getPessoa().getNmEmail().startsWith(nmEmailComparator.getValue())) {
					listUsuarioPessoa.remove(usuarioPessoaDto);
				}
			}
		}
	}
	
	@Override
	public List<UsuarioPessoaDTO> findPessoas(String nmPessoa) throws Exception {
		return findPessoas(nmPessoa, new CustomConnection());
}
	
	@Override
	public List<UsuarioPessoaDTO> findPessoas(String nmPessoa, CustomConnection customConnection)
			throws Exception {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			customConnection.initConnection(false);
			Search<UsuarioPessoaDTO> search = new SearchBuilder<UsuarioPessoaDTO>("seg_usuario A")
					.fields("A.cd_usuario, A.cd_pessoa, A.nm_login, B.nm_pessoa")
					.addJoinTable("LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)")
					.additionalCriterias("nm_login IS NOT NULL AND nm_pessoa IS NOT NULL")
					.additionalCriterias("(nm_pessoa ILIKE '%" + nmPessoa + "%' OR nm_login ILIKE '%" + nmPessoa + "%')")
					.searchCriterios(searchCriterios)
					.customConnection(customConnection)
					.count()
					.log()
					.build();
			
			List<UsuarioPessoaDTO> usuarioList = search.getList(UsuarioPessoaDTO.class);
			customConnection.finishConnection();
			return usuarioList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<com.tivic.manager.seg.Usuario> findUsuario(int usuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<com.tivic.manager.seg.Usuario> Usuarios = findUsuario(usuario, customConnection);
			customConnection.finishConnection();
			return Usuarios;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public  List<com.tivic.manager.seg.Usuario> findUsuario(int usuario, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios(); 
		searchCriterios.addCriteriosEqualInteger("cd_usuario", usuario);
		Search<com.tivic.manager.seg.Usuario> search = new SearchBuilder<com.tivic.manager.seg.Usuario>("seg_usuario")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(com.tivic.manager.seg.Usuario.class);
	}
}