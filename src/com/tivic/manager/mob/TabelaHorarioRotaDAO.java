package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TabelaHorarioRotaDAO{

	public static int insert(TabelaHorarioRota objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaHorarioRota objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_tabela_horario_rota (cd_linha,"+
			                                  "cd_tabela_horario,"+
			                                  "cd_rota,"+
			                                  "cd_variacao) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1,objeto.getCdLinha());
			pstmt.setInt(2,objeto.getCdTabelaHorario());
			pstmt.setInt(3,objeto.getCdRota());
			if(objeto.getCdVariacao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdVariacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaHorarioRota objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(TabelaHorarioRota objeto, int cdLinhaOld, int cdTabelaHorarioOld, int cdRotaOld) {
		return update(objeto, cdLinhaOld, cdTabelaHorarioOld, cdRotaOld, null);
	}

	public static int update(TabelaHorarioRota objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(TabelaHorarioRota objeto, int cdLinhaOld, int cdTabelaHorarioOld, int cdRotaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_tabela_horario_rota SET cd_linha=?,"+
												      		   "cd_tabela_horario=?,"+
												      		   "cd_rota=?,"+
												      		   "cd_variacao=? WHERE cd_linha=? AND cd_tabela_horario=? AND cd_rota=?");
			pstmt.setInt(1,objeto.getCdLinha());
			pstmt.setInt(2,objeto.getCdTabelaHorario());
			pstmt.setInt(3,objeto.getCdRota());
			if(objeto.getCdVariacao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdVariacao());
			pstmt.setInt(5, cdLinhaOld!=0 ? cdLinhaOld : objeto.getCdLinha());
			pstmt.setInt(6, cdTabelaHorarioOld!=0 ? cdTabelaHorarioOld : objeto.getCdTabelaHorario());
			pstmt.setInt(7, cdRotaOld!=0 ? cdRotaOld : objeto.getCdRota());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLinha, int cdTabelaHorario, int cdRota) {
		return delete(cdLinha, cdTabelaHorario, cdRota, null);
	}

	public static int delete(int cdLinha, int cdTabelaHorario, int cdRota, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_tabela_horario_rota WHERE cd_linha=? AND cd_tabela_horario=? AND cd_rota=?");
			pstmt.setInt(1, cdLinha);
			pstmt.setInt(2, cdTabelaHorario);
			pstmt.setInt(3, cdRota);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaHorarioRota get(int cdLinha, int cdTabelaHorario, int cdRota) {
		return get(cdLinha, cdTabelaHorario, cdRota, null);
	}

	public static TabelaHorarioRota get(int cdLinha, int cdTabelaHorario, int cdRota, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_tabela_horario_rota WHERE cd_linha=? AND cd_tabela_horario=? AND cd_rota=?");
			pstmt.setInt(1, cdLinha);
			pstmt.setInt(2, cdTabelaHorario);
			pstmt.setInt(3, cdRota);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaHorarioRota(rs.getInt("cd_linha"),
						rs.getInt("cd_tabela_horario"),
						rs.getInt("cd_rota"),
						rs.getInt("cd_variacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tabela_horario_rota");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TabelaHorarioRota> getList() {
		return getList(null);
	}

	public static ArrayList<TabelaHorarioRota> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TabelaHorarioRota> list = new ArrayList<TabelaHorarioRota>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TabelaHorarioRota obj = TabelaHorarioRotaDAO.get(rsm.getInt("cd_linha"), rsm.getInt("cd_tabela_horario"), rsm.getInt("cd_rota"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioRotaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_tabela_horario_rota", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}