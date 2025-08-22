package com.tivic.manager.mob.rrd;

import java.util.List;

import com.tivic.manager.mob.Rrd;
import com.tivic.manager.mob.RrdDAO;
import com.tivic.manager.mob.Talonario;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;


public class RrdRepositoryDAO implements RrdRepository {
	
	@Override
	public Rrd insert(Rrd Rrd, CustomConnection customConnection) throws Exception {
		int cdRrd = RrdDAO.insert(Rrd, customConnection.getConnection());
		if(cdRrd <= 0)
			throw new Exception("Erro ao inserir Rrd");
		Rrd.setCdRrd(cdRrd);
		return Rrd;
	}

	@Override
	public Rrd update(Rrd Rrd, CustomConnection customConnection) throws Exception {
		RrdDAO.update(Rrd, customConnection.getConnection());
		return Rrd;
	}

	@Override
	public Rrd get(int cdRrd, CustomConnection customConnection) throws Exception {
		return RrdDAO.get(cdRrd, customConnection.getConnection());
	}

	@Override
	public List<Rrd> find(SearchCriterios criterios, CustomConnection customConnection) throws Exception {
		Search<Rrd> search = new SearchBuilder<Rrd>("mob_rrd")
				.searchCriterios(criterios)
				.build();
		return search.getList(Rrd.class);
	}
	
	@Override
	public int getUltimoNrRrd(SearchCriterios searchCriterios, Talonario talonario, CustomConnection customConnection) throws Exception {
		Search<Rrd> search = new SearchBuilder<Rrd>("mob_rrd")
				.fields("nr_rrd")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy("nr_rrd DESC")
				.build();
		List<Rrd> rrds = search.getList(Rrd.class);
		
		if(rrds.isEmpty()) {
			return talonario.getNrInicial() > 0 ? talonario.getNrInicial() - 1 : 0;
		}
		
		return rrds.get(0).getNrRrd();
	}
	
}
