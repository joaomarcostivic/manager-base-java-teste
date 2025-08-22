package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class HorarioDAO{

	public static int insert(Horario objeto) {
		return insert(objeto, null);
	}

	public static int insert(Horario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[5];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_horario");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_tabela_horario");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdTabelaHorario()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_linha");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdLinha()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_rota");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdRota()));
			keys[4] = new HashMap<String,Object>();
			keys[4].put("FIELD_NAME", "cd_trecho");
			keys[4].put("IS_KEY_NATIVE", "NO");
			keys[4].put("FIELD_VALUE", new Integer(objeto.getCdTrecho()));
			int code = Conexao.getSequenceCode("mob_horario", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_horario (cd_horario,"+
			                                  "cd_tabela_horario,"+
			                                  "cd_linha,"+
			                                  "cd_rota,"+
			                                  "cd_trecho,"+
			                                  "lg_recolhe,"+
			                                  "hr_partida,"+
			                                  "hr_chegada,"+
			                                  "cd_parada_chegada,"+
			                                  "cd_parada_partida,"+
			                                  "cd_variacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdHorario() > 0)
				pstmt.setInt(1, objeto.getCdHorario());
			else {
				objeto.setCdHorario(code);
				pstmt.setInt(1, objeto.getCdHorario());
			}
			if(objeto.getCdTabelaHorario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTabelaHorario());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdLinha());
			if(objeto.getCdRota()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdRota());
			if(objeto.getCdTrecho()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTrecho());
			pstmt.setInt(6,objeto.getLgRecolhe());
			if(objeto.getHrPartida()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getHrPartida().getTimeInMillis()));
			if(objeto.getHrChegada()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getHrChegada().getTimeInMillis()));
			if(objeto.getCdParadaChegada()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdParadaChegada());
			if(objeto.getCdParadaPartida()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdParadaPartida());
			pstmt.setInt(11,objeto.getCdVariacao());
			
			pstmt.executeUpdate();
			return objeto.getCdHorario();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Horario objeto) {
		return update(objeto, 0, 0, 0, 0, 0, null);
	}

	public static int update(Horario objeto, int cdHorarioOld, int cdTabelaHorarioOld, int cdLinhaOld, int cdRotaOld, int cdTrechoOld) {
		return update(objeto, cdHorarioOld, cdTabelaHorarioOld, cdLinhaOld, cdRotaOld, cdTrechoOld, null);
	}

	public static int update(Horario objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, connect);
	}

	public static int update(Horario objeto, int cdHorarioOld, int cdTabelaHorarioOld, int cdLinhaOld, int cdRotaOld, int cdTrechoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_horario SET cd_horario=?,"+
												      		   "cd_tabela_horario=?,"+
												      		   "cd_linha=?,"+
												      		   "cd_rota=?,"+
												      		   "cd_trecho=?,"+
												      		   "lg_recolhe=?,"+
												      		   "hr_partida=?,"+
												      		   "hr_chegada=?,"+
												      		   "cd_parada_chegada=?,"+
												      		   "cd_parada_partida=?,"+
												      		   "cd_variacao=? WHERE cd_horario=? AND cd_tabela_horario=? AND cd_linha=? AND cd_rota=? AND cd_trecho=?");
			pstmt.setInt(1,objeto.getCdHorario());
			pstmt.setInt(2,objeto.getCdTabelaHorario());
			pstmt.setInt(3,objeto.getCdLinha());
			pstmt.setInt(4,objeto.getCdRota());
			pstmt.setInt(5,objeto.getCdTrecho());
			pstmt.setInt(6,objeto.getLgRecolhe());
			if(objeto.getHrPartida()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getHrPartida().getTimeInMillis()));
			if(objeto.getHrChegada()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getHrChegada().getTimeInMillis()));
			if(objeto.getCdParadaChegada()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdParadaChegada());
			if(objeto.getCdParadaPartida()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdParadaPartida());
			pstmt.setInt(11,objeto.getCdVariacao());
			pstmt.setInt(12, cdHorarioOld!=0 ? cdHorarioOld : objeto.getCdHorario());
			pstmt.setInt(13, cdTabelaHorarioOld!=0 ? cdTabelaHorarioOld : objeto.getCdTabelaHorario());
			pstmt.setInt(14, cdLinhaOld!=0 ? cdLinhaOld : objeto.getCdLinha());
			pstmt.setInt(15, cdRotaOld!=0 ? cdRotaOld : objeto.getCdRota());
			pstmt.setInt(16, cdTrechoOld!=0 ? cdTrechoOld : objeto.getCdTrecho());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho) {
		return delete(cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, null);
	}

	public static int delete(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_horario WHERE cd_horario=? AND cd_tabela_horario=? AND cd_linha=? AND cd_rota=? AND cd_trecho=?");
			pstmt.setInt(1, cdHorario);
			pstmt.setInt(2, cdTabelaHorario);
			pstmt.setInt(3, cdLinha);
			pstmt.setInt(4, cdRota);
			pstmt.setInt(5, cdTrecho);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Horario get(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho) {
		return get(cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, null);
	}

	public static Horario get(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_horario WHERE cd_horario=? AND cd_tabela_horario=? AND cd_linha=? AND cd_rota=? AND cd_trecho=?");
			pstmt.setInt(1, cdHorario);
			pstmt.setInt(2, cdTabelaHorario);
			pstmt.setInt(3, cdLinha);
			pstmt.setInt(4, cdRota);
			pstmt.setInt(5, cdTrecho);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Horario(rs.getInt("cd_horario"),
						rs.getInt("cd_tabela_horario"),
						rs.getInt("cd_linha"),
						rs.getInt("cd_rota"),
						rs.getInt("cd_trecho"),
						rs.getInt("lg_recolhe"),
						(rs.getTimestamp("hr_partida")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_partida").getTime()),
						(rs.getTimestamp("hr_chegada")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_chegada").getTime()),
						rs.getInt("cd_parada_chegada"),
						rs.getInt("cd_parada_partida"),
						rs.getInt("cd_variacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_horario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Horario> getList() {
		return getList(null);
	}

	public static ArrayList<Horario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Horario> list = new ArrayList<Horario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Horario obj = HorarioDAO.get(rsm.getInt("cd_horario"), rsm.getInt("cd_tabela_horario"), rsm.getInt("cd_linha"), rsm.getInt("cd_rota"), rsm.getInt("cd_trecho"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_horario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}