package com.tivic.manager.acd;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.evt.Evento;
import com.tivic.manager.evt.EventoDAO;
import com.tivic.manager.evt.EventoPessoa;
import com.tivic.manager.evt.EventoPessoaDAO;
import com.tivic.manager.evt.EventoPessoaServices;
import com.tivic.manager.evt.Ocorrencia;
import com.tivic.manager.evt.OcorrenciaServices;
import com.tivic.manager.grl.CidadeServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.sol.connection.Conexao;

import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Util;
import sol.util.Result;
@Deprecated
public class JornadaServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {

	        PrintWriter out    = res.getWriter();	        
			int cdEstado       = req.getParameter("cdEstado") != null ? Integer.valueOf(req.getParameter("cdEstado")) : 0;
			String codigo      = req.getParameter("codigo");
			String comprovante = req.getParameter("comprovante");
			String registrar   = req.getParameter("registrar");
			String nrInscricao = req.getParameter("nrinscricao");
			
			if(codigo != null && codigo.equals("VCjornada2016/01")){
				out.println("true");
	        	out.close();
	        	return;
			}
			
			if(registrar != null && registrar.equals("entrada")){
				Connection connect = Conexao.conectar();
				
				connect.setAutoCommit(false);
				if(nrInscricao.contains("/2016")){	
					String[] splitInscricao = nrInscricao.split("\\/");
					int cdPessoa = Integer.valueOf(splitInscricao[0]);
					if(cdPessoa > 0) {
						PessoaFisica pessoa = PessoaFisicaDAO.get(cdPessoa, connect);
						Ocorrencia ocorrencia = new Ocorrencia();
						ocorrencia.setCdOcorrencia(0);
						ocorrencia.setCdEvento(1);
						ocorrencia.setCdPessoa(pessoa.getCdPessoa());
						ocorrencia.setTpOcorrencia(0);
						ocorrencia.setTxtOcorrencia("REGISTRO DE ENTRADA DA JORNADA PEDAGÓGICA PARA " + nrInscricao);
						ocorrencia.setDtOcorrencia(new GregorianCalendar());
						
						Result saveOcorrencia = OcorrenciaServices.save(ocorrencia, connect);
						
						if(saveOcorrencia.getCode() > 0) {
							out.println("true");
						} else {
							out.println("false");
						}				
					} else {
						out.println("false");
					}
				} else {
					out.println("false");					
				}
				
//				connect.commit();
				out.close();
				return;
			}
			
			if(registrar != null && registrar.equals("saida")){
				String[] splitInscricao = nrInscricao.split("\\/");
				int cdPessoa = Integer.valueOf(splitInscricao[0]);
				if(cdPessoa > 0) {
					PessoaFisica pessoa = PessoaFisicaDAO.get(cdPessoa);
					Ocorrencia ocorrencia = new Ocorrencia();
					ocorrencia.setCdOcorrencia(0);
					ocorrencia.setCdEvento(1);
					ocorrencia.setCdPessoa(pessoa.getCdPessoa());
					ocorrencia.setTpOcorrencia(1);
					ocorrencia.setTxtOcorrencia("REGISTRO DE SAÍDA DA JORNADA PEDAGÓGICA PARA " + nrInscricao);
					ocorrencia.setDtOcorrencia(new GregorianCalendar());
					
					Result saveOcorrencia = OcorrenciaServices.save(ocorrencia);
					
					if(saveOcorrencia.getCode() > 0) {
						out.println("true");
					} else {
						out.println("false");
					}				
				} else {
					out.println("false");
				}
				
				out.close();
				return;
			}
			
			if(comprovante != null && !comprovante.equals("")){
				ResultSetMap rsmPessoa = PessoaServices.findByCpfCnpj(comprovante);
				
				if(rsmPessoa.next()) {
					PessoaFisica pessoa = PessoaFisicaDAO.get(rsmPessoa.getInt("CD_PESSOA"));
					ResultSetMap eventosPessoa = EventoPessoaServices.findEventoPessoa(pessoa.getCdPessoa(), null);	
					
					ResultSetMap rsmConfirmacao = new ResultSetMap();
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_PESSOA", pessoa.getNmPessoa());
					register.put("NR_RG", pessoa.getNrRg());
					register.put("NR_CPF", pessoa.getNrCpf());
					register.put("NR_INSCRICAO", pessoa.getCdPessoa()+"/2016");
					register.put("NR_MATRICULA", pessoa.getIdPessoa());
					register.put("DT_NASCIMENTO", pessoa.getDtNascimento().getTime());
					
					int i = 1;
					while(eventosPessoa.next()){
						register.put("EVENTO_"+i, eventosPessoa.getString("NM_EVENTO"));
						i++;
					}
					
					rsmConfirmacao.addRegister(register);
					
					req.getSession().removeAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
					HashMap<String, Object> paramns = new HashMap<String, Object>();
//					Result report = ReportServices.setReportSession(req.getSession(), "acd/confirmacao_jornada", paramns, rsmConfirmacao);
					
					res.sendRedirect("../report?=0");
				}
				
	        	out.close();
	        	return;
			}
			
