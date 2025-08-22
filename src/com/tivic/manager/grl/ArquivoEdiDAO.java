package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class ArquivoEdiDAO{

	public static int insert(ArquivoEdi objeto) {
		return insert(objeto, null);
	}

	public static int insert(ArquivoEdi objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = ArquivoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdArquivo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_arquivo_edi (cd_arquivo,"+
			                                  "nr_remessa,"+
			                                  "dt_criacao,"+
			                                  "cd_conta,"+
			                                  "st_arquivo) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdArquivo());
			pstmt.setInt(2,objeto.getNrRemessa());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getCdConta()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdConta());
			pstmt.setInt(5,objeto.getStArquivo());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ArquivoEdi objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ArquivoEdi objeto, int cdArquivoOld) {
		return update(objeto, cdArquivoOld, null);
	}

	public static int update(ArquivoEdi objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ArquivoEdi objeto, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_arquivo_edi SET cd_arquivo=?,"+
												      		   "nr_remessa=?,"+
												      		   "dt_criacao=?,"+
												      		   "cd_conta=?,"+
												      		   "st_arquivo=? WHERE cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdArquivo());
			pstmt.setInt(2,objeto.getNrRemessa());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getCdConta()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdConta());
			pstmt.setInt(5,objeto.getStArquivo());
			pstmt.setInt(6, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			if (ArquivoDAO.update(objeto)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdArquivo) {
		return delete(cdArquivo, null);
	}

	public static int delete(int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_arquivo_edi WHERE cd_arquivo=?");
			pstmt.setInt(1, cdArquivo);
			pstmt.executeUpdate();
			if (ArquivoDAO.delete(cdArquivo, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArquivoEdi get(int cdArquivo) {
		return get(cdArquivo, null);
	}

	public static ArquivoEdi get(int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo_edi A, grl_arquivo B WHERE A.cd_arquivo=B.cd_arquivo AND A.cd_arquivo=?");
			pstmt.setInt(1, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ArquivoEdi(rs.getInt("cd_arquivo"),
						rs.getString("nm_arquivo"),
						(rs.getTimestamp("dt_arquivamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_arquivamento").getTime()),
						rs.getString("nm_documento"),
						rs.getBytes("blb_arquivo")==null?null:rs.getBytes("blb_arquivo"),
						rs.getInt("cd_tipo_arquivo"),
						rs.getInt("nr_remessa"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						rs.getInt("cd_conta"),
						rs.getInt("st_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo_edi");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_arquivo_edi", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
