package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.emailsender.InfMailSender;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class IndefereCredencialEstacionamento {
	
	private DocumentoRepository documentoRepository;
	private ParametroRepositoryDAO parametroRepository;
	
	public IndefereCredencialEstacionamento() throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		parametroRepository = new ParametroRepositoryDAO();
	}
	
	public void indeferir(JulgamentoProtocoloEstacionamento julgamentoEstacionamento, CustomConnection customConnection) throws Exception {
		Documento documento = documentoRepository.get(julgamentoEstacionamento.getCdDocumento(), customConnection);
		documento.setCdFase(parametroRepository.getValorOfParametroAsInt("CD_FASE_JULGADO", customConnection));
		documento.setCdSituacaoDocumento(parametroRepository.getValorOfParametroAsInt("CD_SITUACAO_DOCUMENTO_INDEFERIDO", customConnection));
		new InsereDocumentoOcorrencia().inserir(julgamentoEstacionamento, customConnection);
		documentoRepository.update(documento, customConnection);
		new InfMailSender(new BuscaInfEmail().buscar(julgamentoEstacionamento, "PTC_TEMPLATE_INDEFERINEMTO_CREDENCIAL_DEFICIENTE")).send();
	}
}
