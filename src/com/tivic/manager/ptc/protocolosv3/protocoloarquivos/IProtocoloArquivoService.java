package com.tivic.manager.ptc.protocolosv3.protocoloarquivos;

import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IProtocoloArquivoService {
	public ArquivoDTO insert(ArquivoDTO insertArquivo) throws Exception, ValidacaoException;
	public List<ArquivoSimples> getArquivos(SearchCriterios searchCriterios) throws Exception, ValidacaoException;
	public ArquivoDownload download(SearchCriterios searchCriterios) throws Exception, ValidacaoException;
	public ArquivoDownload downloadArquivoCredencial(SearchCriterios searchCriterios) throws Exception;
	public ArquivoDownload downloadArquivoCredencial(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public void delete(Integer cdArquivo, Integer cdDocumento) throws Exception, ValidacaoException;
}
