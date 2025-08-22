package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.ZlibCompression;

public class ProcessoArquivoServices {

	public static Result save(ProcessoArquivo arquivo){
		return save(arquivo, true, null);
	}
	
	public static Result save(ProcessoArquivo arquivo, boolean updateBytes){
		return save(arquivo, updateBytes, null);
	}
	
	public static Result save(ProcessoArquivo arquivo, boolean updateBytes, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(arquivo==null)
				return new Result(-1, "Erro ao salvar. Arquivo é nulo");
			
			if(ParametroServices.getValorOfParametroAsInteger("LG_COMPRESS_FILE", 0) > 0) {
				LogUtils.debug("Compactando arquivo...");
				if(arquivo.getBlbArquivo()!=null) {
					LogUtils.debug("\tOriginal: "+arquivo.getBlbArquivo().length/1024/1024+" MB");
					arquivo.setLgComprimido(1);
					byte[] compress = ZlibCompression.compress(arquivo.getBlbArquivo());
					LogUtils.debug("\tComprimido: "+compress.length/1024/1024+" MB");
					arquivo.setBlbArquivo(compress);
					
				}	
				else
					LogUtils.debug("\tNão há bytes");
			}
			
			int retorno;
			if(arquivo.getCdArquivo()==0){
				arquivo.setDtArquivamento(new GregorianCalendar());
				
				LogUtils.debug("ProcessoArquivoServices.save");
				LogUtils.createTimer("ARQUIVO");
				
				retorno = ProcessoArquivoDAO.insert(arquivo, connect);
				arquivo.setCdArquivo(retorno);
			}
			else {
				retorno = update(arquivo, updateBytes, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			LogUtils.debug("ProcessoArquivoServices.save");
			LogUtils.destroyTimer("ARQUIVO");
			
			if(updateBytes)
				arquivo.setBlbArquivo(null);
			
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
	
	public static int update(ProcessoArquivo arquivo, boolean updateBytes , Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_PROCESSO_ARQUIVO SET CD_ARQUIVO=?,"+
												      		   "CD_PROCESSO=?,"+
												      		   "CD_ANDAMENTO=?,"+
												      		   "NM_ARQUIVO=?,"+
												      		   "NM_DOCUMENTO=?,"+
												      		   "DT_ARQUIVAMENTO=?,"+
												      		   "LG_COMPRIMIDO=?,"+ 
												      		   "ID_REPOSITORIO=?,"+
												      		   "CD_TIPO_DOCUMENTO=? " +
												      		   ((updateBytes)?", BLB_ARQUIVO=? ":"")+
												      		   "WHERE CD_ARQUIVO=? AND CD_PROCESSO=?");
			pstmt.setInt(1,arquivo.getCdArquivo());
			pstmt.setInt(2,arquivo.getCdProcesso());
			if(arquivo.getCdAndamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,arquivo.getCdAndamento());
			pstmt.setString(4,arquivo.getNmArquivo());
			pstmt.setString(5,arquivo.getNmDocumento());
			if(arquivo.getDtArquivamento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(arquivo.getDtArquivamento().getTimeInMillis()));
			pstmt.setInt(7,arquivo.getLgComprimido());
			pstmt.setString(8,arquivo.getIdRepositorio());
			if(arquivo.getCdTipoDocumento()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,arquivo.getCdTipoDocumento());
			
			if(updateBytes){
				if(arquivo.getBlbArquivo()==null)
					pstmt.setNull(10, Types.BINARY);
				else {
					//arquivo.setBlbArquivo(ZlibCompression.compress(arquivo.getBlbArquivo()));
					pstmt.setBytes(10,arquivo.getBlbArquivo());
				}
			}
			
			pstmt.setInt(updateBytes?11:10, arquivo.getCdArquivo());
			pstmt.setInt(updateBytes?12:11, arquivo.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoServices.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdArquivo, int cdProcesso){
		return remove(cdArquivo, cdProcesso, false, null);
	}
	
	public static Result remove(int cdArquivo, int cdProcesso, boolean cascade){
		return remove(cdArquivo, cdProcesso, cascade, null);
	}
	
	public static Result remove(int cdArquivo, int cdProcesso, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				
				/** ARQUIVOS ASSOCIADOS ***/
//				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_arquivo " +
//						" WHERE cd_andamento = ? AND cd_processo = ?");
//				
//				pstmt.setInt(1, cdArquivo);
//				pstmt.setInt(2, cdProcesso);
//				
//				retorno = pstmt.executeUpdate();
				retorno = 1;
			}
				
			if(!cascade || retorno>0)
				retorno = ProcessoArquivoDAO.delete(cdArquivo, cdProcesso, connect);
						
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este arquivo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Arquivo excluído com sucesso!");
		}
		catch(Exception e){
			System.out.println("Erro! ProcessoArquivoServices.remove: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir arquivo!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static byte[] getBytesArquivo(int cdArquivo, int cdProcesso) {
		return getBytesArquivo(cdArquivo, cdProcesso, null);
	}

	public static byte[] getBytesArquivo(int cdArquivo, int cdProcesso, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ProcessoArquivo arquivo = ProcessoArquivoDAO.get(cdArquivo, cdProcesso, connection);
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
	 * Busca os arquivos vinculados a processos.
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
			String nrProcesso = null;
			String nmArquivo = null;
			String nmDocumento = null;
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("nrRegistros")) {
					nrRegistros = Integer.valueOf(criterios.get(i).getValue().toString().trim());
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("nrProcesso")) {
					nrProcesso = criterios.get(i).getValue().toString().trim().replaceAll("\\.", "").replaceAll("-", "");
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("nmArquivo")) {
					nmArquivo = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("nmDocumento")) {
					nmDocumento = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(nrRegistros, 0);

			String sql = " SELECT " + sqlLimit[0]
					   + " A.cd_arquivo, A.cd_processo, A.cd_andamento, A.nm_arquivo, A.nm_documento, A.dt_arquivamento, A.cd_tipo_documento, A.cd_agenda_item,"
					   + " A.id_repositorio, A.txt_ocr, "
					   + " B.*, D.nm_pessoa AS nm_representado, D1.nm_pessoa AS nm_adverso, "
					   + " E.nm_pessoa AS nm_advogado_representante, F.*, G.dt_andamento,"
					   + " H.*, I.*, J.*, K.*, L.*, M.*"
					   + " FROM prc_processo_arquivo A"
					   + " JOIN prc_processo B ON (B.cd_processo = A.cd_processo)"
					   + " LEFT OUTER JOIN prc_parte_cliente C ON (B.cd_processo = C.cd_processo)"
					   + " LEFT OUTER JOIN prc_outra_parte C1 ON (B.cd_processo = C1.cd_processo)"
					   + " LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = C.cd_pessoa)"
					   + " LEFT OUTER JOIN grl_pessoa D1 ON (D1.cd_pessoa = C1.cd_pessoa)"
					   + " LEFT OUTER JOIN grl_pessoa E ON (E.cd_pessoa = B.cd_advogado)"
					   + " LEFT OUTER JOIN prc_tipo_processo F ON (F.cd_tipo_processo = B.cd_tipo_processo)"
					   + " LEFT OUTER JOIN prc_processo_andamento G ON (G.cd_processo = B.cd_processo AND A.cd_andamento = G.cd_andamento)"
					   + " LEFT OUTER JOIN prc_juizo H ON (H.cd_juizo = B.cd_juizo)"
					   + " LEFT OUTER JOIN prc_area_direito I ON (I.cd_area_direito = F.cd_area_direito)"
					   + " LEFT OUTER JOIN prc_tipo_pedido J ON (J.cd_tipo_pedido = B.cd_tipo_pedido)"
					   + " LEFT OUTER JOIN prc_tipo_objeto K ON (K.cd_tipo_objeto = B.cd_tipo_objeto)"
					   + " LEFT OUTER JOIN gpn_tipo_documento L ON (L.cd_tipo_documento = A.cd_tipo_documento)"
					   + " LEFT OUTER JOIN prc_tipo_andamento M ON (M.cd_tipo_andamento = G.cd_tipo_andamento)"
					   + " WHERE 1=1 ";
			
			if(nrProcesso != null) {
				sql +=  " AND TRANSLATE(B.nr_processo, '-.', '') iLIKE '%"+Util.limparAcentos(nrProcesso)+"%' ";
			}
			
			if(nmArquivo != null) {
				sql +=  " AND TRANSLATE(nm_arquivo, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmArquivo)+"%' ";
			}
			
			if(nmDocumento != null) {
				sql +=  " AND TRANSLATE(nm_documento, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmDocumento)+"%' ";
			}
			
			
			
			return Search.findAndLog(sql, sqlLimit[1], criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
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
}