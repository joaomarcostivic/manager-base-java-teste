package com.tivic.manager.mob.aitmovimentodocumento;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IAitMovimentoDocumentoService {
	List<AitMovimentoDocumentoDTO> findProcessos(SearchCriterios searchCriterios) throws Exception;
	List<AitMovimentoDocumentoDTO> findProcessosPublicacao(SearchCriterios searchCriterios) throws Exception;
	PagedResponse<AitMovimentoDocumentoDTO> findProtocolos(SearchCriterios searchCriterios) throws Exception;
	List<DocumentoProcesso> getMovimentosFromDTO(SearchCriterios searchCriterios);
	byte[] printRelatorioProcessos(List<DocumentoProcesso> listDocumentoProcessos, GregorianCalendar dtPublicacao, SearchCriterios searchCriterios) throws Exception;
	AitMovimentoDocumentoDTO saveResultado(AitMovimentoDocumentoDTO documento) throws Exception, ValidationException, BadRequestException;
	AitMovimentoDocumentoDTO saveResultado(AitMovimentoDocumentoDTO documento, CustomConnection customConnection) throws Exception, ValidationException, BadRequestException;
	DadosProtocoloDTO updateFici(AitMovimentoDocumentoDTO documento) throws Exception, ValidationException, BadRequestException;
	DadosProtocoloDTO updateFici(AitMovimentoDocumentoDTO documento, CustomConnection customConnection) throws Exception, ValidationException, BadRequestException;
	AitMovimentoDocumentoDTO cancelaFici(AitMovimentoDocumentoDTO documento) throws Exception, ValidationException, BadRequestException;
	AitMovimentoDocumentoDTO cancelaFici(AitMovimentoDocumentoDTO documento, CustomConnection customConnection) throws Exception, ValidationException, BadRequestException;
	public int getAitByDocumento(SearchCriterios searchCriterios) throws Exception;
	public AitMovimentoDocumento getMovimentoDocumento(SearchCriterios searchCriterios) throws Exception;
	public AitMovimentoDocumento getMovimentoDocumento(SearchCriterios searchCriterios, CustomConnection customConncetion) throws Exception;
}
