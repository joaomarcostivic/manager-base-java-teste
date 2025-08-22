package com.tivic.manager.ctb;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.ctb.EmpresaExercicio;
import com.tivic.manager.grl.CnaePessoaJuridica;
import com.tivic.manager.grl.CnaePessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.util.DefaultProcess;
import com.tivic.manager.util.ProcessManager;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class EmpresaExercicioServices {

	public static String[] tipoCalculoIrpj = {"Lucro presumido","Lucro Real","Lucro Arbitrado","Simples","Imune/Isenta"};

	public static String[] tipoTermos = {"Lucro/Prejuízo","Superávit/Déficit","Sobras/Perdas"};

	public static String[] situacaoExercicio = {"Em aberto","Encerrado","Encerramento cancelado"};

	public static final int ST_EM_ABERTO = 0;
	public static final int ST_ENCERRADO = 1;
	public static final int ST_CANCELADO = 2;

	public static final int ERR_GERAL                            = -1;
	public static final int ERR_NR_DOCUMENTO                     = -2;
	public static final int ERR_DATA_ENCERRAMENTO_NULL           = -3;
	public static final int ERR_CD_RESPONSAVEL_ENCERRAMENTO_NULL = -4;

	public static Result save(EmpresaExercicio empresaExercicio, Empresa empresa) {
		return save(empresaExercicio, empresa, null, 0, null);
	}

	public static Result save(EmpresaExercicio empresaExercicio, Empresa empresa, PessoaEndereco pessoaEndereco) {
		return save(empresaExercicio, empresa, pessoaEndereco, 0, null);
	}

	public static Result save(EmpresaExercicio empresaExercicio, Empresa empresa, PessoaEndereco pessoaEndereco, int cdCnae) {
		return save(empresaExercicio, empresa, pessoaEndereco, cdCnae, null);
	}

	public static Result save(EmpresaExercicio empresaExercicio, Empresa empresa, PessoaEndereco pessoaEndereco, int cdCnae, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if (empresa == null)
				return new Result(-1, "Empresa inválida!");

			int retorno;

			//Empresa
			int cdEmpresa = empresa.getCdEmpresa();
			if(cdEmpresa <= 0){
				retorno = com.tivic.manager.ctb.EmpresaDAO.insert(empresa, connect);
				if (retorno > 0) {
					empresa.setCdEmpresa(retorno);
					empresaExercicio.setCdEmpresa(retorno);
				}
				else {
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar dados contábeis da empresa!");
				}

				retorno = EmpresaExercicioDAO.insert(empresaExercicio, connect);
				if (retorno < 0) {
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar dados do exercício contábil da empresa!");
				}
			}
			else {
				retorno = com.tivic.manager.ctb.EmpresaDAO.update(empresa, connect);
				if (retorno < 0) {
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar dados contábeis da empresa!");
				}
				retorno = retorno > 0?empresa.getCdEmpresa():retorno;

				if (com.tivic.manager.ctb.EmpresaExercicioDAO.get(cdEmpresa, empresaExercicio.getNrAnoExercicio()) == null) {
					retorno = EmpresaExercicioDAO.insert(empresaExercicio, connect);
				}
				else
					retorno = EmpresaExercicioDAO.update(empresaExercicio, connect);

				retorno = retorno > 0?empresaExercicio.getCdEmpresa():retorno;

				if(retorno < 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar atualizar dados do exercício contábil da empresa!");
				}
			}
			//Endereço
			if(pessoaEndereco != null){
				if(pessoaEndereco.getCdPessoa() == 0){
					pessoaEndereco.setCdPessoa(empresa.getCdEmpresa());
					retorno = PessoaEnderecoDAO.insert(pessoaEndereco, connect);
					pessoaEndereco.setCdEndereco(retorno);
				}
				else
					retorno = PessoaEnderecoDAO.update(pessoaEndereco, connect);

				if(retorno < 0){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar dados do endereço da empresa!");
				}
			}

			//CNAE
			if (cdCnae > 0) {
				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_cnae_pessoa_juridica " +
					                                               "WHERE cd_pessoa  = " +empresa.getCdPessoa()+
																   "AND lg_principal = 1");
				pstmt.executeUpdate();

				if (CnaePessoaJuridicaDAO.insert(new CnaePessoaJuridica(empresa.getCdPessoa(), cdCnae, 1), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao salvar o CNAE da empresa!");
				}
			}

			if(retorno < 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			Result result = new Result(retorno);
			result.addObject("empresa", empresa);
			result.addObject("endereco", pessoaEndereco);
			result.addObject("cdEmpresa", empresa.getCdEmpresa());
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-3, "Erro ao tentar incluir ou alterar dados da empresa!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getLancamentos(int cdEmpresa, String nrAnoExercicio) {
		return getLancamentos(cdEmpresa, nrAnoExercicio, null);
	}

	public static ResultSetMap getLancamentos(int cdEmpresa, String nrAnoExercicio, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			EmpresaExercicio empresaExercicio = EmpresaExercicioDAO.get(cdEmpresa, nrAnoExercicio, connect);

			GregorianCalendar dtInicio = empresaExercicio.getDtInicio();
			dtInicio.set(Calendar.HOUR_OF_DAY, 0);
			dtInicio.set(Calendar.MINUTE, 0);
			dtInicio.set(Calendar.SECOND, 0);
			dtInicio.set(Calendar.MILLISECOND, 1);

			GregorianCalendar dtTermino = empresaExercicio.getDtTermino();
			dtTermino.set(Calendar.HOUR_OF_DAY, 23);
			dtTermino.set(Calendar.MINUTE, 59);
			dtTermino.set(Calendar.SECOND, 59);
			dtTermino.set(Calendar.MILLISECOND, 59);

			PreparedStatement pstmt = connect.prepareStatement("SELECT A1.cd_lancamento, " +
				  "	   A1.cd_conta_debito AS cd_conta, A1.cd_historico, A1.nr_documento, " +
				  "	   A1.vl_lancamento, A1.txt_historico, " +
				  "	   A1.txt_observacao, A1.st_lancamento, " +
				  "	   B1.nm_historico, " +
				  "    C1.dt_lancamento, C1.id_lancamento, " +
				  "	   D1.nr_conta, D1.nr_digito, D1.tp_conta, D1.tp_natureza, D1.id_conta, " +
				  "	   E1.nr_lote, E1.vl_lote, E1.st_lote, " +
				  "    F1.nm_conta, " +
				  "	   'D' AS tp_lancamento " +
				  "FROM CTB_LANCAMENTO_DEBITO A1 " +
				  "    LEFT OUTER JOIN CTB_CONTA_PLANO_CONTAS D1 ON (A1.cd_conta_debito = D1.cd_conta_plano_contas) " +
				  "    LEFT OUTER JOIN CTB_LANCAMENTO C1 ON (A1.cd_lancamento = C1.cd_lancamento) " +
				  "    LEFT OUTER JOIN CTB_LOTE E1 ON (C1.cd_lote = E1.cd_lote) " +
				  "    LEFT OUTER JOIN CTB_CONTA F1 ON (D1.cd_conta = F1.cd_conta) " +
				  "    LEFT OUTER JOIN CTB_HISTORICO B1 ON (A1.cd_historico = B1.cd_historico) " +
				  "WHERE (C1.dt_lancamento >= ? AND C1.dt_lancamento <= ?)" +
				  "    AND C1.cd_empresa = ? " +
				  "UNION " +
				  "SELECT A2.cd_lancamento, " +
				  "	   A2.cd_conta_credito AS cd_conta, A2.cd_historico, A2.nr_documento, " +
				  "	   A2.vl_lancamento, A2.txt_historico, " +
				  "	   A2.txt_observacao, A2.st_lancamento, " +
				  "	   B2.nm_historico, " +
				  "    C2.dt_lancamento, C2.id_lancamento, " +
				  "	   D2.nr_conta, D2.nr_digito, D2.tp_conta, D2.tp_natureza, D2.id_conta, " +
				  "	   E2.nr_lote, E2.vl_lote, E2.st_lote, " +
				  "    F2.nm_conta, " +
				  "	   'C' AS tp_lancamento " +
				  "FROM CTB_LANCAMENTO_CREDITO A2 " +
				  "    LEFT OUTER JOIN CTB_CONTA_PLANO_CONTAS D2 ON (A2.cd_conta_credito = D2.cd_conta_plano_contas) " +
				  "    LEFT OUTER JOIN CTB_LANCAMENTO C2 ON (A2.cd_lancamento = C2.cd_lancamento) " +
				  "    LEFT OUTER JOIN CTB_LOTE E2 ON (C2.cd_lote = E2.cd_lote) " +
				  "    LEFT OUTER JOIN CTB_CONTA F2 ON (D2.cd_conta = F2.cd_conta) " +
				  "    LEFT OUTER JOIN CTB_HISTORICO B2 ON (A2.cd_historico = B2.cd_historico) " +
				  "WHERE (C2.dt_lancamento >= ? AND C2.dt_lancamento <= ?)" +
				  "    AND C2.cd_empresa = ?");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtInicio));
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtTermino));
			pstmt.setInt(3, cdEmpresa);
			pstmt.setTimestamp(4, Util.convCalendarToTimestamp(dtInicio));
			pstmt.setTimestamp(5, Util.convCalendarToTimestamp(dtTermino));
			pstmt.setInt(6, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fieldsOrder = new ArrayList<String>();
			fieldsOrder.add("dt_lancamento");
			fieldsOrder.add("nr_conta");
			rsm.orderBy(fieldsOrder);

			return rsm;
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

	public static Result processarEncerramento(int cdEmpresa, String nrAnoExercicio, GregorianCalendar dtEncerramento, int cdResponsavelEncerramento, int cdPlanoContas) {
		return processarEncerramento(cdEmpresa, nrAnoExercicio, dtEncerramento, cdResponsavelEncerramento, cdPlanoContas, null);
	}

	public static Result processarEncerramento(int cdEmpresa, String nrAnoExercicio, GregorianCalendar dtEncerramento, int cdResponsavelEncerramento, int cdPlanoContas, Connection connect) {
		class DefaultProcessEncerramentoExercicio extends DefaultProcess {

			private int cdEmpresa;
			private String nrAnoExercicio;
			private GregorianCalendar dtEncerramento;
			private int cdResponsavelEncerramento;
			private int cdPlanoContas;
			private Connection connect = null;

			public DefaultProcessEncerramentoExercicio(int cdEmpresa, String nrAnoExercicio, GregorianCalendar dtEncerramento, int cdResponsavelEncerramento, int cdPlanoContas, int countContas, Connection connect) {
				super("encerramentoExercicio", 0, (float)countContas + 1, 0);
				setConnection(connect);
				setCdEmpresa(cdEmpresa);
				setNrAnoExercicio(nrAnoExercicio);
				setDtEncerramento(dtEncerramento);
				setCdResponsavelEncerramento(cdResponsavelEncerramento);
				setCdPlanoContas(cdPlanoContas);
			}

			public int getCdEmpresa() {
				return cdEmpresa;
			}

			public void setCdEmpresa(int cdEmpresa) {
				this.cdEmpresa = cdEmpresa;
			}

			public String getNrAnoExercicio() {
				return nrAnoExercicio;
			}

			public void setNrAnoExercicio(String nrAnoExercicio) {
				this.nrAnoExercicio = nrAnoExercicio;
			}

			public GregorianCalendar getDtEncerramento() {
				return dtEncerramento;
			}

			public void setDtEncerramento(GregorianCalendar dtEncerramento) {
				this.dtEncerramento = dtEncerramento;
			}

			public int getCdResponsavelEncerramento() {
				return cdResponsavelEncerramento;
			}

			public void setCdResponsavelEncerramento(int cdResponsavelEncerramento) {
				this.cdResponsavelEncerramento = cdResponsavelEncerramento;
			}

			public int getCdPlanoContas() {
				return cdPlanoContas;
			}

			public void setCdPlanoContas(int cdPlanoContas) {
				this.cdPlanoContas = cdPlanoContas;
			}

			public Connection getConnection() {
				return connect;
			}

			public void setConnection(Connection connection) {
				this.connect = connection;
			}

			@SuppressWarnings("unchecked")
			public void run() {
				boolean isConnectionNull = connect == null;
				try {
					connect = getConnection() == null ? Conexao.conectar() : getConnection();
					connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

					System.out.println("cdEmpresa = " + cdEmpresa);
					System.out.println("nrAnoExercicio = " + nrAnoExercicio);
					System.out.println("getCdEmpresa = " + getCdEmpresa());
					System.out.println("getNrAnoExercicio = " + getNrAnoExercicio());
					System.out.println("getDtEncerramento = " + getDtEncerramento());
					System.out.println("getCdResponsavelEncerramento = " + getCdResponsavelEncerramento());
					System.out.println("getCdPlanoContas = " + getCdPlanoContas());

					EmpresaExercicio empresaExercicio = EmpresaExercicioDAO.get(getCdEmpresa(), getNrAnoExercicio());
					PlanoContas planoContas = PlanoContasDAO.get(getCdPlanoContas());

					int cdContaDespesa = planoContas.getCdContaDespesa();
					int cdContaReceita = planoContas.getCdContaReceita();
					int cdContaLucro = planoContas.getCdContaLucro();
					int cdContaPrejuizo = planoContas.getCdContaPrejuizo();
					int cdContaResultado = planoContas.getCdContaResultado();
					int cdLancamentoResultado = empresaExercicio.getCdLancamentoResultado();
					int retorno = 0;
					Result retornoHash = null;
					float vlTotalDebitos = 0;
					float vlTotalCreditos = 0;

					ContaPlanoContas contaPlanoContas = ContaPlanoContasServices.getByConta(getCdPlanoContas(), cdContaResultado);
					int cdContaPlanoContasResultado = contaPlanoContas.getCdContaPlanoContas();

					contaPlanoContas = ContaPlanoContasServices.getByConta(getCdPlanoContas(), cdContaLucro);
					int cdContaPlanoContasLucro = contaPlanoContas.getCdContaPlanoContas();

					contaPlanoContas = ContaPlanoContasServices.getByConta(getCdPlanoContas(), cdContaPrejuizo);
					int cdContaPlanoContasPrejuizo = contaPlanoContas.getCdContaPlanoContas();

					contaPlanoContas = ContaPlanoContasServices.getByConta(getCdPlanoContas(), cdContaDespesa);
					int cdContaPlanoContasDespesa = contaPlanoContas.getCdContaPlanoContas();

					contaPlanoContas = ContaPlanoContasServices.getByConta(getCdPlanoContas(), cdContaReceita);
					int cdContaPlanoContasReceita = contaPlanoContas.getCdContaPlanoContas();

					System.out.println("cdContaPlanoContasResultado = " + cdContaPlanoContasResultado);
					System.out.println("cdContaPlanoContasLucro = " + cdContaPlanoContasLucro);
					System.out.println("cdContaPlanoContasPrejuizo = " + cdContaPlanoContasPrejuizo);

					Lancamento lancamentoResultado = LancamentoDAO.get(cdLancamentoResultado);
					LancamentoDebito lancamentoDebito = LancamentoDebitoDAO.get(cdLancamentoResultado, cdContaPlanoContasResultado);
					LancamentoCredito lancamentoCredito = LancamentoCreditoDAO.get(cdLancamentoResultado, cdContaPlanoContasResultado);

					if (cdLancamentoResultado <= 0) {
						lancamentoResultado = new Lancamento(0, /* cdLancamento */
							   0, /* cdLote */
							   0, /* cdLancamentoAuto */
							   getDtEncerramento(), /* dtLancamento */
							   0, /* vlTotal */
							   0, /* lgProvisao */
							   getCdEmpresa(), /* cdEmpresa */
							   "RE - " + getNrAnoExercicio(), /* idLancamento */
							   0, /* cdMovimentoConta */
							   0, /* cdContaFinanceira */
							   0, /* cdContaReceber */
							   0 /* cdContaPagar */);
						retorno = com.tivic.manager.ctb.LancamentoDAO.insert(lancamentoResultado, connect);
						if (retorno > 0) {
							lancamentoResultado.setCdLancamento(retorno);
							empresaExercicio.setCdLancamentoResultado(retorno);
							cdLancamentoResultado = retorno;
						}
						else {
							Conexao.rollback(connect);
							return;
						}
						lancamentoDebito = new LancamentoDebito(cdLancamentoResultado, /* cdLancamento */
							cdContaPlanoContasResultado, /* cdContaDebito */
							0, /* cdHistorico */
							"", /* nrDocumento */
							0, /* vlLancamento */
							"", /* txtHistorico */
							"", /* txtObservacao */
							LancamentoServices.ST_LIBERADO /* stLancamento */);
						retorno = com.tivic.manager.ctb.LancamentoDebitoDAO.insert(lancamentoDebito, connect);
						if (retorno < 0) {
							Conexao.rollback(connect);
							return;
						}

						lancamentoCredito = new LancamentoCredito(cdLancamentoResultado, /* cdLancamento */
								cdContaPlanoContasResultado, /* cdContaDebito */
								0, /* cdHistorico */
								"", /* nrDocumento */
								0, /* vlLancamento */
								"", /* txtHistorico */
								"", /* txtObservacao */
								LancamentoServices.ST_LIBERADO /* stLancamento */);
						retorno = com.tivic.manager.ctb.LancamentoCreditoDAO.insert(lancamentoCredito, connect);
						if (retorno < 0) {
							Conexao.rollback(connect);
							return;
						}
					}
					/* Cria rsm para armazenar todos os lançamentos de resultado (despesas/receitas) */
					/* Cria nova coluna (TP_CONTA_RESULTADO) para armazenar o tipo de conta de resultado ([D]espesas/[R]eceita) */
					ResultSetMap rsmContasDespesa = ContaPlanoContasServices.getAllContas(cdPlanoContas, cdContaPlanoContasDespesa, ContaPlanoContasServices.TP_ANALITICA);
					ResultSetMap rsmContasReceita = ContaPlanoContasServices.getAllContas(cdPlanoContas, cdContaPlanoContasReceita, ContaPlanoContasServices.TP_ANALITICA);
					ResultSetMap rsmContasResultado = new ResultSetMap();
					HashMap<String, Object> reg = null;

					rsmContasDespesa.beforeFirst();
					while (rsmContasDespesa != null && rsmContasDespesa.next()) {
						reg = rsmContasDespesa.getRegister();
						reg.put("TP_CONTA_RESULTADO", "D");
						rsmContasResultado.addRegister(reg);
						System.out.println("regDespesa = "+reg.toString());
					}

					rsmContasReceita.beforeFirst();
					while (rsmContasReceita != null && rsmContasReceita.next()) {
						reg = rsmContasReceita.getRegister();
						reg.put("TP_CONTA_RESULTADO", "R");
						rsmContasResultado.addRegister(reg);
						System.out.println("regReceita = "+reg.toString());
					}

					PreparedStatement pstmt = connect.prepareStatement("SELECT A.* " +
						"FROM ctb_conta_plano_contas A " +
						"WHERE A.cd_plano_contas = ? " +
						"   AND A.tp_conta = ? " +
						"ORDER BY A.nr_conta");
					pstmt.setInt(1, getCdPlanoContas());
					pstmt.setInt(2, ContaPlanoContasServices.TP_ANALITICA);
					ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
						retornoHash = com.tivic.manager.ctb.EmpresaExercicioServices.encerrarConta(cdEmpresa, nrAnoExercicio, ContaPlanoContasDAO.get(rs.getInt("CD_CONTA_PLANO_CONTAS")), cdLancamentoResultado, rsmContasResultado, connect);
						if (retornoHash.getCode() < 0) {
							System.out.println("DEU ERRO !!! - 1");
							if (isConnectionNull) {
								Conexao.rollback(connect);
							}
							this.progress = this.maxProgress;
							return;
						}
						HashMap<String, Object> objetos = retornoHash.getObjects();
						HashMap<String, Object> retornoHashObj = (HashMap<String, Object>)objetos.get("retornoHash");

						if (retornoHashObj.get("TIPO_CONTA") == "R") {
							vlTotalDebitos += (Float)retornoHashObj.get("SALDO");
						}
						else if (retornoHashObj.get("TIPO_CONTA") == "D"){
							vlTotalCreditos += (Float)retornoHashObj.get("SALDO");
						}

						System.out.println("------------------------------------------");
						System.out.println("objetos = "+objetos.toString());
						System.out.println("retornoHashObj = "+retornoHashObj.toString());
						System.out.println("SALDO = "+(Float)retornoHashObj.get("SALDO"));
						System.out.println("TIPO_CONTA = "+retornoHashObj.get("TIPO_CONTA"));
						System.out.println("vlTotalDebitos 1 = "+vlTotalDebitos);
						System.out.println("vlTotalCreditos 1 = "+vlTotalCreditos);
						System.out.println("this.progress = "+this.progress);
						System.out.println("retornoHash = "+retornoHash.toString());
						System.out.println("------------------------------------------");

						this.progress ++;
					}

					/* Grava saldos calculados das contas de despesa e receita na conta de "Resultado do Exercício" */
					lancamentoResultado.setVlTotal((vlTotalCreditos == vlTotalDebitos ? vlTotalCreditos : vlTotalCreditos + vlTotalDebitos));
					lancamentoDebito.setVlLancamento(vlTotalDebitos);
					lancamentoCredito.setVlLancamento(vlTotalCreditos);

					retorno = com.tivic.manager.ctb.LancamentoDAO.update(lancamentoResultado, connect);
					if (retorno < 0) {
						Conexao.rollback(connect);
						return;
					}

					retorno = com.tivic.manager.ctb.LancamentoDebitoDAO.update(lancamentoDebito, connect);
					if (retorno < 0) {
						Conexao.rollback(connect);
						return;
					}

					retorno = com.tivic.manager.ctb.LancamentoCreditoDAO.update(lancamentoCredito, connect);
					if (retorno < 0) {
						Conexao.rollback(connect);
						return;
					}

					/* Grava saldo da conta de "Resultado do Exercício" nas contas patrimoniais "Lucros ou Prejuízos Acumulados" */
					float vlSaldoResultado = vlTotalCreditos - vlTotalDebitos;
					if (vlSaldoResultado > 0) {
						lancamentoDebito = new LancamentoDebito(cdLancamentoResultado, /* cdLancamento */
							cdContaPlanoContasResultado, /* cdContaDebito */
							0, /* cdHistorico */
							"", /* nrDocumento */
							vlSaldoResultado, /* vlLancamento */
							"", /* txtHistorico */
							"", /* txtObservacao */
							LancamentoServices.ST_LIBERADO /* stLancamento */);
						retorno = com.tivic.manager.ctb.LancamentoDebitoDAO.insert(lancamentoDebito, connect);
						if (retorno < 0) {
							Conexao.rollback(connect);
							return;
						}

						LancamentoCredito lancamentoLucro = new LancamentoCredito(cdLancamentoResultado, /* cdLancamento */
							cdContaPlanoContasLucro, /* cdContaCredito */
							0, /* cdHistorico */
							"", /* nrDocumento */
							vlSaldoResultado, /* vlLancamento */
							"", /* txtHistorico */
							"", /* txtObservacao */
							LancamentoServices.ST_LIBERADO /* stLancamento */);
						retorno = com.tivic.manager.ctb.LancamentoCreditoDAO.insert(lancamentoLucro, connect);
						if (retorno < 0) {
							Conexao.rollback(connect);
							return;
						}
					}
					else {
						lancamentoDebito = new LancamentoDebito(cdLancamentoResultado, /* cdLancamento */
							cdContaPlanoContasPrejuizo, /* cdContaDebito */
							0, /* cdHistorico */
							"", /* nrDocumento */
							vlSaldoResultado, /* vlLancamento */
							"", /* txtHistorico */
							"", /* txtObservacao */
							LancamentoServices.ST_LIBERADO /* stLancamento */);
						retorno = com.tivic.manager.ctb.LancamentoDebitoDAO.insert(lancamentoDebito, connect);
						if (retorno < 0) {
							Conexao.rollback(connect);
							return;
						}

						LancamentoCredito lancamentoLucro = new LancamentoCredito(cdLancamentoResultado, /* cdLancamento */
							cdContaPlanoContasResultado, /* cdContaCredito */
							0, /* cdHistorico */
							"", /* nrDocumento */
							vlSaldoResultado, /* vlLancamento */
							"", /* txtHistorico */
							"", /* txtObservacao */
							LancamentoServices.ST_LIBERADO /* stLancamento */);
						retorno = com.tivic.manager.ctb.LancamentoCreditoDAO.insert(lancamentoLucro, connect);
						if (retorno < 0) {
							Conexao.rollback(connect);
							return;
						}


					}
					empresaExercicio.setDtEncerramento(dtEncerramento);
					empresaExercicio.setStExercicio(EmpresaExercicioServices.ST_ENCERRADO);
					empresaExercicio.setCdResponsavelEncerramento(cdResponsavelEncerramento);
					retorno = EmpresaExercicioDAO.update(empresaExercicio, connect);
					if (retorno < 0) {
						Conexao.rollback(connect);
						return;
					}

					if (isConnectionNull)
						connect.commit();
					this.progress = this.getMaxProgress();
				}
				catch(Exception e) {
					e.printStackTrace(System.out);
					this.progress = this.getMaxProgress();
				}
				finally {
					if (isConnectionNull)
						Conexao.desconectar(connect);
				}
			}

		}

		boolean isConnectionNull = connect == null;
		try {
			if (dtEncerramento == null)
				return new Result(ERR_DATA_ENCERRAMENTO_NULL, "Não é possível efetuar o enceramento do exercício. " +
					"Data de encerramento não informada ou inválida.");

			if (cdResponsavelEncerramento <= 0)
				return new Result(ERR_CD_RESPONSAVEL_ENCERRAMENTO_NULL, "Não é possível efetuar o encerramento do exercício. Usuário responsável pelo encerramento não identificado.");

			dtEncerramento.set(Calendar.HOUR_OF_DAY, 0);
			dtEncerramento.set(Calendar.MINUTE, 0);
			dtEncerramento.set(Calendar.SECOND, 0);
			dtEncerramento.set(Calendar.MILLISECOND, 0);

			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			PreparedStatement pstmt = connect.prepareStatement("SELECT COUNT(*) " +
					"FROM ctb_conta_plano_contas " +
					"WHERE cd_plano_contas = ? " +
					"   AND tp_conta = ? ");
			pstmt.setInt(1, cdPlanoContas);
			pstmt.setInt(2, ContaPlanoContasServices.TP_ANALITICA);
			ResultSet rs = pstmt.executeQuery();
			int countContas = rs.next() ? rs.getInt(1) : 0;

			System.out.println("countContas = "+countContas);

			DefaultProcessEncerramentoExercicio defaultProcess = new DefaultProcessEncerramentoExercicio(cdEmpresa, nrAnoExercicio, dtEncerramento, cdResponsavelEncerramento, cdPlanoContas, countContas, Conexao.conectar());
			ProcessManager.registerProcess(defaultProcess);
			ProcessManager.startProcess(defaultProcess);
			return new Result(defaultProcess.getCode(), "");
		}
		catch(Exception e) {
			StringWriter traceWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(traceWriter, false);
			e.printStackTrace(printWriter);
			printWriter.close();
			String trackMessage = traceWriter.getBuffer().toString();
			System.out.println(trackMessage);
			return new Result(-1, "Erro ao efetuar encerramento do exercício de " +
				nrAnoExercicio + ". " + "Anote a mensagem de erro e acione o suporte técnico.\n" +
				trackMessage, e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result encerrarConta(int cdEmpresa, String nrAnoExercicio, ContaPlanoContas contaPlanoContas, int cdLancamentoResultado, ResultSetMap rsmContasResultado) {
		return encerrarConta(cdEmpresa, nrAnoExercicio, contaPlanoContas, cdLancamentoResultado, rsmContasResultado, null);
	}

	public static Result encerrarConta(int cdEmpresa, String nrAnoExercicio, ContaPlanoContas contaPlanoContas, int cdLancamentoResultado, ResultSetMap rsmContasResultado, Connection connect) {
	boolean isConnectionNull = connect == null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			Conta conta = ContaDAO.get(contaPlanoContas.getCdConta());
			EmpresaExercicio empresaExercicio = EmpresaExercicioDAO.get(cdEmpresa, nrAnoExercicio);

			GregorianCalendar dtInicio = empresaExercicio.getDtInicio();
			dtInicio.set(Calendar.HOUR_OF_DAY, 0);
			dtInicio.set(Calendar.MINUTE, 0);
			dtInicio.set(Calendar.SECOND, 0);
			dtInicio.set(Calendar.MILLISECOND, 1);

			GregorianCalendar dtTermino = empresaExercicio.getDtTermino();
			dtTermino.set(Calendar.HOUR_OF_DAY, 23);
			dtTermino.set(Calendar.MINUTE, 59);
			dtTermino.set(Calendar.SECOND, 59);
			dtTermino.set(Calendar.MILLISECOND, 59);

			int cdContaPlanoContas = contaPlanoContas.getCdContaPlanoContas();
			int nrAnoExercicioAnterior = Integer.parseInt(nrAnoExercicio) - 1;
			int cdConta = conta.getCdConta();
			int retorno = 0;

			System.out.println("cdConta = "+cdConta);

			HashMap<String, Object> retornoHash = new HashMap<String, Object>();
			ArrayList<HashMap<String, Object>> totalDebitoCredito = getTotalDebitoCredito(cdEmpresa, nrAnoExercicio, cdContaPlanoContas);

			System.out.println("totalDebitoCredito = "+totalDebitoCredito.toString());

			float vlSaldo = 0, vlSaldoAnterior = 0;
			float vlTotalDebitos = 0, vlTotalCreditos = 0;
			String tpConta = "P";

			if (totalDebitoCredito != null) {
				HashMap<String, Object> totais = (HashMap<String, Object>)totalDebitoCredito.get(0);

				System.out.println("totais = "+totais.toString());

				vlTotalDebitos = (Float)totais.get("VL_TOTAL_DEBITOS");
				vlTotalCreditos = (Float)totais.get("VL_TOTAL_CREDITOS");

				System.out.println("cdContaPlanoContas = "+cdContaPlanoContas);
				System.out.println("nrAnoExercicioAnterior = "+nrAnoExercicioAnterior);
				System.out.println("vlTotalDebitos 2 = "+vlTotalDebitos);
				System.out.println("vlTotalCreditos 2 = "+vlTotalCreditos);
				System.out.println("rsmContasResultado 2 = "+rsmContasResultado.toString());

				/* Verifica se a conta é de resultado */
				if (rsmContasResultado.locate("cd_conta", cdConta, true)) {
					HashMap<String, Object> register = rsmContasResultado.getRegister();
					tpConta = (String)register.get("TP_CONTA_RESULTADO");

					System.out.println("register 2 = "+register.toString());

					if (register.get("TP_CONTA_RESULTADO") == "D") {
						vlSaldo = vlTotalDebitos - vlTotalCreditos;
					}
					else {
						vlSaldo = vlTotalCreditos - vlTotalDebitos;
					}
				}
				else { /* Se for uma conta patrimonial */
					tpConta = "P";
					EmpresaExercicio exercicioAnterior = EmpresaExercicioDAO.get(cdEmpresa, Integer.toString(nrAnoExercicioAnterior));
					if (exercicioAnterior != null && SaldoContaExercicioDAO.get(cdEmpresa, Integer.toString(nrAnoExercicioAnterior), cdContaPlanoContas) != null) {
						vlSaldoAnterior = SaldoContaExercicioDAO.get(cdEmpresa, Integer.toString(nrAnoExercicioAnterior), cdContaPlanoContas).getVlSaldo();
					}
					if (contaPlanoContas.getTpNatureza() == ContaPlanoContasServices.TP_DEVEDORA) {
						vlSaldo = (vlSaldoAnterior - vlTotalDebitos) + vlTotalCreditos;
					}
					else {
						vlSaldo = (vlSaldoAnterior - vlTotalCreditos) + vlTotalDebitos;
					}
					SaldoContaExercicio saldoContaExercicio = SaldoContaExercicioDAO.get(cdEmpresa, nrAnoExercicio, cdContaPlanoContas);
					if (saldoContaExercicio == null) {
						saldoContaExercicio = new SaldoContaExercicio(cdEmpresa,
							nrAnoExercicio,
							cdContaPlanoContas,
							vlSaldo);
						retorno = com.tivic.manager.ctb.SaldoContaExercicioDAO.insert(saldoContaExercicio, connect);
					}
					else {
						saldoContaExercicio.setVlSaldo(vlSaldo);
						retorno = com.tivic.manager.ctb.SaldoContaExercicioDAO.update(saldoContaExercicio, connect);
					}
					if (retorno < 0) {
						Conexao.rollback(connect);
						return new Result(-1, "Não é possível efetuar o encerramento do exercício. " +
							"Ocorreram erros na geração do saldo da conta.");
					}

				}
			}
			System.out.println("tpConta = "+tpConta);
			retornoHash.put("VL_TOTAL_DEBITOS", (Float)vlTotalDebitos);
			retornoHash.put("VL_TOTAL_CREDITOS", (Float)vlTotalCreditos);
			retornoHash.put("SALDO", (Float)vlSaldo);
			retornoHash.put("TIPO_CONTA", (String)tpConta);

			System.out.println("retornoHash = "+retornoHash.toString());

			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_lancamento_debito SET st_lancamento = ? " +
		      		   "WHERE cd_lancamento IN " +
		      		   "(SELECT cd_lancamento FROM ctb_lancamento WHERE (dt_lancamento > ? AND dt_lancamento <= ?))");

			pstmt.setInt(1, LancamentoServices.ST_LIBERADO);
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtInicio));
			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtTermino));
			retorno = pstmt.executeUpdate();

			if (retorno < 0) {
				Conexao.rollback(connect);
				return new Result(-1, "Não é possível efetuar o encerramento do exercício. " +
					"Ocorreram erros na atualização de lançamentos a débito.");
			}

			pstmt = connect.prepareStatement("UPDATE ctb_lancamento_credito SET st_lancamento = ? " +
		      		   "WHERE cd_lancamento IN " +
		      		   "(SELECT cd_lancamento FROM ctb_lancamento WHERE (dt_lancamento > ? AND dt_lancamento <= ?))");

			pstmt.setInt(1, LancamentoServices.ST_LIBERADO);
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtInicio));
			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtTermino));
			retorno = pstmt.executeUpdate();

			if (retorno < 0) {
				Conexao.rollback(connect);
				return new Result(-1, "Não é possível efetuar o encerramento do exercício. " +
					"Ocorreram erros na atualização de lançamentos a crédito.");
			}

			return new Result(1, "Saldo da conta " + contaPlanoContas.getNrConta() + " encerrado com sucesso!", "retornoHash", retornoHash);
		}
		catch(Exception e) {
			StringWriter traceWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(traceWriter, false);
			e.printStackTrace(printWriter);
			printWriter.close();
			String trackMessage = traceWriter.getBuffer().toString();
			System.out.println(trackMessage);
			return new Result(-1, "Erro ao efetuar encerramento da conta " +
					contaPlanoContas.getCdConta() + ". " + "Anote a mensagem de erro e acione o suporte técnico.\n" +	trackMessage, e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<HashMap<String, Object>> getTotalDebitoCredito(int cdEmpresa, String nrAnoExercicio, int cdContaPlanoContas) {
		return getTotalDebitoCredito(cdEmpresa, nrAnoExercicio, cdContaPlanoContas, null);
	}

	public static ArrayList<HashMap<String, Object>> getTotalDebitoCredito(int cdEmpresa, String nrAnoExercicio, int cdContaPlanoContas, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			EmpresaExercicio empresaExercicio = EmpresaExercicioDAO.get(cdEmpresa, nrAnoExercicio);

			GregorianCalendar dtInicio = empresaExercicio.getDtInicio();
			dtInicio.set(Calendar.HOUR_OF_DAY, 0);
			dtInicio.set(Calendar.MINUTE, 0);
			dtInicio.set(Calendar.SECOND, 0);
			dtInicio.set(Calendar.MILLISECOND, 1);

			GregorianCalendar dtTermino = empresaExercicio.getDtTermino();
			dtTermino.set(Calendar.HOUR_OF_DAY, 23);
			dtTermino.set(Calendar.MINUTE, 59);
			dtTermino.set(Calendar.SECOND, 59);
			dtTermino.set(Calendar.MILLISECOND, 59);

			HashMap<String, Object> register = new HashMap<String, Object>();
			PreparedStatement pstmt = connect.prepareStatement("SELECT SUM(A1.vl_lancamento) AS vl_total_debitos, " +
				"(SELECT SUM(A2.vl_lancamento) " +
					"FROM ctb_lancamento_credito A2, ctb_lancamento B2 " +
					"WHERE (A2.cd_lancamento = B2.cd_lancamento) " +
					"   AND (B2.dt_lancamento >= ? AND B2.dt_lancamento <= ?) AND A2.cd_conta_credito = ?) AS vl_total_creditos " +
				"FROM ctb_lancamento_debito A1, ctb_lancamento B1 " +
				"WHERE (A1.cd_lancamento = B1.cd_lancamento) " +
				"   AND (B1.dt_lancamento >= ? AND B1.dt_lancamento <= ?) AND A1.cd_conta_debito = ?");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtInicio));
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtTermino));
			pstmt.setInt(3, cdContaPlanoContas);
			pstmt.setTimestamp(4, Util.convCalendarToTimestamp(dtInicio));
			pstmt.setTimestamp(5, Util.convCalendarToTimestamp(dtTermino));
			pstmt.setInt(6, cdContaPlanoContas);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			if (rsm != null && rsm.next()) {
				register.put("VL_TOTAL_DEBITOS", rsm.getFloat("VL_TOTAL_DEBITOS"));
				register.put("VL_TOTAL_CREDITOS", rsm.getFloat("VL_TOTAL_CREDITOS"));
			}
			else {
				register.put("VL_TOTAL_DEBITOS", 0);
				register.put("VL_TOTAL_CREDITOS", 0);
			}
			ArrayList<HashMap<String, Object>> retorno = new ArrayList<HashMap<String, Object>>();
			retorno.add(register);
			return retorno;
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
}
