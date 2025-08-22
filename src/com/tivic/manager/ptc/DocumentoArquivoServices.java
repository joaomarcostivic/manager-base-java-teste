package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.manager.seg.AssinaturaDigital;
import com.tivic.manager.seg.AssinaturaDigitalDAO;
import com.tivic.manager.seg.AssinaturaDigitalServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioChave;
import com.tivic.manager.seg.UsuarioChaveDAO;
import com.tivic.manager.seg.UsuarioChaveServices;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.ZlibCompression;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class DocumentoArquivoServices {
	public static Result save(DocumentoArquivo arquivo){
		return save(arquivo, true, null);
	}
	
	public static Result save(DocumentoArquivo arquivo, boolean updateBytes){
		return save(arquivo, updateBytes, null);
	}
	
	public static Result save(DocumentoArquivo arquivo, boolean updateBytes, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(arquivo==null)
				return new Result(-1, "Erro ao salvar. Arquivo é nulo");
			
			//arquivo.setLgComprimido(1);
			//arquivo.setBlbArquivo(ZlibCompression.compress(arquivo.getBlbArquivo()));
			
			int retorno;
			DocumentoArquivo a = DocumentoArquivoDAO.get(arquivo.getCdArquivo(), arquivo.getCdDocumento(), connect);
			
			if(a==null){ //arquivo ainda não vinculado ao documento
				arquivo.setDtArquivamento(new GregorianCalendar());
				retorno = DocumentoArquivoDAO.insert(arquivo, connect);
				arquivo.setCdArquivo(retorno);
			}
			else {
				retorno = update(arquivo, updateBytes, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao vincular arquivo ao documento.":"Salvo com sucesso...", "ARQUIVO", arquivo);
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
	
	public static int update(DocumentoArquivo arquivo, boolean updateBytes , Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PTC_DOCUMENTO_ARQUIVO SET CD_ARQUIVO=?,"+
															   " CD_DOCUMENTO=?,"+
													      	   " NM_ARQUIVO=?,"+
													      	   " NM_DOCUMENTO=?,"+
													      	   " DT_ARQUIVAMENTO=?,"+
												      		   ((updateBytes)?" BLB_ARQUIVO=?, ":"")+
												      		   " LG_COMPRIMIDO=?,"+
												      		   " CD_TIPO_DOCUMENTO=?,"+
												      		   " ST_ARQUIVO=?,"+
												      		   " ID_REPOSITORIO=?,"+
								                               " CD_ASSINATURA=?,"+
								                               " TXT_OCR=?"+
												      		   " WHERE CD_ARQUIVO=? AND CD_DOCUMENTO=?");
			pstmt.setInt(1,arquivo.getCdArquivo());
			pstmt.setInt(2,arquivo.getCdDocumento());
			pstmt.setString(3,arquivo.getNmArquivo());
			pstmt.setString(4,arquivo.getNmDocumento());
			if(arquivo.getDtArquivamento()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(arquivo.getDtArquivamento().getTimeInMillis()));
			if(updateBytes){
				if(arquivo.getBlbArquivo()==null)
					pstmt.setNull(6, Types.BINARY);
				else {
					//arquivo.setBlbArquivo(ZlibCompression.compress(arquivo.getBlbArquivo()));
					pstmt.setBytes(6,arquivo.getBlbArquivo());
				}
			}
			pstmt.setInt(updateBytes?7:6,arquivo.getLgComprimido());
			if(arquivo.getCdTipoDocumento()==0)
				pstmt.setNull(updateBytes?8:7, Types.INTEGER);
			else
				pstmt.setInt(updateBytes?8:7, arquivo.getCdTipoDocumento());
			pstmt.setInt(updateBytes?9:8, arquivo.getStArquivo());
			pstmt.setString(updateBytes?10:9, arquivo.getIdRepositorio());
			
			if(arquivo.getCdAssinatura()==0)
				pstmt.setNull(updateBytes?11:10, Types.INTEGER);
			else
				pstmt.setInt(updateBytes?11:10, arquivo.getCdAssinatura());
			pstmt.setString(updateBytes?12:11, arquivo.getTxtOcr());
			
			pstmt.setInt(updateBytes?13:12, arquivo.getCdArquivo());
			pstmt.setInt(updateBytes?14:13, arquivo.getCdDocumento());
			pstmt.executeUpdate();
			return pstmt.executeUpdate();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoServices.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdArquivo, int cdDocumento){
		return remove(cdArquivo, cdDocumento, false, null);
	}
	
	public static Result remove(int cdArquivo, int cdDocumento, boolean cascade){
		return remove(cdArquivo, cdDocumento, cascade, null);
	}
	
	public static Result remove(int cdArquivo, int cdDocumento, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				
				/** ARQUIVOS ASSOCIADOS ***/
				retorno = 1;
			}
				
			if(!cascade || retorno>0)
				retorno = DocumentoArquivoDAO.delete(cdArquivo, cdDocumento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este arquivo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Arquivo excluído com sucesso!");
		}
		catch(Exception e){
			System.out.println("Erro! DocumentoArquivoServices.remove: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir arquivo!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static byte[] getBytesArquivo(int cdArquivo, int cdDocumento) {
		return getBytesArquivo(cdArquivo, cdDocumento, null);
	}

	public static byte[] getBytesArquivo(int cdArquivo, int cdDocumento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			DocumentoArquivo arquivo = DocumentoArquivoDAO.get(cdArquivo, cdDocumento, connection);
			return arquivo==null ? null : arquivo.getLgComprimido()==0 ? arquivo.getBlbArquivo() : ZlibCompression.decompress(arquivo.getBlbArquivo());
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
	 * Busca os arquivos vinculados a documentos.
	 * 
	 * @param criterios ArrayList<ItemComparator> os critérios da busca
	 * @return ResultSetMap com o resultado da busca
	 * @author Maurício
	 * @since 18/06/2014
	 * @see com.tivic.manager.grl.ArquivoServices.find
	 */
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			int nrRegistros = 0;
			int lgFormulario = 0;
			String sqlTxtAtributoValor = ""; 
			String sqlCdAtributoValor = ""; 
			String txtOcr = "";
			String nrDocumento = "";
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("nrRegistros")) {
					nrRegistros = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgFormulario")) {
					lgFormulario = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("L.cd_formulario_atributo")) {
					sqlCdAtributoValor += (sqlCdAtributoValor.equals("") ? " L.cd_formulario_atributo IN ( " : ",") + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("L.txt_atributo_valor")) {
					sqlTxtAtributoValor += (!sqlTxtAtributoValor.equals("") ? " OR " : "") + " L.txt_atributo_valor LIKE UPPER('%"+ criterios.get(i).getValue().toString().trim() +"%') ";
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.txt_ocr")) {
					txtOcr = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("B.nr_documento")) {
					nrDocumento = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(nrRegistros, 0);
			
			String sql = " SELECT " + sqlLimit[0]
					   + " A.cd_arquivo, A.cd_documento, A.nm_arquivo, A.nm_documento, A.dt_arquivamento, A.cd_tipo_documento,"
					   + " B.*, C.cd_tipo_documento AS cd_tipo_documento_arquivo, C.nm_tipo_documento AS nm_tipo_documento_arquivo,"
					   + " D.*, E.nm_setor, E.sg_setor, F.nm_setor AS nm_setor_atual, F.sg_setor AS sg_setor_atual, G.*, I.nm_pessoa AS nm_solicitante, "
					   + (lgFormulario==1? " L.*, " : " ")
					   + " M.nm_situacao_documento"
					   + " FROM ptc_documento_arquivo A"
					   + " JOIN ptc_documento B ON (B.cd_documento = A.cd_documento)"
					   + " LEFT OUTER JOIN grl_tipo_documento C ON (C.cd_tipo_documento = A.cd_tipo_documento)"
					   + " LEFT OUTER JOIN gpn_tipo_documento D ON (D.cd_tipo_documento = B.cd_tipo_documento)"
					   + " LEFT OUTER JOIN grl_setor E ON (E.cd_setor = B.cd_setor)"
					   + " LEFT OUTER JOIN grl_setor F ON (F.cd_setor = B.cd_setor_atual)"
					   + " LEFT OUTER JOIN ptc_situacao_documento G ON (G.cd_situacao_documento = B.cd_situacao_documento)"
					   + " LEFT OUTER JOIN ptc_documento_pessoa H ON (H.cd_documento = B.cd_documento)"
					   + " LEFT OUTER JOIN grl_pessoa I ON (I.cd_pessoa = H.cd_pessoa)"
					   + " LEFT OUTER JOIN ptc_situacao_documento M ON (B.cd_situacao_documento=M.cd_situacao_documento)";
			
			if(lgFormulario==1) {
				sql+=" LEFT OUTER JOIN grl_formulario J ON (J.cd_formulario = D.cd_formulario)" +
					 " LEFT OUTER JOIN grl_formulario_atributo K ON (K.cd_formulario = J.cd_formulario)" +
					 " LEFT OUTER JOIN grl_formulario_atributo_valor L ON (L.cd_formulario_atributo = K.cd_formulario_atributo "
					 + "												AND L.cd_documento = A.cd_documento"
					 + "												AND L.cd_arquivo_documento = A.cd_arquivo)"//AND L.cd_arquivo = B.cd_arquivo) "
					 + (!sqlTxtAtributoValor.equals("") ? " WHERE "+sqlCdAtributoValor + ") AND (" +sqlTxtAtributoValor  +")" : "");
			}
			else {
				sql += " WHERE 1=1 ";
			}
			
			if(!txtOcr.equals("")) {
				
				txtOcr = txtOcr.toLowerCase().replaceAll("à|á|ã|ä|â", "_");
				txtOcr = txtOcr.toLowerCase().replaceAll("è|é|ë|ê", "_");
				txtOcr = txtOcr.toLowerCase().replaceAll("ì|í|ï|î", "_");
				txtOcr = txtOcr.toLowerCase().replaceAll("ò|ó|õ|ö|ô", "_");
				txtOcr = txtOcr.toLowerCase().replaceAll("ù|ú|ü|û", "_");
				txtOcr = txtOcr.toLowerCase().replaceAll("ç", "_");
				
				sql += " AND TRANSLATE(UPPER(A.txt_ocr), 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+txtOcr.toUpperCase()+"%' ";
			}
			
			if(!nrDocumento.equals("")) {
				nrDocumento = nrDocumento.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
				sql += " AND (REPLACE(REPLACE(REPLACE(B.nr_documento, '.', ''), '-', ''), '/', '') LIKE '%"+ nrDocumento +"%')";
			}
						
			return Search.find(sql, sqlLimit[1], criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
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
	 * @author Sapucaia
	 * @since Nov/2016
	 * @return
	 */
	public static Result assinarArquivo(int cdArquivo, int cdDocumento, int cdUsuario, String nmSenhaUsuario, byte[] privateKeyBytes) {
		return assinarArquivo(cdArquivo, cdDocumento, cdUsuario, nmSenhaUsuario, privateKeyBytes, null);
	}
	
	public static Result assinarArquivo(int cdArquivo, int cdDocumento, int cdUsuario, String nmSenhaUsuario, byte[] privateKeyBytes, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			/**
			 * 1. CARREGAR ARQUIVO
			 * 2. VALIDAR USUARIO e CHAVES
			 * 3. AUTENTICAR USUARIO
			 * 4. ASSINAR ARQUIVO
			 * 5. GRAVAR ASSINATURA NO BANCO
			 */
			
			//1. CARREGAR ARQUIVO
			DocumentoArquivo arquivo = DocumentoArquivoDAO.get(cdArquivo, cdDocumento, connect);
			
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
			
			if(usuarioChave.getBlbChavePrivada()==null || usuarioChave.getBlbChavePrivada().length==0) {
				return new Result(-5, "Chave privada não corresponde.");
			}
			
			
			if(privateKeyBytes.length!=usuarioChave.getBlbChavePrivada().length) {
				return new Result(-6, "Chave privada não corresponde à chave ativa do usuário.");
			}
			
			//3. AUTENTICAR USUARIO
			Result r = UsuarioServices.autenticar(usuario.getNmLogin(), nmSenhaUsuario, null, null, 0, connect);
			if(r.getCode()<=0) {
				return new Result(-7, "Usuário não pode ser autenticado.");
			}
			
			//4. ASSINAR ARQUIVO
			r = AssinaturaDigitalServices.gerarAssinatura(privateKeyBytes, arquivo.getBlbArquivo());
			
			if(r.getCode()<=0) {
				return r;
			}
			
			//5. GRAVAR ASSINATURA NO BANCO
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
				return new Result(-8, "Erro ao gravar assinatura digital do arquivo.");
			}
			
			arquivo.setCdAssinatura(((AssinaturaDigital) r.getObjects().get("ASSINATURADIGITAL")).getCdAssinatura());
			r = save(arquivo, false, connect);
			
			if(r.getCode()<=0) {
				System.out.println(r.getMessage());
				Conexao.rollback(connect);
				return new Result(-9, "Erro ao gravar arquivo.");
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
	
	public static Result verificarAssinatura(int cdArquivo, int cdDocumento) {
		return verificarAssinatura(cdArquivo, cdDocumento, null);
	}
	
	public static Result verificarAssinatura(int cdArquivo, int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			/**
			 * 1. CARREGAR ARQUIVO
			 * 2. CARREGAR ASSINATURA
			 * 3. CARREGAR USUARIO
			 * 4. CARREGAR CHAVE PUBLICA
			 * 5. VALIDAR ASSINATURA COM A CHAVE PUBLICA
			 */
			
			//1. CARREGAR ARQUIVO
			DocumentoArquivo arquivo = DocumentoArquivoDAO.get(cdArquivo, cdDocumento, connect);
			
			if(arquivo==null || arquivo.getBlbArquivo()==null || arquivo.getBlbArquivo().length==0) {
				return new Result(-2, "Arquivo indicado é nulo ou inexistente.");
			}
			
			//2. CARREGAR ASSINATURA
			AssinaturaDigital assinatura = AssinaturaDigitalDAO.get(arquivo.getCdAssinatura(), connect);
			
			if(assinatura==null || assinatura.getBlbAssinatura()==null || assinatura.getBlbAssinatura().length==0) {
				return new Result(-3, "Assinatura é nula ou inexistente.");
			}
			
			//3. CARREGAR USUARIO
			Usuario usuario = UsuarioDAO.get(assinatura.getCdUsuario(), connect);
			
			if(usuario==null) {
				return new Result(-4, "Usuário da assinatura é nulo ou inexistente.");
			}
			
			//4. CARREGAR CHAVE PUBLICA
			UsuarioChave chaves = UsuarioChaveDAO.get(assinatura.getCdChave(), assinatura.getCdUsuario(), connect);
			
			if(chaves==null) {
				return new Result(-5, "Chave da assinatura é nula ou inexistente.");
			}
			
			//5. VALIDAR ASSINATURA COM A CHAVE PUBLICA
			return AssinaturaDigitalServices.validarAssinatura(chaves.getBlbChavePublica(), arquivo.getBlbArquivo(), assinatura.getBlbAssinatura());
			
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
		
}
