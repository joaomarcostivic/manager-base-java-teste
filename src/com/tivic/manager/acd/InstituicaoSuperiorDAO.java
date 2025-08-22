package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InstituicaoSuperiorDAO{

	public static int insert(InstituicaoSuperior objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoSuperior objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_instituicao_superior", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdInstituicaoSuperior(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_superior (cd_instituicao_superior,"+
			                                  "id_instituicao_superior,"+
			                                  "nm_instituicao_superior,"+
			                                  "cd_cidade,"+
			                                  "tp_dependencia_administrativa,"+
			                                  "tp_instituicao_superior,"+
			                                  "st_funcionamento) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdInstituicaoSuperior());
			pstmt.setString(3,objeto.getNmInstituicaoSuperior());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCidade());
			pstmt.setInt(5,objeto.getTpDependenciaAdministrativa());
			pstmt.setInt(6,objeto.getTpInstituicaoSuperior());
			pstmt.setInt(7,objeto.getStFuncionamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoSuperior objeto) {
		return update(objeto, 0, null);
	}

	public static int update(InstituicaoSuperior objeto, int cdInstituicaoSuperiorOld) {
		return update(objeto, cdInstituicaoSuperiorOld, null);
	}

	public static int update(InstituicaoSuperior objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(InstituicaoSuperior objeto, int cdInstituicaoSuperiorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_superior SET cd_instituicao_superior=?,"+
												      		   "id_instituicao_superior=?,"+
												      		   "nm_instituicao_superior=?,"+
												      		   "cd_cidade=?,"+
												      		   "tp_dependencia_administrativa=?,"+
												      		   "tp_instituicao_superior=?,"+
												      		   "st_funcionamento=? WHERE cd_instituicao_superior=?");
			pstmt.setInt(1,objeto.getCdInstituicaoSuperior());
			pstmt.setString(2,objeto.getIdInstituicaoSuperior());
			pstmt.setString(3,objeto.getNmInstituicaoSuperior());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCidade());
			pstmt.setInt(5,objeto.getTpDependenciaAdministrativa());
			pstmt.setInt(6,objeto.getTpInstituicaoSuperior());
			pstmt.setInt(7,objeto.getStFuncionamento());
			pstmt.setInt(8, cdInstituicaoSuperiorOld!=0 ? cdInstituicaoSuperiorOld : objeto.getCdInstituicaoSuperior());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicaoSuperior) {
		return delete(cdInstituicaoSuperior, null);
	}

	public static int delete(int cdInstituicaoSuperior, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_superior WHERE cd_instituicao_superior=?");
			pstmt.setInt(1, cdInstituicaoSuperior);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoSuperior get(int cdInstituicaoSuperior) {
		return get(cdInstituicaoSuperior, null);
	}

	public static InstituicaoSuperior get(int cdInstituicaoSuperior, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_superior WHERE cd_instituicao_superior=?");
			pstmt.setInt(1, cdInstituicaoSuperior);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoSuperior(rs.getInt("cd_instituicao_superior"),
						rs.getString("id_instituicao_superior"),
						rs.getString("nm_instituicao_superior"),
						rs.getInt("cd_cidade"),
						rs.getInt("tp_dependencia_administrativa"),
						rs.getInt("tp_instituicao_superior"),
						rs.getInt("st_funcionamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_superior");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoSuperior> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoSuperior> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoSuperior> list = new ArrayList<InstituicaoSuperior>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoSuperior obj = InstituicaoSuperiorDAO.get(rsm.getInt("cd_instituicao_superior"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoSuperiorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_superior", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
