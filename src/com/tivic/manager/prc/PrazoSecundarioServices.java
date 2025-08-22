package com.tivic.manager.prc;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class PrazoSecundarioServices {
	
	public static final int TP_CONTAGEM_CORRIDO = 0;
	public static final int TP_CONTAGEM_UTIL 	= 1;
	
	public static final int TP_ACAO_DELETE = 0;
	public static final int TP_ACAO_INSERT = 1;
	public static final int TP_ACAO_UPDATE = 2;
	public static final int TP_ACAO_DONE   = 3;


	public static Result save(PrazoSecundario prazoSecundario){
		return save(prazoSecundario, null, null);
	}

	public static Result save(PrazoSecundario prazoSecundario, AuthData authData){
		return save(prazoSecundario, authData, null);
	}

	public static Result save(PrazoSecundario prazoSecundario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(prazoSecundario==null)
				return new Result(-1, "Erro ao salvar. PrazoSecundario é nulo");

			int retorno;
			
			PrazoSecundario ps = PrazoSecundarioDAO.get(prazoSecundario.getCdPrazoSecundario(), prazoSecundario.getCdTipoPrazo(), connect);
			if(ps==null){
				retorno = PrazoSecundarioDAO.insert(prazoSecundario, connect);
				prazoSecundario.setCdPrazoSecundario(retorno);
			}
			else {
				retorno = PrazoSecundarioDAO.update(prazoSecundario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PRAZOSECUNDARIO", prazoSecundario);
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
	public static Result remove(PrazoSecundario prazoSecundario) {
		return remove(prazoSecundario.getCdPrazoSecundario(), prazoSecundario.getCdTipoPrazo());
	}
	public static Result remove(int cdPrazoSecundario, int cdTipoPrazo){
		return remove(cdPrazoSecundario, cdTipoPrazo, false, null, null);
	}
	public static Result remove(int cdPrazoSecundario, int cdTipoPrazo, boolean cascade){
		return remove(cdPrazoSecundario, cdTipoPrazo, cascade, null, null);
	}
	public static Result remove(int cdPrazoSecundario, int cdTipoPrazo, boolean cascade, AuthData authData){
		return remove(cdPrazoSecundario, cdTipoPrazo, cascade, authData, null);
	}
	public static Result remove(int cdPrazoSecundario, int cdTipoPrazo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = PrazoSecundarioDAO.delete(cdPrazoSecundario, cdTipoPrazo, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_prazo FROM prc_prazo_secundario A " +
											 " LEFT OUTER JOIN prc_tipo_prazo B ON (B.cd_tipo_prazo = A.cd_tipo_prazo)" +
					                         " ORDER BY A.qt_dias");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByTipoPrazo(int cdTipoPrazo) {
		return getAllByTipoPrazo(cdTipoPrazo, null);
	}

	public static ResultSetMap getAllByTipoPrazo(int cdTipoPrazo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_prazo, C.nm_tipo_prazo as nm_tipo_prazo_secundario, D.nm_tipo_processo " +
					" FROM prc_prazo_secundario A " +
					 " LEFT OUTER JOIN prc_tipo_prazo B ON (B.cd_tipo_prazo = A.cd_tipo_prazo) " +
					 " LEFT OUTER JOIN prc_tipo_prazo C ON (C.cd_tipo_prazo = A.cd_prazo_secundario) " +
					 " LEFT OUTER JOIN prc_tipo_processo D ON (D.cd_tipo_processo = A.cd_tipo_processo)" +
					 " WHERE A.cd_tipo_prazo = ? " +
                     " ORDER BY A.qt_dias");
			pstmt.setInt(1, cdTipoPrazo);
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
		return Search.find("SELECT A.*, B.nm_tipo_prazo FROM prc_prazo_secundario A " +
						   " LEFT OUTER JOIN prc_tipo_prazo B ON (B.cd_tipo_prazo = A.cd_tipo_prazo)" +
                           " ORDER BY A.qt_dias", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}