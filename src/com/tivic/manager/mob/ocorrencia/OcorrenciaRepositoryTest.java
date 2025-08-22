package com.tivic.manager.mob.ocorrencia;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ocorrencia;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class OcorrenciaRepositoryTest implements OcorrenciaRepository{

	private List<Ocorrencia> ocorrencias;
	private int cdOcorrencia;
	
	public OcorrenciaRepositoryTest() {
		this.ocorrencias = new ArrayList<Ocorrencia>();
		this.cdOcorrencia = 1;
	}
	
	@Override
	public void insert(Ocorrencia ocorrencia, CustomConnection customConnection) throws Exception {
		ocorrencia.setCdOcorrencia(this.cdOcorrencia++);
		this.ocorrencias.add(ocorrencia);
	}

	@Override
	public void update(Ocorrencia ocorrencia, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.ocorrencias.size(); i++) {
			Ocorrencia ocorrenciaFromList = this.ocorrencias.get(i);
			if(ocorrenciaFromList.getCdOcorrencia() == ocorrencia.getCdOcorrencia()) {
				this.ocorrencias.set(i, ocorrencia);
				break;
			}
		}
	}

	@Override
	public Ocorrencia get(int cdOcorrencia) throws Exception {
		return get(cdOcorrencia, new CustomConnection());
	}

	@Override
	public Ocorrencia get(int cdOcorrencia, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.ocorrencias.size(); i++) {
			Ocorrencia ocorrenciaFromList = this.ocorrencias.get(i);
			if(ocorrenciaFromList.getCdOcorrencia() == cdOcorrencia) {
				return ocorrenciaFromList;
			}
		}
		return null;
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
		return this.ocorrencias;
	}

	@Override
	public List<Ocorrencia> findOcorrenciasApp(SearchCriterios searchCriterios) throws Exception {
		return findOcorrenciasApp(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Ocorrencia> findOcorrenciasApp(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return ocorrencias;
	}

}
