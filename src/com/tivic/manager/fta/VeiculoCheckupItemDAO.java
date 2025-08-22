package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class VeiculoCheckupItemDAO{

	public static int insert(VeiculoCheckupItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(VeiculoCheckupItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_veiculo_checkup_item", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCheckupItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_veiculo_checkup_item (cd_checkup_item,"+
			                                  "cd_tipo_checkup,"+
			                                  "cd_item,"+
			                                  "cd_checkup,"+
			                                  "cd_componente,"+
			                                  "vl_item,"+
			                                  "txt_observacao,"+
			                                  "txt_diagnostico,"+
			                                  "st_checkup_item) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoCheckup()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoCheckup());
			if(objeto.getCdItem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdItem());
			if(objeto.getCdCheckup()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCheckup());
			if(objeto.getCdComponente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdComponente());
			pstmt.setFloat(6,objeto.getVlItem());
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setString(8,objeto.getTxtDiagnostico());
			pstmt.setInt(9,objeto.getStCheckupItem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VeiculoCheckupItem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(VeiculoCheckupItem objeto, int cdCheckupItemOld) {
		return update(objeto, cdCheckupItemOld, null);
	}

	public static int update(VeiculoCheckupItem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(VeiculoCheckupItem objeto, int cdCheckupItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_veiculo_checkup_item SET cd_checkup_item=?,"+
												      		   "cd_tipo_checkup=?,"+
												      		   "cd_item=?,"+
												      		   "cd_checkup=?,"+
												      		   "cd_componente=?,"+
												      		   "vl_item=?,"+
												      		   "txt_observacao=?,"+
												      		   "txt_diagnostico=?,"+
												      		   "st_checkup_item=? WHERE cd_checkup_item=?");
			pstmt.setInt(1,objeto.getCdCheckupItem());
			if(objeto.getCdTipoCheckup()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoCheckup());
			if(objeto.getCdItem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdItem());
			if(objeto.getCdCheckup()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCheckup());
			if(objeto.getCdComponente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdComponente());
			pstmt.setFloat(6,objeto.getVlItem());
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setString(8,objeto.getTxtDiagnostico());
			pstmt.setInt(9,objeto.getStCheckupItem());
			pstmt.setInt(10, cdCheckupItemOld!=0 ? cdCheckupItemOld : objeto.getCdCheckupItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCheckupItem) {
		return delete(cdCheckupItem, null);
	}

	public static int delete(int cdCheckupItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_veiculo_checkup_item WHERE cd_checkup_item=?");
			pstmt.setInt(1, cdCheckupItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VeiculoCheckupItem get(int cdCheckupItem) {
		return get(cdCheckupItem, null);
	}

	public static VeiculoCheckupItem get(int cdCheckupItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_veiculo_checkup_item WHERE cd_checkup_item=?");
			pstmt.setInt(1, cdCheckupItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VeiculoCheckupItem(rs.getInt("cd_checkup_item"),
						rs.getInt("cd_tipo_checkup"),
						rs.getInt("cd_item"),
						rs.getInt("cd_checkup"),
						rs.getInt("cd_componente"),
						rs.getFloat("vl_item"),
						rs.getString("txt_observacao"),
						rs.getString("txt_diagnostico"),
						rs.getInt("st_checkup_item"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_veiculo_checkup_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoCheckupItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_veiculo_checkup_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
