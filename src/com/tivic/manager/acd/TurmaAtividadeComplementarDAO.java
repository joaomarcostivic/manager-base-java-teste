package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TurmaAtividadeComplementarDAO{

	public static int insert(TurmaAtividadeComplementar objeto) {
		return insert(objeto, null);
	}

	public static int insert(TurmaAtividadeComplementar objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_turma_atividade_complementar (cd_atividade_complementar,"+
			                                  "cd_turma) VALUES (?, ?)");
			if(objeto.getCdAtividadeComplementar()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAtividadeComplementar());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTurma());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TurmaAtividadeComplementar objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TurmaAtividadeComplementar objeto, int cdAtividadeComplementarOld, int cdTurmaOld) {
		return update(objeto, cdAtividadeComplementarOld, cdTurmaOld, null);
	}

	public static int update(TurmaAtividadeComplementar objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TurmaAtividadeComplementar objeto, int cdAtividadeComplementarOld, int cdTurmaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_turma_atividade_complementar SET cd_atividade_complementar=?,"+
												      		   "cd_turma=? WHERE cd_atividade_complementar=? AND cd_turma=?");
			pstmt.setInt(1,objeto.getCdAtividadeComplementar());
			pstmt.setInt(2,objeto.getCdTurma());
			pstmt.setInt(3, cdAtividadeComplementarOld!=0 ? cdAtividadeComplementarOld : objeto.getCdAtividadeComplementar());
			pstmt.setInt(4, cdTurmaOld!=0 ? cdTurmaOld : objeto.getCdTurma());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtividadeComplementar, int cdTurma) {
		return delete(cdAtividadeComplementar, cdTurma, null);
	}

	public static int delete(int cdAtividadeComplementar, int cdTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_turma_atividade_complementar WHERE cd_atividade_complementar=? AND cd_turma=?");
			pstmt.setInt(1, cdAtividadeComplementar);
			pstmt.setInt(2, cdTurma);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TurmaAtividadeComplementar get(int cdAtividadeComplementar, int cdTurma) {
		return get(cdAtividadeComplementar, cdTurma, null);
	}

	public static TurmaAtividadeComplementar get(int cdAtividadeComplementar, int cdTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma_atividade_complementar WHERE cd_atividade_complementar=? AND cd_turma=?");
			pstmt.setInt(1, cdAtividadeComplementar);
			pstmt.setInt(2, cdTurma);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TurmaAtividadeComplementar(rs.getInt("cd_atividade_complementar"),
						rs.getInt("cd_turma"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma_atividade_complementar");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TurmaAtividadeComplementar> getList() {
		return getList(null);
	}

	public static ArrayList<TurmaAtividadeComplementar> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TurmaAtividadeComplementar> list = new ArrayList<TurmaAtividadeComplementar>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TurmaAtividadeComplementar obj = TurmaAtividadeComplementarDAO.get(rsm.getInt("cd_atividade_complementar"), rsm.getInt("cd_turma"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAtividadeComplementarDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_turma_atividade_complementar", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
