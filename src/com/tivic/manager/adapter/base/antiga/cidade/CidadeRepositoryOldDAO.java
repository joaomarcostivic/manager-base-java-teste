package com.tivic.manager.adapter.base.antiga.cidade;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ResultSetMap;

public class CidadeRepositoryOldDAO implements CidadeRepository {
	List<Estado> estados;

	@Override
	public Cidade insert(Cidade cidade, CustomConnection customConnection) throws Exception {
		return null;
	}

	@Override
	public Cidade update(Cidade cidade, CustomConnection customConnection) throws Exception {
		return null;
	}

	@Override
	public Cidade get(int cdCidade) throws Exception {
		return null;
	}

	@Override
	public Cidade get(int cdCidade, CustomConnection customConnection) throws Exception {
		return null;
	}

	@Override
	public List<Cidade> getAll() throws Exception {
		return Collections.emptyList();
	}

	@Override
	public List<Cidade> getAll(CustomConnection customConnection) throws Exception {
		return Collections.emptyList();
	}

	@Override
    public List<Cidade> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}
	
	@Override
	public List<Cidade> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {

	    List<Cidade> cidades = new ArrayList<>();
	    PreparedStatement ps = customConnection.getConnection().prepareStatement("SELECT cod_municipio, nm_municipio, nm_uf FROM municipio WHERE nm_municipio <> 'MUNICÍPIO DESCONHECIDO'");
	    ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
	    
	    Search<Estado> estadoSearch = new SearchBuilder<Estado>("grl_estado")
	    		.fields("*")
	    		.searchCriterios(new SearchCriterios())
	    		.customConnection(customConnection).build();
	    estados = estadoSearch.getList(Estado.class);

	    while (rsm.next()) {

	        Cidade cidade = new Cidade();
	        cidade.setCdCidade(rsm.getInt("cod_municipio"));
	        cidade.setNmCidade(rsm.getString("nm_municipio").replaceAll("'", "´"));
	        cidade.setIdCidade(String.valueOf(rsm.getInt("cod_municipio")));
	        
	        Optional<Estado> estado = estados.stream().filter(e -> e.getSgEstado() != null && e.getSgEstado().equals(rsm.getString("nm_uf"))).findFirst();
	        cidade.setCdEstado(estado.isPresent() ? estado.get().getCdEstado() : 0);

	        cidades.add(cidade);
	    }

	    return cidades;
	}
}
