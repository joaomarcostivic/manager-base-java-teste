package com.tivic.manager.mob.infracao;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;


public class InfracaoRepositoryDAO implements InfracaoRepository {

	@Override
	public void insert(Infracao infracao, CustomConnection customConnection) throws Exception {
		int cdInfracao = InfracaoDAO.insert(infracao, customConnection.getConnection());
		if (cdInfracao <= 0)
			throw new Exception("Erro ao inserir Infracao.");
		infracao.setCdInfracao(cdInfracao);	}

	@Override
	public void update(Infracao infracao, CustomConnection customConnection) throws Exception {
		InfracaoDAO.update(infracao, customConnection.getConnection());
	}
	
	@Override
	public Infracao get(int cdInfracao) throws Exception {
		return get(cdInfracao, new CustomConnection());
	}

	@Override
	public Infracao get(int cdInfracao, CustomConnection customConnection) throws Exception {
		return InfracaoDAO.get(cdInfracao, customConnection.getConnection());
	}

	@Override
	public List<Infracao> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Infracao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Infracao> search = new SearchBuilder<Infracao>("mob_infracao")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(Infracao.class);
	}
	
	@Override
	public List<Infracao> getAll(String keyword) throws Exception {
		return getAll(keyword, new CustomConnection());
	}
	
	@Override
	public List<Infracao> getAll(String keyword, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();		
		
		Search<Infracao> search = new SearchBuilder<Infracao>(getSubQuery(keyword))
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.additionalCriterias("row_num = 1")
				.build();
		return search.getList(Infracao.class);
	}
	
	private String getSubQuery(String keyword) {
		String subQuery = " (SELECT *, ROW_NUMBER() OVER (PARTITION BY nr_cod_detran ORDER BY dt_fim_vigencia DESC NULLS FIRST) AS row_num" + 
				"		    FROM mob_infracao" + 
				"		    WHERE " +
				"			(dt_fim_vigencia IS NULL OR dt_fim_vigencia = (" + 
				"		            SELECT MAX(dt_fim_vigencia) " + 
				"		            FROM mob_infracao " + 
				"		            WHERE (UPPER(ds_infracao) LIKE UPPER('%" + keyword + "%')" +
				" 					OR CAST(nr_cod_detran AS VARCHAR) LIKE UPPER('%" + keyword + "%'))" + 
				"		        )) AND (UPPER(ds_infracao) LIKE UPPER('%" + keyword + "%') " + 
				"			OR CAST(nr_cod_detran AS VARCHAR) LIKE UPPER('%" + keyword + "%'))) AS numbered_rows ";
		return subQuery;
	}
	
	@Override
	public List<Infracao> getInfracoesVigentes(CustomConnection customConnection)	throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("dt_fim_vigencia", "", ItemComparator.ISNULL, Types.VARCHAR);
		Search<Infracao> search = new SearchBuilder<Infracao>("mob_infracao")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Infracao.class);
	}
}
