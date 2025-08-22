package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProdutoCodigoDAO{

	public static int insert(ProdutoCodigo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoCodigo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_produto_codigo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProdutoCodigo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_produto_codigo (cd_produto_codigo,"+
			                                  "txt_descricao,"+
			                                  "id_produto_servico,"+
			                                  "id_reduzido,"+
			                                  "cd_produto_servico,"+
			                                  "lg_codigo_barras) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getTxtDescricao());
			pstmt.setString(3,objeto.getIdProdutoServico());
			pstmt.setString(4,objeto.getIdReduzido());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProdutoServico());
			pstmt.setInt(6,objeto.getLgCodigoBarras());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoCodigo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ProdutoCodigo objeto, int cdProdutoCodigoOld) {
		return update(objeto, cdProdutoCodigoOld, null);
	}

	public static int update(ProdutoCodigo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ProdutoCodigo objeto, int cdProdutoCodigoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_produto_codigo SET cd_produto_codigo=?,"+
												      		   "txt_descricao=?,"+
												      		   "id_produto_servico=?,"+
												      		   "id_reduzido=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "lg_codigo_barras=? WHERE cd_produto_codigo=?");
			pstmt.setInt(1,objeto.getCdProdutoCodigo());
			pstmt.setString(2,objeto.getTxtDescricao());
			pstmt.setString(3,objeto.getIdProdutoServico());
			pstmt.setString(4,objeto.getIdReduzido());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProdutoServico());
			pstmt.setInt(6,objeto.getLgCodigoBarras());
			pstmt.setInt(7, cdProdutoCodigoOld!=0 ? cdProdutoCodigoOld : objeto.getCdProdutoCodigo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoCodigo) {
		return delete(cdProdutoCodigo, null);
	}

	public static int delete(int cdProdutoCodigo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_produto_codigo WHERE cd_produto_codigo=?");
			pstmt.setInt(1, cdProdutoCodigo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoCodigo get(int cdProdutoCodigo) {
		return get(cdProdutoCodigo, null);
	}

	public static ProdutoCodigo get(int cdProdutoCodigo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_codigo WHERE cd_produto_codigo=?");
			pstmt.setInt(1, cdProdutoCodigo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoCodigo(rs.getInt("cd_produto_codigo"),
						rs.getString("txt_descricao"),
						rs.getString("id_produto_servico"),
						rs.getString("id_reduzido"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("lg_codigo_barras"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_codigo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProdutoCodigo> getList() {
		return getList(null);
	}

	public static ArrayList<ProdutoCodigo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProdutoCodigo> list = new ArrayList<ProdutoCodigo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProdutoCodigo obj = ProdutoCodigoDAO.get(rsm.getInt("cd_produto_codigo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoCodigoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_produto_codigo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
