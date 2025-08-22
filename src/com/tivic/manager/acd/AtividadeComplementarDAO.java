package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AtividadeComplementarDAO{

	public static int insert(AtividadeComplementar objeto) {
		return insert(objeto, null);
	}

	public static int insert(AtividadeComplementar objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_atividade_complementar", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAtividadeComplementar(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_atividade_complementar (cd_atividade_complementar,"+
			                                  "nm_atividade_complementar,"+
			                                  "id_atividade_complementar,"+
			                                  "cd_atividade_superior,"+
			                                  "cd_disciplina) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAtividadeComplementar());
			pstmt.setString(3,objeto.getIdAtividadeComplementar());
			if(objeto.getCdAtividadeSuperior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAtividadeSuperior());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDisciplina());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AtividadeComplementar objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AtividadeComplementar objeto, int cdAtividadeComplementarOld) {
		return update(objeto, cdAtividadeComplementarOld, null);
	}

	public static int update(AtividadeComplementar objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AtividadeComplementar objeto, int cdAtividadeComplementarOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_atividade_complementar SET cd_atividade_complementar=?,"+
												      		   "nm_atividade_complementar=?,"+
												      		   "id_atividade_complementar=?,"+
												      		   "cd_atividade_superior=?,"+
												      		   "cd_disciplina=? WHERE cd_atividade_complementar=?");
			pstmt.setInt(1,objeto.getCdAtividadeComplementar());
			pstmt.setString(2,objeto.getNmAtividadeComplementar());
			pstmt.setString(3,objeto.getIdAtividadeComplementar());
			if(objeto.getCdAtividadeSuperior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAtividadeSuperior());
			if(objeto.getCdDisciplina()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDisciplina());
			pstmt.setInt(6, cdAtividadeComplementarOld!=0 ? cdAtividadeComplementarOld : objeto.getCdAtividadeComplementar());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtividadeComplementar) {
		return delete(cdAtividadeComplementar, null);
	}

	public static int delete(int cdAtividadeComplementar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_atividade_complementar WHERE cd_atividade_complementar=?");
			pstmt.setInt(1, cdAtividadeComplementar);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AtividadeComplementar get(int cdAtividadeComplementar) {
		return get(cdAtividadeComplementar, null);
	}

	public static AtividadeComplementar get(int cdAtividadeComplementar, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_atividade_complementar WHERE cd_atividade_complementar=?");
			pstmt.setInt(1, cdAtividadeComplementar);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AtividadeComplementar(rs.getInt("cd_atividade_complementar"),
						rs.getString("nm_atividade_complementar"),
						rs.getString("id_atividade_complementar"),
						rs.getInt("cd_atividade_superior"),
						rs.getInt("cd_disciplina"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_atividade_complementar");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AtividadeComplementar> getList() {
		return getList(null);
	}

	public static ArrayList<AtividadeComplementar> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AtividadeComplementar> list = new ArrayList<AtividadeComplementar>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AtividadeComplementar obj = AtividadeComplementarDAO.get(rsm.getInt("cd_atividade_complementar"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeComplementarDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_atividade_complementar", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
