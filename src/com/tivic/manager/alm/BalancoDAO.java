package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BalancoDAO{

	public static int insert(Balanco objeto) {
		return insert(objeto, null);
	}

	public static int insert(Balanco objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			@SuppressWarnings("unchecked")
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_balanco");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_empresa");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdEmpresa()));
			int code = Conexao.getSequenceCode("alm_balanco", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdBalanco(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_balanco (cd_balanco,"+
			                                  "cd_empresa,"+
			                                  "dt_balanco,"+
			                                  "txt_balanco,"+
			                                  "st_balanco,"+
			                                  "cd_usuario,"+
			                                  "dt_fechamento,"+
			                                  "nr_balanco,"+
			                                  "cd_pessoa," +
			                                  "tp_balanco," +
			                                  "cd_local_armazenamento," +
			                                  "cd_grupo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getDtBalanco()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtBalanco().getTimeInMillis()));
			pstmt.setString(4,objeto.getTxtBalanco());
			pstmt.setInt(5,objeto.getStBalanco());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuario());
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			pstmt.setString(8,objeto.getNrBalanco());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdPessoa());
			pstmt.setInt(10,objeto.getTpBalanco());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdLocalArmazenamento());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdGrupo());
			pstmt.executeUpdate();
			return code;
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

	public static int update(Balanco objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Balanco objeto, int cdBalancoOld, int cdEmpresaOld) {
		return update(objeto, cdBalancoOld, cdEmpresaOld, null);
	}

	public static int update(Balanco objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Balanco objeto, int cdBalancoOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_balanco SET cd_balanco=?,"+
												      		   "cd_empresa=?,"+
												      		   "dt_balanco=?,"+
												      		   "txt_balanco=?,"+
												      		   "st_balanco=?,"+
												      		   "cd_usuario=?,"+
												      		   "dt_fechamento=?,"+
												      		   "nr_balanco=?,"+
												      		   "cd_pessoa=?, " +
												      		   "tp_balanco=?," +
												      		   "cd_local_armazenamento=?," +
												      		   "cd_grupo=? WHERE cd_balanco=? AND cd_empresa=?");
			pstmt.setInt(1,objeto.getCdBalanco());
			pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getDtBalanco()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtBalanco().getTimeInMillis()));
			pstmt.setString(4,objeto.getTxtBalanco());
			pstmt.setInt(5,objeto.getStBalanco());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuario());
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			pstmt.setString(8,objeto.getNrBalanco());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdPessoa());
			pstmt.setInt(10,objeto.getTpBalanco());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdLocalArmazenamento());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdGrupo());
			pstmt.setInt(13, cdBalancoOld!=0 ? cdBalancoOld : objeto.getCdBalanco());
			pstmt.setInt(14, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBalanco, int cdEmpresa) {
		return delete(cdBalanco, cdEmpresa, null);
	}

	public static int delete(int cdBalanco, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_balanco WHERE cd_balanco=? AND cd_empresa=?");
			pstmt.setInt(1, cdBalanco);
			pstmt.setInt(2, cdEmpresa);
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

	public static Balanco get(int cdBalanco, int cdEmpresa) {
		return get(cdBalanco, cdEmpresa, null);
	}

	public static Balanco get(int cdBalanco, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_balanco WHERE cd_balanco=? AND cd_empresa=?");
			pstmt.setInt(1, cdBalanco);
			pstmt.setInt(2, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Balanco(rs.getInt("cd_balanco"),
						rs.getInt("cd_empresa"),
						(rs.getTimestamp("dt_balanco")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_balanco").getTime()),
						rs.getString("txt_balanco"),
						rs.getInt("st_balanco"),
						rs.getInt("cd_usuario"),
						(rs.getTimestamp("dt_fechamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fechamento").getTime()),
						rs.getString("nr_balanco"),
						rs.getInt("cd_pessoa"),
						rs.getInt("tp_balanco"),
						rs.getInt("cd_local_armazenamento"),
						rs.getInt("cd_grupo"));
			}
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
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_balanco");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BalancoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_balanco", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
