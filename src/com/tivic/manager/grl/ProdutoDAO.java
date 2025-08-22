package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProdutoDAO{

	public static int insert(Produto objeto) {
		return insert(objeto, null);
	}

	public static int insert(Produto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = ProdutoServicoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProdutoServico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_produto (cd_produto_servico,"+
			                                  "vl_peso_unitario,"+
			                                  "vl_peso_unitario_embalagem,"+
			                                  "vl_comprimento,"+
			                                  "vl_largura,"+
			                                  "vl_altura,"+
			                                  "vl_comprimento_embalagem,"+
			                                  "vl_largura_embalagem,"+
			                                  "vl_altura_embalagem,"+
			                                  "qt_embalagem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setFloat(2,objeto.getVlPesoUnitario());
			pstmt.setFloat(3,objeto.getVlPesoUnitarioEmbalagem());
			pstmt.setFloat(4,objeto.getVlComprimento());
			pstmt.setFloat(5,objeto.getVlLargura());
			pstmt.setFloat(6,objeto.getVlAltura());
			pstmt.setFloat(7,objeto.getVlComprimentoEmbalagem());
			pstmt.setFloat(8,objeto.getVlLarguraEmbalagem());
			pstmt.setFloat(9,objeto.getVlAlturaEmbalagem());
			pstmt.setInt(10,objeto.getQtEmbalagem());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Produto objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Produto objeto, int cdProdutoServicoOld) {
		return update(objeto, cdProdutoServicoOld, null);
	}

	public static int update(Produto objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Produto objeto, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Produto objetoTemp = get(objeto.getCdProdutoServico(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO grl_produto (cd_produto_servico,"+
			                                  "vl_peso_unitario,"+
			                                  "vl_peso_unitario_embalagem,"+
			                                  "vl_comprimento,"+
			                                  "vl_largura,"+
			                                  "vl_altura,"+
			                                  "vl_comprimento_embalagem,"+
			                                  "vl_largura_embalagem,"+
			                                  "vl_altura_embalagem,"+
			                                  "qt_embalagem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE grl_produto SET cd_produto_servico=?,"+
												      		   "vl_peso_unitario=?,"+
												      		   "vl_peso_unitario_embalagem=?,"+
												      		   "vl_comprimento=?,"+
												      		   "vl_largura=?,"+
												      		   "vl_altura=?,"+
												      		   "vl_comprimento_embalagem=?,"+
												      		   "vl_largura_embalagem=?,"+
												      		   "vl_altura_embalagem=?,"+
												      		   "qt_embalagem=? WHERE cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setFloat(2,objeto.getVlPesoUnitario());
			pstmt.setFloat(3,objeto.getVlPesoUnitarioEmbalagem());
			pstmt.setFloat(4,objeto.getVlComprimento());
			pstmt.setFloat(5,objeto.getVlLargura());
			pstmt.setFloat(6,objeto.getVlAltura());
			pstmt.setFloat(7,objeto.getVlComprimentoEmbalagem());
			pstmt.setFloat(8,objeto.getVlLarguraEmbalagem());
			pstmt.setFloat(9,objeto.getVlAlturaEmbalagem());
			pstmt.setInt(10,objeto.getQtEmbalagem());
			if (objetoTemp != null) {
				pstmt.setInt(11, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			}
			pstmt.executeUpdate();
			if (ProdutoServicoDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoServico) {
		return delete(cdProdutoServico, null);
	}

	public static int delete(int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_produto WHERE cd_produto_servico=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.executeUpdate();
			if (ProdutoServicoDAO.delete(cdProdutoServico, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Produto get(int cdProdutoServico) {
		return get(cdProdutoServico, null);
	}

	public static Produto get(int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto A, grl_produto_servico B WHERE A.cd_produto_servico=B.cd_produto_servico AND A.cd_produto_servico=?");
			pstmt.setInt(1, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Produto(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_categoria_economica"),
						rs.getString("nm_produto_servico"),
						rs.getString("txt_produto_servico"),
						rs.getString("txt_especificacao"),
						rs.getString("txt_dado_tecnico"),
						rs.getString("txt_prazo_entrega"),
						rs.getInt("tp_produto_servico"),
						rs.getString("id_produto_servico"),
						rs.getString("sg_produto_servico"),
						rs.getInt("cd_classificacao_fiscal"),
						rs.getInt("cd_fabricante"),
						rs.getInt("cd_marca"),
						rs.getString("nm_modelo"),
						rs.getInt("cd_ncm"),
						rs.getString("nr_referencia"),
						rs.getFloat("vl_peso_unitario"),
						rs.getFloat("vl_peso_unitario_embalagem"),
						rs.getFloat("vl_comprimento"),
						rs.getFloat("vl_largura"),
						rs.getFloat("vl_altura"),
						rs.getFloat("vl_comprimento_embalagem"),
						rs.getFloat("vl_largura_embalagem"),
						rs.getFloat("vl_altura_embalagem"),
						rs.getInt("qt_embalagem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_produto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
