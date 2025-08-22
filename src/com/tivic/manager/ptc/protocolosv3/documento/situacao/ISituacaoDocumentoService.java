package com.tivic.manager.ptc.protocolosv3.documento.situacao;

import java.util.List;

import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.protocolosv3.situacaodocumento.SituacaoDocumentoDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface ISituacaoDocumentoService {
	public List<SituacaoDocumento> getAll() throws Exception;
	public SituacaoDocumentoDTO getDocumentoByOcorrencia(int cdDocumento) throws Exception, ValidacaoException;
	public SituacaoDocumentoDTO getSituacaoDocumento(int cdDocumento) throws Exception;
	public SituacaoDocumentoDTO getSituacaoDocumento(int cdDocumento, CustomConnection customConnection) throws Exception;
	public List<SituacaoDocumento> findAllSituacaoDocumento() throws Exception;
	public List<SituacaoDocumento> findAllSituacaoDocumento(CustomConnection customConnection) throws Exception;
}
