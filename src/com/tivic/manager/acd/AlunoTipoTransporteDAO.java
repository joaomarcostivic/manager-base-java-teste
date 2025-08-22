package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AlunoTipoTransporteDAO{

	public static int insert(AlunoTipoTransporte objeto) {
		return insert(objeto, null);
	}

	public static int insert(AlunoTipoTransporte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_aluno_tipo_transporte (cd_aluno,"+
			                                  "cd_tipo_transporte) VALUES (?, ?)");
			if(objeto.getCdAluno()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAluno());
			if(objeto.getCdTipoTransporte()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoTransporte());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AlunoTipoTransporte objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AlunoTipoTransporte objeto, int cdAlunoOld, int cdTipoTransporteOld) {
		return update(objeto, cdAlunoOld, cdTipoTransporteOld, null);
	}

	public static int update(AlunoTipoTransporte objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AlunoTipoTransporte objeto, int cdAlunoOld, int cdTipoTransporteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_aluno_tipo_transporte SET cd_aluno=?,"+
												      		   "cd_tipo_transporte=? WHERE cd_aluno=? AND cd_tipo_transporte=?");
			pstmt.setInt(1,objeto.getCdAluno());
			pstmt.setInt(2,objeto.getCdTipoTransporte());
			pstmt.setInt(3, cdAlunoOld!=0 ? cdAlunoOld : objeto.getCdAluno());
			pstmt.setInt(4, cdTipoTransporteOld!=0 ? cdTipoTransporteOld : objeto.getCdTipoTransporte());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAluno, int cdTipoTransporte) {
		return delete(cdAluno, cdTipoTransporte, null);
	}

	public static int delete(int cdAluno, int cdTipoTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_aluno_tipo_transporte WHERE cd_aluno=? AND cd_tipo_transporte=?");
			pstmt.setInt(1, cdAluno);
			pstmt.setInt(2, cdTipoTransporte);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AlunoTipoTransporte get(int cdAluno, int cdTipoTransporte) {
		return get(cdAluno, cdTipoTransporte, null);
	}

	public static AlunoTipoTransporte get(int cdAluno, int cdTipoTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_tipo_transporte WHERE cd_aluno=? AND cd_tipo_transporte=?");
			pstmt.setInt(1, cdAluno);
			pstmt.setInt(2, cdTipoTransporte);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AlunoTipoTransporte(rs.getInt("cd_aluno"),
						rs.getInt("cd_tipo_transporte"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_tipo_transporte");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AlunoTipoTransporte> getList() {
		return getList(null);
	}

	public static ArrayList<AlunoTipoTransporte> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AlunoTipoTransporte> list = new ArrayList<AlunoTipoTransporte>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AlunoTipoTransporte obj = AlunoTipoTransporteDAO.get(rsm.getInt("cd_aluno"), rsm.getInt("cd_tipo_transporte"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoTipoTransporteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_aluno_tipo_transporte", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
