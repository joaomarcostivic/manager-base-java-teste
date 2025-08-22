package com.tivic.manager.gpn;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.LogUtils;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoDocumentoDAO{

	public static int insert(TipoDocumento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoDocumento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("gpn_tipo_documento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoDocumento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO gpn_tipo_documento (cd_tipo_documento,"+
			                                  "nm_tipo_documento,"+
			                                  "id_tipo_documento,"+
			                                  "st_tipo_documento,"+
			                                  "cd_empresa,"+
			                                  "cd_setor,"+
			                                  "cd_formulario,"+
			                                  "tp_numeracao,"+
			                                  "id_prefixo_numeracao,"+
			                                  "ds_mascara_numeracao,"+
			                                  "nr_ultima_numeracao,"+
			                                  "cd_tipo_documento_superior,"+
			                                  "lg_numeracao_superior,"+
			                                  "nr_externo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoDocumento());
			pstmt.setString(3,objeto.getIdTipoDocumento());
			pstmt.setInt(4,objeto.getStTipoDocumento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdSetor());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdFormulario());
			pstmt.setInt(8,objeto.getTpNumeracao());
			pstmt.setString(9,objeto.getIdPrefixoNumeracao());
			pstmt.setString(10,objeto.getDsMascaraNumeracao());
			pstmt.setInt(11,objeto.getNrUltimaNumeracao());
			if(objeto.getCdTipoDocumentoSuperior()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdTipoDocumentoSuperior());
			pstmt.setInt(13,objeto.getLgNumeracaoSuperior());
			pstmt.setString(14,objeto.getNrExterno());
			pstmt.executeUpdate();
			
			LogUtils.debug("gpn.TipoDocumentoDAO.insert");
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoDocumento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoDocumento objeto, int cdTipoDocumentoOld) {
		return update(objeto, cdTipoDocumentoOld, null);
	}

	public static int update(TipoDocumento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoDocumento objeto, int cdTipoDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE gpn_tipo_documento SET cd_tipo_documento=?,"+
												      		   "nm_tipo_documento=?,"+
												      		   "id_tipo_documento=?,"+
												      		   "st_tipo_documento=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_formulario=?,"+
												      		   "tp_numeracao=?,"+
												      		   "id_prefixo_numeracao=?,"+
												      		   "ds_mascara_numeracao=?,"+
												      		   "nr_ultima_numeracao=?,"+
												      		   "cd_tipo_documento_superior=?,"+
												      		   "lg_numeracao_superior=?,"+
												      		   "nr_externo=? WHERE cd_tipo_documento=?");
			pstmt.setInt(1,objeto.getCdTipoDocumento());
			pstmt.setString(2,objeto.getNmTipoDocumento());
			pstmt.setString(3,objeto.getIdTipoDocumento());
			pstmt.setInt(4,objeto.getStTipoDocumento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdSetor());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdFormulario());
			pstmt.setInt(8,objeto.getTpNumeracao());
			pstmt.setString(9,objeto.getIdPrefixoNumeracao());
			pstmt.setString(10,objeto.getDsMascaraNumeracao());
			pstmt.setInt(11,objeto.getNrUltimaNumeracao());
			if(objeto.getCdTipoDocumentoSuperior()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdTipoDocumentoSuperior());
			pstmt.setInt(13,objeto.getLgNumeracaoSuperior());
			pstmt.setString(14,objeto.getNrExterno());
			pstmt.setInt(15, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDocumento) {
		return delete(cdTipoDocumento, null);
	}

	public static int delete(int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM gpn_tipo_documento WHERE cd_tipo_documento=?");
			pstmt.setInt(1, cdTipoDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoDocumento get(int cdTipoDocumento) {
		return get(cdTipoDocumento, null);
	}

	public static TipoDocumento get(int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM gpn_tipo_documento WHERE cd_tipo_documento=?");
			pstmt.setInt(1, cdTipoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDocumento(rs.getInt("cd_tipo_documento"),
						rs.getString("nm_tipo_documento"),
						rs.getString("id_tipo_documento"),
						rs.getInt("st_tipo_documento"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_formulario"),
						rs.getInt("tp_numeracao"),
						rs.getString("id_prefixo_numeracao"),
						rs.getString("ds_mascara_numeracao"),
						rs.getInt("nr_ultima_numeracao"),
						rs.getInt("cd_tipo_documento_superior"),
						rs.getInt("lg_numeracao_superior"),
						rs.getString("nr_externo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM gpn_tipo_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoDocumento> getList() {
		return getList(null);
	}

	public static ArrayList<TipoDocumento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoDocumento> list = new ArrayList<TipoDocumento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoDocumento obj = TipoDocumentoDAO.get(rsm.getInt("cd_tipo_documento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM gpn_tipo_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}