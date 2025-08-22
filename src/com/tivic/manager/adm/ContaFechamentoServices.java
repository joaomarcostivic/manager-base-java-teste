package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import com.tivic.manager.alm.DocumentoSaidaItemServices;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.pcb.BicoEncerranteServices;
import com.tivic.manager.pcb.MedicaoFisicaServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.ConfManager;
import sol.util.Result;


public class ContaFechamentoServices {

	public static final int ERR_FECH_DATE_NULL 			= -1;
	public static final int ERR_FECH_CODE_USUARIO_NULL 	= -3;
	public static final int ERR_FECH_DATE_INVALID 		= -4;
	
	public static final String[] situacaoContaFechamento = {"Em Andamento","Em Conferência", "Em Correção", "Concluído", "Bloqueado"};

	public static final int ST_EM_ANDAMENTO    = 0;
	public static final int ST_EM_CONFERENCIA  = 1;
	public static final int ST_EM_CORRECAO     = 2;
	public static final int ST_CONCLUIDO       = 3;
	public static final int ST_BLOQUEADO       = 4;
	
	/*
	 * Retorna os dados do fechamento da conta e dia passados
	 */
	public static ResultSetMap getFechamentoOf(int cdConta, GregorianCalendar dtFechamento, int cdTurno) {
		return getFechamentoOf(cdConta, dtFechamento, cdTurno, null);
	}
	
	public static ResultSetMap getFechamentoOf(int cdConta, GregorianCalendar dtFechamento) {		
		return getFechamentoOf(cdConta, dtFechamento, 0, null);
	}

