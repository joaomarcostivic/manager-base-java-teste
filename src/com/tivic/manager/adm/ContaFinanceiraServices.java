package com.tivic.manager.adm;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.ctb.Conta;
import com.tivic.manager.grl.Banco;
import com.tivic.manager.grl.BancoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.pcb.BicoHistoricoServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.Util;

public class ContaFinanceiraServices {

	public static final int TP_CAIXA          = 0;
	public static final int TP_CONTA_BANCARIA = 1;

	public static final String[] tipoOperacao = {"001-C. Corrente (P.F.)", "002-C. Simples (P.F.)",
		                                         "003-C. Corrente (P.J.)", "013-Poupança (P.F.)",
                                                 "022-Poupança (P.J.)", "023-Conta Caixa Aqui",
                                                 "032-Poupança (P.F.)", "034-Poupança (P.J.)"};

	
	private static String getLabelTipoOperacao(int tpOperacao){
		String label = "";
		for( int i=0;i<tipoOperacao.length;i++){
			if( Integer.parseInt( tipoOperacao[i].substring(1, 3)) == tpOperacao ){
				label = tipoOperacao[i];
				break;
			}
		}
		return label;
		
	}
	/**
	 * @see this{@link #save(ContaFinanceira, Connection)}
	 */
	public static Result save(ContaFinanceira contaFinanceira){
		return save(contaFinanceira, null);
	}
	
	/**
	 * Método para encapsular processo de inserir ou atualizar
	 * 
	 * @author Luiz Romario Filho
	 * @param contaFinanceira entidade a ser persistida
	 * @param conexão caso precise executar uma transação
	 * @since 28/08/2014
	 * @return result código e mensagem da transação, code >= 0 ? sucesso : erro
	 */
	public static Result save(ContaFinanceira contaFinanceira, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(contaFinanceira==null)
				return new Result(-1, "Erro ao salvar. Conta Financeira é nulo");
			
			int retorno;
			if(contaFinanceira.getCdConta()==0){
				retorno = ContaFinanceiraDAO.insert(contaFinanceira, connect);
				contaFinanceira.setCdConta(retorno);
			}
			else {
				retorno = ContaFinanceiraDAO.update(contaFinanceira, connect);
			}
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTAFINANCEIRA", contaFinanceira);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static int insert(ContaFinanceira objeto, int cdUsuario) {
		return insert(objeto, cdUsuario, null);
	}

