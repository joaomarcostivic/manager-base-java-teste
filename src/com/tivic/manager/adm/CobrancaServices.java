package com.tivic.manager.adm;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.mail.SMTPClient;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.util.Util;


public class CobrancaServices {

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;
	
	public static Result save(Cobranca cobranca){
		return save(cobranca, null);
	}

	public static Result save(Cobranca cobranca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cobranca==null)
				return new Result(-1, "Erro ao salvar. Cobranca é nulo");

			int retorno;
			if(cobranca.getCdCobranca()==0){
				retorno = CobrancaDAO.insert(cobranca, connect);
				cobranca.setCdCobranca(retorno);
			}
			else {
				retorno = CobrancaDAO.update(cobranca, connect);
			}

			//Garante que apenas um registro no sistema será o padrão
			if(cobranca.getLgPadrao()==1){
				setPadrao(cobranca.getCdCobranca(), connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "COBRANCA", cobranca);
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
	public static Result remove(int cdCobranca){
		return remove(cdCobranca, false, null);
	}
	public static Result remove(int cdCobranca, boolean cascade){
		return remove(cdCobranca, cascade, null);
	}
	public static Result remove(int cdCobranca, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = CobrancaDAO.delete(cdCobranca, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_cobranca");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CobrancaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM adm_cobranca", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	public static ResultSetMap get(int cdCobranca){
		return get(cdCobranca, null);
	}
	
	public static ResultSetMap get(int cdCobranca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT * FROM adm_cobranca WHERE cd_cobranca = " + cdCobranca;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CobrancaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que faz o envio de emails aos devedores de acordo aos registro de cobrança e nível de cobrança
	 * Caso o sistema não esteja configurado para este envio de email, o metodo simplesmente não fará nada
	 * @author Gabriel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Result enviarEmailCupomDebito(){
		if(ParametroServices.getValorOfParametroAsInteger("LG_COBRANCA", 0)==0){
			return new Result(1);
		}
		Connection connect = null;
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
		    
			ResultSetMap rsmEmpresas = EmpresaServices.getAll();
			System.out.println("iniciando cobrança...");
			PreparedStatement pstmtContasReceber = connect.prepareStatement("SELECT * FROM adm_conta_receber " +
																"				WHERE cd_empresa = ? " +
																"				  AND cd_pessoa  = ?" +
																"				  AND st_conta   = " + ContaReceberServices.ST_EM_ABERTO);
			ResultSetMap rsmContasReceber = new ResultSetMap();
			
			PreparedStatement pstmtNivelCobranca = connect.prepareStatement("SELECT * FROM adm_nivel_cobranca A " +
																"             JOIN adm_cobranca B ON (A.cd_cobranca = B.cd_cobranca)  " +
																"				WHERE A.cd_cobranca = ? " +
																"				  AND A.qt_dias_apos_vencimento <= ? " +
																" 				  AND A.st_nivel_cobranca = " + NivelCobrancaServices.ST_ATIVO +
																" 				  AND B.st_cobranca = " + ST_ATIVO +
																" 				  AND B.lg_envia_cartas_cobranca = 1 " + 
																"			 ORDER BY A.qt_dias_apos_vencimento DESC" +
																"			 LIMIT 1");
			
			
			PreparedStatement pstmtContaReceberArquivo = connect.prepareStatement("SELECT dt_criacao FROM adm_conta_receber_arquivo A " +
																"             		JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo)  " +
																"			 	   WHERE A.cd_conta_receber = ? " + 
																"			   		 AND B.st_arquivo = " + ArquivoServices.ST_ENVIADO +
																"					 AND A.cd_nivel_cobranca = ? " +
																"					 AND A.cd_cobranca = ? " +
																"			 	  ORDER BY B.dt_criacao DESC " +
																"			 	  LIMIT 1");
			
			PreparedStatement pstmtContaReceberArquivo2 = connect.prepareStatement("SELECT dt_criacao FROM adm_conta_receber_arquivo A " +
																"             		JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo)  " +
																"			 	   WHERE A.cd_conta_receber = ? " + 
																"			   		 AND B.st_arquivo = " + ArquivoServices.ST_ENVIADO +
																"					 AND A.cd_nivel_cobranca <> ? " +
																"					 AND A.cd_cobranca = ? " +
																"			 	  ORDER BY B.dt_criacao DESC " +
																"			 	  LIMIT 1");


			
			ResultSetMap rsmNivelCobranca = new ResultSetMap();
			
			//Busca as empresas do sistema
			while(rsmEmpresas.next()){
				Empresa empresa = EmpresaDAO.get(rsmEmpresas.getInt("cd_empresa"), connect);
				int cdEmpresa = rsmEmpresas.getInt("cd_empresa");
				
				//Busca todos os clientes da empresa
				ResultSetMap rsmClientes = ClienteServices.getAllByEmpresa(cdEmpresa, connect);
				while(rsmClientes.next()){
					Cliente cliente = ClienteDAO.get(cdEmpresa, rsmClientes.getInt("cd_pessoa"), connect);
					Pessoa pessoa = PessoaDAO.get(rsmClientes.getInt("cd_pessoa"), connect);
					PessoaFisica clienteFisica = PessoaFisicaDAO.get(rsmClientes.getInt("cd_pessoa"), connect);
					if(cliente != null && cliente.getCdPessoaCobranca() > 0){
						pessoa = PessoaDAO.get(cliente.getCdPessoaCobranca(), connect);
						clienteFisica = PessoaFisicaDAO.get(cliente.getCdPessoaCobranca(), connect);
					}
					pstmtContasReceber.setInt(1, cliente.getCdEmpresa());
					pstmtContasReceber.setInt(2, cliente.getCdPessoa());
					//Busca todas as contas vencidas de cada cliente
					rsmContasReceber = new ResultSetMap(pstmtContasReceber.executeQuery());
					while(rsmContasReceber.next()){
						GregorianCalendar dtVencimento = rsmContasReceber.getGregorianCalendar("dt_vencimento");
						//Calcula quantos dias de vencimento essa conta tem
						int qtDiasAposVencimento = Util.getQuantidadeDiasUteis(dtVencimento, Util.getDataAtual(), connect);
						//Busca qual o nivel de cobrança que essa conta está para esse cliente levando em consideração os dias de vencimento
						pstmtNivelCobranca.setInt(1, cliente.getCdCobranca());
						pstmtNivelCobranca.setInt(2, qtDiasAposVencimento);
						ResultSetMap rsmNivelCobrancaCliente = new ResultSetMap(pstmtNivelCobranca.executeQuery());
						
						//Busca qual o nivel de cobrança que essa conta está para a classificacao desse cliente levando em consideração os dias de vencimento
						Classificacao classificacao = ClassificacaoDAO.get(cliente.getCdClassificacaoCliente(), connect);
						if(classificacao != null && classificacao.getLgAtivo()==1)
							pstmtNivelCobranca.setInt(1, classificacao.getCdCobranca());
						ResultSetMap rsmNivelCobrancaClassificacao = new ResultSetMap(pstmtNivelCobranca.executeQuery());
						rsmNivelCobranca = new ResultSetMap();
						//Caso ache um nivel que se encaixe para o nivel do cliente, esse será o prioritario
						if(rsmNivelCobrancaCliente.next()){
							rsmNivelCobrancaCliente.beforeFirst();
							rsmNivelCobranca = rsmNivelCobrancaCliente;
						}
						//Caso não ache, leva em consideração o da classificação do mesmo
						else if(rsmNivelCobrancaClassificacao.next()){
							rsmNivelCobrancaClassificacao.beforeFirst();
							rsmNivelCobranca = rsmNivelCobrancaClassificacao;
						}
						rsmNivelCobranca.beforeFirst();
						if(rsmNivelCobranca.next()){
							//Usado para descobrir se o nível de cobranca exigirá o envio de um proximo email, a partir da analise do campo qt_dias_entre_aviso_cobranca
							pstmtContaReceberArquivo.setInt(1, rsmContasReceber.getInt("cd_conta_receber"));
							pstmtContaReceberArquivo.setInt(2, rsmNivelCobranca.getInt("cd_nivel_cobranca"));
							pstmtContaReceberArquivo.setInt(3, rsmNivelCobranca.getInt("cd_cobranca"));
							ResultSetMap rsmContaReceberArquivo = new ResultSetMap(pstmtContaReceberArquivo.executeQuery());
							if(rsmContaReceberArquivo.next()){
								GregorianCalendar dtProximoEnvio = rsmContaReceberArquivo.getGregorianCalendar("dt_criacao");
								dtProximoEnvio.add(Calendar.DAY_OF_MONTH, rsmNivelCobranca.getInt("qt_dias_entre_aviso_cobranca"));
								if(!Util.getDataAtual().before(dtProximoEnvio)){
									//Cria um arquivo de email que não estará enviado, mais para baixo do código, as informações serão atualizadas
									Arquivo emailArquivo = new Arquivo(0, null, Util.getDataAtual(), null, null,
																		ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ARQUIVO_EMAIL", 0),
																		Util.getDataAtual(), 0/*cdUsuario*/, ArquivoServices.ST_NAO_ENVIADO, null, null, 0, null, 0, null, null);
									int code = ArquivoDAO.insert(emailArquivo);
									if(code < 0){
										System.out.println("Erro ao crar arquivo de email de cobrança");
									}
									
									emailArquivo.setCdArquivo(code);
									//Relaciona o email criado com a conta a receber
									if(ContaReceberArquivoDAO.insert(new ContaReceberArquivo(rsmContasReceber.getInt("cd_conta_receber"), emailArquivo.getCdArquivo(), rsmNivelCobranca.getInt("cd_nivel_cobranca"), rsmNivelCobranca.getInt("cd_cobranca"))) < 0){
										System.out.println("Erro ao crar arquivo de email de cobrança");
									}
								}	
							}
							else{
								pstmtContaReceberArquivo2.setInt(1, rsmContasReceber.getInt("cd_conta_receber"));
								pstmtContaReceberArquivo2.setInt(2, rsmNivelCobranca.getInt("cd_nivel_cobranca"));
								pstmtContaReceberArquivo2.setInt(3, rsmNivelCobranca.getInt("cd_cobranca"));
								rsmContaReceberArquivo = new ResultSetMap(pstmtContaReceberArquivo2.executeQuery());
								if(rsmContaReceberArquivo.next()){
									//Cria um arquivo de email que não estará enviado, mais para baixo do código, as informações serão atualizadas
									Arquivo emailArquivo = new Arquivo(0, null, Util.getDataAtual(), null, null,
																		ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ARQUIVO_EMAIL", 0),
																		Util.getDataAtual(),0/*cdUsuario*/,	ArquivoServices.ST_NAO_ENVIADO, null, null, 0, null, 0, null, null);
									int code = ArquivoDAO.insert(emailArquivo);
									if(code < 0){
										System.out.println("Erro ao crar arquivo de email de cobrança");
									}
									
									emailArquivo.setCdArquivo(code);
									//Relaciona o email criado com a conta a receber
									if(ContaReceberArquivoDAO.insert(new ContaReceberArquivo(rsmContasReceber.getInt("cd_conta_receber"), emailArquivo.getCdArquivo(), rsmNivelCobranca.getInt("cd_nivel_cobranca"), rsmNivelCobranca.getInt("cd_cobranca"))) < 0){
										System.out.println("Erro ao crar arquivo de email de cobrança");
									}
								}
							}
							
							boolean lgCreditoSuspenso  = rsmNivelCobranca.getInt("lg_suspender_credito") == 1; 
							boolean lgCreditoCancelado = rsmNivelCobranca.getInt("lg_cancelar_credito") == 1;
							Double prMultaAcrescentada  = rsmNivelCobranca.getDouble("pr_encargos_mora");
							Double prJurosAcrescentados = rsmNivelCobranca.getDouble("pr_taxa_juros");
							
							if(lgCreditoCancelado)
								cliente.setTpCredito(ClienteServices.TP_CREDITO_CANCELADO);
							else if(lgCreditoSuspenso)
								cliente.setTpCredito(ClienteServices.TP_CREDITO_SUSPENSO);
							
							if(ClienteDAO.update(cliente) < 0){
								System.out.println("Atualização do cliente " + pessoa.getNmPessoa() + " falhou!");
							}
							
							ContaReceber contaReceber = ContaReceberDAO.get(rsmContasReceber.getInt("cd_conta_receber"));
							contaReceber.setPrMulta(prMultaAcrescentada);
							//Caso a conta a receber já tenha um juros (que seria de programa de fatura) o sistema não acrescentará o de cobrança
							if(contaReceber.getPrJuros() <= 0 || ParametroServices.getValorOfParametroAsInteger("LG_SUBTITUIR_JUROS", 0)==1)
								contaReceber.setPrJuros(prJurosAcrescentados);
							else
								prJurosAcrescentados = 0.0d;
							
							if(ContaReceberDAO.update(contaReceber) < 0){
								System.out.println("Atualização da conta do cliente " + pessoa.getNmPessoa() + " falhou!");
							}
							
							
							String nmEmail = cliente.getNmEmail();
							
							if(nmEmail == null || nmEmail.equals("")){
								nmEmail = pessoa.getNmEmail();
								if(nmEmail == null || nmEmail.equals("")){
									System.out.println("O cliente "+pessoa.getNmPessoa()+" não possui um email cadastrado para que se envie o email.");
									break;
								}
							}
							if(!Util.isEmail(nmEmail)){
								System.out.println("O cliente não possui um email válido.");
								break;
							}
							
							NumberFormat formatoPreco = NumberFormat.getCurrencyInstance();  
					        formatoPreco.setMaximumFractionDigits(2);
					        
							
							String assunto = "Cobrança - Conta Nº " + rsmContasReceber.getString("nr_documento") + " - da Empresa: " + empresa.getNmRazaoSocial();
							
							String textoEmail = "<img src=\'cid:logoEmpresa\' width=\"30%\" height=\"30%\" /><br />";
					        textoEmail += "Prezad" + (clienteFisica == null || clienteFisica.getTpSexo() == 1 ? "a" : "o") + " " + (clienteFisica != null && clienteFisica.getNmFormaTratamento() != null && !clienteFisica.getNmFormaTratamento().equals("") ? clienteFisica.getNmFormaTratamento() : "") + " " + pessoa.getNmPessoa() + "<br /><br />";
							textoEmail += "Este é um aviso de cobrança referente a conta:<br /><br />";
							
							textoEmail += "Parcela Nº: " + (rsmContasReceber.getInt("nr_parcela")+ 1) + " <br />";
							textoEmail += "Valor " + formatoPreco.format(rsmContasReceber.getDouble("vl_conta")) + " <br />";
							textoEmail += "Data de Vencimento " + Util.formatDate(rsmContasReceber.getGregorianCalendar("dt_vencimento"), "dd/MM/yyyy") + " <br /><br />"; 
							
							//Caso a empresa queira, poderá configurar para que o cliente receba informações mais detalhadas do pagamento
							if(ParametroServices.getValorOfParametroAsInteger("LG_EMAIL_COBRANCA_DETALHADO", 0)==1){
								if(lgCreditoCancelado)
									textoEmail += "Pelo atraso no pagamento, seu credito foi cancelado." + " <br />";
								else if(lgCreditoSuspenso)
									textoEmail += "Pelo atraso no pagamento, seu credito foi suspenso." + " <br />";
								
								if(prMultaAcrescentada > 0)
									textoEmail += "Uma multa de "+Util.formatNumber(prMultaAcrescentada)+"% será acrescentada ao valor da conta." + " <br />";
								
								if(prJurosAcrescentados > 0)
									textoEmail += "Juros de "+Util.formatNumber(prJurosAcrescentados)+"% ao dia serão acrescentados ao valor da conta." + " <br />";
								
								textoEmail += "Observação: " + rsmNivelCobranca.getString("txt_nota") + " <br />";
								
							}
							
							
							textoEmail += "Estamos a sua inteira disposição para mais esclarecimentos.<br /><br /><br />";
							textoEmail += "Atenciosamente,<br /><br />";
							textoEmail += "<b>" + empresa.getNmRazaoSocial() + "</b><br />";
							textoEmail += "<b>Fone: " + Util.formatTelefone(empresa.getNrTelefone1())+ "</b><br /><br /><br /><br /><br />";
							
							textoEmail += "*enviado automaticamente por DNA Suíte ERP, CRM e SCM<br /><br />";
							textoEmail += "<img src=\'cid:logoDesenvolvedora\' width=\"10%\" height=\"10%\" />";
							
							String texto = textoEmail;//Assinatura da empresa ao desenvolvedor
							String emailDestinatario = nmEmail;
							
							
							//Caso o sistema identifique que o email já foi enviado para o cliente, ele dará prosseguimento a próxima analise, não enviando novamente o email
							//Identifica se ja um arquivo de email que já tenha sido enviado
							ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_conta_receber", "" + contaReceber.getCdContaReceber(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("CAST(dt_criacao AS DATE)", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
							criterios.add(new ItemComparator("st_arquivo", "" + ArquivoServices.ST_ENVIADO, ItemComparator.EQUAL, Types.INTEGER));
							ResultSetMap rsmContaArquivo = ContaReceberArquivoServices.find(criterios);
							
							//Identifica se não há nenhum arquivo email que esteja a espera de ser enviado
							//Esse criterio é utilizado por conta do campo "Dias entre envio de cobrança" pois pode-se ter mais de um envio para cobranca da
							//mesma conta, logo se ela foi criada, deve ser enviada
							criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_conta_receber", "" + contaReceber.getCdContaReceber(), ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("CAST(dt_criacao AS DATE)", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
							criterios.add(new ItemComparator("st_arquivo", "" + ArquivoServices.ST_NAO_ENVIADO, ItemComparator.EQUAL, Types.INTEGER));
							ResultSetMap rsmContaArquivoNaoEnviado = ContaReceberArquivoServices.find(criterios);
							if(rsmContaArquivo.next() && !rsmContaArquivoNaoEnviado.next()){
								continue;
							}
							
							Arquivo emailArquivo;
							//Faz a busca para saber se o arquivo do email já existe, caso exista, ele apenas atualiza as informações
							//Caso contrário, cria um novo arquivo e vincula a conta
							rsmContaArquivoNaoEnviado.beforeFirst();
							if(rsmContaArquivoNaoEnviado.next()){
								emailArquivo = ArquivoDAO.get(rsmContaArquivoNaoEnviado.getInt("cd_arquivo"));
								emailArquivo.setBlbArquivo(texto.getBytes());
								emailArquivo.setNmArquivo("Email de Cobranca. Para " +emailDestinatario);
								emailArquivo.setNmDocumento(assunto+emailDestinatario);
								if(ArquivoDAO.update(emailArquivo) < 0){
									System.out.println("Erro ao atualizar arquivo de email de cobrança para " + emailDestinatario);
								}
							}
							else{
								emailArquivo = new Arquivo(0, "Email de Cobranca. Para " +emailDestinatario, Util.getDataAtual(), assunto+emailDestinatario, texto.getBytes(),
															ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ARQUIVO_EMAIL", 0),
															Util.getDataAtual(),0/*cdUsuario*/, ArquivoServices.ST_NAO_ENVIADO, null, null, 0, null, 0, null, null);
								int code = ArquivoDAO.insert(emailArquivo);
								if(code < 0){
									System.out.println("Erro ao crar arquivo de email de cobrança para " + emailDestinatario);
								}
								emailArquivo.setCdArquivo(code);
								
								if(ContaReceberArquivoDAO.insert(new ContaReceberArquivo(contaReceber.getCdContaReceber(), emailArquivo.getCdArquivo(), rsmNivelCobranca.getInt("cd_nivel_cobranca"), rsmNivelCobranca.getInt("cd_cobranca"))) < 0){
									System.out.println("Erro ao crar arquivo de email de cobrança para " + emailDestinatario);
								}
							}
							System.out.println("REGISTRO DE EMAIL ENVIADO-----------------------------");
							System.out.println("assunto = " + assunto);
							System.out.println("texto   = " + texto);
							System.out.println("emailDestinatario = " + emailDestinatario);
							System.out.println("cliente = " + cliente);
							System.out.println("contaReceber = " + contaReceber);
							System.out.println("------------------------------------------------------");
							System.out.println();
							
							if(ParametroServices.getValorOfParametroAsString("NM_SERVIDOR_SMTP", "").equals(""))
								return new Result(-1, "Nome do Servidor SMTP não configurado nos parametros!");
							if(ParametroServices.getValorOfParametroAsString("NR_PORTA_SMTP", "").equals(""))
								return new Result(-1, "Número da porta do Servidor SMTP não configurado nos parametros!");
							if(ParametroServices.getValorOfParametroAsString("NM_LOGIN_SERVIDOR_SMTP", "").equals(""))
								return new Result(-1, "Login do Servidor SMTP não configurado nos parametros!");
							if(ParametroServices.getValorOfParametroAsString("NM_SENHA_SERVIDOR_SMTP", "").equals(""))
								return new Result(-1, "Senha do Servidor SMTP não configurado nos parametros!");
							if(ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", "").equals(""))
								return new Result(-1, "Remetente SMTP não configurado nos parametros!");
							
							if(ParametroServices.getValorOfParametroAsInteger("LG_DEBUG_SMTP", 0) == 1){
								System.out.println("PORTA = " + ParametroServices.getValorOfParametroAsInteger("NR_PORTA_SMTP", 0));
								System.out.println("AUT   = " + ParametroServices.getValorOfParametroAsInteger("LG_AUTENTICACAO_SMTP", 0));
								System.out.println("SSL   = " + ParametroServices.getValorOfParametroAsInteger("LG_SSL_SMTP", 0));
							}
							System.out.println("iniciar envio...");
							SMTPClient smtpCliente = new SMTPClient(ParametroServices.getValorOfParametroAsString("NM_SERVIDOR_SMTP", ""), 
																ParametroServices.getValorOfParametroAsInteger("NR_PORTA_SMTP", 0), 
																ParametroServices.getValorOfParametroAsString("NM_LOGIN_SERVIDOR_SMTP", ""), 
																ParametroServices.getValorOfParametroAsString("NM_SENHA_SERVIDOR_SMTP", ""), 
																ParametroServices.getValorOfParametroAsInteger("LG_AUTENTICACAO_SMTP", 0)==1, 
																ParametroServices.getValorOfParametroAsInteger("LG_SSL_SMTP", 0)==1, ParametroServices.getValorOfParametroAsString("NM_TRANSPORT_SMTP", "smtp"));
							if(ParametroServices.getValorOfParametroAsInteger("LG_DEBUG_SMTP", 0) == 1){
								smtpCliente.setDebug(true);	
							}
							if(smtpCliente.connect()) {
								System.out.println("conectado...");
								String[] to = {emailDestinatario};
								String caminho = ParametroServices.getValorOfParametro("NM_ARQUIVO_DESENVOLVEDOR", empresa.getCdEmpresa());
								HashMap<String, Object> attachments[];
								if(caminho != null && !caminho.equals(""))	{
									 attachments = new HashMap[2];
								}
								else{
									 attachments = new HashMap[1];
								}
								HashMap<String, Object> register = new HashMap<String, Object>(); 
								register.put("BYTES", empresa.getImgLogomarca());
								register.put("NAME", "empresa.jpg");
								register.put("CID", "logoEmpresa");
								
								attachments[0] = register;
								
								if(caminho != null && !caminho.equals(""))	{
									try	{
										File file = new File(caminho);
										if(file != null){
											byte[] data = Util.getBytesFromFile(file);
											register = new HashMap<String, Object>(); 
											register.put("BYTES", data);
											register.put("NAME", "desenvolvedora.jpg");
											register.put("CID", "logoDesenvolvedora");
											attachments[1] = register;
										}
									}
									catch(Exception e) {
										e.printStackTrace(System.out);
									}
								}
								
								System.out.println("enviando...");
								smtpCliente.send(ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", ""), new String[] {ParametroServices.getValorOfParametroAsString("NM_EMAIL_REMETENTE_SMTP", "")}, to, null, null, 
											assunto, texto, null, null, attachments);
								
								emailArquivo.setStArquivo(ArquivoServices.ST_ENVIADO);
								emailArquivo.setDtCriacao(Util.getDataAtual());
								if(ArquivoDAO.update(emailArquivo) < 0){
									System.out.println("Erro na atualização do arquivo");
								}
								System.out.println("Envio realizado com sucesso");
							}
							else{
								System.out.println("Erro de conexão!");
							}
							
							smtpCliente.disconnect();
							
						}
					}
					
				}
				
			}
			System.out.println("fim da cobranca");
			return new Result(1, "Email enviado com sucesso!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalServices.enviarEmail: " + e);
			return new Result(-1, "Erro ao enviar o email!");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que busca o registro de cobranca padrão do sistema
	 * @since 15/08/2014
	 * @author Gabriel
	 * @return
	 */
	public static Cobranca getPadrao(){
		return getPadrao(null);
	}
	
	public static Cobranca getPadrao(Connection connection){
		boolean isConnectionNull = connection == null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
								
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_padrao", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = CobrancaDAO.find(criterios, connection);
			while(rsm.next()){
				return CobrancaDAO.get(rsm.getInt("cd_cobranca"), connection);
			}
			
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
	 * Metodo que vai garantir que apenas um registro de cobrança do sistema será a padrão
	 * @since 15/08/2014
	 * @author Gabriel
	 * @param cdCobranca
	 */
	public static void setPadrao(int cdCobranca){
		setPadrao(cdCobranca, null);
	}
	
	public static void setPadrao(int cdCobranca, Connection connection){
		boolean isConnectionNull = connection == null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
								
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_padrao", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = CobrancaDAO.find(criterios, connection);
			while(rsm.next()){
				Cobranca cobranca = CobrancaDAO.get(rsm.getInt("cd_cobranca"), connection);
				cobranca.setLgPadrao(0);
				if(CobrancaDAO.update(cobranca, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
				}
			}
			
			Cobranca cobranca = CobrancaDAO.get(cdCobranca, connection);
			cobranca.setLgPadrao(1);
			if(CobrancaDAO.update(cobranca, connection) < 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
			}
			
			
			if(isConnectionNull)
				connection.commit();
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
}
