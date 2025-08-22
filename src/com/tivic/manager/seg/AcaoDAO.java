package com.tivic.manager.seg;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;

import java.util.HashMap;
import java.util.ArrayList;

public class AcaoDAO{

	public static int insert(Acao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Acao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_acao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_modulo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdModulo()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_sistema");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdSistema()));
			int code = Conexao.getSequenceCode("seg_acao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAcao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_acao (cd_acao,"+
			                                  "cd_modulo,"+
			                                  "cd_sistema,"+
			                                  "nm_acao,"+
			                                  "ds_acao,"+
			                                  "cd_agrupamento,"+
			                                  "id_acao,"+
			                                  "tp_organizacao,"+
			                                  "nr_ordem,"+
			                                  "cd_acao_superior) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdModulo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdModulo());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSistema());
			pstmt.setString(4,objeto.getNmAcao());
			pstmt.setString(5,objeto.getDsAcao());
			if(objeto.getCdAgrupamento()<=0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAgrupamento());
			pstmt.setString(7,objeto.getIdAcao());
			pstmt.setInt(8,objeto.getTpOrganizacao());
			pstmt.setInt(9,objeto.getNrOrdem());
			if(objeto.getCdAcaoSuperior()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdAcaoSuperior());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Acao objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(Acao objeto, int cdAcaoOld, int cdModuloOld, int cdSistemaOld) {
		return update(objeto, cdAcaoOld, cdModuloOld, cdSistemaOld, null);
	}

	public static int update(Acao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(Acao objeto, int cdAcaoOld, int cdModuloOld, int cdSistemaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_acao SET cd_acao=?,"+
												      		   "cd_modulo=?,"+
												      		   "cd_sistema=?,"+
												      		   "nm_acao=?,"+
												      		   "ds_acao=?,"+
												      		   "cd_agrupamento=?,"+
												      		   "id_acao=?,"+
												      		   "tp_organizacao=?,"+
												      		   "nr_ordem=?,"+
												      		   "cd_acao_superior=? WHERE cd_acao=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1,objeto.getCdAcao());
			pstmt.setInt(2,objeto.getCdModulo());
			pstmt.setInt(3,objeto.getCdSistema());
			pstmt.setString(4,objeto.getNmAcao());
			pstmt.setString(5,objeto.getDsAcao());
			if(objeto.getCdAgrupamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAgrupamento());
			pstmt.setString(7,objeto.getIdAcao());
			pstmt.setInt(8,objeto.getTpOrganizacao());
			pstmt.setInt(9,objeto.getNrOrdem());
			if(objeto.getCdAcaoSuperior()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdAcaoSuperior());
			pstmt.setInt(11, cdAcaoOld!=0 ? cdAcaoOld : objeto.getCdAcao());
			pstmt.setInt(12, cdModuloOld!=0 ? cdModuloOld : objeto.getCdModulo());
			pstmt.setInt(13, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAcao, int cdModulo, int cdSistema) {
		return delete(cdAcao, cdModulo, cdSistema, null);
	}

	public static int delete(int cdAcao, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_acao WHERE cd_acao=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdAcao);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdSistema);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Acao get(int cdAcao, int cdModulo, int cdSistema) {
		return get(cdAcao, cdModulo, cdSistema, null);
	}

	public static Acao get(int cdAcao, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_acao WHERE cd_acao=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdAcao);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Acao(rs.getInt("cd_acao"),
						rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"),
						rs.getString("nm_acao"),
						rs.getString("ds_acao"),
						rs.getInt("cd_agrupamento"),
						rs.getString("id_acao"),
						rs.getInt("tp_organizacao"),
						rs.getInt("nr_ordem"),
						rs.getInt("cd_acao_superior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_acao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Acao> getList() {
		return getList(null);
	}

	public static ArrayList<Acao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Acao> list = new ArrayList<Acao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Acao obj = AcaoDAO.get(rsm.getInt("cd_acao"), rsm.getInt("cd_modulo"), rsm.getInt("cd_sistema"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM seg_acao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
