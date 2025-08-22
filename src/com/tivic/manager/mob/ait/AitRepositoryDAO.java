package com.tivic.manager.mob.ait;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.Talonario;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class AitRepositoryDAO implements AitRepository {
	
	@Override
	public Ait insert(Ait ait, CustomConnection customConnection) throws Exception {
		int cdAit = AitDAO.insert(ait, customConnection.getConnection());
		if (cdAit < 0)
			throw new Exception("Erro ao inserir Ait.");
		ait.setCdAit(cdAit);	
		return ait;
	}

	@Override
	public Ait update(Ait ait, CustomConnection customConnection) throws Exception {
		int aitUpdate = AitDAO.update(ait, customConnection.getConnection());
		if(aitUpdate <= 0)
			throw new Exception("Erro ao atualizar o Ait");
		return ait;
	}

	@Override
	public Ait get(int cdAit) throws Exception {
		return get(cdAit, new CustomConnection());
	}

	@Override
	public Ait get(int cdAit, CustomConnection customConnection) throws Exception {
		return AitDAO.get(cdAit, customConnection.getConnection());
	}

	@Override
	public List<Ait> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Ait> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Ait> search = new SearchBuilder<Ait>("mob_ait")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(Ait.class);
	}

	@Override
	public List<Ait> findByEvento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Ait> search = new SearchBuilder<Ait>("mob_ait A")
				.fields("A.*, C.cd_evento")	
				.addJoinTable("LEFT OUTER JOIN mob_ait_evento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("LEFT OUTER JOIN mob_evento_equipamento C ON (B.cd_evento = C.cd_evento)")
				.searchCriterios(searchCriterios)
				.build();

		return search.getList(Ait.class);
	}
	
	@Override
	public boolean hasAit(String idAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_ait", idAit, true);
		List<Ait> aits = find(searchCriterios, customConnection);

		return !aits.isEmpty();
	}
	
	@Override
	public int getUltimoNrAitByTalao(Talonario talonario, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("nr_ait", String.valueOf(talonario.getNrInicial()), ItemComparator.GREATER_EQUAL, Types.VARCHAR);
		searchCriterios.addCriterios("nr_ait", String.valueOf(talonario.getNrFinal()), ItemComparator.MINOR_EQUAL, Types.VARCHAR);
		
		Search<Ait> search = new SearchBuilder<Ait>("mob_ait")
				.fields("MAX(nr_ait) nr_ait")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		List<Ait> aits = search.getList(Ait.class);
		
		if(aits.get(0).getNrAit() == 0) {
			return talonario.getNrInicial() > 0 ? talonario.getNrInicial() - 1 : 0;
		}
		
		return aits.get(0).getNrAit();
	}
}
