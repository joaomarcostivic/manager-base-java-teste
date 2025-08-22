package com.tivic.manager.flp;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class FolhaEventoServices{
	public static final int CALCULADO = 0;
	public static final int INFORMADO = 1;
	public static final int FERIAS    = 2;
	public static final int RESCISAO  = 3;

	public static int insert(FolhaEvento objeto) {
		Connection connect = Conexao.conectar();
		try	{
			FolhaPagamento folha = FolhaPagamentoDAO.get(objeto.getCdFolhaPagamento(), connect);
			if(folha.getStFolhaPagamento()!= 0)
				return -10;
			if (FolhaEventoDAO.insert(objeto, connect) > 0)	{
				FolhaPagamentoServices.calcularFolhaMes(objeto.getCdFolhaPagamento(), objeto.getCdMatricula(), 0, 0);
				return 1;
			}
			return 0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int update(FolhaEvento objeto) {
		Connection connect = Conexao.conectar();
		try	{
			FolhaPagamento folha = FolhaPagamentoDAO.get(objeto.getCdFolhaPagamento(), connect);
			if(folha.getStFolhaPagamento()!= 0)
				return -10;
			if (FolhaEventoDAO.update(objeto, 0, 0, 0, connect) > 0)	{
				FolhaPagamentoServices.calcularFolhaMes(objeto.getCdFolhaPagamento(), objeto.getCdMatricula(), 0, 0);
				return 1;
			}
			return 0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEventoFinanceiro, int cdMatricula, int cdFolhaPagamento) {
		Connection connect = Conexao.conectar();
		try	{
			FolhaPagamento folha = FolhaPagamentoDAO.get(cdFolhaPagamento, connect);
			if(folha.getStFolhaPagamento()!= 0)
				return -10;
			if (FolhaEventoDAO.delete(cdEventoFinanceiro, cdMatricula, cdFolhaPagamento, connect) > 0)	{
				FolhaPagamentoServices.calcularFolhaMes(cdFolhaPagamento, cdMatricula, 0, 0);
				return 1;
			}
			return 0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, " +
						   "B.id_evento_financeiro, B.nm_evento_financeiro, B.tp_evento_financeiro " +
				           "FROM flp_folha_evento A " +
				           "JOIN adm_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro) ",
				           "ORDER BY B.tp_evento_financeiro, B.id_evento_financeiro", criterios, null, Conexao.conectar(), true, false, false);
	}

	public static ResultSetMap findOcorrenciaEvento(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.id_evento_financeiro, B.nm_evento_financeiro, B.tp_evento_financeiro," +
				           "       D.nr_matricula, E.nm_pessoa " +
				           "FROM flp_folha_evento A " +
				           "JOIN adm_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro) " +
				           "JOIN flp_folha_pagamento_funcionario C ON (A.cd_folha_pagamento = C.cd_folha_pagamento) " +
				           "JOIN srh_dados_funcionais D ON (C.cd_matricula = D.cd_matricula) "+
				           "JOIN grl_pessoa E ON (D.cd_pessoa = E.cd_pessoa) ",
				           "ORDER BY B.tp_evento_financeiro, B.id_evento_financeiro", criterios, null, Conexao.conectar(), true, false, false);
	}
}
