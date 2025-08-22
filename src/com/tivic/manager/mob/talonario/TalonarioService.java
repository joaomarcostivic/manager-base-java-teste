package com.tivic.manager.mob.talonario;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.repository.EquipamentoRepository;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.agente.AgenteRepository;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;
import com.tivic.manager.mob.talonario.factory.tipotalaoequipamento.TipoTalaoEquipamentoFactory;
import com.tivic.manager.mob.talonario.factory.ultimodocumento.UltimoNrDocumentoFactory;
import com.tivic.manager.mob.talonario.validatons.TalonarioValidators;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class TalonarioService implements ITalonarioService {

	private TalonarioRepository talonarioRepository;
	private AgenteRepository agenteRepository;
	private EquipamentoRepository equipamentoRepository;
	private ManagerLog managerLog;
	private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
	
	public TalonarioService() throws Exception {
		this.talonarioRepository = (TalonarioRepository) BeansFactory.get(TalonarioRepository.class);
		this.agenteRepository = (AgenteRepository) BeansFactory.get(AgenteRepository.class);
		this.equipamentoRepository = (EquipamentoRepository) BeansFactory.get(EquipamentoRepository.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	    this.conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
	}
	
	@Override
	public List<Talonario> getTalonairoByAgente(int cdAgente) throws Exception {
		CustomConnection customConncetion = new CustomConnection();
		try {
			customConncetion.initConnection(false);
			List<Talonario> talonarioList = getTalonairoByAgente(cdAgente, customConncetion);
			customConncetion.finishConnection();
			return talonarioList;
		} finally {
			customConncetion.closeConnection();
		}
	}

	@Override
	public List<Talonario> getTalonairoByAgente(int cdAgente, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_agente", cdAgente);
		searchCriterios.addCriteriosEqualInteger("st_talao", SituacaoTalaoEnum.ST_TALAO_ATIVO.getKey());
		Search<Talonario> search = new SearchBuilder<Talonario>("mob_talonario")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Talonario.class);
	}

	@Override
	public TalonarioLoteDTO saveLote(TalonarioLoteDTO talonarioLote) throws Exception{
		return saveLote(talonarioLote, new CustomConnection());
	}
	
	private TalonarioLoteDTO saveLote(TalonarioLoteDTO talonarioLote, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			
			int qtdTalonarios = talonarioLote.getQtdTalonarios();
			int nrInicial = talonarioLote.getNrInicial();
			int nrFinal = nrInicial + talonarioLote.getQtdFolhas() - 1;
			
			for(int i = 0; i < qtdTalonarios; i++) {
				Talonario talonario = new Talonario();
				talonario.setNrInicial(nrInicial);
				talonario.setSgTalao(talonarioLote.getSgTalao());
				talonario.setTpTalao(talonarioLote.getTpTalao());
				talonario.setNrFinal(nrFinal);
				talonario.setStTalao(SituacaoTalaoEnum.ST_TALAO_INATIVO.getKey());
				
				insert(talonario);
				
				nrInicial += talonarioLote.getQtdFolhas();
				nrFinal += talonarioLote.getQtdFolhas();
			}
			
			customConnection.finishConnection();
			return talonarioLote;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Talonario insert(Talonario talonario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new TalonarioValidators(talonario).validate(customConnection);
			insert(talonario, customConnection);
			customConnection.finishConnection();
			return talonario;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Talonario insert(Talonario talonario, CustomConnection customConnection) throws Exception {
		talonarioRepository.insert(talonario, customConnection);
		return talonario;
	}
	
	@Override
	public Talonario update(Talonario talonario) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(talonario, customConnection);
			customConnection.finishConnection();
			return talonario;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Talonario update(Talonario talonario, CustomConnection customConnection) throws Exception{
		talonarioRepository.update(talonario, customConnection);
		return talonario;
	}
	
	
	@Override
	public Talonario get(int cdTalao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Talonario talonario = get(cdTalao, customConnection);
			customConnection.finishConnection();
			return talonario;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Talonario get(int cdTalao, CustomConnection customConnction) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_talao", cdTalao);
		Search<Talonario> search = new SearchBuilder<Talonario>("mob_talonario")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnction)
				.build();
		
		if(search.getList(Talonario.class).size() == 0) {
			throw new Exception("Não foi possível encontrar o talão informado.");
		}
		
		return search.getList(Talonario.class).get(0);
	}
	
	@Override
	public List<Talonario> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Talonario> talonarios = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return talonarios;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Talonario> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.talonarioRepository.find(searchCriterios, customConnection);
	}
	
	@Override
	public Talonario getByCdEquipamento(int cdAgente, int cdEquipamento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Talonario talonario = getByCdEquipamento(cdAgente, cdEquipamento, customConnection);
			customConnection.finishConnection();
			return talonario;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Talonario getByCdEquipamento(int cdAgente, int cdEquipamento, CustomConnection customConnection) throws Exception {
		Equipamento equipamento = this.equipamentoRepository.get(cdEquipamento, customConnection);
		
		if(equipamento == null) {
			throw new ValidacaoException("Equipamento não encontrado");
		}
		
		int tpTalao = new TipoTalaoEquipamentoFactory()
				.getStrategy(equipamento.getTpEquipamento())
				.getTpTalaoByEquipamento();
		
		List<Talonario> talonariosAgente = getDisponiveisByCdAgente(cdAgente, customConnection);

        for (Talonario talao : talonariosAgente) {
            if (isTalaoValid(talao, tpTalao)) {
                return talao;
            }
        }
		
        throw new NoContentException("Nenhum talão do tipo '" + TipoTalaoEnum.valueOf(tpTalao) + 
        	    "' disponível para este agente. Consulte o suporte técnico.");

	}
	
	private boolean isTalaoValid(Talonario talao, int tpTalao) {
	    return talao.getNrInicial() > 0 &&
	            talao.getNrUltimoAit() <= talao.getNrFinal() &&
	            talao.getTpTalao() == tpTalao &&
	            talao.getSgTalao() != null;
	}
	
	@Override
	public List<Talonario> getDisponiveisByCdAgente(int cdAgente) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Talonario> talonarios = getDisponiveisByCdAgente(cdAgente, customConnection);
			customConnection.finishConnection();
			return talonarios;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Talonario> getDisponiveisByCdAgente(int cdAgente, CustomConnection customConnection) throws Exception {
		this.agenteRepository = conversorBaseAntigaNovaFactory.getAgenteRepository();
		Agente agente = this.agenteRepository.get(cdAgente, customConnection);
		
		if(agente == null) {
			throw new ValidacaoException("Agente não encontrado");
		}
		
		this.talonarioRepository = conversorBaseAntigaNovaFactory.getTalonarioRepository();
		List<Talonario> talonariosAgente = this.talonarioRepository.getByAgente(agente, buildAgenteCriterios(), customConnection);                                                                                                                                                                                 
		List<Talonario> talonariosDisponiveis = new ArrayList<Talonario>();
		for(Talonario talonario : talonariosAgente) {
			try {
			    int ultimoNrAitTalao =  new UltimoNrDocumentoFactory()
			    		.getStrategy(talonario)
			    		.getUltimoNrDocumento(customConnection);
			    
			    if (ultimoNrAitTalao >= talonario.getNrFinal()) {
			    	managerLog.info("Verificando talonários disponíveis", "Desconsiderando talão pois seu último nrAit (" + ultimoNrAitTalao + ") é maior ou igual ao nrFinal do talão");
			    	continue;		    	
			    }
			    
			    Talonario talao = buildTalonario(talonario, ultimoNrAitTalao);
			    talonariosDisponiveis.add(talao);
			} catch (Exception e) {
				managerLog.info("O talonário " + talonario.getCdTalao() + " é inválido.", e.getMessage());
				continue;
			}
		}
		
		return talonariosDisponiveis;
	}
	
	public Talonario buildTalonario(Talonario talonario, int nrUltimoAitTalao) {
	    return new TalonarioBuilder()
	    .setCdTalao(talonario.getCdTalao())
	    .setCdAgente(talonario.getCdAgente())
	    .setNrInicial(talonario.getNrInicial())
	    .setNrFinal(talonario.getNrFinal())
	    .setDtEntrega(talonario.getDtEntrega())
	    .setDtDevolucao(talonario.getDtDevolucao())
	    .setStTalao(talonario.getStTalao())
	    .setNrTalao(talonario.getNrTalao())
	    .setTpTalao(talonario.getTpTalao())
	    .setSgTalao(talonario.getSgTalao())
	    .setNrUltimoAit(nrUltimoAitTalao)
	    .build();
	}
	
	public SearchCriterios buildAgenteCriterios() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("st_talao", SituacaoTalaoEnum.ST_TALAO_ATIVO.getKey());
		
		return searchCriterios;
	}
	
	@Override
	public List<Talonario> findTaloesUsados(int cdTalao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			searchCriterios.addCriteriosEqualInteger("A.cd_talao", cdTalao, cdTalao > 0);
			List<Talonario> talonarioList = findTaloesUsados(searchCriterios).getList(Talonario.class);
			customConnection.finishConnection();
			return talonarioList;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public Search<Talonario> findTaloesUsados(SearchCriterios searchCriterios) throws Exception{
		Search<Talonario> search = new SearchBuilder<Talonario>("mob_talonario A")
				.addJoinTable("JOIN mob_ait B ON (A.cd_talao = B.cd_talao)")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}

	@Override
	public PagedResponse<TalonarioDTO> findTaloes(SearchCriterios searchCriterios) throws Exception {
		Search<TalonarioDTO> search = findTaloes(searchCriterios, new CustomConnection());
		List<TalonarioDTO> listTaloes = quantidadeTaloesDisponiveis(search.getList(TalonarioDTO.class));
		return new PagedResponse<TalonarioDTO>(listTaloes, search.getRsm().getTotal());
	}

	private Search<TalonarioDTO> findTaloes(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
			Search<TalonarioDTO> search = new SearchBuilder<TalonarioDTO>("mob_talonario A")
					.addJoinTable("LEFT OUTER JOIN mob_agente B ON (A.cd_agente = B.cd_agente)")
					.orderBy("A.dt_entrega DESC")
					.searchCriterios(searchCriterios)
					.customConnection(customConnection)
					.count()
				.build();
		return search;
	}
	
	private List<TalonarioDTO> quantidadeTaloesDisponiveis(List<TalonarioDTO> talonariosDTO) throws Exception {
		List<TalonarioDTO> talonarioList = new ArrayList<TalonarioDTO>();
		for(TalonarioDTO talonarioDTO: talonariosDTO) {
			int folhasUsadas = findTaloesUsados(talonarioDTO.getCdTalao()).size();
			int folhasDisponiveis = (talonarioDTO.getNrFinal() - talonarioDTO.getNrInicial() + 1) - folhasUsadas;
			talonarioDTO.setQtFolhasDisponiveis(folhasDisponiveis);
			talonarioList.add(talonarioDTO);
		}
		return talonarioList;
	}
}
