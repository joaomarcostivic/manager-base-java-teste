package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProdutoServicoDescontoDAO{

	public static int insert(ProdutoServicoDesconto objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ProdutoServicoDesconto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[4];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tipo_desconto");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdTipoDesconto()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_empresa");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdEmpresa()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_faixa_desconto");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdFaixaDesconto()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_desconto");
			keys[3].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_produto_servico_desconto", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDesconto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_produto_servico_desconto (cd_tipo_desconto,"+
			                                  "cd_empresa,"+
			                                  "cd_faixa_desconto,"+
			                                  "cd_desconto,"+
			                                  "cd_produto_servico,"+
			                                  "cd_grupo) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTipoDesconto()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoDesconto());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdFaixaDesconto()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFaixaDesconto());
			pstmt.setInt(4, code);
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProdutoServico());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDescontoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDescontoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoServicoDesconto objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(ProdutoServicoDesconto objeto, int cdTipoDescontoOld, int cdEmpresaOld, int cdFaixaDescontoOld, int cdDescontoOld) {
		return update(objeto, cdTipoDescontoOld, cdEmpresaOld, cdFaixaDescontoOld, cdDescontoOld, null);
	}

	public static int update(ProdutoServicoDesconto objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(ProdutoServicoDesconto objeto, int cdTipoDescontoOld, int cdEmpresaOld, int cdFaixaDescontoOld, int cdDescontoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_produto_servico_desconto SET cd_tipo_desconto=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_faixa_desconto=?,"+
												      		   "cd_desconto=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_grupo=? WHERE cd_tipo_desconto=? AND cd_empresa=? AND cd_faixa_desconto=? AND cd_desconto=?");
			pstmt.setInt(1,objeto.getCdTipoDesconto());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdFaixaDesconto());
			pstmt.setInt(4,objeto.getCdDesconto());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProdutoServico());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGrupo());
			pstmt.setInt(7, cdTipoDescontoOld!=0 ? cdTipoDescontoOld : objeto.getCdTipoDesconto());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(9, cdFaixaDescontoOld!=0 ? cdFaixaDescontoOld : objeto.getCdFaixaDesconto());
			pstmt.setInt(10, cdDescontoOld!=0 ? cdDescontoOld : objeto.getCdDesconto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDescontoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDescontoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, int cdDesconto) {
		return delete(cdTipoDesconto, cdEmpresa, cdFaixaDesconto, cdDesconto, null);
	}

	public static int delete(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, int cdDesconto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_produto_servico_desconto WHERE cd_tipo_desconto=? AND cd_empresa=? AND cd_faixa_desconto=? AND cd_desconto=?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdFaixaDesconto);
			pstmt.setInt(4, cdDesconto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDescontoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDescontoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoServicoDesconto get(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, int cdDesconto) {
		return get(cdTipoDesconto, cdEmpresa, cdFaixaDesconto, cdDesconto, null);
	}

	public static ProdutoServicoDesconto get(int cdTipoDesconto, int cdEmpresa, int cdFaixaDesconto, int cdDesconto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_produto_servico_desconto WHERE cd_tipo_desconto=? AND cd_empresa=? AND cd_faixa_desconto=? AND cd_desconto=?");
			pstmt.setInt(1, cdTipoDesconto);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdFaixaDesconto);
			pstmt.setInt(4, cdDesconto);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoServicoDesconto(rs.getInt("cd_tipo_desconto"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_faixa_desconto"),
						rs.getInt("cd_desconto"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDescontoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDescontoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_produto_servico_desconto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDescontoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDescontoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_produto_servico_desconto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
