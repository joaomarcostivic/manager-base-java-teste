package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class TabelaHorarioDAO{

	public static int insert(TabelaHorario objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaHorario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tabela_horario");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_linha");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdLinha()));
			int code = Conexao.getSequenceCode("mob_tabela_horario", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTabelaHorario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_tabela_horario (cd_tabela_horario,"+
			                                  "cd_linha,"+
			                                  "nr_tabela_horario,"+
			                                  "tp_tabela_horario,"+
			                                  "nr_dia_semana,"+
			                                  "nr_dia_mes,"+
			                                  "dt_especial,"+
			                                  "dt_inicio_vigencia,"+
			                                  "dt_final_vigencia,"+
			                                  "st_tabela_horario,"+
			                                  "id_tabela_horario,"+
			                                  "txt_observacao,"+
			                                  "nm_tabela_horario,"+
			                                  "cd_rota,"+
			                                  "lg_adaptado,"+
			                                  "cd_concessao_veiculo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdLinha()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLinha());
			pstmt.setInt(3,objeto.getNrTabelaHorario());
			pstmt.setInt(4,objeto.getTpTabelaHorario());
			pstmt.setInt(5,objeto.getNrDiaSemana());
			pstmt.setInt(6,objeto.getNrDiaMes());
			if(objeto.getDtEspecial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEspecial().getTimeInMillis()));
			if(objeto.getDtInicioVigencia()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicioVigencia().getTimeInMillis()));
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			pstmt.setInt(10,objeto.getStTabelaHorario());
			pstmt.setString(11,objeto.getIdTabelaHorario());
			pstmt.setString(12,objeto.getTxtObservacao());
			pstmt.setString(13,objeto.getNmTabelaHorario());
			if(objeto.getCdRota()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdRota());
			pstmt.setInt(15,objeto.getLgAdaptado());
			if(objeto.getCdConcessaoVeiculo() > 0) 
				pstmt.setInt(16,objeto.getCdConcessaoVeiculo());
			else
				pstmt.setNull(16, Types.INTEGER );
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaHorario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TabelaHorario objeto, int cdTabelaHorarioOld, int cdLinhaOld) {
		return update(objeto, cdTabelaHorarioOld, cdLinhaOld, null);
	}

	public static int update(TabelaHorario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TabelaHorario objeto, int cdTabelaHorarioOld, int cdLinhaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_tabela_horario SET cd_tabela_horario=?,"+
												      		   "cd_linha=?,"+
												      		   "nr_tabela_horario=?,"+
												      		   "tp_tabela_horario=?,"+
												      		   "nr_dia_semana=?,"+
												      		   "nr_dia_mes=?,"+
												      		   "dt_especial=?,"+
												      		   "dt_inicio_vigencia=?,"+
												      		   "dt_final_vigencia=?,"+
												      		   "st_tabela_horario=?,"+
												      		   "id_tabela_horario=?,"+
												      		   "txt_observacao=?,"+
												      		   "nm_tabela_horario=?,"+
												      		   "cd_rota=?,"+
												      		   "lg_adaptado=?,"+
												      		   "cd_concessao_veiculo=? WHERE cd_tabela_horario=? AND cd_linha=?");
			pstmt.setInt(1,objeto.getCdTabelaHorario());
			pstmt.setInt(2,objeto.getCdLinha());
			pstmt.setInt(3,objeto.getNrTabelaHorario());
			pstmt.setInt(4,objeto.getTpTabelaHorario());
			pstmt.setInt(5,objeto.getNrDiaSemana());
			pstmt.setInt(6,objeto.getNrDiaMes());
			if(objeto.getDtEspecial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEspecial().getTimeInMillis()));
			if(objeto.getDtInicioVigencia()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicioVigencia().getTimeInMillis()));
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			pstmt.setInt(10,objeto.getStTabelaHorario());
			pstmt.setString(11,objeto.getIdTabelaHorario());
			pstmt.setString(12,objeto.getTxtObservacao());
			pstmt.setString(13,objeto.getNmTabelaHorario());
			if(objeto.getCdRota()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdRota());
			pstmt.setInt(15,objeto.getLgAdaptado());
			if(objeto.getCdConcessaoVeiculo()==0)
                pstmt.setNull(16, Types.INTEGER);
            else
                pstmt.setInt(16, objeto.getCdConcessaoVeiculo());
			pstmt.setInt(17, cdTabelaHorarioOld!=0 ? cdTabelaHorarioOld : objeto.getCdTabelaHorario());
			pstmt.setInt(18, cdLinhaOld!=0 ? cdLinhaOld : objeto.getCdLinha());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaHorario, int cdLinha) {
		return delete(cdTabelaHorario, cdLinha, null);
	}

	public static int delete(int cdTabelaHorario, int cdLinha, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_tabela_horario WHERE cd_tabela_horario=? AND cd_linha=?");
			pstmt.setInt(1, cdTabelaHorario);
			pstmt.setInt(2, cdLinha);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaHorario get(int cdTabelaHorario, int cdLinha) {
		return get(cdTabelaHorario, cdLinha, null);
	}

	public static TabelaHorario get(int cdTabelaHorario, int cdLinha, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_tabela_horario WHERE cd_tabela_horario=? AND cd_linha=?");
			pstmt.setInt(1, cdTabelaHorario);
			pstmt.setInt(2, cdLinha);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaHorario(rs.getInt("cd_tabela_horario"),
						rs.getInt("cd_linha"),
						rs.getInt("nr_tabela_horario"),
						rs.getInt("tp_tabela_horario"),
						rs.getInt("nr_dia_semana"),
						rs.getInt("nr_dia_mes"),
						(rs.getTimestamp("dt_especial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_especial").getTime()),
						(rs.getTimestamp("dt_inicio_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_vigencia").getTime()),
						(rs.getTimestamp("dt_final_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_vigencia").getTime()),
						rs.getInt("st_tabela_horario"),
						rs.getString("id_tabela_horario"),
						rs.getString("txt_observacao"),
						rs.getString("nm_tabela_horario"),
						rs.getInt("cd_rota"),
						rs.getInt("lg_adaptado"),
						rs.getInt("cd_concessao_veiculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tabela_horario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TabelaHorario> getList() {
		return getList(null);
	}

	public static ArrayList<TabelaHorario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TabelaHorario> list = new ArrayList<TabelaHorario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TabelaHorario obj = TabelaHorarioDAO.get(rsm.getInt("cd_tabela_horario"), rsm.getInt("cd_linha"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_tabela_horario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}