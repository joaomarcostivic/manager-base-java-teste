package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ClienteProdutoDAO{

	public static int insert(ClienteProduto objeto) {
		return insert(objeto, null);
	}

	public static int insert(ClienteProduto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_cliente_produto (cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_pessoa,"+
			                                  "qt_limite,"+
			                                  "vl_limite,"+
			                                  "cd_tabela_preco," +
			                                  "tp_aplicacao_tabela) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			pstmt.setFloat(4,objeto.getQtLimite());
			pstmt.setFloat(5,objeto.getVlLimite());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTabelaPreco());
			pstmt.setInt(7,objeto.getTpAplicacaoTabela());
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

	public static int update(ClienteProduto objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ClienteProduto objeto, int cdEmpresaOld, int cdProdutoServicoOld, int cdPessoaOld) {
		return update(objeto, cdEmpresaOld, cdProdutoServicoOld, cdPessoaOld, null);
	}

	public static int update(ClienteProduto objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ClienteProduto objeto, int cdEmpresaOld, int cdProdutoServicoOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_cliente_produto SET cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_pessoa=?,"+
												      		   "qt_limite=?,"+
												      		   "vl_limite=?,"+
												      		   "cd_tabela_preco=?," +
												      		   "tp_aplicacao_tabela=? WHERE cd_empresa=? AND cd_produto_servico=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdPessoa());
			pstmt.setFloat(4,objeto.getQtLimite());
			pstmt.setFloat(5,objeto.getVlLimite());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTabelaPreco());
			pstmt.setInt(7, objeto.getTpAplicacaoTabela());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(9, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(10, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
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

	public static int delete(int cdEmpresa, int cdProdutoServico, int cdPessoa) {
		return delete(cdEmpresa, cdProdutoServico, cdPessoa, null);
	}

	public static int delete(int cdEmpresa, int cdProdutoServico, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_cliente_produto WHERE cd_empresa=? AND cd_produto_servico=? AND cd_pessoa=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdPessoa);
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

	public static ClienteProduto get(int cdEmpresa, int cdProdutoServico, int cdPessoa) {
		return get(cdEmpresa, cdProdutoServico, cdPessoa, null);
	}

	public static ClienteProduto get(int cdEmpresa, int cdProdutoServico, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_cliente_produto WHERE cd_empresa=? AND cd_produto_servico=? AND cd_pessoa=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ClienteProduto(rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_pessoa"),
						rs.getFloat("qt_limite"),
						rs.getFloat("vl_limite"),
						rs.getInt("cd_tabela_preco"),
						rs.getInt("tp_aplicacao_tabela"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteProdutoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProdutoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_cliente_produto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteProdutoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProdutoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_cliente_produto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
