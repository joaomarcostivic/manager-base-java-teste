package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.util.Util;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class ClienteProgramaFaturaServices {
	
	public static Result save(ClienteProgramaFatura objeto) {
		return save(objeto, null);
	}

	public static Result save(ClienteProgramaFatura objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ProgramaFatura programaFaturaNovo = ProgramaFaturaDAO.get(objeto.getCdProgramaFatura(), connect);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if(objeto.getCdPessoa() > 0 && objeto.getCdEmpresa() > 0){
				criterios.add(new ItemComparator("cd_pessoa", "" + objeto.getCdPessoa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + objeto.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
			}
			else{
				criterios.add(new ItemComparator("cd_classificacao", "" + objeto.getCdClassificacao(), ItemComparator.EQUAL, Types.INTEGER));
			}
			ResultSetMap rsmClienteProgramaFatura = ClienteProgramaFaturaDAO.find(criterios, connect);
			while(programaFaturaNovo.getLgPeriodo() == 1 && rsmClienteProgramaFatura.next()){
				ProgramaFatura programaFatura = ProgramaFaturaDAO.get(rsmClienteProgramaFatura.getInt("cd_programa_fatura"), connect);
				if(programaFatura.getLgPeriodo()==1){
					if(programaFaturaNovo.getNrInicioPeriodo() < programaFatura.getNrInicioPeriodo() && 
					   programaFaturaNovo.getNrFinalPeriodo() >= programaFatura.getNrInicioPeriodo()){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Programa de fatura " + programaFaturaNovo.getNmProgramaFatura() + " choca-se no periodo com o programa de fatura " + programaFatura.getNmProgramaFatura() + " já cadastrado para esse cliente.");
					}
					else if(programaFatura.getNrInicioPeriodo() < programaFaturaNovo.getNrInicioPeriodo() && 
					   programaFatura.getNrFinalPeriodo() >= programaFaturaNovo.getNrInicioPeriodo()){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Programa de fatura " + programaFaturaNovo.getNmProgramaFatura() + " choca-se no periodo com o programa de fatura " + programaFatura.getNmProgramaFatura() + " já cadastrado para esse cliente.");
					}
				}
				
				if(programaFatura.getLgVencimentoFixo()==1 && programaFaturaNovo.getLgVencimentoFixo()==1){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Programa de fatura " + programaFatura.getNmProgramaFatura() + " já é de vencimento fixo, não se pode cadastrar dois para o mesmo cliente.");
				}
			}
			
			criterios.add(new ItemComparator("cd_programa_fatura", "" + objeto.getCdProgramaFatura(), ItemComparator.EQUAL, Types.INTEGER));
			rsmClienteProgramaFatura = ClienteProgramaFaturaDAO.find(criterios, connect);
			if(rsmClienteProgramaFatura.next()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, (objeto.getCdClassificacao() > 0 ? "SubCategoria" : "Cliente") + " já possui relacionamento com esse programa de fatura");
			}
			
			int retorno = ClienteProgramaFaturaDAO.insert(objeto, connect);
			
			if(retorno < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				
				return new Result(-1, "Problema ao tentar salvar ClienteProgramaFatura");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Salvo com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaDAO.insert: " +  e);
			return new Result(-1, "Erro ClienteProgramaFaturaServices.save");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getByCliente(int cdEmpresa, int cdPessoa) {
		return getByCliente(cdEmpresa,cdPessoa, null);
	}
	
	public static ResultSetMap getByCliente(int cdEmpresa, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT B.*, A.cd_pessoa, A.cd_empresa, A.cd_cliente_programa_fatura " +
					     " FROM adm_cliente_programa_fatura A " +
					     " JOIN adm_programa_fatura B ON(A.cd_programa_fatura = B.cd_programa_fatura) " +
					     " WHERE A.cd_pessoa = " + cdPessoa + 
					     "  AND A.cd_empresa = " + cdEmpresa + 
					     "  AND B.st_programa_fatura = " + ProgramaFaturaServices.ST_ATIVO;
					
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteServices.updateParametrosCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getByClassificacao(int cdClassificacao) {
		return getByClassificacao(cdClassificacao, null);
	}
	
	public static ResultSetMap getByClassificacao(int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT B.*, A.cd_classificacao, A.cd_cliente_programa_fatura " +
					     " FROM adm_cliente_programa_fatura A " +
					     " JOIN adm_programa_fatura B ON(A.cd_programa_fatura = B.cd_programa_fatura) " +
					     " WHERE A.cd_classificacao = " + cdClassificacao;
					
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteServices.updateParametrosCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getProgramaFaturaVigente(int cdEmpresa, int cdPessoa) {
		return getProgramaFaturaVigente(cdEmpresa,cdPessoa, Util.getDataAtual(), null);
	}
	
	public static Result getProgramaFaturaVigente(int cdEmpresa, int cdPessoa, Connection connect) {
		return getProgramaFaturaVigente(cdEmpresa,cdPessoa, Util.getDataAtual(), connect);
	}
	
	public static Result getProgramaFaturaVigente(int cdEmpresa, int cdPessoa, GregorianCalendar dataAtual) {
		return getProgramaFaturaVigente(cdEmpresa,cdPessoa, dataAtual, null);
	}
	
	public static Result getProgramaFaturaVigente(int cdEmpresa, int cdPessoa, GregorianCalendar dataAtual, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			Result resultado = new Result(-1, "Não encontrado");
			//Busca os programas de fatura do cliente
			ResultSetMap rsmProgramaFatura = ClienteProgramaFaturaServices.getByCliente(cdEmpresa, cdPessoa);
			
			if(rsmProgramaFatura==null)
				return resultado;
					
			//Caso o cliente não tenha programa de fatura, o programa de fatura padrão será considerado
			if(rsmProgramaFatura.size()==0){
				ProgramaFatura programaPadrao = ProgramaFaturaServices.getPadrao();
				if(programaPadrao != null){
					rsmProgramaFatura = ProgramaFaturaServices.get(programaPadrao.getCdProgramaFatura(), connect);
				}
			}
			
			//Testa para saber se o Cliente tem programa de fatura de:
			//PERIODO
			while(rsmProgramaFatura.next()){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_programa_fatura", rsmProgramaFatura.getString("cd_programa_fatura"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("lg_periodo", "1", ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = ProgramaFaturaDAO.find(criterios);
				
				GregorianCalendar hoje = dataAtual;
				int diaDoMes = hoje.get(Calendar.DAY_OF_MONTH);
				
				while(rsm.next()){
					if(diaDoMes >= rsm.getInt("nr_inicio_periodo") && diaDoMes <= rsm.getInt("nr_final_periodo")){
						resultado = new Result(1, "Encontrado");
						resultado.addObject("programaFatura", ProgramaFaturaDAO.get(rsm.getInt("cd_programa_fatura")));
						return resultado;
					}
				}
			}
			rsmProgramaFatura.beforeFirst();
			//Dia Fixo
			while(rsmProgramaFatura.next()){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_programa_fatura", rsmProgramaFatura.getString("cd_programa_fatura"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("lg_vencimento_fixo", "1", ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = ProgramaFaturaDAO.find(criterios);
				
				if(rsm.next()){
					resultado = new Result(1, "Encontrado");
					resultado.addObject("programaFatura", ProgramaFaturaDAO.get(rsm.getInt("cd_programa_fatura")));
					return resultado;
				}
			}
			//Testa para saber se a Classificação do cliente tem programa de fatura de:
			//PERIODO
			Pessoa cliente = PessoaDAO.get(cdPessoa);
			Classificacao classificacao = ClassificacaoDAO.get(cliente.getCdClassificacao(), connect);
			//Caso o cliente não tenha classificação, o sistema irá usar a padrão
			if(classificacao == null){
				classificacao = ClassificacaoServices.getPadrao();
			}
			
			if(classificacao != null && classificacao.getLgAtivo()==1){
				//Busca os programas de fatura da classificação do cliente
				rsmProgramaFatura = ClienteProgramaFaturaServices.getByClassificacao(classificacao.getCdClassificacao());
				while(rsmProgramaFatura.next()){
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_programa_fatura", rsmProgramaFatura.getString("cd_programa_fatura"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("lg_periodo", "1", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsm = ProgramaFaturaDAO.find(criterios);
					
					GregorianCalendar hoje = dataAtual;
					int diaDoMes = hoje.get(Calendar.DAY_OF_MONTH);
					
					while(rsm.next()){
						if(diaDoMes >= rsm.getInt("nr_inicio_periodo") && diaDoMes <= rsm.getInt("nr_final_periodo")){
							resultado = new Result(1, "Encontrado");
							resultado.addObject("programaFatura", ProgramaFaturaDAO.get(rsm.getInt("cd_programa_fatura")));
							return resultado;
						}
					}
				}
				rsmProgramaFatura.beforeFirst();
			
				//Dia Fixo
				while(rsmProgramaFatura.next()){
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_programa_fatura", rsmProgramaFatura.getString("cd_programa_fatura"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("lg_vencimento_fixo", "1", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsm = ProgramaFaturaDAO.find(criterios);
					
					if(rsm.next()){
						resultado = new Result(1, "Encontrado");
						resultado.addObject("programaFatura", ProgramaFaturaDAO.get(rsm.getInt("cd_programa_fatura")));
						return resultado;
					}
				}
			}
			return resultado;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaServices.getProgramaFaturaVigente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para definir quais as formas e planos de pagamento carregados inicialmente na hora do faturamento a 
	 * depender do cliente, seus programas de fatura e condições de pagamento
	 * @since 05/08/2014
	 * @author Gabriel
	 * @param cdEmpresa
	 * @param cdPessoa
	 * @return ResultSetMap que contém um objeto rsmFormaPagamento que lista em um outro RSM as formas de pagamento permitidas e um rsmPlanoPagamento que lista os planos
	 */
	public static ResultSetMap getFormasPagamentoPDV(int cdEmpresa, int cdPessoa) {
		try{
			Result resultado = getFaturamentoByCliente(cdEmpresa,cdPessoa, null);
			return (ResultSetMap) resultado.getObjects().get("rsmFormaPagamento");
		}catch(Exception e) {
			Util.registerLog(e);
			return null;
		}
	}
	
	public static Result getFaturamentoByCliente(int cdEmpresa, int cdPessoa) {
		return getFaturamentoByCliente(cdEmpresa,cdPessoa, null);
	}
	
	public static Result getFaturamentoByCliente(int cdEmpresa, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(!isConnectionNull); 
			Result retorno = new Result(1);
			
			Result resultado = getProgramaFaturaVigente(cdEmpresa, cdPessoa, connect);
			
			boolean hasProgramaVigente = false;
			//Inicialmente são carregados todos os planos de pagamento, a partir da alteração das formas de pagamento, irá em outro método
			//carregar os planos de pagamento permitidos, a partir da forma e do cliente
			if(resultado.getCode() > 0){
				hasProgramaVigente = true;
				ResultSetMap rsmPlanoPagamento = PlanoPagamentoDAO.getAll();
				retorno.addObject("rsmPlanoPagamento", rsmPlanoPagamento);
			}
			//Carrega as formas de pagamento para 
			//	MOEDA CORRENTE
			//	TEF 
			//	Titulos de credito permitidos para o cliente
			// 	Forma de Pagamento cadastrada no parametro para Programa de Fatura
			
			//Carrega todas as formas de pagamento que são MOEDA CORRENTE e TEF
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("tp_forma_pagamento", "" + FormaPagamentoServices.TITULO_CREDITO, ItemComparator.DIFFERENT, Types.INTEGER));
			ResultSetMap rsmFormas = FormaPagamentoDAO.find(criterios);
			//Forma de pagamento para programa de fatura
			int cdFormaPagamentoPadrao = ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_PROGRAMA_FATURA", 0, cdEmpresa);
			if(cdFormaPagamentoPadrao <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				Result result = new Result(-1, "Parametro para Forma de Pagamento padrão de programa de fatura deve ser configurado!");
				return result;
			}
			
			
			//Busca os TITULOS DE CREDITO permitidos para esse cliente e para sua classificação
			ResultSetMap rsmCondicaoPagamento = CondicaoPagamentoServices.getFormaPagamentoCliente(cdPessoa, cdEmpresa, connect);
			//Usará a condição padrão caso o cliente não tenha nenhuma
			if(rsmCondicaoPagamento.size() == 0){
				CondicaoPagamento condicaoPadrao = CondicaoPagamentoServices.getPadrao();
				if(condicaoPadrao != null)
					rsmCondicaoPagamento = CondicaoPagamentoServices.get(condicaoPadrao.getCdCondicaoPagamento(), connect);
				// caso nao tenha nenhuma condição cadastrada, etnão busca todas as formas de pagamento.
				else
					rsmCondicaoPagamento = FormaPagamentoServices.getAll();
			}
			
			if(rsmCondicaoPagamento.size() > 0){
				while(rsmCondicaoPagamento.next()){
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_forma_pagamento", "" + rsmCondicaoPagamento.getInt("cd_forma_pagamento"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmFormaPagamento = FormaPagamentoDAO.find(criterios);
					if(rsmFormaPagamento.next())
						rsmFormas.addRegister(rsmFormaPagamento.getRegister());
				}
			}
			//Caso ele não tenha nenhum titulo de credito cadastrado o sistema verificará um parametro que indicará se caso isso ocorra
			//deva-se retornar todas as formas de titulo de credito (COM VERIFICAÇÃO PARA A DE PROGRAMA DE FATURA) ou não.
			else{
				if(ParametroServices.getValorOfParametroAsInteger("LG_CONDICAO_PAGAMENTO_OBRIGATORIA", 0, cdEmpresa)==0){
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("tp_forma_pagamento", "" + FormaPagamentoServices.TITULO_CREDITO, ItemComparator.DIFFERENT, Types.INTEGER));
					criterios.add(new ItemComparator("cd_forma_pagamento", "" + cdFormaPagamentoPadrao, ItemComparator.DIFFERENT, Types.INTEGER));
					ResultSetMap rsmFormasTituloCredito = FormaPagamentoDAO.find(criterios);
					while(rsmFormasTituloCredito.next()){
						rsmFormas.addRegister(rsmFormasTituloCredito.getRegister());
					}
				}
			}
			
			//Verifica se o cliente possui programa de fatura, para cadastrar a opção de forma de pagamento para programa de fatura
			if(hasProgramaVigente){
				ResultSetMap rsmFormaPagamentoPadrao = FormaPagamentoServices.getAsResultSetMap(cdFormaPagamentoPadrao);
				if(rsmFormaPagamentoPadrao.next()){
					rsmFormas.addRegister(rsmFormaPagamentoPadrao.getRegister());
				}
			}
			
			retorno.addObject("prDesconto", 0);
			retorno.addObject("rsmFormaPagamento", rsmFormas);
			//
			return retorno;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaServices.getFaturamentoByCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para carregar os planos de pagamento permitidos quando se escolhe determinada forma de pagamento na tela
	 * @since 05/08/2014
	 * @author Gabriel
	 * @param cdFormaPagamento
	 * @return ResultSetMap que contém um objeto rsmPlanoPagamento que lista todos os planos permitidos para aquela forma de pagamento
	 */
	public static ResultSetMap getPlanoPagamentoPDV(int cdFormaPagamento, int cdEmpresa, int cdPessoa) {
		Result resultado = getPlanoPagamentoByForma(cdFormaPagamento, cdEmpresa, cdPessoa, null);
		return (ResultSetMap) resultado.getObjects().get("rsmPlanoPagamento");
	}
	
	public static Result getPlanoPagamentoByForma(int cdFormaPagamento, int cdEmpresa, int cdPessoa) {
		return getPlanoPagamentoByForma(cdFormaPagamento, cdEmpresa, cdPessoa, null);
	}
	
	public static Result getPlanoPagamentoByForma(int cdFormaPagamento, int cdEmpresa, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(!isConnectionNull); 
			Result retorno = new Result(1);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_forma_pagamento", "" + cdFormaPagamento, ItemComparator.EQUAL, Types.INTEGER));
			
			
			//Busca os planos permitidos para a forma passada
			ResultSetMap rsmPlanoPagamento = FormaPlanoPagamentoServices.find(criterios, connect);
			retorno.addObject("prDesconto", 0);
			
			//Forma de pagamento para programa de fatura
			int cdFormaPagamentoPadrao = ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_PROGRAMA_FATURA", 0, cdEmpresa);
			if(cdFormaPagamentoPadrao <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				Result result = new Result(-1, "Parametro para Forma de Pagamento padrão de programa de fatura deve ser configurado!");
				return result;
			}
			
			//Carrega apenas o plano de pagamento da forma de pagamento do paramento
			if(cdFormaPagamentoPadrao == cdFormaPagamento){
				//Caso o cliente tenha o programa de fatura vigente (do dia), carrega-se também o desconto aplicado
				Result resultado = getProgramaFaturaVigente(cdEmpresa, cdPessoa, connect);
				if(resultado.getCode() > 0){
					ProgramaFatura programaFatura = (ProgramaFatura)resultado.getObjects().get("programaFatura");
					retorno.addObject("prDesconto", programaFatura.getPrDesconto());
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_programa_fatura", "" + programaFatura.getCdProgramaFatura(), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmProgramaFaturaPlano = ProgramaFaturaPlanoDAO.find(criterios, connect);
					//Caso seu programa de fatura tenha algum plano de pagamento associado, apenas estes serão permitidos, caso contrário serão os da própria forma
					if(rsmProgramaFaturaPlano.size() > 0){
						rsmPlanoPagamento = new ResultSetMap();
					}
					//Busca os planos permitidos para aquele programa de fatura
					while(rsmProgramaFaturaPlano.next()){
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_plano_pagamento", "" + rsmProgramaFaturaPlano.getInt("cd_plano_pagamento"), ItemComparator.EQUAL, Types.INTEGER));
							ResultSetMap rsmPlano = PlanoPagamentoDAO.find(criterios, connect);
							if(rsmPlano.next()){
								rsmPlanoPagamento.addRegister(rsmPlano.getRegister());
							}
					}
				}
			}
			
			retorno.addObject("rsmPlanoPagamento", rsmPlanoPagamento);
			//
			return retorno;
		}
		catch(Exception e) {
			System.out.println("Erro e = " + e);
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteProgramaFaturaServices.getFaturamentoByCliente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
