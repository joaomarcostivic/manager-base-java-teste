package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ArquivoRegistroDAO{

	public static int insert(ArquivoRegistro objeto) {
		return insert(objeto, null);
	}

	public static int insert(ArquivoRegistro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_arquivo_registro", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegistro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_arquivo_registro (cd_registro,"+
			                                  "cd_arquivo,"+
			                                  "cd_conta_receber,"+
			                                  "cd_tipo_erro,"+
			                                  "cd_tipo_registro,"+
			                                  "cd_conta_pagar) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdArquivo());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceber());
			if(objeto.getCdTipoErro()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoErro());
			if(objeto.getCdTipoRegistro()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoRegistro());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdContaPagar());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ArquivoRegistro objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ArquivoRegistro objeto, int cdRegistroOld) {
		return update(objeto, cdRegistroOld, null);
	}

	public static int update(ArquivoRegistro objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ArquivoRegistro objeto, int cdRegistroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_arquivo_registro SET cd_registro=?,"+
												      		   "cd_arquivo=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "cd_tipo_erro=?,"+
												      		   "cd_tipo_registro=?,"+
												      		   "cd_conta_pagar=? WHERE cd_registro=?");
			pstmt.setInt(1,objeto.getCdRegistro());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdArquivo());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceber());
			if(objeto.getCdTipoErro()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoErro());
			if(objeto.getCdTipoRegistro()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipoRegistro());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdContaPagar());
			pstmt.setInt(7, cdRegistroOld!=0 ? cdRegistroOld : objeto.getCdRegistro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegistro) {
		return delete(cdRegistro, null);
	}

	public static int delete(int cdRegistro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_arquivo_registro WHERE cd_registro=?");
			pstmt.setInt(1, cdRegistro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArquivoRegistro get(int cdRegistro) {
		return get(cdRegistro, null);
	}

	public static ArquivoRegistro get(int cdRegistro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo_registro WHERE cd_registro=?");
			pstmt.setInt(1, cdRegistro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ArquivoRegistro(rs.getInt("cd_registro"),
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_conta_receber"),
						rs.getInt("cd_tipo_erro"),
						rs.getInt("cd_tipo_registro"),
						rs.getInt("cd_conta_pagar"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo_registro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_arquivo_registro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
