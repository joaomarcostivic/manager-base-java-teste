package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class ClientePagamentoServices {
	/**
	 * getStatusCliente
	 * 
	 * @author Joao Marlon
	 * @param  cdEmpresa            // codigo da empresa para consulta  
	 * @param  cdCliente            // codigo do cliente para consulta
	 * @return ResultSetMap com resultado da pesquisa do status do cliente para ser exibido no pdv.
	 * 		   Considerando as contas em aberto e os limites cadastrados.
	 * */	
	public static ResultSetMap getStatusCliente(int cdEmpresa, int cdCliente){
		return getStatusCliente(cdEmpresa, cdCliente, null);
	}
	
	public static ResultSetMap getStatusCliente(int cdEmpresa, int cdCliente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			boolean hasConta = false;
			String sql = "SELECT * FROM adm_conta_receber " +
						 "WHERE cd_pessoa  = " +cdCliente+
						 "  AND cd_empresa = " +cdEmpresa +
						 "  AND st_conta   = " + ContaReceberServices.ST_EM_ABERTO;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if (rsm.next())
				hasConta = true;
			
			pstmt.close();
			rsm = null;
			//
			sql = "SELECT AA.cd_empresa, B.cd_forma_pagamento, B.nm_forma_pagamento, C.cd_pessoa, C.tp_credito, " +
			     "        C.vl_limite_credito, A.vl_limite AS vl_limite_pagamento, SUM(D.vl_conta)  AS vl_limite_utilizado " +
				 "FROM adm_condicao_pagamento A " +
				 "JOIN adm_condicao_pagamento_cliente AA ON (A.cd_condicao_pagamento = AA.cd_condicao_pagamento) " +				 
				 "LEFT OUTER JOIN adm_condicao_forma_plano_pagamento BB  ON (A.cd_condicao_pagamento = BB.cd_condicao_pagamento) " +
				 "LEFT OUTER JOIN adm_forma_pagamento B  ON (BB.cd_forma_pagamento = B.cd_forma_pagamento) " +
				 "LEFT OUTER JOIN adm_cliente         C  ON (AA.cd_pessoa          = C.cd_pessoa) " +
				 "LEFT OUTER JOIN adm_conta_receber   D  ON (AA.cd_pessoa          = D.cd_pessoa " +
				 "									    AND AA.cd_empresa         = D.cd_empresa " +
				 " 									    AND B.cd_forma_pagamento = D.cd_forma_pagamento) " +
//				 "LEFT OUTER JOIN adm_cliente_produto E ON (A.cd_pessoa          = E.cd_pessoa) "+
//				 "LEFT OUTER JOIN grl_produto_servico F ON (E.cd_produto_servico = F.cd_produto_servico)"+
				 (hasConta? "WHERE D.st_conta   = " + ContaReceberServices.ST_EM_ABERTO + 
						    "  AND AA.cd_pessoa  = " + cdCliente : "WHERE AA.cd_pessoa  = " + cdCliente)+
				 "  AND AA.cd_empresa = " + cdEmpresa +
				 "GROUP BY AA.cd_empresa, B.cd_forma_pagamento, B.nm_forma_pagamento, C.cd_pessoa, C.tp_credito, " +
				 "         C.vl_limite_credito, A.vl_limite ";
			pstmt = connect.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			// Acrescenta o campo com o calculo do valor disponivel para compras em determinada forma de pagamento.
			double vlLimiteDisponivel = 0;
			while (rsm.next()){				
				rsm.setValueToField("VL_PAGT_DISPONIVEL", (rsm.getDouble("vl_limite_pagamento") - rsm.getDouble("vl_limite_utilizado")));
				vlLimiteDisponivel += rsm.getDouble("vl_limite_utilizado");
				rsm.setValueToField("TP_CREDITO_CLIENTE", rsm.getInt("tp_credito"));
			}
			//Calcula o limite disponivel geral para compras.
			rsm.setValueToField("VL_LIMITE_DISPONIVEL", (rsm.getDouble("vl_limite_credito") - vlLimiteDisponivel));
			
			pstmt.close();
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! ClientePagamentoServices.getStatusCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getStatusProdutoCliente(int cdEmpresa, int cdCliente){
		return getStatusProdutoCliente(cdEmpresa, cdCliente, null);
	}
	public static ResultSetMap getStatusProdutoCliente(int cdEmpresa, int cdCliente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql;
			// Acrescimo de combustiveis
			sql = "SELECT * FROM adm_cliente_produto A, grl_produto_servico B " +
				  "WHERE A.cd_produto_servico = B.cd_produto_servico " +
			      "  AND A.cd_empresa = " + cdEmpresa +
				  "  AND A.cd_pessoa  = " + cdCliente;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsmProduto = new ResultSetMap(pstmt.executeQuery());	
			pstmt.close();
			rsmProduto.beforeFirst();
			return rsmProduto;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! ClientePagamentoServices.getStatusCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