	        if(cdEstado > 0){
	        	JSONArray   cidades = new JSONArray();
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
	    } catch (Exception e) {
	        PrintWriter out = res.getWriter();
	        out.println(e);
	        out.close();
	    }
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		Connection connect = null;
        PrintWriter out = res.getWriter();			
        res.setContentType("text/html; charset=utf-8");
		JSONObject response = new JSONObject();
        
		String action = req.getParameter("a");
		
		try {
		
			if (action.equals("passo1")){
				String nome         = req.getParameter("nome");
				String email        = req.getParameter("email");
				String dtNascimento = req.getParameter("dtnascimento");
				String sexo         = req.getParameter("sexo");
				String smed         = req.getParameter("smed");
				
				GregorianCalendar dataNascimento = Util.convStringCalendar(dtNascimento);
				if(nome != null && email != null && dataNascimento != null  && sexo != null){
					
					req.getSession().setAttribute("nome", nome);
					req.getSession().setAttribute("email", email);
					req.getSession().setAttribute("dataNascimento", dataNascimento);
					req.getSession().setAttribute("sexo", sexo);
					
					response.put("code", 1);
					if(smed != null && Integer.valueOf(smed) == 1) {
						response.put("dispatcher", "../jornada/participante/?smed=convidados");
					} else {
						response.put("dispatcher", "../jornada/participante/");
					}
										
				} else {
					
					response.put("code", 0);
					response.put("dispatcher", "null");
					
				}
				
			} else if (action.equals("passo2")){
				
				connect = Conexao.conectar();
				connect.setAutoCommit(false); 	
								
				String nr_rg         = req.getParameter("rg");
				String nr_cpf        = req.getParameter("cpf");
				String matricula     = req.getParameter("matricula");
				String estado        = req.getParameter("estado");
				String cidade        = req.getParameter("cidade");
				String logradouro    = req.getParameter("logradouro");
				String complemento   = req.getParameter("complemento");
				String telefone      = req.getParameter("telefone");
				String numero        = req.getParameter("numero");
				String bairro        = req.getParameter("bairro");
				String cep           = req.getParameter("cep");
				String senha         = req.getParameter("senha");
				String cargo_publico = req.getParameter("cargo_publico");
				String evento_1      = req.getParameter("evento_quinta_tarde");
				String evento_2      = req.getParameter("evento_sexta_manha");
				String evento_3      = req.getParameter("evento_sexta_tarde");
				String evento_4      = req.getParameter("evento_sabado_manha");
				String evento_5      = req.getParameter("evento_sabado_tarde");
				
				if(nr_rg != null && nr_cpf != null && estado != null  && cidade != null && logradouro != null && bairro != null  && cep != null){
					
					req.getSession().setAttribute("nr_rg", nr_rg);
					req.getSession().setAttribute("nr_cpf", nr_cpf);
					req.getSession().setAttribute("matricula", matricula);
					req.getSession().setAttribute("estado", estado);
					req.getSession().setAttribute("cidade", cidade);
					req.getSession().setAttribute("numero", numero);
					req.getSession().setAttribute("logradouro", logradouro);
					req.getSession().setAttribute("complemento", complemento);
					req.getSession().setAttribute("bairro", bairro);
					req.getSession().setAttribute("cep", cep);
					
					PessoaFisica pessoa = new PessoaFisica();
					
					pessoa.setCdPessoa(0);
					pessoa.setNmPessoa((String)req.getSession().getAttribute("nome"));
					pessoa.setDtCadastro(new GregorianCalendar());
					pessoa.setGnPessoa(1);
					pessoa.setNmEmail((String)req.getSession().getAttribute("email"));
					if(telefone.length() < 14){
						pessoa.setNrTelefone1(telefone);
					} else {
						pessoa.setNrCelular(telefone);
					}
					pessoa.setStCadastro(1);
					pessoa.setNrCpf(nr_cpf.replaceAll("-", ""));
					pessoa.setNrRg(nr_rg);
					pessoa.setTpSexo(1);
					pessoa.setIdPessoa(matricula);
					pessoa.setDtNascimento((GregorianCalendar)req.getSession().getAttribute("dataNascimento"));
					
					PessoaEndereco endereco = new PessoaEndereco();
					endereco.setNmBairro(bairro);
					endereco.setNmLogradouro(logradouro);
					endereco.setNrCep(cep.replaceAll("-", ""));
					endereco.setNmComplemento(complemento);
					endereco.setLgPrincipal(1);
					endereco.setNrEndereco(numero);
					endereco.setCdCidade(Integer.valueOf(cidade));
					
					PessoaFisica p = new PessoaFisica();

					Result save = new Result(0);
					ResultSet rs = connect.prepareStatement("SELECT A.cd_pessoa FROM grl_pessoa A, grl_pessoa_fisica B " +
                            "WHERE A.cd_pessoa = B.cd_pessoa AND nr_cpf = \'"+pessoa.getNrCpf().trim()+"\'").executeQuery();
					if (!rs.next())
						save = PessoaServices.save(pessoa, endereco, 0, 0, connect);
					else {
						p = PessoaFisicaDAO.get(rs.getInt("CD_PESSOA"), connect);
						p.setIdPessoa(matricula);
						p.setDtNascimento(pessoa.getDtNascimento());
						PessoaFisicaDAO.update(p, connect);	
						
//						if(p.getDtNascimento().compareTo(pessoa.getDtNascimento())!=0){
//							response.put("code", -1);
//							response.put("message", "A data de nascimento informada não corresponde ao registrado no sistema da Secretaria Municipal de Educação.");		
//							out.print(response);
//							out.close();
//							return;
//						}
					}
					
					ResultSetMap rsmTipoParticipacaoConvidado = EventoPessoaDAO.getAllByParticipacao(0, 1, connect);
					ResultSetMap rsmTipoParticipacaoParticipante = EventoPessoaDAO.getAllByParticipacao(3, 1, connect);					
					ResultSetMap rsmEventoPessoa = EventoPessoaServices.findEventoPessoa(p.getCdPessoa(), connect);
					
					if(rsmEventoPessoa.size() >= 1){
						response.put("code", -1);
						response.put("message", "Essa pessoa já está cadastrada na Jornada Pedagógica e selecionou seus cursos.");		
						out.print(response);
						out.close();
						return;
					}
					
					if(rsmTipoParticipacaoConvidado.size() >= 140 && matricula == null && cargo_publico == null){
						response.put("code", -1);
						response.put("message", "Quantidade de vagas de convidados para a Jornada da Educação já foram preenchidas.");		
						out.print(response);
						out.close();
						return;
					}
					
					if(rsmTipoParticipacaoParticipante.size() >= 1560 && matricula != null && cargo_publico != null){
						response.put("code", -1);
						response.put("message", "Quantidade de vagas da Jornada Pedagógica já foram preenchidas.");		
						out.print(response);
						out.close();
						return;
					}
					
					if(save.getCode() > 0 || p.getCdPessoa() > 0){

						EventoPessoa eventoPessoa = new EventoPessoa();
						pessoa = save.getCode() > 0 ? (PessoaFisica) save.getObjects().get("PESSOA") : p;
						String idInscricao = pessoa.getCdPessoa() + "/2016";


						/* CHECA DISPONIBILIDADE DE VAGAS PARA O EVENTO, CASO HOUVER VAGAS SALVA O REGISTRO
						 * CASO O CONTRÁRIO DESCARTA E RETORNA A MENSAGEM
						 */
						
						Evento objEvento = EventoDAO.get(1, connect);
						ResultSetMap rsmEvento = EventoPessoaDAO.getAllByEvento(1, connect);						
						
						if(rsmEvento.size() >= objEvento.getQtVagas()) {
							response.put("code", -1);
							response.put("message", "Quantidade de vagas preenchidas para " + objEvento.getNmEvento());
							
							out.flush();
							out.println(response);
							return;
						} else {
							eventoPessoa.setCdEvento(1);
							eventoPessoa.setCdPessoa(pessoa.getCdPessoa());
							eventoPessoa.setTpParticipacao(matricula == null || matricula.equals("") ? 0 : 3);
							eventoPessoa.setIdCadastro(idInscricao);
							eventoPessoa.setNrMatricula(matricula);
							if(cargo_publico != null && Integer.valueOf(cargo_publico) != null) {
								eventoPessoa.setTpCargoPublico(Integer.valueOf(cargo_publico));
							}
							
							Result eventoPrincipal = EventoPessoaServices.save(eventoPessoa, connect);
						}
						
						
						/* CHECA DISPONIBILIDADE DE VAGAS PARA O EVENTO, CASO HOUVER VAGAS SALVA O REGISTRO
						 * CASO O CONTRÁRIO DESCARTA E RETORNA A MENSAGEM
						 */
						
						Evento objEvento1 = EventoDAO.get(Integer.valueOf(evento_1));
						ResultSetMap rsmEvento1 = EventoPessoaDAO.getAllByEvento(Integer.valueOf(evento_1));
						
						if(rsmEvento1.size() >= objEvento1.getQtVagas()) {
							response.put("code", -1);
							response.put("message", "Quantidade de vagas preenchidas para " + objEvento1.getNmEvento());
							
							out.flush();
							out.println(response);
							return;
						} else {
							eventoPessoa = new EventoPessoa();
							eventoPessoa.setCdEvento(Integer.valueOf(evento_1));
							eventoPessoa.setCdPessoa(pessoa.getCdPessoa());
							eventoPessoa.setTpParticipacao(matricula == null || matricula.equals("") ? 0 : 3);
							eventoPessoa.setIdCadastro(idInscricao);
							eventoPessoa.setNrMatricula(matricula);
							if(cargo_publico != null && Integer.valueOf(cargo_publico) != null) {
								eventoPessoa.setTpCargoPublico(Integer.valueOf(cargo_publico));
							}
							
							Result evento1 = EventoPessoaServices.save(eventoPessoa, connect);
						}
						
						
						/* CHECA DISPONIBILIDADE DE VAGAS PARA O EVENTO, CASO HOUVER VAGAS SALVA O REGISTRO
						 * CASO O CONTRÁRIO DESCARTA E RETORNA A MENSAGEM
						 */
						
						Evento objEvento2 = EventoDAO.get(Integer.valueOf(evento_2), connect);
						ResultSetMap rsmEvento2 = EventoPessoaDAO.getAllByEvento(Integer.valueOf(evento_2), connect);
						
						if(rsmEvento2.size() >= objEvento2.getQtVagas()) {
							response.put("code", -1);
							response.put("message", "Quantidade de vagas preenchidas para " + objEvento2.getNmEvento());
							
							out.flush();
							out.println(response);
							return;
						} else {
							eventoPessoa = new EventoPessoa();
							eventoPessoa.setCdEvento(Integer.valueOf(evento_2));
							eventoPessoa.setCdPessoa(pessoa.getCdPessoa());
							eventoPessoa.setTpParticipacao(matricula == null || matricula.equals("") ? 0 : 3);
							eventoPessoa.setIdCadastro(idInscricao);
							eventoPessoa.setNrMatricula(matricula);
							if(cargo_publico != null && Integer.valueOf(cargo_publico) != null) {
								eventoPessoa.setTpCargoPublico(Integer.valueOf(cargo_publico));
							}
							
							Result evento2 = EventoPessoaServices.save(eventoPessoa, connect);
						}
						
						
						/* CHECA DISPONIBILIDADE DE VAGAS PARA O EVENTO, CASO HOUVER VAGAS SALVA O REGISTRO
						 * CASO O CONTRÁRIO DESCARTA E RETORNA A MENSAGEM
						 */

						Evento objEvento3 = EventoDAO.get(Integer.valueOf(evento_3), connect);
						ResultSetMap rsmEvento3 = EventoPessoaDAO.getAllByEvento(Integer.valueOf(evento_3), connect);					
						
						if(rsmEvento3.size() >= objEvento3.getQtVagas()) {
							response.put("code", -1);
							response.put("message", "Quantidade de vagas preenchidas para " + objEvento3.getNmEvento());
							
							out.flush();
							out.println(response);
							return;
						} else {
							eventoPessoa = new EventoPessoa();
							eventoPessoa.setCdEvento(Integer.valueOf(evento_3));
							eventoPessoa.setCdPessoa(pessoa.getCdPessoa());
							eventoPessoa.setTpParticipacao(matricula == null || matricula.equals("") ? 0 : 3);
							eventoPessoa.setIdCadastro(idInscricao);
							eventoPessoa.setNrMatricula(matricula);
							if(cargo_publico != null && Integer.valueOf(cargo_publico) != null) {
								eventoPessoa.setTpCargoPublico(Integer.valueOf(cargo_publico));
							}
							
							Result evento3 = EventoPessoaServices.save(eventoPessoa, connect);
						}
						
						
						/* CHECA DISPONIBILIDADE DE VAGAS PARA O EVENTO, CASO HOUVER VAGAS SALVA O REGISTRO
						 * CASO O CONTRÁRIO DESCARTA E RETORNA A MENSAGEM
						 */
						
						Evento objEvento4 = EventoDAO.get(Integer.valueOf(evento_4), connect);
						ResultSetMap rsmEvento4 = EventoPessoaDAO.getAllByEvento(Integer.valueOf(evento_4), connect);						
						
						if(rsmEvento4.size() >= objEvento4.getQtVagas()) {
							response.put("code", -1);
							response.put("message", "Quantidade de vagas preenchidas para " + objEvento4.getNmEvento());
							
							out.flush();
							out.println(response);
							return;
						} else {
							eventoPessoa = new EventoPessoa();
							eventoPessoa.setCdEvento(Integer.valueOf(evento_4));
							eventoPessoa.setCdPessoa(pessoa.getCdPessoa());
							eventoPessoa.setTpParticipacao(matricula == null || matricula.equals("") ? 0 : 3);
							eventoPessoa.setIdCadastro(idInscricao);
							eventoPessoa.setNrMatricula(matricula);
							if(cargo_publico != null && Integer.valueOf(cargo_publico) != null) {
								eventoPessoa.setTpCargoPublico(Integer.valueOf(cargo_publico));
							}
							
							Result evento4 = EventoPessoaServices.save(eventoPessoa, connect);
						}

						Evento objEvento5 = EventoDAO.get(Integer.valueOf(evento_5), connect);
						ResultSetMap rsmEvento5 = EventoPessoaDAO.getAllByEvento(Integer.valueOf(evento_5), connect);						
						
						if(rsmEvento5.size() >= objEvento5.getQtVagas()) {
							response.put("code", -1);
							response.put("message", "Quantidade de vagas preenchidas para " + objEvento5.getNmEvento());
							
							out.flush();
							out.println(response);
							return;
						} else {
							eventoPessoa = new EventoPessoa();
							eventoPessoa.setCdEvento(Integer.valueOf(evento_5));
							eventoPessoa.setCdPessoa(pessoa.getCdPessoa());
							eventoPessoa.setTpParticipacao(matricula == null || matricula.equals("") ? 0 : 3);
							eventoPessoa.setIdCadastro(idInscricao);
							eventoPessoa.setNrMatricula(matricula);
							if(cargo_publico != null && Integer.valueOf(cargo_publico) != null) {
								eventoPessoa.setTpCargoPublico(Integer.valueOf(cargo_publico));
							}
							
							Result evento5 = EventoPessoaServices.save(eventoPessoa, connect);
						}
						
												
						PessoaFisica pessoaFisica = PessoaFisicaDAO.get(pessoa.getCdPessoa(), connect);

						ResultSetMap rsmConfirmacao = new ResultSetMap();
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NM_PESSOA", pessoa.getNmPessoa());
						register.put("NR_RG", pessoaFisica.getNrRg());
						register.put("NR_CPF", pessoaFisica.getNrCpf());
						register.put("NR_INSCRICAO", idInscricao);
						register.put("NR_MATRICULA", pessoa.getIdPessoa());
						register.put("DT_NASCIMENTO", pessoaFisica.getDtNascimento().getTime());
						register.put("EVENTO_1", objEvento1.getNmEvento());
						register.put("EVENTO_2", objEvento2.getNmEvento());
						register.put("EVENTO_3", objEvento3.getNmEvento());
						register.put("EVENTO_4", objEvento4.getNmEvento());
						register.put("EVENTO_5", objEvento5.getNmEvento());
						
						rsmConfirmacao.addRegister(register);
						
						req.getSession().removeAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
						HashMap<String, Object> paramns = new HashMap<String, Object>();
//						Result report = ReportServices.setReportSession(req.getSession(), "acd/confirmacao_jornada", paramns, rsmConfirmacao);
//						if(report.getCode() == 1){
							response.put("dispatcher", "../../report?=0");
//						}
					}
					
					response.put("code", save.getCode());
					response.put("message", save.getMessage());

					
					connect.commit();
				}
			}
			
			out.flush();
			out.println(response);
	        out.close(); 
        
        } catch (Exception e){
        	e.printStackTrace();
			Conexao.rollback(connect);
        	System.err.println(e);
        }
		finally{
			if(connect != null)
				Conexao.desconectar(connect);
		}
		
	}

}
