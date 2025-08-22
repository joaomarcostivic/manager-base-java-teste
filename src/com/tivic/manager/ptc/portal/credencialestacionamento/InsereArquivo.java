package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class InsereArquivo {
	
	private IArquivoRepository arquivoRepository;
	
	public InsereArquivo() throws Exception {
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
	}
	
	public void inserir(Arquivo arquivo, CustomConnection customConnection) throws Exception {
		arquivoRepository.insertCodeSync(arquivo, customConnection);
	}
}
