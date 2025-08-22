package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class EstadoOrgaoServicoDAO{

	public static int insert(EstadoOrgaoServico objeto) {
		return insert(objeto, null);
	}

	public static int insert(EstadoOrgaoServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_estado");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_orgao");
			keys[1].put("IS_KEY_NATIVE", "YES");
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_produto_servico");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdProdutoServico()));
			int code = Conexao.getSequenceCode("prc_estado_orgao_servico", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_estado_orgao_servico (cd_estado,"+
			                                  "cd_orgao,"+
			                                  "cd_produto_servico,"+
			                                  "vl_servico) VALUES (?, ?, ?, ?)");
			if(objeto.getCdEstado()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEstado());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOrgao());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setDouble(4,objeto.getVlServico());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EstadoOrgaoServico objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(EstadoOrgaoServico objeto, int cdEstadoOld, int cdOrgaoOld, int cdProdutoServicoOld) {
		return update(objeto, cdEstadoOld, cdOrgaoOld, cdProdutoServicoOld, null);
	}

	public static int update(EstadoOrgaoServico objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(EstadoOrgaoServico objeto, int cdEstadoOld, int cdOrgaoOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_estado_orgao_servico SET cd_estado=?,"+
												      		   "cd_orgao=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "vl_servico=? WHERE cd_estado=? AND cd_orgao=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdEstado());
			pstmt.setInt(2,objeto.getCdOrgao());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setDouble(4,objeto.getVlServico());
			pstmt.setInt(5, cdEstadoOld!=0 ? cdEstadoOld : objeto.getCdEstado());
			pstmt.setInt(6, cdOrgaoOld!=0 ? cdOrgaoOld : objeto.getCdOrgao());
			pstmt.setInt(7, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEstado, int cdOrgao, int cdProdutoServico) {
		return delete(cdEstado, cdOrgao, cdProdutoServico, null);
	}

	public static int delete(int cdEstado, int cdOrgao, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_estado_orgao_servico WHERE cd_estado=? AND cd_orgao=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdEstado);
			pstmt.setInt(2, cdOrgao);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EstadoOrgaoServico get(int cdEstado, int cdOrgao, int cdProdutoServico) {
		return get(cdEstado, cdOrgao, cdProdutoServico, null);
	}

	public static EstadoOrgaoServico get(int cdEstado, int cdOrgao, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_estado_orgao_servico WHERE cd_estado=? AND cd_orgao=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdEstado);
			pstmt.setInt(2, cdOrgao);
			pstmt.setInt(3, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EstadoOrgaoServico(rs.getInt("cd_estado"),
						rs.getInt("cd_orgao"),
						rs.getInt("cd_produto_servico"),
						rs.getDouble("vl_servico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_estado_orgao_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EstadoOrgaoServico> getList() {
		return getList(null);
	}

	public static ArrayList<EstadoOrgaoServico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EstadoOrgaoServico> list = new ArrayList<EstadoOrgaoServico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EstadoOrgaoServico obj = EstadoOrgaoServicoDAO.get(rsm.getInt("cd_estado"), rsm.getInt("cd_orgao"), rsm.getInt("cd_produto_servico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServicoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_estado_orgao_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}