package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import sol.dao.ResultSetMap;
import sol.util.Result;
import com.tivic.sol.connection.Conexao;

public class ClienteTabelaPrecoServices {

	public static Result insert(ClienteTabelaPreco objeto) {
		return insert(objeto, null);
	}

	public static Result insert(ClienteTabelaPreco objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(!isConnectionNull);
			}
			int retorno = ClienteTabelaPrecoDAO.insert(objeto, connect);
			if(retorno <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir ClienteTabelaPreco");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Inserido com sucesso");
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.insert: " + sqlExpt);
			return new Result(-1, "Erro ao inserir ClienteTabelaPreco");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.insert: " +  e);
			return new Result(-1, "Erro ao inserir ClienteTabelaPreco");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result update(ClienteTabelaPreco objeto) {
		return update(objeto, 0, 0, null);
	}

	public static Result update(ClienteTabelaPreco objeto, int cdClienteTabelaPrecoOld, int cdTabelaPrecoOld) {
		return update(objeto, cdClienteTabelaPrecoOld, cdTabelaPrecoOld, null);
	}

	public static Result update(ClienteTabelaPreco objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static Result update(ClienteTabelaPreco objeto, int cdClienteTabelaPrecoOld, int cdTabelaPrecoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(!isConnectionNull);
			}
			int retorno = ClienteTabelaPrecoDAO.update(objeto, cdClienteTabelaPrecoOld, cdTabelaPrecoOld, connect);
			if(retorno <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar ClienteTabelaPreco");
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Atualizado com sucesso");
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.update: " + sqlExpt);
			return new Result(-1, "Erro ao atualizar ClienteTabelaPreco");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.update: " +  e);
			return new Result(-1, "Erro ao atualizar ClienteTabelaPreco");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result delete(int cdClienteTabelaPreco, int cdTabelaPreco) {
		return delete(cdClienteTabelaPreco, cdTabelaPreco, null);
	}

	public static Result delete(int cdClienteTabelaPreco, int cdTabelaPreco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(!isConnectionNull);
			}
				
			int retorno = ClienteTabelaPrecoDAO.delete(cdClienteTabelaPreco, cdTabelaPreco, connect);
			
			if(retorno <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao remover ClienteTabelaPreco");
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Removido com sucesso");
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.delete: " + sqlExpt);
			return new Result(-1, "Erro ao remover ClienteTabelaPreco");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteTabelaPrecoDAO.delete: " +  e);
			return new Result(-1, "Erro ao remover ClienteTabelaPreco");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByCliente(int cdPessoa,int cdEmpresa){
		return getAllByCliente(cdPessoa, cdEmpresa, null);
	}
	
	public static ResultSetMap getAllByCliente(int cdPessoa,int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT A.*, B.nm_tabela_preco, C.nm_produto_servico, D.nm_grupo " +
					     "FROM adm_cliente_tabela_preco A " +
						 "JOIN adm_tabela_preco                  B  ON (B.cd_tabela_preco = A.cd_tabela_preco) " +
						 "LEFT OUTER JOIN grl_produto_servico    C  ON (C.cd_produto_servico = A.cd_produto_servico) " +
						 "LEFT OUTER JOIN alm_grupo              D  ON (D.cd_grupo = A.cd_grupo) " +
						 "WHERE A.cd_empresa = "+ cdEmpresa +
						 " AND  A.cd_pessoa = " + cdPessoa;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByClassificacao(int cdClassificacao){
		return getAllByClassificacao(cdClassificacao, null);
	}
	
	public static ResultSetMap getAllByClassificacao(int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT A.*, B.nm_tabela_preco, C.nm_produto_servico, D.nm_grupo " +
					     "FROM adm_cliente_tabela_preco A " +
						 "JOIN adm_tabela_preco                  B  ON (B.cd_tabela_preco = A.cd_tabela_preco) " +
						 "LEFT OUTER JOIN grl_produto_servico    C  ON (C.cd_produto_servico = A.cd_produto_servico) " +
						 "LEFT OUTER JOIN alm_grupo              D  ON (D.cd_grupo = A.cd_grupo) " +
						 "WHERE A.cd_classificacao = " + cdClassificacao;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
