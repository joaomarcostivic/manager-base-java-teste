package com.tivic.manager.ptc;

import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.grl.FormularioAtributoValorDAO;
import com.tivic.manager.grl.ModeloDocumento;
import com.tivic.manager.grl.ModeloDocumentoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;
import com.tivic.manager.grl.SetorServices;
import com.tivic.manager.grl.TipoOcorrencia;
import com.tivic.manager.grl.TipoOcorrenciaDAO;
import com.tivic.manager.log.ExecucaoAcao;
import com.tivic.manager.log.ExecucaoAcaoServices;
import com.tivic.manager.mob.AtendimentoDocumento;
import com.tivic.manager.mob.AtendimentoDocumentoDAO;
import com.tivic.manager.mob.AtendimentoDocumentoServices;
import com.tivic.manager.mob.Cartao;
import com.tivic.manager.mob.CartaoDAO;
import com.tivic.manager.prc.Processo;
import com.tivic.manager.prc.ProcessoDAO;
import com.tivic.manager.prc.TipoPrazoServices;
import com.tivic.manager.print.Converter;
import com.tivic.manager.seg.AcaoServices;
import com.tivic.manager.seg.Modulo;
import com.tivic.manager.seg.ModuloServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.EelUtils;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class DocumentoServices {
	/**
	 * @category SICOE
	 */
	public static final int ST_PARADO        		= 0;
	public static final int ST_TRAMITANDO    		= 1;
	public static final int ST_ARQUIVADO     		= 2;
	public static final int ST_SETOR_EXTERNO 		= 3;
	public static final int ST_ARQUIVO_CANCELADO 	= 4;

	public static final String[] tipos = {"Público", "Confidencial"};

	public static final int TP_PUBLICO      = 0;
	public static final int TP_CONFIDENCIAL = 1;
	
	public static final int TP_PRIORIDADE_NORMAL = 0;
	public static final int TP_PRIORIDADE_ALTA = 1;
	public static final int TP_PRIORIDADE_URGENTE = 2;
	
	
	/**
	 * @category EeL
	 */
	public static final int TP_PROTOCOLO_EXTERNO_DOCUMENTO = 0;
	public static final int TP_PROTOCOLO_EXTERNO_PROCESSO  = 1;
	
	/**
	 * Salva Documento e a lista de solicitantes
	 * 	
	 * @param documento objeto documento que serão salvo
	 * @param solicitantes lista de solicitantes do documento
	 * 
	 * @return Result resultado do processo. Caso nao ocorra erro, sera retornado tambem
	 * o Documento, SetorAtual e a Situacao
	 */
	
	public static Result save(Documento documento, Connection connect) {
		return save(documento, null, null, connect);
	}
	
	public static Result save(Documento documento, ArrayList<DocumentoPessoa> solicitantes){
		return save(documento, solicitantes, null, null);
	}
	
	public static Result save(Documento documento, ArrayList<DocumentoPessoa> solicitantes, ArrayList<DocumentoOcorrencia> ocorrencias){
		return save(documento, solicitantes, ocorrencias, null);
	}
	
	public static Result save(Documento documento, ArrayList<DocumentoPessoa> solicitantes, Connection connect){
		return save(documento, solicitantes, null, connect);
	}
	
	public static Result save(Documento documento, ArrayList<DocumentoPessoa> solicitantes, ArrayList<DocumentoOcorrencia> ocorrencias, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(documento==null)
				return new Result(-1, "Erro ao salvar. Documento é nulo");
			
			// Insere dsAssunto caso ele não exista
			if(documento.getDsAssunto()==null || documento.getDsAssunto().equals("")) {
				String txtDoc = documento.getTxtDocumento();
				documento.setDsAssunto(txtDoc.substring(0, (txtDoc.length()>140 ? 139 : txtDoc.length())));
			}
			
			if(documento.getDsAssunto().length()>140) {
				documento.setDsAssunto(documento.getDsAssunto().substring(0, 139));
			}
			
			// Insere setor de recepcao, caso não possua
			if(documento.getCdSetor()<=0) {
				ResultSetMap rsmSetorRecepcao = SetorServices.getSetorRecepcao(documento.getCdEmpresa(), connect);
				if(rsmSetorRecepcao.next()) {
					documento.setCdSetor(rsmSetorRecepcao.getInt("cd_setor"));
				}
			}
			
			Result r;
			if(documento.getCdDocumento()==0) {
				documento.setNrAnoExterno(documento.getDtProtocolo().get(Calendar.YEAR)+"");
				r = insert(documento, solicitantes, connect, null);

			}
			else
				r = update(documento, solicitantes, connect, null);
			
			if(ocorrencias!=null) {
				Result resultAux = DocumentoOcorrenciaServices.removeAll(documento.getCdDocumento(), connect);
				if(resultAux.getCode()<0) {
					if(isConnectionNull)
						connect.rollback();
					return resultAux;
				}
				
				for (DocumentoOcorrencia ocorrencia : ocorrencias) {
					if(ocorrencia.getCdDocumento()<=0)
						ocorrencia.setCdDocumento(documento.getCdDocumento());
					
					resultAux = DocumentoOcorrenciaServices.save(ocorrencia, connect);
					if(resultAux.getCode()<0) {
						if(isConnectionNull)
							connect.rollback();
						return resultAux;
					}
				}
			}
			
			if(r.getCode()<=0) {
				Conexao.rollback(connect);
				return r;
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(r.getCode(), "Salvo com sucesso.");
			result.addObject("DOCUMENTO", r.getObjects().get("documento"));
			result.addObject("SETOR", r.getObjects().get("setor"));
			result.addObject("SITUACAO", r.getObjects().get("situacao"));
			if(ocorrencias!=null)
				result.addObject("OCORRENCIAS", ocorrencias);

			
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
		
	public static Result remove(int cdDocumento){
		return remove(cdDocumento, false, null);
	}
	
	public static Result remove(int cdDocumento, boolean cascade){
		return remove(cdDocumento, cascade, null);
	}
	
	public static Result remove(int cdDocumento, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			Documento documento = DocumentoDAO.get(cdDocumento, connect);
			
			/*
			 * Deleta registros vinculados ao documento
			 */
			if(cascade){
				connect.prepareStatement("UPDATE ptc_documento SET cd_documento_superior=null WHERE cd_documento_superior = "+cdDocumento).execute();
				connect.prepareStatement("DELETE FROM agd_agenda_item WHERE cd_documento = "+cdDocumento).execute();
				connect.prepareStatement("DELETE FROM grl_formulario_atributo_valor WHERE cd_documento = "+cdDocumento).execute();
				connect.prepareStatement("DELETE FROM ptc_anexo WHERE cd_documento = "+cdDocumento).execute();
				connect.prepareStatement("DELETE FROM ptc_documentacao WHERE cd_documento = "+cdDocumento).execute();
				connect.prepareStatement("DELETE FROM ptc_documento_arquivo WHERE cd_documento = "+cdDocumento).execute();
				connect.prepareStatement("DELETE FROM ptc_documento_ocorrencia WHERE cd_documento = "+cdDocumento).execute();
				connect.prepareStatement("DELETE FROM ptc_documento_pendencia WHERE cd_documento = "+cdDocumento).execute();
				connect.prepareStatement("DELETE FROM ptc_documento_pessoa WHERE cd_documento = "+cdDocumento).execute();
				connect.prepareStatement("DELETE FROM ptc_documento_tramitacao WHERE cd_documento = "+cdDocumento).execute();
				
				//TODO:
				
				retorno = 1;
			}
				
			if(!cascade || retorno>0)
				retorno = DocumentoDAO.delete(cdDocumento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este documento está vinculado a outros registros e não pode ser excluído!");
			}
			
			/*
			 * Exclusão da E&L
			 */
			int lgProtocoloEel = ParametroServices.getValorOfParametroAsInteger("LG_PROTOCOLO_EEL", 0, documento.getCdEmpresa(), connect);
			if(lgProtocoloEel==1 && documento.getNrDocumentoExterno()!=null) {
				System.out.println(documento.getNrDocumentoExterno());
				String controle = null;
				if(documento.getNrDocumentoExterno().length()>12)
					controle = documento.getNrDocumentoExterno().substring(0, documento.getNrDocumentoExterno().length()-5);
				else
					controle = documento.getNrDocumentoExterno();
				System.out.println(controle);
				
				Result r = EelUtils.deleteProtocolo(controle);
				if(r.getCode()<=0) {
					connect.rollback();
					return new Result(-3, r.getMessage());
				}
				retorno = r.getCode();
			}
			
			if (retorno>0 && isConnectionNull)
				connect.commit();	
			
			return new Result(1, "Documento excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir documento!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorna o número de CIs em tramitação (dt_recebimento==NULL)
	 * 	 
	 * @param criterios Lista de cirtérios de busca
	 * @param connection Objeto de conexão com o banco
	 * @return Quantidade de CIs
	 */
	public static int getQtCI(ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int cdTipoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_CI", 0, 0, connection);
			ResultSet rs = connection.prepareStatement(
										   "SELECT COUNT(*) AS qt_registros " +
										   "FROM ptc_documento A " +
										   "LEFT OUTER JOIN ptc_documento_tramitacao B ON (A.cd_documento = B.cd_documento AND B.cd_tramitacao = 1)" +
										   "LEFT OUTER JOIN seg_usuario C ON (B.cd_usuario_destino = C.cd_usuario) " +
										   "LEFT OUTER JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa) " +
										   "LEFT OUTER JOIN seg_usuario E ON (A.cd_usuario = E.cd_usuario) " +
										   "LEFT OUTER JOIN grl_pessoa F ON (E.cd_pessoa = F.cd_pessoa) " +
										   "WHERE B.dt_recebimento IS NULL " +
										   "  AND A.cd_tipo_documento = "+cdTipoDocumento).executeQuery();
			return rs.next() ? rs.getInt("qt_registros") : 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static byte[] getBytesImage(int cdDocumento) {
		return getBytesImage(cdDocumento, null);
	}

	public static byte[] getBytesImage(int cdDocumento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			Documento documento = DocumentoDAO.get(cdDocumento, connection);
			Arquivo arquivo = documento==null || documento.getCdArquivo()<=0 ? null : ArquivoDAO.get(documento.getCdArquivo(), connection);
			return arquivo==null ? null : arquivo.getBlbArquivo();
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

	/**
	 * Busca todas as tramitações de um Documento
	 * 
	 * @param cdDocumento código do Documento
	 * @return ResultSetMap Lista de tramitações do Documento
	 */
	public static ResultSetMap getAllTramites(int cdDocumento) {
		return getAllTramites(cdDocumento, false, null);
	}
	
	public static ResultSetMap getAllTramites(int cdDocumento, Connection connect) {
		return getAllTramites(cdDocumento, false, connect);
	}
	
	public static ResultSetMap getAllTramites(int cdDocumento, Boolean lgProtocoloExterno) {
		return getAllTramites(cdDocumento, lgProtocoloExterno, null);
	}

	public static ResultSetMap getAllTramites(int cdDocumento, Boolean lgProtocoloExterno, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			
			Documento doc = DocumentoDAO.get(cdDocumento, connection);
			if(lgProtocoloExterno && doc.getNrDocumentoExterno()!=null) {
				Result result = sincronizarTramitacoes(cdDocumento, doc.getNrDocumentoExterno(), null, null, connection);
				
				if(result.getCode()<0)
					System.out.println("Erro ao sincronizar tramitações. "+result.getMessage());
				
			}
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT A.*, B.nm_login AS nm_usuario_origem, C.nm_pessoa AS nm_pessoa_origem, " +
					                                            "       D.nm_situacao_documento, G.nm_login AS nm_usuario_destino," +
																"       E.nm_setor AS nm_setor_destino, E.sg_setor AS sg_setor_destino, E.nr_setor_externo AS nr_externo_destino, " + 
					                                            "       F.nm_setor AS nm_setor_origem, F.sg_setor AS sg_setor_origem, F.nr_setor_externo AS nr_externo_origem," +
																"		H.cd_pessoa as cd_empresa_origem, H.nm_pessoa as nm_empresa_origem, "+
					                                            "		I.cd_pessoa as cd_empresa_destino, I.nm_pessoa as nm_empresa_destino, "+
																"		J.nm_pessoa as nm_responsavel_setor "+
																"FROM ptc_documento_tramitacao 			A " +
																"LEFT OUTER JOIN seg_usuario            B ON (A.cd_usuario_origem = B.cd_usuario) " +
																"LEFT OUTER JOIN grl_pessoa             C ON (B.cd_pessoa = C.cd_pessoa) " +
																"LEFT OUTER JOIN ptc_situacao_documento D ON (A.cd_situacao_documento = D.cd_situacao_documento) " +
																"LEFT OUTER JOIN grl_setor 				E ON (A.cd_setor_destino = E.cd_setor) " +
																"LEFT OUTER JOIN grl_setor 				F ON (A.cd_setor_origem = F.cd_setor) " +
																"LEFT OUTER JOIN seg_usuario            G ON (A.cd_usuario_destino = G.cd_usuario) " +
																"LEFT OUTER JOIN grl_pessoa 			H ON (F.cd_empresa = H.cd_pessoa) "+
																"LEFT OUTER JOIN grl_pessoa 			I ON (E.cd_empresa = I.cd_pessoa) "+
																"LEFT OUTER JOIN grl_pessoa 			J ON (F.cd_responsavel = J.cd_pessoa) "+
																"WHERE A.cd_documento = " +cdDocumento+
																//"  AND NOT cd_usuario_origem IS NULL " +
																"ORDER BY CD_TRAMITACAO DESC, DT_TRAMITACAO DESC").executeQuery());
			
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

	/**
	 * Realiza o envio (tramitacao) de um documento para um setor externo. Tal situação é estabelecida
	 * atraves de um parametro que define qual a situação do Documento quando em envio externo.
	 * 
	 * @param cdDocumento Código do documento
	 * @param cdUsuarioOrigem Código do usuário que remetente
	 * @param nmLocalDestino Nome do setor de destino
	 * @param txtTramitacao Texto da tramitação
	 * 
	 * @return Resulto Resultado da operação
	 */
	public static Result envioExterno(int cdDocumento, int cdUsuarioOrigem, String nmLocalDestino, String txtTramitacao) {
		return envioExterno(cdDocumento, cdUsuarioOrigem, nmLocalDestino, txtTramitacao, null);
	}

	public static Result envioExterno(int cdDocumento, int cdUsuarioOrigem, String nmLocalDestino, String txtTramitacao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			/* Localiza a situacao de documento referente a envio externo (cliente ou fornecedor) */
			int cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ENVIO_EXTERNO", 0, 0, connection);
			if (cdSituacaoDocumento <= 0)
				return new Result(-10, "Situação de documento em ambiente externo não definida!");

			Documento documento = DocumentoDAO.get(cdDocumento, connection);
			int cdSetorOrigem = documento.getCdSetor();
			//
			DocumentoTramitacao tramitacao = new DocumentoTramitacao(0 /*cdTramitacao*/, cdDocumento, 0 /*cdSetorDestino*/, 0 /*cdUsuarioDestino*/,
																	 cdSetorOrigem, cdUsuarioOrigem, new GregorianCalendar() /*dtTramitacao*/,
																	 null /*dtRecebimento*/, nmLocalDestino, txtTramitacao,
																	 documento.getCdSituacaoDocumento(), documento.getCdFase(), null/*nmLocalOrigem*/, null/*nrRemessa*/);

			Result result = DocumentoTramitacaoServices.insert(tramitacao, connection);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar realizar envio Externo!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	/**
	 * Envia uma lista de documentos para um único setor.
	 * 
	 * @param documentos ArrayList<String> códigos dos documentos 
	 * @param cdUsuarioOrigem código do usuário remetente
	 * @param cdSetorOrigem código do setor de origerm
	 * @param cdSetorDestino código do setor de destino
	 * @param txtTramitacao texto da tramitação
	 * 
	 * @return Result resultado da operação e, em caso de sucesso, um ResultSetMap (rsm) com todos os documentos
	 */
	@SuppressWarnings("unchecked")
	public static sol.util.Result enviarDocumentos(ArrayList<String> documentos, int cdUsuarioOrigem, int cdSetorOrigem, int cdSetorDestino, String txtTramitacao)	{
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			Result result = new Result(0);
			ResultSetMap rsm  = new ResultSetMap();
			String nrRemessa = null;
			String nmUsuarioOrigem = null;
			
			for(int i=0; i<documentos.size(); i++)	{
				
				//VERIFICA SE O DOCUMENTO JA FOI ENVIADO
				Documento doc = DocumentoDAO.get(Integer.valueOf(documentos.get(i)), connect);
				int cdSituacaoDocumentoTramitando = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_TRAMITANDO", 0, 0, connect);
				LogUtils.debug("\n\ndoc.getCdSituacaoDocumento(): "+doc.getCdSituacaoDocumento());
				LogUtils.debug("cdSituacaoDocumentoTramitando: "+cdSituacaoDocumentoTramitando);
				if(doc.getCdSituacaoDocumento()==cdSituacaoDocumentoTramitando)
					continue;
											
				Result rTemp = enviarDocumento(Integer.valueOf(documentos.get(i)), cdUsuarioOrigem, cdSetorOrigem,cdSetorDestino, txtTramitacao, null, 0, null,nrRemessa,connect);
				if(rTemp.getCode() <= 0)	{
					Conexao.rollback(connect);
					return rTemp;
				}
				
				HashMap<String,Object> register = (HashMap<String,Object>)rTemp.getObjects().get("register");
				if(nrRemessa==null) {
					nrRemessa = (String)register.get("NR_REMESSA");
				}
				if(nmUsuarioOrigem==null) {
					nmUsuarioOrigem = (String)register.get("NM_USUARIO_ORIGEM");
				}
				
				rsm.addRegister(register);
				result.setCode(result.getCode() + 1);
			}
			if(!connect.getAutoCommit())
				connect.commit();
			
			Pessoa responsavel = PessoaDAO.get(SetorDAO.get(cdSetorOrigem, connect).getCdResponsavel(), connect);
			
			result.setMessage("Documento(s) enviado(s) com sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("nrRemessa", nrRemessa);
			result.addObject("nmResponsavelSetor", (responsavel!=null ? responsavel.getNmPessoa() : ""));
			result.addObject("nmUsuarioOrigem", nmUsuarioOrigem);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar enviar documentos!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Envia um Documento.
	 * 
	 * @param cdDocumento Código do Documento
	 * @param cdUsuarioOrigem Código do usuário remetente
	 * @param cdSetorOrigem Código do setor de origem
	 * @param cdSetorDestino Código do setor de destino
	 * @param txtTramitacao Texto da tramitação
	 * @param prazo AgendaItem que pode ser vinculada a um documento no momento da tramitação
	 * @param tpPrioridade Prioridade para o segmento do fluxo do Documento
	 * @param documentoArquivo Arquivo que pode ser vinculado ao Documento no momento da tramitação
	 * 
	 * @return Result resultado da operações e, em caso de sucesso, retorna também alguns dados do Documento que foram atualizados
	 * durante o processo de envio
	 */
	public static Result enviarDocumento(int cdDocumento, int cdUsuarioOrigem, int cdSetorOrigem, int cdSetorDestino, String txtTramitacao, AgendaItem prazo, 
			int tpPrioridade, DocumentoArquivo documentoArquivo) {
		return enviarDocumento(cdDocumento, cdUsuarioOrigem, cdSetorOrigem, cdSetorDestino, txtTramitacao, prazo, tpPrioridade, documentoArquivo, null, null);
	}
	
	public static Result enviarDocumento(int cdDocumento, int cdUsuarioOrigem, int cdSetorOrigem, int cdSetorDestino, String txtTramitacao, AgendaItem prazo, 
			int tpPrioridade, DocumentoArquivo documentoArquivo, String nrRemessa) {
		return enviarDocumento(cdDocumento, cdUsuarioOrigem, cdSetorOrigem, cdSetorDestino, txtTramitacao, prazo, tpPrioridade, documentoArquivo, nrRemessa, null);
	}
	
	public static Result enviarDocumento(int cdDocumento, int cdUsuarioOrigem, int cdSetorOrigem, int cdSetorDestino, String txtTramitacao, AgendaItem prazo, 
			int tpPrioridade, DocumentoArquivo documentoArquivo, String nrRemessa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			//
			Documento documento = DocumentoDAO.get(cdDocumento, connection);
			
			// Dados para mensagem de ERRO e ALERTA
			Setor setor = SetorDAO.get(documento.getCdSetorAtual(), connection);
			String nmSetorOrigem  = setor!=null ? setor.getNmSetor() : "DESCONHECIDO";
			setor = SetorDAO.get(cdSetorDestino, connection);
			String nmSetorDestino = setor!=null ? setor.getNmSetor() : "DESCONHECIDO";
			com.tivic.manager.seg.Usuario usuario = com.tivic.manager.seg.UsuarioDAO.get(cdUsuarioOrigem, connection);

			String dadosDocumento = "[Nº Documento: "+documento.getNrDocumento()+", Código: "+documento.getCdDocumento()+
			                        "Setor Atual: "+nmSetorOrigem+", Setor de Destino: "+nmSetorDestino+"]";
			/* localiza a situacao de documento referente a "tramitando" */
			int cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_TRAMITANDO", 0, 0, connection);
			if (cdSituacaoDocumento <= 0)
				return new Result(-9, "Situação padrão para documentos arquivados não definida! "+dadosDocumento);

			// Verifica se o documento estão no setor do usuário que estão enviando
			if(cdSetorOrigem!=documento.getCdSetorAtual())
				return new Result(-2, "O documento não está no setor do usuário! "+dadosDocumento);
			//
			
			/* Verifica se o destino e origem são o mesmo setor */
			if(cdSetorDestino==documento.getCdSetorAtual())
				return new Result(-3, "Não é posssível enviar o documento para o mesmo setor! "+dadosDocumento);
			
			/* Verifica se o documento esta na empresa */
			int lgShowAtendimento = ParametroServices.getValorOfParametroAsInteger("LG_MODULO_ATENDIMENTO", 0, 0, connection);
			if(lgShowAtendimento > 0) {
				
				ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * FROM ptc_documento_ocorrencia"
																				+ " WHERE cd_documento = " + cdDocumento).executeQuery());
				
				boolean lgDevolucao = true;
				while (rsm.next() && lgDevolucao) {
					if (rsm.getInt("CD_TIPO_OCORRENCIA") == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_CARGA", 0, documento.getCdEmpresa(), connection)) {
						lgDevolucao = false;
						
						rsm.beforeFirst();
						while (rsm.next()) {
							if (rsm.getInt("CD_TIPO_OCORRENCIA") == ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_DEV_CARGA", 0, documento.getCdEmpresa(), connection)) {
								lgDevolucao = true;
								break;
							}
						}
					}
				}
				
				if (lgDevolucao == false)
					return new Result(-12, "Documento fora da empresa! "+dadosDocumento);
			}
			
			// Altera a situação do Documento se este for enviado para um setor externo			
//			int cdSituacaoRecebidoExterno = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ENVIO_EXTERNO", 0, 0, connection);
//			boolean lgSetorExterno = setor.getTpSetor()>0;//setor.getLgSetorExterno()==1;
//			if(lgSetorExterno) {
//				documento.setCdSituacaoDocumento(cdSituacaoRecebidoExterno);
//				cdSituacaoDocumento = cdSituacaoRecebidoExterno;
//			}
			

			// Cria a tramitação do Documento
			DocumentoTramitacao tramitacao = new DocumentoTramitacao(0 /*cdTramitacao*/, cdDocumento, cdSetorDestino, 0 /*cdUsuarioDestino*/,
																	 documento.getCdSetorAtual(), cdUsuarioOrigem, new GregorianCalendar() /*dtTramitacao*/,
																	 null /*dtRecebimento*/, null /*nmLocalDestino*/, 
																	 txtTramitacao, documento.getCdSituacaoDocumento(), documento.getCdFase(), null/*nmLocalOrigem*/, nrRemessa);

			Result result = DocumentoTramitacaoServices.insert(tramitacao, connection);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-4, result.getMessage());
			}
			tramitacao = (DocumentoTramitacao)result.getObjects().get("tramitacao");
			
			// Altera a situação do Documento
			documento.setCdSituacaoDocumento(cdSituacaoDocumento);
			SituacaoDocumento situacao = SituacaoDocumentoDAO.get(cdSituacaoDocumento, connection);
			String nmSituacaoDocumento = situacao==null ? "INEXISTENTE" : situacao.getNmSituacaoDocumento();
			
			// Em caso de envio externo, atualiza o setor atual para representar o recebimento
//			if(lgSetorExterno)
//				documento.setCdSetorAtual(cdSetorDestino);
			
			// Atualiza a prioridade do Documento
			documento.setTpPrioridade(tpPrioridade);
							
			int retorno = DocumentoDAO.update(documento, connection);
			
			if (retorno <=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-5, "Não foi possível atualizar o documento.");
			}
			
			// Salva uma AgendaItem vinculada ao Documento, caso exista
			if(prazo!=null) {
				prazo.setCdDocumento(cdDocumento);
				TipoDocumento tpDocumento = com.tivic.manager.ptc.TipoDocumentoDAO.get(documento.getCdTipoDocumento(), connection);
				prazo.setDsDetalhe((tpDocumento==null?"":tpDocumento.getNmTipoDocumento())+" "+
								   (documento.getNrDocumento()==null?"":documento.getNrDocumento())+"\n"+
								   (documento.getTxtDocumento()==null?"":documento.getTxtDocumento()));
				retorno = AgendaItemDAO.insert(prazo, connection);
				
				if (retorno <=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-6, "Não foi possível inserir prazo ao enviar o documento.");
				}
			}
			
			// Vincula um arquivo ao Documento, caso exista
			if(documentoArquivo!=null && retorno>0) {
				documentoArquivo.setDtArquivamento(new GregorianCalendar());
				
				retorno = DocumentoArquivoDAO.insert(documentoArquivo, connection);
				
				if (retorno <=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-7, "Não foi possível inserir o arquivo ao enviar o documento.");
				}
			}
			
			/**
			 * Tramitação no banco da E&L
			 */
			int lgProtocoloEel = ParametroServices.getValorOfParametroAsInteger("LG_PROTOCOLO_EEL", 0, documento.getCdEmpresa(), connection);
			Result r = null;
			if(retorno>0) {
				if(lgProtocoloEel>0) {
					r = enviarDocumentoEel(cdDocumento, cdUsuarioOrigem, cdSetorOrigem, cdSetorDestino, txtTramitacao, nrRemessa, connection);
					
					if(r.getCode()<=0) {
						return new Result(-8, r.getMessage());
					}
					
					if(nrRemessa==null) 
						nrRemessa = (String)r.getObjects().get("NR_REMESSA");
					
					LogUtils.debug("enviarDocumento.nrRemessa: "+nrRemessa);
					
					tramitacao.setNrRemessa(nrRemessa);
					if (DocumentoTramitacaoServices.save(tramitacao, connection).getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-9, "Não foi possível inserir nr_remessa na tramitação do documento.");
					}
				}
			}
			
			// OCORRÊNCIA DE ENVIO
			int cdOcorrenciaEnvioDocumento = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_ENVIO_DOCUMENTO", 0);
			if(cdOcorrenciaEnvioDocumento>0) {
				DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia(cdDocumento, cdOcorrenciaEnvioDocumento, 
						0, cdUsuarioOrigem, new GregorianCalendar(), txtTramitacao, DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO, 0);
				
				result = DocumentoOcorrenciaServices.save(ocorrencia, connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-10, "Não foi possível ocorrência de envio do documento.");
				}
			}
			
			//DOCUMENTOS VINCULADOS
			ResultSetMap rsmVinculados = null;
			if(lgProtocoloEel>0) {
				rsmVinculados = getDocumentosVinculados(cdDocumento, connection);
				while(rsmVinculados.next()) {
					
					if(rsmVinculados.getInt("CD_SITUACAO_DOCUMENTO")==cdSituacaoDocumento)
						continue;
					
					result = enviarDocumento(rsmVinculados.getInt("cd_documento"), cdUsuarioOrigem, cdSetorOrigem, cdSetorDestino, 
							txtTramitacao, prazo, tpPrioridade, documentoArquivo, nrRemessa, connection);
					
					if (result.getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-11, "Não foi possível enviar um documento vinculado.");
					}
				}
			}

			if (isConnectionNull)
				connection.commit();
			//
			//Setor setorAtual = SetorDAO.get(cdSetorDestino);
			//String nmSetorAtual = setorAtual==null ? "" : setorAtual.getNmSetor();
			
			Pessoa pessoaOrigem = PessoaDAO.get(UsuarioDAO.get(cdUsuarioOrigem, connection).getCdPessoa(), connection);
			
			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("NR_DOCUMENTO", documento.getNrDocumento());
			register.put("NM_TIPO_DOCUMENTO", TipoDocumentoDAO.get(documento.getCdTipoDocumento(), connection).getNmTipoDocumento());
			register.put("NM_USUARIO", usuario.getNmLogin());
			register.put("NM_SETOR_ORIGEM", nmSetorOrigem);
			register.put("NM_SETOR_DESTINO", nmSetorDestino);
			register.put("NM_SOLICITANTES", (String)getSolicitantes(cdDocumento, connection).getObjects().get("nmSolicitantes"));
			register.put("DT_PROTOCOLO", Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm"));
			register.put("TXT_TRAMITACAO", txtTramitacao);
			register.put("CD_SITUACAO_DOCUMENTO", cdSituacaoDocumento);
			register.put("NM_SITUACAO_DOCUMENTO", nmSituacaoDocumento);
			register.put("TP_PRIORIDADE", documento.getTpPrioridade());
			register.put("NR_REMESSA", nrRemessa);
			register.put("NM_USUARIO_ORIGEM", pessoaOrigem.getNmPessoa());
			register.put("RSM_DOCUMENTOS_VINCULADOS", rsmVinculados);
//			if(lgSetorExterno)
//				register.put("NM_SETOR_ATUAL", nmSetorAtual);
			//
			result = new Result(1, "Documento enviado com sucesso!");
			result.addObject("register", register);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar realizar enviar documento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result enviarDocumentoEel(int cdDocumento, int cdUsuarioOrigem, int cdSetorOrigem, int cdSetorDestino, String txtTramitacao, String nrRemessa, Connection connect) {
		Connection connExt = EelUtils.conectarEelSqlServer();
		
		if(connect==null) {
			return new Result(-1, "Não existe conexão com o banco de dados.");
		}

		if(connExt==null) {
			return new Result(-2, "Erro ao conectar com o banco de dados da E&L");
		}
		
		try {
			connExt.setAutoCommit(false);
			
			CallableStatement cstmt = null;
			
			Documento documento = DocumentoDAO.get(cdDocumento, connect);
			
			LogUtils.debug("DocumentoServices.enviarDocumentoEel");
			LogUtils.debug("\tdocumento: "+documento);
			
			String nrLocalOrigem = SetorDAO.get(cdSetorOrigem, connect).getNrSetorExterno();
			if(nrLocalOrigem==null) {
				return new Result(-3, "Erro ao encontrar setor no banco de dados E&L.");
			}
			
			int cdPessoaEnvio = UsuarioDAO.get(cdUsuarioOrigem, connect).getCdPessoa();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM grl_pessoa_externa WHERE cd_pessoa = "+cdPessoaEnvio);
			ResultSet rs = pstmt.executeQuery();
			String nrPessoa = null;
			if(rs.next()) { //já existe a paridade
				nrPessoa = rs.getString("cd_pessoa_externa");//"0000368";
			}
			else {
				Pessoa p = PessoaDAO.get(cdPessoaEnvio, connect);
				PessoaFisica pf = PessoaFisicaDAO.get(cdPessoaEnvio, connect);
				pstmt = connExt.prepareStatement("USE [CONT_VIT_CONQUISTA_PREFEITURA];"
						+ " SELECT codigo_g AS cd_pessoa_externa FROM gg_geral"
						+ " WHERE nome_g LIKE '%"+p.getNmPessoa()+"%'"
								+ " OR cpf_g = '"+(pf!=null ? pf.getNrCpf() : "")+"'");
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					nrPessoa = rs.getString("cd_pessoa_externa");
					
					if(nrPessoa!=null) {
						pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_externa(cd_pessoa, cd_pessoa_externa)"
								+ " VALUES (?,?)");
						pstmt.setInt(1, cdPessoaEnvio);
						pstmt.setString(2, nrPessoa);
						
						pstmt.executeUpdate();
					}
				}
			}
			
			String nrLocalDestino = SetorDAO.get(cdSetorDestino, connect).getNrSetorExterno();
			if(nrLocalDestino==null) {
				return new Result(-3, "Erro ao encontrar setor no banco de dados E&L.");
			}
			
			/*
			 * Salvar o documento na base E&L caso ele não exista
			 */
			String nrDocumentoExterno = documento.getNrDocumentoExterno();
			if(nrDocumentoExterno==null) { 
				LogUtils.debug("Não encontrou o documento localmente ao enviar.");
				//return new Result(-3, "Erro ao encontrar documento no banco de dados E&L.");
				ResultSetMap rsmSolicitantes = DocumentoPessoaServices.getAllByDocumento(documento.getCdDocumento());
				rsmSolicitantes.first();
				DocumentoPessoa solicitante = DocumentoPessoaDAO.get(rsmSolicitantes.getInt("cd_documento"), rsmSolicitantes.getInt("cd_tramitacao"), connect);
				Result result = EelUtils.save(documento, solicitante, false);//saveEel(documento, solicitante, connect);
				if(result.getCode()<=0) {
					return result;
				}
				
				//atualizar informações externas do documento
				nrDocumentoExterno = (String)result.getObjects().get("NR_DOCUMENTO_EXTERNO");
				String nrProtocoloExterno = (String)result.getObjects().get("NR_PROTOCOLO_EXTERNO");
				documento.setNrDocumentoExterno(nrDocumentoExterno);
				documento.setNrProtocoloExterno(nrProtocoloExterno);
				result = DocumentoServices.update(documento, null, connect, null);
				if(result.getCode()<=0) {
					return result;
				}
			}
			
			String nrFaseExterna = FaseDAO.get(documento.getCdFase(), connect).getNrFaseExterna();
			if(nrFaseExterna==null) {
				return new Result(-3, "Erro ao encontrar fase no banco de dados E&L.");
			}
			
			/*
			 * CONCLUIR ENVIO ANTERIOR
			 */
			ResultSet rsUltimoTramite = EelUtils.getUltimaTramitacao(nrDocumentoExterno, connExt);
			if(rsUltimoTramite.next()) {
				cstmt = connExt.prepareCall("{call dbo.func_pr_protocolo_enviado"
						+ "							@strEmpresa=?,"
						+ "							@strFilial=?,"
						+ "							@strLocal=?,"
						+ "							@strRemessa=?,"
						+ "							@strControle=?;}");
				cstmt.setString(1,"001");
				cstmt.setString(2, "001");
				cstmt.setString(3, rsUltimoTramite.getString("codigo_local_origem"));
				cstmt.setString(4, rsUltimoTramite.getString("codigo_rem"));
				cstmt.setString(5, nrDocumentoExterno);
				
				cstmt.execute();
				connExt.commit();
			}
			
			/*
			 * REMESSA
			 */
			if(nrRemessa==null) {
				LogUtils.debug("NOVA REMESSA!!!");
				
//				pstmt = connExt.prepareStatement("USE [CONT_VIT_CONQUISTA_PREFEITURA];"
//						+ " SELECT MAX(CAST(codigo_rem AS INTEGER))+1 AS cd_remessa FROM pr_andamento"
//						+ " WHERE codigo_emp='001'"
//						+ " AND codigo_fil='001'"
//						+ " AND codigo_local_origem='"+nrLocalOrigem+"'");
				
				// :::::: Pegar Remessa
				pstmt = connExt.prepareStatement("select registro_atual + 1 as nova_remessa"
						+ "	from gg_sequencial"
						+ "	where nome_tabela = 'pr_andamento'"
						+ " and campo_1 = '001'"
						+ " and campo_2 = '001'"
						+ " and campo_3 = '"+nrLocalOrigem+"'");
				
				rs = pstmt.executeQuery();
				int cdRemessa=0;
				if(rs.next()) {
					cdRemessa = rs.getInt("nova_remessa");
				}
				rs.close();
				
				nrRemessa = new String(String.format ("%09d", cdRemessa));
				
				// :::::: Atualizar valor da remessa
				pstmt = connExt.prepareStatement("UPDATE gg_sequencial SET registro_atual = registro_atual + 1"
						+ " WHERE campo_1 = '001'"
						+ " AND campo_2 = '001'"
						+ " AND campo_3 = '"+nrLocalOrigem+"'");
				
				pstmt.executeUpdate();
			}	
			
			cstmt = connExt.prepareCall("{call dbo.func_pr_envia_protocolo"
					+ " @strEmpresa=?,"
					+ "	@strFilial=?,"
					+ "	@strLocalOrigem=?,"
					+ "	@strPessoa=?,"
					+ "	@strLocalDestino=?,"
					+ " @strRemessa=?,"
					+ "	@strControle=?,"
					+ "	@strSituacao=?,"
					+ "	@strTextoDespacho=?,"
					+ "	@intNumeroFolhas=?,"
					+ "	@dtDataRemessa=?;}");
			
			cstmt.setString(1,"001");
			cstmt.setString(2, "001");
			cstmt.setString(3, nrLocalOrigem);
			cstmt.setString(4, nrPessoa);
			cstmt.setString(5, nrLocalDestino);
			cstmt.setString(6, nrRemessa);
			cstmt.setString(7, nrDocumentoExterno);
			cstmt.setString(8, (nrFaseExterna!=null || !nrFaseExterna.equals("") ? nrFaseExterna : "008"));
			cstmt.setString(9, txtTramitacao);
			cstmt.setInt(10, 1);
			cstmt.setDate(11, new Date(new GregorianCalendar().getTimeInMillis()));
			
			cstmt.execute();
			connExt.commit();
			
			Result r = new Result(1, "Documento enviado com sucesso no banco da E&L!");
			r.addObject("NR_REMESSA", nrRemessa);
			
			return r;
			
		}
		catch(Exception e){
			e.printStackTrace();
			Conexao.rollback(connExt);
			return new Result(-1, "Erro ao enviar documento!");
		}
		finally{
			EelUtils.desconectarEelSqlServer(connExt);
		}
	}
	
	public static sol.util.Result removeTramitacoes(ArrayList<String> documentos, int cdSetorOrigem, int cdUsuarioOrigem)	{
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			Result result = new Result(0);
			
			for(int i=0; i<documentos.size(); i++)	{
				
				DocumentoTramitacao docT = DocumentoTramitacaoServices.getUltimaTramitacao(Integer.valueOf(documentos.get(i)), connect);
				
				if(docT==null) {
					Conexao.rollback(connect);
					return new Result(-2, "Erro ao buscar o último envio do documento.");
				}
							
				Result rTemp = removeTramitacao(docT.getCdDocumento(), docT.getCdTramitacao(), cdUsuarioOrigem, cdSetorOrigem, connect);
				if(rTemp.getCode() <= 0)	{
					Conexao.rollback(connect);
					return rTemp;
				}
				//
				result.setCode(result.getCode() + 1);
			}
			connect.commit();
			result.setMessage("Tramitações desfeitas com sucesso!");
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.removeTramitacoes: " +  e);
			return new Result(-1, "Erro ao tentar desfazer envio!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que desfaz a última tramitação do Documento
	 *  
	 * @param cdDocumento código do Documento
	 * @param cdTramitacao código da Tramitação a ser desfeita
	 * @param cdUsuarioOrigem código do usuário de origem da tramitação
	 * @param cdSetorUsuarioOrigem código do setor do usuário remetente
	 * @return Result resultado da operação
	 */
	public static Result removeTramitacao(int cdDocumento, int cdTramitacao, int cdUsuarioOrigem, int cdSetorUsuarioOrigem) {
		return removeTramitacao(cdDocumento, cdTramitacao, cdUsuarioOrigem, cdSetorUsuarioOrigem, null);
	}
	
	public static Result removeTramitacao(int cdDocumento, int cdTramitacao, int cdUsuarioOrigem, int cdSetorUsuarioOrigem, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			Documento documento = DocumentoDAO.get(cdDocumento, connection);
			DocumentoTramitacao tramitacao = DocumentoTramitacaoDAO.get(cdTramitacao, cdDocumento, connection);
				
			if(tramitacao.getDtRecebimento() != null)
				return new Result(-1, "Erro ao tentar excluir tramitação! Documento já foi recebido!");
			
			int lgProtocoloEel = ParametroServices.getValorOfParametroAsInteger("LG_PROTOCOLO_EEL", 0, documento.getCdEmpresa(), connection);
			if(lgProtocoloEel==0) {
				if(tramitacao.getCdUsuarioOrigem() != cdUsuarioOrigem)
					return new Result(-1, "Erro ao tentar excluir tramitação! O usuário não tem permissão para excluir a tramitação!");
				
				if(tramitacao.getCdSetorOrigem() != cdSetorUsuarioOrigem)
					return new Result(-1, "Erro ao tentar excluir tramitação! Não é possível excluir tramitação de outro setor!");
			}
			
			documento.setCdSetorAtual(cdSetorUsuarioOrigem);
			int cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0, 0, connection);
			documento.setCdSituacaoDocumento(cdSituacaoDocumento);
			DocumentoDAO.update(documento, connection);
			
			int retorno = DocumentoTramitacaoDAO.delete(cdTramitacao, cdDocumento, connection);			
			
			//Remover tramitacao na E&L
			if(retorno>0 && lgProtocoloEel>0) {
				retorno = EelUtils.deleteUltimoAndamento(documento.getNrDocumentoExterno()).getCode();
			}

			if (retorno>0 && isConnectionNull)
				connection.commit();
			else if(retorno<=0 && isConnectionNull) {
				connection.rollback();
				return new Result(-2, "Erro ao excluir tramitação.");
			}
			
			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("NM_SETOR_ATUAL", SetorDAO.get(cdSetorUsuarioOrigem, connection).getNmSetor());
			register.put("NM_SITUACAO_DOCUMENTO", SituacaoDocumentoDAO.get(cdSituacaoDocumento, connection).getNmSituacaoDocumento());
			register.put("CD_SITUACAO_DOCUMENTO", cdSituacaoDocumento);
			register.put("TP_PRIORIDADE", documento.getTpPrioridade());
			
			Result result = new Result(1, "Tramitação excluída com sucesso!");
			result.addObject("register", register);
			return result;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar excluir tramitação!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result arquivarDocumento(int cdDocumento, int cdUsuario, String txtArquivamento) {
		return arquivarDocumento(cdDocumento, cdUsuario, txtArquivamento, null);
	}

	public static Result arquivarDocumento(int cdDocumento, int cdUsuario, String txtArquivamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			/* localiza a situacao de documento referente a "arquivado" */
			int cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ARQUIVADO", 0, 0, connection);
			
			/* em caso negativo, persiste um novo registro e parametriza-o como situação para "arquivado" */
			if (cdSituacaoDocumento <= 0)
				return new Result(-10, "Situação padrão para documentos arquivados não definida!");

			Documento documento = DocumentoDAO.get(cdDocumento, connection);
			DocumentoTramitacao tramitacao = new DocumentoTramitacao(0 /*cdTramitacao*/, cdDocumento, documento.getCdSetorAtual()/*cdSetorDestino*/, cdUsuario /*cdUsuarioDestino*/,
																	 documento.getCdSetorAtual(), cdUsuario /*cdUsuarioOrigem*/, new GregorianCalendar() /*dtTramitacao*/,
																	 new GregorianCalendar() /*dtRecebimento*/, null /*nmLocalDestino*/, "Documento Arquivado" /*txtTramitacao*/,
																	 documento.getCdSituacaoDocumento(), documento.getCdFase(), null/*nmLocalOrigem*/, null/*nrRemessa*/);

			Result result = DocumentoTramitacaoServices.insert(tramitacao, connection);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			//documento.setTxtObservacao(txtArquivamento);
			documento.setCdSituacaoDocumento(cdSituacaoDocumento);
			if (DocumentoDAO.update(documento, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro eo tentar alterar dados do documento após arquivamento!");
			}
			
			//OCORRENCIA
			int cdOcorrenciaArquivamento = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_ARQUIVAMENTO", 0, 0, connection);
			if(cdOcorrenciaArquivamento>0) {
				DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia(cdDocumento, cdOcorrenciaArquivamento, 0/*cdOcorrencia*/, 
						cdUsuario, new GregorianCalendar(), txtArquivamento, DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO, 0);
				
				result = DocumentoOcorrenciaServices.save(ocorrencia, connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-10, "Não foi possível ocorrência de arquivamento do documento.");
				}
			}

			if (isConnectionNull)
				connection.commit();
			
			SituacaoDocumento situacao = SituacaoDocumentoDAO.get(cdSituacaoDocumento, connection);
			String nmSituacaoDocumento = situacao==null ? "INEXISTENTE" : situacao.getNmSituacaoDocumento();
			
			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("TP_PRIORIDADE", TP_PRIORIDADE_NORMAL);
			register.put("CD_SITUACAO_DOCUMENTO", cdSituacaoDocumento);
			register.put("NM_SITUACAO_DOCUMENTO", nmSituacaoDocumento);

			result = new Result(1, "Documento arquivado com sucesso!");
			result.addObject("register", register);
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar arquivar documento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * 
	 * @param documentos
	 * @param cdUsuario
	 * @param txtArquivamento
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static sol.util.Result arquivarDocumentos(ArrayList<String> documentos, int cdUsuario, String txtArquivamento)	{
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			Result result = new Result(0);
			ResultSetMap rsm  = new ResultSetMap();
			for(int i=0; i<documentos.size(); i++)	{
				Result rTemp = arquivarDocumento(Integer.valueOf(documentos.get(i)), cdUsuario, txtArquivamento);
				if(rTemp.getCode() <= 0)	{
					Conexao.rollback(connect);
					return rTemp;
				}
				//
				rsm.addRegister((HashMap<String,Object>)rTemp.getObjects().get("register"));
				result.setCode(result.getCode() + 1);
			}
			connect.commit();
			result.setMessage("Documento(s) arquivados(s) com sucesso!");
			result.addObject("rsm", rsm);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar arquivar documentos!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	/**
	 * Realiza a persistência dos dados referentes a inclusão de um documento interno do tipo "Comunicação Interno".
	 * O referido tipo deve estão previamente configurado (identificado como CD_TIPO_DOCUMENTO_CI);
	 * caso não o esteja, a rotina se encarrega de persistir um regitro de tipo de documento que corresponda é "Comunicação Interno" e
	 * parametriza-o.
	 * @param documento dados do documento de comunicação interna
	 * @param cdUsuarioDestino código do usuário a quem é atribuido a documento
	 * @return Retorna um hash com os dados do documento, após a persistência
	 */
	public static Result insertComunicaoInterna(Documento documento, int cdUsuarioDestino) {
		return insertComunicaoInterna(documento, cdUsuarioDestino, null);
	}

	public static Result insertComunicaoInterna(Documento documento, int cdUsuarioDestino, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			//
			if(documento.getCdSetor()>0 && documento.getCdSetorAtual()<=0)
				documento.setCdSetorAtual(documento.getCdSetor());
			/* localiza o tipo de documento referente a Comunicação Interna */
			int cdTipoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_CI", 0, 0, connection);
			if (cdTipoDocumento <= 0)
				return new Result(-1, "Tipo de documento padrão para CI não definido!");
			//
			com.tivic.manager.seg.Usuario usuario = com.tivic.manager.seg.UsuarioDAO.get(cdUsuarioDestino, connection);
			//
			documento.setCdTipoDocumento(cdTipoDocumento);
			ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
			solicitantes.add(new DocumentoPessoa(0, usuario.getCdPessoa(), "Solicitante"));
			return insert(documento, solicitantes, connection, new ArrayList<FormularioAtributoValor>());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdDocumento) {
		return delete(cdDocumento, null);
	}

	public static int delete(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			connect.prepareStatement("DELETE FROM ptc_documento_tramitacao WHERE cd_documento = "+cdDocumento).execute();

			connect.prepareStatement("DELETE FROM ptc_documento_pessoa WHERE cd_documento = "+cdDocumento).execute();

			if (DocumentoDAO.delete(cdDocumento, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca os Documentos enviados a partir de um setor
	 * 
	 * @param cdSetorOrigem código do setor de origem do Documento
	 * @param nrRegistros número de registros a ser buscado
	 * @return ResultSetMap lista dos Documentos Enviados
	 */
	public static ResultSetMap getListDocumentosEnviados(int cdSetorOrigem, int nrRegistros) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_SETOR_ATUAL", Integer.toString(cdSetorOrigem), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_SITUACAO_DOCUMENTO", Integer.toString(ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_TRAMITANDO", 1)),
											ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("lgUltimaTramitacao", Integer.toString(1), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("nrRegistros", Integer.toString(nrRegistros), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("orderByField", "A.dt_protocolo DESC", ItemComparator.EQUAL, Types.INTEGER));
		
		ResultSetMap rsm = find(criterios);
		
		while(rsm.next()) {
			rsm.setValueToField("NM_SETOR_TRAMITACAO", rsm.getString("NM_ULTIMO_SETOR_DESTINO", null));
			rsm.setValueToField("SG_SETOR_TRAMITACAO", rsm.getString("SG_ULTIMO_SETOR_DESTINO", null));
			rsm.setValueToField("NR_SETOR_EXTERNO_TRAMITACAO", rsm.getString("NR_SETOR_EXTERNO_DESTINO", null));
			
			rsm.setValueToField("CD_EMPRESA_TRAMITACAO", rsm.getInt("CD_EMPRESA_DESTINO"));
			rsm.setValueToField("NM_EMPRESA_TRAMITACAO", rsm.getString("NM_EMPRESA_DESTINO"));
			
			ResultSetMap rsmOcorrencia = DocumentoOcorrenciaServices.getAllByDocumento(rsm.getInt("CD_DOCUMENTO"));
			if(rsmOcorrencia.next()) {
				rsm.setValueToField("DS_OCORRENCIA", rsmOcorrencia.getString("NM_TIPO_OCORRENCIA") + " [" + Util.formatDate(rsmOcorrencia.getTimestamp("DT_OCORRENCIA"), "dd/MM/yyyy") + "]");
			}
		}
		rsm.beforeFirst();
		
		return rsm;
	}

	public static ResultSetMap getListDocumentosEnviados(int cdUsuario, int cdEmpresa, int nrRegistros) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_USUARIO", Integer.toString(cdUsuario), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		
		return find(criterios);
	}
	
	/**
	 * Busca lista de Documentos enviados para um determinado setor.
	 * 
	 * @param cdSetorDestino Código do setor de destino da tramitação
	 * @param nrRegistros Número de registro a serem listados
	 * @return ResultSetMap lista dos Documentos enviados
	 */
	public static ResultSetMap getListDocumentosRecebidos(int cdSetorDestino, int nrRegistros) {
		int stTramitando = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_TRAMITANDO", 1);
		int stSetorExterno = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ENVIO_EXTERNO", 1);
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("M.CD_SETOR_DESTINO", Integer.toString(cdSetorDestino), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_SITUACAO_DOCUMENTO", stTramitando+", "+stSetorExterno, ItemComparator.IN, Types.INTEGER));
		criterios.add(new ItemComparator("lgUltimaTramitacao", Integer.toString(1), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("nrRegistros", Integer.toString(nrRegistros), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("orderByField", "A.dt_protocolo DESC", ItemComparator.EQUAL, Types.INTEGER));

		ResultSetMap rsm = find(criterios);
		
		while(rsm.next()) {
			rsm.setValueToField("NM_SETOR_TRAMITACAO", rsm.getString("NM_ULTIMO_SETOR_ORIGEM", null));
			rsm.setValueToField("SG_SETOR_TRAMITACAO", rsm.getString("SG_ULTIMO_SETOR_ORIGEM", null));
			rsm.setValueToField("NR_SETOR_EXTERNO_TRAMITACAO", rsm.getString("NR_SETOR_EXTERNO_ORIGEM", null));
			
			rsm.setValueToField("CD_EMPRESA_TRAMITACAO", rsm.getInt("CD_EMPRESA_ORIGEM"));
			rsm.setValueToField("NM_EMPRESA_TRAMITACAO", rsm.getString("NM_EMPRESA_ORIGEM"));
			
			ResultSetMap rsmOcorrencia = DocumentoOcorrenciaServices.getAllByDocumento(rsm.getInt("CD_DOCUMENTO"));
			if(rsmOcorrencia.next()) {
				rsm.setValueToField("DS_OCORRENCIA", rsmOcorrencia.getString("NM_TIPO_OCORRENCIA") + " [" + Util.formatDate(rsmOcorrencia.getTimestamp("DT_OCORRENCIA"), "dd/MM/yyyy") + "]");
			}
		}
		rsm.beforeFirst();
		
//		ArrayList<String> columns = new ArrayList<String>();
//		columns.add("TP_PRIORIDADE DESC");
//		columns.add("DT_TRAMITACAO DESC");
//		rsm.orderBy(columns);
//		rsm.beforeFirst();
		
		return rsm;
	}
	
	/**
	 * Busca lista de Documentos recebidos por um setor.
	 * @param cdSetor Código do setor de destino da tramitação
	 * @param cdEmpresa Código da empresa
	 * @param nrRegistros Número máximo de registros no resultado
	 * @return ResultSetMap lista de Documentos
	 */
	public static ResultSetMap getListDocumentosRecebidos(int cdSetor, int cdEmpresa, int nrRegistros) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_SETOR", Integer.toString(cdSetor), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		
		return find(criterios);
	}
	
	/**
	 * Busca lista de documentos parados em um determinado setor. O Documento estão "Parado"
	 * quando estão em um setor com a situação "Em Aberto"
	 *  
	 * @param cdSetor Código do setor
	 * @param nrRegistros Número máximo de registros
	 * @return ResultSetMap lista de Documentos Parados
	 */
	public static ResultSetMap getListDocumentosParados(int cdSetor, int nrRegistros) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_SETOR_ATUAL", Integer.toString(cdSetor), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_SITUACAO_DOCUMENTO", Integer.toString(ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 1)),
				ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("lgUltimaTramitacao", Integer.toString(1), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("nrRegistros", Integer.toString(nrRegistros), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("orderByField", "A.dt_protocolo DESC", ItemComparator.EQUAL, Types.INTEGER));
		
		ResultSetMap rsm = find(criterios);
		
		while(rsm.next()) {
			rsm.setValueToField("NM_SETOR_TRAMITACAO", rsm.getString("NM_ULTIMO_SETOR_ORIGEM", null));
			rsm.setValueToField("SG_SETOR_TRAMITACAO", rsm.getString("SG_ULTIMO_SETOR_ORIGEM", null));
			rsm.setValueToField("NR_SETOR_EXTERNO_TRAMITACAO", rsm.getString("NR_SETOR_EXTERNO_ORIGEM", null));
			
			rsm.setValueToField("CD_EMPRESA_TRAMITACAO", rsm.getInt("CD_EMPRESA_ORIGEM"));
			rsm.setValueToField("NM_EMPRESA_TRAMITACAO", rsm.getString("NM_EMPRESA_ORIGEM"));
			
			ResultSetMap rsmOcorrencia = DocumentoOcorrenciaServices.getAllByDocumento(rsm.getInt("CD_DOCUMENTO"));
			if(rsmOcorrencia.next()) {
				rsm.setValueToField("DS_OCORRENCIA", rsmOcorrencia.getString("NM_TIPO_OCORRENCIA") + " [" + Util.formatDate(rsmOcorrencia.getTimestamp("DT_OCORRENCIA"), "dd/MM/yyyy") + "]");
			}
		}
		rsm.beforeFirst();
		
		return rsm;
	}
	
	/**
	 * 
	 * @param nrRemessa
	 * @return
	 * 
	 * @category EeL
	 */
	public static ResultSetMap getDocumentoByRemessa(String nrRemessa, int cdSetorOrigem) {
//		System.out.println("nrRemessa: "+nrRemessa);
//		System.out.println("cdSetorOrigem: "+cdSetorOrigem);
//		
//		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//		criterios.add(new ItemComparator("M.nr_remessa", nrRemessa, ItemComparator.EQUAL, Types.VARCHAR));
//		criterios.add(new ItemComparator("M.cd_setor_origem", Integer.toString(cdSetorOrigem), ItemComparator.EQUAL, Types.VARCHAR));
//		criterios.add(new ItemComparator("lgUltimaTramitacao", Integer.toString(1), ItemComparator.EQUAL, Types.VARCHAR));
//		
//		
//		
//		
//		ResultSetMap rsm = find(criterios);
//		
//		return rsm;
		Connection connect = Conexao.conectar();
		try {
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.cd_documento, A.nr_documento, A.txt_documento,"
					+ " B.nm_tipo_documento, C.nm_setor"
					+ " FROM ptc_documento A"
					+ " LEFT OUTER JOIN gpn_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento)"
					+ " LEFT OUTER JOIN grl_setor C ON (A.cd_setor = C.cd_setor)"
					+ " JOIN ptc_documento_tramitacao D on (A.cd_documento = D.cd_documento)"
					+ " WHERE D.nr_remessa='"+nrRemessa+"'"
					+ " AND D.cd_setor_origem="+cdSetorOrigem
					+ " AND D.cd_tramitacao = (SELECT max(cd_tramitacao) FROM ptc_documento_tramitacao WHERE cd_documento = A.cd_documento) "
					);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()) {				
				// Acrescentando informação dos solicitantes
				Result result = getSolicitantes(rsm.getInt("cd_documento"), connect);
				rsm.setValueToField("rsmSolicitantes", result.getObjects().get("rsm"));
				rsm.setValueToField("NM_SOLICITANTES", result.getObjects().get("nmSolicitantes"));
				rsm.setValueToField("CD_SOLICITANTES", result.getObjects().get("cdSolicitantes"));
				
				rsm.setValueToField("DS_NR_DOCUMENTO", Util.removeZeroEsquerda((rsm.getString("NR_DOCUMENTO")!=null?rsm.getString("NR_DOCUMENTO"):"")));
				rsm.setValueToField("DS_NR_PROTOCOLO_EXTERNO", Util.removeZeroEsquerda((rsm.getString("NR_PROTOCOLO_EXTERNO")!=null?rsm.getString("NR_PROTOCOLO_EXTERNO"):"")));
			}
			rsm.beforeFirst();
			
			return rsm;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			String nmSolicitante  = null;
			int cdSolicitante = 0;
			int cdTipoPendencia = 0;
			
			String orderByField = null;
			int nrRegistros = 0;
			int lgUltimaTramitacao = 0;
			int cdTipoDocumentoSuperior = 0;
			
			int tpRelatorio = 0;
			
			int cdSetorOrigem = 0;
			int cdSetorDestino = 0;
			String dsDtTramitacaoInicial = null;
			String dsDtTramitacaoFinal = null;
			int tpSetor = -1;
			
			String sqlNrDocumento = null;
			String sqlNrAtendimento = null;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("nrRegistros")) {
					nrRegistros = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("nmSolicitante")) {
					nmSolicitante = criterios.get(i).getValue().toString().trim().toUpperCase();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdSolicitante")) {
					cdSolicitante = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdTipoPendencia")) {
					cdTipoPendencia = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("a.nr_documento"))	{
					/*
					 * Pesquisa o Nr. do documento somente com números
					 */
					//nrProtocoloExterno = criterios.get(i).getValue();
//					int index = nrProtocoloExterno.indexOf("/");
//					if(index>=0) {
//						nrProtocoloExterno = new String(nrProtocoloExterno).substring(0, index);
//					}
					
					String nrDocumento = criterios.get(i).getValue().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
					sqlNrDocumento = " AND (REPLACE(REPLACE(REPLACE(A.nr_documento, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrDocumento +"%\'"
										+ " OR REPLACE(REPLACE(REPLACE(A.nr_protocolo_externo, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrDocumento +"\') ";
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("a.nr_atendimento"))	{
					/*
					 * Pesquisa o Nr. do atendimento somente com números
					 */
					String nrAtendimento = criterios.get(i).getValue().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
					sqlNrAtendimento = " AND (REPLACE(REPLACE(REPLACE(A.nr_atendimento, '.', ''), '-', ''), '/', '') LIKE \'%"+ nrAtendimento +"%\') ";
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("tpRelatorio")) {
					tpRelatorio = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("lgUltimaTramitacao")) {
					lgUltimaTramitacao = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("orderByField")) {
					orderByField = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("A.CD_SITUACAO_DOCUMENTO")) {
					//Caso o critério seja mandado com valor 0, acrescenta um valor de situação
					if(criterios.get(i).getValue().equals("0")) {
						criterios.get(i).setValue(""+ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0, 0, connect));
						i--;
					}
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("M.CD_SETOR_ORIGEM")) {
					cdSetorOrigem = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("M.CD_SETOR_DESTINO")) {
					cdSetorDestino = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("DT_TRAMITACAO_INICIAL")) {
					dsDtTramitacaoInicial = Util.convCalendarStringSqlCompleto(Util.convStringToCalendar(criterios.get(i).getValue().toString().trim()));
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("DT_TRAMITACAO_FINAL")) {
					dsDtTramitacaoFinal = Util.convCalendarStringSqlCompleto(Util.convStringToCalendar(criterios.get(i).getValue().toString().trim()));
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpSetor")) {
					tpSetor = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("cdTipoDocumentoSuperior")) {
					cdTipoDocumentoSuperior = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
			}	
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(nrRegistros, 0);
			
			String sql = "SELECT " + sqlLimit[0] +
					//(nrRegistros>0 ? (Util.getConfManager().getIdOfDbUsed().equals("FB") ? " FIRST "+nrRegistros: " ") : "") +
					" 		A.*, A.nr_assunto AS cd_assunto,"+
					"		C.nm_pessoa as nm_usuario, C.cd_pessoa, C.nm_pessoa, "+
					"		D.nm_tipo_documento, D.cd_formulario,"+
					"		G.cd_empresa as cd_empresa_cadastro, G.nm_setor, G.sg_setor, G.cd_empresa, G1.nm_pessoa as nm_empresa,"+
					"		H.nm_setor AS nm_setor_atual, H.sg_setor AS sg_setor_atual, H.nr_setor_externo AS nr_setor_externo_atual, "+
					"		H.cd_empresa as cd_empresa_atual, H1.nm_pessoa as nm_empresa_atual,"+
					"		I.nm_situacao_documento, J.nm_fase," +
					(cdTipoPendencia>0 ? " L.cd_tipo_pendencia, ": "")+
					"		P.nr_processo " +
					" FROM ptc_documento A " +
					" LEFT OUTER JOIN seg_usuario        B ON (A.cd_usuario = B.cd_usuario) " +
					" LEFT OUTER JOIN grl_pessoa         C ON (B.cd_pessoa = C.cd_pessoa) " +
					" LEFT OUTER JOIN gpn_tipo_documento D on (A.cd_tipo_documento = D.cd_tipo_documento) " +
					" LEFT OUTER JOIN gpn_tipo_documento D1 on (D.cd_tipo_documento = D1.cd_tipo_documento) " +
					" LEFT OUTER JOIN grl_setor          G ON (A.cd_setor = G.cd_setor) " +
					" LEFT OUTER JOIN grl_pessoa		 G1 ON (G.cd_empresa = G1.cd_pessoa)"+
					" LEFT OUTER JOIN grl_setor          H ON (A.cd_setor_atual = H.cd_setor) " +
					" LEFT OUTER JOIN grl_pessoa		 H1 ON (H.cd_empresa = H1.cd_pessoa)"+
					" LEFT OUTER JOIN ptc_situacao_documento I ON (A.cd_situacao_documento = I.cd_situacao_documento) " +
					" LEFT OUTER JOIN ptc_fase           J ON (A.cd_fase = J.cd_fase) " +
					(nmSolicitante!=null ? " JOIN ptc_documento_pessoa E ON (A.cd_documento = E.cd_documento) " +
										   " JOIN grl_pessoa           F ON (E.cd_pessoa = F.cd_pessoa AND F.nm_pessoa LIKE '" + nmSolicitante +"%') " : "") +
					(cdSolicitante>0 ? " JOIN ptc_documento_pessoa E ON (A.cd_documento = E.cd_documento) " +
										   " JOIN grl_pessoa           F ON (E.cd_pessoa = F.cd_pessoa AND F.cd_pessoa = " + cdSolicitante +") " : "") +
					(cdTipoPendencia>0 ? " JOIN ptc_documento_pendencia L ON (A.cd_documento = L.cd_documento AND L.cd_tipo_pendencia = " + cdTipoPendencia +") " : "") +
					" LEFT OUTER JOIN prc_processo P ON (A.cd_processo = P.cd_processo)" +
					" WHERE 1 = 1 " +
					(sqlNrDocumento!=null ? sqlNrDocumento : " ") +
					(sqlNrAtendimento!=null ? sqlNrAtendimento : " ") +
					" AND EXISTS (SELECT M1.cd_tramitacao FROM ptc_documento_tramitacao M1"
					+ "				LEFT OUTER JOIN grl_setor M2 ON (M1.cd_setor_origem=M2.cd_setor)"
					+ "				LEFT OUTER JOIN grl_setor M3 ON (M1.cd_setor_destino=M3.cd_setor)"
					+ "				WHERE A.cd_documento = M1.cd_documento"
					+ (cdSetorOrigem>0 ? " AND M1.cd_setor_origem="+cdSetorOrigem : "")
					+ (cdSetorDestino>0 ? " AND M1.cd_setor_destino="+cdSetorDestino : "")
					+ (dsDtTramitacaoInicial!=null ? " AND M1.dt_tramitacao>='"+dsDtTramitacaoInicial+"'" : "")
					+ (dsDtTramitacaoFinal!=null ? " AND M1.dt_tramitacao<='"+dsDtTramitacaoFinal+"'" : "")
					+ (tpSetor>-1 ? " AND M2.tp_setor="+tpSetor : "")
					+ (tpSetor>-1 ? " AND M3.tp_setor="+tpSetor : "")
					+ (lgUltimaTramitacao==1 ? " AND M1.cd_tramitacao = (SELECT max(cd_tramitacao) FROM ptc_documento_tramitacao WHERE cd_documento = A.cd_documento) " : " ")
					+ ")";

			ResultSetMap rsm = Search.find(sql , (orderByField!=null ? orderByField : " ")+sqlLimit[1], criterios, connect!=null ? connect : connect, false);
			//ResultSetMap rsm = Search.findAndLog(sql, (orderByField!=null ? orderByField : " ")+sqlLimit[1], criterios, connect!=null ? connect : connect, false);
				
			while(rsm.next()) {				
				// Acrescentando informação dos solicitantes
				Result result = getSolicitantes(rsm.getInt("cd_documento"), connect);
				rsm.setValueToField("rsmSolicitantes", result.getObjects().get("rsm"));
				rsm.setValueToField("NM_SOLICITANTES", result.getObjects().get("nmSolicitantes"));
				rsm.setValueToField("CD_SOLICITANTES", result.getObjects().get("cdSolicitantes"));
				
				rsm.setValueToField("DS_NR_DOCUMENTO", Util.removeZeroEsquerda((rsm.getString("NR_DOCUMENTO")!=null?rsm.getString("NR_DOCUMENTO"):"")));
				rsm.setValueToField("DS_NR_PROTOCOLO_EXTERNO", Util.removeZeroEsquerda((rsm.getString("NR_PROTOCOLO_EXTERNO")!=null?rsm.getString("NR_PROTOCOLO_EXTERNO"):"")));
				
				//ULTIMA TRAMITACAO
				DocumentoTramitacao ultimaTramitacao = null;
				Setor setorOrigem = null;
				Setor setorDestino = null;
				int cdDocumento = rsm.getInt("cd_documento");
				ultimaTramitacao = DocumentoTramitacaoServices.getUltimaTramitacao(cdDocumento, connect);
				
				if(ultimaTramitacao!=null) {
					rsm.setValueToField("DT_TRAMITACAO", Util.convCalendarToTimestamp(ultimaTramitacao.getDtTramitacao()));
					rsm.setValueToField("DS_DT_TRAMITACAO", Util.formatDate(ultimaTramitacao.getDtTramitacao(), "dd/MM/yyyy"));
					rsm.setValueToField("DT_RECEBIMENTO", Util.convCalendarToTimestamp(ultimaTramitacao.getDtRecebimento()));
					rsm.setValueToField("DS_DT_RECEBIMENTO", Util.formatDate(ultimaTramitacao.getDtRecebimento(), "dd/MM/yyyy"));
					
					rsm.setValueToField("DT_ULTIMA_TRAMITACAO", Util.convCalendarToTimestamp(ultimaTramitacao.getDtTramitacao()));
					rsm.setValueToField("DS_DT_ULTIMA_TRAMITACAO", Util.formatDate(ultimaTramitacao.getDtTramitacao(), "dd/MM/yyyy"));
					rsm.setValueToField("DT_ULTIMO_RECEBIMENTO", Util.convCalendarToTimestamp(ultimaTramitacao.getDtRecebimento()));
					rsm.setValueToField("DS_DT_ULTIMO_RECEBIMENTO", Util.formatDate(ultimaTramitacao.getDtRecebimento(), "dd/MM/yyyy"));
					
					setorOrigem = SetorDAO.get(ultimaTramitacao.getCdSetorOrigem(), connect);
					setorDestino = SetorDAO.get(ultimaTramitacao.getCdSetorDestino(), connect);
					
					rsm.setValueToField("NM_SETOR_ORIGEM", (setorOrigem==null?"":setorOrigem.getNmSetor()));
					rsm.setValueToField("SG_SETOR_ORIGEM", (setorOrigem==null?"":setorOrigem.getSgSetor()));
					rsm.setValueToField("NR_SETOR_EXTERNO_ORIGEM", (setorOrigem==null?"":setorOrigem.getNrSetorExterno()));
					rsm.setValueToField("NM_SETOR_DESTINO", (setorDestino==null?"":setorDestino.getNmSetor()));
					rsm.setValueToField("SG_SETOR_DESTINO", (setorDestino==null?"":setorDestino.getSgSetor()));
					rsm.setValueToField("NR_SETOR_EXTERNO_DESTINO", (setorDestino==null?"":setorDestino.getNrSetorExterno()));
					
					rsm.setValueToField("NM_ULTIMO_SETOR_ORIGEM", (setorOrigem==null?"":setorOrigem.getNmSetor()));
					rsm.setValueToField("NM_ULTIMO_SETOR_DESTINO", (setorDestino==null?"":setorDestino.getNmSetor()));
					rsm.setValueToField("SG_ULTIMO_SETOR_ORIGEM", (setorOrigem==null?"":setorOrigem.getSgSetor()));
					rsm.setValueToField("SG_ULTIMO_SETOR_DESTINO", (setorDestino==null?"":setorDestino.getSgSetor()));
					rsm.setValueToField("SG_ULTIMO_SETOR_DESTINO", (setorDestino==null?"":setorDestino.getSgSetor()));
				}
			}
			rsm.beforeFirst();
					
			if(tpRelatorio == 4) {
				while(rsm.next()) {
					rsm.setValueToField("LST_TRAMITACOES", getAllTramites(rsm.getInt("cd_documento"), connect).getLines());
				}
				rsm.beforeFirst();
			}
			
			// Dados a ultima ocorrencia do documento
			if(tpRelatorio == 5) {
				int cdDocumento;
				DocumentoOcorrencia ultimaOcorrencia = null;
				
				while(rsm.next()) {
					cdDocumento = rsm.getInt("cd_documento");
					ultimaOcorrencia = DocumentoOcorrenciaServices.getUltimaOcorrencia(cdDocumento, connect);
					
					if(ultimaOcorrencia!=null) {
						rsm.setValueToField("DT_ULTIMA_OCORRENCIA", Util.convCalendarToTimestamp(ultimaOcorrencia.getDtOcorrencia()));
						rsm.setValueToField("DS_DT_ULTIMA_OCORRENCIA", Util.formatDate(ultimaOcorrencia.getDtOcorrencia(), "dd/MM/yyyy"));
						rsm.setValueToField("TXT_ULTIMA_OCORRENCIA", ultimaOcorrencia.getTxtOcorrencia());
						rsm.setValueToField("NM_TP_ULTIMA_OCORRENCIA", TipoOcorrenciaDAO.get(ultimaOcorrencia.getCdTipoOcorrencia(), connect).getNmTipoOcorrencia());
						rsm.setValueToField("NM_USUARIO_ULTIMA_OCORRENCIA", UsuarioDAO.get(ultimaOcorrencia.getCdUsuario(), connect).getNmLogin());
					}
				}
				rsm.beforeFirst();
			}
			
			// Busca de informações dos documentos filhos (com base no tipo documento superior)
			if (cdTipoDocumentoSuperior!=0) {
				
				ResultSetMap rsmTiposDocumentoFilho = TipoDocumentoServices.getAllByDocumentoSuperior(cdTipoDocumentoSuperior, connect);
				ResultSetMap rsmDocumentos = new ResultSetMap();
				rsm = new ResultSetMap();
				
				while (rsmTiposDocumentoFilho.next()) {
					
					// Atualizar o criterio de tipoDocumento
					for (int i=0; criterios!=null && i<criterios.size(); i++) {
						if(criterios.get(i).getColumn().equalsIgnoreCase("A.CD_TIPO_DOCUMENTO")) {
							criterios.get(i).setValue(rsmTiposDocumentoFilho.getInt("CD_TIPO_DOCUMENTO")+"");
							break;
						}
					}
					
					rsmDocumentos = find(criterios);
					if(rsmDocumentos!=null && rsmDocumentos.size()>0) {
						for (int i=0; i<rsmDocumentos.size(); i++) {
							rsm.addRegister(rsmDocumentos.getLines().get(i));
						}	
					}
				}
				
				rsm.beforeFirst();
			}

			//ordenando rsm
			ArrayList<String> columns = new ArrayList<String>();
			if(tpRelatorio == 0 && orderByField==null)
				columns.add("DT_PROTOCOLO");
			else if(orderByField==null)
				columns.add("DT_TRAMITACAO DESC");
			rsm.orderBy(columns);
			rsm.beforeFirst();
			
			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findGratuidadeSolicitacoes(ArrayList<ItemComparator> criterios) {
		return findGratuidadeSolicitacoes(criterios, null);
	}

	public static ResultSetMap findGratuidadeSolicitacoes(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			String orderBy = "";			
			int qtRegistros = 0;
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("LIMIT")) {
					qtRegistros = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
					criterios.remove(i);
					i--;
				}
			}
			
			
			
			String sql = "  SELECT A.*, E.nr_documento, E.cd_documento, F.cd_situacao_documento, F.nm_situacao_documento, G.nr_rg, G.nr_cpf, " + 
					 "  B.cd_pessoa, B.nm_pessoa AS nm_solicitante" + 
					 "	FROM mob_cartao								A" + 
					 "	left JOIN grl_pessoa 						B ON ( A.cd_pessoa = B.cd_pessoa)" + 
					 "	right JOIN  mob_cartao_documento 			C ON ( C.cd_cartao = A.cd_cartao)" + 
					 "	right JOIN mob_cartao_documento_doenca 		D ON ( D.cd_cartao_documento = C.cd_cartao_documento )" + 
					 "	JOIN ptc_documento 							E ON ( E.cd_documento = C.cd_documento )" + 
					 "	JOIN ptc_situacao_documento 				F ON ( F.cd_situacao_documento = E.cd_situacao_documento)" + 
					 "	JOIN grl_pessoa_fisica 						G ON ( G.cd_pessoa = B.cd_pessoa )" + 
					 "	JOIN seg_usuario 							I ON ( I.cd_usuario = E.cd_usuario )" + 
					 "	JOIN grl_pessoa 							J ON ( J.cd_pessoa = I.cd_pessoa )" + 
					 " WHERE 1=1";
						
			ResultSetMap rsm = Search.find(sql, ( orderBy != "" ? orderBy + " DESC " : " ORDER BY A.CD_PESSOA ASC ") + (qtRegistros > 0? " LIMIT " + qtRegistros: ""),
					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaServices.findRelatorio: " + e);
			return null;
		}
	}
	
	public static List<DocumentoDTO> findSolicitacoesDTO(ArrayList<ItemComparator> criterios) throws Exception{
		return findSolicitacoesDTO(criterios, null);
	}
	
	public static List<DocumentoDTO> findSolicitacoesDTO(ArrayList<ItemComparator> criterios, Connection connect){
		ResultSetMap rsm = findGratuidadeSolicitacoes(criterios);
		
		List<DocumentoDTO> list = new ArrayList<DocumentoDTO>();
		
		while(rsm.next()) {
			DocumentoDTO dto = new DocumentoDTO();
			
			Documento documento = DocumentoDAO.get(rsm.getInt("CD_DOCUMENTO"));
			dto.setDocumento(documento);
			
			Pessoa pessoa = PessoaDAO.get(rsm.getInt("CD_PESSOA"));
			dto.setPessoa(pessoa);
			
			Cartao cartao = CartaoDAO.get(rsm.getInt("CD_CARTAO"));
			dto.setCartao(cartao);
			
			DocumentoTramitacao documentoTramitacao = DocumentoTramitacaoDAO.get(rsm.getInt("CD_TRAMITACAO"), rsm.getInt("CD_DOCUMENTO"));
			dto.setDocumentoTramitacao(documentoTramitacao);
			
			SituacaoDocumento situacaoDocumento = SituacaoDocumentoDAO.get(rsm.getInt("CD_SITUACAO_DOCUMENTO"));
			dto.setSituacaoDocumento(situacaoDocumento);
			
			list.add(dto);
		}
		
		
		return list;
	}
	
	public static ResultSetMap getAllDocumentosBySolicitante(int cdSolicitante, Connection connect) {
		String sql = "SELECT A.*, A.nr_assunto AS cd_assunto, C.nm_pessoa AS nm_usuario, C.cd_pessoa, C.nm_pessoa, D.nm_tipo_documento, G.cd_empresa AS cd_empresa_cadastro, " +
					 "G.nm_setor, G.sg_setor, G.cd_empresa, G1.nm_pessoa AS nm_empresa, H.nm_setor AS nm_setor_atual, H.sg_setor AS sg_setor_atual, H.cd_empresa AS cd_empresa_atual, " +
					 "H1.nm_pessoa AS nm_empresa_atual, I.nm_situacao_documento, J.nm_fase, P.nr_processo " +
					 "FROM ptc_documento A " +
					 "LEFT OUTER JOIN seg_usuario B ON (A.cd_usuario = B.cd_usuario)" +
					 "LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
					 "LEFT OUTER JOIN gpn_tipo_documento D ON (A.cd_tipo_documento = D.cd_tipo_documento) " +
//					 "LEFT OUTER JOIN ptc_tipo_documento D1 ON (D.cd_tipo_documento = D1.cd_tipo_documento) " +
					 "LEFT OUTER JOIN grl_setor G ON (A.cd_setor = G.cd_setor) " +
					 "LEFT OUTER JOIN grl_pessoa G1 ON (G.cd_empresa = G1.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_setor H ON (A.cd_setor_atual = H.cd_setor) " +
					 "LEFT OUTER JOIN grl_pessoa H1 ON (H.cd_empresa = H1.cd_pessoa) " +
					 "LEFT OUTER JOIN ptc_situacao_documento I ON (A.cd_situacao_documento = I.cd_situacao_documento) " +
					 "LEFT OUTER JOIN ptc_fase J ON (A.cd_fase = J.cd_fase) " +
					 "JOIN ptc_documento_pessoa E ON (A .cd_documento = E.cd_documento) " +
					 "JOIN grl_pessoa F ON (E.cd_pessoa = F.cd_pessoa AND F.cd_pessoa = " + cdSolicitante + ") " +
					 "LEFT OUTER JOIN prc_processo P ON (A.cd_processo = P.cd_processo) " +
					 "WHERE 1 = 1"; 
		
		ResultSetMap rsm = Search.find(sql , "", null, connect!=null ? connect : connect, false);
		
		return rsm;
	}
	
	/**
	 * Busca: Os documentos pendentes de recebimento mais antigos e a quantos dias ele estão
	 * pendente; Os documentos parados a mais tempo. Busca realizada para todos os setores 
	 * cadastrados
	 * 
	 * @param cdEmpresa Código da Empresa
	 * @return ResultSetMap Ranking dos Documentos
	 */
	public static ResultSetMap getRankingDocumentos(int cdEmpresa) {		
		Connection connect = Conexao.conectar();
		ResultSetMap rsm = null;
		
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.TP_SETOR", Integer.toString(SetorServices.TP_SETOR_INTERNO), ItemComparator.EQUAL, Types.INTEGER));
			
			rsm = SetorServices.findCompleto(criterios, connect);
			int cdSetor = 0;
			while(rsm.next()) {
				cdSetor = rsm.getInt("CD_SETOR");
				
				//Quantidade de documentos a receber e data documento mais antigo
				PreparedStatement pstmt = connect.prepareStatement("SELECT count(A.cd_documento) as qt_tramitados, min(B.dt_tramitacao) as dt_mais_antigo" +
																   " FROM ptc_documento A" +
																   " JOIN ptc_documento_tramitacao B ON (A.cd_documento = B.cd_documento AND B.dt_recebimento IS NULL)" +
																   " WHERE B.cd_setor_destino = ?" +
																   " AND B.cd_tramitacao = (SELECT max(cd_tramitacao) FROM ptc_documento_tramitacao WHERE cd_documento = A.cd_documento)");
				pstmt.setInt(1, cdSetor);
				
				ResultSetMap rsmAux = new ResultSetMap(pstmt.executeQuery());
				if(rsmAux.next()) {
					//cálculo de dias
					GregorianCalendar hoje = new GregorianCalendar();
					GregorianCalendar dtTramitacao = Util.convTimestampToCalendar(rsmAux.getTimestamp("dt_mais_antigo"));
					long qtDias = Math.round((hoje.getTimeInMillis() - (dtTramitacao==null?hoje.getTimeInMillis():dtTramitacao.getTimeInMillis())) / 1000 / 60 / 60 / 24);
					
					rsm.setValueToField("QT_TRAMITADOS", rsmAux.getInt("qt_tramitados"));
					rsm.setValueToField("DT_MAIS_ANTIGO", rsmAux.getTimestamp("dt_mais_antigo"));
					rsm.setValueToField("DS_DT_MAIS_ANTIGO", Util.formatDate(rsmAux.getTimestamp("dt_mais_antigo"), "dd/MM/yyyy"));
					rsm.setValueToField("QT_DIAS", qtDias);
				}
				
				//Quantidade de documentos recebidos
				pstmt = connect.prepareStatement("SELECT count(cd_documento) as qt_parados" +
												 " FROM ptc_documento" +
												 " WHERE cd_setor_atual = ?" +
												 " AND cd_situacao_documento = ?");
				pstmt.setInt(1, cdSetor);
				pstmt.setInt(2, ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0, 0, connect));
				
				rsmAux = new ResultSetMap(pstmt.executeQuery());
				if(rsmAux.next()) 
					rsm.setValueToField("QT_PARADOS", rsmAux.getInt("qt_parados"));
				rsm.setValueToField("QT_TOTAL", rsm.getInt("qt_parados") + rsm.getInt("qt_tramitados"));
				
				//Documento mais antigo
				pstmt = connect.prepareStatement("SELECT A.nr_documento, B.nm_tipo_documento, D.nm_setor as nm_setor_emitente, D.sg_setor as sg_setor_emitente" +
												 " FROM ptc_documento A" +
												 " JOIN gpn_tipo_documento B ON (A.cd_tipo_documento = B.cd_tipo_documento)" +
												 " JOIN ptc_documento_tramitacao C ON (A.cd_documento = C.cd_documento AND C.cd_setor_destino = ? AND C.dt_tramitacao = ?)" +
												 " LEFT OUTER JOIN grl_setor D ON (A.cd_setor = D.cd_setor)");
				pstmt.setInt(1, cdSetor);
				pstmt.setTimestamp(2, rsm.getTimestamp("dt_mais_antigo"));
				
				rsmAux = new ResultSetMap(pstmt.executeQuery());
				if(rsmAux.next()) {
					rsm.setValueToField("NR_DOCUMENTO_ANTIGO", rsmAux.getString("nr_documento"));
					rsm.setValueToField("NM_TIPO_DOCUMENTO_ANTIGO", rsmAux.getString("nm_tipo_documento"));
				}
			}
			rsm.beforeFirst();
			
			ArrayList<String> columns = new ArrayList<String>();
			columns.add("QT_TRAMITADOS DESC");
			rsm.orderBy(columns);
			rsm.beforeFirst();
			
			return rsm;
			
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
		
	}
	
	/**
	 * Busca lista de documentos parados ou recebidos bem como o período que ele estão 
	 * nessa situação.
	 * 
	 * @param lgParado 0-Não recebido, 1-Recebido(Parado)
	 * @param nrRegistros Quantidade máxima de registros
	 * @return ResultSetMap Lista dos documentos ordenada pelo mais antigo
	 */
	public static ResultSetMap getDocumentosPod(boolean lgParado, int nrRegistros) {
		Connection connect = Conexao.conectar();
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("tpSetor", Integer.toString(SetorServices.TP_SETOR_INTERNO), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("lgUltimaTramitacao", Integer.toString(1), ItemComparator.EQUAL, Types.INTEGER));
		//criterios.add(new ItemComparator("M.DT_RECEBIMENTO", "", (lgParado ? ItemComparator.NOTISNULL : ItemComparator.ISNULL), Types.DATE));
		criterios.add(new ItemComparator("A.CD_SITUACAO_DOCUMENTO", ParametroServices.getValorOfParametroAsString("CD_SIT_DOC_ARQUIVADO", "0"), 
				ItemComparator.DIFFERENT, Types.INTEGER));
		criterios.add(new ItemComparator("nrRegistros", Integer.toString(nrRegistros), ItemComparator.EQUAL, Types.INTEGER));
		//criterios.add(new ItemComparator("orderByField", "M.dt_tramitacao", ItemComparator.EQUAL, Types.INTEGER));
		
		ResultSetMap rsm = find(criterios, connect);
		
		while(rsm.next()) {
			GregorianCalendar hoje = new GregorianCalendar();
			long qtDias = Math.round((hoje.getTimeInMillis() - rsm.getTimestamp("dt_tramitacao").getTime()) / 1000 / 60 / 60 / 24);
			rsm.setValueToField("QT_DIAS", qtDias);
			
			rsm.setValueToField("NM_SETOR_TRAMITACAO", rsm.getString("NM_SETOR_DESTINO"));
			rsm.setValueToField("SG_SETOR_TRAMITACAO", rsm.getString("SG_SETOR_DESTINO"));
		}
		rsm.beforeFirst();
		
		ArrayList<String> columns = new ArrayList<String>();
		if(nrRegistros==0)
			columns.add("NM_SETOR_DESTINO");
		columns.add("QT_DIAS DESC");
		rsm.orderBy(columns);
		
		return rsm;
	}
	
	public static Result insertDocumentoVinculado(Documento documento, ResultSetMap rsmSolicitantes) {
		return insertDocumentoVinculado(documento, rsmSolicitantes, null);
	}
	
	public static Result insertDocumentoVinculado(Documento documento, ResultSetMap rsmSolicitantes, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
			while(rsmSolicitantes.next()) {
				solicitantes.add(new DocumentoPessoa(rsmSolicitantes.getInt("CD_DOCUMENTO"), 
													 rsmSolicitantes.getInt("CD_PESSOA"), 
													 rsmSolicitantes.getString("NM_SOLICITANTE")));
			}
			
			Result result = DocumentoServices.update(documento, solicitantes, connect, null);
			if(result.getCode()<0) {
				result.setMessage("Erro ao vincular documento!");
				return result;
			}
			else
				result.setMessage("Documento vinculado com sucesso!");
			
			/*
			 * VINCULO E&L
			 */
			int lgProtocoloEel = ParametroServices.getValorOfParametroAsInteger("LG_PROTOCOLO_EEL", 0, documento.getCdEmpresa(), connect);
			if(lgProtocoloEel==1) {
				String cdDocPrinc = DocumentoDAO.get(documento.getCdDocumentoSuperior(), connect).getNrDocumentoExterno();
				String cdDocSec = documento.getNrDocumentoExterno();
				
				result = EelUtils.vincularDocumentoEel(cdDocPrinc, cdDocSec);
				
				if(result.getCode()<=0) {
					return new Result(-2, "Erro ao vincular adocumentos na E&L.");
				}
			}
			
			return new Result(1, "Documento Vinculado com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeDocumentoVinculado(int cdDocumento) {
		return removeDocumentoVinculado(cdDocumento, null);
	}
	
	public static Result removeDocumentoVinculado(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			Documento documento = DocumentoDAO.get(cdDocumento);
			documento.setCdDocumentoSuperior(0);
			
			ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
			ResultSetMap rsmSolicitantes = DocumentoPessoaServices.getAllByDocumento(cdDocumento);
			while(rsmSolicitantes.next()){
				solicitantes.add(new DocumentoPessoa(rsmSolicitantes.getInt("CD_DOCUMENTO"),
													 rsmSolicitantes.getInt("CD_PESSOA"),
													 rsmSolicitantes.getString("NM_QUALIFICACAO")));
			}
			
			Result result = DocumentoServices.update(documento, solicitantes, connect, null);
			if(result.getCode()<0)
				result.setMessage("Erro ao desvincular documento!");
			else
				result.setMessage("Documento desvinculado com sucesso!");
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDocumentosVinculados(int cdDocumentoSuperior) {
		return getDocumentosVinculados(cdDocumentoSuperior, null);
	}
	
	public static ResultSetMap getDocumentosVinculados(int cdDocumentoSuperior, Connection connection) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_DOCUMENTO_SUPERIOR", Integer.toString(cdDocumentoSuperior), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("lgUltimaTramitacao", Integer.toString(1), ItemComparator.EQUAL, Types.INTEGER));
		
		return DocumentoServices.find(criterios, connection);
	}

	public static ResultSetMap getEstatisticaPod(int cdEmpresa, int dtInicioMes, int dtInicioAno, int dtFinalMes, int dtFinalAno) {
		return getEstatisticaPod(cdEmpresa, dtInicioMes, dtInicioAno, dtFinalMes, dtFinalAno, null);
	}

	public static ResultSetMap getEstatisticaPod(int cdEmpresa, int dtInicioMes, int dtInicioAno, int dtFinalMes, int dtFinalAno, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			GregorianCalendar dtInicial = new GregorianCalendar(dtInicioAno, dtInicioMes, 1);
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = new GregorianCalendar(dtFinalAno, dtFinalMes, 1);
			dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			ResultSetMap rsm = new ResultSetMap();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.TP_SETOR", Integer.toString(SetorServices.TP_SETOR_INTERNO), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmSetores = SetorServices.findCompleto(criterios, connect);
	
			while(rsmSetores.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("CD_SETOR", rsmSetores.getString("CD_SETOR"));
				register.put("NM_SETOR", rsmSetores.getString("NM_SETOR"));
				register.put("SG_SETOR", rsmSetores.getString("SG_SETOR")==null || rsmSetores.getString("SG_SETOR").equals("") ? rsmSetores.getString("NM_SETOR") : rsmSetores.getString("SG_SETOR"));
				
				//QUANTIDADE DE DOCUMENTOS PARADOS
				pstmt = connect.prepareStatement("SELECT count(*) as QT_PARADOS" +
												 " FROM ptc_documento A" +
												 " JOIN ptc_documento_tramitacao B ON (A.cd_documento = B.cd_documento)" +
												 " WHERE A.cd_situacao_documento = ?" +
												 " AND B.cd_tramitacao = (SELECT max(cd_tramitacao) FROM ptc_documento_tramitacao WHERE cd_documento = A.cd_documento)" +
												 " AND A.cd_setor_atual = ?" +
												 " AND B.dt_tramitacao >= '" + Util.convCalendarStringSqlCompleto(dtInicial)+ "'" +
												 " AND B.dt_tramitacao <= '" + Util.convCalendarStringSqlCompleto(dtFinal)  + "'" +
												 " AND A.cd_situacao_documento <> ?");
				pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0, 0, connect));
				pstmt.setInt(2, rsmSetores.getInt("CD_SETOR"));
				pstmt.setInt(3, ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ARQUIVADO", 0, 0, connect));
				ResultSet rs = pstmt.executeQuery();
				
				register.put("QT_PARADOS", rs.next()?rs.getInt("QT_PARADOS"):0);
				
				//QUANTIDADE DE DOCUMENTOS TRAMITADOS
				pstmt = connect.prepareStatement("SELECT count(A.cd_documento) AS QT_TRAMITADOS" +
												 " FROM ptc_documento A " +
												 " JOIN ptc_documento_tramitacao B ON (A.cd_documento = B.cd_documento)" +
												 " WHERE B.cd_tramitacao = (SELECT max(cd_tramitacao) FROM ptc_documento_tramitacao WHERE cd_documento = A.cd_documento)" +
												 " AND ((B.cd_setor_destino = ? AND B.dt_recebimento IS NOT NULL) OR (B.cd_setor_origem = ? AND B.dt_recebimento IS NULL))" +
												 " AND B.dt_tramitacao >= '" + Util.convCalendarStringSqlCompleto(dtInicial)+ "'" +
												 " AND B.dt_tramitacao <= '" + Util.convCalendarStringSqlCompleto(dtFinal)  + "'" +
												 " AND A.cd_situacao_documento <> ?");
				pstmt.setInt(1, rsmSetores.getInt("CD_SETOR"));
				pstmt.setInt(2, rsmSetores.getInt("CD_SETOR"));
				pstmt.setInt(3, ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ARQUIVADO", 0, 0, connect));
				rs = pstmt.executeQuery();
				
				register.put("QT_TRAMITADOS", rs.next()?rs.getInt("QT_TRAMITADOS"):0);
				
				//QUANTIDADE DE DOCUMENTO A RECEBER
				pstmt = connect.prepareStatement("SELECT count(A.cd_documento) AS QT_RECEBER" +
												 " FROM ptc_documento A" +
												 " JOIN ptc_documento_tramitacao B ON (A.cd_documento = B.cd_documento)" +
												 " WHERE B.cd_tramitacao = (SELECT max(cd_tramitacao) FROM ptc_documento_tramitacao WHERE cd_documento = A.cd_documento)" +
												 " AND B.cd_setor_destino = ?" +
												 " AND B.dt_recebimento IS NULL" +
												 " AND B.dt_tramitacao >= '" + Util.convCalendarStringSqlCompleto(dtInicial)+ "'" +
												 " AND B.dt_tramitacao <= '" + Util.convCalendarStringSqlCompleto(dtFinal)  + "'" +
												 " AND A.cd_situacao_documento <> ?");
				pstmt.setInt(1, rsmSetores.getInt("CD_SETOR"));
				pstmt.setInt(2, ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ARQUIVADO", 0, 0, connect));
				rs = pstmt.executeQuery();
				
				register.put("QT_RECEBER", rs.next()?rs.getInt("QT_RECEBER"):0);
				
				rsm.addRegister(register);
				rs.close();
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.getEstatisticaPod: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDetalheEstatisticaPod(int cdSetor, int dtInicioMes, int dtInicioAno, int dtFinalMes, int dtFinalAno, int tpRelatorio) {
		return getDetalheEstatisticaPod(cdSetor, dtInicioMes, dtInicioAno, dtFinalMes, dtFinalAno, tpRelatorio, null);
	}
	
	public static ResultSetMap getDetalheEstatisticaPod(int cdSetor, int dtInicioMes, int dtInicioAno, int dtFinalMes, int dtFinalAno, int tpRelatorio, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt = null;
		ResultSetMap rsm = null;
		try {
			GregorianCalendar dtInicial = new GregorianCalendar(dtInicioAno, dtInicioMes, 1);
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = new GregorianCalendar(dtFinalAno, dtFinalMes, 1);
			dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			String sql = "SELECT A.nr_documento, A.ds_assunto, B.dt_tramitacao, B.dt_recebimento, C.nm_tipo_documento, D.nm_setor as nm_setor_atual, " + 
							 " D.sg_setor as sg_setor_atual,  E.nm_situacao_documento, F.nm_setor as nm_setor_origem, F.sg_setor as sg_setor_origem, " + 
					         " G.nm_setor as nm_setor_destino, G.sg_setor as sg_setor_destino, H.nm_fase, I.nm_setor as nm_setor, I.sg_setor as sg_setor" +
							 " FROM ptc_documento A" +
							 " JOIN ptc_documento_tramitacao B ON (A.cd_documento = B.cd_documento)" +
							 " LEFT OUTER JOIN gpn_tipo_documento C ON (A.cd_tipo_documento = C.cd_tipo_documento)" +
							 " LEFT OUTER JOIN grl_setor D ON (A.cd_setor_atual = D.cd_setor)" +
							 " LEFT OUTER JOIN ptc_situacao_documento E ON (A.cd_situacao_documento = E.cd_situacao_documento)" +
							 " LEFT OUTER JOIN grl_setor F ON (B.cd_setor_origem = F.cd_setor)" +
							 " LEFT OUTER JOIN grl_setor G ON (B.cd_setor_destino = G.cd_setor)" +
							 " LEFT OUTER JOIN ptc_fase H ON (A.cd_fase = H.cd_fase)" +
							 " LEFT OUTER JOIN grl_setor I ON (A.cd_setor = I.cd_setor)";
			
			if(tpRelatorio == 0) { //tramitados
				pstmt = connect.prepareStatement(sql +
						   						 " WHERE B.cd_tramitacao = (SELECT max(cd_tramitacao) FROM ptc_documento_tramitacao WHERE cd_documento = A.cd_documento)" +
												 " AND ((B.cd_setor_destino = ? AND B.dt_recebimento IS NOT NULL) OR (B.cd_setor_origem = ? AND B.dt_recebimento IS NULL))" +
												 " AND B.dt_tramitacao >= '" + Util.convCalendarStringSqlCompleto(dtInicial)+ "'" +
												 " AND B.dt_tramitacao <= '" + Util.convCalendarStringSqlCompleto(dtFinal)  + "'");
				pstmt.setInt(1, cdSetor);
				pstmt.setInt(2, cdSetor);
				
				rsm = new ResultSetMap(pstmt.executeQuery());
			}
			else if(tpRelatorio == 1) { //parados
				pstmt = connect.prepareStatement(sql +
												" WHERE A.cd_situacao_documento = ?" +
												 " AND A.cd_setor_atual = ?" +
												 " AND B.cd_tramitacao = (SELECT max(cd_tramitacao) FROM ptc_documento_tramitacao WHERE cd_documento = A.cd_documento)" +
												 " AND B.dt_tramitacao >= '" + Util.convCalendarStringSqlCompleto(dtInicial)+ "'" +
												 " AND B.dt_tramitacao <= '" + Util.convCalendarStringSqlCompleto(dtFinal)  + "'");
				pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0, 0, connect));
				pstmt.setInt(2, cdSetor);
				
				rsm = new ResultSetMap(pstmt.executeQuery());
			}
			else if(tpRelatorio == 2) {
				pstmt = connect.prepareStatement(sql +
										 " WHERE B.cd_tramitacao = (SELECT max(cd_tramitacao) FROM ptc_documento_tramitacao WHERE cd_documento = A.cd_documento)" +
										 " AND B.cd_setor_destino = ?" +
										 " AND B.dt_recebimento IS NULL" +
										 " AND B.dt_tramitacao >= '" + Util.convCalendarStringSqlCompleto(dtInicial)+ "'" +
										 " AND B.dt_tramitacao <= '" + Util.convCalendarStringSqlCompleto(dtFinal)  + "'");
				pstmt.setInt(1, cdSetor);
				
				rsm = new ResultSetMap(pstmt.executeQuery());
			}
			
			while(rsm.next()) {
				rsm.setValueToField("DS_DT_ENVIO", Util.formatDate(rsm.getTimestamp("dt_tramitacao"), "dd/MM/yyyy"));
				rsm.setValueToField("DS_DT_RECEBIMENTO", Util.formatDate(rsm.getTimestamp("dt_recebimento"), "dd/MM/yyyy"));
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.getDetalheEstatisticaPod: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getForPrint(int cdDocumento) {
		return getForPrint(cdDocumento, null);
	}

	public static ResultSetMap getForPrint(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_documento", Integer.toString(cdDocumento), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = Search.find("SELECT a.*, c.nm_pessoa as nm_usuario, d.nm_tipo_documento, " +
					"J.nm_motivo_trancamento, L.nr_matricula, M.nm_turma, O.nm_produto_servico AS nm_curso, " +
					"(SELECT MAX(I.nm_setor) " +
					" FROM ptc_documento_tramitacao G, grl_setor I " +
					" WHERE G.cd_documento = A.cd_documento " +
					"   AND G.cd_setor_destino = I.cd_setor " +
					"   AND NOT G.cd_setor_destino IS NULL " +
					"   AND G.dt_tramitacao = (SELECT MAX(H.dt_tramitacao) " +
					"						   FROM ptc_documento_tramitacao H " +
					"						   WHERE H.cd_documento = A.cd_documento " +
					"							 AND NOT H.cd_setor_destino IS NULL)) AS nm_setor_atual, " +
					"G.nm_setor " +
					"FROM ptc_documento A " +
					"LEFT OUTER JOIN seg_usuario B ON (A.cd_usuario = B.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
					"LEFT OUTER JOIN gpn_tipo_documento D on (A.cd_tipo_documento = D.cd_tipo_documento) " +
					"LEFT OUTER JOIN grl_setor G ON (A.cd_setor = G.cd_setor) " +
					"LEFT OUTER JOIN adm_contrato_rescisao H ON (A.cd_documento = H.cd_documento) " +
					"LEFT OUTER JOIN acd_matricula_periodo_letivo I ON (H.cd_contrato = I.cd_contrato) " +
					"LEFT OUTER JOIN acd_motivo_trancamento J ON (I.cd_motivo_trancamento = J.cd_motivo_trancamento) " +
					"LEFT OUTER JOIN acd_matricula L ON (I.cd_matricula = L.cd_matricula) " +
					"LEFT OUTER JOIN acd_turma M ON (L.cd_turma = M.cd_turma) " +
					"LEFT OUTER JOIN acd_curso N ON (M.cd_curso = N.cd_curso AND M.cd_instituicao = N.cd_instituicao) " +
					"LEFT OUTER JOIN grl_produto_servico O ON (N.cd_curso = O.cd_produto_servico) " +
					"WHERE 1 = 1 ",
			criterios, connect!=null ? connect : connect, false);

			rsm.beforeFirst();
			while (rsm.next()) {
				rsm.getRegister().remove("rsmSolicitantes");
				PreparedStatement pstmt = connect.prepareStatement(
						"SELECT A.*, B.nm_pessoa, B.gn_pessoa, " +
						"       C.nr_cpf, C.nr_rg, D.nr_cnpj, B.nr_telefone1, B.nr_telefone2, B.nr_celular, B.nr_fax " +
						"FROM ptc_documento_pessoa A " +
						"JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " +
						"LEFT OUTER JOIN grl_pessoa_fisica C ON (B.cd_pessoa = C.cd_pessoa) " +
						"LEFT OUTER JOIN grl_pessoa_juridica D ON (B.cd_pessoa = D.cd_pessoa) " +
						"WHERE A.cd_documento = ?");
				pstmt.setInt(1, rsm.getInt("cd_documento"));
				ResultSet rs = pstmt.executeQuery();
				String nmPessoa = "", nrCpf = "", nrCnpj = "", nrTelefone = "", nrRg = "";
				while (rs.next()) {
					nmPessoa += (nmPessoa.trim().equals("") ? "" : "; ") + rs.getString("nm_pessoa");
					nrCpf    += rs.getInt("gn_pessoa")!=PessoaServices.TP_FISICA ? "" : (nrCpf.trim().equals("") ? "" : "; ") + Util.formatCpf(rs.getString("nr_cpf"), rs.getString("nr_cpf"));
					nrCnpj   += rs.getInt("gn_pessoa")!=PessoaServices.TP_JURIDICA ? "" : (nrCnpj.trim().equals("") ? "" : "; ") + Util.formatCnpj(rs.getString("nr_cnpj"), rs.getString("nr_cnpj"));
					nrRg     += rs.getInt("gn_pessoa")!=PessoaServices.TP_FISICA ? "" : (nrRg.trim().equals("") ? "" : "; ") + rs.getString("nr_rg");
					String nrTelefoneTemp = "";
					String[] telefones = {rs.getString("nr_telefone1"), rs.getString("nr_telefone2"), rs.getString("nr_celular"),
										  rs.getString("nr_fax")};
					for (int i=0; i<telefones.length; i++)
						if (telefones[i]!=null && !telefones[i].trim().equals(""))
							nrTelefoneTemp += (nrTelefoneTemp.trim().equals("") ? "" : "; ") + telefones[i];
					nrTelefone += !nrTelefoneTemp.equals("") ? ((nrTelefone.trim().equals("") ? "" : "; ") + nrTelefoneTemp) : "";
				}
				rsm.getRegister().put("NR_CPFS", nrCpf);
				rsm.getRegister().put("NR_CNPJS", nrCnpj);
				rsm.getRegister().put("NR_TELEFONES", nrTelefone);
				rsm.getRegister().put("NM_SOLICITANTES", nmPessoa);
				rsm.getRegister().put("NR_RGS", nrRg);
			}

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

	public static int inserirAnexo(int cdDocumento, int cdAnexo, GregorianCalendar dtAnexacao, int tpAnexo) {
		return inserirAnexo(cdDocumento, cdAnexo, dtAnexacao, tpAnexo, null);
	}

	public static int inserirAnexo(int cdDocumento, int cdAnexo,
			GregorianCalendar dtAnexacao, int cdTipoAnexo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
				PreparedStatement pstmt = connect.prepareStatement(
						"INSERT INTO ptc_anexo(cd_documento, cd_anexo, dt_anexacao, cd_tipo_anexo)" +
						" VALUES(?,?,?,?)");
				pstmt.setInt(1, cdDocumento);
				pstmt.setInt(2, cdAnexo);
				pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtAnexacao));
				pstmt.setInt(4, cdTipoAnexo);

				pstmt.executeUpdate();

				if (isConnectionNull)
					connect.commit();
				return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int removerAnexo(int cdDocumento, int cdAnexo) {
		return removerAnexo(cdDocumento, cdAnexo, null);
	}

	public static int removerAnexo(int cdDocumento, int cdAnexo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_anexo WHERE cd_documento = ? AND cd_anexo = ?");
				pstmt.setInt(1, cdDocumento);
				pstmt.setInt(2, cdAnexo);

				pstmt.executeUpdate();

				if (isConnectionNull)
					connect.commit();
				return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAnexos(int cdDocumento) {
		return getAnexos(cdDocumento, null);
	}

	public static ResultSetMap getAnexos(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		ResultSetMap rsm = null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("a.cd_documento",String.valueOf(cdDocumento),ItemComparator.EQUAL, Types.INTEGER ));
			rsm = Search.find("SELECT b.*, a.cd_documento, a.cd_anexo, a.dt_anexacao, c.nm_tipo_anexo" +
							  " FROM ptc_anexo a" +
							  " INNER JOIN ptc_documento b ON (a.cd_anexo = b.cd_documento)" +
							  " INNER JOIN ptc_tipo_anexo c ON (a.cd_tipo_anexo = c.cd_tipo_anexo)", criterios, connect!=null ? connect : connect, false);

			while(rsm.next()) {
				int situacaoDocumento = getSituacaoDocumento(rsm.getInt("a.cd_documento", 0), connect);
				SituacaoDocumento sDocumento = SituacaoDocumentoDAO.get(situacaoDocumento, connect);
				rsm.setValueToField("CD_SITUACAO_DOCUMENTO", situacaoDocumento);
				rsm.setValueToField("NM_SITUACAO_DOCUMENTO", sDocumento == null ? "Em Aberto" : sDocumento.getNmSituacaoDocumento());
			}

			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getDocumentosNaoAnexados(int cdDocumento) {
		return getDocumentosNaoAnexados(cdDocumento, null);
	}

	public static ResultSetMap getDocumentosNaoAnexados(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			return new ResultSetMap(connect.prepareStatement("SELECT * FROM ptc_documento" +
															 "WHERE cd_documento <> "+cdDocumento+
															 "  AND cd_documento NOT IN " +
															 "          (SELECT cd_anexo FROM ptc_anexo WHERE cd_documento = "+cdDocumento+")").executeQuery());

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

	
	public static ResultSetMap getArquivos(int cdDocumento){
		return getArquivos(cdDocumento, null);
	}
	
	public static ResultSetMap getArquivos(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
		
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.cd_arquivo, A.cd_documento, A.nm_arquivo, A.nm_documento, " +
					"A.dt_arquivamento, A.lg_comprimido, A.cd_tipo_documento, A.cd_assinatura, " +
					"B.nr_documento, "+
					" C.nm_tipo_documento "+
	                "FROM ptc_documento_arquivo A "+
	                "LEFT OUTER JOIN ptc_documento           B ON (A.cd_documento = B.cd_documento) " +
	                "LEFT OUTER JOIN gpn_tipo_documento C ON (A.cd_tipo_documento = C.cd_tipo_documento) " +
                    "WHERE A.cd_documento = "+cdDocumento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! DocumentoServices.getArquivos: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static int getSituacaoDocumento(int cdDocumento) {
		return getSituacaoDocumento(cdDocumento, null);
	}

	public static int getSituacaoDocumento(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			int situacaoDocumento = 0;
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT cd_situacao_documento FROM ptc_documento " +
					"WHERE cd_documento = "+cdDocumento);
			ResultSet rs = pstmt.executeQuery();
			situacaoDocumento = rs.next() ? rs.getInt(1) : situacaoDocumento;

			return situacaoDocumento;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result checkProcessoProtocolado(int cdProcesso) {
		return checkProcessoProtocolado(cdProcesso, null);
	}

	public static Result checkProcessoProtocolado(int cdProcesso, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT cd_documento FROM ptc_documento " +
					"WHERE cd_processo = "+cdProcesso);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.CD_DOCUMENTO", Integer.toString(rs.getInt("cd_documento")), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmDocumento = find(criterios, connect);
				
				//Documento documento = DocumentoDAO.get(rs.getInt("cd_documento"), connect);
				rsmDocumento.next();
				return new Result(1, "Processo protocolado.", "REG_DOCUMENTO", rsmDocumento.getRegister());
			}
			else {
				return new Result(0, "Processo ainda não protocolado.");
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao checar processo");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSolicitacao(int cdPessoa, GregorianCalendar dtSolicitacao) {
		return getSolicitacao(cdPessoa, dtSolicitacao, null);
	}

	public static ResultSetMap getSolicitacao(int cdPessoa, GregorianCalendar dtSolicitacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmt = null;
			ResultSetMap rsm = null;
			pstmt = connect.prepareStatement(
					"SELECT DISTINCT a.cd_tipo_documento, a1.nm_tipo_documento, b.cd_documento, " +
					"b.dt_protocolo, b.cd_pessoa, d.nm_pessoa, e.cd_usuario, f.nm_pessoa AS nm_usuario " +
					"FROM ptc_tipo_documento a " +
					" JOIN gpn_tipo_documento a1 on (a.cd_tipo_documento=a1.cd_tipo_documento) "+
					" LEFT JOIN (SELECT x.cd_usuario, x.cd_tipo_documento, x.cd_documento, x.dt_protocolo, " +
					" 			y.cd_pessoa FROM ptc_documento x " +
					"			INNER JOIN ptc_documento_pessoa y ON (x.cd_documento = y.cd_documento) " +
					"			WHERE x.dt_protocolo = ? AND y.cd_pessoa = ?) b ON (a.cd_tipo_documento = b.cd_tipo_documento) " +
					"LEFT JOIN grl_pessoa d ON (b.cd_pessoa = d.cd_pessoa) " +
					"LEFT JOIN seg_usuario e ON (b.cd_usuario = e.cd_usuario) " +
					"LEFT JOIN grl_pessoa f ON (e.cd_pessoa = f.cd_pessoa) " +
					"ORDER BY a1.nm_tipo_documento");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtSolicitacao));
			pstmt.setInt(2, cdPessoa);
			rsm = new ResultSetMap(pstmt.executeQuery());

			while(rsm.next()) {
				int cdDocumento = rsm.getInt("cd_documento");
				int lgSolicitacao = cdDocumento == 0 ? 0 : 1;
				rsm.setValueToField("LG_SOLICITACAO", lgSolicitacao);

				int cdSituacaoDocumento = getSituacaoDocumento(cdDocumento, connect);
				SituacaoDocumento situacaoDocumento = SituacaoDocumentoDAO.get(cdSituacaoDocumento, connect);
				String nmSituacaoDocumento = null;
				String nmSetorOrigem = null;
				String nmSetorDestino = null;
				if (cdDocumento > 0) {
					Documento documento = DocumentoDAO.get(cdDocumento);
					Setor setorOrigem = SetorDAO.get(documento.getCdSetor());
					ResultSetMap rsmUltimaTramitacao = DocumentoTramitacaoServices.getUltimaTramitacaoAsResultSetMap(cdDocumento, connect);
					nmSituacaoDocumento = situacaoDocumento != null ? situacaoDocumento.getNmSituacaoDocumento() : "Em aberto";
					nmSetorOrigem = setorOrigem != null ? setorOrigem.getNmSetor() : "";
					nmSetorDestino = rsmUltimaTramitacao.next() ? rsm.getString("nm_setor_destino") : nmSetorOrigem;
				}
				rsm.setValueToField("CD_SITUACAO_DOCUMENTO", cdSituacaoDocumento);
				rsm.setValueToField("NM_SITUACAO_DOCUMENTO", nmSituacaoDocumento);
				rsm.setValueToField("NM_SETOR_ORIGEM", nmSetorOrigem);
				rsm.setValueToField("NM_SETOR_DESTINO", nmSetorDestino);
			}

			if (isConnectionNull)
				connect.commit();

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

	public static ResultSetMap findSolicitacoes(ArrayList<ItemComparator> criterios) {
		return findSolicitacoes(criterios, null);
	}

	public static ResultSetMap findSolicitacoes(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			return Search.find("SELECT DISTINCT a.cd_pessoa, b.nm_pessoa, c.dt_protocolo," +
					"c.cd_usuario, e.nm_pessoa AS nm_usuario" +
					"FROM ptc_documento_pessoa a" +
					"     INNER JOIN grl_pessoa b ON (a.cd_pessoa = b.cd_pessoa)" +
					"     INNER JOIN ptc_documento c ON (a.cd_documento = c.cd_documento)" +
					"     LEFT JOIN seg_usuario d ON (c.cd_usuario = d.cd_usuario)" +
					"     LEFT JOIN grl_pessoa e ON (d.cd_pessoa = e.cd_pessoa)", " ORDER BY dt_protocolo, B.nm_pessoa", criterios,
					connect, isConnectionNull);
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

	public static int insertSolicitacao (int cdSolicitante, GregorianCalendar dtSolicitacao, String txtObservacao, int cdEmpresa, int cdUsuario, ArrayList<Integer> cdTipoDocumento) {
		return insertSolicitacao(cdSolicitante, dtSolicitacao, txtObservacao, cdEmpresa, cdUsuario, cdTipoDocumento, null).getCode();
	}

	public static Result insertSolicitacao (int cdSolicitante, GregorianCalendar dtSolicitacao, String txtObservacao, int cdEmpresa, int cdUsuario,
			ArrayList<Integer> cdTipoDocumento, boolean returnCodeDocs) {
		return insertSolicitacao(cdSolicitante, dtSolicitacao, txtObservacao, cdEmpresa, cdUsuario, cdTipoDocumento, null);
	}

	public static Result insertSolicitacao (int cdSolicitante, GregorianCalendar dtSolicitacao, String txtObservacao, int cdEmpresa, int cdUsuario,
			ArrayList<Integer> cdTipoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documento_pessoa(cd_documento,cd_pessoa) VALUES(?,?)");

			PreparedStatement pstmtTemp = connect.prepareStatement(
					"SELECT * FROM ptc_documento_pessoa a" +
					"INNER JOIN ptc_documento b ON (a.cd_documento = b.cd_documento)" +
					"WHERE A.cd_pessoa         = " +cdSolicitante+
					"  AND b.dt_protocolo      = ? " +
					"  AND b.cd_tipo_documento = ?");
			pstmtTemp.setTimestamp(1, Util.convCalendarToTimestamp(dtSolicitacao));

			ArrayList<Integer> documentos = new ArrayList<Integer>();
			for (int cdTipoDocumentoTemp : cdTipoDocumento) {
				pstmtTemp.setInt(2, cdTipoDocumentoTemp);
				ResultSet rsTemp = pstmtTemp.executeQuery();
				if(rsTemp.next())
					continue;

				Documento documento = new Documento(0 /*cdDocumento*/, 0 /*cdArquivo*/, 0 /*cdSetor*/, cdUsuario, "" /*nmLocalOrigem*/,
													dtSolicitacao /*dtProtocolo*/, TP_PUBLICO /*tpDocumento*/, txtObservacao,
													"" /*idDocumento*/, "" /*nrDocumento*/, cdTipoDocumentoTemp, 0 /*cdServico*/,
													0 /*cdAtendimento*/, "" /*txtDocumento*/,0/*cdSetorAtual*/,0/*cdSituacaoDocumento*/,0/*cdFase*/,
													cdEmpresa, 0 /*cdProcesso*/, 0 /*tpPrioridade*/, 0 /*cdDocumentoSuperior*/, null /*dsAssunto*/, 
													null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, null/*nrDocumentoExterno*/,
													null/*nrAssunto*/, null, null, 0, 1);

				ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
				solicitantes.add(new DocumentoPessoa(0 /*cdDocumento*/, cdSolicitante, "Solicitante"));
				Result result = insert(documento, solicitantes, connect, null);
				if (result.getCode() <=0 )	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
				else
					documentos.add(result.getCode());
				pstmt.setInt(1, result.getCode()); // Código do documento
				pstmt.setInt(2, cdSolicitante);
				pstmt.executeUpdate();
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Solicitação inserida com sucesso!", "documentos", documentos);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar inserir solicitação!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int updateSolicitacao(int cdSolicitante, GregorianCalendar dtSolicitacao, String txtObservacao, int cdEmpresa, int cdUsuario, ArrayList<Integer> cdDocumento) {
		return updateSolicitacao(cdSolicitante, dtSolicitacao, txtObservacao, cdEmpresa, cdUsuario, cdDocumento, null);
	}

	public static int updateSolicitacao(int cdSolicitante, GregorianCalendar dtSolicitacao, String txtObservacao, int cdEmpresa, int cdUsuario, ArrayList<Integer> cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			String notIn = "";
			for (int i = 0; i < cdDocumento.size(); i++) {
				if(i == 0)
					notIn = " AND a.cd_documento NOT IN (";

				notIn += cdDocumento.get(i);
				if(i != cdDocumento.size() - 1)
					notIn += ",";
				else
					notIn += ")";
			}

			//Documentos da solicitação que não estão no ArrayList(estes documentos serão removidos)
			PreparedStatement pstmtQuery = connect.prepareStatement(
					"SELECT * FROM ptc_documento" +
					" WHERE cd_documento IN (SELECT a.cd_documento FROM ptc_documento a" +
					" INNER JOIN ptc_documento_pessoa b ON (a.cd_documento = b.cd_documento)" +
					" WHERE b.cd_pessoa = ? AND a.dt_protocolo = ? " + notIn + ")");
			pstmtQuery.setInt(1, cdSolicitante);
			pstmtQuery.setTimestamp(2, Util.convCalendarToTimestamp(dtSolicitacao));
			ResultSet rsQuery = pstmtQuery.executeQuery();
			PreparedStatement pstmtDelete = connect.prepareStatement(
					"DELETE FROM ptc_documento_pessoa" +
					" WHERE cd_pessoa = ? AND cd_documento = ?");
			pstmtDelete.setInt(1, cdSolicitante);
			while(rsQuery.next()) {
				int cdDocumentoAux = rsQuery.getInt("cd_documento");
				pstmtDelete.setInt(2, cdDocumentoAux);
				pstmtDelete.executeUpdate();
				DocumentoDAO.delete(cdDocumentoAux,connect);
			}

			//verifica se já existe tipo de documento, caso negativo, um novo documento do tipo verificado serão criado
			PreparedStatement pstmtExiste = connect.prepareStatement(
					"SELECT a.cd_tipo_documento" +
					" FROM ptc_documento a" +
					" INNER JOIN ptc_documento_pessoa b ON (a.cd_documento = b.cd_documento)" +
					" WHERE a.dt_protocolo = ? AND a.cd_tipo_documento = ?");
			pstmtExiste.setTimestamp(1, Util.convCalendarToTimestamp(dtSolicitacao));
			ArrayList<Integer> cdTipoDocumentoToInsert = new ArrayList<Integer>();
			for(int cdDocumentoTemp : cdDocumento) {
				int cdTipoDocumento = DocumentoDAO.get(cdDocumentoTemp, connect).getCdTipoDocumento();
				pstmtExiste.setInt(2, cdTipoDocumento);
				ResultSet rs = pstmtExiste.executeQuery();
				if (!rs.next()) {
					cdTipoDocumentoToInsert.add(cdTipoDocumento);
				}
			}

			if (insertSolicitacao(cdSolicitante, dtSolicitacao, txtObservacao, cdEmpresa, cdUsuario, cdTipoDocumentoToInsert, connect).getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteSolicitacao (int cdSolicitante, GregorianCalendar dtSolicitacao) {
		return deleteSolicitacao(cdSolicitante, dtSolicitacao, null);
	}

	public static int deleteSolicitacao (int cdSolicitante, GregorianCalendar dtSolicitacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmtTemp = connect.prepareStatement(
					" SELECT A.cd_documento " +
					" FROM ptc_documento A " +
					" INNER JOIN ptc_documento_pessoa B ON (A.cd_documento = B.cd_documento) " +
					" WHERE A.dt_protocolo = ? AND B.cd_pessoa = ?");
			pstmtTemp.setTimestamp(1, Util.convCalendarToTimestamp(dtSolicitacao));
			pstmtTemp.setInt(2, cdSolicitante);
			ResultSet rsTemp = pstmtTemp.executeQuery();

			PreparedStatement pstmtDelete = connect.prepareStatement(
					" DELETE FROM ptc_documento_pessoa" +
					" WHERE cd_pessoa = ? AND cd_documento = ?");
			pstmtDelete.setInt(1, cdSolicitante);

			while (rsTemp.next()) {
				int cdDocumento = rsTemp.getInt("CD_DOCUMENTO");
				pstmtDelete.setInt(2, cdDocumento);
				pstmtDelete.executeUpdate();
				DocumentoDAO.delete(cdDocumento, connect);
			}

			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getDocumentoAsResultSetMap (int cdDocumento) {
		return getDocumentoAsResultSetMap(cdDocumento, null);
	}

	public static ResultSetMap getDocumentoAsResultSetMap(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			ResultSetMap rsm = new ResultSetMap();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_DOCUMENTO", String.valueOf(cdDocumento), ItemComparator.EQUAL, Types.INTEGER));
			rsm = find(criterios, connect);

			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String, Object> getDocumentoRegister(int cdDocumento) {
		return getDocumentoRegister(cdDocumento, null);
	}

	public static HashMap<String, Object> getDocumentoRegister(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_DOCUMENTO", String.valueOf(cdDocumento), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
					
			if(rsm.next()) {
				return rsm.getRegister();
			}
			else return null;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result insert(Documento documento, ArrayList<DocumentoPessoa> solicitantes, ArrayList<FormularioAtributoValor> atributos) {
		return insert(documento, solicitantes, null,atributos);
	}

	public static sol.util.Result insert(Documento documento, ArrayList<DocumentoPessoa> solicitantes) {
		return insert(documento, solicitantes, null, null);
	}

	public static sol.util.Result insert(Documento documento, ArrayList<DocumentoPessoa> solicitantes, Connection connect, ArrayList<FormularioAtributoValor> atributos) {
		boolean isConnectionNull = connect==null;
		
		try {
			//
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			int lgProtocoloEel = ParametroServices.getValorOfParametroAsInteger("LG_PROTOCOLO_EEL", 0, documento.getCdEmpresa(), connect);
			
			// Verificando situação inicial
			int cdSituacaoDocumento = 0;
			if (documento.getCdSituacaoDocumento() > 0)
				cdSituacaoDocumento = documento.getCdSituacaoDocumento();
			else
				cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0, 0, connect);
			/* em caso negativo, persiste um novo registro e parametriza-o como situação para "em aberto" */
			if (cdSituacaoDocumento <= 0)
				return new Result(-7, "Situação padrão para documento em aberto não localizado!");
			documento.setCdSituacaoDocumento(cdSituacaoDocumento);
			
			
			
			// Numeração de Procotolo
			if((documento.getNrDocumento()==null || documento.getNrDocumento().equals(""))) { 
				String nrDocumento = TipoDocumentoServices.getNextNumeracao(documento.getCdTipoDocumento(), documento.getCdEmpresa(), true, connect);
				documento.setNrDocumento(nrDocumento);
			}
			
			// Previne duplicidade em Numeração
			boolean lgPermitirNumeracaoAnaloga = false;
			ArrayList<Object> tiposDocumento = ParametroServices.getValoresOfParametroAsArrayList("CD_TIPO_DOCUMENTO_NUMERACAO_ANALOGA", 0, connect);
			
			if(tiposDocumento != null) {
				for (Object cdTipoDocumento : tiposDocumento) {
					if((int)cdTipoDocumento == documento.getCdTipoDocumento()) {
						lgPermitirNumeracaoAnaloga = true;
						break;
					}
				}
			}
			
			if (!lgPermitirNumeracaoAnaloga) {
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				if(lgProtocoloEel==1 && documento.getNrDocumentoExterno()!=null) {
					criterios.add(new ItemComparator("NR_DOCUMENTO_EXTERNO", documento.getNrDocumentoExterno(), ItemComparator.EQUAL, Types.VARCHAR));
				} else {
					criterios.add(new ItemComparator("NR_DOCUMENTO", documento.getNrDocumento().trim(), ItemComparator.LIKE, Types.VARCHAR));
					criterios.add(new ItemComparator("CD_TIPO_DOCUMENTO", Integer.toString(documento.getCdTipoDocumento()), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("CD_SETOR", Integer.toString(documento.getCdSetor()), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("CD_EMPRESA", Integer.toString(documento.getCdEmpresa()), ItemComparator.EQUAL, Types.INTEGER));
				}
				
				ResultSetMap rsm = DocumentoDAO.find(criterios, connect);
				if(rsm.next()) {
					LogUtils.debug("DocumentoServices.insert");
					LogUtils.debug("\tDocumento já existe.");
					if(lgProtocoloEel==1)
						LogUtils.debug("\t[E&L]pr_protocolo.controle: "+documento.getNrDocumentoExterno());

					// NAO MUDAR O CODIGO DE ERRO!
					return new Result(-6, "Documento já existe!", "CD_DOCUMENTO", rsm.getInt("cd_documento"));
				}
			}
			
			
			// Setor atual
			if(documento.getCdSetor()>0 && documento.getCdSetorAtual()<=0)
				documento.setCdSetorAtual(documento.getCdSetor());
			
			// INCLUSÃO
			documento.setCdDocumento(DocumentoDAO.insert(documento, connect));
			if(documento.getCdDocumento() < 1) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-5, "Erro ao tentar inserir DOCUMENTO ["+documento.getCdDocumento()+"]");
			}

			/*
			 * INSERE SOLICITANTES
			 */
			for (int i=0; solicitantes!=null && i<solicitantes.size(); i++) {
				if(solicitantes.get(i).getCdDocumento()<=0)
					solicitantes.get(i).setCdDocumento(documento.getCdDocumento());
				int result = DocumentoPessoaDAO.insert(solicitantes.get(i), connect);
				if (result <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-4, "Erro ao tentar inserir SOLICITANTES NO DOCUMENTO");
				}
			}
			
			
			/*
			 * INSERE ATRIBUTOS DINAMICOS
			 */
			if (atributos != null) {
				/* persiste os atributos personalizados do documento */
				for (int i=0; i<atributos.size(); i++) {
					FormularioAtributoValor atributo = atributos.get(i);
					atributo.setCdDocumento(documento.getCdDocumento());
					if (FormularioAtributoValorDAO.insert(atributo, connect) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-3, "Erro ao tentar salvar atributos dinamicos do documento!");
					}
				}
			}
			/*
			 * INSERE MOVIMENTAÇÃO PRA DEFINIR SETOR ATUAL E situação - DOCUMENTO EM ABERTO
			 */
			/* registra a tramitacao do documento para o usuário destino */
			if(lgProtocoloEel==0) {
				DocumentoTramitacao tramitacao = new DocumentoTramitacao(0, documento.getCdDocumento(), documento.getCdSetorAtual() /*cdSetorDestino*/,
                        documento.getCdUsuario(),
                        documento.getCdSetor() /*cdSetorOrigem*/, documento.getCdUsuario() /*cdUsuarioOrigem*/,
						 new GregorianCalendar() /*dtTramitacao*/, new GregorianCalendar() /*dtRecebimento*/,
						 "" /*nmLocalDestino*/, "Registro do Documento",
						 documento.getCdSituacaoDocumento(), documento.getCdFase(), null/*nmLocalOrigem*/, null/*nrRemessa*/);

						int retTramicacao = DocumentoTramitacaoDAO.insert(tramitacao, connect);
						if (retTramicacao <= 0) {
							if (isConnectionNull)
							Conexao.rollback(connect);
							return new Result(-2, "Erro ao tentar incluir tramitação ["+retTramicacao+"]!");
						}
			}
			
			
			
			/*
			 *  INSERE OCORRENCIA INICIAL NO DOCUMENTO
			 */
			int cdOcorrenciaInicial = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_INICIAL", 0, 0, connect);
			if(cdOcorrenciaInicial>0) {
				DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrencia(documento.getCdDocumento(), 
						cdOcorrenciaInicial, 
						0/*cdOcorrencia*/, 
						documento.getCdUsuario(), 
						new GregorianCalendar(), 
						"Documento cadastrado no sistema."/*txtOcorrencia*/, 
						DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO/*tpVisibilidade*/, 0);
				
				Result r = DocumentoOcorrenciaServices.save(documentoOcorrencia, connect);
				if(r.getCode()<0) {
					return new Result(-8, r.getMessage());
				}
			}
			//Ocorrência do protocolo do processo
			if(documento.getCdProcesso()>0) {
				int cdOcorrenciaProtocoloProcesso = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_PROTOCOLO_PROCESSO", 0, 0, connect);
				if(cdOcorrenciaProtocoloProcesso>0) {
					DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrencia(documento.getCdDocumento(), 
							cdOcorrenciaProtocoloProcesso, 
							0/*cdOcorrencia*/, 
							documento.getCdUsuario(), 
							new GregorianCalendar(), 
							documento.getDsAssunto()/*txtOcorrencia*/, 
							DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO/*tpVisibilidade*/, 0);
					
					Result r = DocumentoOcorrenciaServices.save(documentoOcorrencia, connect);
					if(r.getCode()<0) {
						return new Result(-9, r.getMessage());
					}
				}
			}
			
			//
			if (isConnectionNull)
				connect.commit();
			
			Setor setor = SetorDAO.get(documento.getCdSetorAtual(), connect);
			SituacaoDocumento situacao = SituacaoDocumentoDAO.get(documento.getCdSituacaoDocumento(), connect);
			
			Result result = new Result(documento.getCdDocumento());
			result.addObject("documento", documento);
			result.addObject("setor", setor);
			result.addObject("situacao", situacao);
			
			return result;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir documento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result update(Documento documento, ArrayList<DocumentoPessoa> solicitantes, ArrayList<FormularioAtributoValor> atributos) {
		return update(documento, solicitantes, null, atributos);
	}

	public static Result update(Documento documento, ArrayList<DocumentoPessoa> solicitantes, Connection connect, ArrayList<FormularioAtributoValor> atributos) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
	
			if (DocumentoDAO.update(documento, connect) < 1) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar atualizar documento!");
			}

			connect.prepareStatement("DELETE FROM ptc_documento_pessoa " +
					                 "WHERE cd_documento = "+documento.getCdDocumento()).execute();

			for (int i=0; solicitantes!=null && i<solicitantes.size(); i++) {
				solicitantes.get(i).setCdDocumento(documento.getCdDocumento());
				if (DocumentoPessoaDAO.insert(solicitantes.get(i), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao tentar atualizar solicitantes do documento!");
				}
			}

			
			if(atributos!=null) {
				connect.prepareStatement("DELETE FROM grl_formulario_atributo_valor " +
						                 "WHERE cd_documento = "+documento.getCdDocumento()).execute();
	
				for (int i=0; i<atributos.size(); i++) {
					FormularioAtributoValor atributo = (FormularioAtributoValor)atributos.get(i);
					atributo.setCdDocumento(documento.getCdDocumento());
					if (FormularioAtributoValorDAO.insert(atributo, connect) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao tentar atualizar dados dinamicos do documento!");
						}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			Setor setor = SetorDAO.get(documento.getCdSetorAtual(), connect);
			SituacaoDocumento situacao = SituacaoDocumentoDAO.get(documento.getCdSituacaoDocumento(), connect);
			
			Result result = new Result(1);
			result.addObject("documento", documento);
			result.addObject("setor", setor);
			result.addObject("situacao", situacao);
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar atualizar documento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findDocumentosSolicitados(ArrayList<ItemComparator> criterios) {
		return findDocumentosSolicitados(criterios, null);
	}

	public static ResultSetMap findDocumentosSolicitados(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			String sql1 =
				" SELECT B.nm_pessoa AS nm_solicitante, C.*, D.cd_tramitacao, D.cd_setor_destino, D.cd_usuario_destino, D.cd_setor_origem," +
				"        D.cd_usuario_origem, D.dt_tramitacao,  D.dt_recebimento, D.nm_local_destino, D.txt_observacao, D.txt_tramitacao," +
				"        D.lg_recebido, D.cd_situacao_documento, F.nm_pessoa AS nm_usuario, G.nm_tipo_documento, H.nm_situacao_documento, I.nm_setor " +
				" FROM ptc_documento_pessoa A" +
				" JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)" +
				" JOIN ptc_documento C ON (A.cd_documento = C.cd_documento)" +
				" LEFT OUTER JOIN ptc_documento_tramitacao D ON (C.cd_documento = D.cd_documento)" +
				" LEFT OUTER JOIN seg_usuario              E ON (C.cd_usuario = E.cd_usuario)" +
				" LEFT OUTER JOIN grl_pessoa               F ON (E.cd_pessoa = F.cd_pessoa)" +
				" LEFT OUTER JOIN gpn_tipo_documento AS G ON (C.cd_tipo_documento = G.cd_tipo_documento)" +
				" LEFT OUTER JOIN ptc_situacao_documento H ON (D.cd_situacao_documento = H.cd_situacao_documento)" +
				" LEFT OUTER JOIN grl_setor I ON (D.cd_setor_destino = I.cd_setor)" +
				" WHERE (D.dt_tramitacao IS NULL OR D.dt_tramitacao =  (SELECT max(X.dt_tramitacao) FROM ptc_documento_tramitacao X" +
				"                                                       WHERE X.cd_documento = D.cd_documento)) " +
				"   AND (D.cd_tramitacao IS NULL " +
				"     OR D.cd_tramitacao = (SELECT X.cd_tramitacao FROM ptc_documento_tramitacao X " +
				"                                     WHERE X.cd_documento = D.cd_documento " +
				"                                       AND X.dt_tramitacao = D.dt_tramitacao))";

			//não funciona com este metodo
			ResultSetMap rsm =Search.find(sql1, criterios, connect, false);


			ArrayList<String> fields = new ArrayList<String>();
			fields.add("dt_protocolo DESC");
			fields.add("nm_solicitante");
			fields.add("nm_tipo_documento");
			rsm.orderBy(fields);
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

	public static sol.util.Result setNovaFase(int cdDocumento,int cdFase,int cdUsuario,String txtOcorrencia)	{
		Connection connect = Conexao.conectar();
		try {
			if(cdUsuario <= 0)
				return new Result(-1, "Informação do usuário não encontrada!");
			if(cdFase <= 0)
				return new Result(-1, "Nova fase deve ser informada!");
			// Verifica se já estão em tramitação
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_documento_tramitacao A " +
					                                "WHERE A.cd_documento = " +cdDocumento+
					   								"  AND dt_recebimento IS NULL").executeQuery();
			if(rs.next())
				return new Result(-1, "O documento foi enviado do seu setor para outro setor, ou seja, o documento estão tramitando, " +
						              "a fase só poderão ser mudada após o setor de destino realizar o recebimento!");
			// Verifica se a nova fase é igual a atual
			rs = connect.prepareStatement("SELECT A.cd_fase, nm_fase " +
					                      "FROM ptc_documento A " +
					                      "LEFT OUTER JOIN ptc_fase B ON (A.cd_fase = B.cd_fase) " +
					                      "WHERE A.cd_documento = "+cdDocumento).executeQuery();
			if(!rs.next())
				return new Result(-1, "Documento não localizado!");
			if(rs.getInt("cd_fase")==cdFase)
				return new Result(-1, "Fase atual igual a nova fase, você deve informar uma fase diferente!");
			Fase novaFase = FaseDAO.get(cdFase, connect);
			txtOcorrencia = "Fase Anterior: "+(rs.getString("nm_fase")==null?" Sem Fase " : rs.getString("nm_fase"))+" -> "+
			                "Nova Fase: "+novaFase.getNmFase()+"\n"+
							txtOcorrencia;
			//
			connect.setAutoCommit(false);
			//
			int cdTipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_FASE", 0, 0, connect);
			DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia(cdDocumento,cdTipoOcorrencia,0,cdUsuario,new GregorianCalendar(),
																	 txtOcorrencia,1, 0);
			Result result = insertOcorrencia(ocorrencia, connect);
			if(result.getCode() <= 0)	{
				Conexao.rollback(connect);
				return result;
			}
			//
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento SET cd_fase = "+cdFase+
					                                           " WHERE cd_documento      = "+cdDocumento);
			result = new Result(pstmt.executeUpdate());
			connect.commit();

			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar gravar nova fase do Documento!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static sol.util.Result receberDocumentos(ArrayList<String> documentos, int cdSetor, int cdUsuario)	{
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			Result result = new Result(0);
			
			//Documento doc = DocumentoDAO.get(Integer.valueOf(documentos.get(0)), connect);
			//int lgProtocoloEel = ParametroServices.getValorOfParametroAsInteger("LG_PROTOCOLO_EEL", 0, doc.getCdEmpresa(), connect);
			//doc = null;
			
			for(int i=0; i<documentos.size(); i++)	{
							
				Result rTemp = receberDocumento(Integer.valueOf(documentos.get(i)), 0, cdSetor, cdUsuario, connect);
				if(rTemp.getCode() <= 0)	{
					Conexao.rollback(connect);
					return rTemp;
				}
				//
				result.setCode(result.getCode() + 1);
			}
			connect.commit();
			result.setMessage("Documento(s) recebido(s) com sucesso!");
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.receberDocumento: " +  e);
			return new Result(-1, "Erro ao tentar receber documento!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static sol.util.Result receberDocumento(int cdDocumento, int cdTramitacao, int cdSetor, int cdUsuario)	{
		return receberDocumento(cdDocumento, cdTramitacao, cdSetor, cdUsuario, null);
	}
	public static sol.util.Result receberDocumento(int cdDocumento, int cdTramitacao, int cdSetor, int cdUsuario, Connection connect)	{
		boolean isConnectionNull = connect==null;
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
						
			if (cdTramitacao <= 0)	{
				ResultSet rs = connect.prepareStatement("SELECT max(cd_tramitacao) as cd_tramitacao FROM ptc_documento_tramitacao "
						+ " WHERE dt_recebimento IS NULL AND cd_documento = "+cdDocumento).executeQuery();
				if(rs.next())
					cdTramitacao = rs.getInt("cd_tramitacao");
			}
			
			if (cdTramitacao <= 0)
				// ATENÇÃO: Não mudar o código do erro!
				return new Result(-8, "Este documento não está em tramitação!");
			
			// Verificando situação de documento parado
			int cdSituacaoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0, 0, connect);
			int cdSituacaoDocumentoExterno = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ENVIO_EXTERNO", 0, 0, connect);
			if (cdSituacaoDocumento <= 0)
				return new Result(-7, "Situação padrão para documento em aberto não localizado!");
			
			DocumentoTramitacao tramitacao = DocumentoTramitacaoDAO.get(cdTramitacao, cdDocumento, connect);
			
			// Verifica se estão tramitando
			if(tramitacao.getDtRecebimento()!=null)
				return new Result(-6, "O documento não está tramitando! [cdDocumento:"+cdDocumento+",cdTramitacao:"+cdTramitacao+"]");
			
			Documento documento = DocumentoDAO.get(cdDocumento, connect);

			if (documento==null)
				return new Result(-5, "Documento indicado não existe.");
			
			boolean lgSetorExterno = documento.getCdSituacaoDocumento()==cdSituacaoDocumentoExterno;
						
			if(!lgSetorExterno) {
				// Verifica se o documento foi enviado para o setor que o estão recebendo
				if(tramitacao.getCdSetorDestino()!=cdSetor || cdSetor<=0)	{
					Setor setorInformado = SetorDAO.get(cdSetor, connect);
					Setor setorDestino   = SetorDAO.get(tramitacao.getCdSetorDestino(), connect);
					//Documento d = DocumentoDAO.get(cdDocumento, connect);
					
					// ATENÇÃO: Não mudar o código do erro!
					return new Result(-4, "Setor logado diferente de setor destino!" +
							               " Setor destino: "+(setorDestino!=null?setorDestino.getNmSetor():"")+" - Seu setor: "+(setorInformado!=null?setorInformado.getNmSetor():""));
				}
			}

			int retorno;
			
			// Atualizando dados do documento
			documento.setCdSetorAtual(tramitacao.getCdSetorDestino());
			documento.setCdSituacaoDocumento(cdSituacaoDocumento);
			retorno = DocumentoDAO.update(documento, connect);
			
			if(retorno <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-3, "Não foi possível alterar a situação do documento.");
			}
			
			// Gravando informações na tramitação
			tramitacao.setCdUsuarioDestino(cdUsuario);
			tramitacao.setDtRecebimento(new GregorianCalendar());
			retorno = DocumentoTramitacaoDAO.update(tramitacao, connect);
			
			if(retorno <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Não foi possível alterar os dados do recebimento.");
			}

			if(lgSetorExterno) {
				
				DocumentoTramitacao t = new DocumentoTramitacao(0 /*cdTramitacao*/, cdDocumento, cdSetor, cdUsuario /*cdUsuarioDestino*/,
						 tramitacao.getCdSetorDestino(), tramitacao.getCdUsuarioDestino()/*cdUsuarioOrigem*/, 
						 new GregorianCalendar() /*dtTramitacao*/, new GregorianCalendar() /*dtRecebimento*/, null /*nmLocalDestino*/, 
						 "Enviado automaticamente pelo setor externo.", cdSituacaoDocumento, documento.getCdFase(), null/*nmLocalOrigem*/, null/*nrRemessa*/);
	
				Result result = DocumentoTramitacaoServices.insert(t, connect);
				
				documento.setCdSetorAtual(cdSetor);
				retorno = DocumentoDAO.update(documento, connect);
				
				if (result.getCode() <= 0 || retorno <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
						return result;
				}
			}
			
			int lgProtocoloEel = ParametroServices.getValorOfParametroAsInteger("LG_PROTOCOLO_EEL", 0, documento.getCdEmpresa(), connect);
			
			/**
			 * Tramitação no banco da E&L
			 */
			Result r = null;
			if(retorno>0) {
				if(lgProtocoloEel>0) {
					r = receberDocumentoEel(cdDocumento, cdTramitacao, cdSetor, cdUsuario, connect);
					
					if(r.getCode()<=0) {
						return new Result(-9, r.getMessage());
					}
				}
			}
			
			//DOCUMENTOS VINCULADOS
			if(lgProtocoloEel>0) {
				ResultSetMap rsmVinculados = getDocumentosVinculados(cdDocumento);
				while(rsmVinculados.next()) {
					int cdDocVinculado = rsmVinculados.getInt("cd_documento");
					DocumentoTramitacao docT = DocumentoTramitacaoServices.getUltimaTramitacao(cdDocVinculado, connect);
					
					//se já foi recebido
					if(docT.getDtRecebimento()!=null)
						continue;
					
					Result result = receberDocumento(cdDocVinculado, docT.getCdTramitacao(), cdSetor, cdUsuario, connect);

					if (result.getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-11, "Não foi possível receber um documento vinculado.");
					}
				}
			}

			if (isConnectionNull)
				connect.commit();

			Result result = new Result(1, "Documento recebido com sucesso!");
			
			SituacaoDocumento situacao = SituacaoDocumentoDAO.get(cdSituacaoDocumento, connect);
			String nmSituacaoDocumento = situacao==null ? "INEXISTENTE" : situacao.getNmSituacaoDocumento();
			
			Setor setorAtual = SetorDAO.get(lgSetorExterno?cdSetor:documento.getCdSetorAtual());
			String nmSetorAtual = setorAtual==null ? "" : setorAtual.getNmSetor();
			
			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("NM_SITUACAO_DOCUMENTO", nmSituacaoDocumento);
			register.put("CD_SETOR_ATUAL", cdSetor);
			register.put("NM_SETOR_ATUAL", nmSetorAtual);
			register.put("CD_SITUACAO_DOCUMENTO", cdSituacaoDocumento);
			result = new Result(1, "Documento recebido com sucesso!");
			result.addObject("register", register);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.receberDocumento: " +  e);
			return new Result(-1, "Erro ao tentar receber documento!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static sol.util.Result receberDocumentoEel(int cdDocumento, int cdTramitacao, int cdSetor, int cdUsuario, Connection connect)	{
		Connection connExt = EelUtils.conectarEelSqlServer();
		
		if(connect==null) {
			return new Result(-1, "Não existe conexão com o banco de dados.");
		}

		if(connExt==null) {
			return new Result(-2, "Erro ao conectar com o banco de dados da E&L");
		}
		
		try {
			connExt.setAutoCommit(false);
			
			Documento documento = DocumentoDAO.get(cdDocumento, connect);
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			String nrPessoa = null;
			int cdPessoa = UsuarioDAO.get(cdUsuario, connect).getCdPessoa();
			pstmt = connect.prepareStatement(
					"SELECT * FROM grl_pessoa_externa WHERE cd_pessoa = "+cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()) { //já existe a paridade
				nrPessoa = rs.getString("cd_pessoa_externa");//"0000368";
			}
			else {
				Pessoa p = PessoaDAO.get(cdPessoa, connect);
				PessoaFisica pf = PessoaFisicaDAO.get(cdPessoa, connect);
				pstmt = connExt.prepareStatement("USE [CONT_VIT_CONQUISTA_PREFEITURA];"
						+ " SELECT codigo_g AS cd_pessoa_externa FROM gg_geral"
						+ " WHERE nome_g LIKE '%"+p.getNmPessoa()+"%'"
								+ " OR cpf_g = '"+(pf!=null ? pf.getNrCpf() : "")+"'");
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					nrPessoa = rs.getString("cd_pessoa_externa");
					
					if(nrPessoa!=null) {
						pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_externa(cd_pessoa, cd_pessoa_externa)"
								+ " VALUES (?,?)");
						pstmt.setInt(1, cdPessoa);
						pstmt.setString(2, nrPessoa);
						
						pstmt.executeUpdate();
					}
				}
			}
			
			//String nrLocal = "";
			String nrDocumentoExterno = documento.getNrDocumentoExterno();
			if(nrDocumentoExterno==null) {
				return new Result(-3, "Erro ao encontrar documento no banco de dados E&L.");
			}
			
			String nrFaseExterna = FaseDAO.get(documento.getCdFase(), connect).getNrFaseExterna();
			if(nrFaseExterna==null) {
				return new Result(-3, "Erro ao encontrar fase no banco de dados E&L.");
			}
			
			/*
			 * REMESSA
			 * SEQUENCIA
			 */
			String nrRemessa = null;
			int nrSequencia = 0;
			String cdLocalOrigem = null;
			ResultSet rsUltimoTramite = EelUtils.getUltimaTramitacao(nrDocumentoExterno, connExt);
			if(rsUltimoTramite.next()) {
				nrRemessa = rsUltimoTramite.getString("codigo_rem");
				nrSequencia = rsUltimoTramite.getInt("sequencia_tramite");
				cdLocalOrigem = rsUltimoTramite.getString("codigo_local_origem");
			}
			
			CallableStatement cstmt = connExt.prepareCall("{call dbo.func_pr_recebe_protocolo"
					+ "											@strEmpresa=?,"
					+ "											@strFilial=?,"
					+ "											@strLocal=?,"
					+ "											@strRemessa=?,"
					+ "											@strControle=?,"
					+ " 										@strPessoa=?,"
					+ "											@strTramite=?,"
					+ "											@intSequencia=?;}");
			
			cstmt.setString(1,"001");
			cstmt.setString(2, "001");
			cstmt.setString(3, cdLocalOrigem);
			cstmt.setString(4, nrRemessa);
			cstmt.setString(5, nrDocumentoExterno);
			cstmt.setString(6, nrPessoa);
			cstmt.setString(7, nrFaseExterna);
			cstmt.setInt(8, nrSequencia);
			
			cstmt.execute();
			connExt.commit();
			
			return new Result(1, "Documento recebido com sucesso no banco da E&L!");
			
		}
		catch(Exception e){
			e.printStackTrace();
			Conexao.rollback(connExt);
			return new Result(-1, "Erro ao receber documento!");
		}
		finally{
			EelUtils.desconectarEelSqlServer(connExt);
		}
	}

	public static sol.util.Result insertPendencia(DocumentoPendencia pendencia)	{
		return insertPendencia(pendencia, null);
	}
	public static sol.util.Result insertPendencia(DocumentoPendencia pendencia, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			// Verifica se já estão em tramitação
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_documento_tramitacao A " +
					                                "WHERE A.cd_documento = " +pendencia.getCdDocumento()+
					   								"  AND dt_recebimento IS NULL").executeQuery();
			if(rs.next())
			// Verifica usuário
				return new Result(-1, "Documento em tramitação! Não é permitido alterar a fase!");
			if(pendencia.getCdUsuarioRegistro() <= 0)
				return new Result(-1, "Informação do usuário não encontrada!");

			pendencia.setDtPendencia(new GregorianCalendar());
			//
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documento_pendencia (cd_documento,"+
			                                  "cd_tipo_pendencia,"+
			                                  "dt_pendencia,"+
			                                  "dt_baixa,"+
			                                  "txt_pendencia,"+
			                                  "cd_usuario_registro,"+
			                                  "cd_usuario_baixa) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(pendencia.getCdDocumento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,pendencia.getCdDocumento());
			if(pendencia.getCdTipoPendencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,pendencia.getCdTipoPendencia());
			if(pendencia.getDtPendencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(pendencia.getDtPendencia().getTimeInMillis()));
			if(pendencia.getDtBaixa()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(pendencia.getDtBaixa().getTimeInMillis()));
			pstmt.setString(5,pendencia.getTxtPendencia());
			if(pendencia.getCdUsuarioRegistro()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,pendencia.getCdUsuarioRegistro());
			if(pendencia.getCdUsuarioBaixa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,pendencia.getCdUsuarioBaixa());
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.insertPendencia: " +  e);
			return new Result(-1, "Erro ao tentar lançar pendência no Documento!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static sol.util.Result updatePendencia(DocumentoPendencia pendencia)	{
		Connection connect = Conexao.conectar();
		try {
			//System.out.println(new Timestamp(pendencia.getDtPendencia().getTimeInMillis()));
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento_pendencia " +
					                                           "SET cd_tipo_pendencia=?, dt_pendencia=?, txt_pendencia=?"+
			                                                   "WHERE cd_documento = "+pendencia.getCdDocumento() + 
			                                                   " AND cd_tipo_pendencia = " + pendencia.getCdTipoPendencia());
			pstmt.setInt(1,pendencia.getCdTipoPendencia());
			pstmt.setTimestamp(2,new Timestamp(pendencia.getDtPendencia().getTimeInMillis()));
			pstmt.setString(3,pendencia.getTxtPendencia());
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar atualizar pendência no Documento!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static sol.util.Result baixarPendencia(int cdDocumento, int cdTipoPendencia, int cdUsuario)	{
		Connection connect = Conexao.conectar();
		try {
			if(cdUsuario <= 0)
				return new Result(-1, "Informação do usuário não encontrada!");
			//
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento_pendencia " +
					                                           "SET dt_baixa = ?, cd_usuario_baixa = " +cdUsuario+
					                                           "WHERE cd_documento      = "+cdDocumento+
					                                           "  AND cd_tipo_pendencia = "+cdTipoPendencia);
			pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.baixaPendencia: " +  e);
			return new Result(-1, "Erro ao tentar baixar pendência do Documento!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getPendenciasOf(int cdDocumento) {
		Connection connect  = Conexao.conectar();
		try {
			String sql =
				" SELECT A.*, B.nm_tipo_pendencia, C.nm_login AS nm_usuario_registro, D.nm_login AS nm_usuario_baixa " +
				" FROM ptc_documento_pendencia A " +
				" JOIN ptc_tipo_pendencia     B ON (A.cd_tipo_pendencia   = B.cd_tipo_pendencia)" +
				" LEFT OUTER JOIN seg_usuario C ON (A.cd_usuario_registro = C.cd_usuario)" +
				" LEFT OUTER JOIN seg_usuario D ON (A.cd_usuario_baixa    = D.cd_usuario) " +
				"WHERE A.cd_documento = "+cdDocumento;
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static sol.util.Result insertOcorrencia(DocumentoOcorrencia ocorrencia)	{
		return insertOcorrencia(ocorrencia, null);
	}
	@SuppressWarnings("unchecked")
	public static sol.util.Result insertOcorrencia(DocumentoOcorrencia ocorrencia, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_documento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(ocorrencia.getCdDocumento()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_tipo_ocorrencia");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(ocorrencia.getCdTipoOcorrencia()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_ocorrencia");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("ptc_documento_ocorrencia", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar calcular valor da chave primária!");
			}
			ocorrencia.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documento_ocorrencia (cd_documento,cd_tipo_ocorrencia,cd_ocorrencia,"+
							                                   "cd_usuario,dt_ocorrencia,txt_ocorrencia,tp_visibilidade) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(ocorrencia.getCdDocumento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,ocorrencia.getCdDocumento());
			if(ocorrencia.getCdTipoOcorrencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,ocorrencia.getCdTipoOcorrencia());
			pstmt.setInt(3, code);
			if(ocorrencia.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,ocorrencia.getCdUsuario());
			if(ocorrencia.getDtOcorrencia()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(ocorrencia.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(6,ocorrencia.getTxtOcorrencia());
			pstmt.setInt(7,ocorrencia.getTpVisibilidade());
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.insertOcorrencia: " +  e);
			return new Result(-1, "Erro ao tentar incluir ocorrência", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static sol.util.Result updateOcorrencia(DocumentoOcorrencia ocorrencia)	{
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento_ocorrencia " +
					                                           "SET cd_tipo_ocorrencia=?, dt_ocorrencia=?, txt_ocorrencia=?, tp_visibilidade=? " +
			                                  				   "WHERE cd_documento       = "+ocorrencia.getCdDocumento()+
															   "  AND cd_tipo_ocorrencia = "+ocorrencia.getCdTipoOcorrencia()+
															   "  AND cd_ocorrencia      = "+ocorrencia.getCdOcorrencia());
			pstmt.setInt(1,ocorrencia.getCdTipoOcorrencia());
			pstmt.setTimestamp(2,new Timestamp(ocorrencia.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(3,ocorrencia.getTxtOcorrencia());
			pstmt.setInt(4,ocorrencia.getTpVisibilidade());
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar alterar ocorrência", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static Result deleteOcorrencia(int cdDocumento, int cdTipoOcorrencia, int cdOcorrencia)	{
		Connection connect  = Conexao.conectar();
		try {
			return new Result(connect.prepareStatement(" DELETE FROM ptc_documento_ocorrencia " +
														"WHERE cd_documento     = "+cdDocumento+
														"  AND cd_tipo_ocorrencia = "+cdTipoOcorrencia+
														"  AND cd_ocorrencia      = "+cdOcorrencia).executeUpdate());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir ocorrência/anotação!", e);
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getOcorrenciasOf(int cdDocumento) {
		Connection connect  = Conexao.conectar();
		try {
			String sql =
				" SELECT A.*, B.nm_tipo_ocorrencia, C.nm_login " +
				" FROM ptc_documento_ocorrencia A " +
				" JOIN grl_tipo_ocorrencia B ON (A.cd_tipo_ocorrencia = B.cd_tipo_ocorrencia)" +
				" LEFT OUTER JOIN seg_usuario C ON (A.cd_usuario = C.cd_usuario)" +
				"WHERE A.cd_documento = "+cdDocumento;
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllSolicitantesOf(int cdDocumento)	{
		return getAllSolicitantesOf(cdDocumento, null);
	}
	
	public static ResultSetMap getAllSolicitantesOf(int cdDocumento, Connection connect)	{
		return ((ResultSetMap)getSolicitantes(cdDocumento, connect).getObjects().get("rsm"));
	}
	
	public static Result getSolicitantes(int cdDocumento)	{
		return getSolicitantes(cdDocumento, null);
	}
	
	public static Result getSolicitantes(int cdDocumento, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa, B.nm_email, B.st_cadastro FROM ptc_documento_pessoa A, grl_pessoa B " +
					                                           "WHERE A.cd_pessoa = B.cd_pessoa " +
					                                           "  AND A.cd_documento = "+cdDocumento);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			String nmSolicitantes = "", cdSolicitantes = "";
			while(rsm.next())	{
				nmSolicitantes += (nmSolicitantes.equals("")?"":", ")+rsm.getString("nm_pessoa")+
						(rsm.getString("nm_qualificacao")!=null ? " - "+rsm.getString("nm_qualificacao") : "");
				cdSolicitantes += (cdSolicitantes.equals("")?"":";")+rsm.getString("cd_pessoa");
			}
			Result result = new Result(1);
			result.addObject("rsm", rsm);
			result.addObject("cdSolicitantes", cdSolicitantes);
			result.addObject("nmSolicitantes", nmSolicitantes);
			return result;
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

	public static ResultSetMap findPendencia(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		return findPendencia(criterios, groupBy, null);
	}

	public static ResultSetMap findPendencia(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			String groups = "";
			String fields = " A.*, B.nm_tipo_pendencia, C.dt_protocolo, C.nr_documento, D.nm_login AS nm_usuario_registro, L.nm_login AS nm_usuario_baixa, " +
							"       E.nm_pessoa as nm_usuario, F.nm_tipo_documento, " +
							"       G.nm_setor, H.nm_setor AS nm_setor_atual, I.nm_situacao_documento, J.nm_fase," +
							"       (SELECT MAX(nm_pessoa) FROM ptc_documento_pessoa DP, grl_pessoa P " +
							"        WHERE DP.cd_pessoa = P.cd_pessoa) AS nm_solicitantes ";

			// Processa agrupamentos enviados em groupBy
			String [] retorno = com.tivic.manager.util.Util.getFieldsAndGroupBy(groupBy, fields, groups, "COUNT(*) AS QT_PENDENCIA");
			fields = retorno[0];
			groups = retorno[1];

			ResultSetMap rsm = Search.find(
					"SELECT " +fields+
					"FROM ptc_documento_pendencia A " +
					"JOIN ptc_tipo_pendencia B ON (B.cd_tipo_pendencia = A.cd_tipo_pendencia) " +
					"JOIN ptc_documento      C ON (C.cd_documento = A.cd_documento) " +
					"LEFT OUTER JOIN seg_usuario            D ON (D.cd_usuario = A.cd_usuario_registro) " +
					"LEFT OUTER JOIN grl_pessoa             E ON (E.cd_pessoa  = D.cd_pessoa) " +
					"LEFT OUTER JOIN gpn_tipo_documento     F on (F.cd_tipo_documento = C.cd_tipo_documento) " +
					"LEFT OUTER JOIN grl_setor              G ON (G.cd_setor = C.cd_setor) " +
					"LEFT OUTER JOIN grl_setor              H ON (H.cd_setor = C.cd_setor_atual) " +
					"LEFT OUTER JOIN ptc_situacao_documento I ON (I.cd_situacao_documento = C.cd_situacao_documento) " +
					"LEFT OUTER JOIN ptc_fase               J ON (J.cd_fase = C.cd_fase) " +
					"LEFT OUTER JOIN seg_usuario            L ON (L.cd_usuario = A.cd_usuario_baixa)" +
					"WHERE 1 = 1 ", groups=="" ? "ORDER BY A.dt_pendencia" : groups, criterios, connect!=null ? connect : connect, false);
			// Acrescentando informação dos solicitantes
			while(rsm.next()) {
				Result result = getSolicitantes(rsm.getInt("cd_documento"), connect);
				rsm.setValueToField("rsmSolicitantes", result.getObjects().get("rsm"));
				rsm.setValueToField("NM_SOLICITANTES", result.getObjects().get("nmSolicitantes"));
				rsm.setValueToField("CD_SOLICITANTES", result.getObjects().get("cdSolicitantes"));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap insertMovimento(ArrayList<Integer> docs, int cdEmpresaDestino, int cdPessoaDestino, String nmLocalDestino)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmLog = new ResultSetMap();
		try	{
			for(int i=0; i<docs.size(); i++){
				int ret = insertMovimento(docs.get(i).intValue(), cdEmpresaDestino, cdPessoaDestino, nmLocalDestino, connect);
				if (ret <= 0)	{
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("CD_ERRO", new Integer(ret));
					register.put("CD_DOCUMENTO", docs.get(i));
					register.put("DS_ERRO", ret==-10?"Usuario destino não registrado na empresa informada":"Erro desconhecido");
					rsmLog.addRegister(register);
				}
			}
			return rsmLog;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insertMovimento: " + e);
			return rsmLog;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int insertMovimento(int cdDocumento, int cdEmpresaDestino, int cdPessoaDestino, String nmLocalDestino, Connection connect)	{
		boolean isConnectionNull = (connect==null);
		if(isConnectionNull)
			connect = Conexao.conectar();
		try	{
			int code = Conexao.getSequenceCode("PTC_TRAMITACAO_DOCUMENTO", connect);
			connect.setAutoCommit(false);
			if(cdPessoaDestino>0 && !connect.prepareStatement("SELECT * FROM seg_usuario_empresa " +
															  "WHERE cd_empresa = "+cdEmpresaDestino+
															  "  AND cd_usuario = "+cdPessoaDestino).executeQuery().next())	{
				return -10;
			}
			//
			PreparedStatement pstmt = connect.prepareStatement(
					"INSERT INTO ptc_tramitacao_documento (cd_documento,cd_tramitacao,dt_tramitacao,cd_pessoa_destino," +
					"cd_empresa_destino,st_tramitacao,nm_local_destino,lg_liberado)VALUES (?,?,?,?,?,?,?,1)");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, code);
			pstmt.setTimestamp(3, new Timestamp((new GregorianCalendar()).getTimeInMillis()));
			if(cdPessoaDestino > 0)
				pstmt.setInt(4, cdPessoaDestino);
			else
				pstmt.setNull(4, 4);
			if(cdEmpresaDestino > 0){
				pstmt.setInt(5, cdEmpresaDestino);
				pstmt.setInt(6, 0);
			}
			else	{
				pstmt.setNull(5, 4);
				pstmt.setInt(6, 1);
			}
			pstmt.setString(7, nmLocalDestino);
			pstmt.executeUpdate();
			pstmt = connect.prepareStatement("UPDATE ptc_documento SET st_documento = " + (cdEmpresaDestino <= 0 ? 3 : 1) + " WHERE cd_documento = " + cdDocumento);
			pstmt.executeUpdate();
			connect.commit();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insertMovimento: " + e);
			return -1;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap setRecebimento(ArrayList<Integer> docs, ArrayList<Integer> trs, int cdPessoaAtual)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmLog = new ResultSetMap();
		try	{
			for(int i=0; i<docs.size(); i++){
				int ret = setRecebimento(docs.get(i).intValue(), trs.get(i).intValue(), cdPessoaAtual, connect);
				if (ret <= 0)	{
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("CD_ERRO", new Integer(ret));
					register.put("CD_DOCUMENTO", docs.get(i));
					register.put("DS_ERRO", "Erro desconhecido");
					rsmLog.addRegister(register);
				}
			}
			return rsmLog;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insertMovimento: " + e);
			return rsmLog;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int setRecebimento(int cdDocumento, int cdTramitacao, int cdPessoaAtual, Connection connect)	{
		boolean isConnectionNull = (connect==null);
		if(isConnectionNull)
			connect = Conexao.conectar();
		try		{
			int cdEmpresaAtual = 0;
			connect.setAutoCommit(false);
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_empresa_destino, cd_pessoa_destino FROM ptc_tramitacao_documento WHERE" +
					" cd_documento = "
					+ cdDocumento + "  AND cd_tramitacao = " + cdTramitacao);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				cdEmpresaAtual = rs.getInt("cd_empresa_destino");

			pstmt = connect.prepareStatement("UPDATE ptc_tramitacao_documento SET dt_recebimento=?, st_tramitacao = ? " +
					                         "WHERE cd_documento = ? " +
					                         "   AND cd_tramitacao = ? ");
			pstmt.setTimestamp(1, new Timestamp((new GregorianCalendar()).getTimeInMillis()));
			pstmt.setInt(2, 1);
			pstmt.setInt(3, cdDocumento);
			pstmt.setInt(4, cdTramitacao);
			pstmt.executeUpdate();
			pstmt = connect.prepareStatement("UPDATE ptc_documento SET st_documento = 0   ,cd_pessoa_atual  = " + cdPessoaAtual + "   ,cd_empresa_atual = " + cdEmpresaAtual + " WHERE cd_documento = " + cdDocumento);
			pstmt.executeUpdate();
			connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			Conexao.rollback(connect);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimento: " + sqlExpt);
			return -1 * sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimento: " + e);
			return -1;
		}
		finally		{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * @category SICOE
	 */
	public static ResultSetMap setArquivamento(ArrayList<Integer> docs)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmLog = new ResultSetMap();
		try	{
			for(int i=0; i<docs.size(); i++){
				int ret = setArquivamento(docs.get(i).intValue(), connect);
				if (ret <= 0)	{
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("CD_ERRO", new Integer(ret));
					register.put("CD_DOCUMENTO", docs.get(i));
					register.put("DS_ERRO", "Erro desconhecido");
					rsmLog.addRegister(register);
				}
			}
			return rsmLog;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setArquivamento: " + e);
			return rsmLog;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	/**
	 * @category SICOE
	 */
	public static int setArquivamento(int cdDocumento, Connection connect)	{
		boolean isConectionNull = (connect==null);
		if(isConectionNull)
			connect = Conexao.conectar();
		try		{
			connect.setAutoCommit(false);
			PreparedStatement pstmt = connect.prepareStatement(
					"UPDATE ptc_documento SET st_documento = " + ST_ARQUIVADO +
					"                        ,dt_arquivamento = ? "+
					"WHERE cd_documento = " + cdDocumento +
					"  AND st_documento = 0");
			pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			if(pstmt.executeUpdate() > 0)	{
				int cdSituacaoProposta = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_ARQUIVADO", 0);
				if(cdSituacaoProposta > 0)
					connect.prepareStatement("UPDATE sce_contrato SET cd_situacao = " + cdSituacaoProposta +
							                 "WHERE cd_documento = " + cdDocumento).executeUpdate();
			}
			else
				return -10;
			connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			Conexao.rollback(connect);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimento: " + sqlExpt);
			return -1 * sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimento: " + e);
			return -1;
		}
		finally		{
			if(isConectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * @category SICOE
	 */
	public static ResultSetMap findSICOE(ArrayList<ItemComparator> criterios)	{
		String sqlBloqueados = "JOIN sce_contrato_comissao CC ON (CC.cd_contrato_emprestimo = F.cd_contrato_emprestimo) " +
				               "WHERE CC.cd_nota_fiscal IS NULL "+
					           "  AND CC.cd_conta_pagar IS NULL " +
					           "  AND CC.vl_pago > 0 " +
					           "  AND CC.cd_pessoa IS NOT NULL "+
					           "  AND F.cd_contrato_emprestimo IN (SELECT X.cd_contrato_emprestimo "+
					           "                                   FROM sce_contrato_comissao X, adm_nota_fiscal Y, sce_fechamento W "+
					           "                                   WHERE Y.cd_fechamento = W.cd_fechamento" +
					           "                                     AND W.cd_empresa IS NOT NULL " +
					           "                                     AND Y.cd_nota_fiscal = X.cd_nota_fiscal) ";
		String sql = "SELECT A.cd_documento, A.dt_documento, A.nm_local_origem, A.nm_local_destino, " +
					 "       A.lg_confidencial, A.id_documento, A.cd_pessoa_atual, A.cd_pessoa_emissor, " +
					 "       A.cd_empresa_emissora, A.cd_empresa_atual, A.st_documento, A.txt_documento,  " +
					 "       B.nm_pessoa AS nm_pessoa_emissor, C.nm_pessoa AS nm_pessoa_atual,     " +
					 "       D.nm_empresa AS nm_empresa_emissora, E.nm_empresa AS nm_empresa_atual,       " +
					 "       F.qt_parcelas, F.vl_financiado, F.cd_agente, G.nm_pessoa AS nm_contratante, G.nr_cpf_cnpj, " +
					 "       H.nm_pessoa AS nm_agente, J.nm_pessoa AS nm_colaborador, J.nr_cpf_cnpj AS nr_cpf_cnpj_colaborador, " +
					 "       L.nm_produto, M.cd_pessoa AS cd_parceiro, M.nm_pessoa AS nm_parceiro " +
					 "FROM ptc_documento A " +
					 "LEFT OUTER JOIN grl_pessoa   B ON (A.cd_pessoa_emissor   = B.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa   C ON (A.cd_pessoa_atual     = C.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_empresa  D ON (A.cd_empresa_emissora = D.cd_empresa) " +
					 "LEFT OUTER JOIN grl_empresa  E ON (A.cd_empresa_atual    = E.cd_empresa) " +
					 "LEFT OUTER JOIN sce_contrato F ON (A.cd_documento   = F.cd_documento) " +
					 "LEFT OUTER JOIN grl_pessoa   G ON (F.cd_contratante = G.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa   H ON (F.cd_agente      = H.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa_empresa I ON (A.cd_documento = I.cd_documento) " +
					 "LEFT OUTER JOIN grl_pessoa   J ON (I.cd_pessoa = J.cd_pessoa) " +
					 "LEFT OUTER JOIN sce_produto  L ON (F.cd_produto = L.cd_produto) " +
					 "LEFT OUTER JOIN grl_pessoa   M ON (L.cd_instituicao_financeira = M.cd_pessoa) ";
		for(int i=0; i<criterios.size(); i++)
			if(criterios.get(i).getColumn().toUpperCase().equals("LG_BLOQUEADO"))	{
				criterios.remove(i);
				sql += sqlBloqueados;
				break;
			}

		return Search.find(sql, criterios, Conexao.conectar(), true);
	}
	/**
	 * @category SICOE
	 */
	public static ResultSetMap findPendencia(int cdAtendente)	{
		String sql = "SELECT A.cd_documento, A.dt_documento, A.nm_local_origem, A.nm_local_destino, " +
					 "       A.lg_confidencial, A.id_documento, A.cd_pessoa_atual, A.cd_pessoa_emissor, " +
					 "       A.cd_empresa_emissora, A.cd_empresa_atual, A.st_documento, A.txt_documento,  " +
					 "       B.nm_pessoa AS nm_pessoa_emissor, C.nm_pessoa AS nm_pessoa_atual,     " +
					 "       D.nm_empresa AS nm_empresa_emissora, E.nm_empresa AS nm_empresa_atual,       " +
					 "       F.qt_parcelas, F.vl_financiado, F.cd_agente, G.nm_pessoa AS nm_contratante, G.nr_cpf_cnpj, " +
					 "       H.nm_pessoa AS nm_agente, J.nm_pessoa AS nm_colaborador, J.nr_cpf_cnpj AS nr_cpf_cnpj_colaborador, " +
					 "       L.nm_produto, M.cd_pessoa AS cd_parceiro, M.nm_pessoa AS nm_parceiro " +
					 "FROM ptc_documento A " +
					 "LEFT OUTER JOIN grl_pessoa   B ON (A.cd_pessoa_emissor   = B.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa   C ON (A.cd_pessoa_atual     = C.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_empresa  D ON (A.cd_empresa_emissora = D.cd_empresa) " +
					 "LEFT OUTER JOIN grl_empresa  E ON (A.cd_empresa_atual    = E.cd_empresa) " +
					 "LEFT OUTER JOIN sce_contrato F ON (A.cd_documento   = F.cd_documento) " +
					 "LEFT OUTER JOIN grl_pessoa   G ON (F.cd_contratante = G.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa   H ON (F.cd_agente      = H.cd_pessoa) " +
					 "LEFT OUTER JOIN grl_pessoa_empresa I ON (A.cd_documento = I.cd_documento) " +
					 "LEFT OUTER JOIN grl_pessoa   J ON (I.cd_pessoa = J.cd_pessoa) " +
					 "LEFT OUTER JOIN sce_produto  L ON (F.cd_produto = L.cd_produto) " +
					 "LEFT OUTER JOIN grl_pessoa   M ON (L.cd_instituicao_financeira = M.cd_pessoa) " +
					 "WHERE A.cd_pessoa_atual = F.cd_agente " +
					 "  AND F.cd_atendente    = "+cdAtendente;
		return Search.find(sql, new ArrayList<ItemComparator>(), Conexao.conectar(), true);
	}
	/**
	 * @category SICOE
	 */
	public static ResultSetMap findDocumentoEnviado(int cdPessoa, int tpRelacao, int cdParceiro, GregorianCalendar dtInicial, GregorianCalendar dtFinal)	{
		Connection connect = Conexao.conectar();
		try	{
			String nmDestinatario = "";
			if(cdParceiro > 0)	{
				Pessoa pessoa = PessoaDAO.get(cdParceiro, connect);
				nmDestinatario = pessoa!=null ? pessoa.getNmPessoa() : "";
			}
			if(dtInicial!=null)
				dtInicial.set(GregorianCalendar.HOUR_OF_DAY, 0);
			if(dtFinal!=null)
				dtFinal.set(GregorianCalendar.HOUR_OF_DAY, 23);

			String sql = "SELECT A.cd_documento, A.dt_documento, A.nm_local_origem, " +
					     "       A.lg_confidencial, A.id_documento, A.cd_pessoa_atual, A.cd_pessoa_emissor, " +
					     "       A.cd_empresa_emissora, A.cd_empresa_atual, A.st_documento, A.txt_documento, " +
					     "       B.nm_pessoa AS nm_pessoa_destino, D.nm_empresa AS nm_empresa_destino, " +
					     "       F.qt_parcelas, F.vl_financiado, G.nm_pessoa AS nm_contratante, G.nr_cpf_cnpj, " +
					     "       H.nm_pessoa AS nm_agente, J.nm_pessoa AS nm_colaborador, J.nr_cpf_cnpj AS nr_cpf_cnpj_colaborador, " +
					     "       L.nm_produto, M.nm_pessoa AS nm_parceiro, M.cd_pessoa AS cd_parceiro, " +
					     "       TD.dt_tramitacao, TD.cd_tramitacao, TD.nm_local_destino, TD.dt_recebimento " +
					     "FROM ptc_documento  A " +
					     "JOIN ptc_tramitacao_documento TD ON (A.cd_documento  = TD.cd_documento " +
					     (tpRelacao<2? " AND TD.st_tramitacao = "+tpRelacao : "") +
					     (!nmDestinatario.equals("") ? " AND TD.nm_local_destino = \'"+nmDestinatario+"\'": "")+
					     (dtInicial!=null ? " AND TD.dt_tramitacao >= ? " : "")+
					     (dtFinal!=null ? " AND TD.dt_tramitacao <= ? " : "")+")" +
						 "LEFT OUTER JOIN grl_pessoa     B ON (TD.cd_pessoa_destino  = B.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_empresa  D ON (TD.cd_empresa_destino = D.cd_empresa) " +
						 "LEFT OUTER JOIN sce_contrato F ON (A.cd_documento   = F.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   G ON (F.cd_contratante = G.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa   H ON (F.cd_agente      = H.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa_empresa I ON (A.cd_documento = I.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   J ON (I.cd_pessoa = J.cd_pessoa) " +
						 "LEFT OUTER JOIN sce_produto  L ON (F.cd_produto = L.cd_produto) " +
						 "LEFT OUTER JOIN grl_pessoa   M ON (L.cd_instituicao_financeira = M.cd_pessoa) " +
						 "WHERE 1=1 "+
						 (tpRelacao==2 ? " AND A.st_documento = "+ST_SETOR_EXTERNO : (tpRelacao==0 ? " AND A.st_documento = "+ST_TRAMITANDO: ""))+
						 (tpRelacao==0 ? " AND A.cd_pessoa_atual = " + cdPessoa : "");
			PreparedStatement pstmt = connect.prepareStatement(sql);
			int index = 1;
			// Data Inicial
			if(dtInicial!=null)	{
				pstmt.setTimestamp(index, new Timestamp(dtInicial.getTimeInMillis()));
				index++;
			}
			// Data Final
			if(dtFinal!=null)
				pstmt.setTimestamp(index, new Timestamp(dtFinal.getTimeInMillis()));

			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.findDocumentoEnviado: " + e);
			return null;
		}
		finally		{
			Conexao.desconectar(connect);
		}
	}
	/**
	 * @category SICOE
	 */
	public static ResultSetMap findDocumentoChegando(int cdEmpresa, int cdPessoa, ArrayList<ItemComparator> criterios)	{
		try		{
			String sql = "SELECT A.cd_documento, A.dt_documento, A.nm_local_origem, " +
					     "       A.lg_confidencial, A.id_documento, A.cd_pessoa_atual, A.cd_pessoa_emissor, " +
					     "       A.cd_empresa_emissora, A.cd_empresa_atual, A.st_documento, A.txt_documento, " +
					     "       B.nm_pessoa AS nm_pessoa_atual, D.nm_empresa AS nm_empresa_atual, F.cd_agente, " +
					     "       F.qt_parcelas, F.vl_financiado, G.nm_pessoa AS nm_contratante, G.nr_cpf_cnpj," +
					     "       H.nm_pessoa AS nm_agente, J.nm_pessoa AS nm_colaborador, " +
					     "       J.nr_cpf_cnpj AS nr_cpf_cnpj_colaborador, L.nm_produto, " +
					     "       M.nm_pessoa AS nm_parceiro, M.cd_pessoa AS cd_parceiro, TD.dt_tramitacao, " +
					     "       TD.cd_tramitacao, TD.nm_local_destino, TD.dt_recebimento FROM ptc_documento A " +
						 "JOIN ptc_tramitacao_documento TD ON (A.cd_documento   = TD.cd_documento         " +
						 "                                 AND TD.st_tramitacao = 0   " +
						 "                                 AND (TD.cd_pessoa_destino  = "+ cdPessoa +
						 "                                   OR TD.cd_pessoa_destino IS NULL) " +
						 "                                 AND  TD.cd_empresa_destino = " + cdEmpresa + ")" +
						 "LEFT OUTER JOIN grl_pessoa   B ON (A.cd_pessoa_atual  = B.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_empresa  D ON (A.cd_empresa_atual = D.cd_empresa) " +
						 "LEFT OUTER JOIN sce_contrato F ON (A.cd_documento   = F.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   G ON (F.cd_contratante = G.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa   H ON (F.cd_agente      = H.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa_empresa I ON (A.cd_documento = I.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   J ON (I.cd_pessoa = J.cd_pessoa) " +
						 "LEFT OUTER JOIN sce_produto  L ON (F.cd_produto = L.cd_produto) " +
						 "LEFT OUTER JOIN grl_pessoa   M ON (L.cd_instituicao_financeira = M.cd_pessoa) ";
			return Search.find(sql, criterios, Conexao.conectar(), true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.findDocumentoChegando: " + e);
			return null;
		}
	}
	/**
	 * @category SICOE
	 */
	public static ResultSetMap findDocumentoChegandoMatriz(int cdPessoa, ArrayList<ItemComparator> criterios)	{
		Connection connect = Conexao.conectar();
		try		{
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT cd_empresa FROM grl_empresa WHERE lg_matriz = 1").executeQuery());
			String matrizes = "";
			while(rsm.next())
				matrizes += (matrizes.equals("") ? "" : "," )+ rsm.getInt("cd_empresa");

			String sql = "SELECT A.cd_documento, A.dt_documento, A.nm_local_origem, " +
					     "       A.lg_confidencial, A.id_documento, A.cd_pessoa_atual, A.cd_pessoa_emissor, " +
					     "       A.cd_empresa_emissora, A.cd_empresa_atual, A.st_documento, A.txt_documento, " +
					     "       B.nm_pessoa AS nm_pessoa_atual, D.nm_empresa AS nm_empresa_atual, F.cd_agente, " +
					     "       F.qt_parcelas, F.vl_financiado, G.nm_pessoa AS nm_contratante, G.nr_cpf_cnpj," +
					     "       H.nm_pessoa AS nm_agente, J.nm_pessoa AS nm_colaborador, " +
					     "       J.nr_cpf_cnpj AS nr_cpf_cnpj_colaborador, L.nm_produto, " +
					     "       M.nm_pessoa AS nm_parceiro, M.cd_pessoa AS cd_parceiro, TD.dt_tramitacao, " +
					     "       TD.cd_tramitacao, TD.nm_local_destino, TD.dt_recebimento " +
					     "FROM ptc_documento A " +
						 "JOIN ptc_tramitacao_documento TD ON (A.cd_documento   = TD.cd_documento         " +
						 "                                 AND TD.st_tramitacao = 0   " +
						 "                                 AND (TD.cd_pessoa_destino  = "+ cdPessoa +
						 "                                   OR TD.cd_pessoa_destino IS NULL) " +
						 "                                 AND  TD.cd_empresa_destino IN ("+matrizes+"))" +
						 "LEFT OUTER JOIN grl_pessoa   B ON (A.cd_pessoa_atual  = B.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_empresa  D ON (A.cd_empresa_atual = D.cd_empresa) " +
						 "LEFT OUTER JOIN sce_contrato F ON (A.cd_documento   = F.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   G ON (F.cd_contratante = G.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa   H ON (F.cd_agente      = H.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa_empresa I ON (A.cd_documento = I.cd_documento) " +
						 "LEFT OUTER JOIN grl_pessoa   J ON (I.cd_pessoa = J.cd_pessoa) " +
						 "LEFT OUTER JOIN sce_produto  L ON (F.cd_produto = L.cd_produto) " +
						 "LEFT OUTER JOIN grl_pessoa   M ON (L.cd_instituicao_financeira = M.cd_pessoa) ";
			return Search.find(sql, criterios, connect, true);
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.findDocumentoChegandoMatriz: " + sqlExpt);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	/**
	 * @category SICOE
	 */
	public static ResultSetMap findTramitacao(ArrayList<ItemComparator> criterios)	{
		String sql = "SELECT A.*, B.nm_pessoa AS nm_pessoa_destino, C.nm_empresa AS nm_empresa_destino" +
		             " FROM ptc_tramitacao_documento A " +
		             "LEFT OUTER JOIN grl_pessoa   B ON (A.cd_pessoa_destino  = B.cd_pessoa) " +
		             "LEFT OUTER JOIN grl_empresa  C ON (A.cd_empresa_destino = C.cd_empresa) ";
		return Search.find(sql, criterios, Conexao.conectar(), true);
	}
	/**
	 * @category SICOE
	 */
	public static ResultSetMap setRecebimentoAgente(ArrayList<Integer> docs, int cdEmpresaAtual, int cdPessoaAtual)	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmLog = new ResultSetMap();
		try	{
			for(int i=0; i<docs.size(); i++){
				int cdDocumento = docs.get(i).intValue();
				int cdTramitacao = insertMovimento(cdDocumento, cdEmpresaAtual, cdPessoaAtual, "", connect);
				int ret = cdTramitacao;
				if(cdTramitacao>0)
					ret = setRecebimento(cdDocumento, cdTramitacao, cdPessoaAtual, connect);
				if (ret <= 0)	{
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("CD_ERRO", new Integer(ret));
					register.put("CD_DOCUMENTO", docs.get(i));
					register.put("DS_ERRO", ret==-10?"Usuario destino não registrado na empresa informada":"Erro desconhecido");
					rsmLog.addRegister(register);
					System.out.println("DocumentoServices.setRecebimentoAgente: "+(ret==-10?"Usuario destino não registrado na empresa informada":"Erro desconhecido"));
				}
			}
			return rsmLog;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimentoAgente: " + e);
			return rsmLog;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	/**
	 * @category SICOE
	 */
	public static int setRecebimentoAgente(int cdDocumento, int cdEmpresaAtual, int cdPessoaAtual)	{
		Connection connect = Conexao.conectar();
		try	{
			int cdTramitacao = insertMovimento(cdDocumento, cdEmpresaAtual, cdPessoaAtual, "", connect);
			if(cdTramitacao>0)
				return setRecebimento(cdDocumento, cdTramitacao, cdPessoaAtual, connect);
			return -1;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.setRecebimentoAgente: " + e);
			return -1;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getAllQualificacao() {
		return getAllQualificacao(null);
	}

	public static ResultSetMap getAllQualificacao(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT DISTINCT nm_qualificacao FROM ptc_documento_pessoa ORDER BY nm_qualificacao");
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
	
	public static ResultSetMap getAgendas(int cdDocumento){
		return getAgendas(cdDocumento, null);
	}
	
	public static ResultSetMap getAgendas(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.cd_tipo_prazo as cd_tipo_prazo_documento, B.nm_tipo_prazo as nm_tipo_prazo_documento, "
					+ " B.tp_agenda_item, B.tp_agenda_item as tp_agenda_item_documento, C.nm_pessoa, " +
					"D.* "+
					" FROM agd_agenda_item A " +
					" LEFT OUTER JOIN ptc_tipo_prazo B ON (A.cd_tipo_prazo_documento = B.cd_tipo_prazo) " +
					" LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa)" +
					" LEFT OUTER JOIN agd_local D on (A.cd_local = D.cd_local)"+
					" WHERE A.cd_documento = "+cdDocumento+
					" ORDER BY dt_inicial");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! DocumentoServices.getAgendas: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @category JURIS
	 */
	public static HashMap<String, Object> getAllParametrosSituacaoDocumento() {
		return getAllParametrosSituacaoDocumento(0, null);
	}
	
	/**
	 * @category JURIS
	 */
	public static HashMap<String, Object> getAllParametrosSituacaoDocumento(int cdEmpresa) {
		return getAllParametrosSituacaoDocumento(cdEmpresa, null);
	}
	
	/**
	 * @category JURIS
	 */
	public static HashMap<String, Object> getAllParametrosSituacaoDocumento(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			
			params.put("CD_SIT_DOC_EM_ABERTO", ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0, cdEmpresa, connection));
			params.put("CD_SIT_DOC_ARQUIVADO", ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ARQUIVADO", 0, cdEmpresa, connection));
			params.put("CD_SIT_DOC_ENVIO_EXTERNO", ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_ENVIO_EXTERNO", 0, cdEmpresa, connection));
			params.put("CD_SIT_DOC_TRAMITANDO", ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_TRAMITANDO", 0, cdEmpresa, connection));
			params.put("CD_SIT_DOC_EM_ANDAMENTO", ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ANDAMENTO", 0, cdEmpresa, connection));
			
			return params;
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
	
	/**
	 * Salva documentos originados em flex/ptc/AtendimentoForm (PROCON).
	 * Trata de casos específicos de Atendimentos.
	 * 
	 * @param documento Objeto documento
	 * @param solicitantes ArrayList<DocumentoPessoa> Lista de reclamantes/reclamados
	 * @return Result com os objetos SETOR, SITUACAO e DOCUMENTO
	 * 
	 * @since 28/05/2015
	 * @author Maurício
	 */
	public static Result saveAtendimento(Documento documento){
		return saveAtendimento(documento, new ArrayList<DocumentoPessoa>(), null, null);
	}
	
	public static Result saveAtendimento(Documento documento, ArrayList<DocumentoPessoa> solicitantes){
		return saveAtendimento(documento, solicitantes, null, null);
	}
	
	public static Result saveAtendimento(Documento documento, ArrayList<DocumentoPessoa> solicitantes, ArrayList<AtendimentoDocumento> concessoes){
		return saveAtendimento(documento, solicitantes, concessoes, null);
	}
	
	public static Result saveAtendimento(Documento documento, ArrayList<DocumentoPessoa> solicitantes, ArrayList<AtendimentoDocumento> concessoes, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(documento==null)
				return new Result(-1, "Erro ao salvar. Documento é nulo");
			
			// Insere dsAssunto caso ele não exista
			if(documento.getDsAssunto()==null || documento.getDsAssunto().equals("")) {
				String txtDoc = documento.getTxtDocumento();
				documento.setDsAssunto(txtDoc.substring(0, (txtDoc.length()>140 ? 139 : txtDoc.length())));
			}
			
			int cdDocumento = 0;
			
			if(documento.getCdDocumento()<=0) { //INSERT
				
				//NÚMERO
				if(documento.getNrAtendimento()==null || documento.getNrAtendimento().equals("")) { 
					String nrAtendimento = TipoDocumentoServices.getNextNumeracao(documento.getCdTipoDocumento(), documento.getCdEmpresa(), true, connect);
					documento.setNrAtendimento(nrAtendimento);
				}
				
				//SETOR
				if(documento.getCdSetor()>0 && documento.getCdSetorAtual()<=0)
					documento.setCdSetorAtual(documento.getCdSetor());
				
				//<<INSERT>>
				cdDocumento = DocumentoDAO.insert(documento, connect);
				documento.setCdDocumento(cdDocumento);
				
				if(documento.getCdDocumento() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-10, "Erro ao tentar inserir DOCUMENTO ["+documento.getCdDocumento()+"]");
				}
				
				//PARTES
				
				//Eliminando Duplicações
				for (int i = 0; i < solicitantes.size(); i++) {
					for (int j = i+1; j < solicitantes.size(); j++) {
						if(solicitantes.get(i).getCdPessoa() == solicitantes.get(j).getCdPessoa()) {
							solicitantes.remove(j);
						}	
					}
				}
				//Salvando solicitantes
				for (DocumentoPessoa documentoPessoa : solicitantes) {
					documentoPessoa.setCdDocumento(documento.getCdDocumento());
					if (DocumentoPessoaDAO.insert(documentoPessoa, connect) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-9, "Erro ao tentar inserir SOLICITANTES.");
					}
				}	
				
				//CONCESSOES (para atendimento em MOB)
				if(concessoes!=null) {
					
					//Eliminando Duplicações
					if(AtendimentoDocumentoServices.removeByDocumento(documento.getCdDocumento(), null, connect).getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-9, "Erro ao tentar limpar CONCESSOES.");
					}
					for (AtendimentoDocumento atendimentoDocumento : concessoes) {
						if(atendimentoDocumento.getCdDocumento()<=0)
							atendimentoDocumento.setCdDocumento(documento.getCdDocumento());
						
						if(AtendimentoDocumentoDAO.insert(atendimentoDocumento, connect)<=0) {
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-9, "Erro ao tentar inserir CONCESSOES.");
						}
					}
				}
				
				//TRAMITACAO
				DocumentoTramitacao tramitacao = new DocumentoTramitacao(0, documento.getCdDocumento(), documento.getCdSetorAtual() /*cdSetorDestino*/,
                        documento.getCdUsuario(),
                        documento.getCdSetor() /*cdSetorOrigem*/, documento.getCdUsuario() /*cdUsuarioOrigem*/,
						new GregorianCalendar() /*dtTramitacao*/, new GregorianCalendar() /*dtRecebimento*/,
						"" /*nmLocalDestino*/, "Registro do Atendimento",
						documento.getCdSituacaoDocumento(), documento.getCdFase(), null/*nmLocalOrigem*/, null/*nrRemessa*/);
				if (DocumentoTramitacaoDAO.insert(tramitacao, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao tentar incluir tramitação!");
				}
			}
			else { //UPDATE
								
				if(DocumentoDAO.update(documento, connect)<=0) {
					if(isConnectionNull) {
						Conexao.rollback(connect);
					}
					return new Result(-3, "Erro ao tentar atualizar documento.");
				}
				
				cdDocumento = documento.getCdDocumento();
				
				for (DocumentoPessoa documentoPessoa : solicitantes) {
					if(DocumentoPessoaDAO.update(documentoPessoa, connect)<0) {
						if(isConnectionNull) {
							Conexao.rollback(connect);
						}
						return new Result(-2, "Erro ao atualizar lista de solicitantes.");
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			Result result = new Result(cdDocumento, "Salvo com sucesso.");
			result.addObject("DOCUMENTO", documento);
			result.addObject("SETOR", SetorDAO.get(documento.getCdSetor(), connect));
			result.addObject("SITUACAO", SituacaoDocumentoDAO.get(documento.getCdSituacaoDocumento(), connect));
			
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
	 * Método que finaliza o processo de atendimento, lançando ocorrência e tramitações pertinentes
	 * 
	 * @param documento Objedo Documento
	 * @param lgGerarProcesso 0-Não altera tipo e número do documento / 1-Altera tipo e número
	 * @param lgFiscalizacao 0-envia documento para a correspondencia e lanca as pendencias de notificacao/1-envia para a fiscalizacao
	 * @param lgProtocolo 0-envia documento para o protocolo
	 * @return Result com os objetos SETOR, SITUACAO e DOCUMENTO
	 * 
	 * @since 28/05/2015
	 * @author Maurício
	 */
	public static Result finalizarAtendimento(Documento documento, boolean lgGerarProcesso, boolean lgFiscalizacao, boolean lgProtocolo){
		return finalizarAtendimento(documento, lgGerarProcesso, lgFiscalizacao, lgProtocolo, null);
	}
	
	public static Result finalizarAtendimento(Documento documento, boolean lgGerarProcesso, boolean lgFiscalizacao, boolean lgProtocolo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(documento==null)
				return new Result(-1, "Erro ao salvar. Documento é nulo");
			
			documento.setCdSituacaoDocumento(ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ANDAMENTO", 0, 0, connect));
			
			documento.setCdFase(ParametroServices.getValorOfParametroAsInteger("CD_FASE_FINALIZADO", 0, 0, connect));
			
			if(lgGerarProcesso) {
				
				int cdTipoDocumentoProcessoAtendimento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_PROCESSO_ATENDIMENTO", 0, 0, connect);
				if(cdTipoDocumentoProcessoAtendimento<=0) {
					return new Result(-7, "Tipo de Documento padrão de processo não encontrado.");
				}					
				String nrDocumento = TipoDocumentoServices.getNextNumeracao(cdTipoDocumentoProcessoAtendimento, documento.getCdEmpresa(), true, connect);
				
				String nmTipoDocumentoOld = TipoDocumentoDAO.get(documento.getCdTipoDocumento(), connect).getNmTipoDocumento();
				String nmTipoDocumento = TipoDocumentoDAO.get(cdTipoDocumentoProcessoAtendimento, connect).getNmTipoDocumento();
				
				documento.setNrDocumento(nrDocumento);
				documento.setCdTipoDocumentoAnterior(documento.getCdTipoDocumento());
				documento.setCdTipoDocumento(cdTipoDocumentoProcessoAtendimento);
				
				//OCORRENCIA
				int cdOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_MUDANCA_TIPO", 0, 0, connect);
				if(cdOcorrencia<=0) {
					return new Result(-6, "Ocorrência padrão de mudança de tipo não encontrada.");
				}
				DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrencia(documento.getCdDocumento(), 
						cdOcorrencia, 
						0/*cdOcorrencia*/, 
						documento.getCdUsuario(), 
						new GregorianCalendar(), 
						"Documento alterado de "+nmTipoDocumentoOld+ " Nº "+documento.getNrAtendimento() + " para "+
						nmTipoDocumento +" Nº "+nrDocumento, 
						DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO/*tpVisibilidade*/, 0);
				
				if(DocumentoOcorrenciaServices.save(documentoOcorrencia, connect).getCode() <= 0) {
					return new Result(-5, "Erro a lançar ocorrência.");
				}
				
				int cdSetorDestino = 0;
				if (!lgFiscalizacao && !lgProtocolo) {
					
					//PENDENCIAS
//					int cdTipoPendenciaImpressao = ParametroServices.getValorOfParametroAsInteger("CD_TP_PENDENCIA_IMPRESSAO_NOTIFICACAO", 0, 0, connect);
//					if(cdTipoPendenciaImpressao<=0){
//						return new  Result(-10, "Pendência padrão para impressão de notificação não encontrada.");
//					}
//					DocumentoPendencia pendenciaImpressao = new DocumentoPendencia(documento.getCdDocumento(), 
//							cdTipoPendenciaImpressao, new GregorianCalendar()/*dtPendencia*/, null/*dtBaixa*/, 
//							"Imprimir notificação de audiência.", documento.getCdUsuario(), 0/*cdUsuarioBaixa*/, null/*txtBaixa*/);
//					if(DocumentoPendenciaServices.save(pendenciaImpressao, connect).getCode() <= 0){
//						return new Result(-9, "Erro ao lançar pendência de impressão de notificação.");
//					}
//					
//					int cdTipoPendenciaEntrega = ParametroServices.getValorOfParametroAsInteger("CD_TP_PENDENCIA_ENTREGA_NOTIFICACAO", 0, 0, connect);
//					if(cdTipoPendenciaEntrega<=0){
//						return new  Result(-8, "Pendência padrão para entrega de notificação não encontrada.");
//					}
//					DocumentoPendencia pendenciaEntrega = new DocumentoPendencia(documento.getCdDocumento(), 
//							cdTipoPendenciaEntrega, new GregorianCalendar()/*dtPendencia*/, null/*dtBaixa*/, 
//							"Entregar notificação de audiência ao(s) reclamado(s).", documento.getCdUsuario(), 0/*cdUsuarioBaixa*/, null/*txtBaixa*/);
//					if(DocumentoPendenciaServices.save(pendenciaEntrega, connect).getCode() <= 0){
//						return new Result(-7, "Erro ao lançar pendência de entrega de notificação.");
//					}
					
					//Setor destino da tramitacao quando marcado audiencia
					cdSetorDestino = ParametroServices.getValorOfParametroAsInteger("CD_SETOR_DESTINO_TRAMITACAO", 0, 0, connect);
									
				} else if (lgFiscalizacao){	
					//Setor destino da tramitacao quando marcado fiscalizacao
					cdSetorDestino = ParametroServices.getValorOfParametroAsInteger("CD_SETOR_FISCALIZACAO", 0, 0, connect);
				
				} else if (lgProtocolo) {
					//Setor destino da tramitacao quando marcado protocolo
					cdSetorDestino = ParametroServices.getValorOfParametroAsInteger("CD_SETOR_PROTOCOLO", 0, 0, connect);
				}
			
				
				// TRAMITACAO
				int cdSitDocTramitando = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_TRAMITANDO", 0, 0, connect);
				documento.setCdSituacaoDocumento(cdSitDocTramitando);
				
				if(cdSetorDestino<=0) {
					return new Result(-4, "Setor padrão para envio de atendimento não encontrado.");
				}
				DocumentoTramitacao tramitacao = new DocumentoTramitacao(0, documento.getCdDocumento(), cdSetorDestino,
	                    0 /*cdUsuarioDestino*/, documento.getCdSetor() /*cdSetorOrigem*/, documento.getCdUsuario() /*cdUsuarioOrigem*/,
						 new GregorianCalendar() /*dtTramitacao*/, null /*dtRecebimento*/,
						 "" /*nmLocalDestino*/, "Envio automático de registro de atendimento finalizado.",
						 documento.getCdSituacaoDocumento(), documento.getCdFase(), null/*nmLocalOrigem*/, null/*nrRemessa*/);
				
				if(DocumentoTramitacaoServices.insert(tramitacao, connect).getCode()<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-3, "Erro ao tentar incluir tramitação!");
				}
			}
			
			if (DocumentoDAO.update(documento, connect)<=0) {
				if(isConnectionNull) {
					Conexao.rollback(connect);
				}
				return new Result(-2, "Erro ao tentar atualizar registro");
			}
			
			if (isConnectionNull)
				connect.commit();
			
			Result result = new Result(documento.getCdDocumento(), "Salvo com sucesso.");
			result.addObject("DOCUMENTO", documento);
			result.addObject("SETOR", SetorDAO.get(documento.getCdSetor(), connect));
			result.addObject("SITUACAO", SituacaoDocumentoDAO.get(documento.getCdSituacaoDocumento(), connect));
			
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
	 * Busca documentos que possuam alguma pendencia relacionada é notificação
	 * 
	 * @param criterios ArrayList<ItemComparator> cirtérios de busca
	 * @return ResultSetMap lista de documentos
	 */
	public static ResultSetMap getDocumentoNotificacao(ArrayList<ItemComparator> criterios) {
		return getDocumentoNotificacao(criterios, null);
	}
	
	public static ResultSetMap getDocumentoNotificacao(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			//int cdTipoPendencia = 0;
			//int cdTipoPendenciaImpressao = 0;
			int cdDocumentoPessoa = 0;
			int cdCidade = 0;
			int cdAgenda = 0;
			//int cdSetorAtual = 0;
			int noCorrespondencia = 0;
			int cdTipoCorrespodencia = 0;
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("cdTipoPendencia")) {
					//cdTipoPendencia = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdDocumentoPessoa")) {
					cdDocumentoPessoa = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdTipoPendenciaImpressao")) {
					//cdTipoPendenciaImpressao = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdCidade")) {
					cdCidade = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdAgenda")) {
					cdAgenda = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdSetorAtual")) {
					//cdSetorAtual = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("noCorrespondencia")) {
					noCorrespondencia = Integer.valueOf(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
			}

			String sql = "SELECT A.cd_documento, A.nr_documento, A.nr_atendimento, A.txt_documento, A.dt_protocolo, "
					+ " C.dt_inicial, E1.nm_pessoa AS nm_atendente, F.nm_tipo_documento"
					+ " FROM ptc_documento A"				
					+ " LEFT OUTER JOIN agd_agenda_item C ON (A.cd_documento = C.cd_documento "
							+ (cdAgenda>0 ?  "AND C.cd_agenda_item = "+cdAgenda+")" : ")")
					+ " LEFT OUTER JOIN seg_usuario E ON (A.cd_usuario = E.cd_usuario)"
					+ " LEFT OUTER JOIN grl_pessoa E1 ON (E.cd_pessoa = E1.cd_pessoa)"
					+ (cdCidade > 0 ?  " LEFT OUTER JOIN grl_pessoa_endereco E2 ON (E.cd_pessoa = " + cdCidade + ")" : "")
					+ " LEFT OUTER JOIN gpn_tipo_documento F ON (A.cd_tipo_documento = F.cd_tipo_documento) "
					+ " WHERE "
					+ "A.nr_documento IS NOT NULL "
//					+ (cdSetorAtual > 0 ? "AND A.cd_setor_atual = " + cdSetorAtual : "")
					+ (cdDocumentoPessoa > 0 ? " AND D.cd_pessoa = " + cdDocumentoPessoa : "");
			
			ResultSetMap rsm = Search.find(sql, "", criterios, connect, false);
			
			while(rsm.next()) {				
				if(noCorrespondencia < 1){
					cdTipoCorrespodencia = noCorrespondencia == 1 ? 0 : ParametroServices.getValorOfParametroAsInteger("CD_TIPO_ENDERECO_CORRESPONDENCIA", 0);
				}
				ResultSetMap rsmAux = DocumentoPessoaServices.getAllByDocumentoCompleto(rsm.getInt("cd_documento"), cdTipoCorrespodencia,  connect);
				ResultSetMap rsmReclamados = new ResultSetMap();
				ArrayList<String> order = new ArrayList<String>();
				order.add("CD_LOGRADOURO DESC");
				rsmAux.orderBy(order);
				int cdPessoaAux = 0;
				
				while(rsmAux.next()) {
					
					if(cdPessoaAux == rsmAux.getInt("CD_PESSOA")){
						continue;
					} else {
						cdPessoaAux = rsmAux.getInt("CD_PESSOA");
					}					
										
					if(rsmAux.getString("NM_QUALIFICACAO").equalsIgnoreCase("Reclamante")) {
						rsm.setValueToField("NM_RECLAMANTE", rsmAux.getString("NM_PESSOA"));
						rsm.setValueToField("NR_RECLAMANTE_CEP", rsmAux.getString("NR_CEP"));
						rsm.setValueToField("NR_RECLAMANTE_CPF", rsmAux.getString("NR_CPF"));
						rsm.setValueToField("NR_RECLAMANTE_RG", rsmAux.getString("NR_RG"));
						rsm.setValueToField("NM_RECLAMANTE_LOGRADOURO", rsmAux.getString("NM_LOGRADOURO"));
						rsm.setValueToField("NR_RECLAMADO_ENDERECO", rsmAux.getString("NR_ENDERECO"));
						rsm.setValueToField("NM_RECLAMADO_SG_ESTADO", rsmAux.getString("SG_ESTADO"));
						rsm.setValueToField("NR_RECLAMANTE_ENDERECO", rsmAux.getString("NR_ENDERECO"));
						rsm.setValueToField("NM_RECLAMANTE_COMPLEMENTO", rsmAux.getString("NM_COMPLEMENTO"));
						rsm.setValueToField("NM_RECLAMANTE_BAIRRO", rsmAux.getString("NM_BAIRRO"));
						rsm.setValueToField("NM_RECLAMANTE_CIDADE", rsmAux.getString("NM_CIDADE"));
						rsm.setValueToField("NR_RECLAMANTE_TELEFONE", rsmAux.getString("NR_TELEFONE1"));
					}
					else if(rsmAux.getString("NM_QUALIFICACAO").equalsIgnoreCase("Reclamado")) {
						HashMap<String, Object> reg = new HashMap<String, Object>();
						reg.put("CD_RECLAMADO", rsmAux.getString("CD_PESSOA"));
						reg.put("NM_RECLAMADO", rsmAux.getString("NM_PESSOA"));
						reg.put("NR_RECLAMADO_CEP", rsmAux.getString("NR_CEP"));
						reg.put("NR_RECLAMADO_CNPJ", rsmAux.getString("NR_CNPJ"));
						reg.put("NM_RECLAMADO_LOGRADOURO", rsmAux.getString("NM_LOGRADOURO"));
						reg.put("NR_RECLAMADO_ENDERECO", rsmAux.getString("NR_ENDERECO") != null && rsmAux.getString("NR_ENDERECO").equals("") ? "S/N" : rsmAux.getString("NR_ENDERECO"));
						reg.put("NM_RECLAMADO_SG_ESTADO", rsmAux.getString("SG_ESTADO"));
						reg.put("NM_RECLAMADO_COMPLEMENTO", rsmAux.getString("NM_COMPLEMENTO"));
						reg.put("NM_RECLAMADO_BAIRRO", rsmAux.getString("NM_BAIRRO"));
						reg.put("NM_RECLAMADO_CIDADE", rsmAux.getString("NM_CIDADE"));
						reg.put("NR_RECLAMADO_TELEFONE", rsmAux.getString("NR_TELEFONE1"));
						
						rsmReclamados.addRegister(reg);
					}

				}
				
				rsm.setValueToField("RSM_RECLAMADOS", rsmReclamados);
				rsm.setValueToField("LG_CHECKED", 0);
			}
			
			rsm.beforeFirst();
			
			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result executeModeloWeb(int cdModelo, int cdDocumento, int cdEmpresa) {
		return executeModeloWeb(cdModelo, cdDocumento, cdEmpresa, 0, null, null);
	}
	
	public static Result executeModeloWeb(int cdModelo, int cdDocumento, int cdEmpresa, int cdUsuario) {
		return executeModeloWeb(cdModelo, cdDocumento, cdEmpresa, cdUsuario, null, null);
	}
	
	public static Result executeModeloWeb(int cdModelo, int cdDocumento, int cdEmpresa, int cdUsuario, DocumentoOcorrencia ocorrencia) {
		return executeModeloWeb(cdModelo, cdDocumento, cdEmpresa, cdUsuario, ocorrencia, null);
	}

	public static Result executeModeloWeb(int cdModelo, int cdDocumento, int cdEmpresa, int cdUsuario, DocumentoOcorrencia ocorrencia, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModelo, connection);
			if(modelo==null)
				return new Result(-1, "Modelo indicado não existe.");
			
			if(DocumentoDAO.get(cdDocumento, connection)==null)
				return new Result(-1, "Documento indicado não existe.");
			
			//if(EmpresaDAO.get(cdEmpresa, connection)==null)
			//	return new Result(-1, "Empresa indicada não existe.");
			
			String source = new String(modelo.getBlbConteudo(), "UTF-8");
			
			HashMap<String, String> fieldMap = getFieldsMapByDocumento(cdDocumento, cdEmpresa, cdUsuario, ocorrencia, connection);
			for (Object key : fieldMap.keySet().toArray()) {
				
				String v = fieldMap.get(key) == null ? "" : fieldMap.get(key);
				String k = ((String)key).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				
				v = v.replaceAll("\\n|\n|\\r|\r", "&#13;");
				
//				System.out.println("key: "+k+", value: "+v);
				source = source.replace(k, v);
			}
			
			return new Result(1, "Documento executado com sucesso.", "BLB_DOCUMENTO", source.getBytes());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.executeModeloWeb: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result executeModeloNotificacao(int cdModelo, int cdDocumento, int cdEmpresa, int cdUsuario) {
		return executeModeloNotificacao(cdModelo, cdDocumento, cdEmpresa, cdUsuario, null, null);
	}
	
	public static Result executeModeloNotificacao(int cdModelo, int cdDocumento, int cdEmpresa, int cdUsuario, DocumentoOcorrencia ocorrencia, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModelo, connection);
			if(modelo==null)
				return new Result(-1, "Modelo indicado não existe.");
			
			if(DocumentoDAO.get(cdDocumento, connection)==null)
				return new Result(-1, "Documento indicado não existe.");			

			ResultSetMap rsmSolicitantes = getAllSolicitantesOf(cdDocumento);
			rsmSolicitantes.beforeFirst();
			
			String     source = new String(modelo.getBlbConteudo(), "UTF-8");
			JSONArray  append = new JSONArray();
									
			while(rsmSolicitantes.next()){	
				if(rsmSolicitantes.getString("NM_QUALIFICACAO").equals("Reclamado")) {
					String srcEdited      = source;
					
					ResultSetMap p = PessoaServices.findPessoa(rsmSolicitantes.getInt("CD_PESSOA"), cdEmpresa);
					HashMap<String, Object> pessoa = p.getLines().get(0);
					int    cdPessoa = (int)pessoa.get("CD_PESSOA");
					int    gnPessoa = (int)pessoa.get("GN_PESSOA");
					String nrCnpj   = (String)pessoa.get("NR_CNPJ");
					String nrCpf    = (String)pessoa.get("NR_CPF");
					
					Documento documento   = DocumentoDAO.get(cdDocumento);

					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();		
					
					if(pessoa!=null)
						criterios.add(new ItemComparator("A.CD_PESSOA", String.valueOf(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
					
					criterios.add(new ItemComparator("A.LG_PRINCIPAL", "1", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmPessoaEndereco = PessoaEnderecoServices.find(criterios);
					
					/* AUDIENCIA */
					AgendaItem audiencia = null;
					ResultSetMap rsmAgendas = getAgendas(cdDocumento, connection);
					while(rsmAgendas.next()) {
						if(rsmAgendas.getInt("TP_AGENDA_ITEM")==TipoPrazoServices.TP_AUDIENCIA) {
							audiencia = AgendaItemDAO.get(rsmAgendas.getInt("CD_AGENDA_ITEM"), connection);
						}
					}
					
					HashMap<String, String> fieldMap = getFieldsMapByDocumento(cdDocumento, cdEmpresa, cdUsuario, ocorrencia, connection);
					for (Object key : fieldMap.keySet().toArray()) {
						
						String v = fieldMap.get(key) == null ? "" : fieldMap.get(key);
						String k = ((String)key).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
						
						v = v.replaceAll("\"", "&quot;").replaceAll("\\n|\n|\\r|\r", "&#13;");
						
						String txtDocumento = documento.getTxtDocumento()
												.replaceAll("\"", "&quot;")
											    .replaceAll("\\n|\n|\\r|\r", "</span></p> <p textAlign=\\\\\"justify\\\\\" fontFamily=\\\\\"Calibri\\\\\" fontSize=\\\\\"11\\\\\"><span>");
								
						srcEdited = srcEdited.replace("&lt;#Nm_reclamado&gt;", (String)pessoa.get("NM_PESSOA"));
						srcEdited = srcEdited.replace("&lt;#CPFCNPJ_reclamado&gt;", gnPessoa == 0 ? "CNPJ: "+Util.formatCnpj(nrCnpj) : "CPF: "+Util.formatCpf(nrCpf));
						srcEdited = srcEdited.replace("&lt;#Data_impressao&gt;", Util.formatDate(new GregorianCalendar(), "dd 'de' MMMM 'de' yyyy"));
						srcEdited = srcEdited.replace("&lt;#Txt_documento&gt;", txtDocumento);
						srcEdited = srcEdited.replace("&lt;#Nr_documento&gt;", documento.getNrDocumento() != null ? documento.getNrDocumento() : "");
						srcEdited = srcEdited.replace("&lt;#Nm_endereco_reclamado&gt;", (String) rsmPessoaEndereco.getLines().get(0).get("NM_ENDERECO"));
						srcEdited = srcEdited.replace("&lt;#Dt_audiencia&gt;", (audiencia!=null ? Util.formatDate(audiencia.getDtInicial(), "dd/MM/yyyy") : ""));
						srcEdited = srcEdited.replace("&lt;#Hr_audiencia&gt;", (audiencia!=null ? Util.formatDate(audiencia.getDtInicial(), "HH:mm") : ""));
						
						srcEdited = srcEdited.replace(k, v);
					}				
					append.put(srcEdited);
				}
			}
			
			System.out.println(append.toString());
			
			return new Result(1, "Documento executado com sucesso.", "BLB_DOCUMENTO", append.toString().getBytes());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.executeModeloWeb: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static HashMap<String, String> getFieldsMapByDocumento(int cdDocumento, int cdEmpresa, int cdUsuario, DocumentoOcorrencia ocorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			HashMap<String, String> fieldMap = new HashMap<String, String>();
			
			/* EMPRESA */
			fieldMap.put("<#Url_portal>", ParametroServices.getValorOfParametroAsString("NM_URL_PORTAL", ""));
			if(cdUsuario>0) {
				Pessoa p = PessoaDAO.get(UsuarioDAO.get(cdUsuario, connect).getCdPessoa(), connect);
				
				fieldMap.put("<#Nome_usuario>", p.getNmPessoa());
			}
			
			
			/* GERAL */			
			fieldMap.put("<#Data_impressao>", Util.formatDate(new GregorianCalendar(), "dd 'de' MMMM 'de' yyyy"));
			
			PessoaEndereco endereco = PessoaEnderecoServices.getEnderecoPrincipal(cdEmpresa, connect);
			if(endereco!=null) {
				Cidade cidadeEndereco = CidadeDAO.get(endereco.getCdCidade(), connect);
				if(cidadeEndereco!=null)
					fieldMap.put("<#Cidade>", cidadeEndereco.getNmCidade());
			}
			
			/* DOCUMENTO */
			Documento documento = DocumentoDAO.get(cdDocumento, connect);
			
			/* AUDIENCIA */
			AgendaItem audiencia = null;
			ResultSetMap rsmAgendas = getAgendas(cdDocumento, connect);
			while(rsmAgendas.next()) {
				if(rsmAgendas.getInt("TP_AGENDA_ITEM")==TipoPrazoServices.TP_AUDIENCIA) {
					audiencia = AgendaItemDAO.get(rsmAgendas.getInt("CD_AGENDA_ITEM"), connect);
				}
			}
			
			if(documento!=null) {
				
				Setor setor = SetorDAO.get(documento.getCdSetor(), connect);
				Setor setorAtual = SetorDAO.get(documento.getCdSetorAtual());
				
				Fase fase = FaseDAO.get(documento.getCdFase(), connect);
				SituacaoDocumento situacao = SituacaoDocumentoDAO.get(documento.getCdSituacaoDocumento(), connect);
				
				fieldMap.put("<#Nr_documento>", documento.getNrDocumento());
				fieldMap.put("<#Dt_protocolo>", Util.formatDate(documento.getDtProtocolo(), "dd/MM/yyyy"));
				fieldMap.put("<#Nm_tipo_documento>", TipoDocumentoDAO.get(documento.getCdTipoDocumento(), connect).getNmTipoDocumento());
				fieldMap.put("<#Nm_setor>", setor!=null ? setor.getNmSetor() : "");
				fieldMap.put("<#Nm_setor_atual>", setorAtual!=null ? setorAtual.getNmSetor() : "");
				fieldMap.put("<#Nm_fase>", fase !=null ? fase.getNmFase() : "");
				fieldMap.put("<#Nm_situacao>", situacao!=null ? situacao.getNmSituacaoDocumento() : "");
				fieldMap.put("<#Ds_assunto>", documento.getDsAssunto());
				fieldMap.put("<#Txt_documento>", documento.getTxtDocumento());
				
				/**
				 * PROCESSO
				 */
				Processo processo = ProcessoDAO.get(documento.getCdProcesso(), connect);
				fieldMap.put("<#Nr_processo>", (processo!=null?processo.getNrProcesso():""));
				
				/**
				 * PESSOAS
				 */
				String nmContratante = "";
				String nrCpfContratante = "";
				
				String nmSolicitante = "";
				String nrCpfSolicitante = "";
				
				String nmReclamante = "";
				String nrCpfReclamante = "";
				String nrTelefoneReclamante = "";
				String nrCelularReclamante = "";
				String nmEnderecoReclamante = "";
				
				String nmReclamado = "";
				String nrCpfReclamado = "";
				String nrTelefoneReclamado = "";
				String nrCelularReclamado = "";
				String nmEnderecoReclamado = "";
				
				ResultSetMap rsm = DocumentoPessoaServices.getAllByDocumentoCompleto(documento.getCdDocumento(), connect);
				while(rsm.next()) {
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();					
					Pessoa p = PessoaDAO.get(rsm.getInt("CD_PESSOA"), connect);

					criterios.add(new ItemComparator("A.CD_PESSOA", String.valueOf(p.getCdPessoa()), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.LG_PRINCIPAL", "1", ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmPessoaEndereco = PessoaEnderecoServices.find(criterios);
					
					if(rsm.getString("NM_QUALIFICACAO").equalsIgnoreCase("Contratante")) {
						nmContratante = p.getNmPessoa();
						if(p.getGnPessoa()==PessoaServices.TP_FISICA) {
							PessoaFisica pf = PessoaFisicaDAO.get(p.getCdPessoa(), connect);
							nrCpfContratante = pf!=null ? Util.formatCpf(pf.getNrCpf()) : "";
						}
						else {
							PessoaJuridica pj = PessoaJuridicaDAO.get(p.getCdPessoa(), connect);
							nrCpfContratante = pj!=null ? Util.formatCnpj(pj.getNrCnpj()) : "";
						}
						
					}
					else if(rsm.getString("NM_QUALIFICACAO").equalsIgnoreCase("Solicitante")) {
						nmSolicitante = p.getNmPessoa();
						if(p.getGnPessoa()==PessoaServices.TP_FISICA) {
							PessoaFisica pf = PessoaFisicaDAO.get(p.getCdPessoa(), connect);
							nrCpfSolicitante = pf!=null ? Util.formatCpf(pf.getNrCpf()) : "";
						}
						else {
							PessoaJuridica pj = PessoaJuridicaDAO.get(p.getCdPessoa(), connect);
							nrCpfSolicitante = pj!=null ? Util.formatCnpj(pj.getNrCnpj()) : "";
						}
					}
					else if(rsm.getString("NM_QUALIFICACAO").equalsIgnoreCase("Reclamante")) {						
						nmReclamante = p.getNmPessoa();
						nrTelefoneReclamante = p.getNrTelefone1();
						nrCelularReclamante = p.getNrCelular();
						nmEnderecoReclamante = (String) rsmPessoaEndereco.getLines().get(0).get("NM_ENDERECO");
						if(p.getGnPessoa()==PessoaServices.TP_FISICA) {
							PessoaFisica pf = PessoaFisicaDAO.get(p.getCdPessoa(), connect);
							nrCpfReclamante = pf!=null ? Util.formatCpf(pf.getNrCpf()) : "";
						}
						else {
							PessoaJuridica pj = PessoaJuridicaDAO.get(p.getCdPessoa(), connect);
							nrCpfReclamante = pj!=null ? Util.formatCnpj(pj.getNrCnpj()) : "";
						}
					}
					else if(rsm.getString("NM_QUALIFICACAO").equalsIgnoreCase("Reclamado")) {
						nmReclamado = p.getNmPessoa();
						nrTelefoneReclamado = p.getNrTelefone1();
						nrCelularReclamado = p.getNrCelular();
						nmEnderecoReclamado = (String) rsmPessoaEndereco.getLines().get(0).get("NM_ENDERECO");
						if(p.getGnPessoa()==PessoaServices.TP_FISICA) {
							PessoaFisica pf = PessoaFisicaDAO.get(p.getCdPessoa(), connect);
							nrCpfReclamado = pf!=null ? Util.formatCpf(pf.getNrCpf()) : "";
						}
						else {
							PessoaJuridica pj = PessoaJuridicaDAO.get(p.getCdPessoa(), connect);
							nrCpfReclamado = pj!=null ? Util.formatCnpj(pj.getNrCnpj()) : "";
						}
					}
				}
				fieldMap.put("<#Nm_contratante>", nmContratante);
				fieldMap.put("<#CPFCNPJ_contratante>", nrCpfContratante);
				
				fieldMap.put("<#Nm_solicitante>", nmSolicitante);
				fieldMap.put("<#CPFCNPJ_solicitante>", nrCpfSolicitante);
				
				fieldMap.put("<#Nm_reclamante>", nmReclamante);
				fieldMap.put("<#CPFCNPJ_reclamante>", nrCpfReclamante);
				fieldMap.put("<#Telefone_reclamante>", nrTelefoneReclamante);
				fieldMap.put("<#Celular_reclamante>", nrCelularReclamante);
				fieldMap.put("<#Endereco_reclamante>", nmEnderecoReclamante);
				
				fieldMap.put("<#Nm_reclamado>", nmReclamado);
				fieldMap.put("<#CPFCNPJ_reclamado>", nrCpfReclamado);
				fieldMap.put("<#Telefone_reclamado>", nrTelefoneReclamado);
				fieldMap.put("<#Celular_reclamado>", nrCelularReclamado);
				fieldMap.put("<#Endereco_reclamado>", nmEnderecoReclamado);
				
				/**
				 * AUDIENCIA
				 */
				fieldMap.put("<#Dt_audiencia>", (audiencia!=null ? Util.convCalendarStringCompleto(audiencia.getDtFinal()) : ""));
				fieldMap.put("<#Dt_audiencia_ddmmaaaa>", (audiencia!=null ? Util.formatDate(audiencia.getDtFinal(), "dd/MM/yyyy") : ""));
				fieldMap.put("<#Dia_audiencia_extenso>", (audiencia!=null ? Util.formatExtensoOrdinal(audiencia.getDtFinal().get(Calendar.DAY_OF_MONTH)) : ""));
				fieldMap.put("<#Mes_audiencia_extenso>", (audiencia!=null ? new SimpleDateFormat("MMMMM").format(new java.util.Date(audiencia.getDtFinal().getTimeInMillis())).toLowerCase() : ""));
				fieldMap.put("<#Ano_audiencia_extenso>", (audiencia!=null ? Util.formatExtenso(audiencia.getDtFinal().get(Calendar.YEAR), false) : ""));
				
				/**
				 * OCORRENCIA
				 */
				if(ocorrencia!=null) {
					
					fieldMap.put("<#Data_ocorrencia>", Util.formatDate(ocorrencia.getDtOcorrencia(), "dd/MM/yyyy"));
					
					TipoOcorrencia tipo = TipoOcorrenciaDAO.get(ocorrencia.getCdTipoOcorrencia(), connect);
					if(tipo!=null)
						fieldMap.put("<#Tipo_ocorrencia>", tipo.getNmTipoOcorrencia());
					
					fieldMap.put("<#Detalhe_ocorrencia>", ocorrencia.getTxtOcorrencia());
				}
				
			}
			
			return fieldMap;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.getFieldsMapByProcesso: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getReclamantes(int cdDocumento) {
		return getReclamantes(cdDocumento, null);
	}
	
	public static String getReclamantes(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = getAllSolicitantesOf(cdDocumento, connect);
			rsm.beforeFirst();
			ResultSetMap rsmReclamantes = new ResultSetMap();
			while(rsm.next()) {
				if(rsm.getString("NM_QUALIFICACAO").equalsIgnoreCase("Reclamante"))
					rsmReclamantes.addRegister(rsm.getRegister());
			}
			
			String reclamantes = "";
			while(rsmReclamantes.next()) {
				if(rsmReclamantes.getString("NM_QUALIFICACAO").equalsIgnoreCase("Reclamante"))
					reclamantes+=rsmReclamantes.getString("NM_PESSOA");
				
				if(rsmReclamantes.hasMore())
					reclamantes += ", ";
			}
			
			return reclamantes;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.getReclamantes: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static String getReclamados(int cdDocumento) {
		return getReclamados(cdDocumento, null);
	}
	
	public static String getReclamados(int cdDocumento, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = getAllSolicitantesOf(cdDocumento, connect);
			rsm.beforeFirst();
			
			ResultSetMap rsmReclamados = new ResultSetMap();
			while(rsm.next()) {
				if(rsm.getString("NM_QUALIFICACAO").equalsIgnoreCase("Reclamado"))
					rsmReclamados.addRegister(rsm.getRegister());
			}
			
			String reclamados = "";
			while(rsmReclamados.next()) {
				if(rsmReclamados.getString("NM_QUALIFICACAO").equalsIgnoreCase("Reclamado"))
					reclamados+=rsmReclamados.getString("NM_PESSOA");
					
				if(rsmReclamados.hasMore())
					reclamados += ", ";
			}
			
			return reclamados;
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.getReclamantes: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * 
	 * @param nrAtendimento
	 * @param nrDocumento
	 * @return
	 */
	public static Result getDocumentoOcorrencias(String nrDocumento) {
		return getDocumentoOcorrencias(nrDocumento, null);
	}
	
	public static Result getDocumentoOcorrencias(String nrDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
//			int cdTipoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_RECLAMACAO", 0, 0, connect);
//			if(cdTipoDocumento<=0) {
//				return new Result(-2, "DocumentoServices.getDocumentoOcorrencias: Erro ao buscar tipo de documento de reclamação.");
//			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_fase, D.NM_PESSOA AS NM_SOLICITANTE, G.NM_SITUACAO_DOCUMENTO FROM ptc_documento A "
															 + " LEFT OUTER JOIN ptc_fase B ON (A.cd_fase = B.cd_fase)"
															 + " LEFT OUTER JOIN ptc_documento_pessoa C ON (A.cd_documento = C.cd_documento AND C.nm_qualificacao = 'Reclamante')"
															 + " LEFT OUTER JOIN grl_pessoa D ON (C.cd_pessoa = D.cd_pessoa)"
															 + " LEFT OUTER JOIN ptc_documento_pessoa E ON (A.cd_documento = E.cd_documento AND C.nm_qualificacao = 'Reclamado')"
															 + " LEFT OUTER JOIN grl_pessoa F ON (E.cd_pessoa = F.cd_pessoa)"
															 + " LEFT OUTER JOIN ptc_situacao_documento G ON (A.cd_situacao_documento = G.cd_situacao_documento)"
															 + " WHERE (A.nr_documento LIKE '"+nrDocumento+"'"
															 		+ "OR A.nr_atendimento LIKE '"+nrDocumento+"')");
			ResultSetMap rsmDocumento = new ResultSetMap(pstmt.executeQuery());
			
			if(!rsmDocumento.next())
				return new Result(-3, "Registro não encontrado.");
			int cdDocumento = rsmDocumento.getInt("CD_DOCUMENTO");
			rsmDocumento.beforeFirst();
			
			ResultSetMap rsmOcorrencias = DocumentoOcorrenciaServices.getAllByDocumento(cdDocumento, connect);	
			ResultSetMap rsmAgendas = getAgendas(cdDocumento, connect);			
			ResultSetMap rsmTramites = getAllTramites(cdDocumento, connect);			
			ResultSetMap rsmAnexados = DocumentacaoServices.getAllByDocumento(cdDocumento);
			
			Result result = new Result(1, "");
			result.addObject("DOCUMENTO", rsmDocumento);
			result.addObject("OCORRENCIAS", rsmOcorrencias);
			result.addObject("AGENDAS", rsmAgendas);
			result.addObject("TRAMITES", rsmTramites);
			result.addObject("ANEXADOS", rsmAnexados);
			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Buscando informações sobre o documento e tratando os dados para que aparecerão apenas os que serão usados durante o processo.
	 * @author Edgard Hufelande
	 * @param String nrDocumento
	 * @return
	 */
	
	public static String getDocumentoJSON(String nrDocumento) {
		try {
			Result result       = DocumentoServices.getDocumentoOcorrencias(nrDocumento);
			JSONArray JSONArray = new JSONArray();
			
			if(result.getCode() > 0) {
				
				ResultSetMap rsmOcorrencias = (ResultSetMap) result.getObjects().get("OCORRENCIAS");
				ResultSetMap rsmDocumento   = (ResultSetMap) result.getObjects().get("DOCUMENTO");
				ResultSetMap rsmAgendas     = (ResultSetMap) result.getObjects().get("AGENDAS");
				ResultSetMap rsmTramites    = (ResultSetMap) result.getObjects().get("TRAMITES");
				ResultSetMap rsmAnexados    = (ResultSetMap) result.getObjects().get("ANEXADOS");
				
				JSONObject objDocumento   = new JSONObject();
				JSONObject objOcorrencias = new JSONObject();
				JSONObject objAgendas     = new JSONObject();
				JSONObject objTramites    = new JSONObject();
				JSONObject objAnexados    = new JSONObject();
				
				ArrayList<HashMap<String, Object>> documentos  = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> ocorrencias = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> agendas     = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> tramites    = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> anexos      = new ArrayList<HashMap<String, Object>>();
				
				/**
				 *  FILTRANDO informações SOBRE OS DOCUMENTOS RETORNADOS PELO RSM
				 */
				while(rsmDocumento.next()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NR_DOCUMENTO", rsmDocumento.getString("NR_DOCUMENTO"));
					register.put("DS_ASSUNTO", rsmDocumento.getString("DS_ASSUNTO"));
					register.put("NM_FASE", rsmDocumento.getString("NM_FASE"));
					register.put("NM_SOLICITANTE", rsmDocumento.getString("NM_SOLICITANTE"));
					documentos.add(register);
				}
				objDocumento.put("DOCUMENTO", documentos);
				
				/**
				 *  FILTRANDO informações SOBRE AS OCORRENCIAS RETORNADOS PELO RSM
				 */
				while(rsmOcorrencias.next()){
					if(rsmOcorrencias.getInt("TP_VISIBILIDADE") != 1) {
						rsmOcorrencias.setValueToField("DT_OCORRENCIA", rsmOcorrencias.getDateFormat("DT_OCORRENCIA", "dd/MM/yyyy HH:mm"));
						
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NM_TIPO_OCORRENCIA", rsmOcorrencias.getString("NM_TIPO_OCORRENCIA"));
						register.put("TXT_OCORRENCIA", rsmOcorrencias.getString("TXT_OCORRENCIA"));
						register.put("DT_OCORRENCIA", rsmOcorrencias.getString("DT_OCORRENCIA"));
						
						ocorrencias.add(register);
					}					
				}
				objOcorrencias.put("OCORRENCIAS", ocorrencias);
				
				/**
				 *  FILTRANDO informações SOBRE AS AGENDAS RETORNADOS PELO RSM
				 */
				while(rsmAgendas.next()){
					rsmAgendas.setValueToField("DT_INICIAL", rsmAgendas.getDateFormat("DT_INICIAL", "dd/MM/yyyy HH:mm"));
					rsmAgendas.setValueToField("NM_RESPONSAVEL", rsmAgendas.getString("NM_PESSOA"));
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_LOCAL", rsmAgendas.getString("NM_LOCAL"));
					register.put("DT_INICIAL", rsmAgendas.getString("DT_INICIAL"));
					
					agendas.add(register);
				}
				objAgendas.put("AGENDAS", agendas);
								
				/**
				 *  FILTRANDO informações SOBRE OS TRAMITES RETORNADOS PELO RSM
				 */
				while(rsmTramites.next()){
					rsmTramites.setValueToField("DT_TRAMITACAO", rsmTramites.getDateFormat("DT_TRAMITACAO", "dd/MM/yyyy HH:mm"));
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_SETOR_ORIGEM", rsmTramites.getString("NM_SETOR_ORIGEM"));
					register.put("NM_SETOR_DESTINO", rsmTramites.getString("NM_SETOR_DESTINO"));
					register.put("DT_TRAMITACAO", rsmTramites.getString("DT_TRAMITACAO"));
					
					tramites.add(register);
				}
				objTramites.put("TRAMITES", tramites);
				
				
				/**
				 *  FILTRANDO informações SOBRE OS ANEXOS RETORNADOS PELO RSM
				 */
				while(rsmAnexados.next()){
					rsmAnexados.setValueToField("DT_RECEPCAO", rsmAnexados.getDateFormat("DT_RECEPCAO", "dd/MM/yyyy HH:mm"));
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_TIPO_DOCUMENTACAO", rsmAnexados.getString("NM_TIPO_DOCUMENTACAO"));
					register.put("DT_RECEPCAO", rsmAnexados.getString("DT_RECEPCAO"));
					
					anexos.add(register);
				}
				objAnexados.put("ANEXADOS", anexos);

				JSONArray.put(objDocumento);
				JSONArray.put(objOcorrencias);
				JSONArray.put(objAgendas);
				JSONArray.put(objTramites);
				JSONArray.put(objAnexados);
				
			}
			
			return JSONArray.toString(1);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		
	}
	
	public static ResultSetMap getAllAssuntosEel() {
		return getAllAssuntosEel(null);
	}
	
	public static ResultSetMap getAllAssuntosEel(Connection connect) {
		return EelUtils.getAllAssuntos(connect);
	}
	
	public static Result sincronizarDocumentos() {
		return sincronizarDocumentos(null, null, 0, null, null, null);
	}
	
	public static Result sincronizarDocumentos(int cdEmpresa) {
		return sincronizarDocumentos(null, null, cdEmpresa, null, null, null);
	}
	
	public static Result sincronizarDocumentos(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return sincronizarDocumentos(null, null, cdEmpresa, dtInicial, dtFinal, null);
	}
	
	public static Result sincronizarDocumentos(String nrDocumentoExterno, String ano, int cdEmpresa, GregorianCalendar dtInicial, 
			GregorianCalendar dtFinal) {
		return sincronizarDocumentos(nrDocumentoExterno, ano, cdEmpresa, dtInicial, dtFinal, null);
	}

	/**
	 * Busca documentos que tramitaram para os setores internos
	 * 
	 * @param cdEmpresa
	 * @param dtInicial
	 * @param dtFinal
	 * @param connect
	 * @return
	 * 
	 * @category EeL
	 */
	public static Result sincronizarDocumentos(String nrDocumentoExterno, String ano, int cdEmpresa, GregorianCalendar dtInicial, 
			GregorianCalendar dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		GregorianCalendar dtSinc = new GregorianCalendar();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(false);
			
			com.tivic.manager.seg.Acao acao = AcaoServices.getByIdAcao("SINC_PROTOCOLO_EEL", connect);
			int cdUsuarioExterno = ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_EXTERNO_PTC", 0, 0, connect);
			
			PreparedStatement pstmt = null;
			Result result = new Result(1, "Nenhum documento importado.");
			
			if(dtInicial==null) {
				pstmt = connect.prepareStatement("SELECT max(dt_execucao) as dt_inicial FROM log_execucao_acao"
												+ " WHERE cd_acao = "+acao.getCdAcao()
												+ " AND cd_modulo = "+acao.getCdModulo()
												+ " AND cd_sistema = "+acao.getCdSistema());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					dtInicial = Util.convTimestampToCalendar(rs.getTimestamp("dt_inicial"));
				}
				
				if(dtInicial==null) {
					dtInicial = new GregorianCalendar();
				}
			}
			dtInicial.add(Calendar.DAY_OF_MONTH, -1);

			if(dtFinal==null) {
				//caso não exista data final, ela serão a data chamada do método
				dtFinal = new GregorianCalendar();
			}
			dtFinal.add(Calendar.HOUR, 1);
			
			ResultSetMap rsmDocumentos = EelUtils.getDocumentos(nrDocumentoExterno, ano, dtInicial, dtFinal);
			Documento docResult = null;
			int cdFase = 0;
				
			//int count=0;
			while(rsmDocumentos.next()) {
				// VERIFICAR EXISTENCIA DO DOCUMENTO NO BANCO LOCAL
				pstmt = connect.prepareStatement("SELECT cd_documento, nr_documento, nr_ano_externo "
											   + " FROM ptc_documento "
											   + " WHERE nr_documento_externo = '"+rsmDocumentos.getString("cd_documento")+"'");
				ResultSet rs = pstmt.executeQuery();
				int cdDocumento = 0;
				String nrDocumento = null;
				//String nrAno = null;
				if(rs.next()) {
					cdDocumento = rs.getInt("cd_documento");
					nrDocumento = rs.getString("nr_documento");
					continue;
				}
				rs.close();
				
				boolean isInsert = cdDocumento==0;
				
				int cdSetor = SetorServices.getSetorBySetorEel(rsmDocumentos.getString("cd_setor"), connect);
				//CASO O SETOR NÃO  EXISTA
				if(cdSetor<=0) {
					if(rsmDocumentos.getString("nm_setor")!=null) {
						Setor setor = new Setor(0, 0, cdEmpresa, 0, rsmDocumentos.getString("nm_setor"), SetorServices.ST_ATIVO, null, null, 
								null, null, null, null, null, 0, null, null, null, SetorServices.TP_SETOR_EXTERNO, 
								0, rsmDocumentos.getString("cd_setor"));
						
						result = SetorServices.save(setor, connect);
						if(result.getCode()<=0) {
							connect.rollback();
							return result;
						}
						else {
							cdSetor = result.getCode();
						}
					}
					else {
						cdSetor = ParametroServices.getValorOfParametroAsInteger("CD_SETOR_DESCONHECIDO", 0, 0, connect);
					}
				}
				
				GregorianCalendar dtProtocolo = rsmDocumentos.getGregorianCalendar("dt_protocolo");
				
				String txtObservacao = "";
				if(rsmDocumentos.getObject("txt_observacao")!=null) {
					Clob clobTxtObservacao = (Clob)rsmDocumentos.getObject("txt_observacao");
					Reader r = clobTxtObservacao.getCharacterStream();
					
					int c = 0;
					while((c = r.read())!=-1) {
						txtObservacao+=(char)c;
					}
					r.close();
				}
				
				// TIPO DE DOCUMENTO
				int cdTipoDocumento = TipoDocumentoServices.getTipoDocumentoByTipoDocumentoEel(rsmDocumentos.getString("cd_tipo_documento"), connect);
				if(cdTipoDocumento<=0) {
					cdTipoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_NAO_CLASSIFICADO", 0, 0, connect);
				}
				if(cdTipoDocumento<=0) {	
					LogUtils.debug("DocumentoServices.sincronizarDocumentos");
					LogUtils.debug("\tcontrole: "+rsmDocumentos.getString("cd_documento"));
					LogUtils.debug("\tcdTipoDocumento: "+cdTipoDocumento);
					LogUtils.debug("\tNão importado");
					continue;
				}
				
				if(nrDocumento==null) 
					nrDocumento = rsmDocumentos.getString("nr_documento")+"/"+rsmDocumentos.getString("nr_ano");
				
				int cdPessoaUsuario = 0;
				pstmt = connect.prepareStatement("SELECT cd_pessoa FROM grl_pessoa_externa WHERE cd_pessoa_externa = '"+rsmDocumentos.getString("cd_pessoa_usuario")+"'");
				rs = pstmt.executeQuery();
				if(rs.next()) {
					cdPessoaUsuario = rs.getInt("cd_pessoa");
				}
				else {
					ResultSetMap rsmPessoa = EelUtils.getGeral(rsmDocumentos.getString("cd_pessoa_usuario"));
					if(rsmPessoa.next()) {
						Pessoa p = new Pessoa(0, 0, 1, rsmPessoa.getString("nm_pessoa"), null, null, null, null, null, new GregorianCalendar(), 
								PessoaServices.TP_FISICA, null, PessoaServices.ST_ATIVO, 
								null, null, null, 0, null, 0, 0, null);
						
						cdPessoaUsuario = PessoaServices.save(p, null, 0, 0, connect).getCode();
						
						if(cdPessoaUsuario>0) {
							pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_externa(cd_pessoa, cd_pessoa_externa)"
									+ " VALUES (?,?)");
							pstmt.setInt(1, cdPessoaUsuario);
							pstmt.setString(2, rsmDocumentos.getString("cd_pessoa_usuario"));
							
							pstmt.executeUpdate();
						}
					}
					
				}
				int cdUsuario = 0;
				Usuario usuario = UsuarioServices.getByPessoa(cdPessoaUsuario, connect);
				if(usuario!=null) {
					cdUsuario = usuario.getCdUsuario();
				}
				else {
					cdUsuario = cdUsuarioExterno;
				}
				
				Documento doc = new Documento(cdDocumento, 0, cdSetor, cdUsuario, null, dtProtocolo, 0, txtObservacao, null, 
						nrDocumento, cdTipoDocumento, 0, 0, txtObservacao, 0, ST_PARADO, cdFase, cdEmpresa, 0, TP_PRIORIDADE_NORMAL, 
						0, txtObservacao, null, 0, 0, rsmDocumentos.getString("cd_documento"), rsmDocumentos.getString("cd_assunto"), 
						nrDocumento, rsmDocumentos.getString("nr_ano"), Integer.valueOf(rsmDocumentos.getString("tp_documento_externo")),
						rsmDocumentos.getInt("tp_interno_externo"));
				
				//LogUtils.debug(">>> DOCUMENTO: "+doc);
				
				int cdSolicitante = 0;
				pstmt = connect.prepareStatement("SELECT cd_pessoa FROM grl_pessoa_externa WHERE cd_pessoa_externa = '"+rsmDocumentos.getString("cd_solicitante")+"'");
				rs = pstmt.executeQuery();
				if(rs.next()) {
					cdSolicitante = rs.getInt("cd_pessoa");
				}
				else {
					ResultSetMap rsmPessoa = EelUtils.getGeral(rsmDocumentos.getString("cd_solicitante"));
					if(rsmPessoa.next()) {
						Pessoa p = new Pessoa(0, 0, 1, rsmPessoa.getString("nm_pessoa"), null, null, null, null, null, new GregorianCalendar(), 
								PessoaServices.TP_FISICA, null, PessoaServices.ST_ATIVO, 
								null, null, null, 0, null, 0, 0, null);
						
						cdSolicitante = PessoaServices.save(p, null, 0, 0, connect).getCode();
						
						if(cdSolicitante>0) {
							pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_externa(cd_pessoa, cd_pessoa_externa)"
									+ " VALUES (?,?)");
							pstmt.setInt(1, cdSolicitante);
							pstmt.setString(2, rsmDocumentos.getString("cd_solicitante"));
							
							pstmt.executeUpdate();
						}
					}
				}
				
				result = save(doc, null, connect);
				if(result.getCode()<=0) {
					if(result.getCode()==-6) {
						LogUtils.debug("DocumentoServices.sincronizarDocumentos");
						LogUtils.debug("\tcall save()");
						LogUtils.debug("\tresult.getCode(): "+result.getCode());
						LogUtils.debug("\tDocumento já existe na base.");
						continue;
					}
					else {
						connect.rollback();
						return result;
					}
				}
				
				docResult = (Documento)result.getObjects().get("DOCUMENTO");
				
				//LogUtils.debug(">>> docResult: "+docResult);
				
				// INSERIR ENVOLVIDOS
				pstmt = connect.prepareStatement("INSERT INTO ptc_documento_pessoa (cd_documento,"+
                        "cd_pessoa,nm_qualificacao) VALUES (?, ?, ?)");
				if(docResult.getCdDocumento()==0)
					pstmt.setNull(1, Types.INTEGER);
				else
					pstmt.setInt(1,docResult.getCdDocumento());
				if(cdSolicitante==0)
					pstmt.setNull(2, Types.INTEGER);
				else
					pstmt.setInt(2,cdSolicitante);
				pstmt.setString(3, "Solicitante");
				//int r = pstmt.executeUpdate();
				
				
				if(isInsert) {
					//OCORRência
					StringBuilder sb = new StringBuilder();
					sb.append(rsmDocumentos.getString("nm_tipo_documento"));
					sb.append(" Não ");
					sb.append(rsmDocumentos.getString("nr_documento"));
					sb.append("\n");
					sb.append("Solicitante: ");
					sb.append(rsmDocumentos.getString("nm_solicitante"));
					sb.append("\n");
					sb.append(txtObservacao);
					
					
					int cdTipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_SINC", 0, 0, connect);
					DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia(docResult.getCdDocumento(), 
							cdTipoOcorrencia, 0, cdUsuarioExterno, new GregorianCalendar(), 
							sb.toString(), 
							DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO, 0);
					DocumentoOcorrenciaServices.save(ocorrencia, connect);
				}
				
				//count++;
			}
			
			if(isConnectionNull) {
				connect.commit(); 
				Conexao.desconectar(connect);
			}
			
			// TRAMITAÇÕES
			Connection conTramitacao = Conexao.conectar();
			conTramitacao.setAutoCommit(false);
			result = sincronizarTramitacoes(0, nrDocumentoExterno, dtInicial, dtFinal, conTramitacao);
			if(result.getCode()<=0) {
				conTramitacao.rollback();
				return result;
			}
			
			//LOG
			if(nrDocumentoExterno==null) {
				Modulo modulo = ModuloServices.getModuloById("ptc", conTramitacao);
				ExecucaoAcao log = new ExecucaoAcao(0, 
						acao.getCdAcao(), 
						modulo.getCdModulo(), 
						modulo.getCdSistema(), 0, 
						dtSinc, 
						"", //txtExecucao 
						"", //txtResultadoExecucao
						1);//tpResultadoExecucao);
				
				Result rLog = ExecucaoAcaoServices.save(log, conTramitacao);
				
				if(rLog.getCode()<=0) {
					System.out.println("Erro ao gravar log. " +rLog.getMessage());
				}
			}
			
			if(!conTramitacao.getAutoCommit())
				conTramitacao.commit();
			Conexao.desconectar(conTramitacao);
			
			/*
			 * ANEXOS
			 */
			Connection conAnexo = Conexao.conectar();
			conAnexo.setAutoCommit(false);
			rsmDocumentos.beforeFirst();
			result = sincronizarAnexos(rsmDocumentos, conAnexo);
			if(result.getCode()<=0) {
				conAnexo.rollback();
				return result;
			}
			conAnexo.commit();
			Conexao.desconectar(conAnexo);
			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca tramitações (andamentos no banco da E&L)
	 * 
	 * @important este método retorna listas diferentes de acordo com os parâmetros passados
	 * 
	 * @param cdDocumento
	 * @param nrDocumentoExterno
	 * @param dtInicial
	 * @param dtFinal
	 * @return
	 * 
	 * @category E&L
	 */
	public static Result sincronizarTramitacoes(int cdDocumento, String nrDocumentoExterno, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return sincronizarTramitacoes(cdDocumento, nrDocumentoExterno, dtInicial, dtFinal, null);
	}
	
	public static Result sincronizarTramitacoes(int cdDocumento, String nrDocumentoExterno, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
//				connect.setAutoCommit(false);
			}			
			
			ResultSetMap rsmTramites = EelUtils.getTramitacao(nrDocumentoExterno, dtInicial, dtFinal);
			Result result = null;
			int cdFase = 0;
			int cdUsuarioExterno = ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_EXTERNO_PTC", 0, 0, connect);
			Documento documento = null;
			
			//VERIFICAR IGUALDADE DE NUMERO DE TRAMITAÇÕES ENTRE OS BANCOS
//			int qtTramitacoesExternas = rsmTramites.size();
//			ResultSetMap rsmTramitesInternos = DocumentoTramitacaoServices.getTramitacoes(cdDocumento, connect);
//			int qtTramitacoesInternas = rsmTramitesInternos.size();
//			if(qtTramitacoesInternas > qtTramitacoesExternas) {
//				DocumentoTramitacao tramitacao = DocumentoTramitacaoServices.getUltimaTramitacao(cdDocumento, connect);
//				Result r = removeTramitacao(cdDocumento, tramitacao.getCdTramitacao(), 0, 0, connect);				
//			}
			
			while(rsmTramites.next()) {
								
				//documento = DocumentoDAO.get(cdDocumento, connect);
				documento = getDocumentoExterno(rsmTramites.getString("cd_documento_externo"), connect);
				
				LogUtils.debug("DocumentoServices.sincronizarTramitacoes.documento: "+documento);
				
				if(documento==null)
					continue;
				
				int cdTramitacao = Integer.parseInt(rsmTramites.getString("cd_tramitacao"));
				int cdSetorDestino = SetorServices.getSetorBySetorEel(rsmTramites.getString("cd_setor_destino"), connect);
				int cdUsuarioDestino = 0;
				int cdSetorOrigem = SetorServices.getSetorBySetorEel(rsmTramites.getString("cd_setor_origem"), connect);
				int cdUsuarioOrigem = 0;
				GregorianCalendar dtTramitacao = rsmTramites.getGregorianCalendar("dt_envio");
				GregorianCalendar dtRecebimento = rsmTramites.getGregorianCalendar("dt_recebimento");
				String nmLocalOrigem = rsmTramites.getString("nm_local_origem");
				String nmLocalDestino = rsmTramites.getString("nm_local_destino");
				String nrRemessa = (rsmTramites.getString("nr_remessa").trim());
				
				//CASO O SETOR DESTINO NÃO  EXISTA
				if(cdSetorDestino<=0) {
					LogUtils.debug("DocumentoServices.sincronizarTramitacoes");
					LogUtils.debug("Setor destino inexistente no banco local. Cadastrando...");
					LogUtils.debug("nmLocalDestino: "+nmLocalDestino);
					if(nmLocalDestino!=null) {
						Setor setor = new Setor(0, 0, 3, 0, nmLocalDestino, SetorServices.ST_ATIVO, null, null, 
								null, null, null, null, null, 0, null, null, null, SetorServices.TP_SETOR_EXTERNO, 
								0, rsmTramites.getString("cd_setor_destino"));
						
						result = SetorServices.save(setor, connect);
						if(result.getCode()<=0) {
							connect.rollback();
							return result;
						}
						else {
							cdSetorDestino = result.getCode();
						}
					}
					else {
						cdSetorDestino = ParametroServices.getValorOfParametroAsInteger("CD_SETOR_DESCONHECIDO", 0, 0, connect);
					}
				}
				
				//CASO O SETOR ORIGEM NÃO  EXISTA
				if(cdSetorOrigem<=0) {
					LogUtils.debug("DocumentoServices.sincronizarTramitacoes");
					LogUtils.debug("Setor origem inexistente no banco local. Cadastrando...");
					LogUtils.debug("nmLocalOrigem: "+nmLocalOrigem);
					if(nmLocalOrigem!=null) {
						Setor setor = new Setor(0, 0, 3, 0, nmLocalOrigem, SetorServices.ST_ATIVO, null, null, 
								null, null, null, null, null, 0, null, null, null, SetorServices.TP_SETOR_EXTERNO, 
								0, rsmTramites.getString("cd_setor_origem"));
						
						result = SetorServices.save(setor, connect);
						if(result.getCode()<=0) {
							connect.rollback();
							return result;
						}
						else {
							cdSetorOrigem = result.getCode();
						}
					}
					else {
						cdSetorOrigem = ParametroServices.getValorOfParametroAsInteger("CD_SETOR_DESCONHECIDO", 0, 0, connect);
					}
				}
				
				String txtTramitacao = "";
				if(rsmTramites.getObject("txt_tramitacao")!=null) {
					Clob clobTxtTramitacao = (Clob)rsmTramites.getObject("txt_tramitacao");
					Reader r1 = clobTxtTramitacao.getCharacterStream();
					
					int c = 0;
					while((c = r1.read())!=-1) {
						if(c != 0x00)						
							txtTramitacao+=(char)c;
					}
					r1.close();
				}
				
				byte[] rtf = txtTramitacao.getBytes();
				if((new String(rtf)).indexOf("\\rtf1")!=-1) {
					rtf = Converter.rtfToHtml(rtf);
					rtf = (new String(rtf)).replaceAll("\\<[^>]*>","").trim().getBytes();
				}
				txtTramitacao = new String(rtf);
				
				// ATUALIZAR informações DO DOCUMENTO COM BASE NA TRAMITAÇÃO
				if(dtRecebimento==null) {//doc em tramitação
					documento.setCdSetorAtual(cdSetorOrigem);
					documento.setCdSituacaoDocumento(ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_TRAMITANDO", 0, 0, connect));
					cdUsuarioOrigem = cdUsuarioExterno;
					cdUsuarioDestino = 0;
					LogUtils.debug("DocumentoServices.sincronizarTramitacoes");
					LogUtils.debug("TRAMITANDO");
				}
				else { //doc recebido
					documento.setCdSetorAtual(cdSetorDestino);
					documento.setCdSituacaoDocumento(ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0, 0, connect));
					cdUsuarioOrigem = cdUsuarioExterno;
					cdUsuarioDestino = cdUsuarioExterno;
					LogUtils.debug("DocumentoServices.sincronizarTramitacoes");
					LogUtils.debug("RECEBIDO");
				}
				
				cdFase = FaseServices.getFaseByFaseEel(rsmTramites.getString("cd_fase"), connect);
				if(cdFase<=0) {
					cdFase = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INICIAL", 0, 0, connect);
				}
				documento.setCdFase(cdFase);
				
				int r = DocumentoDAO.update(documento, connect);
				if(r<=0) {
					connect.rollback();
					return new Result(r, "Erro ao atualizar documento.");
				}
				
				// SE A TRAMITAÇÃO JÁ EXISTIR LOCALMENTE, ATUALIZA
				// SE NÃO , INSERE 
				DocumentoTramitacao tramitacao = DocumentoTramitacaoDAO.get(cdTramitacao, documento.getCdDocumento(), connect);
				if(tramitacao!=null) {
					tramitacao.setDtRecebimento(dtRecebimento);
					tramitacao.setCdUsuarioDestino(cdUsuarioDestino);
					tramitacao.setNmLocalDestino(nmLocalDestino);
					tramitacao.setCdSituacaoDocumento((dtRecebimento==null ? ST_TRAMITANDO : ST_PARADO));
					if(nrRemessa!=null && !nrRemessa.equals(""))
						tramitacao.setNrRemessa(nrRemessa);
				}
				else {
					LogUtils.debug("INSERT");
					tramitacao = new DocumentoTramitacao(cdTramitacao, 
							documento.getCdDocumento(), cdSetorDestino, cdUsuarioDestino, 
							cdSetorOrigem, cdUsuarioOrigem, dtTramitacao, 
							dtRecebimento, nmLocalDestino, txtTramitacao, 
							(dtRecebimento==null ? ST_TRAMITANDO : ST_PARADO), 0, nmLocalOrigem, nrRemessa);
				}
				result = DocumentoTramitacaoServices.save(tramitacao, connect);
				
				if(result.getCode()<=0) {
					if(isConnectionNull)
						connect.rollback();
					return result;
				}
			}
			
			return new Result(1, "Tramitações sincronizadas com sucesso.");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * 
	 * @param rsmDocumentos
	 * @return
	 * 
	 * @category E&L
	 */
	public static Result sincronizarAnexos(ResultSetMap rsmDocumentos) {
		return sincronizarAnexos(rsmDocumentos, null);
	}
	
	public static Result sincronizarAnexos(ResultSetMap rsmDocumentos, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}			
			
			Result result = null;
			
			while(rsmDocumentos.next()) {
				
				if(rsmDocumentos.getString("cd_documento_superior", null)!=null) {
					Documento docSuperior = getDocumentoExterno(rsmDocumentos.getString("cd_documento_superior"), connect);
					Documento doc = getDocumentoExterno(rsmDocumentos.getString("cd_documento"), connect);
					
					if(docSuperior==null) {
//						docSuperior = new Documento(0, 0, cdSetor, cdUsuario, nmLocalOrigem, dtProtocolo, tpDocumento, txtObservacao, idDocumento, nrDocumento, cdTipoDocumento, cdServico, cdAtendimento, txtDocumento, cdSetorAtual, cdSituacaoDocumento, cdFase, cdEmpresa, cdProcesso, tpPrioridade, cdDocumentoSuperior, dsAssunto, nrAtendimento, lgNotificacao, cdTipoDocumentoAnterior, nrDocumentoExterno, nrAssunto, nrProtocoloExterno, nrAnoExterno)
						continue;
					}
					else {
						if(doc.getCdDocumentoSuperior()<=0)
							doc.setCdDocumentoSuperior(docSuperior.getCdDocumento());
						result = update(doc, null, connect, null);
						if(result.getCode()<=0) {
							if(isConnectionNull)
								connect.rollback();
							return new Result(-2, "Erro ao vincular documentos.");
						}
					}
				}
				else if(rsmDocumentos.getString("cd_documento_anexo", null)!=null) {
					Documento docAnexo = getDocumentoExterno(rsmDocumentos.getString("cd_documento_anexo"), connect);
					Documento doc = getDocumentoExterno(rsmDocumentos.getString("cd_documento"), connect);
					
					if(docAnexo==null) {
						continue;
					}
					else {
						if(docAnexo.getCdDocumentoSuperior()<=0)
							docAnexo.setCdDocumentoSuperior(doc.getCdDocumento());
						result = update(docAnexo, null, connect, null);
						if(result.getCode()<=0) {
							if(isConnectionNull)
								connect.rollback();
							return new Result(-3, "Erro ao vincular documentos.");
						}
						
					}
				}
			}
			
				
			if(result !=null && result.getCode()<=0) {
				if(isConnectionNull)
					connect.rollback();
				return result;
			}
			
			return new Result(1, "Anexos sincronizados com sucesso.");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Documento getDocumentoExterno(String nrDocumentoExterno) {
		return getDocumentoExterno(nrDocumentoExterno, null);
	}

	public static Documento getDocumentoExterno(String nrDocumentoExterno, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento WHERE nr_documento_externo=?");
			pstmt.setString(1, nrDocumentoExterno);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Documento(rs.getInt("cd_documento"),
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_usuario"),
						rs.getString("nm_local_origem"),
						(rs.getTimestamp("dt_protocolo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_protocolo").getTime()),
						rs.getInt("tp_documento"),
						rs.getString("txt_observacao"),
						rs.getString("id_documento"),
						rs.getString("nr_documento"),
						rs.getInt("cd_tipo_documento"),
						rs.getInt("cd_servico"),
						rs.getInt("cd_atendimento"),
						rs.getString("txt_documento"),
						rs.getInt("cd_setor_atual"),
						rs.getInt("cd_situacao_documento"),
						rs.getInt("cd_fase"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_processo"),
						rs.getInt("tp_prioridade"),
						rs.getInt("cd_documento_superior"),
						rs.getString("ds_assunto"),
						rs.getString("nr_atendimento"),
						rs.getInt("lg_notificacao"),
						rs.getInt("cd_tipo_documento_anterior"),
						rs.getString("nr_documento_externo"),
						rs.getString("nr_assunto"),
						rs.getString("nr_protocolo_externo"),
						rs.getString("nr_ano_externo"),
						rs.getInt("tp_documento_externo"),
						rs.getInt("tp_interno_externo"));
			}
			else{
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoService.getDocumentoExterno: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getListDocumentosExternos() {
		return getListDocumentosExternos(null);
	}
	
	public static ResultSetMap getListDocumentosExternos(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_documento, nr_documento_externo "
															+ " FROM ptc_documento "
															+ " WHERE nr_documento_externo IS NOT NULL");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * 
	 * @param nrDocumento
	 * @param cdTipoDocumento
	 * @param cdSetor
	 * @return
	 * 
	 * @category E&L
	 */
	public static ResultSetMap findExterno(String nrDocumento, int cdTipoDocumento, int cdSetor, int cdEmpresa, int nrRegistros, int tpRelatorio) {
		return findExterno(nrDocumento, cdTipoDocumento, cdSetor, cdEmpresa, nrRegistros, tpRelatorio, null, null, null);
	}
	
	public static ResultSetMap findExterno(String nrDocumento, int cdTipoDocumento, int cdSetor, int cdEmpresa, int nrRegistros, int tpRelatorio, 
			GregorianCalendar dtProtocoloInicial, GregorianCalendar dtProtocoloFinal, String txtAssunto) {
		Connection connect = Conexao.conectar();
		try {
			String codigo_docproc = null;
			String codigo_tdc = null;
			String codigo_local_origem = null;
			String ano = null;
			String data_registro_inicial = null;
			String data_registro_final = null;						
			
			if(nrDocumento!=null && nrDocumento.contains("/")) {//nrDocumento.trim().matches("/[0-2][0-9][0-9][0-9]")) { // procura sufixo /YYYY para extrair o ano
				codigo_docproc = new String(nrDocumento.trim().substring(0, nrDocumento.indexOf("/")));
				ano = new String(nrDocumento.substring(nrDocumento.indexOf("/")+1, nrDocumento.length()));
			}
			else if(nrDocumento!=null && nrDocumento.contains("/")) {//!nrDocumento.trim().matches("/[0-2][0-9][0-9][0-9]")) {
				codigo_docproc = nrDocumento.trim();
			}
			
			if(cdTipoDocumento>0) {
				TipoDocumento tpDoc = TipoDocumentoDAO.get(cdTipoDocumento, connect);
				codigo_tdc = tpDoc.getNrExterno();
			}
			
			if(cdSetor>0) {
				Setor setor = SetorDAO.get(cdSetor, connect);
				codigo_local_origem = setor.getNrSetorExterno();
			}
			
			if(dtProtocoloInicial!=null) {
				data_registro_inicial = Util.convCalendarStringSqlCompleto(dtProtocoloInicial);
			}
			
			if(dtProtocoloFinal!=null) {
				data_registro_final = Util.convCalendarStringSqlCompleto(dtProtocoloFinal);
			}
			
			
			Result r = new Result(1);
			ResultSetMap rsmExterno = EelUtils.findProtocolo(codigo_docproc, codigo_tdc, codigo_local_origem, ano, nrRegistros, 
					data_registro_inicial, data_registro_final, txtAssunto);
			if(rsmExterno!=null && rsmExterno.next()) {
				rsmExterno.beforeFirst();
				while(rsmExterno.next()) {
					String nrDocumentoExterno = rsmExterno.getString("nr_documento_externo");
					r = sincronizarDocumentos(nrDocumentoExterno, ano, cdEmpresa, null, null);
				}
			}
			else {
				// sincronizar ultimo dia
				GregorianCalendar dtInicial = new GregorianCalendar();
				dtInicial.add(Calendar.DAY_OF_MONTH, -1);
				dtInicial.set(Calendar.HOUR, 0);
				dtInicial.set(Calendar.MINUTE, 0);
				dtInicial.set(Calendar.SECOND, 0);
				
				GregorianCalendar dtFinal = new GregorianCalendar();
				dtFinal.set(Calendar.HOUR, 23);
				dtFinal.set(Calendar.MINUTE, 59);
				dtFinal.set(Calendar.SECOND, 59);
				
				r = sincronizarDocumentos(cdEmpresa, dtInicial, dtFinal);
			}
			
			ResultSetMap rsm = new ResultSetMap();
			if(r.getCode()>0) {
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.nr_documento", nrDocumento, ItemComparator.LIKE_ANY, Types.VARCHAR));
				if(cdTipoDocumento>0)
					criterios.add(new ItemComparator("A.cd_tipo_documento", Integer.toString(cdTipoDocumento), ItemComparator.EQUAL, Types.INTEGER));
				if(cdSetor>0)
					criterios.add(new ItemComparator("A.cd_setor", Integer.toString(cdSetor), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("nrRegistros", Integer.toString(nrRegistros), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tpRelatorio", Integer.toString(tpRelatorio), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("lgUltimaTramitacao", Integer.toString(1), ItemComparator.EQUAL, Types.INTEGER));
				
				LogUtils.debug("find after sync");
				rsm = find(criterios);
			}
			
			return rsm;
		}
		catch(Exception e) {
			try {
				if(!connect.getAutoCommit())
					Conexao.rollback(connect);
			} catch (SQLException e1) {}
			return null;
			
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca TRAMITAÇÕES de um determinado documento no banco da E&L
	 * 
	 * @param controle
	 * @return
	 * 
	 * @category E&L
	 */
	public static ResultSetMap getTramitesExterno(String controle) {
		try {
			Connection connExt = EelUtils.conectarEelSqlServer();
			
			ResultSetMap rsm = new ResultSetMap(EelUtils.getAndamentos(controle, connExt));
			while(rsm.next()) {
				rsm.setValueToField("ds_dt_envio", Util.formatDate(rsm.getGregorianCalendar("dt_envio"), "dd/MM/yyyy"));
				rsm.setValueToField("ds_dt_recebimento", Util.formatDate(rsm.getGregorianCalendar("dt_recebimento"), "dd/MM/yyyy"));
				
				String txtTramitacao = "";
				if(rsm.getObject("txt_tramitacao")!=null) {
					Clob clobTxtTramitacao = (Clob)rsm.getObject("txt_tramitacao");
					Reader r1 = clobTxtTramitacao.getCharacterStream();
					
					int c = 0;
					while((c = r1.read())!=-1) {
						if(c != 0x00)						
							txtTramitacao+=(char)c;
					}
					r1.close();
				}
				
				byte[] rtf = txtTramitacao.getBytes();
				if((new String(rtf)).indexOf("\\rtf1")!=-1) {
					rtf = Converter.rtfToHtml(rtf);
					//rtf = (new String(rtf)).replaceAll("(?s)\\<html\\>.*\\<body\\>", "").getBytes();
					rtf = (new String(rtf)).replaceAll("\\<[^>]*>","").trim().getBytes();
					//rtf = (new String(rtf)).replaceAll("(?s)\\</body>.*\\</html\\>", "").trim().getBytes();
				}
				txtTramitacao = new String(rtf);
				
				rsm.setValueToField("txt_tramitacao", txtTramitacao);
			}
			
			connExt.close();
			
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} 
	}
	
	public static Result saveProtocoloProcessoEel(Documento documento, DocumentoPessoa solicitante, Connection connect) {
		Connection connExt = EelUtils.conectarEelSqlServer();
		
		if(connect==null) {
			return new Result(-1, "Não existe conexão com o banco de dados.");
		}

		if(connExt==null) {
			return new Result(-2, "Erro ao conectar com o banco de dados da E&L");
		}
		
		try {
			connExt.setAutoCommit(false);
			
			TipoDocumento tpDocumento = TipoDocumentoDAO.get(documento.getCdTipoDocumento(), connect);
			String cdTipoDocExterno = tpDocumento.getNrExterno();//"0001";
			if(cdTipoDocExterno==null) {
				return new Result(-3, "Tipo de Documento equivalente não encontrado no banco da E&L.");
			}

			TipoDocumento tpDocProcesso = TipoDocumentoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_PROCESSO", 0, 0, connect), connect);
			
			Setor setor = SetorDAO.get(documento.getCdSetor(), connect);
			String cdLocalExterno = (setor!=null ? setor.getNrSetorExterno() : null);//"0000062";
			if(cdLocalExterno==null) {
				return new Result(-3, "Setor equivalente não encontrado no banco da E&L.");
			}
			
			int cdRequerente = UsuarioDAO.get(documento.getCdUsuario(), connect).getCdPessoa();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM grl_pessoa_externa WHERE cd_pessoa = "+cdRequerente);
			ResultSet rs = pstmt.executeQuery();
			String cdPessoaRequerente = null;
			if(rs.next()) { //já existe a paridade
				cdPessoaRequerente = rs.getString("cd_pessoa_externa");//"0000368";
			}
			else {
				Pessoa p = PessoaDAO.get(cdRequerente, connect);
				PessoaFisica pf = PessoaFisicaDAO.get(cdRequerente, connect);
				pstmt = connExt.prepareStatement("USE [CONT_VIT_CONQUISTA_PREFEITURA];"
						+ " SELECT codigo_g AS cd_pessoa_externa FROM gg_geral"
						+ " WHERE nome_g LIKE '%"+p.getNmPessoa()+"%'"
								+ " OR cpf_g = '"+(pf!=null ? pf.getNrCpf() : "")+"'");
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					cdPessoaRequerente = rs.getString("cd_pessoa_externa");
					
					if(cdPessoaRequerente!=null) {
						pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_externa(cd_pessoa, cd_pessoa_externa)"
								+ " VALUES (?,?)");
						pstmt.setInt(1, cdRequerente);
						pstmt.setString(2, cdPessoaRequerente);
						
						pstmt.executeUpdate();
					}
				}
			}
			
			if(cdPessoaRequerente==null) {
				cdPessoaRequerente = "0000000"; //NAO LOCALIZADO
				//return new Result(-3, "Usuário de cadastro equivalente não encontrado no banco da E&L.");				
			}
			
			pstmt = connect.prepareStatement(
					"SELECT * FROM grl_pessoa_externa WHERE cd_pessoa = "+solicitante.getCdPessoa());
			rs = pstmt.executeQuery();
			String cdPessoaContato = null;
			if(rs.next()) {
				cdPessoaContato = rs.getString("cd_pessoa_externa");
			}
			else {
				Pessoa p = PessoaDAO.get(solicitante.getCdPessoa(), connect);
				PessoaFisica pf = PessoaFisicaDAO.get(solicitante.getCdPessoa(), connect);
				pstmt = connExt.prepareStatement("USE [CONT_VIT_CONQUISTA_PREFEITURA];"
						+ " SELECT codigo_g AS cd_pessoa_externa FROM gg_geral"
						+ " WHERE 1=1 AND "
						+ " nome_g LIKE '%"+p.getNmPessoa()+"%'"
								+ " OR cpf_g = '"+(pf!=null ? pf.getNrCpf() : "")+"'");
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					cdPessoaContato = rs.getString("cd_pessoa_externa");
					
					if(cdPessoaContato!=null) {
						pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_externa(cd_pessoa, cd_pessoa_externa)"
								+ " VALUES (?,?)");
						pstmt.setInt(1, solicitante.getCdPessoa());
						pstmt.setString(2, cdPessoaContato);
						
						pstmt.executeUpdate();
					}
				}
			}
			
			if(cdPessoaContato==null) {
				cdPessoaContato = "0000000"; //NAO LOCALIZADO
				//return new Result(-4, "Requerente equivalente não encontrado no banco da E&L.");				
			}
			
			CallableStatement cstmt = null;
			ResultSet rsUltimaTramitacao = null;
			String nrControlePrinc = null;
			
			/**
			 * INSERE PROCESSO
			 */
			
			String nrProtocoloProcesso = null;
			cstmt = connExt.prepareCall(//"{call USE [CONT_VIT_CONQUISTA_PREFEITURA]; "
					 "{call dbo.func_wb_grava_protocolo_trb_conquista"
					+ "		@strEmpresa=?, "
					+ "		@strFilial=?,"
					+ " 	@strTipo=?,"
					+ "		@strInternoExterno=?,"
					+ "		@strEspecie=?,"//cd_tipo_documento <--> pr_tiposdocs 
					+ "		@strOrigem=?,"//cd_setor_origem  <--> gg_local
					+ "		@strContato=?,"//cd_pessoa (ptc_documento_pessoa) <--> gg_geral
					+ "		@strRequerente=?,"//cd_pessoa (ptc_documento_pessoa) <--> gg_geral
					+ "		@strAssunto=?,"//documento.nrAssunto <--> pr_assunto
					+ "		@strDetalhamento=?,"
					+ "		@strControle=?; }");
//					+ "GO");
			
			cstmt.setString(1,"001");
			cstmt.setString(2, "001");
			cstmt.setString(3, "1");  //'0'=doc '1'=proc
			cstmt.setString(4, "I");
			cstmt.setString(5, tpDocProcesso.getNrExterno());
			cstmt.setString(6, cdLocalExterno);
			cstmt.setString(7, cdPessoaContato);
			cstmt.setString(8, cdPessoaRequerente);
			cstmt.setString(9, documento.getNrAssunto());
			cstmt.setString(10, (documento.getTxtObservacao()!=null ? documento.getTxtObservacao() : documento.getDsAssunto()));
			
			cstmt.registerOutParameter(11, Types.VARCHAR);
			
			cstmt.execute();
			
			nrControlePrinc = cstmt.getString(11);
			
			LogUtils.debug("DocumentoServices.saveProtocoloProcessoEel");
			LogUtils.debug("nrControlePrinc: "+nrControlePrinc);
			
			pstmt = connExt.prepareStatement("SELECT codigo_docproc FROM pr_protocolo WHERE controle='"+nrControlePrinc+"'");
			rs = pstmt.executeQuery();
			if(rs.next())
				nrProtocoloProcesso = rs.getString("codigo_docproc");
			
			DocumentoTramitacao tramitacao = new DocumentoTramitacao(0, documento.getCdDocumento(), documento.getCdSetorAtual() /*cdSetorDestino*/,
					documento.getCdUsuario(),
					documento.getCdSetor() /*cdSetorOrigem*/, documento.getCdUsuario() /*cdUsuarioOrigem*/,
					 new GregorianCalendar() /*dtTramitacao*/, new GregorianCalendar() /*dtRecebimento*/,
					 "" /*nmLocalDestino*/, "Registro do Processo",
					 documento.getCdSituacaoDocumento(), documento.getCdFase(), null/*nmLocalOrigem*/, null/*nrRemessa*/);

			int retTramicacao = DocumentoTramitacaoDAO.insert(tramitacao, connect);
			if (retTramicacao <= 0) {
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao tentar incluir tramitação ["+retTramicacao+"]!");
			}
			
					
			/*
			 * ATUALIZAR DADOS DE TRAMITAÇÃO
			 */
			rsUltimaTramitacao = EelUtils.getUltimaTramitacao(nrControlePrinc, connExt);
			if(rsUltimaTramitacao.next()) {
				if(rsUltimaTramitacao.getString("codigo_local_origem").equalsIgnoreCase(cdLocalExterno)) {
					pstmt = connExt.prepareStatement("UPDATE pr_andamento SET codigo_local_origem=?, codigo_local_destino=?, data_recebimento=?, estado=?"
													+ "	WHERE codigo_emp='001' AND codigo_fil='001'"
													+ " AND sequencia_tramite=1"
													+ " AND controle=?");
					pstmt.setString(1, cdLocalExterno);
					pstmt.setString(2, cdLocalExterno);
					pstmt.setDate(3, new Date(new GregorianCalendar().getTimeInMillis()));
					pstmt.setString(4, "F");
					pstmt.setString(5, nrControlePrinc);
					
					if(pstmt.executeUpdate()<0) {
						connExt.rollback();
						return new Result(-6, "Erro ao gravar processo no banco E&L.");
					}
				}
			}			
						
			Result result = new Result(1, "Processo salvo com sucesso no banco da E&L!", "NR_DOCUMENTO_EXTERNO", nrControlePrinc);
			result.addObject("NR_PROTOCOLO_EXTERNO", nrProtocoloProcesso);
			
			return result;
			
		}
		catch(Exception e){
			e.printStackTrace();
			Conexao.rollback(connExt);
			return new Result(-1, "Erro ao salvar documento!");
		}
		finally{
			EelUtils.desconectarEelSqlServer(connExt);
		}
	}
	
	public static Result getDadosEmail(int cdDocumento){
		return getDadosEmail(cdDocumento, null);
	}
	
	public static Result getDadosEmail(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			Documento documento = DocumentoDAO.get(cdDocumento, connect);
			Result result = new Result(1, "Mensagem gerada a partir do documento ["+documento.getNrDocumento()+"]");
		
			result.addObject("DESTINATARIOS", getDestinatariosEmail(cdDocumento, connect));
			result.addObject("ARQUIVOS", getAnexosEmail(cdDocumento, connect));
			result.addObject("ASSINATURA", ParametroServices.getValorOfParametroAsString("DS_EMAIL_ASSINATURA", ""));
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! DocumentoServices.getDadosEmail: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDestinatariosEmail(int cdDocumento){
		return getDestinatariosEmail(cdDocumento, null);
	}
	
	public static ResultSetMap getDestinatariosEmail(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			Documento documento = DocumentoDAO.get(cdDocumento, connect);
			
			if(documento==null)
				return null;
			
			ResultSetMap rsm = new ResultSetMap();
			
			//Usuario
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* "
															+ " FROM grl_pessoa A"
															+ " JOIN seg_usuario B ON (A.cd_pessoa = B.cd_pessoa)"
															+ " JOIN ptc_documento C ON (B.cd_usuario = C.cd_usuario)"
															+ " WHERE C.cd_documento = ?");
			pstmt.setInt(1, cdDocumento);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next() && rs.getInt("CD_PESSOA")>0 && rs.getString("NM_EMAIL")!=null && !rs.getString("NM_EMAIL").equals("")){
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("ID_GRUPO", "1");
				register.put("NM_GRUPO", "USUÁRIO DO CADASTRO");
				register.put("CD_PESSOA", rs.getInt("CD_PESSOA"));
				register.put("NM_PESSOA", rs.getString("NM_PESSOA"));
				register.put("NM_EMAIL", rs.getString("NM_EMAIL"));
				register.put("DS_LABEL", rs.getString("NM_PESSOA") + " <"+rs.getString("NM_EMAIL")+">");
				
				rsm.addRegister(register);
			}
			
			//Participantes
			pstmt = connect.prepareStatement("SELECT A.* "
											+ " FROM grl_pessoa A"
											+ " JOIN ptc_documento_pessoa B ON (A.cd_pessoa = B.cd_pessoa AND B.cd_documento = ?)");
			pstmt.setInt(1, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next() && rs.getInt("CD_PESSOA")>0 && rs.getString("NM_EMAIL")!=null && !rs.getString("NM_EMAIL").equals("")){
			
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("ID_GRUPO", "2");
				register.put("NM_GRUPO", "PARTICIPANTES");
				register.put("CD_PESSOA", rs.getInt("CD_PESSOA"));
				register.put("NM_PESSOA", rs.getString("NM_PESSOA"));
				register.put("NM_EMAIL", rs.getString("NM_EMAIL"));
				register.put("DS_LABEL", rs.getString("NM_PESSOA") + " <"+rs.getString("NM_EMAIL")+">");
				
				rsm.addRegister(register);
			}
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! DocumentoServices.getDestinatariosEmail: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAnexosEmail(int cdDocumento){
		return getAnexosEmail(cdDocumento, null);
	}
	
	public static ResultSetMap getAnexosEmail(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			Documento documento = DocumentoDAO.get(cdDocumento, connect);
			
			if(documento==null)
				return null;
			
			ResultSetMap rsm = new ResultSetMap();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * "+
	                "FROM ptc_documento_arquivo A "+
                    "WHERE A.cd_documento = ?");
			pstmt.setInt(1, cdDocumento);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				int length = rs.getBytes("BLB_ARQUIVO")!=null ? rs.getBytes("BLB_ARQUIVO").length : 0;
				
				register.put("ID_GRUPO", "1");
				register.put("CD_DOCUMENTO", rs.getInt("CD_DOCUMENTO"));
				register.put("CD_ARQUIVO", rs.getInt("CD_ARQUIVO"));
				register.put("NM_GRUPO", "ARQUIVOS DO PROCESSO");
				register.put("NM_ARQUIVO", rs.getString("NM_ARQUIVO"));
				register.put("NM_DOCUMENTO", rs.getString("NM_DOCUMENTO"));
				register.put("BLB_ARQUIVO", rs.getBytes("BLB_ARQUIVO"));
				register.put("DS_LABEL", rs.getString("NM_DOCUMENTO") + " ("+(length/1024)+"kb)");
				
				rsm.addRegister(register);
			}
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro! DocumentoServices.getAnexosEmail: " + e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Documento getByNrDocumento(String nrDocumento) {
		return getByNrDocumento(nrDocumento, null);
	}

	public static Documento getByNrDocumento(String nrDocumento, Connection conn) {
		boolean isConnNull = (conn == null);
		
		if(isConnNull)
			conn = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM PTC_DOCUMENTO WHERE NR_DOCUMENTO = ?");
			pstmt.setString(1, nrDocumento);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			if(_rsm.next()) {
				ResultSetMapper<Documento> _docs = new ResultSetMapper<Documento>(_rsm, Documento.class);			
				return _docs.get(0);
			}
			
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnNull)
				Conexao.desconectar(conn);
		}
	}
}
