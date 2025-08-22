package com.tivic.manager.adapter.base.antiga.ocorrencia;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.Ocorrencia;
import com.tivic.manager.mob.ocorrencia.OcorrenciaRepository;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class OcorrenciaRepositoryOldDAO implements OcorrenciaRepository {
	
	private IAdapterService<OcorrenciaOld, Ocorrencia> adapterService;
	
	public OcorrenciaRepositoryOldDAO() throws Exception {
		this.adapterService = new AdapterOcorrenciaService();
	}

	@Override
	public void insert(Ocorrencia ocorrencia, CustomConnection customConnection) throws Exception {
		OcorrenciaOld ocorrenciaOld = this.adapterService.toBaseAntiga(ocorrencia);
		int codRetorno = OcorrenciaOldDAO.insert(ocorrenciaOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar ocorrencia");
		ocorrencia.setCdOcorrencia(codRetorno);
	}

	@Override
	public void update(Ocorrencia ocorrencia, CustomConnection customConnection) throws Exception {
		OcorrenciaOld ocorrenciaOld = this.adapterService.toBaseAntiga(ocorrencia);
		int codRetorno = OcorrenciaOldDAO.update(ocorrenciaOld, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao cadastrar ocorencia");
	}

	@Override
	public Ocorrencia get(int cdOcorrencia) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.finishConnection();
			Ocorrencia ocorrencia = get(cdOcorrencia, customConnection);
			return ocorrencia;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Ocorrencia get(int cdOcorrencia, CustomConnection customConnection) throws Exception {
		OcorrenciaOld ocorrenciaOld = OcorrenciaOldDAO.get(cdOcorrencia, customConnection.getConnection());
		return this.adapterService.toBaseNova(ocorrenciaOld);
	}

	@Override
	public List<Ocorrencia> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Ocorrencia> ocorrenciaList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return ocorrenciaList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Ocorrencia> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
		ResultSetMapper<OcorrenciaOld> rsm = new ResultSetMapper<OcorrenciaOld>(OcorrenciaOldDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), OcorrenciaOld.class);
		List<OcorrenciaOld> OcorrenciaOldList = rsm.toList();
		for (OcorrenciaOld ocorrenciaOld : OcorrenciaOldList) {
			Ocorrencia ocorrencia = this.adapterService.toBaseNova(ocorrenciaOld);
			ocorrencias.add(ocorrencia);
		}
		return ocorrencias;
	}

	@Override
	public List<Ocorrencia> getAll() throws Exception {
    	return getAll(new CustomConnection());
	}

	@Override
	public List<Ocorrencia> getAll(CustomConnection customConnection) throws Exception {
		List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<OcorrenciaOld> search = new SearchBuilder<OcorrenciaOld>("ocorrencia")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		List<OcorrenciaOld> ocorrenciaOldList = search.getList(OcorrenciaOld.class);
		
		for(OcorrenciaOld ocorrenciaOld : ocorrenciaOldList) {
			Ocorrencia ocorrencia = this.adapterService.toBaseNova(ocorrenciaOld);
			ocorrencias.add(ocorrencia);
		}
		
		return ocorrencias;
	}

	@Override
	public List<Ocorrencia> findOcorrenciasApp(SearchCriterios searchCriterios) throws Exception {
		return findOcorrenciasApp(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Ocorrencia> findOcorrenciasApp(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Ocorrencia> ocorrencias = new ArrayList<Ocorrencia>();
		Search<OcorrenciaOld> search = new SearchBuilder<OcorrenciaOld>("ocorrencia")
				.additionalCriterias("ds_ocorrencia = 'ERRO NO PREENCHIMENTO'")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		List<OcorrenciaOld> ocorrenciaOldList = search.getList(OcorrenciaOld.class);
		
		for(OcorrenciaOld ocorrenciaOld : ocorrenciaOldList) {
			Ocorrencia ocorrencia = this.adapterService.toBaseNova(ocorrenciaOld);
			ocorrencias.add(ocorrencia);
		}
		
		return ocorrencias;
	}
}
