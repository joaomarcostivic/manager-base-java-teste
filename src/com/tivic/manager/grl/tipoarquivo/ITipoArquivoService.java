package com.tivic.manager.grl.tipoarquivo;

import java.util.List;

import com.tivic.manager.grl.TipoArquivo;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface ITipoArquivoService {

	public List<TipoArquivo> find() throws ValidacaoException, Exception;

}
