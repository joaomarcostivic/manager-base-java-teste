package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class LocalArmazenamentoDAO{

	public static int insert(LocalArmazenamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(LocalArmazenamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("alm_local_armazenamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLocalArmazenamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_local_armazenamento (cd_local_armazenamento,"+
			                                  "cd_setor,"+
			                                  "cd_nivel_local,"+
			                                  "cd_responsavel,"+
			                                  "nm_local_armazenamento,"+
			                                  "id_local_armazenamento,"+
			                                  "cd_local_armazenamento_superior,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdSetor()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSetor());
			if(objeto.getCdNivelLocal()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdNivelLocal());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdResponsavel());
			pstmt.setString(5,objeto.getNmLocalArmazenamento());
			pstmt.setString(6,objeto.getIdLocalArmazenamento());
			if(objeto.getCdLocalArmazenamentoSuperior()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdLocalArmazenamentoSuperior());
			pstmt.setInt(8,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalArmazenamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalArmazenamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LocalArmazenamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LocalArmazenamento objeto, int cdLocalArmazenamentoOld) {
		return update(objeto, cdLocalArmazenamentoOld, null);
	}

	public static int update(LocalArmazenamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LocalArmazenamento objeto, int cdLocalArmazenamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_local_armazenamento SET cd_local_armazenamento=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_nivel_local=?,"+
												      		   "cd_responsavel=?,"+
												      		   "nm_local_armazenamento=?,"+
												      		   "id_local_armazenamento=?,"+
												      		   "cd_local_armazenamento_superior=?,"+
												      		   "cd_empresa=? WHERE cd_local_armazenamento=?");
			pstmt.setInt(1,objeto.getCdLocalArmazenamento());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSetor());
			if(objeto.getCdNivelLocal()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdNivelLocal());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdResponsavel());
			pstmt.setString(5,objeto.getNmLocalArmazenamento());
			pstmt.setString(6,objeto.getIdLocalArmazenamento());
			if(objeto.getCdLocalArmazenamentoSuperior()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdLocalArmazenamentoSuperior());
			pstmt.setInt(8,objeto.getCdEmpresa());
			pstmt.setInt(9, cdLocalArmazenamentoOld!=0 ? cdLocalArmazenamentoOld : objeto.getCdLocalArmazenamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalArmazenamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalArmazenamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLocalArmazenamento) {
		return delete(cdLocalArmazenamento, null);
	}

	public static int delete(int cdLocalArmazenamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_local_armazenamento WHERE cd_local_armazenamento=?");
			pstmt.setInt(1, cdLocalArmazenamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalArmazenamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalArmazenamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LocalArmazenamento get(int cdLocalArmazenamento) {
		return get(cdLocalArmazenamento, null);
	}

	public static LocalArmazenamento get(int cdLocalArmazenamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE cd_local_armazenamento=?");
			pstmt.setInt(1, cdLocalArmazenamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LocalArmazenamento(rs.getInt("cd_local_armazenamento"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_nivel_local"),
						rs.getInt("cd_responsavel"),
						rs.getString("nm_local_armazenamento"),
						rs.getString("id_local_armazenamento"),
						rs.getInt("cd_local_armazenamento_superior"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalArmazenamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalArmazenamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_nivel_local FROM alm_local_armazenamento A " +
					                         "LEFT OUTER JOIN alm_nivel_local B ON (A.cd_nivel_local = B.cd_nivel_local) " +
					                         "ORDER BY nm_local_armazenamento");
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
		return Search.find("SELECT A.*, B.nm_nivel_local FROM alm_local_armazenamento A " +
                           "LEFT OUTER JOIN alm_nivel_local B ON (A.cd_nivel_local = B.cd_nivel_local) ", 
                           "ORDER BY nm_local_armazenamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
