package com.tivic.manager.grl.equipamento;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.EquipamentoPessoaServices;
import com.tivic.manager.grl.equipamento.base.EquipamentoServiceBase;
import com.tivic.manager.grl.equipamento.base.EquipamentoServiceBaseFactory;
import com.tivic.manager.grl.equipamento.repository.EquipamentoRepository;
import com.tivic.manager.grl.equipamento.validations.SaveEquipamentoValidations;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class EquipamentoService implements IEquipamentoService {
	
	EquipamentoRepository equipamentoRepository;
	EquipamentoServiceBase equipamentoServiceBase;
	
	private ManagerLog managerLog;
	
	public EquipamentoService() throws Exception {
		equipamentoRepository = (EquipamentoRepository) BeansFactory.get(EquipamentoRepository.class);
		equipamentoServiceBase = EquipamentoServiceBaseFactory.build();
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@Override
	public Equipamento insert(Equipamento equipamento) throws BadRequestException, Exception{
		return insert(equipamento, new CustomConnection());
	}
	
	@Override
	public Equipamento insert(Equipamento equipamento, CustomConnection customConnection) throws BadRequestException, Exception{
		try {
			managerLog.info("Inserção de equipamento", "Equipamento a ser inserido: " + equipamento.getNmEquipamento());
			customConnection.initConnection(true);
			new SaveEquipamentoValidations().validate(equipamento, customConnection);
			managerLog.warning("Validações de equipamento", "Equipamento " +equipamento.getNmEquipamento()+ " passou em todas as validações");
			equipamentoRepository.insert(equipamento, customConnection);
			customConnection.finishConnection();
			managerLog.info("Inserção de equipamento", equipamento.getNmEquipamento() + " inserido com sucesso");
			return equipamento;
		} finally {
			customConnection.closeConnection();
		}
		
	}
	
	@Override
	public Equipamento update(Equipamento equipamento) throws Exception{
		return update(equipamento, new CustomConnection());
	}
	
	@Override
	public Equipamento update(Equipamento equipamento, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			new SaveEquipamentoValidations().validate(equipamento, customConnection);
			equipamentoRepository.update(equipamento, customConnection);
			customConnection.finishConnection();
			return equipamento;
		} finally {
			customConnection.closeConnection();
		}
		
	}

	@Override
	public Equipamento get(int cdEquipamento) throws Exception{
		return get(cdEquipamento, new CustomConnection());
	}
	
	@Override
	public Equipamento get(int cdEquipamento, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			Equipamento equipamento = equipamentoRepository.get(cdEquipamento, customConnection);
			if(equipamento == null)
				throw new NoContentException("Nenhum equipamento encontrado");
			customConnection.finishConnection();
			return equipamento;
		} finally {
			customConnection.closeConnection();
		}
		
	}

	@Override
	public List<EquipamentoUsuario> getAll() throws Exception{
		return getAll(new CustomConnection());
	}
	
	@Override
	public List<EquipamentoUsuario> getAll(CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			List<EquipamentoUsuario> equipamentos = equipamentoServiceBase.find(new SearchCriterios(), customConnection);
			if(equipamentos.isEmpty())
				throw new NoContentException("Nenhum equipamento encontrado");
			customConnection.finishConnection();
			return equipamentos;
		} finally {
			customConnection.closeConnection();
		}
		
	}

	@Override
	public List<Equipamento> find(EquipamentoSearch equipamentoSearch) throws Exception{
		return find(equipamentoSearch, new CustomConnection());
	}
	
	@Override
	public List<Equipamento> find(EquipamentoSearch equipamentoSearch, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			List<Equipamento> equipamentos = equipamentoRepository.find(equipamentoSearch.getCriterios(), customConnection);
			if(equipamentos.isEmpty())
				throw new NoContentException("Nenhum equipamento encontrado");
			customConnection.finishConnection();
			return equipamentos;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Equipamento> loadDisponiveisEmprestimo(SearchCriterios searchCriterios) throws Exception{
		return loadDisponiveisEmprestimo(searchCriterios, new CustomConnection());
	}
	
	@Override
	public List<Equipamento> loadDisponiveisEmprestimo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			Search<Equipamento> search = new SearchBuilder<Equipamento>("grl_equipamento")
					.additionalCriterias("NOT EXISTS (" + 
							"					 		SELECT Z.* FROM grl_equipamento_pessoa Z" + 
							"					 		WHERE A.cd_equipamento = Z.cd_equipamento" + 
							"					 		AND Z.st_emprestimo = "+EquipamentoPessoaServices.ST_EM_ANDAMENTO + 
							"					      )")
					.build();
			List<Equipamento> equipamentos = search.getList(Equipamento.class);
			if(equipamentos.isEmpty())
				throw new NoContentException("Nenhum equipamento encontrado");
			customConnection.finishConnection();
			return equipamentos;
		} finally {
			customConnection.closeConnection();
		}
		
	}

	@Override
	public Equipamento getEquipamentoById(String idEquipamento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Equipamento equipamento = getEquipamentoById(idEquipamento, customConnection);
			customConnection.finishConnection();
			return equipamento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Equipamento getEquipamentoById(String idEquipamento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_equipamento", idEquipamento);
		Search<Equipamento> search = new SearchBuilder<Equipamento>("grl_equipamento")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		if(search.getList(Equipamento.class).isEmpty()) {
			throw new Exception("Equipamento não localizado.");
		}
		
		return search.getList(Equipamento.class).get(0);
	}
	public boolean verificarBloqueio(String idSerial) throws Exception{
		return verificarBloqueio(idSerial, new CustomConnection());
	}
	
	@Override
	public boolean verificarBloqueio(String idSerial, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			if(idSerial == null || idSerial.trim().equals("")) {
				throw new BadRequestException("Número de serial deve ser passado para a verificação");
			}
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("id_equipamento", idSerial);
			Search<Equipamento> search = new SearchBuilder<Equipamento>("grl_equipamento")
				.searchCriterios(searchCriterios)
			.build();
			List<Equipamento> equipamentos = search.getList(Equipamento.class);
			if(equipamentos.isEmpty())
				throw new NoContentException("Nenhum equipamento encontrado");
			customConnection.finishConnection();
			return equipamentos.get(0).getStEquipamento() == EquipamentoServices.INATIVO;
		} finally {
			customConnection.closeConnection();
		}
		
	}
	
	@Override
	public Equipamento getByIdEquipamento(String nmEquipamento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Equipamento equipamento = getByIdEquipamento(nmEquipamento, customConnection);
			customConnection.finishConnection();
			return equipamento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Equipamento getByIdEquipamento(String nmEquipamento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_equipamento", nmEquipamento);
		
		List<Equipamento> equipamentos = this.equipamentoRepository.find(searchCriterios.getCriterios(), customConnection);
		
		if(equipamentos.isEmpty())
			throw new NoContentException("Nenhum equipamento encontrado");
		
		return equipamentos.get(0);
	}

	@Override
	public Equipamento getEquipamentoAtivo(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Equipamento equipamento = getEquipamentoAtivo(searchCriterios, customConnection);
			customConnection.finishConnection();
			return equipamento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Equipamento getEquipamentoAtivo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
    	searchCriterios.addCriteriosEqualInteger("st_equipamento", EquipamentoServices.ATIVO);
		List<Equipamento> equipamentos = this.equipamentoRepository.find(searchCriterios.getCriterios(), customConnection);
		if(equipamentos.isEmpty())
			throw new NoContentException("Nenhum equipamento encontrado");
		return equipamentos.get(0);
	}

}
