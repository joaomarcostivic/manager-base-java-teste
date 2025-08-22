package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ArquivoDAO{

	public static int insert(Arquivo objeto) {
		return insert(objeto, null);
	}
	
	/**
	 * @author Alvaro(Modificação)
	 * @param objeto
	 * @param connect
	 * @since 31/03/2015
	 * @return
	 */
	
	public static int insert(Arquivo objeto, Connection connect) {
		return insert(objeto, connect, false);
	}
	
	public static int insert(Arquivo objeto, Connection connect, boolean sync){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = sync ? Conexao.getSequenceCodeSync("grl_arquivo", connect) : Conexao.getSequenceCode("grl_arquivo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdArquivo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_arquivo (cd_arquivo,"+
			                                  "nm_arquivo,"+
			                                  "dt_arquivamento,"+
			                                  "nm_documento,"+
			                                  "blb_arquivo,"+
			                                  "cd_tipo_arquivo,"+
			                                  "dt_criacao,"+
			                                  "cd_usuario,"+
			                                  "st_arquivo,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "cd_assinatura,"+
			                                  "txt_ocr,"+
			                                  "nr_registros," +
			                                  "txt_caminho_arquivo,"+
			                                  "ds_arquivo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmArquivo());
			if(objeto.getDtArquivamento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtArquivamento().getTimeInMillis()));
			pstmt.setString(4,objeto.getNmDocumento());
			if(objeto.getBlbArquivo()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setBytes(5,objeto.getBlbArquivo());
			if(objeto.getCdTipoArquivo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoArquivo());
			
			pstmt.setTimestamp(7,new Timestamp(  System.currentTimeMillis()) );
			
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			pstmt.setInt(9,objeto.getStArquivo());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getCdAssinatura()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdAssinatura());
			pstmt.setString(13,objeto.getTxtOcr());
			if(objeto.getNrRegistros()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14, objeto.getNrRegistros());
			pstmt.setString(15, objeto.getTxtCaminhoArquivo());
			pstmt.setString(16, objeto.getDsArquivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Arquivo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Arquivo objeto, int cdArquivoOld) {
		return update(objeto, cdArquivoOld, null);
	}

	public static int update(Arquivo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}
	/**
	 * @author Alvaro(Modificação), Sapucaia (Assinatura Digital)
	 * @param objeto
	 * @param cdArquivoOld
	 * @param connect
	 * @since 31/03/2015
	 * @since 14/11/2016
	 * @return
	 */
	public static int update(Arquivo objeto, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_arquivo SET cd_arquivo=?,"+
												      		   "nm_arquivo=?,"+
												      		   "dt_arquivamento=?,"+
												      		   "nm_documento=?,"+
												      		   ( ( objeto.getBlbArquivo()!=null )
												      				   ?"blb_arquivo=?,"
												      				   :""
												      		   )+
												      		   "cd_tipo_arquivo=?,"+
												      		   "cd_usuario=?,"+
												      		   "st_arquivo=?, "+
												      		   "dt_inicial=?, "+ 
												      		   "dt_final=?,"+
												      		   "cd_assinatura=?,"+
												      		   "txt_ocr=? WHERE cd_arquivo=?");
			int parameterIndex = 1;
			pstmt.setInt(parameterIndex++, objeto.getCdArquivo());
			pstmt.setString(parameterIndex++,objeto.getNmArquivo());
			if(objeto.getDtArquivamento()==null)
				pstmt.setNull(parameterIndex++, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(parameterIndex++,new Timestamp(objeto.getDtArquivamento().getTimeInMillis()));
			pstmt.setString(parameterIndex++,objeto.getNmDocumento());
			
			if(objeto.getBlbArquivo()!=null)
				pstmt.setBytes(parameterIndex++,objeto.getBlbArquivo());

			if(objeto.getCdTipoArquivo()==0)
				pstmt.setNull(parameterIndex++, Types.INTEGER);
			else
				pstmt.setInt(parameterIndex++,objeto.getCdTipoArquivo());
			
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(parameterIndex++, Types.INTEGER);
			else
				pstmt.setInt(parameterIndex++,objeto.getCdUsuario());
			pstmt.setInt(parameterIndex++,objeto.getStArquivo());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(parameterIndex++, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(parameterIndex++,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(parameterIndex++, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(parameterIndex++,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getCdAssinatura()==0)
				pstmt.setNull(parameterIndex++, Types.INTEGER);
			else
				pstmt.setInt(parameterIndex++,objeto.getCdAssinatura());
			pstmt.setString(parameterIndex++,objeto.getTxtOcr());
			pstmt.setInt(parameterIndex, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdArquivo) {
		return delete(cdArquivo, null);
	}

	public static int delete(int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_arquivo WHERE cd_arquivo=?");
			pstmt.setInt(1, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Arquivo get(int cdArquivo) {
		return get(cdArquivo, null);
	}

	public static Arquivo get(int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo WHERE cd_arquivo=?");
			pstmt.setInt(1, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Arquivo(rs.getInt("cd_arquivo"),
						rs.getString("nm_arquivo"),
						(rs.getTimestamp("dt_arquivamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_arquivamento").getTime()),
						rs.getString("nm_documento"),
						rs.getBytes("blb_arquivo")==null?null:rs.getBytes("blb_arquivo"),
						rs.getInt("cd_tipo_arquivo"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						rs.getInt("cd_usuario"),
						rs.getInt("st_arquivo"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("cd_assinatura"),
						rs.getString("txt_ocr"),
						rs.getInt("nr_registros"),
						rs.getString("txt_caminho_arquivo"),
						rs.getString("ds_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Arquivo> getList() {
		return getList(null);
	}

	public static ArrayList<Arquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Arquivo> list = new ArrayList<Arquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Arquivo obj = ArquivoDAO.get(rsm.getInt("cd_arquivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
}
