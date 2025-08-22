package com.tivic.manager.seg.usuario;

import java.util.List;

import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.IPessoaRepository;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.Vinculo;
import com.tivic.manager.grl.VinculoDAO;
import com.tivic.manager.grl.pessoaempresa.IPessoaEmpresaService;
import com.tivic.manager.seg.UsuarioPessoaDTO;
import com.tivic.sol.auth.usuario.Usuario;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

public class UsuarioPessoaDTOBuilder {
	
	private IPessoaRepository pessoaRepository;
	private IPessoaEmpresaService pessoaEmpresaService;
	
	private UsuarioPessoaDTO usuarioPessoaDto;
	
	public UsuarioPessoaDTOBuilder() throws Exception {
		this.usuarioPessoaDto = new UsuarioPessoaDTO();
		this.pessoaRepository = (IPessoaRepository) BeansFactory.get(IPessoaRepository.class);
		this.pessoaEmpresaService = (IPessoaEmpresaService) BeansFactory.get(IPessoaEmpresaService.class);
	}
	
	public UsuarioPessoaDTOBuilder usuario(Usuario usuario) {
		com.tivic.manager.seg.Usuario usuarioSistema = new com.tivic.manager.seg.Usuario();
		usuarioSistema.setCdUsuario(usuario.getCdUsuario());
		usuarioSistema.setCdPessoa(usuario.getCdPessoa());
		usuarioSistema.setNmLogin(usuario.getNmLogin());
		usuarioSistema.setNmSenha(usuario.getNmSenha());
		usuarioSistema.setTpUsuario(usuario.getTpUsuario());
		usuarioSistema.setStUsuario(usuario.getStUsuario());
		this.usuarioPessoaDto.setUsuario(usuarioSistema);
		return this;
	}
	
	public UsuarioPessoaDTOBuilder pessoa() throws Exception {
		Pessoa pessoa = this.pessoaRepository.get(this.usuarioPessoaDto.getUsuario().getCdPessoa());
		this.usuarioPessoaDto.setPessoa(pessoa);
		return this;
	}

	public UsuarioPessoaDTOBuilder vinculo() throws Exception {
		if(usuarioPessoaDto.getPessoa() == null)
			return this;
		Empresa _empresa = EmpresaServices.getEmpresaById("JARI");
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_empresa", _empresa.getCdEmpresa());
		searchCriterios.addCriteriosEqualInteger("cd_pessoa", usuarioPessoaDto.getPessoa().getCdPessoa());
		List<PessoaEmpresa> listPessoaEmpresa = this.pessoaEmpresaService.find(searchCriterios);
		if(!listPessoaEmpresa.isEmpty()) {
			Vinculo vinculo = VinculoDAO.get(listPessoaEmpresa.get(0).getCdVinculo());
			this.usuarioPessoaDto.setVinculo(vinculo);
		}
		return this;
	}
	
	public UsuarioPessoaDTO build() {
		return this.usuarioPessoaDto;
	}
}
