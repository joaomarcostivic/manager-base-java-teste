package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.OneDriveClient;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.prc.ProcessoArquivo;
import com.tivic.manager.prc.ProcessoArquivoDAO;
import com.tivic.manager.prc.ProcessoArquivoServices;
import com.tivic.manager.print.Converter;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.DocumentoArquivoServices;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoOcorrenciaServices;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.seg.AssinaturaDigital;
import com.tivic.manager.seg.AssinaturaDigitalServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioChave;
import com.tivic.manager.seg.UsuarioChaveServices;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.OneDriveUtils;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;


public class ArquivoServices {

	public static final int ST_NAO_ENVIADO = 0;
	public static final int ST_ENVIADO     = 1;
	
	public static final int NAI_PUBLICADA  = 3;
	public static final int NIP_PUBLICADA  = 5;
	public static final int PROCESSOS_PUBLICADOS = 6;
	
	/**
	 * Salvar arquivo
	 * @param arquivo
	 * @return
	 */
	public static Result save(Arquivo arquivo){
		return save(arquivo, null, null);
	}

	public static Result save(Arquivo arquivo, AuthData authData){
		return save(arquivo, authData, null);
	}

	public static Result save(Arquivo arquivo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(arquivo==null)
				return new Result(-1, "Erro ao salvar. Arquivo é nulo");

			int retorno;
			if(arquivo.getCdArquivo()==0){
				retorno = ArquivoDAO.insert(arquivo, connect);
				arquivo.setCdArquivo(retorno);
			}
			else {
				retorno = ArquivoDAO.update(arquivo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ARQUIVO", arquivo);
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
	 * @author Alvaro
	 * @param cdArquivo
	 * @since 30/03/2015
	 * @return
	 */
	public static Result remove(int cdArquivo){
		return remove(cdArquivo, false, null);
	}
	/**
	 * @author Alvaro
	 * @param cdArquivo
	 * @param cascade
	 * @since 30/03/2015
	 * @return
	 */
	public static Result remove(int cdArquivo, boolean cascade){
		return remove(cdArquivo, cascade, null);
	}
	/**
	 * @author Alvaro
	 * @param cdArquivo
	 * @param cascade
	 * @param connect
	 * @since 30/03/2015
	 * @return
	 */
	public static Result remove(int cdArquivo, boolean cascade, Connection connect){
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
			retorno = ArquivoDAO.delete(cdArquivo, connect);
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
	
	public static Arquivo insert(Arquivo arquivo) {
		return insert(arquivo, null);
	}

	public static Arquivo insert(Arquivo arquivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			arquivo.setDtArquivamento(new GregorianCalendar());
			int cdArquivo = ArquivoDAO.insert(arquivo, connection);
			if (cdArquivo <= 0)
				return null;
			else {
				arquivo.setCdArquivo(cdArquivo);
				return arquivo;
			}
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

	public static Arquivo update(Arquivo arquivo) {
		return update(arquivo, null);
	}

	public static Arquivo update(Arquivo arquivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int cdRetorno = ArquivoDAO.update(arquivo, connection);
			return cdRetorno <= 0 ? null : arquivo;
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
	 * Método de busca de arquivos
	 * O valor do critério "tpBusca" define de onde os arquivos serão
	 * buscados (0: Processos, 1: Documentos) 
	 * 
	 * @param criterios ArrayList<ItemComparator> os critérios da busca
	 * @return ResultSetMap com o resultado da busca
	 * @author Maurício
	 * @since 18/06/2014
	 */
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			int tpBusca = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("tpBusca")) {
					tpBusca = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
			}
				
			switch (tpBusca) {
				case 0:
					return ProcessoArquivoServices.find(criterios, connect);
				case 1:
					return DocumentoArquivoServices.find(criterios, connect);
				case 2:
					return PessoaArquivoServices.find(criterios, connect);
					
				default:
					return null; 
			}			
			
			//return Search.find(sql, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
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
	 * Busca uma lista com os arquivos indexados que não tem um
	 * registro em ptc_documento.
	 * 
	 * @return ResultSetMap 
	 * @author Maurício
	 * @since 24/07/2014
	 */
	public static ResultSetMap getListArquivosDesvinculados(int cdTipoDocumento) {
		return getListArquivosDesvinculados(cdTipoDocumento, null);
	}
	
	public static ResultSetMap getListArquivosDesvinculados(int cdTipoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
					
//			String sql = "SELECT A.cd_arquivo, A.nm_arquivo, A.nm_documento, A.dt_arquivamento"
//					   + " FROM grl_arquivo A "
//					   + " WHERE A.cd_arquivo NOT IN "
//					   + "       (select cd_arquivo from ptc_documento where cd_arquivo is not null)";
			String sql = "SELECT A.cd_arquivo, A.nm_arquivo, A.nm_documento, A.dt_arquivamento, A.dt_vencimento, "
					+ " E.txt_atributo_valor as DT_VIGENCIA_INICIAL, E1.txt_atributo_valor as DT_VIGENCIA_FINAL"
					+ " FROM grl_arquivo A"
					+ " JOIN grl_tipo_arquivo B ON (A.cd_tipo_arquivo = B.cd_tipo_arquivo)"
					+ " JOIN grl_formulario C ON (B.cd_formulario = C.cd_formulario)"
					+ " LEFT OUTER JOIN grl_formulario_atributo D ON (C.cd_formulario = D.cd_formulario "
					+ "													AND D.cd_formulario_atributo = 4)"
					+ " LEFT OUTER JOIN grl_formulario_atributo D1 ON (C.cd_formulario = D1.cd_formulario "
					+ "													AND D1.cd_formulario_atributo = 5)"
					+ " JOIN grl_formulario_atributo_valor E ON (D.cd_formulario_atributo = E.cd_formulario_atributo "
					+ "											 AND E.cd_arquivo = A.cd_arquivo)"
					+ " LEFT OUTER JOIN grl_formulario_atributo_valor E1 ON (D1.cd_formulario_atributo = E1.cd_formulario_atributo "
					+ "														AND E1.cd_arquivo = A.cd_arquivo)"
					+ " WHERE A.cd_arquivo NOT IN "
					+ "       (select cd_arquivo from ptc_documento where cd_arquivo is not null)";

			PreparedStatement pstmt = connect.prepareStatement(sql);
			
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
	 * Gera registros de Documentos e de DocumentoArquivo a partir dos arquivos indexados.
	 * 
	 * @param cdTipoDocumentoArquivo grl/TipoDocumento.cdDocumento
	 * @param cdTipoDocumento ptc/TipoDocumento.cdDocumento
	 * @param cdSituacaoDocumento ptc/SituacaoDocumento
	 * @param cdFormulario grl/Formulario
	 * @param arquivos ArrayList<String> códigos dos arquivos
	 * @param cdAtributoNrDocumento código do atributo que corresponde a nrDocumento
	 * @param cdAtributoDtProtocolo código do atributo que corresponde a dtProtocolo
	 * @param cdAtributoDsAssunto código do atributo que corresponde a dsAssunto
	 * @param cdAtributoCdSolicitante código do atributo que corresponde a cdSolicitante
	 * @param cdEmpresa código de Empresa
	 * @param cdUsuario código de Usuário
	 * @param cdSetor código de Setor
	 * @return
	 * 
	 * @author Maurício
	 * @since 01/08/2014
	 */
	public static Result gerarDocumentos(int cdTipoDocumentoArquivo, int cdTipoDocumento, int cdSituacaoDocumento, 
			int cdFormulario, ArrayList<String> arquivos, int cdAtributoNrDocumento, int cdAtributoDtProtocolo, 
			int cdAtributoDsAssunto, int cdAtributoCdSolicitante, int cdEmpresa, int cdUsuario, int cdSetor) {
		
		return gerarDocumentos(cdTipoDocumentoArquivo, cdTipoDocumento, cdSituacaoDocumento, cdFormulario, arquivos, 
				cdAtributoNrDocumento, cdAtributoDtProtocolo, cdAtributoDsAssunto, cdAtributoCdSolicitante, 
				cdEmpresa, cdUsuario, cdSetor, null);
	}
	
	public static Result gerarDocumentos(int cdTipoDocumentoArquivo, int cdTipoDocumento, int cdSituacaoDocumento, 
			int cdFormulario, ArrayList<String> arquivos, int cdAtributoNrDocumento, int cdAtributoDtProtocolo, 
			int cdAtributoDsAssunto, int cdAtributoCdSolicitante, int cdEmpresa, int cdUsuario, int cdSetor, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(false);
			
			int cdArquivo = 0;
			Result result = null;
			
			int cdFase = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INICIAL", 0, 0, connect);
			
			for(int i = 0; i<arquivos.size(); i++) {
				cdArquivo = Integer.valueOf(arquivos.get(i));
				
				// NUMERO DE DOCUMENTO
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.txt_atributo_valor"
																 + " FROM grl_formulario_atributo_valor A"
																 + " LEFT OUTER JOIN grl_formulario_atributo B ON (B.cd_formulario_atributo = A.cd_formulario_atributo)"
																 + " WHERE A.cd_formulario_atributo = "+cdAtributoNrDocumento
																 + " AND A.cd_arquivo = "+cdArquivo);
				ResultSetMap rsmAtributoValor = new ResultSetMap(pstmt.executeQuery());
				String nrDocumento = null;
				if(rsmAtributoValor.next())
					nrDocumento = rsmAtributoValor.getString("TXT_ATRIBUTO_VALOR");
				else 
					return new Result(-1, "Erro ao buscar atributo NR_DOCUMENTO.");
				
				// DATA DE PROTOCOLO
				pstmt = connect.prepareStatement("SELECT A.txt_atributo_valor"
											   + " FROM grl_formulario_atributo_valor A"
											   + " LEFT OUTER JOIN grl_formulario_atributo B ON (B.cd_formulario_atributo = A.cd_formulario_atributo)"
											   + " WHERE A.cd_formulario_atributo = "+cdAtributoDtProtocolo
											   + " AND A.cd_arquivo = "+cdArquivo);
				rsmAtributoValor = new ResultSetMap(pstmt.executeQuery());
				GregorianCalendar dtProtocolo = null;
				if(rsmAtributoValor.next()) {
					String txtAtributoValor = rsmAtributoValor.getString("TXT_ATRIBUTO_VALOR");
					if(txtAtributoValor!=null)
						dtProtocolo = Util.convStringToCalendar(txtAtributoValor);					
				}
				else
					return new Result(-2, "Erro ao buscar atributo DT_PROTOCOLO.");
				
				// ASSUNTO
				pstmt = connect.prepareStatement("SELECT A.txt_atributo_valor"
											   + " FROM grl_formulario_atributo_valor A"
											   + " LEFT OUTER JOIN grl_formulario_atributo B ON (B.cd_formulario_atributo = A.cd_formulario_atributo)"
											   + " WHERE A.cd_formulario_atributo = "+cdAtributoDsAssunto
											   + " AND A.cd_arquivo = "+cdArquivo);
				rsmAtributoValor = new ResultSetMap(pstmt.executeQuery());
				String dsAssunto = null;
				if(rsmAtributoValor.next()) {
					dsAssunto = rsmAtributoValor.getString("TXT_ATRIBUTO_VALOR");
					
					// dsAssunto e' VARCHAR(140)
					// Evitar data truncation
					if(dsAssunto.length()>139) {
						dsAssunto = dsAssunto.substring(0, 139);
					}
				}
				else
					return new Result(-3, "Erro ao buscar atributo DS_ASSUNTO.");
				
				// DOCUMENTO
				Documento documento = new Documento(0/*cdDocumento*/, 
													cdArquivo,
													cdSetor, 
													cdUsuario, 
													null, 
													dtProtocolo, 
													0/*tpDocumento*/, 
													null/*txtObservacao*/, 
													null/*idDocumento*/, 
													nrDocumento, 
													cdTipoDocumento, 
													0/*cdServico*/, 
													0 /*cdAtendimento*/, 
													dsAssunto/*txtDocumento*/, 
													cdSetor/*cdSetorAtual*/, 
													cdSituacaoDocumento, 
													cdFase, 
													cdEmpresa, 
													0/*cdProcesso*/, 
													0/*tpPrioridade*/, 
													0/*cdDocumentoSuperior*/, 
													dsAssunto,
													null /*nrAtendimento*/,
													0/*lgNotificacao*/,
													0 /*cdTipoDocumentoAnterior*/, 
													null/*nrDocumentoExterno*/,
													null/*nrAssunto*/,
													null,
													null,
													0,
													1);
				result = DocumentoServices.save(documento, null/*solicitantes*/, connect);
				int cdDocumento = result.getCode();
				Documento doc = null;
				if(result.getCode()<0) {
					if(result.getCode()==-6) { //SE O DOCUMENTO EXISTIR
						cdDocumento = ((Integer)result.getObjects().get("CD_DOCUMENTO")).intValue();
						
						//Atualiza o arquivo correspondente ao documento gerado
						doc = DocumentoDAO.get(cdDocumento, connect);
						if(doc!=null && doc.getCdArquivo()==0) {
							doc.setCdArquivo(cdArquivo);
							int r = DocumentoDAO.update(doc, connect);
							if(r < 0) {
								return new Result(-4, "Erro ao atualizar arquivo do documento.");
							}
						}
					}
					else {
						connect.rollback();
						return new Result(-5, "Erro ao salvar documento no. "+nrDocumento);
					}
				}
				
				
				// Situacao do documento.
				// (o metodo DocumentoServices.save pode alterar a situacao do documento
				// as linhas abaixo garantem que o documento tenha a situacao correta)
				doc = DocumentoDAO.get(cdDocumento, connect);
				doc.setCdSituacaoDocumento(cdSituacaoDocumento);
				int r = DocumentoDAO.update(doc, connect);
				if(r < 0) {
					return new Result(-6, "Erro ao atualizar situação do documento.");
				}
				
				// ARQUIVO
				Arquivo arquivo = ArquivoDAO.get(cdArquivo, connect);
				if(arquivo==null) {
					connect.rollback();
					return new Result(-7, "Arquivo não encontrado.");
				}
							
				// DOCUMENTO_ARQUIVO
				DocumentoArquivo documentoArquivo = new DocumentoArquivo(cdArquivo, 
																		 cdDocumento, 
																		 arquivo.getNmArquivo(), 
																		 arquivo.getNmDocumento(), 
																		 arquivo.getDtArquivamento(), 
																		 arquivo.getBlbArquivo(), 
																		 0 /*lgComprimido*/, 
																		 cdTipoDocumentoArquivo, 
																		 0 /*stArquivo*/, 
																		 null /*idRepositorio*/,
																		 0 /*cdAssinatura*/,
																		 null /*txtOcr*/);
				result = DocumentoArquivoServices.save(documentoArquivo, true, connect);
				if(result.getCode()<0) {
					connect.rollback();
					return new Result(-8, "Erro ao salvar documentoArquivo.");
				}
				
				// DOCUMENTO OCORRENCIA
				int cdOcorrenciaIndexacao = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_INDEXACAO", 0, 0, connect);
				DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrencia(cdDocumento/*cdDocumento*/, 
																				cdOcorrenciaIndexacao/*cdTipoOcorrencia*/, 
																				0/*cdOcorrencia*/, 
																				cdUsuario/*cdUsuario*/, 
																				new GregorianCalendar()/*dtOcorrencia*/, 
																				"Arquivo indexado automáticamente."/*txtOcorrencia*/, 
																				DocumentoOcorrenciaServices.TP_VISIBILIDADE_PUBLICO/*tpVisibilidade*/, 0);
				result = DocumentoOcorrenciaServices.save(documentoOcorrencia, connect);
				if(result.getCode()<0) {
					connect.rollback();
					return new Result(-9, "Erro ao salvar documentoOcorrencia.");
				}				
				
				// Commit a cada 50 documentoArquivo
				//if(i%25==0) {
					connect.commit();
				//}
			}
			
			if(result.getCode()>0) {
				System.out.println("commit");
				connect.commit();
			}
			else {
				connect.rollback();
				return new Result(-10, result.getMessage());
			}
			
			return new Result(1, "Documentos vinculados com sucesso.");
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
	 * Gerar .pdf a partir de um arquivo do repositorio OneDrive
	 * 
	 * @param cdProcesso
	 * @param cdArquivo
	 * @return
	 */
	public static Result gerarPdf(int cdProcesso, int cdArquivo, boolean lgBloquearDocumento) {
		return gerarPdf(cdProcesso, cdArquivo, lgBloquearDocumento, null);
	}
	
	public static Result gerarPdf(int cdProcesso, int cdArquivo, boolean lgBloquearDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			ProcessoArquivo arquivo = ProcessoArquivoDAO.get(cdArquivo, cdProcesso, connect);
			if(arquivo==null) {
				return new Result (-2, "Arquivo não encontrado.");
			}
			if(arquivo.getIdRepositorio()==null || arquivo.getIdRepositorio().equals("")) {
				return new Result (-3, "Arquivo não existe no repositório OneDrive.");
			}
			
			OneDriveClient odc = OneDriveUtils.getClient(connect);
			if(odc==null) {
				return new Result(-4, "Erro ao conectar no OneDrive.");
			}
			
			byte[] docxContent = odc.downloadFile(arquivo.getIdRepositorio());
			if(docxContent==null) {
				return new Result(-5, "Erro ao baixar arquivo do OneDrive.");
			}
			
			byte[] pdfFile = Converter.docxToPdf(docxContent);
			if(pdfFile==null) {
				return new Result(-6, "Erro ao converter arquivo.");
			}
			
			arquivo.setBlbArquivo(pdfFile);
			
			/*
			 * Bloqueia acesso ao documento depois que o pdf é gerado
			 */
			if(lgBloquearDocumento) {
				arquivo.setIdRepositorio(null);
			}
			
			arquivo.setNmArquivo(arquivo.getNmArquivo()+".pdf");
			Result rArquivo = ProcessoArquivoServices.save(arquivo, true, connect);
			if(rArquivo.getCode()<=0) {
				System.out.println(rArquivo.getMessage());
				return new Result(-7, "Erro ao gravar arquivo.");
			}
			
						
			return new Result(1, "Arquivo gerado com sucesso!", "ARQUIVO", arquivo);

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
	 * Assina Digitalmente um arquivo gravado
	 * @param cdArquivo
	 * @param cdUsuario
	 * @param privateKeyBytes
	 * @return
	 */
	public static Result assinarArquivo(int cdArquivo, int cdUsuario, byte[] privateKeyBytes) {
		return assinarArquivo(cdArquivo, cdUsuario, privateKeyBytes, null);
	}
	
	public static Result assinarArquivo(int cdArquivo, int cdUsuario, byte[] privateKeyBytes, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			/**
			 * 1. CARREGAR ARQUIVO
			 * 2. VALIDAR USUARIO e CHAVES
			 * 3. ASSINAR ARQUIVO
			 * 4. GRAVAR ASSINATURA NO BANCO
			 */
			
			//1. CARREGAR ARQUIVO
			Arquivo arquivo = ArquivoDAO.get(cdArquivo, connect);
			
			if(arquivo==null || arquivo.getBlbArquivo()==null || arquivo.getBlbArquivo().length==0) {
				return new Result(-2, "Arquivo indicado é nulo ou inexistente.");
			}
			
			//2. VALIDAR USUARIO e CHAVES
			Usuario usuario = UsuarioDAO.get(cdUsuario, connect);
			
			if(usuario==null) {
				return new Result(-3, "Usuário indicado é nulo ou inexistente.");
			}
			
			UsuarioChave usuarioChave = UsuarioChaveServices.getChaveAtiva(cdUsuario, connect);
			
			if(usuarioChave==null) {
				return new Result(-4, "Usuário indicado não possui chave publica/privada gerada ou ativa.");
			}
			
			if(privateKeyBytes==null || privateKeyBytes.length==0) {
				return new Result(-5, "Chave privada é nula.");
			}
			
			if(privateKeyBytes!=usuarioChave.getBlbChavePrivada()) {
				return new Result(-6, "Chave privada não corresponde à chave ativa do usuário.");
			}
			
			//3. ASSINAR ARQUIVO
			Result r = AssinaturaDigitalServices.gerarAssinatura(privateKeyBytes, arquivo.getBlbArquivo());
			
			if(r.getCode()<=0) {
				return r;
			}
			
			//4. GRAVAR ASSINATURA NO BANCO
			byte[] blbAssinatura = (byte[]) r.getObjects().get("SIGNATURE");
			
			AssinaturaDigital assinaturaDigital = new AssinaturaDigital(0, /*cdAssinatura*/
					usuarioChave.getCdChave(), /*cdChave*/
					cdUsuario, /*cdUsuario*/
					new GregorianCalendar(), /*dtAssinatura*/
					blbAssinatura); /*blbAssinatura*/
			
			r = AssinaturaDigitalServices.save(assinaturaDigital, null, connect);
			
			if(r.getCode()<=0) {
				System.out.println(r.getMessage());
				Conexao.rollback(connect);
				return new Result(-7, "Erro ao gravar assinatura digital do arquivo.");
			}
			
			arquivo.setCdAssinatura(((AssinaturaDigital) r.getObjects().get("ASSINATURADIGITAL")).getCdAssinatura());
			r = save(arquivo, null, connect);
			
			if(r.getCode()<=0) {
				System.out.println(r.getMessage());
				Conexao.rollback(connect);
				return new Result(-8, "Erro ao gravar arquivo.");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Documento assinado digitalmente com sucesso!", "ARQUIVO", arquivo);

		}
		catch (Exception e) {
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
	
	public static void salvarArquivoDePostagemDO(byte[] blbArquivo, String tipoEdital, int tpArquivo) throws ValidacaoException{
		salvarArquivoDePostagemDO(blbArquivo, tipoEdital, tpArquivo, null);
	}
	
	public static void salvarArquivoDePostagemDO(byte[] blbArquivo, String tipoEdital, int tpArquivo, Connection connect) throws ValidacaoException{
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			Arquivo arquivo = new ArquivoPostagemDOBuilder().build(blbArquivo, tipoEdital, tpArquivo);
			ArquivoServices.save(arquivo, null, connect);
		}
		catch (Exception e){
			System.out.println("Erro em AitReportServices > salvarArquivoDePostagemDO()");
			e.printStackTrace();
			throw new ValidacaoException("Erro ao salvar arquivo de postagem");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static int verificarTipoArquivo(int tpArquivo) {

		int tpArquivoPublicacao = 0;
		
		switch(tpArquivo)
		{
			case NAI_PUBLICADA:
				tpArquivoPublicacao = Integer.valueOf((String) ParametroServices.getValorOfParametro("mob_tipo_arquivo_publicacao_nai"));
				break;
			case NIP_PUBLICADA:
				tpArquivoPublicacao = Integer.valueOf((String) ParametroServices.getValorOfParametro("mob_tipo_arquivo_publicacao_nip"));
				break;
			case PROCESSOS_PUBLICADOS:
				tpArquivoPublicacao = Integer.valueOf((String) ParametroServices.getValorOfParametro("MOB_TIPO_ARQUIVO_PUBLICACAO_PROCESSOS"));
				break;
		}
		
		return tpArquivoPublicacao;
	}
	
	protected static List<DocumentoPublicacao> buscarArquivosPublicados(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpArquivo) throws ValidacaoException{
		return buscarArquivosPublicados(dtInicial, dtFinal, tpArquivo, null);
	}
	
	protected static List<DocumentoPublicacao> buscarArquivosPublicados(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tpArquivo, Connection connect) throws ValidacaoException{
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			List<DocumentoPublicacao> listArquivosPublicados = new ArrayList<DocumentoPublicacao>();
			
			int codTipoArquivo = verificarTipoArquivo(tpArquivo);
			
			ResultSetMap rsmPublicados = buscarArquivosPublicacaoDO(dtInicial, dtFinal, codTipoArquivo);
			
			while (rsmPublicados.next()){
				listArquivosPublicados.add(criarArquivoPublicado(rsmPublicados.getRegister()));
			}
			
			return listArquivosPublicados;
		}
		catch (Exception e){
			System.out.println("Erro em ArquivoServices > buscarArquivosPublicados()");
			e.printStackTrace();
			throw new ValidacaoException("Erro ao fazer busca de arquivos publicados");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}

	}
	
	private static DocumentoPublicacao criarArquivoPublicado(HashMap<String, Object> hashMap) throws ValidacaoException
	{
		try
		{
			DocumentoPublicacao documentoPublicado = new DocumentoPublicacao();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			
			documentoPublicado.setCdDocumento((int) hashMap.get("CD_ARQUIVO"));
			documentoPublicado.setDataPublicacao(df.format(hashMap.get("DT_CRIACAO")));
			documentoPublicado.setDocumento((String) hashMap.get("NM_DOCUMENTO"));
			documentoPublicado.setNomeArquivo((String) hashMap.get("NM_ARQUIVO"));
			
			return documentoPublicado;
		}
		catch (Exception e)
		{
			System.out.println("Erro em ArquivoServices > criarArquivoPublicado()");
			e.printStackTrace();
			throw new ValidacaoException("Erro ao criar Arquivo Publicado");
		}
	}
	
	protected static byte[] pegarArquivoPublicado(int cdArquivo) throws ValidacaoException {
		return pegarArquivoPublicado(cdArquivo, null);
	}
	

	protected static byte[] pegarArquivoPublicado(int cdArquivo, Connection connect) throws ValidacaoException {
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			Arquivo arquivo =  ArquivoDAO.get(cdArquivo, connect);
			
			if (arquivo.getBlbArquivo().length <= 0) {
				throw new ValidacaoException("Nenhum arquivo de publicação encontrado para este documento");
			}
			
			return arquivo.getBlbArquivo();
		}
		catch(Exception e) {
			System.out.println("Erro em ArquivoServices > criarArquivoPublicado()");
			e.printStackTrace();
			throw new ValidacaoException("Erro ao pegar arquivo publicado.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	protected static ResultSetMap buscarArquivosPublicacaoDO(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int codTipoArquivo) throws ValidacaoException{
		return buscarArquivosPublicacaoDO(dtInicial, dtFinal, codTipoArquivo, null);
	}
	
	protected static ResultSetMap buscarArquivosPublicacaoDO(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int codTipoArquivo, Connection connect) throws ValidacaoException{
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull) {
			connect = Conexao.conectar();
		}
		
		try {
			String sql = " SELECT * FROM grl_arquivo " +
					     "   WHERE dt_criacao BETWEEN ? AND ? AND cd_tipo_arquivo = ?";
			
			PreparedStatement psmt = connect.prepareStatement(sql);
			psmt.setTimestamp(1, com.tivic.manager.util.Util.convCalendarToTimestamp(dtInicial));
			psmt.setTimestamp(2, com.tivic.manager.util.Util.convCalendarToTimestamp(dtFinal));
			psmt.setInt(3, codTipoArquivo);
			
			return new ResultSetMap(psmt.executeQuery());
		}
		catch (Exception e) {
			System.out.println("Erro em ArquivoDAO > buscarArquivosPublicacaoDO()");
			e.printStackTrace();
			throw new ValidacaoException("Erro ao consultar arquivos no banco");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
}
