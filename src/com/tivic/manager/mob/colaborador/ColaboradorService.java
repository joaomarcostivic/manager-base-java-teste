package com.tivic.manager.mob.colaborador;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.IPessoaArquivoRepository;
import com.tivic.manager.grl.IPessoaEmpresaRepository;
import com.tivic.manager.grl.IPessoaFisicaRepository;
import com.tivic.manager.grl.IPessoaRepository;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaArquivo;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.Vinculo;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.grl.pessoavinculohistorico.PessoaVinculoHistorico;
import com.tivic.manager.grl.pessoavinculohistorico.builders.PessoaVinculoHistoricoBuilder;
import com.tivic.manager.grl.pessoavinculohistorico.enums.StPessoaVinculoEnum;
import com.tivic.manager.grl.pessoavinculohistorico.repository.PessoaVinculoHistoricoRepository;
import com.tivic.manager.grl.vinculo.VinculoRepository;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.AgenteServices;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.agente.AgenteRepository;
import com.tivic.manager.mob.colaborador.validators.ColaboradorDesativacaoValidator;
import com.tivic.manager.mob.colaborador.validators.ColaboradorValidator;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.exceptions.UsuarioException;
import com.tivic.manager.mob.orgao.OrgaoRepository;
import com.tivic.manager.relatorios.estatisticasaits.enums.SituacaoAgenteEnum;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.seg.usuario.repositories.IUsuarioRepository;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.auth.usuario.SituacaoUsuarioEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ResultSetMap;

public class ColaboradorService implements IColaboradorService {
	private IPessoaRepository repositoryPessoa;
	private IPessoaFisicaRepository pessoaFisicaRepository;
	private IPessoaEmpresaRepository pessoaEmpresaRepository;
	private PessoaVinculoHistoricoRepository pessoaVinculoHistoricoRepository;
	private AgenteRepository agenteRepository;
	private OrgaoRepository orgaoRepository;
	private VinculoRepository vinculoRepository;
	private IUsuarioRepository usuarioRepository;
	private boolean vinculosIsAutoridadeTransito;
	private IArquivoRepository arquivoRepository;
	private IPessoaArquivoRepository pessoaArquivoRepository;

	
	public ColaboradorService() throws Exception {
		pessoaFisicaRepository = (IPessoaFisicaRepository) BeansFactory.get(IPessoaFisicaRepository.class);
		pessoaEmpresaRepository = (IPessoaEmpresaRepository) BeansFactory.get(IPessoaEmpresaRepository.class);
		repositoryPessoa = (IPessoaRepository) BeansFactory.get(IPessoaRepository.class);
		pessoaVinculoHistoricoRepository = (PessoaVinculoHistoricoRepository) BeansFactory.get(PessoaVinculoHistoricoRepository.class);
		orgaoRepository = (OrgaoRepository) BeansFactory.get(OrgaoRepository.class);
		agenteRepository = (AgenteRepository) BeansFactory.get(AgenteRepository.class);
		usuarioRepository = (IUsuarioRepository) BeansFactory.get(IUsuarioRepository.class);
		vinculoRepository = (VinculoRepository) BeansFactory.get(VinculoRepository.class);
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		pessoaArquivoRepository = (IPessoaArquivoRepository) BeansFactory.get(IPessoaArquivoRepository.class);

	}
	
