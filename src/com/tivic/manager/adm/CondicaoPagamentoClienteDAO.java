package com.tivic.manager.adm;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

public class CondicaoPagamentoClienteDAO {
	public static int insert(CondicaoPagamentoCliente objeto) {
		return insert(objeto, null);
	}

	public static int insert(CondicaoPagamentoCliente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_condicao_pagamento_cliente");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_condicao_pagamento");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdCondicaoPagamento()));
			int code = Conexao.getSequenceCode("adm_condicao_pagamento_cliente", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCondicaoPagamentoCliente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_condicao_pagamento_cliente (cd_condicao_pagamento_cliente, " + 
											  "cd_condicao_pagamento,"+
			                                  "cd_empresa,"+
			                                  "cd_pessoa,"+
			                                  "cd_classificacao) VALUES (?, ?, ?, ?, ?)");
			
			if(objeto.getCdCondicaoPagamentoCliente()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCondicaoPagamentoCliente());
			if(objeto.getCdCondicaoPagamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCondicaoPagamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4, objeto.getCdPessoa());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdClassificacao());
			
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

	public static int update(CondicaoPagamentoCliente objeto) {
		return update(objeto, 0, 0, 0, 0, 0, null);
	}

	public static int update(CondicaoPagamentoCliente objeto, int cdCondicaoPagamentoClienteOld, int cdCondicaoPagamentoOld, int cdEmpresaOld, int cdPessoaOld, int cdClassificacaoOld) {
		return update(objeto, cdCondicaoPagamentoClienteOld, cdCondicaoPagamentoOld, cdEmpresaOld, cdPessoaOld, cdClassificacaoOld, null);
	}

	public static int update(CondicaoPagamentoCliente objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, connect);
	}

	public static int update(CondicaoPagamentoCliente objeto, int cdCondicaoPagamentoClienteOld, int cdCondicaoPagamentoOld, int cdEmpresaOld, int cdPessoaOld, int cdClassificacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_condicao_pagamento_cliente SET cd_condicao_pagamento_cliente=?, " + 
															   "cd_condicao_pagamento=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_classificacao=? WHERE cd_condicao_pagamento_cliente=?, cd_condicao_pagamento=?, cd_empresa=?, cd_pessoa=?, cd_classificacao=?");
			if(objeto.getCdCondicaoPagamentoCliente()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCondicaoPagamentoCliente());
			if(objeto.getCdCondicaoPagamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCondicaoPagamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4, objeto.getCdPessoa());
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdClassificacao());
			pstmt.setInt(6, cdCondicaoPagamentoClienteOld!=0 ? cdCondicaoPagamentoClienteOld : objeto.getCdCondicaoPagamentoCliente());
			pstmt.setInt(7, cdCondicaoPagamentoOld!=0 ? cdCondicaoPagamentoOld : objeto.getCdCondicaoPagamento());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(9, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(10, cdClassificacaoOld!=0 ? cdClassificacaoOld : objeto.getCdClassificacao());
			
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

	public static int delete(int cdCondicaoPagamentoCliente, int cdCondicaoPagamento) {
		return delete(cdCondicaoPagamentoCliente, cdCondicaoPagamento, null);
	}

	public static int delete(int cdCondicaoPagamentoCliente, int cdCondicaoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_condicao_pagamento_cliente WHERE cd_condicao_pagamento_cliente=? AND cd_condicao_pagamento=?");
			pstmt.setInt(1, cdCondicaoPagamentoCliente);
			pstmt.setInt(2, cdCondicaoPagamento);
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

	public static CondicaoPagamentoCliente get(int cdCondicaoPagamentoCliente, int cdCondicaoPagamento, int cdPessoa, int cdClassificacao, int cdEmpresa) {
		return get(cdCondicaoPagamentoCliente, cdCondicaoPagamento, cdPessoa, cdClassificacao, cdEmpresa, null);
	}

	public static CondicaoPagamentoCliente get(int cdCondicaoPagamentoCliente, int cdCondicaoPagamento, int cdPessoa, int cdClassificacao, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento_cliente WHERE cd_condicao_pagamento_cliente=? AND cd_condicao_pagamento=? AND cd_empresa=? AND cd_pessoa=? AND cd_classificacao=?");
			pstmt.setInt(1, cdCondicaoPagamentoCliente);
			pstmt.setInt(2, cdCondicaoPagamento);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdPessoa);
			pstmt.setInt(5, cdClassificacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CondicaoPagamentoCliente(rs.getInt("cd_condicao_pagamento_cliente"),
						rs.getInt("cd_condicao_pagamento"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_classificacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoClienteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoClienteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento_cliente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoClienteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoClienteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_condicao_pagamento_cliente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
