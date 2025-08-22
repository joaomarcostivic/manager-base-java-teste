package com.tivic.manager.mob.ait.sync.builders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.categoriaveiculo.CategoriaVeiculoRepository;
import com.tivic.manager.fta.tipoveiculo.TipoVeiculoRepository;
import com.tivic.manager.grl.Bairro;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.Pais;
import com.tivic.manager.grl.bairro.BairroRepository;
import com.tivic.manager.grl.equipamento.IEquipamentoService;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.grl.pais.PaisRepository;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoObservacao;
import com.tivic.manager.mob.Ocorrencia;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.ait.sync.entities.AgenteSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.BairroSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.CategoriaVeiculoSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.CidadeSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.EstadoSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.InfracaoObservacaoSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.InfracaoSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.MarcaModeloSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.OcorrenciaSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.PaisSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.ParametroSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.SyncResponse;
import com.tivic.manager.mob.ait.sync.entities.TalonarioSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.TipoVeiculoSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.UserManagerDTO;
import com.tivic.manager.mob.ait.sync.entities.UsuarioSuporteSyncDTO;
import com.tivic.manager.mob.ait.sync.entities.UsuarioSyncDTO;
import com.tivic.manager.mob.convenio.Convenio;
import com.tivic.manager.mob.convenio.IConvenioService;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.exceptions.UsuarioException;
import com.tivic.manager.mob.orgao.CidadeOrgaoResolver;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.mob.talonario.SituacaoTalaoEnum;
import com.tivic.manager.mob.talonario.factory.ultimodocumento.UltimoNrDocumentoFactory;
import com.tivic.manager.seg.Usuario;
import com.tivic.sol.auth.UserManager;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AitSyncDTOBuilder {
	private final List<SyncResponse<?>> aitSyncResponse;
	private final EstadoRepository estadoRepository;
	private final PaisRepository paisRepository;
	private final IEquipamentoService equipamentoService;
	private final IConvenioService convenioService;
	private int cdUsuario;
	private final SearchCriterios searchCriterios;
	private final ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
	private final TipoVeiculoRepository tipoVeiculoRepository;
	private final CategoriaVeiculoRepository categoriaVeiculoRepository;
	private final BairroRepository bairroRepository;
	private final CidadeOrgaoResolver cidadeOrgaoResolver;

	public AitSyncDTOBuilder() throws Exception {
		aitSyncResponse = new ArrayList<>();
		estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
		paisRepository = (PaisRepository) BeansFactory.get(PaisRepository.class);
		equipamentoService = (IEquipamentoService) BeansFactory.get(IEquipamentoService.class);
		convenioService = (IConvenioService) BeansFactory.get(IConvenioService.class);
		searchCriterios = new SearchCriterios();
		conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
		tipoVeiculoRepository = (TipoVeiculoRepository) BeansFactory.get(TipoVeiculoRepository.class);
		categoriaVeiculoRepository = (CategoriaVeiculoRepository) BeansFactory.get(CategoriaVeiculoRepository.class);
		bairroRepository = (BairroRepository) BeansFactory.get(BairroRepository.class);
		cidadeOrgaoResolver = (CidadeOrgaoResolver) BeansFactory.get(CidadeOrgaoResolver.class);
	}

	public AitSyncDTOBuilder addAllAitSync(int cdAgente, CustomConnection customConnection) throws Exception {
	    addPaises(paisRepository.getAll(customConnection));
	    addEstados(estadoRepository.getAll(customConnection));
	    addCidades(conversorBaseAntigaNovaFactory.getCidadeRepository().find(searchCriterios, customConnection));
	    addInfracoes(conversorBaseAntigaNovaFactory.getInfracaoRepository().getInfracoesVigentes(customConnection), customConnection);
	    addAgente(conversorBaseAntigaNovaFactory.getAgenteRepository().get(cdAgente));
	    addUsuario(conversorBaseAntigaNovaFactory.getUsuarioRepository().get(cdUsuario));
	    addTalonarios(conversorBaseAntigaNovaFactory.getTalonarioRepository().getEletronicoByAgente(cdAgente, customConnection), customConnection);
	    addMarcasModelos(conversorBaseAntigaNovaFactory.getMarcaModeloRepository().findForApp(searchCriterios, customConnection));
	    addOcorrencias(conversorBaseAntigaNovaFactory.getOcorrenciaRepository().findOcorrenciasApp(searchCriterios, customConnection));
	    addEspeciesVeiculo(conversorBaseAntigaNovaFactory.getEspecieVeiculoRepository().find(searchCriterios, customConnection));
	    addConvenio(convenioService.find(searchCriterios, customConnection));
	    addParametro(conversorBaseAntigaNovaFactory.getParametroSyncDTOBuilder().build());
	    //addTiposVeiculo(tipoVeiculoRepository.find(searchCriterios, customConnection));
	    //addCategoriasVeiculo(categoriaVeiculoRepository.find(searchCriterios, customConnection));
	    //addBairros(bairroRepository.findForApp(cidadeOrgaoResolver.getCdCidadeOrgao(customConnection), customConnection));
	    addUsuarioSuporte(customConnection);
	    return this;
	}

	public void addCidades(List<Cidade> cidades) {
		List<CidadeSyncDTO> cidadesDTO = new ArrayList<>();
		SyncResponse<CidadeSyncDTO> cidadeDTO = new SyncResponse<>();
		for(Cidade cidade: cidades) {
			cidadesDTO.add(new CidadeSyncDTOBuilder(cidade).build());
		}
		cidadeDTO.setEntity("Cidade");
		cidadeDTO.setItems(cidadesDTO);
		this.aitSyncResponse.add(cidadeDTO);
	}

	public void addEspeciesVeiculo(List<EspecieVeiculo> especiesVeiculo) {
		SyncResponse<EspecieVeiculo> especieVeiculo = new SyncResponse<>();
		especieVeiculo.setEntity("EspecieVeiculo");
		especieVeiculo.setItems(especiesVeiculo);
		this.aitSyncResponse.add(especieVeiculo);
	}

	public void addEstados(List<Estado> estados) {
		List<EstadoSyncDTO> estadosDTO = new ArrayList<>();
		SyncResponse<EstadoSyncDTO> estadoDTO = new SyncResponse<>();
		for(Estado estado: estados ) {
			estadosDTO.add(new EstadoSyncDTOBuilder(estado).build());
		}
		estadoDTO.setEntity("Estado");
		estadoDTO.setItems(estadosDTO);
		this.aitSyncResponse.add(estadoDTO);
	}

	public void addInfracoes(List<Infracao> infracoes, CustomConnection connection) throws Exception {
		List<InfracaoSyncDTO> infracoesDTO = new ArrayList<>();
		SyncResponse<InfracaoSyncDTO> infracaoDTO = new SyncResponse<>();
		for(Infracao infracao: infracoes) {
			infracoesDTO.add(new InfracaoSyncDTOBuilder(infracao).build());
			}
		infracaoDTO.setEntity("Infracao");
		infracaoDTO.setItems(infracoesDTO);
		this.aitSyncResponse.add(infracaoDTO);
		addInfracoesObservacoes(infracoes, connection);
	}

	public void addInfracoesObservacoes(List<Infracao> infracoes, CustomConnection connection) throws Exception {
		List<InfracaoObservacao> infracoesObservacao = new ArrayList<>();
		setinfracoesObservacao(infracoes, infracoesObservacao, connection);
		List<InfracaoObservacaoSyncDTO> infracoesObservacaoDTO = new ArrayList<>();
		SyncResponse<InfracaoObservacaoSyncDTO> infracaoObservacaoDTO = new SyncResponse<>();
		for(InfracaoObservacao infracaoObservacao: infracoesObservacao) {
			infracoesObservacaoDTO.add(new InfracaoObservacaoSyncDTOBuilder(infracaoObservacao).build());
		}

		infracaoObservacaoDTO.setEntity("InfracaoObservacao");
		infracaoObservacaoDTO.setItems(infracoesObservacaoDTO);
		this.aitSyncResponse.add(infracaoObservacaoDTO);
	}

	private void setinfracoesObservacao(List<Infracao> infracoes, List<InfracaoObservacao> infracoesObservacao, CustomConnection connection) throws Exception {
		for(Infracao infracao: infracoes) {
			List<InfracaoObservacao> infracaoObservacaoList = conversorBaseAntigaNovaFactory.getInfracaoObservacaoRepository().getObservacaoByCdInfracao(infracao.getCdInfracao(), connection);
            infracoesObservacao.addAll(infracaoObservacaoList);
		}
	}

	public void addMarcasModelos(List<MarcaModelo> marcasModelo) {
		List<MarcaModeloSyncDTO> marcasModeloDTO = new ArrayList<>();
		SyncResponse<MarcaModeloSyncDTO> marcaModeloDTO = new SyncResponse<>();
		for(MarcaModelo marcaModelo: marcasModelo) {
			marcasModeloDTO.add(new MarcaModeloSyncDTOBuilder(marcaModelo).build());
		}
		marcaModeloDTO.setEntity("MarcaModelo");
		marcaModeloDTO.setItems(marcasModeloDTO);
		this.aitSyncResponse.add(marcaModeloDTO);
	}

	public void addOcorrencias(List<Ocorrencia> ocorrencias) {
		List<OcorrenciaSyncDTO> ocorrenciasDTO = new ArrayList<>();
		SyncResponse<OcorrenciaSyncDTO> ocorrenciaDTO = new SyncResponse<>();
		for(Ocorrencia ocorrencia: ocorrencias) {
			ocorrenciasDTO.add(new OcorrenciaSyncDTOBuilder(ocorrencia).build());
		}
		ocorrenciaDTO.setEntity("Ocorrencia");
		ocorrenciaDTO.setItems(ocorrenciasDTO);
		this.aitSyncResponse.add(ocorrenciaDTO);
	}

	public void addPaises(List<Pais> paises) {
		List<PaisSyncDTO> paisesDTO = new ArrayList<>();
		SyncResponse<PaisSyncDTO> paisDTO = new SyncResponse<>();
		for(Pais pais: paises) {
			paisesDTO.add(new PaisSyncDTOBuilder(pais).build());
		}
		paisDTO.setEntity("Pais");
		paisDTO.setItems(paisesDTO);
		this.aitSyncResponse.add(paisDTO);
	}

	public void addTalonarios(List<Talonario> talonarios, CustomConnection customConnection) throws Exception {
		List<TalonarioSyncDTO> talonariosDTO = new ArrayList<>();
		SyncResponse<TalonarioSyncDTO> talonarioDTO = new SyncResponse<>();
		for(Talonario talonario: talonarios) {
			if(talonario.getStTalao() == SituacaoTalaoEnum.ST_TALAO_ATIVO.getKey()) {
				talonario.setNrUltimoAit(new UltimoNrDocumentoFactory()
						.getStrategy(talonario)
						.getUltimoNrDocumento(customConnection));
			}
			talonariosDTO.add(new TalonarioSyncDTOBuilder(talonario).build());
		}
		talonarioDTO.setEntity("Talonario");
		talonarioDTO.setItems(talonariosDTO);
		this.aitSyncResponse.add(talonarioDTO);
	}

	public void addParametro(List<ParametroSyncDTO> parametros) {
		SyncResponse<ParametroSyncDTO> parametrosSync = new SyncResponse<>();
		parametrosSync.setEntity("Parametro");
		parametrosSync.setItems(parametros);
		this.aitSyncResponse.add(parametrosSync);
	}

	public void addConvenio(List<Convenio> convenios) {
		SyncResponse<Convenio> convenioSync = new SyncResponse<>();
		convenioSync.setEntity("Convenio");
		convenioSync.setItems(convenios);
		aitSyncResponse.add(convenioSync);
	}

	public void addAgente(Agente agente) {
	    this.cdUsuario = agente.getCdUsuario();

	    AgenteSyncDTO agenteDTO = new AgenteSyncDTOBuilder(agente).build();
	    SyncResponse<AgenteSyncDTO> agenteSync = new SyncResponse<>();

	    agenteSync.setEntity("Agente");
	    agenteSync.setItems(Collections.singletonList(agenteDTO));

	    this.aitSyncResponse.add(agenteSync);
	}

	public void addUsuario(Usuario usuario) {
	    UsuarioSyncDTO usuarioDTO = new UsuarioSyncDTOBuilder(usuario).build();
	    SyncResponse<UsuarioSyncDTO> usuarioSync = new SyncResponse<>();

	    usuarioSync.setEntity("Usuario");
	    usuarioSync.setItems(Collections.singletonList(usuarioDTO));

	    this.aitSyncResponse.add(usuarioSync);
	}

	public void addUsuarioSuporte(CustomConnection customConnection) throws Exception {
		int cdUsuarioMaster = this.conversorBaseAntigaNovaFactory.getParametroRepository().getValorOfParametroAsInt("CD_USUARIO_MASTER", customConnection);
        if (cdUsuarioMaster <= 0) {
            throw new Exception("O parâmetro CD_USUARIO_MASTER não está definido ou está vazio.");
        }

		Usuario usuario = this.conversorBaseAntigaNovaFactory.getUsuarioRepository().get(cdUsuarioMaster, customConnection);
		if (usuario == null) {
			throw new UsuarioException("Usuário Master não localizado.");
		}

	    UsuarioSuporteSyncDTO usuarioDTO = new UsuarioSuporteSyncDTOBuilder(usuario).build();
	    SyncResponse<UsuarioSuporteSyncDTO> usuarioSync = new SyncResponse<>();

	    usuarioSync.setEntity("UsuarioSuporte");
	    usuarioSync.setItems(Collections.singletonList(usuarioDTO));

	    this.aitSyncResponse.add(usuarioSync);
	}

	public void addTiposVeiculo(List<TipoVeiculo> tiposVeiculo) {
		List<TipoVeiculoSyncDTO> tiposVeiculoDTO = new ArrayList<>();
		SyncResponse<TipoVeiculoSyncDTO> tipoVeiculoDTO = new SyncResponse<>();
		for(TipoVeiculo tipoVeiculo: tiposVeiculo) {
			tiposVeiculoDTO.add(new TipoVeiculoSyncDTOBuilder(tipoVeiculo).build());
		}
		tipoVeiculoDTO.setEntity("TipoVeiculo");
		tipoVeiculoDTO.setItems(tiposVeiculoDTO);
		this.aitSyncResponse.add(tipoVeiculoDTO);
	}

	public void addCategoriasVeiculo(List<CategoriaVeiculo> categoriasVeiculo) {
		List<CategoriaVeiculoSyncDTO> categoriasVeiculoDTO = new ArrayList<>();
		SyncResponse<CategoriaVeiculoSyncDTO> categoriaVeiculoDTO = new SyncResponse<>();
		for(CategoriaVeiculo categoriaVeiculo: categoriasVeiculo) {
			categoriasVeiculoDTO.add(new CategoriaVeiculoSyncDTOBuilder(categoriaVeiculo).build());
		}
		categoriaVeiculoDTO.setEntity("CategoriaVeiculo");
		categoriaVeiculoDTO.setItems(categoriasVeiculoDTO);
		this.aitSyncResponse.add(categoriaVeiculoDTO);
	}

	public void addBairros(List<Bairro> bairros) {
		List<BairroSyncDTO> bairrosDTO = new ArrayList<>();
		SyncResponse<BairroSyncDTO> bairroDTO = new SyncResponse<>();
		for(Bairro bairro: bairros) {
			bairrosDTO.add(new BairroSyncDTOBuilder(bairro).build());
		}
		bairroDTO.setEntity("Bairro");
		bairroDTO.setItems(bairrosDTO);
		this.aitSyncResponse.add(bairroDTO);
	}

	public UserManagerDTO setUser(UserManager userManager, int cdEquipamento) throws Exception {
		Agente agente = conversorBaseAntigaNovaFactory.getAgenteRepository().getByCdUsuario(userManager.getUsuario().getCdUsuario());
		UserManagerDTO user = new UserManagerDTO();
		user.setUsuario(userManager.getUsuario());
		user.setToken(userManager.getToken());
		user.setAgente(agente);
		user.setEquipamento(equipamentoService.get(cdEquipamento));
		return user;
	}

	public List<SyncResponse<?>> build() {
		return this.aitSyncResponse;
	}
}
