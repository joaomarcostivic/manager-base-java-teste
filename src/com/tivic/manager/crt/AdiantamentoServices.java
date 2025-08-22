package com.tivic.manager.crt;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class AdiantamentoServices {
	public static final int POR_PAGAMENTO = 0;
	public static final int POR_SEMANA = 1;
	public static final int POR_MES = 2;

	public static final int ST_SOLICITADO = 0;
	public static final int ST_AUTORIZADO = 1;
	public static final int ST_PAGO = 2;
	public static final int ST_DESCONTADO = 3;
	public static final int ST_NEGADO = 4;

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		String sql = "SELECT A.*, B.nm_pessoa "+
					 "FROM sce_adiantamento A, grl_pessoa B "+
				 	 "WHERE A.cd_pessoa = B.cd_pessoa ";
 		return Search.find(sql, "ORDER BY B.nm_pessoa ", criterios, Conexao.conectar(), true);
	}

	public static ResultSetMap findWithTotal(ArrayList<ItemComparator> criterios) {
		String sql = "SELECT A.cd_adiantamento, A.dt_adiantamento, A.vl_adiantamento, "+
					 "       A.qt_parcelas, A.tp_parcelamento, A.st_adiantamento, C.nm_empresa, " +
					 "       D.nm_pessoa, E.nr_conta, E.nr_dv, E.nr_agencia, E.tp_operacao, " +
					 "       E.nr_cpf_cnpj AS nr_cpf_cnpj_titular, E.nm_titular, F.nr_banco, " +
					 "       F.nm_banco, SUM(B.vl_pago) AS vl_pago "+
					 "FROM sce_adiantamento A "+
					 "LEFT OUTER JOIN sce_adiantamento_pagamento B ON (A.cd_adiantamento = B.cd_adiantamento) "+
					 "LEFT OUTER JOIN grl_empresa C ON (A.cd_empresa = C.cd_empresa) "+
					 "LEFT OUTER JOIN grl_pessoa  D ON (A.cd_pessoa = D.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa_conta_bancaria E ON (A.cd_pessoa = E.cd_pessoa" +
					 "                                            AND E.st_conta  = 1) " +
		             "LEFT OUTER JOIN grl_banco                 F ON (E.cd_banco  = F.cd_banco) ";
 		return Search.find(sql,
 						   "GROUP BY A.cd_adiantamento, A.dt_adiantamento, A.vl_adiantamento, "+
						   "         A.qt_parcelas, A.tp_parcelamento, A.st_adiantamento, C.nm_empresa, "+
						   "         D.nm_pessoa, E.nr_conta, E.nr_dv, E.nr_agencia, E.tp_operacao, " +
						   "         E.nr_cpf_cnpj, E.nm_titular, F.nr_banco, F.nm_banco "+
						   "ORDER BY A.dt_adiantamento", criterios, Conexao.conectar(), true);
	}

	public static ResultSetMap getPagamentosOfAdiantamento(int cdAdiantamento) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM sce_adiantamento_pagamento A, adm_conta_pagar B "+
			                                 "WHERE A.cd_conta_pagar  = B.cd_conta_pagar "+
			                                 "  AND A.cd_adiantamento = ? "+
			                                 "ORDER BY dt_vencimento");
			pstmt.setInt(1, cdAdiantamento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AdiantamentoServices.getPagamentosOfAdiantamento: " + e);
			return null;
		}
	}

	public static ResultSetMap getAdiantamentoEmAberto(int cdEmpresa, int cdPessoa, boolean somenteParaPagamento) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String sql = "SELECT A.cd_adiantamento, A.dt_adiantamento, A.vl_adiantamento, "+
						 "       A.qt_parcelas, A.tp_parcelamento, A.st_adiantamento, SUM(B.vl_pago) AS vl_pago "+
						 "FROM sce_adiantamento A "+
						 "LEFT OUTER JOIN sce_adiantamento_pagamento B ON (A.cd_adiantamento = B.cd_adiantamento) "+
						 "WHERE A.cd_empresa = ? "+
						 "  AND A.cd_pessoa  = ? "+
						 "  AND A.st_adiantamento IN ("+ST_PAGO+","+ST_AUTORIZADO+")"+
						 "GROUP BY A.cd_adiantamento, A.dt_adiantamento, A.vl_adiantamento, "+
						 "         A.qt_parcelas, A.tp_parcelamento, A.st_adiantamento "+
						 "HAVING (SUM(B.vl_pago) IS NULL) OR (A.vl_adiantamento > SUM(B.vl_pago)) "+
						 "ORDER BY A.dt_adiantamento";
            pstmt = connect.prepareStatement(sql);
            pstmt.setInt(1, cdEmpresa);
            pstmt.setInt(2, cdPessoa);
            ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
            while(somenteParaPagamento && rsm.next())	{
            	GregorianCalendar dtInicial = new GregorianCalendar();
            	dtInicial = new GregorianCalendar(dtInicial.get(Calendar.YEAR), dtInicial.get(Calendar.MONTH), dtInicial.get(Calendar.DAY_OF_MONTH));
            	GregorianCalendar dtFinal 	= (GregorianCalendar)dtInicial.clone();
            	if(rsm.getInt("tp_parcelamento")==POR_SEMANA)
   					dtInicial.add(Calendar.DAY_OF_MONTH, dtInicial.get(Calendar.DAY_OF_WEEK)*(-1)+1);
	           	else if(rsm.getInt("tp_parcelamento")==POR_MES)
   					dtInicial.set(Calendar.DAY_OF_MONTH, 1);
	           	else
	           		continue;
	           	pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar A, sce_adiantamento_pagamento B "+
	                                             "WHERE A.cd_conta_pagar = B.cd_conta_pagar "+
	                                             "  AND A.dt_vencimento BETWEEN ? AND ? "+
	                                             "  AND B.cd_adiantamento = ? ");
	            pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
	            pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
       			pstmt.setInt(3, rsm.getInt("cd_adiantamento"));
       			if(pstmt.executeQuery().next())
       				rsm.deleteRow();
            }
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.findAdiantamentoEmAberto: " +  e);
			return null;
		}
	}

	public static int setPagamento(int cdEmpresa, int cdPessoa, float vlPagamento, int cdContaPagar,
			Connection connect) {
		PreparedStatement pstmt;
		try {
			ResultSetMap rsm = AdiantamentoServices.getAdiantamentoEmAberto(cdEmpresa, cdPessoa, true);
			while(rsm.next() && vlPagamento>0)	{
				float vlSaldoDevedor = rsm.getFloat("vl_adiantamento") - rsm.getFloat("vl_pago");
				float vlParcela      = rsm.getFloat("vl_adiantamento") / rsm.getInt("qt_parcelas");
				vlParcela = vlParcela>vlSaldoDevedor ? vlSaldoDevedor : vlParcela;
				float vlPago = vlPagamento>vlParcela ? vlParcela : vlPagamento;
				vlPagamento -= vlPago;
				vlSaldoDevedor -= vlPagamento;
	            pstmt = connect.prepareStatement("INSERT INTO sce_adiantamento_pagamento "+
	                                             " (cd_adiantamento,cd_conta_pagar, vl_pago) "+
	                                             "VALUES (?,?,?)");
    	        pstmt.setInt(1, rsm.getInt("cd_adiantamento"));
        	    pstmt.setInt(2, cdContaPagar);
        	    pstmt.setFloat(3, vlPago);
        	    pstmt.executeUpdate();
        	    // Alterando a situação do adiantamento
        	    if(vlSaldoDevedor <= 0)	{
    	            pstmt = connect.prepareStatement(
    	            		"UPDATE sce_adiantamento SET st_adiantamento = ? "+
    	            		"WHERE cd_adiantamento = ? ");
					pstmt.setInt(1, ST_DESCONTADO);
					pstmt.setInt(2, rsm.getInt("cd_adiantamento"));
					pstmt.executeUpdate();
        	    }
			}
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.setPagamento: " +  e);
			return -1;
		}
	}

	public static int updatePagamento(int cdAdiantamento, int cdContaPagar, float vlPagamento) {
		Connection connect = Conexao.conectar();
		try {
			// verifica se a conta não está fechada
			if(!connect.prepareStatement("SELECT * FROM adm_conta_pagar  "+
									 	 "WHERE cd_conta_pagar = "+cdContaPagar+
									 	 "  AND st_conta = 0").executeQuery().next())
			{
				return -10;
			}
			ResultSet rs = connect.prepareStatement("SELECT A.vl_adiantamento, SUM(B.vl_pago) AS vl_pago " +
					                                "FROM sce_adiantamento A " +
					                                "LEFT OUTER JOIN sce_adiantamento_pagamento B ON (A.cd_adiantamento = B.cd_adiantamento" +
					                                "                                             AND B.cd_conta_pagar <> "+cdContaPagar+") "+
				 	                             	"WHERE A.cd_adiantamento = "+cdAdiantamento+
				 	                             	" GROUP BY A.vl_adiantamento").executeQuery();
			if (!rs.next())	{
				return -20;

			}
			if(rs.getFloat("vl_adiantamento") - rs.getFloat("vl_pago") < vlPagamento)
				return -30;
			float vlConta = 0;
			float vlAbatimento = 0;
			// Somando comissões
			rs = connect.prepareStatement("SELECT SUM(vl_pago) FROM sce_contrato_comissao "+
										  "WHERE cd_conta_pagar = "+cdContaPagar+
										  "  AND vl_pago > 0").executeQuery();
			if(rs.next())
				vlConta = rs.getFloat(1);
			// Somando estornos
			rs = connect.prepareStatement("SELECT SUM(vl_pago) FROM sce_contrato_comissao "+
										  "WHERE cd_conta_pagar = "+cdContaPagar+
										  "  AND vl_pago < 0").executeQuery();
			if(rs.next())
				vlAbatimento = (rs.getFloat(1) * -1);
			// Somando Adiantamentos
			rs = connect.prepareStatement("SELECT SUM(vl_pago) FROM sce_adiantamento_pagamento "+
					 					  "WHERE cd_conta_pagar = "+cdContaPagar+
					 					  "  AND cd_adiantamento <> "+cdAdiantamento).executeQuery();
			if(rs.next())
				vlAbatimento += rs.getFloat(1);
			// verifica se há saldo para atualizar adiantamento
            if((vlConta - vlAbatimento) > vlPagamento)	{
            	connect.setAutoCommit(false);
            	int ret = connect.prepareStatement("UPDATE sce_adiantamento_pagamento SET vl_pago = "+vlPagamento+
											       " WHERE cd_conta_pagar  = "+cdContaPagar+
											       "   AND cd_adiantamento = "+cdAdiantamento).executeUpdate();
            	if(ret > 0)	{
            		ContaPagarServices.atualizaValoresConta(cdContaPagar, connect);
            		connect.commit();
            	}
            	else
            		Conexao.rollback(connect);
            	return ret;
            }
            else
            	return -40;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.setPagamento: " +  e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int deletePagamentoOfAdiantamento(int cdAdiantamento, int cdContaPagar) {
		Connection connect = Conexao.conectar();
		try {
			// Alterando situação dos adiantamentos
            PreparedStatement pstmt = connect.prepareStatement("SELECT st_conta FROM adm_conta_pagar " +
					                                           "WHERE cd_conta_pagar = ?");
            pstmt.setInt(1, cdContaPagar);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())	{
            	if(rs.getInt("st_conta")==1)
            		return -10;
            }
            else
            	return -20;
            connect.setAutoCommit(false);
            pstmt = connect.prepareStatement("UPDATE sce_adiantamento SET st_adiantamento = ? "+
              								 "WHERE cd_adiantamento = ?");
            pstmt.setInt(1, ST_PAGO);
            pstmt.setInt(2, cdAdiantamento);
            pstmt.executeUpdate();
            // Excluindo pagamentos
            pstmt = connect.prepareStatement("DELETE FROM sce_adiantamento_pagamento "+
                              				 "WHERE cd_conta_pagar = ? "+
                              				 "  AND cd_adiantamento = ?");
      	    pstmt.setInt(1, cdContaPagar);
      	    pstmt.setInt(2, cdAdiantamento);
      	    pstmt.executeUpdate();
      	    ContaPagarServices.atualizaValoresConta(cdContaPagar, connect);
      	    connect.commit();
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.deletePagamento: " +  e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
}