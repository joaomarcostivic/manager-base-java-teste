package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoDocumentoFaseDAO{

	public static int insert(TipoDocumentoFase objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoDocumentoFase objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_tipo_documento_fase (cd_tipo_documento,"+
			                                  "cd_fase,"+
			                                  "lg_obrigatorio,"+
			                                  "nr_ordem,"+
			                                  "tp_associacao) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoDocumento());
			if(objeto.getCdFase()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFase());
			pstmt.setInt(3,objeto.getLgObrigatorio());
			pstmt.setInt(4,objeto.getNrOrdem());
			pstmt.setInt(5,objeto.getTpAssociacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoDocumentoFase objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TipoDocumentoFase objeto, int cdTipoDocumentoOld, int cdFaseOld) {
		return update(objeto, cdTipoDocumentoOld, cdFaseOld, null);
	}

	public static int update(TipoDocumentoFase objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TipoDocumentoFase objeto, int cdTipoDocumentoOld, int cdFaseOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_tipo_documento_fase SET cd_tipo_documento=?,"+
												      		   "cd_fase=?,"+
												      		   "lg_obrigatorio=?,"+
												      		   "nr_ordem=?,"+
												      		   "tp_associacao=? WHERE cd_tipo_documento=? AND cd_fase=?");
			pstmt.setInt(1,objeto.getCdTipoDocumento());
			pstmt.setInt(2,objeto.getCdFase());
			pstmt.setInt(3,objeto.getLgObrigatorio());
			pstmt.setInt(4,objeto.getNrOrdem());
			pstmt.setInt(5,objeto.getTpAssociacao());
			pstmt.setInt(6, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			pstmt.setInt(7, cdFaseOld!=0 ? cdFaseOld : objeto.getCdFase());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDocumento, int cdFase) {
		return delete(cdTipoDocumento, cdFase, null);
	}

	public static int delete(int cdTipoDocumento, int cdFase, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_tipo_documento_fase WHERE cd_tipo_documento=? AND cd_fase=?");
			pstmt.setInt(1, cdTipoDocumento);
			pstmt.setInt(2, cdFase);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoDocumentoFase get(int cdTipoDocumento, int cdFase) {
		return get(cdTipoDocumento, cdFase, null);
	}

	public static TipoDocumentoFase get(int cdTipoDocumento, int cdFase, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_tipo_documento_fase WHERE cd_tipo_documento=? AND cd_fase=?");
			pstmt.setInt(1, cdTipoDocumento);
			pstmt.setInt(2, cdFase);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDocumentoFase(rs.getInt("cd_tipo_documento"),
						rs.getInt("cd_fase"),
						rs.getInt("lg_obrigatorio"),
						rs.getInt("nr_ordem"),
						rs.getInt("tp_associacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_tipo_documento_fase");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoDocumentoFase> getList() {
		return getList(null);
	}

	public static ArrayList<TipoDocumentoFase> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoDocumentoFase> list = new ArrayList<TipoDocumentoFase>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoDocumentoFase obj = TipoDocumentoFaseDAO.get(rsm.getInt("cd_tipo_documento"), rsm.getInt("cd_fase"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoFaseDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_tipo_documento_fase", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}