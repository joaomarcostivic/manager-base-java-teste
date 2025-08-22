package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.emailsender.InfMailSender;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DefereCredencialEstacionamento {

	private DocumentoRepository documentoRepository;
	private ParametroRepositoryDAO parametroRepository;
	
	public DefereCredencialEstacionamento() throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		parametroRepository = new ParametroRepositoryDAO();
	}
	
	public void deferir(JulgamentoProtocoloEstacionamento julgamentoEstacionamento, CustomConnection customConnection) throws Exception {
		Documento documento = documentoRepository.get(julgamentoEstacionamento.getCdDocumento(), customConnection);
		documento.setCdFase(parametroRepository.getValorOfParametroAsInt("CD_FASE_JULGADO", customConnection));
		documento.setCdSituacaoDocumento(parametroRepository.getValorOfParametroAsInt("CD_SITUACAO_DOCUMENTO_DEFERIDO", customConnection));
		new InsereDocumentoOcorrencia().inserir(julgamentoEstacionamento, customConnection);
		documentoRepository.update(documento, customConnection);
		new InfMailSender(new BuscaInfEmail().buscar(documento.getCdDocumento(), "PTC_TEMPLATE_DEFERIMENTO_CREDENCIAL_DEFICIENTE")).send();
	}
}
