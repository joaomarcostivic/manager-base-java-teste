package com.tivic.manager.mob.publicacao.edital;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.publicacao.dto.ArquivoEditalDTO;
import com.tivic.manager.mob.publicacao.dto.ArquivoEditalPortalDTO;
import com.tivic.manager.ptc.protocolosv3.protocoloarquivos.ArquivoDownload;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IEditalPublicacaoService {

	public ArquivoEditalDTO insert(ArquivoEditalDTO arquivoEditalDTO) throws Exception, ValidacaoException;
	public ArquivoEditalDTO insert(ArquivoEditalDTO arquivoEditalDTO, CustomConnection customConnection) throws Exception, ValidacaoException;
	public ArquivoDownload buscarArquivoEdital(int cdLoteImpressao) throws Exception;
	public Search<Arquivo> buscarArquivoEdital(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public ArquivoDownload download(int cdArquivo) throws Exception, ValidacaoException;
	public PagedResponse<ArquivoEditalPortalDTO> find(SearchCriterios searchCriterios) throws Exception, ValidacaoException;
	public Search<ArquivoEditalPortalDTO> buscarArquivosEdital(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

}
