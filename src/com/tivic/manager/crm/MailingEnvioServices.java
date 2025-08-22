package com.tivic.manager.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.mail.SMTPClient;

@DestinationConfig(enabled = false)
public class MailingEnvioServices {
	public static final int ST_PENDENTE = 0;
	public static final int ST_ENVIADO = 1;

	public static final String[] situacaoEnvio = {"Pendente de Envio", "Enviado"};

	public static int executeEnvio(int cdMailing, int cdPlanejamento, int cdUsuario, HashMap<String, String> parametros) {
		return executeEnvio(cdMailing, cdPlanejamento, cdUsuario, parametros, null);
	}

	public static int executeEnvio(int cdMailing, int cdPlanejamento, int cdUsuario, HashMap<String, String> parametros, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Mailing mailing = MailingDAO.get(cdMailing, connection);

			if(mailing==null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1; //mailing inexistente
			}

			if(mailing.getCdModelo()<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -2;//modelo do mailing nao indicado
			}

			//TODO: Executar modelos de documentos mais novos em vez dos antigos e depreciados 
			String modelo = "";//ModeloDocumentoServices.executeModelo(mailing.getCdModelo(), parametros, connection);

			if(modelo!=null){
				modelo.replaceAll("[\n\r\t]", "");
				MailingPlanejamento planejamento = MailingPlanejamentoDAO.get(cdPlanejamento, cdMailing, connection);
				if(planejamento==null) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -3; //planejamento inexistente
				}

				MailingConta contaEnvio = MailingContaDAO.get(planejamento.getCdContaEnvio(), connection);
				SMTPClient smtp = new SMTPClient(contaEnvio.getNmServidorSmtp(),
												 contaEnvio.getNrPortaSmtp(),
												 contaEnvio.getNmLogin(),
												 contaEnvio.getNmSenha(),
												 contaEnvio.getLgAutenticacaoSmtp()==1,
												 contaEnvio.getLgSslSmtp()==1);
				smtp.connect();
				ResultSetMap destinatarios = MailingServices.getDestinatarios(cdMailing, connection);
				GregorianCalendar dtAtual = new GregorianCalendar();
				while(destinatarios.next()){
					modelo = "<!-- Enviado pelo módulo de mailing do CRM Manager ["+System.currentTimeMillis()+"]-->"+modelo+"<!-- Enviado pelo módulo de mailing do CRM Manager ["+System.currentTimeMillis()+"]-->";
					MailingEnvio envio = new MailingEnvio(destinatarios.getInt("cd_destino"),
							cdMailing,
							cdPlanejamento,
							dtAtual,
							0,
							modelo,
							cdUsuario);
					if(destinatarios.getInt("cd_pessoa")>0){//envio para contato
						MailingEnvioDAO.insert(envio, connection);
						smtp.send(contaEnvio.getNmEmail(),
								  contaEnvio.getNmEmail(),
								  destinatarios.getString("nm_email"),
								  planejamento.getNmAssunto().replaceAll("%DATA_ATUAL%", new SimpleDateFormat("dd/MM/yyyy").format(dtAtual.getTimeInMillis())).replaceAll("%HORA_ATUAL%", new SimpleDateFormat("HH:mm").format(dtAtual.getTimeInMillis())),
								  modelo);
					}
					else if (destinatarios.getInt("cd_agendamento")>0) {
						MailingEnvioDAO.insert(envio, connection);
						PreparedStatement pstmt = connection.prepareStatement("SELECT B.nm_pessoa, B.nm_email " +
								"FROM agd_agendamento_participante A, grl_pessoa B " +
								"WHERE A.cd_participante = B.cd_pessoa " +
								"  AND A.cd_agendamento = ? " +
								"UNION " +
								"SELECT nm_convidado AS nm_pessoa, nm_email " +
								"FROM agd_agendamento_convidado " +
								"WHERE cd_agendamento = ?");
						pstmt.setInt(1, destinatarios.getInt("cd_agendamento"));
						pstmt.setInt(2, destinatarios.getInt("cd_agendamento"));
						ResultSet rs = pstmt.executeQuery();
						while (rs.next()) {
							if (rs.getString("nm_email")!=null && !rs.getString("nm_email").trim().equals("")) {
								smtp.send(contaEnvio.getNmEmail(),
										  contaEnvio.getNmEmail(),
										rs.getString("nm_email").trim(),
										planejamento.getNmAssunto().replaceAll("%DATA_ATUAL%", new SimpleDateFormat("dd/MM/yyyy").format(dtAtual.getTimeInMillis())).replaceAll("%HORA_ATUAL%", new SimpleDateFormat("HH:mm").format(dtAtual.getTimeInMillis())),
										modelo);
							}
						}
					}
					envio.setStEnvio(ST_ENVIADO);
					MailingEnvioDAO.update(envio, connection);
				}
				smtp.disconnect();

				planejamento.setDtEnvio(dtAtual);
				planejamento.setStPlanejamento(MailingPlanejamentoServices.ST_ENVIADO);
				if (MailingPlanejamentoDAO.update(planejamento, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingEnvioServices.executeEnvio: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -4;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
