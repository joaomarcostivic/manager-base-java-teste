package com.tivic.manager.grl.arquivo;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ArquivoService implements IArquivoService {
	
	private IArquivoRepository arquivoRepository;
	
	public ArquivoService() throws Exception {
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
	}

	public Arquivo save(Arquivo arquivo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			save(arquivo, customConnection);
			customConnection.finishConnection();
			return arquivo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public Arquivo save(Arquivo arquivo, CustomConnection customConnection) throws Exception{
		if(arquivo==null)
			throw new ValidacaoException("O arquivo não pode ser nulo.");
		
		if(arquivo.getCdArquivo()==0)
			arquivoRepository.insert(arquivo, customConnection);
		else 
			arquivoRepository.update(arquivo, customConnection);
		return arquivo;
	}
	
	@Override
	public Arquivo get(int cdArquivo) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Arquivo arquivo = get(cdArquivo, customConnection);
			customConnection.finishConnection();
			return arquivo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Arquivo get(int cdArquivo, CustomConnection customConnection) throws Exception{
		Arquivo arquivo = arquivoRepository.get(cdArquivo, customConnection);
		
		if(arquivo == null)
			throw new NoContentException("Nenhum arquivo encontrado");
		
		return arquivo;
		
	}	
}
