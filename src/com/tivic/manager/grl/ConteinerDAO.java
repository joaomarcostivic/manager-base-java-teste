package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ConteinerDAO{

	public static int insert(Conteiner objeto) {
		return insert(objeto, null);
	}

	public static int insert(Conteiner objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_conteiner", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConteiner(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_conteiner (cd_conteiner,"+
			                                  "cd_conteiner_pai,"+
			                                  "nm_conteiner,"+
			                                  "cd_usuario,"+
			                                  "cd_empresa,"+
			                                  "cd_tipo_conteiner,"+
			                                  "vl_capacidade,"+
			                                  "id_conteiner) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConteinerPai()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConteinerPai());
			pstmt.setString(3,objeto.getNmConteiner());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdTipoConteiner()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoConteiner());
			pstmt.setFloat(7,objeto.getVlCapacidade());
			pstmt.setString(8,objeto.getIdConteiner());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConteinerDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Conteiner objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Conteiner objeto, int cdConteinerOld) {
		return update(objeto, cdConteinerOld, null);
	}

	public static int update(Conteiner objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Conteiner objeto, int cdConteinerOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_conteiner SET cd_conteiner=?,"+
												      		   "cd_conteiner_pai=?,"+
												      		   "nm_conteiner=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_tipo_conteiner=?,"+
												      		   "vl_capacidade=?,"+
												      		   "id_conteiner=? WHERE cd_conteiner=?");
			pstmt.setInt(1,objeto.getCdConteiner());
			if(objeto.getCdConteinerPai()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConteinerPai());
			pstmt.setString(3,objeto.getNmConteiner());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdTipoConteiner()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoConteiner());
			pstmt.setFloat(7,objeto.getVlCapacidade());
			pstmt.setString(8,objeto.getIdConteiner());
			pstmt.setInt(9, cdConteinerOld!=0 ? cdConteinerOld : objeto.getCdConteiner());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConteinerDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConteiner) {
		return delete(cdConteiner, null);
	}

	public static int delete(int cdConteiner, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_conteiner WHERE cd_conteiner=?");
			pstmt.setInt(1, cdConteiner);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConteinerDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Conteiner get(int cdConteiner) {
		return get(cdConteiner, null);
	}

	public static Conteiner get(int cdConteiner, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_conteiner WHERE cd_conteiner=?");
			pstmt.setInt(1, cdConteiner);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Conteiner(rs.getInt("cd_conteiner"),
						rs.getInt("cd_conteiner_pai"),
						rs.getString("nm_conteiner"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_tipo_conteiner"),
						rs.getFloat("vl_capacidade"),
						rs.getString("id_conteiner"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConteinerDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_conteiner");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConteinerDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_conteiner", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
