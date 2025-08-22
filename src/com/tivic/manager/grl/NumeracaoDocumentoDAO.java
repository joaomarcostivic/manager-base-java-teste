package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class NumeracaoDocumentoDAO{

	public static int insert(NumeracaoDocumento objeto) {
		return insert(objeto, null);
	}

	public static int insert(NumeracaoDocumento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_numeracao_documento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNumeracaoDocumento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_numeracao_documento (cd_numeracao_documento,"+
			                                  "nm_tipo_documento,"+
			                                  "cd_ultimo_numero,"+
			                                  "nr_ano,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoDocumento());
			pstmt.setInt(3,objeto.getCdUltimoNumero());
			pstmt.setInt(4,objeto.getNrAno());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NumeracaoDocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NumeracaoDocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NumeracaoDocumento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(NumeracaoDocumento objeto, int cdNumeracaoDocumentoOld) {
		return update(objeto, cdNumeracaoDocumentoOld, null);
	}

	public static int update(NumeracaoDocumento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(NumeracaoDocumento objeto, int cdNumeracaoDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_numeracao_documento SET cd_numeracao_documento=?,"+
												      		   "nm_tipo_documento=?,"+
												      		   "cd_ultimo_numero=?,"+
												      		   "nr_ano=?,"+
												      		   "cd_empresa=? WHERE cd_numeracao_documento=?");
			pstmt.setInt(1,objeto.getCdNumeracaoDocumento());
			pstmt.setString(2,objeto.getNmTipoDocumento());
			pstmt.setInt(3,objeto.getCdUltimoNumero());
			pstmt.setInt(4,objeto.getNrAno());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			pstmt.setInt(6, cdNumeracaoDocumentoOld!=0 ? cdNumeracaoDocumentoOld : objeto.getCdNumeracaoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NumeracaoDocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NumeracaoDocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNumeracaoDocumento) {
		return delete(cdNumeracaoDocumento, null);
	}

	public static int delete(int cdNumeracaoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_numeracao_documento WHERE cd_numeracao_documento=?");
			pstmt.setInt(1, cdNumeracaoDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NumeracaoDocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NumeracaoDocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NumeracaoDocumento get(int cdNumeracaoDocumento) {
		return get(cdNumeracaoDocumento, null);
	}

	public static NumeracaoDocumento get(int cdNumeracaoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_numeracao_documento WHERE cd_numeracao_documento=?");
			pstmt.setInt(1, cdNumeracaoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NumeracaoDocumento(rs.getInt("cd_numeracao_documento"),
						rs.getString("nm_tipo_documento"),
						rs.getInt("cd_ultimo_numero"),
						rs.getInt("nr_ano"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NumeracaoDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NumeracaoDocumentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_numeracao_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NumeracaoDocumentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NumeracaoDocumentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_numeracao_documento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
