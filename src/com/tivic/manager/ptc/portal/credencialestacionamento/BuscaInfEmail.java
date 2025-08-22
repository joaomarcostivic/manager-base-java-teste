package com.tivic.manager.ptc.portal.credencialestacionamento;

import java.text.SimpleDateFormat;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class BuscaInfEmail {
	
	private ParametroRepositoryDAO parametroRepositoryDAO;
	
	public BuscaInfEmail() throws Exception {
		parametroRepositoryDAO = new ParametroRepositoryDAO();
	}
	
	public SendEmailProtocolo buscar(int cdDocumento, String nmTemplate) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);	
			SendEmailProtocolo sendEmail = getInfoEmail(cdDocumento, customConnection);
			setConf(sendEmail, nmTemplate, customConnection);
			setBody(sendEmail, null, nmTemplate, customConnection);
			customConnection.finishConnection();
			return sendEmail;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public SendEmailProtocolo buscar(JulgamentoProtocoloEstacionamento julgamentoEstacionamento, String nmTemplate) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);	
			SendEmailProtocolo sendEmail = getInfoEmail(julgamentoEstacionamento.getCdDocumento() , customConnection);
			setConf(sendEmail, nmTemplate, customConnection);
			setBody(sendEmail, julgamentoEstacionamento, nmTemplate, customConnection);
			customConnection.finishConnection();
			return sendEmail;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private SendEmailProtocolo getInfoEmail(int cdDocumento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_documento", cdDocumento);
		Search<SendEmailProtocolo> search = new SearchBuilder<SendEmailProtocolo>("ptc_documento A")
				.fields("D.dt_ocorrencia, A.dt_protocolo, A.nr_documento as nr_protocolo, C.nm_pessoa, C.nm_email")
				.addJoinTable("JOIN ptc_documento_pessoa B ON (A.cd_documento = b.cd_documento)")
				.addJoinTable("JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa)")
				.addJoinTable("LEFT JOIN ptc_documento_ocorrencia D ON (A.cd_documento = D.cd_documento)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(SendEmailProtocolo.class).get(0);
	}
	
	private void setConf(SendEmailProtocolo sendEmail, String nmTemplate, CustomConnection customConnection) throws Exception {
		sendEmail = new ConfEmailProtocoloBuilder(sendEmail)
				.setProvedorHost(validaEmailConf("EMAIL_PROVEDOR_HOST"))
				.setProvedorPort(validaEmailConfAsInt("EMAIL_PROVEDOR_PORT"))
				.setEmailRemetente(validaEmailConf("EMAIL_REMETENTE"))
				.setSenhaRemetente(validaEmailConf("SENHA_REMETENTE"))
				.setEmailHeader(parametroRepositoryDAO.getValorOfParametroAsString("HEADER_EMAIL", customConnection))
				.setNmTemplate(nmTemplate)
				.setNmAssunto("Credencial de estacionamento")
				.build();
	}
	
	private void setBody(SendEmailProtocolo sendEmail, JulgamentoProtocoloEstacionamento julgamentoEstacionamento, String nmTemplate, CustomConnection customConnection) throws Exception {
		String msgEmail = setMsgEmail(nmTemplate, julgamentoEstacionamento, customConnection);
		sendEmail.setBody(new BodyEmailProtocoloBuilder()
				.setParams("nome", sendEmail.getNmPessoa())
				.setParams("protocolo", sendEmail.getNrProtocolo())
				.setParams("mensagem", msgEmail)
				.setParams("data_registro", new SimpleDateFormat("dd/MM/yyyy")
												.format(sendEmail.getDtOcorrencia() != null 
													  ? sendEmail.getDtOcorrencia().getTime()
													  : sendEmail.getDtProtocolo().getTime()))
				.setParams("url_sistema", ManagerConf.getInstance().get("URL_SISTEMA"))
				.setParams("email_header", sendEmail.getEmailHeader())
				.setParams("email_footer", parametroRepositoryDAO.getValorOfParametroAsString("FOOTER_EMAIL", customConnection))
				.build());
	}
	
	private String setMsgEmail(String nmTemplate, JulgamentoProtocoloEstacionamento julgamentoEstacionamento, CustomConnection customConnection) throws Exception {
		int lgEnviarMotivo = parametroRepositoryDAO.getValorOfParametroAsInt("LG_ENVIAR_MOTIVO_SOLICITACAO_EMAIL", customConnection);
		if (lgEnviarMotivo == -1) throw new ValidacaoException("O parâmetro LG_ENVIAR_MOTIVO_SOLICITACAO_EMAIL não foi configurado.");
		String motivo = julgamentoEstacionamento != null ? julgamentoEstacionamento.getTxtOcorrencia() : "";
		String mensagem = lgEnviarMotivo == 1 ? new BuscaMensagemEmail().buscar(nmTemplate) + "<br>" + motivo : new BuscaMensagemEmail().buscar(nmTemplate);
		return mensagem;
	}
	
	private String validaEmailConf(String key) throws Exception {
		String conf = ManagerConf.getInstance().get(key);
		if (conf == null || conf.isEmpty() || conf.equals("0")) 
			throw new Exception("Configuração " + key + " de e-mail não definida no sistema.");
		return conf;
	}
	
	private int validaEmailConfAsInt(String key) throws Exception {
		int conf = ManagerConf.getInstance().getAsInteger(key);
		if (conf == 0) 
			throw new Exception("Configuração " + key + " de e-mail não definida no sistema.");
		return conf;
	}
}
