package com.tivic.manager.ctb;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ContaServices {

	public static Conta save(Conta conta){
		return save(conta, null);
	}

	public static Conta save(Conta conta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 1;
			if(conta.getCdConta() <= 0) {
				retorno = ContaDAO.insert(conta, connect);
				if(retorno > 0){
					conta.setCdConta(retorno);
				}
			}
			else {
				retorno = ContaDAO.update(conta, connect);
			}
			if(retorno < 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			return conta;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ctb.ContaServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, " +
						   "	B.nm_historico AS nm_historico_padrao " +
						   "FROM ctb_conta A " +
						   "	LEFT OUTER JOIN ctb_historico B ON (A.cd_historico_padrao = B.cd_historico) " +
						   "WHERE (1=1)", "ORDER BY A.nm_conta", criterios, connect!=null ? connect : Conexao.conectar(), connect == null);
	}
}
