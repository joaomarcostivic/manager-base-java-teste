package com.tivic.manager.msg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.prc.ProcessoArquivoServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.mail.SMTPClient;
import sol.util.Result;

public class MensagemServices {
	
	
	public static Result save(Mensagem mensagem, ArrayList<HashMap<String, Object>> destinatarios){
		return save(mensagem, destinatarios, null);
	}
	
	public static Result save(Mensagem mensagem, ArrayList<HashMap<String, Object>> destinatarios, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(mensagem==null)
				return new Result(-1, "Erro ao salvar. Mensagem é nula");
			
			int retorno;
			
			if(mensagem.getCdMensagem()==0){
				
				mensagem.setDtEnvio(new GregorianCalendar());
				mensagem.setStMensagemRemetente(1);
				
				retorno = MensagemDAO.insert(mensagem, connect);
				mensagem.setCdMensagem(retorno);
				
				if(retorno>0 && destinatarios!=null && destinatarios.size()>0) {
					for (HashMap<String, Object> i : destinatarios) {
						retorno = MensagemDestinatarioDAO.insert(new MensagemDestinatario((Integer)i.get("CD_PESSOA"), mensagem.getCdMensagem(), null, 0, 1), connect);
						
						//notificar cada destinatario que seja usuario do sistema
						if(retorno>0) {
							Usuario usuario = UsuarioServices.getByPessoa((Integer)i.get("CD_PESSOA"));
							
							if(usuario!=null) {
								Notificacao notificacao = new Notificacao(0, usuario.getCdUsuario(), "Nova mensagem recebida", Notificacao.MENSAGEM, 
										"Nova mensagem: "+mensagem.getDsAssunto(), new GregorianCalendar(), null, mensagem.getCdMensagem(), 0, null);
								retorno = NotificacaoDAO.insert(notificacao, connect);
							}
						}
						
						if (retorno<=0) 
							break;
						
					}
				}
			}
			else {
				retorno = MensagemDAO.update(mensagem, connect);
			}
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(mensagem.getCdMensagem(), (retorno<0)?"Erro ao salvar...":"Mensagem enviada com sucesso...");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result enviarMensagem(Mensagem mensagem, ArrayList<HashMap<String, Object>> destinatarios, ArrayList<HashMap<String, Object>> anexos){
		return enviarMensagem(mensagem, destinatarios, null, anexos, null);
	}
	
	public static Result enviarMensagem(Mensagem mensagem, ArrayList<HashMap<String, Object>> destinatarios, ArrayList<HashMap<String, Object>> ccs, ArrayList<HashMap<String, Object>> anexos){
		return enviarMensagem(mensagem, destinatarios, ccs, anexos, null);
	}
	
	@SuppressWarnings("unchecked")
	public static Result enviarMensagem(Mensagem mensagem, ArrayList<HashMap<String, Object>> destinatarios, ArrayList<HashMap<String, Object>> ccs, ArrayList<HashMap<String, Object>> anexos, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			LogUtils.debug("MensagemServices.enviarMensagem");
			LogUtils.createTimer("EMAIL");
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(mensagem==null)
				return new Result(-1, "Erro ao enviar. Mensagem é nula");
			
			if(destinatarios==null || destinatarios.size()==0)
				return new Result(-2, "Erro ao enviar. Nenhum destinatário para a mensagem.");
			
			int retorno;
			
			if(mensagem.getCdMensagem()==0){
				
				mensagem.setDtEnvio(new GregorianCalendar());
				mensagem.setStMensagemRemetente(1);
				mensagem.setTxtMensagem(mensagem.getTxtMensagem().replaceAll("\\</p>","\n"));
				mensagem.setTxtMensagem(mensagem.getTxtMensagem().replaceAll("\\<.*?>",""));
				mensagem.setTxtMensagem(StringEscapeUtils.unescapeHtml(mensagem.getTxtMensagem()));
				
				
				retorno = MensagemDAO.insert(mensagem, connect);
				mensagem.setCdMensagem(retorno);
				
				if(retorno>0 && destinatarios!=null && destinatarios.size()>0){
					for (HashMap<String, Object> i : destinatarios) {
						if(i.get("CD_PESSOA")!=null){
							MensagemDestinatario destinatario = new MensagemDestinatario((Integer)i.get("CD_PESSOA"), mensagem.getCdMensagem(), null, 0, 1);
							retorno = MensagemDestinatarioDAO.insert(destinatario, connect);
							
							//notificar cada destinatario que seja usuario do sistema
							if(retorno>0) {
								Usuario usuario = UsuarioServices.getByPessoa((Integer)i.get("CD_PESSOA"));
								
								//TODO: notificar
								if(usuario!=null) {
									Notificacao notificacao = new Notificacao(0, usuario.getCdUsuario(), "Nova mensagem recebida", Notificacao.MENSAGEM, 
											"Nova mensagem: "+mensagem.getDsAssunto(), new GregorianCalendar(), null, mensagem.getCdMensagem(), 0, null);
									retorno = NotificacaoDAO.insert(notificacao, connect);
								}
							}
							
							if (retorno<=0) 
								break;
						}
					}
				}
				
				if(retorno>0 && anexos!=null && anexos.size()>0){
					for (HashMap<String, Object> a : anexos) {
						if(a.get("NM_ARQUIVO")!=null){
							
							byte[] anexo = null;
							if(a.containsKey("CD_ARQUIVO") && a.containsKey("CD_PROCESSO")) {
								anexo = ProcessoArquivoServices.getBytesArquivo((int)a.get("CD_ARQUIVO"), (int)a.get("CD_PROCESSO"), connect);
								a.put("BLB_ARQUIVO", anexo);
							}
							else {
								anexo = (byte[])a.get("BLB_ARQUIVO");
							}
							
							LogUtils.debug("MensagemServices.enviarMensagem");
							LogUtils.debug("anexo: "+anexo);
							
							MensagemArquivo arquivo = new MensagemArquivo(mensagem.getCdMensagem(), (String)a.get("NM_ARQUIVO"), anexo);
							retorno = MensagemArquivoDAO.insert(arquivo, connect);
							if (retorno<=0) 
								break;
						}
					}
				}
				
				//enviar para destinatarios
				String[] to = new String[destinatarios.size()];
				for (int i = 0; i < destinatarios.size(); i++) {
					to[i] = (String)destinatarios.get(i).get("NM_EMAIL");
				}
				
				//enviar cópia para
				String[] cc = ccs==null ? null : new String[ccs.size()];
				for (int i = 0; ccs!=null && i < ccs.size(); i++) {
					cc[i] = (String)ccs.get(i).get("NM_EMAIL");
				}
				
				String nmServidorSMTP = ParametroServices.getValorOfParametroAsString("NM_SERVIDOR_SMTP", "");
				int nrPortaSMTP = ParametroServices.getValorOfParametroAsInteger("NR_PORTA_SMTP", 0);
				String nmLoginServidorSMTP = ParametroServices.getValorOfParametroAsString("NM_LOGIN_SERVIDOR_SMTP", "");
				String nmSenhaServidorSMTP = ParametroServices.getValorOfParametroAsString("NM_SENHA_SERVIDOR_SMTP", "");
				int lgAutenticacaoSMTP = ParametroServices.getValorOfParametroAsInteger("LG_AUTENTICACAO_SMTP", 0);
				int lgSSLSMTP = ParametroServices.getValorOfParametroAsInteger("LG_SSL_SMTP", 0);
				String nmEmailRemetenteSMTP = ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", "");
				String nmEmailRespostaSMTP = ParametroServices.getValorOfParametroAsString("NM_EMAIL_RESPOSTA_SMTP", "");
				String nmTransportSMTP = ParametroServices.getValorOfParametroAsString("NM_TRANSPORT_SMTP", "smtp");
				int lgDebugEmail = ParametroServices.getValorOfParametroAsInteger("LG_DEBUG_SMTP", 0);
								
				if(nmServidorSMTP==null || nmServidorSMTP.equals(""))
					return new Result(-1, "Servidor SMTP não configurado.");
				
				if(nrPortaSMTP==0)
					return new Result(-1, "Porta do servidor SMTP não configurado.");
				
				if(nmLoginServidorSMTP==null || nmLoginServidorSMTP.equals(""))
					return new Result(-1, "Login do servidor SMTP não configurado.");
				
				if(nmSenhaServidorSMTP==null || nmSenhaServidorSMTP.equals(""))
					return new Result(-1, "Senha do servidor SMTP não configurado.");
				
				if(nmEmailRemetenteSMTP==null || nmEmailRemetenteSMTP.equals(""))
					return new Result(-1, "Email do remetente não configurado.");
				
				if(lgDebugEmail==1) {
					LogUtils.debug("nmServidorSMTP: "+nmServidorSMTP +
								   "\nnrPortaSMTP: "+nrPortaSMTP +
								   "\nnmLoginServidorSMTP: "+nmLoginServidorSMTP+
								   "\nnmSenhaServidorSMTP: "+ nmSenhaServidorSMTP+
								   "\nlgAutenticacaoSMTP: "+lgAutenticacaoSMTP+
								   "\nlgSSLSMTP: "+lgSSLSMTP+
								   "\nnmTransportSMTP: "+nmTransportSMTP+
								   "\nlgDebugEmail: "+lgDebugEmail);
				}
				
				SMTPClient cliente = new SMTPClient(nmServidorSMTP, nrPortaSMTP, nmLoginServidorSMTP, nmSenhaServidorSMTP, 
						lgAutenticacaoSMTP==1, lgSSLSMTP==1, nmTransportSMTP, lgDebugEmail==1);
				
				if(cliente.connect()) {
				
					HashMap<String, Object>[] attachments = new HashMap[0];
					if(anexos!=null && anexos.size()>0) {
						attachments = new HashMap[anexos.size()];
						
						for (int i = 0; i < anexos.size(); i++) {
							HashMap<String, Object> register = (HashMap<String, Object>)anexos.get(i);
							register.put("BYTES", register.get("BLB_ARQUIVO"));
							register.put("NAME", register.get("NM_ARQUIVO"));
							attachments[i] = register;
						}
					}
					
					LogUtils.debug("nmEmailRemetenteSMTP: "+nmEmailRemetenteSMTP);
					LogUtils.debug("new String[] {nmEmailRespostaSMTP}: "+new String[] {nmEmailRespostaSMTP});
					LogUtils.debug("to: "+to);
					LogUtils.debug("cc: "+cc);
					LogUtils.debug("mensagem.getDsAssunto(): "+mensagem.getDsAssunto());
					LogUtils.debug("mensagem: "+(mensagem.getTxtMensagem()!=null ? mensagem.getTxtMensagem().replaceAll("\n", "<br/>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;") : ""));
					LogUtils.debug("attachments: "+attachments);
					
					cliente.send(nmEmailRemetenteSMTP, new String[] {nmEmailRespostaSMTP}, to, cc, null, 
							mensagem.getDsAssunto(), (mensagem.getTxtMensagem()!=null ? mensagem.getTxtMensagem().replaceAll("\n", "<br/>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;") : ""), 
							null, null, attachments);
				}
				
				cliente.disconnect();
				
				
			}
			else {
				retorno = MensagemDAO.update(mensagem, connect);
			}
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			LogUtils.logTimer("EMAIL", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.destroyTimer("EMAIL");
			
			return new Result(mensagem.getCdMensagem(), (retorno<0)?"Erro ao salvar...":"Mensagem enviada com sucesso...");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getList(int cdUsuarioOrigem, int cdUsuarioDestino, 
			String txtSearch, int nrRegistroInicial, int qtRegistros) {
		return getList(cdUsuarioOrigem, cdUsuarioDestino, txtSearch, nrRegistroInicial, qtRegistros, null);
	}
	
	public static ResultSetMap getListMensagensEnviadas(int cdUsuario, 
			String txtSearch, int nrRegistroInicial, int qtRegistros) {
		return getList(cdUsuario, 0, txtSearch, nrRegistroInicial, qtRegistros, null);
	}
	
	public static ResultSetMap getListMensagensRecebidas(int cdUsuario, 
			String txtSearch, int nrRegistroInicial, int qtRegistros) {
		return getList(0, cdUsuario, txtSearch, nrRegistroInicial, qtRegistros, null);
	}
	
	public static ResultSetMap getList(int cdUsuarioOrigem, int cdPessoaDestino, 
			String txtSearch, int nrRegistroInicial, int qtRegistros, Connection connect) {			
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String from = "FROM msg_mensagem A " +
						"LEFT OUTER JOIN seg_usuario B ON (A.cd_usuario_origem = B.cd_usuario) " +
						"LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = B.cd_pessoa) " +
						((cdPessoaDestino>0)?" JOIN msg_mensagem_destinatario C ON (A.cd_mensagem = C.cd_mensagem) ":"") +
						"WHERE 1=1 "+
						((cdPessoaDestino>0)?" AND C.cd_pessoa = "+cdPessoaDestino+" AND C.st_mensagem_destinatario = 1":"")+
						((cdUsuarioOrigem>0)?" AND A.cd_usuario_origem = "+cdUsuarioOrigem+" AND A.st_mensagem_remetente = 1":"")+
						((txtSearch!=null && !txtSearch.equals(""))?" AND A.txt_mensagem like '%"+txtSearch+"%'":"");
			
			String stat = " ORDER BY A.dt_envio DESC, A.cd_mensagem DESC ";

			String cmpsCaixaEntrada = ", C.dt_leitura, C.lg_confirmada ";
			String sql = "SELECT "+
				(Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
				 (((qtRegistros>0)?" FIRST "+qtRegistros:"") + ((nrRegistroInicial>0)?" SKIP "+nrRegistroInicial:"")) : "")+
				" A.*, D.cd_pessoa as cd_pessoa_origem, D.nm_pessoa as nm_usuario_origem "+((cdPessoaDestino>0)?cmpsCaixaEntrada:"")+from+stat;
			
			pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(qtRegistros>0 || nrRegistroInicial>0){
				pstmt = connect.prepareStatement("SELECT count(*) as QT_TOTAL "+from);
				
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()){
					int nrTotal = rs.getInt("QT_TOTAL");
					
					rsm.setOffset(nrRegistroInicial);
					rsm.setLimit(qtRegistros);
					rsm.setTotal(nrTotal);
					rsm.setHasMore((nrTotal-nrRegistroInicial-qtRegistros)>0);
				}
			}
			
			while(rsm.next()) {
				HashMap<String, Object> register = rsm.getRegister();
				pstmt = connect.prepareStatement("SELECT A.*, C.nm_pessoa as nm_usuario, C.nm_pessoa " +
						"FROM msg_mensagem_destinatario A " +
						"LEFT OUTER JOIN seg_usuario B ON (A.cd_pessoa = B.cd_usuario) " +
						"LEFT OUTER JOIN grl_pessoa C ON (C.cd_pessoa = A.cd_pessoa) " +
						"WHERE cd_mensagem = "+rsm.getInt("CD_MENSAGEM") +
						" ORDER BY nm_pessoa");
				register.put("DESTINATARIOS", new ResultSetMap(pstmt.executeQuery()));
				rsm.updateRegister(register);
			}
			
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemServices.getList: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemServices.getList: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<String, Object> load(int cdMensagem) {
		return load(cdMensagem, null);
	}
	
	public static HashMap<String, Object> load(int cdMensagem, Connection connect) {			
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			pstmt = connect.prepareStatement("SELECT A.*, D.nm_pessoa as nm_usuario_origem FROM msg_mensagem A " +
					"LEFT OUTER JOIN seg_usuario B ON (A.cd_usuario_origem = B.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = B.cd_usuario) " +
					"WHERE cd_mensagem = ?");
			pstmt.setInt(1, cdMensagem);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			while(rsm.next()) {
				register = rsm.getRegister();
				pstmt = connect.prepareStatement("SELECT A.*, C.nm_pessoa as nm_usuario, C.nm_pessoa " +
						"FROM msg_mensagem_destinatario A " +
						"LEFT OUTER JOIN seg_usuario B ON (A.cd_pessoa = B.cd_usuario) " +
						"LEFT OUTER JOIN grl_pessoa C ON (C.cd_pessoa = A.cd_pessoa) " +
						"WHERE cd_mensagem = "+rsm.getInt("CD_MENSAGEM") +
						" ORDER BY nm_pessoa");
				register.put("DESTINATARIOS", new ResultSetMap(pstmt.executeQuery()));
				rsm.updateRegister(register);
			}
			
			return register;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemServices.load: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemServices.load: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/*
	 * Estatistiscas de mensagens recebidas
	 */
	public static HashMap<String, Object> getStatsRecebidas(int cdPessoa){
		return getStatsRecebidas(cdPessoa, null);
	}
	
	public static HashMap<String, Object> getStatsRecebidas(int cdPessoa, Connection connect) {			
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String, Object> STATS = new HashMap<String, Object>();
			
			pstmt = connect.prepareStatement("SELECT count(*) as QT_NAO_LIDAS " +
					" FROM msg_mensagem_destinatario " +
					" WHERE dt_leitura is null " +
					"   AND cd_pessoa = "+cdPessoa +
					"   AND st_mensagem_destinatario = 1");
			ResultSet rs = pstmt.executeQuery();
			
			STATS.put("QT_NAO_LIDAS", rs.next()?rs.getInt("QT_NAO_LIDAS"):0);
			
			pstmt = connect.prepareStatement("SELECT count(*) as QT_TOTAL " +
					" FROM msg_mensagem_destinatario " +
					" WHERE cd_pessoa = "+cdPessoa +
					"   AND st_mensagem_destinatario = 1");
			
			rs = pstmt.executeQuery();
			STATS.put("QT_TOTAL", rs.next()?rs.getInt("QT_TOTAL"):0);
			
			return STATS;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MensagemServices.getStatsRecebidas: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MensagemServices.getStatsRecebidas: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdMensagem){
		return remove(cdMensagem, true, null);
	}
	
	public static Result remove(int cdMensagem, boolean cascade){
		return remove(cdMensagem, cascade, null);
	}
	
	public static Result remove(int cdMensagem, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM msg_notificacao " +
						" WHERE cd_mensagem = ?");
				pstmt.setInt(1, cdMensagem);
				
				retorno = pstmt.executeUpdate();
				
				if(retorno>0) {
					pstmt = connect.prepareStatement("DELETE FROM msg_mensagem_destinatario " +
							" WHERE cd_mensagem = ?");
					pstmt.setInt(1, cdMensagem);
					pstmt.executeUpdate();
					
					retorno = 1;
				}
				
				if(retorno>0) {
					pstmt = connect.prepareStatement("DELETE FROM msg_mensagem " +
							" WHERE cd_mensagem_origem = ?");
					pstmt.setInt(1, cdMensagem);
					pstmt.executeUpdate();
					
					retorno = 1;
				}
				
				if(retorno>=0)
					retorno = MensagemDAO.delete(cdMensagem, connect);
			}
			
			if(!cascade)
				retorno = inativar(cdMensagem, 0, connect).getCode();
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta Mensagem está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Mensagem excluída com sucesso!");
		}
		catch(Exception e){
			System.out.println("Erro! MensagemServices.deleteMensagem: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir mensagem!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result inativar(int cdMensagem, int cdPessoa){
		return inativar(cdMensagem, cdPessoa, null);
	}
	
	public static Result inativar(int cdMensagem, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno;
			
			Mensagem mensagem = MensagemDAO.get(cdMensagem, connect);
			
			if(mensagem==null)
				return new Result(-1, "Erro. Mensagem não existe.");
			
			if(cdPessoa>0) {
				PreparedStatement pstmt = connect.prepareStatement("UPDATE msg_mensagem_destinatario SET st_mensagem_destinatario = 0 " +
						"WHERE cd_mensagem = ? AND cd_pessoa = ?");
				pstmt.setInt(1, cdMensagem);
				pstmt.setInt(2, cdPessoa);
				
				pstmt.executeUpdate();
				retorno = 1;
			}
			else {
				PreparedStatement pstmt = connect.prepareStatement("UPDATE msg_mensagem SET st_mensagem_remetente = 0 WHERE cd_mensagem = ?");
				pstmt.setInt(1, cdMensagem);
				
				pstmt.executeUpdate();
				retorno = 1;
			}
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao inativar...":"Inativado com sucesso...");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result updateStatus(int cdMensagem, int cdPessoa, boolean lgLido, boolean lgConfirmada){
		return updateStatus(cdMensagem, cdPessoa, lgLido, lgConfirmada, null);
	}
	
	public static Result updateStatus(int cdMensagem, int cdPessoa, boolean lgLido, boolean lgConfirmada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno;
			
			Mensagem mensagem = MensagemDAO.get(cdMensagem, connect);
			
			if(mensagem==null)
				return new Result(-1, "Erro ao salvar. Mensagem é nula");
			
			MensagemDestinatario m = new MensagemDestinatario(cdPessoa, cdMensagem, lgLido?new GregorianCalendar():null, lgConfirmada?1:0, 1);
			
			retorno = MensagemDestinatarioDAO.update(m, connect);

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, (retorno<0)?"Erro ao alterar estado...":"Status alterado...", "MENSAGEMDESTINATARIO", m);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}