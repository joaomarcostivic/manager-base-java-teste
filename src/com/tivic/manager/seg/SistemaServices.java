package com.tivic.manager.seg;

import java.sql.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.*;
import sol.security.SystemServices;
import sol.util.Result;

public class SistemaServices implements SystemServices {


	public static Result save(Sistema sistema){
		return save(sistema, null);
	}

	public static Result save(Sistema sistema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(sistema==null)
				return new Result(-1, "Erro ao salvar. Sistema é nulo");

			int retorno;
			if(sistema.getCdSistema()==0){
				retorno = SistemaDAO.insert(sistema, connect);
				sistema.setCdSistema(retorno);
			}
			else {
				retorno = SistemaDAO.update(sistema, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "SISTEMA", sistema);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdSistema){
		return remove(cdSistema, false, null);
	}
	public static Result remove(int cdSistema, boolean cascade){
		return remove(cdSistema, cascade, null);
	}
	public static Result remove(int cdSistema, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = SistemaDAO.delete(cdSistema, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_sistema");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_sistema", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	@Deprecated
	public static int salvar(Sistema sistema){
		return save(sistema, null).getCode();
	}
	
//	public static int save(Sistema sistema, Connection connect){
//		boolean isConnectionNull = connect==null;
//		try {
//			if (isConnectionNull) {
//				connect = Conexao.conectar();
//				connect.setAutoCommit(false);
//			}
//			if(sistema==null){
//				return -1;
//			}
//
//			int retorno;
//			if(sistema.getCdSistema()==0){
//				retorno = SistemaDAO.insert(sistema, connect);
//			}
//			else{
//				retorno = SistemaDAO.update(sistema, connect);
//				retorno = retorno>0?sistema.getCdSistema():retorno;
//			}
//
//			if(retorno<0)
//				Conexao.rollback(connect);
//			else if (isConnectionNull)
//				connect.commit();
//
//			return retorno;
//		}
//		catch(SQLException sqlExpt){
//			sqlExpt.printStackTrace(java.lang.System.out);
//			java.lang.System.err.println("Erro! SistemaServices.save: " + sqlExpt);
//			if (isConnectionNull)
//				Conexao.rollback(connect);
//			return (-1)*sqlExpt.getErrorCode();
//		}
//		catch(Exception e){
//			e.printStackTrace(java.lang.System.out);
//			java.lang.System.err.println("Erro! SistemaServices.save: " +  e);
//			if (isConnectionNull)
//				Conexao.rollback(connect);
//			return  -1;
//		}
//		finally{
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
//	}

	public int insertSystem(String name, String sigle) {
		return SistemaDAO.insert(new Sistema(0,
				name,
				sigle,
				1/*lgAtivo*/));
	}

	public int updateSystem(int id, String name, String sigle) {
		return SistemaDAO.update(new Sistema(id,
				name,
				sigle,
				1/*lgAtivo*/));
	}

	public int deleteSystem(int id) {
		return SistemaDAO.delete(id);
	}
	
	public ResultSetMap getSystems() {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT CD_SISTEMA AS ID_SYSTEM, NM_SISTEMA AS NAME_SYSTEM, ID_SISTEMA AS SIGLE_SYSTEM FROM SEG_SISTEMA");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (connect != null)
				Conexao.desconectar(connect);
		}
	}

	public ResultSetMap getSistemasAndModulos() {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_modulo WHERE cd_sistema = ? ORDER BY ID_MODULO");
			ResultSetMap rsmSistema = new ResultSetMap(connect.prepareStatement("SELECT * FROM seg_sistema").executeQuery());
			while(rsmSistema.next())	{
				pstmt.setInt(1, rsmSistema.getInt("cd_sistema"));
				rsmSistema.getRegister().put("subResultSetMap", new ResultSetMap(pstmt.executeQuery()));
			}
			rsmSistema.beforeFirst();
			return rsmSistema;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (connect != null)
				Conexao.desconectar(connect);
		}
	}

	public static sol.security.System findSystem(int idSystem) {
		return findSystem(idSystem, null);
	}

	public static sol.security.System findSystem(int idSystem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			Sistema sistema = SistemaDAO.get(idSystem);
			return sistema==null ? null : new sol.security.System(sistema.getCdSistema(), sistema.getNmSistema(), sistema.getIdSistema());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static sol.security.System findSystemBySg(String sgSystem) {
		return findSystemBySg(sgSystem, null);
	}

	public static sol.security.System findSystemBySg(String sgSystem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("ID_SISTEMA", sgSystem, ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap sistemas = SistemaDAO.find(criterios);
			return sistemas==null || !sistemas.next() ? null : new sol.security.System(sistemas.getInt("CD_SISTEMA"), sistemas.getString("NM_SISTEMA"), sistemas.getString("ID_SISTEMA"));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Object[] getModulosAndForms(int cdSistema) {
		return getModulosAndForms(cdSistema, null);
	}

	public static Object[] getModulosAndForms(int cdSistema, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			Object[] modulosAndForms = new Object[2];
			modulosAndForms[0] = ModuloServices.getModulesOfSystem(cdSistema, connection);
			modulosAndForms[1] = FormularioServices.getFormsOfSystem(cdSistema, connection);
			return modulosAndForms;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	public static Sistema getById(String idSistema) {
		return getById(idSistema, null);
	}

	public static Sistema getById(String idSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_sistema WHERE id_sistema=?");
			pstmt.setString(1, idSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Sistema(rs.getInt("cd_sistema"),
						rs.getString("nm_sistema"),
						rs.getString("id_sistema"),
						rs.getInt("lg_ativo"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaServices.getById: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Sistema getByCod(int cdSistema) {
		return getByCod(cdSistema, null);
	}

	public static Sistema getByCod(int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_sistema WHERE cd_sistema=?");
			pstmt.setInt(1, cdSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Sistema(rs.getInt("cd_sistema"),
						rs.getString("nm_sistema"),
						rs.getString("id_sistema"),
						rs.getInt("lg_ativo"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaServices.getById: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void main(String args[]){
		
	}
}