package com.tivic.manager.adapter.base.antiga.talonario;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.talonario.TalonarioRepository;
import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class TalonarioRepositoryOldDAO implements TalonarioRepository {

	private IAdapterService<TalonarioOld, Talonario> adapterService;
	
	public TalonarioRepositoryOldDAO() throws Exception {
		adapterService = new AdapterTalonarioService();
	}
	
	@Override
	public void insert(Talonario talonario, CustomConnection customConnection) throws Exception {
		TalonarioOld talonarioOld = adapterService.toBaseAntiga(talonario);
		int cdTalao = TalonarioOldDAO.insert(talonarioOld, customConnection.getConnection());
		if (cdTalao <= 0)
			throw new Exception("Erro ao inserir Talonario.");
		talonario.setCdTalao(cdTalao);	
	}

	@Override
	public void update(Talonario talonario, CustomConnection customConnection) throws Exception {
		TalonarioOld talonarioOld = adapterService.toBaseAntiga(talonario);
		int codRetorno = TalonarioOldDAO.update(talonarioOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar talonário.");
	}

	@Override
	public Talonario get(int cdTalao, CustomConnection customConnection) throws Exception {
		TalonarioOld talonarioOld = TalonarioOldDAO.get(cdTalao, customConnection.getConnection());
		Talonario talonario = adapterService.toBaseNova(talonarioOld);
		return talonario;
	}


	@Override
	public List<Talonario> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Talonario> talonarios = new ArrayList<Talonario>();
		ResultSetMapper<TalonarioOld> rsm = new ResultSetMapper<TalonarioOld>(TalonarioOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), TalonarioOld.class);
		List<TalonarioOld> talonarioOldList = rsm.toList();
		for (TalonarioOld talonarioOld : talonarioOldList) {
			Talonario talonario = adapterService.toBaseNova(talonarioOld);
			talonarios.add(talonario);
		}
		return talonarios;
	}
	
	public List<Talonario> getByAgente(Agente agente, SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Talonario> talonarios = new ArrayList<Talonario>();
		Search<TalonarioOld> search = new SearchBuilder<TalonarioOld>("talonario")
				.searchCriterios(searchCriterios)
				.additionalCriterias("cod_agente = " + agente.getCdAgente())
				.customConnection(customConnection)
				.orderBy("dt_entrega ASC")
				.build();
		
		List<TalonarioOld> talonarioOldList = search.getList(TalonarioOld.class);
		for (TalonarioOld talonarioOld : talonarioOldList) {
			Talonario talonario = adapterService.toBaseNova(talonarioOld);
			talonarios.add(talonario);
		}
		return talonarios;
	}
	
	@Override
	public List<Talonario> getEletronicoByAgente(int cdAgente, CustomConnection customConnection) throws Exception {
		List<Talonario> talonarios = new ArrayList<Talonario>();
		SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualInteger("A.cod_agente", cdAgente);
	    searchCriterios.addCriteriosEqualInteger("A.tp_talao", TipoTalaoEnum.TP_TALONARIO_ELETRONICO_TRANSITO.getKey());
		Search<TalonarioOld> search = new SearchBuilder<TalonarioOld>("talonario A")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
	    
	    List <TalonarioOld> talonarioOldList = search.getList(TalonarioOld.class);
	    for (TalonarioOld talonarioOld : talonarioOldList) {
	    	Talonario talonario = adapterService.toBaseNova(talonarioOld);
	    	talonarios.add(talonario);
	    }
	    
	    return talonarios;
	}
}
