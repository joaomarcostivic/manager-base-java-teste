package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AtendenteDAO{

	public static int insert(Atendente objeto) {
		return insert(objeto, null);
	}

	public static int insert(Atendente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_atendente (cd_central,"+
			                                  "cd_usuario,"+
			                                  "nm_login_im,"+
			                                  "nm_senha_im,"+
			                                  "nm_apelido_im," +
			                                  "st_atendente) VALUES (?, ?, ?, ?, ?, 1)");
			if(objeto.getCdCentral()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCentral());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setString(3,objeto.getNmLoginIm());
			pstmt.setString(4,objeto.getNmSenhaIm());
			pstmt.setString(5,objeto.getNmApelidoIm());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Atendente objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Atendente objeto, int cdCentralOld, int cdUsuarioOld) {
		return update(objeto, cdCentralOld, cdUsuarioOld, null);
	}

	public static int update(Atendente objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Atendente objeto, int cdCentralOld, int cdUsuarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_atendente SET cd_central=?,"+
												      		   "cd_usuario=?,"+
												      		   "nm_login_im=?,"+
												      		   "nm_senha_im=?,"+
												      		   "nm_apelido_im=? WHERE cd_central=? AND cd_usuario=?");
			pstmt.setInt(1,objeto.getCdCentral());
			pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setString(3,objeto.getNmLoginIm());
			pstmt.setString(4,objeto.getNmSenhaIm());
			pstmt.setString(5,objeto.getNmApelidoIm());
			pstmt.setInt(6, cdCentralOld!=0 ? cdCentralOld : objeto.getCdCentral());
			pstmt.setInt(7, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCentral, int cdUsuario) {
		return delete(cdCentral, cdUsuario, null);
	}

	public static int delete(int cdCentral, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return connect.prepareStatement("DELETE FROM crm_atendente WHERE cd_central="+cdCentral+" AND cd_usuario="+cdUsuario).executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Atendente get(int cdCentral, int cdUsuario) {
		return get(cdCentral, cdUsuario, null);
	}

	public static Atendente get(int cdCentral, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSet rs = connect.prepareStatement("SELECT * FROM crm_atendente WHERE cd_central="+cdCentral+" AND cd_usuario="+cdUsuario).executeQuery();
			
			if(rs.next())
				return new Atendente(rs.getInt("cd_central"),rs.getInt("cd_usuario"), rs.getString("nm_login_im"), rs.getString("nm_senha_im"), rs.getString("nm_apelido_im"));
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM crm_atendente").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM crm_atendente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
