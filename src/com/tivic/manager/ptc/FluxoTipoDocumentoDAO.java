package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FluxoTipoDocumentoDAO{

	public static int insert(FluxoTipoDocumento objeto) {
		return insert(objeto, null);
	}

	public static int insert(FluxoTipoDocumento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_fluxo_tipo_documento (cd_fluxo,"+
			                                  "cd_tipo_documento) VALUES (?, ?)");
			if(objeto.getCdFluxo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdFluxo());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FluxoTipoDocumento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(FluxoTipoDocumento objeto, int cdFluxoOld, int cdTipoDocumentoOld) {
		return update(objeto, cdFluxoOld, cdTipoDocumentoOld, null);
	}

	public static int update(FluxoTipoDocumento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(FluxoTipoDocumento objeto, int cdFluxoOld, int cdTipoDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_fluxo_tipo_documento SET cd_fluxo=?,"+
												      		   "cd_tipo_documento=? WHERE cd_fluxo=? AND cd_tipo_documento=?");
			pstmt.setInt(1,objeto.getCdFluxo());
			pstmt.setInt(2,objeto.getCdTipoDocumento());
			pstmt.setInt(3, cdFluxoOld!=0 ? cdFluxoOld : objeto.getCdFluxo());
			pstmt.setInt(4, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFluxo, int cdTipoDocumento) {
		return delete(cdFluxo, cdTipoDocumento, null);
	}

	public static int delete(int cdFluxo, int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_fluxo_tipo_documento WHERE cd_fluxo=? AND cd_tipo_documento=?");
			pstmt.setInt(1, cdFluxo);
			pstmt.setInt(2, cdTipoDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FluxoTipoDocumento get(int cdFluxo, int cdTipoDocumento) {
		return get(cdFluxo, cdTipoDocumento, null);
	}

	public static FluxoTipoDocumento get(int cdFluxo, int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_fluxo_tipo_documento WHERE cd_fluxo=? AND cd_tipo_documento=?");
			pstmt.setInt(1, cdFluxo);
			pstmt.setInt(2, cdTipoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FluxoTipoDocumento(rs.getInt("cd_fluxo"),
						rs.getInt("cd_tipo_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_fluxo_tipo_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FluxoTipoDocumento> getList() {
		return getList(null);
	}

	public static ArrayList<FluxoTipoDocumento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FluxoTipoDocumento> list = new ArrayList<FluxoTipoDocumento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FluxoTipoDocumento obj = FluxoTipoDocumentoDAO.get(rsm.getInt("cd_fluxo"), rsm.getInt("cd_tipo_documento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FluxoTipoDocumentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_fluxo_tipo_documento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
