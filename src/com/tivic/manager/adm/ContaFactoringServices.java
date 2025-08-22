package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaContaBancaria;
import com.tivic.manager.grl.PessoaContaBancariaDAO;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class ContaFactoringServices {

	public static Result insert(ContaPagar conta) {
		return insert(conta, null);
	}
	
	public static Result insert(ContaPagar conta, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			int cdEmpresa = conta.getCdEmpresa();
			
			/*Decidir Categoria Economica a set usada*/
			int cdCategoriaEconomica = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_CHEQUE_VISTA", 0, cdEmpresa, (Connection)null);
			if(cdCategoriaEconomica == 0){
				return new Result(-1, "Parametro da Categoria Economica de Cheque a vista não configurado!");
			}
			int cdCentroCusto = 0;
			
			ArrayList<ContaPagarCategoria> categorias = new ArrayList<ContaPagarCategoria>();
			categorias.add(new ContaPagarCategoria(0, cdCategoriaEconomica, conta.getVlConta().floatValue(), cdCentroCusto));
			
			/*Preencher os campo com valores padroes para chegue de retorno das Factorings*/
			int cdConta = ParametroServices.getValorOfParametroAsInteger("CD_CARTEIRA", 0, cdEmpresa, (Connection)null);
			int cdContaBancaria = 0;
			GregorianCalendar dtHoje = Util.getDataAtual();
			dtHoje.set(Calendar.HOUR, 0);
			dtHoje.set(Calendar.MINUTE, 0);
			dtHoje.set(Calendar.SECOND, 0);
			dtHoje.set(Calendar.MILLISECOND, 0);
			GregorianCalendar dtVencimento = dtHoje;
			GregorianCalendar dtPagamento = dtHoje;
			GregorianCalendar dtAutorizacao = dtHoje;
			String nrReferencia = "";
			int stConta = 0;
			int lgAutorizado = 1;
			int tpFrequencia = ContaPagarServices.UNICA_VEZ;
			int cdViagem = 0;
			int cdManutencao = 0;
			String txtObservacao = "";
			GregorianCalendar dtDigitacao = Util.getDataAtual();
			GregorianCalendar dtVencimentoOriginal = Util.getDataAtual();
			int cdTurno = 0;
			String dsHistorico = "";
			
			
			conta.setCdConta(cdConta);
			conta.setCdContaBancaria(cdContaBancaria);
			conta.setDtVencimento(dtVencimento);
			conta.setDtPagamento(dtPagamento);
			conta.setDtAutorizacao(dtAutorizacao);
			conta.setNrReferencia(nrReferencia);
			conta.setStConta(stConta);
			conta.setLgAutorizado(lgAutorizado);
			conta.setTpFrequencia(tpFrequencia);
			conta.setCdViagem(cdViagem);
			conta.setCdManutencao(cdManutencao);
			conta.setTxtObservacao(txtObservacao);
			conta.setDtDigitacao(dtDigitacao);
			conta.setDtVencimentoOriginal(dtVencimentoOriginal);
			conta.setCdTurno(cdTurno);
			conta.setDsHistorico(dsHistorico);
			Result result = ContaPagarServices.insert(conta, true, false, categorias, connection);
			
			if(result.getCode() <= 0){
				Conexao.rollback(connection);
				return new Result(-1, "Erro ao cadastrar a conta a pagar!");
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return result;
	
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connection);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir a conta a receber!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result update(ContaPagar objeto) {
		return update(objeto, null);
	}

	public static Result update(ContaPagar objeto, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			/*
			 * Tentando atualizar
			 */
			if (ContaPagarDAO.update(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				Exception e = new Exception("Não foi possível atualizar conta à pagar!");
				com.tivic.manager.util.Util.registerLog(e);
				return new Result(-1, e.getMessage(), e);
			}
			
			atualizarValoresContaPagar(objeto.getCdContaPagar(), connection);
			
			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Atualizado com sucesso!", "rsm", ContaPagarServices.getAsResultSet(objeto.getCdContaPagar()));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarServices.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar salvar conta à pagar!", e);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllContasReceberOfContaPagar(int cdContaPagar) {
		return getAllContasReceberOfContaPagar(cdContaPagar, 0, null);
	}

	public static ResultSetMap getAllContasReceberOfContaPagar(int cdContaPagar, int cdContaReceber) {
		return getAllContasReceberOfContaPagar(cdContaPagar, cdContaReceber, null);
	}
	
	public static ResultSetMap getAllContasReceberOfContaPagar(int cdContaPagar, int cdContaReceber, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			return  new ResultSetMap(connection.prepareStatement(
					    "SELECT  A.*, PS.nm_pessoa, CR.*, PSF.nr_cpf, PSJ.nr_cnpj, TC.nr_agencia, TC.st_titulo, TC.tp_emissao " +
                    	"FROM adm_conta_factoring A " +
                    	"LEFT OUTER JOIN adm_conta_receber CR ON (A.cd_conta_receber_antecipada = CR.cd_conta_receber) " +
                    	"LEFT OUTER JOIN adm_titulo_credito TC ON (CR.cd_conta_receber = TC.cd_conta_receber) " +
                    	"LEFT OUTER JOIN grl_pessoa PS ON (CR.cd_pessoa = PS.cd_pessoa) " +
                    	"LEFT OUTER JOIN grl_pessoa_fisica PSF ON (CR.cd_pessoa = PSF.cd_pessoa) " +
                    	"LEFT OUTER JOIN grl_pessoa_juridica PSJ ON (CR.cd_pessoa = PSJ.cd_pessoa) " +
                    	"WHERE A.cd_conta_pagar = "+cdContaPagar+ (cdContaReceber > 0 ? " AND CR.cd_conta_receber = " + cdContaReceber : "")).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllContasReceberOfPessoa(int cdPessoa) {
		return getAllContasReceberOfPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAllContasReceberOfPessoa(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			return  new ResultSetMap(connection.prepareStatement(
					    "SELECT  A.*, PS.nm_pessoa, CR.*, PC.nm_pessoa AS nm_cliente " +
                    	"FROM adm_conta_factoring A " +
                    	"JOIN adm_conta_receber CR ON (A.cd_conta_receber_antecipada = CR.cd_conta_receber) " +
                    	"JOIN adm_conta_pagar 	CP ON (A.cd_conta_pagar = CP.cd_conta_pagar) " +
                    	"JOIN grl_pessoa PS ON (CR.cd_pessoa = PS.cd_pessoa) " +
                    	"JOIN grl_pessoa PC ON (CP.cd_pessoa = PC.cd_pessoa) " +
                    	"WHERE PS.cd_pessoa = "+cdPessoa+" AND CR.st_conta = " + ContaReceberServices.ST_EM_ABERTO).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	//Usada para pegar todas as contas a Receber que NAO estejam atreladas a uma conta a pagar especifica
	public static ResultSetMap getAllContasReceberOfCliente(int cdPessoa, int cdContaPagar) {
		return getAllContasReceberOfCliente(cdPessoa, cdContaPagar, null);
	}
	
	public static ResultSetMap getAllContasReceberOfCliente(int cdPessoa) {
		return getAllContasReceberOfCliente(cdPessoa, 0, null);
	}

	public static ResultSetMap getAllContasReceberOfCliente(int cdPessoa, int cdContaPagar, Connection connection) {
		return getAllContasReceberOfCliente(cdPessoa, cdContaPagar, false, connection);
	}
	
	public static ResultSetMap getAllContasReceberOfCliente(int cdPessoa, int cdContaPagar, boolean hasJuros) {
		return getAllContasReceberOfCliente(cdPessoa, cdContaPagar, hasJuros, null);
	}
	
	public static ResultSetMap getAllContasReceberOfCliente(int cdPessoa, int cdContaPagar, boolean hasJuros, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			return  new ResultSetMap(connection.prepareStatement(
					    "SELECT  A.*, PS.nm_pessoa, CR.*, PE.nm_pessoa AS nm_emitente " +
                    	"FROM adm_conta_factoring A " +
                    	"JOIN adm_conta_receber CR ON (A.cd_conta_receber_antecipada = CR.cd_conta_receber) " +
                    	"JOIN adm_conta_pagar 	CP ON (A.cd_conta_pagar = CP.cd_conta_pagar) " +
                    	"JOIN grl_pessoa PS ON (CP.cd_pessoa = PS.cd_pessoa) " +
                    	"JOIN grl_pessoa PE ON (CR.cd_pessoa = PE.cd_pessoa) " +
                    	"WHERE PS.cd_pessoa = "+cdPessoa+" " +
                    	"	AND CR.st_conta = " + ContaReceberServices.ST_EM_ABERTO + 
                    	((cdContaPagar > 0) ? "	AND CP.cd_conta_pagar <> " + cdContaPagar : "") +
                    	(hasJuros ? " AND CAST(CR.dt_vencimento AS DATE) < '"+Util.convCalendarStringSql(Util.getDataAtual())+"'" : "")).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllContasReceberOfClienteWithJuros(int cdPessoa, int cdContaPagar){
		return getAllContasReceberOfClienteWithJuros(cdPessoa, cdContaPagar, null);
	}
	
	public static ResultSetMap getAllContasReceberOfClienteWithJuros(int cdPessoa, int cdContaPagar, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			ResultSetMap rsm = getAllContasReceberOfCliente(cdPessoa, cdContaPagar, true, connection);
			while(rsm.next()){
				Object result[] = ContaReceberServices.calcJurosMultaCheque(rsm.getInt("cd_conta_receber"), Util.getDataAtual(), 1);
				if((Float)result[0] == -10 && (Float)result[1] == -10){
					return null;
				}
				rsm.setValueToField("VL_MULTA", result[0]);
				rsm.setValueToField("CD_CATEGORIA_MULTA", result[4]);
				rsm.setValueToField("NM_CATEGORIA_MULTA", result[5]);
				rsm.setValueToField("VL_JUROS", result[1]);
				rsm.setValueToField("CD_CATEGORIA_JUROS", result[2]);
				rsm.setValueToField("NM_CATEGORIA_JUROS", result[3]);
			}
			rsm.beforeFirst();
			
			return rsm;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int deleteAllOfContaReceber(int cdContaReceber) {
		return deleteAllOfContaReceber(cdContaReceber, null);
	}

	public static int deleteAllOfContaReceber(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_conta_pagar FROM adm_conta_factoring WHERE cd_conta_receber_antecipada = "+cdContaReceber);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			//Considerando que uma conta a receber esta relacionada apenas a uma conta a pagar
			int cdContaPagar = 0;
			if(rsm.next()){
				cdContaPagar = rsm.getInt("cd_conta_pagar");
			
				pstmt = connect.prepareStatement("DELETE FROM adm_conta_factoring WHERE cd_conta_receber_antecipada = "+cdContaReceber);
				if(pstmt.executeUpdate() <= 0){
					Conexao.rollback(connect);
					return -1;
				}
				ContaFactoringServices.atualizarValoresContaPagar(cdContaPagar, connect);
				if(isConnectionNull)
					connect.commit();
			}
			return 1;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result atualizarValoresContaPagar(int cdContaPagar){
		return atualizarValoresContaPagar(cdContaPagar, null);
	}
	public static Result atualizarValoresContaPagar(int cdContaPagar, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			String sql = "SELECT A.vl_conta, B.vl_desconto FROM adm_conta_receber A, adm_conta_factoring B" +
					"		WHERE A.cd_conta_receber = B.cd_conta_receber_antecipada " +
					"			AND B.cd_conta_pagar = " + cdContaPagar;
			ResultSetMap rsmContasReceber = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			float vlConta	 	= 0;
			while(rsmContasReceber.next()){
				vlConta    += rsmContasReceber.getFloat("vl_conta");
			}
			ContaPagar conta = ContaPagarDAO.get(cdContaPagar, connection);
			conta.setVlConta( Double.valueOf( Float.toString(vlConta) ));
			if(ContaPagarDAO.update(conta, connection) <= 0){
				Conexao.rollback(connection);
				new Result(-1, "Erro ao atualizar conta pagar!");
			}
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_conta_pagar", Integer.toString(cdContaPagar), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = ContaPagarCategoriaDAO.find(criterios, connection);
			//Deletar as categorias antigas
			while(rsm.next()){
				if(ContaPagarCategoriaDAO.delete(cdContaPagar, rsm.getInt("cd_categoria_economica"), connection) <= 0){
					Conexao.rollback(connection);
					new Result(-1, "Erro ao deletar categoria da conta a pagar!");
				}
			}
			int cdCategoriaEonomicaChequeVista = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_CHEQUE_VISTA", 0, conta.getCdEmpresa(), connection);
			if(cdCategoriaEonomicaChequeVista == 0){
				Conexao.rollback(connection);
				new Result(-1, "Erro: Parametro de categoria economica para cheque a vista do factoring não configurado!");
			}
			int cdCategoriaEonomicaDescontoChequeVista = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_DESCONTO_CHEQUE_VISTA", 0, conta.getCdEmpresa(), connection);
			if(cdCategoriaEonomicaDescontoChequeVista == 0){
				Conexao.rollback(connection);
				new Result(-1, "Erro: Parametro de categoria economica para desconto no cheque a vista do factoring não configurado!");
			}
			//Refaz as categorias da conta
			if(ContaPagarCategoriaDAO.insert(new ContaPagarCategoria(cdContaPagar, cdCategoriaEonomicaChequeVista, vlConta, 0),  connection) <= 0){
				Conexao.rollback(connection);
				new Result(-1, "Erro ao inserir categoria da conta a pagar!");
			}
			
			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Conta Pagar atualizada!");
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connection);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar atualizar a conta a pagar!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result verificarPagamento(int cdContaPagar, int cdContaReceber, int cdContaBancaria){
		return verificarPagamento(cdContaPagar, cdContaReceber, cdContaBancaria, null);
	}
	
	public static Result verificarPagamento(int cdContaPagar, int cdContaBancaria, Connection connection){
		return verificarPagamento(cdContaPagar, 0, cdContaBancaria, connection);
	}
	
	public static Result verificarPagamento(int cdContaPagar, int cdContaReceber, int cdContaBancaria, Connection connection){
		return verificarPagamento(cdContaPagar, cdContaReceber, cdContaBancaria, false, connection);
	}
	
	public static Result verificarPagamento(int cdContaPagar, int cdContaReceber, int cdContaBancaria, boolean lgAutorizaBloqueado, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			String MENSAGEM_ALERTA = "Sistema desatualizado, vá na parte de segurança e inicialize os parametros, caso a mensagem persista, contate os desenvolvedores!";
			//Buscar conta a pagar
			ContaPagar contaPagar = ContaPagarDAO.get(cdContaPagar, connection);
			if(contaPagar == null){
				Conexao.rollback(connection);
				return new Result(-1, "Valor de codigo de conta a pagar inválido!");
			}
			
			//Buscar Cliente
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_pessoa", String.valueOf(contaPagar.getCdPessoa()), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("findCliente", String.valueOf(true), ItemComparator.EQUAL, Types.BOOLEAN));
			ResultSetMap rsmCliente = PessoaServices.find(criterios);
			if(!rsmCliente.next()){
				Conexao.rollback(connection);
				return new Result(-1, "Cliente não identificado!");
			}
			
			//Verificar estado do cliente
			if(rsmCliente.getInt("st_cadastro") == 1 && !lgAutorizaBloqueado){
				Conexao.rollback(connection);
				return new Result(-1, "O cliente " +rsmCliente.getString("nm_pessoa")+ " está bloqueado!");
			}
			
			//Buscar contas a receber factoring
			ResultSetMap rsmContaReceberFactoring = getAllContasReceberOfContaPagar(cdContaPagar, cdContaReceber, connection);
			Pessoa pessoa = new Pessoa();
			
			//Verificar estado do emitente
			while(rsmContaReceberFactoring.next()){
				pessoa = new Pessoa();
				pessoa = PessoaDAO.get(rsmContaReceberFactoring.getInt("cd_pessoa"), connection);
				if(pessoa.getStCadastro() == 1 && !lgAutorizaBloqueado){
					Conexao.rollback(connection);
					return new Result(-1, "O emitente " +pessoa.getNmPessoa()+ " está bloqueado!");
				}
			}
			rsmContaReceberFactoring.beforeFirst();
			boolean fromCliente = false;
			//-----------------------------------------------------------------------
			//Declaracao de validador
			int qtPrazoMinimo = 0;
			//Buscar parametro de prazo minimo para desconto do cheque
			if(rsmCliente.getFloat("qt_prazo_minimo_factoring_trabalho") > 0){
				qtPrazoMinimo = rsmCliente.getInt("qt_prazo_minimo_factoring_trabalho");
				fromCliente = true;
			}
			else{
				qtPrazoMinimo = ParametroServices.getValorOfParametroAsInteger("QT_PRAZO_MINIMO", 0, contaPagar.getCdEmpresa(), connection);
				fromCliente = false;
			}
			if(qtPrazoMinimo != 0){
				while(rsmContaReceberFactoring.next()){
					if((qtPrazoMinimo * 30) > rsmContaReceberFactoring.getInt("qt_dias")){
						Conexao.rollback(connection);
						return new Result(-1, "A quantidade de dias do cheque N. " + rsmContaReceberFactoring.getString("nr_documento") + " de "+rsmContaReceberFactoring.getInt("qt_dias")+" dia(s) é menor " +
											  "que a quantidade minima configurada "+(fromCliente?"para o cliente":"no geral")+" de "+(qtPrazoMinimo*30)+" dia(s)!");
					}
				}
				rsmContaReceberFactoring.beforeFirst();
			}
			else{
				if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == 1){
					Conexao.rollback(connection);
					return new Result(-1, "Parametro de Prazo Minimo não configurado!");
				}
				else if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == -1){
					Conexao.rollback(connection);
					return new Result(-1, MENSAGEM_ALERTA);
				}
				
			}
			//-----------------------------------------------------------------------
			//Declaracao de validador
			int qtPrazoMaximo = 0;
			//Buscar parametro de prazo maximo para desconto do cheque
			if(rsmCliente.getFloat("qt_prazo_maximo_factoring_trabalho") > 0){
				qtPrazoMaximo = rsmCliente.getInt("qt_prazo_maximo_factoring_trabalho");
				fromCliente = true;
			}
			else{
				qtPrazoMaximo = ParametroServices.getValorOfParametroAsInteger("QT_PRAZO_MAXIMO", 0, contaPagar.getCdEmpresa(), connection);
				fromCliente = false;
			}
			if(qtPrazoMaximo != 0){
				while(rsmContaReceberFactoring.next()){
					if((qtPrazoMaximo*30) < rsmContaReceberFactoring.getInt("qt_dias")){
						Conexao.rollback(connection);
						return new Result(-1, "A quantidade de dias do cheque N. " + rsmContaReceberFactoring.getString("nr_documento") + " de "+rsmContaReceberFactoring.getInt("qt_dias")+" dia(s) é maior " +
											  "que a quantidade máxima configurada "+(fromCliente?"para o cliente":"no geral")+" de "+(qtPrazoMaximo*30)+" dia(s)!");
					}
				}
				rsmContaReceberFactoring.beforeFirst();
			}
			else{
				if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == 1){
					Conexao.rollback(connection);
					return new Result(-1, "Parametro de Prazo Máximo não configurado!");
				}
				else if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == -1){
					Conexao.rollback(connection);
					return new Result(-1, MENSAGEM_ALERTA);
				}
				
			}
			//-----------------------------------------------------------------------
			//Declaracao de validador
			int qtIdadeConta = 0;
			//Buscar parametro de idade minima da conta
			if(rsmCliente.getFloat("qt_idade_minima_factoring_trabalho") > 0){
				qtIdadeConta = rsmCliente.getInt("qt_idade_minima_factoring_trabalho");
				fromCliente = true;
			}
			else{
				qtIdadeConta = ParametroServices.getValorOfParametroAsInteger("QT_IDADE_MINIMA", 0, contaPagar.getCdEmpresa(), connection);
				fromCliente = false;
			}
			if(qtIdadeConta != 0){
				while(rsmContaReceberFactoring.next()){
					PessoaContaBancaria pessoaConta = PessoaContaBancariaDAO.get(cdContaBancaria, rsmContaReceberFactoring.getInt("cd_pessoa"), connection);
					if(pessoaConta != null){
						int qtDiasAbertura = Util.getQuantidadeDiasUteis(pessoaConta.getDtAbertura(), Util.getDataAtual(), connection);
						if((qtIdadeConta*30) > qtDiasAbertura){
							Conexao.rollback(connection);
							return new Result(-1, "A idade da conta do emitente " + rsmContaReceberFactoring.getString("nm_pessoa") + " de "+qtDiasAbertura+" dia(s) é menor " +
												  "que a idade minima configurada "+(fromCliente?"para o cliente":"no geral")+" de "+(qtIdadeConta*30)+" dia(s)!");
						}
					}
					else{
						Conexao.rollback(connection);
						return new Result(-1, "Emitente sem conta configurada para essa agência!");
					}
				}
				rsmContaReceberFactoring.beforeFirst();
			}
			else{
				if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == 1){
					Conexao.rollback(connection);
					return new Result(-1, "Parametro de Idade Mínima não configurado!");
				}
				else if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == -1){
					Conexao.rollback(connection);
					return new Result(-1, MENSAGEM_ALERTA);
				}
				
			}
			
			
			//-----------------------------------------------------------------------
			//Declaracao de validador
			float vlGanhoMinimo = 0;
			//Buscar parametro para ganho minimo por cheque
			if(rsmCliente.getFloat("vl_ganho_minimo_factoring_trabalho") > 0){
				vlGanhoMinimo = rsmCliente.getFloat("vl_ganho_minimo_factoring_trabalho");
				fromCliente = true;
			}
			else{
				vlGanhoMinimo = ParametroServices.getValorOfParametroAsFloat("VL_GANHO_MINIMO", 0, contaPagar.getCdEmpresa(), connection);
				fromCliente = false;
			}
			if(vlGanhoMinimo != 0){
				while(rsmContaReceberFactoring.next()){
					if(vlGanhoMinimo > rsmContaReceberFactoring.getFloat("vl_desconto")){
						if(ParametroServices.getValorOfParametroAsInteger("LG_PERMITIR_SUBSTITUICAO", -1, contaPagar.getCdEmpresa(), connection) == 0){
							Conexao.rollback(connection);
							return new Result(-1, "O ganho minimo para o cheque N. " + rsmContaReceberFactoring.getString("nr_documento") + " de R$ "+Util.formatNumber(rsmContaReceberFactoring.getFloat("vl_desconto"))+" é menor " +
												  "do que o ganho minimo configurado "+(fromCliente?"para o cliente":"no geral")+" de R$ "+Util.formatNumber(vlGanhoMinimo)+"!");
						}
						else{
							int cdContaFac = rsmContaReceberFactoring.getInt("cd_conta_factoring");
							ContaFactoring contaFac = ContaFactoringDAO.get(cdContaFac, connection);
							if(contaFac != null){
								contaFac.setVlDesconto(vlGanhoMinimo);
								rsmContaReceberFactoring.setValueToField("vl_desconto", vlGanhoMinimo);
								if(ContaFactoringDAO.update(contaFac, connection) <= 0){
									Conexao.rollback(connection);
									return new Result(-1, "Parametro de Valor de Ganho Minimo não configurado!");
								}
							}
							
									
						}
						
					}
				}
				rsmContaReceberFactoring.beforeFirst();
			}
			else{
				if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == 1){
					Conexao.rollback(connection);
					return new Result(-1, "Parametro de Valor de Ganho Minimo não configurado!");
				}
				else if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == -1){
					Conexao.rollback(connection);
					return new Result(-1, MENSAGEM_ALERTA);
				}
				
			}
			//-----------------------------------------------------------------------
			//Declaracao de validador
			float vlTaxaMinima = 0;
			//Buscar parametro para taxa minima por cheque
			if(rsmCliente.getFloat("pr_taxa_minima_factoring_trabalho") > 0){
				vlTaxaMinima = rsmCliente.getFloat("pr_taxa_minima_factoring_trabalho");
				fromCliente = true;
			}
			else{
				vlTaxaMinima = ParametroServices.getValorOfParametroAsFloat("VL_TAXA_MINIMA", 0, contaPagar.getCdEmpresa(), connection);
				fromCliente = false;
			}
			if(vlTaxaMinima != 0){
				while(rsmContaReceberFactoring.next()){
					float vlGanho = rsmContaReceberFactoring.getFloat("vl_conta") * vlTaxaMinima / 100;
					
					if(vlGanho > rsmContaReceberFactoring.getFloat("vl_desconto")){
						if(ParametroServices.getValorOfParametroAsInteger("LG_PERMITIR_SUBSTITUICAO", -1, contaPagar.getCdEmpresa(), connection) == 0){
							Conexao.rollback(connection);
							return new Result(-1, "O valor descontado para o cheque N. " + rsmContaReceberFactoring.getString("nr_documento") + " de R$ "+Util.formatNumber(rsmContaReceberFactoring.getFloat("vl_desconto"))+" é menor " +
												  "do que o valor de taxa minima configurado "+(fromCliente?"para o cliente":"no geral")+" de "+Util.formatNumber(vlTaxaMinima)+"% ou R$ "+Util.formatNumber(vlGanho)+"!");
						}
						else{
							int cdContaFac = rsmContaReceberFactoring.getInt("cd_conta_factoring");
							ContaFactoring contaFac = ContaFactoringDAO.get(cdContaFac, connection);
							if(contaFac != null){
								contaFac.setVlDesconto(vlGanho);
								contaFac.setPrJuros(vlTaxaMinima);
								rsmContaReceberFactoring.setValueToField("vl_desconto", vlGanho);
								if(ContaFactoringDAO.update(contaFac, connection) <= 0){
									Conexao.rollback(connection);
									return new Result(-1, "Parametro de Valor de Ganho Minimo não configurado!");
								}
							}
									
						}
					}
				}
				rsmContaReceberFactoring.beforeFirst();
			}
			else{
				if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == 1){
					Conexao.rollback(connection);
					return new Result(-1, "Parametro de Valor de taxa minima não configurada!");
				}
				else if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == -1){
					Conexao.rollback(connection);
					return new Result(-1, MENSAGEM_ALERTA);
				}
				
			}
			//-----------------------------------------------------------------------
			//Declaracao dos validadores
			float vlLimiteFactoring 		= 0;
			float vlLimiteFactoringEmissor 	= 0;
			float vlLimiteFactoringUnitario = 0;
			//Buscar limite factoring - Cliente
			if(rsmCliente.getFloat("vl_limite_factoring_trabalho") > 0){
				vlLimiteFactoring = rsmCliente.getFloat("vl_limite_factoring_trabalho");
				fromCliente = true;
			}
			else{
				vlLimiteFactoring = ParametroServices.getValorOfParametroAsFloat("VL_LIMITE_FACTORING", 0, contaPagar.getCdEmpresa(), connection);
				fromCliente = false;
			}
			if(vlLimiteFactoring == 0){
				if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == 1){
					Conexao.rollback(connection);
					return new Result(-1, "Parametro de Limite Factoring por cliente não configurado!");
				}
				else if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == -1){
					Conexao.rollback(connection);
					return new Result(-1, MENSAGEM_ALERTA);
				}
				
			}
			float vlContaTotal = 0;
			
			//Busca os valores de todas as contas a receber ligadas pelo factoring para esse cliente
			ResultSetMap rsmTodasContas = getAllContasReceberOfCliente(rsmCliente.getInt("cd_pessoa"), 0, connection);
			while(rsmTodasContas.next())
				vlContaTotal += rsmTodasContas.getFloat("vl_conta");
			
			if(vlLimiteFactoring < vlContaTotal){
				Conexao.rollback(connection);
				return new Result(-1, "Valor da conta ultrapassou o limite configurado "+(fromCliente?"para o cliente":"no geral")+"! Valor em aberto para o cliente: R$ "+Util.formatNumber(vlContaTotal)+", Valor limite configurado: R$ "+Util.formatNumber(vlLimiteFactoring));
			}
			//-----------------------------------------------------------------------
			//Buscar limite factoring - Emissor
			if(rsmCliente.getFloat("vl_limite_factoring_emissor_trabalho") > 0){
				vlLimiteFactoringEmissor = rsmCliente.getFloat("vl_limite_factoring_emissor_trabalho");
				fromCliente = true;
			}
			else{
				vlLimiteFactoringEmissor = ParametroServices.getValorOfParametroAsFloat("VL_LIMITE_FACTORING_EMISSOR", 0, contaPagar.getCdEmpresa(), connection);
				fromCliente = false;
			}
			
			if(vlLimiteFactoringEmissor == 0){
				if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == 1){
					Conexao.rollback(connection);
					return new Result(-1, "Parametro de Limite Factoring por Emissor não configurado!");
				}
				else if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == -1){
					Conexao.rollback(connection);
					return new Result(-1, MENSAGEM_ALERTA);
				}
				
			}
			while(rsmContaReceberFactoring.next()){
				rsmTodasContas = getAllContasReceberOfPessoa(rsmContaReceberFactoring.getInt("cd_pessoa"), connection);
				float vlContaEmissor = 0;
				while(rsmTodasContas.next())
					vlContaEmissor += rsmTodasContas.getFloat("vl_conta");
				
				if(vlLimiteFactoringEmissor < vlContaEmissor){
					pessoa = new Pessoa();
					pessoa = PessoaDAO.get(rsmContaReceberFactoring.getInt("cd_pessoa"), connection);
					Conexao.rollback(connection);
					return new Result(-1, "Valor da conta do emissor "+pessoa.getNmPessoa()+" ultrapassou o limite por emissor configurado "+(fromCliente?"para o cliente":"no geral")+"! Valor em aberto para o emissor: R$ "+Util.formatNumber(vlContaEmissor)+", Valor limite configurado: R$ "+Util.formatNumber(vlLimiteFactoringEmissor));
				}
			}
			rsmContaReceberFactoring.beforeFirst();
			
			//-----------------------------------------------------------------------			
			//Buscar limite factoring - Cheque
			if(rsmCliente.getFloat("vl_limite_factoring_unitario_trabalho") > 0){
				vlLimiteFactoringUnitario = rsmCliente.getFloat("vl_limite_factoring_unitario_trabalho");
				fromCliente = true;
			}
			else{
				vlLimiteFactoringUnitario = ParametroServices.getValorOfParametroAsFloat("VL_LIMITE_FACTORING_UNITARIO", 0, contaPagar.getCdEmpresa(), connection);
				fromCliente = false;
			}
			
			if(vlLimiteFactoringUnitario == 0){
				if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == 1){
					Conexao.rollback(connection);
					return new Result(-1, "Parametro de Limite Factoring por cheque não configurado!");
				}
				else if(ParametroServices.getValorOfParametroAsInteger("LG_PARAMETROS_OBRIGATORIOS", -1, contaPagar.getCdEmpresa(), connection) == -1){
					Conexao.rollback(connection);
					return new Result(-1, MENSAGEM_ALERTA);
				}
				
			}
			
			while(rsmContaReceberFactoring.next()){
				if(vlLimiteFactoringUnitario < rsmContaReceberFactoring.getFloat("vl_conta")){
					Conexao.rollback(connection);
					return new Result(-1, "Valor do cheque N. "+rsmContaReceberFactoring.getString("nr_documento")+" ultrapassou o limite por cheque configurado "+(fromCliente?"para o cliente":"no geral")+"! Valor do cheque: R$ "+Util.formatNumber(rsmContaReceberFactoring.getFloat("vl_conta"))+", Valor limite configurado: R$ "+Util.formatNumber(vlLimiteFactoringUnitario));
				}
			}
			rsmContaReceberFactoring.beforeFirst();
			//-----------------------------------------------------------------------
			//Declaracao de validador
			int qtMaximoDocumento = 0;
			//Buscar parametro de quantidade maxima de documentos por emitente
			if(rsmCliente.getFloat("qt_maximo_documento_trabalho") > 0){
				qtMaximoDocumento = rsmCliente.getInt("qt_maximo_documento_trabalho");
				fromCliente = true;
			}
			else{
				fromCliente = false;
			}
			if(qtMaximoDocumento != 0){
				int qtDocumentos = 0;
				rsmTodasContas = getAllContasReceberOfCliente(rsmCliente.getInt("cd_pessoa"), 0, connection);
				while(rsmTodasContas.next()){
					qtDocumentos++;
				}
				if(qtMaximoDocumento < qtDocumentos){
					Conexao.rollback(connection);
					return new Result(-1, "A quantidade de maxima de documento(s) abertos configurado "+(fromCliente?"para o cliente":"no geral")+" de "+qtMaximoDocumento+ " documento(s), foi atingida!");
				}
				
				rsmContaReceberFactoring.beforeFirst();
			}
						
			//-----------------------------------------------------------------------			
			
			
			
			if (isConnectionNull)
				connection.commit();
			return new Result(1, "Todas as contas passaram na validação!");
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connection);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir a conta a pagar!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Result gerarRelatorioChequesAReceber(int cdEmpresa, int tpData, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int stTitulo, String nmEmitente, String nmCliente, String nrDocumento){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			if(dtInicial!=null) {
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
				dtInicial.set(Calendar.MILLISECOND, 0);
			}
			if(dtFinal!=null) {
				dtFinal.set(Calendar.HOUR, 0);
				dtFinal.set(Calendar.MINUTE, 0);
				dtFinal.set(Calendar.SECOND, 0);
				dtFinal.set(Calendar.MILLISECOND, 0);
			}
			String dsCampoData = "";
			switch(tpData) {
				case 0:
					dsCampoData = "A.dt_vencimento"; 
					break;
				case 1: 	
					dsCampoData = "A.dt_emissao";
					break;
				case 2: 	
					dsCampoData = "A.dt_recebimento";
			}
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("isFactoring", "1", ItemComparator.EQUAL, Types.INTEGER));
			if(tpData >= 0) {
				criterios.add(new ItemComparator(dsCampoData, Util.formatDate(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.DATE));
				criterios.add(new ItemComparator(dsCampoData, Util.formatDate(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.DATE));
			}
			if(stTitulo >= 0)
				criterios.add(new ItemComparator("I.st_titulo", String.valueOf(stTitulo), ItemComparator.EQUAL, Types.INTEGER));
			if(nmEmitente!=null && !nmEmitente.trim().equals(""))
				criterios.add(new ItemComparator("C.nm_pessoa", nmEmitente, ItemComparator.LIKE_ANY, Types.VARCHAR));
			if(nmCliente!=null && !nmCliente.trim().equals(""))
				criterios.add(new ItemComparator("CPP.nm_pessoa", nmCliente, ItemComparator.LIKE_ANY, Types.VARCHAR));
			if(nrDocumento!=null && !nrDocumento.trim().equals(""))
				criterios.add(new ItemComparator("A.nr_documento", nrDocumento, ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsm = null;
			rsm = ContaReceberServices.findParcelas(criterios, connection);
			int contConta = 0;
			while(rsm.next())	{
				rsm.setValueToField("NM_EMITENTE", rsm.getString("NM_PESSOA"));
				rsm.setValueToField("CD_EMITENTE", rsm.getString("CD_PESSOA"));
				rsm.setValueToField("VL_CHEQUE", rsm.getFloat("VL_CONTA"));
				rsm.setValueToField("VL_DESCONTO", rsm.getFloat("VL_ABATIMENTO"));
				rsm.setValueToField("VL_ARECEBER", (rsm.getFloat("VL_CONTA") - rsm.getFloat("VL_ABATIMENTO") - rsm.getFloat("VL_RECEBIDO")));
				if(rsm.getFloat("VL_ARECEBER") < 0)
					rsm.setValueToField("VL_ARECEBER", 0);
				contConta++;
			}
			
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DS_GRUPO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("nmTotal", "Total " + contConta + " conta(s)");
			param.put("dtInicial", Util.convCalendarString(dtInicial));
			param.put("dtFinal", Util.convCalendarString(dtFinal));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Result gerarRelatorioChequesAReceberByEmitente(int cdEmpresa, int tpData, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int stTitulo, String nmEmitente, String nmCliente, String nrDocumento){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			if(dtInicial!=null) {
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
				dtInicial.set(Calendar.MILLISECOND, 0);
			}
			if(dtFinal!=null) {
				dtFinal.set(Calendar.HOUR, 0);
				dtFinal.set(Calendar.MINUTE, 0);
				dtFinal.set(Calendar.SECOND, 0);
				dtFinal.set(Calendar.MILLISECOND, 0);
			}
			String dsCampoData = "";
			switch(tpData) {
				case 0:
					dsCampoData = "A.dt_vencimento"; 
					break;
				case 1: 	
					dsCampoData = "A.dt_emissao";
					break;
				case 2: 	
					dsCampoData = "A.dt_recebimento";
			}
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("isFactoring", "1", ItemComparator.EQUAL, Types.INTEGER));
			if(tpData >= 0) {
				criterios.add(new ItemComparator(dsCampoData, Util.formatDate(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.DATE));
				criterios.add(new ItemComparator(dsCampoData, Util.formatDate(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.DATE));
			}
			if(stTitulo >= 0)
				criterios.add(new ItemComparator("I.st_titulo", String.valueOf(stTitulo), ItemComparator.EQUAL, Types.INTEGER));
			if(nmEmitente!=null && !nmEmitente.trim().equals(""))
				criterios.add(new ItemComparator("C.nm_pessoa", nmEmitente, ItemComparator.LIKE_ANY, Types.VARCHAR));
			if(nmCliente!=null && !nmCliente.trim().equals("")){
				criterios.add(new ItemComparator("CPP.nm_pessoa", nmCliente, ItemComparator.LIKE_ANY, Types.VARCHAR));
				criterios.add(new ItemComparator("isGroupByCliente", "1", ItemComparator.EQUAL, Types.INTEGER));
			}
			if(nrDocumento!=null && !nrDocumento.trim().equals(""))
				criterios.add(new ItemComparator("A.nr_documento", nrDocumento, ItemComparator.EQUAL, Types.VARCHAR));
			ResultSetMap rsm = null;
			rsm = ContaReceberServices.findByCliente(criterios, connection);
			//
			//
			int contEmitente = 0;
			while(rsm.next())	{
				rsm.setValueToField("NM_EMITENTE", rsm.getString("NM_PESSOA"));
				rsm.setValueToField("CD_EMITENTE", rsm.getString("CD_PESSOA"));
				rsm.setValueToField("VL_ARECEBER", (rsm.getFloat("VL_CONTA") - rsm.getFloat("VL_RECEBIDO")));
				if(rsm.getFloat("VL_ARECEBER") < 0)
					rsm.setValueToField("VL_ARECEBER", 0);
				contEmitente++;
			}
			
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DS_GRUPO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("nmTotal", "Total " + contEmitente + " emitente(s)");
			param.put("dtInicial", Util.convCalendarString(dtInicial));
			param.put("dtFinal", Util.convCalendarString(dtFinal));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result gerarRelatorioPessoas(int cdEmpresa, int tpRelatorio, GregorianCalendar dtInicial, GregorianCalendar dtFinal, String nmPessoa, int tpData, int cdCidade, int stTitulo){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			if(dtInicial!=null) {
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
				dtInicial.set(Calendar.MILLISECOND, 0);
			}
			if(dtFinal!=null) {
				dtFinal.set(Calendar.HOUR, 0);
				dtFinal.set(Calendar.MINUTE, 0);
				dtFinal.set(Calendar.SECOND, 0);
				dtFinal.set(Calendar.MILLISECOND, 0);
			}
			String dsCampoData = null;
			switch(tpData) {
				case 0:
					dsCampoData = "CR.dt_vencimento"; 
					break;
				case 1: 	
					dsCampoData = "CR.dt_emissao";
					break;
				
			}
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("B.cd_empresa", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			if(nmPessoa!=null && !nmPessoa.trim().equals(""))
				criterios.add(new ItemComparator("A.nm_pessoa", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR));
			if(dtInicial != null)
				criterios.add(new ItemComparator("dtInicial", Util.formatDate(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.LIKE, Types.VARCHAR));
			if(dtFinal != null)
				criterios.add(new ItemComparator("dtFinal", Util.formatDate(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.LIKE, Types.VARCHAR));
			if(dsCampoData != null)
				criterios.add(new ItemComparator("dsCampoData", dsCampoData, ItemComparator.LIKE, Types.VARCHAR));
			if(tpRelatorio == 0 || tpRelatorio == 2){
				criterios.add(new ItemComparator("cdVinculo", Integer.toString(ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0)), ItemComparator.EQUAL, Types.INTEGER));
			}
			else{
				criterios.add(new ItemComparator("cdVinculo", Integer.toString(ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_EMITENTE", 0)), ItemComparator.EQUAL, Types.INTEGER));
			}
			if(cdCidade > 0){
				criterios.add(new ItemComparator("F.cd_cidade", "" + cdCidade, ItemComparator.EQUAL, Types.INTEGER));
			}
			if(stTitulo >= 0){
				criterios.add(new ItemComparator("stTitulo", "" + stTitulo, ItemComparator.EQUAL, Types.INTEGER));
			}
			ResultSetMap rsm = null;
			rsm = PessoaServices.findPessoaFactoring(criterios, connection);
			//
			//
			int contEmitente = 0;
			while(rsm.next())	{
				if(rsm.getString("nr_cpf") != null && !rsm.getString("nr_cpf").equals("")){
					rsm.setValueToField("NR_CPF_CNPJ", rsm.getString("nr_cpf"));
				}
				else if(rsm.getString("nr_cnpj") != null && !rsm.getString("nr_cnpj").equals("")){
					rsm.setValueToField("NR_CPF_CNPJ", rsm.getString("nr_cnpj"));
				}
				contEmitente++;
			}
			
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DS_GRUPO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("nmTotal", "Total " + contEmitente + " " + (tpRelatorio == 0 || tpRelatorio == 2 ? "cliente" : "emitente") +"(s)");
			param.put("dtInicial", Util.convCalendarString(dtInicial));
			param.put("dtFinal", Util.convCalendarString(dtFinal));
			param.put("tpRelatorio", tpRelatorio);
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllContaBancaria(int cdEmitente) {
		return getAllContaBancaria(cdEmitente, null);
	}

	public static ResultSetMap getAllContaBancaria(int cdEmitente, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_banco FROM grl_pessoa_conta_bancaria A " +
														"		JOIN grl_banco B ON (A.cd_banco = B.cd_banco) " +
														"		WHERE cd_pessoa = " + cdEmitente+
														"		ORDER BY nm_banco");
			ResultSetMap rsm =  new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("NM_CONTA_BANCARIA", rsm.getString("nr_agencia") + " " + rsm.getString("nm_banco") + " - " + rsm.getString("nr_conta") + " " + rsm.getString("nr_dv"));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringServices.getAllContaBancaria: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarFinalizacao(int cdContaPagar){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			ContaPagar contaPagar = ContaPagarDAO.get(cdContaPagar, connection);
			Pessoa pessoa 		  = PessoaDAO.get(contaPagar.getCdPessoa(), connection);
			
			int contConta = 0;
			ResultSetMap rsm = getAllContasReceberOfContaPagar(cdContaPagar);
			while(rsm.next())	{
				rsm.setValueToField("CD_EMITENTE", rsm.getString("CD_PESSOA"));
				rsm.setValueToField("NM_EMITENTE", rsm.getString("NM_PESSOA"));
				if(rsm.getString("NR_CPF") != null)
					rsm.setValueToField("NR_CPF_CNPJ", rsm.getString("NR_CPF"));
				if(rsm.getString("NR_CNPJ") != null)
					rsm.setValueToField("NR_CPF_CNPJ", rsm.getString("NR_CNPJ"));
				rsm.setValueToField("NR_DOCUMENTO", rsm.getString("NR_DOCUMENTO"));
				rsm.setValueToField("DT_EMISSAO", Util.convCalendarString(Util.convTimestampToCalendar(rsm.getTimestamp("DT_EMISSAO"))));
				rsm.setValueToField("DT_VENCIMENTO", Util.convCalendarString(Util.convTimestampToCalendar(rsm.getTimestamp("DT_VENCIMENTO"))));
				rsm.setValueToField("QT_DIAS", rsm.getInt("QT_DIAS"));
				rsm.setValueToField("VL_JUROS", rsm.getFloat("PR_JUROS"));
				rsm.setValueToField("VL_CHEQUE", rsm.getFloat("VL_CONTA"));
				rsm.setValueToField("VL_DESCONTO", rsm.getFloat("VL_DESCONTO"));
				rsm.setValueToField("VL_APAGAR", (rsm.getFloat("VL_CONTA") - rsm.getFloat("VL_DESCONTO")));
				contConta++;
			}
			
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DS_GRUPO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("nmTotal", "Total " + contConta + " conta(s)");
			
			param.put("CD_CLIENTE", pessoa.getCdPessoa());
			param.put("NM_CLIENTE", pessoa.getNmPessoa());
			param.put("NR_DOCUMENTO", contaPagar.getNrDocumento());
			param.put("DT_EMISSAO", Util.convCalendarString(contaPagar.getDtEmissao()));
			param.put("VL_CHEQUE", contaPagar.getVlConta());
			param.put("VL_DESCONTO", contaPagar.getVlAbatimento());
			param.put("VL_APAGAR", (contaPagar.getVlConta() - contaPagar.getVlAbatimento()));
			
			param.put("dtFinalizacao", Util.convCalendarString(Util.getDataAtual()));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFactoringServices.gerarFinalizacao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result setPagamentoResumido(int cdContaPagar, int cdConta, float vlDesconto, int cdCategoriaDesconto,
			float vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque, int cdTurno, ArrayList<HashMap<String, Object>> listaContaReceber, float vlAbatimento)
	{
		return setPagamentoResumido(cdContaPagar, cdConta,
				                    vlDesconto, cdCategoriaDesconto, vlPago, cdFormaPagamento, cdCheque, dsHistorico, cdUsuario, dtPagamento, stCheque, cdTurno, listaContaReceber, vlAbatimento, null);
	}
	
	public static Result setPagamentoResumido(int cdContaPagar, int cdConta, float vlDesconto, int cdCategoriaDesconto,
			float vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque, int cdTurno, ArrayList<HashMap<String, Object>> listaContaReceber, float vlAbatimento, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
		
			ContaPagar conta = ContaPagarDAO.get(cdContaPagar, connect);
			ArrayList<MovimentoContaReceber> movimentoContaReceber = new ArrayList<MovimentoContaReceber>();
			ArrayList<MovimentoContaCategoria> movimentoCategoria = new ArrayList<MovimentoContaCategoria>();
			Double vlRecebidoTotal = 0.0d;
			//Categoria utilizada para o desconto pelo abatimento do valor total da conta por outros cheques a receber que foram compensados 
			int cdCategoriaCompensacaoAReceber = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_DESCONTO_COMPENSACAO", 0, conta.getCdEmpresa());
			//Buscar forma de pagamento de dinheiro
			int cdFormaPagamentoCompensacao = ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_COMPENSACAO", 0);
			//Percorre a lista de contas a receber que foram selecionadas para serem abatidas nesse cheque a pagar
			for(int i = 0; i < listaContaReceber.size(); i++){
				//Busca valores de cada conta a receber que será descontada
				int cdContaReceber 	  = listaContaReceber.get(i)==null || listaContaReceber.get(i).get("cdContaReceber")==null ? 0 : (Integer) listaContaReceber.get(i).get("cdContaReceber");
				float vlJurosAReceber = listaContaReceber.get(i)==null || listaContaReceber.get(i).get("vlJuros")==null ? 0 : (Float) listaContaReceber.get(i).get("vlJuros");
				float vlMultaAReceber = listaContaReceber.get(i)==null || listaContaReceber.get(i).get("vlMulta")==null ? 0 : (Float) listaContaReceber.get(i).get("vlMulta");
				
				//Inicializa os parametros das categorias que serao usadas
				int cdCategoriaJurosAReceber 	   = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_JUROS_ATRASO", 0, conta.getCdEmpresa());
				int cdCategoriaMultaAReceber 	   = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_MULTA_DEVOLUCAO", 0, conta.getCdEmpresa());
				
				
				//Busca a conta a receber
				ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connect);

				//Calcula o valor recebido de cada conta, ou seja, o valor da propria conta somado ao valor de juros e multa
				Double vlRecebido = (contaReceber.getVlConta() + vlJurosAReceber + vlMultaAReceber);
				
				//Soma de todos os valores recebidos das contas, para fazer um valor total de movimento de conta
				vlRecebidoTotal += vlRecebido;
				
				//Significa que o valor das contas a receber a descontar ultrapassaram o valor a pagar
				//logo o cheque a pagar irá descontar uma parte do valor do ultimo cheque a receber
				//mas este ficará em aberto pois seu valor nao será pago completamente
				if((i == (listaContaReceber.size() - 1)) && vlPago < 0){
					//Instruções usadas para que o valor recebido total não ultrapasse o valor realmente pago, deixando o resto ainda na conta para ser paga
					vlRecebidoTotal = vlRecebidoTotal - vlRecebido;
					vlRecebido = contaReceber.getVlConta() + vlJurosAReceber + vlMultaAReceber + vlPago;
					vlRecebidoTotal = vlRecebidoTotal + vlRecebido;
					 
					//A data de vencimento da conta sera reajustada para a data atual, para que so se conte juros do resto a pagar, a partir da data de hoje (Ultimo Pagamento)
					contaReceber.setDtVencimento(Util.getDataAtual());
					if(ContaReceberDAO.update(contaReceber, connect) <= 0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar conta a receber!");
					}
					
					/*
					 *  Cria o recebimento
					 */
					movimentoContaReceber.add(new MovimentoContaReceber(cdConta, 0 /*cdMovimentoConta)*/,
																				  cdContaReceber, vlRecebido.floatValue(),
																				  vlJurosAReceber,vlMultaAReceber,0,
																				  0 /*vlTarifaCobranca*/,0 /*cdArquivo*/,
																				  0 /*cdRegistro*/));
					/*
					 *  Cria a classificação em categorias do movimento na conta
					 */
					float vlTotalClassificado = 0;
					
					ResultSetMap rsmCategorias = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdContaReceber, connect);
					rsmCategorias.beforeFirst();
					//USADO PARA PEGAR O VALOR DA PRIMEIRA CATEGORIA QUE EH A DA CONTA, MAS MUDAR ISSO
					int count = 0;
					while(rsmCategorias.next()){
						count++;
						Double vlMovimentoCategoria = rsmCategorias.getDouble("vl_conta_categoria");
						vlTotalClassificado += vlMovimentoCategoria;
						if(count == 1){
							vlMovimentoCategoria = vlMovimentoCategoria - (vlTotalClassificado - vlRecebido);
						}
						movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, rsmCategorias.getInt("cd_categoria_economica"),
																		   vlMovimentoCategoria.floatValue(), 0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/,
																		   cdContaReceber, MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
					}
					if (vlTotalClassificado+0.01 < (vlRecebido-vlMultaAReceber-vlJurosAReceber))	{
						Conexao.rollback(connect);
						return new Result(-30, "setBaixaResumida: Conta não classificada correntamente! [Valor Total Classificado: "+vlTotalClassificado+", Valor da Conta: "+(vlRecebido-vlMultaAReceber-vlJurosAReceber)+"]");
					}
					
					continue;
				}
				
				/*
				 *  Cria o recebimento
				 */
				movimentoContaReceber.add(new MovimentoContaReceber(cdConta, 0 /*cdMovimentoConta)*/,
																			  cdContaReceber, vlRecebido.floatValue(),
																			  vlJurosAReceber,vlMultaAReceber,0,
																			  0 /*vlTarifaCobranca*/,0 /*cdArquivo*/,
																			  0 /*cdRegistro*/));
				//Cria categorias da conta a receber
				if(vlJurosAReceber > 0)
					if(ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(cdContaReceber, cdCategoriaJurosAReceber, vlJurosAReceber, 0), connect) <=0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir categoria da conta a receber!");
					}
				
				if(vlMultaAReceber > 0)
					if(ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(cdContaReceber, cdCategoriaMultaAReceber, vlMultaAReceber, 0), connect) <=0){
						Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir categoria da conta a receber!");
					}
				
				/*
				 *  Cria a classificação em categorias do movimento na conta
				 */
				float vlTotalClassificado = 0;
				
				ResultSetMap rsmCategorias = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdContaReceber, connect);
				rsmCategorias.beforeFirst();
				while(rsmCategorias.next()){
					float vlMovimentoCategoria = rsmCategorias.getFloat("vl_conta_categoria") / (contaReceber.getVlConta().floatValue()) * 
							                                                                    (vlRecebido.floatValue() - vlMultaAReceber - vlJurosAReceber);
					vlTotalClassificado += vlMovimentoCategoria;
					movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, rsmCategorias.getInt("cd_categoria_economica"),
																	   vlMovimentoCategoria, 0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/,
																	   cdContaReceber, MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
				}
				if (vlTotalClassificado+0.01 < (vlRecebido-vlMultaAReceber-vlJurosAReceber))	{
					Conexao.rollback(connect);
					return new Result(-30, "setBaixaResumida: Conta não classificada correntamente! [Valor Total Classificado: "+vlTotalClassificado+", Valor da Conta: "+(vlRecebido-vlMultaAReceber-vlJurosAReceber)+"]");
				}
				
			}
			
			/*
			 *  Cria o movimento de conta
			 */
			int stMovimento = MovimentoContaServices.ST_COMPENSADO;
			MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/, cdConta, 0 /*cdContaOrigem*/, 0 /*cdMovimentoOrigem*/,
															cdUsuario, 0 /*cdCheque*/, 0 /*cdViagem*/,
															Util.getDataAtual(), vlRecebidoTotal.floatValue(),  null /*nrDocumento*/,
															MovimentoContaServices.CREDITO, 0 /*tpOrigem*/,
															stMovimento,
															dsHistorico, Util.getDataAtual(), null /*idExtrato*/,
															cdFormaPagamentoCompensacao, 0 /*cdFechamento*/,0/*cdTurno*/);
			/*
			 *  Chama o método que faz o lançamento da conta
			 */
			int cdMovimentoConta = MovimentoContaServices.insert(movimento, movimentoContaReceber, movimentoCategoria, new ArrayList<MovimentoContaTituloCredito>(), 0, connect).getCode();
			if(cdMovimentoConta<=0)	{
				Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("setBaixaResumida: Não foi possível gravar o movimento!"));
				return new Result(-40, "setBaixaResumida: Não foi possível gravar o movimento!");
			}
			
			//Atualiza o valor recebido do cheque
			for(int i = 0; i < listaContaReceber.size(); i++){
				//Busca valores de cada conta a receber que será descontada
				int cdContaReceber 	  = listaContaReceber.get(i)==null || listaContaReceber.get(i).get("cdContaReceber")==null ? 0 : (Integer) listaContaReceber.get(i).get("cdContaReceber");
				Double vlJurosAReceber = listaContaReceber.get(i)==null || listaContaReceber.get(i).get("vlJuros")==null ? 0 : (Double) listaContaReceber.get(i).get("vlJuros");
				Double vlMultaAReceber = listaContaReceber.get(i)==null || listaContaReceber.get(i).get("vlMulta")==null ? 0 : (Double) listaContaReceber.get(i).get("vlMulta");
				//Busca a conta a receber
				ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connect);

				//Calcula o valor recebido de cada conta, ou seja, o valor da propria conta somado ao valor de juros e multa
				//E caso for a ultima conta e o valor pago for negativo quer dizer que os cheques a receber ultrapassaram o valor
				//do cheque a pagar, e a ultima conta não foi paga totalmente
				Double vlRecebido = (contaReceber.getVlConta() + vlJurosAReceber + vlMultaAReceber + ((vlPago < 0 && i == (listaContaReceber.size() - 1)) ? vlPago : 0));
				
				//Atualiza os valores da conta a receber
				contaReceber.setVlAcrescimo(vlJurosAReceber + vlMultaAReceber);
				contaReceber.setVlRecebido(vlRecebido);
				if(contaReceber.getVlRecebido() == (contaReceber.getVlConta() + vlJurosAReceber + vlMultaAReceber)){
					contaReceber.setStConta(ContaReceberServices.ST_RECEBIDA);
					contaReceber.setDtRecebimento(Util.getDataAtual());
					contaReceber.setCdFormaPagamento(cdFormaPagamentoCompensacao);
					TituloCreditoServices.setSituacaoTitulosOfConta(contaReceber.getCdContaReceber(), 10, Util.getDataAtual(), connect);
				}
				if(ContaReceberDAO.update(contaReceber, connect) <= 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar conta a receber!");
				}
			}
			
			//Instrução usada para quando os cheques a receber descontados forem maior do que o cheque a pagar
			if(vlPago < 0)
				vlPago = 0;
			
			//Categoria usada para balancear o movimento da conta a receber
			int cdCategoriaCompensacaoAPagar = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_DESCONTO_BALANCO", 0, conta.getCdEmpresa());
			
			//Faz o pagamento que balanceia o caixa (Paga o mesmo tanto que foi recebido pela compensação)
			Result result = null;
			if(vlRecebidoTotal > 0){
				result = setPagamentoResumido(cdContaPagar, cdConta, 0, 0, vlRecebidoTotal.floatValue(), cdFormaPagamentoCompensacao, 0, null, 
				        		cdUsuario, Util.getDataAtual(), 0, 0, cdTurno, cdCategoriaCompensacaoAPagar, connect);
				if(result.getCode() <= 0){
					Conexao.rollback(connect);
					return result;
				}
			}
			//Faz o pagamento real, do valor com o desconto das compensações embutido
			result = setPagamentoResumido(cdContaPagar, cdConta, vlDesconto, cdCategoriaDesconto, vlRecebidoTotal.floatValue(), cdCategoriaCompensacaoAReceber, vlPago, cdFormaPagamento, cdCheque, dsHistorico, 
								        		cdUsuario, dtPagamento, stCheque, 0, cdTurno, 0, connect);
			if(result.getCode() <= 0){
				Conexao.rollback(connect);
				return result;
			}
			
			if (isConnectionNull)
				connect.commit();
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lançar pagamento resumido!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result setPagamentoResumido(int cdContaPagar, int cdConta, float vlDesconto, int cdCategoriaDesconto,
			float vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque, int cdTurno)
	{
		return setPagamentoResumido(cdContaPagar, cdConta, vlDesconto, cdCategoriaDesconto, vlPago, cdFormaPagamento, cdCheque, dsHistorico, cdUsuario, dtPagamento, stCheque, 0, cdTurno, null);
	}

	public static Result setPagamentoResumido(int cdContaPagar, int cdConta,
			float vlDesconto, int cdCategoriaDesconto,
			float vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque,
			int cdTituloCredito, int cdTurno, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			//Atualizando valor de conta a pagar com os descontos recebidos
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			Result result = setPagamentoResumido(cdContaPagar, cdConta, vlDesconto, cdCategoriaDesconto, vlPago, cdFormaPagamento, cdCheque, dsHistorico, cdUsuario, dtPagamento, stCheque, 0, cdTurno, 0, null);
			if(result.getCode() <= 0){
				Conexao.rollback(connect);
				return result;
			}
			if (isConnectionNull)
				connect.commit();
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lançar pagamento resumido!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result setPagamentoResumido(int cdContaPagar, int cdConta, float vlDesconto, int cdCategoriaDesconto,
			float vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque, int cdTurno, int cdCategoria)
	{
		return setPagamentoResumido(cdContaPagar, cdConta, vlDesconto, cdCategoriaDesconto, vlPago, cdFormaPagamento, cdCheque, dsHistorico, cdUsuario, dtPagamento, stCheque, 0, cdTurno, cdCategoria, null);
	}

	public static Result setPagamentoResumido(int cdContaPagar, int cdConta,
			float vlDesconto, int cdCategoriaDesconto,
			float vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque,
			int cdTituloCredito, int cdTurno, int cdCategoria, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			//Atualizando valor de conta a pagar com os descontos recebidos
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			Result result = setPagamentoResumido(cdContaPagar, cdConta, vlDesconto, cdCategoriaDesconto, 0, 0, vlPago, cdFormaPagamento, cdCheque, dsHistorico, cdUsuario, dtPagamento, stCheque, 0, cdTurno, cdCategoria, connect);
			
			if (isConnectionNull)
				connect.commit();
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lançar pagamento resumido!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
	
	public static Result setPagamentoResumido(int cdContaPagar, int cdConta, float vlDesconto, int cdCategoriaDesconto, float vlCompensacao, int cdCategoriaCompensacao,
			float vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque, int cdTurno, int cdCategoria)
	{
		return setPagamentoResumido(cdContaPagar, cdConta, vlDesconto, cdCategoriaDesconto, vlCompensacao, cdCategoriaCompensacao, vlPago, cdFormaPagamento, cdCheque, dsHistorico, cdUsuario, dtPagamento, stCheque, 0, cdTurno, cdCategoria, null);
	}

	public static Result setPagamentoResumido(int cdContaPagar, int cdConta,
			float vlDesconto, int cdCategoriaDesconto, float vlCompensacao, int cdCategoriaCompensacao,
			float vlPago, int cdFormaPagamento, int cdCheque, String dsHistorico, int cdUsuario, GregorianCalendar dtPagamento, int stCheque,
			int cdTituloCredito, int cdTurno, int cdCategoria, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			//Atualizando valor de conta a pagar com os descontos recebidos
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			/*
			 *  Verifica a Forma de Pagamento
			 */
			FormaPagamento formaPagamento = FormaPagamentoDAO.get(cdFormaPagamento, connect);
			if(formaPagamento.getTpFormaPagamento()==FormaPagamentoServices.TITULO_CREDITO &&
					formaPagamento.getNmFormaPagamento().toLowerCase().indexOf("cheque")>=0 && (cdCheque<=0))	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-10, "Pagamento em título de crédito só é permitido com a informação do cheque!");
			}
			/*
			 *  Verifica classificação de Desconto
			 */
			if(vlDesconto>0 && cdCategoriaDesconto<=0)	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-20, "Classificação do desconto não informada!");
			}

			String nrDocumento = "";
			if(cdCheque > 0)
				nrDocumento = ChequeDAO.get(cdCheque, connect).getNrCheque();
			
			ContaPagar conta = ContaPagarDAO.get(cdContaPagar, connect);
			
			
			/*
			 *  Cria o movimento de conta
			 */
			int stMovimento = MovimentoContaServices.ST_COMPENSADO;
			if(dtPagamento.after(new GregorianCalendar()) || cdCheque>0)
				stMovimento = MovimentoContaServices.ST_NAO_COMPENSADO;
			MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/,cdConta,0 /*cdContaOrigem*/,0 /*cdMovimentoOrigem*/,
														  cdUsuario,cdCheque,0 /*cdViagem*/,dtPagamento,vlPago,nrDocumento,
														  MovimentoContaServices.DEBITO, MovimentoContaServices.toPAGAMENTO /*tpOrigem*/, stMovimento,dsHistorico,
														  null /*dtDeposito*/,null /*idExtrato*/,cdFormaPagamento,0 /*cdFechamento*/, cdTurno);
			ArrayList<MovimentoContaPagar> movimentoContaPagar = new ArrayList<MovimentoContaPagar>();
			/*
			 *  Cria o pagamento
			 */
			movimentoContaPagar.add(new MovimentoContaPagar(cdConta, 0 /*cdMovimentoConta)*/, cdContaPagar, vlPago, 0,0,vlDesconto));
			/*
			 *  Cria a classificação em categorias do movimento na conta
			 */
			
			//Cria as categorias da conta a pagar
			int cdCategoriaDescontoChequeAVista = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_DESCONTO_CHEQUE_VISTA", 0, conta.getCdEmpresa());
			
			if(vlDesconto > 0)
				if(ContaPagarCategoriaDAO.insert(new ContaPagarCategoria(cdContaPagar, cdCategoriaDescontoChequeAVista, vlDesconto, 0), connect) <=0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir categoria de desconto da conta a pagar!");
				}
			
			if(vlCompensacao > 0)
				if(ContaPagarCategoriaDAO.insert(new ContaPagarCategoria(cdContaPagar, cdCategoriaCompensacao, vlCompensacao, 0), connect) <=0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir categoria de desconto da conta a pagar!");
				}
			
			if(cdCategoria == 0)
				cdCategoria = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_CHEQUE_VISTA", 0, conta.getCdEmpresa());
			else{
				if(ContaPagarCategoriaDAO.insert(new ContaPagarCategoria(cdContaPagar, cdCategoria, conta.getVlConta().floatValue(), 0), connect) <=0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir categoria da conta a receber!");
				}
			}
			float vlTotalClassificado = 0;
			ArrayList<MovimentoContaCategoria> movimentoCategoria = new ArrayList<MovimentoContaCategoria>();
			ArrayList<Integer> cdCategoriasValidas = new ArrayList<Integer>();
			cdCategoriasValidas.add(cdCategoria);
			cdCategoriasValidas.add(cdCategoriaDescontoChequeAVista);
			cdCategoriasValidas.add(cdCategoriaCompensacao);
			ResultSetMap rsmCategorias = ContaPagarCategoriaServices.getCategoriaOfContaPagar(cdContaPagar, cdCategoriasValidas, connect);
			rsmCategorias.beforeFirst();
			while(rsmCategorias.next()){
				Double vlMovimentoCategoria = rsmCategorias.getFloat("vl_conta_categoria") / conta.getVlConta() * (vlPago+vlDesconto+vlCompensacao);
				vlTotalClassificado += vlMovimentoCategoria;
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, rsmCategorias.getInt("cd_categoria_economica"),
							                                       vlMovimentoCategoria.floatValue(), 0 /*cdMovimentoContaCategoria*/, cdContaPagar, 0 /*cdContaReceber*/,
							                                       MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
			}
			if (vlTotalClassificado+0.01 < (vlPago+vlDesconto+vlCompensacao))	{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-30, "Conta não classificada correntamente! [Valor da Conta: "+(vlPago+vlDesconto+vlCompensacao)+", Valor Classificado: "+vlTotalClassificado+"]");
			}
			ArrayList<MovimentoContaTituloCredito> titulos = new ArrayList<MovimentoContaTituloCredito>();  
			if(cdTituloCredito > 0)
				titulos.add(new MovimentoContaTituloCredito(cdTituloCredito,0/*cdMovimentoConta*/,cdConta));
			/*
			 *  Chama o método que faz o lançamento da conta e outras operações
			 */
			Result result = MovimentoContaServices.insert(movimento, movimentoContaPagar, movimentoCategoria, titulos, -1/*stCheque*/, connect);
			if(result.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("Pagamento Resumido: Nao foi possivel salvar o movimento!"));
				result.setMessage("Pagamento Resumido: Nao foi possivel salvar o movimento! "+result.getMessage());
				return result;
			}

			/*
			 *  Altera situação do cheque
			 */
			if(cdCheque > 0 && stCheque >= 0)	{
				result = ChequeServices.verificaSituacaoCheque(cdCheque, stCheque, connect);
				if(result.getCode() <= 0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					com.tivic.manager.util.Util.registerLog(new Exception("Nao foi possivel alterar a situacao do cheque!"));
					return new Result(-50, "Nao foi possivel alterar a situacaoo do cheque!");
				}
			}

			//Atualiza a conta a pagar
			conta.setVlAbatimento(  Double.valueOf( Float.toString( vlDesconto )));
			conta.setVlPago(conta.getVlPago() + vlPago);
			conta.setStConta(ContaPagarServices.ST_PAGA);
			conta.setDtPagamento(Util.getDataAtual());
			if(ContaPagarDAO.update(conta, connect) <= 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar a conta a pagar!");
			}
			
			if (isConnectionNull)
				connect.commit();
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lançar pagamento resumido!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result setBaixaResumida(int cdContaReceber, int cdConta,
			float vlMulta, int cdCategoriaMulta, float vlJuros, int cdCategoriaJuros, float vlDesconto, int cdCategoriaDesconto,
			float vlRecebido, int cdFormaPagamento, TituloCredito tituloCredito, int cdUsuario, GregorianCalendar dtDeposito,
			GregorianCalendar dtMovimento, String dsHistorico)
	{
		return setBaixaResumida(cdContaReceber, cdConta, vlMulta, cdCategoriaMulta, vlJuros, cdCategoriaJuros,
                vlDesconto, cdCategoriaDesconto, vlRecebido, cdFormaPagamento, tituloCredito, cdUsuario, dtDeposito, dtMovimento, dsHistorico, null);
	}
	
	public static Result setBaixaResumida(int cdContaReceber, int cdConta,
			float vlMulta, int cdCategoriaMulta, float vlJuros, int cdCategoriaJuros, float vlDesconto, int cdCategoriaDesconto,
			float vlRecebido, int cdFormaPagamento, TituloCredito tituloCredito, int cdUsuario, GregorianCalendar dtDeposito,
			GregorianCalendar dtMovimento, String dsHistorico, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			/*
			 *  Instancia a conta a receber
			 */
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connect);
			/*
			 *  Verifica a Forma de Pagamento
			 */
			FormaPagamento formaPagamento = FormaPagamentoDAO.get(cdFormaPagamento, connect);
			if(formaPagamento.getTpFormaPagamento()==FormaPagamentoServices.TITULO_CREDITO && (tituloCredito==null))
				return new Result(-10, "Recebimento em título de crédito só é permitido com todas as informações ao título!");
			/*
			 *  Pagamento com título de crédito
			 */
			ArrayList<MovimentoContaTituloCredito> titulos = new ArrayList<MovimentoContaTituloCredito>();

			int cdCategoriaMultaProrrogacao = ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_ECONOMICA_MULTA_PRORROGACAO", 0, conta.getCdEmpresa(), connect);
			if(tituloCredito!=null && tituloCredito.getCdTituloCredito()<=0)	{
				int cdPessoa = 0;
				ContaFinanceira contaFinanceira = ContaFinanceiraDAO.get(cdConta, connect);
				String sql = "SELECT * FROM grl_pessoa A " +
							 "LEFT OUTER JOIN grl_pessoa_fisica B ON (A.cd_pessoa = B.cd_pessoa) " +
							 "WHERE nr_cpf = \'"+tituloCredito.getNrDocumentoEmissor()+"\'";
				if (tituloCredito.getTpDocumentoEmissor()==TituloCreditoServices.tdocCNPJ) 	{
					sql = "SELECT * FROM grl_pessoa A " +
						  "LEFT OUTER JOIN grl_pessoa_juridica C ON (A.cd_pessoa = C.cd_pessoa) "+
						  "WHERE nr_cnpj = \'"+tituloCredito.getNrDocumentoEmissor()+"\'";
				}
				ResultSet rs = connect.prepareStatement(sql).executeQuery();
				if(rs.next())
					cdPessoa = rs.getInt("cd_pessoa");
				//
				ContaReceber contaReceber = new ContaReceber(0, cdPessoa, contaFinanceira.getCdEmpresa(), 0 /*cdContrato*/, 0 /*cdContaOrigem*/,
															 0 /*cdDocumentoSaida*/, 0 /*cdContaCarteira*/, cdConta, 0 /*cdFrete*/,
															 tituloCredito.getNrDocumento(), null, 1 /*nrParcela*/, "1/1" /*nrReferencia*/,
															 tituloCredito.getCdTipoDocumento(), "Pagamento em título de crédito",
															 tituloCredito.getDtVencimento(), tituloCredito.getDtCredito(), null /*dtRecebimento*/,
															 null /*dtProrrogacao*/, tituloCredito.getVlTitulo(), 0.0d /*vlAbatimento*/,
															 0.0d /*vlAcrescimo*/, 0.0d /*vlRecebido*/, ContaReceberServices.ST_EM_ABERTO, ContaReceberServices.UNICA_VEZ, 1 /*qtParcelas*/,
															 ContaReceberServices.TP_PARCELA, 0 /*cdNegociacao*/, null /*txtObservacao*/, 0 /*cdPlanoPagamento*/,
															 0 /*cdFormaPagamento*/, new GregorianCalendar(), tituloCredito.getDtVencimento(), 0/*cdTurno*/, 0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/);
				ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();

				ResultSetMap rsm = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdContaReceber, connect);
				float vlTotal = 0;
				while(rsm.next())
					vlTotal += rsm.getFloat("vl_conta_categoria");
				rsm.beforeFirst();
				while(rsm.next())
					categorias.add(new ContaReceberCategoria(0 /*cdContaReceber*/,
							                                 rsm.getInt("cd_categoria_economica"), tituloCredito.getVlTitulo() / vlTotal * rsm.getFloat("vl_conta_categoria"), rsm.getInt("cd_centro_custo")));
				Result ret = ContaReceberServices.insert(contaReceber, categorias, tituloCredito, true, connect);
				contaReceber.setCdContaReceber(ret.getCode());
				if(contaReceber.getCdContaReceber() < 0)
					return new Result(-11, "Não foi possível criar a conta a receber do título de crédito!");

				titulos.add(new MovimentoContaTituloCredito(tituloCredito.getCdTituloCredito(), 0, cdConta));
			}
			/*
			 *  Verifica classificação de Multa, Juros e Desconto
			 */
			if((vlMulta>0 && cdCategoriaMulta<=0) || (vlJuros>0 && cdCategoriaJuros<=0) || (vlDesconto>0 && cdCategoriaDesconto<=0))
				return new Result(-20, "Classificação dos juros/multa ou desconto inexistente!");
			/*
			 *  Instancia a conta a receber
			 */
			conta = ContaReceberDAO.get(cdContaReceber, connect);
			/*
			 *  Cria o movimento de conta
			 */
			int stMovimento = MovimentoContaServices.ST_COMPENSADO;
			if(dtDeposito!=null && dtMovimento.after(new GregorianCalendar()))
				stMovimento = MovimentoContaServices.ST_NAO_COMPENSADO;
			MovimentoConta movimento = new MovimentoConta(0 /*cdMovimentoConta*/, cdConta, 0 /*cdContaOrigem*/, 0 /*cdMovimentoOrigem*/,
															cdUsuario, 0 /*cdCheque*/, 0 /*cdViagem*/,
															dtMovimento, vlRecebido,  null /*nrDocumento*/,
															MovimentoContaServices.CREDITO, 0 /*tpOrigem*/,
															stMovimento,
															dsHistorico, dtDeposito, null /*idExtrato*/,
															cdFormaPagamento, 0 /*cdFechamento*/,0/*cdTurno*/);
			ArrayList<MovimentoContaReceber> movimentoContaReceber = new ArrayList<MovimentoContaReceber>();
			/*
			 *  Cria o recebimento
			 */
			movimentoContaReceber.add(new MovimentoContaReceber(cdConta, 0 /*cdMovimentoConta)*/,
																		  cdContaReceber, vlRecebido,
																		  vlJuros,vlMulta,vlDesconto,
																		  0 /*vlTarifaCobranca*/,0 /*cdArquivo*/,
																		  0 /*cdRegistro*/));
			/*
			 *  Cria a classificação em categorias do movimento na conta
			 */
			float vlTotalClassificado = 0;
			ArrayList<MovimentoContaCategoria> movimentoCategoria = new ArrayList<MovimentoContaCategoria>();
			ResultSetMap rsmCategorias = ContaReceberCategoriaServices.getCategoriaOfContaReceber(cdContaReceber, connect);
			rsmCategorias.beforeFirst();
			while(rsmCategorias.next()){
				Double vlMovimentoCategoria = rsmCategorias.getDouble("vl_conta_categoria") / (conta.getVlConta()) * 
						                                                                    (vlRecebido - vlMulta - vlJuros + vlDesconto - conta.getVlAcrescimo());
				vlTotalClassificado += vlMovimentoCategoria;
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, rsmCategorias.getInt("cd_categoria_economica"),
																   vlMovimentoCategoria.floatValue(), 0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/,
																   cdContaReceber, MovimentoContaCategoriaServices.TP_PRE_CLASSIFICACAO /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
			}
			if(vlMulta > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, cdCategoriaMulta, vlMulta,
																   0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/, cdContaReceber,
																   MovimentoContaCategoriaServices.TP_MULTA /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
			if(conta.getVlAcrescimo() > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, cdCategoriaMultaProrrogacao, conta.getVlAcrescimo().floatValue(),
																   0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/, cdContaReceber,
																   MovimentoContaCategoriaServices.TP_MULTA /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
			if(vlJuros > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, cdCategoriaJuros, vlJuros,
						                                           0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/, cdContaReceber,
						                                           MovimentoContaCategoriaServices.TP_JUROS /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
			if(vlDesconto > 0)
				movimentoCategoria.add(new MovimentoContaCategoria(cdConta, 0/*cdMovimentoConta*/, cdCategoriaDesconto, vlDesconto,
						                                           0 /*cdMovimentoContaCategoria*/, 0 /*cdContaPagar*/, cdContaReceber,
						                                           MovimentoContaCategoriaServices.TP_DESCONTO /*tpMovimento*/, rsmCategorias.getInt("cd_centro_custo")));
//			
			if (vlTotalClassificado+0.01 < (vlRecebido-vlMulta-vlJuros+vlDesconto-conta.getVlAcrescimo()))	{
				return new Result(-30, "setBaixaResumida: Conta não classificada correntamente! [Valor Total Classificado: "+vlTotalClassificado+", Valor da Conta: "+(vlRecebido-vlMulta-vlJuros+vlDesconto)+"]");
			}
			/*
			 *  Chama o método que faz o lançamento da conta, o título de crédito,
			 */
			int cdMovimentoConta = MovimentoContaServices.insert(movimento, movimentoContaReceber, movimentoCategoria, titulos, 0, connect).getCode();
			if(cdMovimentoConta<=0)	{
				if(isConnectionNull)
					Conexao.rollback(connect);
				com.tivic.manager.util.Util.registerLog(new Exception("setBaixaResumida: Não foi possível gravar o movimento!"));
				return new Result(-40, "setBaixaResumida: Não foi possível gravar o movimento!");
			}

			//Atualizando valor de acrescimo na conta a receber
			conta = ContaReceberDAO.get(cdContaReceber, connect);
			conta.setVlAcrescimo(conta.getVlAcrescimo() + vlJuros + vlMulta);
			conta.setVlAbatimento(conta.getVlAbatimento() + vlDesconto);
			if(conta.getVlRecebido() == conta.getVlConta() + conta.getVlAcrescimo() - conta.getVlAbatimento()){
				conta.setStConta(ContaReceberServices.ST_RECEBIDA);
				conta.setDtRecebimento(Util.getDataAtual());
				TituloCreditoServices.setSituacaoTitulosOfConta(cdContaReceber, ContaReceberServices.ST_RECEBIDA, Util.getDataAtual(), connect);
			}
			if(ContaReceberDAO.update(conta, connect) <= 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar valores de conta a receber!");
			}
			if (isConnectionNull)
				connect.commit();

			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lançar recebimento resumido!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}

	}
	
	public static Result setBaixaResumidaResgate(int cdContaReceber, int cdConta,
			float vlMulta, int cdCategoriaMulta, float vlJuros, int cdCategoriaJuros, float vlDesconto, int cdCategoriaDesconto,
			float vlRecebido, int cdFormaPagamento, TituloCredito tituloCredito, int cdUsuario, GregorianCalendar dtDeposito,
			GregorianCalendar dtMovimento, String dsHistorico)
	{
		return setBaixaResumidaResgate(cdContaReceber, cdConta, vlMulta, cdCategoriaMulta, vlJuros, cdCategoriaJuros,
                vlDesconto, cdCategoriaDesconto, vlRecebido, cdFormaPagamento, tituloCredito, cdUsuario, dtDeposito, dtMovimento, dsHistorico, null);
	}
	
	public static Result setBaixaResumidaResgate(int cdContaReceber, int cdConta,
			float vlMulta, int cdCategoriaMulta, float vlJuros, int cdCategoriaJuros, float vlDesconto, int cdCategoriaDesconto,
			float vlRecebido, int cdFormaPagamento, TituloCredito tituloCredito, int cdUsuario, GregorianCalendar dtDeposito,
			GregorianCalendar dtMovimento, String dsHistorico, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
		
		
			Result retorno = setBaixaResumida(cdContaReceber, cdConta, vlMulta, cdCategoriaMulta, vlJuros, cdCategoriaJuros,
                								vlDesconto, cdCategoriaDesconto, vlRecebido, cdFormaPagamento, tituloCredito, cdUsuario, dtDeposito, dtMovimento, dsHistorico, connect);
			
			if(retorno.getCode() <= 0){
				Conexao.rollback(connect);
				return retorno;
			}
			
			//Atualizando situacao de titulo de credito para cheque resgatado
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_conta_receber", Integer.toString(cdContaReceber), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmTitulo = TituloCreditoDAO.find(criterios, connect);
			TituloCredito tituloConta = null;
			while(rsmTitulo.next()){
				tituloConta = TituloCreditoDAO.get(rsmTitulo.getInt("cd_titulo_credito"), connect);
			}
			if(tituloConta == null){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao buscar titulo de credito relacionado ao cheque!");
			}
			tituloConta.setStTitulo(TituloCreditoServices.stRESGATADO);
			if(TituloCreditoDAO.update(tituloConta, connect) <= 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar titulo de credito do cheque!");
			}
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar lançar recebimento resumido!", e);
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}

	}
}
