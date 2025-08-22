package com.tivic.manager.ptc.portal.credencialestacionamento;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.documentoocorrencia.IDocumentoOcorrenciaService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class InsereDocumentoOcorrencia {
	
	private IDocumentoOcorrenciaService documentoOcorrenciaService;
	private ParametroRepositoryDAO parametroRepositoryDAO;
	
	public InsereDocumentoOcorrencia() throws Exception {
		documentoOcorrenciaService = (IDocumentoOcorrenciaService) BeansFactory.get(IDocumentoOcorrenciaService.class);
		parametroRepositoryDAO =  new ParametroRepositoryDAO();
	}
	
	private DocumentoOcorrencia setDocumentoOcorrencia(JulgamentoProtocoloEstacionamento julgamentoEstacionamento) throws Exception {
		DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrencia();
		documentoOcorrencia.setCdDocumento(julgamentoEstacionamento.getCdDocumento());
		documentoOcorrencia.setCdTipoOcorrencia(parametroRepositoryDAO.getValorAsIntWithCustomDb(julgamentoEstacionamento.getNmTipoOcorrencia()));
		documentoOcorrencia.setDtOcorrencia(new GregorianCalendar());
		documentoOcorrencia.setCdUsuario(julgamentoEstacionamento.getCdUsuario());
		documentoOcorrencia.setTxtOcorrencia(julgamentoEstacionamento.getTxtOcorrencia());
		return documentoOcorrencia;
	}
	
	public void inserir(JulgamentoProtocoloEstacionamento julgamentoEstacionamento, CustomConnection customConnection) throws Exception {
		DocumentoOcorrencia documentoOcorrencia = setDocumentoOcorrencia(julgamentoEstacionamento);
		documentoOcorrenciaService.insertWithCustomDb(documentoOcorrencia, customConnection);
	}
}
