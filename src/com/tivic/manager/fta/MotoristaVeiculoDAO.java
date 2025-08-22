package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class MotoristaVeiculoDAO{

	public static int insert(MotoristaVeiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(MotoristaVeiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_motorista_veiculo (cd_motorista,"+
			                                  "cd_veiculo,"+
			                                  "dt_inicio_atividade,"+
			                                  "dt_final_atividade,"+
			                                  "lg_ativo) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMotorista());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			if(objeto.getDtInicioAtividade()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicioAtividade().getTimeInMillis()));
			if(objeto.getDtFinalAtividade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinalAtividade().getTimeInMillis()));
			pstmt.setInt(5,objeto.getLgAtivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaVeiculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaVeiculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MotoristaVeiculo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MotoristaVeiculo objeto, int cdMotoristaOld, int cdVeiculoOld) {
		return update(objeto, cdMotoristaOld, cdVeiculoOld, null);
	}

	public static int update(MotoristaVeiculo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MotoristaVeiculo objeto, int cdMotoristaOld, int cdVeiculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_motorista_veiculo SET cd_motorista=?,"+
												      		   "cd_veiculo=?,"+
												      		   "dt_inicio_atividade=?,"+
												      		   "dt_final_atividade=?,"+
												      		   "lg_ativo=? WHERE cd_motorista=? AND cd_veiculo=?");
			pstmt.setInt(1,objeto.getCdMotorista());
			pstmt.setInt(2,objeto.getCdVeiculo());
			if(objeto.getDtInicioAtividade()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicioAtividade().getTimeInMillis()));
			if(objeto.getDtFinalAtividade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinalAtividade().getTimeInMillis()));
			pstmt.setInt(5,objeto.getLgAtivo());
			pstmt.setInt(6, cdMotoristaOld!=0 ? cdMotoristaOld : objeto.getCdMotorista());
			pstmt.setInt(7, cdVeiculoOld!=0 ? cdVeiculoOld : objeto.getCdVeiculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaVeiculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaVeiculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMotorista, int cdVeiculo) {
		return delete(cdMotorista, cdVeiculo, null);
	}

	public static int delete(int cdMotorista, int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_motorista_veiculo WHERE cd_motorista=? AND cd_veiculo=?");
			pstmt.setInt(1, cdMotorista);
			pstmt.setInt(2, cdVeiculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaVeiculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaVeiculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MotoristaVeiculo get(int cdMotorista, int cdVeiculo) {
		return get(cdMotorista, cdVeiculo, null);
	}

	public static MotoristaVeiculo get(int cdMotorista, int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_motorista_veiculo WHERE cd_motorista=? AND cd_veiculo=?");
			pstmt.setInt(1, cdMotorista);
			pstmt.setInt(2, cdVeiculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MotoristaVeiculo(rs.getInt("cd_motorista"),
						rs.getInt("cd_veiculo"),
						(rs.getTimestamp("dt_inicio_atividade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_atividade").getTime()),
						(rs.getTimestamp("dt_final_atividade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_atividade").getTime()),
						rs.getInt("lg_ativo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaVeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_motorista_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotoristaVeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotoristaVeiculoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_motorista_veiculo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
