package com.tivic.manager.prc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import com.tivic.manager.adm.CategoriaEconomicaServices;
import com.tivic.manager.adm.ContaCarteiraServices;
import com.tivic.manager.adm.ContaFinanceiraServices;
import com.tivic.manager.adm.ContaPagar;
import com.tivic.manager.adm.ContaPagarArquivo;
import com.tivic.manager.adm.ContaPagarArquivoServices;
import com.tivic.manager.adm.ContaPagarCategoria;
import com.tivic.manager.adm.ContaPagarDAO;
import com.tivic.manager.adm.ContaPagarServices;
import com.tivic.manager.adm.ContaReceber;
import com.tivic.manager.adm.ContaReceberArquivo;
import com.tivic.manager.adm.ContaReceberArquivoServices;
import com.tivic.manager.adm.ContaReceberCategoria;
import com.tivic.manager.adm.ContaReceberDAO;
import com.tivic.manager.adm.ContaReceberServices;
import com.tivic.manager.agd.AgendaItemServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaContaBancaria;
import com.tivic.manager.grl.PessoaContaBancariaDAO;
import com.tivic.manager.grl.PessoaContaBancariaServices;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.print.ExcelServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ProcessoFinanceiroServices {
	
	public static final int TP_NATUREZA_RECEITA = 0;
	public static final int TP_NATUREZA_DESPESA = 1;
	
	public static final String[] natureza = {"Receita", "Despesa"};
	
	public static final String NR_REFERENCIA_PARCELA_UNICA = "1/1";
	
	public static Result save(ProcessoFinanceiro processoFinanceiro){
		return save(processoFinanceiro, null, null);
	}
	
	public static Result save(ProcessoFinanceiro processoFinanceiro, AuthData authData){
		return save(processoFinanceiro, authData, null);
	}
	
	public static Result save(ProcessoFinanceiro processoFinanceiro, Connection connect){
		return save(processoFinanceiro, null, connect);
	}
	
	public static Result save(ProcessoFinanceiro processoFinanceiro, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(processoFinanceiro==null)
				return new Result(-1, "Erro ao salvar. Processo financeiro é nulo");
			
			//COMPLIANCE
			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			
			
			int retorno;
						
			if(processoFinanceiro.getCdEventoFinanceiro()==0){
				retorno = ProcessoFinanceiroDAO.insert(processoFinanceiro, connect);
				processoFinanceiro.setCdEventoFinanceiro(retorno);
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
			}
			else {
				retorno = ProcessoFinanceiroDAO.update(processoFinanceiro, connect);
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
			}

			ComplianceManager.process(processoFinanceiro, authData, tpAcao, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROCESSOFINANCEIRO", processoFinanceiro);
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
	
	public static Result addProcessosFinanceiros(ProcessoFinanceiro processoFinanceiro, boolean reembolsavel, int cdEmpresa){
		return addProcessosFinanceiros(processoFinanceiro, reembolsavel, cdEmpresa, null, 1, 0, null, null, null);
	}
	
	public static Result addProcessosFinanceiros(ProcessoFinanceiro processoFinanceiro, boolean reembolsavel, int cdEmpresa, ResultSetMap rsmContas, 
			int qtParcelas, int frequenciaRepeticao, AuthData authData){
		return addProcessosFinanceiros(processoFinanceiro, reembolsavel, cdEmpresa, rsmContas, qtParcelas, frequenciaRepeticao, authData, null, null);
	}
	
	public static Result addProcessosFinanceiros(ProcessoFinanceiro processoFinanceiro, boolean reembolsavel, int cdEmpresa, ResultSetMap rsmContas, 
			int qtParcelas, int frequenciaRepeticao, AuthData authData, GregorianCalendar dtReembolso){
		return addProcessosFinanceiros(processoFinanceiro, reembolsavel, cdEmpresa, rsmContas, qtParcelas, frequenciaRepeticao, authData, dtReembolso, null);
	}
	
	public static Result addProcessosFinanceiros(ProcessoFinanceiro processoFinanceiro, boolean reembolsavel, int cdEmpresa, ResultSetMap rsmContas, 
			int qtParcelas, int frequenciaRepeticao, AuthData authData, GregorianCalendar dtReembolso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(processoFinanceiro==null)
				return new Result(-1, "Erro ao salvar. Processo financeiro é nulo");
			
			int retorno = 0;
			
			int cdContaPadrao = ParametroServices.getValorOfParametroAsInteger("CD_CONTA_PADRAO_DAJ", 0);
			int cdTipoDocumentoPadrao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_DAJ", 0);
			int cdTipoArquivoPadrao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ARQUIVO_DAJ", 0);
							
			processoFinanceiro.setDtLancamento(new GregorianCalendar());
			
			LogUtils.debug("ProcessoFinanceiroServices.addProcessosFinanceiros");
			LogUtils.debug("processoFinanceiro: "+processoFinanceiro);
			
			if(processoFinanceiro.getTpNaturezaEvento()==TP_NATUREZA_RECEITA && qtParcelas>0) { //RECEITA: multiparcela
				
				GregorianCalendar dtEvento = (GregorianCalendar)processoFinanceiro.getDtEventoFinanceiro().clone();
				
				for (int i = 0; i < qtParcelas; i++) {
					ProcessoFinanceiro p = (ProcessoFinanceiro)processoFinanceiro.clone();
					
					p.setCdEventoFinanceiroOrigem(processoFinanceiro.getCdEventoFinanceiro());
					
					if(i>0) {
						switch(frequenciaRepeticao){
							case 0: //mensal
								dtEvento.add(Calendar.MONTH, 1);
								break;
							case 1: //bimestral
								dtEvento.add(Calendar.MONTH, 2);
								break;
							case 2: //trimestral
								dtEvento.add(Calendar.MONTH, 3);
								break;
							case 3: //semestral
								dtEvento.add(Calendar.MONTH, 6);
								break;
							case 4: //anual
								dtEvento.add(Calendar.YEAR, 1);
								break;
						}
					}
					
					p.setDtEventoFinanceiro(dtEvento);
					p.setNrReferencia((i+1)+"/"+qtParcelas);
					
					retorno = ProcessoFinanceiroDAO.insert(p, connect);
					
					if(retorno<=0)
						break;
				}
				
			}
			else { //DESPESA 
				
				if(rsmContas==null || rsmContas.size()==0){ //sem conta pagar
					retorno = ProcessoFinanceiroDAO.insert(processoFinanceiro, connect);
					processoFinanceiro.setCdEventoFinanceiro(retorno);
				}
				else { //multiplas contas a pagar / arquivos
					
					if(cdContaPadrao<=0)
						return new Result(-1, "Conta Padrão para DAJ não está configurada.");
					
					if(cdTipoDocumentoPadrao<=0)
						return new Result(-1, "Tipo de Documento Padrão para DAJ não está configurado.");
					
					if(cdTipoArquivoPadrao<=0)
						return new Result(-1, "Tipo de Arquivo Padrão para DAJ não está configurado.");
					
					
					retorno = saveContasProcessoFinanceiro(processoFinanceiro, cdEmpresa, rsmContas, connect).getCode();
				}
				
				if(retorno >0 && reembolsavel){
					ProcessoFinanceiro reembolso = (ProcessoFinanceiro)processoFinanceiro.clone();
					reembolso.setCdEventoFinanceiro(0);
					if(dtReembolso!=null)
						reembolso.setDtEventoFinanceiro(dtReembolso);
					reembolso.setTpNaturezaEvento(TP_NATUREZA_RECEITA);
					reembolso.setCdEventoFinanceiroOrigem(processoFinanceiro.getCdEventoFinanceiro());
					
					ResultSetMap rsmCliente = ProcessoServices.getParteCliente(processoFinanceiro.getCdProcesso(), connect);
					if(rsmCliente.next())
						reembolso.setCdPessoa(rsmCliente.getInt("cd_pessoa"));
					
					retorno = ProcessoFinanceiroDAO.insert(reembolso, connect);
					reembolso.setCdEventoFinanceiro(retorno);
					ComplianceManager.process(reembolso, authData, ComplianceManager.TP_ACAO_INSERT, connect);
				}
			}
			

			//COMPLIANCE
			ComplianceManager.process(processoFinanceiro, authData, ComplianceManager.TP_ACAO_INSERT, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			

			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...");
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
	
	/**
	 * Gera processos financeiros e suas contas a pagar associadas baseado em um processo financeiro e um conjunto de valores de contas, categorias e arquivos.
	 * @param processoFinanceiro
	 * @param cdEmpresa
	 * @param rsmContas
	 * @return
	 */
	public static Result saveContasProcessoFinanceiro(ProcessoFinanceiro processoFinanceiro, int cdEmpresa, ResultSetMap rsmContas) {
		return saveContasProcessoFinanceiro(processoFinanceiro, cdEmpresa, rsmContas, null);
	}
	
	public static Result saveContasProcessoFinanceiro(ProcessoFinanceiro processoFinanceiro, int cdEmpresa, ResultSetMap rsmContas, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(processoFinanceiro==null)
				return new Result(-1, "Erro ao salvar. Processo financeiro é nulo");
			
			int cdContaPadrao = ParametroServices.getValorOfParametroAsInteger("CD_CONTA_PADRAO_DAJ", 0);
			int cdTipoDocumentoPadrao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_DAJ", 0);
			int cdTipoArquivoPadrao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ARQUIVO_DAJ", 0);
			
			int retorno = 0;
			
			int nrParcela = 1;
			while(rsmContas.next()){
				
				ProcessoFinanceiro p = (ProcessoFinanceiro)processoFinanceiro.clone();
				p.setVlEventoFinanceiro(rsmContas.getDouble("VL_CONTA"));
				
				if(rsmContas.getObject("DT_VENCIMENTO") instanceof Date)
					rsmContas.setValueToField("DT_VENCIMENTO", Util.dateToTimestamp((Date)rsmContas.getObject("DT_VENCIMENTO")));
				
				if(rsmContas.getObject("DT_EMISSAO") instanceof Date)
					rsmContas.setValueToField("DT_EMISSAO", Util.dateToTimestamp((Date)rsmContas.getObject("DT_EMISSAO")));
				
				//adicionar conta pagar
				ContaPagar conta = new ContaPagar(0, 0, p.getCdPessoa(), 
						cdEmpresa, 
						0, //cdContaOrigem, 
						0, //cdDocumentoEntrada, 
						cdContaPadrao, 
						0, //cdContaBancaria, 
						rsmContas.getGregorianCalendar("DT_VENCIMENTO"), 
						rsmContas.getGregorianCalendar("DT_EMISSAO"),
						null, 
						new GregorianCalendar(), //dtAutorizacao 
						rsmContas.getString("NR_DOCUMENTO"), 
						nrParcela + "/" + rsmContas.size(), 
						nrParcela, 
						cdTipoDocumentoPadrao, //rsmContas.getInt("CD_TIPO_DOCUMENTO"), 
						rsmContas.getFloat("VL_CONTA"), 
						0, //vlAbatimento, 
						0, //vlAcrescimo, 
						0, //vlPago, 
						rsmContas.getString("DS_HISTORICO"), 
						0, //stConta, 
						1, //lgAutorizado, 
						0, //tpFrequencia, 
						1, //qtParcelas, 
						0, //vlBaseAutorizacao, 
						0, //cdViagem, 
						0, //cdManutencao, 
						null, //txtObservacao, 
						new GregorianCalendar(), //dtDigitacao, 
						rsmContas.getGregorianCalendar("DT_VENCIMENTO"),
						0 /*cdTurno*/);
				
				
				ArrayList<ContaPagarCategoria> categorias = new ArrayList<ContaPagarCategoria>();
				
				int cdCentroCusto = rsmContas.getInt("CD_CENTRO_CUSTO");
				
				if (p.getCdProdutoServico() > 0) {
					ProdutoServico produto = ProdutoServicoDAO.get(p.getCdProdutoServico(), connect);
					categorias.add(new ContaPagarCategoria(0, 
											produto.getCdCategoriaDespesa(), 
											conta.getVlConta().floatValue() - conta.getVlAbatimento().floatValue() + conta.getVlAcrescimo().floatValue(), 
											cdCentroCusto));
				}
				
				LogUtils.debug("conta: "+conta);
				LogUtils.debug("categorias"+categorias);
				
				Result result = ContaPagarServices.insert(conta, true, false, categorias, connect);
				retorno = result.getCode();
				conta.setCdContaPagar(result.getCode());
				
				if(retorno<=0)
					break;
				
				//arquivo
				if(rsmContas.getObject("BLB_ARQUIVO")!=null){
					Arquivo arquivo = new Arquivo(0, 
							rsmContas.getString("NM_ARQUIVO"), //nmArquivo,
							new GregorianCalendar(), //dtArquivamento,
							rsmContas.getString("NM_ARQUIVO"), //nmDocumento, 
							(byte[])rsmContas.getObject("BLB_ARQUIVO"), //blbArquivo,
							cdTipoArquivoPadrao,//cdTipoArquivo
							null/*dtCriacao*/, 0/*cdUsuario*/,0/*stArquivo*/, 
							null /*dtInicial*/, null/*dtFinal*/, 0/*cdAssinatura*/, null/*txtOcr*/, 0, null, null); 
					
					retorno = ArquivoDAO.insert(arquivo, connect);
					
					if(retorno > 0) {
						arquivo.setCdArquivo(retorno);
						ContaPagarArquivo contaPagarArquivo = new ContaPagarArquivo();
						contaPagarArquivo.setCdArquivo(arquivo.getCdArquivo());
						contaPagarArquivo.setCdContaPagar(conta.getCdContaPagar());
						ContaPagarArquivoServices.save(contaPagarArquivo, arquivo, connect);
						
					}
					else
						break;
				}
				
				p.setCdContaPagar(conta.getCdContaPagar());
				retorno = save(p, connect).getCode();
				
				if(retorno<=0)
					break;
					
				
				nrParcela++;
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao lançar contas...":"Contas lançadas com sucesso...");

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

	public static Result remove(int cdEventoFinanceiro, int cdProcesso){
		return remove(cdEventoFinanceiro, cdProcesso, false, null, null);
	}
	
	public static Result remove(int cdEventoFinanceiro, int cdProcesso, boolean cascade){
		return remove(cdEventoFinanceiro, cdProcesso, cascade, null, null);
	}
	
	public static Result remove(int cdEventoFinanceiro, int cdProcesso, boolean cascade, Connection connect){
		return remove(cdEventoFinanceiro, cdProcesso, cascade, null, null);
	}
	
	public static Result remove(int cdEventoFinanceiro, int cdProcesso, boolean cascade, AuthData authData){
		return remove(cdEventoFinanceiro, cdProcesso, cascade, authData, null);
	}
		
	public static Result remove(int cdEventoFinanceiro, int cdProcesso, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ProcessoFinanceiro processoFinanceiro = ProcessoFinanceiroDAO.get(cdEventoFinanceiro, cdProcesso, connect);
			
			int retorno = 0;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
				
			if(!cascade || retorno>0)
				retorno = ProcessoFinanceiroDAO.delete(cdEventoFinanceiro, cdProcesso, connect);
			

			
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este processo financeiro está vinculado a outros registros e não pode ser excluído!");
			}
			
			ComplianceManager.process(processoFinanceiro, authData, ComplianceManager.TP_ACAO_DELETE, connect);
			
			if (isConnectionNull)
				connect.commit();
			
			
			return new Result(1, "Processo financeiro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir processo financeiro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Remove lista de processos financeiros.
	 * 
	 * @param processosFinanceiros ArrayList<Integer> lista de cdEventoFinanceiro
	 * @param cdProcesso processo ao qual os eventos pertencem
	 * @return Result o resultado da operação
	 * @since 26/02/2015
	 * @author Maurício
	 */
	public static Result removeProcessosFinanceiros(ArrayList<Integer> processosFinanceiros, int cdProcesso) {
		return removeProcessosFinanceiros(processosFinanceiros, cdProcesso, false, null);
	}
	
	public static Result removeProcessosFinanceiros(ArrayList<Integer> processosFinanceiros, int cdProcesso, boolean cascade) {
		return removeProcessosFinanceiros(processosFinanceiros, cdProcesso, cascade, null);
	}
	
	public static Result removeProcessosFinanceiros(ArrayList<Integer> processosFinanceiros, int cdProcesso, boolean cascade, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			for (Integer cdProcessoFinanceiro : processosFinanceiros) {
				Result r = remove(cdProcessoFinanceiro, cdProcesso, cascade, connect);
				
				if(r.getCode()<=0) {
					Conexao.rollback(connect);
					System.out.println("ProcessoFinanceiroServices.removeProcessosFinanceiros: "+r.getMessage());
					return new Result(-2, "Algum processo financeiro está vinculado a outros registros e não pode ser excluído!");
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Processos financeiros excluídos com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir processo financeiro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Remove lista de processos financeiros.
	 * 
	 * @param processosFinanceiros ArrayList<ProcessoFinanceiro> lista de eventos financeiros
	 * @return Result o resultado da operação
	 * @since 17/05/2016
	 * @author Sapucaia
	 */
	public static Result removeProcessosFinanceiros(ArrayList<ProcessoFinanceiro> processosFinanceiros) {
		return removeProcessosFinanceiros(processosFinanceiros, false, null, null);
	}
	
	public static Result removeProcessosFinanceiros(ArrayList<ProcessoFinanceiro> processosFinanceiros, AuthData authData) {
		return removeProcessosFinanceiros(processosFinanceiros, false, authData, null);
	}
	
	public static Result removeProcessosFinanceiros(ArrayList<ProcessoFinanceiro> processosFinanceiros, boolean cascade) {
		return removeProcessosFinanceiros(processosFinanceiros, cascade, null, null);
	}
	
	public static Result removeProcessosFinanceiros(ArrayList<ProcessoFinanceiro> processosFinanceiros, boolean cascade, AuthData authData) {
		return removeProcessosFinanceiros(processosFinanceiros, cascade, authData, null);
	}
	
	public static Result removeProcessosFinanceiros(ArrayList<ProcessoFinanceiro> processosFinanceiros, boolean cascade, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			for (ProcessoFinanceiro evento : processosFinanceiros) {
				Result r = remove(evento.getCdEventoFinanceiro(), evento.getCdProcesso(), cascade, authData, connect);
				
				if(r.getCode()<=0) {
					Conexao.rollback(connect);
					System.out.println("ProcessoFinanceiroServices.removeProcessosFinanceiros: "+r.getMessage());
					return new Result(-2, "Algum processo financeiro está vinculado a outros registros e não pode ser excluído!");
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Processos financeiros excluídos com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir processo financeiro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorna um evento fincanceiro associado à conta a receber indicada.
	 * @param cdContaReceber
	 * @return
	 * @author Sapucaia
	 * @since 30/01/2017
	 */
	public static ArrayList<ProcessoFinanceiro> getByContaReceber(int cdContaReceber) {
		return getByContaReceber(cdContaReceber, null);
	}

	public static ArrayList<ProcessoFinanceiro> getByContaReceber(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_PROCESSO_FINANCEIRO WHERE CD_CONTA_RECEBER=?");
			pstmt.setInt(1, cdContaReceber);
			rs = pstmt.executeQuery();
			ArrayList<ProcessoFinanceiro> processos = new ArrayList<ProcessoFinanceiro>();
			while(rs.next()){
				processos.add(
						new ProcessoFinanceiro(rs.getInt("CD_PROCESSO"),
								rs.getInt("CD_EVENTO_FINANCEIRO"),
								rs.getInt("CD_PRODUTO_SERVICO"),
								rs.getInt("CD_ANDAMENTO"),
								rs.getInt("CD_PESSOA"),
								rs.getInt("TP_NATUREZA_EVENTO"),
								(rs.getTimestamp("DT_EVENTO_FINANCEIRO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_EVENTO_FINANCEIRO").getTime()),
								rs.getDouble("VL_EVENTO_FINANCEIRO"),
								(rs.getTimestamp("DT_LANCAMENTO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_LANCAMENTO").getTime()),
								rs.getInt("CD_CONTA_PAGAR"),
								rs.getInt("CD_CONTA_RECEBER"),
								rs.getInt("CD_USUARIO"),
								rs.getInt("CD_ARQUIVO"),
								(rs.getTimestamp("DT_REVISAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_REVISAO").getTime()),
								rs.getInt("CD_USUARIO_REVISAO"),
								rs.getInt("TP_SEGMENTO"),
								rs.getInt("TP_INSTANCIA"),
								rs.getInt("CD_ESTADO"),
								rs.getInt("CD_AGENDA_ITEM"),
								rs.getInt("CD_REGRA_FATURAMENTO"),
								rs.getInt("CD_EVENTO_FINANCEIRO_ORIGEM"),
								rs.getString("NR_REFERENCIA"))
					);
				
			}
			
			if( processos.size() > 0 ){
				return processos;
			}else
				return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroServices.getByContaPagar: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroServices.getByContaPagar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorna um evento fincanceiro associado à conta a pagar indicada.
	 * @param cdContaPagar
	 * @return
	 * @author Sapucaia
	 * @since 30/01/2017
	 */
	public static ArrayList<ProcessoFinanceiro> getByContaPagar(int cdContaPagar) {
		return getByContaPagar(cdContaPagar, null);
	}

	public static ArrayList<ProcessoFinanceiro> getByContaPagar(int cdContaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_PROCESSO_FINANCEIRO WHERE CD_CONTA_PAGAR=?");
			pstmt.setInt(1, cdContaPagar);
			rs = pstmt.executeQuery();
			ArrayList<ProcessoFinanceiro> processos = new ArrayList<ProcessoFinanceiro>();
			
			while(rs.next()){
				processos.add(
					new ProcessoFinanceiro(rs.getInt("CD_PROCESSO"),
							rs.getInt("CD_EVENTO_FINANCEIRO"),
							rs.getInt("CD_PRODUTO_SERVICO"),
							rs.getInt("CD_ANDAMENTO"),
							rs.getInt("CD_PESSOA"),
							rs.getInt("TP_NATUREZA_EVENTO"),
							(rs.getTimestamp("DT_EVENTO_FINANCEIRO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_EVENTO_FINANCEIRO").getTime()),
							rs.getDouble("VL_EVENTO_FINANCEIRO"),
							(rs.getTimestamp("DT_LANCAMENTO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_LANCAMENTO").getTime()),
							rs.getInt("CD_CONTA_PAGAR"),
							rs.getInt("CD_CONTA_RECEBER"),
							rs.getInt("CD_USUARIO"),
							rs.getInt("CD_ARQUIVO"),
							(rs.getTimestamp("DT_REVISAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_REVISAO").getTime()),
							rs.getInt("CD_USUARIO_REVISAO"),
							rs.getInt("TP_SEGMENTO"),
							rs.getInt("TP_INSTANCIA"),
							rs.getInt("CD_ESTADO"),
							rs.getInt("CD_AGENDA_ITEM"),
							rs.getInt("CD_REGRA_FATURAMENTO"),
							rs.getInt("CD_EVENTO_FINANCEIRO_ORIGEM"),
							rs.getString("NR_REFERENCIA"))
				);
				
			}
			
			if( processos.size() > 0 ){
				return processos;
			}else
				return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroServices.getByContaPagar: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoFinanceiroServices.getByContaPagar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_financeiro ORDER BY dt_evento_financeiro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<String, Object> getParametrosDAJ() {
		HashMap<String, Object> reg = new HashMap<String, Object>();
		reg.put("CD_CONTA_PADRAO_DAJ", ParametroServices.getValorOfParametroAsInteger("CD_CONTA_PADRAO_DAJ", 0));
		reg.put("CD_TIPO_DOCUMENTO_DAJ", ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_DAJ", 0));
		reg.put("CD_SERVICO_PADRAO_DAJ", ParametroServices.getValorOfParametroAsInteger("CD_SERVICO_PADRAO_DAJ", 0));
		reg.put("CD_TIPO_ARQUIVO_DAJ", ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ARQUIVO_DAJ", 0));
		
		return reg;
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try	{
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			int cdCliente = 0;
			
			int cdPessoa = 0;
			
			String dtEmissaoInicial = null;
			String dtEmissaoFinal = null;
			String dtVencimentoInicial = null;
			String dtVencimentoFinal = null;
			String dtQuitacaoInicial = null;
			String dtQuitacaoFinal = null;
			
			int tpNatureza = -1;
			
			boolean lgReembolsaveis = false;
			String sqlNrProcesso = "";
			
			for(int i=0; i<criterios.size(); i++)	{
				if(criterios.get(i).getColumn().indexOf("CD_CLIENTE")>=0)	{
					cdCliente = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
				}
				else if(criterios.get(i).getColumn().indexOf("CD_PESSOA")>=0)	{
					cdPessoa = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
				}
				else if(criterios.get(i).getColumn().indexOf("TP_NATUREZA_EVENTO")>=0)	{
					tpNatureza = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("dtEmissaoInicial")) {
					dtEmissaoInicial = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("dtEmissaoFinal")) {
					dtEmissaoFinal = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("dtVencimentoInicial")) {
					dtVencimentoInicial = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("dtVencimentoFinal")) {
					dtVencimentoFinal = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("dtQuitacaoInicial")) {
					dtQuitacaoInicial = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("dtQuitacaoFinal")) {
					dtQuitacaoFinal = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgReembolsaveis")) {
					lgReembolsaveis = true;
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("B.nr_processo"))	{
					String nrProcesso = criterios.get(i).getValue().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
					sqlNrProcesso += " AND (REPLACE(REPLACE(REPLACE(B.nr_processo, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrProcesso +"%\') ";
					criterios.remove(i);
					i--;
				}
			}
			
			LogUtils.debug("ProcessoFinanceiroServices.find");
						
			String sql = "SELECT A.*, B.nr_processo, B.tp_sentenca, B.st_processo, B.nm_conteiner3 as nm_pasta, B.dt_repasse," + 
						 "		 B1.nm_tipo_processo, B2.nm_cidade, B22.sg_estado, B3.dt_realizacao, B3.dt_final, B3.lg_preposto, B4.nm_tipo_prazo, "+ 
						 "		 B5.nm_grupo AS nm_grupo_trabalho, B6.nm_grupo_processo, C.nm_produto_servico, "+
                         "       D.dt_andamento, D1.nm_tipo_andamento, E.nm_pessoa, E1.cd_orgao, E1.tp_contratacao, F.nm_login, "+
                         "       G.nr_documento AS nr_documento_receber, G.dt_vencimento AS dt_vencimento_receber, G.dt_emissao AS dt_emissao_receber, "+
                         "       G.dt_recebimento, G.vl_conta AS vl_conta_receber, G.st_conta AS st_conta_receber, G.ds_historico AS ds_historico_receber, "+
                         "       H.nr_documento AS nr_documento_pagar, H.dt_vencimento AS dt_vencimento_pagar, H.dt_emissao AS dt_emissao_pagar, "+
                         "       H.dt_pagamento, H.vl_conta AS vl_conta_pagar, H.st_conta AS st_conta_pagar, H.cd_arquivo AS cd_arquivo_pagar, H.ds_historico AS ds_historico_pagar,"+
                         (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
                        		 //Firebird
                        		 "       (SELECT nm_cliente FROM get_clientes(A.cd_processo)) AS nm_cliente, "+
                                 "       (SELECT * FROM get_contra_parte(A.cd_processo)) AS nm_parte_contraria, "  : 
                                 //Postgre
                                 "       (SELECT nm_cliente FROM get_clientes(A.cd_processo) AS nm_cliente), "+
                                 "       (SELECT nm_parte_contraria FROM get_contra_parte(A.cd_processo) AS nm_parte_contraria), ")+
                         "		 I1.cd_categoria_economica as cd_categoria_receita, I1.nm_categoria_economica as nm_categoria_receita, "+
                         "       I1.nr_categoria_economica as nr_categoria_receita, I1.tp_categoria_economica as tp_categoria_receita, "+
                         "		 I2.cd_categoria_economica as cd_categoria_despesa, I2.nm_categoria_economica as nm_categoria_despesa, "+
                         "       I2.nr_categoria_economica as nr_categoria_despesa, I2.tp_categoria_economica as tp_categoria_despesa, "+
                         "		 J.nm_regra_faturamento,  "+
                         "(select count(*) from adm_conta_receber_arquivo G1 where A.cd_conta_receber = G1.cd_conta_receber)  as QT_ARQUIVOS_CONTA_RECEBER, "+
                         "(select count(*) from adm_conta_pagar_arquivo H1 where A.cd_conta_pagar = H1.cd_conta_pagar) as QT_ARQUIVOS_CONTA_PAGAR "+
                         "FROM prc_processo_financeiro A "+
                         "           JOIN prc_processo           B  ON (A.cd_processo = B.cd_processo) "+
                         "LEFT OUTER JOIN prc_tipo_processo      B1 ON (B.cd_tipo_processo = B1.cd_tipo_processo) "+
                         "LEFT OUTER JOIN grl_cidade      	     B2 ON (B.cd_cidade = B2.cd_cidade) "+
                         "LEFT OUTER JOIN grl_estado			 B22 ON (B2.cd_estado = B22.cd_estado)"+
                         "LEFT OUTER JOIN agd_agenda_item      	 B3 ON (B.cd_processo = B3.cd_processo AND A.cd_agenda_item = B3.cd_agenda_item) "+
                         "LEFT OUTER JOIN prc_tipo_prazo      	 B4 ON (B3.cd_tipo_prazo = B4.cd_tipo_prazo) "+
                         "LEFT OUTER JOIN agd_grupo				 B5 ON (B.cd_grupo_trabalho = B5.cd_grupo) "+
                         "LEFT OUTER JOIN prc_grupo_processo 	 B6 ON (B.cd_grupo_processo = B6.cd_grupo_processo) "+
                         "           JOIN grl_produto_servico    C  ON (A.cd_produto_servico = C.cd_produto_servico) "+
                         "LEFT OUTER JOIN prc_processo_andamento D  ON (A.cd_processo = D.cd_processo "+
                         "                                          AND A.cd_andamento = D.cd_andamento) "+
                         "LEFT OUTER JOIN prc_tipo_andamento     D1 ON (D.cd_tipo_andamento = D1.cd_tipo_andamento) "+
                         "LEFT OUTER JOIN grl_pessoa             E  ON (A.cd_pessoa = E.cd_pessoa) "+
                         "LEFT OUTER JOIN prc_orgao             E1  ON (A.cd_pessoa = E1.cd_pessoa) "+
                         "LEFT OUTER JOIN seg_usuario            F  ON (A.cd_usuario = F.cd_usuario) "+
                         "LEFT OUTER JOIN adm_conta_receber      G  ON (A.cd_conta_receber = G.cd_conta_receber) "+
                         //"LEFT OUTER JOIN adm_conta_receber_arquivo G1 ON (A.cd_conta_receber = G1.cd_conta_receber)"+
                         "LEFT OUTER JOIN adm_conta_pagar        H  ON (A.cd_conta_pagar = H.cd_conta_pagar) "+
                         //"LEFT OUTER JOIN adm_conta_pagar_arquivo H1 ON (A.cd_conta_pagar = H1.cd_conta_pagar)"+
                         "LEFT OUTER JOIN adm_categoria_economica I1 ON (C.cd_categoria_receita = I1.cd_categoria_economica) "+
                         "LEFT OUTER JOIN adm_categoria_economica I2 ON (C.cd_categoria_despesa = I2.cd_categoria_economica) "+
                         "LEFT OUTER JOIN prc_regra_faturamento   J ON (A.cd_regra_faturamento = J.cd_regra_faturamento) "+
                         (!lgReembolsaveis?"LEFT OUTER":"")+" JOIN prc_processo_financeiro K ON (A.cd_evento_financeiro_origem = K.cd_evento_financeiro AND"+
                         "	 											A.cd_processo = K.cd_processo) "+
                         "LEFT OUTER JOIN adm_conta_receber		  K1 ON (K.cd_conta_receber = K1.cd_conta_receber) "+
                         "LEFT OUTER JOIN adm_conta_pagar		  K2 ON (K.cd_conta_pagar = K2.cd_conta_pagar) "+
                         "WHERE 1=1 " +
                         (cdCliente>0 ? " AND EXISTS (SELECT * FROM prc_parte_cliente PC WHERE A.cd_processo = PC.cd_processo AND PC.cd_pessoa = "+cdCliente+")" : "") +
                         (cdPessoa>0 ? " AND (B3.cd_pessoa="+cdPessoa+" OR A.cd_pessoa="+cdPessoa+")" : "")+
                         sqlNrProcesso;
			
			//DATAS DE CONTA PAGAR/RCEBER
			String joinAliasDespesa = (lgReembolsaveis?"K2":"H");
			String joinAliasReceita = (lgReembolsaveis?"K1":"G");
			if(tpNatureza==TP_NATUREZA_DESPESA) {
                sql += (dtEmissaoInicial!=null ? " AND "+joinAliasDespesa+".dt_emissao>='"+dtEmissaoInicial.replaceAll("/", ".")+"'" : "")
                	+ (dtEmissaoFinal!=null ? " AND "+joinAliasDespesa+".dt_emissao<='"+dtEmissaoFinal.replaceAll("/", ".")+"'" : "")
                	+ (dtVencimentoInicial!=null ? " AND "+joinAliasDespesa+".dt_vencimento>='"+dtVencimentoInicial.replaceAll("/", ".")+"'" : "")
                	+ (dtVencimentoFinal!=null ? " AND "+joinAliasDespesa+".dt_vencimento<='"+dtVencimentoFinal.replaceAll("/", ".")+"'" : "")
                	+ (dtQuitacaoInicial!=null ? " AND "+joinAliasDespesa+".dt_pagamento>='"+dtQuitacaoInicial.replaceAll("/", ".")+"'" : "")
                	+ (dtQuitacaoFinal!=null ? " AND "+joinAliasDespesa+".dt_pagamento<='"+dtQuitacaoFinal.replaceAll("/", ".")+"'" : "");
			}
			else if(tpNatureza==TP_NATUREZA_RECEITA) {
				sql += (dtEmissaoInicial!=null ? " AND "+joinAliasReceita+".dt_emissao>='"+dtEmissaoInicial.replaceAll("/", ".")+"'" : "")
					+ (dtEmissaoFinal!=null ? " AND "+joinAliasReceita+".dt_emissao<='"+dtEmissaoFinal.replaceAll("/", ".")+"'" : "")
					+ (dtVencimentoInicial!=null ? " AND "+joinAliasReceita+".dt_vencimento>='"+dtVencimentoInicial.replaceAll("/", ".")+"'" : "")
					+ (dtVencimentoFinal!=null ? " AND "+joinAliasReceita+".dt_vencimento<='"+dtVencimentoFinal.replaceAll("/", ".")+"'" : "")
					+ (dtQuitacaoInicial!=null ? " AND "+joinAliasReceita+".dt_recebimento>='"+dtQuitacaoInicial.replaceAll("/", ".")+"'" : "")
					+ (dtQuitacaoFinal!=null ? " AND "+joinAliasReceita+".dt_recebimento<='"+dtQuitacaoFinal.replaceAll("/", ".")+"'" : "");
			}
			
			
//			String[] orderByColumns = new String[] {"A.dt_lancamento"};
//			LogUtils.debug("SQL:\n\n\n"+Search.getStatementSQL(sql, orderByColumns, criterios, true)+"\n\n\n");
			String[] orderByColumns = new String[] {"A.cd_evento_financeiro"};
			
			ResultSetMap rsm = Search.find(sql, orderByColumns, criterios, Conexao.conectar(), false);
			while(rsm.next()) {
				ResultSetMap rsmSentenca = ProcessoSentencaServices.getAllByProcesso(rsm.getInt("CD_PROCESSO"));
				if(rsmSentenca.next()) {
					rsm.setValueToField("cd_sentenca", rsmSentenca.getInt("cd_sentenca"));
					rsm.setValueToField("tp_sentenca", rsmSentenca.getInt("tp_sentenca"));
					rsm.setValueToField("dt_sentenca", rsmSentenca.getGregorianCalendar("dt_sentenca"));
					rsm.setValueToField("vl_sentenca", rsmSentenca.getDouble("vl_sentenca"));
					rsm.setValueToField("vl_acordo", rsmSentenca.getDouble("vl_acordo"));
				}
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}

	}

	/**
	 * REALIZA A REVISAO DE FATURAS
	 * @param itens
	 * @param cdUsuario
	 * @return
	 */
	public static Result revisarFaturas(ArrayList<ProcessoFinanceiro> itens, int cdUsuario){
		return revisarFaturas(itens, cdUsuario, null);
	}
	
	public static Result revisarFaturas(ArrayList<ProcessoFinanceiro> itens, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			for (ProcessoFinanceiro processoFinanceiro : itens) {
				retorno = revisar(processoFinanceiro.getCdEventoFinanceiro(), processoFinanceiro.getCdProcesso(), cdUsuario, connect).getCode();
				if(retorno<=0)
					break;
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Ocorreram erros ao revisar o(s) evento(s) financeiro(s)!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Evento(s) financeiro(s) revisado(s) com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao revisar evento(s) financeiro(s)!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result revisar(int cdEventoFinanceiro, int cdProcesso, int cdUsuario){
		return revisar(cdEventoFinanceiro, cdProcesso, cdUsuario, null);
	}
	
	public static Result revisar(int cdEventoFinanceiro, int cdProcesso, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_PROCESSO_FINANCEIRO SET DT_REVISAO = ?, CD_USUARIO_REVISAO = ? " +
					"WHERE CD_EVENTO_FINANCEIRO=? AND CD_PROCESSO=?");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(new GregorianCalendar()));
			pstmt.setInt(2, cdUsuario);
			pstmt.setInt(3, cdEventoFinanceiro);
			pstmt.setInt(4, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Ocorreram erros ao revisar o evento financeiro!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Evento financeiro revisado com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao revisar evento financeiro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * CANCELA A REVISAO DE FATURAS
	 * @param itens
	 * @param cdUsuario
	 * @return
	 */
	public static Result cancelarRevisaoFaturas(ArrayList<ProcessoFinanceiro> itens, int cdUsuario){
		return cancelarRevisaoFaturas(itens, cdUsuario, null);
	}
	
	public static Result cancelarRevisaoFaturas(ArrayList<ProcessoFinanceiro> itens, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			for (ProcessoFinanceiro processoFinanceiro : itens) {
				retorno = cancelarRevisao(processoFinanceiro.getCdEventoFinanceiro(), processoFinanceiro.getCdProcesso(), cdUsuario, connect).getCode();
				if(retorno<=0)
					break;
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Ocorreram erros ao cancelar a revisão de evento(s) financeiro(s)!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Revisão de evento(s) financeiro(s) cancelada com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao cancelar a revisão de evento(s) financeiro(s)!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result cancelarRevisao(int cdEventoFinanceiro, int cdProcesso, int cdUsuario){
		return cancelarRevisao(cdEventoFinanceiro, cdProcesso, cdUsuario, null);
	}
	
	public static Result cancelarRevisao(int cdEventoFinanceiro, int cdProcesso, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_PROCESSO_FINANCEIRO SET DT_REVISAO = ?, CD_USUARIO_REVISAO = ? " +
					"WHERE CD_EVENTO_FINANCEIRO=? AND CD_PROCESSO=?");
			pstmt.setNull(1, Types.TIMESTAMP);
			pstmt.setNull(2, Types.INTEGER);
			pstmt.setInt(3, cdEventoFinanceiro);
			pstmt.setInt(4, cdProcesso);
			
			int retorno = pstmt.executeUpdate();
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Ocorreram erros ao cancelar a revisão do evento financeiro!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Cancelamento de revisão do Evento financeiro realizada com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao cancelar a revisão de evento financeiro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Realiza um cancelamento de Conta a Pagar, cancelando a revisão e faturamento do evento financeiro associado.
	 * @param cdContaPagar
	 * @return
	 * @author Sapucaia
	 * @since 30/01/2017
	 */
	public static Result cancelarFaturamentoContaPagar(int cdContaPagar){
		return cancelarFaturamentoContaPagar(cdContaPagar, null);
	}
	
	public static Result cancelarFaturamentoContaPagar(int cdContaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ContaPagar conta = ContaPagarDAO.get(cdContaPagar, connect);
			if(conta==null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Conta indicada não existe.");
			}
			
			ArrayList<ProcessoFinanceiro> eventos = ProcessoFinanceiroServices.getByContaPagar(cdContaPagar, connect);
			if(eventos==null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Evento financeiro não existe para a conta indicada.");
			}
			
			Result r = new Result(-1, "Falha ao atualizar eventos financeiros");
			Iterator<ProcessoFinanceiro> eventoIterator = eventos.iterator();
			while (eventoIterator.hasNext()) {
				ProcessoFinanceiro evento = (ProcessoFinanceiro) eventoIterator.next();
				evento.setCdContaPagar(0);
				evento.setDtRevisao(null);
				evento.setCdUsuarioRevisao(0);
				
				r = ProcessoFinanceiroServices.save(evento, connect);
				if( r.getCode() <= 0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					r.setMessage("Erro ao atualizar Processo Financeiro");
					return r;
				}
			}
			
			if(r.getCode()<=0){
				Conexao.rollback(connect);
				return new Result(r.getCode(), r.getMessage());
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Cancelamento de faturamento de conta a pagar realizada com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao cancelar o faturamento de conta a pagar!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	/**
	 * Realiza um cancelamento de Conta a Receber, cancelando a revisão e faturamento do evento financeiro associado.
	 * @param cdContaReceber
	 * @return
	 * @author Sapucaia
	 * @since 30/01/2017
	 */
	public static Result cancelarFaturamentoContaReceber(int cdContaReceber){
		return cancelarFaturamentoContaPagar(cdContaReceber, null);
	}
	
	public static Result cancelarFaturamentoContaReceber(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ContaReceber conta = ContaReceberDAO.get(cdContaReceber, connect);
			if(conta==null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Conta indicada não existe.");
			}
			
			ArrayList<ProcessoFinanceiro> eventos = ProcessoFinanceiroServices.getByContaReceber(cdContaReceber, connect);
			if(eventos==null) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Evento financeiro não existe para a conta indicada.");
			}
			
			Result r = new Result(-1, "Falha ao atualizar eventos financeiros");
			Iterator<ProcessoFinanceiro> eventoIterator = eventos.iterator();
			while (eventoIterator.hasNext()) {
				ProcessoFinanceiro evento = (ProcessoFinanceiro) eventoIterator.next();
				evento.setCdContaReceber(0);
				evento.setDtRevisao(null);
				evento.setCdUsuarioRevisao(0);
				
				r = ProcessoFinanceiroServices.save(evento, connect);
				if( r.getCode() <= 0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					r.setMessage("Erro ao atualizar Processo Financeiro");
					return r;
				}
			}
			
			if(r.getCode()<=0){
				Conexao.rollback(connect);
				return new Result(r.getCode(), r.getMessage());
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Cancelamento de faturamento de conta a receber realizada com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao cancelar o faturamento de conta a receber!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Gera faturas para os processos financeiros indicados. Permite incluir arquivos para as contas geradas.
	 * @param itens
	 * @param tpNaturezaEvento
	 * @param cdEmpresa
	 * @param cdUsuario
	 * @param cdTipoDocumento
	 * @param nrDocumento
	 * @param cdPessoa
	 * @param dtVencimento
	 * @param dsHistorico
	 * @param cats
	 * @param stConta
	 * @param vlDesc
	 * @param cdConta
	 * @param arquivos
	 * @param dtEmissao
	 * @return
	 */
	public static Result gerarFaturas(ArrayList<ProcessoFinanceiro> itens, int tpNaturezaEvento, int cdEmpresa, int cdUsuario, int cdTipoDocumento, 
									  String nrDocumento, int cdPessoa, GregorianCalendar dtVencimento, String dsHistorico, 
									  ArrayList<HashMap<String, Object>> cats, int stConta, Double vlDesc, int cdConta,
									  ArrayList<Arquivo> arquivos, GregorianCalendar dtEmissao, AuthData authData) {
		Connection connect = Conexao.conectar();
		try {
			connect = Conexao.conectar();
			connect.setAutoCommit(false);
			
			if(cdPessoa<=0) {
				Conexao.rollback(connect);
				return new Result(-1, "Você deve informar o favorecido!");
			}
			
			// Buscando Conta Financeira (Caixa / Bancária/0
			Double vlConta = 0.0;
			if(cdConta<=0) {
				ResultSetMap rsmContas = ContaFinanceiraServices.getContas(cdEmpresa);
				if(rsmContas.next())
					cdConta = rsmContas.getInt("cd_conta");
			}
			
			//conta do sacado/favorecido
			int cdContaSacado = 0;
			ArrayList<ItemComparator> criterios = new ArrayList<>();
			criterios.add(new ItemComparator("A.CD_PESSOA", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.LG_PRINCIPAL", Integer.toString(1), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmContaFav = PessoaContaBancariaServices.find(criterios, connect);
			if(rsmContaFav.next()) {
				cdContaSacado = rsmContaFav.getInt("CD_CONTA_BANCARIA");
			}
			
			// RECEITA
			if(tpNaturezaEvento == 0)	{
				
				// Buscando categoria economica
				int cdCategoriaDefault = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT", 0, cdEmpresa);
				if(cdCategoriaDefault <= 0)	{
					ResultSetMap rsmCats = CategoriaEconomicaServices.getAllCategoriaReceita();
					if(rsmCats.next())
						cdCategoriaDefault = rsmCats.getInt("cd_categoria_economica"); 
				}
				
				// Obtem a conta carteira
				int cdContaCarteira = 0;
				ResultSetMap rsmCc = ContaCarteiraServices.getCarteiraOfConta(cdConta, connect);
				if( rsmCc.next() )
					cdContaCarteira = rsmCc.getInt("CD_CONTA_CARTEIRA");
				
				//CONTA A RECEBER
				ContaReceber contaReceber = new ContaReceber(0 /*cdContaReceber*/, cdPessoa, cdEmpresa,
						 0 /*cdContrato*/, 0 /*cdContaOrigem*/, 0 /*cdDocumentoSaida*/,
						 cdContaCarteira /*cdContaCarteira*/, cdConta, 0 /*cdFrete*/,
						 nrDocumento, "" /*idContaReceber*/, 0 /*nrParcela*/,
						 "" /*nrReferencia*/, cdTipoDocumento, dsHistorico,
						 dtVencimento, (dtEmissao == null ? new GregorianCalendar() : dtEmissao) /*dtEmissao*/,
						 null /*dtRecebimento*/, null /*dtProrrogacao*/,
						 vlConta,
						 vlDesc /*vlAbatimento*/, 0.0d /*vlAcrescimo*/, 0.0d /*vlRecebido*/,
						 0/*stConta*/, ContaReceberServices.UNICA_VEZ /*tpFrequencia*/,
						 1 /*qtParcelas*/, ContaReceberServices.TP_OUTRO /*tpContaReceber*/,
						 0 /*cdNegociacao*/, "" /*txtObservacao*/, 0 /*cdPlanoPagamento*/,
						 0 /*cdFormaPagamento*/, new GregorianCalendar(), dtVencimento, 0/*cdTurno*/,
						 0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/, 0/*lgPrioritaria*/, 
						 0/*cdFormaPagamentoPreferencial*/, cdContaSacado, cdUsuario);
	
				//CATEGORIAS ECONOMICAS
				ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
				if(cats==null) {
					categorias.add(new ContaReceberCategoria(0, cdCategoriaDefault, vlConta, 0));
				}
				else {
					for (HashMap<String, Object> c : cats) {
						categorias.add(new ContaReceberCategoria(0, 
												(Integer)c.get("CD_CATEGORIA_ECONOMICA"), 
												c.get("VL_CONTA_CATEGORIA") instanceof String ? Double.parseDouble((String)c.get("VL_CONTA_CATEGORIA")) : (Float)c.get("VL_CONTA_CATEGORIA"), 
												c.get("CD_CENTRO_CUSTO")==null ? 0 : (Integer)c.get("CD_CENTRO_CUSTO")));
					}
				}
					
				Result result = ContaReceberServices.insert(contaReceber, categorias, null, true, authData, connect); 
				if(result.getCode()<0) {
					Conexao.rollback(connect);
					return result;
				}
				
				int cdContaReceber = result.getCode();
				
				for(int i=0; i<itens.size(); i++)	{
					vlConta += itens.get(i).getVlEventoFinanceiro();
					
					connect.prepareStatement("UPDATE prc_processo_financeiro SET cd_conta_receber = "+result.getCode()+
							                 " WHERE cd_processo          = "+itens.get(i).getCdProcesso()+
							                 "   AND cd_evento_financeiro = "+itens.get(i).getCdEventoFinanceiro()).executeUpdate();
//					ProcessoFinanceiro despesa = ProcessoFinanceiroDAO.get(itens.get(i).getCdEventoFinanceiro(), itens.get(i).getCdProcesso(), connect);
//					despesa.setCdContaReceber(result.getCode());
//					save(despesa, authData, connect);
					
					
				}
				
				connect.prepareStatement("UPDATE adm_conta_receber SET vl_conta = "+vlConta+
		                 				 " WHERE cd_conta_receber = "+cdContaReceber).executeUpdate();
				
//				connect.prepareStatement("UPDATE adm_conta_receber_categoria SET vl_conta_categoria = "+vlConta+
//	    				 				 "WHERE cd_conta_receber = "+cdContaReceber).executeUpdate();
//				
				//MOVIMENTACAO
				if(stConta==1) {// RECEBIDA
					
					int cdFormaPagamento = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_DINHEIRO", 0, 0);
					
					Result r = ContaReceberServices.lancarRecebimento(cdContaReceber, 
							cdConta, 
							0.0/*vlMulta*/, 
							0/*cdCategoriaMulta*/, 
							0.0/*vlJuros*/, 
							0/*cdCategoriaJuros*/, 
							vlDesc, 
							cdCategoriaDefault/*cdCategoriaDesconto*/, 
							vlConta/*vlRecebido*/, 
							cdFormaPagamento, 
							null/*tituloCredito*/, 
							cdUsuario, 
							null/*dtDeposito*/, 
							new GregorianCalendar()/*dtMovimento*/, 
							dsHistorico,
							connect);
					
					if(r.getCode()<0) {
						Conexao.rollback(connect);
						
						System.out.println("Erro ao lançar movimentação financeira. "+r.getMessage());
						r.setMessage("Erro ao lançar movimentação financeira.");
						return r;
					}
				}
				
				//ARQUIVOS
				if(arquivos!=null) {
					for (Arquivo arquivo : arquivos) {
						Result rArquivo = ContaReceberArquivoServices.save(new ContaReceberArquivo(cdContaReceber, 0, 0, 0), arquivo, connect);
						
						if(rArquivo.getCode()<=0) {
							Conexao.rollback(connect);
							return rArquivo;
						}
					}
				}				
			}
			// DESPESA
			else	{
				// Buscando categoria economica
				int cdCategoriaDefault = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESPESAS_DEFAULT", 0, cdEmpresa);
				if(cdCategoriaDefault <= 0)	{
					ResultSetMap rsmCats = CategoriaEconomicaServices.getAllCategoriaDespesa();
					if(rsmCats.next())
						cdCategoriaDefault = rsmCats.getInt("cd_categoria_economica"); 
				}
				
				//CONTA A PAGAR
//				ContaPagar contaPagar2 = new ContaPagar(0/*cdContaPagar*/, 0/*cdContrato*/, cdPessoa, cdEmpresa,0/*cdContaOrigem*/,0/*cdDocumentoEntrada*/,
//													   cdConta, 0/*cdContaBancaria*/, dtVencimento, 
//													   (dtEmissao == null ? new GregorianCalendar() : dtEmissao) /*dtEmissao*/,
//													   null /*dtPagamento*/, null /*dtAutorizacao*/, nrDocumento, "" /*nrReferencia*/,
//													   1 /*nrParcela*/, cdTipoDocumento, vlConta, vlDesc/*vlAbatimento*/,0 /*vlAcrescimo*/,
//													   0 /*vlPago*/, dsHistorico, 0/*stConta*/, 1 /*lgAutorizado*/, 0 /*tpFrequencia*/,
//													   1 /*qtParcelas*/, vlConta /*vlBaseAutorizacao*/, 0 /*cdViagem*/,
//													   0 /*cdManutencao*/,"" /*txtObservacao*/, new GregorianCalendar(), dtVencimento, 0/*cdTurno*/);
				
				ContaPagar contaPagar = new ContaPagar(0, 0, cdPessoa, cdEmpresa, 0, 0, 
						cdConta, 0, dtVencimento, (dtEmissao == null ? new GregorianCalendar() : dtEmissao), null, null, nrDocumento, null, 
						1, cdTipoDocumento, vlConta, vlDesc, 0, 0, dsHistorico, 0, 1, 
						0, 1, vlConta, 0, 0, null, new GregorianCalendar(), 
						dtVencimento, 0, 0, 0, 0, 
						null, cdContaSacado, cdUsuario);
	
				//CATEGORIAS ECONOMICAS
				ArrayList<ContaPagarCategoria> categorias = new ArrayList<ContaPagarCategoria>();
				if(cats==null) {
					categorias.add(new ContaPagarCategoria(0, cdCategoriaDefault, vlConta, 0));
				}
				else {
					for (HashMap<String, Object> c : cats) {
						categorias.add(new ContaPagarCategoria(0, (Integer)c.get("CD_CATEGORIA_ECONOMICA"),  
												c.get("VL_CONTA_CATEGORIA") instanceof String ? Float.parseFloat((String)c.get("VL_CONTA_CATEGORIA")) : (Float)c.get("VL_CONTA_CATEGORIA"), 
												c.get("CD_CENTRO_CUSTO")==null ? 0 : (Integer)c.get("CD_CENTRO_CUSTO")));
					}
				}
				
				Result result = ContaPagarServices.insert(contaPagar, true, false, categorias, authData, connect);
				if(result.getCode()<0) {
					Conexao.rollback(connect);
					return result;
				}
				
				int cdContaPagar = result.getCode();
				for(int i=0; i<itens.size(); i++)	{
					vlConta += itens.get(i).getVlEventoFinanceiro();
					
					connect.prepareStatement("UPDATE prc_processo_financeiro SET cd_conta_pagar = "+result.getCode()+
							                 " WHERE cd_processo          = "+itens.get(i).getCdProcesso()+
							                 "   AND cd_evento_financeiro = "+itens.get(i).getCdEventoFinanceiro()).executeUpdate();
//					ProcessoFinanceiro despesa = ProcessoFinanceiroDAO.get(itens.get(i).getCdEventoFinanceiro(), itens.get(i).getCdProcesso(), connect);
//					despesa.setCdContaPagar(cdContaPagar);
//					save(despesa, authData, connect);
				}
				
				connect.prepareStatement("UPDATE adm_conta_pagar SET vl_conta = "+vlConta+
		                 				 " WHERE cd_conta_pagar = "+cdContaPagar).executeUpdate();
				
//				connect.prepareStatement("UPDATE adm_conta_pagar_categoria SET vl_conta_categoria = "+vlConta+
//	    				 				 "WHERE cd_conta_pagar = "+cdContaPagar).executeUpdate();
				
				// MOVIMENTACAO
				if(stConta==1) {// PAGA
					int cdFormaPagamento = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_DINHEIRO", 0, 0);
					
					Result r = ContaPagarServices.setPagamentoResumido(cdContaPagar, 
													cdConta, 
													0.0/*vlMulta*/, 
													0/*cdCategoriaMulta*/, 
													0.0/*vlJuros*/, 
													0/*cdCategoriaJuros*/, 
													vlDesc, 
													cdCategoriaDefault/*cdCategoriaDesconto*/, 
													vlConta, 
													cdFormaPagamento, 
													0/*cdCheque*/,
													dsHistorico, 
													cdUsuario, 
													new GregorianCalendar()/*dtPagamento*/, 
													0/*stCheque*/, 
													0/*cdTituloCredito*/, 
													0/*cdTurno*/, 
													authData,
													connect);
					
					if(r.getCode()<0) {
						Conexao.rollback(connect);
						
						r.setMessage("Erro ao lançar movimentação financeira.");
						return r;
					}
				}
				
				//ARQUIVOS
				if(arquivos!=null) {
					for (Arquivo arquivo : arquivos) {
						ContaPagarArquivo contaPagarArquivo = new ContaPagarArquivo(cdContaPagar, 0);
						Result rArquivo = ContaPagarArquivoServices.save(contaPagarArquivo, arquivo, connect);
						
						if(rArquivo.getCode()<=0) {
							Conexao.rollback(connect);
							return rArquivo;
						}
					}
				}
			}
			
			connect.commit();
			
			return new Result(1, "Registro(s) faturado(s) com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			Conexao.rollback(connect);
			return new Result(-1, "Erro ao verificar lançamento automático!");
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarPautaCompromisso(ArrayList<Integer> agendas, int cdEmpresa, int tpNaturezaEvento){
		return gerarPautaCompromisso(agendas, null, null, cdEmpresa, tpNaturezaEvento, null, null, null);
	}
	
	public static Result gerarPautaCompromisso(ArrayList<Integer> agendas, ArrayList<Integer> processoFinanceiro, ArrayList<Integer> produtoServico, 
			int cdEmpresa, int tpNaturezaEvento, GregorianCalendar dtRealizacaoInicial, GregorianCalendar dtRealizacaoFinal){
		return gerarPautaCompromisso(agendas, processoFinanceiro, produtoServico, cdEmpresa, tpNaturezaEvento, dtRealizacaoInicial, dtRealizacaoFinal, null);
	}
	
	public static Result gerarPautaCompromisso(ArrayList<Integer> agendas, ArrayList<Integer> processoFinanceiro, ArrayList<Integer> produtoServico, 
			int cdEmpresa, int tpNaturezaEvento, GregorianCalendar dtRealizacaoInicial, GregorianCalendar dtRealizacaoFinal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_AGENDA_ITEM", Util.join(agendas), ItemComparator.IN, Types.INTEGER));
			if(processoFinanceiro!=null && processoFinanceiro.size()>0)
				criterios.add(new ItemComparator("U.CD_EVENTO_FINANCEIRO", Util.join(processoFinanceiro), ItemComparator.IN, Types.INTEGER));
			if(produtoServico!=null && produtoServico.size()>0)
				criterios.add(new ItemComparator("U.CD_PRODUTO_SERVICO", Util.join(produtoServico), ItemComparator.IN, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("tpModulo", Integer.toString(0), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lgFaturamento", Integer.toString(1), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("U.tp_natureza_evento", Integer.toString(tpNaturezaEvento), ItemComparator.EQUAL, Types.INTEGER));
			if(dtRealizacaoInicial!=null)
				criterios.add(new ItemComparator("A.DT_REALIZACAO", Util.convCalendarStringSql(dtRealizacaoInicial), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			if(dtRealizacaoFinal!=null)
				criterios.add(new ItemComparator("A.DT_REALIZACAO", Util.convCalendarStringSql(dtRealizacaoFinal), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			
			LogUtils.debug("\n\n\n"+criterios+"\n\n\n");
			
			ResultSetMap rsm = AgendaItemServices.getList(criterios, connect);
			
			Empresa empresa = EmpresaDAO.get(cdEmpresa, connect);

			//ExcelServices.gerarPautaCompromissosFaturamento foi depreciado
			return ExcelServices.gerarPautaCompromissosFaturamento2(rsm, empresa);
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, "Erro ao gerar pauta de compromissos.");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLogCompliance(int cdEventoFinanceiro, int cdProcesso, boolean lgDelete) {
		return getLogCompliance(cdEventoFinanceiro, cdProcesso, lgDelete, null);
	}
	
	public static ResultSetMap getLogCompliance(int cdEventoFinanceiro, int cdProcesso, boolean lgDelete, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = ComplianceManager
					.search(" SELECT * FROM prc_processo_financeiro "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND cd_processo="+cdProcesso+" AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE	
						  :
						  " AND cd_evento_financeiro="+cdEventoFinanceiro+" AND cd_processo="+cdProcesso)
						+ " ORDER BY dt_compliance DESC ");
			
			while(rsm.next()) {
				if(rsm.getPointer()==0 && !lgDelete)
					rsm.setValueToField("tp_versao_compliance", "ATUAL");
				else
					rsm.setValueToField("tp_versao_compliance", "ANTIGA");
				
				rsm.setValueToField("nm_tp_acao", ComplianceManager.tipoAcao[rsm.getInt("tp_acao_compliance")].toUpperCase());
				
				if(rsm.getInt("cd_usuario_compliance", 0) > 0) {
					Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario_compliance"), connect);
					Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					rsm.setValueToField("nm_usuario_compliance", pessoa.getNmPessoa());
				}
				
				if(rsm.getInt("cd_usuario", 0) > 0) {
					Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario"), connect);
					Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					rsm.setValueToField("nm_usuario", pessoa.getNmPessoa());
				}
				
				//natureza
				rsm.setValueToField("nm_tp_natureza", natureza[rsm.getInt("tp_natureza_evento")].toUpperCase());
				
				//serviço
				if(rsm.getInt("cd_produto_servico", 0) > 0) {
					ProdutoServico servico = ProdutoServicoDAO.get(rsm.getInt("cd_produto_servico"), connect);
					rsm.setValueToField("nm_produto_servico", servico.getNmProdutoServico());
				}
				
				//tomador/prestador
				if(rsm.getInt("cd_pessoa", 0) > 0) {
					Pessoa pessoa = PessoaDAO.get(rsm.getInt("cd_pessoa"), connect);
					rsm.setValueToField("nm_pessoa", pessoa.getNmPessoa());
				}
				
				if(lgDelete) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", " ");
					rsmDetalhes.addRegister(regAtual);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
					
				}
			}
			rsm.beforeFirst();

			
			if(!lgDelete) {
				while(rsm.next()) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", "PARA");
					rsmDetalhes.addRegister(regAtual);
					
					if(rsm.next()) { //como a ordem é decrescente, o próximo registro representa a versão anterior
						HashMap<String, Object> regAnterior = (HashMap<String, Object>)rsm.getRegister().clone();
						regAnterior.put("TP_ITEM_COMPLIANCE", "DE");
						rsmDetalhes.addRegister(regAnterior);
						rsm.previous();
					}
					
					ArrayList<String> fields = new ArrayList<>();
					fields.add("TP_ITEM_COMPLIANCE");
					rsmDetalhes.orderBy(fields);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
				}
				rsm.beforeFirst();
			}
			
			return rsm;
		}
		catch (Exception e) {
			System.out.println("Erro ao buscar log: "+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
