package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sol.dao.ResultSetMap;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ClienteProdutoServices {
	
	public static Result save(ClienteProduto objeto) {
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			// Verifica 
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_cliente " +
					                                "WHERE cd_empresa = "+objeto.getCdEmpresa()+
					                                "  AND cd_pessoa  = "+objeto.getCdPessoa()).executeQuery();
			if(!rs.next()) {
				connect.prepareStatement("INSERT INTO adm_cliente (cd_empresa, cd_pessoa) VALUES ("+objeto.getCdEmpresa()+","+objeto.getCdPessoa()+")").executeUpdate();
			}
			// Verifica se já existe o produto
			String sql = "SELECT * FROM adm_cliente_produto " +
						 "WHERE cd_empresa         = "+ objeto.getCdEmpresa() +
						 "  AND cd_pessoa          = "+ objeto.getCdPessoa() +
						 "  AND cd_produto_servico = "+objeto.getCdProdutoServico();
			rs = connect.prepareStatement(sql).executeQuery();
			Result result = new Result(0);
			if(rs.next())
				result.setCode(ClienteProdutoDAO.update(objeto, connect));
			else
				result.setCode(ClienteProdutoDAO.insert(objeto, connect));
			//
			connect.commit();
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return new Result(-1, "Falha ao tentar salvar associação de produto com cliente!", e);
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getClienteProduto(int cdPessoa,int cdEmpresa){
		return getClienteProduto(cdPessoa, cdEmpresa, null);
	}
	
	public static ResultSetMap getClienteProduto(int cdPessoa,int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT CP.*,PSP.vl_preco, PS.nm_produto_servico, TP.nm_tabela_preco " +
					     "FROM adm_cliente_produto CP " +
						 "JOIN grl_produto_servico                  PS  ON (CP.cd_produto_servico = PS.cd_produto_servico) " +
						 "LEFT OUTER JOIN adm_tabela_preco          TP  ON (CP.cd_tabela_preco = TP.cd_tabela_preco) " +
						 "LEFT OUTER JOIN adm_produto_servico_preco PSP ON (TP.cd_tabela_preco    = PSP.cd_tabela_preco " +
						 "                                              AND CP.cd_produto_servico = PSP.cd_produto_servico" +
						 "                                              AND dt_termino_validade IS NULL) " +
						 "WHERE CP.cd_empresa = "+ cdEmpresa +
						 " AND CP.cd_pessoa = " + cdPessoa;
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