	public static int insert(ContaFinanceira objeto, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			connect.setAutoCommit(false);
			int cdConta = ContaFinanceiraDAO.insert(objeto, connect);
			if(cdConta > 0)
				insertUsuarioConta(cdUsuario, cdConta, connect);
			else	{
				Conexao.rollback(connect);
				return -10;
			}
			connect.commit();
			return cdConta;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/*
	 * Get Extrato pega o Arquivo OFC e gera um extrato no sistema com todas as operações registradas no log
	 */
	public static Result getExtrato(byte[] fileOfc, int cdConta)	{
		Connection connect = Conexao.conectar();
		try	{
			String[] tipoMovimento = new String[] {"Crédito","Débito","Interesse","Dividendo","Taxa Bancária",
												   "Depósito","Retirada em ATM","Transferência","Cheque",
												   "Pagamento","Saque","Depósito de Salário","Outros"};
			ResultSetMap rsm = new ResultSetMap();
			String idBanco="", nrConta="";
			GregorianCalendar dtInicial=null, dtFinal=null, dtAuxilio=null;
			Double vlSaldoFinal = 0.0;
			Double vlSaldoInicial = 0.0;
			BufferedReader reader = fileOfc==null ? null : new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileOfc)));
			String line = "";
			HashMap<String,Object> register = new HashMap<String,Object>();
			Double vlMovimento = 0.0;
			int ordemDaOperacaoNoDia=0;
			
			
			/*
			 * Laço que busca os dados do extrato no arquivo ofc
			 * */
			while(reader!=null && (line = reader.readLine())!=null)	{
				// ID do Banco
				if(line.indexOf("<BANKID>")>=0)
					idBanco = line.substring(line.indexOf(">")+1);
				// ID da Conta
				if(line.indexOf("<ACCTID>")>=0)
					nrConta = line.substring(line.indexOf(">")+1);
				// Tipo de Conta
				/*
				if(line.indexOf("<ACCTTYPE>")>=0)
					tpConta = line.substring(line.indexOf(">")+1);
				*/
				// Data inicial do período
				if(line.indexOf("<DTSTART>")>=0)	{
					String d = line.substring(line.indexOf(">")+1);
					d = d.substring(6,8)+"/"+d.substring(4,6)+"/"+d.substring(0,4);
					dtInicial = Util.convStringToCalendar(d);
				}
				// Data inicial do período
				if(line.indexOf("<DTEND>")>=0)	{
					String d = line.substring(line.indexOf(">")+1);
					d = d.substring(6,8)+"/"+d.substring(4,6)+"/"+d.substring(0,4);
					dtFinal = Util.convStringToCalendar(d);
				}
				// Saldo
				if(line.indexOf("<LEDGER>")>=0)
					vlSaldoFinal = Double.parseDouble(line.substring(line.indexOf(">")+1));
				// Início da transação
				if(line.indexOf("<STMTTRN>")>=0)
					register = new HashMap<String,Object>();
				// Data do Movimento
				if(line.indexOf("<DTPOSTED>")>=0)	{
					String d = line.substring(line.indexOf(">")+1);
					d = d.substring(6,8)+"/"+d.substring(4,6)+"/"+d.substring(0,4);
					register.put("DT_MOVIMENTO", Util.convStringToCalendar(d));
					register.put("DT_POSTED", Util.convStringToCalendar(d));
					String dtMovimento=d.substring(0,2)+d.substring(3,5)+d.substring(6,10);
					register.put("dtMovimento",dtMovimento);
					/*
					 * Verifica a ordem em que aparece a transação no extrato para efeito de geração do id_extrato de acordo com a data
					 * em que foi feita a operação
					 */
					if(ordemDaOperacaoNoDia==0){//verifica se a primeira vez que se esta procurando no extrato
						dtAuxilio=Util.convStringToCalendar(d);//guarda temporariamente a data que está sendo analisada
						ordemDaOperacaoNoDia++;
					}else{
						if(!(dtAuxilio.equals(Util.convStringToCalendar(d)))){//caso mude a data 
							ordemDaOperacaoNoDia=1;//reinicia-se o contador para o primeiro movimento do dia
							dtAuxilio=Util.convStringToCalendar(d);//guarda a nova data do dia atual que está sendo analisado
						}else{
							ordemDaOperacaoNoDia++;//caso a data não mude, apenas incrementa o contador da ordem dos movimentos
						}
					}
				}
				// Tipo de Movimento
				if(line.indexOf("<TRNTYPE>")>=0)
					register.put("TP_ORIGEM", line.substring(line.indexOf(">")+1));
				// Valor do Movimento
				if(line.indexOf("<TRNAMT>")>=0)	{
					vlMovimento = Double.parseDouble(line.substring(line.indexOf(">")+1));
					register.put("VL_MOVIMENTO", new Float(line.substring(line.indexOf(">")+1)));
					register.put("TP_MOVIMENTO", Float.parseFloat(line.substring(line.indexOf(">")+1))>0?"0":"1");
					register.put("CL_TIPO", Float.parseFloat(line.substring(line.indexOf(">")+1))>0?"C":"D");
				}
				// Número do Documento
				if(line.indexOf("<CHKNUM>")>=0)
					register.put("NR_DOCUMENTO", line.substring(line.indexOf(">")+1));
				// ID
				if(line.indexOf("<FITID>")>=0)
					//register.put("ID_MOVIMENTO", line.substring(line.indexOf(">")+1)); //linha comentada em 12-03-2010
				// Número do Documento
				if(line.indexOf("<MEMO>")>=0)
					register.put("DS_HISTORICO", line.substring(line.indexOf(">")+1));
				//
				if(line.indexOf("</STMTTRN>")>=0)	{
					String dsHistorico = (String)register.get("DS_HISTORICO");
					//gera um id movimento com o campo do log dtMovimento(<DTPOSTED>) + VL_MOVIMENTO(<TRNAMT>) + ordemDaOperacaoNoDia
					String idMovimento = (String)register.get("dtMovimento")+"_"+Math.abs(Float.parseFloat((register.get("VL_MOVIMENTO").toString())))+"_"+ordemDaOperacaoNoDia+"|";
					register.put("ID_MOVIMENTO",idMovimento);
					dsHistorico = dsHistorico!=null ? dsHistorico.toLowerCase() : "";
					int tpOrigem = register.get("TP_ORIGEM")==null || !Util.isWellFormattedIntegerValue(register.get("TP_ORIGEM").toString()) ?
							MovimentoContaServices.toCREDITO : Integer.parseInt((String)register.get("TP_ORIGEM"));
					if(vlMovimento<0 && dsHistorico.indexOf("cheque")>=0)
						tpOrigem = 8;
					else if(dsHistorico.indexOf("transferencia")>=0 || dsHistorico.indexOf("transferência")>=0)
						tpOrigem = 7;
					else if(dsHistorico.indexOf("tarifa")>=0 || dsHistorico.indexOf("taxa")>=0)
						tpOrigem = 4;
					else if(dsHistorico.indexOf("saque")>=0)
						tpOrigem = 10;
					else if(dsHistorico.indexOf("pagamento")>=0 || dsHistorico.indexOf("imposto")>=0 || dsHistorico.indexOf("inss")>=0||dsHistorico.indexOf("pagto")>=0)
						tpOrigem = 9;
					register.put("TP_ORIGEM", String.valueOf(tpOrigem));
					register.put("CL_ORIGEM", tipoMovimento[tpOrigem]);
					register.put("NR_BANCO", idBanco);
					register.put("NR_CONTA", nrConta);
					register.put("DT_INICIAL", dtInicial);
					register.put("DT_FINAL", dtFinal);
					if(connect.prepareStatement("SELECT * FROM adm_movimento_conta" +
							                    " WHERE id_extrato like \'%|"+idMovimento+"%\'" +
							                    "  AND cd_conta="+cdConta).executeQuery().next()){
						register.put("LG_CONCILIADO", "OK");   
					}else
						register.put("LG_CONCILIADO", "*");
					rsm.addRegister(register);
				}
			}
			rsm.last();
			if(rsm.size()>0){
				vlSaldoInicial = vlSaldoFinal;
				do {
					rsm.setValueToField("VL_SALDO", vlSaldoInicial);
					vlSaldoInicial -= rsm.getDouble("VL_MOVIMENTO");
				}while(rsm.previous());
			}
			rsm.beforeFirst();
			reader.close();
			Result result = new Result(1, "", "RSMEXTRATO", rsm);
			HashMap<String, Object> extrato = new HashMap<String, Object>();
			extrato.put("NR_CONTA", nrConta);
			extrato.put("ID_BANCO", idBanco);
			extrato.put("DT_INICIAL", dtInicial);
			extrato.put("DT_FINAL", dtFinal);
			extrato.put("VL_SALDO_INICIAL", vlSaldoInicial);
			extrato.put("VL_SALDO_FINAL", vlSaldoFinal);
			result.getObjects().put("EXTRATO", extrato);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getExtrato: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	/**
	 * @author Álvaro Santos
	 * @param fileOfx
	 * @param cdConta
	 * @return
	 * 
	 * Especificação do PADRÃO OFX
	 * http://www.ofx.net/downloads.html
	 */
	public static Result getExtratoOfx(byte[] fileOfx, int cdConta)	{
		Connection connect = Conexao.conectar();
		try	{
			String[] tipoMovimento = new String[] {"Crédito","Débito","Interesse","Dividendo","Taxa Bancária",
												   "Depósito","Retirada em ATM","Transferência","Cheque",
												   "Pagamento","Saque","Depósito de Salário","Outros"};
			/**
			 * REPEATPMT - Repeating payment/standing order
			 * oque mais se aproxima da "Depósito de Salário"
			 */
			HashMap<String, Integer> tipoMovimentoOFX = new HashMap<String, Integer>();
			tipoMovimentoOFX.put("CREDIT", 0);
			tipoMovimentoOFX.put("DEBIT", 1);
			tipoMovimentoOFX.put("INT", 2);
			tipoMovimentoOFX.put("DIV", 3);
			tipoMovimentoOFX.put("FEE", 4);
			tipoMovimentoOFX.put("DEP", 5);
			tipoMovimentoOFX.put("ATM", 6);
			tipoMovimentoOFX.put("XFER", 7);
			tipoMovimentoOFX.put("CHECK", 8);
			tipoMovimentoOFX.put("PAYMENT", 9);
			tipoMovimentoOFX.put("CASH", 10);
			tipoMovimentoOFX.put("REPEATPMT", 11);
			tipoMovimentoOFX.put("OTHER", 12);

			ResultSetMap rsm = new ResultSetMap();
			String idBanco="", nrConta="";
			GregorianCalendar dtInicial=null, dtFinal=null, dtAuxilio=null;
			Double vlSaldoFinal = 0.0;
			Double vlSaldoInicial = 0.0;
			BufferedReader reader = fileOfx==null ? null : new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileOfx)));
			String line = "";
			HashMap<String,Object> register = new HashMap<String,Object>();
			Double vlMovimento = 0.0;
			int ordemDaOperacaoNoDia=0;
			
			/*
			 * Laço que busca os dados do extrato no arquivo ofx
			 * */
			while(reader!=null && (line = reader.readLine())!=null)	{
				// ID do Banco
				if(line.indexOf("<BANKID>")>=0)
					idBanco = line.substring(line.indexOf(">")+1).replace("</BANKID>", "");
				// ID da Conta
				if(line.indexOf("<ACCTID>")>=0)
					nrConta = line.substring(line.indexOf(">")+1).replace("</ACCTID>", "");
				// Tipo de Conta
				/*
				if(line.indexOf("<ACCTTYPE>")>=0)
					tpConta = line.substring(line.indexOf(">")+1);
				*/
				// Data inicial do período
				if(line.indexOf("<DTSTART>")>=0)	{
					String d = line.substring(line.indexOf(">")+1).replace("</DTSTART>", "");
					d = d.substring(6,8)+"/"+d.substring(4,6)+"/"+d.substring(0,4);
					dtInicial = Util.convStringToCalendar(d);
					dtInicial.add(Calendar.DATE, -1);
				}
				// Data inicial do período
				if(line.indexOf("<DTEND>")>=0)	{
					String d = line.substring(line.indexOf(">")+1).replace("</DTEND>", "");
					d = d.substring(6,8)+"/"+d.substring(4,6)+"/"+d.substring(0,4);
					dtFinal = Util.convStringToCalendar(d);
				}
//				 Saldo na primeira transação localizada no arquivo,
//				 total é calculado posteriormente, com base nas movimentações
				if(line.indexOf("<BALAMT>")>=0)
					vlSaldoFinal = Double.parseDouble(line.substring(line.indexOf(">")+1).replace(",", ".").replace("</BALAMT>", ""));
				/**
				 * Cada porção de linhas entre as tags STMTTRN representam movimentações.
				 * Aqui se inicia a coleta das informações contidas nesse bloco, para posterior tratamento.
				 * A cada ocorrência de <STMTTRN> um novo 'register' é criado para armazenar suas informações
				 * alimentando o rsm
				 */ 
				if(line.indexOf("<STMTTRN>")>=0)
					register = new HashMap<String,Object>();
				// Data do Movimento
				if(line.indexOf("<DTPOSTED>")>=0)	{
					String d = line.substring(line.indexOf(">")+1).replace("</DTPOSTED>", "");
					d = d.substring(6,8)+"/"+d.substring(4,6)+"/"+d.substring(0,4);
					register.put("DT_MOVIMENTO", Util.convStringToCalendar(d));
					register.put("DT_POSTED", Util.convStringToCalendar(d));
					String dtMovimento=d.substring(0,2)+d.substring(3,5)+d.substring(6,10);
					register.put("dtMovimento",dtMovimento);
					/*
					 * Verifica a ordem em que aparece a tranzação no extrato para efeito de geração do id_extrato de acordo com a data
					 * em que foi feita a operação
					 */
					if(ordemDaOperacaoNoDia==0){//verifica se a primeira vez que se esta procurando no extrato
						dtAuxilio=Util.convStringToCalendar(d);//guarda temporariamente a data que está sendo analisada
						ordemDaOperacaoNoDia++;
					}else{
						if(!(dtAuxilio.equals(Util.convStringToCalendar(d)))){//caso mude a data 
							ordemDaOperacaoNoDia=1;//reinicia-se o contador para o primeiro movimento do dia
							dtAuxilio=Util.convStringToCalendar(d);//guarda a nova data do dia atual que está sendo analisado
						}else{
							ordemDaOperacaoNoDia++;//caso a data não mude, apenas incrementa o contador da ordem dos movimentos
						}
					}
				}
				// Tipo de Movimento
				if(line.indexOf("<TRNTYPE>")>=0)
					register.put("TP_ORIGEM", line.substring(line.indexOf(">")+1).replace("</TRNTYPE>", ""));
				
				// Valor do Movimento
				if(line.indexOf("<TRNAMT>")>=0)	{
					vlMovimento = Double.parseDouble( line.substring(line.indexOf(">")+1).replace(",", ".").replace("</TRNAMT>", ""));
					register.put("VL_MOVIMENTO", new Double(line.substring(line.indexOf(">")+1).replace(",", ".").replace("</TRNAMT>", "")));
					register.put("TP_MOVIMENTO", vlMovimento>0?"0":"1");
					register.put("CL_TIPO", vlMovimento>0?"C":"D");
				}
				// Numero do Documento
				if(line.indexOf("<CHECKNUM>")>=0)
					register.put("NR_DOCUMENTO", line.substring(line.indexOf(">")+1).replace("</CHECKNUM>", ""));
				
				// Histórico
				if(line.indexOf("<MEMO>")>=0)
					register.put("DS_HISTORICO", line.substring(line.indexOf(">")+1).replace("</MEMO>", ""));
				/**
				 * Tag de fechamento da movimentação, 
				 * este bloco é responsavel por tratar as informações coletadas
				 * e adicionar ao ResultSetMap de retorno, para apresentação e conciliação pelo usuário
				 */
				if(line.indexOf("</STMTTRN>")>=0)	{
					String dsHistorico = (String)register.get("DS_HISTORICO");
					//gera um id movimento com o campo do log dtMovimento(<DTPOSTED>) + VL_MOVIMENTO(<TRNAMT>) + ordemDaOperacaoNoDia
					String idMovimento = (String)register.get("dtMovimento")+"_"+Math.abs(Float.parseFloat((register.get("VL_MOVIMENTO").toString())))+"_"+ordemDaOperacaoNoDia+"|";
					register.put("ID_MOVIMENTO",idMovimento);
					dsHistorico = dsHistorico!=null ? dsHistorico.toLowerCase() : "";
					int tpOrigem = register.get("TP_MOVIMENTO")==null || !Util.isWellFormattedIntegerValue(register.get("TP_MOVIMENTO").toString()) ?
							MovimentoContaServices.toCREDITO : Integer.parseInt((String)register.get("TP_MOVIMENTO"));
					if(vlMovimento<0 && dsHistorico.indexOf("cheque")>=0)
						tpOrigem = 8;
					else if(dsHistorico.indexOf("transferencia")>=0 || dsHistorico.indexOf("transferência")>=0)
						tpOrigem = 7;
					else if(dsHistorico.indexOf("tarifa")>=0 || dsHistorico.indexOf("taxa")>=0)
						tpOrigem = 4;
					else if(dsHistorico.indexOf("saque")>=0)
						tpOrigem = 10;
					else if(dsHistorico.indexOf("pagamento")>=0 || dsHistorico.indexOf("imposto")>=0 || dsHistorico.indexOf("inss")>=0||dsHistorico.indexOf("pagto")>=0)
						tpOrigem = 9;
					register.put("TP_ORIGEM", String.valueOf(tpOrigem));
					register.put("CL_ORIGEM", tipoMovimento[tpOrigem]);
					register.put("NR_BANCO", idBanco);
					register.put("NR_CONTA", nrConta);
					register.put("DT_INICIAL", dtInicial);
					register.put("DT_FINAL", dtFinal);
					if(connect.prepareStatement("SELECT * FROM adm_movimento_conta" +
							                    " WHERE id_extrato like \'%|"+idMovimento+"%\'" +
							                    "  AND cd_conta="+cdConta).executeQuery().next()){
						register.put("LG_CONCILIADO", "OK");   
					}else
						register.put("LG_CONCILIADO", "*");
					rsm.addRegister(register);
				}
			}
			rsm.last();
			if(rsm.size()>0){
				vlSaldoInicial = vlSaldoFinal;
				do {
					rsm.setValueToField("VL_SALDO", vlSaldoInicial);
					if( Integer.parseInt(rsm.getString("TP_MOVIMENTO")) == MovimentoContaServices.CREDITO ){
						vlSaldoInicial -= rsm.getDouble("VL_MOVIMENTO");
					}else{
						vlSaldoInicial += rsm.getDouble("VL_MOVIMENTO")*(-1);
						rsm.setValueToField("VL_MOVIMENTO", rsm.getDouble("VL_MOVIMENTO")*(-1)  );
					}
				}while(rsm.previous());
			}
			rsm.first();
			reader.close();
			Result result = new Result(1, "", "RSMEXTRATO", rsm);
			HashMap<String, Object> extrato = new HashMap<String, Object>();
			extrato.put("NR_CONTA", nrConta);
			extrato.put("ID_BANCO", idBanco);
			extrato.put("DT_INICIAL", dtInicial);
			extrato.put("DT_FINAL", dtFinal);
			extrato.put("VL_SALDO_INICIAL", vlSaldoInicial);
			extrato.put("VL_SALDO_FINAL", vlSaldoFinal);
			result.getObjects().put("EXTRATO", extrato);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getExtratoOfx: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result getResultSaldoAnterior(int cdEmpresa, int cdConta, int cdFormaPagamento, GregorianCalendar dtInicial,
		boolean includeMovNaoConfirmados, Connection connect ){
		Double saldoAnterior = getSaldoAnterior( cdEmpresa, cdConta, cdFormaPagamento, dtInicial, includeMovNaoConfirmados, connect);
		return new Result(1, "", "SALDOANTERIOR", saldoAnterior);
	}
	
	public static float getSaldoAnteriorPDV(int cdConta, int cdFormaPagamento, GregorianCalendar dtInicial) {
		double result = getSaldoAnterior(0, cdConta, cdFormaPagamento, dtInicial, false, null);
		return (float) result;
	}
	
	public static Double getSaldoAnterior(int cdConta, int cdFormaPagamento, GregorianCalendar dtInicial) {
		return getSaldoAnterior(0, cdConta, cdFormaPagamento, dtInicial, false, null);
	}

	public static Double getSaldoAnterior(int cdConta, GregorianCalendar dtInicial) {
		return getSaldoAnterior(0, cdConta, 0, dtInicial, false, null);
	}

	public static Double getSaldoAnterior(int cdConta, GregorianCalendar dtInicial, Connection connect) {
		return getSaldoAnterior(0, cdConta, 0, dtInicial, false, connect);
	}
	
	public static Double getSaldoAnterior(int cdEmpresa, int cdConta, int cdFormaPagamento, GregorianCalendar dtInicial,
			boolean includeMovNaoConfirmados, Connection connect) {
		boolean isConnectionNull = (connect == null);
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			Double vlSaldo     = 0.0d;
			
			ResultSetMap rsmContas = new ResultSetMap( connect.prepareStatement("SELECT cd_conta, vl_saldo_inicial, dt_saldo_inicial " +
                    "FROM adm_conta_financeira " +
						"WHERE 1=1 " +
						(cdConta<=0 ? "" : " AND cd_conta = "+cdConta)).executeQuery());
			
			rsmContas.beforeFirst();
			while ( rsmContas.next() ) {
				// Valor do saldo inicial
				if(cdFormaPagamento <= 0 || FormaPagamentoDAO.get(cdFormaPagamento, connect).getTpFormaPagamento()==FormaPagamentoServices.MOEDA_CORRENTE)	{
					vlSaldo += rsmContas.getDouble("vl_saldo_inicial");
				}
				
				// Movimentos
				PreparedStatement pstmt = connect.prepareStatement(
						   "SELECT tp_movimento, SUM(vl_movimento) AS vl_total " +
						   "FROM adm_movimento_conta A " +
				           (cdEmpresa>0 ? "JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta AND B.cd_empresa = "+cdEmpresa+")" : "" )+
				           "WHERE A.st_movimento IN ("+MovimentoContaServices.ST_COMPENSADO+","
				           							  +MovimentoContaServices.ST_CONCILIADO+","
				           							  +MovimentoContaServices.ST_LIQUIDADO+
				           (includeMovNaoConfirmados ? "," +MovimentoContaServices.ST_NAO_CONFERIDO : "") +")" +
				           "  AND A.cd_conta = "+rsmContas.getInt("CD_CONTA")+
				           (cdFormaPagamento>0 ? "  AND A.cd_forma_pagamento = "+cdFormaPagamento : "" )+
				           "  AND dt_movimento < ? "+
				           ((rsmContas.getTimestamp("DT_SALDO_INICIAL") != null )?"  AND dt_movimento > ? ":"")+
				           " GROUP BY tp_movimento");
				pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
				if(rsmContas.getTimestamp("DT_SALDO_INICIAL") != null )
					pstmt.setTimestamp(2, rsmContas.getTimestamp("DT_SALDO_INICIAL"));
				ResultSetMap rsmMovimentacoes = new ResultSetMap( pstmt.executeQuery());
				rsmMovimentacoes.beforeFirst();
				while(rsmMovimentacoes.next())
					if(rsmMovimentacoes.getInt("tp_movimento")==MovimentoContaServices.CREDITO)
						vlSaldo += rsmMovimentacoes.getDouble("vl_total");
					else
						vlSaldo -= rsmMovimentacoes.getDouble("vl_total");
				
			}
			
			return vlSaldo;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0.0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Float> getDiferencasConciliacao(int cdConta, GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			float vlInicialExtrato, float vlTotalCredito, float vlTotalDebito)	{
		return getDiferencasConciliacao(cdConta, dtInicial, dtFinal, vlInicialExtrato, vlTotalCredito, vlTotalDebito, null);
	}
	public static ArrayList<Float> getDiferencasConciliacao(int cdConta, GregorianCalendar dtInicial, GregorianCalendar dtFinal,
				float vlInicialExtrato, float vlTotalCredito, float vlTotalDebito, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			Double vlDiferencaInicial = getSaldoAnterior(cdConta, dtInicial, connect);
			ArrayList<Float> diferencas = new ArrayList<Float>();
			diferencas.add(new Float((double)Math.round((vlInicialExtrato-vlDiferencaInicial) * 100)/100));
			diferencas.add(new Float(vlTotalCredito));
			diferencas.add(new Float(vlTotalDebito));

			pstmt = connect.prepareStatement(
					   "SELECT tp_movimento, SUM(vl_movimento) AS vl_total " +
			           "FROM adm_movimento_conta  " +
			           "WHERE st_movimento IN ("+MovimentoContaServices.ST_COMPENSADO+","+MovimentoContaServices.ST_CONCILIADO+")" +
			           "  AND cd_conta = "+cdConta+
			           "  AND dt_movimento BETWEEN ? AND ? "+
			           " GROUP BY tp_movimento");
			pstmt.setTimestamp(1, new  Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new  Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
				if(rs.getInt("tp_movimento")==MovimentoContaServices.CREDITO)
					diferencas.set(1, new Float((double)Math.round((rs.getFloat("vl_total") - vlTotalCredito)*100)/100));
				else
					diferencas.set(2, new Float((double)Math.round((rs.getFloat("vl_total") - vlTotalDebito)*100)/100));
			return diferencas;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getDiferencasConciliacao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Método sobrescrito de {@link #find(ArrayList, int)} para remover o cd_usuario dos cirtérios passados
	 * 
	 * @param criterios para a consulta
	 * @author Luiz Romario Filho
	 * @since 28/08/2014
	 * @return map com os resultados encontrados
	 */
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		int cdUsuario = 0;
		
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for(int i=0; i<criterios.size(); i++)	{
			if(criterios.get(i).getColumn().equalsIgnoreCase("cd_usuario"))	{
				cdUsuario = Integer.parseInt(criterios.get(i).getValue());
			}
			else
				crt.add(criterios.get(i));
		}
		
		return find(crt, cdUsuario, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, int cdUsuario) {
		return find(criterios, cdUsuario, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, int cdUsuario, Connection connect) {
		String nmConta = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_CONTA")) {
				nmConta =	Util.limparTexto(criterios.get(i).getValue());
				nmConta = nmConta.trim();
				criterios.remove(i);
				i--;
			}
		}
		try {
			if (criterios==null)
				criterios = new ArrayList<sol.dao.ItemComparator>();
			return Search.find("SELECT A.*, B.nr_agencia, C.*, D.nm_pessoa AS nm_responsavel, nm_turno,"+
							   " D2.nm_pessoa as nm_empresa, "+
							   " (Select MAX(dt_fechamento) "+
							   " 	FROM adm_conta_fechamento WHERE cd_conta = a.cd_conta) as FECHAMENTO " +
					           "FROM adm_conta_financeira A " +
					           "LEFT OUTER JOIN grl_agencia B  ON (A.cd_agencia = B.cd_agencia) " +
					           "LEFT OUTER JOIN grl_banco   C  ON (B.cd_banco = C.cd_banco) " +
					           "LEFT OUTER JOIN grl_pessoa  D  ON (A.cd_responsavel = D.cd_pessoa) " +
					           "LEFT OUTER JOIN grl_pessoa  D2 ON (A.cd_empresa = D2.cd_pessoa) " +
					           "LEFT OUTER JOIN adm_turno   E  ON (A.cd_turno       = E.cd_turno) " +
					          (cdUsuario>0 ?
					        		  "WHERE EXISTS (SELECT * FROM seg_usuario_conta_financeira S " +
					        		  "              WHERE S.cd_conta   = A.cd_conta " +
					        		  "                AND S.cd_usuario = "+cdUsuario+")":"WHERE 1=1 ") +
					          (!nmConta.equals("") ?
										" AND TRANSLATE (a.nm_conta, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', "+
										"					'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmConta)+"%' "
										: ""),
					          " ORDER BY A.nm_conta", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		catch (Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new ResultSetMap();
		}
	}

	public static ResultSetMap findWithSaldo(ArrayList<sol.dao.ItemComparator> criterios, int cdUsuario) {
		ResultSetMap rsm = find(criterios, cdUsuario);
		Connection connect = Conexao.conectar();
		try	{
			while(rsm.next())	{
				rsm.setValueToField("VL_SALDO", getSaldoAnterior(rsm.getInt("cd_conta"), new GregorianCalendar()));
				ResultSetMap rsmSaldoEmTitulo = TituloCreditoServices.getSaldoEmTitulo(rsm.getInt("cd_conta"), new GregorianCalendar(), connect);
				String dsSaldo = "";
				while(rsmSaldoEmTitulo.next())	{
					String idDoc = rsmSaldoEmTitulo.getString("sg_tipo_documento")!=null ? rsmSaldoEmTitulo.getString("sg_tipo_documento") : rsmSaldoEmTitulo.getString("nm_tipo_documento");
					dsSaldo += (dsSaldo.equals("") ? "" : ", ")+idDoc+": "+Util.formatNumber(rsmSaldoEmTitulo.getFloat("vl_saldo"));
				}
				rsm.setValueToField("DS_SALDO", dsSaldo);
			}
			rsm.beforeFirst();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		return rsm;
	}
	
	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, null);
	}
	
	public static ResultSetMap getAll(int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, " +
								   "	   B.nm_conta, B.nr_conta, B.nr_dv, B.vl_limite, " +
								   "	   C.nr_agencia, D.nr_banco, D.nm_banco, D.id_banco " +
						           "FROM adm_conta_carteira A " +
						           "JOIN adm_conta_financeira   B ON (A.cd_conta = B.cd_conta AND B.cd_empresa = "+cdEmpresa+") " +
						           "LEFT OUTER JOIN grl_agencia C ON (B.cd_agencia = C.cd_agencia) " +
						           "LEFT OUTER JOIN grl_banco   D ON (C.cd_banco = D.cd_banco)");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm != null && rsm.next()) {
				rsm.getRegister().put("CD_CONTA_NM_CONTA", rsm.getInt("cd_conta") + "-" + rsm.getString("nm_conta"));
				rsm.getRegister().put("NM_CONTA_CONTA", rsm.getString("nr_conta") + "-" + rsm.getString("nm_conta") + ((rsm.getString("nr_agencia") != null) ? " - " + rsm.getString("nr_agencia") : "") + ((rsm.getString("nm_banco") != null) ? " - " + rsm.getString("nm_banco") : ""));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getUsuarioOfConta(int cdConta) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.*, C.nm_pessoa " +
													         "FROM seg_usuario_conta_financeira A "  +
													         "JOIN seg_usuario B ON (A.cd_usuario = B.cd_usuario) "+
															 "LEFT OUTER JOIN grl_pessoa C ON (B.CD_PESSOA=C.CD_PESSOA) " +
															 "WHERE A.cd_conta = "+cdConta).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * @see #addUsuarioToConta(int, int, Connection)
	 * 
	 */
	public static Result addUsuarioToConta(int cdUsuario, int cdConta) {
		return addUsuarioToConta(cdUsuario, cdConta, null);
	}
	
	/**
	 * Método que adiciona usuário a uma conta
	 * 
	 * @param cdUsuario chave primária de {@link Usuario}
	 * @param cdConta chave primária de {@link Conta}
	 * @return result com código e mensagem da transação, code >= 0 ? sucesso : erro 
	 */
	private static Result addUsuarioToConta(int cdUsuario, int cdConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();

			/*
			 * Verifica se o usuário já foi adicionado à conta
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM seg_usuario_conta_financeira " +
					                                "WHERE cd_usuario = "+cdUsuario+" AND cd_conta = "+cdConta).executeQuery();
			/*
			 * Inclui o usuário
			 */
			if(!rs.next())	{
				return new Result(connect.prepareStatement("INSERT INTO seg_usuario_conta_financeira (cd_usuario,cd_conta) " +
                                                           "VALUES ("+cdUsuario+" ,"+cdConta+")").executeUpdate(), "Usuário incluÃ¯Â¿Â½do com sucesso!");
			}
			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o usuário na conta!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int insertUsuarioConta(int cdUsuario, int cdConta) {
		return insertUsuarioConta(cdUsuario, cdConta, null);
	}

	public static int insertUsuarioConta(int cdUsuario, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_usuario_conta_financeira (cd_usuario,"+
			                                  "cd_conta) VALUES (?, ?)");
			if(cdUsuario==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1, cdUsuario);
			if(cdConta==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, cdConta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.insertUsuarioConta: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @see #removeUsuarioToConta(int, int, Connection)
	 * 
	 */
	public static Result removeUsuarioToConta(int cdUsuario, int cdConta) {
		return removeUsuarioToConta(cdUsuario, cdConta, null);
	}

	/**
	 * Método para remover usuário de uma conta financeira
	 * 
	 * @author Luiz Romario Filho
	 * @since 28/08/2014
	 * @param cdUsuario chave primária do usuário
	 * @param cdConta chave primária da conta financeira
	 * @param connect conexão caso precise executar transação
	 */
	private static Result removeUsuarioToConta(int cdUsuario, int cdConta, Connection connect) {
		int code = deleteUsuarioConta(cdUsuario, cdConta);
		return code > 0 ? new Result(code, "Usuário removido com sucesso!") : new Result(code, "Erro ao remover usuário!") ;
	}

	public static int deleteUsuarioConta(int cdUsuario, int cdConta) {
		return deleteUsuarioConta(cdUsuario, cdConta, null);
	}

	public static int deleteUsuarioConta(int cdUsuario, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_usuario_conta_financeira " +
					                                           "WHERE cd_usuario = ? "+
			                                                   "  AND cd_conta = ?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdConta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.deleteUsuarioConta: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Banco getBancoOfConta(int cdConta) {
		return getBancoOfConta(cdConta, null);
	}

	public static Banco getBancoOfConta(int cdConta, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			ArrayList<sol.dao.ItemComparator> criterios = new ArrayList<sol.dao.ItemComparator>();
			criterios.add(new ItemComparator("A.cd_conta", Integer.toString(cdConta), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, -1, connection);
			return rsm.next() ? BancoServices.getFromRsm(rsm) : null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getBancoOfConta: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * @author Alvaro
	 * @param cdUsuario
	 * @param cdEmpresa
	 * @param dtInicial
	 * @param dtFinal
	 * @param connect
	 * @return Obtêm o saldo da conta e os valores a receber e a pagar( movimentações não compensadas ),
	 * @since 19/03/2015  
	 */
	public static ResultSetMap getResumoContasOfUsuario( int cdUsuario, int cdEmpresa, GregorianCalendar dtInicial ){
		Connection connect = Conexao.conectar();
		try {
			ResultSetMap rsmContasUsuario = getContasOfUsuario(cdUsuario, cdEmpresa);
			rsmContasUsuario.beforeFirst();
			while( rsmContasUsuario.next() ){
				
				Double vlSaldo = getSaldoAnterior(cdEmpresa, rsmContasUsuario.getInt("CD_CONTA"), -1, dtInicial, false, connect);
				Double vlApagar = 0.0d;
				Double vlAreceber = 0.0d;
				
				//A Pagar
				PreparedStatement pstmtApagar = connect.prepareStatement(
						"  SELECT  ( vl_conta-vl_abatimento+vl_acrescimo ) AS vl_conta,  "+
					    "   ( vl_pago+vl_multa+vl_juros-vl_desconto ) AS vl_pago         "+
					    "   FROM (                                                       "+
					    "   	SELECT A.vl_conta, A.vl_abatimento, A.vl_acrescimo,      "+
					    "           SUM(B.vl_pago) AS vl_pago,                           "+
					    "   		SUM(B.vl_multa) AS vl_multa,                         "+
					    "           SUM(B.vl_juros) AS vl_juros,                         "+
					    "           SUM(B.vl_desconto) AS vl_desconto                    "+
					    "   	FROM adm_conta_pagar A                                   "+
					    "   	LEFT JOIN adm_movimento_conta_pagar B                    "+
					    "                 ON ( A.cd_conta_pagar = B.cd_conta_pagar       "+
					    "                 AND A.cd_conta = B.cd_conta )                  "+
					    "   	WHERE A.st_conta = ?                                     "+
					    "   	AND   A.cd_conta = ?                                     "+
					    "   	AND   A.cd_empresa = ?                                   "+
					    "   	GROUP BY A.cd_conta_pagar, A.vl_conta, A.vl_abatimento, A.vl_acrescimo, "+
					    "   	         B.cd_conta_pagar, B.cd_conta, B.cd_movimento_conta, "+
					    "                B.vl_pago, B.vl_multa, B.vl_juros, B.vl_desconto "+
					    " ) AS sub");
				pstmtApagar.setInt(1, ContaPagarServices.ST_EM_ABERTO );
				pstmtApagar.setInt(2, rsmContasUsuario.getInt("CD_CONTA"));
				pstmtApagar.setInt(3, cdEmpresa);
				ResultSetMap rsmApagar = new ResultSetMap(pstmtApagar.executeQuery());
				rsmApagar.beforeFirst();
				while( rsmApagar.next() )
					vlApagar += (  rsmApagar.getDouble("VL_CONTA")-rsmApagar.getDouble("VL_PAGO") );
				
				//Movimentos de conta financeira DÉBITO
				PreparedStatement pstmtMovApagar = connect.prepareStatement(
						" SELECT SUM(B.vl_movimento) AS vl_movimento                "+
						" FROM adm_conta_financeira A								"+
						" JOIN adm_movimento_conta B on ( A.cd_conta = B.cd_conta ) "+
						" WHERE A.cd_empresa = ?									"+
						" AND A.cd_conta = ?										"+
						" AND B.st_movimento = ?									"+
						" AND B.tp_movimento = ?									");
				pstmtMovApagar.setInt(1, cdEmpresa);
				pstmtMovApagar.setInt(2, rsmContasUsuario.getInt("CD_CONTA"));
				pstmtMovApagar.setInt(3, MovimentoContaServices.ST_NAO_COMPENSADO);
				pstmtMovApagar.setInt(4, MovimentoContaServices.DEBITO);
				rsmApagar = new ResultSetMap(pstmtApagar.executeQuery());
				rsmApagar.beforeFirst();
				while( rsmApagar.next() )
					vlApagar += rsmApagar.getDouble("VL_MOVIMENTO");
				/************************************************************************************************************/
				
				
				//A Receber
				PreparedStatement pstmtAreceber = connect.prepareStatement(
						"  SELECT  ( vl_conta-vl_abatimento+vl_acrescimo ) AS vl_conta,  "+
					    "  ( vl_recebido+vl_multa+vl_juros-vl_desconto ) AS vl_pago     "+
					    "  FROM (                                                       "+
					    "   	SELECT A.vl_conta, A.vl_abatimento, A.vl_acrescimo,      "+
					    "           SUM(B.vl_recebido) AS vl_recebido,                   "+
					    "   		SUM(B.vl_multa) AS vl_multa,                         "+
					    "           SUM(B.vl_juros) AS vl_juros,                         "+
					    "           SUM(B.vl_desconto) AS vl_desconto                    "+
					    "   	FROM adm_conta_receber A                                 "+
					    "   	LEFT JOIN adm_movimento_conta_receber B                  "+
					    "                 ON ( A.cd_conta_receber = B.cd_conta_receber   "+
					    "                 AND A.cd_conta = B.cd_conta )                  "+
					    "   	WHERE A.st_conta = ?                                     "+
					    "   	AND   A.cd_conta = ?                                     "+
					    "   	AND   A.cd_empresa = ?                                   "+
					    "   	GROUP BY A.cd_conta_receber, A.vl_conta, A.vl_abatimento, A.vl_acrescimo, "+
					    "   	         B.cd_conta_receber, B.cd_conta, B.cd_movimento_conta, "+
					    "                B.vl_recebido, B.vl_multa, B.vl_juros, B.vl_desconto "+
					    "    ) AS sub");
				pstmtAreceber.setInt(1, ContaPagarServices.ST_EM_ABERTO );
				pstmtAreceber.setInt(2, rsmContasUsuario.getInt("CD_CONTA"));
				pstmtAreceber.setInt(3, cdEmpresa);
				ResultSetMap rsmAreceber = new ResultSetMap(pstmtAreceber.executeQuery());
				rsmAreceber.beforeFirst();
				while( rsmAreceber.next() )
					vlAreceber += (  rsmAreceber.getDouble("VL_CONTA")-rsmAreceber.getDouble("VL_PAGO") );
				
				//Movimentos de conta financeira CRÉDITO
				PreparedStatement pstmtMovAreceber = connect.prepareStatement(
						" SELECT SUM(B.vl_movimento) AS vl_movimento                "+
						" FROM adm_conta_financeira A								"+
						" JOIN adm_movimento_conta B on ( A.cd_conta = B.cd_conta ) "+
						" WHERE A.cd_empresa = ?									"+
						" AND A.cd_conta = ?										"+
						" AND B.st_movimento = ?									"+
						" AND B.tp_movimento = ?									");
				pstmtMovAreceber.setInt(1, cdEmpresa);
				pstmtMovAreceber.setInt(2, rsmContasUsuario.getInt("CD_CONTA"));
				pstmtMovAreceber.setInt(3, MovimentoContaServices.ST_NAO_COMPENSADO);
				pstmtMovAreceber.setInt(4, MovimentoContaServices.CREDITO);
				rsmAreceber = new ResultSetMap(pstmtMovAreceber.executeQuery());
				rsmAreceber.beforeFirst();
				while( rsmAreceber.next() )
					vlAreceber += rsmAreceber.getDouble("VL_MOVIMENTO");
				/************************************************************************************************************/
				
				rsmContasUsuario.getRegister().put("VL_SALDO", vlSaldo);
				rsmContasUsuario.getRegister().put("VL_APAGAR", vlApagar);
				rsmContasUsuario.getRegister().put("VL_ARECEBER", vlAreceber);
				rsmContasUsuario.getRegister().put("VL_SALDO_PREVISTO", vlSaldo+vlAreceber-vlApagar);
			}
			return rsmContasUsuario;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getResumoContasOfUsuario: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getContasOfUsuario(int cdUsuario, int cdEmpresa) {
		return getContasOfUsuario(cdUsuario, cdEmpresa, -1, null);
	}

	public static ResultSetMap getContasOfUsuario(int cdUsuario, int cdEmpresa, int tpConta) {
		return getContasOfUsuario(cdUsuario, cdEmpresa, tpConta, null);
	}

	public static ResultSetMap getContasOfUsuario(int cdUsuario, int cdEmpresa, int tpConta, Connection connect) {
		return getContasOfUsuario(cdUsuario, cdEmpresa, tpConta, false, null, null);
	}

	public static ResultSetMap getContasOfUsuario(int cdUsuario, int cdEmpresa, int tpConta, boolean lgSaldo, GregorianCalendar dtInicial, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nr_agencia, C.*, D.nm_turno, E.nm_pessoa " +
					"FROM adm_conta_financeira A " +
					"LEFT OUTER JOIN grl_agencia B ON (A.cd_agencia = B.cd_agencia) " +
					"LEFT OUTER JOIN grl_banco   C ON (B.cd_banco = C.cd_banco) " +
					"LEFT OUTER JOIN adm_turno   D ON (D.cd_turno = A.cd_turno) " +
					"LEFT OUTER JOIN grl_pessoa  E ON (A.cd_empresa = E.cd_pessoa) " +
	                "WHERE A.cd_empresa = "+cdEmpresa+
	                "  AND EXISTS (SELECT * FROM seg_usuario_conta_financeira S" +
	                "              WHERE S.cd_conta = A.cd_conta " +
	                "                AND S.cd_usuario = "+cdUsuario+") " +
	                (tpConta!=-1 ? "  AND A.tp_conta = " + tpConta : "")+
	                " ORDER BY A.nm_conta, B.nr_agencia, A.nr_conta");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_TP_OPERACAO",  getLabelTipoOperacao(rsm.getInt("TP_OPERACAO")) );
				if(lgSaldo){
					rsm.setValueToField("VL_SALDO",getSaldoAnterior(rsm.getInt("CD_CONTA"), dtInicial));
				}
				
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getContasOfUsuario: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getContas(int cdEmpresa) {
		return getContas(cdEmpresa, false, null, null);
	}
	
	public static ResultSetMap getContas(int cdEmpresa, boolean lgSaldo, GregorianCalendar dtInicial) {
		return getContas(cdEmpresa, lgSaldo, dtInicial, null);
	}

	public static ResultSetMap getContas(int cdEmpresa, boolean lgSaldo, GregorianCalendar dtInicial, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT A.*, B.nr_agencia, C.*, D.nm_turno " +
																"FROM adm_conta_financeira A " +
																"LEFT OUTER JOIN grl_agencia B ON (B.cd_agencia = A.cd_agencia) " +
																"LEFT OUTER JOIN grl_banco   C ON (C.cd_banco   = B.cd_banco) " +
																"LEFT OUTER JOIN adm_turno   D ON (D.cd_turno   = A.cd_turno) " +
																"WHERE A.cd_empresa = " +cdEmpresa+
																"ORDER BY nm_conta").executeQuery());
			if(lgSaldo){
				while (rsm.next()) {
					rsm.setValueToField("VL_SALDO",getSaldoAnterior(rsm.getInt("CD_CONTA"), dtInicial));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getContas: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getMovimentosNotFechadosByFormaPag(int cdConta) {
		return getMovimentosNotFechadosByFormaPag(cdConta, null, null);
	}

	public static ResultSetMap getMovimentosNotFechadosByFormaPag(int cdConta, GregorianCalendar dtPosicao) {
		return getMovimentosNotFechadosByFormaPag(cdConta, dtPosicao, null);
	}

	public static ResultSetMap getMovimentosNotFechadosByFormaPag(int cdConta, GregorianCalendar dtPosicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			dtPosicao = dtPosicao==null ? new GregorianCalendar() : dtPosicao;
			dtPosicao.set(Calendar.HOUR_OF_DAY, 23);
			dtPosicao.set(Calendar.MINUTE, 59);
			dtPosicao.set(Calendar.SECOND, 59);
			dtPosicao.set(Calendar.MILLISECOND, 59);

			PreparedStatement pstmt = connect.prepareStatement(
										"SELECT A.cd_forma_pagamento, B.nm_forma_pagamento, CAST(0.00 AS FLOAT) AS vl_saldo_ant, " +
										"       (SELECT COUNT(*) FROM adm_movimento_conta A2" +
										"        WHERE A2.cd_conta = "+cdConta+
										"          AND A2.st_movimento IN (0,1) " +
										"          AND A2.tp_movimento = " +MovimentoContaServices.CREDITO+
										"		   AND A2.dt_movimento <= ? " +
										"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
										"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS qt_credito, " +
										"       (SELECT SUM(vl_movimento) FROM adm_movimento_conta A2" +
										"        WHERE A2.cd_conta = "+cdConta+
										"          AND A2.st_movimento IN (0,1) " +
										"		   AND A2.dt_movimento <= ? " +
										"          AND A2.tp_movimento = " +MovimentoContaServices.CREDITO+
										"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
										"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS vl_credito, " +
										"       (SELECT COUNT(*) FROM adm_movimento_conta A2" +
										"        WHERE A2.cd_conta = "+cdConta+
										"          AND A2.st_movimento IN (0,1) " +
										"		   AND A2.dt_movimento <= ? " +
										"          AND A2.tp_movimento = " +MovimentoContaServices.DEBITO+
										"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
										"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS qt_debito, " +
										"       (SELECT SUM(vl_movimento) FROM adm_movimento_conta A2" +
										"        WHERE A2.cd_conta = "+cdConta+
										"          AND A2.st_movimento IN (0,1) " +
										"		   AND A2.dt_movimento <= ? " +
										"          AND A2.tp_movimento = " +MovimentoContaServices.DEBITO+
										"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
										"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS vl_debito " +
										"FROM adm_movimento_conta A " +
										"LEFT OUTER JOIN adm_forma_pagamento B ON (A.cd_forma_pagamento = B.cd_forma_pagamento) " +
										"WHERE A.cd_conta = "+cdConta+
										"  AND A.st_movimento IN (0,1) " +
										"GROUP BY A.cd_forma_pagamento, B.nm_forma_pagamento");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtPosicao));
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtPosicao));
			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtPosicao));
			pstmt.setTimestamp(4, Util.convCalendarToTimestamp(dtPosicao));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			pstmt = connect.prepareStatement("SELECT A.tp_movimento, A.cd_forma_pagamento, B.nm_forma_pagamento, SUM(A.vl_movimento) AS vl_total " +
					"FROM adm_movimento_conta A, adm_forma_pagamento B " +
					"WHERE A.cd_forma_pagamento = B.cd_forma_pagamento " +
					"  AND NOT A.cd_fechamento IS NULL " +
					"  AND A.cd_conta = ? " +
					"  AND CAST(A.dt_movimento AS DATE) <= (SELECT MAX(dt_fechamento) " +
					"						  FROM adm_conta_fechamento " +
					"						  WHERE cd_conta = ? " +
					"							AND dt_fechamento < ?)" +
					"GROUP BY A.tp_movimento, A.cd_forma_pagamento, B.nm_forma_pagamento");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdConta);
			dtPosicao.set(Calendar.HOUR_OF_DAY, 0);
			dtPosicao.set(Calendar.MINUTE, 0);
			dtPosicao.set(Calendar.SECOND, 0);
			dtPosicao.set(Calendar.MILLISECOND, 0);
			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtPosicao));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				boolean locate = rsm.locate("cd_forma_pagamento", rs.getInt("cd_forma_pagamento"));
				HashMap<String, Object> register = !locate ? new HashMap<String, Object>() : rsm.getRegister();
				if (!locate) {
					register.put("CD_FORMA_PAGAMENTO", rs.getInt("cd_forma_pagamento"));
					register.put("NM_FORMA_PAGAMENTO", rs.getString("nm_forma_pagamento"));
					register.put("VL_CREDITO", 0.00);
					register.put("QT_CREDITO", 0);
					register.put("VL_DEBITO", 0.00);
					register.put("QT_DEBITO", 0);
					register.put("VL_SALDO_ANT", 0.00);
					rsm.addRegister(register);
				}
				register.put("VL_SALDO_ANT", ((Double)register.get("VL_SALDO_ANT")).doubleValue() +
						(rs.getInt("tp_movimento")==MovimentoContaServices.CREDITO ? 1 : -1) * rs.getFloat("vl_total"));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getMovimentosNotFechadosByFormaPag: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTitulos(int cdConta) {
		return getTitulos(cdConta, null);
	}

	public static ResultSetMap getTitulos(int cdConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
										"SELECT A.cd_forma_pagamento, B.nm_forma_pagamento, " +
										"       (SELECT COUNT(*) FROM adm_movimento_conta A2" +
										"        WHERE A2.cd_conta = "+cdConta+
										"          AND A2.st_movimento IN (0,1) " +
										"          AND A2.tp_movimento = " +MovimentoContaServices.CREDITO+
										"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
										"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS qt_credito, " +
										"       (SELECT SUM(vl_movimento) FROM adm_movimento_conta A2" +
										"        WHERE A2.cd_conta = "+cdConta+
										"          AND A2.st_movimento IN (0,1) " +
										"          AND A2.tp_movimento = " +MovimentoContaServices.CREDITO+
										"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
										"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS vl_credito, " +
										"       (SELECT COUNT(*) FROM adm_movimento_conta A2" +
										"        WHERE A2.cd_conta = "+cdConta+
										"          AND A2.st_movimento IN (0,1) " +
										"          AND A2.tp_movimento = " +MovimentoContaServices.DEBITO+
										"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
										"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS qt_debito, " +
										"       (SELECT SUM(vl_movimento) FROM adm_movimento_conta A2" +
										"        WHERE A2.cd_conta = "+cdConta+
										"          AND A2.st_movimento IN (0,1) " +
										"          AND A2.tp_movimento = " +MovimentoContaServices.DEBITO+
										"          AND (A2.cd_forma_pagamento = A.cd_forma_pagamento" +
										"           OR (A2.cd_forma_pagamento IS NULL AND A.cd_forma_pagamento IS NULL))) AS vl_debito " +
										"FROM adm_movimento_conta A " +
										"LEFT OUTER JOIN adm_forma_pagamento B ON (A.cd_forma_pagamento = B.cd_forma_pagamento) " +
										"WHERE A.cd_conta = "+cdConta+
										"  AND A.st_movimento IN (0,1) " +
										"GROUP BY A.cd_forma_pagamento, B.nm_forma_pagamento").executeQuery());
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getTitulos: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setFechamentoCaixa(int cdConta, int cdContaDestino, int cdFormaPagamento, int cdUsuario, float vlTransferencia, ArrayList<Integer> titulosCredito) {
		return setFechamentoCaixa(cdConta, cdContaDestino, cdFormaPagamento, cdUsuario, vlTransferencia, titulosCredito, null);
	}

	public static int setFechamentoCaixa(int cdConta, int cdContaDestino, int cdFormaPagamento, int cdUsuario, float vlTransferencia,
			                             ArrayList<Integer> titulosCredito, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			if(vlTransferencia <= 0)	{
				new Exception("setFechamentoCaixa: valor da transferï¿½ncia não informado!");
				return -1;
			}
			if(cdConta == cdContaDestino)	{
				new Exception("setFechamentoCaixa: conta de destino e de origem iguais!");
				return -1;
			}

			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			// Realizando Transferência
			MovimentoConta mov = new MovimentoConta(0 /*cdMovimentoConta*/, cdConta, 0/*cdContaOrigem*/, 0/*cdMovimentoOrigem*/, cdUsuario, 0/*cdCheque*/,
													0/*cdViagem*/, new GregorianCalendar(), vlTransferencia, "FC" /*nrDocumento*/, MovimentoContaServices.DEBITO,
													MovimentoContaServices.toTRANSFERENCIA, MovimentoContaServices.ST_COMPENSADO, "Fechamento de Caixa" /*dsHistorico*/,
													null /*dtDeposito*/, null /*idExtrato*/, cdFormaPagamento, 0 /*cdFechamento*/, 0/*cdTurno*/);
			ArrayList<MovimentoContaTituloCredito> titulosTemp = new ArrayList<MovimentoContaTituloCredito>();
			for (int i=0; titulosCredito!=null && i<titulosCredito.size(); i++)
				titulosTemp.add(new MovimentoContaTituloCredito(titulosCredito.get(i), 0 /*cdMovimentoConta*/, 0 /*cdConta*/));
			
			int ret = MovimentoContaServices.insertTransfer(mov, cdContaDestino, titulosTemp, connect).getCode();

			// Atualizando o STATUS do movimento da contas
			if(ret>0)	{
				ret = connect.prepareStatement("UPDATE adm_movimento_conta SET st_movimento = "+MovimentoContaServices.ST_CX_FECHADO+
								               " WHERE cd_conta = "+cdConta+
								               "   AND cd_forma_pagamento "+(cdFormaPagamento==0?" IS NULL ":" = "+cdFormaPagamento)+
								               "   AND st_movimento IN ("+MovimentoContaServices.ST_CONFERIDO+","+MovimentoContaServices.ST_NAO_CONFERIDO+")").executeUpdate();
			}

			if(ret < 0 && isConnectionNull)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return ret;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getDadosFechamentoCaixa: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static boolean hasPermissao(int cdConta, int cdUsuario) {
		return hasPermissao(cdConta, cdUsuario, null);
	}
	
	public static boolean hasPermissao(int cdConta, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			// Verifica permissão de acesso à conta
			ResultSet rsUsuario = connect.prepareStatement("SELECT * " +
					                                       "FROM seg_usuario_conta_financeira A " +
					                                       "JOIN seg_usuario B ON (A.cd_usuario = B.cd_usuario) " +
  		                                                   "WHERE A.cd_conta   = "+cdConta+
		                                                   "  AND A.cd_usuario = "+cdUsuario).executeQuery();
			return rsUsuario.next();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return false;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result transferenciaCaixa(int cdConta, int cdResponsavelAtual, int cdNovoUsuario) {
		return transferenciaCaixa(cdConta, cdResponsavelAtual, cdNovoUsuario, null);
	}

	public static Result transferenciaCaixa(int cdConta, int cdResponsavelAtual, int cdNovoUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
//			connect = isConnectionNull ? Conexao.conectar() : connect;
			// Verifica se o responsável foi passado
			if(cdNovoUsuario <= 0 || cdConta<= 0)
				return new Result(-1, "O responsável pela abertura do caixa ou a conta a ser aberta não foi[ram] informado(s). [cdResponsavel="+cdNovoUsuario+",cdConta="+cdConta+"]");
			// Verifica se já não foi aberta por outro usuário
			ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
			if(conta.getCdResponsavel()>0 && conta.getCdResponsavel()!=cdResponsavelAtual)	{
				Pessoa pessoa = PessoaDAO.get(conta.getCdResponsavel(), connect);
				new Result(-1, "O responsável atual não foi informado corretamente [Aberta por: "+pessoa.getNmPessoa()+"]");
			}
			//
			com.tivic.manager.seg.Usuario usuario = com.tivic.manager.seg.UsuarioDAO.get(cdNovoUsuario, connect);
			if(usuario==null || usuario.getCdPessoa()<=0)
				return new Result(-1, "Novo usuario nao localizado ou usuario informacoes pessoais (Nome)!");
			// Verifica permissão de acesso à conta
			ResultSet rsUsuario = connect.prepareStatement("SELECT * " +
					                                       "FROM seg_usuario_conta_financeira A " +
					                                       "JOIN seg_usuario B ON (A.cd_usuario = B.cd_usuario) " +
  		                                                   "WHERE A.cd_conta   = "+cdConta+
		                                                   "  AND A.cd_usuario = "+cdNovoUsuario).executeQuery();
			if(!rsUsuario.next())
				return new Result(-1, "Novo usuario não tem permissão de acesso a essa conta!");
			// Atualizando responsável da conta
			connect.prepareStatement("UPDATE adm_conta_financeira SET cd_responsavel = " +usuario.getCdPessoa()+
				                     " WHERE cd_conta = "+cdConta).executeUpdate();

//			if (isConnectionNull)
//				connect.commit();

			return new Result(1);
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar realizar transferência de caixa!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result abrirConta(int cdConta, int cdResponsavel, int cdTurno, float vlFundoCaixa, int cdContaOrigem) {
		return abrirConta(cdConta, cdResponsavel, cdTurno, vlFundoCaixa, cdContaOrigem, null);
	}

	public static Result abrirConta(int cdConta, int cdResponsavel, int cdTurno, float vlFundoCaixa, int cdContaOrigem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			// Verifica se o responsável foi passado
			if(cdResponsavel <= 0 || cdConta<= 0)
				return new Result(-1, "O responsável pela abertura do caixa ou a conta a ser aberta não foi[ram] informado(s). [cdResponsavel="+cdResponsavel+",cdConta="+cdConta+"]");
			// Verifica se já não foi aberta por outro usuário
			ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
			if(conta.getCdResponsavel()>0 && conta.getCdResponsavel()!=cdResponsavel)	{
				Pessoa pessoa = PessoaDAO.get(conta.getCdResponsavel(), connect);
				new Result(-1, "A conta informada já foi aberta por outro usuário! [Aberta por: "+pessoa.getNmPessoa()+"]");
			}
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_conta_financeira " +
					                                "WHERE cd_conta = "+cdConta).executeQuery();
			if(!rs.next())
				return new Result(-1, "Conta não localizada!");
			

			if(rs.getInt("cd_responsavel") > 0) 
				return new Result(-1, "Conta já aberta pra outro usuário!");
			// Verifica se já não existe um fechamento para o dia e turno informado
			GregorianCalendar dtFechamento = new GregorianCalendar();
			dtFechamento.set(Calendar.HOUR_OF_DAY, 0);
			dtFechamento.set(Calendar.MINUTE, 0);
			dtFechamento.set(Calendar.SECOND, 0);
			dtFechamento.set(Calendar.MILLISECOND, 0);
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_fechamento FROM adm_conta_fechamento " +
								                               "WHERE cd_conta      = "+cdConta+
					                                           "  AND cd_turno      = "+cdTurno+
					                                           "  AND dt_fechamento = ?");
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			if(pstmt.executeQuery().next())
				return new Result(-1, "Ja existe um fechamento para a conta e o turno informados!");
			// Atualizando responsável da conta
			connect.prepareStatement("UPDATE adm_conta_financeira SET cd_responsavel = " +cdResponsavel+", cd_turno = "+(cdTurno>0 ? String.valueOf(cdTurno) : " NULL ")+
				                     " WHERE cd_conta = "+cdConta+
				                     "   AND cd_responsavel IS NULL").executeUpdate();

			// Verificando 
			/*
			 * MUDANÇA DE TANQUE NO BICO
			 */
			//
			GregorianCalendar dtAbertura = new GregorianCalendar();
			dtAbertura.set(Calendar.HOUR, 0);
			dtAbertura.set(Calendar.MINUTE, 0);
			dtAbertura.set(Calendar.SECOND, 0);
			dtAbertura.set(Calendar.MILLISECOND, 0);
			BicoHistoricoServices.verificarTrocaTanque(0/*Todos os Bicos*/, dtAbertura, 0, connect);
			//
			if(vlFundoCaixa > 0)	{
				// Lançamento de Origem
				MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/, cdContaOrigem /*cdConta*/, 0 /*cdContaOrigem*/,
						                                      0 /*cdMovimentoOrigem*/, cdResponsavel, 0/*cdCheque*/, 0/*cdViagem*/,
						                                      new GregorianCalendar() /*dtMovimento*/, vlFundoCaixa, "",
						                                      MovimentoContaServices.DEBITO, 0 /*tpOrigem*/,
						                                      MovimentoContaServices.ST_COMPENSADO, "Transferência para fundo de caixa",
						                                      null /*dtDeposito*/, null /*idExtrato*/,
						                                      ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_DINHEIRO", 0, 0, connect),
						                                      0 /*cdFechamento*/,0/*cdTurno*/);
				Result result = MovimentoContaServices.insertTransfer(movimento, cdConta, null, connect); 
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar abrir caixa!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @see #remove(int, boolean)
	 */
	public static Result remove(int cdConta){
		return remove(cdConta, false, null);
	}
	
	/**
	 * @see #remove(int, boolean, Connection))
	 */
	public static Result remove(int cdConta, boolean cascade){
		return remove(cdConta, cascade, null);
	}
	
	/**
	 * Método que remove uma conta financeira
	 * 
	 * @author Luiz Romario Filho
	 * @since 28/08/2014
	 * @param cdConta chave primária da conta financeira
	 * @param cascade remover em cascata ou não
	 * @param connect conexão caso precise executar transação
	 * @return result com código e mensagem da transação, code >= 0 ? sucesso : erro
	 */
	public static Result remove(int cdConta, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			/**
			 * Não permite excluir a conta caso haja movimentações
			 */
			
			ResultSetMap rsm = Search.find(" SELECT COUNT(*) "
										+ " FROM adm_movimento_conta " 
										+ " WHERE cd_conta = "+cdConta,
										"",
										null, connect, false);
			rsm.next();
			if( rsm.getInt("count") > 0 ){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Esta Conta não pôde ser excluida pois possui movimentações associadas!");
			}
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = UsuarioServices.removeByConta(cdConta, connect).getCode();
//				System.out.println("usuario ==== " + retorno);
				if(retorno > 0) {
					retorno = ContaCarteiraServices.removeByConta(cdConta, connect).getCode();
				}
			}
				
			if(!cascade || retorno>0) {
				retorno = ContaFinanceiraDAO.delete(cdConta, connect);
//				System.out.println("conta ==== " + retorno);
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta conta estão vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Conta excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir conta!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int delete(int cdConta) {
		return delete(cdConta, null);
	}
	
	public static int delete(int cdConta, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			connection.prepareStatement("DELETE FROM adm_conta_carteira WHERE cd_conta="+cdConta).execute();

			connection.prepareStatement("DELETE FROM seg_usuario_conta_financeira WHERE cd_conta="+cdConta).execute();

			if (ContaFinanceiraDAO.delete(cdConta, connection) <= 0) {
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
			System.err.println("Erro! ContaFinanceiraServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllCarteiras(int cdConta) {
		return getAllCarteiras(cdConta, null);
	}

	public static ResultSetMap getAllCarteiras(int cdConta, Connection connection) {
		boolean isConnectionNull = connection==null;
		try	{
			if (isConnectionNull)
				connection = Conexao.conectar();
			return new ResultSetMap(connection.prepareStatement("SELECT * " +
																"FROM adm_conta_carteira " +
																"WHERE A.cd_conta = "+cdConta).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllContasCarteira() {
		return getAllContasCarteira( null);
	}

	public static ResultSetMap getAllContasCarteira(Connection connection) {
		boolean isConnectionNull = connection==null;
		try	{
			if (isConnectionNull)
				connection = Conexao.conectar();
			return new ResultSetMap(connection.prepareStatement("SELECT * " +
																"FROM adm_conta_carteira A " +
																"LEFT OUTER JOIN adm_conta_financeira B ON (B.cd_conta = A.cd_conta) " +
																"LEFT OUTER JOIN grl_agencia C ON (C.cd_agencia = B.cd_agencia) " +
																"LEFT OUTER JOIN grl_banco   D ON (D.cd_banco   = C.cd_banco) ").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int getResponsavelCaixa(int cdCaixa)	{
		Connection connect = Conexao.conectar();
		try {
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_conta_financeira WHERE cd_conta = "+cdCaixa).executeQuery();
			if(rs.next())
				return rs.getInt("cd_responsavel");
			return 0;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return 0;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result isCaixaAbertoTo(int cdConta, int cdMovimentoConta, int cdUsuario, int cdFechamento, GregorianCalendar dtMovimento, Connection connect)	{
		boolean isConnNull = (connect==null); 
		try {
			if(isConnNull)
				connect = Conexao.conectar();
			// Conta
			if(cdConta <= 0)
				return new Result(-1, "Conta nao informada!");
			// Usuário
			if(cdUsuario <= 0)
				return new Result(-1, "Usuario nao informado!");
			Usuario usuario = UsuarioDAO.get(cdUsuario, connect);
			int tpUsuario   = usuario!=null ? usuario.getTpUsuario() : UsuarioServices.OPERADOR;
			// Verifica o movimento
			if(cdMovimentoConta > 0 && tpUsuario!=UsuarioServices.ADMINISTRADOR)	{
				MovimentoConta movConta = MovimentoContaDAO.get(cdMovimentoConta, cdConta, connect);
				if(movConta.getCdFechamento() > 0)
					return new Result(-1, "Este movimento já faz parte de um fechamento! Não é permitido altera-lo ou exclui-lo!");
				if(movConta.getStMovimento()==MovimentoContaServices.ST_CONCILIADO)
					return new Result(-1, "Este movimento já foi conciliado! Não é permitido altera-lo ou exclui-lo!");
			}
			// Verifica permissão de acesso à conta
			ResultSet rsUsuario = connect.prepareStatement("SELECT * " +
					                                       "FROM seg_usuario_conta_financeira A " +
					                                       "JOIN seg_usuario B ON (A.cd_usuario = B.cd_usuario) " +
  		                                                   "WHERE A.cd_conta   = "+cdConta+
		                                                   "  AND A.cd_usuario = "+cdUsuario).executeQuery();
			
			if(!rsUsuario.next())
				return new Result(-1, "O usuário nao tem permissao para efetuar lancamentos na conta informada!");
			// Verifica a conta
			ResultSet rsConta = connect.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_responsavel " +
			                                             "FROM adm_conta_financeira  A " +
			                                             "LEFT OUTER JOIN grl_pessoa B ON (A.cd_responsavel = B.cd_pessoa) " +
			                                             "WHERE A.cd_conta = "+cdConta).executeQuery();

			if(!rsConta.next())
				return new Result(-1, "A conta informada é invalida! [cdConta="+cdConta+"]");
			// int cdTurno = rsConta.getInt("cd_turno");
			// Verifica se à conta caixa 
			if(rsConta.getInt("tp_conta")==ContaFinanceiraServices.TP_CAIXA && tpUsuario!=UsuarioServices.ADMINISTRADOR)	{
				// Verifica se estão fechada
				if(rsConta.getInt("cd_responsavel")<=0)
					return new Result(-1, "A conta nao foi aberta para lancamento!");
			
				// Verifica o responsável
				if(rsConta.getInt("cd_responsavel")!=rsUsuario.getInt("cd_pessoa"))	
					return new Result(-1, "A conta esta aberta para outro usuario! [Usuario: "+rsConta.getString("nm_responsavel"));
			}
			
			// Verifica a existÃ¯Â¿Â½ncia de fechamento para o período
			/*
			dtMovimento = new GregorianCalendar(dtMovimento.get(Calendar.YEAR), dtMovimento.get(Calendar.MONTH), dtMovimento.get(Calendar.DATE));	
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento " +
					                                           "WHERE cd_conta                    = "+cdConta+
					                                           "  AND cd_turno                    = "+cdTurno+
			                                                   "  AND CAST(dt_fechamento AS DATE) = ?");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtMovimento));
			ResultSet rsFechamento = pstmt.executeQuery();
			if(rsFechamento.next() && cdFechamento!=rsFechamento.getInt("cd_fechamento"))
					return new Result(-10, "Fechamento do dia ja iniciado!");
			*/
			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return new Result(-1, "Erro ao tentar verificar abertura de caixa!", e);
		}
		finally	{
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
	/**
     * Verifica se existe pelo menos uma conta (caixa ou bancária) para as matrizes
     * @author Hugo
     * @category SICOE
     * @param connect Conexão com o banco de dados
     * @return none
     */
	public static void verificaContaMaster(Connection connect)	{
		PreparedStatement pstmt;
		try {
			ResultSet rsEmp = connect.prepareStatement("SELECT * FROM grl_empresa WHERE lg_matriz = 1 ").executeQuery();
			while(rsEmp.next())	{
				int cdEmpresa = rsEmp.getInt("cd_empresa");
				pstmt = connect.prepareStatement("SELECT * FROM adm_conta_financeira " +
						                         "WHERE cd_empresa = "+cdEmpresa);
				ResultSet rs = pstmt.executeQuery();
				if(!rs.next())	{
					
					ContaFinanceira conta = new ContaFinanceira(0/*cdConta*/,0/*cdResponsavel*/,cdEmpresa,0/*cdAgencia*/,"CONTA PADRÃO"/*Nome da Conta*/,
							                                    0/*tpConta*/,"001"/*nrConta*/,null/*nrDv*/,0/*tpOperacao*/,(float)0/*vlLimite*/,
							                                    null/*dtFechamento*/,null/*idConta*/,null/*dtVencimentoLimite*/,
							                                    (float)0/*vlSaldoInicial*/,null/*dtSaldoInicial*/,0/*cdTurno*/);
					ContaFinanceiraDAO.insert(conta, connect);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.verificaContaMaster: " + e);
		}
	}

	public static String getNrCaixaSped(String nrConta) {
		if(nrConta.length() == 3)
			return nrConta;
		else if(nrConta.length() < 3)
			return Util.fillNum(Integer.parseInt(Util.limparFormatos(nrConta, 'N')), 3);
		else
			return nrConta.substring(nrConta.length()-3);
	}
	
	public static Result gerarRelatorioConta(ArrayList<sol.dao.ItemComparator> criterios, int cdUsuario){
		return gerarRelatorioConta(criterios, cdUsuario, null);
	}
	
	public static Result gerarRelatorioConta(ArrayList<sol.dao.ItemComparator> criterios, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap();
			
			rsm = findWithSaldo(criterios, cdUsuario);
			
			while(rsm.next()){	
//				rsm.setValueToField("DS_SALDO", rsm.getString("DS_SALDO"));
				rsm.setValueToField("VL_SALDO", rsm.getString("VL_SALDO"));
			}
			
			rsm.beforeFirst();
						
			HashMap<String, Object> param = new HashMap<String, Object>();
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			
			return result;
			
		} catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Obtêm o saldo das contas  
	 * 
	 * @param criterios
	 * @param connect
	 * @return 
	 */
	public static ResultSetMap getAtivosDRE(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsmCaixa = Search.find(" SELECT A.cd_conta, id_conta,  nm_conta, sum(vl_recebido) AS vl_total "
							+ " FROM adm_conta_financeira A " 
							+ " LEFT JOIN adm_movimento_conta_receber B ON B.cd_conta = A.cd_conta "
							+ " WHERE tp_conta = 0",
							"GROUP BY A.cd_conta ORDER BY id_conta" ,
				 	null, connect, false);
			rsmCaixa.beforeFirst();
			HashMap<String, Object> registerCaixa = new HashMap<String, Object>();
			registerCaixa.put("ID_CONTA", "6.1.1.001");
			registerCaixa.put("NM_CONTA", "CAIXA");
			registerCaixa.put("NR_NIVEL", 4);
			registerCaixa.put("VL_TOTAL", null ); // TODO Iterar para obter total
			rsmCaixa.getLines().add(0, registerCaixa);
			
			ResultSetMap rsmBancaria = Search.find(" SELECT A.cd_conta, id_conta,  nm_conta, sum(vl_recebido) AS vl_total "
					+ " FROM adm_conta_financeira A " 
					+ " LEFT JOIN adm_movimento_conta_receber B ON B.cd_conta = A.cd_conta "
					+ " WHERE tp_conta = 1",
					"GROUP BY A.cd_conta ORDER BY id_conta" ,
		 	null, connect, false);
			
			rsmBancaria.beforeFirst();
			HashMap<String, Object> registerBancaria = new HashMap<String, Object>();
			registerBancaria.put("ID_CONTA", "6.1.1.002");
			registerBancaria.put("NM_CONTA", "CONTA BANCÁRIA");
			registerBancaria.put("NR_NIVEL", 4);
			registerBancaria.put("VL_TOTAL", null ); // TODO Iterar para obter total
			rsmBancaria.getLines().add(0, registerBancaria);
			
			ResultSetMap rsm = new ResultSetMap();
			
			int nrCategoria = 0;
			//formata os campos das contas caixa
			while(rsmCaixa.next()){
				if(rsmCaixa.getPosition() == 0){
					continue;
				}
				rsmCaixa.setValueToField("ID_CONTA","6.1.1.001." +  String.format("%04d", ++nrCategoria));
				rsmCaixa.setValueToField("NR_NIVEL", 5);
			}

			nrCategoria = 0;
			//formata os campos das contas bancárias
			while(rsmBancaria.next()){
				if(rsmBancaria.getPosition() == 0){
					continue;
				}
				rsmBancaria.setValueToField("ID_CONTA","6.1.1.002." +  String.format("%04d", ++nrCategoria));
				rsmBancaria.setValueToField("NR_NIVEL", 5);
			}
			
			// adiciona as contas
			rsm.getLines().addAll(rsmCaixa.getLines());
			rsm.getLines().addAll(rsmBancaria.getLines());
			
			rsm.beforeFirst();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("VL_TOTAL", null);
			register.put("NM_CONTA", "ATIVOS");
			register.put("ID_CONTA", 6 );
			register.put("NR_NIVEL", 1);
			rsm.getLines().add( 0,register);
			
			register = new HashMap<String, Object>();
			register.put("VL_TOTAL", null);
			register.put("NM_CONTA", "ATIVOS");
			register.put("ID_CONTA", "6.1" );
			register.put("NR_NIVEL", 2);
			rsm.getLines().add( 1,register);
			
			register = new HashMap<String, Object>();
			register.put("VL_TOTAL", null);
			register.put("NM_CONTA", "CONTAS");
			register.put("ID_CONTA", "6.1.1" );
			register.put("NR_NIVEL", 3);
			register.put("CD_CONTA", null );
			rsm.getLines().add( 2,register);
			
			rsm.beforeFirst();

			return rsm;
			
		} catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getPassivosDRE(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			int cdVinculo = 2;
			ResultSetMap rsm = Search.find("SELECT SUM(vl_conta - vl_pago + vl_acrescimo - vl_abatimento) AS vl_total, count(A.cd_conta_pagar) AS qt_contas, D.nm_razao_social AS nm_fornecedor  " 
											+ " FROM adm_conta_pagar A "
											+ " LEFT JOIN grl_pessoa_empresa B ON (B.cd_pessoa = A.cd_pessoa AND B.cd_empresa = A.cd_empresa AND B.cd_vinculo = " + cdVinculo + ") "
											+ " LEFT JOIN grl_pessoa C ON (C.cd_pessoa = B.cd_pessoa) "
											+ " LEFT JOIN grl_pessoa_juridica D ON (D.cd_pessoa = B.cd_pessoa)"
											+ " WHERE A.st_conta = " + ContaPagarServices.ST_EM_ABERTO,
											" GROUP BY D.cd_pessoa ORDER BY D.cd_pessoa",
					null, connect, false);
			rsm.beforeFirst();
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("VL_TOTAL", null);
			register.put("NM_FORNECEDOR", "PASSIVO");
			register.put("ID_CONTA", 9 );
			register.put("NR_NIVEL", 1);
			rsm.getLines().add( 0,register);
			register = new HashMap<String, Object>();

			register.put("VL_TOTAL", null);
			register.put("NM_FORNECEDOR", "DESPESAS");
			register.put("ID_CONTA", "9.1" );
			register.put("NR_NIVEL", 2);
			rsm.getLines().add( 1,register);
			register = new HashMap<String, Object>();
			
			register.put("VL_TOTAL", null);
			register.put("NM_FORNECEDOR", "CONTAS A PAGAR");
			register.put("ID_CONTA", "9.1.1" );
			register.put("NR_NIVEL", 3);
			rsm.getLines().add( 2,register);
			register = new HashMap<String, Object>();
			
			register.put("VL_TOTAL", null);
			register.put("NM_FORNECEDOR", "FORNECEDORES A PAGAR");
			register.put("ID_CONTA", "9.1.1.001" );
			register.put("NR_NIVEL", 4);
			rsm.getLines().add( 3,register);

			int nrConta = 1;
			while(rsm.next()){
				if(rsm.getPosition() < 4){
					continue;
				}
				rsm.setValueToField("ID_CONTA", "9.1.1.001." + String.format("%04d",nrConta++));
				rsm.setValueToField("NR_NIVEL", 5);
			}
			rsm.beforeFirst();
			return rsm;
			
		} catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}