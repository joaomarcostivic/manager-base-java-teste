package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ModeloDocumentoDAO{

	public static int insert(ModeloDocumento objeto) {
		return insert(objeto, null);
	}

	public static int insert(ModeloDocumento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_modelo_documento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdModelo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_modelo_documento (cd_modelo,"+
			                                  "nm_modelo,"+
			                                  "txt_modelo,"+
			                                  "tp_modelo,"+
			                                  "blb_conteudo,"+
			                                  "txt_conteudo,"+
			                                  "st_modelo,"+
			                                  "nm_titulo,"+
			                                  "cd_tipo_documento,"+
			                                  "id_modelo,"+
			                                  "url_modelo,"+
			                                  "id_repositorio,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmModelo());
			pstmt.setString(3,objeto.getTxtModelo());
			pstmt.setInt(4,objeto.getTpModelo());
			if(objeto.getBlbConteudo()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setBytes(5,objeto.getBlbConteudo());
			pstmt.setString(6,objeto.getTxtConteudo());
			pstmt.setInt(7,objeto.getStModelo());
			pstmt.setString(8,objeto.getNmTitulo());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdTipoDocumento());
			pstmt.setString(10,objeto.getIdModelo());
			pstmt.setString(11,objeto.getUrlModelo());
			pstmt.setString(12,objeto.getIdRepositorio());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ModeloDocumento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ModeloDocumento objeto, int cdModeloOld) {
		return update(objeto, cdModeloOld, null);
	}

	public static int update(ModeloDocumento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ModeloDocumento objeto, int cdModeloOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_modelo_documento SET cd_modelo=?,"+
												      		   "nm_modelo=?,"+
												      		   "txt_modelo=?,"+
												      		   "tp_modelo=?,"+
												      		   "blb_conteudo=?,"+
												      		   "txt_conteudo=?,"+
												      		   "st_modelo=?,"+
												      		   "nm_titulo=?,"+
												      		   "cd_tipo_documento=?,"+
												      		   "id_modelo=?,"+
												      		   "url_modelo=?,"+
												      		   "id_repositorio=?,"+
												      		   "cd_empresa=?"+
												      		   " WHERE cd_modelo=?");
			pstmt.setInt(1,objeto.getCdModelo());
			pstmt.setString(2,objeto.getNmModelo());
			pstmt.setString(3,objeto.getTxtModelo());
			pstmt.setInt(4,objeto.getTpModelo());
			if(objeto.getBlbConteudo()==null)
				pstmt.setNull(5, Types.BINARY);
			else
				pstmt.setBytes(5,objeto.getBlbConteudo());
			pstmt.setString(6,objeto.getTxtConteudo());
			pstmt.setInt(7,objeto.getStModelo());
			pstmt.setString(8,objeto.getNmTitulo());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdTipoDocumento());
			pstmt.setString(10,objeto.getIdModelo());
			pstmt.setString(11,objeto.getUrlModelo());
			pstmt.setString(12,objeto.getIdRepositorio());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdEmpresa());
			pstmt.setInt(14, cdModeloOld!=0 ? cdModeloOld : objeto.getCdModelo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdModelo) {
		return delete(cdModelo, null);
	}

	public static int delete(int cdModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_modelo_documento WHERE cd_modelo=?");
			pstmt.setInt(1, cdModelo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ModeloDocumento get(int cdModelo) {
		return get(cdModelo, null);
	}

	public static ModeloDocumento get(int cdModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_modelo_documento WHERE cd_modelo=?");
			pstmt.setInt(1, cdModelo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ModeloDocumento(rs.getInt("cd_modelo"),
						rs.getString("nm_modelo"),
						rs.getString("txt_modelo"),
						rs.getInt("tp_modelo"),
						rs.getBytes("blb_conteudo")==null?null:rs.getBytes("blb_conteudo"),
						rs.getString("txt_conteudo"),
						rs.getInt("st_modelo"),
						rs.getString("nm_titulo"),
						rs.getInt("cd_tipo_documento"),
						rs.getString("id_modelo"),
						rs.getString("url_modelo"),
						rs.getString("id_repositorio"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_modelo_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ModeloDocumento> getList() {
		return getList(null);
	}

	public static ArrayList<ModeloDocumento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ModeloDocumento> list = new ArrayList<ModeloDocumento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ModeloDocumento obj = ModeloDocumentoDAO.get(rsm.getInt("cd_modelo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloDocumentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_modelo_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
