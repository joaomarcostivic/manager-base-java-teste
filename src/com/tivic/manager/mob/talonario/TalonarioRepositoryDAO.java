package com.tivic.manager.mob.talonario;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.TalonarioDAO;
import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class TalonarioRepositoryDAO implements TalonarioRepository {

	@Override
	public void insert(Talonario talonario, CustomConnection customConnection) throws Exception {
		int cdTalao = TalonarioDAO.insert(talonario, customConnection.getConnection());
		if (cdTalao <= 0)
			throw new Exception("Erro ao inserir Talonario.");
		talonario.setCdTalao(cdTalao);	
	}

	@Override
	public void update(Talonario talonario, CustomConnection customConnection) throws Exception {
		int codRetorno = TalonarioDAO.update(talonario, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar talonário.");
	}

	@Override
	public Talonario get(int cdTalao, CustomConnection customConnection) throws Exception {
		return TalonarioDAO.get(cdTalao, customConnection.getConnection());
	}

	@Override
	public List<Talonario> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Talonario> search = new SearchBuilder<Talonario>("mob_talonario")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(Talonario.class);
	}
	
	public List<Talonario> getByAgente(Agente agente, SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.addCriteriosEqualInteger("cd_agente", agente.getCdAgente());
		Search<Talonario> search = new SearchBuilder<Talonario>("mob_talonario")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy("dt_entrega ASC")
				.build();
		
		return search.getList(Talonario.class);
	}
	
	@Override
	public List<Talonario> getEletronicoByAgente(int cdAgente, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_agente", cdAgente);
		searchCriterios.addCriteriosEqualInteger("tp_talao", TipoTalaoEnum.TP_TALONARIO_ELETRONICO_TRANSITO.getKey());
		Search<Talonario> search = new SearchBuilder<Talonario>("mob_talonario")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Talonario.class);
	}
}
