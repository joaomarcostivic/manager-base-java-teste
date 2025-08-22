package com.tivic.manager.ctb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class LancamentoServices {

	public static final int ST_EM_ABERTO = 0;
	public static final int ST_LIBERADO = 1;

	public static final int ERR_LANCAMENTO_DEBITO = -2;
	public static final int ERR_LANCAMENTO_CREDITO = -3;
	public static final int ERR_CONTA_NOT_FOUND = -4;

	public static final String[] situacaoLancamento = {"Aberto", "Liberado"};

	public static ResultSetMap getAll(int cdLote) {
		return getAll(cdLote, null);
	}

	public static ResultSetMap getAll(int cdLote, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_lancamento, A.cd_lote, A.cd_lancamento_auto, A.dt_lancamento, A.vl_total, A.lg_provisao, " +
				  "   B.nm_lancamento_auto, A.cd_lancamento_auto || ' - ' || B.nm_lancamento_auto AS ds_lancamento_auto, " +
				  "	  SUM(DISTINCT C.vl_lancamento) AS vl_total_credito, SUM(DISTINCT D.vl_lancamento) AS vl_total_debito " +
			      "FROM ctb_lancamento A " +
			      "   LEFT OUTER JOIN ctb_lancamento_auto B ON (A.cd_lancamento_auto = B.cd_lancamento_auto) " +
			      "	  LEFT OUTER JOIN ctb_lancamento_credito C ON (A.cd_lancamento = C.cd_lancamento) " +
			      "	  LEFT OUTER JOIN ctb_lancamento_debito D ON (A.cd_lancamento = D.cd_lancamento) " +
			      "WHERE A.cd_lote = ? " +
			      "GROUP BY A.cd_lancamento, A.cd_lote, A.cd_lancamento_auto, A.dt_lancamento, A.vl_total, A.lg_provisao, " +
			      "   B.nm_lancamento_auto " +
			      "ORDER BY A.dt_lancamento, A.vl_total");
			pstmt.setInt(1, cdLote);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoServices.getAll: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getLancamentosDebito(int cdLancamento) {
		return getLancamentosDebito(cdLancamento, null);
	}

	public static ResultSetMap getLancamentosDebito(int cdLancamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, " +
					  "   B.cd_lote, B.dt_lancamento, B.vl_total, B.lg_provisao, " +
					  "	  C.cd_plano_contas, C.nr_conta, C.nr_digito, C.tp_conta, C.tp_natureza, C.id_conta, " +
					  "	  G.nr_conta_reduzida, G.nm_conta, C.tp_conta, C.tp_natureza, G.cd_historico_padrao, " +
					  "   C.nr_conta || ' - ' || G.nm_conta AS ds_conta, " +
					  "	  D.nm_historico, " +
					  "	  E.nm_historico AS nm_historico_padrao, " +
					  "   A.cd_conta_debito AS CD_CONTA " +
				      "FROM ctb_lancamento_debito A " +
				      "	  JOIN ctb_lancamento B ON (A.cd_lancamento = B.cd_lancamento) " +
				      "	  JOIN ctb_conta_plano_contas C ON (A.cd_conta_debito = C.cd_conta_plano_contas) " +
				      "	  JOIN ctb_conta G ON (C.cd_conta = G.cd_conta) " +
				      "   LEFT OUTER JOIN ctb_historico D ON (A.cd_historico = D.cd_historico) " +
				      "   LEFT OUTER JOIN ctb_historico E ON (G.cd_historico_padrao = E.cd_historico) " +
				      "WHERE A.cd_lancamento = ? " +
				      "ORDER BY B.dt_lancamento, A.vl_lancamento, A.nr_documento");
			pstmt.setInt(1, cdLancamento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoServices.getLancamentosDebito: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getLancamentosCredito(int cdLancamento) {
		return getLancamentosCredito(cdLancamento, null);
	}

	public static ResultSetMap getLancamentosCredito(int cdLancamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, " +
				  "   B.cd_lote, B.dt_lancamento, B.vl_total, B.lg_provisao, " +
				  "	  C.cd_plano_contas, C.nr_conta, C.nr_digito, C.tp_conta, C.tp_natureza, C.id_conta, " +
				  "	  G.nr_conta_reduzida, G.nm_conta, G.cd_historico_padrao, " +
				  "   C.nr_conta || ' - ' || G.nm_conta AS ds_conta, " +
				  "	  D.nm_historico, " +
				  "	  E.nm_historico AS nm_historico_padrao, " +
				  "   A.cd_conta_credito AS CD_CONTA " +
			      "FROM ctb_lancamento_credito A " +
			      "	  JOIN ctb_lancamento B ON (A.cd_lancamento = B.cd_lancamento) " +
			      "	  JOIN ctb_conta_plano_contas C ON (A.cd_conta_credito = C.cd_conta_plano_contas) " +
			      "	  JOIN ctb_conta G ON (C.cd_conta = G.cd_conta) " +
			      "   LEFT OUTER JOIN ctb_historico D ON (A.cd_historico = D.cd_historico) " +
			      "   LEFT OUTER JOIN ctb_historico E ON (G.cd_historico_padrao = E.cd_historico) " +
			      "WHERE A.cd_lancamento = ? " +
			      "ORDER BY B.dt_lancamento, A.vl_lancamento, A.nr_documento");
			pstmt.setInt(1, cdLancamento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoServices.getLancamentosCredito: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result save(Lancamento lancamento){
		return save(lancamento, false, null);
	}

	public static Result save(Lancamento lancamento, boolean isLancamentoAuto){
		return save(lancamento, isLancamentoAuto, null);
	}

	public static Result save(Lancamento lancamento, boolean isLancamentoAuto, Connection connect){
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect= Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 1;
			if(lancamento.getCdLancamento() == 0){
				retorno = LancamentoDAO.insert(lancamento, connect);
				if(retorno > 0)
					lancamento.setCdLancamento(retorno);
			}
			else 
				retorno = LancamentoDAO.update(lancamento, connect);

			if (isLancamentoAuto) {
				int cdLancamentoAuto = lancamento.getCdLancamentoAuto();
				String nrAnoExercicio = "" + lancamento.getDtLancamento().get(GregorianCalendar.YEAR);
				LancamentoAuto lancamentoAuto = LancamentoAutoDAO.get(cdLancamentoAuto);
				EmpresaExercicio empresaExercicio = EmpresaExercicioDAO.get(lancamentoAuto.getCdEmpresa(), nrAnoExercicio, connect);
				ContaPlanoContas contaPlanoContas = ContaPlanoContasServices.getByCdConta(lancamentoAuto.getCdContaDebito(), empresaExercicio.getCdPlanoContas());

				if (contaPlanoContas == null) 
					return new Result(ERR_CONTA_NOT_FOUND, "Conta não localizada!");
				

				LancamentoDebito lancamentoDebito = new LancamentoDebito( lancamento.getCdLancamento(), /* cdLancamento */
																		contaPlanoContas.getCdContaPlanoContas(), /* cdContaPlanoContas */
																		lancamentoAuto.getCdHistorico(), /* cdHistorico */
																		"DOC Nº ", /* nrDocumento */
																		lancamento.getVlTotal(), /* vlLancamento */
																		lancamentoAuto.getTxtHistorico(), /* txtHistorico */
																		null, /* txtObservacao */
																		LancamentoDebitoServices.ST_EM_ABERTO /* stLancamento */);

				contaPlanoContas = ContaPlanoContasServices.getByCdConta(lancamentoAuto.getCdContaCredito(), empresaExercicio.getCdPlanoContas());
				LancamentoCredito lancamentoCredito = new LancamentoCredito(
						lancamento.getCdLancamento(), /* cdLancamento */
						contaPlanoContas.getCdContaPlanoContas(), /* cdContaPlanoContas */
						lancamentoAuto.getCdHistorico(), /* cdHistorico */
						"DOC Nº ", /* nrDocumento */
						lancamento.getVlTotal(), /* vlLancamento */
						lancamentoAuto.getTxtHistorico(), /* txtHistorico */
						null, /* txtObservacao */
						LancamentoCreditoServices.ST_EM_ABERTO /* stLancamento */);

				retorno = LancamentoDebitoDAO.insert(lancamentoDebito, connect);
				if(retorno <= 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}

				retorno = LancamentoCreditoDAO.insert(lancamentoCredito, connect);
				if(retorno <= 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
			}
			if (isConnectionNull)
				connect.commit();
			return new Result(1, "Lançamento realizado com sucesso!", "lancamento", lancamento);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar realizar lançamento contábil.", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLancamento){
		return delete(cdLancamento, null);
	}

	public static int delete(int cdLancamento, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			/* exclusão dos lançamento a débito */
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM ctb_lancamento_debito A " +
					"WHERE A.cd_lancamento = ?");
			pstmt.setInt(1, cdLancamento);
			ResultSet rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				if (LancamentoDebitoDAO.delete(cdLancamento, rs.getInt("cd_conta_plano_contas"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return ERR_LANCAMENTO_DEBITO;
				}
			}

			/* exclusão dos lançamento a crédito */
			pstmt = connection.prepareStatement("SELECT * " +
					"FROM ctb_lancamento_credito A " +
					"WHERE A.cd_lancamento = ?");
			pstmt.setInt(1, cdLancamento);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				if (LancamentoCreditoDAO.delete(cdLancamento, rs.getInt("cd_conta_plano_contas"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return ERR_LANCAMENTO_CREDITO;
				}
			}

			if (LancamentoDAO.delete(cdLancamento, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		String sql = "SELECT A.cd_lancamento, A.cd_lote, A.cd_lancamento_auto, A.dt_lancamento, A.vl_total, A.lg_provisao, A.cd_empresa, A.id_lancamento,  " +
		  "   B.nm_lancamento_auto, A.cd_lancamento_auto || ' - ' || B.nm_lancamento_auto AS ds_lancamento_auto, " +
		  "	  SUM(DISTINCT C.vl_lancamento) AS vl_total_credito, SUM(DISTINCT D.vl_lancamento) AS vl_total_debito " +
	      "FROM ctb_lancamento A " +
	      "   LEFT OUTER JOIN ctb_lancamento_auto B ON (A.cd_lancamento_auto = B.cd_lancamento_auto) " +
	      "	  LEFT OUTER JOIN ctb_lancamento_credito C ON (A.cd_lancamento = C.cd_lancamento) " +
	      "	  LEFT OUTER JOIN ctb_lancamento_debito D ON (A.cd_lancamento = D.cd_lancamento) " +
	      "WHERE (A.cd_lote IS NULL) ";
		ResultSetMap rsm = Search.find(sql, "GROUP BY A.cd_lancamento, A.cd_lote, A.cd_lancamento_auto, A.dt_lancamento, A.vl_total, A.lg_provisao, A.cd_empresa, A.id_lancamento, " +
			      "	  B.nm_lancamento_auto " + "ORDER BY A.dt_lancamento, A.vl_total", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		return rsm;
	}

	public static ResultSetMap findLancamentoAuto(ArrayList<ItemComparator> criterios) {
		return findLancamentoAuto(criterios, null);
	}

	public static ResultSetMap findLancamentoAuto(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, A.cd_lancamento_auto || ' - ' || A.nm_lancamento_auto AS ds_lancamento_auto, " +
						   "	B.nm_conta AS nm_conta_debito, " +
						   "	C.nm_conta AS nm_conta_credito, " +
						   "	E.nr_centro_custo || ' - ' || E.nm_centro_custo AS ds_centro_custo_debito, " +
						   "	F.nr_centro_custo || ' - ' || F.nm_centro_custo AS ds_centro_custo_credito, " +
						   " 	D.nm_historico, D.lg_complemento " +
						   "FROM ctb_conta B, ctb_conta C, ctb_lancamento_auto A " +
						   "	LEFT OUTER JOIN ctb_historico D ON (A.cd_historico = D.cd_historico) " +
						   "	LEFT OUTER JOIN ctb_centro_custo E ON (A.cd_centro_custo_debito = E.cd_centro_custo) " +
						   "	LEFT OUTER JOIN ctb_centro_custo F ON (A.cd_centro_custo_credito = F.cd_centro_custo) " +
						   "WHERE (A.cd_conta_debito = B.cd_conta) " +
						   "    AND (A.cd_conta_credito = C.cd_conta) ", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
