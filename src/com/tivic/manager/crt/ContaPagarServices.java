package com.tivic.manager.crt;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import com.tivic.manager.conexao.*;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.sol.connection.Conexao;

public class ContaPagarServices {

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		String sql = "SELECT A.*, B.nm_empresa, C.nm_pessoa, D.*, E.nr_banco, E.nm_banco "+
		             "FROM adm_conta_pagar A "+
		             "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) "+
		             "JOIN grl_pessoa  C ON (A.cd_pessoa  = C.cd_pessoa) "+
		             "LEFT OUTER JOIN grl_pessoa_conta_bancaria D ON (A.cd_pessoa = D.cd_pessoa "+
		             "                                            AND A.cd_conta_bancaria = D.cd_conta_bancaria) "+
		             "LEFT OUTER JOIN grl_banco                 E ON (D.cd_banco = E.cd_banco) ";
		return Search.find(sql, "ORDER BY dt_vencimento, nm_empresa, nm_pessoa", criterios, Conexao.conectar(), true, false);
	}

	public static ResultSetMap findWithComissao(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			String sql = "SELECT A.*, B.nm_empresa, C.nm_pessoa, D.*, E.nr_banco, E.nm_banco, "+
						 "       F.cd_comissao, F.vl_comissao, F.vl_pago AS vl_pago_comissao, F.pr_aplicacao, "+
						 "       G.cd_contrato_emprestimo, G.dt_contrato, G.qt_parcelas AS qt_parcelas_contrato, "+
						 "       G.dt_pagamento, G.vl_financiado, G.vl_liberado, "+
						 "       H.nm_produto, I.nm_pessoa AS nm_contratante, "+
						 "       J.nm_pessoa AS nm_parceiro "+
			             "FROM adm_conta_pagar A "+
			             "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) "+
			             "JOIN grl_pessoa  C ON (A.cd_pessoa  = C.cd_pessoa) "+
			             "LEFT OUTER JOIN grl_pessoa_conta_bancaria D ON (A.cd_pessoa = D.cd_pessoa "+
			             "                                            AND A.cd_conta_bancaria = D.cd_conta_bancaria) "+
			             "LEFT OUTER JOIN grl_banco                 E ON (D.cd_banco = E.cd_banco) "+
			             "JOIN sce_contrato_comissao F ON (A.cd_conta_pagar = F.cd_conta_pagar) "+
			             "JOIN sce_contrato G ON (F.cd_contrato_emprestimo = G.cd_contrato_emprestimo) "+
			             "JOIN sce_produto  H ON (G.cd_produto = H.cd_produto ) "+
			             "JOIN grl_pessoa   I ON (G.cd_contratante = I.cd_pessoa)"+
			             "JOIN grl_pessoa   J ON (H.cd_instituicao_financeira = J.cd_pessoa)";
			ResultSetMap rsm = Search.find(sql, "ORDER BY A.dt_vencimento, B.nm_empresa, C.nm_pessoa", criterios, Conexao.conectar(), true);
			int cdContaPagar = 0;
			while(rsm.next())
				if(rsm.getFloat("vl_abatimento")>0 && cdContaPagar!=rsm.getInt("cd_conta_pagar")){
					cdContaPagar = rsm.getInt("cd_conta_pagar");
					pstmt = connect.prepareStatement("SELECT * FROM sce_adiantamento_pagamento A, sce_adiantamento B "+
							                         "WHERE A.cd_conta_pagar = ? "+
							                         "  AND A.cd_adiantamento = B.cd_adiantamento");
					pstmt.setInt(1, rsm.getInt("cd_conta_pagar"));
					ResultSet rs = pstmt.executeQuery();
					while(rs.next()){
						HashMap<String,Object> register = new HashMap<String,Object>();
						register.put("CD_CONTA_PAGAR", new Integer(rsm.getInt("cd_conta_pagar")));
						register.put("CD_ADIANTAMENTO", new Integer(rs.getInt("cd_adiantamento")));
						register.put("NM_EMPRESA", rsm.getString("NM_EMPRESA"));
						register.put("NM_PESSOA", rsm.getString("NM_PESSOA"));
						register.put("NM_PARCEIRO", "");
						register.put("NM_PRODUTO", "");
						register.put("TP_COMISSAO", new Integer(-1));
						register.put("VL_PAGO_COMISSAO", new Float(rs.getFloat("VL_PAGO")*(-1)));
						register.put("DT_CONTRATO", rs.getTimestamp("DT_ADIANTAMENTO"));
						register.put("VL_LIBERADO", new Float(rs.getFloat("VL_ADIANTAMENTO")));
						register.put("PR_APLICACAO", new Float(0));
						register.put("QT_PARCELAS_CONTRATO", new Integer(rs.getInt("QT_PARCELAS")));
						register.put("NM_CONTRATANTE", "Desconto de Adiantamento");
						rsm.addRegister(register);
					}
				}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.findWithComissao: " +  e);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		String group  = "";
		String fields = " A.*, B.nm_empresa, C.nm_pessoa, D.*, E.nr_banco, E.nm_banco ";

		for(int i=0; i<groupBy.size();i++)	{
			String nmToGroup = (String)groupBy.get(i);
			String nmToField = (String)groupBy.get(i);
			if(nmToGroup.toUpperCase().indexOf("AS")>=0)	{
				nmToGroup = nmToGroup.substring(0, nmToGroup.toUpperCase().indexOf("AS"));
				nmToField = nmToField.substring(nmToField.toUpperCase().indexOf("AS")+3, nmToField.length()).trim();
			}
			//
			if(nmToField.equals("NM_EMPRESA"))	{
				nmToField = " A.cd_empresa, "+nmToGroup+" AS "+nmToField;
				nmToGroup = " A.cd_empresa, "+nmToGroup;
			}
			else if(nmToField.equals("NM_PESSOA"))	{
				nmToField = " A.cd_pessoa, "+nmToGroup+" AS "+nmToField;
				nmToGroup = " A.cd_pessoa, "+nmToGroup;
			}
			else if(nmToField.equals("NM_BANCO"))	{
				nmToField = " E.cd_banco, nr_banco, "+nmToGroup+" AS "+nmToField;
				nmToGroup = " E.cd_banco, E.nr_banco, "+nmToGroup;
			}
			else
				nmToField = nmToGroup+" AS "+nmToField;
			if(i==0)	{
				group  = "GROUP BY "+nmToGroup;
				fields = nmToField;
			}
			else	{
				fields = fields+", "+nmToField;
				group  = group+", "+nmToGroup;
			}
		}
		fields = groupBy.size()==0 ? fields : fields + ", COUNT(*) AS QT_CONTA, SUM(vl_conta) AS vl_conta,  "+
		                                               "  SUM(vl_abatimento) AS vl_abatimento ";


		String sql = "SELECT "+fields+" "+
		             "FROM adm_conta_pagar A "+
		             "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) "+
	    	         "JOIN grl_pessoa  C ON (A.cd_pessoa  = C.cd_pessoa) "+
	        	     "LEFT OUTER JOIN grl_pessoa_conta_bancaria D ON (A.cd_pessoa = D.cd_pessoa "+
	            	 "                                            AND A.cd_conta_bancaria = D.cd_conta_bancaria) "+
		             "LEFT OUTER JOIN grl_banco                 E ON (D.cd_banco = E.cd_banco) ";
		return Search.find(sql, group, criterios, Conexao.conectar(), true);
	}


	public static int setPagamento(int cdContaPagar, GregorianCalendar dtPagamento, float vlPago) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			// Confirmando pagamento da conta
			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement(
					"UPDATE adm_conta_pagar SET st_conta = 1, dt_pagamento = ?, vl_pago = "+vlPago+
					" WHERE cd_conta_pagar = "+cdContaPagar);
			pstmt.setTimestamp(1, new Timestamp(dtPagamento.getTimeInMillis()));
			pstmt.executeUpdate();
			// Confirmando o dia de pagamento dos agentes
			pstmt = connect.prepareStatement(
					"UPDATE sce_contrato " +
					"SET dt_pagamento_agente = (SELECT MAX(A.dt_pagamento) " +
					"                           FROM adm_conta_pagar A, sce_contrato_comissao B "+
                    "                           WHERE A.cd_conta_pagar = B.cd_conta_pagar "+
                    "                             AND B.cd_contrato_emprestimo = sce_contrato.cd_contrato_emprestimo) "+
                    "WHERE cd_contrato_emprestimo IN (SELECT cd_contrato_emprestimo FROM sce_contrato_comissao CC " +
                    "                                 WHERE CC.cd_conta_pagar = "+cdContaPagar+")");
			pstmt.executeUpdate();
			// Atualiza situação das comissões
			pstmt = connect.prepareStatement(
					"UPDATE sce_contrato_comissao SET dt_pagamento = ?, cd_situacao = ? "+
					"WHERE cd_conta_pagar = ? ");
			pstmt.setTimestamp(1, new Timestamp(dtPagamento.getTimeInMillis()));
			if (ParametroServices.getValorOfParametro("CD_COMISSAO_PAGA")==null)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, new Integer(ParametroServices.getValorOfParametro("CD_COMISSAO_PAGA")).intValue());
			pstmt.setInt(3, cdContaPagar);
			pstmt.executeUpdate();
			connect.commit();
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setContaPagar: " +  e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int setPagamento(String lista, GregorianCalendar dtPagamento) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement(
					"UPDATE adm_conta_pagar SET st_conta = 1, dt_pagamento = ?, vl_pago = vl_conta "+
					"WHERE cd_conta_pagar IN ("+lista+")");
			pstmt.setTimestamp(1, new Timestamp(dtPagamento.getTimeInMillis()));
			pstmt.executeUpdate();
			// Atualiza situação das comissões
			pstmt = connect.prepareStatement(
					"UPDATE sce_contrato_comissao SET dt_pagamento = ?, cd_situacao = ? "+
					"WHERE cd_conta_pagar IN ("+lista+")");
			pstmt.setTimestamp(1, new Timestamp(dtPagamento.getTimeInMillis()));
			if (ParametroServices.getValorOfParametro("CD_COMISSAO_PAGA")==null)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, new Integer(ParametroServices.getValorOfParametro("CD_COMISSAO_PAGA")).intValue());
			pstmt.executeUpdate();
			connect.commit();
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setContaPagar: " +  e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int atualizaValoresConta(int cdContaPagar, Connection connect) {
		PreparedStatement pstmt;
		try	{
			float vlConta = 0;
			float vlAbatimento = 0;
			// Somando comissões
			ResultSet rs = connect.prepareStatement("SELECT SUM(vl_pago) FROM sce_contrato_comissao "+
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
					 					  "WHERE cd_conta_pagar = "+cdContaPagar).executeQuery();
			if(rs.next())
				vlAbatimento += rs.getFloat(1);
			// Atualizando valor da conta
			if(vlConta==0 && vlAbatimento==0)	{
				pstmt = connect.prepareStatement("DELETE FROM adm_conta_pagar WHERE cd_conta_pagar = "+cdContaPagar);
			}
			else	{
				pstmt = connect.prepareStatement(
						 "UPDATE adm_conta_pagar SET vl_conta = "+vlConta+", vl_abatimento = "+vlAbatimento+
						 " WHERE cd_conta_pagar = "+cdContaPagar);
			}
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setContaPagar: " +  e);
			return -1;
		}
	}

	public static int atualizaValoresConta() {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			// Atualizando valores das comissões
			pstmt = connect.prepareStatement(
				"SELECT cd_comissao, pr_aplicacao, vl_liberado, vl_comissao, A.vl_pago "+
				"FROM sce_contrato_comissao A, sce_contrato B, adm_conta_pagar C, sce_produto D, "+
				"     grl_pessoa E, grl_empresa F "+
				"WHERE A.cd_contrato_emprestimo = B.cd_contrato_emprestimo "+
				"  AND A.cd_conta_pagar = C.cd_conta_pagar "+
				"  AND C.st_conta = 0 "+
				"  AND B.cd_produto = D.cd_produto "+
				"  AND B.cd_empresa = F.cd_empresa "+
				"  AND D.nm_produto LIKE \'INSS%\'"+
				"  AND D.cd_instituicao_financeira = E.cd_pessoa "+
				"  AND E.nm_pessoa IN (\'GE\',\'BMC\') "+
				"  AND F.nm_empresa IN (\'MACEIO\',\'SALVADOR\',\'V. DA CONQUISTA\',\'ILHEUS\') "+
				"  AND A.tp_comissao = 2 "+
				"  AND B.qt_parcelas = 36 ");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next())	{
				pstmt = connect.prepareStatement(
						"UPDATE sce_contrato_comissao SET vl_pago = ? "+
						"WHERE cd_comissao = ?");
				pstmt.setFloat(1, rsm.getFloat("pr_aplicacao") * rsm.getFloat("vl_liberado") / 100);
				pstmt.setInt(2, rsm.getInt("cd_comissao"));
				pstmt.executeUpdate();
				System.out.println(rsm.getInt("cd_comissao")+": vlLiberado = "+rsm.getFloat("vl_liberado")+
						                      "prAplicacao = "+rsm.getFloat("pr_aplicacao")+"  = "+
						                      (rsm.getFloat("pr_aplicacao") * rsm.getFloat("vl_liberado") / 100));
			}
			// Somando comissões
			pstmt = connect.prepareStatement(
					 "SELECT cd_conta_pagar FROM adm_conta_pagar "+
					 "WHERE st_conta = 0 ");
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())	{
				System.out.println("cdCotnaPagar = "+rs.getInt(1));
				atualizaValoresConta(rs.getInt(1), connect);
			}
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setContaPagar: " +  e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int setAutorizacao(String lista, int lgAutorizacao) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			GregorianCalendar dt = new GregorianCalendar();
			dt = new GregorianCalendar(dt.get(Calendar.YEAR), dt.get(Calendar.MONTH), dt.get(Calendar.DAY_OF_MONTH));
			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement(
					"UPDATE adm_conta_pagar SET lg_autorizado = ?, dt_autorizacao = ? "+
					"WHERE cd_conta_pagar IN ("+lista+")");
			pstmt.setInt(1, lgAutorizacao);
			pstmt.setTimestamp(2, new Timestamp(dt.getTimeInMillis()));
			pstmt.executeUpdate();
			connect.commit();
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.setContaPagar: " +  e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
}