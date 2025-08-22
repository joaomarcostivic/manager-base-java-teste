package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import sol.dao.Util;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class PontoDAO{

	public static int insert(Ponto objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Ponto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ponto");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_matricula");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdMatricula()));
			int code = Conexao.getSequenceCode("srh_ponto", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPonto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_ponto (cd_ponto,"+
			                                  "cd_matricula,"+
			                                  "dt_abertura,"+
			                                  "dt_fechamento,"+
			                                  "st_ponto,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatricula());
			if(objeto.getDtAbertura()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtAbertura().getTimeInMillis()));
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStPonto());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Ponto objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Ponto objeto, int cdPontoOld, int cdMatriculaOld) {
		return update(objeto, cdPontoOld, cdMatriculaOld, null);
	}

	public static int update(Ponto objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Ponto objeto, int cdPontoOld, int cdMatriculaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_ponto SET cd_ponto=?,"+
												      		   "cd_matricula=?,"+
												      		   "dt_abertura=?,"+
												      		   "dt_fechamento=?,"+
												      		   "st_ponto=?,"+
												      		   "txt_observacao=? WHERE cd_ponto=? AND cd_matricula=?");
			pstmt.setInt(1,objeto.getCdPonto());
			pstmt.setInt(2,objeto.getCdMatricula());
			if(objeto.getDtAbertura()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtAbertura().getTimeInMillis()));
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStPonto());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7, cdPontoOld!=0 ? cdPontoOld : objeto.getCdPonto());
			pstmt.setInt(8, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPonto, int cdMatricula) {
		return delete(cdPonto, cdMatricula, null);
	}

	public static int delete(int cdPonto, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_ponto WHERE cd_ponto=? AND cd_matricula=?");
			pstmt.setInt(1, cdPonto);
			pstmt.setInt(2, cdMatricula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Ponto get(int cdPonto, int cdMatricula) {
		return get(cdPonto, cdMatricula, null);
	}

	public static Ponto get(int cdPonto, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_ponto WHERE cd_ponto=? AND cd_matricula=?");
			pstmt.setInt(1, cdPonto);
			pstmt.setInt(2, cdMatricula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Ponto(rs.getInt("cd_ponto"),
						rs.getInt("cd_matricula"),
						(rs.getTimestamp("dt_abertura")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_abertura").getTime()),
						(rs.getTimestamp("dt_fechamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fechamento").getTime()),
						rs.getInt("st_ponto"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_ponto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_ponto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
