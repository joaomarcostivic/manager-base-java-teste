package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class VeiculoEquipamentoDAO{

	public static int insert(VeiculoEquipamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(VeiculoEquipamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
//			int code = VeiculoEquipamentoDAO.insert(objeto, connect);
//			if (code <= 0) {
//				if (isConnectionNull)
//					Conexao.rollback(connect);
//				return -1;
//			}
			int code = 0;
			if(objeto.getCdInstalacao()<=0)	{
				code = Conexao.getSequenceCode("mob_veiculo_equipamento", connect);
				if (code <= 0)
					return -1;
				objeto.setCdInstalacao(code);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_veiculo_equipamento (cd_equipamento,"+
							                                   "cd_veiculo,"+
							                                   "cd_instalacao,"+
							                                   "dt_instalacao,"+
							                                   "st_instalacao,"+
							                                   "txt_observacao) VALUES ( ?, ?, ?, ?, ?,?)");
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEquipamento());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			pstmt.setInt(3, code);
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStInstalacao());
			pstmt.setString(6,objeto.getTxtObservacao());			
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VeiculoEquipamento objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(VeiculoEquipamento objeto, int cdEquipamentoOld, int cdVeiculoOld, int cdInstalacaoOld) {
		return update(objeto, cdEquipamentoOld, cdVeiculoOld, cdInstalacaoOld, null);
	}

	public static int update(VeiculoEquipamento objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(VeiculoEquipamento objeto, int cdEquipamentoOld, int cdVeiculoOld, int cdInstalacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_veiculo_equipamento SET cd_equipamento=?,"+
												      		   "cd_veiculo=?,"+
												      		   "cd_instalacao=?,"+
												      		   "dt_instalacao=?,"+
												      		   "st_instalacao=?,"+
												      		   "txt_observacao=? WHERE cd_equipamento=? AND cd_veiculo=? AND cd_instalacao=?");
			pstmt.setInt(1,objeto.getCdEquipamento());
			pstmt.setInt(2,objeto.getCdVeiculo());
			pstmt.setInt(3,objeto.getCdInstalacao());
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStInstalacao());
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7, cdEquipamentoOld!=0 ? cdEquipamentoOld : objeto.getCdEquipamento());
			pstmt.setInt(8, cdVeiculoOld!=0 ? cdVeiculoOld : objeto.getCdVeiculo());
			pstmt.setInt(9, cdInstalacaoOld!=0 ? cdInstalacaoOld : objeto.getCdInstalacao());			
			pstmt.executeUpdate();
//			if (VeiculoEquipamentoDAO.update(objeto, connect)<=0) {
//				if (isConnectionNull)
//					Conexao.rollback(connect);
//				return -1;
//			}
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEquipamento, int cdVeiculo, int cdInstalacao) {
		return delete(cdEquipamento, cdVeiculo, cdInstalacao, null);
	}

	public static int delete(int cdEquipamento, int cdVeiculo, int cdInstalacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_veiculo_equipamento WHERE cd_equipamento=? AND cd_veiculo=? AND cd_instalacao=?");
			pstmt.setInt(1, cdEquipamento);
			pstmt.setInt(2, cdVeiculo);
			pstmt.setInt(3, cdInstalacao);			
			int retorno = pstmt.executeUpdate();
			if (retorno < 0) {
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
			System.err.println("Erro! VeiculoEquipamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VeiculoEquipamento get(int cdEquipamento, int cdVeiculo) {
		return get(cdEquipamento, cdVeiculo, null);
	}

	public static VeiculoEquipamento get(int cdEquipamento, int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_veiculo_equipamento WHERE cd_equipamento=? AND cd_veiculo=?");
			pstmt.setInt(1, cdEquipamento);
			pstmt.setInt(2, cdVeiculo);
			//pstmt.setInt(3, cdInstalacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VeiculoEquipamento(
						rs.getInt("cd_equipamento"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_instalacao"),
						(rs.getTimestamp("dt_instalacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_instalacao").getTime()),
						rs.getInt("st_instalacao"),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_veiculo_equipamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<VeiculoEquipamento> getList() {
		return getList(null);
	}

	public static ArrayList<VeiculoEquipamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<VeiculoEquipamento> list = new ArrayList<VeiculoEquipamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				VeiculoEquipamento obj = VeiculoEquipamentoDAO.get(rsm.getInt("cd_equipamento"), rsm.getInt("cd_veiculo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoEquipamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_veiculo_equipamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
