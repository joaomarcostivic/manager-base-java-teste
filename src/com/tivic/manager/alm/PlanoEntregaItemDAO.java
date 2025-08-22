package com.tivic.manager.alm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PlanoEntregaItemDAO{

	public static int insert(PlanoEntregaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoEntregaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_plano_entrega_item (cd_plano,"+
			                                  "cd_documento_saida,"+
			                                  "cd_documento_saida_consignada,"+
			                                  "cd_documento_entrada) VALUES (?, ?, ?, ?)");
			if(objeto.getCdPlano()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPlano());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumentoSaida());
			pstmt.setInt(3,objeto.getCdDocumentoSaidaConsignada());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoEntrada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoEntregaItem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PlanoEntregaItem objeto, int cdPlanoOld, int cdDocumentoSaidaOld) {
		return update(objeto, cdPlanoOld, cdDocumentoSaidaOld, null);
	}

	public static int update(PlanoEntregaItem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PlanoEntregaItem objeto, int cdPlanoOld, int cdDocumentoSaidaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_plano_entrega_item SET cd_plano=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "cd_documento_saida_consignada=?,"+
												      		   "cd_documento_entrada=? WHERE cd_plano=? AND cd_documento_saida=?");
			pstmt.setInt(1,objeto.getCdPlano());
			pstmt.setInt(2,objeto.getCdDocumentoSaida());
			pstmt.setInt(3,objeto.getCdDocumentoSaidaConsignada());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoEntrada());
			pstmt.setInt(5, cdPlanoOld!=0 ? cdPlanoOld : objeto.getCdPlano());
			pstmt.setInt(6, cdDocumentoSaidaOld!=0 ? cdDocumentoSaidaOld : objeto.getCdDocumentoSaida());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlano, int cdDocumentoSaida) {
		return delete(cdPlano, cdDocumentoSaida, null);
	}

	public static int delete(int cdPlano, int cdDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_plano_entrega_item WHERE cd_plano=? AND cd_documento_saida=?");
			pstmt.setInt(1, cdPlano);
			pstmt.setInt(2, cdDocumentoSaida);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoEntregaItem get(int cdPlano, int cdDocumentoSaida) {
		return get(cdPlano, cdDocumentoSaida, null);
	}

	public static PlanoEntregaItem get(int cdPlano, int cdDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_plano_entrega_item WHERE cd_plano=? AND cd_documento_saida=?");
			pstmt.setInt(1, cdPlano);
			pstmt.setInt(2, cdDocumentoSaida);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoEntregaItem(rs.getInt("cd_plano"),
						rs.getInt("cd_documento_saida"),
						rs.getInt("cd_documento_saida_consignada"),
						rs.getInt("cd_documento_entrada"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_plano_entrega_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoEntregaItem> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoEntregaItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoEntregaItem> list = new ArrayList<PlanoEntregaItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoEntregaItem obj = PlanoEntregaItemDAO.get(rsm.getInt("cd_plano"), rsm.getInt("cd_documento_saida"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoEntregaItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM alm_plano_entrega_item", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}