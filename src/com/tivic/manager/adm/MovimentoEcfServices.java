package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;

import sol.util.Result;

public class MovimentoEcfServices	{
	
	public static sol.util.Result insert(MovimentoEcf objeto, int cdResponsavel, int cdConta, int cdTurno) {
		Connection connect = Conexao.conectar();
		try {
			// Verifica se o responsável foi passado
			if(cdResponsavel <= 0 || cdConta<= 0)
				return new Result(1, "O responsável pela abertura do caixa ou a conta a ser aberta não foi[ram] informado(s). [cdResponsavel="+cdResponsavel+",cdConta="+cdConta+"]");
			// Verifica se já não foi aberta por outro usuário
			ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
			if(conta.getCdResponsavel()>0 && conta.getCdResponsavel()!=cdResponsavel)	{
				Pessoa pessoa = PessoaDAO.get(conta.getCdResponsavel(), connect);
				new Result(-1, "A conta informada já foi aberta por outro usuário! [Aberta por: "+pessoa.getNmPessoa()+"]");
			}
			Result result = new Result(1);
			// Registra marcadores do ECF
			connect.setAutoCommit(false);
			MovimentoEcf movimentoECF = MovimentoEcfDAO.get(objeto.getCdReferencia(), objeto.getDtMovimento(), connect);
			if(movimentoECF == null)	{
				result.setCode(MovimentoEcfDAO.insert(objeto, null));
			
				if(result.getCode() <= 0)	{
					Conexao.rollback(connect);
					return result;
				}
			}
			// Registra o responsável pela abertura da conta
			result.setCode(connect.prepareStatement("UPDATE adm_conta_financeira " +
					                                "SET cd_responsavel = "+cdResponsavel+
					                                "   ,cd_turno       = "+(cdTurno> 0 ? String.valueOf(cdTurno) : " null ")+
					                                " WHERE cd_conta = "+cdConta).executeUpdate());			
			if(result.getCode() <= 0)	{
				result.setMessage("Erro ao tentar definir o responsável pela conta!");
				Conexao.rollback(connect);
				return result;
			}
			//
			connect.commit();
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return new sol.util.Result(-1, "Erro ao tentar registrar abertura do ECF!", e);
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static int isMovimentoDiaAberto(int cdReferenciaEcf, GregorianCalendar dtMovimento){
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT * FROM adm_movimento_ecf " +
					 "WHERE cd_referencia = "+cdReferenciaEcf+
					 "  AND dt_movimento  = ?");
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			if(pstmt.executeQuery().next())
				return 1;
			else
				return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static int setFechamento(int cdReferenciaEcf, GregorianCalendar dtMovimento, 
			String nrContadorFinal, String nrReducao, float vlVendaBruta, float vlGeralAcumulado){
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					 "UPDATE adm_movimento_ecf " +
					 "SET nr_contador_final = \'"+nrContadorFinal+"\', " +
					 "    nr_reducao = \'"+nrReducao+"\', " +
					 "    vl_venda_bruta = "+vlVendaBruta+", " +
					 "    vl_geral_acumulado = "+vlGeralAcumulado+
					 " WHERE cd_referencia = "+cdReferenciaEcf+
					 "   AND dt_movimento  = ?");
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			return pstmt.executeUpdate();
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfServices.setFechamento: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static int existeMovimentoEmAberto(int cdReferenciaEcf){
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT * FROM adm_movimento_ecf " +
					 "WHERE cd_referencia = "+cdReferenciaEcf+
					 "  AND nr_contador_final IS NULL");
			if(pstmt.executeQuery().next())
				return 1;
			else
				return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoEcfServices.isMovimentoDiaAberto: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
		
	}

}