package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.msg.Mensagem;
import com.tivic.manager.msg.MensagemServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ServicoRecorteOcorrenciaServices {
	
	public static final int TP_SUCESSO = 0;
	public static final int TP_ALERTA  = 1;
	public static final int TP_ERRO    = 2;

	public static Result save(ServicoRecorteOcorrencia servicoRecorteOcorrencia){
		return save(servicoRecorteOcorrencia, null, null);
	}

	public static Result save(ServicoRecorteOcorrencia servicoRecorteOcorrencia, AuthData authData){
		return save(servicoRecorteOcorrencia, authData, null);
	}

	public static Result save(ServicoRecorteOcorrencia servicoRecorteOcorrencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(servicoRecorteOcorrencia==null)
				return new Result(-1, "Erro ao salvar. ServicoRecorteOcorrencia é nulo");

			int retorno;
			if(servicoRecorteOcorrencia.getCdOcorrencia()==0){
				retorno = ServicoRecorteOcorrenciaDAO.insert(servicoRecorteOcorrencia, connect);
				servicoRecorteOcorrencia.setCdOcorrencia(retorno);
			}
			else {
				retorno = ServicoRecorteOcorrenciaDAO.update(servicoRecorteOcorrencia, connect);
			}
			
			if(servicoRecorteOcorrencia.getTpOcorrencia() != ServicoRecorteOcorrenciaServices.TP_SUCESSO) {
				//TODO: email
				
				Processo processo = ProcessoDAO.get(servicoRecorteOcorrencia.getCdProcesso(), connect);
				
//				String txtMensagem = "O seguinte erro aconteceu, numa tentativa de comunicação com webservice da "
//									 + "Publicações Online " + servicoRecorteOcorrencia.getTxtRespostaServico()!=null ? servicoRecorteOcorrencia.getTxtRespostaServico() : ""
//									 + " no dia "
//									 + (Util.formatDate(servicoRecorteOcorrencia.getDtOcorrencia(), "dd/MM/yyyy 'às' HH:mm")) + ".\n\n"
//									 + (processo!=null? "Erro referente ao processo " + processo.getNrProcesso():""+".\n\n")
//									 + "E-mail enviado automaticamente, por gentileza, não responder.\n"
//									 + "Em caso de dúvidas, entre em contato através dos nossos canais de atendimento (77)3429-8484 ou suportejuris@tivic.com.br.\n\n"
//									 + "Atenciosamente,\n"
//									 + "Tivic Tecnologia e Informação Ltda";
				
				String txtMensagem = "";
				if(servicoRecorteOcorrencia.getTxtRespostaServico()!=null) {
					txtMensagem = "O seguinte erro aconteceu, numa tentativa de comunicação com webservice da "
									+ "Publicações Online " + servicoRecorteOcorrencia.getTxtRespostaServico()
									+ " no dia "
									+ (Util.formatDate(servicoRecorteOcorrencia.getDtOcorrencia(), "dd/MM/yyyy 'às' HH:mm")) + ".\n\n"
									+ (processo!=null? "Erro referente ao processo " + processo.getNrProcesso(): "" + " .\n\n")
									+ "E-mail enviado automaticamente, por gentileza, não responder.\n"
									+ "Em caso de dúvidas, entre em contato através dos nossos canais de atendimento (77)3429-8484 ou suportejuris@tivic.com.br.\n\n"
									+ "Atenciosamente,\n"
									+ "Tivic Tecnologia e Informação Ltda";
				} else {
					txtMensagem = "O servidor da Publicações Online não respondeu à tentativa de comunicação realizada no dia "
									+ (Util.formatDate(servicoRecorteOcorrencia.getDtOcorrencia(), "dd/MM/yyyy 'às' HH:mm")) + ".\n\n"
									+ "E-mail enviado automaticamente, por gentileza, não responder.\n"
									+ "Em caso de dúvidas, entre em contato através dos nossos canais de atendimento (77)3429-8484 ou suportejuris@tivic.com.br.\n\n"
									+ "Atenciosamente,\n"
									+ "Tivic Tecnologia e Informação Ltda";
				}
				
				String dsAssunto = "Falha na comunicação com o webservice da Publicações Online";

				
				ArrayList<HashMap<String, Object>> destinatarios = new ArrayList<>();
				
				HashMap<String, Object> to = new HashMap<>();
				to.put("NM_EMAIL", "suportejuris@tivic.com.br");
				destinatarios.add(to);
				
//				to = new HashMap<>();
//				to.put("NM_EMAIL", "suporte@publicacoesonline.com.br");
//				destinatarios.add(to);
				
				MensagemServices.enviarMensagem(new Mensagem(
							0, 
							dsAssunto, 
							txtMensagem, 
							(authData!=null?authData.getUsuario().getCdUsuario():0), 
							servicoRecorteOcorrencia.getDtOcorrencia(), 
							0, 
							0, 
							0, 
							0, 
							servicoRecorteOcorrencia.getCdProcesso(), 
							0, 
							0), 
						destinatarios, null);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "SERVICORECORTEOCORRENCIA", servicoRecorteOcorrencia);
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
	public static Result remove(ServicoRecorteOcorrencia servicoRecorteOcorrencia) {
		return remove(servicoRecorteOcorrencia.getCdOcorrencia(), servicoRecorteOcorrencia.getCdServico());
	}
	public static Result remove(int cdOcorrencia, int cdServico){
		return remove(cdOcorrencia, cdServico, false, null, null);
	}
	public static Result remove(int cdOcorrencia, int cdServico, boolean cascade){
		return remove(cdOcorrencia, cdServico, cascade, null, null);
	}
	public static Result remove(int cdOcorrencia, int cdServico, boolean cascade, AuthData authData){
		return remove(cdOcorrencia, cdServico, cascade, authData, null);
	}
	public static Result remove(int cdOcorrencia, int cdServico, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ServicoRecorteOcorrenciaDAO.delete(cdOcorrencia, cdServico, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_servico_recorte_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ServicoRecorteOcorrenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_servico_recorte_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
