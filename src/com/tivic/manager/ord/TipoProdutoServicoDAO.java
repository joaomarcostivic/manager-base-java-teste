package com.tivic.manager.ord;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoProdutoServicoDAO{

	public static int insert(TipoProdutoServico objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoProdutoServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ord_tipo_produto_servico", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoProdutoServico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ord_tipo_produto_servico (cd_tipo_produto_servico,"+
			                                  "nm_tipo_produto_servico,"+
			                                  "id_tipo_produto_servico,"+
			                                  "txt_descricao) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoProdutoServico());
			pstmt.setString(3,objeto.getIdTipoProdutoServico());
			pstmt.setString(4,objeto.getTxtDescricao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoProdutoServico objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoProdutoServico objeto, int cdTipoProdutoServicoOld) {
		return update(objeto, cdTipoProdutoServicoOld, null);
	}

	public static int update(TipoProdutoServico objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoProdutoServico objeto, int cdTipoProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ord_tipo_produto_servico SET cd_tipo_produto_servico=?,"+
												      		   "nm_tipo_produto_servico=?,"+
												      		   "id_tipo_produto_servico=?,"+
												      		   "txt_descricao=? WHERE cd_tipo_produto_servico=?");
			pstmt.setInt(1,objeto.getCdTipoProdutoServico());
			pstmt.setString(2,objeto.getNmTipoProdutoServico());
			pstmt.setString(3,objeto.getIdTipoProdutoServico());
			pstmt.setString(4,objeto.getTxtDescricao());
			pstmt.setInt(5, cdTipoProdutoServicoOld!=0 ? cdTipoProdutoServicoOld : objeto.getCdTipoProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoProdutoServico) {
		return delete(cdTipoProdutoServico, null);
	}

	public static int delete(int cdTipoProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ord_tipo_produto_servico WHERE cd_tipo_produto_servico=?");
			pstmt.setInt(1, cdTipoProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoProdutoServico get(int cdTipoProdutoServico) {
		return get(cdTipoProdutoServico, null);
	}

	public static TipoProdutoServico get(int cdTipoProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ord_tipo_produto_servico WHERE cd_tipo_produto_servico=?");
			pstmt.setInt(1, cdTipoProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoProdutoServico(rs.getInt("cd_tipo_produto_servico"),
						rs.getString("nm_tipo_produto_servico"),
						rs.getString("id_tipo_produto_servico"),
						rs.getString("txt_descricao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_tipo_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoProdutoServico> getList() {
		return getList(null);
	}

	public static ArrayList<TipoProdutoServico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoProdutoServico> list = new ArrayList<TipoProdutoServico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoProdutoServico obj = TipoProdutoServicoDAO.get(rsm.getInt("cd_tipo_produto_servico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProdutoServicoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ord_tipo_produto_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}