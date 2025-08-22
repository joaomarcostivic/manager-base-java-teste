package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ClienteTabelaPrecoDAO{

	public static int insert(ClienteTabelaPreco objeto) {
		return insert(objeto, null);
	}

	public static int insert(ClienteTabelaPreco objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_cliente_tabela_preco", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdClienteTabelaPreco(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_cliente_tabela_preco (cd_cliente_tabela_preco,"+
			                                  "cd_tabela_preco,"+
			                                  "cd_pessoa,"+
			                                  "cd_empresa,"+
			                                  "cd_classificacao,"+
			                                  "cd_produto_servico,"+
			                                  "cd_grupo) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTabelaPreco());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdClassificacao());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdProdutoServico());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ClienteTabelaPreco objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ClienteTabelaPreco objeto, int cdClienteTabelaPrecoOld, int cdTabelaPrecoOld) {
		return update(objeto, cdClienteTabelaPrecoOld, cdTabelaPrecoOld, null);
	}

	public static int update(ClienteTabelaPreco objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ClienteTabelaPreco objeto, int cdClienteTabelaPrecoOld, int cdTabelaPrecoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_cliente_tabela_preco SET cd_cliente_tabela_preco=?,"+
												      		   "cd_tabela_preco=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_classificacao=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_grupo=? WHERE cd_cliente_tabela_preco=? AND cd_tabela_preco=?");
			pstmt.setInt(1,objeto.getCdClienteTabelaPreco());
			pstmt.setInt(2,objeto.getCdTabelaPreco());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdClassificacao());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdProdutoServico());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdGrupo());
			pstmt.setInt(8, cdClienteTabelaPrecoOld!=0 ? cdClienteTabelaPrecoOld : objeto.getCdClienteTabelaPreco());
			pstmt.setInt(9, cdTabelaPrecoOld!=0 ? cdTabelaPrecoOld : objeto.getCdTabelaPreco());
			int ret = pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdClienteTabelaPreco, int cdTabelaPreco) {
		return delete(cdClienteTabelaPreco, cdTabelaPreco, null);
	}

	public static int delete(int cdClienteTabelaPreco, int cdTabelaPreco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_cliente_tabela_preco WHERE cd_cliente_tabela_preco=? AND cd_tabela_preco=?");
			pstmt.setInt(1, cdClienteTabelaPreco);
			pstmt.setInt(2, cdTabelaPreco);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ClienteTabelaPreco get(int cdClienteTabelaPreco, int cdTabelaPreco) {
		return get(cdClienteTabelaPreco, cdTabelaPreco, null);
	}

	public static ClienteTabelaPreco get(int cdClienteTabelaPreco, int cdTabelaPreco, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_cliente_tabela_preco WHERE cd_cliente_tabela_preco=? AND cd_tabela_preco=?");
			pstmt.setInt(1, cdClienteTabelaPreco);
			pstmt.setInt(2, cdTabelaPreco);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ClienteTabelaPreco(rs.getInt("cd_cliente_tabela_preco"),
						rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_classificacao"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_cliente_tabela_preco");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_cliente_tabela_preco", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
