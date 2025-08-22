package com.tivic.manager.seg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class AgrupamentoAcaoDAO{

	public static int insert(AgrupamentoAcao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(AgrupamentoAcao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_agrupamento");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_modulo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdModulo()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_sistema");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdSistema()));
			int code = Conexao.getSequenceCode("seg_agrupamento_acao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAgrupamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_agrupamento_acao (cd_agrupamento,"+
			                                  "cd_modulo,"+
			                                  "cd_sistema,"+
			                                  "nm_agrupamento,"+
			                                  "id_agrupamento,"+
			                                  "ds_agrupamento,"+
			                                  "cd_agrupamento_superior,"+
			                                  "lg_ativo,"+
			                                  "nr_ordem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdModulo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdModulo());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSistema());
			pstmt.setString(4,objeto.getNmAgrupamento());
			pstmt.setString(5,objeto.getIdAgrupamento());
			pstmt.setString(6,objeto.getDsAgrupamento());
			if(objeto.getCdAgrupamentoSuperior()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdAgrupamentoSuperior());
			pstmt.setInt(8,objeto.getLgAtivo());
			pstmt.setInt(9,objeto.getNrOrdem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgrupamentoAcao objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(AgrupamentoAcao objeto, int cdAgrupamentoOld, int cdModuloOld, int cdSistemaOld) {
		return update(objeto, cdAgrupamentoOld, cdModuloOld, cdSistemaOld, null);
	}

	public static int update(AgrupamentoAcao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(AgrupamentoAcao objeto, int cdAgrupamentoOld, int cdModuloOld, int cdSistemaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_agrupamento_acao SET cd_agrupamento=?,"+
												      		   "cd_modulo=?,"+
												      		   "cd_sistema=?,"+
												      		   "nm_agrupamento=?,"+
												      		   "id_agrupamento=?,"+
												      		   "ds_agrupamento=?,"+
												      		   "cd_agrupamento_superior=?,"+
												      		   "lg_ativo=?,"+
												      		   "nr_ordem=? WHERE cd_agrupamento=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1,objeto.getCdAgrupamento());
			pstmt.setInt(2,objeto.getCdModulo());
			pstmt.setInt(3,objeto.getCdSistema());
			pstmt.setString(4,objeto.getNmAgrupamento());
			pstmt.setString(5,objeto.getIdAgrupamento());
			pstmt.setString(6,objeto.getDsAgrupamento());
			if(objeto.getCdAgrupamentoSuperior()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdAgrupamentoSuperior());
			pstmt.setInt(8,objeto.getLgAtivo());
			pstmt.setInt(9,objeto.getNrOrdem());
			pstmt.setInt(10, cdAgrupamentoOld!=0 ? cdAgrupamentoOld : objeto.getCdAgrupamento());
			pstmt.setInt(11, cdModuloOld!=0 ? cdModuloOld : objeto.getCdModulo());
			pstmt.setInt(12, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgrupamento, int cdModulo, int cdSistema) {
		return delete(cdAgrupamento, cdModulo, cdSistema, null);
	}

	public static int delete(int cdAgrupamento, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_agrupamento_acao WHERE cd_agrupamento=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdAgrupamento);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdSistema);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgrupamentoAcao get(int cdAgrupamento, int cdModulo, int cdSistema) {
		return get(cdAgrupamento, cdModulo, cdSistema, null);
	}

	public static AgrupamentoAcao get(int cdAgrupamento, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_agrupamento_acao WHERE cd_agrupamento=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdAgrupamento);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgrupamentoAcao(rs.getInt("cd_agrupamento"),
						rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"),
						rs.getString("nm_agrupamento"),
						rs.getString("id_agrupamento"),
						rs.getString("ds_agrupamento"),
						rs.getInt("cd_agrupamento_superior"),
						rs.getInt("lg_ativo"),
						rs.getInt("nr_ordem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_agrupamento_acao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AgrupamentoAcao> getList() {
		return getList(null);
	}

	public static ArrayList<AgrupamentoAcao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AgrupamentoAcao> list = new ArrayList<AgrupamentoAcao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AgrupamentoAcao obj = AgrupamentoAcaoDAO.get(rsm.getInt("cd_agrupamento"), rsm.getInt("cd_modulo"), rsm.getInt("cd_sistema"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgrupamentoAcaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM seg_agrupamento_acao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}