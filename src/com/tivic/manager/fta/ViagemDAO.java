package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ViagemDAO{

	public static int insert(Viagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(Viagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_viagem", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdViagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_viagem (cd_viagem,"+
			                                  "cd_rota,"+
			                                  "cd_veiculo,"+
			                                  "cd_motivo,"+
			                                  "dt_saida,"+
			                                  "dt_chegada,"+
			                                  "txt_observacao,"+
			                                  "tp_viagem,"+
			                                  "dt_previsao_chegada," +
			                                  "st_viagem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdRota()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRota());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVeiculo());
			if(objeto.getCdMotivo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMotivo());
			if(objeto.getDtSaida()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtSaida().getTimeInMillis()));
			if(objeto.getDtChegada()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtChegada().getTimeInMillis()));
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setInt(8,objeto.getTpViagem());
			if(objeto.getDtPrevisaoChegada()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPrevisaoChegada().getTimeInMillis()));
			pstmt.setInt(10, objeto.getStViagem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Viagem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Viagem objeto, int cdViagemOld) {
		return update(objeto, cdViagemOld, null);
	}

	public static int update(Viagem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Viagem objeto, int cdViagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_viagem SET cd_viagem=?,"+
												      		   "cd_rota=?,"+
												      		   "cd_veiculo=?,"+
												      		   "cd_motivo=?,"+
												      		   "dt_saida=?,"+
												      		   "dt_chegada=?,"+
												      		   "txt_observacao=?,"+
												      		   "tp_viagem=?,"+
												      		   "dt_previsao_chegada=?," +
												      		   "st_viagem=? WHERE cd_viagem=?");
			pstmt.setInt(1,objeto.getCdViagem());
			if(objeto.getCdRota()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRota());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVeiculo());
			if(objeto.getCdMotivo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMotivo());
			if(objeto.getDtSaida()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtSaida().getTimeInMillis()));
			if(objeto.getDtChegada()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtChegada().getTimeInMillis()));
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setInt(8,objeto.getTpViagem());
			if(objeto.getDtPrevisaoChegada()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPrevisaoChegada().getTimeInMillis()));
			pstmt.setInt(10, objeto.getStViagem());
			pstmt.setInt(11, cdViagemOld!=0 ? cdViagemOld : objeto.getCdViagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdViagem) {
		return delete(cdViagem, null);
	}

	public static int delete(int cdViagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_viagem WHERE cd_viagem=?");
			pstmt.setInt(1, cdViagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Viagem get(int cdViagem) {
		return get(cdViagem, null);
	}

	public static Viagem get(int cdViagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_viagem WHERE cd_viagem=?");
			pstmt.setInt(1, cdViagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Viagem(rs.getInt("cd_viagem"),
						rs.getInt("cd_rota"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_motivo"),
						(rs.getTimestamp("dt_saida")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_saida").getTime()),
						(rs.getTimestamp("dt_chegada")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_chegada").getTime()),
						rs.getString("txt_observacao"),
						rs.getInt("tp_viagem"),
						(rs.getTimestamp("dt_previsao_chegada")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_previsao_chegada").getTime()),
						rs.getInt("st_viagem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_viagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_viagem", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
