package com.tivic.manager.ptc.fici;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ApresentacaoCondutorService implements IApresentacaoCondutorService{
	
	private ApresentacaoCondutorRepository ficiRepository;
	
	public ApresentacaoCondutorService() throws Exception {
		this.ficiRepository = (ApresentacaoCondutorRepository) BeansFactory.get(ApresentacaoCondutorRepository.class);
	}

	@Override
	public ApresentacaoCondutor get(int id) throws BadRequestException, Exception {
		return get(id, new CustomConnection());
	}
	
	private ApresentacaoCondutor get(int id, CustomConnection customConnection) throws BadRequestException, Exception, NoContentException {
		try {
			customConnection.initConnection(false);
			ApresentacaoCondutor fici = ficiRepository.get(id, customConnection);
			customConnection.finishConnection();
			if(fici == null) {
				throw new NoContentException("Nenhuma Apresentação de Condutor encontrada!");
			}
			return fici;
		} finally {
			customConnection.closeConnection();
		}	}

	@Override
	public ApresentacaoCondutor save(ApresentacaoCondutor obj) throws BadRequestException, Exception {
		return save(obj, new CustomConnection());
	}

	@Override
	public ApresentacaoCondutor save(ApresentacaoCondutor obj, CustomConnection customConnection)
			throws BadRequestException, Exception {
		try {
			if(obj==null) 
				throw new ValidacaoException("Erro ao salvar. Apresentação de Condutor é nula");
			customConnection.initConnection(true);
			
			if(obj.getCdApresentacaoCondutor()==0){
				ficiRepository.insert(obj);
				customConnection.finishConnection();
				return obj;
			}
			
			else {
				obj = ficiRepository.update(obj, customConnection);
				customConnection.finishConnection();
				return obj;
			}		
		}
		finally {
			customConnection.closeConnection();
		}
	}


}
