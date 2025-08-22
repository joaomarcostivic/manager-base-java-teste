package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class ExameDAO{

	public static int insert(Exame objeto) {
		return insert(objeto, null);
	}

	public static int insert(Exame objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_exame");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_tipo_exame");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdTipoExame()));
			int code = Conexao.getSequenceCode("mob_exame", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdExame(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_exame (cd_exame,"+
			                                  "cd_tipo_exame,"+
			                                  "cd_rrd,"+
			                                  "txt_laudo,"+
			                                  "cd_arquivo,"+
			                                  "cd_trrav) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoExame()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoExame());
			if(objeto.getCdRrd()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdRrd());
			pstmt.setString(4,objeto.getTxtLaudo());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdArquivo());
			if(objeto.getCdTrrav()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTrrav());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Exame objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Exame objeto, int cdExameOld, int cdTipoExameOld) {
		return update(objeto, cdExameOld, cdTipoExameOld, null);
	}

	public static int update(Exame objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Exame objeto, int cdExameOld, int cdTipoExameOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_exame SET cd_exame=?,"+
												      		   "cd_tipo_exame=?,"+
												      		   "cd_rrd=?,"+
												      		   "txt_laudo=?,"+
												      		   "cd_arquivo=?,"+
												      		   "cd_trrav=? WHERE cd_exame=? AND cd_tipo_exame=?");
			pstmt.setInt(1,objeto.getCdExame());
			pstmt.setInt(2,objeto.getCdTipoExame());
			if(objeto.getCdRrd()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdRrd());
			pstmt.setString(4,objeto.getTxtLaudo());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdArquivo());
			if(objeto.getCdTrrav()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTrrav());
			pstmt.setInt(7, cdExameOld!=0 ? cdExameOld : objeto.getCdExame());
			pstmt.setInt(8, cdTipoExameOld!=0 ? cdTipoExameOld : objeto.getCdTipoExame());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdExame, int cdTipoExame) {
		return delete(cdExame, cdTipoExame, null);
	}

	public static int delete(int cdExame, int cdTipoExame, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_exame WHERE cd_exame=? AND cd_tipo_exame=?");
			pstmt.setInt(1, cdExame);
			pstmt.setInt(2, cdTipoExame);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Exame get(int cdExame, int cdTipoExame) {
		return get(cdExame, cdTipoExame, null);
	}

	public static Exame get(int cdExame, int cdTipoExame, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_exame WHERE cd_exame=? AND cd_tipo_exame=?");
			pstmt.setInt(1, cdExame);
			pstmt.setInt(2, cdTipoExame);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Exame(rs.getInt("cd_exame"),
						rs.getInt("cd_tipo_exame"),
						rs.getInt("cd_rrd"),
						rs.getString("txt_laudo"),
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_trrav"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_exame");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Exame> getList() {
		return getList(null);
	}

	public static ArrayList<Exame> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Exame> list = new ArrayList<Exame>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Exame obj = ExameDAO.get(rsm.getInt("cd_exame"), rsm.getInt("cd_tipo_exame"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExameDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_exame", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}