package com.tivic.manager.adapter.base.antiga.infracao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.adapter.base.antiga.talonario.TalonarioOld;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class InfracaoRepositoryOldDAO implements InfracaoRepository {

	private IAdapterService<InfracaoOld, Infracao> adapterService;
	
	public InfracaoRepositoryOldDAO() throws Exception {
		this.adapterService = new AdapterInfracaoService();
	}
	
	@Override
	public void insert(Infracao infracao, CustomConnection customConnection) throws Exception {
		InfracaoOld infracaoOld = this.adapterService.toBaseAntiga(infracao);
		int cdInfracao = InfracaoOldDAO.insert(infracaoOld, customConnection.getConnection());
		if (cdInfracao <= 0)
			throw new Exception("Erro ao inserir Infracao.");
		infracao.setCdInfracao(cdInfracao);	
	}

	@Override
	public void update(Infracao infracao, CustomConnection customConnection) throws Exception {
		InfracaoOld infracaoOld = this.adapterService.toBaseAntiga(infracao);
		InfracaoOldDAO.update(infracaoOld, customConnection.getConnection());
	}
	
	@Override
	public Infracao get(int cdInfracao) throws Exception {
		return get(cdInfracao, new CustomConnection());
	}

	@Override
	public Infracao get(int cdInfracao, CustomConnection customConnection) throws Exception {
		InfracaoOld infracaoOld = InfracaoOldDAO.get(cdInfracao, customConnection.getConnection());
		return this.adapterService.toBaseNova(infracaoOld);
	}
	
	@Override
	public List<Infracao> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Infracao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Infracao> infracoes = new ArrayList<Infracao>();
		ResultSetMapper<InfracaoOld> rsm = new ResultSetMapper<InfracaoOld>(InfracaoOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), InfracaoOld.class);
		List<InfracaoOld> infracaoOldList = rsm.toList();
		for (InfracaoOld infracaoOld : infracaoOldList) {
			Infracao infracao = this.adapterService.toBaseNova(infracaoOld);
			infracoes.add(infracao);
		}
		return infracoes;
	}
	
	@Override
	public List<Infracao> getAll(String keyword) throws Exception {
		return getAll(keyword, new CustomConnection());
	}
	
	@Override
	public List<Infracao> getAll(String keyword, CustomConnection customConnection) throws Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    List<Infracao> infracoes = new ArrayList<>();
	    Search<InfracaoOld> search = new SearchBuilder<InfracaoOld>(getSubQuery(keyword))
	            .searchCriterios(searchCriterios)
	            .customConnection(customConnection)
	            .additionalCriterias("row_num = 1")
	            .build();
	    List<InfracaoOld> infracaoOldList = search.getList(InfracaoOld.class);
	    for (InfracaoOld infracaoOld : infracaoOldList) {
	        Infracao infracao = this.adapterService.toBaseNova(infracaoOld);
	        infracoes.add(infracao);
	    }
	    return infracoes;
	}
	
	private String getSubQuery(String keyword) {
		String subQuery = " (SELECT *, ROW_NUMBER() OVER (PARTITION BY nr_cod_detran ORDER BY dt_fim_vigencia DESC NULLS FIRST) AS row_num" + 
				"		    FROM infracao" + 
				"		    WHERE " +
				"			(dt_fim_vigencia IS NULL OR dt_fim_vigencia = (" + 
				"		            SELECT MAX(dt_fim_vigencia) " + 
				"		            FROM infracao " + 
				"		            WHERE (UPPER(ds_infracao2) LIKE UPPER('%" + keyword + "%')" +
				" 					OR CAST(nr_cod_detran AS VARCHAR) LIKE UPPER('%" + keyword + "%'))" + 
				"		        )) AND (UPPER(ds_infracao2) LIKE UPPER('%" + keyword + "%') " + 
				"			OR CAST(nr_cod_detran AS VARCHAR) LIKE UPPER('%" + keyword + "%'))) AS numbered_rows ";
		return subQuery;
	}
	
	public List<Infracao> getInfracoesVigentes(CustomConnection customConnection) throws Exception {
		List<Infracao> talonarios = new ArrayList<Infracao>();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("dt_fim_vigencia", "", ItemComparator.ISNULL, Types.VARCHAR);
		Search<InfracaoOld> search = new SearchBuilder<InfracaoOld>("infracao")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		List<InfracaoOld> infracaoOldList = search.getList(InfracaoOld.class);
		
		for (InfracaoOld infracaoOld : infracaoOldList) {
			Infracao infracao = this.adapterService.toBaseNova(infracaoOld);
			talonarios.add(infracao);
		}
		return talonarios;
	}
}