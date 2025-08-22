package com.tivic.manager.mob.convenio;

import java.util.List;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ConvenioService implements IConvenioService {
	
	private ConvenioRepository convenioRepository;
	private IMudaConvenioDefault mudaConvenioDefault;
	
	public ConvenioService() throws Exception {
		convenioRepository = (ConvenioRepository) BeansFactory.get(ConvenioRepository.class);
		mudaConvenioDefault = (IMudaConvenioDefault) BeansFactory.get(IMudaConvenioDefault.class);
	}

	@Override
	public void insert(Convenio convenio) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(convenio, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void insert(Convenio convenio, CustomConnection customConnection) throws Exception {
		convenioRepository.insert(convenio, customConnection);
	}

	@Override
	public void update(Convenio convenio) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(convenio, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void update(Convenio convenio, CustomConnection customConnection) throws Exception {
		convenioRepository.update(convenio, customConnection);
	}

	@Override
	public Convenio get(int cdConvenio) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Convenio convenio = get(cdConvenio, customConnection);
			customConnection.finishConnection();
			return convenio;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Convenio get(int cdConvenio, CustomConnection customConnection) throws Exception {
		return convenioRepository.get(cdConvenio, customConnection);
	}

	@Override
	public List<Convenio> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Convenio> convenioList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return convenioList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Convenio> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return convenioRepository.find(searchCriterios, customConnection);
	}

	@Override
	public Convenio applyConvenioDefault(int cdConvenio) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			Convenio convenio = applyConvenioDefault(cdConvenio, customConnection);
			customConnection.finishConnection();
			return convenio;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Convenio applyConvenioDefault(int cdConvenio, CustomConnection customConnection) throws Exception {
		mudaConvenioDefault.removerDefault(customConnection);
		Convenio convenio = mudaConvenioDefault.aplicarDefault(cdConvenio, customConnection);
		return convenio;
	}
}
