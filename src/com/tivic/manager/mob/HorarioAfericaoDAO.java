package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class HorarioAfericaoDAO{

	public static int insert(HorarioAfericao objeto) {
		return insert(objeto, null);
	}

	public static int insert(HorarioAfericao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_horario_afericao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdControle(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_horario_afericao (cd_controle,"+
			                                  "dt_lancamento,"+
			                                  "hr_chegada,"+
			                                  "hr_partida,"+
			                                  "hr_previsto,"+
			                                  "cd_horario,"+
			                                  "cd_tabela_horario,"+
			                                  "cd_linha,"+
			                                  "cd_rota,"+
			                                  "cd_trecho,"+
			                                  "cd_agente,"+
			                                  "cd_concessao_veiculo," +
			                                  "st_horario_afericao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			if(objeto.getHrChegada()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getHrChegada().getTimeInMillis()));
			if(objeto.getHrPartida()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrPartida().getTimeInMillis()));
			if(objeto.getHrPrevisto()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrPrevisto().getTimeInMillis()));
			if(objeto.getCdHorario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdHorario());
			if(objeto.getCdTabelaHorario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTabelaHorario());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdLinha());
			if(objeto.getCdRota()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdRota());
			if(objeto.getCdTrecho()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTrecho());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdAgente());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdConcessaoVeiculo());
			if(objeto.getStHorarioAfericao() == 0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13, objeto.getStHorarioAfericao());
			pstmt.executeUpdate();
			
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(HorarioAfericao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(HorarioAfericao objeto, int cdControleOld) {
		return update(objeto, cdControleOld, null);
	}

	public static int update(HorarioAfericao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(HorarioAfericao objeto, int cdControleOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_horario_afericao SET cd_controle=?,"+
												      		   "dt_lancamento=?,"+
												      		   "hr_chegada=?,"+
												      		   "hr_partida=?,"+
												      		   "hr_previsto=?,"+
												      		   "cd_horario=?,"+
												      		   "cd_tabela_horario=?,"+
												      		   "cd_linha=?,"+
												      		   "cd_rota=?,"+
												      		   "cd_trecho=?,"+
												      		   "cd_agente=?,"+
												      		   "cd_concessao_veiculo=?," +
												      		   "st_horario_afericao=? WHERE cd_controle=?");
			pstmt.setInt(1,objeto.getCdControle());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			if(objeto.getHrChegada()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getHrChegada().getTimeInMillis()));
			if(objeto.getHrPartida()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrPartida().getTimeInMillis()));
			if(objeto.getHrPrevisto()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrPrevisto().getTimeInMillis()));
			if(objeto.getCdHorario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdHorario());
			if(objeto.getCdTabelaHorario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTabelaHorario());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdLinha());
			if(objeto.getCdRota()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdRota());
			if(objeto.getCdTrecho()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTrecho());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdAgente());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdConcessaoVeiculo());
			if(objeto.getStHorarioAfericao() == 0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13, objeto.getStHorarioAfericao());
			pstmt.setInt(14, cdControleOld!=0 ? cdControleOld : objeto.getCdControle());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdControle) {
		return delete(cdControle, null);
	}

	public static int delete(int cdControle, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_horario_afericao WHERE cd_controle=?");
			pstmt.setInt(1, cdControle);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HorarioAfericao get(int cdControle) {
		return get(cdControle, null);
	}

	public static HorarioAfericao get(int cdControle, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_horario_afericao WHERE cd_controle=?");
			pstmt.setInt(1, cdControle);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new HorarioAfericao(rs.getInt("cd_controle"),
						(rs.getTimestamp("dt_lancamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lancamento").getTime()),
						(rs.getTimestamp("hr_chegada")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_chegada").getTime()),
						(rs.getTimestamp("hr_partida")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_partida").getTime()),
						(rs.getTimestamp("hr_previsto")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_previsto").getTime()),
						rs.getInt("cd_horario"),
						rs.getInt("cd_tabela_horario"),
						rs.getInt("cd_linha"),
						rs.getInt("cd_rota"),
						rs.getInt("cd_trecho"),
						rs.getInt("cd_agente"),
						rs.getInt("cd_concessao_veiculo"),
						rs.getInt("st_horario_afericao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_horario_afericao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<HorarioAfericao> getList() {
		return getList(null);
	}

	public static ArrayList<HorarioAfericao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<HorarioAfericao> list = new ArrayList<HorarioAfericao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				HorarioAfericao obj = HorarioAfericaoDAO.get(rsm.getInt("cd_controle"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioAfericaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_horario_afericao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}