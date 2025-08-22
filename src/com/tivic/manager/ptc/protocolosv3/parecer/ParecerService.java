package com.tivic.manager.ptc.protocolosv3.parecer;

import java.util.List;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ParecerService implements IParecerService {

    private final IParecerRepository parecerRepository;

    public ParecerService() throws Exception {
    	parecerRepository = (IParecerRepository) BeansFactory.get(IParecerRepository.class);
    }

    @Override
    public Parecer get(int cdParecer) throws Exception {
        CustomConnection customConnection = new CustomConnection();
        try {
            customConnection.initConnection(false);
            return parecerRepository.get(cdParecer, customConnection);
        } finally {
            customConnection.closeConnection();
        }
    }

    @Override
    public List<Parecer> find(SearchCriterios searchCriterios) throws Exception {
        CustomConnection customConnection = new CustomConnection();
        try {
            customConnection.initConnection(false);
            return parecerRepository.find(searchCriterios, customConnection);
        } finally {
            customConnection.closeConnection();
        }
    }

    @Override
    public void insert(Parecer parecer) throws Exception {
        CustomConnection customConnection = new CustomConnection();
        try {
            customConnection.initConnection(true);
            parecerRepository.insert(parecer, customConnection);
            customConnection.finishConnection();
        } finally {
            customConnection.closeConnection();
        }
    }

    @Override
    public void update(Parecer parecer) throws Exception {
        CustomConnection customConnection = new CustomConnection();
        try {
            customConnection.initConnection(true);
            parecerRepository.update(parecer, customConnection);
            customConnection.finishConnection();
        } finally {
            customConnection.closeConnection();
        }
    }

    

    @Override
    public void delete(int cdParecer) throws Exception {
        CustomConnection customConnection = new CustomConnection();
        try {
            customConnection.initConnection(true);
            parecerRepository.delete(cdParecer, customConnection);
        } finally {
            customConnection.closeConnection();
        }
    }
}