package com.tivic.manager.mob.ocorrencia;

import java.util.List;

import com.tivic.manager.mob.Ocorrencia;
import com.tivic.manager.mob.OcorrenciaDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class OcorrenciaRepositoryDAO implements OcorrenciaRepository {

	@Override
	public void insert(Ocorrencia Ocorrencia, CustomConnection customConnection) throws Exception {
		int cdOcorrencia = OcorrenciaDAO.insert(Ocorrencia, customConnection.getConnection());
		if (cdOcorrencia < 0)
			throw new Exception("Erro ao inserir Ocorrencia.");
		Ocorrencia.setCdOcorrencia(cdOcorrencia);	
	}

	@Override
	public void update(Ocorrencia Ocorrencia, CustomConnection customConnection) throws Exception {
		OcorrenciaDAO.update(Ocorrencia, customConnection.getConnection());
	}

	@Override
	public Ocorrencia get(int cdOcorrencia) throws Exception {
		return get(cdOcorrencia, new CustomConnection());
	}

	@Override
	public Ocorrencia get(int cdOcorrencia, CustomConnection customConnection) throws Exception {
		return OcorrenciaDAO.get(cdOcorrencia, customConnection.getConnection());
	}
	
	@Override
    public List<Ocorrencia> getAll() throws Exception {
    	return getAll(new CustomConnection());
    }
	
	@Override
	public List<Ocorrencia> getAll(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<Ocorrencia> search = new SearchBuilder<Ocorrencia>("mob_ocorrencia")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Ocorrencia.class);
	}

	@Override
	public List<Ocorrencia> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Ocorrencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Ocorrencia> search = new SearchBuilder<Ocorrencia>("mob_ocorrencia")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(Ocorrencia.class);
	}

	@Override
	public List<Ocorrencia> findOcorrenciasApp(SearchCriterios searchCriterios) throws Exception {
		return findOcorrenciasApp(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Ocorrencia> findOcorrenciasApp(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Ocorrencia> search = new SearchBuilder<Ocorrencia>("mob_ocorrencia")
				.additionalCriterias("tp_ocorrencia = " + TipoOcorrenciaEnum.OCORRENCIA_APP.getKey())
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(Ocorrencia.class);
	}
}
