package com.tivic.manager.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.tivic.manager.acd.Aluno;
import com.tivic.manager.acd.AlunoDAO;
import com.tivic.manager.acd.Circulo;
import com.tivic.manager.acd.CirculoDAO;
import com.tivic.manager.acd.Curso;
import com.tivic.manager.acd.CursoDAO;
import com.tivic.manager.acd.CursoEtapaDAO;
import com.tivic.manager.acd.CursoMatrizServices;
import com.tivic.manager.acd.CursoModulo;
import com.tivic.manager.acd.CursoModuloServices;
import com.tivic.manager.acd.CursoServices;
import com.tivic.manager.acd.Instituicao;
import com.tivic.manager.acd.InstituicaoCirculo;
import com.tivic.manager.acd.InstituicaoCirculoDAO;
import com.tivic.manager.acd.InstituicaoDAO;
import com.tivic.manager.acd.InstituicaoEducacenso;
import com.tivic.manager.acd.InstituicaoEducacensoDAO;
import com.tivic.manager.acd.InstituicaoEducacensoServices;
import com.tivic.manager.acd.InstituicaoHorario;
import com.tivic.manager.acd.InstituicaoHorarioDAO;
import com.tivic.manager.acd.InstituicaoServices;
import com.tivic.manager.acd.Matricula;
import com.tivic.manager.acd.MatriculaDAO;
import com.tivic.manager.acd.MatriculaDisciplina;
import com.tivic.manager.acd.MatriculaDisciplinaServices;
import com.tivic.manager.acd.MatriculaModulo;
import com.tivic.manager.acd.MatriculaModuloServices;
import com.tivic.manager.acd.MatriculaPeriodoLetivo;
import com.tivic.manager.acd.MatriculaPeriodoLetivoServices;
import com.tivic.manager.acd.MatriculaServices;
import com.tivic.manager.acd.MatriculaTipoTransporte;
import com.tivic.manager.acd.MatriculaTipoTransporteDAO;
import com.tivic.manager.acd.OcorrenciaMatricula;
import com.tivic.manager.acd.OcorrenciaMatriculaServices;
import com.tivic.manager.acd.QuadroVagas;
import com.tivic.manager.acd.QuadroVagasCurso;
import com.tivic.manager.acd.QuadroVagasCursoDAO;
import com.tivic.manager.acd.QuadroVagasCursoServices;
import com.tivic.manager.acd.QuadroVagasServices;
import com.tivic.manager.acd.Turma;
import com.tivic.manager.acd.TurmaDAO;
import com.tivic.manager.acd.TurmaHorarioDAO;
import com.tivic.manager.acd.TurmaServices;
import com.tivic.manager.adm.CategoriaEconomica;
import com.tivic.manager.adm.CategoriaEconomicaDAO;
import com.tivic.manager.adm.CategoriaEconomicaServices;
import com.tivic.manager.adm.ContaReceberServices;
import com.tivic.manager.adm.FormaPagamentoEmpresa;
import com.tivic.manager.adm.FormaPagamentoEmpresaDAO;
import com.tivic.manager.adm.FormaPlanoPagamento;
import com.tivic.manager.adm.FormaPlanoPagamentoDAO;
import com.tivic.manager.adm.FormaPlanoPagamentoServices;
import com.tivic.manager.adm.MovimentoConta;
import com.tivic.manager.adm.MovimentoContaDAO;
import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemDAO;
import com.tivic.manager.agd.AgendaItemOcorrencia;
import com.tivic.manager.agd.AgendaItemOcorrenciaServices;
import com.tivic.manager.agd.AgendaItemServices;
import com.tivic.manager.alm.LocalArmazenamento;
import com.tivic.manager.alm.LocalArmazenamentoDAO;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.egov.DAMUtils;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeServices;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoServices;
import com.tivic.manager.grl.IndicadorVariacao;
import com.tivic.manager.grl.IndicadorVariacaoDAO;
import com.tivic.manager.grl.IndicadorVariacaoServices;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaContaBancariaServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorServices;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.mob.AfericaoCatraca;
import com.tivic.manager.mob.AfericaoCatracaDAO;
import com.tivic.manager.mob.AgenteServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDAO;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.AitMovimentoDocumentoDAO;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.AitTransporte;
import com.tivic.manager.mob.AitTransporteDAO;
import com.tivic.manager.mob.AitTransporteServices;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.mob.ArquivoMovimentoDAO;
import com.tivic.manager.mob.ConcessaoServices;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosEtiquetaDAO;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.EventoArquivoServices;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.EventoEquipamentoDAO;
import com.tivic.manager.mob.EventoEquipamentoServices;
import com.tivic.manager.mob.Horario;
import com.tivic.manager.mob.HorarioDAO;
import com.tivic.manager.mob.HorarioServices;
import com.tivic.manager.mob.Linha;
import com.tivic.manager.mob.LinhaDAO;
import com.tivic.manager.mob.LinhaRota;
import com.tivic.manager.mob.LinhaRotaServices;
import com.tivic.manager.mob.TabelaHorarioRota;
import com.tivic.manager.mob.TabelaHorarioRotaServices;
import com.tivic.manager.mob.TalonarioAITServices;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.mob.TipoEventoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.Vistoria;
import com.tivic.manager.mob.VistoriaDAO;
import com.tivic.manager.mob.VistoriaPlanoItemServices;
import com.tivic.manager.mob.VistoriaServices;
import com.tivic.manager.mob.aitmovimentodocumento.DocumentoProcesso;
import com.tivic.manager.mob.correios.DigitoVerificador;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoBuilder;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;
import com.tivic.manager.ord.OrdemServicoTecnico;
import com.tivic.manager.ord.OrdemServicoTecnicoServices;
import com.tivic.manager.prc.Orgao;
import com.tivic.manager.prc.OrgaoDAO;
import com.tivic.manager.prc.Processo;
import com.tivic.manager.prc.ProcessoAndamento;
import com.tivic.manager.prc.ProcessoAndamentoServices;
import com.tivic.manager.prc.ProcessoDAO;
import com.tivic.manager.prc.ProcessoSentenca;
import com.tivic.manager.prc.ProcessoSentencaDAO;
import com.tivic.manager.prc.ProcessoSentencaServices;
import com.tivic.manager.prc.ProcessoServices;
import com.tivic.manager.prc.ServicoRecorte;
import com.tivic.manager.prc.ServicoRecorteServices;
import com.tivic.manager.prc.TipoAndamentoDAO;
import com.tivic.manager.print.Converter;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoOcorrenciaDAO;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.DocumentoTramitacao;
import com.tivic.manager.ptc.DocumentoTramitacaoDAO;
import com.tivic.manager.ptc.DocumentoTramitacaoServices;
import com.tivic.manager.ptc.protocolosv3.documento.ocorrencia.DocumentoOcorrenciaBuilder;
import com.tivic.manager.ptc.protocolosv3.publicacao.ProtocoloPublicacaoPendenteDto;
import com.tivic.manager.seg.AcaoServices;
import com.tivic.manager.seg.AgrupamentoAcao;
import com.tivic.manager.seg.AgrupamentoAcaoDAO;
import com.tivic.manager.seg.UsuarioModulo;
import com.tivic.manager.seg.UsuarioModuloDAO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.cdi.InjectApplicationBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.cdi.InicializationBeans;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import io.jsonwebtoken.io.IOException;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.HashServices;
import sol.util.Result;

public class FixServices {
	
	public FixServices() throws Exception {
		InicializationBeans.init(new InjectApplicationBuilder());
	}
	
	/**
	 * Lista todos os mï¿½todos de fix na classe.
	 * Utilizado no dashboard de Manutenï¿½ï¿½o para execuï¿½ï¿½o.
	 * @return
	 */
	public static Result getAllFixes() {
		try {
			Method[] methods =  FixServices.class.getDeclaredMethods();
			
			ArrayList<HashMap<String, Object>> fixes = new ArrayList<>();
			for (Method method : methods) {
				method.setAccessible(true);
				
				boolean add = true;
				
				HashMap<String, Object> fixMap = new HashMap<String, Object>(); 
				
				fixMap.put("NAME", method.getName());
				fixMap.put("RETURN", method.getReturnType().getName());
				
				if(method.getParameterTypes().length > 0) {
					
					Class<?>[] argsTypes = method.getParameterTypes();
					ArrayList<HashMap<String, Object>> argsList = new ArrayList<>();
					
					for (Class<?> argType : argsTypes) {
						
						//Nao adiciona o mï¿½todo caso tenha argumentos do tipo Connection
						if(argType == Connection.class) {
							add = false;
							break;
						}
						
						HashMap<String, Object> arg = new HashMap<String, Object>();
						arg.put("NAME", "arg");
						arg.put("TYPE", argType.getName());
						
						argsList.add(arg);
					}
					
					fixMap.put("ARGS", argsList);
				}
				
				if(add)
					fixes.add(fixMap);
			}
			
			return new Result(1, "Lista de metodos fix retornada", "FIXES", fixes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-1, "Erro ao retornar lista de metodos fix.");
		}
	}
	
	public static Result fixTeste() {
		System.out.println("Este ï¿½ um fix de teste.");
		return new Result(1, "Fix de teste executado com sucesso.");
	}
	
	/**
	 * CORRIGE O TIPO DE PROCESSO (FISICO/ELETRONICO) DOS PROCESSOS QUE SOFRERAM ALTERACAO 
	 * EM LOTE E PERDERAM SEU VALOR ORIGINAL. ï¿½ BASEADO NO LOG DE INCLUSï¿½O DO PROCESSO, 
	 * VERIFICANDO SE O CAMPO TP_AUTOS ESTï¿½ DIFERENTE DO VALOR DE INCLUSï¿½O;
	 * OBS: Nï¿½o ï¿½ perfeito, pois um registro pode realmente ter este campo alterado intencionalmente.
	 * @author Sapucaia
	 * @since 10/09/2015
	 * @return
	 */
	public static Result jurisFix001FisicoEletronico(int lgCorrigir) {
		return jurisFix001FisicoEletronico(lgCorrigir, null);
	}
	public static Result jurisFix001FisicoEletronico(int lgCorrigir, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			//1. BUSCAR REGISTROS INCLUIDOS COM TP_AUTOS = 1
			PreparedStatement pstmt = connect.prepareStatement("select * from log_execucao_acao A where A.txt_execucao like 'Inclusï¿½o de novo Processo:%TP_AUTOS: 1%'");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			//2. ISOLAR OS REGISTROS QUE FORAM ALTERADOS (QUE TENHAM ATUALMENTE TP_AUTOS=0), BUSCANDO ATRAVï¿½S DO Nï¿½ PROCESSO
			System.out.println(rsm.size()+" processo(s) encontrados.");
			ResultSetMap rsmAlterados = new ResultSetMap();
			while(rsm.next()) {
				String txtExecucao = rsm.getString("txt_execucao");
				
				if(txtExecucao.indexOf("NR_PROCESSO: ")>-1){
					String nrProcesso = txtExecucao.substring(txtExecucao.indexOf("NR_PROCESSO: ")+13);
					nrProcesso = nrProcesso.substring(0, nrProcesso.indexOf("\n")).trim();
					
					System.out.print("Verificando Processo: "+nrProcesso);
					
					pstmt = connect.prepareStatement("select * from prc_processo where nr_processo = ?");
					pstmt.setString(1, nrProcesso);
					ResultSetMap rsm2 = new ResultSetMap(pstmt.executeQuery());
					
					if(rsm2.next()) {
						System.out.print(" ("+Util.formatDate(rsm2.getGregorianCalendar("DT_CADASTRO"), "dd/MM/yyyy")+")");
						if(rsm2.getInt("TP_AUTOS")==0) {
							rsmAlterados.addRegister(rsm2.getRegister());
							System.out.println("... necessita correcao.");
						}
						else {
							System.out.println("... correto.");
						}
					}
					else 
						System.out.println("... nï¿½o encontrado.");
				}
			}
			
			int retorno = 1;
			
			//3. ALTERAR TP_AUTOS PARA 1
			System.out.println(rsmAlterados.size()+" processo(s) precisam ser corrigidos. ");
			if(lgCorrigir==1){
				System.out.println("\nIniciando correcao...");
				while(rsmAlterados.next()) {
					Processo processo = ProcessoServices.getProcessoByNrProcesso(rsmAlterados.getString("NR_PROCESSO"), connect);
					
					System.out.print("Corrigindo processo "+processo.getNrProcesso());
					
					System.out.print("\n\n"+processo);
					processo.setTpAutos(1);
					Result r = ProcessoServices.save(processo, connect);
					System.out.print("\n\n"+processo);
					
					if(r.getCode()>0) {
						System.out.println("... corrigido.");
					}
					else {
						System.out.println("... erro ao corrigir.");
						System.out.println(r.getMessage());
						retorno = 0;
						break;
					}
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * Corrige valores/codificaï¿½ï¿½o do campo blb_ata em prc_processo_andamento
	 * 
	 * @return
	 */
	public static Result jurisFix002BlbAta() {
		return jurisFix002BlbAta(null);
	}
	public static Result jurisFix002BlbAta(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT cd_processo, cd_andamento, blb_ata "
					+ " FROM prc_processo_andamento"
					+ " WHERE blb_ata IS NOT NULL"
				);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(!rs.next()) {
				return new Result(-1, "Nenhum andamento encontrado.");
			}
			
			System.out.println("INICIANDO...\n");
			do {
				System.out.println("\n"+rs.getRow()+". ");
				
				byte[] blbAta = rs.getBytes("blb_ata");
				int cdProcesso = rs.getInt("cd_processo");
				int cdAndamento = rs.getInt("cd_andamento");
				
				System.out.println("blbAta: "+blbAta.length);
				
				String blbAta2 = new String(blbAta);
				
				System.out.println("cdProcesso: "+cdProcesso);
				System.out.println("cdAndamento: "+cdAndamento);
				System.out.println("blbAta2: "+blbAta2);
				
				pstmt = connect.prepareStatement("UPDATE prc_processo_andamento "
												+ " SET blb_ata2 = "+blbAta2
												+ " WHERE cd_processo="+cdProcesso
												+ " AND cd_andamento="+cdAndamento);
				
				retorno = pstmt.executeUpdate(); 
				
				if(retorno<=0) {
					Conexao.rollback(connect);
					return new Result(retorno, "Erro ao executar update no registro [processo: "+cdProcesso+", Andamento: "+cdAndamento+"]");
				}
			} while(rs.next());
						
			if (isConnectionNull)
				connect.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result jurisFix003PtcVerificarImportacao(String nmArquivo) {
		return jurisFix003VerificarImportacao(nmArquivo, "ptc", null);
	}
	public static Result jurisFix003PrcVerificarImportacao(String nmArquivo) {
		return jurisFix003VerificarImportacao(nmArquivo, "prc", null);
	}
	
	/**
	 * Verifica quais documentos/processos de um .csv nï¿½o foram importados.
	 * 
	 * @param nmArquivo Caminho do arquivo.csv
	 * @param nmModulo Mï¿½dulo Protocolo ou Jurï¿½dico (ptc/prc)
	 * @param connect
	 * @return
	 */
	public static Result jurisFix003VerificarImportacao(String nmArquivo, String nmModulo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			RandomAccessFile raf = new RandomAccessFile(nmArquivo, "rw");
			System.out.println(nmArquivo+" carregado...");
	  		String nrDocumento = "";
	  		
	  		PreparedStatement pstmt = null;
	  		ResultSet rs = null;
	  		
	  		File log = new File(nmArquivo+".txt");
	  		FileWriter fw = new FileWriter(log);
	  		BufferedWriter bw = new BufferedWriter(fw);
	  		
	  		boolean blank = true;
	  		
	  		System.out.println("Iniciando.....");
	  		while((nrDocumento = raf.readLine()) != null) {
	  			
	  			nrDocumento = nrDocumento.trim();
	  			
	  			pstmt = connect.prepareStatement(nmModulo.equals("ptc") ? "SELECT cd_documento FROM ptc_documento WHERE nr_documento LIKE '%"+nrDocumento+"%'" : 
		  																  "SELECT cd_processo FROM prc_processo WHERE nr_processo LIKE '%"+nrDocumento+"%'");
	  			rs = pstmt.executeQuery();
	  			
	  			if(!rs.next()) {
	  				bw.write(nrDocumento+" nï¿½o encontrado.");
	  				bw.newLine();
	  				
	  				blank = false;
	  			}
	  		}
	  		
	  		if(blank) {
	  			bw.write("Todos os documentos estï¿½o importados.");
	  		}
	  		
	  		bw.close();
	  		fw.close();
	  		
	  		raf.close();
	  		
	  		System.out.println("Finalizado.....");
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Lanï¿½ar tarefas para validaï¿½ï¿½o da migraï¿½ï¿½o de processos da SSAA
	 * 
	 * @return
	 * @since 21/12/2015
	 * @author Maurï¿½cio
	 * 
	 * @category importaï¿½ï¿½o SSAA
	 */
	@Deprecated
	public static Result jurisFix004LancarTarefa() {
		return jurisFix004LancarTarefa(null);
	}
	@Deprecated
	public static Result jurisFix004LancarTarefa(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//Buscar processos migrados
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_processo FROM prc_processo"
															+ " WHERE cd_usuario_cadastro=10414"
															+ " AND dt_repasse='04.01.2016'"
															+ " AND nr_processo NOT IN"
															+ " ('40469620124013702','90001689420108100048',"
															+ "'90003336220108100139','90004325520118100120',"
															+ "'1632009','90001628020118100136',"
															+ "'90001704820108100118','90001754520128100039',"
															+ "'1682010','3332010',"
															+ "'4322011','1632009',"
															+ "'1622011','1702010',"
															+ "'1752012')");
			//pstmt.setTimestamp(1, Util.convCalendarToTimestamp(Util.convStringToCalendar("04/01/2016")));
			
			ResultSet rs = pstmt.executeQuery();
			int cdProcesso = 0;
			Result result = null;
			GregorianCalendar dtTarefa = Util.convStringToCalendar("21/12/2015");
			GregorianCalendar dtCompromisso = Util.convStringToCalendar("11/01/2016");
			while(rs.next()) {
				cdProcesso = rs.getInt("cd_processo");
				
				/*
				 * TAREFA (AUDITAR - 151)
				 */
//				AgendaItem tarefa = new AgendaItem(0, dtTarefa, dtTarefa, new GregorianCalendar(), 
//						"Auditar processo cadastrado atravï¿½s da migraï¿½ï¿½o BMG 2015", AgendaItemServices.ST_AGENDA_A_CUMPRIR, 
//						151/*cdTipoPrazo-AUDITAR*/, 27601/*cdPessoa-UILTON*/, cdProcesso, 0, null, null, null, 
//						10414/*cdUsuario-TIVIC*/, null, null, 0, 0, null, 0, 0, 3, null, 0, 0, null, 0, 0, 0, 0);
//				AgendaItem tarefa = new AgendaItem();
//				result = AgendaItemServices.save(tarefa, connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						connect.rollback();
					return result;
				}
				
				/*
				 * COMPROMISSO (JUNTADA DE PETIï¿½ï¿½O REQUERENDO HABILITAï¿½ï¿½O - 102)
				 */
//				AgendaItem compromisso = new AgendaItem(0, dtCompromisso, dtCompromisso, new GregorianCalendar(), 
//						"Processo cadastrado atravï¿½s da migraï¿½ï¿½o BMG 2015", AgendaItemServices.ST_AGENDA_A_CUMPRIR, 
//						102, 27601/*cdPessoa-UILTON*/, cdProcesso, 0, null, null, null, 
//						10414/*cdUsuario-TIVIC*/, null, null, 0, 0, null, 0, 0, 3, null, 0, 0, null, 0, 0, 0);
//				AgendaItem compromisso = new AgendaItem();
//				result = AgendaItemServices.save(compromisso, connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						connect.rollback();
					return result;
				}
			}
						
			if (isConnectionNull)
				connect.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Veriifica se os processo foram importado do csv para a base de dados.
	 * 
	 * @return
	 * @author Maurï¿½cio
	 * 
	 * @category importaï¿½ï¿½o SSAA (2016)
	 */
	public static Result jurisFix005VerificarProcessos() {
		return jurisFix005VerificarProcessos(null);
	}
	
	public static Result jurisFix005VerificarProcessos(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
  		
	  		PreparedStatement pstmt = null;
	  		ResultSet rs = null;
	  		ResultSet rsAux = null;
	  		String nrProcessoOriginal = null;
	  		
	  		File logFound = new File("/tivic/import/found.txt");
	  		FileWriter fwFound = new FileWriter(logFound);
	  		BufferedWriter bwFound = new BufferedWriter(fwFound);
	  		
	  		File logNotFound = new File("/tivic/import/notFound.txt");
	  		FileWriter fwNotFound = new FileWriter(logNotFound);
	  		BufferedWriter bwNotFound = new BufferedWriter(fwNotFound);
	  		
	  		
	  		System.out.println("Iniciando.....");
	  		
	  		pstmt = connect.prepareStatement("SELECT * FROM temp_uilton");
	  		rs = pstmt.executeQuery();
	  		
	  		while(rs.next()) {
	  			nrProcessoOriginal = rs.getString("nr_processo");
	  			
	  			String nrProcessoPartes = null;
	  			if(nrProcessoOriginal.length()>=20) {					
					nrProcessoPartes = nrProcessoOriginal.substring(0, 7) +"%"+
										nrProcessoOriginal.substring(7, 9) +"%"+
										nrProcessoOriginal.substring(9, 13) +"%"+
										nrProcessoOriginal.substring(13, 14) +"%"+
										nrProcessoOriginal.substring(14, 16) +"%"+
										nrProcessoOriginal.substring(16, 20);
	  			}
	  			
	  			pstmt = connect.prepareStatement("SELECT A.nr_processo, B.nm_grupo_processo"
	  											+ " FROM prc_processo A"
	  											+ " LEFT OUTER JOIN prc_grupo_processo B ON (A.cd_grupo_processo=B.cd_grupo_processo)"
	  											+ " WHERE A.nr_processo LIKE ?"
	  											+ (nrProcessoPartes!=null ? " OR nr_processo LIKE '"+nrProcessoPartes+"'" : ""));
	  			pstmt.setString(1, nrProcessoOriginal);
	  			rsAux = pstmt.executeQuery();
	  			
	  			if(rsAux.next()) {
	  				bwFound.write(rsAux.getString("nr_processo") + "\t\t" +rsAux.getString("nm_grupo_processo") + " encontrado.");
	  				bwFound.newLine();
	  			}
	  			else {
	  				bwNotFound.write(nrProcessoOriginal + " nï¿½o encontrado.");
	  				bwNotFound.newLine();
	  			}
	  		}
	  		
	  		
	  		bwFound.close();
	  		fwFound.close();
	  		
	  		bwNotFound.close();
	  		fwNotFound.close();
	  		
	  		System.out.println("Finalizado.....");
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Insere solicitantes em documentos sincronizados sem documentoPessoa
	 * 
	 * @return
	 * @since fevereiro/2016
	 * @author Maurï¿½cio
	 */
	public static Result jurisFix006CadastrarSolicitante() {
		return jurisFix006CadastrarSolicitante(null);
	}
	
	public static Result jurisFix006CadastrarSolicitante(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
  		
	  		PreparedStatement pstmt = null;
	  		ResultSet rs = null;
	  		
	  		System.out.println("Iniciando.....");
	  		
	  		ResultSetMap rsm = DocumentoServices.getListDocumentosExternos(connect);
	  		
	  		int count = 0;
	  		while(rsm.next()) {
	  			String nrControle = rsm.getString("nr_documento_externo");
	  			int cdDocumento = rsm.getInt("cd_documento");
	  			String[] solicitante = EelUtils.getRequerenteByControle(nrControle);
	  			int cdSolicitante = 0;
	  			
	  			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_pessoa"
	  											+ " WHERE cd_documento="+cdDocumento);
	  			rs = pstmt.executeQuery();
	  			if(rs.next()) {
	  				continue;
	  			}
	  			else {
	  				pstmt = connect.prepareStatement("SELECT cd_pessoa FROM grl_pessoa_externa WHERE cd_pessoa_externa = '"+solicitante[0]+"'");
					
					rs = pstmt.executeQuery();
					if(rs.next()) {
						cdSolicitante = rs.getInt("cd_pessoa");
					}
					else {
						Pessoa p = new Pessoa(0, 0, 1, solicitante[1], null, null, null, null, null, new GregorianCalendar(), 
								PessoaServices.TP_FISICA, null, PessoaServices.ST_ATIVO, 
								null, null, null, 0, null, 0, 0, null);
						
						cdSolicitante = PessoaServices.save(p, null, 0, 0, connect).getCode();
						
						if(cdSolicitante>0) {
							pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_externa(cd_pessoa, cd_pessoa_externa)"
									+ " VALUES (?,?)");
							pstmt.setInt(1, cdSolicitante);
							pstmt.setString(2, solicitante[1]);
							
							pstmt.executeUpdate();
						}
						
					}
	  			}
	  			
	  			pstmt = connect.prepareStatement("INSERT INTO ptc_documento_pessoa (cd_documento,"+
                        "cd_pessoa,nm_qualificacao) VALUES (?, ?, ?)");
				if(cdDocumento==0)
					pstmt.setNull(1, Types.INTEGER);
				else
					pstmt.setInt(1,cdDocumento);
				if(cdSolicitante==0)
					pstmt.setNull(2, Types.INTEGER);
				else
					pstmt.setInt(2,cdSolicitante);
				pstmt.setString(3, "Solicitante");
				int r = pstmt.executeUpdate();
				
				if(r<=0) {
					return new Result(r, "Erro ao inserir solicitante");
				}
				
				count++;
				
				if(count%200==0) {
					if(isConnectionNull)
						connect.commit();
					
					Thread.sleep(1*60*1000);
					System.out.println(count);
				}
	  		}
	  		
	  		System.out.println("Finalizado.....");
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Insere o nï¿½mero de protocolo da E&L nos documentos sincronizados.
	 * 
	 * @return
	 * @author Maurï¿½cio
	 * @since fevereiro/2016
	 * @category sincronizaï¿½ï¿½o E&L
	 */
	public static Result jurisFix007NrProtocolo() {
		return jurisFix007NrProtocolo(null);
	}
	
	public static Result jurisFix007NrProtocolo(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
  		
	  		PreparedStatement pstmt = null;
	  		ResultSet rs = null;
	  		
	  		System.out.println("Iniciando.....");
	  		
	  		pstmt = connect.prepareStatement("SELECT cd_documento, nr_documento_externo "
												+ " FROM ptc_documento "
												+ " WHERE nr_documento_externo IS NOT NULL"
												+ " AND dt_protocolo>='01.01.2016'");
	  		
	  		ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
	  		
	  		Connection connExt = EelUtils.conectarEelSqlServer();
	  		if(connExt==null) {
	  			return new Result(-2, "Erro ao conectar.");
	  		}
	  		
	  		int count = 0;
	  		while(rsm.next()) {
	  			pstmt = connExt.prepareStatement("use CONT_VIT_CONQUISTA_PREFEITURA; "
						+ " SELECT  A.controle AS cd_documento,"
						+ "		   	A.ano AS nr_ano,"
						+ "		   	A.data_registro AS dt_protocolo,"
						+ "			A.codigo_docproc AS nr_documento"
						+ "	FROM pr_protocolo A"
						+ " WHERE A.codigo_emp='001' AND A.codigo_fil='001'"
						+ " AND A.controle = "+rsm.getString("nr_documento_externo")
						+ " AND A.data_registro >= '2016-01-01'");
	  			
	  			rs = pstmt.executeQuery();
	  			if(rs.next()) {
	  				System.out.println(rs.getString("nr_documento")+"/"+rs.getString("nr_ano"));
	  				
	  				pstmt = connect.prepareStatement("UPDATE ptc_documento SET nr_protocolo_externo = '"+rs.getString("nr_documento")+"/"+rs.getString("nr_ano")+"'"+
	  												" WHERE nr_documento_externo='"+rs.getString("cd_documento")+"'");
	  				
	  				if(pstmt.executeUpdate()<0) {
	  					return new Result(-3, "Erro ao atualizar.");
	  				}
	  				
	  			}
	  			count++;
	  			
	  		}
	  		
	  		System.out.println("\n"+count);
	  		System.out.println("\nFinalizado.....");
	  		
	  		connect.commit();
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Criaï¿½ï¿½o da funï¿½ï¿½o sql get_clientes no banco postgres
	 * @param connect
	 * @return
	 */
	public static Result jurisFix008getClientes() {
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			PreparedStatement pstmt = null;
			
			System.out.println("Iniciando.....");
			
			pstmt = connect.prepareStatement(" CREATE OR REPLACE FUNCTION get_clientes(codigoprocesso integer) "+
											"	  RETURNS character varying AS									"+
											"	$BODY$															"+
											"	  DECLARE														"+
											"	    nm_cliente character varying;								"+
											"	    registros RECORD;											"+
											"	  BEGIN															"+
											"	    FOR registros in SELECT nm_pessoa FROM prc_parte_cliente A, grl_pessoa B "+
											"	      WHERE (A.cd_pessoa = B.cd_pessoa) AND (A.cd_processo = codigoProcesso)	"+
											"	    LOOP																		"+
											"	      IF (nm_cliente IS NULL) THEN												"+
											"	        nm_cliente := registros.nm_pessoa;										"+
											"	      ELSE																		"+
											"	        IF(LENGTH(NM_CLIENTE)+LENGTH(registros.nm_pessoa)>200) THEN				"+
											"	          IF(LENGTH(nm_cliente)<198)THEN										"+
											"	            nm_cliente := nm_cliente || '...';									"+
											"	          ELSE																	"+
											"	            nm_cliente := nm_cliente ||'', '' || registros.nm_pessoa;			"+
											"	          END IF;   															"+
											"	        END IF;																	"+
											"	      END IF;  																	"+
											"	    END LOOP;																	"+
											"	    return nm_cliente;															"+
											"	  END																			"+
											"	$BODY$																		   "+
											"	  LANGUAGE plpgsql VOLATILE													  "+
											"	  COST 100;																	"+
											"	ALTER FUNCTION get_clientes(integer)									"+
											"	  OWNER TO postgres;  ");							
			
			pstmt.execute();
			System.out.println("\nFinalizado.....");
			connect.commit();
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	/**
	 * Criaï¿½ï¿½o da funï¿½ï¿½o sql get_contra_parte no banco postgres
	 * @param connect
	 * @return
	 */
	public static Result jurisFix009getContraParte(){
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			PreparedStatement pstmt = null;
			
			System.out.println("Iniciando.....");
			
			pstmt = connect.prepareStatement(
					" CREATE OR REPLACE FUNCTION get_contra_parte(codigoprocesso integer)		 		"+
					"   RETURNS character varying AS													"+
					" $BODY$																			"+
					"  DECLARE																			"+
					"    nm_contra_parte character varying(50);											"+
					"    registros RECORD;																"+
					"  BEGIN																			"+
					"    FOR registros in SELECT nm_pessoa FROM prc_outra_parte A, grl_pessoa B			"+
					"        WHERE (A.cd_pessoa = B.cd_pessoa) AND (A.cd_processo = codigoProcesso)		"+
					"    LOOP																			"+
					"      IF (nm_contra_parte IS NULL) THEN											"+
					"        nm_contra_parte := registros.nm_pessoa;									"+
					"      ELSE																			"+
					"         IF(LENGTH(nm_contra_parte)+LENGTH(registros.nm_pessoa)>200) THEN			"+
					"         IF(LENGTH(nm_contra_parte)<198)THEN										"+
					"             nm_contra_parte = nm_contra_parte || '...';							"+
					"           ELSE																	"+
					"             nm_contra_parte = nm_contra_parte || '', '' || registros.nm_pessoa;	"+
					"           END IF;  								"+
					"        END IF;									"+
					"      END IF;    									"+
					"     END LOOP;										"+
					"    RETURN nm_contra_parte;						"+
					"   END												"+
					" $BODY$											"+
					"   LANGUAGE plpgsql VOLATILE						"+
					"   COST 100;										"+
					" ALTER FUNCTION get_contra_parte(integer)			"+
					"   OWNER TO postgres;								" );						
			
			pstmt.execute();
			System.out.println("\nFinalizado.....");
			connect.commit();
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Insere o vï¿½nculo CORRESPONDENTE em pessoas de ï¿½rgï¿½o sem o vï¿½nculo e 
	 * cria o ï¿½rgï¿½o para pessoas com vï¿½nculo de CORRESPONDENTE. 
	 * 
	 * Pode gerar duplicaï¿½ï¿½o de ï¿½rgï¿½os
	 * 
	 * @since abril/2016
	 * @author Maurï¿½cio
	 */
	public static Result jurisFix010Correspondente() {
		return jurisFix010Correspondente(null);
	}
	
	public static Result jurisFix010Correspondente(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
  		
	  		int count = 0;
	  		
	  		System.out.println("jurisFix010VinculoCorrespondente.Iniciando.....");
	  		int cdVinculoCorrespondente = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CORRESPONDENTE", 0, 0, connect);
	  		
	  		/*
	  		 * Falta de vï¿½nculo
	  		 */
	  		String sql = "select * from prc_orgao"
	  				+ " where cd_pessoa not in ("
	  				+ "		select A.cd_pessoa from grl_pessoa A"
	  				+ "		left outer join grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa)"
	  				+ "		where B.cd_vinculo="+cdVinculoCorrespondente+")";
	  		
	  		ResultSetMap rsm = Search.find(sql, "", null, connect, false);
	  		
	  		Result result = null;
	  		while(rsm.next()) {
	  			result = PessoaServices.addVinculo(rsm.getInt("cd_pessoa"), 3/*SSAA*/, cdVinculoCorrespondente, connect);
	  			if(result.getCode()<=0) {
	  				if(result.getCode()==-3)
	  					continue;
	  				else {
		  				connect.rollback();
		  				return result;	  					
	  				}
	  			}
	  			
	  			count++;
	  		}
	  		System.out.println(count+" vï¿½nculos criados.");
	  		
	  		/*
	  		 * Falta de ï¿½rgï¿½o
	  		 */
	  		count=0;
	  		sql = "select A.cd_pessoa, A.nm_pessoa from grl_pessoa A"
	  				+ " left outer join grl_pessoa_empresa B ON (A.cd_pessoa = B.cd_pessoa)"
	  				+ " where B.cd_vinculo="+cdVinculoCorrespondente
	  				+ " and not exists (select * from prc_orgao where A.cd_pessoa = cd_pessoa)";
	  		rsm = Search.find(sql, "", null, connect, false);
	  		while(rsm.next()) {
	  			Orgao orgao = new Orgao(0, 0, rsm.getString("nm_pessoa"), null, 0, rsm.getInt("cd_pessoa"), 0);
	  			if(OrgaoDAO.insert(orgao, connect)<=0) {
	  				connect.rollback();
	  				return new Result(-2, "Erro ao salvar ï¿½rgï¿½o");
	  			}
	  			count++;
	  		}
	  		System.out.println(count+" ï¿½rgï¿½o criados.");
	  		System.out.println("jurisFix010VinculoCorrespondente.Finalizado.....");
	  		
	  		connect.commit();
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Apaga documentos duplicados pela sincrinizaï¿½ï¿½o com a E&L
	 * 
	 * @return
	 * @since abril/2016
	 * @author Maurï¿½cio
	 * @category sincronizaï¿½ï¿½o E&L
	 */
	public static Result jurisFix011PtcDocumentoDuplicado() {
		return jurisFix011PtcDocumentoDuplicado(null);
	}
	
	public static Result jurisFix011PtcDocumentoDuplicado(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
	  		
	  		System.out.println("jurisFix011PtcDocumentoDuplicado.Iniciando.....");
  		
	  		PreparedStatement pstmt = null;
	  		ResultSet rs = null;
	  		Result result = null;
	  		
	  		pstmt = connect.prepareStatement("select nr_documento_externo from ptc_documento"
	  				+ " group by nr_documento_externo"
	  				+ " having count(nr_documento_externo)>1 "
	  				+ " and nr_documento_externo is not null");
	  		rs = pstmt.executeQuery();
	  		while(rs.next()) {
	  			System.out.println("nr_documento_externo: "+rs.getString("nr_documento_externo"));
	  			ResultSetMap rsmAux = new ResultSetMap(connect.prepareStatement("SELECT cd_documento FROM ptc_documento WHERE nr_documento_externo='"+rs.getString("nr_documento_externo")+"'").executeQuery());
	  			if(rsmAux.next() && rsmAux.size()>1){
	  				System.out.println("\tcd_documento: "+rsmAux.getInt("cd_documento"));
	  				result = DocumentoServices.remove(rsmAux.getInt("cd_documento"), true, connect);
	  				if(result.getCode()<=0) {
	  					connect.rollback();
	  					return result;
	  				}
	  			}
	  		}
	  		
	  		connect.commit();
	  		System.out.println("jurisFix011PtcDocumentoDuplicado.Finalizando.....");
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retira tags rtf e html de textos de tramitaï¿½ï¿½es oriundas da E&L
	 * 
	 * @return
	 * @since abril/2016
	 * @author Maurï¿½cio
	 * @category sincronizaï¿½ï¿½o E&L
	 */
	public static Result jurisFix012TxtTramitacao() {
		return jurisFix012TxtTramitacao(null);
	}
	
	public static Result jurisFix012TxtTramitacao(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
	  		
	  		System.out.println("jurisFix012TxtTramitacao.Iniciando.....");
  		
	  		PreparedStatement pstmt = null;
	  		ResultSet rs = null;
	  		Result result = null;
	  		int count = 0;
	  		
	  		pstmt = connect.prepareStatement("SELECT A.txt_tramitacao, A.cd_documento, A.cd_tramitacao "
	  				+ " FROM ptc_documento_tramitacao A"
	  				+ " JOIN ptc_documento B ON (A.cd_documento = B.cd_documento)"
	  				+ " WHERE B.nr_documento_externo IS NOT NULL"
	  				+ " AND A.txt_tramitacao IS NOT NULL");
	  		rs = pstmt.executeQuery();
	  		while(rs.next()) {
	  			String txtTramitacao = "";
							
				byte[] rtf = rs.getString("txt_tramitacao").getBytes();
				if((new String(rtf)).indexOf("\\rtf1")!=-1) {
					rtf = Converter.rtfToHtml(rtf);
					rtf = (new String(rtf)).replaceAll("\\<[^>]*>","").trim().getBytes();
					
					txtTramitacao = new String(rtf);

					DocumentoTramitacao tramitacao = DocumentoTramitacaoDAO.get(rs.getInt("cd_tramitacao"), rs.getInt("cd_documento"), connect);
					tramitacao.setTxtTramitacao(txtTramitacao);
					result = DocumentoTramitacaoServices.save(tramitacao, connect);
					if(result.getCode()<=0) {
						connect.rollback();
						return result;
					}

					count++;
				}
	  		}
	  		
	  		connect.commit();
	  		System.out.println(count+" registros alterados.");
	  		System.out.println("jurisFix012TxtTramitacao.Finalizando.....");
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Corrige a situaï¿½ï¿½o de agendas cumpridas pelo JurisManager Desktop
	 * 
	 * @return
	 * @since maio/2016
	 * 
	 * @author Maurï¿½cio
	 */
	public static Result jurisFix013StAgenda() {
		return jurisFix013StAgenda(null);
	}
	
	public static Result jurisFix013StAgenda(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
	  		
	  		System.out.println("jurisFix013StAgenda.Iniciando.....");
  		
	  		PreparedStatement pstmt = null;
	  		ResultSet rs = null;
	  		int result = 0;
	  		int count = 0;
	  		
	  		pstmt = connect.prepareStatement("SELECT A.cd_agenda_item "
	  				+ " FROM agd_agenda_item A"
	  				+ " WHERE A.dt_realizacao IS NOT NULL"
	  				+ " AND A.st_agenda_item NOT IN (1, 4)" //1:CUMPRIDO | 4:PENDENTE
			);
	  		rs = pstmt.executeQuery();
	  		while(rs.next()) {
	  			AgendaItem agenda = AgendaItemDAO.get(rs.getInt("cd_agenda_item"), connect);
	  			agenda.setStAgendaItem(AgendaItemServices.ST_AGENDA_CUMPRIDO);
	  			result = AgendaItemDAO.update(agenda, connect);
	  			
	  			if(result<=0) {
	  				connect.rollback();
	  				return new Result(result, "Erro ao alterar situaï¿½ï¿½o da agenda");
	  			}
	  			count++;
	  		}
	  		
	  		connect.commit();
	  		System.out.println(count+" registros alterados.");
	  		System.out.println("jurisFix013StAgenda.Finalizando.....");
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Corrige tipo de ocorrencia vindos da importaï¿½ï¿½o do PROCON
	 * 
	 * @return
	 * @since maio/2016
	 * 
	 * @author Maone
	 */
	public static Result jurisFix014TpOcorrencia() {
		return jurisFix014TpOcorrencia(null);
	}
	
	public static Result jurisFix014TpOcorrencia(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
	  		
	  		System.out.println("jurisFix014TpOcorrencia.Iniciando.....");
  		
	  		PreparedStatement pstmt = null;
	  		ResultSet rs = null;
	  		int result = 0;
	  		int count = 0;
	  		
	  		pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_ocorrencia, B.cd_tipo_ocorrencia as cd_tipo_ocorrencia_destino FROM ptc_documento_ocorrencia A" +
	  										" JOIN grl_tipo_ocorrencia B ON (A.txt_ocorrencia like B.nm_tipo_ocorrencia)" +
	  										" WHERE A.cd_tipo_ocorrencia = 19"
			);
	  		rs = pstmt.executeQuery();
	  		while(rs.next()) {
	  			DocumentoOcorrencia documentoOcorrencia = DocumentoOcorrenciaDAO.get(rs.getInt("cd_documento"), rs.getInt("cd_ocorrencia"), rs.getInt("cd_tipo_ocorrencia"), connect);
	  			documentoOcorrencia.setCdTipoOcorrencia(rs.getInt("cd_tipo_ocorrencia_destino"));
	  			result = DocumentoOcorrenciaDAO.update(documentoOcorrencia, rs.getInt("cd_documento"), rs.getInt("cd_ocorrencia"), rs.getInt("cd_tipo_ocorrencia"), connect);
	  			
	  			if(result<=0) {
	  				connect.rollback();
	  				return new Result(result, "Erro ao alterar tipo de ocorrencia");
	  			}
	  			count++;
	  		}
	  		
	  		connect.commit();
	  		System.out.println(count+" registros alterados.");
	  		System.out.println("jurisFix014TpOcorrencia.Finalizando.....");
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * 
	 */
	public static Result jurisFix015Recortes() {
		return jurisFix015Recortes(null);
	}
	
	public static Result jurisFix015Recortes(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
	  		
	  		System.out.println("jurisFix015Recortes.Iniciando.....");
  		
	  		GregorianCalendar dtInicial = new GregorianCalendar(2021, 5, 7);
	  		GregorianCalendar dtFinal = new GregorianCalendar(2021, 5, 7);

	  		ServicoRecorteServices.executeServices(dtInicial, dtFinal, connect);
	  		
	  		System.out.println("jurisFix015Recortes.Finalizando.....");
			
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Corrige o cadastro de cpf no banco do Dr. Edson
	 * 
	 * @category JurisManager
	 * @return
	 * @since maio/2016
	 * 
	 * @author Maurï¿½cio
	 */
	public static Result fixNrCpf() {
		return fixNrCpf(null);
	}
	public static Result fixNrCpf(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			int count = 0;
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT cd_pessoa, nr_cpf_cnpj "
					+ " FROM grl_pessoa "
					+ " WHERE gn_pessoa = 1"
				);
			
			ResultSet rs = pstmt.executeQuery();
						
			System.out.println("INICIANDO...\n");
			
			while(rs.next()) {
				int cdPessoa = rs.getInt("cd_pessoa");
				String nrCpf = rs.getString("nr_cpf_cnpj");
				
				pstmt = connect.prepareStatement("UPDATE grl_pessoa_fisica SET nr_cpf = ?"
												+ " WHERE cd_pessoa = ?");
				pstmt.setString(1, nrCpf);
				pstmt.setInt(2, cdPessoa);
				
				retorno = pstmt.executeUpdate();
				if(retorno<0) {
					if (isConnectionNull)
						connect.rollback();
					return new Result(-1, "Erro ao atualizar registro.");					
				}
				count++;
			}
						
			if (isConnectionNull)
				connect.commit();
			
			System.out.println(count+" registros alterados.\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Remove espaï¿½o no inicio e final em nm_pessoa de grl_pessoa
	 * 
	 * @return
	 * @since maio/2016
	 * @author Maurï¿½cio
	 */
	public static Result fixTrimNmPessoa() {
		return fixTrimNmPessoa(null);
	}
	public static Result fixTrimNmPessoa(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			int count = 0;
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT cd_pessoa, nm_pessoa FROM grl_pessoa WHERE nm_pessoa LIKE ' %' OR nm_pessoa LIKE '% '"
				);
			
			ResultSet rs = pstmt.executeQuery();
						
			System.out.println("INICIANDO...\n");
			
			while(rs.next()) {
				int cdPessoa = rs.getInt("cd_pessoa");
				String nmPessoa = rs.getString("nm_pessoa").trim();
				
				pstmt = connect.prepareStatement("UPDATE grl_pessoa SET nm_pessoa = ?"
												+ " WHERE cd_pessoa = ?");
				pstmt.setString(1, nmPessoa);
				pstmt.setInt(2, cdPessoa);
				
				retorno = pstmt.executeUpdate();
				if(retorno<0) {
					if (isConnectionNull)
						connect.rollback();
					return new Result(-1, "Erro ao atualizar registro.");					
				}
				count++;
			}
						
			if (isConnectionNull)
				connect.commit();
			
			System.out.println(count+" registros alterados.\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Cria um setor "Secretaria" em todas as instituiï¿½ï¿½es para envio e recebimento 
	 * de documentos (protocolo). Exceto para a SMED
	 * 
	 * @return
	 * @since maio/2016
	 * @author Maurï¿½cio
	 * @category protocolo SMED
	 */
	public static Result fixCriarSetorRecepcao() {
		return fixCriarSetorRecepcao(null);
	}
	public static Result fixCriarSetorRecepcao(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int count = 0;
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT cd_empresa FROM grl_empresa WHERE cd_empresa<>2"
				);
			
			ResultSet rs = pstmt.executeQuery();
						
			System.out.println("INICIANDO...\n");
			
			Result result = null;
			while(rs.next()) {
				int cdEmpresa = rs.getInt("cd_empresa");
				
				Setor setor = new Setor();
				setor.setCdEmpresa(cdEmpresa);
				setor.setNmSetor("SECRETARIA");
				setor.setSgSetor("SEC");
				setor.setLgRecepcao(1);
				setor.setTpSetor(SetorServices.TP_SETOR_INTERNO);
				setor.setStSetor(SetorServices.ST_ATIVO);
				
				result = SetorServices.save(setor, connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						connect.rollback();
					return result;
				}
				
				count++;
			}
						
			if (isConnectionNull)
				connect.commit();
			
			System.out.println(count+" setores criados.\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Cria agendas do tipo AUDIï¿½NCIA DE CONCILIAï¿½ï¿½O de acordo com o arquivo passado.
	 * Formato: nrDocumento; dtAudiencia; nmLocal; nmResponsavel
	 *  
	 * @param nmArquivo .csv com as agendas
	 * @return
	 * @author Maurï¿½cio
	 * @since 10/06/2016
	 */
	public static Result fixJuris015CriarAudienciaProcon(String nmArquivo) {
		return fixJuris015CriarAudienciaProcon(nmArquivo, null);
	}
	public static Result fixJuris015CriarAudienciaProcon(String nmArquivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int count = 0;
			RandomAccessFile raf = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			LogUtils.debug("INICIANDO...\n");
			
			raf = new RandomAccessFile(nmArquivo, "rw");
	  		String line = "";
			
	  		while((line = raf.readLine()) != null)	{
	  			
	  			StringTokenizer tokens = new StringTokenizer(line, ";", false);
	  			
	  			String nrDocumento = tokens.nextToken().trim();
	  			GregorianCalendar dtAudiencia = Util.convStringToCalendar(tokens.nextToken().trim());
	  			String nmLocal = tokens.nextToken().trim();
	  			String nmResponsavel = tokens.nextToken().trim();
	  			
	  			int cdDocumento = 0;
	  			int cdTipoPrazo = 0;
	  			int cdUsuario = 0;
	  			int cdResponsavel = 0;
	  			int cdEmpresa = 0;
	  			int cdLocal = 0;
	  			
	  			// Pegar cd_documento
	  			pstmt = connect.prepareStatement("SELECT cd_documento FROM ptc_documento WHERE nr_documento LIKE '"+nrDocumento+"'");
	  			rs = pstmt.executeQuery();
	  			if(rs.next()) {
	  				cdDocumento = rs.getInt("cd_documento");
	  			}
	  			rs.close();
	  			
	  			//Pegar cd_tipo_prazo
	  			pstmt = connect.prepareStatement("SELECT cd_tipo_prazo FROM ptc_tipo_prazo WHERE nm_tipo_prazo = ?");
	  			pstmt.setString(1, "AUDIENCIA DE CONCILIAï¿½ï¿½O");
	  			rs = pstmt.executeQuery();
	  			if(rs.next()) {
	  				cdTipoPrazo = rs.getInt("cd_tipo_prazo");
	  			}
	  			rs.close();
	  			
	  			// cd_usuario - SUPORTE TIVIC
	  			cdUsuario = 5;
	  			
	  			// cd_empresa
	  			cdEmpresa = 2;
	  			
	  			
	  			// Pegar cd_responsavel
	  			pstmt = connect.prepareStatement("SELECT A.cd_pessoa FROM grl_pessoa A"
	  											+ " JOIN seg_usuario B ON (A.cd_pessoa = B.cd_pessoa)"
	  											+ " WHERE A.nm_pessoa LIKE '%"+nmResponsavel+"%'");
	  			rs = pstmt.executeQuery();
	  			if(rs.next()) {
	  				cdResponsavel = rs.getInt("cd_pessoa");
	  			}
	  			rs.close();
	  			
	  			// Pegar cd_local
	  			pstmt = connect.prepareStatement("SELECT cd_local FROM agd_local WHERE nm_local LIKE '%"+nmLocal+"%'");
	  			rs = pstmt.executeQuery();
	  			if(rs.next()) {
	  				cdLocal = rs.getInt("cd_local");
	  			}
	  			rs.close();
	  			
	  			AgendaItem audiencia = new AgendaItem();
	  			audiencia.setCdAgendaItem(0);
	  			audiencia.setDtInicial(dtAudiencia);
	  			audiencia.setDtFinal(dtAudiencia);
	  			audiencia.setDtLancamento(new GregorianCalendar());
	  			audiencia.setCdDocumento(cdDocumento);
	  			audiencia.setCdUsuario(cdUsuario);
	  			audiencia.setCdPessoa(cdResponsavel);
	  			audiencia.setCdTipoPrazoDocumento(cdTipoPrazo);
	  			audiencia.setCdEmpresa(cdEmpresa);
	  			audiencia.setCdLocal(cdLocal);
	  			audiencia.setStAgendaItem(AgendaItemServices.ST_AGENDA_A_CUMPRIR);
	  			
	  			if(AgendaItemDAO.insert(audiencia, connect)<=0) {
	  				System.out.println("Erro ao lanï¿½ar agenda para: "+nrDocumento);
	  			}
	  			
	  		}
	  		
	  		raf.close();
	  		rs.close();
	  		pstmt.close();
						
			if (isConnectionNull)
				connect.commit();
			
			LogUtils.debug(count+" audiï¿½ncias criadas.\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Insere a string "/2016" nos campos nrDocumento e nrProtocoloExterno
	 * nos documentos sincronizados que nï¿½o a possuem.
	 * 
	 * @return
	 * @author Maurï¿½cio
	 * @since 14/06/2016
	 * @category Eel
	 */
	public static Result fixJuris015NrDocumento() {
		return fixJuris015NrDocumento(null);
	}
	public static Result fixJuris015NrDocumento(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int count = 0;
			System.out.println("INICIANDO...\n");
			
			/*
			 * nrDocumento
			 */
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT cd_documento FROM ptc_documento"
					+ " WHERE nr_documento_externo IS NOT NULL"
					+ " AND nr_ano_externo = '2016'"
					+ " AND nr_documento NOT LIKE '%/2016'"
				);
			
			ResultSet rs = pstmt.executeQuery();
			int retorno = 0;
			
			while(rs.next()) {
				int cdDocumento = rs.getInt("cd_documento");
				Documento doc = DocumentoDAO.get(cdDocumento, connect);
				doc.setNrDocumento(doc.getNrDocumento()+"/2016");
				
				retorno = DocumentoDAO.update(doc, connect);
				if(retorno<=0) {
					if(isConnectionNull)
						connect.rollback();
					return new Result(-1, "Erro ao alterar nrDocumento.");
				}
				
				count++;
			}
			
			/*
			 * nrProtocoloExterno
			 */
			pstmt = connect.prepareStatement(
					"SELECT cd_documento FROM ptc_documento"
					+ " WHERE nr_documento_externo IS NOT NULL"
					+ " AND nr_ano_externo = '2016'"
					+ " AND nr_protocolo_externo NOT LIKE '%/2016'"
				);
			
			rs = pstmt.executeQuery();
			retorno = 0;
			
			while(rs.next()) {
				int cdDocumento = rs.getInt("cd_documento");
				Documento doc = DocumentoDAO.get(cdDocumento, connect);
				doc.setNrProtocoloExterno(doc.getNrProtocoloExterno()+"/2016");
				
				retorno = DocumentoDAO.update(doc, connect);
				if(retorno<=0) {
					if(isConnectionNull)
						connect.rollback();
					return new Result(-1, "Erro ao alterar nrProtocoloExterno.");
				}
				
				count++;
			}
				
			rs.close();
			
			if (isConnectionNull)
				connect.commit();
			
			System.out.println(count+" nï¿½meros alterados.\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @return
	 * @author ï¿½lvaro
	 * @since 07/07/2016
	 */
	public static Result fixManager001NrCategoriasEconomicas() {
		return fixManager001NrCategoriasEconomicas(null);
	}
	public static Result fixManager001NrCategoriasEconomicas(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO fixManager001NrCategoriasEconomicas...\n");
			connect.prepareStatement(
					
					"UPDATE ADM_CATEGORIA_ECONOMICA "+ 
							"SET NR_CATEGORIA_ECONOMICA =''"		
					).executeUpdate();
			ResultSetMap rsm = new ResultSetMap( connect.prepareStatement(
					
					"SELECT * FROM ADM_CATEGORIA_ECONOMICA "+ 
							"WHERE CD_CATEGORIA_SUPERIOR IS NULL"		
					).executeQuery());
			rsm.beforeFirst();
			int nrCategoriaInicial = 1;
			while( rsm.next() ){
				CorrigirCategorias( rsm.getInt("CD_CATEGORIA_ECONOMICA"), 1, nrCategoriaInicial, "", rsm.getInt("TP_CATEGORIA_ECONOMICA"),  connect);
				nrCategoriaInicial++;
			}
			
			if (isConnectionNull)
				connect.commit();
			System.out.println("FINALIZADO fixManager001NrCategoriasEconomicas...\n");
			return new Result(1, "Executado com sucesso.");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Configura conta bancï¿½ria principal para registros de pessoa que possuem
	 * mais de uma conta bancï¿½ria
	 * @return
	 */
	public static Result fixManager002ContaBancariaPrincipal() {
		return fixManager002ContaBancariaPrincipal(null);
	}
	public static Result fixManager002ContaBancariaPrincipal(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO fixManager002ContaBancariaPrincipal...\n");
			ResultSetMap rsmContasBancaria = new ResultSetMap( connect.prepareStatement(
					" SELECT DISTINCT A.CD_PESSOA FROM GRL_PESSOA_CONTA_BANCARIA A "+ 
					" JOIN GRL_PESSOA_CONTA_BANCARIA B ON ( A.CD_PESSOA = B.CD_PESSOA ) "+
					" WHERE A.CD_CONTA_BANCARIA != B.CD_CONTA_BANCARIA "	
				).executeQuery());
			rsmContasBancaria.beforeFirst();
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(1, 0);
			
			while( rsmContasBancaria.next() ){
				 connect.prepareStatement(
						" UPDATE GRL_PESSOA_CONTA_BANCARIA SET LG_PRINCIPAL = 0 "+
						" WHERE CD_PESSOA = "+rsmContasBancaria.getInt("CD_PESSOA")	
					).executeUpdate();

				 connect.prepareStatement(
						 " UPDATE GRL_PESSOA_CONTA_BANCARIA SET LG_PRINCIPAL = 1 "+
						 " WHERE CD_PESSOA = "+rsmContasBancaria.getInt("CD_PESSOA")+
						 " AND CD_CONTA_BANCARIA = ( "+
						 "		             SELECT "+sqlLimit[0]+" CD_CONTA_BANCARIA "+
						 "						FROM GRL_PESSOA_CONTA_BANCARIA "+
						 "					    WHERE CD_PESSOA = "+rsmContasBancaria.getInt("CD_PESSOA")+
						 " 						"+sqlLimit[1]+"	) "	
				 ).executeUpdate();
				 
				 
			}
						
			if (isConnectionNull)
				connect.commit();
			System.out.println("FINALIZADO fixManager002ContaBancariaPrincipal...\n");
			return new Result(1, "Executado com sucesso.");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result fixManager003CopiarFormaPlanoPagamento( int cdEmpresa ) {
		return fixManager003CopiarFormaPlanoPagamento(cdEmpresa, null);
	}
	public static Result fixManager003CopiarFormaPlanoPagamento( int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO fixManager003CopiarFormaPlanoPagamento...\n");
			ResultSetMap rsmFormaPlanoPagamanento = new ResultSetMap( connect.prepareStatement(
					" select * from adm_forma_pagamento_empresa a "+
					" join adm_forma_plano_pagamento b on ( a.cd_forma_pagamento = b.cd_forma_pagamento and a.cd_empresa = b.cd_empresa ) "+
					" join adm_plano_pagamento c on ( b.cd_plano_pagamento = c.cd_plano_pagamento ) "+
					" where a.cd_empresa =  "+cdEmpresa+
					" and cd_administrador is not null"
			).executeQuery());
			
			ResultSetMap rsmFormaPagamentoEmpresa =  new ResultSetMap( connect.prepareStatement(
					" select * from adm_forma_pagamento_empresa a "+
//					" join adm_forma_plano_pagamento b on ( a.cd_forma_pagamento = b.cd_forma_pagamento and a.cd_empresa = b.cd_empresa ) "+
//					" join adm_plano_pagamento c on ( b.cd_plano_pagamento = c.cd_plano_pagamento ) "+
					" join adm_tipo_documento d on ( d.cd_tipo_documento = a.cd_tipo_documento ) "+
					" where a.cd_empresa  <> "+cdEmpresa+
					" and a.cd_tipo_documento in (65, 42 ) "+ //CARTAO DE CREDITO(65) E Dï¿½BITO(42)
					" and cd_administrador is not null"
			).executeQuery());
			
			rsmFormaPagamentoEmpresa.beforeFirst();
			while( rsmFormaPagamentoEmpresa.next() ){
				
				FormaPagamentoEmpresa pagEmpresa = FormaPagamentoEmpresaDAO.get(
								rsmFormaPagamentoEmpresa.getInt("CD_FORMA_PAGAMENTO"),
								rsmFormaPagamentoEmpresa.getInt("CD_EMPRESA"));
				pagEmpresa.setQtDiasCredito(30);
				if( rsmFormaPagamentoEmpresa.getInt("CD_TIPO_DOCUMENTO") == 42 ){
					//Dï¿½BITO
					pagEmpresa.setPrTaxaDesconto(2.2);
				}else if(  rsmFormaPagamentoEmpresa.getInt("CD_TIPO_DOCUMENTO") == 65 ){
					rsmFormaPlanoPagamanento.beforeFirst();
					while ( rsmFormaPlanoPagamanento.next() ) {
						FormaPlanoPagamento novaFormaPlanoPag = FormaPlanoPagamentoDAO.get(
															rsmFormaPlanoPagamanento.getInt("CD_FORMA_PAGAMENTO"),
															cdEmpresa,
															rsmFormaPlanoPagamanento.getInt("CD_PLANO_PAGAMENTO") );
						novaFormaPlanoPag.setCdEmpresa( pagEmpresa.getCdEmpresa()  );
						
						Result r = FormaPlanoPagamentoServices.save(novaFormaPlanoPag, connect);
						if( r.getCode() <= 0 ){
							Conexao.rollback(connect);
							return new Result(-1, "Erro ao copias planos de pagamento");
						}
					}
				}
				
				int i = FormaPagamentoEmpresaDAO.update(pagEmpresa, connect);
				if( i<=0 ){
					Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar forma de pagamento empresa");
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			System.out.println("FINALIZADO fixManager003CopiarFormaPlanoPagamento...\n");
			return new Result(1, "Executado com sucesso.");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result fixManager004pagamentosContaDiversa() {
		return fixManager004pagamentosContaDiversa(null);
	}
	
	public static Result fixManager004pagamentosContaDiversa( Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO fixManager004agamentosContaDiversa...\n");
			ResultSetMap rsmPagamentos = new ResultSetMap( connect.prepareStatement(
					"select c.cd_conta, a.cd_conta as cd_conta_financeira, c.cd_movimento_conta, a.cd_conta_pagar  "+
					" from adm_conta_pagar a "+
					"join adm_movimento_conta_pagar b on ( a.cd_conta_pagar = b.cd_conta_pagar ) "+
					" join adm_movimento_conta c on ( b.cd_conta = c.cd_conta and b.cd_movimento_conta = c.cd_movimento_conta ) "+
					" join adm_conta_financeira d on ( c.cd_conta = d.cd_Conta ) "+
					" where a.cd_conta <> c.cd_conta and a.cd_empresa <> d.cd_empresa "+
					" order by cd_conta, cd_conta_financeira, cd_movimento_conta"

					).executeQuery());
			connect.prepareStatement(
					" alter table adm_movimento_conta_categoria drop constraint adm_movimento_conta_categoria_cd_movimento_conta_fkey"
					).executeUpdate();
		
			connect.prepareStatement(
					" alter table adm_movimento_conta_pagar drop constraint adm_movimento_conta_pagar_cd_movimento_conta_fkey"
					).executeUpdate();
			
			
			
			int lastCdMovimentoConta = 0;
			int lastCdConta = 0;
			while( rsmPagamentos.next() ){
				int cdContaFinanceira = rsmPagamentos.getInt("CD_CONTA_FINANCEIRA");
				int cdConta = rsmPagamentos.getInt("CD_CONTA");
				int cdMovimentoConta = rsmPagamentos.getInt("CD_MOVIMENTO_CONTA");
				int cdContaPagar = rsmPagamentos.getInt("CD_CONTA_PAGAR");
				int cdMovimentoNovo = 0;
				
				ResultSet rs = connect.prepareStatement(
						" select MAX(cd_movimento_conta ) as cd_movimento_novo "+
						" from adm_movimento_conta "+
						" WHERE cd_conta =  "+cdContaFinanceira
					).executeQuery();
				
				rs.next();
				cdMovimentoNovo = rs.getInt("cd_movimento_novo")+1;
				System.out.println( "REG:"+rsmPagamentos.getPosition());
				System.out.println( "Conta Pagar :"+cdContaPagar );
				System.out.println( "Movimento Antigo :"+cdMovimentoConta );
				System.out.println( "Conta fin. Antiga :"+cdConta );
				System.out.println( "Movimento Novo :"+cdMovimentoNovo );
				System.out.println( "Conta fin. nova :"+cdContaFinanceira );
				System.out.println( "-------------------------------------------------------------");
				if( cdMovimentoConta != lastCdMovimentoConta && cdContaFinanceira != lastCdConta ||
					cdMovimentoConta != lastCdMovimentoConta && cdContaFinanceira == lastCdConta 
				){
					int i = connect.prepareStatement(
							" UPDATE ADM_MOVIMENTO_CONTA SET CD_CONTA = "+cdContaFinanceira+", CD_MOVIMENTO_CONTA = "+cdMovimentoNovo+
							" WHERE cd_conta =  "+cdConta+
							" and cd_movimento_conta =  "+cdMovimentoConta
							).executeUpdate();
					
					
					if( i <=0 ){
						if(isConnectionNull)
							Conexao.desconectar(connect);
						System.out.println( "Conta fin. :"+cdContaFinanceira );
						System.out.println( "Conta Pagar :"+cdContaPagar );
						return new Result(-2, "Nenhum movimento atualizado");
					}
				}
				
				int j = connect.prepareStatement(
						" UPDATE ADM_MOVIMENTO_CONTA_CATEGORIA  SET CD_CONTA = "+cdContaFinanceira+", CD_MOVIMENTO_CONTA = "+cdMovimentoNovo+
						" WHERE cd_conta = "+cdConta+" and cd_movimento_conta = "+cdMovimentoConta+
						" and cd_conta_pagar = "+cdContaPagar
						).executeUpdate();
				if( j <=0 ){
					if(isConnectionNull)
						Conexao.desconectar(connect);
					System.out.println( "Conta fin. :"+cdContaFinanceira );
					System.out.println( "Conta Pagar :"+cdContaPagar );
					return new Result(-2, "Nenhum movimento categoria atualizado");
				}
				
				int k = connect.prepareStatement(
						" UPDATE ADM_MOVIMENTO_CONTA_PAGAR SET CD_CONTA = "+cdContaFinanceira+", CD_MOVIMENTO_CONTA = "+cdMovimentoNovo+
						" WHERE cd_conta = "+cdConta+" and cd_movimento_conta = "+cdMovimentoConta+
						" and cd_conta_pagar = "+cdContaPagar
					).executeUpdate();
				if( k <=0 ){
					if(isConnectionNull)
						Conexao.desconectar(connect);
					System.out.println( "Conta fin. :"+cdContaFinanceira );
					System.out.println( "Conta Pagar :"+cdContaPagar );
					return new Result(-2, "Nenhum movimento conta pagar atualizado");
				}
				
				lastCdMovimentoConta = cdMovimentoConta;
				lastCdConta = cdContaFinanceira;
				//instanciar MovimentoContaPagar e atualizar com a conta bancï¿½ria da conta a pagar
				//instanciar MovimentoConta e atualizar com a conta bancï¿½ria da conta a pagar
			}
			
			connect.prepareStatement(
					" alter table adm_movimento_conta_categoria add  "+
					" CONSTRAINT adm_movimento_conta_categoria_cd_movimento_conta_fkey FOREIGN KEY (cd_movimento_conta, cd_conta) "+
					" REFERENCES adm_movimento_conta (cd_movimento_conta, cd_conta) MATCH SIMPLE"+
				    "  ON UPDATE NO ACTION ON DELETE NO ACTION  "
					).executeUpdate();
			
			connect.prepareStatement(
					" alter table adm_movimento_conta_pagar add  "+
					" CONSTRAINT adm_movimento_conta_pagar_cd_movimento_conta_fkey FOREIGN KEY (cd_movimento_conta, cd_conta) "+
					"   REFERENCES adm_movimento_conta (cd_movimento_conta, cd_conta) MATCH SIMPLE "+
					"  ON UPDATE NO ACTION ON DELETE NO ACTION "
				).executeUpdate();
			
			ResultSetMap rsmPagamentosRestantes = new ResultSetMap( connect.prepareStatement(
					"select a.cd_conta as cd_conta_financeira, a.cd_conta_pagar, c.cd_conta,  c.cd_movimento_conta "+
					" from adm_conta_pagar a "+
					"join adm_movimento_conta_pagar b on ( a.cd_conta_pagar = b.cd_conta_pagar ) "+
					" join adm_movimento_conta c on ( b.cd_conta = c.cd_conta and b.cd_movimento_conta = c.cd_movimento_conta ) "+
					" join adm_conta_financeira d on ( c.cd_conta = d.cd_Conta ) "+
					" where a.cd_conta <> c.cd_conta and a.cd_empresa <> d.cd_empresa "+
					" order by cd_movimento_conta, cd_conta, cd_conta_financeira"

					).executeQuery());
			
			System.out.println( "Fico com :"+rsmPagamentosRestantes.size() );
			if( rsmPagamentosRestantes.size() > 0 ){
				rsmPagamentosRestantes.next();
				System.out.println( "-------------------------------------------------------------");
				System.out.println( "Conta Pagar :"+rsmPagamentos.getInt("CD_CONTA_PAGAR") );
				System.out.println( "Conta fin. Pag :"+rsmPagamentos.getInt("CD_CONTA_FINANCEIRA"));
				System.out.println( "Movimento  :"+rsmPagamentos.getInt("CD_MOVIMENTO_CONTA"));
				System.out.println( "Conta Movimento  :"+rsmPagamentos.getInt("CD_CONTA"));
				
				if(isConnectionNull)
					Conexao.desconectar(connect);
				return new Result(-2, "15");
			}
			if (isConnectionNull)
				connect.commit();
			System.out.println("FINALIZADO fixManager004agamentosContaDiversa...\n");
			return new Result(1, "Executado com sucesso.");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result fixManager005NrDocContaMovimentacao() {
		Connection connect = Conexao.conectar();
		try {
			System.out.println("INICIANDO fixManager005NrDocContaMovimentacao...\n");

			connect.setAutoCommit(false);
			int r;
			ResultSetMap rsmContas;
			String nrDocConta = "";
			MovimentoConta movimento;
			
			ResultSet rsmMovimentos = connect.prepareStatement(
					" SELECT * FROM ( "+
						" SELECT A.CD_CONTA, A.CD_MOVIMENTO_CONTA, "+
						" COUNT(cd_conta_pagar) as qt_conta_pagar, "+
						" COUNT(cd_conta_receber) as qt_conta_receber "+
						" FROM adm_movimento_conta A "+
						" LEFT JOIN adm_movimento_conta_pagar   B on ( A.cd_conta = B.cd_conta AND A.cd_movimento_conta = B.cd_movimento_conta ) "+
						" LEFT JOIN adm_movimento_conta_receber C on ( A.cd_conta = C.cd_conta AND A.cd_movimento_conta = C.cd_movimento_conta ) "+
						" WHERE A.nr_doc_conta is null or A.nr_doc_conta = '' "+
						" GROUP BY A.CD_CONTA, A.CD_MOVIMENTO_CONTA "+
					"            ) AS tmp "+
					" WHERE tmp.qt_conta_pagar > 0 OR tmp.qt_conta_receber > 0 "
					).executeQuery();
			
			while( rsmMovimentos.next() ){
				
				if( rsmMovimentos.getInt("QT_CONTA_PAGAR") > 0 ){
					rsmContas = new ResultSetMap( connect.prepareStatement(
							" SELECT C.NR_DOCUMENTO FROM adm_movimento_conta A "+
									" JOIN adm_movimento_conta_pagar   B on ( A.cd_conta = B.cd_conta AND A.cd_movimento_conta = B.cd_movimento_conta ) "+
									" JOIN adm_conta_pagar             C on ( B.cd_conta_pagar = C.cd_conta_pagar ) "+
									" WHERE A.cd_conta = "+rsmMovimentos.getInt("CD_CONTA")+
									"       AND A.cd_movimento_conta = "+rsmMovimentos.getInt("CD_MOVIMENTO_CONTA")
							).executeQuery());
				}else{
					rsmContas = new ResultSetMap( connect.prepareStatement(
							" SELECT C.NR_DOCUMENTO FROM adm_movimento_conta A "+
									" JOIN adm_movimento_conta_receber   B on ( A.cd_conta = B.cd_conta AND A.cd_movimento_conta = B.cd_movimento_conta ) "+
									" JOIN adm_conta_receber             C on ( B.cd_conta_receber = C.cd_conta_receber ) "+
									" WHERE A.cd_conta = "+rsmMovimentos.getInt("CD_CONTA")+
									"       AND A.cd_movimento_conta = "+rsmMovimentos.getInt("CD_MOVIMENTO_CONTA")
							).executeQuery());
				}
				
				movimento = MovimentoContaDAO.get( rsmMovimentos.getInt("CD_MOVIMENTO_CONTA") , rsmMovimentos.getInt("CD_CONTA"), connect);
				rsmContas.beforeFirst();
				nrDocConta = "";
				while (rsmContas.next() ) {
					if( rsmContas.getString("NR_DOCUMENTO") != null && rsmContas.getString("NR_DOCUMENTO").trim().length() > 0 )
						nrDocConta += rsmContas.getString("NR_DOCUMENTO")+";";
				}
				if( nrDocConta.length() > 0 )
					movimento.setNrDocConta( nrDocConta.substring(0, nrDocConta.length()-1) );
				
				r = MovimentoContaDAO.update(movimento, connect);
				if( r <= 0){
					Conexao.rollback(connect);
					return new Result(1, "Erro ao atualizar movimetaï¿½ï¿½es.");
				}
			}
			
			connect.commit();
			System.out.println("FINALIZADO fixManager005NrDocContaMovimentacao...\n");
			return new Result(1, "Executado com sucesso.");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static Result fixManager006MigrarContasFinanceira(int cdContaOrigem, int cdContaDestino) {
		Connection connect = Conexao.conectar();
		try {
			System.out.println("INICIANDO fixManager006MigrarContasFinanceira...\n");
			
			connect.setAutoCommit(false);
			//remove as constraint
			System.out.println("Removendo constraints\n");
			connect.prepareStatement(
							" ALTER TABLE ADM_MOVIMENTO_CONTA_PAGAR "+
								" DROP CONSTRAINT adm_movimento_conta_pagar_cd_movimento_conta_fkey ;"+
							
							" ALTER TABLE ADM_MOVIMENTO_CONTA_RECEBER"+
								" DROP CONSTRAINT adm_movimento_conta_receber_cd_movimento_conta_fkey ;"+
							
							" ALTER TABLE ADM_MOVIMENTO_CONTA "+
								" DROP CONSTRAINT adm_movimento_conta_cd_movimento_origem_fkey;"+
							
							" ALTER TABLE ADM_MOVIMENTO_CONTA "+
								" DROP CONSTRAINT adm_movimento_conta_cd_conta_fkey1 ;"+
							
							" ALTER TABLE ADM_CONTA_FECHAMENTO_ARQUIVO "+
								" DROP CONSTRAINT adm_conta_fechamento_arquivo_cd_conta_fkey ;"+
							
							" ALTER TABLE ADM_FECHAMENTO_OCORRENCIA "+
								" DROP CONSTRAINT adm_fechamento_ocorrencia_cd_conta_fkey ;"+
							
							" ALTER TABLE ADM_MOVIMENTO_CONTA_ARQUIVO "+
								" DROP CONSTRAINT adm_movimento_conta_arquivo_cd_movimento_conta_fkey ;"+
							
							" ALTER TABLE adm_conta_movimento_origem "+
								" DROP CONSTRAINT adm_conta_mov_origem_fkey_1 ;"+
							
							" ALTER TABLE adm_movimento_conta_categoria"+
								" DROP CONSTRAINT adm_movimento_conta_categoria_cd_movimento_conta_fkey ;").executeUpdate();
			
			System.out.println("Atualizando tabelas independentes\n");
			connect.prepareStatement(
				"UPDATE ADM_CONTA_PAGAR SET CD_CONTA = "+cdContaDestino+" WHERE CD_CONTA = "+cdContaOrigem+"  ;"+
				"UPDATE ADM_CONTA_RECEBER SET CD_CONTA = "+cdContaDestino+" WHERE CD_CONTA = "+cdContaOrigem+"  ;"+
				"UPDATE ADM_CONTA_FECHAMENTO SET CD_CONTA = "+cdContaDestino+" WHERE CD_CONTA = "+cdContaOrigem+"  ;"+
				"UPDATE ADM_CONTA_FECHAMENTO_ARQUIVO SET CD_CONTA = "+cdContaDestino+" WHERE CD_CONTA = "+cdContaOrigem+"  ;"+
				"UPDATE ADM_FECHAMENTO_OCORRENCIA SET CD_CONTA = "+cdContaDestino+" WHERE CD_CONTA = "+cdContaOrigem+"  ;"
			).executeUpdate();
			
			ResultSetMap rsmMovimentacoes = new ResultSetMap( 
											connect.prepareStatement(
													" SELECT * "+
													" FROM ADM_MOVIMENTO_CONTA "+
													" WHERE CD_CONTA = "+cdContaOrigem).executeQuery());
			ResultSet rs;		
			rs = connect.prepareStatement(" SELECT MAX(CD_MOVIMENTO_CONTA) as CD_MOVIMENTO_CONTA "+
										  " FROM ADM_MOVIMENTO_CONTA WHERE CD_CONTA = "+cdContaDestino).executeQuery();		
			rs.next();
			int cdMovimentoContaNovo = rs.getInt("CD_MOVIMENTO_CONTA");
			
			System.out.println("Atualizando movimentaï¿½ï¿½es e referencias\n");
			while( rsmMovimentacoes.next() ){
				cdMovimentoContaNovo++;
				connect.prepareStatement(
						" UPDATE ADM_MOVIMENTO_CONTA "+
						" SET CD_CONTA = "+cdContaDestino+",CD_MOVIMENTO_CONTA = "+cdMovimentoContaNovo+
						" WHERE CD_CONTA = "+cdContaOrigem+" AND CD_MOVIMENTO_CONTA = "+rsmMovimentacoes.getInt("CD_MOVIMENTO_CONTA")  
					).executeUpdate();

				connect.prepareStatement(
						" UPDATE ADM_MOVIMENTO_CONTA "+
								" SET CD_CONTA_ORIGEM = "+cdContaDestino+", CD_MOVIMENTO_ORIGEM = "+cdMovimentoContaNovo+
								" WHERE CD_CONTA_ORIGEM = "+cdContaOrigem+" AND CD_MOVIMENTO_ORIGEM = "+rsmMovimentacoes.getInt("CD_MOVIMENTO_CONTA")  
						).executeUpdate();
				
				connect.prepareStatement(
						" UPDATE ADM_MOVIMENTO_CONTA_PAGAR "+
								" SET CD_CONTA = "+cdContaDestino+", CD_MOVIMENTO_CONTA = "+cdMovimentoContaNovo+
								" WHERE CD_CONTA = "+cdContaOrigem+" AND CD_MOVIMENTO_CONTA = "+rsmMovimentacoes.getInt("CD_MOVIMENTO_CONTA")  
						).executeUpdate();
				
				connect.prepareStatement(
						" UPDATE ADM_MOVIMENTO_CONTA_RECEBER "+
								" SET CD_CONTA = "+cdContaDestino+",CD_MOVIMENTO_CONTA = "+cdMovimentoContaNovo+
								" WHERE CD_CONTA = "+cdContaOrigem+" AND CD_MOVIMENTO_CONTA = "+rsmMovimentacoes.getInt("CD_MOVIMENTO_CONTA")  
						).executeUpdate();
				
				connect.prepareStatement(
						" UPDATE ADM_MOVIMENTO_CONTA_CATEGORIA "+
								" SET CD_CONTA = "+cdContaDestino+",CD_MOVIMENTO_CONTA = "+cdMovimentoContaNovo+
								" WHERE CD_CONTA = "+cdContaOrigem+" AND CD_MOVIMENTO_CONTA = "+rsmMovimentacoes.getInt("CD_MOVIMENTO_CONTA")  
						).executeUpdate();

				connect.prepareStatement(
						" UPDATE ADM_MOVIMENTO_CONTA_ARQUIVO "+
								" SET CD_CONTA = "+cdContaDestino+",CD_MOVIMENTO_CONTA = "+cdMovimentoContaNovo+
								" WHERE CD_CONTA = "+cdContaOrigem+" AND CD_MOVIMENTO_CONTA = "+rsmMovimentacoes.getInt("CD_MOVIMENTO_CONTA")  
						).executeUpdate();
				connect.prepareStatement(
						" UPDATE ADM_CONTA_MOVIMENTO_ORIGEM "+
								" SET CD_CONTA = "+cdContaDestino+",CD_MOVIMENTO_CONTA = "+cdMovimentoContaNovo+
								" WHERE CD_CONTA = "+cdContaOrigem+" AND CD_MOVIMENTO_CONTA = "+rsmMovimentacoes.getInt("CD_MOVIMENTO_CONTA")  
						).executeUpdate();
			}
			System.out.println("Reinserindo constraints\n");
			//reinsere as constraints
			connect.prepareStatement(
					" ALTER TABLE ADM_MOVIMENTO_CONTA_PAGAR "+
					" ADD CONSTRAINT adm_movimento_conta_pagar_cd_movimento_conta_fkey FOREIGN KEY (cd_movimento_conta, cd_conta) "+
				    " REFERENCES adm_movimento_conta (cd_movimento_conta, cd_conta) MATCH SIMPLE "+
				    " ON UPDATE NO ACTION ON DELETE NO ACTION "
					).executeUpdate();

			connect.prepareStatement(
					" ALTER TABLE ADM_MOVIMENTO_CONTA_RECEBER "+
							" ADD CONSTRAINT adm_movimento_conta_receber_cd_movimento_conta_fkey FOREIGN KEY (cd_movimento_conta, cd_conta) "+
							" REFERENCES adm_movimento_conta (cd_movimento_conta, cd_conta) MATCH SIMPLE "+
							" ON UPDATE NO ACTION ON DELETE NO ACTION "
					).executeUpdate();

			connect.prepareStatement(
					" ALTER TABLE adm_movimento_conta_categoria "+
					" ADD CONSTRAINT adm_movimento_conta_categoria_cd_movimento_conta_fkey FOREIGN KEY (cd_movimento_conta, cd_conta)"+
					" REFERENCES adm_movimento_conta (cd_movimento_conta, cd_conta) MATCH SIMPLE"+
					" ON UPDATE NO ACTION ON DELETE NO ACTION"
					).executeUpdate();
			
			connect.prepareStatement(
					" ALTER TABLE adm_movimento_conta "+
					" ADD CONSTRAINT adm_movimento_conta_cd_movimento_origem_fkey FOREIGN KEY (cd_movimento_origem, cd_conta_origem) "+
				    "  REFERENCES adm_movimento_conta (cd_movimento_conta, cd_conta) MATCH SIMPLE "+
				    "  ON UPDATE NO ACTION ON DELETE NO ACTION"
					).executeUpdate();
			
			connect.prepareStatement(
					" ALTER TABLE adm_movimento_conta "+
							" ADD CONSTRAINT adm_movimento_conta_cd_conta_fkey1 FOREIGN KEY (cd_conta, cd_fechamento) "+
					     " REFERENCES adm_conta_fechamento (cd_conta, cd_fechamento) MATCH SIMPLE "+
					     " ON UPDATE NO ACTION ON DELETE NO ACTION"
					).executeUpdate();

			
			connect.prepareStatement(
					" ALTER TABLE adm_conta_movimento_origem "+
					" ADD CONSTRAINT adm_conta_mov_origem_fkey_1 FOREIGN KEY (cd_movimento_conta, cd_conta) "+
				    "  REFERENCES adm_movimento_conta (cd_movimento_conta, cd_conta) MATCH SIMPLE "+
				    "  ON UPDATE NO ACTION ON DELETE NO ACTION "
					).executeUpdate();
		
			connect.prepareStatement(
					" ALTER TABLE adm_movimento_conta_arquivo "+
							" ADD CONSTRAINT adm_movimento_conta_arquivo_cd_movimento_conta_fkey FOREIGN KEY (cd_movimento_conta, cd_conta) "+
						    "  REFERENCES adm_movimento_conta (cd_movimento_conta, cd_conta) MATCH SIMPLE "+
						    "  ON UPDATE NO ACTION ON DELETE NO ACTION "
					).executeUpdate();
			connect.prepareStatement(
					" ALTER TABLE adm_conta_fechamento_arquivo "+
					" ADD CONSTRAINT adm_conta_fechamento_arquivo_cd_conta_fkey FOREIGN KEY (cd_conta, cd_fechamento) "+
				    "  REFERENCES adm_conta_fechamento (cd_conta, cd_fechamento) MATCH SIMPLE "+
				    "  ON UPDATE NO ACTION ON DELETE NO ACTION "
					).executeUpdate();

			connect.prepareStatement(
					" ALTER TABLE adm_fechamento_ocorrencia "+
							" ADD CONSTRAINT adm_fechamento_ocorrencia_cd_conta_fkey FOREIGN KEY (cd_conta, cd_fechamento) "+
							"  REFERENCES adm_conta_fechamento (cd_conta, cd_fechamento) MATCH SIMPLE "+
							"  ON UPDATE NO ACTION ON DELETE NO ACTION "
					).executeUpdate();
			
			connect.prepareStatement(
					" DELETE FROM ADM_CONTA_FINANCEIRA WHERE CD_CONTA = "+cdContaOrigem ).executeUpdate();
			
			connect.commit();
			System.out.println("FINALIZADO fixManager006MigrarContasFinanceira...\n");
			return new Result(1, "Executado com sucesso.");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	private static Result CorrigirCategorias(int cdCategoriaEconomica, int nrNivel, int nrCategoriaEconomica, String nrCategoriaSuperior, int tpCategoriaEconomica, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			CategoriaEconomica cat = CategoriaEconomicaDAO.get(cdCategoriaEconomica);
			String nrCategoriaNova = String.valueOf( nrCategoriaEconomica );
			if( cat.getCdCategoriaSuperior() > 0 )
				nrCategoriaNova = nrCategoriaSuperior+"."+nrCategoriaNova;
			
			cat.setNrCategoriaEconomica(nrCategoriaNova);
			cat.setIdCategoriaEconomica(nrCategoriaNova);
			cat.setNrNivel(nrNivel);
			if( tpCategoriaEconomica > -1 ){
				
				if( tpCategoriaEconomica == CategoriaEconomicaServices.TP_DESPESA 
					&& ( cat.getTpCategoriaEconomica() ==  CategoriaEconomicaServices.TP_RECEITA
						 || cat.getTpCategoriaEconomica() ==  CategoriaEconomicaServices.TP_DEDUCAO_DESPESA	)
				){
					cat.setTpCategoriaEconomica(tpCategoriaEconomica);
				}

				if( tpCategoriaEconomica == CategoriaEconomicaServices.TP_RECEITA 
					&& ( cat.getTpCategoriaEconomica() ==  CategoriaEconomicaServices.TP_DESPESA
						|| cat.getTpCategoriaEconomica() ==  CategoriaEconomicaServices.TP_DEDUCAO_RECEITA	)
				){
					cat.setTpCategoriaEconomica(tpCategoriaEconomica);
				}
			}
			CategoriaEconomicaServices.save(cat, connect);
			ResultSetMap rsm = new ResultSetMap( connect.prepareStatement(
					"SELECT * FROM ADM_CATEGORIA_ECONOMICA "+
					" WHERE CD_CATEGORIA_SUPERIOR = "+cdCategoriaEconomica
				).executeQuery());
			
			rsm.beforeFirst();
			int nrCategoriaFilha = 1;
			while( rsm.next() ){
				CorrigirCategorias( rsm.getInt("CD_CATEGORIA_ECONOMICA"), nrNivel+1, nrCategoriaFilha, nrCategoriaNova, cat.getTpCategoriaEconomica(), connect );
				nrCategoriaFilha++;
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Executado com sucesso.");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result fixMob001DescricaoDefeitosVistoria() {
		return fixMob001DescricaoDefeitosVistoria(null);
	}
	public static Result fixMob001DescricaoDefeitosVistoria(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO fixMob001DescricaoDefeitosVistoria...\n");
			ResultSetMap rsm = new ResultSetMap( connect.prepareStatement(
					" SELECT * FROM MOB_VISTORIA "+ 
					" WHERE ST_VISTORIA = "+VistoriaServices.ST_REPROVADA+
					" AND ( ds_observacao IS NULL OR ds_observacao = '' ) "		
				).executeQuery());
			rsm.beforeFirst();
			while( rsm.next() ){
				ResultSetMap rsmResultado = VistoriaServices.getResultadoVistoria(rsm.getInt("CD_VISTORIA"), false, connect);
				rsmResultado.beforeFirst();
				String defeitos = "ITENS REPROVADOS NA VISTORIA: ";
				int countItem = 0;
				int countDefeito = 0;
				while( rsmResultado.next() ){
					if( rsmResultado.getInt("ST_ITEM") == VistoriaPlanoItemServices.ST_REPROVADO  ){
						countItem++;
						defeitos += (countItem)+" - "+rsmResultado.getString("NM_VISTORIA_ITEM")+" ";
						ResultSetMap rsmDefeitos = (ResultSetMap)rsmResultado.getObject("DEFEITOS");
						countDefeito = 0;
						rsmDefeitos.beforeFirst();
						while( rsmDefeitos.next() ){
							if( rsmDefeitos.getInt("CD_VISTORIA_PLANO_ITEM") > 0 ){
								countDefeito++;
								defeitos += (countItem+"."+countDefeito)+" - "+rsmDefeitos.getString("NM_DEFEITO")+" ";
							}
						}
					}
				}
				System.out.println("Atualizando "+(rsm.getPosition()+1)+"ï¿½ vistoria de "+rsm.size()+", cdVistoria: "+rsm.getInt("CD_VISTORIA"));
				Vistoria vistoria = VistoriaDAO.get( rsm.getInt("CD_VISTORIA"), connect);
				vistoria.setDsObservacao( defeitos );
				int r = VistoriaDAO.update(vistoria, connect);
				if( r<= 0 ){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(1, "Erro ao atualizar vistoria.");
				}
			}
			
			//Conexao.rollback(connect);
			if (isConnectionNull)
				connect.commit();
			System.out.println("FINALIZADO fixMob001DescricaoDefeitosVistoria...\n");
			return new Result(1, "Executado com sucesso.");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * 
	 * @return
	 * @category EeL
	 */
	public static Result jurisFixInternoExterno() {
		return jurisFixInternoExterno(null);
	}
	public static Result jurisFixInternoExterno(Connection innerConn) {
		boolean isConnectionNull = innerConn==null;
		Connection outsideConn = EelUtils.conectarEelSqlServer();
		try {
			if (isConnectionNull) {
				innerConn = Conexao.conectar();
				innerConn.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			int retorno = 0;
			
			PreparedStatement pstmt = innerConn.prepareStatement("select cd_documento, nr_documento_externo "
					+ " from ptc_documento "
					+ "	where nr_documento_externo is not null "
					+ " and dt_protocolo>='2016.01.01 00:00:00'");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()) {
				pstmt = outsideConn.prepareStatement("select (case interno_externo when 'E' then 0 when 'I' then 1 end) as tp_interno_externo"
						+ " from pr_protocolo where controle=?");
				pstmt.setString(1, rsm.getString("nr_documento_externo"));
				ResultSetMap rsmAux = new ResultSetMap(pstmt.executeQuery());
				
				if(rsmAux.next()) {
					Documento doc = DocumentoDAO.get(rsm.getInt("cd_documento"), innerConn);
					doc.setTpInternoExterno(rsmAux.getInt("tp_interno_externo"));
					retorno = DocumentoDAO.update(doc, innerConn);
				}
				System.out.print(".");
			}
			System.out.println();
						
			if(retorno<=0)
				Conexao.rollback(innerConn);
			else if (isConnectionNull)
				innerConn.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(innerConn);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(innerConn);
			EelUtils.desconectarEelSqlServer(outsideConn);
		}
	}
	
	
	public static Result jurisFixDuplicidadeComarca(int cdCidade, int cdCidadeDelete) {
		return jurisFixDuplicidadeComarca(cdCidade, cdCidadeDelete, null);
	}
	public static Result jurisFixDuplicidadeComarca(int cdCidade, int cdCidadeDelete, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			System.out.println("cdCidade: "+cdCidade);
			System.out.println("cdCidadeDelete: "+cdCidadeDelete);
			
			int retorno = 0;
			PreparedStatement[] pstmtUpdate = {
					connection.prepareStatement("UPDATE adm_cliente set cd_cidade=? where cd_cidade=?"),
					connection.prepareStatement("UPDATE adm_produto_servico_tributo set cd_cidade=? where cd_cidade=?"),
					connection.prepareStatement("UPDATE grl_distrito set cd_cidade=? where cd_cidade=?"),
					connection.prepareStatement("UPDATE grl_pessoa_endereco set cd_cidade=? where cd_cidade=?"),
					connection.prepareStatement("UPDATE grl_pessoa_fisica set cd_naturalidade=? where cd_naturalidade=?"),
					connection.prepareStatement("UPDATE prc_bem_penhora set cd_cidade=? where cd_cidade=?"),
					connection.prepareStatement("UPDATE prc_comarca set cd_cidade=? where cd_cidade=?"),
					connection.prepareStatement("UPDATE prc_processo set cd_cidade=? where cd_cidade=?"),
					connection.prepareStatement("UPDATE prc_processo_instancia set cd_cidade=? where cd_cidade=?"),
					connection.prepareStatement("UPDATE prc_regra_faturamento set cd_cidade=? where cd_cidade=?"),
					connection.prepareStatement("UPDATE prc_tribunal_cidade set cd_cidade=? where cd_cidade=?")};
			
			PreparedStatement pstmtDeleteRegra = connection.prepareStatement("delete from prc_regra_fat_cidade where cd_regra_faturamento=? and cd_cidade=?");
			PreparedStatement pstmtDelete = connection.prepareStatement("delete from grl_cidade where cd_cidade=?");
			
			PreparedStatement pstmtAux = null;
			for(int j=0; j<pstmtUpdate.length; j++) {
				System.out.println("j: "+j);
				pstmtAux = pstmtUpdate[j];
				pstmtAux.setInt(1, cdCidade);
				pstmtAux.setInt(2, cdCidadeDelete);
				retorno = pstmtAux.executeUpdate();
				
				if(retorno<0) {
					Conexao.rollback(connection);
					return new Result(-2, "Erro ao atualizar referencias");
				}
			}	
			
			//REGRA DE FATURAMENTO
			pstmtAux = connection.prepareStatement("SELECT cd_regra_faturamento FROM prc_regra_fat_cidade WHERE cd_cidade=?");
			pstmtAux.setInt(1, cdCidadeDelete);
			ResultSetMap rsmAux = new ResultSetMap(pstmtAux.executeQuery());
			while(rsmAux.next()) {
				pstmtDeleteRegra.setInt(1, rsmAux.getInt("cd_regra_faturamento"));
				pstmtDeleteRegra.setInt(2, cdCidadeDelete);
				retorno = pstmtDeleteRegra.executeUpdate();
				
				if(retorno<0) {
					Conexao.rollback(connection);
					return new Result(-3, "Erro ao atualizar referencias em Regra de Faturamento");
				}
			}
			
			
			pstmtDelete.setInt(1, cdCidadeDelete);
			retorno = pstmtDelete.executeUpdate();
					
						
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	@Deprecated
	public static Result jurisFixDuplicidadePessoa(int cdPessoa, int cdPessoaDelete) {
		return jurisFixDuplicidadePessoa(cdPessoa, cdPessoaDelete, null);
	}
	public static Result jurisFixDuplicidadePessoa(int cdPessoa, int cdPessoaDelete, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			System.out.println("cdPessoa: "+cdPessoa);
			System.out.println("cdPessoaDelete: "+cdPessoaDelete);
			
			int retorno = 0;
			PreparedStatement[] pstmtUpdate = {
					connection.prepareStatement("UPDATE adm_avalista_contrato set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE adm_cliente set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE adm_cliente set cd_convenio=? where cd_convenio=?"),
					connection.prepareStatement("UPDATE adm_conta_financeira set cd_responsavel=? where cd_responsavel=?"),
					connection.prepareStatement("UPDATE adm_conta_pagar set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE adm_conta_receber set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE adm_conta_receber_evento set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE adm_avalista_contrato set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE adm_contrato set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE adm_contrato set cd_agente=? where cd_agente=?"),
					connection.prepareStatement("UPDATE adm_cotacao_pedido_item set cd_fornecedor=? where cd_fornecedor=?"),
					connection.prepareStatement("UPDATE adm_fornecedor_fabricante set cd_fornecedor=? where cd_fornecedor=?"),
					connection.prepareStatement("UPDATE adm_avalista_contrato set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE adm_historico_classificacao set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE adm_ordem_compra set cd_fornecedor=? where cd_fornecedor=?"),
					connection.prepareStatement("UPDATE adm_pedido_compra_fornecedor set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE adm_pedido_venda set cd_cliente=? where cd_cliente=?"),
					connection.prepareStatement("UPDATE adm_produto_fornecedor set cd_representante=? where cd_representante=?"),
					connection.prepareStatement("UPDATE adm_produto_fornecedor set cd_fornecedor=? where cd_fornecedor=?"),
					
					connection.prepareStatement("UPDATE agd_agenda_item set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE agd_agenda_item_participante set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE agd_grupo_pessoa set cd_pessoa=? where cd_pessoa=?"),
					
					connection.prepareStatement("UPDATE grl_formulario_atributo_valor set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_formulario_atributo_valor set cd_pessoa_valor=? where cd_pessoa_valor=?"),
					connection.prepareStatement("UPDATE grl_grupo_pessoa set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_ocorrencia set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_parametro set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_parametro_opcao set cd_pessoa=? where cd_pessoa=?"),
//					connection.prepareStatement("UPDATE grl_parametro_atributo_valor set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_pessoa set cd_pessoa_superior=? where cd_pessoa_superior=?"),
					connection.prepareStatement("UPDATE grl_pessoa_arquivo set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_pessoa_conta_bancaria set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_pessoa_empresa set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_pessoa_endereco set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_formulario_atributo_valor set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_pessoa_fisica set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_pessoa_juridica set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE grl_produto_servico set cd_fabricante=? where cd_fabricante=?"),
					
					connection.prepareStatement("UPDATE prc_atendimento set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE prc_contrato set cd_devedor=? where cd_devedor=?"),
					connection.prepareStatement("UPDATE prc_orgao set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE prc_orgao set cd_responsavel=? where cd_responsavel=?"),
					connection.prepareStatement("UPDATE prc_orgao_judicial_pessoa set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE prc_outra_parte set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE prc_parte_cliente set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE prc_processo set cd_advogado=? where cd_advogado=?"),
					connection.prepareStatement("UPDATE prc_processo set cd_advogado_contrario=? where cd_advogado_contrario=?"),
					connection.prepareStatement("UPDATE prc_processo_financeiro set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE prc_regra_faturamento set cd_cliente=? where cd_cliente=?"),
					connection.prepareStatement("UPDATE prc_testemunha set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE prc_tipo_andamento_faturamento set cd_cliente=? where cd_cliente=?"),
					connection.prepareStatement("UPDATE prc_tribunal set cd_pessoa=? where cd_pessoa=?"),
					connection.prepareStatement("UPDATE prc_tribunal set cd_pessoa_juridica=? where cd_pessoa_juridica=?"),
					
					connection.prepareStatement("UPDATE seg_usuario set cd_pessoa=? where cd_pessoa=?")
				};
			
			@SuppressWarnings("unused")
			PreparedStatement pstmtDelete[] = {
					connection.prepareStatement("delete from grl_pessoa_fisica where cd_pessoa=?"),
					connection.prepareStatement("delete from grl_pessoa_juridica where cd_pessoa=?"),
					connection.prepareStatement("delete from grl_pessoa_endereco where cd_pessoa=?"),
					connection.prepareStatement("delete from grl_pessoa_conta_bancaria where cd_pessoa=?"),
					connection.prepareStatement("delete from grl_pessoa_arquivo where cd_pessoa=?"),
					connection.prepareStatement("delete from grl_pessoa where cd_pessoa=?")
			};
			
			PreparedStatement pstmtAux = null;
			for(int j=0; j<pstmtUpdate.length; j++) {
				System.out.println("j: "+j);
				pstmtAux = pstmtUpdate[j];
				pstmtAux.setInt(1, cdPessoa);
				pstmtAux.setInt(2, cdPessoaDelete);
				retorno = pstmtAux.executeUpdate();
				
				if(retorno<0) {
					Conexao.rollback(connection);
					return new Result(-2, "Erro ao atualizar referencias");
				}
			}	
			
//			pstmtDelete.setInt(1, cdPessoaDelete);
//			retorno = pstmtDelete.executeUpdate();
					
						
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	public static Result jurisFixConfigAcoesAgenda() {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			connect.setAutoCommit(false);
			
			PreparedStatement pstmt = null;
			
			 		
			// criar aï¿½ï¿½es
			int cdModuloAgd = 20;
			AcaoServices.initPermissoesAgd(1, cdModuloAgd, connect);
			LogUtils.debug("Aï¿½ï¿½es criadas com sucesso.");
			
			// Usuï¿½rios
			pstmt = connect.prepareStatement("SELECT A.* FROM seg_usuario A "
					+ " JOIN grl_pessoa B ON (A.cd_pessoa=B.cd_pessoa)"
					+ " WHERE A.st_usuario=1");
			ResultSetMap rsmUsers = new ResultSetMap(pstmt.executeQuery());
			
			LogUtils.debug("SQL.Users");
			LogUtils.debug("SELECT A.* FROM seg_usuario A "
					+ " JOIN grl_pessoa B ON (A.cd_pessoa=B.cd_pessoa)"
					+ " WHERE A.st_usuario=1");
			
			//Novos Agrupamentos
			pstmt = connect.prepareStatement("SELECT A.cd_acao, A.id_acao, A.cd_modulo, A.cd_sistema, A.cd_agrupamento"
											+ " FROM seg_acao A"
											+ " JOIN seg_agrupamento_acao B on (A.cd_agrupamento=B.cd_agrupamento)"
											+ " WHERE A.cd_sistema=1"
											+ " AND A.cd_modulo="+cdModuloAgd
											+ " AND B.id_agrupamento IN ('AGD_GERAL', 'AGD_AUDIENCIA', 'AGD_PRAZO', 'AGD_TAREFA', 'AGD_DILIGENCIA')");
			ResultSetMap rsmAcoes = new ResultSetMap(pstmt.executeQuery());
			
			// Mapeamento das antigas aï¿½ï¿½es nas novas
			//<Nova, Antiga>
			HashMap<String, String> mapIdAcoes = new HashMap<>();
			mapIdAcoes.put("AGD.VIEW_AGENDA_TODOS", "AGENDA_TODOS");
			mapIdAcoes.put("AGD.VIEW_LISTA", "AGENDA_REL_LISTA_COMPROMISSOS");
			mapIdAcoes.put("AGD.VIEW_AGENDA_DIA", "AGENDA_REL_POR_DIA");
			mapIdAcoes.put("AGD.VIEW_AGENDA_SEMANA", "AGENDA_REL_POR_SEMANA");
			mapIdAcoes.put("AGD.VIEW_AGENDA_COMARCA", "AGENDA_REL_POR_COMARCA");
			mapIdAcoes.put("AGD.VIEW_AGENDA_EXCEL", "AGENDA_EXPORTAR_EXCEL");

			mapIdAcoes.put("AGD.INS_AUDIENCIA", "AGENDA_AUD_I");
			mapIdAcoes.put("AGD.UPD_AUDIENCIA", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_AUDIENCIA_DT_INICIAL", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_AUDIENCIA_DT_FINAL", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_AUDIENCIA_SITUACAO", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_AUDIENCIA_DETALHE", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_AUDIENCIA_RESPONSAVEL", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_AUDIENCIA_DT_REALIZACAO", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_AUDIENCIA_CUMPRIR", "CUMPRIR_AGENDA");
			mapIdAcoes.put("AGD.UPD_AUDIENCIA_CANCELAR", "AGENDA_CANC");
			mapIdAcoes.put("AGD.DEL_AUDIENCIA", "AGENDA_E");

			mapIdAcoes.put("AGD.INS_PRAZO", "AGENDA_COMP_I");
			mapIdAcoes.put("AGD.UPD_PRAZO", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_PRAZO_DT_INICIAL", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_PRAZO_DT_FINAL", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_PRAZO_SITUACAO", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_PRAZO_DETALHE", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_PRAZO_RESPONSAVEL", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_PRAZO_DT_REALIZACAO", "AGENDA_A");
			mapIdAcoes.put("AGD.UPD_PRAZO_CUMPRIR", "CUMPRIR_AGENDA");
			mapIdAcoes.put("AGD.UPD_PRAZO_CANCELAR", "AGENDA_CANC");
			mapIdAcoes.put("AGD.DEL_PRAZO", "AGENDA_E");

			mapIdAcoes.put("AGD.INS_TAREFA", "TAREFA_I");
			mapIdAcoes.put("AGD.UPD_TAREFA", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_TAREFA_DT_INICIAL", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_TAREFA_DT_FINAL", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_TAREFA_SITUACAO", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_TAREFA_DETALHE", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_TAREFA_RESPONSAVEL", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_TAREFA_DT_REALIZACAO", "TAREFA_CONCLUIDA");
			mapIdAcoes.put("AGD.UPD_TAREFA_CUMPRIR", "CUMPRIR_AGENDA");
			mapIdAcoes.put("AGD.UPD_TAREFA_CANCELAR", "AGENDA_CANC");
			mapIdAcoes.put("AGD.DEL_TAREFA", "TAREFA_E");

			mapIdAcoes.put("AGD.INS_DILIGENCIA", "TAREFA_I");
			mapIdAcoes.put("AGD.UPD_DILIGENCIA", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_DILIGENCIA_DT_INICIAL", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_DILIGENCIA_DT_FINAL", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_DILIGENCIA_SITUACAO", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_DILIGENCIA_DETALHE", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_DILIGENCIA_RESPONSAVEL", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_DILIGENCIA_DT_REALIZACAO", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_DILIGENCIA_VL_SERVICO", "TAREFA_A");
			mapIdAcoes.put("AGD.UPD_DILIGENCIA_CUMPRIR", "CUMPRIR_AGENDA");
			mapIdAcoes.put("AGD.UPD_DILIGENCIA_CANCELAR", "AGENDA_CANC");
			mapIdAcoes.put("AGD.DEL_DILIGENCIA", "TAREFA_E");
			

			UsuarioServices usuarioServices = new UsuarioServices();		
			while(rsmUsers.next()) {
				int cdUsuario = rsmUsers.getInt("cd_usuario");
				
				while(rsmAcoes.next()) {
					//Para cada uma das novas aï¿½ï¿½es
					// pega a permissï¿½o do usuï¿½rio para a aï¿½ï¿½o antiga correspondente
					String idAcaoOld = mapIdAcoes.get(rsmAcoes.getString("id_acao"));
					pstmt = connect.prepareStatement("select A.* from seg_usuario_permissao_acao A "
							+ " join seg_acao b on (A.cd_acao=B.cd_acao)"
							+ " where B.cd_agrupamento=7 "
							+ " AND A.cd_sistema=1 "
							+ " AND A.cd_modulo=1"
							+ " and A.cd_usuario="+cdUsuario
							+ " and b.id_acao='"+idAcaoOld+"'");
					
					ResultSetMap rsmPermissao = new ResultSetMap(pstmt.executeQuery());
					
					//configura a nova permissï¿½o para o usuï¿½rio com base na permissï¿½o para a antiga aï¿½ï¿½o
					if(rsmPermissao.next()) {
						LogUtils.debug("User: "+cdUsuario+"\tAcao: "+rsmPermissao.getInt("cd_acao"));
						int r = usuarioServices.addPermissaoAcao(
								cdUsuario, 
								rsmAcoes.getInt("cd_acao"), 
								rsmAcoes.getInt("cd_modulo"), 
								rsmAcoes.getInt("cd_sistema"), 
								rsmPermissao.getInt("lg_natureza"),
								connect);
						
						if(r<=0) {
							connect.rollback();
							return new Result(-1, "Erro ao configurar permissï¿½o.");
						}
					}
				}
				rsmAcoes.beforeFirst();
			}	
			
			connect.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static Result jurisFixConfigAcoesProcesso() {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			connect.setAutoCommit(false);
			
			PreparedStatement pstmt = null;
			

			// Mapeamento das antigas aï¿½ï¿½es nas novas
			//<Nova, Antiga>
			HashMap<String, String> mapIdAcoes = new HashMap<>();
			mapIdAcoes.put("PRC.INS_PROCESSO", "PROCESSO_I");
			mapIdAcoes.put("PRC.UPD_PROCESSO", "PROCESSO_A");
			mapIdAcoes.put("PRC.DEL_PROCESSO", "PROCESSO_E");
			
			mapIdAcoes.put("PRC.INS_TESTEMUNHA", "TESTEMUNHAS_I");
			mapIdAcoes.put("PRC.UPD_TESTEMUNHA", "TESTEMUNHAS_A");
			mapIdAcoes.put("PRC.DEL_TESTEMUNHA", "TESTEMUNHAS_E");
			
			mapIdAcoes.put("PRC.INS_OFICIAL_JUSTICA", "OFICIAISJUSTIï¿½A_I");
			mapIdAcoes.put("PRC.UPD_OFICIAL_JUSTICA", "OFICIAISJUSTIï¿½A_A");
			mapIdAcoes.put("PRC.DEL_OFICIAL_JUSTICA", "OFICIAISJUSTIï¿½A_E");
			
			mapIdAcoes.put("PRC.INS_TRIBUNAL", "TRIBUNAIS_I");
			mapIdAcoes.put("PRC.UPD_TRIBUNAL", "TRIBUNAIS_A");
			mapIdAcoes.put("PRC.DEL_TRIBUNAL", "TRIBUNAIS_E");
			
			mapIdAcoes.put("PRC.INS_JUIZO", "JUï¿½ZOS_I");
			mapIdAcoes.put("PRC.UPD_JUIZO", "JUï¿½ZOS_A");
			mapIdAcoes.put("PRC.DEL_JUIZO", "JUï¿½ZOS_E");
			
			mapIdAcoes.put("PRC.INS_GRUPO_PROCESSO", "GRUPOSPROCESSOS_I");
			mapIdAcoes.put("PRC.UPD_GRUPO_PROCESSO", "GRUPOSPROCESSOS_A");
			mapIdAcoes.put("PRC.DEL_GRUPO_PROCESSO", "GRUPOSPROCESSOS_E");
			
			mapIdAcoes.put("PRC.INS_TIPO_ANDAMENTO", "TIPOSANDAMENTO_I");
			mapIdAcoes.put("PRC.UPD_TIPO_ANDAMENTO", "TIPOSANDAMENTO_A");
			mapIdAcoes.put("PRC.DEL_TIPO_ANDAMENTO", "TIPOSANDAMENTO_E");
			
			mapIdAcoes.put("PRC.INS_TIPO_DOCUMENTO", "TIPODOCUMENTO(GED)_I");
			mapIdAcoes.put("PRC.UPD_TIPO_DOCUMENTO", "TIPODOCUMENTO(GED)_A");
			mapIdAcoes.put("PRC.DEL_TIPO_DOCUMENTO", "TIPODOCUMENTO(GED)_E");

			mapIdAcoes.put("PRC.INS_TIPO_PRAZO", "TIPOSPRAZO_I");
			mapIdAcoes.put("PRC.UPD_TIPO_PRAZO", "TIPOSPRAZO_A");
			mapIdAcoes.put("PRC.DEL_TIPO_PRAZO", "TIPOSPRAZO_E"); 		
			
			mapIdAcoes.put("PRC.INS_TIPO_ACAO", "TIPOSAï¿½ï¿½O_I");
			mapIdAcoes.put("PRC.UPD_TIPO_ACAO", "TIPOSAï¿½ï¿½O_A");
			mapIdAcoes.put("PRC.DEL_TIPO_ACAO", "TIPOSAï¿½ï¿½O_E"); 
			
			mapIdAcoes.put("PRC.INS_AREA_DIREITO", "ï¿½REASDIREITO_I");
			mapIdAcoes.put("PRC.UPD_AREA_DIREITO", "ï¿½REASDIREITO_A");
			mapIdAcoes.put("PRC.DEL_AREA_DIREITO", "ï¿½REASDIREITO_E"); 
			
			mapIdAcoes.put("PRC.INS_TIPO_SITUACAO", "FASES/SITUAï¿½ï¿½ES_I");
			mapIdAcoes.put("PRC.UPD_TIPO_SITUACAO", "FASES/SITUAï¿½ï¿½ES_A");
			mapIdAcoes.put("PRC.DEL_TIPO_SITUACAO", "FASES/SITUAï¿½ï¿½ES_E");
			
			mapIdAcoes.put("PRC.INS_TIPO_PEDIDO", "TIPOSPEDIDOS_I");
			mapIdAcoes.put("PRC.UPD_TIPO_PEDIDO", "TIPOSPEDIDOS_A");
			mapIdAcoes.put("PRC.DEL_TIPO_PEDIDO", "TIPOSPEDIDOS_E");
			
			mapIdAcoes.put("PRC.INS_TIPO_OBJETO", "OBJETOSAï¿½ï¿½O_I");
			mapIdAcoes.put("PRC.UPD_TIPO_OBJETO", "OBJETOSAï¿½ï¿½O_A");
			mapIdAcoes.put("PRC.DEL_TIPO_OBJETO", "OBJETOSAï¿½ï¿½O_E");
			
			mapIdAcoes.put("PRC.INS_ANDAMENTO", "PRC_AND_I");
			mapIdAcoes.put("PRC.UPD_ANDAMENTO", "PRC_AND_A");
			mapIdAcoes.put("PRC.DEL_ANDAMENTO", "PRC_AND_E");
			
			mapIdAcoes.put("PRC.INS_ARQUIVO", "PRC_ARQ_I");
			mapIdAcoes.put("PRC.UPD_ARQUIVO", "PRC_ARQ_A");
			mapIdAcoes.put("PRC.DEL_ARQUIVO", "PRC_ARQ_E");
			mapIdAcoes.put("PRC.VIEW_ARQUIVO", "PRC_ARQ_ACESSO");
			
			mapIdAcoes.put("PRC.INS_CONTRATO", "PRC_CRT_I");
			mapIdAcoes.put("PRC.UPD_CONTRATO", "PRC_CRT_A");
			mapIdAcoes.put("PRC.DEL_CONTRATO", "PRC_CRT_E");
			
			mapIdAcoes.put("PRC.INS_GARANTIA", "PRC_GRT_I");
			mapIdAcoes.put("PRC.UPD_GARANTIA", "PRC_GRT_A");
			mapIdAcoes.put("PRC.DEL_GARANTIA", "PRC_GRT_E");
			
			mapIdAcoes.put("PRC.INS_PROCESSO_FINANCEIRO_RECEITA", "PRC_FIN_RECEITA");
			mapIdAcoes.put("PRC.UPD_PROCESSO_FINANCEIRO_RECEITA", "PRC_FIN_A");
			mapIdAcoes.put("PRC.DEL_PROCESSO_FINANCEIRO_RECEITA", "PRC_FIN_E");
			
			mapIdAcoes.put("PRC.INS_PROCESSO_FINANCEIRO_DESPESA", "PRC_FIN_DESPESA");
			mapIdAcoes.put("PRC.UPD_PROCESSO_FINANCEIRO_DESPESA", "PRC_FIN_A");
			mapIdAcoes.put("PRC.DEL_PROCESSO_FINANCEIRO_DESPESA", "PRC_FIN_E");
			
			mapIdAcoes.put("PRC.PRINT_PROCESSO", "PRC_PRINT_PROCESSO");
			mapIdAcoes.put("PRC.UPD_PROCESSO_LOTE", "PRC_ALTERACAO_MASSA");
			mapIdAcoes.put("PRC.INS_ANDAMENTO_COLETIVO", "PRC_ANDAMENTO_COLETIVO");
			mapIdAcoes.put("PRC.SEND_EMAIL", "PRC_EMAIL");
			
			mapIdAcoes.put("PRC.INS_GRUPO_TRABALHO", "GRUPOSTRABALHO_I");
			mapIdAcoes.put("PRC.UPD_GRUPO_TRABALHO", "GRUPOSTRABALHO_A");
			mapIdAcoes.put("PRC.DEL_GRUPO_TRABALHO", "GRUPOSTRABALHO_E");
			
			
			
			// criar aï¿½ï¿½es
			int cdModuloJur = 1;
			AcaoServices.initPermissoesJur(1, cdModuloJur, connect);
			LogUtils.debug("Aï¿½ï¿½es criadas com sucesso.");
			
			// Usuï¿½rios
			pstmt = connect.prepareStatement("SELECT A.* FROM seg_usuario A "
					+ " JOIN grl_pessoa B ON (A.cd_pessoa=B.cd_pessoa)"
					+ " WHERE A.st_usuario=1");
			ResultSetMap rsmUsers = new ResultSetMap(pstmt.executeQuery());
			
			//Novos Agrupamentos
			pstmt = connect.prepareStatement("SELECT A.cd_acao, A.id_acao, A.cd_modulo, A.cd_sistema, A.cd_agrupamento"
											+ " FROM seg_acao A"
											+ " JOIN seg_agrupamento_acao B on (A.cd_agrupamento=B.cd_agrupamento)"
											+ " WHERE A.cd_sistema=1"
											+ " AND A.cd_modulo="+cdModuloJur
											+ " AND B.id_agrupamento IN ('PRC_PROCESSO','PRC_TRIBUNAL','PRC_JUIZO','PRC_GRUPO_PROCESSO',"
											+ "							 'PRC_TIPO_ANDAMENTO','PRC_TIPO_DOCUMENTO','PRC_TIPO_PRAZO','PRC_TIPO_ACAO',"
											+ "							 'PRC_AREA_DIREITO','PRC_TIPO_SITUACAO','PRC_TIPO_PEDIDO','PRC_TIPO_OBJETO',"
											+ "							 'PRC_GRUPO_TRABALHO')");
			ResultSetMap rsmAcoes = new ResultSetMap(pstmt.executeQuery());
			
			

			UsuarioServices usuarioServices = new UsuarioServices();		
			while(rsmUsers.next()) {
				int cdUsuario = rsmUsers.getInt("cd_usuario");
				
				while(rsmAcoes.next()) {
					//Para cada uma das novas aï¿½ï¿½es
					// pega a permissï¿½o do usuï¿½rio para a aï¿½ï¿½o antiga correspondente
					String idAcaoOld = mapIdAcoes.get(rsmAcoes.getString("id_acao"));
					pstmt = connect.prepareStatement("select A.* "
							+ " from seg_usuario_permissao_acao A "
							+ " join seg_acao b on (A.cd_acao=B.cd_acao)"
							+ " where A.cd_sistema=1 "
							+ " and A.cd_modulo="+cdModuloJur
							+ " and A.cd_usuario="+cdUsuario
							+ " and b.id_acao='"+idAcaoOld+"'");
					
					ResultSetMap rsmPermissao = new ResultSetMap(pstmt.executeQuery());
					
					//configura a nova permissï¿½o para o usuï¿½rio com base na permissï¿½o para a antiga aï¿½ï¿½o
					if(rsmPermissao.next()) {
												
						LogUtils.debug("User: "+cdUsuario+"\tAcao: "+rsmPermissao.getInt("cd_acao"));
						int r = usuarioServices.addPermissaoAcao(
								cdUsuario, 
								rsmAcoes.getInt("cd_acao"), 
								rsmAcoes.getInt("cd_modulo"), 
								rsmAcoes.getInt("cd_sistema"), 
								rsmPermissao.getInt("lg_natureza"),
								connect);
						
						if(r<=0) {
							connect.rollback();
							return new Result(-1, "Erro ao configurar permissï¿½o.");
						}
					}
				}
				rsmAcoes.beforeFirst();
			}	
			
			//desativar agrupamentos depreciados
			String[] idAcoes = mapIdAcoes.keySet().toArray(new String[0]);
			String[] idAcoesOld = new String[idAcoes.length];
			for (int i=0; i<idAcoes.length; i++) {
				idAcoesOld[i] = mapIdAcoes.get(idAcoes[i]);
			}
			pstmt = connect.prepareStatement("select distinct(cd_agrupamento) from seg_acao"
											+ " where cd_modulo="+cdModuloJur
											+ " and id_acao in ("+Util.join(idAcoesOld, "'", ",")+")");
			ResultSetMap rsmAcoesOld = new ResultSetMap(pstmt.executeQuery());
			while(rsmAcoesOld.next()) {
				AgrupamentoAcao aa = AgrupamentoAcaoDAO.get(rsmAcoesOld.getInt("cd_agrupamento"), cdModuloJur, 1, connect);
				aa.setLgAtivo(0);
				if(AgrupamentoAcaoDAO.update(aa, connect)<=0) {
					connect.rollback();
					return new Result(-1, "Erro ao desativar agrupamento depreciado.");
				}
			}
			
			while(rsmAcoes.next()) {
				AgrupamentoAcao aa = AgrupamentoAcaoDAO.get(rsmAcoes.getInt("cd_agrupamento"), cdModuloJur, 1, connect);
				aa.setLgAtivo(1);
				if(AgrupamentoAcaoDAO.update(aa, connect)<=0) {
					connect.rollback();
					return new Result(-1, "Erro ao ativar novo agrupamento.");
				}
			}
			
			connect.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static Result jurisFixAcoesPessoa() {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			connect.setAutoCommit(false);
			
			PreparedStatement pstmt = null;

			// Mapeamento das antigas aï¿½ï¿½es nas novas
			//<Nova, Antiga>
			HashMap<String, String> mapIdAcoes = new HashMap<>();
			mapIdAcoes.put("GRL.INS_PESSOA", "TERCEIROS_I");
			mapIdAcoes.put("GRL.UPD_PESSOA", "TERCEIROS_A");
			mapIdAcoes.put("GRL.DEL_PESSOA", "TERCEIROS_E");

			mapIdAcoes.put("GRL.INS_PESSOA_ADV_EMPRESA", "ADVOGADOSESCRITï¿½RIO_I");
			mapIdAcoes.put("GRL.UPD_PESSOA_ADV_EMPRESA", "ADVOGADOSESCRITï¿½RIO_A");
			mapIdAcoes.put("GRL.DEL_PESSOA_ADV_EMPRESA", "ADVOGADOSESCRITï¿½RIO_E");

			mapIdAcoes.put("GRL.INS_PESSOA_ADV_ADVERSO", "ADVOGADOS_ADVERSOS_I");
			mapIdAcoes.put("GRL.UPD_PESSOA_ADV_ADVERSO", "ADVOGADOS_ADVERSOS_A");
			mapIdAcoes.put("GRL.DEL_PESSOA_ADV_ADVERSO", "ADVOGADOS_ADVERSOS_E");

			mapIdAcoes.put("GRL.INS_PESSOA_CLIENTE", "CLIENTES_I");
			mapIdAcoes.put("GRL.UPD_PESSOA_CLIENTE", "CLIENTES_A");
			mapIdAcoes.put("GRL.DEL_PESSOA_CLIENTE", "CLIENTES_E");

			mapIdAcoes.put("GRL.INS_PESSOA_ADVERSO", "PARTES_CONTRï¿½RIAS_I");
			mapIdAcoes.put("GRL.UPD_PESSOA_ADVERSO", "PARTES_CONTRï¿½RIAS_A");
			mapIdAcoes.put("GRL.DEL_PESSOA_ADVERSO", "PARTES_CONTRï¿½RIAS_E");

			mapIdAcoes.put("GRL.INS_PESSOA_COLABORADOR", "COLABORADORES_I");
			mapIdAcoes.put("GRL.UPD_PESSOA_COLABORADOR", "COLABORADORES_A");
			mapIdAcoes.put("GRL.DEL_PESSOA_COLABORADOR", "COLABORADORES_E");
			
			
			
			// criar aï¿½ï¿½es
			int cdModuloGrl = 26;
			AcaoServices.initPermissoesGrl(1, cdModuloGrl, connect);
			LogUtils.debug("Aï¿½ï¿½es criadas com sucesso.");
			
			// Usuï¿½rios
			pstmt = connect.prepareStatement("SELECT A.* FROM seg_usuario A "
					+ " JOIN grl_pessoa B ON (A.cd_pessoa=B.cd_pessoa)"
					+ " WHERE A.st_usuario=1");
			ResultSetMap rsmUsers = new ResultSetMap(pstmt.executeQuery());
			
			//Novos Agrupamentos
			pstmt = connect.prepareStatement("SELECT A.cd_acao, A.id_acao, A.cd_modulo, A.cd_sistema, A.cd_agrupamento"
											+ " FROM seg_acao A"
											+ " JOIN seg_agrupamento_acao B on (A.cd_agrupamento=B.cd_agrupamento)"
											+ " WHERE A.cd_sistema=1"
											+ " AND A.cd_modulo="+cdModuloGrl
											+ " AND B.id_agrupamento IN ('GRL_CADASTRO_PESSOA')");
			ResultSetMap rsmAcoes = new ResultSetMap(pstmt.executeQuery());

			UsuarioServices usuarioServices = new UsuarioServices();		
			while(rsmUsers.next()) {
				int cdUsuario = rsmUsers.getInt("cd_usuario");
				
				while(rsmAcoes.next()) {
					//Para cada uma das novas aï¿½ï¿½es
					// pega a permissï¿½o do usuï¿½rio para a aï¿½ï¿½o antiga correspondente
					String idAcaoOld = mapIdAcoes.get(rsmAcoes.getString("id_acao"));
					pstmt = connect.prepareStatement("select A.* "
							+ " from seg_usuario_permissao_acao A "
							+ " join seg_acao b on (A.cd_acao=B.cd_acao)"
							+ " where A.cd_sistema=1 "
							+ " and A.cd_modulo=1"//+1
							+ " and A.cd_usuario="+cdUsuario
							+ " and b.id_acao='"+idAcaoOld+"'");
					
					ResultSetMap rsmPermissao = new ResultSetMap(pstmt.executeQuery());
					
					//configura a nova permissï¿½o para o usuï¿½rio com base na permissï¿½o para a antiga aï¿½ï¿½o
					if(rsmPermissao.next()) {
												
						LogUtils.debug("User: "+cdUsuario+"\tAcao: "+rsmPermissao.getInt("cd_acao"));
						int r = usuarioServices.addPermissaoAcao(
								cdUsuario, 
								rsmAcoes.getInt("cd_acao"), 
								rsmAcoes.getInt("cd_modulo"), 
								rsmAcoes.getInt("cd_sistema"), 
								rsmPermissao.getInt("lg_natureza"),
								connect);
						
						if(r<=0) {
							connect.rollback();
							return new Result(-1, "Erro ao configurar permissï¿½o.");
						}
					}
				}
				rsmAcoes.beforeFirst();
			}	
			
			//desativar agrupamentos depreciados
			String[] idAcoes = mapIdAcoes.keySet().toArray(new String[0]);
			String[] idAcoesOld = new String[idAcoes.length];
			for (int i=0; i<idAcoes.length; i++) {
				idAcoesOld[i] = mapIdAcoes.get(idAcoes[i]);
			}
			pstmt = connect.prepareStatement("select distinct(cd_agrupamento) from seg_acao"
											+ " where cd_modulo="+cdModuloGrl
											+ " and id_acao in ("+Util.join(idAcoesOld, "'", ",")+")");
			ResultSetMap rsmAcoesOld = new ResultSetMap(pstmt.executeQuery());
			while(rsmAcoesOld.next()) {
				AgrupamentoAcao aa = AgrupamentoAcaoDAO.get(rsmAcoesOld.getInt("cd_agrupamento"), cdModuloGrl, 1, connect);
				aa.setLgAtivo(0);
				if(AgrupamentoAcaoDAO.update(aa, connect)<=0) {
					connect.rollback();
					return new Result(-1, "Erro ao desativar agrupamento depreciado.");
				}
			}
			
			while(rsmAcoes.next()) {
				AgrupamentoAcao aa = AgrupamentoAcaoDAO.get(rsmAcoes.getInt("cd_agrupamento"), cdModuloGrl, 1, connect);
				aa.setLgAtivo(1);
				if(AgrupamentoAcaoDAO.update(aa, connect)<=0) {
					connect.rollback();
					return new Result(-1, "Erro ao ativar novo agrupamento.");
				}
			}
			
			connect.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	
	public static Result fixPessoaNomeVisualizacao() {
		return fixPessoaNomeVisualizacao(null);
	}
	public static Result fixPessoaNomeVisualizacao(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			int retorno = 0;
			
			ResultSetMap rsmPessoa = new ResultSetMap(connection.prepareStatement("SELECT * FROM grl_pessoa A, acd_aluno B WHERE A.cd_pessoa = B.cd_aluno").executeQuery());
			while(rsmPessoa.next()){
				String nmPessoa = rsmPessoa.getString("nm_pessoa");
				
				String nmPessoaBanco = Util.limparAcentos(nmPessoa);
				
				String[] nmPessoaVisualizacaoArray = nmPessoa.split(" ");
				String nmPessoaVisualizacao = "";
				for(String nmPessoaVisualizacaoParte : nmPessoaVisualizacaoArray){
					nmPessoaVisualizacaoParte = nmPessoaVisualizacaoParte.toLowerCase();
					nmPessoaVisualizacao += nmPessoaVisualizacaoParte.substring(0, 1).toUpperCase() +(nmPessoaVisualizacaoParte.length() > 1 ? nmPessoaVisualizacaoParte.substring(1) : "") + " "; 
				}
				nmPessoaVisualizacao = nmPessoaVisualizacao.substring(0, nmPessoaVisualizacao.length() - 1);
				
				connection.prepareStatement("UPDATE grl_pessoa SET nm_pessoa = '"+nmPessoaBanco+"', nm_pessoa_visualizacao = '"+nmPessoaVisualizacao+"' WHERE cd_pessoa = " + rsmPessoa.getInt("cd_pessoa")).executeUpdate();
			}
			rsmPessoa.beforeFirst();	
			
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	public static Result fixHorarioTurmas() {
		return fixHorarioTurmas(null);
	}
	public static Result fixHorarioTurmas(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			int retorno = 0;
			
			ResultSetMap rsmTurma = new ResultSetMap(connection.prepareStatement("select cd_turma from acd_turma_horario group by cd_turma having count(cd_turma) > 5 order by cd_turma").executeQuery());
			while(rsmTurma.next()){
				Turma turma = TurmaDAO.get(rsmTurma.getInt("cd_turma"), connection);
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_turma", "" + turma.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmHorariosTurma = TurmaHorarioDAO.find(criterios, connection);
				while(rsmHorariosTurma.next()){
					InstituicaoHorario instituicaoHorario = InstituicaoHorarioDAO.get(rsmHorariosTurma.getInt("cd_horario"), connection);
					if(turma.getTpTurno() != instituicaoHorario.getTpTurno()){
						if(connection.prepareStatement("DELETE FROM acd_turma_horario WHERE cd_horario = " + instituicaoHorario.getCdHorario() + " AND cd_turma = " + turma.getCdTurma()).executeUpdate() < 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao atualizar horï¿½rios da turma");
						}
					}
				}
				
			}
			rsmTurma.beforeFirst();	
			
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixTurmaAnterior() {
		return fixTurmaAnterior(null);
	}
	public static Result fixTurmaAnterior(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			int retorno = 0;

			ResultSetMap rsmInstituicoes = InstituicaoServices.getAllAtivas();
			while(rsmInstituicoes.next()){
				ResultSetMap rsmCursos = CursoServices.getAllCursosOf(rsmInstituicoes.getInt("cd_instituicao"), false, connection);
				while(rsmCursos.next()){
					int posTurma = 1;
					ResultSetMap rsmTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma A, acd_instituicao_periodo B WHERE A.cd_periodo_letivo = B.cd_periodo_letivo AND nm_periodo_letivo = '2017' AND A.cd_instituicao = " + rsmInstituicoes.getInt("cd_instituicao") + " AND A.cd_curso = " + rsmCursos.getInt("cd_curso") + " ORDER BY A.nm_turma").executeQuery());
					while(rsmTurma.next()){
						int cdTurmaAntiga = 0;
						int posTurmaAntiga = 1;
						int cdCursoAnterior = 0;
						
						int cdCursoEtapa = 0;
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_curso", "" + rsmCursos.getInt("cd_curso"), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmCursoEtapa = CursoEtapaDAO.find(criterios, connection);
						if(rsmCursoEtapa.next())
							cdCursoEtapa = rsmCursoEtapa.getInt("cd_curso_etapa");
						
						ResultSetMap rsmCursoAnterior = new ResultSetMap(connection.prepareStatement("SELECT A.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + cdCursoEtapa).executeQuery());
						if(rsmCursoAnterior.next())
							cdCursoAnterior = rsmCursoAnterior.getInt("cd_curso");
						ResultSetMap rsmTurmaAntiga = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma A, acd_instituicao_periodo B WHERE A.cd_periodo_letivo = B.cd_periodo_letivo AND nm_periodo_letivo = '2016' AND A.cd_instituicao = " + rsmInstituicoes.getInt("cd_instituicao") + " AND A.cd_curso = " + cdCursoAnterior + " AND st_turma = "+TurmaServices.ST_ATIVO+" ORDER BY A.nm_turma").executeQuery());
						while(rsmTurmaAntiga.next()){
							
							if(posTurma == posTurmaAntiga){
								cdTurmaAntiga = rsmTurmaAntiga.getInt("cd_turma");
								break;
							}
							
							posTurmaAntiga++;
						}
						rsmTurmaAntiga.beforeFirst();
						
						if(cdTurmaAntiga > 0){
							Turma turma = TurmaDAO.get(rsmTurma.getInt("cd_turma"), connection);
							Curso curso = CursoDAO.get(turma.getCdCurso(), connection);
							
							Turma turmaAntiga = TurmaDAO.get(cdTurmaAntiga, connection);
							Curso cursoAntigo = CursoDAO.get(turmaAntiga.getCdCurso(), connection);
							
							System.out.println("Turma Antiga: " + cursoAntigo.getNmProdutoServico() + " - " + turmaAntiga.getNmTurma());
							System.out.println("Turma Nova:   " + curso.getNmProdutoServico() + " - " + turma.getNmTurma());
							System.out.println();
							
							turma.setCdTurmaAnterior(cdTurmaAntiga);
							if(TurmaDAO.update(turma, connection) < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Erro ao atualizar turma");
							}
						}
						
						posTurma++;
					}
					rsmTurma.beforeFirst();	
				}
			}
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	public static Result fixMatriculaTransporte() {
		return fixMatriculaTransporte(null);
	}
	public static Result fixMatriculaTransporte(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			int retorno = 0;
			
			ResultSetMap rsmAlunoTipoTransporte = new ResultSetMap(connection.prepareStatement("select * from acd_aluno_tipo_transporte").executeQuery());
			while(rsmAlunoTipoTransporte.next()){
				Aluno aluno = AlunoDAO.get(rsmAlunoTipoTransporte.getInt("cd_aluno"), connection);
				System.out.println("Aluno: " + aluno.getNmPessoa());
				ResultSetMap rsmMatriculas = new ResultSetMap(connection.prepareStatement("SELECT A.cd_matricula, A.cd_curso FROM acd_matricula A, acd_instituicao_periodo B WHERE A.cd_periodo_letivo = B.cd_periodo_letivo AND B.nm_periodo_letivo = '2016' AND A.cd_aluno = " + rsmAlunoTipoTransporte.getInt("cd_aluno") + " AND st_matricula = " + MatriculaServices.ST_ATIVA).executeQuery());
				while(rsmMatriculas.next()){
					Curso curso = CursoDAO.get(rsmMatriculas.getInt("cd_curso"), connection);
					if(rsmMatriculas.getInt("cd_curso") != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
						if(MatriculaTipoTransporteDAO.get(rsmMatriculas.getInt("cd_matricula"), rsmAlunoTipoTransporte.getInt("cd_tipo_transporte"), connection) == null){
							System.out.println("Cadastrado na Modalidade: " + curso.getNmProdutoServico());
							MatriculaTipoTransporte matriculaTipoTransporte = new MatriculaTipoTransporte(rsmMatriculas.getInt("cd_matricula"), rsmAlunoTipoTransporte.getInt("cd_tipo_transporte"));
							if(MatriculaTipoTransporteDAO.insert(matriculaTipoTransporte, connection) < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Erro ao inserir Matricula Tipo Transporte");
							}
						}
						else{
							System.out.println("Nï¿½o cadastrado na Modalidade: " + curso.getNmProdutoServico());
						}
						Matricula matricula = MatriculaDAO.get(rsmMatriculas.getInt("cd_matricula"), connection);
						matricula.setLgTransportePublico(1);
						if(MatriculaDAO.update(matricula, connection) < 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao atualizar matricula");
						}
					}
					else{
						Matricula matricula = MatriculaDAO.get(rsmMatriculas.getInt("cd_matricula"), connection);
						matricula.setLgTransportePublico(0);
						if(MatriculaDAO.update(matricula, connection) < 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao atualizar matricula");
						}
					}
				}
				System.out.println();
				rsmMatriculas.beforeFirst();
			}
			rsmAlunoTipoTransporte.beforeFirst();	
			
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result jurisFixStDocumento() {
		return jurisFixStDocumento(null);
	}
	public static Result jurisFixStDocumento(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			int cdSitDocRecebido = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0);
			int cdSitDocTramitando = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_TRAMITANDO", 0);
			if(cdSitDocRecebido==0) {
				connect.rollback();
				return new Result(-2, "Erro ao buscar situacao EM ABERTO");
			}
			if(cdSitDocTramitando==0) {
				connect.rollback();
				return new Result(-2, "Erro ao buscar situacao TRAMITANDO");
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.TP_SETOR", SetorServices.TP_SETOR_INTERNO+"", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmSetor = SetorServices.find(criterios, connect);
			
			while(rsmSetor.next()) {
				ResultSetMap rsmDoc = DocumentoServices.getListDocumentosParados(rsmSetor.getInt("CD_SETOR"), 2000);
				
				while(rsmDoc.next()) {
					System.out.print(rsmDoc.getInt("CD_DOCUMENTO")+" | ");
					Documento documento = DocumentoDAO.get(rsmDoc.getInt("CD_DOCUMENTO"), connect);
					DocumentoTramitacao tramitacao = DocumentoTramitacaoServices.getUltimaTramitacao(rsmDoc.getInt("CD_DOCUMENTO"), connect);
					
					if(documento.getCdSituacaoDocumento()==cdSitDocRecebido && tramitacao.getDtRecebimento()==null) {
						documento.setCdSituacaoDocumento(cdSitDocTramitando);
						
						Result result = DocumentoServices.update(documento, null, connect, null);
						if(result.getCode()<0) {
							retorno = result.getCode();
							connect.rollback();
							return result;
						}
					}
				}
			}
						
						
			if (retorno>0 && isConnectionNull)
				connect.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
//	public static Result importarMatriculas2017(){
//		String nmUrlDatabase = "jdbc:postgresql://192.168.10.43/edf_smed";
//		String nmLoginDataBase = "postgres";
//		String nmSenhaDatBase = "CDF3DP!";
//		
//		return importarMatriculas2017(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
//	}
	
//	public static Result importarMatriculas2017(String nmUrlDatabase, String nmLoginDataBase, String nmSenhaDatBase){
//		//Faz a conexao com o Local
//		Connection connectLocal = Conexao.conectar();
//		try{
//			connectLocal.setAutoCommit(false);
//			
//
//			Connection connectServer = Conexao.conectar(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
//			
//			try{
//				
//				connectServer.setAutoCommit(false);
//				
//				int x = 0;
//				ResultSetMap rsmLocal = new ResultSetMap(connectLocal.prepareStatement("SELECT * FROM acd_turma WHERE cd_periodo_letivo > 1347 AND nm_turma = 'TRANSFERENCIA'").executeQuery());
//				while(rsmLocal.next()){
//					System.out.println("Turmas cadastradas = " + (++x) + " de " + rsmLocal.size());
//					TurmaDAO.insert(new Turma(rsmLocal.getInt("cd_turma"), rsmLocal.getInt("cd_matriz"), rsmLocal.getInt("cd_periodo_letivo"), 
//											rsmLocal.getString("nm_turma"), rsmLocal.getGregorianCalendar("dt_abertura"), null, 
//											rsmLocal.getInt("tp_turno"), 0, 0, rsmLocal.getInt("st_turma"), 0, rsmLocal.getInt("cd_instituicao"), 
//											rsmLocal.getInt("cd_curso"), rsmLocal.getInt("qt_vagas"), rsmLocal.getInt("cd_curso_modulo"), null, 
//											rsmLocal.getInt("qt_dias_semana_atividade"), rsmLocal.getInt("tp_atendimento"), rsmLocal.getInt("tp_modalidade_ensino"), 
//											rsmLocal.getString("id_turma"), rsmLocal.getInt("tp_educacao_infantil"), rsmLocal.getInt("lg_mais_educa"), 
//											rsmLocal.getInt("cd_turma_anterior"), 0), connectServer);
//				}
//				
//				
//				x = 0;
//				rsmLocal = new ResultSetMap(connectLocal.prepareStatement("SELECT * FROM acd_matricula WHERE cd_periodo_letivo > 1347").executeQuery());
//				while(rsmLocal.next()){
//					System.out.println("Matriculas cadastradas = " + (++x) + " de " + rsmLocal.size());
//					MatriculaDAO.insert(new Matricula(rsmLocal.getInt("cd_matricula"), rsmLocal.getInt("cd_matriz"),
//													rsmLocal.getInt("cd_turma"), rsmLocal.getInt("cd_periodo_letivo"), 
//													Util.getDataAtual(), null, rsmLocal.getInt("st_matricula"), rsmLocal.getInt("tp_matricula"), 
//													rsmLocal.getString("nr_matricula"), rsmLocal.getInt("cd_aluno"), rsmLocal.getInt("cd_matricula_origem"), 
//													rsmLocal.getInt("cd_reserva"), rsmLocal.getInt("cd_area_interesse"), null, null, rsmLocal.getInt("cd_curso"), 
//													0, 0, rsmLocal.getInt("lg_transporte_publico"), rsmLocal.getInt("tp_poder_responsavel"), 
//													rsmLocal.getInt("tp_forma_ingresso"), null, null, 0, 0, 0), connectServer);
//				}
//				
//				System.out.println("final");
//				//Da commit em ambos
//				connectLocal.commit();
//				connectServer.commit();
//			
//				return new Result(1, "ImportaÃ§Ã£o realizada");
//			}
//			catch(Exception e){
//				Conexao.rollback(connectLocal);
//				Conexao.rollback(connectServer);
//				//registra log de erro quando a classe Ã© utilizad pelo pdv
//				Util.registerLog(e);
//				e.printStackTrace();
//				return new Result(-1, "Erro na importaÃ§Ã£o");
//			}
//			finally{
//				Conexao.desconectar(connectServer);
//			}
//		}
//		
//		catch(Exception e){
//			Util.registerLog(e);
//			e.printStackTrace();
//			Conexao.rollback(connectLocal);
//			return new Result(-1, "Erro na sincronizaÃ§Ã£o");
//		}
//		finally{
//			Conexao.desconectar(connectLocal);
//		}
//	}
	
	/**
	 * Criar rotas de ida para linhas sem rotas
	 * @return
	 */
	public static Result fixCriarLinhaRota() {
		Connection connBase = null;
		try {
			connBase = Conexao.conectar();
			connBase.setAutoCommit(false);
			
			System.out.println("\n\n\n");
			System.out.println("fixLinhaRota");
			System.out.println("-----------------");
			
			int retorno = 0;
			int count = 0;
			
			String sql = "SELECT cd_linha, nr_linha FROM mob_linha WHERE cd_linha NOT IN (SELECT cd_linha FROM mob_linha_rota GROUP BY cd_linha);";
			
			ResultSetMap rsmLinhas = new ResultSetMap(connBase.prepareStatement(sql).executeQuery());
			System.out.println(rsmLinhas.getLines().size()+" linhas sem rota.");
			
			while(rsmLinhas.next()) {
				
				count++;
				System.out.println("Linha "+ rsmLinhas.getString("NR_LINHA") + " com rota criada!");
				
				LinhaRota linhaRota = new LinhaRota();
				
				linhaRota.setCdLinha(rsmLinhas.getInt("CD_LINHA"));
				linhaRota.setTpRota(0);
				linhaRota.setStRota(0);
				linhaRota.setCdRota(0);
				linhaRota.setQtKm(0.0);
				
				retorno = LinhaRotaServices.save(linhaRota, null, connBase).getCode();
				
				if(retorno<0) {
					Conexao.rollback(connBase);
					return new Result(retorno, "Erro ao atualizar agenda");
				}
			}
			
			if(retorno<0)
				Conexao.rollback(connBase);
			else
				connBase.commit();
			
			System.out.println(count+" rotas criadas.");
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connBase);
			return null;
		}
		finally {
			Conexao.desconectar(connBase);
		}
	}
	
	public static Result fixProcessoAgenda() {
		Connection connBase = null;
		Connection connCompliance = null;
		try {
			connBase = Conexao.conectar();
			connBase.setAutoCommit(false);
			
			connCompliance = ComplianceManager.conectar();
			
			System.out.println("\n\n\n");
			System.out.println("fixProcessoAgenda");
			System.out.println("-----------------");
			
			int retorno = 0;
			int count = 0;
			
			String sql = "select cd_agenda_item "
					+ " from agd_agenda_item "
					+ " where dt_lancamento >= '01.01.2016' "
					+ " group by cd_agenda_item";
			
			ResultSetMap rsmAgenda = new ResultSetMap(connCompliance.prepareStatement(sql).executeQuery());
			System.out.println(rsmAgenda.getLines().size()+" agendas no compliance.");
			
			while(rsmAgenda.next()) {
				sql = "select distinct(cd_processo) "
						+ " from agd_agenda_item "
						+ " where cd_agenda_item="+rsmAgenda.getInt("cd_agenda_item")
						+ " order by dt_compliance";
				ResultSetMap rsmProcesso = new ResultSetMap(connCompliance.prepareStatement(sql).executeQuery());
				if(rsmProcesso.getLines().size()>1) {
					count++;
					System.out.println("Agenda "+rsmAgenda.getInt("cd_agenda_item")+" com processo alterado.");
					
					AgendaItem agenda = AgendaItemDAO.get(rsmAgenda.getInt("cd_agenda_item"), connBase);
					
					rsmProcesso.next();
					agenda.setCdProcesso(rsmProcesso.getInt("cd_processo"));
					
					retorno = AgendaItemDAO.update(agenda, connBase);
					
					if(retorno<0) {
						Conexao.rollback(connBase);
						return new Result(retorno, "Erro ao atualizar agenda");
					}
				}
			}
			
			if(retorno<0)
				Conexao.rollback(connBase);
			else
				connBase.commit();
			
			System.out.println(count+" agendas alteradas.");
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connBase);
			return null;
		}
		finally {
			Conexao.desconectar(connBase);
		}
	}
	
	public static Result fixAlunosCrecheInfantil() {
		return fixAlunosCrecheInfantil(null);
	}
	public static Result fixAlunosCrecheInfantil(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			int retorno = 0;
			
			int qtDivergente2Anos = 0;
			int qtDivergente3Anos = 0;
			int qtDivergente4Anos = 0;
			int qtDivergente5Anos = 0;
			int qtNaoEncontradoNovasTurmas = 0;
			String encontrados = "";
			String naoEncontrados = "";
			
			ResultSetMap rsmMatriculaCrecheInfantil = new ResultSetMap(connection.prepareStatement("select A.cd_matricula, A.cd_curso, B.dt_nascimento, C.nm_pessoa AS nm_aluno, D.nm_turma, E.nm_produto_servico AS nm_curso, F.nm_pessoa AS nm_instituicao, D.cd_instituicao from acd_matricula A, grl_pessoa_fisica B, grl_pessoa C, acd_turma D, grl_produto_servico E, grl_pessoa F WHERE A.cd_aluno = B.cd_pessoa AND A.cd_aluno = C.cd_pessoa AND A.cd_turma = D.cd_turma AND A.cd_curso = E.cd_produto_servico AND D.cd_instituicao = F.cd_pessoa AND A.cd_periodo_letivo > 1347 AND A.st_matricula IN (0, 4, 5) and A.cd_curso IN (1179, 1147, 1159, 1181) ORDER BY F.nm_pessoa, E.nm_produto_servico, C.nm_pessoa").executeQuery());
			while(rsmMatriculaCrecheInfantil.next()){
				String analise = "";
				boolean encontrado = true;
				int idade = Util.getIdade(rsmMatriculaCrecheInfantil.getGregorianCalendar("dt_nascimento"), 31, 2, 2017);
				Matricula matricula = MatriculaDAO.get(rsmMatriculaCrecheInfantil.getInt("cd_matricula"),  connection);
				if(rsmMatriculaCrecheInfantil.getInt("cd_curso") == 1179 && idade != 2){
					qtDivergente2Anos++;
					analise += "Escola: " + rsmMatriculaCrecheInfantil.getString("nm_instituicao") + "\n";
					analise += "Nome: " + rsmMatriculaCrecheInfantil.getString("nm_aluno") + ", Data de Nascimento: " + Util.convCalendarStringCompleto(rsmMatriculaCrecheInfantil.getGregorianCalendar("dt_nascimento")).substring(0, 10) + " ("+idade+" anos)\n";
					analise += "Turma Antiga: " + rsmMatriculaCrecheInfantil.getString("nm_curso") + " - " + rsmMatriculaCrecheInfantil.getString("nm_turma") + "\n";
					if(idade == 3){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1147 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1147 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else if(idade == 4){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1181 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1181 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else if(idade == 5){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1159 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1159 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else{
						analise += "Nï¿½o encontrado Modalidade" + "\n";
						encontrado = false;
						qtNaoEncontradoNovasTurmas++;
					}
					
				}
				else if(rsmMatriculaCrecheInfantil.getInt("cd_curso") == 1147 && idade != 3){
					qtDivergente3Anos++;
					analise += "Escola: " + rsmMatriculaCrecheInfantil.getString("nm_instituicao") + "\n";
					analise += "Nome: " + rsmMatriculaCrecheInfantil.getString("nm_aluno") + ", Data de Nascimento: " + Util.convCalendarStringCompleto(rsmMatriculaCrecheInfantil.getGregorianCalendar("dt_nascimento")).substring(0, 10) + " ("+idade+" anos)\n";
					analise += "Turma Antiga: " + rsmMatriculaCrecheInfantil.getString("nm_curso") + " - " + rsmMatriculaCrecheInfantil.getString("nm_turma") + "\n";
					if(idade == 2){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1179 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1179 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					
					else if(idade == 3){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1147 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1147 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else if(idade == 4){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1181 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1181 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else if(idade == 5){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1159 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1159 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else{
						analise += "Nï¿½o encontrado Modalidade" + "\n";
						encontrado = false;
						qtNaoEncontradoNovasTurmas++;
					}
					
				}
				else if(rsmMatriculaCrecheInfantil.getInt("cd_curso") == 1181 && idade != 4){
					qtDivergente4Anos++;
					analise += "Escola: " + rsmMatriculaCrecheInfantil.getString("nm_instituicao") + "\n";
					analise += "Nome: " + rsmMatriculaCrecheInfantil.getString("nm_aluno") + ", Data de Nascimento: " + Util.convCalendarStringCompleto(rsmMatriculaCrecheInfantil.getGregorianCalendar("dt_nascimento")).substring(0, 10) + " ("+idade+" anos)\n";
					analise += "Turma Antiga: " + rsmMatriculaCrecheInfantil.getString("nm_curso") + " - " + rsmMatriculaCrecheInfantil.getString("nm_turma") + "\n";
					if(idade == 2){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1179 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1179 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					
					else if(idade == 3){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1147 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1147 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else if(idade == 4){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1181 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1181 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else if(idade == 5){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1159 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1159 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else{
						analise += "Nï¿½o encontrado Modalidade" + "\n";
						encontrado = false;
						qtNaoEncontradoNovasTurmas++;
					}
					
				}
				else if(rsmMatriculaCrecheInfantil.getInt("cd_curso") == 1159 && idade != 5){
					qtDivergente5Anos++;
					analise += "Escola: " + rsmMatriculaCrecheInfantil.getString("nm_instituicao") + "\n";
					analise += "Nome: " + rsmMatriculaCrecheInfantil.getString("nm_aluno") + ", Data de Nascimento: " + Util.convCalendarStringCompleto(rsmMatriculaCrecheInfantil.getGregorianCalendar("dt_nascimento")).substring(0, 10) + " ("+idade+" anos)\n";
					analise += "Turma Antiga: " + rsmMatriculaCrecheInfantil.getString("nm_curso") + " - " + rsmMatriculaCrecheInfantil.getString("nm_turma") + "\n";
					if(idade == 2){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1179 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1179 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					
					else if(idade == 3){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1147 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1147 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else if(idade == 4){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1181 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1181 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else if(idade == 5){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1159 AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso = 1159 AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else if(idade == 6){
						ResultSetMap rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso IN (1149, 1161) AND nm_turma = '"+rsmMatriculaCrecheInfantil.getString("nm_turma")+"' AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao")).executeQuery());
						if(rsmBuscaTurma.next()){
							Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
							if(result.getCode() < 0){
								if(isConnectionNull)
									Conexao.rollback(connection);
								return result;
							}
							analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
						}
						else{
							rsmBuscaTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_curso IN (1149, 1161) AND cd_periodo_letivo > 1347 AND st_turma = " + TurmaServices.ST_ATIVO + " AND cd_instituicao = " + rsmMatriculaCrecheInfantil.getInt("cd_instituicao") + " ORDER BY cd_turma").executeQuery());
							if(rsmBuscaTurma.next()){
								Result result = MatriculaServices.saveRealocacao(matricula, 1, matricula.getCdPeriodoLetivo(), rsmBuscaTurma.getInt("cd_curso"), rsmBuscaTurma.getInt("cd_turma"), connection);
								if(result.getCode() < 0){
									if(isConnectionNull)
										Conexao.rollback(connection);
									return result;
								}
								analise += "Nova Turma: " + CursoDAO.get(rsmBuscaTurma.getInt("cd_curso"), connection).getNmProdutoServico() + " - " + rsmBuscaTurma.getString("nm_turma") + "\n";
							}
							else{
								analise += "Nï¿½o encontrado Turma" + "\n";
								encontrado = false;
								qtNaoEncontradoNovasTurmas++;
							}
						}
					}
					else{
						analise += "Nï¿½o encontrado Modalidade" + "\n";
						encontrado = false;
						qtNaoEncontradoNovasTurmas++;
					}
				}
				
				if(encontrado){
					encontrados += (analise.equals("") ? "" : analise + "\n");
				}
				else{
					naoEncontrados += analise + "\n";
				}
					
					
			}
			rsmMatriculaCrecheInfantil.beforeFirst();
			
			System.out.println("Divergente 2 anos = " + qtDivergente2Anos);
			System.out.println("Divergente 3 anos = " + qtDivergente3Anos);
			System.out.println("Divergente 4 anos = " + qtDivergente4Anos);
			System.out.println("Divergente 5 anos = " + qtDivergente5Anos);
			System.out.println("Divergente Total = " + (qtDivergente2Anos + qtDivergente3Anos + qtDivergente4Anos + qtDivergente5Anos));
			
			System.out.println("Nï¿½o encontrado nova turma = " + qtNaoEncontradoNovasTurmas);
			
			System.out.println("Encontrados:\n"+encontrados + "\n");
			System.out.println("Nï¿½o Encontrados:\n"+naoEncontrados + "\n");
			
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result importarCirculo(){
		String nmUrlDatabase = "jdbc:postgresql://127.0.0.1/edf_13122016";
		String nmLoginDataBase = "postgres";
		String nmSenhaDatBase = "CDF3DP!";
		
		return importarCirculo(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
	}
	
	public static Result importarCirculo(String nmUrlDatabase, String nmLoginDataBase, String nmSenhaDatBase){
		//Faz a conexao com o Local
		Connection connectLocal = Conexao.conectar();
		try{
			connectLocal.setAutoCommit(false);
			

			Connection connectServer = Conexao.conectar(nmUrlDatabase, nmLoginDataBase, nmSenhaDatBase);
			
			try{
				
				connectServer.setAutoCommit(false);
				
				ResultSetMap rsmServer = new ResultSetMap(connectServer.prepareStatement("SELECT * FROM acd_circulo").executeQuery());
				while(rsmServer.next()){
					Circulo circulo = CirculoDAO.get(rsmServer.getInt("cd_circulo"), connectServer);
					circulo.setCdCirculo(CirculoDAO.insert(circulo, connectLocal));
					if(circulo.getCdCirculo() < 0){
						Conexao.rollback(connectLocal);
						Conexao.rollback(connectServer);
						return new Result(-1);
					}
					
					ResultSetMap rsmInstituicaoCirculo =  new ResultSetMap(connectServer.prepareStatement("SELECT * FROM acd_instituicao_circulo WHERE cd_circulo = " + rsmServer.getInt("cd_circulo")).executeQuery());
					while(rsmInstituicaoCirculo.next()){
						InstituicaoCirculo instituicaoCirculo = new InstituicaoCirculo(circulo.getCdCirculo(), rsmInstituicaoCirculo.getInt("cd_instituicao"));
						if(InstituicaoCirculoDAO.insert(instituicaoCirculo, connectLocal) < 0){
							Conexao.rollback(connectLocal);
							Conexao.rollback(connectServer);
							return new Result(-1);
						}
					}
				}
				
				System.out.println("final 2");
				//Da commit em ambos
				connectLocal.commit();
				connectServer.commit();
			
				return new Result(1, "Importaï¿½ï¿½o realizada");
			}
			catch(Exception e){
				Conexao.rollback(connectLocal);
				Conexao.rollback(connectServer);
				//registra log de erro quando a classe ï¿½ utilizad pelo pdv
				Util.registerLog(e);
				e.printStackTrace();
				return new Result(-1, "Erro na importaï¿½ï¿½o");
			}
			finally{
				Conexao.desconectar(connectServer);
			}
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			Conexao.rollback(connectLocal);
			return new Result(-1, "Erro na sincronizaï¿½ï¿½o");
		}
		finally{
			Conexao.desconectar(connectLocal);
		}
	}
	
	public static Result fixOcorrenciaCancelado() {
		return fixOcorrenciaCancelado(null);
	}
	public static Result fixOcorrenciaCancelado(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			int retorno = 0;
			
			ResultSetMap rsmOcorrenciaMatricula = new ResultSetMap(connection.prepareStatement("select * from grl_ocorrencia A, acd_ocorrencia_matricula B where A.cd_ocorrencia = B.cd_ocorrencia AND cd_tipo_ocorrencia = 22 and (txt_ocorrencia like '%FALECEU%' OR txt_ocorrencia like '%Faleceu%' OR txt_ocorrencia like '%faleceu%' OR txt_ocorrencia like '%MORREU%' OR txt_ocorrencia like '%Morreu%' OR txt_ocorrencia like '%morreu%' OR txt_ocorrencia like '%MORTO%' OR txt_ocorrencia like '%Morto%' OR txt_ocorrencia like '%morto%' OR txt_ocorrencia like '%OBITO%' OR txt_ocorrencia like '%Obito%' OR txt_ocorrencia like '%obito%' OR txt_ocorrencia like '%ï¿½BITO%' OR txt_ocorrencia like '%ï¿½bito%' OR txt_ocorrencia like '%ï¿½bito%' OR txt_ocorrencia like '%FALECIMENTO%' OR txt_ocorrencia like '%Falecimento%' OR txt_ocorrencia like '%falecimento%') and st_matricula_origem <> 4 and st_matricula_origem <> 5").executeQuery());
			while(rsmOcorrenciaMatricula.next()){
				
				Matricula matricula = MatriculaDAO.get(rsmOcorrenciaMatricula.getInt("cd_matricula_origem"), connection);
				
				int stMatriculaOrigem = matricula.getStMatricula();
				
				matricula.setStMatricula(MatriculaServices.ST_FALECIDO);
				matricula.setStAlunoCenso(MatriculaServices.ST_ALUNO_CENSO_FALECIDO);
				int ret = MatriculaDAO.update(matricula, connection); 
				if(ret <= 0){
					return new Result(-1, "Erro ao atualizar matrï¿½cula");
				}
				
				
				int cdTipoOcorrenciaFalecido = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FALECIDO, connection).getCdTipoOcorrencia();
				if(cdTipoOcorrenciaFalecido > 0){
					Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connection);
					Turma turma = TurmaDAO.get(matricula.getCdTurma(), connection);
					
					OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " considerado falecido", Util.getDataAtual(), cdTipoOcorrenciaFalecido, OcorrenciaServices.ST_CONCLUIDO, 0, 1, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, 1);
					if(OcorrenciaMatriculaServices.save(ocorrencia, connection).getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao registrar ocorrencia de falecido do aluno");
					}
				}
			}
			rsmOcorrenciaMatricula.beforeFirst();
			
			rsmOcorrenciaMatricula = new ResultSetMap(connection.prepareStatement("select * from grl_ocorrencia A, acd_ocorrencia_matricula B where A.cd_ocorrencia = B.cd_ocorrencia AND cd_tipo_ocorrencia = 22 and (txt_ocorrencia like '%DESISTENCIA%' OR txt_ocorrencia like '%Desistencia%' OR txt_ocorrencia like '%desistencia%' OR txt_ocorrencia like '%DESISTï¿½NCIA%' OR txt_ocorrencia like '%Desistï¿½ncia%' OR txt_ocorrencia like '%desistï¿½ncia%' OR txt_ocorrencia like '%DESISTENTE%' OR txt_ocorrencia like '%Desistente%' OR txt_ocorrencia like '%desistente%')and st_matricula_origem <> 4 and st_matricula_origem <> 5").executeQuery());
			while(rsmOcorrenciaMatricula.next()){
				Matricula matricula = MatriculaDAO.get(rsmOcorrenciaMatricula.getInt("cd_matricula_origem"), connection);
				
				int stMatriculaOrigem = matricula.getStMatricula();
				
				matricula.setStMatricula(MatriculaServices.ST_DESISTENTE);
				matricula.setStAlunoCenso(MatriculaServices.ST_ALUNO_CENSO_DESISTENTE);
				int ret = MatriculaDAO.update(matricula, connection); 
				if(ret <= 0){
					return new Result(-1, "Erro ao atualizar matrï¿½cula");
				}
				
				
				int cdTipoOcorrenciaDesistente = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_DESISTENTE, connection).getCdTipoOcorrencia();
				if(cdTipoOcorrenciaDesistente > 0){
					Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connection);
					Turma turma = TurmaDAO.get(matricula.getCdTurma(), connection);
					
					OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " considerado desistente", Util.getDataAtual(), cdTipoOcorrenciaDesistente, OcorrenciaServices.ST_CONCLUIDO, 0, 1, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, 1);
					if(OcorrenciaMatriculaServices.save(ocorrencia, connection).getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao registrar ocorrencia de desistencia de matrï¿½cula");
					}
				}
			}
			rsmOcorrenciaMatricula.beforeFirst();
			
			
			rsmOcorrenciaMatricula = new ResultSetMap(connection.prepareStatement("select * from grl_ocorrencia A, acd_ocorrencia_matricula B where A.cd_ocorrencia = B.cd_ocorrencia AND cd_tipo_ocorrencia = 22 and (txt_ocorrencia like '%EVADIDO%' OR txt_ocorrencia like '%Evadido%' OR txt_ocorrencia like '%evadido%' OR txt_ocorrencia like '%EVADIU%' OR txt_ocorrencia like '%Evadiu%' OR txt_ocorrencia like '%evadiu%')and st_matricula_origem <> 4 and st_matricula_origem <> 5").executeQuery());
			while(rsmOcorrenciaMatricula.next()){
				Matricula matricula = MatriculaDAO.get(rsmOcorrenciaMatricula.getInt("cd_matricula_origem"), connection);
				
				int stMatriculaOrigem = matricula.getStMatricula();
				
				matricula.setStMatricula(MatriculaServices.ST_EVADIDO);
				matricula.setStAlunoCenso(MatriculaServices.ST_ALUNO_CENSO_EVADIDO);
				int ret = MatriculaDAO.update(matricula, connection); 
				if(ret <= 0){
					return new Result(-1, "Erro ao atualizar matrï¿½cula");
				}
				
				
				int cdTipoOcorrenciaEvasao = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_EVADIDO, connection).getCdTipoOcorrencia();
				if(cdTipoOcorrenciaEvasao > 0){
					Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connection);
					Turma turma = TurmaDAO.get(matricula.getCdTurma(), connection);
					
					OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " considerado evadido", Util.getDataAtual(), cdTipoOcorrenciaEvasao, OcorrenciaServices.ST_CONCLUIDO, 0, 1, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, 1);
					if(OcorrenciaMatriculaServices.save(ocorrencia, connection).getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao registrar ocorrencia de evasï¿½o de matrï¿½cula");
					}
				}
			}
			rsmOcorrenciaMatricula.beforeFirst();
			
			rsmOcorrenciaMatricula = new ResultSetMap(connection.prepareStatement("select * from grl_ocorrencia A, acd_ocorrencia_matricula B where A.cd_ocorrencia = B.cd_ocorrencia AND cd_tipo_ocorrencia = 22 and (txt_ocorrencia like '%TRANSFERIDO%' OR txt_ocorrencia like '%Transferido%' OR txt_ocorrencia like '%transferido%' OR txt_ocorrencia like '%TRANSFERENCIA%' OR txt_ocorrencia like '%Transferencia%' OR txt_ocorrencia like '%transferencia%' OR txt_ocorrencia like '%TRANSFERï¿½NCIA%' OR txt_ocorrencia like '%Transferï¿½ncia%' OR txt_ocorrencia like '%transferï¿½ncia%')and st_matricula_origem <> 4").executeQuery());
			while(rsmOcorrenciaMatricula.next()){
				Matricula matricula = MatriculaDAO.get(rsmOcorrenciaMatricula.getInt("cd_matricula_origem"), connection);
				
				int stMatriculaOrigem = matricula.getStMatricula();
				
				matricula.setStMatricula(MatriculaServices.ST_TRANSFERIDO);
				matricula.setStAlunoCenso(MatriculaServices.ST_ALUNO_CENSO_TRANSFERIDO);
				int ret = MatriculaDAO.update(matricula, connection); 
				if(ret <= 0){
					return new Result(-1, "Erro ao atualizar matrï¿½cula");
				}
				
				
				int cdTipoOcorrenciaEvasao = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_TRANSFERENCIA, connection).getCdTipoOcorrencia();
				if(cdTipoOcorrenciaEvasao > 0){
					Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connection);
					Turma turma = TurmaDAO.get(matricula.getCdTurma(), connection);
					
					OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, matricula.getCdAluno(), "Aluno " + aluno.getNmPessoa() + " transferido", Util.getDataAtual(), cdTipoOcorrenciaEvasao, OcorrenciaServices.ST_CONCLUIDO, 0, 1, matricula.getCdMatricula(), matricula.getCdMatricula(), turma.getCdTurma(), turma.getCdTurma(), stMatriculaOrigem, 1);
					if(OcorrenciaMatriculaServices.save(ocorrencia, connection).getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao registrar ocorrencia de transferï¿½ncia de matrï¿½cula");
					}
				}
			}
			rsmOcorrenciaMatricula.beforeFirst();
			
			
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Migra os dados de PTC_TIPO_DOCUMENTO para 
	 * GPN_TIPO_DOCUMENTO, mantendo os cï¿½digos
	 * @return
	 */
	public static Result fixPtcToGpnTipoDocumento() {
		return fixPtcToGpnTipoDocumento(null);
	}
	@SuppressWarnings("resource")
	public static Result fixPtcToGpnTipoDocumento(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			int retorno = 0;
			PreparedStatement pstmt = null;
			
			//renomear tabela antiga
			pstmt = connection.prepareStatement("ALTER TABLE ptc_tipo_documento RENAME TO ptc_tipo_documento_2");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-2, "Erro ao renomear ptc_tipo_documento");
			}
			
			//criar nova ptc_tipo_documento
			pstmt = connection.prepareStatement(
					"CREATE TABLE PTC_TIPO_DOCUMENTO"
					+ "("
					+ "	cd_tipo_documento     INTEGER  NOT NULL "
					+ ");"
					+ " CREATE UNIQUE INDEX XPKPTC_TIPO_DOCUMENTO ON PTC_TIPO_DOCUMENTO (cd_tipo_documento);"
					+ " ALTER TABLE PTC_TIPO_DOCUMENTO ADD  PRIMARY KEY (cd_tipo_documento);"
					+ " ALTER TABLE PTC_TIPO_DOCUMENTO ADD  FOREIGN KEY (cd_tipo_documento) REFERENCES GPN_TIPO_DOCUMENTO(cd_tipo_documento);"
				);
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-2, "Erro ao criar ptc_tipo_documento");
			}
			
			/*
			 * PTC
			 */
			pstmt = connection.prepareStatement(
					"SELECT * FROM ptc_tipo_documento_2");
			ResultSetMap rsmOld = new ResultSetMap(pstmt.executeQuery());
			while(rsmOld.next()) {
				System.out.print(".");
				pstmt = connection.prepareStatement(
						"INSERT INTO gpn_tipo_documento (cd_tipo_documento,"+
                        "nm_tipo_documento,"+
                        "id_tipo_documento,"+
                        "st_tipo_documento,"+
                        "cd_empresa,"+
                        "cd_setor,"+
                        "cd_formulario,"+
                        "tp_numeracao,"+
                        "id_prefixo_numeracao,"+
                        "ds_mascara_numeracao,"+
                        "nr_ultima_numeracao,"+
                        "cd_tipo_documento_superior,"+
                        "lg_numeracao_superior,"+
                        "nr_externo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				
				pstmt.setInt(1, rsmOld.getInt("cd_tipo_documento"));
				pstmt.setString(2,rsmOld.getString("nm_tipo_documento"));
				pstmt.setString(3,rsmOld.getString("id_tipo_documento"));
				pstmt.setInt(4,rsmOld.getInt("st_tipo_documento"));
				if(rsmOld.getInt("cd_empresa")==0)
					pstmt.setNull(5, Types.INTEGER);
				else
					pstmt.setInt(5,rsmOld.getInt("cd_empresa"));
				if(rsmOld.getInt("cd_setor")==0)
					pstmt.setNull(6, Types.INTEGER);
				else
					pstmt.setInt(6,rsmOld.getInt("cd_setor"));
				if(rsmOld.getInt("cd_formulario")==0)
					pstmt.setNull(7, Types.INTEGER);
				else
					pstmt.setInt(7,rsmOld.getInt("cd_formulario"));
				pstmt.setInt(8,rsmOld.getInt("tp_numeracao"));
				pstmt.setString(9,rsmOld.getString("id_prefixo_numeracao"));
				pstmt.setString(10,rsmOld.getString("ds_mascara_numeracao"));
				pstmt.setInt(11,rsmOld.getInt("nr_ultima_numeracao"));
				if(rsmOld.getInt("cd_tipo_documento_superior")==0)
					pstmt.setNull(12, Types.INTEGER);
				else
					pstmt.setInt(12,rsmOld.getInt("cd_tipo_documento_superior"));
				pstmt.setInt(13,rsmOld.getInt("lg_numeracao_superior"));
				pstmt.setString(14,rsmOld.getString("nr_tipo_documento_externo"));
				retorno = pstmt.executeUpdate();
				
				if(retorno<0) {
					connection.rollback();
					return new Result(-2, "Erro ao salvar gpn_tipo_documento");
				}
				
				pstmt = connection.prepareStatement("INSERT INTO ptc_tipo_documento (cd_tipo_documento)"+
                        " VALUES (?)");
				pstmt.setInt(1, rsmOld.getInt("cd_tipo_documento"));
				retorno = pstmt.executeUpdate();
				
				if(retorno<0) {
					connection.rollback();
					return new Result(-3, "Erro ao salvar ptc_tipo_documento");
				}
			}
			
			// ATUALIZAR grl_generator
			int cdTipoDocumento = 0;
			pstmt = connection.prepareStatement("SELECT max(cd_tipo_documento) AS codigo FROM gpn_tipo_documento");
			ResultSetMap rsmAux = new ResultSetMap(pstmt.executeQuery());
			if(rsmAux.next()) {
				cdTipoDocumento = rsmAux.getInt("codigo");
			}
			
			if(connection.prepareStatement("SELECT * FROM grl_generator WHERE nm_generator='gpn_tipo_documento'").executeQuery().next()) {
				pstmt = connection.prepareStatement("UPDATE grl_generator SET cd_generator=? WHERE nm_generator='gpn_tipo_documento'");
				pstmt.setInt(1, cdTipoDocumento);
			}
			else {
				pstmt = connection.prepareStatement("INSERT INTO grl_generator(nm_generator, cd_generator)"
						+ " VALUES ('gpn_tipo_documento', ?)");
				pstmt.setInt(1, cdTipoDocumento);
			}
			retorno = pstmt.executeUpdate();
			
			if(retorno<0) {
				connection.rollback();
				return new Result(-4, "Erro ao atualizar grl_generator");
			}
			
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Migra os dados de PRC_TIPO_DOCUMENTO para 
	 * GPN_TIPO_DOCUMENTO, mantendo os cï¿½digos
	 * @return
	 */
	public static Result fixPrcToGpnTipoDocumento() {
		return fixPrcToGpnTipoDocumento(null);
	}
	public static Result fixPrcToGpnTipoDocumento(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			int retorno = 0;
			PreparedStatement pstmt = null;
			
			//renomear tabela antiga
			pstmt = connection.prepareStatement("ALTER TABLE prc_tipo_documento RENAME TO prc_tipo_documento_2");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-2, "Erro ao renomear prc_tipo_documento");
			}
			System.out.println("tabela renomeada");
			
			//criar nova prc_tipo_documento
			pstmt = connection.prepareStatement(
					"CREATE TABLE PRC_TIPO_DOCUMENTO"
					+ "("
					+ "	cd_tipo_documento     INTEGER  NOT NULL ,"
					+ "	cd_tipo_andamento     INTEGER  "
					+ ");"
					+ " CREATE UNIQUE INDEX XPKPRC_TIPO_DOCUMENTO ON PRC_TIPO_DOCUMENTO (cd_tipo_documento);"
					+ " ALTER TABLE PRC_TIPO_DOCUMENTO ADD  PRIMARY KEY (cd_tipo_documento);"
					+ " ALTER TABLE PRC_TIPO_DOCUMENTO ADD  FOREIGN KEY (cd_tipo_andamento) REFERENCES PRC_TIPO_ANDAMENTO(cd_tipo_andamento);"
					+ " ALTER TABLE PRC_TIPO_DOCUMENTO ADD  FOREIGN KEY (cd_tipo_documento) REFERENCES GPN_TIPO_DOCUMENTO(cd_tipo_documento);"
				);
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-2, "Erro ao criar prc_tipo_documento");
			}
			System.out.println("tabela criada");
			
			/*
			 * CAMPOS PARA MIGRAï¿½ï¿½O
			 */
			//grl_modelo_documento
			pstmt = connection.prepareStatement("ALTER TABLE grl_modelo_documento ADD cd_tipo_documento_gpn INTEGER,"
					+ " ADD  FOREIGN KEY (cd_tipo_documento_gpn) REFERENCES gpn_tipo_documento(cd_tipo_documento)");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-2, "Erro ao criar cd_tipo_documento_gpn em grl_modelo_documento");
			}
			System.out.println("campo em grl_modelo_documento criado");
			
			//prc_processo_arquivo
			pstmt = connection.prepareStatement("ALTER TABLE prc_processo_arquivo ADD cd_tipo_documento_gpn INTEGER,"
					+ " ADD  FOREIGN KEY (cd_tipo_documento_gpn) REFERENCES gpn_tipo_documento(cd_tipo_documento)");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-2, "Erro ao criar cd_tipo_documento_gpn em prc_processo_arquivo");
			}
			System.out.println("campo em prc_processo_arquivo criado");
			
			//prc_tipo_prazo
			pstmt = connection.prepareStatement("ALTER TABLE prc_tipo_prazo ADD cd_tipo_documento_cumprimento_gpn INTEGER,"
					+ " ADD  FOREIGN KEY (cd_tipo_documento_cumprimento_gpn) REFERENCES gpn_tipo_documento(cd_tipo_documento)");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-2, "Erro ao criar cd_tipo_documento_gpn em prc_tipo_prazo");
			}
			System.out.println("campo em prc_tipo_prazo criado");
			
			if(retorno>=0) {
				connection.commit();
				Conexao.desconectar(connection);
			}
			
			connection = Conexao.conectar();
			connection.setAutoCommit(false);
			
			pstmt = connection.prepareStatement(
					"SELECT * FROM prc_tipo_documento_2");
			ResultSetMap rsmOld = new ResultSetMap(pstmt.executeQuery());
			while(rsmOld.next()) {
				int cdTipoDocumentoOld = rsmOld.getInt("cd_tipo_documento");
				int cdTipoDocumento = Conexao.getSequenceCode("gpn_tipo_documento", connection);;
				
				System.out.println("cdTipoDocumentoOld: "+cdTipoDocumentoOld);
				
				pstmt = connection.prepareStatement(
						"INSERT INTO gpn_tipo_documento (cd_tipo_documento,"+
                        "nm_tipo_documento,"+
                        "id_tipo_documento) VALUES (?, ?, ?)");
				
				pstmt.setInt(1, cdTipoDocumento);
				pstmt.setString(2,rsmOld.getString("nm_tipo_documento"));
				pstmt.setString(3,rsmOld.getString("id_tipo_documento"));
				
				retorno = pstmt.executeUpdate();
				
				if(retorno<0) {
					connection.rollback();
					return new Result(-2, "Erro ao salvar gpn_tipo_documento");
				}
				
				pstmt = connection.prepareStatement("INSERT INTO prc_tipo_documento(cd_tipo_documento, cd_tipo_andamento)"
						+ " VALUES(?, ?)");
				pstmt.setInt(1, cdTipoDocumento);
				if(rsmOld.getInt("cd_tipo_andamento")==0)
					pstmt.setNull(2, Types.INTEGER);
				else
					pstmt.setInt(2, rsmOld.getInt("cd_tipo_andamento"));
				retorno = pstmt.executeUpdate();
				if(retorno<0) {
					connection.rollback();
					return new Result(-2, "Erro ao criar cd_tipo_documento_gpn em prc_tipo_prazo");
				}
								
				//grl_modelo_documento
				pstmt = connection.prepareStatement("UPDATE grl_modelo_documento SET cd_tipo_documento_gpn=?"
												  + " WHERE cd_tipo_documento=?");
				pstmt.setInt(1, cdTipoDocumento);
				pstmt.setInt(2, cdTipoDocumentoOld);
				retorno = pstmt.executeUpdate();
				if(retorno<0) {
					connection.rollback();
					return new Result(-3, "Erro ao atualizar grl_modelo_documento");
				}
				System.out.println("campo em grl_modelo_documento atualizado");
				
				//prc_processo_arquivo
				pstmt = connection.prepareStatement("UPDATE prc_processo_arquivo SET cd_tipo_documento_gpn=?"
						  + " WHERE cd_tipo_documento=?");
				pstmt.setInt(1, cdTipoDocumento);
				pstmt.setInt(2, cdTipoDocumentoOld);
				retorno = pstmt.executeUpdate();
				if(retorno<0) {
					connection.rollback();
					return new Result(-3, "Erro ao atualizar prc_processo_arquivo");
				}
				System.out.println("campo em prc_processo_arquivo atualizado");
				
				//prc_tipo_prazo
				pstmt = connection.prepareStatement("UPDATE prc_tipo_prazo SET cd_tipo_documento_cumprimento_gpn=?"
						  + " WHERE cd_tipo_documento_cumprimento=?");
				pstmt.setInt(1, cdTipoDocumento);
				pstmt.setInt(2, cdTipoDocumentoOld);
				retorno = pstmt.executeUpdate();
				if(retorno<0) {
					connection.rollback();
					return new Result(-3, "Erro ao atualizar prc_tipo_prazo");
				}
				System.out.println("campo em prc_tipo_prazo atualizado");
				
			}
			
			/*
			 * DROP campos antigos
			 */
			//grl_modelo_documento
			pstmt = connection.prepareStatement("ALTER TABLE grl_modelo_documento DROP cd_tipo_documento");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-3, "Erro ao atualizar grl_modelo_documento");
			}
			pstmt = connection.prepareStatement("ALTER TABLE grl_modelo_documento RENAME cd_tipo_documento_gpn TO cd_tipo_documento");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-3, "Erro ao atualizar grl_modelo_documento");
			}
			System.out.println("campo em grl_modelo_documento excuido");
			
			//prc_processo_arquivo
			pstmt = connection.prepareStatement("ALTER TABLE prc_processo_arquivo DROP cd_tipo_documento");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-3, "Erro ao atualizar prc_processo_arquivo");
			}
			pstmt = connection.prepareStatement("ALTER TABLE prc_processo_arquivo RENAME cd_tipo_documento_gpn TO cd_tipo_documento");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-3, "Erro ao atualizar prc_processo_arquivo");
			}
			System.out.println("campo em prc_processo_arquivo excuido");
			
			//prc_tipo_prazo
			pstmt = connection.prepareStatement("ALTER TABLE prc_tipo_prazo DROP cd_tipo_documento_cumprimento");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-3, "Erro ao atualizar prc_tipo_prazo");
			}
			pstmt = connection.prepareStatement("ALTER TABLE prc_tipo_prazo RENAME cd_tipo_documento_cumprimento_gpn TO cd_tipo_documento_cumprimento");
			retorno = pstmt.executeUpdate();
			if(retorno<0) {
				connection.rollback();
				return new Result(-3, "Erro ao atualizar prc_tipo_prazo");
			}
			System.out.println("campo em prc_tipo_prazo excuido");
			
			//======
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	

	
	public static Result fixSituacaoAlunoCenso() {
		return fixSituacaoAlunoCenso(null);
	}
	public static Result fixSituacaoAlunoCenso(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			int retorno = 0;
			
			ResultSetMap rsmMatricula = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_matricula WHERE ST_MATRICULA IN ("+MatriculaServices.ST_DESISTENTE+", "+MatriculaServices.ST_TRANSFERIDO+", "+MatriculaServices.ST_EM_TRANSFERENCIA+", "+MatriculaServices.ST_EVADIDO+", "+MatriculaServices.ST_FALECIDO+")").executeQuery());
			while(rsmMatricula.next()){
				Matricula matricula = MatriculaDAO.get(rsmMatricula.getInt("cd_matricula"), connection);
				
				switch(matricula.getStMatricula()){
				
					case MatriculaServices.ST_DESISTENTE:
						matricula.setStAlunoCenso(MatriculaServices.ST_ALUNO_CENSO_DESISTENTE);
					break;
					
					case MatriculaServices.ST_TRANSFERIDO:
					case MatriculaServices.ST_EM_TRANSFERENCIA:
						matricula.setStAlunoCenso(MatriculaServices.ST_ALUNO_CENSO_TRANSFERIDO);
					break;
						
					case MatriculaServices.ST_EVADIDO:
						matricula.setStAlunoCenso(MatriculaServices.ST_ALUNO_CENSO_EVADIDO);
					break;	
					
					case MatriculaServices.ST_FALECIDO:
						matricula.setStAlunoCenso(MatriculaServices.ST_ALUNO_CENSO_FALECIDO);
					break;	
				
				}
				
				if(MatriculaDAO.update(matricula, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1);
				}
			}
			rsmMatricula.beforeFirst();
			
			
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixVinculoInstituicao() {
		return fixVinculoInstituicao(null);
	}
	public static Result fixVinculoInstituicao(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			int cdEmpresa = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0, 0, connection);
			if(cdEmpresa<=0) {
				connection.rollback();
				return new Result(-2, "cdEmpresa nï¿½o encontrado");
			}
			
			int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_INSTITUICAO_ACADEMICA", 0, 0, connection);
			if(cdVinculo<=0) {
				connection.rollback();
				return new Result(-2, "cdVinculo nï¿½o encontrado");
			}
			
			ResultSetMap rsmInstituicao = InstituicaoServices.getAllAtivas();
			Result result = null;
			while(rsmInstituicao.next()) {
				result = PessoaServices.addVinculo(rsmInstituicao.getInt("cd_instituicao"), cdEmpresa, cdVinculo, connection);
				
				if(result.getCode()<=0) {
					connection.rollback();
					return new Result(-1, "Erro ao incluir vï¿½nculo");
				}
			}
			
			
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	
	
	public static Result fixEducacensoPeriodo() {
		return fixEducacensoPeriodo(null);
	}
	public static Result fixEducacensoPeriodo(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_pkey; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_abastecimento_agua_fkey; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_abastecimento_agua_fkey1; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_abastecimento_agua_fkey2; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_abastecimento_agua_fkey3; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_abastecimento_agua_fkey4; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_abastecimento_agua_fkey5; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_instituicao_fkey1; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_instituicao_fkey2; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_instituicao_fkey3; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_instituicao_fkey4; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_agua_cd_instituicao_fkey5; ").executeUpdate();
			
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_energia_pkey; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_en_cd_abastecimento_energia_fkey1; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_en_cd_abastecimento_energia_fkey2; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_en_cd_abastecimento_energia_fkey3; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_en_cd_abastecimento_energia_fkey4; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_en_cd_abastecimento_energia_fkey5; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_ene_cd_abastecimento_energia_fkey; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_energia_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_energia_cd_instituicao_fkey1; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_energia_cd_instituicao_fkey2; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_energia_cd_instituicao_fkey3; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_energia_cd_instituicao_fkey4; " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  DROP CONSTRAINT acd_instituicao_abastecimento_energia_cd_instituicao_fkey5; ").executeUpdate();

			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_compartilhamento " +
										"  DROP CONSTRAINT acd_instituicao_compartilhamento_pkey; " +
										"ALTER TABLE public.acd_instituicao_compartilhamento " +
										"  DROP CONSTRAINT acd_instituicao_compartilhamento_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_compartilhamento " +
										"  DROP CONSTRAINT acd_instituicao_compartilhamento_cd_instituicao_fkey1; ").executeUpdate();
			
			
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey1; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey10; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey11; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey12; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey13; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey14; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey15; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey16; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey17; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey18; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey2; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey3; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey4; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey5; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey6; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey7; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey8; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey9; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey1; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey10; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey11; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey12; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey13; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey14; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey15; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey16; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey17; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey18; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey2; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey3; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey4; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey5; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey6; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey7; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey8; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  DROP CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey9;").executeUpdate();

			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_pkey; " + 
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_destinacao_lixo_fkey; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_destinacao_lixo_fkey1; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_destinacao_lixo_fkey2; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_destinacao_lixo_fkey3; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_destinacao_lixo_fkey4; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_destinacao_lixo_fkey5; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_instituicao_fkey1; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_instituicao_fkey2; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_instituicao_fkey3; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_instituicao_fkey4; " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  DROP CONSTRAINT acd_instituicao_destinacao_lixo_cd_instituicao_fkey5;").executeUpdate();
			
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_pkey; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_esgoto_sanitario_fkey; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_esgoto_sanitario_fkey1; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_esgoto_sanitario_fkey2; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_esgoto_sanitario_fkey3; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_esgoto_sanitario_fkey4; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_esgoto_sanitario_fkey5; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_instituicao_fkey1; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_instituicao_fkey2; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_instituicao_fkey3; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_instituicao_fkey4; " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  DROP CONSTRAINT acd_instituicao_esgoto_sanitario_cd_instituicao_fkey5;").executeUpdate();
			
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_local_funcionamento " +
										"  DROP CONSTRAINT acd_instituicao_local_funcionamento_pkey; " +
										"ALTER TABLE public.acd_instituicao_local_funcionamento " +
										"  DROP CONSTRAINT acd_instituicao_local_funcionamento_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_local_funcionamento " +
										"  DROP CONSTRAINT acd_instituicao_local_funcionamento_cd_local_funcionamento_fkey;").executeUpdate();
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_tipo_equipamento " +
										"  DROP CONSTRAINT acd_instituicao_tipo_equipamento_pkey; " +
										"ALTER TABLE public.acd_instituicao_tipo_equipamento " +
										"  DROP CONSTRAINT acd_instituicao_tipo_equipamento_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_tipo_equipamento " +
										"  DROP CONSTRAINT acd_instituicao_tipo_equipamento_cd_tipo_equipamento_fkey;").executeUpdate();




			connection.prepareStatement("ALTER TABLE public.acd_instituicao_tipo_mantenedora " +
										"  DROP CONSTRAINT acd_instituicao_tipo_mantenedora_pkey; " +
										"ALTER TABLE public.acd_instituicao_tipo_mantenedora " +
										"  DROP CONSTRAINT acd_instituicao_tipo_mantenedora_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_tipo_mantenedora " +
										"  DROP CONSTRAINT acd_instituicao_tipo_mantenedora_cd_tipo_mantenedora_fkey;").executeUpdate();

			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_pkey; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_etapa_fkey; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_etapa_fkey1; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_etapa_fkey2; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_etapa_fkey3; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_etapa_fkey4; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_etapa_fkey5; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_etapa_fkey6; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_etapa_fkey7; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_instituicao_fkey1; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_instituicao_fkey2; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_instituicao_fkey3; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_instituicao_fkey4; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_instituicao_fkey5; " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  DROP CONSTRAINT acd_instituicao_tipo_etapa_cd_instituicao_fkey6;").executeUpdate();

			
			
			connection.prepareStatement("ALTER TABLE public.acd_curso_disciplina " +
										"  DROP CONSTRAINT acd_curso_disciplina_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_curso_disciplina " +
										"  ADD CONSTRAINT acd_curso_disciplina_cd_instituicao_fkey FOREIGN KEY (cd_instituicao) REFERENCES public.acd_instituicao (cd_instituicao) ON UPDATE NO ACTION ON DELETE NO ACTION; " +
										"ALTER TABLE public.acd_curso_disciplina " +
										"  ADD CONSTRAINT acd_curso_disciplina_cd_curso_fkey FOREIGN KEY (cd_curso) REFERENCES public.acd_curso (cd_curso) ON UPDATE NO ACTION ON DELETE NO ACTION;").executeUpdate();

			
			connection.prepareStatement("ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey1; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey10; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey11; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey12; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey13; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey14; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey2; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey3; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey4; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey5; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey6; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey7; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey8; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  DROP CONSTRAINT acd_pre_matricula_cd_instituicao_fkey9; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  ADD CONSTRAINT acd_pre_matricula_cd_instituicao_fkey FOREIGN KEY (cd_instituicao) REFERENCES public.acd_instituicao (cd_instituicao) ON UPDATE NO ACTION ON DELETE NO ACTION; " +
										"ALTER TABLE public.acd_pre_matricula " +
										"  ADD CONSTRAINT acd_pre_matricula_cd_curso_fkey FOREIGN KEY (cd_curso) REFERENCES public.acd_curso (cd_curso) ON UPDATE NO ACTION ON DELETE NO ACTION;").executeUpdate();

			
			
			connection.prepareStatement("ALTER TABLE public.acd_quadro_vagas_curso " +
										"  DROP CONSTRAINT acd_quadro_vagas_curso_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_quadro_vagas_curso " +
										"  ADD CONSTRAINT acd_quadro_vagas_curso_cd_instituicao_fkey FOREIGN KEY (cd_instituicao) REFERENCES public.acd_instituicao (cd_instituicao) ON UPDATE NO ACTION ON DELETE NO ACTION; " +
										"ALTER TABLE public.acd_quadro_vagas_curso " +
										"  ADD CONSTRAINT acd_quadro_vagas_curso_cd_curso_fkey FOREIGN KEY (cd_curso) REFERENCES public.acd_curso (cd_curso) ON UPDATE NO ACTION ON DELETE NO ACTION;").executeUpdate();

			
			connection.prepareStatement("DROP INDEX public.xif1acd_instituicao_curso; " +
										"DROP INDEX public.xif2acd_instituicao_curso; " +
										"DROP INDEX public.xpkacd_instituicao_curso;").executeUpdate();
			
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_pkey; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey1; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey10; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey11; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey12; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey13; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey14; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey15; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey16; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey17; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey18; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey19; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey2; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey3; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey4; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey5; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey6; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey7; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey8; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_curso_fkey9; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey1; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey10; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey11; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey12; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey13; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey14; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey15; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey16; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey17; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey18; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey19; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey2; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey3; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey4; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey5; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey6; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey7; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey8; " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  DROP CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey9;").executeUpdate();


			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey1; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey10; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey11; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey12; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey13; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey14; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey15; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey16; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey17; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey18; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey19; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey2; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey3; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey4; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey5; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey6; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey7; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey8; " +
										"ALTER TABLE public.acd_instituicao_horario " +
										"  DROP CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey9;").executeUpdate();

			
			
			
			
			
			
			connection.prepareStatement("ALTER TABLE acd_instituicao_abastecimento_agua " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_instituicao_abastecimento_energia " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_instituicao_compartilhamento " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_instituicao_dependencia " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_instituicao_destinacao_lixo " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();	
			connection.prepareStatement("ALTER TABLE acd_instituicao_esgoto_sanitario " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_instituicao_local_funcionamento " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_instituicao_tipo_equipamento " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_instituicao_tipo_mantenedora " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_instituicao_tipo_etapa " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_instituicao_curso " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_instituicao_horario " +
										"  ADD COLUMN cd_periodo_letivo INTEGER;").executeUpdate();
			
			
			int x = 0;
			ResultSetMap rsmInstituicaoAbastecimentoAgua = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_abastecimento_agua").executeQuery());
			while(rsmInstituicaoAbastecimentoAgua.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoAbastecimentoAgua.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					if(x == 0){
						connection.prepareStatement("UPDATE acd_instituicao_abastecimento_agua SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoAbastecimentoAgua.getInt("cd_instituicao") + " AND cd_abastecimento_agua = " + rsmInstituicaoAbastecimentoAgua.getInt("cd_abastecimento_agua")).executeUpdate();
					}
					else{
						connection.prepareStatement("INSERT INTO acd_instituicao_abastecimento_agua VALUES ("+rsmInstituicaoAbastecimentoAgua.getInt("cd_instituicao")+", "+rsmInstituicaoAbastecimentoAgua.getInt("cd_abastecimento_agua")+", "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
					}
					x++;
				}
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_abastecimento_agua SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoAbastecimentoAgua.getInt("cd_instituicao")).executeUpdate();
				}
				
			}
			rsmInstituicaoAbastecimentoAgua.beforeFirst();
			
			ResultSetMap rsmInstituicaoAbastecimentoEnergia = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_abastecimento_energia").executeQuery());
			while(rsmInstituicaoAbastecimentoEnergia.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoAbastecimentoEnergia.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					if(x == 0){
						connection.prepareStatement("UPDATE acd_instituicao_abastecimento_energia SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoAbastecimentoEnergia.getInt("cd_instituicao") + " AND cd_abastecimento_energia = " + rsmInstituicaoAbastecimentoEnergia.getInt("cd_abastecimento_energia")).executeUpdate();
					}
					else{
						connection.prepareStatement("INSERT INTO acd_instituicao_abastecimento_energia VALUES ("+rsmInstituicaoAbastecimentoEnergia.getInt("cd_instituicao")+", "+rsmInstituicaoAbastecimentoEnergia.getInt("cd_abastecimento_energia")+", "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
					}
					x++;
				}
				rsmInstituicaoPeriodo.beforeFirst();
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_abastecimento_energia SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoAbastecimentoEnergia.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoAbastecimentoEnergia.beforeFirst();
			
			
			ResultSetMap rsmInstituicaoDependencia = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_dependencia").executeQuery());
			while(rsmInstituicaoDependencia.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoDependencia.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					
					ResultSetMap rsmGenerator = new ResultSetMap(connection.prepareStatement("SELECT * FROM grl_generator WHERE nm_generator = 'acd_instituicao_dependencia'").executeQuery());
					int cdDependencia = 0;
					if(rsmGenerator.next()){
						if(x == 0){
							connection.prepareStatement("UPDATE acd_instituicao_dependencia SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoDependencia.getInt("cd_instituicao") + " AND cd_dependencia = " + rsmInstituicaoDependencia.getInt("cd_dependencia")).executeUpdate();
						}
						else{
							cdDependencia = rsmGenerator.getInt("cd_generator") + 1;
							connection.prepareStatement("INSERT INTO acd_instituicao_dependencia VALUES ("+cdDependencia+", "+rsmInstituicaoDependencia.getInt("cd_instituicao")+", "+rsmInstituicaoDependencia.getInt("cd_tipo_dependencia")+", '"+rsmInstituicaoDependencia.getString("nm_dependencia")+"', '"+(rsmInstituicaoDependencia.getString("txt_dependencia") != null ? rsmInstituicaoDependencia.getString("txt_dependencia").replaceAll("'", "") : null)+"', "+rsmInstituicaoDependencia.getInt("st_dependencia")+", "+rsmInstituicaoDependencia.getInt("lg_permanente")+", "+rsmInstituicaoDependencia.getInt("tp_localizacao")+", "+rsmInstituicaoDependencia.getInt("vl_capacidade")+", "+rsmInstituicaoDependencia.getInt("lg_rampa_acesso")+", '"+rsmInstituicaoDependencia.getString("id_dependencia")+"', "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
							connection.prepareStatement("UPDATE grl_generator SET cd_generator = " + cdDependencia + " WHERE nm_generator = 'acd_instituicao_dependencia'").executeUpdate();
						}
					}
					
					x++;
				}
				rsmInstituicaoPeriodo.beforeFirst();
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_dependencia SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoDependencia.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoDependencia.beforeFirst();
			
			ResultSetMap rsmInstituicaoDestinacaoLixo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_destinacao_lixo").executeQuery());
			while(rsmInstituicaoDestinacaoLixo.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoDestinacaoLixo.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					if(x == 0){
						connection.prepareStatement("UPDATE acd_instituicao_destinacao_lixo SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoDestinacaoLixo.getInt("cd_instituicao") + " AND cd_destinacao_lixo = " + rsmInstituicaoDestinacaoLixo.getInt("cd_destinacao_lixo")).executeUpdate();
					}
					else{
						connection.prepareStatement("INSERT INTO acd_instituicao_destinacao_lixo VALUES ("+rsmInstituicaoDestinacaoLixo.getInt("cd_instituicao")+", "+rsmInstituicaoDestinacaoLixo.getInt("cd_destinacao_lixo")+", "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
					}
					x++;
				}
				rsmInstituicaoPeriodo.beforeFirst();
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_destinacao_lixo SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoDestinacaoLixo.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoDestinacaoLixo.beforeFirst();
			
			ResultSetMap rsmInstituicaoEsgotoSanitario = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_esgoto_sanitario").executeQuery());
			while(rsmInstituicaoEsgotoSanitario.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoEsgotoSanitario.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					if(x == 0){
						connection.prepareStatement("UPDATE acd_instituicao_esgoto_sanitario SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoEsgotoSanitario.getInt("cd_instituicao") + " AND cd_esgoto_sanitario = " + rsmInstituicaoEsgotoSanitario.getInt("cd_esgoto_sanitario")).executeUpdate();
					}
					else{
						connection.prepareStatement("INSERT INTO acd_instituicao_esgoto_sanitario VALUES ("+rsmInstituicaoEsgotoSanitario.getInt("cd_instituicao")+", "+rsmInstituicaoEsgotoSanitario.getInt("cd_esgoto_sanitario")+", "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
					}
					x++;
				}
				rsmInstituicaoPeriodo.beforeFirst();
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_esgoto_sanitario SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoEsgotoSanitario.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoEsgotoSanitario.beforeFirst();
			
			
			ResultSetMap rsmInstituicaoLocalFuncionamento = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_local_funcionamento").executeQuery());
			while(rsmInstituicaoLocalFuncionamento.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoLocalFuncionamento.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					if(x == 0){
						connection.prepareStatement("UPDATE acd_instituicao_local_funcionamento SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoLocalFuncionamento.getInt("cd_instituicao") + " AND cd_local_funcionamento = " + rsmInstituicaoLocalFuncionamento.getInt("cd_local_funcionamento")).executeUpdate();
					}
					else{
						connection.prepareStatement("INSERT INTO acd_instituicao_local_funcionamento VALUES ("+rsmInstituicaoLocalFuncionamento.getInt("cd_instituicao")+", "+rsmInstituicaoLocalFuncionamento.getInt("cd_local_funcionamento")+", "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
					}
					x++;
				}
				rsmInstituicaoPeriodo.beforeFirst();
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_local_funcionamento SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoLocalFuncionamento.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoLocalFuncionamento.beforeFirst();
			
			
			ResultSetMap rsmInstituicaoTipoEquipamento = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_tipo_equipamento").executeQuery());
			while(rsmInstituicaoTipoEquipamento.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoTipoEquipamento.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					if(x == 0){
						connection.prepareStatement("UPDATE acd_instituicao_tipo_equipamento SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoTipoEquipamento.getInt("cd_instituicao") + " AND cd_tipo_equipamento = " + rsmInstituicaoTipoEquipamento.getInt("cd_tipo_equipamento")).executeUpdate();
					}
					else{
						connection.prepareStatement("INSERT INTO acd_instituicao_tipo_equipamento VALUES ("+rsmInstituicaoTipoEquipamento.getInt("cd_instituicao")+", "+rsmInstituicaoTipoEquipamento.getInt("cd_tipo_equipamento")+", "+rsmInstituicaoTipoEquipamento.getInt("qt_equipamento")+", "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
					}
					x++;
				}
				rsmInstituicaoPeriodo.beforeFirst();
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_tipo_equipamento SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoTipoEquipamento.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoTipoEquipamento.beforeFirst();
			
			
			ResultSetMap rsmInstituicaoTipoMantenedora = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_tipo_mantenedora").executeQuery());
			while(rsmInstituicaoTipoMantenedora.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoTipoMantenedora.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					if(x == 0){
						connection.prepareStatement("UPDATE acd_instituicao_tipo_mantenedora SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoTipoMantenedora.getInt("cd_instituicao") + " AND cd_tipo_mantenedora = " + rsmInstituicaoTipoMantenedora.getInt("cd_tipo_mantenedora")).executeUpdate();
					}
					else{
						connection.prepareStatement("INSERT INTO acd_instituicao_tipo_mantenedora VALUES ("+rsmInstituicaoTipoMantenedora.getInt("cd_instituicao")+", "+rsmInstituicaoTipoMantenedora.getInt("cd_tipo_mantenedora")+", "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
					}
					x++;
				}
				rsmInstituicaoPeriodo.beforeFirst();
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_tipo_mantenedora SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoTipoMantenedora.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoTipoMantenedora.beforeFirst();
			
			
			ResultSetMap rsmInstituicaoTipoEtapa = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_tipo_etapa").executeQuery());
			while(rsmInstituicaoTipoEtapa.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoTipoEtapa.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					if(x == 0){
						connection.prepareStatement("UPDATE acd_instituicao_tipo_etapa SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoTipoEtapa.getInt("cd_instituicao") + " AND cd_etapa = " + rsmInstituicaoTipoEtapa.getInt("cd_etapa")).executeUpdate();
					}
					else{
						connection.prepareStatement("INSERT INTO acd_instituicao_tipo_etapa VALUES ("+rsmInstituicaoTipoEtapa.getInt("cd_etapa")+", "+rsmInstituicaoTipoEtapa.getInt("cd_instituicao")+", "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
					}
					x++;
				}
				rsmInstituicaoPeriodo.beforeFirst();
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_tipo_etapa SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoTipoEtapa.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoTipoEtapa.beforeFirst();
			
			
			ResultSetMap rsmInstituicaoCurso = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_curso").executeQuery());
			while(rsmInstituicaoCurso.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoCurso.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					if(x == 0){
						connection.prepareStatement("UPDATE acd_instituicao_curso SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoCurso.getInt("cd_instituicao") + " AND cd_curso = " + rsmInstituicaoCurso.getInt("cd_curso")).executeUpdate();
					}
					else{
						connection.prepareStatement("INSERT INTO acd_instituicao_curso VALUES ("+rsmInstituicaoCurso.getInt("cd_instituicao")+", "+rsmInstituicaoCurso.getInt("cd_curso")+", "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
					}
					x++;
				}
				rsmInstituicaoPeriodo.beforeFirst();
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_curso SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoCurso.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoCurso.beforeFirst();
			
			ResultSetMap rsmInstituicaoHorario = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_horario").executeQuery());
			while(rsmInstituicaoHorario.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoHorario.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					ResultSetMap rsmGenerator = new ResultSetMap(connection.prepareStatement("SELECT * FROM grl_generator WHERE nm_generator = 'acd_instituicao_horario'").executeQuery());
					int cdHorario = 0;
					if(rsmGenerator.next()){
						if(x == 0){
							connection.prepareStatement("UPDATE acd_instituicao_horario SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoHorario.getInt("cd_instituicao") + " AND cd_horario = " + rsmInstituicaoHorario.getInt("cd_horario")).executeUpdate();
						}
						else{
							cdHorario = rsmGenerator.getInt("cd_generator") + 1;
							connection.prepareStatement("INSERT INTO acd_instituicao_horario VALUES ("+cdHorario+", "+rsmInstituicaoHorario.getInt("tp_turno")+", '"+Util.convCalendarStringSqlCompleto(rsmInstituicaoHorario.getGregorianCalendar("hr_inicio"))+"', '"+Util.convCalendarStringSqlCompleto(rsmInstituicaoHorario.getGregorianCalendar("hr_termino"))+"', "+rsmInstituicaoHorario.getInt("nr_dia_semana")+", "+rsmInstituicaoHorario.getInt("cd_instituicao")+", "+rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+")").executeUpdate();
							connection.prepareStatement("UPDATE grl_generator SET cd_generator = " + cdHorario + " WHERE nm_generator = 'acd_instituicao_horario'").executeUpdate();
						}
					}
					x++;
				}
				rsmInstituicaoPeriodo.beforeFirst();
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_horario SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoHorario.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoHorario.beforeFirst();
			
			
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_educacenso   " +
										"  ADD COLUMN cd_periodo_letivo integer;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  ADD COLUMN qt_total_funcionarios integer;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_pkey;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey1;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey10;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey11;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey12;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey13;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey14;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey15;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey16;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey17;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey18;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey19;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey2;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey3;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey4;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey5;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey6;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey7;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey8;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey9;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_lingua_indigena_fkey;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_lingua_indigena_fkey1;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_lingua_indigena_fkey2;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_lingua_indigena_fkey3;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_lingua_indigena_fkey4;   " +
										"ALTER TABLE public.acd_instituicao_educacenso   " +
										"  DROP CONSTRAINT acd_instituicao_educacenso_cd_lingua_indigena_fkey5; " + 
										"DROP INDEX public.xpkacd_instituicao_educacenso;").executeUpdate();
			
			
			
			
			
			ResultSetMap rsmInstituicaoEducacenso= new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_educacenso").executeQuery());
			while(rsmInstituicaoEducacenso.next()){
				x = 0;
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connection.prepareStatement("SELECT cd_periodo_letivo, cd_instituicao FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicaoEducacenso.getInt("cd_instituicao") + " ORDER BY cd_periodo_letivo").executeQuery());
				while(rsmInstituicaoPeriodo.next()){
					if(x == 0){
						connection.prepareStatement("UPDATE acd_instituicao_educacenso SET cd_periodo_letivo = " + rsmInstituicaoPeriodo.getInt("cd_periodo_letivo") + " WHERE cd_instituicao = "+ rsmInstituicaoEducacenso.getInt("cd_instituicao")).executeUpdate();
					}
					else{
						connection.prepareStatement("INSERT INTO acd_instituicao_educacenso VALUES ("+rsmInstituicaoEducacenso.getInt("cd_instituicao")+", "
																									 +"'"+rsmInstituicaoEducacenso.getString("nr_inep")+"'"+", "
																									 +"'"+rsmInstituicaoEducacenso.getString("nr_orgao_regional")+"'"+", "
																									 +"'"+rsmInstituicaoEducacenso.getString("nm_orgao_regional")+"'"+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_dependencia_administrativa")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_localizacao")+", "
																									 +"'"+rsmInstituicaoEducacenso.getString("nr_cpnj_executora")+"'"+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_categoria_privada")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_convenio")+", "
																									 +"'"+rsmInstituicaoEducacenso.getInt("nr_cnas")+"'"+", "
																									 +"'"+rsmInstituicaoEducacenso.getInt("nr_cebas")+"'"+", "
																									 +rsmInstituicaoEducacenso.getInt("st_regulamentacao")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_predio_compartilhado")+", "
																									 +rsmInstituicaoEducacenso.getInt("st_agua_consumida")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_abastecimento_agua")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_fornecimento_energia")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_esgoto_sanitario")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_destino_lixo")+", "
																									 +rsmInstituicaoEducacenso.getInt("qt_sala_aula")+", "
																									 +rsmInstituicaoEducacenso.getInt("qt_sala_aula_externa")+", "
																									 +rsmInstituicaoEducacenso.getInt("qt_computador_administrativo")+", "
																									 +rsmInstituicaoEducacenso.getInt("qt_computador_aluno")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_internet")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_banda_larga")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_alimentacao_escolar")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_atendimento_especializado")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_atividade_complementar")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_modalidade_ensino")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_ensino_fundamental_ciclo")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_educacao_infantil")+", "
																									 +rsmInstituicaoEducacenso.getInt("nr_anos_ensino_fundalmental")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_ensino_medio")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_educacao_jovem_adulto")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_localizacao_diferenciada")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_material_especifico")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_educacao_indigena")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_lingua")+", "
																									 +"'"+rsmInstituicaoEducacenso.getString("nr_lingua_indigena")+"'"+", "
																									 +rsmInstituicaoEducacenso.getInt("st_instituicao_publica")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_material_didatico_indigena")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_material_didatico_quilombola")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_brasil_alfabetizado")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_espaco_comunidade")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_formacao_alternancia")+", "
																									 +rsmInstituicaoEducacenso.getInt("tp_forma_ocupacao")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_modalidade_regular")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_modalidade_especial")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_modalidade_eja")+", "
																									 +(rsmInstituicaoEducacenso.getInt("cd_lingua_indigena") > 0 ? rsmInstituicaoEducacenso.getInt("cd_lingua_indigena") : "null")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_lingua_ministrada_indigena")+", "
																									 +rsmInstituicaoEducacenso.getInt("lg_lingua_ministrada_portuguesa")+", "
																									 +rsmInstituicaoPeriodo.getInt("cd_periodo_letivo")+ ", "
																									 +InstituicaoDAO.get(rsmInstituicaoEducacenso.getInt("cd_instituicao"), connection).getNrFuncionarios()+")").executeUpdate();
						
						
					}					
					x++;
				}
				
				if(rsmInstituicaoPeriodo.size() == 0){
					connection.prepareStatement("UPDATE acd_instituicao_educacenso SET cd_periodo_letivo = 541 WHERE cd_instituicao = "+ rsmInstituicaoEducacenso.getInt("cd_instituicao")).executeUpdate();
				}
			}
			rsmInstituicaoEducacenso.beforeFirst();
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_educacenso " +
										"  ADD CONSTRAINT acd_instituicao_educacenso_pkey PRIMARY KEY (cd_instituicao, cd_periodo_letivo); " +
										"ALTER TABLE public.acd_instituicao_educacenso " +
										"  ADD CONSTRAINT acd_instituicao_educacenso_cd_instituicao_fkey FOREIGN KEY (cd_instituicao) REFERENCES acd_instituicao;" +
										"ALTER TABLE public.acd_instituicao_educacenso " +
										"  ADD CONSTRAINT acd_instituicao_educacenso_cd_periodo_letivo_fkey FOREIGN KEY (cd_periodo_letivo) REFERENCES acd_instituicao_periodo;" +
										"ALTER TABLE public.acd_instituicao_educacenso " +
										"  ADD CONSTRAINT acd_instituicao_educacenso_cd_lingua_indigena_fkey FOREIGN KEY (cd_lingua_indigena) REFERENCES acd_lingua_indigena;").executeUpdate();
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  ADD CONSTRAINT acd_instituicao_abastecimento_agua_pkey PRIMARY KEY (cd_instituicao, cd_abastecimento_agua, cd_periodo_letivo); " +
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  ADD CONSTRAINT acd_instituicao_abastecimento_agua_cd_abastecimento_agua_fkey FOREIGN KEY (cd_abastecimento_agua) REFERENCES acd_abastecimento_agua;"+
										"ALTER TABLE public.acd_instituicao_abastecimento_agua " +
										"  ADD CONSTRAINT acd_instituicao_abastecimento_agua_cd_instituicao_cd_periodo_letivo_fkey FOREIGN KEY (cd_instituicao, cd_periodo_letivo) REFERENCES acd_instituicao_educacenso;").executeUpdate();
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  ADD CONSTRAINT acd_instituicao_abastecimento_energia_pkey PRIMARY KEY (cd_instituicao, cd_abastecimento_energia, cd_periodo_letivo); " +
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  ADD CONSTRAINT acd_instituicao_abastecimento_energia_cd_abastecimento_energia_fkey FOREIGN KEY (cd_abastecimento_energia) REFERENCES acd_abastecimento_energia;"+
										"ALTER TABLE public.acd_instituicao_abastecimento_energia " +
										"  ADD CONSTRAINT acd_instituicao_abastecimento_energia_cd_instituicao_cd_periodo_letivo_fkey FOREIGN KEY (cd_instituicao, cd_periodo_letivo) REFERENCES acd_instituicao_educacenso;").executeUpdate();
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  ADD CONSTRAINT acd_instituicao_esgoto_sanitario_pkey PRIMARY KEY (cd_instituicao, cd_esgoto_sanitario, cd_periodo_letivo); " +
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  ADD CONSTRAINT acd_instituicao_esgoto_sanitario_cd_esgoto_sanitario_fkey FOREIGN KEY (cd_esgoto_sanitario) REFERENCES acd_esgoto_sanitario;"+
										"ALTER TABLE public.acd_instituicao_esgoto_sanitario " +
										"  ADD CONSTRAINT acd_instituicao_esgoto_sanitario_cd_instituicao_cd_periodo_letivo_fkey FOREIGN KEY (cd_instituicao, cd_periodo_letivo) REFERENCES acd_instituicao_educacenso;").executeUpdate();
			
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  ADD CONSTRAINT acd_instituicao_destinacao_lixo_pkey PRIMARY KEY (cd_instituicao, cd_destinacao_lixo, cd_periodo_letivo); " +
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  ADD CONSTRAINT acd_instituicao_destinacao_lixo_cd_destinacao_lixo_fkey FOREIGN KEY (cd_destinacao_lixo) REFERENCES acd_destinacao_lixo;"+
										"ALTER TABLE public.acd_instituicao_destinacao_lixo " +
										"  ADD CONSTRAINT acd_instituicao_destinacao_lixo_cd_instituicao_cd_periodo_letivo_fkey FOREIGN KEY (cd_instituicao, cd_periodo_letivo) REFERENCES acd_instituicao_educacenso;").executeUpdate();
			
			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_dependencia " +
										"  ADD CONSTRAINT acd_instituicao_dependencia_cd_instituicao_fkey FOREIGN KEY (cd_instituicao, cd_periodo_letivo) REFERENCES acd_instituicao_educacenso; " +
										"ALTER TABLE public.acd_instituicao_dependencia " +
										"  ADD CONSTRAINT acd_instituicao_dependencia_cd_tipo_dependencia_fkey FOREIGN KEY (cd_tipo_dependencia) REFERENCES acd_tipo_dependencia;").executeUpdate();

			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_local_funcionamento " +
										"  ADD CONSTRAINT acd_instituicao_local_funcionamento_pkey PRIMARY KEY (cd_instituicao, cd_local_funcionamento, cd_periodo_letivo); " +
										"ALTER TABLE public.acd_instituicao_local_funcionamento " +
										"  ADD CONSTRAINT acd_instituicao_local_funcionamento_cd_local_funcionamento_fkey FOREIGN KEY (cd_local_funcionamento) REFERENCES acd_local_funcionamento;"+
										"ALTER TABLE public.acd_instituicao_local_funcionamento " +
										"  ADD CONSTRAINT acd_instituicao_local_funcionamento_cd_instituicao_cd_periodo_letivo_fkey FOREIGN KEY (cd_instituicao, cd_periodo_letivo) REFERENCES acd_instituicao_educacenso;").executeUpdate();

			
			connection.prepareStatement("ALTER TABLE public.acd_instituicao_tipo_equipamento " +
										"  ADD CONSTRAINT acd_instituicao_tipo_equipamento_pkey PRIMARY KEY (cd_instituicao, cd_tipo_equipamento, cd_periodo_letivo); " +
										"ALTER TABLE public.acd_instituicao_tipo_equipamento " +
										"  ADD CONSTRAINT acd_instituicao_tipo_equipamento_cd_tipo_equipamento_fkey FOREIGN KEY (cd_tipo_equipamento) REFERENCES acd_tipo_equipamento;"+
										"ALTER TABLE public.acd_instituicao_tipo_equipamento " +
										"  ADD CONSTRAINT acd_instituicao_tipo_equipamento_cd_instituicao_cd_periodo_letivo_fkey FOREIGN KEY (cd_instituicao, cd_periodo_letivo) REFERENCES acd_instituicao_educacenso;").executeUpdate();

			connection.prepareStatement("ALTER TABLE public.acd_instituicao_tipo_mantenedora " +
										"  ADD CONSTRAINT acd_instituicao_tipo_mantenedora_pkey PRIMARY KEY (cd_instituicao, cd_tipo_mantenedora, cd_periodo_letivo); " +
										"ALTER TABLE public.acd_instituicao_tipo_mantenedora " +
										"  ADD CONSTRAINT acd_instituicao_tipo_mantenedora_cd_tipo_mantenedora_fkey FOREIGN KEY (cd_tipo_mantenedora) REFERENCES acd_tipo_mantenedora;"+
										"ALTER TABLE public.acd_instituicao_tipo_mantenedora " +
										"  ADD CONSTRAINT acd_instituicao_tipo_mantenedora_cd_instituicao_cd_periodo_letivo_fkey FOREIGN KEY (cd_instituicao, cd_periodo_letivo) REFERENCES acd_instituicao_educacenso;").executeUpdate();

			connection.prepareStatement("ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  ADD CONSTRAINT acd_instituicao_tipo_etapa_pkey PRIMARY KEY (cd_instituicao, cd_etapa, cd_periodo_letivo); " +
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  ADD CONSTRAINT acd_instituicao_tipo_etapa_cd_tipo_etapa_fkey FOREIGN KEY (cd_etapa) REFERENCES acd_tipo_etapa;"+
										"ALTER TABLE public.acd_instituicao_tipo_etapa " +
										"  ADD CONSTRAINT acd_instituicao_tipo_etapa_cd_instituicao_cd_periodo_letivo_fkey FOREIGN KEY (cd_instituicao, cd_periodo_letivo) REFERENCES acd_instituicao_educacenso;").executeUpdate();

			connection.prepareStatement("ALTER TABLE public.acd_instituicao_curso " +
										"  ADD CONSTRAINT acd_instituicao_curso_pkey PRIMARY KEY (cd_instituicao, cd_curso, cd_periodo_letivo); " +
										"ALTER TABLE public.acd_instituicao_curso " +
										"  ADD CONSTRAINT acd_instituicao_curso_cd_curso_fkey FOREIGN KEY (cd_curso) REFERENCES acd_curso;"+
										"ALTER TABLE public.acd_instituicao_curso " +
										"  ADD CONSTRAINT acd_instituicao_curso_cd_instituicao_fkey FOREIGN KEY (cd_instituicao) REFERENCES acd_instituicao;"+
										"ALTER TABLE public.acd_instituicao_curso " +
										"  ADD CONSTRAINT acd_instituicao_curso_cd_periodo_letivo_fkey FOREIGN KEY (cd_periodo_letivo) REFERENCES acd_instituicao_periodo").executeUpdate();

			connection.prepareStatement("ALTER TABLE public.acd_instituicao_horario " +
										"  ADD CONSTRAINT acd_instituicao_horario_cd_instituicao_fkey FOREIGN KEY (cd_instituicao) REFERENCES acd_instituicao;"+
										"ALTER TABLE public.acd_instituicao_horario " +
										"  ADD CONSTRAINT acd_instituicao_horario_cd_periodo_letivo_fkey FOREIGN KEY (cd_periodo_letivo) REFERENCES acd_instituicao_periodo").executeUpdate();


			
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	/**
	 * Migra os dados de PTC_TIPO_DOCUMENTO para 
	 * GPN_TIPO_DOCUMENTO, mantendo os cï¿½digos
	 * @return
	 */
	public static Result fixModalidadeToRecomendacao() {
		return fixModalidadeToRecomendacao(null);
	}
	public static Result fixModalidadeToRecomendacao(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			int retorno = 0;
			PreparedStatement pstmt = null;
			
			pstmt = connection.prepareStatement("SELECT * FROM cae_modalidade_ingrediente");
			ResultSetMap rsmOld = new ResultSetMap(pstmt.executeQuery());
			while(rsmOld.next()) {
				System.out.print(".");
				pstmt = connection.prepareStatement(
						"INSERT INTO cae_ingrediente_recomendacao (cd_ingrediente,"+
                        "cd_recomendacao_nutricional,"+
                        "vl_per_capta) VALUES (?, ?, ?)");
				
				pstmt.setInt(1, rsmOld.getInt("cd_ingrediente"));
				pstmt.setInt(2, rsmOld.getInt("cd_modalidade"));
				pstmt.setDouble(3, rsmOld.getDouble("vl_per_capta"));

				retorno = pstmt.executeUpdate();
				
				if(retorno<0) {
					connection.rollback();
					return new Result(-2, "Erro ao salvar cae_modalidade_ingrediente");
				}
			}
			
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixLocalArmazenamentoInstituicao(){
		return fixLocalArmazenamentoInstituicao(null);
	}
	
	public static Result fixLocalArmazenamentoInstituicao(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			System.out.print("INICIANDO FIX -> fixLocalArmazenamentoInstituicao");

			/* BUSCANDO TODAS AS INSTITUIï¿½ï¿½ES */			
			ResultSetMap rsmInstituicoes = InstituicaoServices.getAll(0, false, true, false, connection);
			
			/* ALTERANDO A QUANTIDADE DE CARACTERES PARA A COLUMA DO NOME DO LOCAL DE ARMAZENAMENTO*/
			
			pstmt = connection.prepareStatement("ALTER TABLE alm_local_armazenamento ALTER COLUMN nm_local_armazenamento TYPE character varying(80)");
			pstmt.execute();
			
			/* INTERANDO SOBRE CADA UMA PARA CRIAR SEUS RESPECTIVOS LOCAIS DE ARMAZENAMENTO */
			while (rsmInstituicoes.next()){

				System.out.println("============================================================================");
				System.out.println("CRIANDO LOCAL DE ARMAZENAMAENTO [" + rsmInstituicoes.getString("NM_INSTITUICAO") + " - PRINCIPAL]");
				
				/* SEPARANDO A PRIMEIRA LETRA DO NOME DA INSTITUICAO PARA USAR COMO ID */
				StringBuilder sb = new StringBuilder();
			    for(String s : rsmInstituicoes.getString("NM_INSTITUICAO").split(" ")){
			    	if(s.length() > 0)
			    		sb.append(s.charAt(0));         
			    }
				
				/* CRIANDO LOCAL DE ARMAZENAMENTO PRINCIPAL DA INSTITUIï¿½ï¿½O */
				LocalArmazenamento local = new LocalArmazenamento();				
				local.setCdEmpresa(rsmInstituicoes.getInt("CD_INSTITUICAO"));
				local.setNmLocalArmazenamento(rsmInstituicoes.getString("NM_INSTITUICAO") + " - PRINCIPAL");
				local.setIdLocalArmazenamento(sb.toString());
				
		        pstmt = connection.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE cd_empresa = ?");
		        pstmt.setInt(1, rsmInstituicoes.getInt("CD_INSTITUICAO"));
				ResultSet rs    = pstmt.executeQuery();
								
				if(!rs.next()){
					int retorno = LocalArmazenamentoDAO.insert(local, connection);
					if(retorno > 0){
						System.out.println("^-----> CRIADO!");
					}
				} else {
					System.out.println("^-----> Jï¿½ POSSUI!");
				}
				
				System.out.println("============================================================================");
			}
			
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("FINALIZADO!");
			
			return new Result(1, "Locais de Armazenamento criados com sucesso!");
		} 
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixPessoaContaBancariaPrincipal(){
		return fixPessoaContaBancariaPrincipal(null);
	}
	
	public static Result fixPessoaContaBancariaPrincipal(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			System.out.println("INICIANDO FIX -> fixPessoaContaBancariaPrincipal");

			//pessoas com conta onde nenhuma ï¿½ principal
			pstmt = connection.prepareStatement(
					  " select A.cd_pessoa from grl_pessoa A "
					+ " join grl_pessoa_conta_bancaria B ON (A.cd_pessoa = B.cd_pessoa) "
					+ " where B.lg_principal <> 1"
					+ " group by A.cd_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()) {
				int cdPessoa = rsm.getInt("cd_pessoa");
				ResultSetMap rsmAux = PessoaContaBancariaServices.getBancoCliente(cdPessoa, connection);
				if(rsmAux.next()) {
					pstmt = connection.prepareStatement("update grl_pessoa_conta_bancaria set lg_principal=1"
							+ " where cd_pessoa=? and cd_conta_bancaria=?");
					pstmt.setInt(1, rsmAux.getInt("cd_pessoa"));
					pstmt.setInt(2, rsmAux.getInt("cd_conta_bancaria"));
					
					int retorno = pstmt.executeUpdate();
					if(retorno<0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-2, "Erro ao atualizar PessoaContaBancaria.");
					}
				}
				System.out.print(".");
			}
			
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO!");
			
			return new Result(1, "fixPessoaContaBancariaPrincipal executado com sucesso!");
		} 
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	

	
	/**
	 * 1. Buscar o csv
	 * 2. carregar o csv no rsm
	 * 3. iterar o rsm por cada processo
	 * 4. inserir a agenda no processo
	 */
//	public static Result fixImportacaoAgenda(int cdResponsavel, int cdTipoPrazo, String dtInicial, String dtFinal){
//		return fixImportacaoAgenda(cdResponsavel, cdTipoPrazo, dtInicial, dtFinal, null);
//	}
	
	public static Result fixImportacaoAgenda(){
		return fixImportacaoAgenda(null);
	}
	
	public static Result fixImportacaoAgenda(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			GregorianCalendar dtInicialObj=Util.convStringToCalendar("20/03/2017 00:00:00");
			GregorianCalendar dtFinalObj=Util.convStringToCalendar("20/03/2017 18:00:00");
			//1./2. csv
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("/tivic/processos.csv", ";", true);
			//3.
			while(rsm.next()){
				//4. buscar processo
				String nrProcesso = rsm.getString("NR_PROCESSO");
				Processo processo = ProcessoServices.getProcessoByNrProcesso(nrProcesso, connection);
				
				if(processo == null)
					continue;
				else {
					AgendaItem agd=new AgendaItem();
					agd.setCdProcesso(processo.getCdProcesso());
					agd.setCdPessoa(28626); //LUA CLARA
					agd.setCdTipoPrazo(131); //"MONITORAR TRï¿½NSITO EM JULGADO"
					agd.setDtInicial(dtInicialObj);
					agd.setDtFinal(dtFinalObj);
					
					Result r= AgendaItemServices.save(agd, connection);
					
					if (r.getCode()>0) {
						AgendaItemOcorrencia aio = new AgendaItemOcorrencia(0, 
								((AgendaItem)r.getObjects().get("AGENDAITEM")).getCdAgendaItem(), 
								ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_INSERT_AGENDA", 0, 0, connection), 
								10414, 
								new GregorianCalendar(), 
								"Cadastro em lote solicitado por LUA CLARA", 
								AgendaItemOcorrenciaServices.TP_PUBLICO, 
								new GregorianCalendar());
						
						Result rAux = AgendaItemOcorrenciaServices.save(aio, connection);
						if(rAux.getCode()<=0) {
							connection.rollback();
							return new Result(-2, "ERRO ao lanï¿½ar ocorrï¿½ncia.");
						}
					}
					else{
						System.out.println("ERRO! no processo "+rsm.getString("NR_PROCESSO"));
						connection.rollback();
						return new Result(-1, "ERRO! no processo "+rsm.getString("NR_PROCESSO"));
					}
				}
				System.out.print(".");
			}
			System.out.println("Importaï¿½ï¿½o realizada com sucesso.");
			
			connection.commit();
			return new Result(1, "Importaï¿½ï¿½o realizda com sucesso.");
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixImportacaoProcesso(){
		return fixImportacaoProcesso(null);
	}
	
	public static Result fixImportacaoProcesso(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			Result r = new Result(0);
			GregorianCalendar dtCadastro=Util.convStringToCalendar("10/08/2021 18:00:00");
			//1./2. csv
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("D:\\Downloads\\importacao_caixa_local.csv", ",", true);
			//3.
			while(rsm.next()){
				
				GregorianCalendar dtRepasse=Util.convStringToCalendar(rsm.getString("DT_REPASSE") + " 00:00:00");
				GregorianCalendar dtDistribuicao=Util.convStringToCalendar(rsm.getString("DT_DISTRIBUICAO") + " 00:00:00");
				
				//4. ler os processos
				Processo prc=new Processo();
				prc.setCdProcesso(0);
				prc.setNrProcesso(rsm.getString("NR_PROCESSO"));
				prc.setCdTribunal(Integer.parseInt(rsm.getString("CD_TRIBUNAL")));
				prc.setCdCidade(Integer.parseInt(rsm.getString("CD_CIDADE")));
				prc.setNrJuizo(rsm.getString("NR_JUIZO"));
				prc.setCdJuizo(Integer.parseInt(rsm.getString("CD_JUIZO")));
				prc.setLgClienteAutor(Integer.parseInt(rsm.getString("LG_CLIENTE_AUTOR")));
				prc.setCdTipoProcesso(Integer.parseInt(rsm.getString("CD_TIPO_PROCESSO")));
				prc.setTpRito(Integer.parseInt(rsm.getString("TP_RITO")));
				prc.setTpAutos(Integer.parseInt(rsm.getString("TP_AUTOS")));
				prc.setDtDistribuicao(dtDistribuicao);
				prc.setDtRepasse(dtRepasse);
				prc.setTpRepasse(Integer.parseInt(rsm.getString("TP_REPASSE")));
				prc.setVlProcesso(Double.parseDouble(rsm.getString("VL_PROCESSO")));
				prc.setCdGrupoTrabalho(Integer.parseInt(rsm.getString("CD_GRUPO_TRABALHO")));
				prc.setCdTipoObjeto(Integer.parseInt(rsm.getString("CD_TIPO_OBJETO")));
				prc.setTpPerda(Integer.parseInt(rsm.getString("TP_PERDA")));
				prc.setTpInstancia(Integer.parseInt(rsm.getString("TP_INSTANCIA")));
				prc.setCdGrupoProcesso(Integer.parseInt(rsm.getString("CD_GRUPO_PROCESSO")));
				prc.setCdSistemaProcesso(Integer.parseInt(rsm.getString("CD_SISTEMA_PROCESSO")));
				prc.setCdTipoSituacao(Integer.parseInt(rsm.getString("CD_TIPO_SITUACAO")));
				prc.setStProcesso(Integer.parseInt(rsm.getString("ST_PROCESSO")));
				prc.setNmConteiner3(rsm.getString("NM_CONTEINER3"));
				prc.setCdAdvogado(28626); ////////////////// LUA CLARA SANTOS SILVA
				prc.setCdUsuarioCadastro(16337); //////////////////// VANUBIA DE ARAUJO MENEZES BITTENCOURT
				prc.setDtCadastro(dtCadastro);
				prc.setTxtObservacao("IMPORTAÇÃO DE CADASTRO SOLICITADA POR VANUBIA MENEZES VIA E-MAIL, EM 04/08/2021 ");
				
				// Parte Cliente - CAIXA
                ResultSetMap parteCliente = new ResultSetMap();
                HashMap<String, Object> register1 = new HashMap<String, Object>();
                register1.put("CD_PESSOA", 46338); ////////// codigo da caixa - coloquei
                parteCliente.addRegister(register1);
                parteCliente.beforeFirst();

                // Outra Parte
                ResultSetMap outraParte = new ResultSetMap();
                HashMap<String, Object> register2 = new HashMap<String, Object>();
                if ((rsm.getString("NR_CPF_CNPJ") != null) && (!rsm.getString("NR_CPF_CNPJ").trim().equals(""))) {
	                ResultSetMap pessoas = PessoaServices.findByCpfCnpj(rsm.getString("NR_CPF_CNPJ"));
	                if(pessoas.getLines().size()>0) {
	                	pessoas.next();
	                    register2.put("CD_PESSOA", Integer.parseInt(pessoas.getString("CD_PESSOA")));
	                    
	                } else {
	                    int retorno = 0;
	                    if (rsm.getString("NR_CPF_CNPJ").length()<=11) {
	                        PessoaFisica pessoaFisica = new PessoaFisica();
	                        pessoaFisica.setNrCpf(rsm.getString("NR_CPF_CNPJ"));
	                        pessoaFisica.setNmPessoa(rsm.getString("NM_OUTRA_PARTE"));
	                        retorno = PessoaFisicaDAO.insert(pessoaFisica);
	                    } else {
	                        PessoaJuridica pessoaJuridica = new PessoaJuridica();
	                        pessoaJuridica.setNrCnpj(rsm.getString("NR_CPF_CNPJ"));
	                        pessoaJuridica.setNmPessoa(rsm.getString("NM_OUTRA_PARTE"));
	                        retorno = PessoaJuridicaDAO.insert(pessoaJuridica);
	                    }
	
	                    if (retorno>0) {
	                        register2.put("CD_PESSOA", retorno);
	                    } else {
	    					connection.rollback();
	                    }
	
	                }
		            outraParte.addRegister(register2);
                }
				
	            // salvar
				r = ProcessoServices.save(prc, parteCliente, outraParte, null, 10414, null, null, null);
				
				if (r.getCode()>0) {
					System.out.println("Processo "+rsm.getString("NR_PROCESSO")+" - ok");
				} else if (r.getCode()==-2){
					System.out.println("Processo existente");
				} else {
					System.out.println("ERRO! no processo "+rsm.getString("NR_PROCESSO"));
					connection.rollback();
					break;
				}
			}
			
			if (r.getCode()>0) {
				System.out.println("Sucesso!");
				connection.commit();
			}
			
			return new Result(1, "Importacao concluída.");
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixAlteracaoProcesso(){
		return fixAlteracaoProcesso(null);
	}
	
	public static Result fixAlteracaoProcesso(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			Result r = new Result(0);
			//1./2. csv
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("D:\\Downloads\\inativacao_processo.csv", ",", true);
			//3.
			while(rsm.next()){
				
//				GregorianCalendar dtRepasse=Util.convStringToCalendar(rsm.getString("DT_REPASSE") + " 00:00:00");
//				GregorianCalendar dtDistribuicao=Util.convStringToCalendar(rsm.getString("DT_DISTRIBUICAO") + " 00:00:00");
				GregorianCalendar dtInativacao=Util.convStringToCalendar(rsm.getString("DT_INATIVACAO") + " 00:00:00");
				
				//4. ler os processos
				Processo prc = new Processo();
				prc = ProcessoServices.getProcessoByNrProcesso(rsm.getString("NR_PROCESSO"));
				System.out.println(".");
				
				if (prc!=null) {
//					prc.setCdTribunal(Integer.parseInt(rsm.getString("CD_TRIBUNAL")));
//					prc.setCdCidade(Integer.parseInt(rsm.getString("CD_CIDADE")));
//					prc.setNrJuizo(rsm.getString("NR_JUIZO"));
//					prc.setCdJuizo(Integer.parseInt(rsm.getString("CD_JUIZO")));
//					prc.setLgClienteAutor(Integer.parseInt(rsm.getString("LG_CLIENTE_AUTOR")));
//					prc.setCdTipoProcesso(Integer.parseInt(rsm.getString("CD_TIPO_PROCESSO")));
//					prc.setTpRito(Integer.parseInt(rsm.getString("TP_RITO")));
//					prc.setTpAutos(Integer.parseInt(rsm.getString("TP_AUTOS")));
//					prc.setDtDistribuicao(dtDistribuicao);
//					prc.setDtRepasse(dtRepasse);
//					prc.setTpRepasse(Integer.parseInt(rsm.getString("TP_REPASSE")));
//					prc.setVlProcesso(Double.parseDouble(rsm.getString("VL_PROCESSO")));
//					prc.setCdGrupoTrabalho(Integer.parseInt(rsm.getString("CD_GRUPO_TRABALHO")));
//					prc.setCdTipoObjeto(Integer.parseInt(rsm.getString("CD_TIPO_OBJETO")));
//					prc.setTpPerda(Integer.parseInt(rsm.getString("TP_PERDA")));
//					prc.setTpInstancia(Integer.parseInt(rsm.getString("TP_INSTANCIA")));
//					prc.setCdGrupoProcesso(Integer.parseInt(rsm.getString("CD_GRUPO_PROCESSO")));
//					prc.setCdSistemaProcesso(Integer.parseInt(rsm.getString("CD_SISTEMA_PROCESSO")));
//					prc.setCdTipoSituacao(Integer.parseInt(rsm.getString("CD_TIPO_SITUACAO")));
					prc.setStProcesso(Integer.parseInt(rsm.getString("ST_PROCESSO")));
//					prc.setNmConteiner3(rsm.getString("NM_CONTEINER3"));
//					prc.setCdAdvogado(28626); ////////////////// LUA CLARA SANTOS SILVA
					prc.setDtInativacao(dtInativacao);
					
		            // salvar
					r = ProcessoServices.save(prc);
				
					if (r.getCode()>0) {
						System.out.println("Processo "+rsm.getString("NR_PROCESSO")+" - ok");
					} else {
						System.out.println("ERRO! no processo "+rsm.getString("NR_PROCESSO"));
						connection.rollback();
						break;
					}
				} else
					System.out.println("Processo não cadastrado "+rsm.getString("NR_PROCESSO"));
			}
			
			if (r.getCode()>0) {
				System.out.println("Sucesso!");
				connection.commit();
			}
			
			return new Result(1, "Importacao concluída.");
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixImportacaoEncerramento(){
		return fixImportacaoEncerramento(null);
	}
	
	public static Result fixImportacaoEncerramento(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			Result r = new Result(0);
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("D:\\Downloads\\teste.csv", ",", true);

			while(rsm.next()){

				String nrProcesso = rsm.getString("NR_PROCESSO");
				Processo processo = ProcessoServices.getProcessoByNrProcesso(nrProcesso, connection);
				
				GregorianCalendar dt = new GregorianCalendar();
				String[] textoSeparado = rsm.getString("DT_SENTENCA").split("/");
				dt.set(Integer.parseInt(textoSeparado[2]), Integer.parseInt(textoSeparado[1])-1, Integer.parseInt(textoSeparado[0]), 0, 0, 0);
				
				if(processo == null) {
					System.out.println("Processo não cadastrado - " + rsm.getString("NR_PROCESSO"));
					continue;
				
				} else {
					ProcessoSentenca processoSentenca = new ProcessoSentenca();
					processoSentenca.setCdProcesso(processo.getCdProcesso());
					processoSentenca.setTpSentenca(Integer.parseInt(rsm.getString("TP_SENTENCA")));
					processoSentenca.setDtSentenca(dt);
					processoSentenca.setVlSentenca(0.0);
					processoSentenca.setVlAcordo(0.0);
					processoSentenca.setVlTotal(0.0);
					r = ProcessoSentencaServices.save(processoSentenca);
					
					System.out.println("Processo "+rsm.getString("NR_PROCESSO")+" - ok");
				}
				
				if (r.getCode()>0) {
					System.out.println("Processo "+rsm.getString("NR_PROCESSO")+" - ok");
				} else {
					System.out.println("ERRO! No cadastro do encerramento "+rsm.getString("NR_PROCESSO"));
					break;
				}
			}
			
			if (r.getCode()>0) {
				System.out.println("Sucesso!");
				connection.commit();
			} else {
				connection.rollback();
				Conexao.rollback(connection);
				Conexao.desconectar(connection);
			}
			
			return new Result(1, "Importacao concluída.");
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result atualizarSituacaoFinalCenso2016()	{
		Connection connect = Conexao.conectar();
		try	{
			
			connect.setAutoCommit(false);
			int x = 1;
			ResultSetMap rsmInstituicoes = InstituicaoServices.getAllAtivas(835);
			while(rsmInstituicoes.next()){
				ResultSetMap rsmInstituicaoPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsmInstituicoes.getInt("cd_instituicao") + " AND nm_periodo_letivo = '2016'").executeQuery());
				if(rsmInstituicaoPeriodo.next()){
					ResultSetMap rsmAlunos = InstituicaoServices.getAlunosOfTurmas(rsmInstituicoes.getInt("cd_instituicao"), rsmInstituicaoPeriodo.getInt("cd_periodo_letivo"));
					while(rsmAlunos.next()){
						Matricula matricula = MatriculaDAO.get(rsmAlunos.getInt("CD_MATRICULA"), connect);
						matricula.setStCensoFinal(MatriculaServices.ST_CENSO_FINAL_LANCADO);
						System.out.println(x++);
						if(MatriculaDAO.update(matricula, connect) < 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar matricula");
						}
					}
				}
			}
			
			connect.commit();
			
			return new Result(1, "Sucesso ao atualizar registros");
					
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result fixUltimaEscola() {
		return fixUltimaEscola(null);
	}
	public static Result fixUltimaEscola(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			int retorno = 0;
			ResultSetMap rsmMatricula = new ResultSetMap(connection.prepareStatement("SELECT A.cd_matricula, C.cd_turma_origem from acd_matricula A "
																					+"	JOIN acd_ocorrencia_matricula C ON (A.cd_matricula = C.cd_matricula_destino) "
																					+"	JOIN grl_ocorrencia B ON (A.cd_aluno = B.cd_pessoa) "
																					+"	WHERE cd_periodo_letivo > 1648 "
																					+"	  AND nm_ultima_escola IS NULL "
																					+"	  AND st_matricula IN (0, 4) "
																					+"	  AND cd_tipo_ocorrencia = 13 "
																					+"	  AND B.cd_ocorrencia = C.cd_ocorrencia").executeQuery());
			while(rsmMatricula.next()){
				Matricula matricula = MatriculaDAO.get(rsmMatricula.getInt("cd_matricula"), connection);
				Turma turmaOrigem = TurmaDAO.get(rsmMatricula.getInt("cd_turma_origem"), connection);
				Instituicao instituicaoOrigem = InstituicaoDAO.get(turmaOrigem.getCdInstituicao(), connection);
				matricula.setNmUltimaEscola(instituicaoOrigem.getNmPessoa());
				if(MatriculaDAO.update(matricula, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1);
				}
			}
			rsmMatricula.beforeFirst();
			
			
			rsmMatricula = new ResultSetMap(connection.prepareStatement("select A.cd_matricula from acd_matricula A, acd_turma AA where A.cd_turma = AA.cd_turma AND A.cd_periodo_letivo > 1648 and nm_ultima_escola is null and st_matricula IN (0, 4) and nr_matricula NOT LIKE '2018%' " 
																		+"	and not exists (select * from grl_ocorrencia B, acd_ocorrencia_matricula C where cd_tipo_ocorrencia = 13 AND B.cd_ocorrencia = C.cd_ocorrencia AND A.cd_aluno = B.cd_pessoa AND A.cd_matricula = C.cd_matricula_destino) "
																		+"	and exists (select * from acd_matricula D, acd_turma E where D.cd_turma = E.cd_turma AND AA.cd_instituicao = E.cd_instituicao AND D.st_matricula = 0 and D.cd_aluno = A.cd_aluno and D.cd_periodo_letivo > 1347 and D.cd_periodo_letivo < 1648) ").executeQuery());
			
			while(rsmMatricula.next()){
				Matricula matricula = MatriculaDAO.get(rsmMatricula.getInt("cd_matricula"), connection);
				Turma turmaOrigem = TurmaDAO.get(matricula.getCdTurma(), connection);
				Instituicao instituicaoOrigem = InstituicaoDAO.get(turmaOrigem.getCdInstituicao(), connection);
				matricula.setNmUltimaEscola(instituicaoOrigem.getNmPessoa());
				if(MatriculaDAO.update(matricula, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1);
				}
			}
			rsmMatricula.beforeFirst();
			
			
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixCancelarAgenda(){
		return fixCancelarAgenda(null);
	}
	
	public static Result fixCancelarAgenda(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			ResultSetMap rsmOrigem = ResultSetMap.getResultsetMapFromCSVFile("/tivic/HabilitacaoCancelamentoDePrazos.csv", ";", true);
			while(rsmOrigem.next()) {
				
				pstmt = connection.prepareStatement(
						" SELECT A.cd_agenda_item FROM agd_agenda_item A"
						+ "	LEFT OUTER JOIN prc_processo B ON (A.cd_processo = B.cd_processo)"
						+ " LEFT OUTER JOIN prc_tipo_prazo C ON (A.cd_tipo_prazo = C.cd_tipo_prazo)"
						+ " WHERE A.dt_inicial = '"+(rsmOrigem.getString("DATA")+" "+rsmOrigem.getString("HORA"))+"'"
						+ " AND A.st_agenda_item = "+AgendaItemServices.ST_AGENDA_A_CUMPRIR
						+ " AND B.nr_processo LIKE '"+rsmOrigem.getString("NR_PROCESSO")+"'"
						+ " AND C.nm_tipo_prazo LIKE '"+rsmOrigem.getString("NM_TIPO_PRAZO").trim()+"'");
				
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				
				if(rsm.next()) {
					AgendaItem agenda = AgendaItemDAO.get(rsm.getInt("cd_agenda_item"), connection);
					agenda.setStAgendaItem(AgendaItemServices.ST_AGENDA_CANCELADO);
					//agenda.setCdPessoa(10414); //RESPONSAVEL
					
					int result = AgendaItemDAO.update(agenda, connection);
					if(result<=0) {
						if(isConnectionNull)
							connection.rollback();
						return new Result(-1, "Erro ao atualizar agenda");
					}
				}
			}
			
			if(isConnectionNull)
				connection.commit();
			
			return new Result(1, "Atualizado com sucesso");
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
	}
	public static Result fixAlterarResponsavelAgenda(){
		return fixAlterarResponsavelAgenda(null);
	}
	
	public static Result fixAlterarResponsavelAgenda(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			ResultSetMap rsmOrigem = ResultSetMap.getResultsetMapFromCSVFile("/tivic/HabilitacaoAlteracaoDeResponsavel.csv", ";", true);
			while(rsmOrigem.next()) {
				
				pstmt = connection.prepareStatement(
						" SELECT A.cd_agenda_item FROM agd_agenda_item A"
						+ "	LEFT OUTER JOIN prc_processo B ON (A.cd_processo = B.cd_processo)"
						+ " LEFT OUTER JOIN prc_tipo_prazo C ON (A.cd_tipo_prazo = C.cd_tipo_prazo)"
						+ " WHERE A.dt_inicial = '"+(rsmOrigem.getString("DATA")+" "+rsmOrigem.getString("HORA"))+"'"
						+ " AND A.st_agenda_item = "+AgendaItemServices.ST_AGENDA_A_CUMPRIR
						+ " AND B.nr_processo LIKE '"+rsmOrigem.getString("NR_PROCESSO")+"'"
						+ " AND C.nm_tipo_prazo LIKE '"+rsmOrigem.getString("NM_TIPO_PRAZO").trim()+"'");
				
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				
				if(rsm.next()) {
					AgendaItem agenda = AgendaItemDAO.get(rsm.getInt("cd_agenda_item"), connection);
					//agenda.setStAgendaItem(AgendaItemServices.ST_AGENDA_CANCELADO);
					agenda.setCdPessoa(53195); //RESPONSAVEL
					
					int result = AgendaItemDAO.update(agenda, connection);
					if(result<=0) {
						if(isConnectionNull)
							connection.rollback();
						return new Result(-1, "Erro ao atualizar agenda");
					}
				}
			}
			
			if(isConnectionNull)
				connection.commit();
			
			return new Result(1, "Atualizado com sucesso");
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
	}
	
	
	public static Result fixHorarioTurma() {
		return fixHorarioTurma(null);
	}
	public static Result fixHorarioTurma(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			connection.prepareStatement("ALTER TABLE public.acd_turma_horario " +
										"  DROP CONSTRAINT acd_turma_horario_cd_horario_fkey;").executeUpdate();

			
			connection.prepareStatement("ALTER TABLE acd_turma_horario " +
										"  ADD COLUMN tp_turno SMALLINT;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_turma_horario " +
										"  ADD COLUMN hr_inicio TIMESTAMP WITHOUT TIME ZONE;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_turma_horario " +
										"  ADD COLUMN hr_termino TIMESTAMP WITHOUT TIME ZONE;").executeUpdate();
			connection.prepareStatement("ALTER TABLE acd_turma_horario " +
										"  ADD COLUMN nr_dia_semana SMALLINT;").executeUpdate();
			
			int x = 0;
			ResultSetMap rsmHorarioTurma = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma_horario").executeQuery());
			while(rsmHorarioTurma.next()){
				x = 0;
				InstituicaoHorario instituicaoHorario = InstituicaoHorarioDAO.get(rsmHorarioTurma.getInt("cd_horario"), connection);
				
				connection.prepareStatement("UPDATE acd_turma_horario SET tp_turno = " + instituicaoHorario.getTpTurno() + ", hr_inicio = '"+Util.convCalendarStringSqlCompleto(instituicaoHorario.getHrInicio())+"', hr_termino = '"+Util.convCalendarStringSqlCompleto(instituicaoHorario.getHrTermino())+"', nr_dia_semana = "+instituicaoHorario.getNrDiaSemana()+" WHERE cd_horario = "+ rsmHorarioTurma.getInt("cd_horario") + " AND cd_turma = " + rsmHorarioTurma.getInt("cd_turma")).executeUpdate();
			}
			rsmHorarioTurma.beforeFirst();
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixPermissaoBISecretarios() {
		return fixPermissaoBISecretarios(null);
	}
	public static Result fixPermissaoBISecretarios(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			int x = 0;
			ResultSetMap rsmSecretarios = new ResultSetMap(connection.prepareStatement("select cd_usuario from seg_usuario where cd_usuario in (select cd_usuario from seg_usuario_modulo_empresa where cd_modulo = 9)").executeQuery());
			while(rsmSecretarios.next()){
				UsuarioModulo usuarioModulo = UsuarioModuloDAO.get(rsmSecretarios.getInt("cd_usuario"), 27, 1, connection);
				if(usuarioModulo == null){
					connection.prepareStatement("insert into seg_usuario_modulo VALUES ("+rsmSecretarios.getInt("cd_usuario")+", 27, 1, 1);").executeUpdate();
					connection.prepareStatement("insert into seg_usuario_modulo_empresa VALUES ("+rsmSecretarios.getInt("cd_usuario")+", 2, 27, 1);").executeUpdate();
					x++;
				}
			}
			rsmSecretarios.beforeFirst();
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. " + x + " usuï¿½rios cadastrados no BI");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixPrcSentenca() {
		return fixPrcSentenca(null);
	}
	public static Result fixPrcSentenca(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			ResultSetMap rsm = ProcessoDAO.getAll();
			while(rsm.next()) {
				if(rsm.getInt("tp_sentenca")>=0) {
					Result result = ProcessoSentencaServices.save(
							new ProcessoSentenca(0, rsm.getInt("cd_processo"), rsm.getInt("tp_sentenca"), 
									rsm.getGregorianCalendar("dt_sentenca"), rsm.getDouble("vl_sentenca"), 
									rsm.getDouble("vl_acordo"), rsm.getDouble("vl_total")), 
							null, connection);
					
					if(result.getCode()<=0) {
						if(isConnectionNull)
							Conexao.rollback(connection);
						return result;
					}
				}
			}
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixGrlIndicador() {
		return fixGrlIndicador(null);
	}
	public static Result fixGrlIndicador(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			GregorianCalendar dtInicio = new GregorianCalendar(2003, 1, 1);
			GregorianCalendar dtFinal = new GregorianCalendar(2017, 12, 1);
			
			Result result = null;
			
			while(dtInicio.compareTo(dtFinal)<=0) {
				
				int cdInpc = 3;
				int cdIgpdi = 1;
				int cdSalMinimo = 2;
				
				if(IndicadorVariacaoDAO.get(cdInpc, dtInicio, connection)==null) {
					IndicadorVariacao iInpc = new IndicadorVariacao(cdInpc, dtInicio, new Double(0.32).floatValue());
					result = IndicadorVariacaoServices.save(iInpc, connection);
					if(result.getCode()<=0) {
						if(isConnectionNull)
							connection.rollback();
						return result;
					}
				}
				
				if(IndicadorVariacaoDAO.get(cdIgpdi, dtInicio, connection)==null) {
					IndicadorVariacao iIgpdi = new IndicadorVariacao(cdIgpdi, dtInicio, new Double(0.41).floatValue());
					result = IndicadorVariacaoServices.save(iIgpdi, connection);
					if(result.getCode()<=0) {
						if(isConnectionNull)
							connection.rollback();
						return result;
					}
				}
				
				if(IndicadorVariacaoDAO.get(cdSalMinimo, dtInicio, connection)==null) {
					IndicadorVariacao iSalMinimo = new IndicadorVariacao(cdSalMinimo, dtInicio, new Double(937).floatValue());
					result = IndicadorVariacaoServices.save(iSalMinimo, connection);
					if(result.getCode()<=0) {
						if(isConnectionNull)
							connection.rollback();
						return result;
					}
				}
				
				//verificar se existe variacao para o indicador no perï¿½odo
				//se nï¿½o existir, criar.
				
				dtInicio.add(Calendar.MONTH, 1);
			}
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result jurisFixDeletePessoas() {
		return jurisFixDeletePessoas(null);
	}
	public static Result jurisFixDeletePessoas(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			int cdVinculo = 2;
			ResultSetMap rsmPessoas;
			System.out.println("INICIANDO...\n");
			
			switch (cdVinculo) { 
			case 1: // Clientes
				rsmPessoas = new ResultSetMap(connection.prepareStatement("SELECT cd_pessoa from grl_pessoa_empresa A WHERE cd_vinculo = 1 " +
						"AND NOT EXISTS (select cd_pessoa from prc_parte_cliente C where A.cd_pessoa = C.cd_pessoa) " +
						"AND NOT EXISTS (select cd_pessoa from adm_conta_pagar E where A.cd_pessoa = E.cd_pessoa) " +
						"AND NOT EXISTS (select cd_pessoa from prc_outra_parte D where A.cd_pessoa = D.cd_pessoa) " +
						"AND NOT EXISTS (select cd_pessoa from prc_processo F where A.cd_pessoa = F.cd_advogado_contrario) " +
						"AND NOT EXISTS (select cd_pessoa from adm_conta_receber K where A.cd_pessoa = K.cd_pessoa)").executeQuery());
				break;
				
			case 2: // Parte contraria
				rsmPessoas = new ResultSetMap(connection.prepareStatement("SELECT cd_pessoa from grl_pessoa_empresa A WHERE cd_vinculo = 2 " +
						"AND NOT EXISTS (select cd_pessoa from prc_outra_parte D where A.cd_pessoa = D.cd_pessoa) " +
						"AND NOT EXISTS (select cd_pessoa from prc_parte_cliente C where A.cd_pessoa = C.cd_pessoa) " +
						"AND NOT EXISTS (select cd_pessoa from adm_conta_pagar E where A.cd_pessoa = E.cd_pessoa) " +
						"AND NOT EXISTS (select cd_pessoa from prc_processo F where A.cd_pessoa = F.cd_advogado_contrario) " +
						"AND NOT EXISTS (select cd_pessoa from prc_contrato H where A.cd_pessoa = H.cd_devedor)").executeQuery());
				break;
				
			case 4: // Advogados
				rsmPessoas = new ResultSetMap(connection.prepareStatement("SELECT cd_pessoa from grl_pessoa_empresa A WHERE cd_vinculo = 4 " +
						"AND NOT EXISTS (select cd_advogado_contrario from prc_processo B where A.cd_pessoa = B.cd_advogado_contrario) " +
						"AND NOT EXISTS (select cd_pessoa from prc_orgao C where A.cd_pessoa = C.cd_pessoa) " +
						"AND NOT EXISTS (select cd_pessoa from prc_outra_parte D where A.cd_pessoa = D.cd_pessoa) " +
						"AND NOT EXISTS (select cd_pessoa from adm_conta_pagar E where A.cd_pessoa = E.cd_pessoa)").executeQuery());
				break;

			default: // Pessoa sem vinculo
				rsmPessoas = new ResultSetMap(connection.prepareStatement("SELECT cd_pessoa FROM grl_pessoa A WHERE" +
						" NOT EXISTS (select cd_pessoa from grl_pessoa_empresa B where A.cd_pessoa = B.cd_pessoa)" +
						" AND NOT EXISTS (select cd_pessoa from seg_usuario B where A.cd_pessoa = B.cd_pessoa)" +
						" AND NOT EXISTS (select cd_pessoa from agd_agenda_item C where A.cd_pessoa = C.cd_pessoa)" +
						" AND NOT EXISTS (select cd_pessoa_juridica from prc_tribunal D where A.cd_pessoa = D.cd_pessoa_juridica)" +
						" AND NOT EXISTS (select cd_pessoa from prc_tribunal E where A.cd_pessoa = E.cd_pessoa)" +
						" AND NOT EXISTS (select cd_pessoa from prc_processo F where A.cd_pessoa = F.cd_advogado_contrario)" +
						" AND NOT EXISTS (select cd_pessoa from grl_agencia G where A.cd_pessoa = G.cd_agencia)" +
						" AND NOT EXISTS (select cd_pessoa from prc_contrato H where A.cd_pessoa = H.cd_devedor)" +
						" AND NOT EXISTS (select cd_pessoa from prc_outra_parte I where A.cd_pessoa = I.cd_pessoa)" +
						" AND NOT EXISTS (select cd_pessoa from adm_conta_pagar J where A.cd_pessoa = J.cd_pessoa)" +
						" AND NOT EXISTS (select cd_pessoa from adm_conta_receber K where A.cd_pessoa = K.cd_pessoa)" +
						" AND NOT EXISTS (select cd_pessoa from grl_pessoa_conta_bancaria L where A.cd_pessoa = L.cd_pessoa)" +
						" AND NOT EXISTS (select cd_pessoa from prc_parte_cliente M where A.cd_pessoa = M.cd_pessoa)" +
						" AND NOT EXISTS (select cd_pessoa from prc_processo_financeiro N where A.cd_pessoa = N.cd_pessoa)").executeQuery());
				break;
			}
			
			int retorno = 0;
			PreparedStatement pstmtDelete[] = {
					connection.prepareStatement("delete from grl_pessoa_fisica where cd_pessoa=?"),
					connection.prepareStatement("delete from grl_pessoa_juridica where cd_pessoa=?"),
					connection.prepareStatement("delete from grl_pessoa_endereco where cd_pessoa=?"),
					connection.prepareStatement("delete from grl_pessoa_empresa where cd_pessoa=?"),
					connection.prepareStatement("delete from grl_pessoa where cd_pessoa=?")
			};
			
			while(rsmPessoas.next()) {
				
				PreparedStatement pstmtAux = null;
				for(int j=0; j<pstmtDelete.length; j++) {
					
					pstmtAux = pstmtDelete[j];
					pstmtAux.setInt(1, rsmPessoas.getInt("CD_PESSOA"));
					retorno = pstmtAux.executeUpdate();
					
					if(retorno<0) {
						Conexao.rollback(connection);
						return new Result(-2, "Erro ao deletar pessoa = " + rsmPessoas.getInt("CD_PESSOA"));
					} else {
						System.out.println("CD_PESSOA = " + rsmPessoas.getInt("CD_PESSOA"));
					}
				}	
			}
			
			if(retorno<0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixMobAitTransporte() {
		return fixMobAitTransporte(null);
	}
	public static Result fixMobAitTransporte(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			String sql= " SELECT A.* FROM mob_ait_transporte  A " +
						" LEFT OUTER JOIN mob_talonario B ON (A.cd_talao = B.cd_talao) " +
						" LEFT OUTER JOIN mob_agente    C ON (A.cd_agente = C.cd_agente) " +
						" WHERE A.cd_agente <> B.cd_agente " +
						"   AND C.tp_agente =  " + AgenteServices.TP_TRANSPORTE+
						" ORDER BY cd_ait ";
			
			ResultSetMap rsm01 = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			int cont = 0;
			while( rsm01.next()){
				AitTransporte aitTransporte = AitTransporteDAO.get(rsm01.getInt("cd_ait"));
				sql = " SELECT * FROM mob_talonario "+
					  " WHERE " + rsm01.getString("nr_ait") + " BETWEEN nr_inicial AND nr_final "+
					  "   AND (tp_talao = "+TalonarioAITServices.TP_TALONARIO_ELETRONICO_TRANSPORTE+
					      " OR tp_talao = "+TalonarioAITServices.TP_TALONARIO_TRANSPORTE+") ";
				ResultSetMap rsmTalao = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
				if(rsmTalao.next()){
					aitTransporte.setCdTalao(rsmTalao.getInt("cd_talao"));
					AitTransporteDAO.update(aitTransporte);
					cont++;
				}else{
					System.out.println(rsm01.getString("nr_ait") + " talï¿½o nï¿½o encontrado");
				}
					
			}	
			System.out.println(cont + " aits atualizados - 01");
			System.out.println("Segunda Parte...\n");

			sql= " SELECT A.* FROM mob_ait_transporte  A " +
				" LEFT OUTER JOIN mob_talonario B ON (A.cd_talao = B.cd_talao) " +
				" LEFT OUTER JOIN mob_agente    C ON (A.cd_agente = C.cd_agente) " +
				" WHERE A.cd_talao IS NULL " +
				"   AND A.nr_ait NOT LIKE 'B%'"+
				"   AND C.tp_agente =  " + AgenteServices.TP_TRANSPORTE+ 
				"   AND A.dt_infracao > \'01-01-2014 00:00:00\'"+
				" ORDER BY cd_ait ";
			
			ResultSetMap rsm02 = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			while( rsm02.next()){
				AitTransporte aitTransporte = AitTransporteDAO.get(rsm02.getInt("cd_ait"));
				sql = " SELECT * FROM mob_talonario "+
					  " WHERE " + rsm02.getString("nr_ait") + " BETWEEN nr_inicial AND nr_final "+
					  "   AND (tp_talao = "+TalonarioAITServices.TP_TALONARIO_ELETRONICO_TRANSPORTE+
					  "     OR tp_talao = "+TalonarioAITServices.TP_TALONARIO_TRANSPORTE+") ";
				ResultSetMap rsmTalao = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
				if(rsmTalao.next()){
					aitTransporte.setCdTalao(rsmTalao.getInt("cd_talao"));
					AitTransporteDAO.update(aitTransporte);
					cont++;
				}else{
					System.out.println(rsm02.getString("nr_ait") + " talï¿½o nï¿½o encontrado");
				}
					
			}	
			
			System.out.println(cont + " aits atualizados - 02");
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixOrdTecnicos() {
		return fixOrdTecnicos(null);
	}
	public static Result fixOrdTecnicos(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			String sql= " SELECT A.cd_ordem_servico, A.cd_tecnico_responsavel "
					+ "	FROM ord_ordem_servico A ";
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			int cont = 0;
			
			while(rsm.next()) {
				if(rsm.getObject("cd_tecnico_responsavel")==null || rsm.getInt("cd_tecnico_responsavel")==0)
					continue;
				
				OrdemServicoTecnico ost = new OrdemServicoTecnico(rsm.getInt("cd_ordem_servico"), rsm.getInt("cd_tecnico_responsavel"), 1);
				Result r = OrdemServicoTecnicoServices.save(ost, null, connection);
				if(r.getCode()<0) {
					if(isConnectionNull)
						connection.rollback();
					return r;
				}
				cont++;
				System.out.print(".");
			}
			System.out.println();
			
			System.out.println(cont + " OS atualizadas.");
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixMobLacreCatraca() {
		return fixMobLacreCatraca(null);
	}
	/***
	 * Corrigindo as aferiï¿½ï¿½es que nï¿½o tiveram o lacre setado
	 * @param connection
	 * @return
	 */
	public static Result fixMobLacreCatraca(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			String sql= " SELECT * FROM mob_lacre_catraca  " +
						" WHERE cd_afericao_aplicacao > 0 ";
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			int cont = 0;
			while( rsm.next()){
				cont++;
				AfericaoCatraca afericaoCatraca = AfericaoCatracaDAO.get(rsm.getInt("cd_afericao_aplicacao"));
				
				afericaoCatraca.setCdLacre(rsm.getInt("cd_lacre"));	
				if(AfericaoCatracaDAO.update(afericaoCatraca) <= 0)
					return new Result(-1, "Erro ao atualizar aferiï¿½ï¿½o");
				
			}	
			
			sql= " SELECT * FROM mob_lacre_catraca  " +
				 " WHERE cd_afericao_aplicacao IS NULL AND cd_afericao_remocao > 0 ";
		
			rsm = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			
			while( rsm.next()){
				cont++;
				AfericaoCatraca afericaoCatraca = AfericaoCatracaDAO.get(rsm.getInt("cd_afericao_remocao"));
				
				afericaoCatraca.setCdLacre(rsm.getInt("cd_lacre"));	
				if(AfericaoCatracaDAO.update(afericaoCatraca) <= 0)
					return new Result(-1, "Erro ao atualizar aferiï¿½ï¿½o");
				
			}
			
			
			System.out.println(cont + " aferiï¿½ï¿½es atualizadas");
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Mobilidade - Transporte
	 * Seta uma faixa de AITs dentro de um talï¿½o (Inicial a Final)
	 */	
	public static Result fixMobTalaoAit() {
		return fixMobTalaoAit(null);
	}
	public static Result fixMobTalaoAit(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			String sql= " SELECT * FROM mob_talonario  " +
						" WHERE cd_talao = 3911 "; //cd_talao do talao que sera setado dentro do array de aits inicial ao final.
			
			ResultSetMap rsmTalao = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			int cont = 0;
			int nrAit = 0;
			if( rsmTalao.next()){
				nrAit = Integer.valueOf(rsmTalao.getString("nr_inicial"));
				
				while(nrAit <= Integer.valueOf(rsmTalao.getString("nr_final"))){
					sql= " SELECT A.* FROM mob_ait_transporte  A " +
							" LEFT OUTER JOIN mob_talonario B ON (A.cd_talao = B.cd_talao) " +
							" LEFT OUTER JOIN mob_agente    C ON (A.cd_agente = C.cd_agente) " +
							" WHERE C.tp_agente =  " + AgenteServices.TP_TRANSPORTE+
							"   AND A.nr_ait LIKE \'"+nrAit +"\'"+
							" ORDER BY cd_ait ";
					
					ResultSetMap rsmAit = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
					if(rsmAit.next()){
						AitTransporte aitTransporte = AitTransporteDAO.get(rsmAit.getInt("cd_ait"));
						aitTransporte.setCdTalao(rsmTalao.getInt("cd_talao"));
						AitTransporteDAO.update(aitTransporte);
						cont++;
						System.out.println(nrAit + " <<< Setada com sucesso!");
					}else{
						System.out.println(nrAit + " <<< numero ait nï¿½o encontrado");
					}
					nrAit++;	
				}
			}	
			System.out.println(cont + " registros");
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixMobDataAITCancelada() {
		return fixMobDataAITCancelada(null);
	}
	public static Result fixMobDataAITCancelada(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			String sql= " SELECT cd_ait, nr_ait, dt_infracao, dt_notificacao_inicial, "+
						"		 dt_julgamento1, dt_cancelamento, dt_notificacao1, dt_recebimento "+
						"   FROM mob_ait_transporte "+
						" WHERE st_ait =  "+ AitTransporteServices.ST_CANCELADA +
						"   AND dt_cancelamento IS NULL "; 
			
			ResultSetMap rsmAits = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			int cont = 0;
			while( rsmAits.next()){
				cont++;
				
				AitTransporte aitTransporte = AitTransporteDAO.get(rsmAits.getInt("cd_ait"));
				
				if(rsmAits.getGregorianCalendar("dt_julgamento1") != null){
					aitTransporte.setDtCancelamento(rsmAits.getGregorianCalendar("dt_julgamento1"));
					AitTransporteDAO.update(aitTransporte);
				}
				else if(rsmAits.getGregorianCalendar("dt_recebimento") != null){
					aitTransporte.setDtCancelamento(rsmAits.getGregorianCalendar("dt_recebimento"));
					AitTransporteDAO.update(aitTransporte);
				}
				else if(rsmAits.getGregorianCalendar("dt_notificacao_inicial") != null){
					aitTransporte.setDtCancelamento(rsmAits.getGregorianCalendar("dt_notificacao_inicial"));
					AitTransporteDAO.update(aitTransporte);
				}
				else {
					aitTransporte.setDtCancelamento(rsmAits.getGregorianCalendar("dt_infracao"));
					AitTransporteDAO.update(aitTransporte);
				}
				
					
			}	
			System.out.println(cont + " registros");
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Juris - PROCON
	 * Exclui contar cancelas no banco da EeL
	 */
	public static Result fixExcluirDamEL() {
		return fixExcluirDamEL(null);
	}
	
	public static Result fixExcluirDamEL(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			String sql= "select D.nr_documento, date_part('year', A.dt_vencimento), A.dt_vencimento, B.cod_movimento, C.nm_pessoa from adm_conta_receber A" +
						" left outer join ptc_documento_conta B on (A.cd_conta_receber = B.cd_conta_receber)" +
						" left outer join grl_pessoa C on (A.cd_pessoa = C.cd_pessoa)" +
						" left outer join ptc_documento D on (B.cd_documento = D.cd_documento)" +
						" where A.st_conta = " + ContaReceberServices.ST_CANCELADA +
						" and B.nr_documento is not null";
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			int cont = 0;
			
			while(rsm.next()) {
				
				DAMUtils.deleteTaxa("001", rsm.getDouble("DATE_PART") + "", rsm.getString("COD_MOVIMENTO"));
				cont++;
				System.out.print(".");
			}
			
			System.out.println(cont + " DAMs excluidos.");
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Mobilidade - Transporte
	 * Copia valor da infracao para o AIT
	 */
	public static Result fixMobValorAIT() {
		return fixMobValorAIT(null);
	}
	public static Result fixMobValorAIT(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			String sql= "SELECT * FROM mob_infracao_transporte where nr_valor_ufir <> 0 ORDER BY cd_infracao";
			ResultSetMap rsmInfracoes = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			
			while(rsmInfracoes.next()){
				
				sql= "SELECT count(*) AS total from mob_ait_transporte WHERE vl_multa IS NULL AND cd_infracao = " + rsmInfracoes.getInt("cd_infracao");
				ResultSetMap rsmAits = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
				if(rsmAits.next())
					System.out.println("Infracao n = " + rsmInfracoes.getInt("cd_infracao") + " - " + rsmAits.getInt("total") + " registros alterados.");
				
				connection.prepareStatement("UPDATE mob_ait_transporte SET vl_multa = " + rsmInfracoes.getDouble("nr_valor_ufir") + 
										   " WHERE vl_multa is null AND cd_infracao = " + rsmInfracoes.getInt("cd_infracao")).executeUpdate();				
			}
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Mobilidade - Transporte
	 * Preenche o campo vl_multa em mob_ait_transporte de acordo o valor da infracao
	 * @return
	 */
	public static Result fixMobSetValorAit() {
		return fixMobSetValorAit(null);
	}
	public static Result fixMobSetValorAit(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			String sql= "SELECT * FROM mob_infracao_transporte " +
					" WHERE tp_concessao = " + ConcessaoServices.TP_COLETIVO_URBANO + 
					" AND nr_valor_ufir <> 0" + 
					" ORDER BY nr_infracao"; 
			
			ResultSetMap rsmInfracoes = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			
			while (rsmInfracoes.next()){
				System.out.println("infracao = " + rsmInfracoes.getString("NR_INFRACAO"));
				
				sql= "UPDATE mob_ait_transporte SET vl_multa = " + rsmInfracoes.getDouble("NR_VALOR_UFIR") + 
						" WHERE cd_infracao = " + rsmInfracoes.getInt("CD_INFRACAO") +
						" AND tp_ait = " + AitTransporteServices.AIT_COLETIVO_URBANO +
						" AND st_ait <> " + AitTransporteServices.ST_CANCELADA +
						" AND (vl_multa is null OR vl_multa = 0)";
				
				int retorno = connection.prepareStatement(sql).executeUpdate();
				
//				if (retorno<=0) {
//					System.out.println("ERROR UPDATE!");
//					break;
//				}
			}
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Mobilidade - Transporte
	 * Cria as linhas (rotas e trechos) de uma concessao para a outra
	 */
	public static Result fixMobCopiarLinha() {
		return fixMobCopiarLinha(null);
	}
	public static Result fixMobCopiarLinha(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			int cdConcessaoOrigem = 13;
			int cdConcessaoDestino = 14;
			
			System.out.println("INICIANDO...\n");

			String sql= "SELECT cd_linha FROM mob_linha where cd_concessao = " + cdConcessaoOrigem;
			ResultSetMap rsmLinhas = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			
			System.out.println("Qtd linhas que serao copiadas = " + rsmLinhas.size());
			
			while(rsmLinhas.next()){
				
				Linha linha = LinhaDAO.get(rsmLinhas.getInt("CD_LINHA"), connection);
				linha.setCdLinha(0);
				linha.setCdConcessao(cdConcessaoDestino);
				
				int cdLinha = LinhaDAO.insert(linha, connection);
				if (cdLinha<=0) {
					System.out.println("rollback - linha");
					connection.rollback();
					break; 
				}
				
				// Linha rota
				/*sql= "SELECT cd_rota FROM mob_linha_rota WHERE cd_linha = " + rsmLinhas.getInt("CD_LINHA");
				ResultSetMap rsmLinhaRota = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
				while(rsmLinhaRota.next()){
					
					LinhaRota linhaRota = LinhaRotaDAO.get(rsmLinhaRota.getInt("CD_LINHA"), rsmLinhaRota.getInt("CD_ROTA"), connection);
					linhaRota.setCdLinha(cdLinha);
					linhaRota.setCdRota(0);
					
					int cdRota = LinhaRotaDAO.insert(linhaRota, connection);
					if (cdRota<=0) {
						System.out.println("rollback - linha_rota");
						connection.rollback();
						break; 
					}*/
					
					// Linha trecho
					/*sql= "SELECT cd_trecho FROM mob_linha_trecho WHERE cd_linha = " + rsmLinhas.getInt("CD_LINHA") + 
																" AND cd_rota = " + rsmLinhaRota.getInt("CD_ROTA");
					ResultSetMap rsmLinhaTrecho = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
					while(rsmLinhaTrecho.next()){
						
						LinhaTrecho linhaTrecho = LinhaTrechoDAO.get(rsmLinhas.getInt("CD_LINHA"), rsmLinhaRota.getInt("CD_ROTA"), rsmLinhaTrecho.getInt("CD_TRECHO"), connection);
						linhaTrecho.setCdLinha(cdLinha);
						linhaTrecho.setCdRota(cdRota);
						linhaTrecho.setCdTrecho(0);
						
						if (LinhaTrechoDAO.insert(linhaTrecho, connection)<=0) {
							System.out.println("rollback - linha_trecho");
							connection.rollback();
							break; 
						}
					}
					
				}*/
								
			}
						
			if (isConnectionNull) {
				connection.commit();
				
			}
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Mobilidade - Transporte
	 * Coloca o tipo da 
	 */
	public static Result fixMobTpInternoExterno() {
		return fixMobTpInternoExterno(null);
	}
	public static Result fixMobTpInternoExterno(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			// Busca os processos que possuem a ultima tramitacao informando que necessita de pericia
			String sql= "SELECT a.CD_DOCUMENTO, a.NR_DOCUMENTO, max(b.CD_TRAMITACAO)" +
							" FROM ptc_documento a" +
							" JOIN ptc_documento_tramitacao b ON (b.CD_DOCUMENTO = a.CD_DOCUMENTO)" +
							" WHERE a.cd_situacao_documento = 7 AND b. dt_tramitacao is not null AND b.txt_tramitacao ilike '%sem%perï¿½cia%'" +
							" GROUP BY a.CD_DOCUMENTO";
			ResultSetMap rsmDocumentos = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			System.out.println("Qtd de documentos que serao alterados = " + rsmDocumentos.size());
			
			// altera o documento colocando o campo tipo_interno_externo como interno (dispensa pericia) 
			while(rsmDocumentos.next()){
				
				sql = "UPDATE ptc_documento SET tp_interno_externo = 0 WHERE cd_documento = " + rsmDocumentos.getInt("CD_DOCUMENTO");
				connection.prepareStatement(sql).executeUpdate();	
			}
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Mobilidade - Transporte
	 * corrige as aits que tiveram recursos lanï¿½ados, mas nï¿½o tiveram seus status alterados.
	 */
	public static Result fixMobSituacaoAIT() {
		return fixMobSituacaoAIT(null);
	}
	public static Result fixMobSituacaoAIT(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			String sql= "SELECT * FROM mob_ait_transporte "+
						" WHERE st_ait <=  "+ AitTransporteServices.ST_RECEBIDA + 
						"   AND st_ait <> "+ AitTransporteServices.ST_CANCELADA +
						"   AND (cd_recurso1 > 0 OR cd_recurso2 > 0) "+
						"   AND dt_infracao > \'2014-12-31 00:00:00\' ";
			ResultSetMap rsmAits = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			
			while(rsmAits.next()){
				
				//TODO atualizar os aits com base na fase do recurso, atualizando tambï¿½m cada data de limite.			
			}
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Mobilidade - Transporte
	 * Preenche a tabela MOB_TABELA_HORARIO_ROTA com as idas e voltas de cada linha
	 */
	public static Result fixMobTabelaHorarioRota() {
		return fixMobTabelaHorarioRota(null);
	}
	public static Result fixMobTabelaHorarioRota(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			

			
			String sql= "SELECT A.* FROM mob_tabela_horario A" +
						" LEFT OUTER JOIN mob_linha_rota B ON (A.cd_linha = B.cd_linha AND A.cd_rota = B.cd_rota)" +
						" WHERE B.TP_ROTA = " + LinhaRotaServices.TP_IDA;
			ResultSetMap rsmTabelas = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			
			int retorno = 0; 
			while(rsmTabelas.next()){
				
				// ATENCAO AS LINHAS D41 (17) E R12 (35) - ESSAS TEM MAIS DE 2 ROTAS
				if (rsmTabelas.getInt("CD_LINHA") != 17 && rsmTabelas.getInt("CD_LINHA") != 35) {
					
					TabelaHorarioRota thrIda = new TabelaHorarioRota(rsmTabelas.getInt("CD_LINHA"), rsmTabelas.getInt("CD_TABELA_HORARIO"), rsmTabelas.getInt("CD_ROTA"), 0);
					retorno = TabelaHorarioRotaServices.save(thrIda, null, connection).getCode();
					System.out.println("ida = " + retorno);
					
					sql= "SELECT * FROM mob_linha_rota WHERE cd_linha = " + rsmTabelas.getInt("CD_LINHA") +
						" AND cd_rota <> " + rsmTabelas.getInt("CD_ROTA");
					ResultSetMap rsmRotas = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
					
					if (rsmRotas.getLines().size() == 1 && retorno>0) {
						rsmRotas.next();
						
						TabelaHorarioRota thrVolta = new TabelaHorarioRota(rsmTabelas.getInt("CD_LINHA"), rsmTabelas.getInt("CD_TABELA_HORARIO"), rsmRotas.getInt("CD_ROTA"), 0);
						retorno = TabelaHorarioRotaServices.save(thrVolta, null, connection).getCode();
						System.out.println("volta = " + retorno);
					}
					
					if (retorno<=0)
						break;
				}
			}
			
			if (isConnectionNull && retorno>0)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Inclui na base de dados da Kurier (Recortes) os processos que serï¿½o acompanhados
	 * 
	 * @return
	 * 
	 * @author mauricio
	 * @category Juris - Recortes
	 */
	public static Result fixPrcInsereProcessosKurier() {
		return fixPrcInsereProcessosKurier(null);
	}
	public static Result fixPrcInsereProcessosKurier(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
			}
			
			int retorno = 0;
			
			// PROCESSOS ATIVOS
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.ST_PROCESSO", Integer.toString(ProcessoServices.ST_PROCESSO_DESCONTRATADO), ItemComparator.DIFFERENT, Types.INTEGER));
//			criterios.add(new ItemComparator("nrRegistros", Integer.toString(10), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmProcessos = ProcessoServices.find(criterios, null, connection);
			
			// SERVIï¿½O DE RECORTES
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("TP_SERVICO", Integer.toString(ServicoRecorteServices.TP_KURIER_SOAP), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmServico = ServicoRecorteServices.find(criterios, connection);
			ServicoRecorte servico = null;
			if(rsmServico.next()) {
				servico = new ServicoRecorte(
						rsmServico.getInt("cd_servico"), 
						rsmServico.getString("nm_servico"), 
						rsmServico.getInt("tp_servico"), 
						rsmServico.getString("id_servico"), 
						rsmServico.getString("id_cliente"), 
						rsmServico.getString("nm_senha"), 
						rsmServico.getString("ds_url"), 
						rsmServico.getString("txt_formato"), 
						rsmServico.getInt("vl_timeout"), 
						rsmServico.getGregorianCalendar("dt_ultima_busca"), 
						rsmServico.getInt("lg_estado"), 
						rsmServico.getInt("st_servico"), 
						rsmServico.getInt("lg_confirmar_leitura")
					);
			}
			
			ResultSetMap rsmAux = new ResultSetMap();
			while(rsmProcessos.next()) {
				rsmAux.addRegister(rsmProcessos.getRegister());
				
				if(rsmAux.size() >= 100 || !rsmProcessos.hasMore()) {
					Result r = ServicoRecorteServices.inserirProcessosKurier(rsmAux, servico);
					if(r.getCode()<0) {
						return r;
					}
					rsmAux = new ResultSetMap();
				}
			}
			
			
			return new Result(1, "Processos enviados com sucesso a Kurier.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	public static Result fixRemoveDuplicidadeSentencas() {
		return fixRemoveDuplicidadeSentencas(null);
	}
	public static Result fixRemoveDuplicidadeSentencas(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsmProcessos = Search.find(
					" select cd_processo, tp_sentenca, dt_sentenca, vl_total, vl_sentenca, vl_acordo"
					+ " from prc_processo_sentenca"
					+ " group by cd_processo, tp_sentenca, dt_sentenca, vl_total, vl_sentenca, vl_acordo"
					+ " having count(tp_sentenca)>1"
					+ " order by cd_processo", null, connection, connection==null);
			
			System.out.println(" - INICIO - ");
			
			while(rsmProcessos.next()) {
				PreparedStatement pstmt = null;
				
				if (rsmProcessos.getGregorianCalendar("DT_SENTENCA")==null) {
					pstmt = connection.prepareStatement(
							"DELETE FROM prc_processo_sentenca "
							+ " WHERE cd_processo=?"
							+ " AND tp_sentenca=?"
							+ " AND dt_sentenca is null "
							+ " AND vl_sentenca=?"
							+ " AND vl_acordo=?"
							+ " AND vl_total=?");
					pstmt.setInt(1, rsmProcessos.getInt("CD_PROCESSO"));
					pstmt.setInt(2, rsmProcessos.getInt("TP_SENTENCA"));
					pstmt.setDouble(3, rsmProcessos.getDouble("VL_SENTENCA"));
					pstmt.setDouble(4, rsmProcessos.getDouble("VL_ACORDO"));
					pstmt.setDouble(5, rsmProcessos.getDouble("VL_TOTAL"));
				
				} else {
					pstmt = connection.prepareStatement(
							"DELETE FROM prc_processo_sentenca "
							+ " WHERE cd_processo=?"
							+ " AND tp_sentenca=?"
							+ " AND dt_sentenca=?"
							+ " AND vl_sentenca=?"
							+ " AND vl_acordo=?"
							+ " AND vl_total=?");
					pstmt.setInt(1, rsmProcessos.getInt("CD_PROCESSO"));
					pstmt.setInt(2, rsmProcessos.getInt("TP_SENTENCA"));
					pstmt.setTimestamp(3, Util.convCalendarToTimestamp(rsmProcessos.getGregorianCalendar("DT_SENTENCA")));
					pstmt.setDouble(4, rsmProcessos.getDouble("VL_SENTENCA"));
					pstmt.setDouble(5, rsmProcessos.getDouble("VL_ACORDO"));
					pstmt.setDouble(6, rsmProcessos.getDouble("VL_TOTAL"));
				}
				
				retorno = pstmt.executeUpdate();
				if (retorno<=0) {
					System.out.println("ERROR DELETE! cdProcesso = " + rsmProcessos.getInt("CD_PROCESSO"));
					break;
				}
				else
					System.out.println("d");
			}
			
			if (retorno>0) {
				
				System.out.println("apagados - ok");
				System.out.println("salvado");
				
				rsmProcessos.beforeFirst();
				
				while(rsmProcessos.next()) {
					ProcessoSentenca sentenca = new ProcessoSentenca();
					
					sentenca.setCdProcesso(rsmProcessos.getInt("CD_PROCESSO"));
					sentenca.setTpSentenca(rsmProcessos.getInt("TP_SENTENCA"));
					sentenca.setDtSentenca(rsmProcessos.getGregorianCalendar("DT_SENTENCA"));
					sentenca.setVlSentenca(rsmProcessos.getDouble("VL_SENTENCA"));
					sentenca.setVlAcordo(rsmProcessos.getDouble("VL_ACORDO"));
					sentenca.setVlTotal(rsmProcessos.getDouble("VL_TOTAL"));
					
					retorno = ProcessoSentencaServices.save(sentenca, null, connection).getCode();
					if (retorno<=0) {
						System.out.println("ERROR SAVE! cdProcesso = " + rsmProcessos.getInt("CD_PROCESSO"));
						break;
					}
					else
						System.out.println("s");
				}
			}
			
			
			if (retorno>0){
				System.out.println("commit");
				connection.commit();
			} else {
				System.out.println("rollback");
				connection.rollback();
			}
			
			System.out.println("\n - FIM - ");
			return new Result(1, "Fix executado!");
		}
		
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Mobilidade - Transporte
	 * Preenche a tabela MOB_TABELA_HORARIO_ROTA com as idas e voltas de cada linha
	 */
	public static Result fixMobImportacaoHorario() {
		return fixMobImportacaoHorario(null);
	}
	public static Result fixMobImportacaoHorario(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			
			System.out.println("INICIANDO...\n");
			
			String line = "";
            File fileDir = new File("Local do arquivo...");
			BufferedReader raf = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
			
			while ((line = raf.readLine()) != null) {
				System.out.println("===========================================================================================================");
				StringTokenizer tokens = new StringTokenizer(line, ";", false);
				
				//Tabela
				String tabela = tokens.nextToken();
				//Bairro ou Centro
				String hrHorario = tokens.nextToken();
//				String hrCentro = tokens.nextToken();
				
				//Bairro ou Centro Partida
				GregorianCalendar hrPartida = new GregorianCalendar();
				hrPartida.set(0, 0, 0, Integer.parseInt(hrHorario.split(":")[0]), Integer.parseInt(hrHorario.split(":")[1]));
				GregorianCalendar hrChegada = new GregorianCalendar();
				hrChegada.set(0, 0, 0, Integer.parseInt(hrHorario.split(":")[0]), Integer.parseInt(hrHorario.split(":")[1]));
				System.out.println(Util.formatDate(hrPartida, "dd/MM/yyyy HH:mm:ss"));
				
				
				//Horï¿½rio Bairro/Centro
				Horario horarioCampo1 = new Horario();
				
				//Setar parï¿½metros da linha que serï¿½ inserido os horï¿½rios no banco.
				
				horarioCampo1.setCdTabelaHorario(0); // Cï¿½digo da Tabela Horï¿½rio
//				horarioCampo1.setCdTabelaHorarioRota(1); //Cï¿½digo da Tabela Horï¿½rio Rota
				horarioCampo1.setCdLinha(0); // Cï¿½digo da linha
				horarioCampo1.setCdRota(0); // Ida 1 | Volta 2				
				horarioCampo1.setCdTrecho(0); // Cï¿½digo do trecho 1 primeira parada e 2 segunda parada
//				horarioCampo1.setCdVariacao(cdVariacao); // Azul | Branco			
				horarioCampo1.setHrChegada(hrChegada);
				horarioCampo1.setHrPartida(hrPartida);
				
				HorarioServices.save(horarioCampo1, null, connection);
				
				connection.commit();
				
				//Horï¿½rio Bairro/Centro
					Horario horarioCampo2 = new Horario();
					
					//Setar parï¿½metros da linha que serï¿½ inserido os horï¿½rios no banco.
					
					horarioCampo2.setCdTabelaHorario(0); // Cï¿½digo da Tabela Horï¿½rio
	//				horarioCampo2.setCdTabelaHorarioRota(1); //Cï¿½digo da Tabela Horï¿½rio Rota
					horarioCampo2.setCdLinha(0); // Cï¿½digo da linha
					horarioCampo2.setCdRota(0); // Ida 1 | Volta 2				
					horarioCampo2.setCdTrecho(0); // Cï¿½digo do trecho 1 primeira parada e 2 segunda parada
	//				horarioCampo2.setCdVariacao(cdVariacao); // Azul | Branco			
					horarioCampo2.setHrChegada(hrChegada);
					horarioCampo2.setHrPartida(hrPartida);
					
					HorarioServices.save(horarioCampo2, null, connection);
					
					connection.commit();
			}
			
			if (isConnectionNull && retorno>0)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result criarQuadroVagasCursoTurmas() {
		return criarQuadroVagasCursoTurmas(null);
	}
	public static Result criarQuadroVagasCursoTurmas(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");

			String sql= "SELECT * FROM acd_quadro_vagas ORDER BY cd_instituicao, cd_quadro_vagas";
			ResultSetMap rsmQuadroVagas = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			int cdInstituicao = 0;
			HashMap<String, Object> oldRegister = new HashMap<String, Object>();
			while(rsmQuadroVagas.next()){
				
				if(cdInstituicao == 0)
					cdInstituicao = rsmQuadroVagas.getInt("cd_instituicao");
				
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoRecente(cdInstituicao, connection);
				int cdPeriodoLetivoAtual = 0;
				if(rsmPeriodoAtual.next()){
					cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
				
				InstituicaoEducacenso educacenso = InstituicaoEducacensoDAO.get(cdInstituicao, cdPeriodoLetivoAtual, connection);
				Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connection);
				if(educacenso == null || educacenso.getStInstituicaoPublica() != InstituicaoEducacensoServices.ST_EM_ATIVIDADE){
					cdInstituicao = rsmQuadroVagas.getInt("cd_instituicao");
					oldRegister = rsmQuadroVagas.getRegister();
					continue;
				}
				System.out.println("instituicao = " + instituicao.getNmPessoa());
				
				if(cdInstituicao != rsmQuadroVagas.getInt("cd_instituicao")){
					
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_quadro_vagas", oldRegister.get("CD_QUADRO_VAGAS").toString(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_instituicao", oldRegister.get("CD_INSTITUICAO").toString(), ItemComparator.EQUAL, Types.INTEGER));
					
					
					
					QuadroVagas quadroVagas = new QuadroVagas(0, cdInstituicao, cdPeriodoLetivoAtual, Util.getDataAtual(), QuadroVagasServices.ST_PENDENTE, null);
					Result result = QuadroVagasServices.save(quadroVagas, connection);
					if(result.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return result;
					}
					
					ResultSetMap rsmQuadroVagasCurso = QuadroVagasCursoDAO.find(criterios, connection);
					while(rsmQuadroVagasCurso.next()){
						QuadroVagasCurso quadroVagasCurso = new QuadroVagasCurso(quadroVagas.getCdQuadroVagas(), cdInstituicao, rsmQuadroVagasCurso.getInt("cd_curso"), rsmQuadroVagasCurso.getInt("tp_turno"), rsmQuadroVagasCurso.getInt("qt_turmas"), rsmQuadroVagasCurso.getInt("qt_vagas"));
						result = QuadroVagasCursoServices.save(quadroVagasCurso, connection);
						if(result.getCode() < 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1);
						}
					}
					result = QuadroVagasServices.adicionarTurmas(cdPeriodoLetivoAtual, connection);
					if(result.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return result;
					}
					
					
				}
				
				cdInstituicao = rsmQuadroVagas.getInt("cd_instituicao");
				oldRegister = rsmQuadroVagas.getRegister();
			}
			
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_quadro_vagas", oldRegister.get("CD_QUADRO_VAGAS").toString(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_instituicao", oldRegister.get("CD_INSTITUICAO").toString(), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoRecente(cdInstituicao, connection);
			int cdPeriodoLetivoAtual = 0;
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			QuadroVagas quadroVagas = new QuadroVagas(0, cdInstituicao, cdPeriodoLetivoAtual, Util.getDataAtual(), QuadroVagasServices.ST_PENDENTE, null);
			Result result = QuadroVagasServices.save(quadroVagas, connection);
			if(result.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}
			
			ResultSetMap rsmQuadroVagasCurso = QuadroVagasCursoDAO.find(criterios, connection);
			while(rsmQuadroVagasCurso.next()){
				QuadroVagasCurso quadroVagasCurso = new QuadroVagasCurso(quadroVagas.getCdQuadroVagas(), cdInstituicao, rsmQuadroVagasCurso.getInt("cd_curso"), rsmQuadroVagasCurso.getInt("tp_turno"), rsmQuadroVagasCurso.getInt("qt_turmas"), rsmQuadroVagasCurso.getInt("qt_vagas"));
				result = QuadroVagasCursoServices.save(quadroVagasCurso, connection);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return result;
				}
			}
			
			result = QuadroVagasServices.adicionarTurmas(cdPeriodoLetivoAtual, connection);
			if(result.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}
			
			
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	public static Result sentencaValorTotal() {
		return sentencaValorTotal(null);
	}
	public static Result sentencaValorTotal(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("INICIANDO...\n");
			
			ResultSetMap rsmTotal = Search.find(
					"select cd_sentenca, cd_processo from prc_processo_sentenca"
					+" where (vl_sentenca<>0 or vl_acordo<>0) and "
					+ "vl_total=0 and tp_sentenca in (0,1,2,3,5,8,9)", 
					null, connection, connection==null);
			while(rsmTotal.next()) {
				ProcessoSentenca sentenca=ProcessoSentencaDAO.get(rsmTotal.getInt("cd_sentenca"), rsmTotal.getInt("cd_processo"), connection);
				sentenca.setVlTotal(sentenca.getVlAcordo()+sentenca.getVlSentenca());
				int retorno=ProcessoSentencaDAO.update(sentenca, connection);
				if(retorno<0) {
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1,"Erro ao alterar total da sentenï¿½a");
				}
			}
			
			if(isConnectionNull)
				connection.commit();
			
			return new Result(1, "Valores alterados com sucesso!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull) 
				Conexao.desconectar(connection);
			return null;
		}
		finally {
			if (isConnectionNull) {
				Conexao.desconectar(connection);
			}
		}
	}
	public static Result fixOSNFeFromCSV(){
		String nmArquivo = "C:\\TIVIC\\OS_NF.csv";
		int cdFornecedor = 198026;
		String nrNf = "1945"; 
		
		return fixOSNFeFromCSV(nmArquivo);
	}

	private static Result fixOSNFeFromCSV(String nmArquivo) {
		//abre conexï¿½o
		Connection connect = Conexao.conectar();
		try {
			//seta falso para nï¿½o commitar
			connect.setAutoCommit(false);
			//ler arquivo 
			File fileDir = new File("C:\\Users\\darll\\Desktop\\Pasta1.csv");
			BufferedReader raf = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\darll\\Desktop\\Pasta1.csv"), "UTF8"));
			System.out.println(nmArquivo + " carregado dados...");
			
			//consulta
			PreparedStatement pOS = connect.prepareStatement("SELECT cd_ordem_servico, nr_ordem_servico FROM ord_ordem_servico WHERE nr_ordem_servico = ?");
			//update
			PreparedStatement updateOS = connect.prepareStatement("UPDATE ord_ordem_servico SET (cd_fornecedor, nr_nota_fiscal) = (?,?) WHERE cd_ordem_servico = ?");
			
			int result = 0;
			int cdOs = 0;
			String nrOs = "";
			int cdFornecedor = 0;
			String nrNf = "";
			String line = "";
			System.out.println("iniciando while");
			while ((line = raf.readLine()) != null) {
				StringTokenizer tokens = new StringTokenizer(line, ";", false);
				nrOs = tokens.nextToken()+"/2017";
				cdFornecedor = Integer.parseInt(tokens.nextToken());
				nrNf = tokens.nextToken();
				
				//posiï¿½ï¿½o do campo na sql - exemplo DAO
				pOS.setString(1, nrOs);
				ResultSet rsOS = pOS.executeQuery();
				
				if(rsOS.next())
					cdOs = rsOS.getInt("cd_ordem_servico");
				else 
					System.out.println("Cï¿½digo nï¿½o localizado "+nrOs);
				
				updateOS.setInt(1, cdFornecedor);
				updateOS.setString(2, nrNf);
				updateOS.setInt(3, cdOs);
				result = updateOS.executeUpdate();
				
				if(result <= 0)
					return new Result(-1, "Erro ao atualizar OS");
				
				System.out.println("Atualizou " + rsOS.getString("nr_ordem_servico"));
			
			}
			
			connect.commit();

			System.out.println("fim while");
			return new Result(1, "OSs atualizadas com sucesso!");
			
		} catch (Exception e) {
			e.printStackTrace();
			Conexao.rollback(connect);
			return new Result(-1, e.getMessage()); 
		}
		
	}
	public static void corrigirDataHrPartida(){
		corrigirDataHrPartida(null);			
	}
	
	
	public static void corrigirDataHrPartida(Connection connect  ){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_horario");
			
			rs = pstmt.executeQuery();
			int cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho,cdParadaChegada, cdPardaPartida, cdVariacao = 0;
			GregorianCalendar hrPartida, hrChegada;
			
			Horario horarioTemp;
						
			while(rs.next()){
				horarioTemp = new Horario();
				
				cdHorario = rs.getInt("cd_horario");
				cdTabelaHorario = rs.getInt("cd_tabela_horario");
				cdLinha = rs.getInt("cd_linha");
				cdRota = rs.getInt("cd_rota");
				cdTrecho = rs.getInt("cd_trecho");
				hrPartida = (rs.getTimestamp("hr_partida")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_partida").getTime());
				hrChegada = (rs.getTimestamp("hr_chegada")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_chegada").getTime());

				if(hrPartida != null)				
				hrPartida.set(1970, 0, 1);
				if(hrChegada != null)
				hrChegada.set(1970, 0, 1);
				
				cdParadaChegada = rs.getInt("cd_parada_chegada");
				cdPardaPartida = rs.getInt("cd_parada_partida");
				cdVariacao = rs.getInt("cd_variacao");	
				
				horarioTemp.setCdHorario(cdHorario);
				horarioTemp.setCdTabelaHorario(cdTabelaHorario);
				horarioTemp.setCdLinha(cdLinha);
				horarioTemp.setCdRota(cdRota);
				horarioTemp.setCdTrecho(cdTrecho);
				horarioTemp.setCdParadaChegada(cdParadaChegada);
				horarioTemp.setCdParadaPartida(cdPardaPartida);
				horarioTemp.setCdVariacao(cdVariacao);
				horarioTemp.setHrPartida(hrPartida);
				horarioTemp.setHrChegada(hrChegada);
				
				HorarioDAO.update(horarioTemp);
				 
			}
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.get: " + sqlExpt);
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioDAO.get: " + e);
			
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Mobilidade - Transporte
	 * Coloca o codigo do talao nos aits
	 */	
	public static Result fixMobCdTalaoAit() {
		return fixMobCdTalaoAit(null);
	}
	public static Result fixMobCdTalaoAit(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			int cont = 0;
			System.out.println("INICIANDO...\n");

			// busca tds aits sem cd_talao
			String sql= "SELECT cd_ait, nr_ait FROM mob_ait_transporte WHERE cd_talao IS NULL";
			ResultSetMap rsmAits = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			
			while (rsmAits.next()) {
				
				// busca o talonario que contem o numero da ait
				String nrAit = rsmAits.getString("NR_AIT");
				
				if (StringUtils.isNumeric(nrAit)) {
					sql= "SELECT * FROM mob_talonario A WHERE ('"+ rsmAits.getString("NR_AIT") +"' BETWEEN A.nr_inicial AND A.nr_final )" +
							" AND (TP_TALAO=" + TalonarioAITServices.TP_TALONARIO_ELETRONICO_TRANSPORTE  + " OR TP_TALAO = " + TalonarioAITServices.TP_TALONARIO_TRANSPORTE + ")";
					ResultSetMap rsmTalao = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
					
					if (rsmTalao.next() && rsmTalao.getLines().size()==1) {
						AitTransporte aitTransporte = AitTransporteDAO.get(rsmAits.getInt("CD_AIT"));
						aitTransporte.setCdTalao(rsmTalao.getInt("CD_TALAO"));
						
						// atualizando codigo do talao no ait
						if (AitTransporteDAO.update(aitTransporte)<0)
							System.out.println(rsmAits.getString("NR_AIT") + " <<< erro ao salvar ait");
						else {
							cont++;
							System.out.println(".");
						}
					
					} else
						System.out.println(rsmAits.getString("NR_AIT") + " <<< erro no talao da ait");
				}
			}
			
			System.out.println(cont + " registros alterados");
						
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	/**
	 * Mobilidade - Transporte
	 * Coloca o codigo do talao nos aits
	 */	
	public static Result fixMatriculaDisciplina() {
		return fixMatriculaDisciplina(null);
	}
	public static Result fixMatriculaDisciplina(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			ResultSetMap rsmMatriculas = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_matricula WHERE st_matricula = " + MatriculaServices.ST_ATIVA + " AND cd_periodo_letivo > 1648").executeQuery());
			while(rsmMatriculas.next()){
				
				Matricula matricula = MatriculaDAO.get(rsmMatriculas.getInt("cd_matricula"), connection);
				
				if (matricula.getCdPeriodoLetivo() > 0) {
					MatriculaPeriodoLetivoServices.save(new MatriculaPeriodoLetivo(matricula.getCdMatricula(), matricula.getCdPeriodoLetivo(), 0, matricula.getDtMatricula(), matricula.getStMatricula(), 0, 0, 0), connection);
					/**
					 * Salvando matriculaModulo
					 */
					//TODO: Alterar quando a matrï¿½cula por mï¿½dulo for implementada
					CursoModulo cursoModulo = CursoModuloServices.getById(matricula.getCdCurso()+"001", matricula.getCdCurso(), connection);
					if(matricula.getCdCurso() > 0 && (cursoModulo != null && cursoModulo.getCdCursoModulo() > 0 )){
						MatriculaModuloServices.save(new MatriculaModulo(matricula.getCdMatricula(), 
													cursoModulo.getCdCursoModulo(), 
													matricula.getCdPeriodoLetivo()), connection);
					}
					
					/**
					 * Salvando matriculaDisciplinas
					 */
					ResultSetMap matriculaDisciplinas = CursoMatrizServices.getAllDisciplinas(matricula.getCdCurso(), matricula.getCdMatriz(), matricula.getCdTurma(), false, connection);
					while (matriculaDisciplinas.next()) {
						MatriculaDisciplinaServices.save(new MatriculaDisciplina(0, matricula.getCdMatricula(), matricula.getCdPeriodoLetivo(), 
								0/*cdConceito*/, null/*dtInicio*/, null/*dtConclusao*/, 0/*nrFaltas*/, 
								0/*tpMatricula*/, 0F/*vlConceito*/, 0/*qtChComplemento*/, 
								0F/*vlConceitoAproveitamento*/, ""/*nmInstitiuicaoAproveitamento*/, 
								0/*stMatriculaDisciplina*/, 0/*cdProfessor*/, 0/*cdSupervisorPratica*/, 
								0/*cdInstituicaoPratica*/, matriculaDisciplinas.getInt("cd_matriz"), matriculaDisciplinas.getInt("cd_curso"), 
								matriculaDisciplinas.getInt("cd_curso_modulo"), matriculaDisciplinas.getInt("cd_disciplina"), matriculaDisciplinas.getInt("cd_oferta"), 0), connection);
					}
				}
			}
			
			
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	//REALIZADA
//	public static Result fixTabelasAvaliacao() {
//		return fixTabelasAvaliacao(null);
//	}
//	public static Result fixTabelasAvaliacao(Connection connection) {
//		boolean isConnectionNull = connection==null;
//		try {
//			if (isConnectionNull) {
//				connection = Conexao.conectar();
//				connection.setAutoCommit(false);
//			}
//			
//			System.out.println("INICIADO");
//			
//			System.out.println("INICIADO CURSO MODULO");
//			connection.prepareStatement("DELETE FROM acd_aula_matricula").executeUpdate();
//			connection.prepareStatement("DELETE FROM acd_aula").executeUpdate();
//			
//			connection.prepareStatement("DELETE FROM acd_ocorrencia_oferta_avaliacao").executeUpdate();
//			connection.prepareStatement("DELETE FROM grl_ocorrencia where cd_ocorrencia = 387629").executeUpdate();
//			connection.prepareStatement("DELETE FROM acd_ocorrencia_disciplina_avaliacao_aluno").executeUpdate();
//			connection.prepareStatement("DELETE FROM grl_ocorrencia where cd_ocorrencia in (387639, 387641, 387793)").executeUpdate();
//			
//			connection.prepareStatement("DELETE FROM acd_disciplina_avaliacao_aluno").executeUpdate();
//			connection.prepareStatement("DELETE FROM acd_disciplina_avaliacao").executeUpdate();
//						
//			
//			//CORRIGIR CURSO MODULO
//			connection.prepareStatement("DELETE FROM acd_matricula_disciplina WHERE cd_curso_modulo IN  (1269, 352, 1154, 1156, 1170)").executeUpdate();	
//			
//			connection.prepareStatement("DELETE FROM acd_curso_disciplina WHERE cd_curso_modulo IN (1269,352,1154,1156,1170)").executeUpdate();
//
//			connection.prepareStatement("DELETE FROM acd_matricula_modulo WHERE cd_curso_modulo IN (1269,352,1154,1156,1170)").executeUpdate();
//		
//			connection.prepareStatement("DELETE FROM acd_curso_modulo WHERE cd_curso_modulo IN (1269,352,1154,1156,1170)").executeUpdate();
//			System.out.println("FINALIZADO CURSO MODULO");
//			
//			System.out.println("INICIADO CURSO DISCIPLINA");
//			//APAGAR E REFAZER CURSO DISCIPLINA
//			connection.prepareStatement("DELETE FROM acd_curso_unidade_conceito").executeUpdate();
//
//			connection.prepareStatement("DELETE FROM acd_matricula_disciplina").executeUpdate();
//
//			connection.prepareStatement("DELETE FROM acd_curso_disciplina").executeUpdate();
//			
//			
//			ArrayList<Integer> cursosAnosIniciais = new ArrayList<Integer>();
//			cursosAnosIniciais.add(19);
//			cursosAnosIniciais.add(1197);
//			cursosAnosIniciais.add(1189);
//			cursosAnosIniciais.add(1161);
//			cursosAnosIniciais.add(1157);
//			cursosAnosIniciais.add(1155);
//			cursosAnosIniciais.add(1151);
//			cursosAnosIniciais.add(1153);
//			cursosAnosIniciais.add(1169);
//			cursosAnosIniciais.add(1167);
//			cursosAnosIniciais.add(1165);
//			cursosAnosIniciais.add(1163);
//			cursosAnosIniciais.add(1149);
//			cursosAnosIniciais.add(1191);
//			
//			
//			ArrayList<Integer> disciplinasAnosIniciais = new ArrayList<Integer>();
//			disciplinasAnosIniciais.add(207);
//			disciplinasAnosIniciais.add(209);
//			disciplinasAnosIniciais.add(210);
//			disciplinasAnosIniciais.add(211);
//			disciplinasAnosIniciais.add(212);
//			disciplinasAnosIniciais.add(214);
//			disciplinasAnosIniciais.add(215);
//			disciplinasAnosIniciais.add(216);
//			disciplinasAnosIniciais.add(217);
//			disciplinasAnosIniciais.add(218);
//			disciplinasAnosIniciais.add(219);
//			disciplinasAnosIniciais.add(222);
//			disciplinasAnosIniciais.add(226);
//			disciplinasAnosIniciais.add(228);
//			disciplinasAnosIniciais.add(229);
//			disciplinasAnosIniciais.add(220);
//			disciplinasAnosIniciais.add(213);
//			disciplinasAnosIniciais.add(230);
//			
//			for(Integer cdCurso : cursosAnosIniciais){
//				
//				
//				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
//				ResultSetMap rsmCursoModulo = CursoModuloServices.find(criterios, connection);
//				int cdCursoModulo = 0;
//				if(rsmCursoModulo.next()){
//					cdCursoModulo = rsmCursoModulo.getInt("cd_curso_modulo");
//				}
//				
//				int nrOrdem = 0;
//				for(Integer cdDisciplina : disciplinasAnosIniciais){
//					Result result = CursoDisciplinaServices.save(new CursoDisciplina(cdCurso.intValue(), cdCursoModulo, cdDisciplina.intValue(), 1/*cdMatriz*/, 0/*cdNucleo*/, 0/*qtCargaHoraria*/, nrOrdem, 0/*tpClassificacao*/, 0/*cdInstituicao*/), connection);
//					if(result.getCode() < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao inserir cursos disciplina");
//					}
//					nrOrdem++;
//				}
//				
//			}
//				
//			
//			
//			ArrayList<Integer> cursosAnosFinais = new ArrayList<Integer>();
//			cursosAnosFinais.add(89);
//			cursosAnosFinais.add(18);
//			cursosAnosFinais.add(1185);
//			cursosAnosFinais.add(1177);
//			cursosAnosFinais.add(1195);
//			cursosAnosFinais.add(1193);
//			cursosAnosFinais.add(1183);
//			cursosAnosFinais.add(1199);
//			cursosAnosFinais.add(1201);
//			cursosAnosFinais.add(1175);
//			cursosAnosFinais.add(1173);
//			cursosAnosFinais.add(1171);
//			
//			ArrayList<Integer> disciplinasAnosFinais = new ArrayList<Integer>();
//			disciplinasAnosFinais.add(205);
//			disciplinasAnosFinais.add(206);
//			disciplinasAnosFinais.add(207);
//			disciplinasAnosFinais.add(208);
//			disciplinasAnosFinais.add(209);
//			disciplinasAnosFinais.add(210);
//			disciplinasAnosFinais.add(211);
//			disciplinasAnosFinais.add(212);
//			disciplinasAnosFinais.add(214);
//			disciplinasAnosFinais.add(215);
//			disciplinasAnosFinais.add(216);
//			disciplinasAnosFinais.add(217);
//			disciplinasAnosFinais.add(218);
//			disciplinasAnosFinais.add(219);
//			disciplinasAnosFinais.add(222);
//			disciplinasAnosFinais.add(226);
//			disciplinasAnosFinais.add(228);
//			disciplinasAnosFinais.add(229);
//			disciplinasAnosFinais.add(221);
//			disciplinasAnosFinais.add(213);
//			disciplinasAnosFinais.add(230);
//			
//
//			for(Integer cdCurso : cursosAnosFinais){
//				
//				
//				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
//				ResultSetMap rsmCursoModulo = CursoModuloServices.find(criterios, connection);
//				int cdCursoModulo = 0;
//				if(rsmCursoModulo.next()){
//					cdCursoModulo = rsmCursoModulo.getInt("cd_curso_modulo");
//				}
//				
//				int nrOrdem = 0;
//				for(Integer cdDisciplina : disciplinasAnosFinais){
//					Result result = CursoDisciplinaServices.save(new CursoDisciplina(cdCurso.intValue(), cdCursoModulo, cdDisciplina.intValue(), 1/*cdMatriz*/, 0/*cdNucleo*/, 0/*qtCargaHoraria*/, nrOrdem, 0/*tpClassificacao*/, 0/*cdInstituicao*/), connection);
//					if(result.getCode() < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao inserir cursos disciplina");
//					}
//					nrOrdem++;
//				}
//				
//			}
//			System.out.println("FINALIZADO CURSO DISCIPLINA");
//			
//			
//			System.out.println("INICIADO MATRICULA DISCIPLINA");
//			//APAGAR TODOS OS MATRICULA DISCIPLINA
//			connection.prepareStatement("DELETE FROM acd_matricula_disciplina").executeUpdate();
//			System.out.println("FINALIZADO MATRICULA DISCIPLINA");
//			
//			ResultSetMap rsmMatriculas = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_matricula WHERE st_matricula = " + MatriculaServices.ST_ATIVA + " AND cd_periodo_letivo > 1648").executeQuery());
//			while(rsmMatriculas.next()){
//				
//				System.out.println("INICIADO MATRICULA PERIODO LETIVO");
//				//CORRIGIR E ACRESCENTAR MATRICULA PERIODO LETIVO
//				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_matricula", "" + rsmMatriculas.getInt("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
//				ResultSetMap rsmMatriculaPeriodoLetivo = MatriculaPeriodoLetivoServices.find(criterios, connection);
//				if(rsmMatriculaPeriodoLetivo.next()){
//					MatriculaPeriodoLetivo matriculaPeriodoLetivo = new MatriculaPeriodoLetivo(rsmMatriculas.getInt("cd_matricula"), rsmMatriculas.getInt("cd_periodo_letivo"), 0/*cdContrato*/, rsmMatriculas.getGregorianCalendar("dt_matricula"), rsmMatriculas.getInt("st_matricula"), 0/*cdMotivoTrancamento*/, 0/*cdPedidoVenda*/, 0/*cdRota*/);
//					if(MatriculaPeriodoLetivoDAO.update(matriculaPeriodoLetivo, connection) < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao atualizar matricula periodo letivo");
//					}
//				}
//				else{
//					MatriculaPeriodoLetivo matriculaPeriodoLetivo = new MatriculaPeriodoLetivo(rsmMatriculas.getInt("cd_matricula"), rsmMatriculas.getInt("cd_periodo_letivo"), 0/*cdContrato*/, rsmMatriculas.getGregorianCalendar("dt_matricula"), rsmMatriculas.getInt("st_matricula"), 0/*cdMotivoTrancamento*/, 0/*cdPedidoVenda*/, 0/*cdRota*/);
//					if(MatriculaPeriodoLetivoDAO.insert(matriculaPeriodoLetivo, connection) < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao inserir matricula periodo letivo");
//					}
//				}
//				System.out.println("FINALIZADO MATRICULA PERIODO LETIVO");
//				
//				
//				criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_matricula", "" + rsmMatriculas.getInt("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
//				
//				System.out.println("INICIADO MATRICULA MODULO");
//				//CORRIGIR E ACRESCENTAR MATRICULA MODULO
//				ResultSetMap rsmMatriculaModulo = MatriculaModuloServices.find(criterios, connection);
//				if(rsmMatriculaModulo.next()){
//					
//					MatriculaPeriodoLetivo matriculaPeriodoLetivo = MatriculaPeriodoLetivoDAO.get(rsmMatriculaModulo.getInt("cd_matricula"), rsmMatriculaModulo.getInt("cd_periodo_letivo"), connection);
//					matriculaPeriodoLetivo.setCdPeriodoLetivo(rsmMatriculas.getInt("cd_periodo_letivo"));
//					
//					MatriculaModulo matriculaModulo = new MatriculaModulo(rsmMatriculaModulo.getInt("cd_matricula"), rsmMatriculaModulo.getInt("cd_curso_modulo"), rsmMatriculaModulo.getInt("cd_periodo_letivo"));
//					matriculaModulo.setCdPeriodoLetivo(rsmMatriculas.getInt("cd_periodo_letivo"));
//					
//					if(MatriculaModuloDAO.delete(rsmMatriculaModulo.getInt("cd_matricula"), rsmMatriculaModulo.getInt("cd_curso_modulo"), connection) < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao deletar matricula modulo");
//					}
//					
//					if(MatriculaPeriodoLetivoDAO.delete(rsmMatriculaModulo.getInt("cd_matricula"), rsmMatriculaModulo.getInt("cd_periodo_letivo"), connection) < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao deletar matricula periodo letivo");
//					}
//					
//					int ret = MatriculaPeriodoLetivoDAO.insert(matriculaPeriodoLetivo, connection);
//					if(ret < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao atualizar matricula periodo letivo");
//					}
//					
//					
//					if(MatriculaModuloDAO.insert(matriculaModulo, connection) < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao atualizar matricula modulo");
//					}
//				}
//				else{
//					criterios = new ArrayList<ItemComparator>();
//					criterios.add(new ItemComparator("cd_curso", "" + rsmMatriculas.getInt("cd_curso"), ItemComparator.EQUAL, Types.INTEGER));
//					ResultSetMap rsmCursoModulo = CursoModuloServices.find(criterios, connection);
//					int cdCursoModulo = 0;
//					if(rsmCursoModulo.next()){
//						cdCursoModulo = rsmCursoModulo.getInt("cd_curso_modulo");
//					}
//					
//					MatriculaModulo matriculaModulo = new MatriculaModulo(rsmMatriculas.getInt("cd_matricula"), cdCursoModulo, rsmMatriculas.getInt("cd_periodo_letivo"));
//					if(MatriculaModuloDAO.insert(matriculaModulo, connection) < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao inserir matricula modulo");
//					}
//				}
//				System.out.println("FINALIZADO MATRICULA MODULO");
//				
//				
//				
//			}
//					
//			System.out.println("INICIADO AVALIACAO");
//			//APAGAR TUDO DE AVALIACAO
//			connection.prepareStatement("DELETE FROM acd_oferta_avaliacao").executeUpdate();
//			connection.prepareStatement("DELETE FROM acd_disciplina_avaliacao_aluno").executeUpdate();
//			System.out.println("FINALIZADO AVALIACAO");
//			
//			if (isConnectionNull)
//				connection.commit();
//			
//			System.out.println("\nFINALIZADO. ");
//			return new Result(1, "Executado com sucesso.");
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connection);
//			return null;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connection);
//		}
//	}
	
	
	//REALIZADA
	public static Result fixNumerosMatricula() {
		return fixNumerosMatricula(null);
	}
	public static Result fixNumerosMatricula(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			ResultSetMap rsmMatriculas = new ResultSetMap(connection.prepareStatement("select cd_aluno, nr_matricula, cd_matricula from acd_matricula where nr_matricula like '2018%' AND ('' || cd_aluno) <> substring(nr_matricula from 5)").executeQuery());
			while(rsmMatriculas.next()){
				
				Matricula matricula = MatriculaDAO.get(rsmMatriculas.getInt("cd_matricula"), connection);
				
				matricula.setNrMatricula("2018" + rsmMatriculas.getString("cd_aluno"));
				
				if(MatriculaDAO.update(matricula, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar matrÃ­cula");
				}
			}
			

			
			if (isConnectionNull)
				connection.commit();
			
			System.out.println("\nFINALIZADO. ");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	//REALIZADA
//	public static Result fixEnderecoAlunos() {
//		return fixEnderecoAlunos(null);
//	}
//	public static Result fixEnderecoAlunos(Connection connection) {
//		boolean isConnectionNull = connection==null;
//		try {
//			if (isConnectionNull) {
//				connection = Conexao.conectar();
//				connection.setAutoCommit(false);
//			}
//			
//			int quantAlunosMod = 0;
//			ResultSetMap rsmAlunos = new ResultSetMap(connection.prepareStatement("SELECT * FROM grl_pessoa A, acd_aluno B WHERE A.cd_pessoa = B.cd_aluno").executeQuery());
//			while(rsmAlunos.next()){
//				
//				ResultSetMap rsmEndereco = new ResultSetMap(connection.prepareStatement("SELECT * FROM grl_pessoa_endereco WHERE cd_pessoa = " + rsmAlunos.getString("cd_pessoa")).executeQuery());
//				
//				int enderecosPrincipais = 0;
//				int cdEnderecoMaior = 0;
//				
//				while(rsmEndereco.next()){
//					if(rsmEndereco.getInt("lg_principal") == 1){
//						enderecosPrincipais++;
//					}
//					
//					if(rsmEndereco.getInt("cd_endereco") > cdEnderecoMaior){
//						cdEnderecoMaior = rsmEndereco.getInt("cd_endereco"); 
//					}
//				}
//				rsmEndereco.beforeFirst();
//				
//				if(enderecosPrincipais > 1){
//					while(rsmEndereco.next()){
//						if(rsmEndereco.getInt("cd_endereco") != cdEnderecoMaior){
//							Pessoa pessoa = PessoaDAO.get(rsmEndereco.getInt("cd_pessoa"), connection);
//							System.out.println("Aluno com endereÃ§os corrigidos: " + pessoa.getNmPessoa());
//							quantAlunosMod++;
//							connection.prepareStatement("UPDATE grl_pessoa_endereco SET lg_principal = 0 WHERE cd_pessoa = " + rsmEndereco.getInt("cd_pessoa") + " AND cd_endereco = " + rsmEndereco.getInt("cd_endereco")).executeUpdate();
//						}
//					}
//					rsmEndereco.beforeFirst();
//				}
//				
//			}
//			
//			System.out.println("Quantidade de alunos com endereÃ§os corrigidos: " + quantAlunosMod);
//			
//			
//			if (isConnectionNull)
//				connection.commit();
//			
//			System.out.println("\nFINALIZADO. ");
//			return new Result(1, "Executado com sucesso.");
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connection);
//			return null;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connection);
//		}
//	}	
	
	//RESOLVIDA
//	public static Result fixCursoDisciplina() {
//		return fixCursoDisciplina(null);
//	}
//	public static Result fixCursoDisciplina(Connection connection) {
//		boolean isConnectionNull = connection==null;
//		try {
//			if (isConnectionNull) {
//				connection = Conexao.conectar();
//				connection.setAutoCommit(false);
//			}
//			
//			System.out.println("INICIADO");
//			
//			ArrayList<Integer> cursosAnosIniciais = new ArrayList<Integer>();
//			cursosAnosIniciais.add(19);
//			cursosAnosIniciais.add(1197);
//			cursosAnosIniciais.add(1189);
//			cursosAnosIniciais.add(1161);
//			cursosAnosIniciais.add(1157);
//			cursosAnosIniciais.add(1155);
//			cursosAnosIniciais.add(1151);
//			cursosAnosIniciais.add(1153);
//			cursosAnosIniciais.add(1169);
//			cursosAnosIniciais.add(1167);
//			cursosAnosIniciais.add(1165);
//			cursosAnosIniciais.add(1163);
//			cursosAnosIniciais.add(1149);
//			cursosAnosIniciais.add(1191);
//			
//			
//			ArrayList<Integer> disciplinasAnosIniciais = new ArrayList<Integer>();
//			disciplinasAnosIniciais.add(207);
//			disciplinasAnosIniciais.add(209);
//			disciplinasAnosIniciais.add(210);
//			disciplinasAnosIniciais.add(211);
//			disciplinasAnosIniciais.add(212);
//			disciplinasAnosIniciais.add(214);
//			disciplinasAnosIniciais.add(215);
//			disciplinasAnosIniciais.add(216);
//			disciplinasAnosIniciais.add(217);
//			disciplinasAnosIniciais.add(218);
//			disciplinasAnosIniciais.add(219);
//			disciplinasAnosIniciais.add(222);
//			disciplinasAnosIniciais.add(226);
//			disciplinasAnosIniciais.add(228);
//			disciplinasAnosIniciais.add(229);
//			disciplinasAnosIniciais.add(220);
//			disciplinasAnosIniciais.add(213);
//			disciplinasAnosIniciais.add(230);
//			
//			for(Integer cdCurso : cursosAnosIniciais){
//				
//				
//				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
//				ResultSetMap rsmCursoModulo = CursoModuloServices.find(criterios, connection);
//				int cdCursoModulo = 0;
//				if(rsmCursoModulo.next()){
//					cdCursoModulo = rsmCursoModulo.getInt("cd_curso_modulo");
//				}
//				
//				int nrOrdem = 0;
//				for(Integer cdDisciplina : disciplinasAnosIniciais){
//					Result result = CursoDisciplinaServices.save(new CursoDisciplina(cdCurso.intValue(), cdCursoModulo, cdDisciplina.intValue(), 1/*cdMatriz*/, 0/*cdNucleo*/, 0/*qtCargaHoraria*/, nrOrdem, 0/*tpClassificacao*/, 0/*cdInstituicao*/), connection);
//					if(result.getCode() < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao inserir cursos disciplina");
//					}
//					nrOrdem++;
//				}
//				
//			}
//				
//			
//			
//			ArrayList<Integer> cursosAnosFinais = new ArrayList<Integer>();
//			cursosAnosFinais.add(89);
//			cursosAnosFinais.add(18);
//			cursosAnosFinais.add(1185);
//			cursosAnosFinais.add(1177);
//			cursosAnosFinais.add(1195);
//			cursosAnosFinais.add(1193);
//			cursosAnosFinais.add(1183);
//			cursosAnosFinais.add(1199);
//			cursosAnosFinais.add(1201);
//			cursosAnosFinais.add(1175);
//			cursosAnosFinais.add(1173);
//			cursosAnosFinais.add(1171);
//			
//			ArrayList<Integer> disciplinasAnosFinais = new ArrayList<Integer>();
//			disciplinasAnosFinais.add(205);
//			disciplinasAnosFinais.add(206);
//			disciplinasAnosFinais.add(207);
//			disciplinasAnosFinais.add(208);
//			disciplinasAnosFinais.add(209);
//			disciplinasAnosFinais.add(210);
//			disciplinasAnosFinais.add(211);
//			disciplinasAnosFinais.add(212);
//			disciplinasAnosFinais.add(214);
//			disciplinasAnosFinais.add(215);
//			disciplinasAnosFinais.add(216);
//			disciplinasAnosFinais.add(217);
//			disciplinasAnosFinais.add(218);
//			disciplinasAnosFinais.add(219);
//			disciplinasAnosFinais.add(222);
//			disciplinasAnosFinais.add(226);
//			disciplinasAnosFinais.add(228);
//			disciplinasAnosFinais.add(229);
//			disciplinasAnosFinais.add(221);
//			disciplinasAnosFinais.add(213);
//			disciplinasAnosFinais.add(230);
//			
//
//			for(Integer cdCurso : cursosAnosFinais){
//				
//				
//				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
//				ResultSetMap rsmCursoModulo = CursoModuloServices.find(criterios, connection);
//				int cdCursoModulo = 0;
//				if(rsmCursoModulo.next()){
//					cdCursoModulo = rsmCursoModulo.getInt("cd_curso_modulo");
//				}
//				
//				int nrOrdem = 0;
//				for(Integer cdDisciplina : disciplinasAnosFinais){
//					Result result = CursoDisciplinaServices.save(new CursoDisciplina(cdCurso.intValue(), cdCursoModulo, cdDisciplina.intValue(), 1/*cdMatriz*/, 0/*cdNucleo*/, 0/*qtCargaHoraria*/, nrOrdem, 0/*tpClassificacao*/, 0/*cdInstituicao*/), connection);
//					if(result.getCode() < 0){
//						if(isConnectionNull)
//							Conexao.rollback(connection);
//						return new Result(-1, "Erro ao inserir cursos disciplina");
//					}
//					nrOrdem++;
//				}
//				
//			}
//			System.out.println("FINALIZADO CURSO DISCIPLINA");
//			
//			if (isConnectionNull)
//				connection.commit();
//			
//			System.out.println("\nFINALIZADO. ");
//			return new Result(1, "Executado com sucesso.");
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connection);
//			return null;
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connection);
//		}
//	}
		
	public static Result fixPrcDtCadastro() {
		Connection connection = null;
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println("FIXING prc_processo.dt_cadastro");
			
			
			RandomAccessFile raf = new RandomAccessFile("E:\\tivic\\dtcadastro20162017.csv", "rw");
	  		String line = "";
	  		int i = 0;
			long start = System.currentTimeMillis();
			long total = 0;
			
			while((line = raf.readLine()) != null)	{
				total++;
			}
			raf.seek(0);
			
			System.out.println("FILE LOADED");
	  		
	  		int cdProcesso = 0;
	  		String nrProcesso = null;
	  		String dsDtCadastro = null;
			
	  		while((line = raf.readLine()) != null)	{
	  			i++;
	  			printProgress(start, total, i);
	  			
	  			StringTokenizer tokens = new StringTokenizer(line, ";", false);
	  			
	  			cdProcesso = Integer.parseInt(tokens.nextToken());
	  			nrProcesso = tokens.nextToken().replaceAll("\"", "");
	  			dsDtCadastro = tokens.nextToken().replaceAll("\"", "");
	  			
	  			if(cdProcesso<=0) {
	  				System.out.println("\nLINE "+i+"> cdProcesso NOT FOUND!");
	  				if(isConnectionNull)
	  		  			connection.rollback();
	  				return new Result(-2, "Erro: cdProcesso: "+cdProcesso);
	  			}
	  			
	  			PreparedStatement ps = connection
	  					.prepareStatement("UPDATE prc_processo SET dt_cadastro = '"+dsDtCadastro+"' "
	  							+ " WHERE cd_processo = "+cdProcesso
	  							+ " AND nr_processo = '"+nrProcesso+"'");
	  			if(ps.executeUpdate()<0) {
	  				System.out.println("\nLINE "+i+"> processo NOT UPDATED!");
	  				if(isConnectionNull)
	  		  			connection.rollback();
	  				return new Result(-2, "Erro: update failed");
	  			}
	  		}
	  		
	  		if(isConnectionNull)
	  			connection.commit();
	  		
	  		raf.close();
	  		
	  		System.out.println("\nTHE FIX IS DONE");
	  		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	  		
			return new Result(1, "Fix executado com sucesso.");				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixNrCategoriaEconomicaNivelUm() {
		Connection connection = null;
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println("FIXING adm_categoria_economica.nr_categoria_economica");
			
			
			PreparedStatement ps = connection
					.prepareStatement("SELECT cd_categoria_economica FROM adm_categoria_economica");
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			long start = System.currentTimeMillis();
			long total = rsm.size();
			int i = 0;
			
			while(rsm.next()) {
				i++;
	  			//printProgress(start, total, i, 100);
	  			
	  			//--
	  			
	  			CategoriaEconomica categoria = CategoriaEconomicaDAO.get(rsm.getInt("cd_categoria_economica"), connection);	  			
	  			String[] newNumber = categoria.getNrCategoriaEconomica().split("\\.");
	  			
	  			if(newNumber.length==0)
	  				continue;
	  			
	  			for(int j=1; j<newNumber.length; j++) {
	  				int n = Integer.parseInt(newNumber[j]);
		  			if(n<10) {
		  				newNumber[j] = "0"+n;
		  			}
	  			}
	  			
	  			categoria.setNrCategoriaEconomica(Util.join(newNumber, "", "."));
	  			
	  			if(CategoriaEconomicaDAO.update(categoria, connection)<0) {
	  				if(isConnectionNull)
	  					connection.rollback();
	  				System.out.println("FAILED");
	  				return new Result(-1, "Go now, My Lord, while there is time. There are places bellow.");
	  			}
			}
			
			connection.commit();
				
	  		System.out.println("\nThe field is lost. Everything is lost. The Black One has fallen from the sky, and the Towers in ruin lies.");
	  		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	  		
			return new Result(1, "Fix executado com sucesso.");				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixRestoreNmCategoriaEconomica() {
		Connection connection = null;
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println("FIXING adm_categoria_economica.nm_categoria_economica&nr_categoria_economica");
			
			
			RandomAccessFile raf = new RandomAccessFile("E:\\tivic\\restore.csv", "rw");
	  		String line = "";
	  		int i = 0;
			long start = System.currentTimeMillis();
			long total = 0;
			
			while((line = raf.readLine()) != null)	{
				total++;
			}
			raf.seek(0);
			
			System.out.println("FILE LOADED");
	  		
	  		int cdCategoria = 0;
	  		String nmCategoria = null;
	  		String nrCategoria = null;
			
	  		while((line = raf.readLine()) != null)	{
	  			i++;
	  			//printProgress(start, total, i, 100);
	  			
	  			StringTokenizer tokens = new StringTokenizer(line, ";", false);
	  			
	  			cdCategoria = Integer.parseInt(tokens.nextToken());
	  			nmCategoria = tokens.nextToken();
	  			nrCategoria = tokens.nextToken();
	  			
	  			if(cdCategoria<=0) {
	  				System.out.println("\nLINE "+i+"> cdCategoria NOT FOUND!");
	  				if(isConnectionNull)
	  		  			connection.rollback();
	  				return new Result(-2, "Erro: cdCategoria: "+cdCategoria);
	  			}
	  			
	  			PreparedStatement ps = connection
	  					.prepareStatement("UPDATE adm_categoria_economica SET nm_categoria_economica = '"+nmCategoria+"', nr_categoria_economica = '"+nrCategoria+"' "
	  							+ " WHERE cd_categoria_economica = "+cdCategoria);
	  			if(ps.executeUpdate()<0) {
	  				System.out.println("\nLINE "+i+"> processo NOT UPDATED!");
	  				if(isConnectionNull)
	  		  			connection.rollback();
	  				return new Result(-2, "Erro: update failed");
	  			}
	  		}
	  		
	  		if(isConnectionNull)
	  			connection.commit();
	  		
	  		raf.close();
	  		
	  		System.out.println("\nTHE FIX IS DONE");
	  		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	  		
			return new Result(1, "Fix executado com sucesso.");				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	@Deprecated
	public static Result deleteProcessoArquivo() {
		Connection connection = null;
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println("SENDING prc_processo_arquivo TO HELL...");
			
			ResultSetMap rsm = new ResultSetMap(connection
						.prepareStatement(" SELECT DISTINCT(cd_processo) "
										+ " FROM prc_processo_arquivo "
										+ " WHERE dt_arquivamento >= '2018-01-01 00:00:00' "
										+ " AND   dt_arquivamento <= '2018-05-31 23:59:59'")
						.executeQuery()
					);
			
	  		int i = 0;
			long start = System.currentTimeMillis();
			long total = rsm.size();
			
			while(rsm.next()) {
	  			i++;
	  			printProgress(start, total, i, 100);
	  			
	  			int cdProcesso = rsm.getInt("cd_processo");
	  			
	  			ResultSetMap rsmArq = new ResultSetMap(connection
						.prepareStatement("SELECT cd_arquivo FROM prc_processo_arquivo WHERE cd_processo = "+cdProcesso)
						.executeQuery()
					);
	  			
	  			while(rsmArq.next()) {
	  				
	  				int cdArquivo = rsmArq.getInt("cd_arquivo");
	  				
	  				connection
	  					.prepareStatement("DELETE FROM msg_regra_notificacao WHERE cd_processo="+cdProcesso+" AND cd_arquivo="+cdArquivo)
	  					.executeUpdate();
	  				
	  				connection
	  					.prepareStatement("DELETE FROM prc_processo_financeiro WHERE cd_processo="+cdProcesso+" AND cd_arquivo="+cdArquivo)
	  					.executeUpdate();
	  				
	  				connection
	  					.prepareStatement("DELETE FROM prc_processo_arquivo_agenda WHERE cd_processo="+cdProcesso+" AND cd_arquivo="+cdArquivo)
	  					.executeUpdate();
	  				
	  			}
	  			
	  			connection
					.prepareStatement("DELETE FROM prc_processo_arquivo WHERE cd_processo="+cdProcesso)
					.executeUpdate();
			}
	  		
	  		if(isConnectionNull)
	  			connection.commit();
	  		
	  		System.out.println("\nDONE!");
	  		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	  		
			return new Result(1, "Fix executado com sucesso.");				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
		
	public static Result fixVisibilidadeAndamento() {
		Connection connection = null;
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println("UPDATE tp_visibilidade ON prc_processo_andamento...");
			
			ResultSetMap rsm = TipoAndamentoDAO.getAll(connection);
			
			int done = 0;
			long start = System.currentTimeMillis();
			long total = rsm.size();
			
			int batch = rsm.size() / 10;
			int left = rsm.size();
			
			while(rsm.next()) {
				done++;
	  			printProgress(start, total, done, 100);
				
				PreparedStatement statement = connection
						.prepareStatement("UPDATE prc_processo_andamento SET tp_visibilidade = ? WHERE cd_tipo_andamento = ?");
				statement.setInt(1, rsm.getInt("tp_visibilidade"));
				statement.setInt(2, rsm.getInt("cd_tipo_andamento"));
				
				int retorno = statement.executeUpdate();
				
				if(retorno<0) {
					if(isConnectionNull)
						connection.rollback();
					return new Result(-2, "Erro durante a execuï¿½ï¿½o do FIX");
				}
				
				if(done % batch == 0 || left < batch) {
					connection.commit();
					Conexao.desconectar(connection);
					
					connection = Conexao.conectar();
					connection.setAutoCommit(false);
				}
			}
			
			
			
	  		System.out.println("\nDONE!");
	  		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	  		
			return new Result(1, "Fix executado com sucesso.");				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixNovoProcessoPublicacoesOnline() {
		return fixNovoProcessoPublicacoesOnline("2018-11-22");
	}
	
	public static Result fixNovoProcessoPublicacoesOnline(String dtInicial) {
		Connection connection = null;
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println("WORKING ON...");
			
			ResultSetMap rsm = new ResultSetMap(connection
					.prepareStatement(
							" select nr_processo, nr_antigo, get_clientes(cd_processo) as nm_cliente, get_contra_parte(cd_processo) as nm_adverso "
						  + " from prc_processo "
						  + " where dt_cadastro >= '"+dtInicial+" 00:00:00' "
						  + " and nr_processo is not null")
					.executeQuery());
			
			int done = 0;
			long start = System.currentTimeMillis();
			long total = rsm.size();
			
			StringBuilder builder = new StringBuilder();
			
			while(rsm.next()) {
				done++;
	  			//printProgress(start, total, done, 100);
				
	  			builder.append("{");
	  			builder.append("\"numero\":\""+rsm.getString("nr_processo")+"\","
	  						 + "\"autor\":\""+rsm.getString("nm_cliente")+"\","
	  						 + "\"reu\":\""+rsm.getString("nm_adverso")+"\"");
	  			builder.append("}");
	  			
	  			if(rsm.getString("nr_antigo", null)!=null) {
	  				builder.append(",{");
		  			builder.append("\"numero\":\""+rsm.getString("nr_antigo")+"\","
		  						 + "\"autor\":\""+rsm.getString("nm_cliente")+"\","
		  						 + "\"reu\":\""+rsm.getString("nm_adverso")+"\"");
		  			builder.append("}");
	  			}
	  			
				if(rsm.hasMore())
					builder.append(",");
			}

			Client client = ClientBuilder.newClient();
			WebTarget target = client.target("https://publicacoesonline.com.br/sarmento_silva_processos.php");

			Response response = target.request(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON).post(Entity.json("["+builder.toString()+"]"));
			response.bufferEntity();

			
			if(response.getStatus() != 200) {
				LogUtils.info("Erro (-1)! sendNewProcessPublicacoesOnline: "+response.toString());
				return new Result(-1, response.readEntity(String.class));
			}
			
			JSONObject jsoResponse = new JSONObject(response.readEntity(String.class));
			
			if(!jsoResponse.getBoolean("success")) {
				LogUtils.info("Erro (-2)! sendNewProcessPublicacoesOnline: "+jsoResponse.getString("message"));
				return new Result(-2, jsoResponse.getString("message"), "JSON", jsoResponse);
			}
			
			System.out.println("\nSucesso! response: "+jsoResponse.getString("message"));
			
	  		System.out.println("\nDONE!");
	  		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	  		
			return new Result(1, "Fix executado com sucesso.", "JSON", jsoResponse);				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			
			if(e.getClass()==ConnectException.class)
				return new Result(-3, "Erro! FixServices.fixNovoProcessoPublicacoesOnline: "+e.getMessage());
			
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Imprime uma barra de progresso no log do tomcat em tempo real
	 * @throws InterruptedException 
	 */
	private static void printProgress(long startTime, long total, long current) throws InterruptedException {
		printProgress(startTime, total, current, 0);
	}
	
	private static void printProgress(long startTime, long total, long current, long sleeptime) throws InterruptedException {
	    long eta = current == 0 ? 0 : 
	        (total - current) * (System.currentTimeMillis() - startTime) / current;

	    String etaHms = current == 0 ? "N/A" : 
	            String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
	                    TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
	                    TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));

	    StringBuilder string = new StringBuilder(140);   
	    int percent = (int) (current * 100 / total);
	    string
	        .append('\r')
	        .append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
	        .append(String.format(" %d%% [", percent))
	        .append(String.join("", Collections.nCopies(percent, "=")))
	        .append('>')
	        .append(String.join("", Collections.nCopies(100 - percent, " ")))
	        .append(']')
	        .append(String.join("", Collections.nCopies((int) (Math.log10(total)) - (int) (Math.log10(current)), " ")))
	        .append(String.format(" %d/%d, ETA: %s", current, total, etaHms));
	    
	    System.out.print(string);
	    
	    if(sleeptime>0)
	    	Thread.sleep(sleeptime);
	}

	public static void ultimoAndamentoNoProcesso() {
		Connection connection = null;
		Boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			System.out.println("Adicionando a data do ultimo andamento, no campo \"dt ultimo and\"");
			
			ResultSetMap rsm = new ResultSetMap(connection
					.prepareStatement("SELECT cd_processo FROM prc_processo WHERE dt_cadastro >= '2018-01-01 00:00:00'")
					.executeQuery());
			int cont = 0;
			while(rsm.next()){
				Processo processo = ProcessoDAO.get(rsm.getInt("CD_PROCESSO"), connection);
				
				ProcessoAndamento processoAndamento = ProcessoAndamentoServices.getUltimoAndamento(rsm.getInt("CD_PROCESSO"), connection);
				
				processo.setDtUltimoAndamento(processoAndamento.getDtAndamento());
				
				int retorno = ProcessoDAO.update(processo, connection);
					if(retorno<0) {
						connection.rollback();
						System.out.println("ERRO AO ATUALIZAR A DATA DO ULTIMO ANDAMENTO!");
						return;
					}
					cont ++;
					System.out.println(cont + " Processos altrados!");
			}
			connection.commit();

		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static void fixImagemRadar() {
		Connection connection = null;
		Boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("Processando imagens do radar...");
			
			String secretKey = "sk_DEMODEMODEMODEMODEMODEMO";
		    String u = "https://api.openalpr.com/v2/recognize_bytes?recognize_vehicle=1&country=br&secret_key=" + secretKey;
		    
			ResultSetMap rsm = new ResultSetMap(connection
					.prepareStatement(
					  " SELECT * " + 
					  " FROM mob_evento_equipamento A," + 
                      " mob_evento_arquivo B " + 
					  " WHERE A.cd_equipamento = 420 " +
                      "   AND A.cd_evento = B.cd_evento " +
                      "   AND B.tp_arquivo = 0 " + 
                      "   AND A.lg_olpr is null " + 
                      "   AND (A.nr_placa is null OR A.nr_placa = '')")
					.executeQuery());
			
			int cont = 0;		
			String log = "";
			
			while(rsm.next()){				
				cont++;
				
				Arquivo arquivo = ArquivoDAO.get(rsm.getInt("CD_ARQUIVO"), connection);		
				
				if((cont % 2) != 0) {
					continue;
				}		
												
				String base64 = new String(Base64.getEncoder().encode(arquivo.getBlbArquivo()));
				log += "-- Evento " + rsm.getInt("CD_EVENTO") + " | "
							+ " Arquivo " + rsm.getInt("CD_ARQUIVO") + ": " + (arquivo.getBlbArquivo().length / 1024) + " KB | "
							+ " Dt. Conclusï¿½o: " + rsm.getDateFormat("DT_CONCLUSAO", "dd/MM/yyyy HH:mm:ss") + "\n\n";
				
				
				URL url = new URL(u);
				URLConnection con = url.openConnection();
				HttpURLConnection http = (HttpURLConnection)con;
				http.setRequestMethod("POST"); // PUT is another valid option
				http.setDoInput(true);
				http.setDoOutput(true);
				
				OutputStream os = http.getOutputStream();
				BufferedWriter writer = new BufferedWriter(
				            new OutputStreamWriter(os, "UTF-8"));
				writer.write(base64);
				writer.flush();
				writer.close();
				
				os.close();

				http.connect();

		        String response = "";
				int responseCode=http.getResponseCode();

				 String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(http.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
	            
                JSONObject obj = new JSONObject(response);
                String nrPlaca = "";
                
                try {
            		if(obj.get("results")!=null && ((JSONArray)obj.get("results")).length() > 0) {
            			nrPlaca = String.valueOf(((JSONObject)((JSONArray)obj.get("results")).get(0)).get("plate"));
                    }                	
                } catch(JSONException jex) { 
                	System.out.println("Erro: "+jex.getMessage());               
                }
                
                String sql = "update mob_evento_equipamento set nr_placa = '"+nrPlaca+"', lg_olpr = 1 where cd_evento = " + rsm.getInt("CD_EVENTO");
                
                System.out.println(sql);
                
                connection.prepareStatement(sql).executeUpdate();
			}
			
			System.out.println("C:\\tivic\\openalpr_radar_"+Util.formatDate(new GregorianCalendar(), "ddMMyyyy")+"_"+Util.formatDate(new GregorianCalendar(), "HHmm")+".log");

			LogUtils.fileLog("C:\\tivic\\openalpr_radar_"+Util.formatDate(new GregorianCalendar(), "ddMMyyyy")+"_"+Util.formatDate(new GregorianCalendar(), "HHmm")+".log", log);

			System.out.println(cont + " imagens do radar processadas.");
			
			
			connection.commit();

		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static void fixMobAitVlMulta() {
		Connection conn = null;
		try {
			conn = Conexao.conectar();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("SELECT A.codigo_ait, A.cod_infracao"
					+ " FROM ait A"
					+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
					+ " WHERE A.vl_multa = 0 OR A.vl_multa IS NULL"
					+ " AND B.tp_equipamento = ?"
					+ " AND A.cd_agente <> 12");
			ps.setInt(1, EquipamentoServices.TALONARIO_ELETRONICO);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			System.out.println(rsm.getLimit()+" AITS encontrados");
			
			while(rsm.next()) {
				System.out.print("+");
				if(!rsm.hasMore())
					System.out.println();					
				
				float vlMulta = 0.0f;
				ps = conn.prepareStatement("SELECT vl_infracao FROM infracao WHERE cod_infracao = ?");
				ps.setInt(1, rsm.getInt("cod_infracao"));
				
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					vlMulta = rs.getFloat("vl_infracao");
				}
				
				if(vlMulta == 0.0f) {
					System.out.println("Erro ao atualizar ait "+rsm.getInt("codigo_ait"));
					conn.rollback();
					return;
				}
				
				ps = conn.prepareStatement("UPDATE ait SET vl_multa = ? WHERE codigo_ait = ?");
				ps.setFloat(1,vlMulta);
				ps.setInt(2, rsm.getInt("codigo_ait"));
				int r = ps.executeUpdate();
				if(r < 0) {
					System.out.println("Erro ao atualizar ait "+rsm.getInt("codigo_ait"));
					conn.rollback();
					return;
				}
				System.out.println(rsm.getInt("codigo_ait")+" atualizado\t"+(rsm.getPointer()+1)+"/"+rsm.getLines().size());
			}
			conn.commit();
			System.out.println("CONCLUÍDO.");
		} catch(Exception e) {
			Conexao.rollback(conn);
			e.printStackTrace();
		} finally {
			Conexao.desconectar(conn);
		}
	}
	
	public static void fixMobGrlCidade() {
		

		Connection connOrigem = Conexao.conectar("org.postgresql.Driver", "jdbc:postgresql://192.168.1.60:25432/etransitovca", "postgres", "433UQM!");
		Connection connDestino = Conexao.conectar(); 
		
		
		try {
			connDestino.setAutoCommit(false);
			PreparedStatement ps = connOrigem.prepareStatement("SELECT cod_municipio, nm_municipio, nm_uf FROM municipio");
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			while(rsm.next()) {
				System.out.println("Cidade: " + rsm.getString("nm_municipio"));
				
				Estado estado = EstadoServices.getBySg(rsm.getString("nm_uf"));
				
				Cidade cidade = new Cidade();
				
				cidade.setIdCidade(rsm.getString("cod_municipio"));
				cidade.setNmCidade(rsm.getString("nm_municipio").replaceAll("'", "´"));
				
				cidade.setCdEstado(estado!=null ? estado.getCdEstado() : 0);
				
				System.out.println("Cidade Salva: " + cidade.getNmCidade());
				
				Result r = CidadeServices.save(cidade, connDestino);
				if(r.getCode() == -100) {
					continue;
				} else if(r.getCode() <= 0) {
					System.out.println(r.getMessage());
					connDestino.rollback();
					return;
				}
			}
			connDestino.commit();
			System.out.println("Concluído");
		} catch (SQLException e) {
			Conexao.rollback(connDestino);
			e.printStackTrace();
		}
		finally {
			Conexao.desconectar(connDestino);
			Conexao.desconectar(connOrigem);
		}
	}
	/**
	 * 1. Buscar o csv
	 * 2. carregar o csv no rsm
	 * 3. iterar o rsm por cada processo
	 * 4. alterar grupo de processo
	 * @author darllan
	 * @category Juris - Processos
	 */
	
	public static Result fixAlterarGrupoProcesso(){
		return fixAlterarGrupoProcesso(null);
	}
	
	public static Result fixAlterarGrupoProcesso(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			//1./2. csv
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("C:\\Users\\darll\\Documents\\grupo_processo.csv", ";", true);
			//3.
			while(rsm.next()){
				//4. buscar processo
				String nrProcesso = rsm.getString("NR_PROCESSO");
				Processo processo = ProcessoServices.getProcessoByNrProcesso(nrProcesso, connection);
				if(processo == null) {
					System.out.println("ERRO! no processo "+rsm.getString("NR_PROCESSO"));
				
				} else {
					processo.setCdGrupoProcesso(Integer.parseInt(rsm.getString("CD_GRUPO_PROCESSO")));
					
					Result r= ProcessoServices.save(processo, connection);
					if (r.getCode() <= 0) {
						System.out.println("ERRO! no processo "+rsm.getString("NR_PROCESSO"));
						connection.rollback();
						return new Result(-1, "ERRO! no processo "+rsm.getString("NR_PROCESSO"));
					}
					System.out.println(rsm.getString("NR_PROCESSO")+" >>>>>> OK");
				}
			}
			
			System.out.println("Alterações realizadas com sucesso.");
			
			connection.commit();
			return new Result(1, "Alterações realizdas com sucesso.");
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixDtVencimentoAits(){
		return fixDtVencimentoAits(null);
	}
	
	public static Result fixDtVencimentoAits(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			System.out.println("Iniciando verificação...");

			int index = 1;
			String line = "";
			String fileDir = "F:\\AUTUACAO.CSV";
			String tokenSeparator = ";";
			ArrayList<HashMap<String, Object>> aitsImportacao = new ArrayList<HashMap<String, Object>>();
			
			BufferedReader raf = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
				
			System.out.println("Pesquisando...");
			int contAit = 0;
			FileWriter arq = new FileWriter("F:\\ait-data-invalida.txt");
			while ((line = raf.readLine()) != null) 
			{
				
				if(index <= 2) 
				{	
					index++;
					continue;
				}
				
				String nrPlaca = null;
				String nrAit = null;
				String dtLimiteRecurso = null;
				
				line.split(";");
				
				String collums[] = line.split(";");	
				
				//System.out.println("Coluna de placa: " + collums[0]);
				nrPlaca = collums[0];
				
				//System.out.println("Coluna de id ait: " + collums[7]);
				nrAit = collums[7];
				
				//System.out.println("Coluna de data: " + collums[35]);
				dtLimiteRecurso = collums[35];
	
				GregorianCalendar dtVencimento = null;

				if(!dtLimiteRecurso.trim().equals("0")) 
				{
					dtVencimento = new GregorianCalendar();
					String year = dtLimiteRecurso.substring(0, 4);
					String month = dtLimiteRecurso.substring(4, 6);
					String day = dtLimiteRecurso.substring(6, 8);
					String[] data = {year, month, day};
						
					dtVencimento.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
						
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("nrPlaca", nrPlaca);
					register.put("nrAit", nrAit);
					register.put("dtVencimento", dtVencimento);
						
					if (nrAit != null)
					{
						System.out.println("Nr ait diferente de nulo");
						boolean verify = verificarAit(nrAit, nrPlaca, dtVencimento, data, arq, dtLimiteRecurso, connection); 
						if (verify)
						{
							contAit += 1;
						}
					}
					else
					{
						System.out.println("Ait com nrAit nullo ou data invalida: " + "nº " + nrAit + " Placa: " + nrPlaca + "Data: " + dtLimiteRecurso);
					}
						
					aitsImportacao.add(register);
						
				}
					
//				if (contAit >= 1)
//				{
//					System.out.println("Fim loop..");
//					break;
//				}
					
				index++;
					
			}
			
			arq.close();
			System.out.println("Número de registros: " + aitsImportacao.size());
			System.out.println("Alterações realizadas com sucesso.");
			//connection.commit();
			return new Result(1, "Alterações realizdas com sucesso.");
		}
		catch (Exception e) 
		{
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally 
		{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	@SuppressWarnings("static-access")
	private static boolean verificarAit(String nrAit, String nrPlaca, GregorianCalendar dataVencimento, String[] data, FileWriter arq, String dtLimiteRecurso, Connection connect)
	{
		System.out.println("Verificando ait...");
		DateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY");
		
		try
		{
			AitServices aitSearch = new AitServices();
			ResultSetMap ait = new ResultSetMap();
			
			ait = aitSearch.find(criteriosSeachAit(nrAit, nrPlaca), connect);
			
			System.out.println("Result search: " + ait);
			
			if (ait.next())
			{
				System.out.println("--------------------------------");
				System.out.println("AitEncontrado: " + ait.getInt("CD_AIT"));
				System.out.println("Id Ait: " + nrAit);
				System.out.println("Data de vencimento: " + ait.getDateFormat("DT_VENCIMENTO", "YY/MM/dd"));
				System.out.println("Placa: " + nrPlaca);
				System.out.println("--------------------------------");
				System.out.println("Data CSV: " + dtLimiteRecurso);
				System.out.println("Data limite encontrada no CSV formatada: " + dateFormat.format(dataVencimento.getTime()));
				
				System.out.println("Date in Array: " + "Year: " + data[0] + " Month: " + data[1] + " Day: " + data[2]);
				System.out.println("--------------------------------");
				
				/*Chamar metodo para validar data*/
				boolean dataValida = validarDataVencimento(data, nrAit);
				/*-------------------------------*/
				
				if (dataValida)
				{
					updateDtVencimento(ait.getInt("CD_AIT"), dataVencimento, connect);
				}
				else
				{
					System.out.println("----------Ait com data invalida----------");
					System.out.println("Código do AIT: " + ait.getInt("CD_AIT"));
					System.out.println("Id Ait: " + nrAit);
					System.out.println("Data de vencimento: " + ait.getDateFormat("DT_VENCIMENTO", "YY/MM/dd"));
					System.out.println("Placa: " + nrPlaca);
					System.out.println("Data limite encontrada no CSV: " + dateFormat.format(dataVencimento.getTime()));
					System.out.println("--------------------------------------------");
					salvarAitsComDataInvalida(ait, dtLimiteRecurso, arq);
				}
				
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			System.out.println("Erro in verificarAit() : " + e.getMessage());
			return false;
		}
	
	}
	
	
	private static ArrayList<ItemComparator> criteriosSeachAit(String nrAit, String nrPlaca)
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		criterios.add(new ItemComparator("A.id_ait", nrAit, ItemComparator.EQUAL, Types.CHAR));
		criterios.add(new ItemComparator("A.nr_placa", nrPlaca, ItemComparator.EQUAL, Types.CHAR));
		criterios.add(new ItemComparator("K.tp_status", String.valueOf(AitMovimentoServices.NIP_ENVIADA), ItemComparator.EQUAL, Types.INTEGER));
		return criterios;
	}
	
	
	@SuppressWarnings("static-access")
	private static void updateDtVencimento(int cdAit, GregorianCalendar dtVencimento, Connection connect)
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		System.out.println("Faznedo update............................");
		Ait ait = new Ait();
		AitDAO aitSearch = new AitDAO();
		
		ait = AitDAO.get(cdAit, connect);
		ait.setDtVencimento(dtVencimento);
		aitSearch.update(ait);
		
		System.out.println("cdAit: " + ait.getCdAit());
		System.out.println("idAit: " + ait.getIdAit());
		System.out.println("Data de vencimento: " + dateFormat.format(ait.getDtVencimento().getTime()));
		System.out.println("Placa: " + ait.getNrPlaca());
		
		System.out.println("---------------------------------------------");
	}
	
	private static boolean validarDataVencimento(String[] data, String idAit)
	{
		System.out.println("Checando data do AIT de id: " + idAit);
		boolean dataValida = true;
		
		if ((Integer.parseInt(data[0]) < 2000) || (Integer.parseInt(data[0]) > 2021))
		{
			System.out.println("Ano invalido: " + data[0]);
			dataValida = false;
		}
		
		if ((Integer.parseInt(data[1]) < 01) || (Integer.parseInt(data[1]) > 12))
		{
			System.out.println("Mês invalido: " + data[1]);
			dataValida = false;
		}
		
		if ((Integer.parseInt(data[2]) < 01) || (Integer.parseInt(data[2]) > 31))
		{
			System.out.println("Dia invalido: " + data[2]);
			dataValida = false;
		}
		
		return dataValida;
	}
	
	private static void salvarAitsComDataInvalida(ResultSetMap ait, String dataVencimento, FileWriter arq)
	{
		System.out.println("Salvando arquivo com falha");
		
		try 
		{	
			PrintWriter gravarArq = new PrintWriter(arq);
			gravarArq.printf( ait.getInt("CD_AIT") + " | " + ait.getString("ID_AIT")  + " | " + ait.getString("NR_PLACA")  + " | " + dataVencimento + "\n");	    
			gravarArq.printf("+--------------------------------------------------------------------------------------------------------+%n");
			System.out.printf("Arquivo salvo com sucesso");
		}
		catch (Exception e)
		{
			System.out.println("Erro ao gravar arquivo de log");
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public static Result fixEventosAntigosRadar(){
		return fixEventosAntigosRadar(null);
	}
	
	public static Result fixEventosAntigosRadar(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			SimpleDateFormat logSDF = new SimpleDateFormat("dd-MM-yyyy_HH-mm");

			File logDir  = new File(ContextManager.getRealPath() + "/WEB-INF/logs/fixEventosAntigosRadar");
			
			if (!logDir.exists()) logDir.mkdirs();
			
			File logFile = new File(logDir.getAbsolutePath()+"/"+logSDF.format(new Date())+".log");
			
			if(!logFile.exists()) {
				logFile.createNewFile();
			}

			PrintStream fileStream = new PrintStream(logFile);
			System.setOut(fileStream);

			Result r = new Result(-1);
			
			String driver = ManagerConf.getInstance().get("RADAR_DRIVER");
			String url = ManagerConf.getInstance().get("RADAR_DBPATH");
			String login = ManagerConf.getInstance().get("RADAR_LOGIN");
			String senha = ManagerConf.getInstance().get("RADAR_PASS");
			

			String dtInicialConf = ManagerConf.getInstance().get("DT_INICIAL_EVENTOS_SYNC");
			String dtFinalConf = ManagerConf.getInstance().get("DT_FINAL_EVENTOS_SYNC");
			
			Calendar calendar = new GregorianCalendar();
			GregorianCalendar dtInicial = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)-1, 01, 0, 0, 0);
			GregorianCalendar dtFinal   = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 31, 23, 59, 59);
			
			String nmOrgaoAutuador = ManagerConf.getInstance().get("RADAR_ORGAO_AUTUADOR");

			if (driver == null || url == null || login == null || senha == null || nmOrgaoAutuador == null) {
				System.out.println("Impossível sincronizar com o servidor do radar. Valores de configurações inexistentes.");
				return new Result(-2, "Impossível sincronizar com o servidor do radar. Valores de configurações inexistentes.");
			}
			
			if((dtInicialConf != null && dtFinalConf == null) || (dtInicialConf == null && dtFinalConf != null)) {
				System.out.println("Ao informar uma data de sincronização, obrigatoriamente ambas devem ser preenchidas. (DT_INICIAL_EVENTOS_SYNC, DT_FINAL_EVENTOS_SYNC)");
				return new Result(-2, "Ao informar uma data de sincronização, obrigatoriamente ambas devem ser preenchidas. (DT_INICIAL_EVENTOS_SYNC, DT_FINAL_EVENTOS_SYNC)");				
			}
			
			if(dtInicialConf != null)
				dtInicial = DateUtil.convStringToCalendar(dtInicialConf);
			
			if(dtFinalConf != null)
				dtFinal = DateUtil.convStringToCalendar(dtFinalConf);		

			if(dtInicial.after(dtFinal)) {
				System.out.println("A data inicial não pode ser anterior a data final.");
				return new Result(-2, "A data inicial não pode ser anterior a data final.");				
			}

			Connection connectRS = Conexao.conectar(driver, url, login, senha);
						
			pstmt = connection.prepareStatement(" SELECT * FROM mob_evento_equipamento A, mob_tipo_evento B "
					+ " WHERE A.cd_tipo_evento = B.cd_tipo_evento "
					+ " AND B.id_tipo_evento IN ('ASV', 'VAL', 'PSF', 'RLP', 'CEX', 'LNP', 'LHP', 'CLP') "
					+ " AND A.nm_orgao_autuador = 'PMVC'"
					+ " AND A.dt_conclusao >= ? "
					+ " AND A.dt_conclusao <= ? "
					+ " ORDER BY A.nm_equipamento, A.dt_conclusao ");
			
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));

			ResultSetMap rsmEventos = new ResultSetMap(pstmt.executeQuery());
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			ArrayList<HashMap<String, Object>> eventos = new ArrayList<HashMap<String, Object>>(); 

			while(rsmEventos.next()) {
				String nmEquipamento             = rsmEventos.getString("NM_EQUIPAMENTO");
				GregorianCalendar dtEvento       = rsmEventos.getGregorianCalendar("DT_EVENTO");
				GregorianCalendar dtConclusao    = rsmEventos.getGregorianCalendar("DT_CONCLUSAO");
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("dtEvento", dtEvento);
				register.put("nmEquipamento", nmEquipamento);
				register.put("dtConclusao", dtConclusao);
				
				eventos.add(register);
			}
			
			PreparedStatement pstmtRS = connectRS
					.prepareStatement(" SELECT * FROM mob_evento_equipamento A, mob_tipo_evento B "
							+ " WHERE A.cd_tipo_evento = B.cd_tipo_evento "
							+ " AND B.id_tipo_evento IN ('ASV', 'VAL', 'PSF', 'RLP', 'CEX', 'LNP', 'LHP', 'CLP') "
							+ " AND A.st_evento <> " + EventoEquipamentoServices.ST_EVENTO_NAO_PROCESSADO
							+ " AND A.nm_orgao_autuador = 'PMVC' "
							+ " AND A.dt_conclusao >= ? "
							+ " AND A.dt_conclusao <= ? "
							+ " ORDER BY A.nm_equipamento, A.dt_conclusao ");
					
			pstmtRS.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmtRS.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			

			ResultSetMap rsmEventosRadar = new ResultSetMap(pstmtRS.executeQuery());
			
			List<HashMap<String, Object>> eventosFiltrados = rsmEventosRadar.getLines().stream().filter(evento -> {
				String nmEquipamento             = (String) evento.get("NM_EQUIPAMENTO");
				GregorianCalendar dtEvento       = new GregorianCalendar();
				dtEvento.setTime(new Date(((Timestamp)evento.get("DT_EVENTO")).getTime()));
				GregorianCalendar dtConclusao    = new GregorianCalendar();
				dtConclusao.setTime(new Date(((Timestamp)evento.get("DT_CONCLUSAO")).getTime()));
				
				return !eventos.stream().anyMatch(ev -> (nmEquipamento.equals(ev.get("nmEquipamento")) && dtEvento.equals(ev.get("dtEvento")) && dtConclusao.equals(ev.get("dtConclusao"))));
			}).collect(Collectors.toList());
			
			
			ResultSetMap rsmEventosFiltrados = new ResultSetMap();
			rsmEventosFiltrados.setLines(new ArrayList<HashMap<String, Object>>(eventosFiltrados));
			
			System.out.println("[FIX_SERVICES] Verificação de eventos restantes executado em "+sdf.format(new Date())+", período de " + sdf.format(dtInicial.getTime()) + " até " + sdf.format(dtFinal.getTime()));
			System.out.println("[RADAR] Total de eventos: " + rsmEventosRadar.size());
			System.out.println("[PMVC] Total de eventos: " + eventos.size());
			System.out.println("[RADAR] Diferença de eventos: " + eventosFiltrados.size() + "\n\n");
			
			while(rsmEventosFiltrados.next()) {
				boolean exists = false;
				Equipamento equipamento = EquipamentoServices.getByIdEquipamento(rsmEventosFiltrados.getString("NM_EQUIPAMENTO"), connection);
				TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento(rsmEventosFiltrados.getString("ID_TIPO_EVENTO"), connection);
				
				EventoEquipamento evento = new EventoEquipamento(0, equipamento.getCdEquipamento(),
						tipoEvento.getCdTipoEvento(), rsmEventosFiltrados.getGregorianCalendar("DT_EVENTO"),
						rsmEventosFiltrados.getString("NM_ORGAO_AUTUADOR"), rsmEventosFiltrados.getString("NM_EQUIPAMENTO"),
						rsmEventosFiltrados.getString("DS_LOCAL"), rsmEventosFiltrados.getString("ID_IDENTIFICACAO_INMETRO"),
						rsmEventosFiltrados.getGregorianCalendar("DT_AFERICAO"), rsmEventosFiltrados.getInt("NR_PISTA"),
						rsmEventosFiltrados.getGregorianCalendar("DT_CONCLUSAO"), rsmEventosFiltrados.getInt("VL_LIMITE"),
						rsmEventosFiltrados.getInt("VL_VELOCIDADE_TOLERADA"), rsmEventosFiltrados.getInt("VL_MEDIDA"),
						rsmEventosFiltrados.getInt("VL_CONSIDERADA"), rsmEventosFiltrados.getString("NR_PLACA"),
						rsmEventosFiltrados.getInt("LG_TEMPO_REAL"), rsmEventosFiltrados.getInt("TP_VEICULO"),
						rsmEventosFiltrados.getInt("VL_COMPRIMENTO_VEICULO"), rsmEventosFiltrados.getInt("ID_MEDIDA"),
						rsmEventosFiltrados.getString("NR_SERIAL"), rsmEventosFiltrados.getString("NM_MODELO_EQUIPAMENTO"),
						rsmEventosFiltrados.getString("NM_RODOVIA"), rsmEventosFiltrados.getString("NM_UF_RODOVIA"),
						rsmEventosFiltrados.getString("NM_KM_RODOVIA"), rsmEventosFiltrados.getString("NM_METROS_RODOVIA"),
						rsmEventosFiltrados.getString("NM_SENTIDO_RODOVIA"), rsmEventosFiltrados.getInt("ID_CIDADE"),
						rsmEventosFiltrados.getString("NM_MARCA_EQUIPAMENTO"), rsmEventosFiltrados.getInt("TP_EQUIPAMENTO"),
						rsmEventosFiltrados.getInt("NR_PISTA"), rsmEventosFiltrados.getGregorianCalendar("DT_EXECUCAO_JOB"),
						rsmEventosFiltrados.getString("ID_UUID"), rsmEventosFiltrados.getInt("TP_RESTRICAO"),
						rsmEventosFiltrados.getInt("TP_CLASSIFICACAO"), rsmEventosFiltrados.getInt("VL_PERMANENCIA"),
						rsmEventosFiltrados.getInt("VL_SEMAFORO_VERMELHO"), rsmEventosFiltrados.getInt("ST_EVENTO"),
						rsmEventosFiltrados.getInt("CD_MOTIVO_CANCELAMENTO"), rsmEventosFiltrados.getString("TXT_EVENTO"),
						rsmEventosFiltrados.getInt("LG_OPLR"), rsmEventosFiltrados.getGregorianCalendar("DT_CANCELAMENTO"),
						rsmEventosFiltrados.getInt("CD_USUARIO_CANCELAMENTO"),
						rsmEventosFiltrados.getGregorianCalendar("DT_PROCESSAMENTO"), 0, 0, 0, rsmEventosFiltrados.getInt("VL_TOLERANCIA"));
				
				PreparedStatement pstmtb = connection.prepareStatement(
					"SELECT COUNT(cd_evento) as total FROM mob_evento_equipamento A, mob_tipo_evento B WHERE dt_conclusao = ? and dt_evento = ? and nm_equipamento = ?"
				);
				pstmtb.setTimestamp(1, new Timestamp(evento.getDtConclusao().getTimeInMillis()));
				pstmtb.setTimestamp(2, new Timestamp(evento.getDtEvento().getTimeInMillis()));
				pstmtb.setString(3, evento.getNmEquipamento());
				ResultSet rs = pstmtb.executeQuery();
				
				if(rs.next())
					exists = rs.getInt("total") > 0;
					
				if(!exists) {

					System.out.println("Sincronizando evento do radar " + rsmEventosFiltrados.getString("NM_EQUIPAMENTO") + " do dia " + sdf.format(rsmEventosFiltrados.getGregorianCalendar("DT_EVENTO").getTime()));
					
					r = EventoEquipamentoServices.save(evento, null, connection);
					
					if (r.getCode() > 0) {

						evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());

						pstmtRS = connectRS.prepareStatement("SELECT * FROM mob_evento_arquivo A, grl_arquivo B "
								+ " WHERE A.cd_arquivo = B.cd_arquivo " + " AND A.cd_evento = ? "
								+ " AND A.tp_arquivo = " + EventoArquivoServices.TP_ARQUIVO_FOTO, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
						pstmtRS.setInt(1, rsmEventosFiltrados.getInt("CD_EVENTO"));

						ResultSet rsEventoArquivosRS = pstmtRS.executeQuery();
						int size= 0;  
						if (rsEventoArquivosRS!= null)   
						{  
							rsEventoArquivosRS.beforeFirst();  
							rsEventoArquivosRS.last();  
						  size = rsEventoArquivosRS.getRow();  
						}
						System.out.println("Quantidade de arquivos: " + size);
						
						if(size == 0) {
							System.out.println("--- Evento sem imagem, ignorando...");
							continue;
						}

						rsEventoArquivosRS.beforeFirst();  
						// 5. GRAVAR ARQUIVOS VINCULADOS AO EVENTO LOCALMENTE
						while (rsEventoArquivosRS.next()) {

							// 5.1. GRAVAR ARQUIVO
							
							System.out.println(rsEventoArquivosRS.getString("NM_ARQUIVO") + ": " + (rsEventoArquivosRS.getBytes("BLB_ARQUIVO").length / 1024) + " kb");

							Arquivo arquivo = new Arquivo();
							arquivo.setCdArquivo(0);
							arquivo.setNmArquivo(rsEventoArquivosRS.getString("NM_ARQUIVO"));
							arquivo.setBlbArquivo(rsEventoArquivosRS.getBytes("BLB_ARQUIVO"));
							arquivo.setDtArquivamento(Util.convTimestampToCalendar(rsEventoArquivosRS.getTimestamp("DT_ARQUIVAMENTO")));
							arquivo.setDtCriacao(Util.convTimestampToCalendar(rsEventoArquivosRS.getTimestamp("DT_CRIACAO")));

							r = ArquivoServices.save(arquivo, null, connection);

							// 2.2. GRAVAR ARQUIVO x EVENTO

							if (r.getCode() > 0) {
								arquivo.setCdArquivo(((Arquivo) r.getObjects().get("ARQUIVO")).getCdArquivo());

								EventoArquivo eventoArquivo = new EventoArquivo();

								eventoArquivo.setCdEvento(evento.getCdEvento());
								eventoArquivo.setCdArquivo(arquivo.getCdArquivo());
								eventoArquivo.setTpArquivo(rsEventoArquivosRS.getInt("TP_ARQUIVO"));
								eventoArquivo.setIdArquivo(rsEventoArquivosRS.getString("ID_ARQUIVO"));
								eventoArquivo.setTpEventoFoto(rsEventoArquivosRS.getInt("TP_EVENTO_FOTO"));
								eventoArquivo.setTpFoto(rsEventoArquivosRS.getInt("TP_FOTO"));
								eventoArquivo.setDtArquivo(Util.convTimestampToCalendar(rsEventoArquivosRS.getTimestamp("DT_ARQUIVO")));

								r = EventoArquivoServices.save(eventoArquivo, null, connection);

								if (r.getCode() <= 0)
									break;
							} else
								break;
						}
					}
				}
			}
			System.out.println("Sincronização executada com sucesso!");
			
			fileStream.close();
			
			if (r.getCode() <= 0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();

			Conexao.desconectar(connectRS);
			
			return new Result(1, "Alterações realizadas com sucesso.");
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result fixEventosNovosRadar(){
		return fixEventosNovosRadar(null);
	}
	
	public static Result fixEventosNovosRadar(Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			String destEnv = System.getenv("RADAR_DEST_DATABASE");
			
			if(destEnv == null || destEnv.trim().equals("") ) {
				return new Result(-1, "Configurações de banco de dados destino inexistentes.");
			}
			
			String[] splitDestEnv = destEnv.split(";");
			
			if(splitDestEnv.length != 3) {
				return new Result(-1, "Configurações de banco de dados destino não foram informadas corretamente.");
			}
			
			Connection destConnection = Conexao.conectar(splitDestEnv[0], splitDestEnv[1], splitDestEnv[2]);
			
			
			
			connection.commit();
			return new Result(1, "Alterações realizdas com sucesso.");
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	@SuppressWarnings("static-access")
	private static void updatedtPrazoDefesaNai(Connection connect)
	{
		boolean isConnectionNull = connect==null;
		ArquivoMovimento arquivoMovimento = new ArquivoMovimento();
		ArquivoMovimentoDAO arquivoMovimentoDAO = new ArquivoMovimentoDAO();
		
		try 
		{			
			if (isConnectionNull) 
			{
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			String sql = " SELECT * FROM mob_arquivo_movimento "
					   + " WHERE tp_status = 2 AND nr_erro IS null"
					   + " ORDER BY dt_arquivo ASC";
			
			PreparedStatement psmtp = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(psmtp.executeQuery());
			
			System.out.println("Iniciando atualização de dados...");
			while (rsm.next())
			{
				Ait ait = new  Ait();
				int cdAit = rsm.getInt("CD_AIT");
				ait = AitDAO.get(cdAit, connect);
				
				System.out.println("Atualizando dados AIT:  " + cdAit);
				
				arquivoMovimento = arquivoMovimentoDAO.get(rsm.getInt("cd_arquivo_movimento"), rsm.getInt("cd_movimento"), cdAit);
				JSONObject obj = new JSONObject(arquivoMovimento.getDsSaida());;
				String dtDefesaNai = obj.getString("prazoDefesa");
				
		        GregorianCalendar dtPrazoDefesa = new GregorianCalendar();
		        dtPrazoDefesa.setTime(parseDate(dtDefesaNai)); 
		        
		        ait.setDtPrazoDefesa(dtPrazoDefesa);
		        AitDAO.update(ait);		
			}
			System.out.println("Dados atualizados...");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
    public static Date parseDate(String data) throws ParseException   {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setLenient(false);
        return sdf.parse(data);
    }
    
    public static void cancelarAitsIntempestivoRadar() throws Exception {
    	int userTivic = 39;
    	int prazoDecorrido = 1;
    	System.out.println("Cancelando AUTOS...");
    	List<Ait> aitList = searchAitsRadar().getList(Ait.class);
    	System.out.println("Quantidade encontrada: " + aitList.size());
    	for(Ait ait: aitList) {
    		System.out.println("Cancelando AIT: " + ait.getCdAit());
    		AitServices.cancelarAit(ait.getCdAit(), userTivic, prazoDecorrido);
    		System.out.println("--------------------------------------------");
    	}
    }
    
    private static com.tivic.sol.search.Search<Ait> searchAitsRadar() throws Exception{
    	com.tivic.sol.search.Search<Ait> search = new SearchBuilder<Ait>("mob_ait A")
    			.fields(" A.cd_ait, A.id_ait, A.dt_infracao, "
    				  + " B.cd_equipamento, B.tp_equipamento, "
    				  + " C.dt_movimento, C.tp_status, C.lg_enviado_detran")
    			.addJoinTable(" LEFT OUTER JOIN grl_equipamento   B ON (B.cd_equipamento = A.cd_equipamento)"
    						+ " LEFT OUTER JOIN mob_ait_movimento C ON (C.cd_ait = A.cd_ait)")
    			.searchCriterios(montarCriterios())
    			.additionalCriterias(" CAST(A.dt_infracao as date) + 30 < CURRENT_DATE"
    							   + " AND A.dt_infracao > '2021-01-01 00:00:00' AND"
    							   + " NOT EXISTS "
    							   + " ("
    							   + "  	SELECT D.tp_status from mob_ait_movimento D"
    							   + "		WHERE "
    							   + "		("
    							   + "			D.tp_status = " + AitMovimentoServices.NIP_ENVIADA
    							   + "			OR D.tp_status = " + AitMovimentoServices.NAI_ENVIADO
    							   + "			OR D.tp_status = " + AitMovimentoServices.CANCELA_REGISTRO_MULTA
    							   + "			OR D.tp_status = " + AitMovimentoServices.CADASTRO_CANCELADO
    							   + "			OR D.tp_status = " + AitMovimentoServices.CANCELAMENTO_AUTUACAO
    							   + "			OR D.tp_status = " + AitMovimentoServices.CANCELAMENTO_MULTA
    							   + "		)"
    							   + "		AND D.cd_ait = A.cd_ait"
    							   + " )")
    			.orderBy("A.dt_infracao desc")
    			.build();
    	return search;
    }
    
	private static SearchCriterios montarCriterios() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("B.tp_equipamento", 2, true);
		return searchCriterios;
	}
	
	public static void updateStatusFromPublicacao() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			List<AitMovimento> listMovimentoPublicado = searchMovimentoPublicado().getList(AitMovimento.class);
			for (AitMovimento movimento: listMovimentoPublicado) {
				SearchCriterios searchCriterios = montarCriteriosArquivoMovimentoPublicado(movimento.getCdAit(), movimento.getCdMovimento());
				ResultSetMap arquivoMovimento = ArquivoMovimentoDAO.find(searchCriterios.getCriterios());
				if (arquivoMovimento.next()) {
					movimento.setTpStatus(arquivoMovimento.getInt("TP_STATUS"));
					AitMovimentoDAO.update(movimento, customConnection.getConnection());
				}
			}
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private static com.tivic.sol.search.Search<AitMovimento> searchMovimentoPublicado() throws Exception{
		 com.tivic.sol.search.Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				 .searchCriterios(montarCriteriosMovimentoPublicado())
				 .build();
		return search;
	}
	 
	private static SearchCriterios montarCriteriosMovimentoPublicado() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.tp_status", 0, true);
		return searchCriterios;
	}
	
	private static SearchCriterios montarCriteriosArquivoMovimentoPublicado(int cdAit, int cdMovimento) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("cd_movimento", cdMovimento, true);
		return searchCriterios;
	}
	
	public static void cancelarAutosDuplicados() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			int userTivic = 39;
			int autoDuplicado = 16;
			ResultSetMap rsmDuplicada = searchAutosDuplicados().getRsm();
			while (rsmDuplicada.next()) {
				if (rsmDuplicada.getString("DUPLICADA").equals("DUPLICADA")) {
					verificarRegistro(rsmDuplicada.getInt("CD_AIT"), rsmDuplicada.getInt("CD_MOVIMENTO_ATUAL"));
					Result result = AitServices.cancelarAit(rsmDuplicada.getInt("CD_AIT"), userTivic, autoDuplicado, customConnection.getConnection());
					if (result.getCode() == -1 &&
							( rsmDuplicada.getString("NR_CONTROLE") != null || !rsmDuplicada.getString("NR_CONTROLE").equals("")) ) {
						setarRegistroEnviado(rsmDuplicada.getInt("CD_AIT"));
						AitServices.cancelarAit(rsmDuplicada.getInt("CD_AIT"), userTivic, autoDuplicado, customConnection.getConnection());
					}
				}
			}
			customConnection.finishConnection();
		} 
		catch(Exception e) {
			System.out.println("Ait Com erro");
			e.getMessage();
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	private static void verificarRegistro(int cdAit, int cdMovimentoAtual) {
		if (cdMovimentoAtual <= 0) {
			criarRegistroAit(cdAit);
		}
	}
	
	private static AitMovimento criarRegistroAit(int cdAit) {
		Ait ait = AitDAO.get(cdAit);
		AitMovimento aitMovimento = new AitMovimento();
		aitMovimento.setCdAit(cdAit);
		aitMovimento.setTpStatus(TipoStatusEnum.REGISTRO_INFRACAO.getKey());
		aitMovimento.setDtMovimento(new GregorianCalendar());
		AitMovimentoDAO.insert(aitMovimento);
		ait.setCdMovimentoAtual(aitMovimento.getCdMovimento());
		ait.setTpStatus(aitMovimento.getTpStatus());
		AitDAO.update(ait);
		return aitMovimento;
	}
	
	private static void setarRegistroEnviado(int cdAit) {
		System.out.println("Atualizando situação do movimento"); 
		AitMovimento aitMovimento = AitMovimentoServices.getMovimentoTpStatus(cdAit, TipoStatusEnum.REGISTRO_INFRACAO.getKey());
		aitMovimento.setLgEnviadoDetran(TipoStatusEnum.ENVIADO_AO_DETRAN.getKey());
		AitMovimentoDAO.update(aitMovimento);
	}
	
	private static com.tivic.sol.search.Search<Ait> searchAutosDuplicados() throws Exception{
		 com.tivic.sol.search.Search<Ait> search = new SearchBuilder<Ait>("mob_ait A")
				 .fields( " ROW_NUMBER() OVER(ORDER BY dt_infracao ASC) AS NUM,"
				 		+ " (CASE WHEN ROW_NUMBER() OVER(ORDER BY dt_infracao ASC, A.cd_ait) % 2 = 0 THEN '' ELSE 'DUPLICADA' END) as Duplicada,"
				 		+ " * ")
				 .additionalCriterias("A.dt_infracao in ( "
				 		+ "select dt_infracao from mob_ait "
				 		+ " where cd_agente = 233 "
				 		+ "group by dt_infracao, nr_placa "
				 		+ "having count(nr_placa) = 2 "
				 		+ ") "
				 		+ "and A.cd_ait not in (select cd_ait from mob_ait_movimento where cd_ait = A.cd_ait and "
				 		+ "tp_status = " +  TipoStatusEnum.MULTA_PAGA.getKey() + ")")
				 .searchCriterios(montarCriteriosAutosDuplicados())
				 .orderBy("A.dt_infracao, A.cd_ait")
				 .log()
				 .build();
		return search;
	}
	
	private static SearchCriterios montarCriteriosAutosDuplicados() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosGreaterDate("A.dt_infracao", "2022-01-31", true);
		return searchCriterios;
	}
	
	public static void initNovoLoteCorrecoes() throws Exception {
		List<Ait> listNAIs = searchAitsNAI().getList(Ait.class);
		criarLoteNotificacao(listNAIs, TipoStatusEnum.NAI_ENVIADO.getKey());
		List<Ait> listNIPs = searchAitsNIPs().getList(Ait.class);
		criarLoteNotificacao(listNIPs, TipoStatusEnum.NIP_ENVIADA.getKey());
	}
	 
	private static void criarLoteNotificacao(List<Ait> aitList, int tpLote) throws Exception {
		ILoteNotificacaoService loteNotificacaoService;
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		loteNotificacaoService.gerarLoteNotificacao(montarLoteImpressao(aitList, tpLote));
	}
	
	private static LoteImpressao montarLoteImpressao(List<Ait> aitList, int tpLote) {
		int userTivic = ParametroServices.getValorOfParametroAsInteger("MOB_USER_TIVIC", 1);
		LoteImpressao loteImpressao = new LoteImpressaoBuilder()
				.addAits(montarLoteImpressaoAit(aitList))
				.addCdUsuario(userTivic)
				.addTpLoteImpressao(tpLote)
				.build();
		return loteImpressao;
	}
	
	private static List<LoteImpressaoAit> montarLoteImpressaoAit(List<Ait> aitList) {
		List<LoteImpressaoAit> loteImpressaoaitList = new ArrayList<LoteImpressaoAit>();
		for (Ait ait : aitList) {
			LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAit(); 
			if(ait.getCdAit() > 0 )  {
				loteImpressaoAit.setCdAit(ait.getCdAit());
				loteImpressaoaitList.add(loteImpressaoAit);
			}
		}
		return loteImpressaoaitList;
	}
	
	public static com.tivic.sol.search.Search<Ait> searchAitsNAI() throws Exception {
		com.tivic.sol.search.Search<Ait> search = new SearchBuilder<Ait>("MOB_AIT A")
				.fields("A.*, K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran ")
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento K ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento)")
				.searchCriterios(montarCriteriosNAIs())
				.additionalCriterias(" NOT EXISTS"
								   + " ("   
								   + " 		SELECT tp_status FROM mob_ait_movimento K"
								   + " 		WHERE"
								   + "			("
								   + "				tp_status = " + AitMovimentoServices.NIP_ENVIADA
								   + "				OR tp_status = " + AitMovimentoServices.CADASTRO_CANCELADO
								   + "				OR tp_status = " + AitMovimentoServices.CANCELA_REGISTRO_MULTA
								   + "				OR tp_status = " + AitMovimentoServices.CANCELAMENTO_AUTUACAO
								   + "				OR tp_status = " + AitMovimentoServices.CANCELAMENTO_MULTA
								   + "				OR tp_status = " + AitMovimentoServices.CANCELAMENTO_PONTUACAO
								   + "				OR tp_status = " + AitMovimentoServices.DEVOLUCAO_PAGAMENTO
								   + "				"
								   + "			)"
								   + "		AND K.cd_ait = A.cd_ait"
								   + "	)"
								   + "	AND NOT EXISTS"
								   + "	("
								   + "		SELECT D.cd_lote_impressao, D.tp_lote_impressao, E.cd_ait FROM mob_lote_impressao D"
								   + "		JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao)"
								   + "		WHERE"
								   + "			("
								   + "				D.tp_documento = " + TipoLoteNotificacaoEnum.LOTE_NAI.getKey()
								   + "			)"
								   + "	AND E.cd_ait = A.cd_ait"
								   + "	)")
				.orderBy("A.DT_INFRACAO DESC")
				.count()
				.build();
		if (search.getRsm().getLines().isEmpty()) {
			throw new ValidacaoException("Nenhuma NAI encontrada para gerar lote");
		}
		return search;
	}
	
	private static SearchCriterios montarCriteriosNAIs() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("K.tp_status", AitMovimentoServices.NAI_ENVIADO, true);
		searchCriterios.addCriteriosEqualInteger("K.lg_enviado_detran", AitMovimentoServices.REGISTRADO, true);
		searchCriterios.setQtLimite(1000);
		return searchCriterios;
	}
	
	public static com.tivic.sol.search.Search<Ait> searchAitsNIPs() throws Exception {
		com.tivic.sol.search.Search<Ait> search = new SearchBuilder<Ait>("MOB_AIT A")
				.fields("A.*, K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran ")
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento K ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento)")
				.searchCriterios(montarCriteriosNIPs())
				.additionalCriterias(" NOT EXISTS"
								   + " ("   
								   + " 		SELECT tp_status FROM mob_ait_movimento K"
								   + " 		WHERE"
								   + "			("
								   + "				tp_status = " + AitMovimentoServices.CADASTRO_CANCELADO
								   + "				OR tp_status = " + AitMovimentoServices.CANCELA_REGISTRO_MULTA
								   + "				OR tp_status = " + AitMovimentoServices.CANCELAMENTO_AUTUACAO
								   + "				OR tp_status = " + AitMovimentoServices.CANCELAMENTO_MULTA
								   + "				OR tp_status = " + AitMovimentoServices.CANCELAMENTO_PONTUACAO
								   + "				OR tp_status = " + AitMovimentoServices.DEVOLUCAO_PAGAMENTO
								   + "				"
								   + "			)"
								   + "		AND K.cd_ait = A.cd_ait"
								   + "	)"
								   + "	AND NOT EXISTS"
								   + "	("
								   + "		SELECT D.cd_lote_impressao, D.tp_lote_impressao, E.cd_ait FROM mob_lote_impressao D"
								   + "		JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao)"
								   + "		WHERE"
								   + "			("
								   + "				D.tp_documento = " + TipoLoteNotificacaoEnum.LOTE_NIP.getKey()
								   + "			)"
								   + "	AND E.cd_ait = A.cd_ait"
								   + "	)")
				.orderBy("A.DT_INFRACAO DESC")
				.count()
				.build();
		if (search.getRsm().getLines().isEmpty()) {
			throw new ValidacaoException("Nenhuma NIP encontrada para gerar lote");
		}
		return search;
	}
	
	private static SearchCriterios montarCriteriosNIPs() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("K.tp_status", AitMovimentoServices.NIP_ENVIADA, true);
		searchCriterios.addCriteriosEqualInteger("K.lg_enviado_detran", AitMovimentoServices.REGISTRADO, true);
		return searchCriterios;
	}

    public static void teste(Integer a) {
    	System.out.println("teste");
    }
    
	public static void fixSenhasBanco(Integer a) {
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(false);
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * FROM seg_usuario").executeQuery());
			while(rsm.next()) {
				String palavra = rsm.getString("nm_senha");
				if(palavra != null) {
					String novaPalavra = "";
					for(int i = 0; i < palavra.length(); i++) {
						char letra = palavra.charAt(i);
						for(int j = 0; j < 10; j++) {
							letra = (char)((int)letra-1);
						}
						novaPalavra += letra;
					}
					System.out.println(novaPalavra);
					
					StringBuilder strBuilder = new StringBuilder();
					strBuilder.append("9afs1F4F494S9as9V9d8A4slçjfWASh2XAsFGJ9dqmcvxznjvoa9S1Z");
					strBuilder.append(novaPalavra);
					String hash = HashServices.md5( strBuilder.toString());
					
					String sql = "UPDATE seg_usuario SET nm_senha = '" + hash + "' WHERE cd_usuario = " + rsm.getInt("cd_usuario");
					System.out.println(sql);
					connection.prepareStatement(sql).executeUpdate();
				}
			}
			connection.commit();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			Conexao.desconectar(connection);
		}
		
	}
	
	public static void migrarRelatores() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			List<Pessoa> pessoaList = searchRelatores(customConnection).getList(Pessoa.class);
			if (pessoaList.isEmpty())
				throw new ValidacaoException("Nenhum Relator Encontrado");
			for (Pessoa pessoa: pessoaList) {
				cadastrarEnderecoRelator(pessoa, customConnection);
			}
			customConnection.finishConnection();
		}
		finally{
			customConnection.closeConnection();
		}
	}
	
	public static com.tivic.sol.search.Search<Pessoa> searchRelatores(CustomConnection customConnection) throws Exception {
		com.tivic.sol.search.Search<Pessoa> search = new SearchBuilder<Pessoa>("GRL_PESSOA A")
				.customConnection(customConnection)
				.fields("A.*, C.*")
				.addJoinTable(" JOIN GRL_PESSOA_EMPRESA B ON (A.CD_PESSOA = B.CD_PESSOA) ")
				.addJoinTable(" JOIN SEG_USUARIO C ON (A.CD_PESSOA = C.CD_PESSOA)  ")
				.searchCriterios(criteriosSearchRelatores())
				.build();
		return search;
	}
	
	private static SearchCriterios criteriosSearchRelatores() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("B.cd_vinculo", ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_RELATOR", 0), true);
		return searchCriterios;
	}
	
	private static void cadastrarEnderecoRelator(Pessoa pessoa, CustomConnection customConnection) {
		PessoaEndereco pessoaEndereco = new PessoaEndereco();
		pessoaEndereco.setCdPessoa(pessoa.getCdPessoa());
		PessoaEnderecoDAO.insert(pessoaEndereco, customConnection.getConnection());
	}
	
	public static void updateMovimentoEnviados() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			List<AitMovimento> listMovimentos = searchMovimentoEnviados(customConnection).getList(AitMovimento.class);
			for (AitMovimento movimento : listMovimentos) {
				System.out.println("Atualizando AIT/Movimento: " + movimento.getCdAit() + "/" + movimento.getTpStatus());
				movimento.setLgEnviadoDetran(TipoStatusEnum.ENVIADO_AO_DETRAN.getKey());
				AitMovimentoDAO.update(movimento, customConnection.getConnection());
			}
			customConnection.finishConnection();
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	public static com.tivic.sol.search.Search<AitMovimento> searchMovimentoEnviados(CustomConnection customConnection) throws Exception {
		com.tivic.sol.search.Search<AitMovimento> search = new SearchBuilder<AitMovimento>("MOB_AIT_MOVIMENTO A")
				.customConnection(customConnection)
				.fields("A.*")
				.addJoinTable(" JOIN mob_arquivo_movimento B on (B.cd_ait = A.cd_ait AND B.nr_erro is null AND B.tp_status = A.tp_status) ")
				.searchCriterios(criteriosMovimentoEnviados())
				.orderBy(" A.dt_movimento DESC ")
				.build();
		return search;
	}
	
	private static SearchCriterios criteriosMovimentoEnviados() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.lg_enviado_detran", TipoStatusEnum.CADASTRADO.getKey(), true);
		return searchCriterios;
	}
	
	public static void findAllFilesInFolder(File folder) throws Exception {
		for (File file : folder.listFiles()) {
			if (!file.isDirectory()) {
				System.out.println("Scaneando arquivo: " + file.getName());
				readPDFJARI(file.getPath());
			} else {
				findAllFilesInFolder(file);
			}
		}
	}
	
	private static void readPDFJARI(String Path) throws Exception {
		List<DocumentoProcesso> documentoProcessoList = new ArrayList<DocumentoProcesso>();
		System.out.println("================= Iniciando leitura de PDF ========================");
		 try {
			 SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
			 PdfReader pdfReader = new PdfReader(Path); 
			 String texto = PdfTextExtractor.getTextFromPage(pdfReader,1);
			 String[] linhas = texto.split("\n");
			 int i = 0;
			 int i2 = 1;
			 GregorianCalendar dtPublicacao =  new GregorianCalendar();
			 String idAit = "";
			 String nrPlaca = "";
			 String resultado = "";
			 for (String t : linhas) {
				 i++;
				 if (i == 7) {
					 String [] dadosPublicacao = t.split(" ");
					 String DataNotFormat = dadosPublicacao[4].substring(0, dadosPublicacao[4].length()-1);
					 dtPublicacao.setTime(formato.parse(DataNotFormat));
				 }
				 if (t.startsWith(String.valueOf(i2))) {
					 i2++;
					 String [] dadosAuto = t.split(" ");
					 idAit = dadosAuto[3];
					 nrPlaca = dadosAuto[4];
					 resultado = dadosAuto[5];
					 DocumentoProcesso documentoProcesso = buscarResultadoJari(idAit, nrPlaca, dtPublicacao, resultado);
					 documentoProcessoList.add(documentoProcesso);
				 }
			 }
			 System.out.println("=========MAPEAR LISTA DE RESULTDOS===============");
			 for(DocumentoProcesso documentoProcesso: documentoProcessoList) {
				 System.out.println("AIT: " + documentoProcesso.getIdAit() + " / Resultado: " + documentoProcesso.getCdFase());
			 }
			 System.out.println("=========================================");
			 publicarResultadoJari(documentoProcessoList, dtPublicacao);
			 System.out.println("=================== finalizando leitura de PDF ========================");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static DocumentoProcesso buscarResultadoJari(String idAit, String nrPlaca, GregorianCalendar dtPublicacao, String resultado) throws Exception {
		DocumentoProcesso documentoResultadoJARI = new DocumentoProcesso();
		if (resultado.equals("Deferido")) {
			documentoResultadoJARI = buscarJariDeferida(idAit, nrPlaca);
		}
		else if (resultado.equals("Indeferido")) {
			documentoResultadoJARI = buscarJariIndeferida(idAit, nrPlaca);
		}
		return documentoResultadoJARI;
	}
	
	private static DocumentoProcesso buscarJariDeferida(String idAit, String nrPlaca) throws Exception {
		int faseDeferida = 20;
		int tipoJari = 18;
		ResultSetMap rsm = AitDAO.find(criteriosJARI(idAit, nrPlaca));
		SearchCriterios search = new SearchCriterios();
		if (rsm.next()) {
			search = criteriosPublicacaoJARI(faseDeferida, tipoJari, rsm.getInt("CD_AIT"));
		}
		DocumentoProcesso documento = searchProcessosPublicacao(search).getList(DocumentoProcesso.class).get(0);
		return documento;
	}
	
	private static DocumentoProcesso buscarJariIndeferida(String idAit, String nrPlaca) throws Exception {
		int faseIndeferida = 21;
		int tipoJari = 18;
		ResultSetMap rsm = AitDAO.find(criteriosJARI(idAit, nrPlaca));
		SearchCriterios search = new SearchCriterios();
		if (rsm.next()) {
			search = criteriosPublicacaoJARI(faseIndeferida, tipoJari, rsm.getInt("CD_AIT"));
		}
		DocumentoProcesso documento = searchProcessosPublicacao(search).getList(DocumentoProcesso.class).get(0);
		return documento;
	}
	
	private static SearchCriterios criteriosPublicacaoJARI(int cdFase, int cdTipoDocumento, int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("F.cd_fase", cdFase, cdFase > 0);
		searchCriterios.addCriteriosEqualInteger("E.cd_tipo_documento", cdTipoDocumento, cdTipoDocumento > 0);
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, cdAit > 0);
		return searchCriterios;
	}
	
	private static com.tivic.sol.search.Search<DocumentoProcesso> searchProcessosPublicacao(SearchCriterios searchCriterios) throws Exception {
		com.tivic.sol.search.Search<DocumentoProcesso> search = new SearchBuilder<DocumentoProcesso>("mob_ait_movimento A")
				.fields(" A.*, "
					  + " B.ID_AIT, B.NR_PLACA, D.NR_DOCUMENTO,D.NM_REQUERENTE, D.CD_DOCUMENTO, D.DT_PROTOCOLO, D.TXT_OBSERVACAO, B.DT_INFRACAO, E.NM_TIPO_DOCUMENTO,"
					  + "F.CD_FASE, F.NM_FASE")
				.addJoinTable(" JOIN mob_ait B ON (A.CD_AIT = B.CD_AIT) ")
				.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.CD_AIT = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
				.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
				.searchCriterios(searchCriterios)
				.orderBy(" B.NR_PLACA ")
				.build();
		return search;
	}
	
	private static void publicarResultadoJari(List<DocumentoProcesso> documentoProcessoList, GregorianCalendar dtPublicacao) throws Exception {
		com.tivic.manager.mob.aitmovimento.AitMovimentoService aitMovimentoServices = new com.tivic.manager.mob.aitmovimento.AitMovimentoService();
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			 System.out.println("Data para publicação: " + dtPublicacao.getTime());
			 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> PUBLICANDO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			 aitMovimentoServices.setDtPublicacaoDO(documentoProcessoList, dtPublicacao);
			 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>End Publication<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private static ArrayList<ItemComparator> criteriosJARI(String idAit, String nrPlaca) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("id_ait", idAit, ItemComparator.EQUAL, Types.CHAR));
		criterios.add(new ItemComparator("nr_placa", nrPlaca, ItemComparator.EQUAL, Types.CHAR));
		return criterios;
	}
	
	private static void cadastrarDgVerificador() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			List<CorreiosEtiqueta> correiosEtiquetaList = searchEtiquetasSemDV().getList(CorreiosEtiqueta.class);
			System.out.println("Size Etiquetas encontradas: " + correiosEtiquetaList.size());                           
			for (CorreiosEtiqueta correiosEtiqueta: correiosEtiquetaList) {
				correiosEtiqueta.setNrDigitoVerificador(new DigitoVerificador().GerarDigitoVerificador(correiosEtiqueta.getNrEtiqueta()));
				CorreiosEtiquetaDAO.update(correiosEtiqueta, customConnection.getConnection());
			}
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private static com.tivic.sol.search.Search<CorreiosEtiqueta> searchEtiquetasSemDV() throws Exception {
		com.tivic.sol.search.Search<CorreiosEtiqueta> search = new SearchBuilder<CorreiosEtiqueta>("mob_correios_etiqueta A")
				.fields(" A.*")
				.searchCriterios(criteriosEtiquetasSemDV())
				.build();
		return search;
	}
	
	private static SearchCriterios criteriosEtiquetasSemDV() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("A.nr_digito_verificador", "", ItemComparator.ISNULL, Types.INTEGER);
		return searchCriterios;
	}
	public static Result fixImagensEventos() {
		Connection connection = null;
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			System.out.println("-------------------------------------------------");
			System.out.println("Imagens eventos");
			
			
			PreparedStatement ps = connection
					.prepareStatement("SELECT * FROM mob_evento_equipamento A" + 
							" WHERE A.dt_afericao between '2022-10-26 00:00:00' and '2022-10-29 23:59:59'" + 
							"  AND NOT EXISTS (SELECT * FROM mob_evento_arquivo B WHERE A.cd_evento = B.cd_evento)");
			
			String eventosSemOuUmArquivo = "";
			String codigosArquivosRemovidos = "";
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			while(rsm.next()) {
				System.out.println("Evento: " + rsm.getInt("cd_evento"));
				GregorianCalendar dtAfericao = rsm.getGregorianCalendar("dt_afericao");
				String nmArquivo = "" + dtAfericao.get(Calendar.YEAR) + 
								  (dtAfericao.get(Calendar.MONTH) + 1) + 
								   dtAfericao.get(Calendar.DAY_OF_MONTH) + "_" + 
								   (dtAfericao.get(Calendar.HOUR_OF_DAY) <= 9 ? "0" + dtAfericao.get(Calendar.HOUR_OF_DAY) : dtAfericao.get(Calendar.HOUR_OF_DAY)) + 
								   (dtAfericao.get(Calendar.MINUTE) <= 9 ? "0" + dtAfericao.get(Calendar.MINUTE) : dtAfericao.get(Calendar.MINUTE)) +
								   (dtAfericao.get(Calendar.SECOND) <= 9 ? "0" + dtAfericao.get(Calendar.SECOND) : dtAfericao.get(Calendar.SECOND));
				
				PreparedStatement ps2 = connection
						.prepareStatement("SELECT * FROM grl_arquivo A " + 
											"WHERE NOT EXISTS (SELECT * FROM mob_evento_arquivo B WHERE B.cd_arquivo = A.cd_arquivo)" + 
											"  AND A.nm_arquivo ilike '"+nmArquivo+"%_"+rsm.getString("nr_pista")+"._.tarja.jpg'" + 
											"  AND A.dt_criacao between '2022-10-26 00:00:00' and '2022-10-29 23:59:59'");
				
				List<String> arquivosJaInseridos = new ArrayList<String>();
				ResultSetMap rsmArquivos = new ResultSetMap(ps2.executeQuery());
				while(rsmArquivos.next()) {
					if(!arquivosJaInseridos.contains(rsmArquivos.getString("nm_arquivo"))) {
						System.out.println("Arquivo achado: " + rsmArquivos.getString("cd_arquivo") + ": " + rsmArquivos.getString("nm_arquivo"));
						connection.prepareStatement("INSERT INTO mob_evento_arquivo (cd_evento, cd_arquivo, tp_arquivo, dt_arquivo, tp_evento_foto, tp_foto) VALUES ("+rsm.getInt("cd_evento")+", "+rsmArquivos.getInt("cd_arquivo")+", 0, "+ (rsmArquivos.getGregorianCalendar("dt_criacao") != null ? "'"+DateUtil.formatDate(rsmArquivos.getGregorianCalendar("dt_criacao"), "yyyy-MM-dd HH:mm:ss")+"'" : null) + ", 3, 0)").executeUpdate();
						arquivosJaInseridos.add(rsmArquivos.getString("nm_arquivo"));
					} else {
						System.out.println("Arquivo removido: " + rsmArquivos.getString("cd_arquivo") + ": " + rsmArquivos.getString("nm_arquivo"));
						connection.prepareStatement("DELETE FROM grl_arquivo WHERE cd_arquivo = " + rsmArquivos.getInt("cd_arquivo")).executeUpdate();
						codigosArquivosRemovidos += rsmArquivos.getInt("cd_arquivo") + "\n";
					}
				}
				rsmArquivos.beforeFirst();
				
				if(arquivosJaInseridos.size() <= 1) {
					eventosSemOuUmArquivo += rsm.getString("cd_evento") + ";" + rsm.getString("nm_equipamento") + ";" + DateUtil.formatDate(rsm.getGregorianCalendar("dt_afericao"), "dd/MM/yyyy HH:mm:ss") + "\n";
				}
				
				System.out.println("");
			}
			
			
			
			String nmPath = "eventos_sem_arquivos.txt";	
			File file = new File(nmPath);
            if (!file.exists())
            	file.createNewFile();
            FileOutputStream fos = new FileOutputStream(nmPath);
            fos.write(eventosSemOuUmArquivo.toString().getBytes());
            fos.close();
            
            String nmPath2 = "arquivos_removidos.txt";	
			File file2 = new File(nmPath2);
            if (!file2.exists())
            	file2.createNewFile();
            FileOutputStream fos2 = new FileOutputStream(nmPath2);
            fos2.write(codigosArquivosRemovidos.toString().getBytes());
            fos2.close();
			
	  		System.out.println("------------------------------------------------");
	  		
	  		connection.commit();
	  		
			return new Result(1, "Fix executado com sucesso.");				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static void fixLogradouro(Integer a) {
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(false);
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("C:\\Users\\Gabriel\\Downloads\\LayoutArquivoTE_Guaratinguetá.csv", ";", true);
			while(rsm.next()) {
				String sql = "INSERT INTO grl_logradouro (cd_logradouro, nm_logradouro, cd_cidade) VALUES ("+rsm.getString("cd_logradouro")+", '"+rsm.getString("nm_logradouro")+"', 11133)";
				System.out.println(sql);
				System.out.println();
				connection.prepareStatement(sql).executeUpdate();
			}
			connection.commit();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			Conexao.desconectar(connection);
		}
		
	}
	
	public static void fixPublicacaoDO() throws Exception {
		boolean isPublicacao = true;
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------------------------------------------------------------------------");
			System.out.println("========================= PUBLICAÇÃO DO =========================");
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriterios("tp_status", " 7, 10 ", ItemComparator.IN, Types.INTEGER);
			searchCriterios.addCriterios("dt_publicacao_do", "", ItemComparator.NOTISNULL, Types.TIMESTAMP);
			
			com.tivic.sol.search.Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento")
					.searchCriterios(searchCriterios)
					.log()
					.build();
			System.out.println(search.getList(AitMovimento.class).toString());
			List<AitMovimento> aitMovimentoList = search.getList(AitMovimento.class);
			for (int i = 0; i < aitMovimentoList.size(); i++) {
				SearchCriterios searchCriteriosTemp = new SearchCriterios();
				searchCriteriosTemp.addCriteriosEqualInteger("cd_ait", aitMovimentoList.get(i).getCdAit());
				searchCriteriosTemp.addCriteriosEqualInteger("cd_movimento", aitMovimentoList.get(i).getCdMovimento());
				System.out.println("CONTANDO >>>>>>>>>>>> " + i);
				ResultSetMapper<AitMovimentoDocumento> rsm = new ResultSetMapper<AitMovimentoDocumento>(AitMovimentoDocumentoDAO.find(searchCriteriosTemp.getCriterios(), customConnection.getConnection()), AitMovimentoDocumento.class);
				AitMovimentoDocumento aitMovimentoDocumento = rsm.toList().get(0);
				System.out.println("=======================GERANDO OCORRENCIA "+ aitMovimentoDocumento.getCdAit() +" =======================");
				lancarOcorrenciaPublicacaoGerada(aitMovimentoDocumento.getCdDocumento(), aitMovimentoList.get(i).getDtPublicacaoDo(), isPublicacao, customConnection);
				System.out.println("=======================OCORRENCIA "+ aitMovimentoDocumento.getCdAit() +" GERADA=======================");
				System.out.println("=======================ENVIANDO OCORRENCIA "+ aitMovimentoDocumento.getCdAit() +" =======================");
				lancarOcorrenciaPublicacaoEnviada(aitMovimentoDocumento.getCdDocumento(), aitMovimentoList.get(i).getDtPublicacaoDo(), isPublicacao, customConnection);
				System.out.println("=======================OCORRENCIA "+ aitMovimentoDocumento.getCdAit() +" ENVIADA=======================");
			}
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private static void lancarOcorrenciaPublicacaoGerada(int cdDocumento, GregorianCalendar dtOcorrenciaDo, boolean isPublicada, CustomConnection customConnection) throws ValidacaoException, Exception {
		GregorianCalendar date = new GregorianCalendar(2000, 0, 1);
		DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrenciaBuilder()
				.addCdDocumento(cdDocumento)
				.addCdTipoOcorrencia(ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_ARQUIVO_PUBLICACAO_GERADO", 0))
				.addCdUsuario(ParametroServices.getValorOfParametroAsInteger("MOB_USER_TIVIC", 1))
				.addDtOcorrencia(date)
				.build();
		if(isPublicada) {
			documentoOcorrencia.setDtOcorrencia(dtOcorrenciaDo);
		}
		DocumentoOcorrenciaDAO.insert(documentoOcorrencia, customConnection.getConnection());
	}
	
	private static void lancarOcorrenciaPublicacaoEnviada(int cdDocumento, GregorianCalendar dtOcorrenciaDo, boolean isPublicada, CustomConnection customConnection) throws ValidacaoException, Exception {
		GregorianCalendar date = new GregorianCalendar(2000, 0, 1);
		DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrenciaBuilder()
				.addCdDocumento(cdDocumento)
				.addCdTipoOcorrencia(ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_ARQUIVO_PUBLICACAO_ENVIADO", 0))
				.addCdUsuario(ParametroServices.getValorOfParametroAsInteger("MOB_USER_TIVIC", 1))
				.addDtOcorrencia(date)
				.build();
		if(isPublicada) {
			documentoOcorrencia.setDtOcorrencia(dtOcorrenciaDo);
		}
		DocumentoOcorrenciaDAO.insert(documentoOcorrencia, customConnection.getConnection());
	}
	
	public static void fixPublicaDocumentoAntigo() throws Exception {
		boolean isPublicacao = false;
		int codJulgado = ParametroServices.getValorOfParametroAsInteger("CD_FASE_JULGADO", 0);
		int codPublicacaoGerada = ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_ARQUIVO_PUBLICACAO_GERADO", 0);
		int codPublicacaoEnviada = ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_ARQUIVO_PUBLICACAO_ENVIADO", 0);
		String codsPublicacao = String.valueOf(codPublicacaoGerada) + ", " + String.valueOf(codPublicacaoEnviada);
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			SearchCriterios searchCriterios = new SearchCriterios();
			com.tivic.sol.search.Search<ProtocoloPublicacaoPendenteDto> search = new SearchBuilder<ProtocoloPublicacaoPendenteDto>("PTC_DOCUMENTO A")
					.addJoinTable("JOIN PTC_DOCUMENTO_OCORRENCIA B ON (B.CD_DOCUMENTO = A.CD_DOCUMENTO)")
					.addJoinTable("JOIN MOB_AIT_MOVIMENTO_DOCUMENTO C ON (C.CD_DOCUMENTO = A.CD_DOCUMENTO)")
					.addJoinTable("JOIN MOB_AIT D ON (D.CD_AIT = C.CD_AIT)")
					.addJoinTable("JOIN GPN_TIPO_DOCUMENTO E ON (E.CD_TIPO_DOCUMENTO = A.CD_TIPO_DOCUMENTO)")
					.addJoinTable("JOIN PTC_SITUACAO_DOCUMENTO F ON (F.CD_SITUACAO_DOCUMENTO = A.CD_SITUACAO_DOCUMENTO)")
					.addJoinTable("JOIN mob_ait_movimento G ON (G.cd_ait = D.cd_ait)")
					.searchCriterios(searchCriterios)
					.additionalCriterias("NOT EXISTS (SELECT Z.* FROM PTC_DOCUMENTO_OCORRENCIA Z WHERE Z.CD_TIPO_OCORRENCIA IN (" + codsPublicacao + ")\r\n" + 
							"	AND Z.CD_DOCUMENTO = A.CD_DOCUMENTO)\r\n" + 
							"	AND E.ID_TIPO_DOCUMENTO IN ('7', '10')\r\n" + 
							"	AND A.CD_FASE = "  + codJulgado + "\r\n" + 
							"	AND G.dt_movimento <= '2021-12-31'\r\n" + 
							"	AND G.dt_publicacao_do is null\r\n" + 
							"	AND G.tp_status IN (7,10)")
					.log()
					.build();
			List<ProtocoloPublicacaoPendenteDto> listProtocol =  search.getList(ProtocoloPublicacaoPendenteDto.class);
			for (int i = 0; i < listProtocol.size(); i++) {
				System.out.println("=======================GERANDO OCORRENCIA "+ listProtocol.get(i).getCdAit() +" =======================");
				lancarOcorrenciaPublicacaoGerada(listProtocol.get(i).getCdDocumento(), null, isPublicacao, customConnection);
				System.out.println("=======================OCORRENCIA "+ listProtocol.get(i).getCdAit() +" GERADA=======================");
				System.out.println("=======================ENVIANDO OCORRENCIA "+ listProtocol.get(i).getCdAit() +" =======================");
				lancarOcorrenciaPublicacaoEnviada(listProtocol.get(i).getCdDocumento(), null, isPublicacao, customConnection);
				System.out.println("=======================OCORRENCIA "+ listProtocol.get(i).getCdAit() +" ENVIADA=======================");
			}
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	public static void fixLimpezaBanco(Integer a) {
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(true);
			
			List<String> pacotesARemover = Arrays.asList(
				"acd",
				"adm",
				"agd",
				"alm",
				"app",
				"aud",
				"bdv",
				"blb",
				"bpm",
				"cae",
				"cms",
				"cmt",
				"crm",
				"crt",
				"ctb",
				"ecm",
				"evt",
				"flp",
				"fsc",
				"hlp",
				"log",
				"mcr",
				"msg",
				"pcb",
				"prc",
				"psq",
				"sce",
				"sinc",
				"srh"
			); 
			
			String sql = "";
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT table_name"
					+ "  FROM information_schema.tables"
					+ " WHERE table_schema='public'"
					+ "   AND table_type='BASE TABLE' "
					+ " ORDER BY table_name;").executeQuery());
			System.out.println("Quantidade de tabelas: " + rsm.size());
			int count = 0;
			List<String> tabelasGerais = new ArrayList<>();
			List<String> tabelasApagadas = new ArrayList<>();
			while(rsm.next()) {
				tabelasGerais.add(rsm.getString("table_name"));
				for(String pacote : pacotesARemover) {
					if(rsm.getString("table_name").startsWith(pacote)
							//acd_tipo_dependencia
							//acd_tipo_etapa
							//acd_tipo_equipamento
							//acd_tipo_mantenedora
							//acd_tipo_periodo
							//acd_local_funcionamento
							//acd_plano_secao
							//acd_tipo_grupo_escolar
							//alm_nivel_local
							//blb_assunto
							//blb_editora
							//blb_publicacao
							//blb_publicacao_autor
							//blb_publicacao_assunto
							//crm_tipo_necessidade
							&& !rsm.getString("table_name").equals("agd_agenda_item")
							&& !rsm.getString("table_name").equals("adm_tipo_documento")
							&& !rsm.getString("table_name").equals("bpm_bem")
							&& !rsm.getString("table_name").equals("bpm_referencia")) {
						count++;
						tabelasApagadas.add(rsm.getString("table_name"));
						connection.prepareStatement("DROP TABLE " + rsm.getString("table_name") + " CASCADE").executeUpdate();
						System.out.println("DROP TABLE " + rsm.getString("table_name") + " CASCADE;\n");
						sql += "DROP TABLE " + rsm.getString("table_name") + " CASCADE;\n";
						break;
					}
				}
			}
			System.out.println("Tabelas apagadas: " + count);
			rsm = new ResultSetMap(connection.prepareStatement("SELECT table_name"
					+ "  FROM information_schema.tables"
					+ " WHERE table_schema='public'"
					+ "   AND table_type='BASE TABLE' "
					+ " ORDER BY table_name;").executeQuery());
			System.out.println("Quantidade de tabelas após: " + rsm.size());
			while(rsm.next()) {
				tabelasGerais.remove(rsm.getString("table_name"));
			}
			for(String tabelaApagada : tabelasApagadas) {
				tabelasGerais.remove(tabelaApagada);
			}
			System.out.println("Tabelas que foram removidas a mais:");
			for(String tabelaGeral : tabelasGerais ) {
				System.out.println(tabelaGeral);
			}
			
			File file = new File("SQL_LIMPEZA_BANCO_TRANSITO.txt");
			if(!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(sql.getBytes());
			fileOutputStream.close();
			
			//connection.commit();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			Conexao.desconectar(connection);
		}
		
	}
	
	public static void fixSegundaLimpezaTransporteBanco(Integer a) {
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(false);
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT table_name"
					+ "  FROM information_schema.tables"
					+ " WHERE table_schema='public'"
					+ "   AND table_type='BASE TABLE' "
					+ " ORDER BY table_name;").executeQuery());
			System.out.println("Quantidade de tabelas: " + rsm.size());
			int count = 0;
			List<String> tabelasGerais = new ArrayList<>();
			List<String> tabelasApagadas = new ArrayList<>();
			while(rsm.next()) {
				tabelasGerais.add(rsm.getString("table_name"));
				ResultSetMap rsmTabela = new ResultSetMap(connection.prepareStatement("SELECT * from " + rsm.getString("table_name")).executeQuery());
				if(rsmTabela.size() == 0) {
					count++;
					tabelasApagadas.add(rsm.getString("table_name"));
					System.out.println(rsm.getString("table_name"));
					connection.prepareStatement("DROP TABLE " + rsm.getString("table_name") + " CASCADE").executeUpdate();
				}
			}
			System.out.println("Tabelas apagadas: " + count);
			rsm = new ResultSetMap(connection.prepareStatement("SELECT table_name"
					+ "  FROM information_schema.tables"
					+ " WHERE table_schema='public'"
					+ "   AND table_type='BASE TABLE' "
					+ " ORDER BY table_name;").executeQuery());
			System.out.println("Quantidade de tabelas após: " + rsm.size());
			while(rsm.next()) {
				tabelasGerais.remove(rsm.getString("table_name"));
			}
			for(String tabelaApagada : tabelasApagadas) {
				tabelasGerais.remove(tabelaApagada);
			}
			System.out.println("Tabelas que foram removidas a mais:");
			for(String tabelaGeral : tabelasGerais ) {
				System.out.println(tabelaGeral);
			}
			
			connection.commit();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			Conexao.desconectar(connection);
		}
		
	}
	
	public static void fixSegundaLimpezaTransitoBanco(Integer a) {
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(false);
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT table_name"
					+ "  FROM information_schema.tables"
					+ " WHERE table_schema='public'"
					+ "   AND table_type='BASE TABLE' "
					+ " ORDER BY table_name;").executeQuery());
			System.out.println("Quantidade de tabelas: " + rsm.size());
			int count = 0;
			List<String> tabelasGerais = new ArrayList<>();
			List<String> tabelasApagadas = new ArrayList<>();
			while(rsm.next()) {
				tabelasGerais.add(rsm.getString("table_name"));
				
			}
			
			String sql = "";
			
			BufferedReader reader = new BufferedReader(new FileReader("/home/gabriel/Documentos/TABELAS_APAGAR_TRANSITO.txt"));
			String line = reader.readLine();

			while (line != null) {
				if(tabelasGerais.contains(line)) {
					count++;
					tabelasApagadas.add(line);
					connection.prepareStatement("DROP TABLE " + line + " CASCADE").executeUpdate();
					sql += "DROP TABLE " + line + " CASCADE;\n";
				}
				line = reader.readLine();
			}
			reader.close();
			
			System.out.println("Tabelas apagadas: " + count);
			rsm = new ResultSetMap(connection.prepareStatement("SELECT table_name"
					+ "  FROM information_schema.tables"
					+ " WHERE table_schema='public'"
					+ "   AND table_type='BASE TABLE' "
					+ " ORDER BY table_name;").executeQuery());
			System.out.println("Quantidade de tabelas após: " + rsm.size());
			while(rsm.next()) {
				tabelasGerais.remove(rsm.getString("table_name"));
			}
			for(String tabelaApagada : tabelasApagadas) {
				tabelasGerais.remove(tabelaApagada);
			}
			System.out.println("Tabelas que foram removidas a mais:");
			for(String tabelaGeral : tabelasGerais ) {
				System.out.println(tabelaGeral);
			}
			

			File file = new File("SQL_SEGUNDA_LIMPEZA_BANCO_TRANSITO.txt");
			if(!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(sql.getBytes());
			fileOutputStream.close();
			
			connection.commit();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			Conexao.desconectar(connection);
		}
		
	}
	
	public static void limpezaRegistroSituacaoDocumento(Integer a) {
		Connection connection = Conexao.conectar();
		try {
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM ptc_situacao_documento " + 
					"WHERE nm_situacao_documento IN ('Deferido com advertência', 'Cancelada', 'Julgado')");
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
	public static void limpezaRegistroFase(Integer a) {
		Connection connection = Conexao.conectar();
		try {
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM ptc_fase " + 
					"WHERE nm_fase IN ('Deferido', 'Indeferido', 'Em análise', 'Externo')");
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			Conexao.desconectar(connection);
		}
	}

	public static Result fixNrAitRadar(Integer a) {
		return fixNrAitRadar(a, null);
	}
	
	public static Result fixNrAitRadar(Integer a, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT cod_talao, nr_inicial, nr_final"
					+ " FROM talonario"
					+ " WHERE tp_talao = " + TipoTalaoEnum.TP_TALONARIO_RADAR_FIXO.getKey()
				);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(!rs.next()) {
				return new Result(-1, "Nenhum talonário encontrado.");
			}
			
			System.out.println("INICIANDO...\n");
			do {
				int codTalao = rs.getInt("cod_talao");
				int nrInicial = rs.getInt("nr_inicial");
				int nrFinal = rs.getInt("nr_final");
				
				System.out.println("Iniciando alteração nos AITs do talonário: " + codTalao);

				PreparedStatement pstmtAit = connect.prepareStatement(
						"SELECT codigo_ait, nr_ait"
						+ " FROM ait"
						+ " WHERE REGEXP_REPLACE(nr_ait, '[^0-9]', '', 'g')::INTEGER BETWEEN " + nrInicial + " AND " + nrFinal
					);
				
				ResultSet rsAit = pstmtAit.executeQuery();
				
				if(!rsAit.next()) {
					return new Result(-1, "Nenhum AIT encontrado.");
				}
				
				do {
					int codAit = rsAit.getInt("codigo_ait");
					String nrAit = rsAit.getString("nr_ait");

				    if (nrAit.length() < 10 && nrAit.matches("\\d+")) {
				    	String nrAitSigla = String.format("%s%08d", "RT", Integer.valueOf(nrAit));
				        
						pstmt = connect.prepareStatement("UPDATE ait "
								+ " SET nr_ait = ?"
								+ " WHERE codigo_ait = ?");
						
						pstmt.setString(1, nrAitSigla);
						pstmt.setInt(2, codAit);

						retorno = pstmt.executeUpdate();
						
						if(retorno<=0) {
							Conexao.rollback(connect);
							return new Result(retorno, "Erro ao executar update no AIT: " + codAit);
						}
						
						System.out.println("nrAit " + nrAit + " virou " + nrAitSigla);
				    }
				} while(rsAit.next());

			} while(rs.next());
									
			connect.commit();
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
	public static Result fixInativacaoEventos(Integer a) {
		Connection connect = Conexao.conectar();
		
		try {
		
			connect.setAutoCommit(false);
			
			String cdEquipamento = "select cd_equipamento from grl_equipamento ge where nm_equipamento like 'ST%'";
			GregorianCalendar dataInicial = new GregorianCalendar(2024, 4, 9, 19, 0, 0);
			GregorianCalendar dataFinal = new GregorianCalendar(2024, 4, 9, 22, 0, 0);
			int cdUsuarioCancelamento = 222;//EMILE
			int cdMotivoCancelamento = 18;//SOLICITACAO DO ORGAO
			String idTipoEvento = "'VAL', 'ASV', 'PSF'";
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM mob_evento_equipamento A, mob_tipo_evento B "
					+ "WHERE A.cd_tipo_evento = B.cd_tipo_evento "
					+ " AND B.id_tipo_evento IN ("+idTipoEvento+") "
					+ " AND A.st_evento in (0,1) "
					+ " AND NOT EXISTS (SELECT * FROM mob_ait_evento B WHERE A.cd_evento = B.cd_evento) "
					+ " AND cd_equipamento IN (" + cdEquipamento + ") "
					+ (dataInicial != null ? " AND A.dt_conclusao >= '"+DateUtil.formatDate(dataInicial, "yyyy-MM-dd HH:mm:ss")+"' " : "")
					+ (dataFinal != null ? " AND A.dt_conclusao <= '"+DateUtil.formatDate(dataFinal, "yyyy-MM-dd HH:mm:ss")+"'" : "")
					+ " order by A.dt_conclusao asc"
				);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			int x = 0;
			while(rsm.next()) {
				System.out.println((x++) + " / " + rsm.size());
				EventoEquipamento eventoEquipamento = EventoEquipamentoDAO.get(rsm.getInt("cd_evento"), connect);
				eventoEquipamento.setDtCancelamento(DateUtil.getDataAtual());
				eventoEquipamento.setCdUsuarioCancelamento(cdUsuarioCancelamento);
				eventoEquipamento.setCdMotivoCancelamento(cdMotivoCancelamento);
				eventoEquipamento.setStEvento(EventoEquipamentoServices.ST_EVENTO_CANCELADO);
				System.out.println("eventoEquipamento = " + eventoEquipamento);
				EventoEquipamentoDAO.update(eventoEquipamento, connect);
			}
												
			connect.commit();
			System.out.println("\nFINALIZADO.");
			return new Result(1, "Executado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static void tratarSiglaAitsVM() {
		Connection connect = Conexao.conectar();
		try {
			ResultSetMap rsmEquipamento = buscarEquipamento(connect);
			ResultSetMap rsmTalonarios = buscarTalonariosWithSigla(connect);
			int codEquipamento = 0;
			if (rsmEquipamento.next()) {
				codEquipamento = rsmEquipamento.getInt("CD_EQUIPAMENTO");
			} else {
				throw new BadRequestException("Equipamento de VM não encontrado.");
			}
			while(rsmTalonarios.next()) {
				ResultSetMap rsmAits = buscarAitsByAgenteAndEquipamento(codEquipamento, rsmTalonarios, connect);
				atualizarNrAits(rsmAits, rsmTalonarios, connect);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new BadRequestException("Ocorreu um erro ao atualizar os AITs de VM");
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	private static ResultSetMap buscarEquipamento(Connection connect) throws SQLException {
		System.out.println("Buscando Equipamento de VM....");
		PreparedStatement pstmt = connect.prepareStatement(" SELECT * FROM GRL_EQUIPAMENTO "
				+ " WHERE TP_EQUIPAMENTO = " + EquipamentoServices.CAMERA);
		ResultSetMap rsmEquipamento = new ResultSetMap(pstmt.executeQuery());
		System.out.println("Equipamento encontrado: " + rsmEquipamento);
		return rsmEquipamento;
	}
	
	private static ResultSetMap buscarTalonariosWithSigla(Connection connect) throws SQLException {
		System.out.println("Buscando Talonarios....");
		PreparedStatement pstmt = connect.prepareStatement(" SELECT * FROM TALONARIO "
				+ " WHERE SG_TALAO IS NOT NULL ");
		ResultSetMap rsmTalonarios = new ResultSetMap(pstmt.executeQuery());
		System.out.println("Talonarios encontrado: " + rsmTalonarios);
		return rsmTalonarios;
	}
	
	private static ResultSetMap buscarAitsByAgenteAndEquipamento(int codEquipamento, ResultSetMap rsmTalonarios, Connection connect) throws SQLException {
		System.out.println("-------- Buscando AITS -------");			
		PreparedStatement pstmt = connect.prepareStatement(" SELECT * FROM AIT "
				+ " WHERE CD_EQUIPAMENTO = ? AND COD_AGENTE = ? AND REGEXP_REPLACE(nr_ait, '[^0-9]', '', 'g')::INTEGER BETWEEN ? AND ? ");
		pstmt.setInt(1, codEquipamento);
		pstmt.setInt(2, rsmTalonarios.getInt("COD_AGENTE"));
		pstmt.setInt(3, rsmTalonarios.getInt("NR_INICIAL"));
		pstmt.setInt(4, rsmTalonarios.getInt("NR_FINAL"));
		System.out.println("SQL: " + pstmt);
		ResultSetMap rsmAits = new ResultSetMap(pstmt.executeQuery());
		System.out.println("--------- QTD AITs Encontrados: " + rsmAits.getLines().size());
		return rsmAits;
	}
	
	private static void atualizarNrAits(ResultSetMap rsmAits, ResultSetMap rsmTalonarios, Connection connect) throws SQLException {
		ManagerLog managerLog = new ManagerLog();
		managerLog.info("Iniciando atualização de NR_AITS para VM, quantidade a atualizar: ", String.valueOf(rsmAits.getLines().size()));
		int qtdAtualizada = 0;
		String sgTalao = rsmTalonarios.getString("SG_TALAO");
		while(rsmAits.next()) {
			try {
				String nrAitOld = rsmAits.getString("NR_AIT").replaceAll("[^0-9]*", "");
				System.out.println("AIT OLD: " + nrAitOld);
				String nrAitSG = sgTalao + Util.fillNum(Integer.valueOf(nrAitOld), 10);		
				System.out.println("AIT NEW: " + nrAitSG);					
				PreparedStatement pstmt = connect.prepareStatement(" UPDATE AIT SET NR_AIT = ? WHERE CODIGO_AIT = ?");
				pstmt.setString(1, nrAitSG);
				pstmt.setInt(2, rsmAits.getInt("CODIGO_AIT"));
				System.out.println("SQL UPDATE: " + pstmt);
				pstmt.executeUpdate();
				qtdAtualizada++;
			} catch (Exception e) {
				managerLog.error("Não foi possivel atualizar o número do AIT:" , rsmAits.getString("CODIGO_AIT"));
				e.printStackTrace();
				continue;
			}
		}
		managerLog.info("Finalizando atualização de NR_AITS para VM, quantidade atualizada: ", String.valueOf(qtdAtualizada));
	}
	
	public static void fixDuplicidadeEventos(Integer a) {
	    CustomConnection customConnection = null;
	    try {
	        customConnection = new CustomConnection();
	        customConnection.initConnection(true);
	        System.out.println("----------------------------------- INICIADO ---------------------------------------");

	        Connection connection = customConnection.getConnection();
	        connection.setAutoCommit(false);

	        Map<Integer, String> datasJaVistas = new HashMap<Integer, String>();
	        
	        String selectQuery = "select nr_placa, dt_conclusao, nr_pista, cd_equipamento,st_evento, cd_evento from mob_evento_equipamento A where A.dt_conclusao in ( " + 
	        		"select dt_conclusao from mob_evento_equipamento " + 
	        		"where dt_conclusao > '2024-04-01 00:00:00' " + 
	        		"and st_evento in (0, 1) " + 
	        		"group by dt_conclusao, nr_placa, nr_pista, nm_equipamento, vl_medida " + 
	        		"having count(dt_conclusao) > 1 " + 
	        		")order by dt_conclusao, st_evento, cd_evento desc";

	        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            ResultSetMap resultSet = new ResultSetMap(selectStatement.executeQuery());
	        System.out.println("Total com duplicidade encontrado: " + resultSet.size());
            while (resultSet.next()) {
            	GregorianCalendar rawDtConclusao = resultSet.getGregorianCalendar("dt_conclusao");
            	String dtConclusao = DateUtil.formatDate(rawDtConclusao, "dd/MM/yyyy HH:mm:ss.sss");
            	int cdEquipamento = resultSet.getInt("cd_equipamento");
            	if(datasJaVistas.get(cdEquipamento) != null && datasJaVistas.get(cdEquipamento).equals(dtConclusao)) {
            		continue;
            	}
            	datasJaVistas.put(cdEquipamento, dtConclusao);
            	
            	PreparedStatement pstmt = connection.prepareStatement("update mob_evento_equipamento set st_evento = 2, cd_motivo_cancelamento = 14, dt_cancelamento = '2024-06-19', cd_usuario_cancelamento = 39, cd_usuario_confirmacao = null, dt_processamento = null where cd_evento in ("+resultSet.getInt("cd_evento")+")");
            	int resultado = pstmt.executeUpdate();
            	if(resultado <= 0) {
            		throw new Exception("Erro ao atualizar duplicado");
            	}
            }
	        //connection.commit();
	        System.out.println("----------------------------------- FINALIZADO ---------------------------------------");

	    } catch (Exception e) {
	        e.printStackTrace();
	        if (customConnection != null) {
	            try {
	                customConnection.getConnection().rollback();
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
	    } finally {
	        if (customConnection != null) {
	            try {
	                customConnection.finishConnection();
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
	    }
	}
}


