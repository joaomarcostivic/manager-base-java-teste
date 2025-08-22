package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MovimentoContaCategoriaDAO{

	public static int insert(MovimentoContaCategoria objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(MovimentoContaCategoria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[4];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_conta");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdConta()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_movimento_conta");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdMovimentoConta()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_categoria_economica");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdCategoriaEconomica()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_movimento_conta_categoria");
			keys[3].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_movimento_conta_categoria", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMovimentoContaCategoria(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_movimento_conta_categoria (cd_conta,"+
			                                  "cd_movimento_conta,"+
			                                  "cd_categoria_economica,"+
			                                  "vl_movimento_categoria,"+
			                                  "cd_movimento_conta_categoria,"+
			                                  "cd_conta_pagar,"+
			                                  "cd_conta_receber,"+
			                                  "tp_movimento," +
			                                  "cd_centro_custo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdConta()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConta());
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMovimentoConta());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaEconomica());
			pstmt.setDouble(4,objeto.getVlMovimentoCategoria());
			pstmt.setInt(5, code);
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdContaPagar());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContaReceber());
			pstmt.setInt(8,objeto.getTpMovimento());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCentroCusto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentoContaCategoria objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(MovimentoContaCategoria objeto, int cdContaOld, int cdMovimentoContaOld, int cdCategoriaEconomicaOld, int cdMovimentoContaCategoriaOld) {
		return update(objeto, cdContaOld, cdMovimentoContaOld, cdCategoriaEconomicaOld, cdMovimentoContaCategoriaOld, null);
	}

	public static int update(MovimentoContaCategoria objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(MovimentoContaCategoria objeto, int cdContaOld, int cdMovimentoContaOld, int cdCategoriaEconomicaOld, int cdMovimentoContaCategoriaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_movimento_conta_categoria SET cd_conta=?,"+
												      		   "cd_movimento_conta=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "vl_movimento_categoria=?,"+
												      		   "cd_movimento_conta_categoria=?,"+
												      		   "cd_conta_pagar=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "tp_movimento=?," +
												      		   "cd_centro_custo=? WHERE cd_conta=? AND cd_movimento_conta=? AND cd_categoria_economica=? AND cd_movimento_conta_categoria=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setInt(2,objeto.getCdMovimentoConta());
			pstmt.setInt(3,objeto.getCdCategoriaEconomica());
			pstmt.setDouble(4,objeto.getVlMovimentoCategoria());
			pstmt.setInt(5,objeto.getCdMovimentoContaCategoria());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdContaPagar());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContaReceber());
			pstmt.setInt(8,objeto.getTpMovimento());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCentroCusto());
			pstmt.setInt(10, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(11, cdMovimentoContaOld!=0 ? cdMovimentoContaOld : objeto.getCdMovimentoConta());
			pstmt.setInt(12, cdCategoriaEconomicaOld!=0 ? cdCategoriaEconomicaOld : objeto.getCdCategoriaEconomica());
			pstmt.setFloat(13, cdMovimentoContaCategoriaOld!=0 ? cdMovimentoContaCategoriaOld : objeto.getCdMovimentoContaCategoria());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta, int cdMovimentoConta, int cdCategoriaEconomica, int cdMovimentoContaCategoria) {
		return delete(cdConta, cdMovimentoConta, cdCategoriaEconomica, cdMovimentoContaCategoria, null);
	}

	public static int delete(int cdConta, int cdMovimentoConta, int cdCategoriaEconomica, int cdMovimentoContaCategoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_movimento_conta_categoria WHERE cd_conta=? AND cd_movimento_conta=? AND cd_categoria_economica=? AND cd_movimento_conta_categoria=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdMovimentoConta);
			pstmt.setInt(3, cdCategoriaEconomica);
			pstmt.setInt(4, cdMovimentoContaCategoria);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MovimentoContaCategoria get(int cdConta, int cdMovimentoConta, int cdCategoriaEconomica, int cdMovimentoContaCategoria) {
		return get(cdConta, cdMovimentoConta, cdCategoriaEconomica, cdMovimentoContaCategoria, null);
	}

	public static MovimentoContaCategoria get(int cdConta, int cdMovimentoConta, int cdCategoriaEconomica, int cdMovimentoContaCategoria, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_categoria WHERE cd_conta=? AND cd_movimento_conta=? AND cd_categoria_economica=? AND cd_movimento_conta_categoria=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdMovimentoConta);
			pstmt.setInt(3, cdCategoriaEconomica);
			pstmt.setInt(4, cdMovimentoContaCategoria);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MovimentoContaCategoria(rs.getInt("cd_conta"),
						rs.getInt("cd_movimento_conta"),
						rs.getInt("cd_categoria_economica"),
						rs.getFloat("vl_movimento_categoria"),
						rs.getInt("cd_movimento_conta_categoria"),
						rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_conta_receber"),
						rs.getInt("tp_movimento"),
						rs.getInt("cd_centro_custo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_categoria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_movimento_conta_categoria", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