	public static ResultSetMap getFechamentoOf(int cdConta, GregorianCalendar dtFechamento, int cdTurno, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement(
									"SELECT A.*, B.nm_login AS nm_login_responsavel, B2.nm_pessoa AS nm_responsavel, " +
                                    "       C.nm_login AS nm_login_supervisor, C2.nm_pessoa AS nm_supervisor        " +
									" FROM adm_conta_fechamento A " +
									"  LEFT OUTER JOIN seg_usuario B ON (A.cd_responsavel = B.cd_usuario) " +
									"  LEFT OUTER JOIN seg_usuario C ON (A.cd_supervisor  = C.cd_usuario) " +
									"  LEFT OUTER JOIN grl_pessoa B2 ON (B.cd_pessoa = B2.cd_pessoa) " +
									"  LEFT OUTER JOIN grl_pessoa C2 ON (C.cd_pessoa = C2.cd_pessoa) " +
									" WHERE cd_conta      = " +cdConta+
									"  AND CAST(dt_fechamento AS DATE) = ? " +
									  (cdTurno > 0 ? "AND cd_turno = "+cdTurno: ""));
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtFechamento));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * 
	 * @param contaFechamento
	 * @return
	 */
	public static Result save(ContaFechamento contaFechamento){
		return save(contaFechamento, null, null);
	}
	
	public static Result save(ContaFechamento contaFechamento, Connection connect){
		return save(contaFechamento, null, connect);
	}
	
	public static Result save(ContaFechamento contaFechamento, Usuario usuario){
		return save(contaFechamento, usuario, null);
	}
	
	public static Result save(ContaFechamento contaFechamento, Usuario usuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(contaFechamento==null)
				return new Result(-1, "Erro ao salvar. ContaFechamento é nulo");

			int retorno;
			HashMap<String, Object> novoFechamento = null;
			if(contaFechamento.getCdFechamento()==0){
				retorno = ContaFechamentoDAO.insert(contaFechamento, connect);
				contaFechamento.setCdFechamento(retorno);
			}
			else {
				ContaFechamento fechamentoAtual = ContaFechamentoDAO.get( contaFechamento.getCdConta(), contaFechamento.getCdFechamento());
				String msgOcorrencia = "";
				/**
				 * Mudança de responsável
				 */
				Result resultOcorrencia = null;
				if( usuario != null && contaFechamento.getCdResponsavel() != fechamentoAtual.getCdResponsavel() ){
					Pessoa responsavelAtual = PessoaDAO.get( UsuarioDAO.get( fechamentoAtual.getCdResponsavel() ).getCdPessoa() );
					Pessoa novoResponsavel  = PessoaDAO.get( UsuarioDAO.get( contaFechamento.getCdResponsavel() ).getCdPessoa() );
					msgOcorrencia += "Responsável alterado de "+responsavelAtual.getNmPessoa()+" para "+novoResponsavel.getNmPessoa();
				}
				
				/**
				 * Mudança de Supervisor
				 */
				if( usuario != null && contaFechamento.getCdSupervisor() != fechamentoAtual.getCdSupervisor() ){
					Pessoa supervisorAtual = PessoaDAO.get( UsuarioDAO.get( fechamentoAtual.getCdSupervisor() ).getCdPessoa() );
					Pessoa novoSupervisor  = PessoaDAO.get( UsuarioDAO.get( contaFechamento.getCdSupervisor() ).getCdPessoa() );
					if( msgOcorrencia.length() > 0 )
						msgOcorrencia += "; ";
					msgOcorrencia += "Supervisor alterado de "+supervisorAtual.getNmPessoa()+" para "+novoSupervisor.getNmPessoa();
				}
				/**
				 * Mudança de situação
				 */
				if( usuario != null && contaFechamento.getStFechamento() != fechamentoAtual.getStFechamento() ){
					if( msgOcorrencia.length() > 0 )
						msgOcorrencia += "; ";
					msgOcorrencia += "Fechamento alterado de '"+ situacaoContaFechamento[ fechamentoAtual.getStFechamento() ]
								     +"' para '"+situacaoContaFechamento[contaFechamento.getStFechamento()]+"'";
					if( fechamentoAtual.getStFechamento() == ST_CONCLUIDO ){
						/**
						 * REVERTE OPERAÇÕES GERADAS COM O FECHAMENTO
						 */
						int cdConta = contaFechamento.getCdConta();
						int cdFechamento = contaFechamento.getCdFechamento();
						// Volta situação de fechado para conferido
						connect.prepareStatement("UPDATE adm_movimento_conta SET st_movimento = " +MovimentoContaServices.ST_CONFERIDO+
									                " WHERE cd_conta      = " +cdConta+
													"   AND cd_fechamento = " +cdFechamento+
													"   AND st_movimento  = "+MovimentoContaServices.ST_CX_FECHADO).executeUpdate();
						// Exclui dados dos TÍTULOS vinculados ao fechamento
						connect.prepareStatement("DELETE FROM adm_conta_fechamento_tit_cred "+
									                " WHERE cd_conta      = " +cdConta+
													"   AND cd_fechamento = " +cdFechamento).executeUpdate();
						// Exclui dados dos saldos em TÍTULOS de crédito
						connect.prepareStatement("DELETE FROM adm_conta_fechamento_tipo_doc "+
									                " WHERE cd_conta      = " +cdConta+
													"   AND cd_fechamento = " +cdFechamento).executeUpdate();
						// Exclui dados dos bicos
						connect.prepareStatement("DELETE FROM pcb_bico_encerrante "+
									                " WHERE cd_conta      = " +cdConta+
													"   AND cd_fechamento = " +cdFechamento).executeUpdate();
						// Exclui dados da medição física
						connect.prepareStatement("DELETE FROM pcb_medicao_fisica "+
									                " WHERE cd_conta      = " +cdConta+
													"   AND cd_fechamento = " +cdFechamento).executeUpdate();
					}
				}
				
				
				if( msgOcorrencia.length() > 0 && usuario != null ){
					FechamentoOcorrencia ocorrencia = new FechamentoOcorrencia(0/*cdFechamentoOcorrencia*/, 
							usuario.getCdUsuario(),
							contaFechamento.getCdFechamento(),
							contaFechamento.getCdConta(), 
							msgOcorrencia,
							new GregorianCalendar(new Locale("pt", "BR")),0);
					resultOcorrencia = FechamentoOcorrenciaServices.save(ocorrencia, connect);
					if( resultOcorrencia.getCode() <= 0){
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao gerar ocorrências de atualização!");
					}
				}
				
				retorno = ContaFechamentoDAO.update(contaFechamento, connect);
				ResultSetMap rsm = getFechamentoOf( contaFechamento.getCdConta(), contaFechamento.getDtFechamento(),  contaFechamento.getCdTurno(), connect);
				if( rsm != null && rsm.next() )
					novoFechamento = rsm.getRegister();
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result result = new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...",
											"CONTAFECHAMENTO", (novoFechamento!=null)?novoFechamento:contaFechamento);
			return result;
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
	 * Fluxo PAX
	 * Remove movimentações não conferidas que estejam vinculadas ao fechamento
	 * @param cdFechamento
	 * @return
	 */
	public static Result remove(ContaFechamento fechamento) {
		return remove(fechamento, null);
	}
	public static Result remove(ContaFechamento fechamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			ArrayList<Integer> contasReceber = new ArrayList<Integer>();
			// Movimentações vinculadas ao fechamento
			ResultSetMap movimentos = new ResultSetMap(connection.prepareStatement(
											" SELECT * FROM adm_movimento_conta_receber A"+
											" JOIN adm_movimento_conta b on ( A.cd_conta = B.cd_conta AND A.cd_movimento_conta = B.cd_movimento_conta )  "+
							                " WHERE B.cd_conta      = " +fechamento.getCdConta()+
											"   AND B.cd_fechamento = "+fechamento.getCdFechamento()).executeQuery() );
			while ( movimentos.next() ) {
				contasReceber.add( movimentos.getInt("CD_CONTA_RECEBER") );
				
				MovimentoContaReceberServices.remove( movimentos.getInt("CD_CONTA"),
													  movimentos.getInt("CD_MOVIMENTO_CONTA"),
													  movimentos.getInt("CD_CONTA_RECEBER"), true, connection);
			}
			if( contasReceber.size() > 0 ){
				Integer[] cdsContas = new Integer[contasReceber.size()];
				cdsContas = contasReceber.toArray(cdsContas);
				ContaReceberServices.remove( cdsContas, connection );
			}
			
			Result resOcorrencia = FechamentoOcorrenciaServices.removeAll(fechamento.getCdConta(), fechamento.getCdFechamento(), connection);
			Result resArquivos = ContaFechamentoArquivoServices.removeAll(fechamento.getCdConta(), fechamento.getCdFechamento(), connection);
			Result res = remove( fechamento.getCdConta(), fechamento.getCdFechamento(), connection );
			if( resOcorrencia.getCode() <=0 || res.getCode()<=0 || resArquivos.getCode()<=0){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao remover fechamento.");
			}
			if(isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir fechamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	/**
	 * Fluxo Pax
	 * @param fechamento
	 * @param cdMovimentoConta
	 * @param cdUsuario
	 * @param connection
	 * @return
	 */
	public static Result saveMovimentacoes( ContaFechamento fechamento, ResultSetMap movimentos, int cdEmpresa ){
		return saveMovimentacoes( fechamento, movimentos, cdEmpresa, null );
	}
	public static Result saveMovimentacoes( ContaFechamento fechamento, ResultSetMap movimentos, int cdEmpresa, Connection connection ) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			if( movimentos == null){
				if(isConnectionNull)
					connection.rollback();
				return new Result(-1, "Movimentos não informado.");
			}
			Result result =  new Result(1);
			while( movimentos.next() ){
				result = saveMovimentacao(fechamento, (MovimentoConta) movimentos.getObject("MOVIMENTOCONTA"), cdEmpresa,
								movimentos.getInt("CD_CONTA_BANCARIA_TRANSFERENCIA"),
								(Arquivo) movimentos.getObject("FILE") );
				if( result.getCode() <= 0 ){
					if(isConnectionNull)
						connection.rollback();
					return new Result(-1, "Erro ao salvar movimento["+movimentos.getPosition()+"]: "+result.getMessage());
				}
			}
			
			if(isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir fechamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result saveMovimentacao( ContaFechamento fechamento, MovimentoConta movimento, int cdEmpresa,
			int cdContaBancariaTransf, Arquivo arquivo ) {
		return saveMovimentacao(fechamento, movimento, cdEmpresa, cdContaBancariaTransf, arquivo, null, null, null, true, null);
	}
	public static Result saveMovimentacao( ContaFechamento fechamento, MovimentoConta movimento, int cdEmpresa,
									int cdContaBancariaTransf, Arquivo arquivo, Boolean lgRealizarTransferencia ) {
		return saveMovimentacao(fechamento, movimento, cdEmpresa, cdContaBancariaTransf, arquivo, null, null, null, lgRealizarTransferencia, null);
	}
	
	public static Result saveMovimentacao( ContaFechamento fechamento, MovimentoConta movimento, int cdEmpresa,
						int cdContaBancariaTransf, Arquivo arquivo, TituloCredito titulo) {
		return saveMovimentacao(fechamento, movimento, cdEmpresa, cdContaBancariaTransf, arquivo, titulo, null, null, true, null);
	}

	public static Result saveMovimentacao( ContaFechamento fechamento, MovimentoConta movimento, int cdEmpresa,
			int cdContaBancariaTransf, Arquivo arquivo, TituloCredito titulo, Pessoa portador, Pessoa emissor) {
		return saveMovimentacao(fechamento, movimento, cdEmpresa, cdContaBancariaTransf, arquivo, titulo, portador, emissor, true, null);
	}
	
	public static Result saveMovimentacao( ContaFechamento fechamento, MovimentoConta movimento, int cdEmpresa,
					int cdContaBancariaTransf, Arquivo arquivo, TituloCredito titulo,
					Pessoa portador, Pessoa emissor, Boolean lgRealizarTransferencia, Connection connection ) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			Result result =  new Result(1);
			
			/**
			 * TODO Refatorar separando a lógica de gravação por forma de pagamento
			 */
			
			/**
			 * Atualiza dados de portador e emissor do cheque
			 */
			if( portador != null ){
				Pessoa portadorTmp = PessoaDAO.get(portador.getCdPessoa(), connection);
				portadorTmp.setNrTelefone1( portador.getNrTelefone1() );
				PessoaDAO.update(portadorTmp, connection);
			}
			if( emissor != null ){
				Pessoa emissorTmp = PessoaDAO.get(emissor.getCdPessoa());
				emissorTmp.setNrTelefone1( emissor.getNrTelefone1() );
				PessoaDAO.update(emissorTmp, connection);
			}
			
			
			ContaFinanceira contaCaixa = ContaFinanceiraDAO.get(movimento.getCdConta(), connection);

			SimpleDateFormat formatDate = new SimpleDateFormat("ddMMyyyy");
			String nrDocumento = formatDate.format(new java.util.Date());
			if( contaCaixa.getIdConta() != null )
				nrDocumento = nrDocumento+""+contaCaixa.getIdConta();
			
			ResultSet rs = connection.prepareStatement(" SELECT COUNT(*) as qt_movimentos FROM adm_movimento_conta "+
													   " WHERE cd_conta = "+movimento.getCdConta()+
													   " AND cd_fechamento = "+fechamento.getCdFechamento()+
													   ((fechamento.getCdTurno()>0)?
														 " AND cd_turno = "+fechamento.getCdTurno():"")
					).executeQuery();
			if( rs.next() ){
				nrDocumento = nrDocumento+""+Util.fillNum(rs.getInt("qt_movimentos")+1, 3);
			}else{
				nrDocumento = nrDocumento+""+Util.fillNum(1, 3);
			}
			
			if( movimento.getNrDocumento()!=null && movimento.getNrDocumento().equals("") )
				movimento.setNrDocumento(nrDocumento);
			
			movimento.setCdFechamento( fechamento.getCdFechamento() );
			ArrayList<ContaReceberCategoria> categorias = null;
			
			ContaReceber contaReceber = new ContaReceber(0/*cdContaReceber*/,0/* cdPessoa*/,cdEmpresa/* cdEmpresa*/,0/* cdContrato*/,0/* cdContaOrigem*/,
											0/*cdDocumentoSaida*/,0/* cdContaCarteira*/, movimento.getCdConta()/*cdConta*/,0/* cdFrete*/,
											nrDocumento/* nrDocumento*/,""/* idContaReceber*/,0/*nrParcela*/,""/* nrReferencia*/,
											ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_FECHAMENTO_CAIXA", 0, 0, connection)/* cdTipoDocumento*/,
											movimento.getDsHistorico()/* dsHistorico*/,
											movimento.getDtMovimento()/* dtVencimento*/,
											movimento.getDtMovimento()/* dtEmissao*/,
											null/*dtRecebimento*/,null/* dtProrrogacao*/,
											movimento.getVlMovimento()/* vlConta*/,0/* vlAbatimento*/,0/* vlAcrescimo*/,0/* vlRecebido*/,
											ContaReceberServices.ST_EM_ABERTO/* stConta*/,0/*tpFrequencia*/,0/* qtParcelas*/,0/* tpContaReceber*/,0/* cdNegociacao*/,
											movimento.getDsHistorico()/* txtObservacao*/,0/* cdPlanoPagamento*/,
											movimento.getCdFormaPagamento()/*cdFormaPagamento*/,new GregorianCalendar()/* dtDigitacao*/,
											movimento.getDtMovimento()/* dtVencimentoOriginal*/,
											movimento.getCdTurno()/* cdTurno*/,0/* prJuros*/,0/* prMulta*/,0/* lgProtesto*/,
											0/*lgPrioritaria*/,0/* cdFormaPagamentoPreferencial*/,0/* cdContaSacado*/, movimento.getCdUsuario());
			ContaReceberCategoria contaReceberCat = new ContaReceberCategoria(0/*cdContaReceber*/, 
												ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT", 0, cdEmpresa, connection)/*cdCategoriaEconomica*/,
																				contaReceber.getVlConta()/*vlContaCategoria*/, 0/*cdCentroCusto*/);
			categorias = new ArrayList<ContaReceberCategoria>();
			categorias.add(contaReceberCat);
			result = ContaReceberServices.save(contaReceber,null, categorias, connection);
			if( result.getCode() <= 0 ){
				connection.rollback();
				return new Result(-1,"Erro ao registrar conta a receber");
			}
			contaReceber.setCdContaReceber( result.getCode() );
			contaReceberCat.setCdContaReceber( result.getCode() );
			MovimentoContaReceber movContaReceber = new MovimentoContaReceber(movimento.getCdConta()/*cdConta*/,0/* cdMovimentoConta*/,
																				contaReceber.getCdContaReceber()/* cdContaReceber*/,
																				contaReceber.getVlConta()/* vlRecebido*/,0.0/* vlJuros*/,
																				0.0/* vlMulta*/,0.0/* vlDesconto*/,
																				0.0/*vlTarifaCobranca*/,0/* cdArquivo*/,0/* cdRegistro*/);
			
			/**
			 * Salvando Movimentação de conta a receber
			 */
			ArrayList<MovimentoContaReceber> movsContaRec = new ArrayList<MovimentoContaReceber>();
			movsContaRec.add(movContaReceber);
			
			ArrayList<MovimentoContaCategoria> movsContaCat = new ArrayList<MovimentoContaCategoria>();
			movsContaCat.add(new MovimentoContaCategoria( contaReceber.getCdConta() /*cdConta*/,0/* cdMovimentoConta*/,
										ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT", 0, cdEmpresa, connection)/*cdCategoriaEconomica*/,
										contaReceber.getVlConta()/* vlMovimentoCategoria*/,0/*cdMovimentoContaCategoria*/,0/* cdContaPagar*/,
										contaReceber.getCdContaReceber()/*cdContaReceber*/, 0/* tpMovimento*/,0/* cdCentroCusto*/));
			
			result = MovimentoContaServices.insert( movimento, movsContaRec/*movimentoConta*/,
					movsContaCat/*categorias*/, false/*filaImpressao*/, connection);
			
			if( result.getCode() <= 0 ){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, result.getMessage());
			}
			movimento.setCdMovimentoConta( result.getCode() );
			
			FormaPagamento formaPag = FormaPagamentoDAO.get( movimento.getCdFormaPagamento() );
			if( formaPag == null ){
				if(isConnectionNull)
					connection.rollback();
				return new Result(-1,"Forma de Pagamento não informada");
			}
			
			/**
			 * Criação de contas a receber provenientes de recebimento via cartão de crédito/débito
			 */
			TituloCredito tituloCreditoCheque = null;
			if( formaPag.getTpFormaPagamento() == FormaPagamentoServices.TEF ){
				
				int qtParcelas    = 1;
				int qtDiasCredito = 0;
				Double vlDesconto  = 0.0; 
				Double prTaxaDesconto  = 0.0;
				Double vlParcela  = movimento.getVlMovimento()/qtParcelas;
				
				FormaPagamentoEmpresa formaPagEmpresa = FormaPagamentoEmpresaDAO.get(  movimento.getCdFormaPagamento(), cdEmpresa);
				qtDiasCredito = formaPagEmpresa.getQtDiasCredito();
				prTaxaDesconto = formaPagEmpresa.getPrTaxaDesconto();
				vlDesconto = vlParcela*(prTaxaDesconto/100);
								
				if( formaPagEmpresa.getCdTipoDocumento() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_CARTAO_CREDITO", 0,  0, connection) ){
					if( movimento.getCdPlanoPagamento() <= 0 ){
						if(isConnectionNull)
							connection.rollback();
						return new Result(-10, "Plano de Pagamento não informado.");
					}
					ResultSetMap rsm = new ResultSetMap( connection.prepareStatement(
							" SELECT * FROM ADM_FORMA_PLANO_PAGAMENTO A "+
							" JOIN ADM_PLANO_PAGAMENTO B ON ( B.CD_PLANO_PAGAMENTO = A.CD_PLANO_PAGAMENTO )"+ 
							" JOIN ADM_PLANO_PARCELAMENTO C ON ( C.CD_PLANO_PAGAMENTO = B.CD_PLANO_PAGAMENTO )"+
							" WHERE  A.CD_FORMA_PAGAMENTO = "+movimento.getCdFormaPagamento()+
							" AND  A.CD_EMPRESA = "+cdEmpresa+
							" AND  A.CD_PLANO_PAGAMENTO = "+movimento.getCdPlanoPagamento()
						).executeQuery());
				
					rsm.next();
					qtParcelas = rsm.getInt("QT_PARCELAS");
					vlParcela = movimento.getVlMovimento()/qtParcelas;
					prTaxaDesconto = rsm.getDouble("PR_TAXA_DESCONTO");
					vlDesconto = vlParcela*(prTaxaDesconto/100);
				}
				
				
				
				/**
				 * Criar título de crédito e conta a receber no nome da administradora do cartão
				 */
				
				categorias.add( new ContaReceberCategoria(0,  ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_DESCONTO_TEF", 0, 0, connection), vlDesconto, 0, 0) );
				GregorianCalendar dtVencimentoParcela = (GregorianCalendar) movimento.getDtMovimento().clone();
				for(int i=0; i<qtParcelas;i++){
					
					dtVencimentoParcela.add(Calendar.DATE, qtDiasCredito);
					TituloCredito tituloCredito = new TituloCredito(0/*cdTituloCredito*/,0/* cdInstituicaoFinanceira*/,0/* cdAlinea*/,
							movimento.getNrDocumento()/*nrDocumento*/,""/* nrDocumentoEmissor*/,
							0/* tpDocumentoEmissor*/,""/* nmEmissor*/, contaReceber.getVlConta()/* vlTitulo*/,
							0/*tpEmissao*/,""/* nrAgencia*/, dtVencimentoParcela/* dtVencimento*/,
							dtVencimentoParcela/* dtCredito*/,
							TituloCreditoServices.stEM_ABERTO/* stTitulo*/,
							movimento.getDsHistorico()/*dsObservacao*/,
							formaPagEmpresa.getCdTipoDocumento()/* cdTipoDocumento*/,
							0/* tpCirculacao*/,
							formaPagEmpresa.getCdConta()/* cdConta*/,
							0/* cdContaReceber*/, 0, 0, "");
					ContaReceber contaReceberTitulo = new ContaReceber(0, formaPagEmpresa.getCdAdministrador(),
							contaReceber.getCdEmpresa(),
							0/*cdContrato*/,
							0/*cdContaOrigem*/,
							0 /*cdDocumentoSaida*/,
							formaPagEmpresa.getCdContaCarteira()/*cdContaCarteira*/, 
							formaPagEmpresa.getCdConta(),
							0 /*cdFrete*/,
							tituloCredito.getNrDocumento(), null, 1 /*nrParcela*/, (i+1)+"/"+qtParcelas/*nrReferencia*/,
							tituloCredito.getCdTipoDocumento(),
							"Recebiemnto em título de crédito - "+movimento.getDsHistorico(),
							tituloCredito.getDtVencimento(), tituloCredito.getDtCredito(),
							null /*dtRecebimento*/,
							null /*dtProrrogacao*/,
							vlParcela,
							vlDesconto /*vlAbatimento*/,
							0.0d /*vlAcrescimo*/, 0.0d /*vlRecebido*/,
							ContaReceberServices.ST_EM_ABERTO, ContaReceberServices.UNICA_VEZ,
							1 /*qtParcelas*/,
							ContaReceberServices.TP_PARCELA, 0 /*cdNegociacao*/,
							movimento.getDsHistorico() /*txtObservacao*/, movimento.getCdPlanoPagamento()/*cdPlanoPagamento*/,
							0 /*cdFormaPagamento*/, 
							new GregorianCalendar(),
							tituloCredito.getDtVencimento(), 0/*cdTurno*/,
							0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/, 0/*lgPrioritaria*/,0/*cdFormaPagamentoPreferencial*/,
							0/*cdContaSacado*/, movimento.getCdUsuario());
					
					ArrayList<ContaReceberCategoria>  cloneCategorias = (ArrayList<ContaReceberCategoria>) categorias.clone();
					for( int j=0;j<cloneCategorias.size();j++ ){
						cloneCategorias.get(j).setCdContaReceberCategoria(0);
					}
					Result resCartao = ContaReceberServices.save(contaReceberTitulo, tituloCredito, categorias, connection);
					if( resCartao.getCode() <= 0 ){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-11, "Não foi possível criar a conta a receber do Título de Crédito!");
					}
					contaReceberTitulo.setCdContaReceber( resCartao.getCode() );
					/**
					 * Vincula a conta a receber ao movimento de recebimento que as gerou
					 */
					ContaMovimentoOrigem contaMovOrigem = new ContaMovimentoOrigem( 0/*cdContaMovimentoOrigem*/,
							movimento.getCdMovimentoConta()/*cdMovimentoConta*/,
							movimento.getCdConta()/*cdConta*/,
							contaReceberTitulo.getCdContaReceber()/*cdContaReceber*/);
					Result resMovOrigem = ContaMovimentoOrigemServices.save(contaMovOrigem, connection);
					if( resMovOrigem.getCode() <=0 ){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-12, "Erro ao registrar contas a receber em nome da administradora de cartão");
					}
				}
			}else if( formaPag.getTpFormaPagamento() == FormaPagamentoServices.TITULO_CREDITO 
					&& movimento.getCdFormaPagamento() == ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_CHEQUE", 0, 0, connection)
					
			) {
				
				/**
				 * Instancia e registra o título (Cheque) usado no recebimento
				 */
				if( movimento.getCdFormaPagamento() == ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_CHEQUE", 0, 0, connection) ){
					
//					tituloCreditoCheque = new TituloCredito(0/*cdTituloCredito*/,0/* cdInstituicaoFinanceira*/, 0/* cdAlinea*/,
//							movimento.getNrDocumento()/*nrDocumento*/,""/* nrDocumentoEmissor*/,
//							0/* tpDocumentoEmissor*/,"NM_PORTADOR"/* nmEmissor*/, contaReceber.getVlConta()/* vlTitulo*/,
//							0/*tpEmissao*/,""/* nrAgencia*/, movimento.getDtMovimento()/* dtVencimento*/,
//							movimento.getDtMovimento()/* dtCredito*/,TituloCreditoServices.stEM_ABERTO/* stTitulo*/,
//							"Entrada em cheque no fechamento de caixa"/*dsObservacao*/,
//							ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_CHEQUE", 0)/* cdTipoDocumento*/,
//							0/* tpCirculacao*/,
//							movimento.getCdConta()/* cdConta*/,
//							0/* cdContaReceber*/, 0, 0);
					
					ContaReceber contaReceberTituloCheque = new ContaReceber(0, 0,
							 contaReceber.getCdEmpresa(),
							 0/*cdContrato*/,
							 0 /*cdContaOrigem*/,
							 0 /*cdDocumentoSaida*/,
							 0/*cdContaCarteira*/, 
							 fechamento.getCdConta(),
							 0 /*cdFrete*/,
							 titulo.getNrDocumento(), null, 1 /*nrParcela*/, "1/1" /*nrReferencia*/,
							 titulo.getCdTipoDocumento(), "Pagamento em título de crédito",
							 titulo.getDtVencimento(), titulo.getDtCredito(),
							 null /*dtRecebimento*/,
							 null /*dtProrrogacao*/, contaReceber.getVlConta(),
							 0.0d /*vlAbatimento*/,
							 0.0d /*vlAcrescimo*/, 0.0d /*vlRecebido*/,
							 ContaReceberServices.ST_EM_ABERTO, ContaReceberServices.UNICA_VEZ,
							 1 /*qtParcelas*/,
							 ContaReceberServices.TP_PARCELA, 0 /*cdNegociacao*/,
							 "" /*txtObservacao*/, 0 /*cdPlanoPagamento*/,
							 0 /*cdFormaPagamento*/, 
							 new GregorianCalendar(),
							 titulo.getDtVencimento(), 0/*cdTurno*/,
							 0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/, 0/*lgPrioritaria*/,0/*cdFormaPagamentoPreferencial*/,
							 0/*cdContaSacado*/, movimento.getCdUsuario());
					
					ArrayList<ContaReceberCategoria>  cloneCategorias = (ArrayList<ContaReceberCategoria>) categorias.clone();
					for( int j=0;j<cloneCategorias.size();j++ ){
						cloneCategorias.get(j).setCdContaReceberCategoria(0);
					}
					Result ret = ContaReceberServices.save(contaReceberTituloCheque, titulo, categorias , connection);
					if( ret.getCode() <= 0 ){
						if( isConnectionNull )
							connection.rollback();
						return new Result(-11, "Não foi possível criar a conta a receber do Título de Crédito(Cheque)!");
					}
				}
			}
			/**
			 * Atualiza o movimento informando que está vinculado a um fechamento
			 */
			int resutlMov = MovimentoContaDAO.update(movimento, connection);
			if( resutlMov < 0 ){
				connection.rollback();
				return new Result(-1,"Erro ao registrar movimentação de conta.");
			}
			/**
			 * Realiza a transferência da conta caixa para a conta bancária
			 */
			if( cdContaBancariaTransf > 0 && lgRealizarTransferencia ){
				ArrayList<MovimentoContaTituloCredito> titulos = null;
				if( movimento.getCdFormaPagamento() == ParametroServices.getValorOfParametroAsInteger("CD_FORMA_PAGAMENTO_CHEQUE", 0, 0, connection) ){
					titulos = new ArrayList<MovimentoContaTituloCredito>();
					titulos.add(new MovimentoContaTituloCredito(titulo.getCdTituloCredito(),
							0 /* cdMovimentoConta */, 0 /* cdConta */));
					
					/**
					 * Associa o título ao movimento principal
					 */
					MovimentoContaTituloCredito movContaTitulo = new MovimentoContaTituloCredito(
														titulo.getCdTituloCredito(),
														movimento.getCdMovimentoConta(),
														movimento.getCdConta());
					
					MovimentoContaTituloCreditoDAO.insert(movContaTitulo, connection);
				}
				
				
				/**
				 * Registra o movimento de transferencia no Fechamento corrente
				 * Obs.: Em insertTransfer() o objeto movTransf torna-se o registro de entrada na conta destino,
				 * por este motivo instancia-se seu movimento de origem, qual seja,
				 * o movimenta de saída da conta caixa do fechamento atual, para vinculação.
				 * e atualização da situação para conferido.
				 */
				MovimentoConta movTransfDestino = (MovimentoConta) movimento.clone();
				movTransfDestino.setDtMovimento( movTransfDestino.getDtDeposito() );
				result = MovimentoContaServices.insertTransfer( movTransfDestino, cdContaBancariaTransf, titulos, connection);
				
				MovimentoConta movTransfOrigem = MovimentoContaDAO.get( movTransfDestino.getCdMovimentoOrigem(), movimento.getCdConta(), connection );
				movTransfOrigem.setCdFechamento( fechamento.getCdFechamento() );
				movTransfOrigem.setCdMovimentoOrigem( movimento.getCdMovimentoConta() );
				movTransfOrigem.setStMovimento(MovimentoContaServices.ST_CONFERIDO);
				/**
				 * Recupera o movimento de entrada na conta bancária, para alterar sua situação para 'não compensado'
				 * de forma que possa ser efetuado o processo de conciliação bancária posteriormente,
				 * e a movimentação para a data do depósito
				 */
				movTransfDestino.setDtMovimento( movTransfOrigem.getDtDeposito() );
				movTransfDestino.setStMovimento( MovimentoContaServices.ST_NAO_COMPENSADO );
				int resTransf = MovimentoContaDAO.update(movTransfOrigem, connection);
				int resTransfDestino = MovimentoContaDAO.update(movTransfDestino, connection);
				if( result.getCode() <= 0 || resTransf <= 0 || resTransfDestino <=0){
					connection.rollback();
					return new Result(-1,"Erro ao registrar transferência para a conta bancária.");
				}
			}
			/**
			 * Salva arquivo
			 */
			if( arquivo != null ){
				MovimentoContaArquivo movArq = new MovimentoContaArquivo( movimento.getCdMovimentoConta(),
																		  movimento.getCdConta(), 0/*cdArquivo*/);
				result = MovimentoContaArquivoServices.save(movArq, arquivo, connection );
				if( result.getCode() <= 0 ){
					connection.rollback();
					return new Result(-1,"Erro ao salvar arquivo!");
				}
			}
			
			if( result.getCode()<=0 ){
				if(!isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao remover fechamento.");
			}
			if(isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir fechamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Fluxo Pax
	 */
	public static Result updateMovimentacao( MovimentoConta movimento, int cdEmpresa ){
		return updateMovimentacao( movimento, cdEmpresa, null );
	}
	public static Result updateMovimentacao( MovimentoConta movimento, int cdEmpresa, Connection connection ) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			if( movimento == null){
				if(isConnectionNull)
					connection.rollback();
				return new Result(-1, "Movimento não informado.");
			}
			Result result =  new Result(1);
			//ATUALIZA CATEGORIA DA MOVIMENTAÇÃO
			connection.prepareStatement(
						" UPDATE ADM_MOVIMENTO_CONTA_CATEGORIA "+
						" SET VL_MOVIMENTO_CATEGORIA = "+movimento.getVlMovimento()+
						" WHERE CD_MOVIMENTO_CONTA = "+movimento.getCdMovimentoConta()+
						" AND CD_CONTA = "+movimento.getCdConta()
					).executeUpdate();
			
			//Atualizar conta a receber associada
			connection.prepareStatement(
					" UPDATE ADM_CONTA_RECEBER "+
					" SET VL_CONTA = "+movimento.getVlMovimento()+", "+
					" 	  VL_RECEBIDO = "+movimento.getVlMovimento()+", "+
					" 	  NR_DOCUMENTO = '"+movimento.getNrDocumento()+"'"+
						" FROM ( "+
						"     SELECT * FROM ADM_MOVIMENTO_CONTA_RECEBER "+
						"     WHERE CD_MOVIMENTO_CONTA = "+movimento.getCdMovimentoConta()+
						"     AND CD_CONTA = "+movimento.getCdConta()+
						"     LIMIT 1 "+
						" ) AS TMP"+
					" WHERE ADM_CONTA_RECEBER.CD_CONTA_RECEBER = TMP.CD_CONTA_RECEBER "
					).executeUpdate();
			
			connection.prepareStatement(
					" UPDATE ADM_CONTA_RECEBER_CATEGORIA A "+
							" SET VL_CONTA_CATEGORIA = "+movimento.getVlMovimento()+
							" FROM ( "+
							"     SELECT * FROM ADM_MOVIMENTO_CONTA_RECEBER "+
							"     WHERE CD_MOVIMENTO_CONTA = "+movimento.getCdMovimentoConta()+
							"     AND CD_CONTA = "+movimento.getCdConta()+
							"     LIMIT 1 "+
							" ) AS TMP"+
							" WHERE TMP.CD_CONTA_RECEBER = A.CD_CONTA_RECEBER "
					).executeUpdate();
			//Atualizar transferencia Associada
			ResultSet rs = connection.prepareStatement(
					" SELECT * FROM adm_movimento_conta A "+
					" WHERE A.cd_conta      = " +movimento.getCdConta()+
					"   AND A.cd_fechamento = " +movimento.getCdFechamento()+
					"   AND A.cd_movimento_origem = "+ movimento.getCdMovimentoConta()  ).executeQuery();
			if( rs.next() ){
				MovimentoConta movTransfOrigem = MovimentoContaDAO.get(rs.getInt("CD_MOVIMENTO_CONTA"), rs.getInt("CD_CONTA"), connection);
				movTransfOrigem.setVlMovimento( movimento.getVlMovimento() );
				movTransfOrigem.setDtMovimento( movimento.getDtDeposito() );
				movTransfOrigem.setNrDocumento( movimento.getNrDocumento() );
				MovimentoContaDAO.update(movTransfOrigem, connection);
				
				ResultSetMap rsmMovDestino = MovimentoContaServices.getDestinoAsResultSet(movTransfOrigem.getCdConta(), movTransfOrigem.getCdMovimentoConta(), connection);
				rsmMovDestino.next();
				MovimentoConta movTransfDestino = MovimentoContaDAO.get(rsmMovDestino.getInt("CD_MOVIMENTO_CONTA"), rsmMovDestino.getInt("CD_CONTA"), connection);
				movTransfDestino.setVlMovimento( movimento.getVlMovimento() );
				movTransfDestino.setDtMovimento( movimento.getDtDeposito() );
				movTransfDestino.setNrDocumento( movimento.getNrDocumento() );
				MovimentoContaDAO.update(movTransfDestino, connection);
			}
			//Atualizar Título de Crédito
			connection.prepareStatement(
					" UPDATE ADM_TITULO_CREDITO "+
					" SET VL_TITULO = "+movimento.getVlMovimento()+
						" FROM ( "+
						"     SELECT * FROM ADM_MOVIMENTO_TITULO_CREDITO "+
						"     WHERE CD_MOVIMENTO_CONTA = "+movimento.getCdMovimentoConta()+
						"     AND CD_CONTA = "+movimento.getCdConta()+
						"     LIMIT 1 "+
						" ) AS TMP "+
					" WHERE ADM_TITULO_CREDITO.CD_TITULO_CREDITO = TMP.CD_TITULO_CREDITO "
					).executeUpdate();
			
			//Atualizar Parcelas de cartão de crédito
			FormaPagamento formaPag = FormaPagamentoDAO.get(movimento.getCdFormaPagamento(), connection);
			if( formaPag.getTpFormaPagamento() == FormaPagamentoServices.TEF ){
				
				
				
				int qtParcelas    = 1;
				Double vlDesconto  = 0.0; 
				Double prTaxaDesconto  = 0.0;
				Double vlParcela  = movimento.getVlMovimento()/qtParcelas;
				
				FormaPagamentoEmpresa formaPagEmpresa = FormaPagamentoEmpresaDAO.get(  movimento.getCdFormaPagamento(), cdEmpresa, connection);
				prTaxaDesconto = formaPagEmpresa.getPrTaxaDesconto();
				vlDesconto = vlParcela*(prTaxaDesconto/100);
								
				if( formaPagEmpresa.getCdTipoDocumento() == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_CARTAO_CREDITO", 0,  0, connection) ){
					if( movimento.getCdPlanoPagamento() <= 0 ){
						if(isConnectionNull)
							connection.rollback();
						return new Result(-10, "Plano de Pagamento não informado.");
					}
					ResultSetMap rsm = new ResultSetMap( connection.prepareStatement(
							" SELECT * FROM ADM_FORMA_PLANO_PAGAMENTO A "+
							" JOIN ADM_PLANO_PAGAMENTO B ON ( B.CD_PLANO_PAGAMENTO = A.CD_PLANO_PAGAMENTO )"+ 
							" JOIN ADM_PLANO_PARCELAMENTO C ON ( C.CD_PLANO_PAGAMENTO = B.CD_PLANO_PAGAMENTO )"+
							" WHERE  A.CD_FORMA_PAGAMENTO = "+movimento.getCdFormaPagamento()+
							" AND  A.CD_EMPRESA = "+cdEmpresa+
							" AND  A.CD_PLANO_PAGAMENTO = "+movimento.getCdPlanoPagamento()
						).executeQuery());
				
					rsm.next();
					qtParcelas = rsm.getInt("QT_PARCELAS");
					vlParcela = movimento.getVlMovimento()/qtParcelas;
					prTaxaDesconto = rsm.getDouble("PR_TAXA_DESCONTO");
					vlDesconto = vlParcela*(prTaxaDesconto/100);
					
				}

				 //atualiza valores das parcelas e dos títulos associados	
				 connection.prepareStatement(
						 " UPDATE ADM_CONTA_RECEBER "+
							" SET VL_CONTA = "+vlParcela+", "+
							"     VL_ABATIMENTO = "+vlDesconto+" "+
								" FROM ( "+
								"     SELECT * FROM ADM_MOVIMENTO_CONTA A "+
								"     JOIN ADM_CONTA_MOVIMENTO_ORIGEM B ON ( A.CD_CONTA = B.CD_CONTA AND A.CD_MOVIMENTO_CONTA = B.CD_MOVIMENTO_CONTA )  "+
								"     WHERE A.CD_MOVIMENTO_CONTA = "+movimento.getCdMovimentoConta()+
								"     AND A.CD_CONTA = "+movimento.getCdConta()+
								" ) AS TMP "+
							" WHERE ADM_CONTA_RECEBER.CD_CONTA_RECEBER = TMP.CD_CONTA_RECEBER "
						).executeUpdate();
				
				 connection.prepareStatement(
						 " UPDATE ADM_TITULO_CREDITO "+
							" SET VL_TITULO = "+vlParcela+
								" FROM ( "+
								"     SELECT * FROM ADM_MOVIMENTO_CONTA A "+
								"     JOIN ADM_CONTA_MOVIMENTO_ORIGEM B ON ( A.CD_CONTA = B.CD_CONTA AND A.CD_MOVIMENTO_CONTA = B.CD_MOVIMENTO_CONTA )  "+
								"     WHERE A.CD_MOVIMENTO_CONTA = "+movimento.getCdMovimentoConta()+
								"     AND A.CD_CONTA = "+movimento.getCdConta()+
								" ) AS TMP "+
							" WHERE ADM_TITULO_CREDITO.CD_CONTA_RECEBER = TMP.CD_CONTA_RECEBER "
						).executeUpdate();
				
			}
				
			
			//Atualizar movimentação
			int r = MovimentoContaDAO.update(movimento, connection);
			if( r <= 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao atualizar movimentação");
			}
			if(isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.desconectar(connection);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao atualizar movimentação", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
	 * Fluxo Pax
	 */
	public static Result reprovarMovimentacao( MovimentoConta movimento, FechamentoOcorrencia  ocorrencia ){
		return reprovarMovimentacao( movimento, ocorrencia, null );
	}
	public static Result reprovarMovimentacao( MovimentoConta movimento, FechamentoOcorrencia  ocorrencia, Connection connection ) {
		boolean isConnectionNull = connection==null;
		try {
			if ( isConnectionNull ) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			if( movimento == null ){
				if( isConnectionNull )
					connection.rollback();
				return new Result( -1, "Movimento não informado." );
			}
			
			//Reverter situação da movimentação, e registros associados para 'não compesado'
			
			
			ocorrencia.setDtOcorrencia( new GregorianCalendar( new Locale( "pt", "BR" ) ) );
			ContaFechamento fechamento = ContaFechamentoDAO.get( ocorrencia.getCdConta(),
																 ocorrencia.getCdFechamento(),
																 connection );
			String dsOcorrencia = ( fechamento.getTxtObservacao().equals("null") ) ? "" : fechamento.getTxtObservacao() ;
			dsOcorrencia += Util.formatDate( ocorrencia.getDtOcorrencia(), "dd/MM/yyyy HH:mm" );
			dsOcorrencia += " - "+ocorrencia.getDsOcorrencia();
			
			fechamento.setTxtObservacao( fechamento.getTxtObservacao()+"\n"+dsOcorrencia );
			
			Result resultFechamento = ContaFechamentoServices.save( fechamento, connection );
			Result result = FechamentoOcorrenciaServices.save( ocorrencia, connection );
			if( result.getCode() <= 0 || resultFechamento.getCode() <= 0 ){
				if( isConnectionNull )
					Conexao.rollback( connection );
				return new Result( -1, "Erro ao reprovar movimentação" );
			}
			if( isConnectionNull )
				connection.commit();
			
			return result;
		}
		catch( Exception e ) {
			if ( isConnectionNull )
				Conexao.desconectar( connection );
			com.tivic.manager.util.Util.registerLog( e );
			e.printStackTrace( System.out );
			return new Result( -1, "Erro ao atualizar movimentação", e );
		}
		finally {
			if ( isConnectionNull )
				Conexao.desconectar( connection );
		}
	}
	
	public static Result conferirMovimentacao( MovimentoConta movimento ){
		return conferirMovimentacao( movimento, null );
	}
	public static Result conferirMovimentacao( MovimentoConta movimento, Connection connection ) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			if( movimento == null){
				if(isConnectionNull)
					connection.rollback();
				return new Result(-1, "Movimento não informado.");
			}
			Result result =  new Result(1);
			
			connection.prepareStatement("UPDATE ADM_FECHAMENTO_OCORRENCIA SET CD_MOVIMENTO_CONTA = NULL"+
										" WHERE CD_CONTA = "+movimento.getCdConta()+
										" AND CD_MOVIMENTO_CONTA = "+movimento.getCdMovimentoConta()
					).executeUpdate();
			result = MovimentoContaServices.confirmarMovimento(
												movimento.getCdConta(),
												movimento.getCdMovimentoConta(),
												true,
												MovimentoContaServices.ST_COMPENSADO,
												connection);
			if( result.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao conferir movimentação");
			}
			if(isConnectionNull)
				connection.commit();
			
			return result;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.desconectar(connection);
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao atualizar movimentação", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * 
	 * Remove uma movimentação que está em processo de validação para fechamento de caixa
	 * Fluxo Pax
	 * @param fechamento
	 * @param cdMovimentoConta
	 * @param cdUsuario
	 * @param connection
	 * @return
	 */
	public static Result removerMovimentacao( ContaFechamento fechamento, int cdMovimentoConta, int cdUsuario ) {
		return removerMovimentacao(fechamento, cdMovimentoConta, cdUsuario, null);
	}
	public static Result removerMovimentacao( ContaFechamento fechamento, int cdMovimentoConta, int cdUsuario, Connection connection ) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			Result result =  new Result(1);
			ArrayList<Integer> contasReceber = new ArrayList<Integer>();
			// Movimentações vinculadas ao fechamento
			ResultSetMap movimentos = new ResultSetMap(connection.prepareStatement(
											" SELECT * FROM adm_movimento_conta_receber A "+
											" JOIN adm_movimento_conta b on ( A.cd_conta = B.cd_conta AND A.cd_movimento_conta = B.cd_movimento_conta )  "+
							                " WHERE B.cd_conta      = " +fechamento.getCdConta()+
											"   AND B.cd_fechamento = "+fechamento.getCdFechamento()+
											"   AND B.cd_movimento_conta = "+cdMovimentoConta ).executeQuery() );
			while ( movimentos.next() ) {
				contasReceber.add( movimentos.getInt("CD_CONTA_RECEBER") );
				result = MovimentoContaServices.remove( 
													movimentos.getInt("CD_CONTA"),
													movimentos.getInt("CD_MOVIMENTO_CONTA"),
													cdUsuario, true, connection);
				result = ContaReceberServices.remove( movimentos.getInt("CD_CONTA_RECEBER"), true, true, connection );
				if( result.getCode() <= 0 ){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-2, "Erro ao remover contas a receber associadas.");
				}
				
				/**
				 * Remove transferências associadas
				 */
				ResultSetMap rsmMovAssociada = 	 new ResultSetMap(connection.prepareStatement(
									" SELECT * FROM adm_movimento_conta A "+
									" WHERE A.cd_conta      = " +fechamento.getCdConta()+
									"   AND A.cd_fechamento = "+fechamento.getCdFechamento()+
									"   AND A.cd_movimento_origem = "+ cdMovimentoConta  ).executeQuery() );
				if( rsmMovAssociada != null){
					while( rsmMovAssociada.next() ){
						/**
						 * Altera a transferencia para nao comppensado e desassocia do fechamento
						 * ara que o usuario não administrador possa excluí-lo
						 */
						MovimentoConta movTransf = MovimentoContaDAO.get(rsmMovAssociada.getInt("CD_MOVIMENTO_CONTA"), rsmMovAssociada.getInt("CD_CONTA"));
						movTransf.setStMovimento( MovimentoContaServices.ST_NAO_COMPENSADO );
						movTransf.setCdFechamento(0);
						
						MovimentoContaDAO.update(movTransf, connection);
						result = MovimentoContaServices.remove(rsmMovAssociada.getInt("CD_CONTA"),
													rsmMovAssociada.getInt("CD_MOVIMENTO_CONTA"),
													cdUsuario, true, connection);
						if( result.getCode() <= 0 ){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-2, "Erro ao remover movimentações associadas.");
						}
					}
				}
				
			}
			
			if( result.getCode()<=0 ){
				if(!isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao remover fechamento.");
			}
			if(isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir movimentação!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Chamada sobrescrita para contornar a palavra reservada 'delete' do Flex
	 * @param cdConta
	 * @param cdFechamento
	 * @return
	 */
	public static Result remove(int cdConta, int cdFechamento) {
		return  delete(cdConta, cdFechamento, null);
	}
	public static Result remove(int cdConta, int cdFechamento, Connection connection) {
		return  delete(cdConta, cdFechamento, connection);
	}
	public static Result delete(int cdConta, int cdFechamento) {
		return delete(cdConta, cdFechamento, null);
	}

	public static Result delete(int cdConta, int cdFechamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? true : connection.getAutoCommit());
			// Volta situação de fechado para conferido
			connection.prepareStatement("UPDATE adm_movimento_conta SET st_movimento = " +MovimentoContaServices.ST_CONFERIDO+
						                " WHERE cd_conta      = " +cdConta+
										"   AND cd_fechamento = " +cdFechamento+
										"   AND st_movimento  = "+MovimentoContaServices.ST_CX_FECHADO).executeUpdate();
			// Apaga informação ligação dos movimentos com o fechamento
			connection.prepareStatement("UPDATE adm_movimento_conta SET cd_fechamento = null "+
						                " WHERE cd_conta      = " +cdConta+
										"   AND cd_fechamento = " +cdFechamento).executeUpdate();
			// Exclui dados dos TÍTULOS vinculados ao fechamento
			connection.prepareStatement("DELETE FROM adm_conta_fechamento_tit_cred "+
						                " WHERE cd_conta      = " +cdConta+
										"   AND cd_fechamento = " +cdFechamento).executeUpdate();
			// Exclui dados dos saldos em TÍTULOS de crédito
			connection.prepareStatement("DELETE FROM adm_conta_fechamento_tipo_doc "+
						                " WHERE cd_conta      = " +cdConta+
										"   AND cd_fechamento = " +cdFechamento).executeUpdate();
			// Exclui dados dos bicos
			connection.prepareStatement("DELETE FROM pcb_bico_encerrante "+
						                " WHERE cd_conta      = " +cdConta+
										"   AND cd_fechamento = " +cdFechamento).executeUpdate();
			// Exclui dados da medição física
			connection.prepareStatement("DELETE FROM pcb_medicao_fisica "+
						                " WHERE cd_conta      = " +cdConta+
										"   AND cd_fechamento = " +cdFechamento).executeUpdate();
			// EXCLUINDO O FECHAMENTO
			connection.prepareStatement("DELETE FROM adm_conta_fechamento "+
						                " WHERE cd_conta      = " +cdConta+
										"   AND cd_fechamento = " +cdFechamento).executeUpdate();
			if(!isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir fechamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getUsuariosBloqueados() {
		return getUsuariosBloqueados(null);
	}
	
	public static ResultSetMap getUsuariosBloqueados( Connection connection ) {
		boolean isConnectionNull = connection==null;
		try {
			if( isConnectionNull ){
				connection = Conexao.conectar();
			}
			
			ResultSetMap rsmUsuarioBloqueados = new ResultSetMap( 
							connection.prepareStatement(
									" SELECT * FROM GRL_PARAMETRO_VALOR A "+
									" JOIN SEG_USUARIO B ON ( to_number(a.vl_inicial, '0000') = B.CD_USUARIO  )"+
									" JOIN GRL_PESSOA C ON ( B.cd_pessoa = C.cd_pessoa ) "+
									" WHERE cd_parametro = "+ParametroServices.getByName("CD_USUARIOS_BLOQUEADOS_FECHAMENTO_CAIXA").getCdParametro()   ).executeQuery()
							);
			return rsmUsuarioBloqueados;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	public static Result setSituacao(int stContaFechamento, ContaFechamento fechamento, Usuario usuario) {
		return setSituacao(stContaFechamento, fechamento, usuario, null);
	}

	public static Result setSituacao(int stContaFechamento, ContaFechamento fechamento,  Usuario usuario, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if( isConnectionNull ){
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			if( fechamento == null || usuario == null ){
				if (isConnectionNull)
					Conexao.desconectar(connection);
				return new Result(-2, "Fechamento ou usuário não informado.");
			}
			
			fechamento.setStFechamento(stContaFechamento);
			Result result = ContaFechamentoServices.save(fechamento, connection);
			
			if( result.getCode() <= 0 ){
				if (isConnectionNull)
					Conexao.desconectar(connection);
				return new Result(-1, "Erro ao atualizar situação do fechamento.");
			}
			
			FechamentoOcorrencia ocorrencia = new FechamentoOcorrencia(0/*cdFechamentoOcorrencia*/, 
																	usuario.getCdUsuario(),
																	fechamento.getCdFechamento(),
																	fechamento.getCdConta(), 
																	"Fechamento atualizado para '"+situacaoContaFechamento[stContaFechamento]+"'",
																	new GregorianCalendar(new Locale("pt", "BR")), 0);
			result = FechamentoOcorrenciaServices.save(ocorrencia, connection);
			
			if( result.getCode() <= 0 ){
				if (isConnectionNull)
					Conexao.desconectar(connection);
				return new Result(-1, "Erro ao atualizar situação do fechamento.");
			}
			
			if (isConnectionNull)
				connection.commit();
			return new Result(1, "Fechamento atualizado para '"+situacaoContaFechamento[stContaFechamento]+"' com sucesso. ");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return new Result(-1, "Erro ao mudar situação do fechamento.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/*
	 * Retorna o valor do fechado do dia anterior = Saldo anterior
	 */
	public static float getSaldoAnterior(int cdConta, GregorianCalendar dtFechamento) {
		return getSaldoAnterior(cdConta, dtFechamento, null);
	}

	public static float getSaldoAnterior(int cdConta, GregorianCalendar dtFechamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT vl_fechamento " +
																  "FROM adm_conta_fechamento " +
																  "WHERE cd_conta = ? " +
																  "  AND dt_fechamento = (SELECT MAX(dt_fechamento) " +
																  "						  FROM adm_conta_fechamento " +
															 	  "						  WHERE dt_fechamento < ?)");
			pstmt.setInt(1, cdConta);
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtFechamento));
			ResultSet rs = pstmt.executeQuery();
			return rs.next() ? rs.getFloat(1) : 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getResumoBySaida(int cdConta, GregorianCalendar dtFechamento, int cdTurno) {
		return getResumoBySaida(cdConta, dtFechamento, cdTurno, null);
	}
	public static ResultSetMap getResumoBySaida(int cdConta, GregorianCalendar dtFechamento, int cdTurno, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT B.cd_forma_pagamento, nm_forma_pagamento, tp_forma_pagamento, " +
					                                              "       COUNT(*) AS qt_pagamento, SUM(vl_pagamento) AS vl_pagamento " +
																  "FROM alm_documento_saida A " +
																  "JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
																  "JOIN adm_forma_pagamento             C ON (B.cd_forma_pagamento = C.cd_forma_pagamento) " +
																  "WHERE A.cd_conta                       = "+cdConta+
																  "  AND CAST(dt_documento_saida AS DATE) = ? " +
																  "  AND A.cd_turno                       = "+cdTurno+
																  "GROUP BY 1, 2, 3" +
																  "ORDER BY nm_forma_pagamento DESC ");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtFechamento));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			// Somando Descontos
			String sql = "SELECT COUNT(*) AS qt_total_desconto, SUM(DSI.vl_desconto) AS vl_total_desconto "+
				  "FROM alm_documento_saida_item     DSI " +
				  "INNER JOIN alm_documento_saida    DS ON (DS.cd_documento_saida = DSI.cd_documento_saida) "+
				  "WHERE DS.cd_turno                         = " +cdTurno+
				  "  AND DS.cd_conta                         = " +cdConta+
				  "  AND CAST(DS.dt_documento_saida AS DATE) = ? ";
			pstmt = connection.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			ResultSetMap rs = new ResultSetMap(pstmt.executeQuery());
			rs.next();
			if(rs.getFloat("vl_total_desconto") > 0) {
				HashMap<String, Object> register   = new HashMap<String, Object>();
				register.put("CD_FORMA_PAGAMENTO", -20);
				register.put("NM_FORMA_PAGAMENTO", "Descontos Concedidos");
				register.put("QT_PAGAMENTO", rs.getFloat("qt_total_desconto"));
				register.put("VL_PAGAMENTO", rs.getFloat("vl_total_desconto"));
				rsm.addRegister(register);
			}
			// Descontos Especiais
			ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connection);
			int cdTabelaPreco  = 0;
			int cdTipoOperacao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, conta!=null ? conta.getCdEmpresa() : 0, connection);
			TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacao, connection);
			if(tipoOperacao!=null)
				cdTabelaPreco = tipoOperacao.getCdTabelaPreco();
			sql = " SELECT B.cd_documento_saida, B.dt_documento_saida, B.vl_total_documento, D.nm_pessoa, B.nr_documento_saida, " +
			      "        F.nm_pessoa AS nm_usuario, A.vl_unitario, A.qt_saida, " +
			      "       (SELECT vl_preco FROM adm_produto_servico_preco PR "+ 
				  "        WHERE PR.cd_produto_servico = A.cd_produto_servico "+
				  "          AND PR.cd_tabela_preco      = "+cdTabelaPreco+
				  "          AND (((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                 WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                   AND PR.cd_produto_servico = A.cd_produto_servico "+
				  "                   AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NOT NULL " +
				  "                   AND PR.dt_termino_validade = (SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                                                 WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                                                   AND PR.cd_produto_servico = A.cd_produto_servico "+
				  "                                                   AND PR.cd_tabela_preco      = "+cdTabelaPreco+")) OR "+
				  "               ((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                 WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                   AND PR.cd_produto_servico  = A.cd_produto_servico "+
				  "                   AND PR.cd_tabela_preco     = "+cdTabelaPreco+") IS NULL AND PR.dt_termino_validade IS NULL))) AS vl_preco_tabela "+
				  "FROM alm_documento_saida_item A, alm_documento_saida B "+
			      "LEFT OUTER JOIN grl_pessoa  D ON (B.cd_cliente   = D.cd_pessoa) "+
				  "LEFT OUTER JOIN seg_usuario E ON (B.cd_digitador = E.cd_usuario) " +
				  "LEFT OUTER JOIN grl_pessoa  F ON (F.cd_pessoa    = E.cd_pessoa) "+
				  "WHERE CAST(B.dt_documento_saida AS DATE) = ? " +
				  "  AND B.cd_conta                         = "+cdConta+
				  "  AND B.cd_turno                         = "+cdTurno+
				  "  AND A.cd_documento_saida               = B.cd_documento_saida "+
				  "  AND A.vl_unitario < (SELECT vl_preco FROM adm_produto_servico_preco PR "+ 
				  "                       WHERE PR.cd_produto_servico = A.cd_produto_servico "+
				  "                         AND PR.cd_tabela_preco      = "+cdTabelaPreco+
				  "                         AND (((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                                WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                                  AND PR.cd_produto_servico = A.cd_produto_servico "+
				  "                                  AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NOT NULL" +
				  "                         AND PR.dt_termino_validade = (SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                                                       WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                                                         AND PR.cd_produto_servico = A.cd_produto_servico "+
				  "                                                         AND PR.cd_tabela_preco      = "+cdTabelaPreco+")) OR "+
				  "                              ((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
				  "                                WHERE PR.dt_termino_validade > B.dt_documento_saida "+
				  "                                  AND PR.cd_produto_servico = A.cd_produto_servico "+
				  "                                  AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NULL AND PR.dt_termino_validade IS NULL))) ";
			pstmt = connection.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			rs = new ResultSetMap(pstmt.executeQuery());
			float vlDesconto  = 0;
			int   qtDescontos = 0;      
			while(rs.next()) {
				qtDescontos++;
				//
				float vlTotalItem    = rs.getFloat("vl_unitario") * rs.getFloat("qt_saida");
				float vlTotalItemTab = (rs.getFloat("vl_preco_tabela") * rs.getFloat("qt_saida"));
				float vlDescontoItem = vlTotalItemTab - vlTotalItem;
				//
				vlDesconto += vlDescontoItem;
			}
			if(vlDesconto > 0) {
				HashMap<String, Object> register   = new HashMap<String, Object>();
				register.put("CD_FORMA_PAGAMENTO", -30);
				register.put("NM_FORMA_PAGAMENTO", "Descontos Especiais");
				register.put("QT_PAGAMENTO", qtDescontos);
				register.put("VL_PAGAMENTO", vlDesconto);
				rsm.addRegister(register);
			}
			// Somando e calculando %
			float vlTotal = 0;
			rsm.beforeFirst();
			while(rsm.next()) 
				vlTotal += rsm.getFloat("vl_pagamento");
			rsm.beforeFirst();
			while(rsm.next()) 
				rsm.setValueToField("PR_PAGAMENTO", rsm.getFloat("vl_pagamento") / vlTotal * 100);
			//
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getResumoFormaPagamentoFinanceiro(int cdConta, int cdFechamento, GregorianCalendar dtFechamento, int cdTurno) {
		return getResumoFormaPagamentoFinanceiro(cdConta, cdFechamento, dtFechamento, cdTurno, null);
	}
	public static ResultSetMap getResumoFormaPagamentoFinanceiro(int cdConta, int cdFechamento, GregorianCalendar dtFechamento, int cdTurno, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			
			String sql = " SELECT B.cd_forma_pagamento, nm_forma_pagamento, B.tp_movimento, D.nm_plano_pagamento, tp_forma_pagamento, SUM(vl_movimento) AS vl_total, "+
					" SUM(vl_movimento) AS vl_movimento, COUNT(B.cd_forma_pagamento) AS QT_PAGAMENTO, 0 AS PR_PAGAMENTO " +
					" FROM adm_movimento_conta       B "+
					" JOIN adm_forma_pagamento       C on ( B.cd_forma_pagamento = C.cd_forma_pagamento )"+
					" LEFT JOIN adm_plano_pagamento       D on ( B.cd_plano_pagamento = D.cd_plano_pagamento )"+
					" WHERE B.cd_conta = "+cdConta+
					"  AND  B.cd_turno  = "+cdTurno+
					"  AND  B.cd_fechamento  = "+cdFechamento+
					"  AND  CAST(B.dt_movimento AS DATE) = ? "+
					" GROUP BY B.cd_forma_pagamento, B.tp_movimento, nm_forma_pagamento, nm_plano_pagamento, tp_forma_pagamento "+
					" ORDER BY B.cd_forma_pagamento ";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if( rsm != null ){
				rsm.beforeFirst();
				while( rsm.next() ){
					if( rsm.getInt("CD_PLANO_PAGAMENTO") == 0 )
						rsm.setValueToField("NM_PLANO_PAGAMENTO", "A VISTA");
				}
			}
//			Double vlTotal = 0.0;
//			while(rsm.next()){
//				if(rsm.getInt("tp_movimento")==MovimentoContaServices.CREDITO){
//					rsm.setValueToField("PR_PAGAMENTO", Double.valueOf(rsm.getString("PR_PAGAMENTO")) + (rsm.getDouble("vl_diferenca") / vlTotal) * 100);
//					rsm.setValueToField("QT_PAGAMENTO", Double.valueOf(rsm.getString("QT_PAGAMENTO"))+ 1);
//					rsm.setValueToField("VL_PAGAMENTO", rsm.getDouble("VL_PAGAMENTO") + rsm.getDouble("vl_diferenca"));
//				}
//				else{
//					rsm.setValueToField("PR_PAGAMENTO", Double.valueOf(rsm.getString("PR_PAGAMENTO")) - (rsm.getDouble("vl_diferenca") / vlTotal) * 100);
//					rsm.setValueToField("QT_PAGAMENTO", Double.valueOf(rsm.getString("QT_PAGAMENTO"))- 1);
//					rsm.setValueToField("VL_PAGAMENTO", rsm.getDouble("VL_PAGAMENTO") - rsm.getDouble("vl_diferenca"));
//				}
//			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	public static ResultSetMap getResumoByPagamento(int cdConta, GregorianCalendar dtFechamento, int cdTurno) {
		return getResumoByPagamento(cdConta, dtFechamento, cdTurno, null);
	}
	public static ResultSetMap getResumoByPagamento(int cdConta, GregorianCalendar dtFechamento, int cdTurno, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			//Buscando formas de pagamento usadas nos documentos de saidas
			PreparedStatement pstmt = connection.prepareStatement("SELECT B.cd_forma_pagamento, nm_forma_pagamento, tp_forma_pagamento, SUM(vl_pagamento) AS vl_total, "
					+ "0 AS vl_pagamento, SUM(vl_pagamento) AS vl_diferenca, 0 AS QT_PAGAMENTO, 0 AS PR_PAGAMENTO " +
					  "FROM alm_documento_saida A " +
					  "JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
					  "JOIN adm_forma_pagamento             C ON (B.cd_forma_pagamento = C.cd_forma_pagamento) " +
					  "WHERE A.cd_conta                       = "+cdConta+
					  "  AND CAST(dt_documento_saida AS DATE) = ? " +
					  "  AND A.cd_turno                       = "+cdTurno+
					  "GROUP BY 1, 2, 3 " +
					  "ORDER BY B.cd_forma_pagamento ");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtFechamento));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			// Calcula o total
			Double vlTotal = 0.0;
			while(rsm.next())
				vlTotal += rsm.getDouble("vl_total");
			rsm.beforeFirst();
			// Somando pagamentos
			String sql = " SELECT B.cd_forma_pagamento, vl_movimento, tp_movimento, 0 AS QT_PAGAMENTO, 0 AS PR_PAGAMENTO " +
					     " FROM adm_movimento_conta       B "+
						 " WHERE B.cd_conta = "+cdConta+
						 "  AND B.cd_turno = "+cdTurno+
						 "  AND CAST(B.dt_movimento AS DATE) = ? "+
						 "ORDER BY B.cd_forma_pagamento ";
			pstmt = connection.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			ResultSetMap rs = new ResultSetMap(pstmt.executeQuery());
			while(rs.next())	{
				if(rsm.locate("CD_FORMA_PAGAMENTO", rs.getInt("cd_forma_pagamento"))){
					if(rs.getInt("tp_movimento")==MovimentoContaServices.CREDITO){
						rsm.setValueToField("PR_PAGAMENTO", Float.valueOf(rsm.getString("PR_PAGAMENTO")) + (rs.getFloat("vl_movimento") / vlTotal) * 100);
						rsm.setValueToField("QT_PAGAMENTO", Double.valueOf(rsm.getString("QT_PAGAMENTO"))+ 1);
						rsm.setValueToField("VL_PAGAMENTO", rsm.getFloat("VL_PAGAMENTO") + rs.getFloat("vl_movimento"));
					}
					else{
						rsm.setValueToField("PR_PAGAMENTO", Float.valueOf(rsm.getString("PR_PAGAMENTO")) - (rs.getFloat("vl_movimento") / vlTotal) * 100);
						rsm.setValueToField("QT_PAGAMENTO", Double.valueOf(rsm.getString("QT_PAGAMENTO"))- 1);
						rsm.setValueToField("VL_PAGAMENTO", rsm.getFloat("VL_PAGAMENTO") - rs.getFloat("vl_movimento"));
					}
				}
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getResumoOfTpPagamento(int cdConta, GregorianCalendar dtFechamento,int cdTurno, boolean dinheiro){
		return getResumoOfTpPagamento(cdConta, dtFechamento, cdTurno,dinheiro, null);
	}
	
	public static ResultSetMap getResumoOfTpPagamento(int cdConta, GregorianCalendar dtFechamento,int cdTurno,boolean dinheiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
				PreparedStatement pstmt = connect.prepareStatement("SELECT tp_forma_pagamento,C.cd_forma_pagamento, nm_forma_pagamento, nm_plano_pagamento, " +
	                    "       COUNT(*) AS qt_pagamento, SUM(vl_pagamento) AS vl_pagamento " +
						  "FROM alm_documento_saida A " +
						  "JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
						  "JOIN adm_forma_pagamento             C ON (B.cd_forma_pagamento = C.cd_forma_pagamento) " +
						  "JOIN adm_plano_pagamento             D ON (B.cd_plano_pagamento = D.cd_plano_pagamento) " +
						  "WHERE A.cd_conta                       = "+cdConta+
						  "  AND CAST(dt_documento_saida AS DATE) < ? " +
			   (cdTurno>0?"  AND A.cd_turno                       = "+cdTurno:"")+
	     (dinheiro==false?"  AND C.tp_forma_pagamento             <> "+FormaPagamentoServices.MOEDA_CORRENTE:"")+
						  "GROUP BY 1, 2, 3, 4" +
						  "ORDER BY tp_forma_pagamento, nm_forma_pagamento, nm_plano_pagamento DESC ");
				pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtFechamento));
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

				String sqlEntradas = "SELECT tp_forma_pagamento, nm_forma_pagamento, nm_plano_pagamento, " +
									 "       COUNT(*) AS qt_pagamento, SUM(vl_pagamento) AS vl_pagamento " +
									 "FROM alm_documento_saida A " +
									 "JOIN adm_plano_pagto_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
									 "JOIN adm_forma_pagamento             C ON (B.cd_forma_pagamento = C.cd_forma_pagamento) " +
									 "JOIN adm_plano_pagamento             D ON (B.cd_plano_pagamento = D.cd_plano_pagamento) " +
									 "WHERE A.cd_conta                       = "+cdConta+
									 "  AND CAST(dt_documento_saida AS DATE) = ? " +
						  (cdTurno>0?"  AND A.cd_turno                       = "+cdTurno:"")+
						  			 "  AND C.cd_forma_pagamento = ? " +
							         "GROUP BY 1, 2, 3" +
									 "ORDER BY tp_forma_pagamento, nm_forma_pagamento, nm_plano_pagamento DESC ";
				
				String sqlSaidas = "SELECT SUM(MCP.vl_pago) as vl_pago " +
								   " FROM adm_conta_pagar CP " +
								   " JOIN adm_movimento_conta_pagar MCP ON(CP.cd_conta_pagar = MCP.cd_conta_pagar) " +
								   " JOIN adm_movimento_conta       MC  ON(MCP.cd_movimento_conta=MC.cd_movimento_conta) " +
								   " WHERE MC.cd_forma_pagamento = ? " +
								   " AND CAST(CP.dt_pagamento AS DATE) = ? " +
								   " AND CP.cd_conta = " + cdConta +
								   " AND CP.st_conta = " + ContaPagarServices.ST_PAGA;
				
				while(rsm.next()){
					PreparedStatement pstmtEntradas = connect.prepareStatement(sqlEntradas);
					pstmtEntradas.setTimestamp(1, Util.convCalendarToTimestamp(dtFechamento));
					pstmtEntradas.setInt(2, rsm.getInt("CD_FORMA_PAGAMENTO"));
					ResultSet rsEntradas = pstmtEntradas.executeQuery();
					if(rsEntradas.next())
						rsm.setValueToField("QT_ENTRADA", rsEntradas.getDouble("vl_pagamento"));
					else
						rsm.setValueToField("QT_ENTRADA", 0);
					
					PreparedStatement pstmtSaidas = connect.prepareStatement(sqlSaidas);
					pstmtSaidas.setInt(1, rsm.getInt("CD_FORMA_PAGAMENTO"));
					pstmtSaidas.setTimestamp(2, Util.convCalendarToTimestamp(dtFechamento));
					ResultSet rsSaidas = pstmtSaidas.executeQuery();
					if(rsSaidas.next())
						rsm.setValueToField("QT_SAIDA", rsSaidas.getDouble("vl_pago"));
					else
						rsm.setValueToField("QT_SAIDA", 0);
					
				}
				return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getRelatorioConsolidacaoAcumuladoReceber(ResultSetMap rsmFechamentos){
		return getRelatorioConsolidacaoAcumuladoReceber(rsmFechamentos, null);
	}
	
	public static ResultSetMap getRelatorioConsolidacaoAcumuladoReceber(ResultSetMap rsmFechamentos, Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmAcumuladoReceber = new ResultSetMap();
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			while(rsmFechamentos.next()){
				ResultSetMap rsmTmpAcumuladoReceber = ContaFechamentoServices.getResumoOfTpPagamento( rsmFechamentos.getInt("CD_CONTA"), rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO"), rsmFechamentos.getInt("CD_TURNO"), false);
				rsmTmpAcumuladoReceber.beforeFirst();
				while( rsmTmpAcumuladoReceber.next() ){
					boolean hasRegister = false;
					while( rsmAcumuladoReceber.next() ){
						if( rsmAcumuladoReceber.getInt("CD_FORMA_PAGAMENTO") == rsmTmpAcumuladoReceber.getInt("CD_FORMA_PAGAMENTO") 
							&& rsmAcumuladoReceber.getString("NM_FORMA_PAGAMENTO").equals(rsmTmpAcumuladoReceber.getString("NM_FORMA_PAGAMENTO")) ){
							//CONTABILIZA TOTAL E SINALIZA QUE JÁ POSSUI O REGISTRO NO RSM
							Float vlPagamento = rsmAcumuladoReceber.getFloat("VL_PAGAMENTO")+rsmTmpAcumuladoReceber.getFloat("VL_PAGAMENTO");
							Double qtEntrada = rsmAcumuladoReceber.getDouble("QT_ENTRADA")+rsmTmpAcumuladoReceber.getDouble("QT_ENTRADA");
							Double qtSaida = rsmAcumuladoReceber.getDouble("QT_SAIDA")+rsmTmpAcumuladoReceber.getDouble("QT_SAIDA");
							
							rsmAcumuladoReceber.setValueToField("VL_PAGAMENTO", vlPagamento);
							rsmAcumuladoReceber.setValueToField("QT_ENTRADA", qtEntrada);
							rsmAcumuladoReceber.setValueToField("QT_SAIDA", qtSaida);
							hasRegister = true;
						}
					}
					if(!hasRegister){
						rsmAcumuladoReceber.addRegister( rsmTmpAcumuladoReceber.getRegister() );
					}
					rsmAcumuladoReceber.beforeFirst();
				}
			}
			return rsmAcumuladoReceber;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getRelatorioConsolidacaoRetiradas(ResultSetMap rsmFechamentos){
		return getRelatorioConsolidacaoRetiradas(rsmFechamentos, null);
	}
	
	public static ResultSetMap getRelatorioConsolidacaoRetiradas(ResultSetMap rsmFechamentos, Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmRetiradas = new ResultSetMap();
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			while(rsmFechamentos.next()){
				ResultSetMap rsmTmpRetiradas = ContaFechamentoServices.getTransferenciasOf( rsmFechamentos.getInt("CD_CONTA"), rsmFechamentos.getInt("CD_FECHAMENTO"), rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO"), rsmFechamentos.getInt("CD_TURNO"), false);
				rsmTmpRetiradas.beforeFirst();
				ContaFinanceira conta;
				HashMap<String, Object> register;
				while( rsmTmpRetiradas.next() ){
					if(rsmTmpRetiradas.getInt("TP_ORIGEM") == 7)	{
						conta = ContaFinanceiraDAO.get(rsmTmpRetiradas.getInt("CD_CONTA_DESTINO"));
						if(conta.getTpConta() == ContaFinanceiraServices.TP_CONTA_BANCARIA){
							register = new HashMap<String,Object>();
							register.put("NM_CONTA_FINANCEIRA", conta.getNmConta());
							register.put("TP_CONTA","CONTA BANCÁRIA");
							register.put("ID_EXTRATO",rsmTmpRetiradas.getString("ID_EXTRATO"));
							if(conta.getCdResponsavel() > 0){
								Pessoa pessoaResponsavel = PessoaDAO.get(conta.getCdResponsavel());
								register.put("NM_RESPONSAVEL",pessoaResponsavel.getNmPessoa());
							}
							register.put("DS_HISTORICO",rsmTmpRetiradas.getString("DS_HISTORICO"));
							register.put("VL_MOVIMENTO",rsmTmpRetiradas.getFloat("VL_MOVIMENTO"));
							rsmRetiradas.addRegister(register);
						}
					}
				}
			}
			return rsmRetiradas;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result iniciarFechamento( ContaFechamento fechamento ) {
		return iniciarFechamento(fechamento, null);
	}
	public static Result iniciarFechamento( ContaFechamento fechamento, Connection connect ) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Result result = iniciarFechamento( fechamento.getCdConta(), fechamento.getDtFechamento(), fechamento.getCdResponsavel(), fechamento.getCdTurno(), connect);
			if( result.getCode() > 0 ){
				/**
				 * FLUXO PAX NACIONAL
				 */
				fechamento.setCdFechamento(result.getCode());
				Result resultSituacao = setSituacao( ST_EM_ANDAMENTO, fechamento, UsuarioDAO.get( fechamento.getCdResponsavel() ), connect);
				if( resultSituacao.getCode() <= 0 ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao tentar iniciar fechamento!");
				}
				result.addObject("CONTAFECHAMENTO", ContaFechamentoDAO.get(fechamento.getCdConta(), result.getCode()));
			}else{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar iniciar fechamento!");
			}
			if (isConnectionNull)
				connect.commit();
			return result;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return new Result(-1, "Erro ao tentar iniciar fechamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/*
	 * Inicia o fechamento de caixa, bloqueando novos lanamentos exceto as transferências que façam parte do próprio fechamento
	 */
	public static Result iniciarFechamento(int cdConta, GregorianCalendar dtFechamento, int cdUsuario, int cdTurno) {
		return iniciarFechamento(cdConta, dtFechamento, cdUsuario, cdTurno, null);
	}

	public static Result iniciarFechamento(int cdConta, GregorianCalendar dtFechamento, int cdUsuario, int cdTurno, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
			// Turno
			if (cdTurno <= 0)
				return new Result(-3, "Não é possível efetuar o fechamento de caixa da conta informada. Turno não informado.");

			// Verificações
			if (dtFechamento == null)
				return new Result(-3, "Não é possível efetuar o fechamento de caixa da conta informada. Data de fechamento não informada ou inválida.");

			if (cdUsuario <= 0)
				return new Result(ERR_FECH_CODE_USUARIO_NULL, "Não é possível efetuar o fechamento de caixa da conta informada para o dia " +
						          Util.formatDate(dtFechamento, "dd/MM/yyyy") + ". Usuário responsável pelo fechamento não identificado.");
			// Verifica se existem fechamentos posteriores
			/*
			PreparedStatement pstmt = connect.prepareCall("SELECT * FROM adm_conta_fechamento " +
					                                      "WHERE cd_conta      = "+cdConta+
					                           			  "  AND dt_fechamento > ? " +
					                           			  "ORDER BY dt_fechamento ASC ");
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next())
				return new Result(-10, "O fechamento do dia "+Util.formatDate(rsm.getGregorianCalendar("dt_fechamento"), "dd/MM/yyyy")+" ja foi realizado, nao permitido outro fechamento anterior!");
			*/
			// 
			ResultSetMap rsm = getFechamentoOf(cdConta, dtFechamento, cdTurno, connect);
			if(rsm.size()>0)
				return new Result(-10, "Já existe fechamento para o dia e turno informado!");
			/**
			 *  A data e hora é passada direto do PDV, verifique se é necessário alterar direto do web
			 */
//			dtFechamento.set(Calendar.HOUR_OF_DAY, 0);
//			dtFechamento.set(Calendar.MINUTE, 0);
//			dtFechamento.set(Calendar.SECOND, 0);
//			dtFechamento.set(Calendar.MILLISECOND, 0);
			/*
			 * Verifica se nao existe um pré-fechamento
			 */
			connect.setAutoCommit(false);
			ContaFechamento fechamento = null;
			int cdFechamento           = 0; 
			ResultSet rs = connect.prepareStatement("SELECT cd_fechamento FROM adm_conta_fechamento " +
													"WHERE cd_conta      = "+cdConta+
													"  AND cd_turno      = "+cdTurno+
													"  AND dt_fechamento IS NULL").executeQuery();

			
			Double vlSaldoAnterior =  ContaFinanceiraServices.getSaldoAnterior(cdConta, dtFechamento);
			if(rs.next())	{
				cdFechamento = rs.getInt("cd_fechamento");
				fechamento = ContaFechamentoDAO.get(cdConta, cdFechamento, connect);
				fechamento.setVlSaldoAnterior(vlSaldoAnterior);
				fechamento.setCdResponsavel(cdUsuario);
				fechamento.setDtFechamento(dtFechamento);
				ContaFechamentoDAO.update(fechamento, connect);
			}
			else	{
				fechamento = new ContaFechamento(cdConta,0/*cdFechamento*/,dtFechamento,0/*cdSupervisor*/,cdUsuario,0.0/*vlFechamento*/,
												0.0/*vlTotalEntradas*/,0.0/*vlTotalSaidas*/,""/**/,vlSaldoAnterior,
												cdTurno, ST_EM_ANDAMENTO);
				cdFechamento = ContaFechamentoDAO.insert(fechamento, connect);
			}
			// Encerrantes e Medição Física nos Tanques
			if(cdFechamento > 0  )	{
				BicoEncerranteServices.iniciarFechamento(cdConta, cdFechamento, conta.getCdEmpresa(), connect);
				//
				MedicaoFisicaServices.iniciarFechamento(cdConta, cdFechamento, conta.getCdEmpresa(), connect);
			}
			// Gravando
			connect.commit();
			return new Result(cdFechamento, (cdFechamento > 0) ? "Fechamento iniciado com sucesso!" : "Erro ao tentar iniciar fechamento!");
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return new Result(-1, "Erro ao tentar iniciar fechamento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * Fluxo PAX NACIONAL
	 * Executa o bloqueio dos usuários que não efetuaram a envio/correção/conferência do caixa 
	 *
	 * condições: 
	 *	Filtrar contas caixas não encerradas.
	 *	
	 *	Verificação executada para validar fechamentos do dia anterior
	 *	Fechamento diário executa verificação a meia noite.
	 *  Fechamento COB executa verificação ao meio dia.	
	 *
	 *  Para fechamentos iniciados:
	 *		Fechamento se encontra "Em Andamento" ou "Em Correção"
	 *  		-> Bloqueia o responsável.
	 *  
	 * 		Fechamento se encontra "Em conferência" com informação de supervisor
	 *  		-> Bloqueia o supervisor
	 *  
	 * 		Fechamento se encontra "Em congerência" sem informaçãp de supervisor
	 *  		-> Bloqueia todos com permissão de conferir/Concluir para a conta.
	 *    
	 *  Para Fechamentos não iniciados:
	 * 		-> Bloqueia usuários com permissao de iniciar para a conta
	 *    	
	 * 
	 * Obs. Configurar task no Manager.conf do cliente
	 *  
	 *  Final do dia para bloquear os fechamentos padrão
	 *  TASK1_CLASS=com.tivic.manager.adm.ContaFechamentoServices
	 *	TASK1_METHOD=bloquearFechamentosAtrasados()
	 *	TASK1_DATE=15-10-2016 00:00:00
	 *	TASK1_PERIOD=1
	 *
	 *  Ao meio dia para bloquear fechamentos cob
	 *  TASK2_CLASS=com.tivic.manager.adm.ContaFechamentoServices
	 *	TASK2_METHOD=bloquearFechamentosAtrasados()
	 *	TASK2_DATE=15-10-2016 12:01:00
	 *	TASK2_PERIOD=1
	 * @return
	 */
	public static Result bloquearFechamentosAtrasados(){
		Connection connection = Conexao.conectar();
		try{
			
			int cdUsuarioFechamento = ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_BLOQUEIO_FECHAMENTO_AUTOMATICO", 0, 0, connection);
			int cdTurnoPadrao = ParametroServices.getValorOfParametroAsInteger("CD_TURNO_FECHAMENTO_DEFAULT", 0, 0, connection);
			int cdTurnoCob = ParametroServices.getValorOfParametroAsInteger("CD_TURNO_FECHAMENTO_COB", 0, 0, connection);
			if( cdTurnoPadrao == 0 || cdTurnoCob == 0 ){
				return new Result(-2, "Erro ao bloquear usuários para fechamentos. Parâmetros obrigatórios não configurados.");
			}
			connection.setAutoCommit(false);
			
			ArrayList<Integer> cdUsuariosAbloquear = new ArrayList<Integer>();
			
			GregorianCalendar beginOfYesterday = new GregorianCalendar();
			beginOfYesterday.set(Calendar.HOUR, 0);
			beginOfYesterday.set(Calendar.MINUTE, 0);
			beginOfYesterday.set(Calendar.SECOND, 0);
			beginOfYesterday.add(Calendar.DATE, -1);
			
			GregorianCalendar endOfYesterday = new GregorianCalendar();
			endOfYesterday.set(Calendar.HOUR, 23);
			endOfYesterday.set(Calendar.MINUTE, 59);
			endOfYesterday.set(Calendar.SECOND, 59);
			endOfYesterday.add(Calendar.DATE, -1);

			GregorianCalendar now = new GregorianCalendar();
			
			//Fechamentos em aberto, em correção, em Conferencia
			PreparedStatement pstmt = connection.prepareStatement(
					" SELECT * FROM adm_conta_fechamento A "+
					" JOIN adm_conta_financeira B on ( A.cd_conta = B.cd_conta ) "+
					" WHERE A.st_fechamento in ( "+ST_EM_ANDAMENTO+", "+ST_EM_CORRECAO+", "+ST_EM_CONFERENCIA+" ) "+
					" AND  B.tp_conta =  "+ContaFinanceiraServices.TP_CAIXA+
					//Conta não está encerrada
					" AND  B.DT_FECHAMENTO IS NULL  "+
					" AND A.DT_FECHAMENTO BETWEEN ? AND ? "
			);
			pstmt.setTimestamp(1, new Timestamp( beginOfYesterday.getTimeInMillis() ) );
			pstmt.setTimestamp(2, new Timestamp( endOfYesterday.getTimeInMillis() ) );
			ResultSetMap rsmFechamentosIniciados = new ResultSetMap(pstmt.executeQuery());
			while( rsmFechamentosIniciados != null && rsmFechamentosIniciados.next() ){
				/**
				 * Fechamento Padrão bloqueia a meia noite
				 * Fechamento cob bloqueia ao meio dia
				 */
				if( (rsmFechamentosIniciados.getInt("CD_TURNO") == cdTurnoPadrao && now.get( Calendar.HOUR ) == 00)
					|| ( rsmFechamentosIniciados.getInt("CD_TURNO") == cdTurnoCob && now.get( Calendar.HOUR ) == 12 )
				){
					
					if( rsmFechamentosIniciados.getInt("ST_FECHAMENTO") == ST_EM_CONFERENCIA ){
						if( rsmFechamentosIniciados.getInt("CD_SUPERVISOR") > 0 ){
							//ADICIONA SUPERVISOR A LISTA DE BLOQUEIO
							if( !cdUsuariosAbloquear.contains( rsmFechamentosIniciados.getInt("CD_SUPERVISOR") ) ){
								cdUsuariosAbloquear.add( rsmFechamentosIniciados.getInt("CD_SUPERVISOR") );
							}
						}else{
							//NENHUM SUPERVISOR VERIFICOU OS LANÇAMENTOS, ADICIONA TODOS COM PERMISSÃO DE VERIFICAR/CONCLUIR
							ResultSetMap rsmSupervisores = new ResultSetMap(
									connection.prepareStatement(
											" SELECT cd_usuario FROM seg_usuario_permissao_acao A                                 "+ 
													" JOIN  seg_acao 					 B on (  A.cd_acao = B.cd_acao            "+
													"											AND A.cd_modulo = B.cd_modulo     "+
													"											AND A.cd_sistema = B.cd_sistema ) "+
													" JOIN  seg_usuario_conta_financeira C on ( A.cd_usuario = B.cd_usuario )        "+
													" WHERE B.ID_ACAO = 'CONFERIR_MOV_FECHAMENTO_CAIXA' "+ 
													" AND   C.cd_conta = "+rsmFechamentosIniciados.getInt("CD_CONTA")
													
											).executeQuery()
									);
							while ( rsmSupervisores != null && rsmSupervisores.next()) {
								if( !cdUsuariosAbloquear.contains( rsmSupervisores.getInt("CD_USUARIO") ) ){
									cdUsuariosAbloquear.add( rsmSupervisores.getInt("CD_USUARIO") );
								}
							}
						}
						
						
					}else{
						//ADICIONA RESPONSÁVEL A LISTA DE BLOQUEIO
						if( !cdUsuariosAbloquear.contains( rsmFechamentosIniciados.getInt("CD_RESPONSAVEL") ) ){
							cdUsuariosAbloquear.add( rsmFechamentosIniciados.getInt("CD_RESPONSAVEL") );
						}
						
					}
				}
			}
			
			//FECHAMENTOS NÃO INICIADOS
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.DT_FECHAMENTO", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format( new Date( endOfYesterday.getTimeInMillis() ) ), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			ResultSetMap rsmPeriodoSemFechamento = getPeriodoSemFechamento(criterios);
			if( rsmPeriodoSemFechamento != null ){
				rsmPeriodoSemFechamento.beforeFirst();
				while( rsmPeriodoSemFechamento.next() ){
					/**
					 *  SE HOUVER FECHAMENTO PENDENTE DENTRO DE UM PERIODO RETORNADO
					 *  Inclui usuarios com permissao de inciar a lista de bloqueados 
					  */
					 
					if(  Util.compareDates(beginOfYesterday, rsmPeriodoSemFechamento.getGregorianCalendar("DT_INICIO")) >= 0
						|| Util.compareDates(rsmPeriodoSemFechamento.getGregorianCalendar("DT_FINAL"), beginOfYesterday) >= 0  ){
						
						//BLOQUEIA USUARIOS COM PERMISSÃO DE INICIAR
						ResultSetMap rsmUserPermissaoIniciar = new ResultSetMap(
								connection.prepareStatement(
										" SELECT * FROM seg_usuario_permissao_acao A                                 "+ 
												" JOIN  seg_acao 					 B on (  A.cd_acao = B.cd_acao            "+
												"											AND A.cd_modulo = B.cd_modulo     "+
												"											AND A.cd_sistema = B.cd_sistema ) "+
												" JOIN  seg_usuario_conta_financeira C on ( A.cd_usuario = C.cd_usuario )        "+
												" WHERE B.ID_ACAO = 'INICIAR_FECHAMENTO_CAIXA' "+ 
												" AND   C.cd_conta = "+rsmPeriodoSemFechamento.getInt("CD_CONTA")
												
										).executeQuery()
								);
						
						while ( rsmUserPermissaoIniciar != null && rsmUserPermissaoIniciar.next()) {
							if( !cdUsuariosAbloquear.contains(  rsmUserPermissaoIniciar.getInt("CD_USUARIO") ) ){
								cdUsuariosAbloquear.add( rsmUserPermissaoIniciar.getInt("CD_USUARIO") );
							}
						}
					}
					
				}
			}
			
			int retorno = ParametroServices.setValoresOfParametro( ParametroServices.getByName("CD_USUARIOS_BLOQUEADOS_FECHAMENTO_CAIXA").getCdParametro(),
													cdUsuariosAbloquear.toArray(), connection);
			
			if( retorno <= 0 ){
				Conexao.rollback(connection);
				return new Result(-1, "Erro ao registrar usuários a bloquear");
			}
			connection.commit();
			return new Result(1,"Usuários bloqueados com sucesso!");
		}catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao bloquear fechamento aos usuário!", e);
		}
		finally {
			Conexao.desconectar(connection);
		}
	}
	
	/*
	 * Envia o fechamento de caixa para correção pelo responsável
	 */
	public static Result enviarCorrecao( ContaFechamento fechamento, Usuario usuario ) {
		return enviarCorrecao( fechamento, usuario, null);
	}
	public static Result enviarCorrecao( ContaFechamento fechamento, Usuario usuario, Connection connection) {
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			if (isConnectionNull) {
				connection.setAutoCommit(false);
			}
			
			Result resultSituacao = setSituacao(ST_EM_CORRECAO, fechamento, usuario, connection );
			if( resultSituacao.getCode() <= 0 ){
				if (isConnectionNull)
					connection.rollback();
				return new Result(-1,"Erro ao atualizar situação do fechamento!");
			}
			if (isConnectionNull)
				connection.commit();
			return new Result(1,"Caixa enviado para correção.");
		}catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar enviar caixa para correção!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Fluxo PAX
	 * Envia o fechamento de caixa para aprovações pelo supervisor
	 * 
	 */
	public static Result enviarConferencia(ResultSetMap movimentos, ContaFechamento fechamento, Usuario usuario ) {
		return enviarConferencia(movimentos, fechamento, usuario, null);
	}
	public static Result enviarConferencia(ResultSetMap movimentos, ContaFechamento fechamento, Usuario usuario, Connection connection) {
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			if (isConnectionNull) {
				connection.setAutoCommit(false);
			}
			//verifica se existem anexos
			ResultSetMap rsmArquivos = ContaFechamentoArquivoServices.getAllByFechamento( fechamento.getCdConta(), fechamento.getCdFechamento(), connection);
			if( rsmArquivos == null || ( rsmArquivos != null && rsmArquivos.size() == 0 )  ){
				if (isConnectionNull)
					connection.rollback();
				return new Result(-1,"Anexo não informado!");
			}
			
			
			Result resultSituacao = setSituacao(ST_EM_CONFERENCIA, fechamento, usuario, connection );
			if( resultSituacao.getCode() <= 0 ){
				if (isConnectionNull)
					connection.rollback();
				return new Result(-1,"Erro ao atualizar situação do fechamento!");
			}
			if (isConnectionNull)
				connection.commit();
			return new Result(1,"Caixa enviado para conferência.");
		}catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar enviar caixa para conferência!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
	 * Fluxo Pax
	 * @param cdConta
	 * @param cdFechamento
	 * @param cdSupervisor
	 * @param txtObservacao
	 * @return
	 */
	public static Result concluirFechamento(ContaFechamento contaFechamento) {
		return concluirFechamento(contaFechamento, null);
	}
	public static Result concluirFechamento(ContaFechamento contaFechamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			
			if( contaFechamento.getCdFechamento() <= 0){
				 if(isConnectionNull)
					 Conexao.rollback(connection);
				 return new Result(-1, "Fechamento não pôde ser concluído.\nAnexo não informado!" );
			}
			
			ResultSetMap rsm = getAllMovimentos( contaFechamento.getCdConta(), contaFechamento.getCdFechamento(), connection);
			while( rsm.next() ){
				if( rsm.getInt("ST_MOVIMENTO") != MovimentoContaServices.ST_CONFERIDO ){
					 if(isConnectionNull)
						 Conexao.rollback(connection);
					 return new Result(-1, "Fechamento não pôde ser concluído.\nExistem movimentações não Conferidas/Reprovadas!" );
				}
			}
			
			return concluirFechamento( contaFechamento.getCdConta(), contaFechamento.getCdFechamento(),
					contaFechamento.getCdSupervisor(), contaFechamento.getTxtObservacao(), connection);
		}catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar concluir fechamento de caixa!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result concluirFechamento(int cdConta, int cdFechamento, int cdSupervisor, String txtObservacao) {
		return concluirFechamento(cdConta, cdFechamento, cdSupervisor, txtObservacao, null);
	}

	public static Result concluirFechamento(int cdConta, int cdFechamento, int cdSupervisor, String txtObservacao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			
			// Verifica se foi passado um supervisor
			if (cdSupervisor <= 0)
				return new Result(-10, "Não foi possível efetuar o fechamento de caixa da conta informada." +
						         "Supervisor do fechamento nâo identificado.");
			// Verifica a existência do fechamento
			ContaFechamento fechamento = ContaFechamentoDAO.get(cdConta, cdFechamento, connection);
			if(fechamento==null)
				return new Result(-10, "Fechamento não localizado! [Código do Fechamento: "+cdFechamento+"]");
			int cdTurno = fechamento.getCdTurno();
			// Atualizando dados do fechamento
			fechamento.setCdSupervisor(cdSupervisor);
			fechamento.setTxtObservacao(txtObservacao);
			fechamento.setVlSaldoAnterior( ContaFinanceiraServices.getSaldoAnterior(cdConta, fechamento.getDtFechamento(), connection));
			//
			GregorianCalendar dtFinalDia = (GregorianCalendar)fechamento.getDtFechamento().clone();
			dtFinalDia.set(Calendar.HOUR_OF_DAY, 23);
			dtFinalDia.set(Calendar.MINUTE, 59);
			dtFinalDia.set(Calendar.SECOND, 59);
			dtFinalDia.set(Calendar.MILLISECOND, 59);
			// CONEXÃO
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// Marcando a movimentação conferida como fechada e atribuindo ao fechamento
			PreparedStatement pstmt = connection.prepareStatement("UPDATE adm_movimento_conta " +
																  "SET st_movimento  = "+MovimentoContaServices.ST_CX_FECHADO+
																  "   ,cd_fechamento = "+cdFechamento+
																  " WHERE cd_conta      = "+cdConta+
																  "   AND cd_fechamento IS NULL "+
																  "   AND st_movimento  = "+MovimentoContaServices.ST_CONFERIDO+
																  "   AND dt_movimento <= ?" +
																  (cdTurno>0?" AND cd_turno = "+cdTurno : ""));

			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtFinalDia));
			pstmt.executeUpdate();
			// Sumariza créditos e débitos- em moeda corrente
			ResultSet rs = connection.prepareStatement("SELECT tp_movimento, SUM(TRUNC(vl_movimento*100)/100) AS vl_movimento " +
												       "FROM adm_movimento_conta " +
												       "WHERE cd_conta      = "+cdConta+
												       "  AND st_movimento  = "+MovimentoContaServices.ST_CX_FECHADO+
												       "  AND cd_fechamento = "+cdFechamento+
													   "GROUP BY tp_movimento").executeQuery();
			while (rs.next())
				if(rs.getInt("tp_movimento")==MovimentoContaServices.CREDITO)
					fechamento.setVlTotalEntradas(rs.getDouble("vl_movimento"));
				else
					fechamento.setVlTotalSaidas(rs.getDouble("vl_movimento"));
			fechamento.setVlFechamento(fechamento.getVlSaldoAnterior() + fechamento.getVlTotalEntradas() - fechamento.getVlTotalSaidas());
			/*
			 * SALVANDO MOVIMENTação EM TÍTULOS DE Crédito
			 */
			// Marcando a movimentação de título de crédito como parte do fechamento
			pstmt = connection.prepareStatement("UPDATE adm_movimento_conta A SET cd_fechamento = "+cdFechamento+
												" WHERE cd_conta = "+cdConta+
												"   AND cd_fechamento IS NULL "+
												"   AND dt_movimento <= ? " +
												(cdTurno>0?" AND cd_turno = "+cdTurno : "")+
												"   AND EXISTS (SELECT * FROM adm_movimento_titulo_credito B " +
												"               WHERE A.cd_conta           = B.cd_conta "+
					  							"                 AND A.cd_movimento_conta = B.cd_movimento_conta) ");

			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtFinalDia));
			pstmt.executeUpdate();
			//
			ResultSetMap rsmTipoDocumento = new ResultSetMap();
			// Sumarizando por tipo de movimentação e tipo de documento
			pstmt = connection.prepareCall(
					  "SELECT A.tp_movimento, D.cd_tipo_documento, D.nm_tipo_documento, " +
					  "       SUM(TRUNC(C.vl_titulo * 100) / 100) AS vl_movimento, COUNT(*) AS qt_titulo "+
					  "FROM adm_movimento_conta A " +
					  "JOIN adm_movimento_titulo_credito B ON (A.cd_conta           = B.cd_conta "+
					  "                                    AND A.cd_movimento_conta = B.cd_movimento_conta) " +
					  "JOIN adm_titulo_credito           C ON (B.cd_titulo_credito = C.cd_titulo_credito) "+
					  "JOIN adm_tipo_documento           D ON (C.cd_tipo_documento = D.cd_tipo_documento) "+
					  "WHERE A.cd_conta      = " +cdConta+
					  "  AND A.cd_fechamento = " +cdFechamento+
					  "  AND A.dt_movimento <= ? "+
					  "GROUP BY A.tp_movimento, D.cd_tipo_documento, D.nm_tipo_documento");
			pstmt.setTimestamp(1, new Timestamp(dtFinalDia.getTimeInMillis()));
			rs = pstmt.executeQuery();
			while(rs.next())	{
				if(rsmTipoDocumento.locate("cd_tipo_documento", rs.getInt("cd_tipo_documento")))
					rsmTipoDocumento.setValueToField(rs.getInt("tp_movimento")==0?"VL_TOTAL_ENTRADAS":"VL_TOTAL_SAIDAS", rs.getFloat("vl_movimento"));
				else	{
					HashMap<String,Object> reg = new HashMap<String,Object>();
					reg.put("CD_TIPO_DOCUMENTO", rs.getInt("cd_tipo_documento"));
					reg.put(rs.getInt("tp_movimento")==0?"VL_TOTAL_ENTRADAS":"VL_TOTAL_SAIDAS", rs.getFloat("vl_movimento"));
					rsmTipoDocumento.addRegister(reg);
				}
			}
			ResultSetMap rsmSaldos = TituloCreditoServices.getSaldoEmTitulo(cdConta, dtFinalDia, connection);
			while(rsmSaldos.next()){
				if(rsmTipoDocumento.locate("cd_tipo_documento", rsmSaldos.getInt("cd_tipo_documento")))
					rsmTipoDocumento.setValueToField("VL_SALDO_FINAL", rsmSaldos.getFloat("vl_saldo"));
				else	{
					HashMap<String,Object> reg = new HashMap<String,Object>();
					reg.put("CD_TIPO_DOCUMENTO", rsmSaldos.getInt("cd_tipo_documento"));
					reg.put("VL_SALDO_FINAL", rsmSaldos.getFloat("vl_saldo"));
					rsmTipoDocumento.addRegister(reg);
				}
			}
			// Gravando os sumário da movimentação em títulos
			pstmt = connection.prepareStatement("INSERT INTO adm_conta_fechamento_tipo_doc " +
                                                "(cd_conta, cd_fechamento, cd_tipo_documento, vl_saldo_anterior, vl_total_entradas, vl_total_saidas, vl_saldo_final) " +
                                                "VALUES ("+cdConta+","+cdFechamento+",?,?,?,?,?)");
			rsmTipoDocumento.beforeFirst();
			while(rsmTipoDocumento.next()){
				float vlSaldoAnterior = rsmTipoDocumento.getFloat("vl_saldo")-rsmTipoDocumento.getFloat("vl_total_entradas")+rsmTipoDocumento.getFloat("vl_total_saidas");
				pstmt.setInt(1, rsmTipoDocumento.getInt("cd_tipo_documento"));
				pstmt.setFloat(2, vlSaldoAnterior);
				pstmt.setFloat(3, rsmTipoDocumento.getFloat("vl_total_entradas"));
				pstmt.setFloat(4, rsmTipoDocumento.getFloat("vl_total_saidas"));
				pstmt.setFloat(5, rsmTipoDocumento.getFloat("vl_saldo"));
				pstmt.executeUpdate();
				if(rsmTipoDocumento.getFloat("vl_saldo") > 0)	{
					ResultSetMap rsmTitulos = TituloCreditoServices.getTituloOfSaldo(cdConta, rsmTipoDocumento.getInt("cd_tipo_documento"), dtFinalDia);
					while(rsmTitulos.next())
						connection.prepareStatement("INSERT INTO adm_conta_fechamento_tit_cred (cd_conta, cd_fechamento, cd_tipo_documento, cd_titulo_credito) " +
								                    "VALUES ("+cdConta+","+cdFechamento+
								                    ","+rsmTipoDocumento.getInt("cd_tipo_documento")+","+rsmTitulos.getInt("cd_titulo_credito")+
								                    ")").executeUpdate();
				}
			}
			// Atualiza fechamento
			Result resultSituacao = setSituacao(ST_CONCLUIDO, fechamento, UsuarioDAO.get( fechamento.getCdResponsavel() ), connection);
			if( resultSituacao.getCode() <= 0 ){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, resultSituacao.getMessage());
			}
			ContaFechamentoDAO.update(fechamento, connection);
			// Fecha o caixa
			connection.prepareStatement("UPDATE adm_conta_financeira SET cd_responsavel = null, cd_turno = null WHERE cd_conta = "+cdConta).executeUpdate();

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Fechamento do dia " + Util.formatDate(fechamento.getDtFechamento(), "dd/MM/yyyy") + " concluído com sucesso!");
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar concluir fechamento de caixa!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/*
	 * Função usada pelo Manager - WEB
	 */
	public static Result fecharCaixa(int cdConta, GregorianCalendar dtFechamento, int cdUsuario, String txtObservacao, int cdTurno) {
		return fechamentoResumido(cdConta, dtFechamento, cdUsuario, cdUsuario, txtObservacao, cdTurno, null);
	}

	public static Result fechamentoResumido(int cdConta, GregorianCalendar dtFechamento, int cdUsuario, int cdSupervisor, String txtObservacao, int cdTurno, Connection connection)
	{
		boolean isConnectionNull = connection==null;
		Result result = new Result(0, "Nenhuma operação realidada!");
		try {
			// Verificações são realizadas em cada função [iniciar e concluir]
			result = iniciarFechamento(cdConta, dtFechamento, cdUsuario, cdTurno);
			if(result.getCode()<=0)
				return result;

			result = concluirFechamento(cdConta, result.getCode()/*cdFechamento*/, cdSupervisor, txtObservacao);
			return new Result(1, "Caixa fechado no dia " + Util.formatDate(dtFechamento, "dd/MM/yyyy") + " com sucesso!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Problemas reportados ao fechar o caixa da conta informada do dia " +
					Util.formatDate(dtFechamento, "dd/MM/yyyy") + ". " +
					"Clique no botão detalhe, anote a mensagem de erro e entre em contato com o administrador ou o suporte técnico.", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_conta, B.cd_empresa, "+
						   "  (SELECT SUM(vl_movimento) " +
						   "				 FROM adm_movimento_conta " +
						   "				 WHERE cd_conta = A.cd_conta " +
						   "				   AND cd_fechamento = A.cd_fechamento "+
						   "				   AND tp_movimento = "+MovimentoContaServices.CREDITO+
						   "               ) AS vl_total_entradas_fechamento, " +
						   "  (SELECT SUM(vl_saldo_final) " +
						   "				 FROM adm_conta_fechamento_tipo_doc " +
						   "				 WHERE cd_conta = A.cd_conta " +
						   "				   AND cd_fechamento = A.cd_fechamento"+
						   "               ) AS vl_saldo_titulos, " +
						   " C.nm_login as nm_responsavel, D.nm_login as nm_supervisor,   "+
						   " E.nm_pessoa as nm_empresa, F.nm_turno      "+
						   " FROM adm_conta_fechamento A    " +
						   " JOIN            adm_conta_financeira  B ON ( A.cd_conta = B.cd_conta) "+
						   " LEFT JOIN       seg_usuario           C on ( A.cd_responsavel = C.cd_usuario ) "+
						   " LEFT JOIN       seg_usuario           D on ( A.cd_supervisor = D.cd_usuario )  "+
						   " LEFT JOIN       grl_pessoa            E on ( B.cd_empresa = e.cd_pessoa )      "+
						   " LEFT OUTER JOIN adm_turno             F on ( A.cd_turno = F.cd_turno )      "+
						   " WHERE 1=1 ",
						   "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static final ResultSetMap getAllMovimentos(int cdConta, int cdFechamento) {
		return getAllMovimentos(cdConta, cdFechamento, null);
	}

	public static final ResultSetMap getAllMovimentos(int cdConta, int cdFechamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement(
					" SELECT A.*, B.nm_forma_pagamento " +
					" FROM adm_movimento_conta A, adm_forma_pagamento B " +
					" WHERE A.cd_forma_pagamento = B.cd_forma_pagamento " +
					"  AND A.cd_conta      = "+cdConta+
					"  AND A.cd_fechamento = "+cdFechamento);
			return new ResultSetMap(pstmt.executeQuery());
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

	public static final ResultSetMap getAllSaldosByTipoDoc(int cdConta, int cdFechamento) {
		return getAllSaldosByTipoDoc(cdConta, cdFechamento, null, 0/*cdTurno*/, null);
	}

	public static final ResultSetMap getAllSaldosByTipoDoc(int cdConta, GregorianCalendar dtFechamento, int cdTurno) {
		return getAllSaldosByTipoDoc(cdConta, 0, dtFechamento, cdTurno, null);
	}

	public static final ResultSetMap getAllSaldosByTipoDoc(int cdConta, int cdFechamento, GregorianCalendar dtFechamento, int cdTurno, Connection connection) {
		boolean isConnectionNull = connection==null;
		connection = isConnectionNull ? Conexao.conectar() : connection;
		try {
			// Se é a movimentação de um caixa fechado
			if(cdFechamento > 0)	{
				ContaFechamento fechamento = ContaFechamentoDAO.get(cdConta, cdFechamento, connection);
				if(fechamento!=null)
					dtFechamento = fechamento.getDtFechamento();

				if(fechamento!=null && fechamento.getCdSupervisor()>0)
					return new ResultSetMap(connection.prepareStatement("SELECT A.*, B.nm_tipo_documento " +
							                                            "FROM adm_conta_fechamento_tipo_doc A, adm_tipo_documento B " +
							                                            "WHERE A.cd_tipo_documento = B.cd_tipo_documento " +
							                                            "  AND A.cd_conta      = " +cdConta+
						                                                "  AND A.cd_fechamento = " +cdFechamento).executeQuery());
			}
			// Se é a movimentação de um período sem fechamento
			if(dtFechamento!=null)	{
				dtFechamento.set(Calendar.HOUR_OF_DAY, 23);
				dtFechamento.set(Calendar.MINUTE, 59);
				dtFechamento.set(Calendar.SECOND, 59);
				dtFechamento.set(Calendar.MILLISECOND, 59);
			}
			// Sumarizando por tipo de movimentação e tipo de documento
			PreparedStatement pstmt = connection.prepareStatement(
					  "SELECT A.tp_movimento, D.cd_tipo_documento, D.nm_tipo_documento, " +
					  "       SUM(TRUNC(C.vl_titulo * 100) / 100) AS vl_movimento, COUNT(*) AS qt_titulo "+
					  "FROM adm_movimento_conta A " +
					  "JOIN adm_movimento_titulo_credito B ON (A.cd_conta           = B.cd_conta "+
					  "                                    AND A.cd_movimento_conta = B.cd_movimento_conta) " +
					  "JOIN adm_titulo_credito           C ON (B.cd_titulo_credito = C.cd_titulo_credito) "+
					  "JOIN adm_tipo_documento           D ON (C.cd_tipo_documento = D.cd_tipo_documento) "+
					  "WHERE A.cd_conta      = "+cdConta+
					  "  AND A.cd_fechamento IS NULL "+
					  "  AND A.dt_movimento  <= ? "+
					  // (cdTurno > 0 ? " AND A.cd_turno = "+cdTurno : "")+
					  "GROUP BY A.tp_movimento, D.cd_tipo_documento, D.nm_tipo_documento");
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			ResultSetMap rsmTipoDocumento = new ResultSetMap();
			while(rs.next())	{
				if(rsmTipoDocumento.locate("cd_tipo_documento", rs.getInt("cd_tipo_documento")))
					rsmTipoDocumento.setValueToField(rs.getInt("tp_movimento")==0?"VL_TOTAL_ENTRADAS":"VL_TOTAL_SAIDAS", rs.getFloat("vl_movimento"));
				else	{
					HashMap<String,Object> reg = new HashMap<String,Object>();
					reg.put("CD_TIPO_DOCUMENTO", rs.getInt("cd_tipo_documento"));
					reg.put("NM_TIPO_DOCUMENTO", rs.getString("nm_tipo_documento"));
					reg.put("VL_SALDO_ANTERIOR", new Float(0));
					reg.put(rs.getInt("tp_movimento")==0?"VL_TOTAL_ENTRADAS":"VL_TOTAL_SAIDAS", rs.getFloat("vl_movimento"));
					rsmTipoDocumento.addRegister(reg);
				}
			}
			ResultSetMap rsmSaldos = TituloCreditoServices.getSaldoEmTitulo(cdConta, dtFechamento, connection);
			while(rsmSaldos.next())	{
				if(rsmTipoDocumento.locate("CD_TIPO_DOCUMENTO", rsmSaldos.getInt("cd_tipo_documento")))	{
					rsmTipoDocumento.setValueToField("VL_SALDO_FINAL", rsmSaldos.getFloat("vl_saldo"));
					rsmTipoDocumento.setValueToField("VL_SALDO_ANTERIOR", rsmTipoDocumento.getFloat("VL_SALDO_FINAL")+
							                                              rsmTipoDocumento.getFloat("VL_TOTAL_SAIDAS")-
							                                              rsmTipoDocumento.getFloat("VL_TOTAL_ENTRADAS"));
				}
				else	{
					HashMap<String,Object> reg = new HashMap<String,Object>();
					reg.put("CD_TIPO_DOCUMENTO", rsmSaldos.getInt("cd_tipo_documento"));
					reg.put("NM_TIPO_DOCUMENTO", rsmSaldos.getString("nm_tipo_documento"));
					reg.put("VL_SALDO_ANTERIOR", rsmSaldos.getFloat("vl_saldo"));
					reg.put("VL_SALDO_FINAL",    rsmSaldos.getFloat("vl_saldo"));
					rsmTipoDocumento.addRegister(reg);
				}
			}
			rsmTipoDocumento.beforeFirst();
			return rsmTipoDocumento;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static final ResultSetMap getAllTitulos(int cdConta, int cdFechamento) {
		return getAllTitulos(cdConta, cdFechamento, null);
	}

	public static final ResultSetMap getAllTitulos(int cdConta, int cdFechamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			return new ResultSetMap(connection.prepareStatement(
									"SELECT C.*, B.nm_tipo_documento, nm_banco AS nm_instituicao_financeira " +
									"FROM adm_conta_fechamento_tit_cred A " +
									"JOIN adm_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento) " +
									"JOIN adm_titulo_credito C ON (A.cd_titulo_credito = C.cd_titulo_credito) " +
									"LEFT OUTER JOIN grl_banco D ON (C.cd_instituicao_financeira = D.cd_banco) " +
									"  AND A.cd_conta      = " +cdConta+
									"  AND A.cd_fechamento = " +cdFechamento).executeQuery());
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

	public static final ResultSetMap getTransferenciasOf(int cdConta, int cdFechamento, GregorianCalendar dtFechamento, int cdTurno, boolean listaTitulos) {
		return getTransferenciasOf(cdConta, cdFechamento, dtFechamento, cdTurno, listaTitulos, null);
	}

	public static final ResultSetMap getTransferenciasOf(int cdConta, int cdFechamento, GregorianCalendar dtFechamento, int cdTurno, boolean listaTitulos, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(dtFechamento!= null)	{
				dtFechamento.set(GregorianCalendar.HOUR, 23);
				dtFechamento.set(GregorianCalendar.MINUTE, 59);
				dtFechamento.set(GregorianCalendar.SECOND, 59);
			}
			connection = isConnectionNull ? Conexao.conectar() : connection;
			String sql = "SELECT A.*, B2.nm_pessoa as nm_responsavel, B1.dt_fechamento, A.*, B.nm_conta AS nm_conta_origem, B.nr_conta AS nr_conta_origem, " +
					     "       C.nm_pessoa AS nm_usuario, U.nm_login, D.nr_cheque, D.st_cheque, " +
					     "       E.nm_forma_pagamento, E.tp_forma_pagamento " +
					     (listaTitulos ?
							"   ,F.nr_documento AS nr_titulo, F.nr_agencia, F.dt_vencimento, F.nm_emissor, G.nr_banco AS nr_instituicao_financeira, " +
							"    G.nm_banco AS nm_instituicao_financeira, H.sg_tipo_documento, H.nm_tipo_documento, " +
							"	 (TRUNC(F.vl_titulo * 100) / 100) AS vl_titulo " : "")+
				         "FROM adm_movimento_conta A " +
					     "LEFT OUTER JOIN adm_conta_financeira	B ON (A.cd_conta_origem = B.cd_conta) " +
					     " LEFT OUTER JOIN adm_conta_fechamento	B1 ON (A.cd_fechamento = B1.cd_fechamento AND A.cd_conta = B1.cd_conta ) " +
					     " LEFT OUTER JOIN grl_pessoa	B2 ON (B2.cd_pessoa = B1.cd_responsavel) " + 
					     "LEFT OUTER JOIN seg_usuario 			U ON (A.cd_usuario = U.cd_usuario) " +
					     "LEFT OUTER JOIN grl_pessoa 			C ON (U.cd_pessoa = C.cd_pessoa) " +
					     "LEFT OUTER JOIN adm_cheque 			D ON (A.cd_cheque = D.cd_cheque) " +
					     "LEFT OUTER JOIN adm_forma_pagamento 	E ON (A.cd_forma_pagamento = E.cd_forma_pagamento) " +
					     (listaTitulos ?
					    	"LEFT OUTER JOIN adm_movimento_titulo_credito MTC ON (A.cd_conta = MTC.cd_conta " +
					    	"                                                 AND A.cd_movimento_conta = MTC.cd_movimento_conta) " +
					    	"LEFT OUTER JOIN adm_titulo_credito   F ON (MTC.cd_titulo_credito = F.cd_titulo_credito) " +
				            "LEFT OUTER JOIN grl_banco            G ON (F.cd_instituicao_financeira = G.cd_banco) " +
				            "LEFT OUTER JOIN adm_tipo_documento   H ON (F.cd_tipo_documento = H.cd_tipo_documento) "
				            /*"LEFT OUTER JOIN grl_pessoa         I ON (F.cd_pessoa = I.cd_pessoa) "*/ :  "")+
					     "WHERE A.cd_conta     = "+cdConta+
					     "  AND A.tp_origem    = "+MovimentoContaServices.toTRANSFERENCIA+
					     "  AND A.tp_movimento = "+MovimentoContaServices.DEBITO+
					     (cdFechamento>0 ? " AND A.cd_fechamento = "+cdFechamento :
					    	               " AND A.cd_fechamento IS NULL " +
					    	               " AND A.dt_movimento <= ? " +
					    	               " AND A.cd_turno      = "+cdTurno)+
					     " ORDER BY CAST(A.dt_movimento AS DATE)"
					    	               +(listaTitulos ?", H.sg_tipo_documento, F.nm_emissor, G.nm_banco, F.nr_documento "
					    	            		            :"");
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			if(cdFechamento<=0)
				pstmt.setTimestamp(1, new java.sql.Timestamp(dtFechamento.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			PreparedStatement destinos = connection.prepareStatement(
					"SELECT A.cd_conta, nm_conta, nr_conta, nr_dv " +
					"FROM adm_movimento_conta A " +
					"JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta) " +
                    "WHERE A.cd_conta_origem 	 = ?" +
                    "  AND A.cd_movimento_origem = ?");
			/*
			 *  Buscando pagamentos e recebimentos
			 */
			while(rsm.next())	{
				destinos.setInt(1, rsm.getInt("cd_conta"));
				destinos.setInt(2, rsm.getInt("cd_movimento_conta"));
				ResultSet rs = destinos.executeQuery();
				String nmConta = "";
				int cdContaDestino = 0;
				while(rs.next()){
					nmConta += (!nmConta.equals("")?",":"")+rs.getString("nm_conta");
					cdContaDestino = rs.getInt("cd_conta");
				}
				rsm.setValueToField("NM_CONTA_DESTINO", nmConta);
				rsm.setValueToField("CD_CONTA_DESTINO", cdContaDestino);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return new ResultSetMap();
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static final ResultSetMap getRecebimentosOf(int cdConta, int cdFechamento, GregorianCalendar dtFechamento, int cdTurno) {
		return getRecebimentosOf(cdConta, cdFechamento, dtFechamento, cdTurno, null);
	}

	public static final ResultSetMap getRecebimentosOf(int cdConta, int cdFechamento, GregorianCalendar dtFechamento, int cdTurno, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if(dtFechamento!= null)	{
				dtFechamento.set(GregorianCalendar.HOUR, 23);
				dtFechamento.set(GregorianCalendar.MINUTE, 59);
				dtFechamento.set(GregorianCalendar.SECOND, 59);
			}
			connection = isConnectionNull ? Conexao.conectar() : connection;
			String sql = "SELECT A.*, B.nm_conta AS nm_conta_origem, B.nr_conta AS nr_conta_origem, " +
					     "       C.nm_pessoa AS nm_usuario, U.nm_login, D.nr_cheque, D.st_cheque, " +
					     " ORDER BY CAST(A.dt_movimento AS DATE), H.sg_tipo_documento, F.nm_emissor, " +
					     "          G.nm_banco, F.nr_documento ";
			//
			PreparedStatement pstmt = connection.prepareStatement(sql);
			if(cdFechamento<=0)
				pstmt.setTimestamp(1, new java.sql.Timestamp(dtFechamento.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			PreparedStatement destinos = connection.prepareStatement(
					"SELECT nm_conta, nr_conta, nr_dv " +
					"FROM adm_movimento_conta A " +
					"JOIN adm_conta_financeira B ON (A.cd_conta = B.cd_conta) " +
                    "WHERE A.cd_conta_origem 	 = ?" +
                    "  AND A.cd_movimento_origem = ?");
			/*
			 *  Buscando pagamentos e recebimentos
			 */
			while(rsm.next())	{
				destinos.setInt(1, rsm.getInt("cd_conta"));
				destinos.setInt(2, rsm.getInt("cd_movimento_conta"));
				ResultSet rs = destinos.executeQuery();
				String nmConta = "";
				while(rs.next())
					nmConta += (!nmConta.equals("")?",":"")+rs.getString("nm_conta");
				rsm.setValueToField("NM_CONTA_DESTINO", nmConta);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return new ResultSetMap();
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
	 * @author Alvaro
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap getPeriodoSemFechamento(ArrayList<ItemComparator> criterios) {
		return getPeriodoSemFechamento( criterios, null);
	}
	/**
	 * @author Alvaro
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap getPeriodoSemFechamento( ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			
			ResultSetMap rsmPeriodos = new ResultSetMap();
			ResultSetMap rsm = Search.find(
					   " SELECT *  "+
					   " FROM adm_conta_fechamento  A    " +
					   " JOIN adm_conta_financeira  B ON ( A.cd_conta = B.cd_conta) "+
					   " JOIN grl_pessoa            C ON ( B.cd_empresa = C.cd_pessoa) "+
					   " WHERE  B.tp_conta = "+ContaFinanceiraServices.TP_CAIXA+
					   " AND    B.dt_fechamento is null ",
					   " ORDER BY B.CD_EMPRESA, B.CD_CONTA, A.DT_FECHAMENTO ASC ", 
					   criterios, connection!=null ? connection : Conexao.conectar(), connection==null);
			int cdProximoFechamento = 0;
			HashMap<String, Object> nextRegister = null;
			GregorianCalendar dtProximoFechamento = null;
			GregorianCalendar hoje = new GregorianCalendar();
			int qtDiasIntervalo = 0;
			if( rsm != null ){
				rsm.beforeFirst();
				while( rsm.next() && ( rsm.getLines().size() - rsm.getPosition() > 0 ) ){
					
					cdProximoFechamento = -1;
					if( ( rsm.getLines().size() - rsm.getPosition() > 1 ) ){
						cdProximoFechamento = (Integer) rsm.getLines().get( rsm.getPosition()+1 ).get("CD_CONTA");
					}
					
					if( rsm.getInt("CD_CONTA") == cdProximoFechamento ){
						/**
						 * Registra o intervalo entre os fechamentos existentes
						 */
						nextRegister = rsm.getLines().get( rsm.getPointer()+1 );
						dtProximoFechamento =  new GregorianCalendar();
						dtProximoFechamento.setTimeInMillis( ((Timestamp)nextRegister.get("DT_FECHAMENTO")).getTime() )  ;
						qtDiasIntervalo = Util.getQuantidadeDiasUteis(rsm.getGregorianCalendar("DT_FECHAMENTO"), dtProximoFechamento);
						
						if( qtDiasIntervalo > 1 ){
							HashMap<String, Object> register = new HashMap<String, Object>();
							register.put("NM_EMPRESA", rsm.getString("NM_PESSOA"));
							register.put("CD_CONTA", rsm.getInt("CD_CONTA"));
							register.put("NM_CONTA", rsm.getString("NM_CONTA"));
							GregorianCalendar dtInicio = rsm.getGregorianCalendar("DT_FECHAMENTO");
							dtInicio.add(Calendar.DATE, 1);
							dtProximoFechamento.add(Calendar.DATE, -1); 
							register.put("DT_INICIO", new Timestamp(dtInicio.getTimeInMillis()));
							register.put("DT_FINAL", new Timestamp(dtProximoFechamento.getTimeInMillis()));
							register.put("QT_DIAS_INTERVALO", Util.getQuantidadeDiasUteis(dtInicio, dtProximoFechamento) );
							rsmPeriodos.addRegister(register);
						}
					}else if( Util.getQuantidadeDiasUteis(dtProximoFechamento, hoje ) > 1 ){
						/**
						 * Registra o intervalo entre o último fechamento e a data atual
						 */
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NM_EMPRESA", rsm.getString("NM_PESSOA"));
						register.put("CD_CONTA", rsm.getInt("CD_CONTA"));
						register.put("NM_CONTA", rsm.getString("NM_CONTA"));
						register.put("DT_INICIO", new Timestamp(dtProximoFechamento.getTimeInMillis()));
						register.put("DT_FINAL", new Timestamp(hoje.getTimeInMillis()));
						register.put("QT_DIAS_INTERVALO", Util.getQuantidadeDiasUteis(dtProximoFechamento, hoje) );
						rsmPeriodos.addRegister(register);
					}
				}
			}
			
			/**
			 * Relação de fechamentos pendentes por período identificado
			 */
			if( rsmPeriodos != null && rsmPeriodos.size() > 0 ){
				rsmPeriodos.beforeFirst();
				while ( rsmPeriodos.next() ) {
					GregorianCalendar dtInicioPeriodo = rsmPeriodos.getGregorianCalendar("DT_INICIO");
					ArrayList<HashMap<String, Object>> fechamentos = new ArrayList<HashMap<String, Object>>();
					for( int i=0;i<rsmPeriodos.getInt("QT_DIAS_INTERVALO");i++ ){
						HashMap<String, Object> fechamentoPendente = new HashMap<String, Object>();
						fechamentoPendente.put("DT_FECHAMENTO", dtInicioPeriodo.clone());
						fechamentos.add(fechamentoPendente);
						dtInicioPeriodo.add(Calendar.DATE, 1);
					}
				}
			}
			
			return rsmPeriodos;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoServices.getPeriodoSemFechamento: " +  e);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/*
	 * Retorna os dados sobre o ultimo fechamento e o primeiro dia com movimento apos o fechamento
	 */
	public static ResultSetMap getPeriodoSemFechamento(int cdConta) {
		return getPeriodoSemFechamento(cdConta, null);
	}
	public static ResultSetMap getPeriodoSemFechamento(int cdConta, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("DT_ULTIMO_FECHAMENTO", "");
			register.put("DT_PRIMEIRO_MOVIMENTO", "");
			// Ultimo fechamento
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT MAX(dt_fechamento) AS dt_fechamento " +
                                                                            "FROM adm_conta_fechamento " +
                                                                            "WHERE cd_conta = " +cdConta).executeQuery());
			if(rsm.next() && rsm.getGregorianCalendar("dt_fechamento")!=null)
				register.put("DT_ULTIMO_FECHAMENTO", Util.formatDate(rsm.getGregorianCalendar("dt_fechamento"), "dd/MM/yyyy"));

			// Primeiro dia sem movimento
			rsm = new ResultSetMap(connection.prepareStatement("SELECT MIN(dt_movimento) AS dt_movimento " +
                                                               "FROM adm_movimento_conta A " +
                                                               "WHERE cd_conta      = " +cdConta+
                                                               "  AND (st_movimento  = "+MovimentoContaServices.ST_CONFERIDO+
                                                               "  OR  EXISTS (SELECT * FROM adm_movimento_titulo_credito B " +
                                                               "              WHERE A.cd_conta           = B.cd_conta "+
                                                               "                AND A.cd_movimento_conta = B.cd_movimento_conta)) "+
                                                               "  AND cd_fechamento IS NULL").executeQuery());
			if(rsm.next() && rsm.getGregorianCalendar("dt_movimento")!=null)
				register.put("DT_PRIMEIRO_MOVIMENTO", Util.formatDate(rsm.getGregorianCalendar("dt_movimento"), "dd/MM/yyyy"));
			rsm = new ResultSetMap();
			rsm.addRegister(register);
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getPagamentos(int cdConta, String dtFechamento){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		ItemComparator item = new ItemComparator("A.dt_pagamento",dtFechamento,ItemComparator.EQUAL,Types.TIMESTAMP);
		criterios.add(item);
		item = new ItemComparator("A.cd_conta",""+cdConta,ItemComparator.EQUAL,Types.INTEGER);
		criterios.add(item);
		ResultSetMap rsmPagamentos    = ContaPagarServices.find(criterios);
		return rsmPagamentos;
	}
	
	public static ResultSetMap getRetiradas(int cdConta,int cdFechamento, GregorianCalendar dtFechamento,int cdTurno){
		ResultSetMap rsmTransferencia = ContaFechamentoServices.getTransferenciasOf(cdConta, cdFechamento,dtFechamento, cdTurno, true);
		ResultSetMap rsmDepositos = new ResultSetMap();
		HashMap<String,Object> register;
		while(rsmTransferencia.next())	{
			if(rsmTransferencia.getInt("TP_ORIGEM") == 7){
				ContaFinanceira conta = ContaFinanceiraDAO.get(rsmTransferencia.getInt("CD_CONTA_DESTINO"));
				if(conta.getTpConta() == ContaFinanceiraServices.TP_CONTA_BANCARIA){
					register = new HashMap<String,Object>();
					register.put("NM_CONTA_FINANCEIRA", conta.getNmConta());
					register.put("TP_CONTA","CONTA BANCÁRIA");
					register.put("ID_EXTRATO",rsmTransferencia.getString("ID_EXTRATO"));
					if(conta.getCdResponsavel() > 0){
						Pessoa pessoaResponsavel = PessoaDAO.get(conta.getCdResponsavel());
						register.put("NM_RESPONSAVEL",pessoaResponsavel.getNmPessoa());
					}
					register.put("DS_HISTORICO",rsmTransferencia.getString("DS_HISTORICO"));
					register.put("VL_MOVIMENTO",rsmTransferencia.getFloat("VL_MOVIMENTO"));
					rsmDepositos.addRegister(register);
				}
			}
		}
		return rsmDepositos;
	}

	public static int getCdFechamentoAnterior(int cdConta, GregorianCalendar dtMovimento, int cdTurno, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			//
			dtMovimento = (GregorianCalendar)dtMovimento.clone();
			dtMovimento.set(Calendar.HOUR, 0);
			dtMovimento.set(Calendar.MINUTE, 0);
			dtMovimento.set(Calendar.SECOND, 0);
			dtMovimento.set(Calendar.MILLISECOND, 0);
			if (isConnectionNull)
				connect = Conexao.conectar();
			Turno turno = TurnoDAO.get(cdTurno, connect);
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento A "+
                                                               "LEFT OUTER JOIN adm_turno B ON (A.cd_turno = B.cd_turno) "+
															   "WHERE A.cd_conta = "+cdConta+
															   "  AND dt_fechamento IS NOT NULL "+ 
															   "  AND ((dt_fechamento < ?) OR (dt_fechamento = ? and B.nr_ordem < ?)) "+
															   "ORDER BY dt_fechamento DESC, nr_ordem DESC "+
															   "LIMIT 1");
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtMovimento.getTimeInMillis()));
			pstmt.setInt(3, turno.getNrOrdem());
			ResultSet rs = pstmt.executeQuery();
			// Se encontrou um turno anterior a data do movimento continua a mesma
			if(rs.next()) 
				return rs.getInt("CD_FECHAMENTO");
			return 0;
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
	/**
	 * @author João Marlon
	 * @param cdConta  código da conta
	 * @param dtMovimento Data do fechamento do caixa
	 * @param cdTurno código do turno do fechamento do caixa
	 * Método que verifica se há caixas anteriores em aberto. Ele obriga o usuário fechar o caixa anterior antes de fechar o seguinte. 
	 */
	 
	public static Result isCaixaAnteriorAberto(int cdConta, GregorianCalendar dtMovimento, int cdTurno) {
		return isCaixaAnteriorAberto(cdConta, dtMovimento, cdTurno, null);
	}
	
	public static Result isCaixaAnteriorAberto(int cdConta, GregorianCalendar dtMovimento, int cdTurno, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			//			
			GregorianCalendar dtMovimentoInicial = (GregorianCalendar)dtMovimento.clone();
			GregorianCalendar dtMovimentoFinal   = (GregorianCalendar)dtMovimento.clone();
			dtMovimentoInicial.set(Calendar.HOUR, 0);
			dtMovimentoInicial.set(Calendar.MINUTE, 0);
			dtMovimentoInicial.set(Calendar.SECOND, 0);
			dtMovimentoFinal.set(Calendar.HOUR, 23);
			dtMovimentoFinal.set(Calendar.MINUTE, 59);
			dtMovimentoFinal.set(Calendar.SECOND, 59);
//			dtMovimento.set(Calendar.MILLISECOND, 0);
			if (isConnectionNull)
				connect = Conexao.conectar();
			// Verifica se há movimento para o mesmo dia em aberto para outro turno
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_turno FROM adm_movimento_conta " +
                                                               "WHERE cd_turno     <> "+ cdTurno +
															   "  AND cd_conta     = "+ cdConta+
															   "  AND cd_fechamento IS NULL " + 
															   "  AND dt_movimento BETWEEN ? AND ? " +
															   "ORDER BY cd_movimento_conta DESC, cd_turno "+
															   "LIMIT 1 ");
			pstmt.setTimestamp(1, new Timestamp(dtMovimentoInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtMovimentoFinal.getTimeInMillis()));;
			ResultSet rs = pstmt.executeQuery();
			// Se encontrou um turno anterior a data do movimento continua a mesma
			if(rs.next()){ 
				Turno turno = TurnoDAO.get(rs.getInt("cd_turno"), connect);
				return new Result(-1,"Antes de prosseguir com o fechamento atual, realize o fechamento do caixa do "+turno.getNmTurno().trim().toUpperCase()+" turno que ainda encontra-se aberto.");	
			}
			// Verifica último registro de movimento, anterior a hoje, que está sem fechamento.	
			PreparedStatement pstmt1 = connect.prepareStatement("SELECT cd_movimento_conta, dt_movimento, cd_turno FROM adm_movimento_conta " +
	 														    "WHERE cd_fechamento IS NULL "+
																"  AND cd_conta     = "+ cdConta+
										                        "  AND dt_movimento < ? " +
																"ORDER BY cd_movimento_conta DESC "+
																"LIMIT 1 ");
			pstmt1.setTimestamp(1, new Timestamp(dtMovimentoInicial.getTimeInMillis()));
			ResultSet rs1 = pstmt1.executeQuery();
			// Verifica último registro de movimento, anterior a hoje, independente de fechamento.
			PreparedStatement pstmt2 = connect.prepareStatement("SELECT cd_movimento_conta, cd_fechamento FROM adm_movimento_conta " +
																"WHERE dt_movimento < ? " +
																"  AND cd_conta     = "+ cdConta+
																"ORDER BY cd_movimento_conta DESC "+
																"LIMIT 1 ");
			pstmt2.setTimestamp(1, new Timestamp(dtMovimentoInicial.getTimeInMillis()));
			ResultSet rs2 = pstmt2.executeQuery();
			// Se o rs1 for igual ao rs2 e o não existir registro no cd_fechamento, então o caixa anterior ainda não foi fechado
			if (rs1.next()){
				if (rs2.next()){
					if((rs1.getInt("cd_movimento_conta") == rs2.getInt("cd_movimento_conta")) && !(rs2.getInt("cd_fechamento") > 0)){
						
						Date date = rs1.getDate("dt_movimento");
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						Turno turno = TurnoDAO.get(rs1.getInt("cd_turno"), connect);
						return new Result(-1,"Antes de prosseguir com o fechamento atual, realize o fechamento do caixa anterior do dia "+df.format(date)+" do "+turno.getNmTurno().trim().toUpperCase()+" turno.");
					}
				}
						
			}
			pstmt.close();
			pstmt1.close();
			pstmt2.close();
			return new Result(1);
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-2,"Erro ao executar método isCaixaAnteriorAberto em ContaFechamentoServices");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * Relatório
	 * @param cdUsuario
	 * @param cdEmpresa
	 * @param cdFechamento
	 * @param cdConta
	 * @param cdTurno
	 * @param dtFechamento
	 * @param pathServer
	 * @return
	 */
	public static Result relatFechaContaCaixa(int cdUsuario, int cdEmpresa, int cdFechamento, int cdConta, int cdTurno, String dtFechamento, String pathServer){
		try {
			Usuario usuario     =  new Usuario();
			usuario.setCdUsuario(cdUsuario);		
			Pessoa empresa      = PessoaDAO.get(cdEmpresa);
			if(cdEmpresa > 0 && cdFechamento > 0 && cdConta > 0 && cdTurno > 0){ 
				ContaFechamento contaFechamento = ContaFechamentoDAO.get(cdConta, cdFechamento);
				Pessoa responsavel = PessoaDAO.get(contaFechamento.getCdResponsavel());
				Pessoa pessoaUsuario = PessoaDAO.get(usuario.getCdUsuario());
				Turno turno = TurnoDAO.get(cdTurno);			
							
				// Definido
				GregorianCalendar dtMovimento = (GregorianCalendar)contaFechamento.getDtFechamento().clone();
				dtMovimento.set(Calendar.HOUR, 0);
				dtMovimento.set(Calendar.MINUTE, 0);
				dtMovimento.set(Calendar.SECOND, 0);
				dtMovimento.set(Calendar.MILLISECOND, 0);
				// Data Final
				HashMap<String,Object> parametros = new HashMap<String,Object>();
				ResultSetMap rsmVendasDias = DocumentoSaidaItemServices.findSaidaByDiaOfProduto(0, cdEmpresa, dtMovimento, 0, cdTurno, true, false);
				
				JRResultSetDataSource jrRS = null;
				/*
				 * PARAMETROS
				 */
				parametros.put("logoPath",pathServer);
				parametros.put("nmEmpresa", empresa.getNmPessoa());
				parametros.put("dtFechamento", new Timestamp(dtMovimento.getTimeInMillis()));
				parametros.put("nmTurno",turno.getNmTurno());
				parametros.put("nmResponsavel",responsavel.getNmPessoa());
				parametros.put("nmUsuario",pessoaUsuario.getNmPessoa());
				ResultSetMap rsmEncerrantes   = BicoEncerranteServices.getLeituraBicos(cdFechamento);
				ResultSetMap rsmMedicaoFisica = MedicaoFisicaServices.getMedicaoFisicaOf(cdConta, cdFechamento, contaFechamento.getDtFechamento());
				parametros.put("rsmEncerrantes",rsmEncerrantes.getLines());
				parametros.put("rsmMedicaoFisica",rsmMedicaoFisica.getLines());
				ResultSetMap rsmVendasBico = BicoEncerranteServices.getVendasByEncerrantes(cdEmpresa, cdConta, cdTurno, dtMovimento);
				parametros.put("rsmVendasBico",rsmVendasBico.getLines());
				parametros.put("rsmVendasProdutos",rsmVendasDias.getLines());
				// Resumo de Vendas
				ResultSetMap rsmResumoVendas = DocumentoSaidaItemServices.findResumoOfItensByGrupo(cdTurno, dtMovimento);
				parametros.put("rsmResumoVendas", rsmResumoVendas.getLines());
				// Despesas
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("B.dt_movimento", dtFechamento, ItemComparator.EQUAL, Types.TIMESTAMP));
				criterios.add(new ItemComparator("B.cd_conta",""+cdConta,ItemComparator.EQUAL,Types.INTEGER));
				criterios.add(new ItemComparator("B.cd_turno",""+cdTurno,ItemComparator.EQUAL,Types.INTEGER));
				ResultSetMap rsmPagamentos = MovimentoContaPagarServices.find(criterios);
				parametros.put("rsmPagamentos", rsmPagamentos.getLines());
				// Retiradas
				ResultSetMap rsmTransferencia = ContaFechamentoServices.getTransferenciasOf(cdConta, cdFechamento,contaFechamento.getDtFechamento(), cdTurno, true);
				ResultSetMap rsmDepositos = new ResultSetMap();
				HashMap<String,Object> register;
				while(rsmTransferencia.next())	{
					if(rsmTransferencia.getInt("TP_ORIGEM") == 7)	{
						ContaFinanceira conta = ContaFinanceiraDAO.get(rsmTransferencia.getInt("CD_CONTA_DESTINO"));
						if(conta.getTpConta() == ContaFinanceiraServices.TP_CONTA_BANCARIA){
							register = new HashMap<String,Object>();
							register.put("NM_CONTA_FINANCEIRA", conta.getNmConta());
							register.put("TP_CONTA","CONTA BANCÁRIA");
							register.put("ID_EXTRATO",rsmTransferencia.getString("ID_EXTRATO"));
							if(conta.getCdResponsavel() > 0){
								Pessoa pessoaResponsavel = PessoaDAO.get(conta.getCdResponsavel());
								register.put("NM_RESPONSAVEL",pessoaResponsavel.getNmPessoa());
							}
							register.put("DS_HISTORICO",rsmTransferencia.getString("DS_HISTORICO"));
							register.put("VL_MOVIMENTO",rsmTransferencia.getFloat("VL_MOVIMENTO"));
							rsmDepositos.addRegister(register);
						}
					}
				}
				parametros.put("rsmDepositos",rsmDepositos.getLines());
				//
				ResultSetMap rsmCartoes = TituloCreditoServices.getCartoesCredito(cdConta, cdTurno, dtMovimento);
				//
				ResultSetMap rsmCheques = TituloCreditoServices.getTituloCreditoOfTurno(cdConta, cdTurno, dtMovimento);
				ResultSetMap rsmChequesVista    = new ResultSetMap();
				ResultSetMap rsmChequesPrazo    = new ResultSetMap();
				ResultSetMap rsmTitulosCredito2 = new ResultSetMap();
				//
				HashMap<String,Object> registerCheque;
				//
				while(rsmCheques.next()){
					if(rsmCheques.getString("NM_TIPO_DOCUMENTO").indexOf("Cheque") >= 0){ 
						registerCheque = new HashMap<String,Object>();
						registerCheque.put("DT_VENCIMENTO",rsmCheques.getTimestamp("DT_VENCIMENTO"));
						registerCheque.put("NM_EMISSOR",rsmCheques.getString("NM_EMISSOR"));
						registerCheque.put("NR_CPF_CNPJ",null);
						registerCheque.put("NR_DOCUMENTO",rsmCheques.getString("NR_DOCUMENTO"));
						registerCheque.put("VL_TITULO",rsmCheques.getFloat("VL_TITULO"));
						registerCheque.put("CD_TITULO_CREDITO",rsmCheques.getInt("CD_TITULO_CREDITO"));
						if(rsmCheques.getTimestamp("DT_VENCIMENTO")!=null) {
							rsmCheques.getTimestamp("DT_VENCIMENTO").setHours(0);
							rsmCheques.getTimestamp("DT_VENCIMENTO").setMinutes(0);
							rsmCheques.getTimestamp("DT_VENCIMENTO").setSeconds(0);
						}
						if(rsmCheques.getTimestamp("DT_VENCIMENTO").equals(new Timestamp(dtMovimento.getTimeInMillis()))) {
							if(!rsmChequesVista.locate("CD_TITULO_CREDITO", rsmCheques.getInt("CD_TITULO_CREDITO")))
								rsmChequesVista.addRegister(registerCheque);
						}
						else if(!rsmChequesPrazo.locate("CD_TITULO_CREDITO", rsmCheques.getInt("CD_TITULO_CREDITO")))
							rsmChequesPrazo.addRegister(registerCheque);
					}
					else if((rsmCheques.getInt("TP_FORMA_PAGAMENTO") == FormaPagamentoServices.TITULO_CREDITO) && !rsmTitulosCredito2.locate("CD_TITULO_CREDITO", rsmCheques.getInt("CD_TITULO_CREDITO")))
						rsmTitulosCredito2.addRegister(rsmCheques.getRegister());
				}
				parametros.put("rsmTitulosCredito",rsmTitulosCredito2.getLines());
				parametros.put("rsmChequeVista",rsmChequesVista.getLines());
				parametros.put("rsmChequePrazo",rsmChequesPrazo.getLines());
				parametros.put("rsmCartoes",rsmCartoes.getLines());				
				// Resumo de Itens por Grupo
				ResultSetMap rsmResumo = DocumentoSaidaItemServices.findResumoOfItensByGrupoPCB(cdConta, cdFechamento, dtMovimento, cdTurno);
				parametros.put("rsmResumo",rsmResumo.getLines());
				ResultSetMap rsmResumoFechamento = ContaFechamentoServices.getResumoByPagamento(cdConta, dtMovimento, cdTurno);
				parametros.put("rsmResumoFechamento",rsmResumoFechamento.getLines());				
				//
				ResultSetMap rsmAcumuladoReceber = ContaFechamentoServices.getResumoOfTpPagamento(cdConta, dtMovimento, 0, false);
				parametros.put("rsmAcumuladoReceber",rsmAcumuladoReceber.getLines());				
				// GERANDO RELATÓRIO			
				Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa1");
				Util.printInFile("C:/TIVIC/marlon.log", "\nparametros: " + parametros.toString());
				//Util.printInFile("C:/TIVIC/marlon.log", "\njrRS: " + jrRS.toString());
//				JasperPrint js =  JasperFillManager.fillReport(pathServer+"fechamento.jasper", parametros, jrRS);			
//				// Criando RELATÓRIO e exportando para o PDF
//				Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa2");
//				JasperExportManager.exportReportToPdfFile(js, pathServer +"fechamento.pdf");				
//				Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa3");
			
				String printFileName = null;
			    try {
			    	Util.printInFile("C:/TIVIC/marlon.log", "\ntry");
			         printFileName = JasperFillManager.fillReportToFile(pathServer+"fechamento.jasper", parametros,jrRS);
			         Util.printInFile("C:/TIVIC/marlon.log", "\ntry0");
			         if (printFileName != null) {
			            /**
			             * 1- export to PDF
			             */
			        	 Util.printInFile("C:/TIVIC/marlon.log", "\ntry1");
			            JasperExportManager.exportReportToPdfFile(printFileName,
			                  "C://TIVIC/sample_report.pdf");

			            /**
			             * 2- export to HTML
			             */
			            Util.printInFile("C:/TIVIC/marlon.log", "\ntry2");
			            JasperExportManager.exportReportToHtmlFile(printFileName,
			                  "C://sample_report.html");

			            /**
			             * 3- export to Excel sheet
			             */
			            Util.printInFile("C:/TIVIC/marlon.log", "\ntry3");
			            JRXlsExporter exporter = new JRXlsExporter();

			            exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME,
			                  printFileName);
			            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
			                  "C://TIVIC/sample_report.pdf");

			            exporter.exportReport();
			         }
			      } catch (JRException e) {
			    	  Util.registerLog(e);
			         e.printStackTrace();
			      }
				return new Result(1);			
		  }
		  else	{		    	  				
			  return new Result(-2,"Ocorreu um erro ao criar o relatório, verifique se todos os campos estão preenchidos");				  
		  }
		}
		catch (Exception e){
			com.tivic.manager.util.Util.registerLog(e);				
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao processar Relatório de Fechamento");					
			
		}
	}
	////////////////////////////
	@Deprecated
	public static Result relatFechaContaCaixa1(int cdUsuario, int cdEmpresa, int cdFechamento, int cdConta, int cdTurno, String dtFechamento, String pathServer){
		try{
			Usuario usuario     =  new Usuario();
			usuario.setCdUsuario(cdUsuario);		
			//Pessoa empresa      = PessoaDAO.get(cdEmpresa);
			String output = "";
			if(usuario != null){
				Empresa empresa     = EmpresaDAO.get(cdEmpresa);
				if(cdEmpresa > 0 && cdFechamento > 0 && cdConta > 0 && cdTurno > 0){
					ContaFinanceira contaFinanceira = ContaFinanceiraDAO.get(cdConta);
					ContaFechamento contaFechamento = ContaFechamentoDAO.get(cdConta, cdFechamento);
					Usuario usuarioConta = UsuarioDAO.get(contaFechamento.getCdResponsavel());
					Pessoa responsavel = PessoaDAO.get(usuarioConta.getCdPessoa());
					Pessoa pessoaUsuario = PessoaDAO.get(usuario.getCdUsuario());
					Turno turno = TurnoDAO.get(cdTurno);
					try {
					
						Connection connect = Conexao.conectar();
						// Definido
	//					response.reset();
	//					response.setContentType("application/pdf");
						GregorianCalendar dtMovimento = (GregorianCalendar)contaFechamento.getDtFechamento().clone();
						dtMovimento.set(Calendar.HOUR, 0);
						dtMovimento.set(Calendar.MINUTE, 0);
						dtMovimento.set(Calendar.SECOND, 0);
						dtMovimento.set(Calendar.MILLISECOND, 0);
						// Data Final
						HashMap<String,Object> parametros = new HashMap<String,Object>();
						ResultSetMap rsmVendasDias = DocumentoSaidaItemServices.findSaidaByDiaOfProduto(0, cdEmpresa, dtMovimento, 0, cdTurno, true, false);
						JRResultSetDataSource jrRS = null;
						/*
						 * PARAMETROS
						 */
						String pathLogo         = pathServer+"imagem/";
						parametros.put("nmEmpresa", empresa.getNmPessoa());
						parametros.put("dtFechamento", new Timestamp(dtMovimento.getTimeInMillis()));
						parametros.put("nmTurno",turno.getNmTurno());
						parametros.put("nmResponsavel",responsavel.getNmPessoa());
						parametros.put("nmUsuario",pessoaUsuario.getNmPessoa());
						parametros.put("LOGO",   empresa!=null ? empresa.getImgLogomarca() : "");
						ResultSetMap rsmEncerrantes   = BicoEncerranteServices.getLeituraBicos(cdFechamento);
						ResultSetMap rsmMedicaoFisica = MedicaoFisicaServices.getMedicaoFisicaOf(cdConta, cdFechamento, contaFechamento.getDtFechamento());
						parametros.put("rsmEncerrantes",rsmEncerrantes.getLines());
						parametros.put("rsmMedicaoFisica",rsmMedicaoFisica.getLines());
						ResultSetMap rsmVendasBico = BicoEncerranteServices.getVendasByEncerrantes(cdEmpresa, cdConta, cdTurno, dtMovimento);
						parametros.put("rsmVendasBico",rsmVendasBico.getLines());
						parametros.put("rsmVendasProdutos",rsmVendasDias.getLines());
						// Resumo de Vendas
						ResultSetMap rsmResumoVendas = DocumentoSaidaItemServices.findResumoOfItensByGrupo(cdTurno, dtMovimento);
						parametros.put("rsmResumoVendas", rsmResumoVendas.getLines());
						// Resumo dos Títulos de Crédito
						ResultSetMap rsmTitulosCredito = TituloCreditoServices.getTituloCreditoOfTurno(cdConta, cdTurno, dtMovimento);
						// Recebimentos
						ResultSetMap rsmRecebimentos = MovimentoContaServices.getRecebimentosOf(cdConta, cdFechamento, dtMovimento, cdTurno);
						// Despesas
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("B.dt_movimento", dtFechamento, ItemComparator.EQUAL, Types.TIMESTAMP));
						criterios.add(new ItemComparator("B.cd_conta",""+cdConta,ItemComparator.EQUAL,Types.INTEGER));
						criterios.add(new ItemComparator("B.cd_turno",""+cdTurno,ItemComparator.EQUAL,Types.INTEGER));
						ResultSetMap rsmPagamentos = MovimentoContaPagarServices.find(criterios);
						parametros.put("rsmPagamentos", rsmPagamentos.getLines());
						// Retiradas
						ResultSetMap rsmTransferencia = ContaFechamentoServices.getTransferenciasOf(cdConta, cdFechamento,contaFechamento.getDtFechamento(), cdTurno, false);
						ResultSetMap rsmDepositos = new ResultSetMap();
						HashMap<String,Object> register;
						while(rsmTransferencia.next())	{
							if(rsmTransferencia.getInt("TP_ORIGEM") == 7)	{
								ContaFinanceira conta = ContaFinanceiraDAO.get(rsmTransferencia.getInt("CD_CONTA_DESTINO"));
								
								if(conta.getTpConta() == ContaFinanceiraServices.TP_CONTA_BANCARIA){
									register = new HashMap<String,Object>();
									register.put("NM_CONTA_FINANCEIRA", conta.getNmConta());
									register.put("TP_CONTA","CONTA BANCÁRIA");
									register.put("ID_EXTRATO",rsmTransferencia.getString("ID_EXTRATO"));
									if(conta.getCdResponsavel() > 0){
										register.put("NM_RESPONSAVEL",rsmTransferencia.getString("NM_RESPONSAVEL") + " " + rsmTransferencia.getDateFormat("DT_MOVIMENTO", "dd/MM/yyyy") );
									}
									register.put("DS_HISTORICO",rsmTransferencia.getString("DS_HISTORICO"));
									register.put("VL_MOVIMENTO",rsmTransferencia.getFloat("VL_MOVIMENTO"));
									rsmDepositos.addRegister(register);
								}
							}
						}
						Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa1");
						parametros.put("rsmDepositos",rsmDepositos.getLines());
						//
						ResultSetMap rsmCartoes = TituloCreditoServices.getCartoesCredito(cdConta, cdTurno, dtMovimento);
						
						ResultSetMap rsmCheques = TituloCreditoServices.getTituloCreditoOfTurno(cdConta, cdTurno, dtMovimento);
						ResultSetMap rsmChequesVista = new ResultSetMap();
						ResultSetMap rsmChequesPrazo = new ResultSetMap();
						ResultSetMap rsmTitulosCredito2 = new ResultSetMap();
						
						HashMap<String,Object> registerCheque;
						
						ResultSetMap rsmNotaPrazo = TituloCreditoServices.getTituloCreditoOfTurno(cdConta, cdTurno, dtMovimento, "NP");
						Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa2");
						while(rsmCheques.next()){
							if(rsmCheques.getString("NM_TIPO_DOCUMENTO").indexOf("Cheque") >= 0){ 
								registerCheque = new HashMap<String,Object>();
								registerCheque.put("DT_VENCIMENTO",rsmCheques.getTimestamp("DT_VENCIMENTO"));
								registerCheque.put("NM_EMISSOR",rsmCheques.getString("NM_EMISSOR"));
								registerCheque.put("NR_CPF_CNPJ",null);
								registerCheque.put("NR_DOCUMENTO",rsmCheques.getString("NR_DOCUMENTO"));
								registerCheque.put("VL_TITULO",rsmCheques.getFloat("VL_TITULO"));
								registerCheque.put("CD_TITULO_CREDITO",rsmCheques.getInt("CD_TITULO_CREDITO"));
								rsmCheques.getTimestamp("DT_VENCIMENTO").setHours(0);
								rsmCheques.getTimestamp("DT_VENCIMENTO").setMinutes(0);
								rsmCheques.getTimestamp("DT_VENCIMENTO").setSeconds(0);
								if(rsmCheques.getTimestamp("DT_VENCIMENTO") == new Timestamp(dtMovimento.getTimeInMillis())){
									if(!rsmChequesVista.locate("CD_TITULO_CREDITO", rsmCheques.getInt("CD_TITULO_CREDITO")))
										rsmChequesVista.addRegister(registerCheque);
								}
								else if(!rsmChequesPrazo.locate("CD_TITULO_CREDITO", rsmCheques.getInt("CD_TITULO_CREDITO")))
									rsmChequesPrazo.addRegister(registerCheque);
							}
							else if((rsmCheques.getInt("TP_FORMA_PAGAMENTO") == FormaPagamentoServices.TITULO_CREDITO) && !rsmTitulosCredito2.locate("CD_TITULO_CREDITO", rsmCheques.getInt("CD_TITULO_CREDITO")))
								rsmTitulosCredito2.addRegister(rsmCheques.getRegister());
						}
						parametros.put("rsmTitulosCredito",rsmTitulosCredito2.getLines());
						parametros.put("rsmChequeVista",rsmChequesVista.getLines());
						parametros.put("rsmChequePrazo",rsmChequesPrazo.getLines());
						parametros.put("rsmCartoes",rsmCartoes.getLines());
						// Resumo de Itens por Grupo
						ResultSetMap rsmResumo = DocumentoSaidaItemServices.findResumoOfItensByGrupoPCB(cdConta, cdFechamento, dtMovimento, cdTurno);
						parametros.put("rsmResumo",rsmResumo.getLines());
						ResultSetMap rsmResumoFechamento = ContaFechamentoServices.getResumoByPagamento(cdConta, dtMovimento, cdTurno);
						parametros.put("rsmResumoFechamento",rsmResumoFechamento.getLines());
	
						ResultSetMap rsmAcumuladoReceber = ContaFechamentoServices.getResumoOfTpPagamento(cdConta, dtMovimento, 0, false);
						parametros.put("rsmAcumuladoReceber",rsmAcumuladoReceber.getLines());
						Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa3");
						String subReport1  = "/fechamento_leitura_bico_tanque";
						String subReport2  = "/fechamento_medicao_fisica";
						String subReport3  = "/fechamento_estoque";
						String subReport4  = "/fechamento_mov_bico";
						String subReport5  = "/fechamento_resumo_vendas";
						String subReport6  = "/fechamento_titulos";
						String subReport7  = "/fechamento_despesas";
						// RETIRADA/DEPÓSITO BANCÁRIO
						String subReport8  = "/fechamento_retirada";
						String subReport9  = "/fechamento_cheques_a_vista";
						String subReport10 = "/fechamento_cheque_prazo";
						String subReport11 = "/fechamento_cartoes";
						String subReport12 = "/fechamento_subreport1";
						String subReport13 = "/fechamento_resumo_fechamento";
						String subReport14 = "/fechamento_acumulado_receber";
						String subReport15 = "/fechamento_receitas";
						Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa4");
						ArrayList<String> listaSubReports = new ArrayList<String>();
						listaSubReports.add(subReport1);
						listaSubReports.add(subReport2);
						listaSubReports.add(subReport3);
						listaSubReports.add(subReport4);
						listaSubReports.add(subReport5);
						listaSubReports.add(subReport6);
						listaSubReports.add(subReport7);
						listaSubReports.add(subReport8);
						listaSubReports.add(subReport9);
						listaSubReports.add(subReport10);
						listaSubReports.add(subReport11);
						listaSubReports.add(subReport12);
						listaSubReports.add(subReport13);
						listaSubReports.add(subReport14);
						listaSubReports.add(subReport15);
						Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa5");
						parametros.put("SUBREPORT_NAMES", listaSubReports);
						
//						ConfManager conf = Util.getConfManager();
//				    	String reportPath = conf.getProps().getProperty("REPORT_PATH");
//				    	String path = ContextManager.getRealPath()+"/"+reportPath + "/pcb/";
						Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa6");
				    	parametros.put("SUBREPORT_DIR", pathServer);
				    	Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa7");
				    	HttpSession session = null;
//					    ReportServices.setReportSession(session,pathServer+"/fechamento", parametros);
//					    ReportServlet report = new ReportServlet();
//						report.doPost(request, response);
//					    
//						JasperPrint js =  JasperFillManager.fillReport(pathServer+"fechamento.jasper", parametros, jrRS);			
//						// Criando relatório e exportando para o PDF
//						Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa2");
//						JasperExportManager.exportReportToPdfFile(js, pathServer +"fechamento.pdf");				
//						Util.printInFile("C:/TIVIC/marlon.log", "\nrelatFechaContaCaixa3");
						return new Result(1);			
						
					}
					catch (Exception cnf){
						cnf.printStackTrace(System.out);
						Util.registerLog(cnf);
						output = "Erro ao processar Relatório de Fechamento";
						return new Result(-1,"Erro ao processar Relatório de Fechamento");
					}
				  }
			      else	{
					  return new Result(-2,"Ocorreu um erro ao criar o relatório, verifique se todos os campos estão preenchidos");
				  }
			}else{
				
				return new Result(-3,"Ocorreu um erro ao criar relatório, faça um novo login no sistema e tente novamente!");
			}
		}catch(Exception e){
			Util.registerLog(e);
			return new Result(-4,"Erro geral, entre em contato com o suporte!");
		}
	}
	///////////////////////////
	
	/**
	 *  Método para gerar relatório de fechamento de caixa.
	 */
	
	public static Result gerarRelatorioFechamento(int cdUsuario, int cdEmpresa, int cdFechamento, int cdConta, int cdTurno, String dtFechamento){
		boolean isConnectionNull = true;
		Connection connection = null;
		
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			ContaFinanceira contaFinanceira = ContaFinanceiraDAO.get(cdConta);
			ContaFechamento contaFechamento = ContaFechamentoDAO.get(cdConta, cdFechamento);
			Usuario usuarioConta = UsuarioDAO.get(contaFechamento.getCdResponsavel());
			Pessoa responsavel = PessoaDAO.get(usuarioConta.getCdPessoa());
			Pessoa pessoaUsuario = PessoaDAO.get(cdUsuario);
			Turno turno = TurnoDAO.get(cdTurno);
			Empresa empresa     = EmpresaDAO.get(cdEmpresa);
			
			String nmSupervisor = "";
			if( contaFechamento.getCdSupervisor() > 0 ){
				Usuario usuarioSupervisor = UsuarioDAO.get(contaFechamento.getCdSupervisor());
				Pessoa supervisor = PessoaDAO.get(usuarioSupervisor.getCdPessoa());
				nmSupervisor = supervisor.getNmPessoa();
			}
					

			ResultSetMap rsm = new ResultSetMap();
			HashMap<String, Object> param = new HashMap<String, Object>();
			
			if(cdEmpresa > 0 && cdFechamento > 0 && cdConta > 0 && cdTurno > 0){
				
				GregorianCalendar dtMovimentoInicial = (GregorianCalendar) contaFechamento.getDtFechamento().clone();
				GregorianCalendar dtMovimentoFinal   = (GregorianCalendar) contaFechamento.getDtFechamento().clone();
				
				dtMovimentoInicial.set(Calendar.HOUR, 0);
				dtMovimentoInicial.set(Calendar.MINUTE, 0);
				dtMovimentoInicial.set(Calendar.SECOND, 0);
				
				dtMovimentoFinal.set(Calendar.HOUR, 23);
				dtMovimentoFinal.set(Calendar.MINUTE, 59);
				dtMovimentoFinal.set(Calendar.SECOND, 59);
				
				rsm = new ResultSetMap(DocumentoSaidaItemServices.findSaidaAndItens(cdTurno, dtMovimentoInicial, dtMovimentoFinal));
				ResultSetMap rsmRecebimentos = MovimentoContaServices.getRecebimentosOf(cdConta, cdFechamento, dtMovimentoFinal, cdTurno);
				if( rsmRecebimentos != null )
					param.put("rsmRecebimentos", rsmRecebimentos.getLines());
				ResultSetMap rsmTransferencia = ContaFechamentoServices.getTransferenciasOf(cdConta, cdFechamento, contaFechamento.getDtFechamento(), cdTurno, true);
				if( rsmTransferencia != null )
					param.put("rsmTransferencias", rsmTransferencia.getLines());
				ResultSetMap rsmResumoFormaPag = ContaFechamentoServices.getResumoFormaPagamentoFinanceiro(cdConta, cdFechamento,contaFechamento.getDtFechamento(), cdTurno);
				if( rsmResumoFormaPag != null )
					param.put("rsmResumo", rsmResumoFormaPag.getLines());
			}
			
			ConfManager conf = Util.getConfManager();
	    	String reportPath = conf.getProps().getProperty("REPORT_PATH");
	    	String path = ContextManager.getRealPath() + reportPath + "/adm/";
	    	
	    	/**
	    	 * Resumo Dinheiro
	    	 */
	    	param.put("vlSaldoInicial", String.valueOf(0));
	    	param.put("vlTransferencia", String.valueOf(0));
	    	param.put("vlTotalEntradas", String.valueOf(0));
	    	param.put("vlRecebimento", String.valueOf(0));
	    	param.put("vlDespesas", String.valueOf(0));
	    	
	    	
	    	param.put("SUBREPORT_DIR", path);
	    	param.put("nmEmpresa", empresa.getNmPessoa());
	    	param.put("nmConta", contaFinanceira.getNmConta());
	    	param.put("nmTurno", turno.getNmTurno());
	    	param.put("nmResponsavel", responsavel.getNmPessoa());
	    	param.put("nmSupervisor", nmSupervisor);
	    	param.put("nmUsuario", pessoaUsuario.getNmPessoa());
	    	param.put("dtFechamento", new Date( contaFechamento.getDtFechamento().getTimeInMillis() ) );
	    	param.put("LOGO",   empresa!=null ? empresa.getImgLogomarca() : null);
	    	param.put("LG_SHOW_ESTOQUE", false);
	    	param.put("LG_SHOW_VENDA_DIA", false);
			
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();
	
			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}
