package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class ProcessoArquivoDAO{

	public static int insert(ProcessoArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProcessoArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_arquivo");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_processo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProcesso()));
			int code = Conexao.getSequenceCode("prc_processo_arquivo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdArquivo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_processo_arquivo (cd_arquivo,"+
			                                  "cd_processo,"+
			                                  "cd_andamento,"+
			                                  "nm_arquivo,"+
			                                  "nm_documento,"+
			                                  "dt_arquivamento,"+
			                                  "blb_arquivo,"+
			                                  "lg_comprimido,"+
			                                  "st_arquivo,"+
			                                  "cd_agenda_item,"+
			                                  "dt_backup,"+
			                                  "id_repositorio,"+
			                                  "cd_assinatura,"+
			                                  "txt_ocr,"+
			                                  "cd_tipo_documento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getCdAndamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAndamento());
			pstmt.setString(4,objeto.getNmArquivo());
			pstmt.setString(5,objeto.getNmDocumento());
			if(objeto.getDtArquivamento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtArquivamento().getTimeInMillis()));
			if(objeto.getBlbArquivo()==null)
				pstmt.setNull(7, Types.BINARY);
			else
				pstmt.setBytes(7,objeto.getBlbArquivo());
			pstmt.setInt(8,objeto.getLgComprimido());
			pstmt.setInt(9,objeto.getStArquivo());
			pstmt.setInt(10,objeto.getCdAgendaItem());
			if(objeto.getDtBackup()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtBackup().getTimeInMillis()));
			pstmt.setString(12,objeto.getIdRepositorio());
			if(objeto.getCdAssinatura()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdAssinatura());
			pstmt.setString(14,objeto.getTxtOcr());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTipoDocumento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProcessoArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProcessoArquivo objeto, int cdArquivoOld, int cdProcessoOld) {
		return update(objeto, cdArquivoOld, cdProcessoOld, null);
	}

	public static int update(ProcessoArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProcessoArquivo objeto, int cdArquivoOld, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_processo_arquivo SET cd_arquivo=?,"+
												      		   "cd_processo=?,"+
												      		   "cd_andamento=?,"+
												      		   "nm_arquivo=?,"+
												      		   "nm_documento=?,"+
												      		   "dt_arquivamento=?,"+
												      		   "blb_arquivo=?,"+
												      		   "lg_comprimido=?,"+
												      		   "st_arquivo=?,"+
												      		   "cd_agenda_item=?,"+
												      		   "dt_backup=?,"+
												      		   "id_repositorio=?,"+
												      		   "cd_assinatura=?,"+
												      		   "txt_ocr=?,"+
												      		   "cd_tipo_documento=? WHERE cd_arquivo=? AND cd_processo=?");
			pstmt.setInt(1,objeto.getCdArquivo());
			pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getCdAndamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAndamento());
			pstmt.setString(4,objeto.getNmArquivo());
			pstmt.setString(5,objeto.getNmDocumento());
			if(objeto.getDtArquivamento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtArquivamento().getTimeInMillis()));
			if(objeto.getBlbArquivo()==null)
				pstmt.setNull(7, Types.BINARY);
			else
				pstmt.setBytes(7,objeto.getBlbArquivo());
			pstmt.setInt(8,objeto.getLgComprimido());
			pstmt.setInt(9,objeto.getStArquivo());
			pstmt.setInt(10,objeto.getCdAgendaItem());
			if(objeto.getDtBackup()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtBackup().getTimeInMillis()));
			pstmt.setString(12,objeto.getIdRepositorio());
			if(objeto.getCdAssinatura()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdAssinatura());
			pstmt.setString(14,objeto.getTxtOcr());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTipoDocumento());
			pstmt.setInt(16, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.setInt(17, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdArquivo, int cdProcesso) {
		return delete(cdArquivo, cdProcesso, null);
	}

	public static int delete(int cdArquivo, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_arquivo WHERE cd_arquivo=? AND cd_processo=?");
			pstmt.setInt(1, cdArquivo);
			pstmt.setInt(2, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProcessoArquivo get(int cdArquivo, int cdProcesso) {
		return get(cdArquivo, cdProcesso, null);
	}

	public static ProcessoArquivo get(int cdArquivo, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_arquivo WHERE cd_arquivo=? AND cd_processo=?");
			pstmt.setInt(1, cdArquivo);
			pstmt.setInt(2, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProcessoArquivo(rs.getInt("cd_arquivo"),
						rs.getInt("cd_processo"),
						rs.getInt("cd_andamento"),
						rs.getString("nm_arquivo"),
						rs.getString("nm_documento"),
						(rs.getTimestamp("dt_arquivamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_arquivamento").getTime()),
						rs.getBytes("blb_arquivo")==null?null:rs.getBytes("blb_arquivo"),
						rs.getInt("lg_comprimido"),
						rs.getInt("st_arquivo"),
						rs.getInt("cd_agenda_item"),
						(rs.getTimestamp("dt_backup")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_backup").getTime()),
						rs.getString("id_repositorio"),
						rs.getInt("cd_assinatura"),
						rs.getString("txt_ocr"),
						rs.getInt("cd_tipo_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProcessoArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<ProcessoArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProcessoArquivo> list = new ArrayList<ProcessoArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProcessoArquivo obj = ProcessoArquivoDAO.get(rsm.getInt("cd_arquivo"), rsm.getInt("cd_processo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_processo_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}