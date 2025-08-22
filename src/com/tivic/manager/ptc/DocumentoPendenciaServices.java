package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.LogUtils;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class DocumentoPendenciaServices {
	public static Result save(DocumentoPendencia pendencia){
		return save(pendencia, null);
	}
	
	public static Result save(DocumentoPendencia pendencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(pendencia==null)
				return new Result(-1, "Erro ao salvar. Pendência é nula");
			
//			// Verifica se já existe pendência do mesmo tipo
//			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_documento_pendencia A " +
//					                                "WHERE A.cd_documento = " +pendencia.getCdDocumento()+
//					   								"  AND A.cd_tipo_pendencia ="+pendencia.getCdTipoPendencia()).executeQuery();
//			if(rs.next())
//				return new Result(-1, "Já existe uma pendência deste tipo para o documento.");
			
			// Verifica se já está em tramitação
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_documento_tramitacao A " +
			                                "WHERE A.cd_documento = " +pendencia.getCdDocumento()+
			   								"  AND dt_recebimento IS NULL").executeQuery();
			if(rs.next())
				return new Result(-1, "Documento em tramitação! Não é permitido alterar!");
			
			// Verifica usuário
			if(pendencia.getCdUsuarioRegistro() <= 0)
				return new Result(-1, "Informação do usuário não encontrada!");
			
			int retorno = 0;
			DocumentoPendencia p = DocumentoPendenciaDAO.get(pendencia.getCdDocumento(), pendencia.getCdTipoPendencia(), connect);
			if(p==null)
				retorno = DocumentoPendenciaDAO.insert(pendencia, connect);
			else
				retorno = DocumentoPendenciaDAO.update(pendencia, connect);
			
			// PTC WORKFLOW ====================
			ResultSetMap rsmRegras = null;
			boolean lgPtcWorkflow = ParametroServices.getValorOfParametroAsInteger("LG_PTC_WORKFLOW", 0, 0, connect)==1;
			if(lgPtcWorkflow) {
				rsmRegras = WorkflowRegraServices.getAllByNmEntidade("PENDENCIA", connect);
				
				System.out.println(rsmRegras);
				
				ResultSetMap rsmAux = new ResultSetMap();
				while(rsmRegras.next()) {
					if(rsmRegras.getInt("tp_gatilho")==WorkflowGatilhoServices.TP_GATILHO_LANCAMENTO_PENDENCIA)
						rsmAux.addRegister(rsmRegras.getRegister());
				}
				
				System.out.println(rsmAux);
				System.out.println(pendencia);
				
				retorno = aplicarRegrasPtcWorkflow(rsmAux, pendencia, connect).getCode();
			}
			// =================================
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DOCUMENTOPENDENCIA", pendencia);
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
	
	public static Result baixar(int cdDocumento, int cdTipoPendencia, int cdUsuario){
		return baixar(cdDocumento, cdTipoPendencia, cdUsuario, null, null);
	}
	
	public static Result baixar(int cdDocumento, int cdTipoPendencia, int cdUsuario, String txtBaixa){
		return baixar(cdDocumento, cdTipoPendencia, cdUsuario, txtBaixa, null);
	}
	
	public static Result baixar(int cdDocumento, int cdTipoPendencia, int cdUsuario, String txtBaixa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
				
//			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento_pendencia " +
//																"SET dt_baixa=?, cd_usuario_baixa=?, txt_baixa=? " +
//																"WHERE cd_documento="+cdDocumento+
//																		" AND cd_tipo_pendencia="+cdTipoPendencia);
//			
//			pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar().getTimeInMillis()));
//			pstmt.setInt(2, cdUsuario);
//			pstmt.setString(3, txtBaixa);
			
			LogUtils.debug("DocumentoPendenciaServices.baixar");
			LogUtils.debug("cdDocumento: "+cdDocumento);
			LogUtils.debug("cdTipoPendencia: "+cdTipoPendencia);
			
			DocumentoPendencia pendencia = DocumentoPendenciaDAO.get(cdDocumento, cdTipoPendencia, connect);
			pendencia.setCdUsuarioBaixa(cdUsuario);
			pendencia.setDtBaixa(new GregorianCalendar());
			pendencia.setTxtBaixa(txtBaixa);
			
			int retorno = DocumentoPendenciaDAO.update(pendencia, connect);//pstmt.executeUpdate();
			
			
			LogUtils.debug("result: "+retorno);
			
//			if (isConnectionNull && result>0)
//				connect.commit();
			
			// PTC WORKFLOW ====================
			ResultSetMap rsmRegras = null;
			boolean lgPtcWorkflow = ParametroServices.getValorOfParametroAsInteger("LG_PTC_WORKFLOW", 0, 0, connect)==1;
			if(lgPtcWorkflow) {
				pendencia = DocumentoPendenciaDAO.get(cdDocumento, cdTipoPendencia, connect);
				
				rsmRegras = WorkflowRegraServices.getAllByNmEntidade("PENDENCIA", connect);
				
				ResultSetMap rsmAux = new ResultSetMap();
				while(rsmRegras.next()) {
					if(rsmRegras.getInt("tp_gatilho")==WorkflowGatilhoServices.TP_GATILHO_BAIXA_PENDENCIA)
						rsmAux.addRegister(rsmRegras.getRegister());
				}
				
				retorno = aplicarRegrasPtcWorkflow(rsmAux, pendencia, connect).getCode();
			}
			// =================================
			
			if (isConnectionNull && retorno>0) {
				connect.commit();
				LogUtils.debug("commit");
			}
			
			return new Result(1, "Pendência baixada com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao baixar pendência!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdDocumento, int cdTipoPendencia){
		return remove(cdDocumento, cdTipoPendencia, false, null);
	}
	
	public static Result remove(int cdDocumento, int cdTipoPendencia, boolean cascade){
		return remove(cdDocumento, cdTipoPendencia, cascade, null);
	}
	
	public static Result remove(int cdDocumento, int cdTipoPendencia, boolean cascade, Connection connect){
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
				retorno = DocumentoPendenciaDAO.delete(cdDocumento, cdTipoPendencia, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este pendencia está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Pendência excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir pendência!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return DocumentoPendenciaDAO.find(criterios, connect);
		//return Search.find("SELECT * FROM PRC_JUIZO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}


	public static ResultSetMap getAllByDocumento(int cdDocumento) {
		Connection connect  = Conexao.conectar();
		try {
			String sql =
					" SELECT A.*, B.nm_tipo_pendencia, C.nm_login AS nm_usuario_registro, D.nm_login AS nm_usuario_baixa " +
					" FROM ptc_documento_pendencia A " +
					" JOIN ptc_tipo_pendencia     B ON (A.cd_tipo_pendencia   = B.cd_tipo_pendencia)" +
					" LEFT OUTER JOIN seg_usuario C ON (A.cd_usuario_registro = C.cd_usuario)" +
					" LEFT OUTER JOIN seg_usuario D ON (A.cd_usuario_baixa    = D.cd_usuario) " +
					"WHERE A.cd_documento = "+cdDocumento;
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
	
	public static Result aplicarRegrasPtcWorkflow(ResultSetMap rsmRegras, DocumentoPendencia pendencia){
		return aplicarRegrasPtcWorkflow(rsmRegras, pendencia, null);
	}
	
	public static Result aplicarRegrasPtcWorkflow(ResultSetMap rsmRegras, DocumentoPendencia pendencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 1;
			while(rsmRegras.next()) {
				int tpGatilho = rsmRegras.getInt("TP_GATILHO");
				int tpAcao = -1;
				switch (tpGatilho) {
					case WorkflowGatilhoServices.TP_GATILHO_LANCAMENTO_PENDENCIA:
						tpAcao = rsmRegras.getInt("TP_ACAO");
						switch(tpAcao) {
							case WorkflowAcaoServices.TP_ACAO_LANCAR_OCORRENCIA:
								
								DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia(
										pendencia.getCdDocumento(), 
										rsmRegras.getInt("cd_tipo_ocorrencia"), 
										0, //cdOcorrencia, 
										pendencia.getCdUsuarioRegistro(), //cdUsuario, 
										new GregorianCalendar(), //dtOcorrencia, 
										pendencia.getTxtPendencia(),//txtOcorrencia, 
										DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO, 
										0);
								
								retorno = DocumentoOcorrenciaServices.save(ocorrencia, connect).getCode();
								System.out.println("retorno: "+retorno);
								break;
								
						}
						break;
					case WorkflowGatilhoServices.TP_GATILHO_BAIXA_PENDENCIA:
						tpAcao = rsmRegras.getInt("TP_ACAO");
						switch(tpAcao) {
							case WorkflowAcaoServices.TP_ACAO_LANCAR_OCORRENCIA:
								
								DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia(
										pendencia.getCdDocumento(), 
										rsmRegras.getInt("cd_tipo_ocorrencia"), 
										0, //cdOcorrencia, 
										pendencia.getCdUsuarioBaixa(), //cdUsuario, 
										new GregorianCalendar(), //dtOcorrencia, 
										pendencia.getTxtBaixa(),//txtOcorrencia, 
										DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO,
										0);
								
								retorno = DocumentoOcorrenciaServices.save(ocorrencia, connect).getCode();
								break;
						}
						break;
					default:
						break;
						
				}
				
			}
						
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<0)?"Erro ao aplicar...":"Aplicado com sucesso...");
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
