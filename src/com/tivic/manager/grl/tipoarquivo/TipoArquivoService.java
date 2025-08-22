package com.tivic.manager.grl.tipoarquivo;

import java.util.List;

import com.tivic.manager.grl.TipoArquivo;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

public class TipoArquivoService implements ITipoArquivoService {
	private ITipoArquivoRepository tipoArquivoRepository;
	
	public TipoArquivoService() throws Exception {
		tipoArquivoRepository = (ITipoArquivoRepository) BeansFactory.get(ITipoArquivoRepository.class);
	}

	@Override
	public 	List<TipoArquivo> find() throws ValidacaoException, Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		List<TipoArquivo> tipoArquivos = this.tipoArquivoRepository.find(searchCriterios);
		if(tipoArquivos == null) {
			throw new ValidacaoException ("Não há tipos de arquivo cadastrados.");
		} 
		return tipoArquivos;
	}
	
}
