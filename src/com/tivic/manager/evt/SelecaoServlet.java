
package com.tivic.manager.evt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tivic.manager.adm.ContaReceber;
import com.tivic.manager.adm.ContaReceberDAO;
import com.tivic.manager.egov.DAMServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.CidadeServices;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.PessoaDoenca;
import com.tivic.manager.grl.PessoaDoencaServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.print.ReportServlet;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoConta;
import com.tivic.manager.ptc.DocumentoContaServices;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.RequestUtilities;
import sol.util.Result;


@WebServlet("/SelecaoServlet")
public class SelecaoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final String USER_AGENT = "Mozilla/5.0";


    public SelecaoServlet() {
        super();

    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
		String acao         = RequestUtilities.getParameterAsString(request, "acao", "");		
		if(!acao.equals("") && acao.equals("emitirBoleto")){
			this.emitirBoleto(request, response);
			return;
		}
			
		int    cdEstado 	= RequestUtilities.getParameterAsInteger(request, "cdEstado", 0);
		
		if(cdEstado > 0){
			this.getEstados(cdEstado, request, response);
			return;
        }
		

		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String acao         = RequestUtilities.getParameterAsString(request, "acao", "");		
		if(!acao.equals("") && acao.equals("emitirBoleto")){
			this.emitirBoleto(request, response);
			return;
		}
		
		Connection  connect = null;
		JSONObject  res     = new JSONObject();
		PrintWriter out    = response.getWriter();
		String      nrInscricao = String.valueOf(System.currentTimeMillis()).substring(String.valueOf(System.currentTimeMillis()).length()-9);
		response.setHeader("charset", "UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		GregorianCalendar dtLimiteIsencao = new GregorianCalendar();
		dtLimiteIsencao.set(new GregorianCalendar().get(Calendar.YEAR), GregorianCalendar.DECEMBER, 6, 23, 59, 59);
		
		try {
			connect = Conexao.conectar();
			connect.setAutoCommit(false); 
			
			/* Instanciamento de variaveis */
			int    tpSexo                  = RequestUtilities.getParameterAsInteger(request, "sexo", 0);
			int    cdEstadoRg              = RequestUtilities.getParameterAsInteger(request, "unfederativa", 0);
			String nmPessoa                = RequestUtilities.getParameterAsString(request, "nome", "");
			String nrRg                    = RequestUtilities.getParameterAsString(request, "nr_rg", "");
			String sgOrgaoRg               = RequestUtilities.getParameterAsString(request, "orgao", "");
			String nrCpf                   = RequestUtilities.getParameterAsString(request, "nr_cpf", "");
			GregorianCalendar dtNascimento = RequestUtilities.getParameterAsGregorianCalendar(request, "dt_nascimento", null);
			String[] deficiencias		   = request.getParameterValues("deficiencia") != null ? request.getParameterValues("deficiencia") : new String[0];
			String txtDeficienciaObs       = RequestUtilities.getParameterAsString(request, "txtDeficienciaObservacao", "");
			
	
			int    cdCidade                = RequestUtilities.getParameterAsInteger(request, "cidades", 0);
			String nmBairro                = RequestUtilities.getParameterAsString(request, "bairro", "");
			String nmLogradouro            = RequestUtilities.getParameterAsString(request, "logradouro", "");
			String nrCep                   = RequestUtilities.getParameterAsString(request, "nr_cep", "");
			String nrEndereco              = RequestUtilities.getParameterAsString(request, "nr_endereco", "");
			String nmComplemento           = RequestUtilities.getParameterAsString(request, "complemento", "");
			
			String nrTelefone              = RequestUtilities.getParameterAsString(request, "nr_telefone", null);
			String nrCelular               = RequestUtilities.getParameterAsString(request, "nr_celular", null);
			String nmEmail                 = RequestUtilities.getParameterAsString(request, "email", null);
	
			int    lgLactante              = RequestUtilities.getParameterAsInteger(request, "lactante", 0);
			int    cdVaga                  = RequestUtilities.getParameterAsInteger(request, "vagas", 0);
			int    cdLocal                 = RequestUtilities.getParameterAsInteger(request, "localidade", 0);
			int    isencao                 = RequestUtilities.getParameterAsInteger(request, "isencao", 0);
			String nrNis                   = RequestUtilities.getParameterAsString(request, "nr_isencao", "");
			boolean gratuidade			   = false;
						
			/* Instanciamento de classes */
			Evento         evento 	       = new Evento();
			PessoaFisica   pessoa          = new PessoaFisica();
			PessoaEndereco endereco        = new PessoaEndereco();
			EventoPessoa   eventoPessoa    = new EventoPessoa();
			ContaReceber   contaReceber    = new ContaReceber();
			
			/* Verificações */			
			pessoa                         = PessoaFisicaServices.getByCpf(nrCpf, connect);
			ResultSetMap rsmEventoPessoa = new ResultSetMap();
			HashMap<String, Object> ultimaInscricao = new HashMap<String, Object>(); 
			
			if(pessoa != null && pessoa.getCdPessoa() > 0){						
					evento = EventoDAO.get(cdVaga, connect);
					
					if(evento != null && evento.getCdEvento() > 0) {
						rsmEventoPessoa = EventoPessoaServices.findCadastrosByEvento(evento.getCdEventoPrincipal(), pessoa.getCdPessoa(), connect);
						if(rsmEventoPessoa.next()){
							res.put("code", "-1");
							res.put("message", "Você já possui um cadastro ativo para esta seleção, favor emitir comprovante.");
							out.print(res.toString());
							return;
						}
						
					} else {
						res.put("code", "-2");
						res.put("message", "Houve um problema ao buscar a vaga selecionada.");
						out.print(res.toString());
						return;
					}
			} else {
				pessoa = new PessoaFisica();
				pessoa.setCdPessoa(0);				
				evento = EventoDAO.get(cdVaga, connect);
			}
			
			ArrayList<PessoaDoenca> listPessoaDoenca = new ArrayList<PessoaDoenca>();
			if(deficiencias != null && deficiencias.length > 0){
				for(String def : deficiencias) {
					PessoaDoenca pessoaDoenca = new PessoaDoenca();
					pessoaDoenca.setCdDoenca(Integer.parseInt(def));
					pessoaDoenca.setCdPessoa(pessoa.getCdPessoa());
					
					if(!txtDeficienciaObs.trim().equals(""))
						pessoaDoenca.setTxtDescricao(txtDeficienciaObs);
					
					listPessoaDoenca.add(pessoaDoenca);
				}
				
				if(PessoaDoencaServices.limparDoencasByPessoa(pessoa.getCdPessoa(), connect) >= 0){
					for(PessoaDoenca pessoaDoenca : listPessoaDoenca){
						PessoaDoencaServices.save(pessoaDoenca, connect);
					}
				}
			}
			
			Evento eventoPrincipal = EventoDAO.get(evento.getCdEventoPrincipal(), connect);
			
			if(Util.compareDates(new GregorianCalendar(), eventoPrincipal.getDtFinal() ) > 0){
				res.put("code", "-1");
				res.put("message", "Período de inscrições finalizada.");
				out.print(res.toString());		
				return;
			}
			
			pessoa.setNmPessoa(nmPessoa.toUpperCase());
			pessoa.setDtNascimento(dtNascimento);
			pessoa.setNrRg(nrRg);
			pessoa.setNrCpf(Util.apenasNumeros(nrCpf));
			pessoa.setSgOrgaoRg(sgOrgaoRg);
			pessoa.setCdEstadoRg(cdEstadoRg);
			pessoa.setTpSexo(tpSexo);
			pessoa.setNrTelefone1(Util.apenasNumeros(nrTelefone));
			pessoa.setNrCelular(Util.apenasNumeros(nrCelular));
			pessoa.setNmEmail(nmEmail.toLowerCase());
			pessoa.setLgDeficienteFisico(lgLactante);
			
			endereco.setCdCidade(cdCidade);
			endereco.setNmLogradouro(nmLogradouro);
			endereco.setNrEndereco(nrEndereco);
			endereco.setNrCep(Util.apenasNumeros(nrCep));
			endereco.setNmBairro(nmBairro);
			endereco.setNmComplemento(nmComplemento);
			
			if(pessoa.getCdPessoa() > 0) {
				endereco.setCdPessoa(pessoa.getCdPessoa());
				PessoaFisicaDAO.update(pessoa, connect);
				PessoaEnderecoDAO.update(endereco, connect);
			} else {
				PessoaServices.save(pessoa, endereco, 0, 0, connect);
			}
			
			if(evento.getVlInscricao() > 0) {
									
				contaReceber.setVlConta(evento.getVlInscricao());
				contaReceber.setCdPessoa(pessoa.getCdPessoa());
				contaReceber.setNrDocumento(nrInscricao);
				contaReceber.setNrReferencia(nrInscricao);
				contaReceber.setIdContaReceber(nrInscricao);
				contaReceber.setCdFormaPagamento(2);
				contaReceber.setDsHistorico("Pagamento de inscrição do sistema de seleção");
				contaReceber.setDtEmissao(new GregorianCalendar());
				contaReceber.setDtVencimento(eventoPrincipal.getDtVencimentoBoleto());
				contaReceber.setCdTipoDocumento(12);
				contaReceber.setVlAbatimento(0.0);
				contaReceber.setVlAcrescimo(0.0);
				contaReceber.setVlRecebido(0.0);
				contaReceber.setPrJuros(0.0);
				contaReceber.setPrMulta(0.0);
				
				int cdContaReceber = ContaReceberDAO.insert(contaReceber, connect);			
				contaReceber       = ContaReceberDAO.get(cdContaReceber, connect);
			
			}
						
			eventoPessoa.setCdPessoa(pessoa.getCdPessoa());
			eventoPessoa.setCdEvento(eventoPrincipal.getCdEvento());
			eventoPessoa.setCdSubevento(evento.getCdEvento());
			eventoPessoa.setTpParticipacao(4);
			eventoPessoa.setIdCadastro(nrInscricao);		
			//eventoPessoa.setCdContaReceber(contaReceber.getCdContaReceber());
			
			//if(dtLimiteIsencao.getTime().getTime() >= new Date().getTime())
			eventoPessoa.setNrMatricula("");
			eventoPessoa.setDtInscricao(new GregorianCalendar());
			eventoPessoa.setCdLocal(0);
			
			Result saveEvento = EventoPessoaServices.save(eventoPessoa, connect);
			connect.commit();
			
			if(saveEvento.getCode() > 0) {
				res.put("code", "1");
				res.put("message", "Evento cadastrado com sucesso!");
				out.print(res.toString());
			}
			
			
			return;
			
		} catch (Exception e){
			System.out.println("Erro: "+ e);
			e.printStackTrace();
			Conexao.rollback(connect);
			return;
		}		
	}
	
	protected void emitirBoleto(HttpServletRequest request, HttpServletResponse response){
		try {			
			
			PrintWriter out    = response.getWriter();
			String nrCpf        = RequestUtilities.getParameterAsString(request, "nrCpf", "");
			int cdEvento        = RequestUtilities.getParameterAsInteger(request, "cdSelecao", 0);
			int cdVaga 	        = RequestUtilities.getParameterAsInteger(request, "cdVaga", 0);

			
			if(nrCpf.equals("")){
				out.print("Não foi possível possível completar a requisição. #1");
				return;
			}
			
			if(cdEvento == 0){
				out.print("Não foi possível possível completar a requisição. #2");
				return;
			}
			
			if(cdVaga == 0){
				out.print("Não foi possível possível completar a requisição. #3");
				return;
			}

			response.reset();
			response.setContentType("application/pdf");
			request.setAttribute("type", ReportServlet.PDF);

			Connection  connect = null;
			connect = Conexao.conectar();
			connect.setAutoCommit(false); 
			
			PessoaFisica pessoa = PessoaFisicaServices.getByCpf(nrCpf, connect);
			PessoaEndereco pessoaEndereco = PessoaEnderecoServices.getEnderecoPrincipal(pessoa.getCdPessoa(), connect);
			Cidade cidade = CidadeDAO.get(pessoaEndereco.getCdCidade(), connect);
			Estado estado = EstadoDAO.get(cidade.getCdEstado(), connect);
			
			
			Evento eventoPrincipal = EventoDAO.get(cdEvento, connect);
			Evento subEvento = EventoDAO.get(cdVaga, connect);
			EventoPessoa eventoPessoa = new EventoPessoa();
			DocumentoConta conta = new DocumentoConta();
			
			ResultSetMap rsmEventoPessoa = EventoPessoaServices.findCadastrosBySubevento(subEvento.getCdEventoPrincipal(), subEvento.getCdEvento(), pessoa.getCdPessoa(), connect);
			if(rsmEventoPessoa.getLines().size() > 0){
				int cdInscricao = (int)rsmEventoPessoa.getLines().get(0).get("CD_INSCRICAO");
				eventoPessoa = EventoPessoaDAO.get(pessoa.getCdPessoa(), cdEvento, cdInscricao, connect);
			}
			
			if(eventoPessoa != null){
				String idCadastro = String.format ("%015d", Integer.parseInt(eventoPessoa.getIdCadastro()));
				ContaReceber contaReceber = ContaReceberDAO.get(eventoPessoa.getCdContaReceber(), connect);				
				Documento documento = EventoPessoaServices.getInscricaoDocumento(idCadastro, connect);
				
				if(contaReceber != null && documento == null) {
					documento = new Documento();
					documento.setCdSituacaoDocumento(1);
					documento.setCdEmpresa(2);
					documento.setTxtDocumento("Pagamento de Inscrição");
					documento.setDtProtocolo(new GregorianCalendar());
					documento.setTpDocumento(DocumentoServices.TP_PRIORIDADE_NORMAL);
					documento.setNrDocumento(idCadastro);
										
					ArrayList<DocumentoPessoa> solicitante = new ArrayList<DocumentoPessoa>();				
					DocumentoPessoa docPessoa = new DocumentoPessoa();
					docPessoa.setCdPessoa(pessoa.getCdPessoa());
					docPessoa.setNmQualificacao("Participante Seleção");
					solicitante.add(docPessoa);
					
					Result documentoService = DocumentoServices.save(documento, solicitante, connect);
					documento = (Documento) documentoService.getObjects().get("DOCUMENTO");
	
					if(documento.getCdDocumento() > 0) {
						conta = new DocumentoConta();
						conta.setCdContaReceber(contaReceber.getCdContaReceber());
						conta.setCdDocumento(documento.getCdDocumento());
						conta.setStDocumentoConta(1);
						Result documentoConta = DocumentoContaServices.save(conta, connect);
						conta.setCdDocumentoConta(documentoConta.getCode());
					}
				} else {
					conta = EventoPessoaServices.getInscricaoConta(documento.getCdDocumento(), connect);
				}
								
				Result dam = null;
				
				if(documento.getCdDocumento() > 0 && conta.getCdDocumentoConta() > 0 && conta.getCdContaReceber() > 0){
					dam = DAMServices.getCodigoBarrasEL(
							String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), 
						    subEvento.getDtVencimentoBoleto(), 
						    subEvento.getVlInscricao(), 
						    (int) 1, 
						    pessoa.getNmPessoa(), 
						    "F", 
						    documento.getNrDocumento(),
						    pessoa.getNmPessoa(), 
						    pessoa.getCdPessoa(), 
						    documento.getCdDocumento(), 
						    conta.getCdDocumentoConta(), 
						    contaReceber.getCdContaReceber(), 0, connect
					    );
				}
				
				String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
				
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("nmRazaoSocial", "PREFEITURA MUNICIPAL DE VITÓRIA DA CONQUISTA");
				params.put("dsEnderecoEmpresa", "Praça Joaquim Correia, 55 - Centro - CEP: 45000-60");
				params.put("dsLocalizacaoEmpresa", "Vitória da Conquista - BA");
				params.put("dsFoneFax", "(77) 3424-8500");
				params.put("NM_EMPRESA", "PREFEITURA MUNICIPAL DE VITÓRIA DA CONQUISTA");
				params.put("nrAnoBase", eventoPrincipal.getDtVencimentoBoleto().get(Calendar.YEAR));
				params.put("nrCodMovimento", conta.getCodMovimento());

				params.put("dsMensagem", eventoPrincipal.getNmEvento() + "\nInscrição N° " + eventoPessoa.getIdCadastro() + "\nVaga concorrente: ["+subEvento.getIdEvento()+"] - 	" + subEvento.getNmEvento());
				params.put("nrParcela", 1);
				params.put("logoPath", "../../edf/imagens/");
				params.put("vlTotalGeral", String.valueOf(subEvento.getVlInscricao()));
				params.put("vlTotal", String.valueOf(subEvento.getVlInscricao()));
				params.put("NR_CODIGO_BARRAS_ATUAL", dam.getObjects().get("nrCodigoBarras"));
				params.put("dtVencimento", Util.formatDate(eventoPrincipal.getDtVencimentoBoleto(), "dd/MM/yyyy"));
				params.put("nmContribuinte", pessoa.getNmPessoa());
				params.put("nmLogradouro", pessoaEndereco.getNmLogradouro());
				params.put("nrEndereco", pessoaEndereco.getNrEndereco());
				params.put("nmBairro", pessoaEndereco.getNmBairro());
				params.put("nmComplemento", pessoaEndereco.getNmComplemento());
				params.put("nrCep", pessoaEndereco.getNrCep());
				params.put("vlTotalTributos", "0");
				params.put("nmCidade", cidade.getNmCidade());
				params.put("sgEstado", estado.getNmEstado());
				
				params.put("NR_CPF_CNPJ", pessoa.getNrCpf());
				params.put("DT_EMISSAO", contaReceber.getDtEmissao().getTime());
				params.put("NR_DOCUMENTO", contaReceber.getNrDocumento());

				params.put("nmServicos", "001 \t " + eventoPrincipal.getNmEvento() + " \t\t " + subEvento.getVlInscricao());
								
				params.put("nrCodigoBarras", dam.getObjects().get("nrCodigoBarras"));
				params.put("nrCodigoBarras1", dam.getObjects().get("bloco1"));
				params.put("nrCodigoBarras2", dam.getObjects().get("bloco2"));
				params.put("nrCodigoBarras3", dam.getObjects().get("bloco3"));
				params.put("nrCodigoBarras4", dam.getObjects().get("bloco4"));
				
				ArrayList<String> subreports = new ArrayList<String>();
				subreports.add("adm/dam_servicos");
				subreports.add("adm/dam_tributos");
				subreports.add("adm/dam_tributos_resumo");
				
				ResultSetMap rsmTributos = new ResultSetMap();
				HashMap<String, Object> tributos = new HashMap<String, Object>();
				tributos.put("CD_TRIBUTO", 1);
				tributos.put("NM_TRIBUTO", "SELEC " + eventoPessoa.getIdCadastro());
				tributos.put("VL_TRIBUTO", subEvento.getVlInscricao());
				
				ResultSetMap rsmServicos = new ResultSetMap();
				
				params.put("SUBREPORT_NAMES", subreports);
				params.put("rsmServicos", rsmServicos.getLines());
				params.put("rsmTributos", rsmTributos.getLines());
				params.put("rsmTributosR", rsmTributos.getLines());

				ResultSetMap rsm = new ResultSetMap();
				HashMap<String, Object> register = new HashMap<String, Object>();
				rsm.addRegister(register);
				
				connect.commit();
				
//				ReportServices.setReportSession(request.getSession(), "adm/dam", params, rsm);
				ReportServlet report = new ReportServlet();
				report.doPost(request, response);
			}
			
		} catch (Exception cnf){
			cnf.printStackTrace(System.out);
			response.reset();
			response.setContentType("text/html");
		}
	}
	
	protected void getEstados(int cdEstado, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException{
		JSONArray   cidades = new JSONArray();
		PrintWriter     out = response.getWriter();
    	ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
    	criterios.add(new ItemComparator("B.CD_ESTADO", String.valueOf(cdEstado), ItemComparator.EQUAL, Types.INTEGER));
    	ResultSetMap rsmCidades = CidadeServices.find(criterios);
    	while(rsmCidades.next()){
    		JSONObject cidade = new JSONObject();
    		cidade.put("cdCidade", rsmCidades.getInt("CD_CIDADE"));
    		cidade.put("nmCidade", rsmCidades.getString("NM_CIDADE"));
    		cidades.put(cidade);
    	}
    	out.println(cidades);
    	out.close();
    	return;
	}

}