package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CentroCustoDAO{

	public static int insert(CentroCusto objeto) {
		return insert(objeto, null);
	}

	public static int insert(CentroCusto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_centro_custo");
			if (code <= 0) 
				return code;
			
			objeto.setCdCentroCusto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_centro_custo (cd_centro_custo,"+
			                                  "cd_centro_custo_superior,"+
			                                  "cd_setor,"+
			                                  "cd_plano_centro_custo,"+
			                                  "nm_centro_custo,"+
			                                  "nr_centro_custo,"+
			                                  "dt_inativacao,"+
			                                  "id_centro_custo,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCentroCustoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCentroCustoSuperior());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getCdPlanoCentroCusto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPlanoCentroCusto());
			pstmt.setString(5,objeto.getNmCentroCusto());
			pstmt.setString(6,objeto.getNrCentroCusto());
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setString(8,objeto.getIdCentroCusto());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CentroCusto objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CentroCusto objeto, int cdCentroCustoOld) {
		return update(objeto, cdCentroCustoOld, null);
	}

	public static int update(CentroCusto objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CentroCusto objeto, int cdCentroCustoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_centro_custo SET cd_centro_custo=?,"+
												      		   "cd_centro_custo_superior=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_plano_centro_custo=?,"+
												      		   "nm_centro_custo=?,"+
												      		   "nr_centro_custo=?,"+
												      		   "dt_inativacao=?,"+
												      		   "id_centro_custo=?,"+
												      		   "txt_observacao=? WHERE cd_centro_custo=?");
			pstmt.setInt(1,objeto.getCdCentroCusto());
			if(objeto.getCdCentroCustoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCentroCustoSuperior());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getCdPlanoCentroCusto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPlanoCentroCusto());
			pstmt.setString(5,objeto.getNmCentroCusto());
			pstmt.setString(6,objeto.getNrCentroCusto());
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			pstmt.setString(8,objeto.getIdCentroCusto());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setInt(10, cdCentroCustoOld!=0 ? cdCentroCustoOld : objeto.getCdCentroCusto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCentroCusto) {
		return delete(cdCentroCusto, null);
	}

	public static int delete(int cdCentroCusto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_centro_custo WHERE cd_centro_custo=?");
			pstmt.setInt(1, cdCentroCusto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CentroCusto get(int cdCentroCusto) {
		return get(cdCentroCusto, null);
	}

	public static CentroCusto get(int cdCentroCusto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_centro_custo WHERE cd_centro_custo=?");
			pstmt.setInt(1, cdCentroCusto);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CentroCusto(rs.getInt("cd_centro_custo"),
						rs.getInt("cd_centro_custo_superior"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_plano_centro_custo"),
						rs.getString("nm_centro_custo"),
						rs.getString("nr_centro_custo"),
						(rs.getTimestamp("dt_inativacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inativacao").getTime()),
						rs.getString("id_centro_custo"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_centro_custo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_centro_custo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