	@Override
	public ColaboradorDTO create(ColaboradorDTO colaboradorDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new ColaboradorValidator(colaboradorDTO).validate(customConnection);
			colaboradorDTO.setPessoaFisica(new PessoaToPessoaFisicaBuilder(colaboradorDTO).build());
			pessoaFisicaRepository.insert(colaboradorDTO.getPessoaFisica(), customConnection);
			PessoaEmpresa pessoaEmpresa = new ColaboradorEmpresaBuilder(colaboradorDTO.getPessoaFisica().getCdPessoa(), colaboradorDTO.getCdVinculo()).build();
			pessoaEmpresaRepository.insert(pessoaEmpresa, customConnection);
			PessoaVinculoHistorico pessoaVinculoHistorico = buildPessoaHistoricoVinculo(colaboradorDTO);
			pessoaVinculoHistoricoRepository.insert(pessoaVinculoHistorico, customConnection);

			salvarArquivoColaborador(colaboradorDTO, customConnection);

			if(colaboradorDTO.getUsuario().getNmLogin() != null) {
				colaboradorDTO.getUsuario().setCdPessoa(colaboradorDTO.getPessoaFisica().getCdPessoa());
				colaboradorDTO.getUsuario().setStUsuario(SituacaoUsuarioEnum.ATIVO.getKey());

				Usuario usuario = UsuarioServices.getByNmLogin(colaboradorDTO.getUsuario().getNmLogin(), null);
				if(usuario != null) {
					throw new ValidacaoException("Já existe um usuário com o login escolhido.");
				}
				usuarioRepository.insert(colaboradorDTO.getUsuario(), customConnection);
			}
			
			if(colaboradorDTO.getAgente().getNrMatricula() != null) {
				colaboradorDTO.getAgente().setCdAgente(0);
				ResultSetMap rsm = AgenteServices.getByMatricula(colaboradorDTO.getAgente().getNrMatricula());
				if(rsm.next()) {
					throw new ValidacaoException("Já existe agente cadastrado com essa matrícula");
				}
				
				colaboradorDTO.getAgente().setCdUsuario(colaboradorDTO.getUsuario().getCdUsuario());
				colaboradorDTO.getAgente().setNmAgente(colaboradorDTO.getPessoa().getNmPessoa());
				colaboradorDTO.getAgente().setStAgente(SituacaoAgenteEnum.ST_ATIVO.getKey());
				agenteRepository.insert(colaboradorDTO.getAgente(), customConnection);
			}
			
			atualizaResponsavelOrgaoPorVinculo(colaboradorDTO.getCdVinculo(), colaboradorDTO.getAgente(), customConnection);
			customConnection.finishConnection();
			return colaboradorDTO;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void atualizaResponsavelOrgaoPorVinculo(int cdVinculo, Agente agente, CustomConnection customConnection) throws Exception {
		Vinculo vinculo = vinculoRepository.get(cdVinculo);
		if(vinculo == null) {
			throw new ValidacaoException("Não foi possível encontrar o vínculo.");
		}
		
		vinculosIsAutoridadeTransito = Normalizer.normalize(vinculo.getNmVinculo(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase().contains("TRANSITO");
		if(vinculosIsAutoridadeTransito) {
			Orgao orgao = new Orgao();
			List<Orgao> orgaos = this.orgaoRepository.find(new SearchCriterios());
			if(orgaos.isEmpty())
				throw new Exception("Nenhum orgão encontrado");
			for(Orgao orgaoUnico: orgaos) {
				if(orgaoUnico.getLgEmitirAit() == 0)
					continue;
				orgao = orgaoUnico;
				break;
			}
			if(agente.getStAgente() == SituacaoAgenteEnum.ST_INATIVO.getKey()) {
				orgao.setCdAgenteResponsavel(0);
				orgaoRepository.update(orgao, customConnection);
				return;
			}
			orgao.setCdAgenteResponsavel(agente.getCdAgente());
			orgaoRepository.update(orgao, customConnection);
		}
	}

	private PessoaVinculoHistorico buildPessoaHistoricoVinculo(ColaboradorDTO colaboradorDTO) throws UsuarioException, Exception {
		return new PessoaVinculoHistoricoBuilder()
				.addCdPessoa(colaboradorDTO.getPessoaFisica().getCdPessoa())
				.addCdVinculo(colaboradorDTO.getCdVinculo())
				.addCdUsuario(colaboradorDTO.getCdUsuario())
				.addStVinculo(StPessoaVinculoEnum.ST_ATIVO.getKey())
				.addDtVinculoHistorico(Util.getDataAtual())
				.build();
	}
	
	@Override
	public List<ColaboradorDTO> findAll(SearchCriterios searchCriterios) throws Exception {
		List<Pessoa> listPessoaColaborador = searchPessoaColaborador(searchCriterios).getList(Pessoa.class);
		if (listPessoaColaborador.isEmpty()) {
			throw new NoContentException("Nenhum registro de colaborador encontrado.");
		}
		return new ColaboradorDTOPageBuilder(listPessoaColaborador).build();
	}
	
	@Override
	public Search<Pessoa> searchPessoaColaborador(SearchCriterios searchCriterios) throws Exception {
		Search<Pessoa> search = new SearchBuilder<Pessoa>("GRL_PESSOA A")
				.fields("A.*, A.cd_pessoa, C.cd_vinculo, D.nm_vinculo, C.st_vinculo")
				.addJoinTable(" LEFT OUTER JOIN GRL_PESSOA_FISICA A1 ON (A.CD_PESSOA = A1.CD_PESSOA) ")
				.addJoinTable(" JOIN GRL_PESSOA_EMPRESA C ON (A.CD_PESSOA = C.CD_PESSOA)  ")
				.addJoinTable("JOIN GRL_VINCULO D ON (C.CD_VINCULO = D.CD_VINCULO) ")
				.searchCriterios(searchCriterios)
				.orderBy(" A.CD_PESSOA DESC ")
				.count()
				.build();
		return search;
	}
	
	@Override
	public String buscaNomeAutoridadeTransito() throws Exception {
		Orgao orgao = buscarOrgaoUnico();
		int codigoAgenteResponsavel = orgao.getCdAgenteResponsavel();
		Agente agente = agenteRepository.get(codigoAgenteResponsavel);
		return agente.getNmAgente();	
	}
	
	private Orgao buscarOrgaoUnico() throws Exception {
		Orgao orgao = new Orgao();
		List<Orgao> orgaos = this.orgaoRepository.find(new SearchCriterios());
		if(orgaos.isEmpty())
			throw new Exception("Nenhum orgão encontrado");
		for(Orgao orgaoUnico: orgaos) {
			if(orgaoUnico.getLgEmitirAit() == 0)
				continue;
			orgao = orgaoUnico;
			break;
		}
		return orgao;
	}

	@Override
	public ColaboradorDTO findById(int cdPessoa) throws Exception {
		List<Pessoa> listPessoaColaborador = new ArrayList<Pessoa>();
		listPessoaColaborador.add(repositoryPessoa.get(cdPessoa));
		return new ColaboradorDTOPageBuilder(listPessoaColaborador).build().get(0);
	}
	
	@Override
	public ColaboradorDTO update(ColaboradorDTO colaboradorDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			colaboradorDTO.setPessoaFisica(new PessoaToPessoaFisicaBuilder(colaboradorDTO).build());
			pessoaFisicaRepository.update(colaboradorDTO.getPessoaFisica(), customConnection);
			
			if(colaboradorDTO.getArquivo() != null) {
				arquivoRepository.update(colaboradorDTO.getArquivo(), customConnection); 
			}
			if(colaboradorDTO.getUsuario().getNmLogin() != null) {
				colaboradorDTO.getUsuario().setCdPessoa(colaboradorDTO.getPessoaFisica().getCdPessoa());
				
				Usuario usuario = UsuarioServices.getByNmLogin(colaboradorDTO.getUsuario().getNmLogin(), null);
				if(usuario != null && usuario.getCdUsuario() != colaboradorDTO.getUsuario().getCdUsuario()) {
					throw new ValidacaoException("Já existe um usuário com o login escolhido.");
				}
				usuarioRepository.update(colaboradorDTO.getUsuario(), customConnection);
			}
			
			if(colaboradorDTO.getAgente().getNrMatricula() != null) {
				colaboradorDTO.getAgente().setCdUsuario(colaboradorDTO.getUsuario().getCdUsuario());
				colaboradorDTO.getAgente().setNmAgente(colaboradorDTO.getPessoa().getNmPessoa());
				agenteRepository.update(colaboradorDTO.getAgente(), customConnection);
			}
			
			customConnection.finishConnection();
			return colaboradorDTO;
		} finally {
			customConnection.closeConnection();
		}
	}
		
	@Override
	public ColaboradorDTO get(int cdPessoa) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			ColaboradorDTO colaborador = get(cdPessoa, customConnection);
			customConnection.finishConnection();
			if(colaborador == null) {
				throw new ValidationException("Nenhum colaborador encontrado.");
			}
			return colaborador;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public ColaboradorDTO get(int cdPessoa, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_pessoa", cdPessoa);
		Search<ColaboradorDTO> colaboradores = new SearchBuilder<ColaboradorDTO>("grl_pessoa A")
		           .fields("A.cd_pessoa, B.st_vinculo, B.cd_empresa, B.dt_vinculo, C.nm_vinculo, C.cd_vinculo, D.cd_usuario, F.cd_agente, F.nm_Agente, H.cd_arquivo")
		           .addJoinTable("JOIN grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa)")
		           .addJoinTable("JOIN grl_vinculo C ON (C.cd_vinculo = B.cd_vinculo)")
		           .addJoinTable("LEFT JOIN seg_usuario D ON (A.cd_pessoa = D.cd_pessoa)")
		           .addJoinTable("LEFT JOIN mob_agente F ON (D.cd_usuario = F.cd_usuario)")
		           .addJoinTable("LEFT JOIN grl_pessoa_arquivo G ON (A.cd_pessoa = G.cd_pessoa)")
		           .addJoinTable("LEFT JOIN grl_arquivo H ON (G.cd_arquivo = H.cd_arquivo)")
		           .searchCriterios(searchCriterios)
		           .customConnection(customConnection)
		           .build();
		ColaboradorDTO colaborador = new ColaboradorDTOBuilder(colaboradores.getList(ColaboradorDTO.class).get(0)).populate().build();
		return colaborador;
	}

	@Override
	public ColaboradorDTO inativar(ColaboradorDTO colaboradorDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new ColaboradorDesativacaoValidator(colaboradorDTO).validate(customConnection);
			ColaboradorDTO colaboradorInativo = inativar(colaboradorDTO, customConnection);
			customConnection.finishConnection();
			return colaboradorInativo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public ColaboradorDTO inativar(ColaboradorDTO colaboradorDTO, CustomConnection customConnection) throws Exception {
		PessoaEmpresa pessoaEmpresa = pessoaEmpresaRepository.get(colaboradorDTO.getCdEmpresa(), colaboradorDTO.getCdPessoa(), colaboradorDTO.getCdVinculo());
		pessoaEmpresa.setStVinculo(StPessoaVinculoEnum.ST_INATIVO.getKey());
		pessoaEmpresaRepository.update(pessoaEmpresa, customConnection);
		PessoaVinculoHistorico pessoaVinculoHistorico = buildPessoaHistoricoVinculo(colaboradorDTO);
		pessoaVinculoHistorico.setStVinculo(StPessoaVinculoEnum.ST_INATIVO.getKey());
		pessoaVinculoHistorico.setDtVinculoHistorico(colaboradorDTO.getDtVinculoHistorico());
		pessoaVinculoHistoricoRepository.insert(pessoaVinculoHistorico, customConnection);
		if(colaboradorDTO.getAgente().getNrMatricula() != null ) {
			Agente agente = agenteRepository.get(colaboradorDTO.getAgente().getCdAgente());
			agente.setStAgente(SituacaoAgenteEnum.ST_INATIVO.getKey());
			agenteRepository.update(agente, customConnection);
			atualizaResponsavelOrgaoPorVinculo(colaboradorDTO.getCdVinculo(), agente, customConnection);
		}
		if(colaboradorDTO.getUsuario().getNmLogin() != null ) {
			Usuario usuario = usuarioRepository.get(colaboradorDTO.getUsuario().getCdUsuario());
			usuario.setStUsuario(SituacaoUsuarioEnum.INATIVO.getKey());
			usuarioRepository.update(usuario, customConnection);
		}
		atualizaResponsavelOrgaoPorVinculo(colaboradorDTO.getCdVinculo(), colaboradorDTO.getAgente(), customConnection);
		return colaboradorDTO;
	}

	@Override
	public ColaboradorDTO ativar(ColaboradorDTO colaboradorDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new ColaboradorValidator(colaboradorDTO).validate(customConnection);
			ColaboradorDTO colaboradorAtivo = ativar(colaboradorDTO, customConnection);
			customConnection.finishConnection();
			return colaboradorAtivo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public ColaboradorDTO ativar(ColaboradorDTO colaboradorDTO, CustomConnection customConnection) throws Exception {
		PessoaEmpresa pessoaEmpresa = pessoaEmpresaRepository.get(colaboradorDTO.getCdEmpresa(), colaboradorDTO.getCdPessoa(), colaboradorDTO.getCdVinculo());
		pessoaEmpresa.setStVinculo(StPessoaVinculoEnum.ST_ATIVO.getKey());
		pessoaEmpresaRepository.update(pessoaEmpresa, customConnection);
		PessoaVinculoHistorico pessoaVinculoHistorico = buildPessoaHistoricoVinculo(colaboradorDTO);
		pessoaVinculoHistoricoRepository.insert(pessoaVinculoHistorico, customConnection);
		if(colaboradorDTO.getAgente().getNrMatricula() != null ) {
			Agente agente = agenteRepository.get(colaboradorDTO.getAgente().getCdAgente());
			agente.setStAgente(SituacaoAgenteEnum.ST_ATIVO.getKey());
			agenteRepository.update(agente, customConnection);
			atualizaResponsavelOrgaoPorVinculo(colaboradorDTO.getCdVinculo(), agente, customConnection);
		}
		if(colaboradorDTO.getUsuario().getNmLogin() != null ) {
			Usuario usuario = usuarioRepository.get(colaboradorDTO.getUsuario().getCdUsuario());
			usuario.setStUsuario(SituacaoUsuarioEnum.ATIVO.getKey());
			usuarioRepository.update(usuario, customConnection);
		}
		return colaboradorDTO;
	}

	@Override
	public PagedResponse<ColaboradorTableDTO> findColaboradores(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<ColaboradorTableDTO> colaboradoresSearch = findColaboradores(searchCriterios, customConnection);
			List<ColaboradorTableDTO> colaboradorList = new ArrayList<ColaboradorTableDTO>(colaboradoresSearch.getList(ColaboradorTableDTO.class));
			customConnection.finishConnection();
			return new PagedResponse<ColaboradorTableDTO>(colaboradorList, colaboradoresSearch.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<ColaboradorTableDTO> findColaboradores(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<ColaboradorTableDTO> colaboradores = new SearchBuilder<ColaboradorTableDTO>("grl_pessoa A")
				.fields("A.cd_pessoa, A.nm_pessoa, A.nr_celular, B.dt_nascimento, "
						+ "B.nr_cpf, C.st_vinculo, C.cd_empresa, C.dt_vinculo, D.nm_vinculo, D.cd_vinculo")
				.addJoinTable("LEFT OUTER JOIN grl_pessoa_fisica B ON (A.cd_pessoa = B.cd_pessoa)")
				.addJoinTable("LEFT OUTER JOIN grl_pessoa_empresa C ON (A.cd_pessoa = C.cd_pessoa)")
				.addJoinTable("LEFT OUTER JOIN grl_vinculo D ON (C.cd_vinculo = D.cd_vinculo)")
				.orderBy(" A.cd_pessoa DESC ")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.count()
			.build();
		return colaboradores;	
	}
	
	private void salvarArquivoColaborador(ColaboradorDTO colaboradorDTO, CustomConnection customConnection) throws Exception {
	    if (colaboradorDTO.getArquivo() != null && colaboradorDTO.getArquivo().getBlbArquivo() != null) {

	        Arquivo arquivo = new ArquivoBuilder()
	                .setBlbArquivo(colaboradorDTO.getArquivo().getBlbArquivo())
	                .setDtArquivamento(new GregorianCalendar())
	                .setDtCriacao(new GregorianCalendar())
	                .setNmDocumento("Arquivo de Assinatura do Colaborador")
	                .setNmArquivo("ARQUIVO_ASSINATURA_PESSOA_" + colaboradorDTO.getPessoaFisica().getCdPessoa())
	                .build();
	        
			Arquivo arquivoSalvo = this.arquivoRepository.insert(arquivo, customConnection);
			
			PessoaArquivo pessoaArquivo = new PessoaArquivo();
	        pessoaArquivo.setCdPessoa(colaboradorDTO.getPessoaFisica().getCdPessoa());
	        pessoaArquivo.setCdArquivo(arquivoSalvo.getCdArquivo());
			
			this.pessoaArquivoRepository.insert(pessoaArquivo, customConnection);
	        
	    }
	}
	
	@Override
	public Map<String, Object> buscarAssinaturaAutoridade() throws Exception {
	    Orgao orgao = buscarOrgaoUnico();
	    Agente agente = agenteRepository.get(orgao.getCdAgenteResponsavel());
	    Usuario usuario = usuarioRepository.get(agente.getCdUsuario());

	    SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualInteger("cd_pessoa", usuario.getCdPessoa());

	    List<PessoaArquivo> pessoaArquivo = pessoaArquivoRepository.find(searchCriterios);
	    if (pessoaArquivo != null && !pessoaArquivo.isEmpty()) {
	    	Arquivo arquivo = arquivoRepository.get(pessoaArquivo.get(0).getCdArquivo());
		    byte[] assinatura = arquivo.getBlbArquivo() != null ? arquivo.getBlbArquivo() : null;
		    Map<String, Object> autoridade = new HashMap<>();
		    autoridade.put("nomeAutoridade", agente.getNmAgente());
		    autoridade.put("assinaturaAutoridade", assinatura);
		    return autoridade;
		}
	    return new HashMap<>();
	}
}