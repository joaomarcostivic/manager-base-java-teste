package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TurmaAeeDAO{

	public static int insert(TurmaAee objeto) {
		return insert(objeto, null);
	}

	public static int insert(TurmaAee objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_turma_aee (cd_atendimento_especializado,"+
			                                  "cd_turma) VALUES (?, ?)");
			if(objeto.getCdAtendimentoEspecializado()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAtendimentoEspecializado());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTurma());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TurmaAee objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TurmaAee objeto, int cdAtendimentoEspecializadoOld, int cdTurmaOld) {
		return update(objeto, cdAtendimentoEspecializadoOld, cdTurmaOld, null);
	}

	public static int update(TurmaAee objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TurmaAee objeto, int cdAtendimentoEspecializadoOld, int cdTurmaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_turma_aee SET cd_atendimento_especializado=?,"+
												      		   "cd_turma=? WHERE cd_atendimento_especializado=? AND cd_turma=?");
			pstmt.setInt(1,objeto.getCdAtendimentoEspecializado());
			pstmt.setInt(2,objeto.getCdTurma());
			pstmt.setInt(3, cdAtendimentoEspecializadoOld!=0 ? cdAtendimentoEspecializadoOld : objeto.getCdAtendimentoEspecializado());
			pstmt.setInt(4, cdTurmaOld!=0 ? cdTurmaOld : objeto.getCdTurma());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtendimentoEspecializado, int cdTurma) {
		return delete(cdAtendimentoEspecializado, cdTurma, null);
	}

	public static int delete(int cdAtendimentoEspecializado, int cdTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_turma_aee WHERE cd_atendimento_especializado=? AND cd_turma=?");
			pstmt.setInt(1, cdAtendimentoEspecializado);
			pstmt.setInt(2, cdTurma);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TurmaAee get(int cdAtendimentoEspecializado, int cdTurma) {
		return get(cdAtendimentoEspecializado, cdTurma, null);
	}

	public static TurmaAee get(int cdAtendimentoEspecializado, int cdTurma, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma_aee WHERE cd_atendimento_especializado=? AND cd_turma=?");
			pstmt.setInt(1, cdAtendimentoEspecializado);
			pstmt.setInt(2, cdTurma);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TurmaAee(rs.getInt("cd_atendimento_especializado"),
						rs.getInt("cd_turma"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma_aee");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TurmaAee> getList() {
		return getList(null);
	}

	public static ArrayList<TurmaAee> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TurmaAee> list = new ArrayList<TurmaAee>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TurmaAee obj = TurmaAeeDAO.get(rsm.getInt("cd_atendimento_especializado"), rsm.getInt("cd_turma"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaAeeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_turma_aee", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
