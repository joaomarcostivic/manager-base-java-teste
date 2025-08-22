package com.tivic.manager.mob.ocorrencia;

import java.util.List;

import com.tivic.manager.mob.Ocorrencia;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface OcorrenciaRepository {
	public void insert(Ocorrencia ocorrencia, CustomConnection customConnection) throws Exception;
	public void update(Ocorrencia ocorrencia, CustomConnection customConnection) throws Exception;
	public Ocorrencia get(int cdOcorrencia) throws Exception;
	public Ocorrencia get(int cdOcorrencia, CustomConnection customConnection) throws Exception;
	public List<Ocorrencia> getAll() throws Exception;
	public List<Ocorrencia> getAll(CustomConnection customConnection) throws Exception;
	public List<Ocorrencia> find(SearchCriterios searchCriterios) throws Exception;
	public List<Ocorrencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<Ocorrencia> findOcorrenciasApp(SearchCriterios searchCriterios) throws Exception;
	public List<Ocorrencia> findOcorrenciasApp(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
