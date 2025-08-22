package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.TipoOcorrencia;
import com.tivic.manager.grl.TipoOcorrenciaDAO;
import com.tivic.manager.msg.Mensagem;
import com.tivic.manager.msg.MensagemServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class DocumentoOcorrenciaServices {
	
	// VISIBILIDADE
	public static final int TP_VISIBILIDADE_PUBLICO = 0;
	public static final int TP_VISIBILIDADE_PRIVADO = 1;
	
	public static final int TP_DESTINATARIO_PARTES = 0;
	public static final int TP_DESTINATARIO_SOLICITANTE = 1;
	
	public static Result save(DocumentoOcorrencia ocorrencia){
		return save(ocorrencia, null);
	}
	
	public static Result save(DocumentoOcorrencia ocorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(ocorrencia==null)
				return new Result(-1, "Erro ao salvar. Ocorrência é nula");
			
			int retorno;
			DocumentoOcorrencia d = DocumentoOcorrenciaDAO.get(ocorrencia.getCdDocumento(), ocorrencia.getCdOcorrencia(), ocorrencia.getCdTipoOcorrencia(), connect);
			if(d==null){
				GregorianCalendar dtOcorrencia = ocorrencia.getDtOcorrencia();
				GregorianCalendar hoje = new GregorianCalendar();
				dtOcorrencia.set(Calendar.HOUR, hoje.get(Calendar.HOUR));
				dtOcorrencia.set(Calendar.MINUTE, hoje.get(Calendar.MINUTE));
				dtOcorrencia.set(Calendar.SECOND, hoje.get(Calendar.SECOND));
				ocorrencia.setDtOcorrencia(dtOcorrencia);
				retorno = DocumentoOcorrenciaDAO.insert(ocorrencia, connect);
				ocorrencia.setCdOcorrencia(retorno);
			}
			else {
				retorno = DocumentoOcorrenciaDAO.update(ocorrencia, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			/**
			 * ENVIO DE NOTIFICAÇÃO POR EMAIL
			 * Se o parametro lgNotificacaoAutomaticaEmail é true e o tipo de ocorrencia está indicando que permite envio automático (TipoOcorrencia.lgEmail == true); 
			 */
			boolean lgNotificacaoAutomaticaEmail = ParametroServices.getValorOfParametroAsInteger("LG_NOTIFICACAO_AUTOMATICA_EMAIL", 0, 0, connect)==1;
			TipoOcorrencia tipo = TipoOcorrenciaDAO.get(ocorrencia.getCdTipoOcorrencia(), connect);
			Result rEmail = null;

			if(lgNotificacaoAutomaticaEmail && tipo.getLgEmail()==1) {
				rEmail = enviarNotificacaoOcorrencia(ocorrencia, connect);
			}
			
			Result r = new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DOCUMENTOOCORRENCIA", ocorrencia);
			r.addObject("RESULT_NOTIFICACAO", rEmail);
			
			return r;
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
	
	private static Result enviarNotificacaoOcorrencia(DocumentoOcorrencia d, Connection connect) {
		try {
			int tpDestinatarioNotificacaoEmailOcorrencia = ParametroServices.getValorOfParametroAsInteger("TP_DESTINATARIO_NOTIFICACAO_EMAIL_OCORRENCIA", -1, 0, connect);
			Result rEmail = null;
			
			if(d==null || d.getCdDocumento()==0 || d.getCdOcorrencia()==0) {
				rEmail = new Result(-6, "Não é possível enviar email automático. Ocorrência não encontrada.");
			}
			else {
				Documento documento = DocumentoDAO.get(d.getCdDocumento(), connect);
				
				Mensagem mensagem = new Mensagem();
				mensagem.setCdUsuarioOrigem(documento.getCdUsuario());
				//mensagem.setCdDocumento(documento.getCdDocumento());
				mensagem.setDsAssunto("[JurisManager] Nova ocorrência no documento Nº " + documento.getNrDocumento());
				
				Result rTextoEmail = getTxtEmailOcorrencia(d, connect);
				
				if(rTextoEmail.getCode()<=0) {
					System.out.println("Não é possível enviar email automático. Parâmetro do modelo de email não configurado.");
					rEmail = new Result(-6, "Não é possível enviar email automático. Parâmetro do modelo de email não configurado.");
				}
				else {
								
					String source = new String((byte[])rTextoEmail.getObjects().get("BLB_DOCUMENTO"));
					
					ResultSetMap rsmDestinatarios = new ResultSetMap();
					if(tpDestinatarioNotificacaoEmailOcorrencia == TP_DESTINATARIO_SOLICITANTE)
						rsmDestinatarios = DocumentoServices.getAllSolicitantesOf(d.getCdDocumento(), connect);
					else
						rsmDestinatarios = DocumentoPessoaServices.getAllByDocumentoCompleto(d.getCdDocumento(), connect);
					
					ArrayList<HashMap<String, Object>> destinatarios = new ArrayList<HashMap<String,Object>>();
										
					for (HashMap<String, Object> reg : rsmDestinatarios.getLines()) {
						
						if(reg.get("NM_EMAIL")!=null && !reg.get("NM_EMAIL").equals("")) {
							HashMap<String,Object> dest = new HashMap<String, Object>();
							dest.put("CD_PESSOA", reg.get("CD_PESSOA"));
							dest.put("NM_EMAIL", reg.get("NM_EMAIL"));
							
							destinatarios.add(dest);
						}
					}
					
					if(destinatarios.isEmpty()) { 
						//NÃO TROCAR CÓDIGO DE ERRO
						rEmail = new Result(-5, "Não é possível enviar mensagem. Destinatários não encontrados.");
					}
					else {
						int indexInicial = source.indexOf("<p>");
						int indexFinal = source.indexOf("</p></TextFlow", indexInicial) + 4;
						source = source.substring(indexInicial, indexFinal);
						mensagem.setTxtMensagem(source);
										
						Result r = MensagemServices.enviarMensagem(mensagem, destinatarios, null, null, connect);
						if(r.getCode()<=0) {
							rEmail = new Result(-4, "Erro ao enviar mensagem.");
						}
						else
							rEmail = new Result(1, "Email enviado com sucesso");
					}
				}
			}
			
			if(rEmail.getCode()<=0)
				System.out.println(rEmail);
			
			return rEmail;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-7, "Erro desconhecido ao enviar mensagem.");
		}
	}

	public static Result getTxtEmailOcorrencia(DocumentoOcorrencia d, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			/*
			 * código do modelo para notificação de andamentos via email.
			 */
			int cdModelo = ParametroServices.getValorOfParametroAsInteger("CD_MODELO_EMAIL_OCORRENCIA", 0);
			
			if(cdModelo<=0)
				return new Result(-1, "Parametro do modelo de email para notificação de ocorrência não encontrado");
			else {
				//Executa modelo com ou sem processo
				Result r = DocumentoServices.executeModeloWeb(cdModelo, d.getCdDocumento(), 0, 0, d, connect);
						
				if(r.getCode()<=0)
					return new Result(-2, "Erro ao executar modelo.");
				
				byte[] blbDocumento = (byte[])r.getObjects().get("BLB_DOCUMENTO");
				
				if(blbDocumento==null)
					return new Result(-2, "Text de modelo nao encontrado");
				
				return new Result(1, "Executado com sucesso", "BLB_DOCUMENTO", blbDocumento);
			}
		}
		catch(Exception e){
			System.out.println("Erro!DocumentoOcorrenciaServices.getTxtEmailOcorrencia: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir Item!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia){
		return remove(cdDocumento, cdOcorrencia, cdTipoOcorrencia, false, null);
	}
	
	public static Result remove(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia, boolean cascade){
		return remove(cdDocumento, cdOcorrencia, cdTipoOcorrencia, cascade, null);
	}
	
	public static Result remove(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
				
			if(!cascade || retorno>0)
				retorno = DocumentoOcorrenciaDAO.delete(cdDocumento, cdOcorrencia, cdTipoOcorrencia, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este ocorrencia está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Ocorrência excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir ocorrência!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeAll(int cdDocumento) {
		return removeAll(cdDocumento, null);
	}

	public static Result removeAll(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		PreparedStatement pstmt;

		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			pstmt = connect.prepareStatement(" DELETE FROM ptc_documento_ocorrencia"
										   + " WHERE cd_documento=?");
			pstmt.setInt(1, cdDocumento);
			
			int retorno = pstmt.executeUpdate();
			if(retorno<0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(retorno, "Erro ao excluir ocorrências.");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Ocorrências excluídas com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_ocorrencia ORDER BY nm_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return DocumentoOcorrenciaDAO.find(criterios, connect);
		//return Search.find("SELECT * FROM PRC_JUIZO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}


	public static ResultSetMap getAllByDocumento(int cdDocumento) {
		return getAllByDocumento(cdDocumento, null);
	}
	
	public static ResultSetMap getAllByDocumento(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql =
				" SELECT A.*, B.nm_tipo_ocorrencia, C.nm_login " +
				" FROM ptc_documento_ocorrencia A " +
				" JOIN grl_tipo_ocorrencia B ON (A.cd_tipo_ocorrencia = B.cd_tipo_ocorrencia)" +
				" LEFT OUTER JOIN seg_usuario C ON (A.cd_usuario = C.cd_usuario)" +
				" WHERE A.cd_documento = "+cdDocumento+ " ORDER BY A.dt_ocorrencia DESC";
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Ultima ocorrencia do documento
	 * @param cdDocumento
	 * @return
	 */
	public static DocumentoOcorrencia getUltimaOcorrencia(int cdDocumento) {
		return getUltimaOcorrencia(cdDocumento, null);
	}

	public static DocumentoOcorrencia getUltimaOcorrencia(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PreparedStatement pstmt = connect.prepareStatement(" SELECT * FROM ptc_documento_ocorrencia" +
																" WHERE dt_ocorrencia = (SELECT max(dt_ocorrencia)" +
																							" FROM ptc_documento_ocorrencia " +
																							" WHERE cd_documento = ?) " +
																" AND cd_documento = ?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdDocumento);

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return !rsm.next() ? null : new DocumentoOcorrencia(
					rsm.getInt("cd_documento"),
					rsm.getInt("cd_tipo_ocorrencia"),
					rsm.getInt("cd_ocorrencia"),
					rsm.getInt("cd_usuario", 0),
					rsm.getGregorianCalendar("dt_ocorrencia"),
					rsm.getString("txt_ocorrencia"),
					rsm.getInt("tp_visibilidade", 0),
					rsm.getInt("tp_consistencia"));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
