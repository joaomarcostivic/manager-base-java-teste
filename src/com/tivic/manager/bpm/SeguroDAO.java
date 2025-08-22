package com.tivic.manager.bpm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class SeguroDAO{

	public static int insert(Seguro objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int insert(Seguro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap[] keys = new HashMap[1];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_contrato");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdContrato()));
			int code = Conexao.getSequenceCode("bpm_seguro", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO bpm_seguro (cd_contrato,"+
			                                  "cd_corretora,"+
			                                  "nr_apolice,"+
			                                  "nr_proposta,"+
			                                  "vl_franquia,"+
			                                  "nr_classe_bonus,"+
			                                  "vl_cobertura_casco,"+
			                                  "vl_cobertura_dm,"+
			                                  "vl_cobertura_dp,"+
			                                  "txt_observacao,"+
			                                  "cd_referencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdCorretora()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCorretora());
			pstmt.setString(3,objeto.getNrApolice());
			pstmt.setString(4,objeto.getNrProposta());
			pstmt.setFloat(5,objeto.getVlFranquia());
			pstmt.setString(6,objeto.getNrClasseBonus());
			pstmt.setFloat(7,objeto.getVlCoberturaCasco());
			pstmt.setFloat(8,objeto.getVlCoberturaDm());
			pstmt.setFloat(9,objeto.getVlCoberturaDp());
			pstmt.setString(10,objeto.getTxtObservacao());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdReferencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SeguroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SeguroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Seguro objeto) {
		return update(objeto, null);
	}

	public static int update(Seguro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE bpm_seguro SET cd_corretora=?,"+
			                                  "nr_apolice=?,"+
			                                  "nr_proposta=?,"+
			                                  "vl_franquia=?,"+
			                                  "nr_classe_bonus=?,"+
			                                  "vl_cobertura_casco=?,"+
			                                  "vl_cobertura_dm=?,"+
			                                  "vl_cobertura_dp=?,"+
			                                  "txt_observacao=?,"+
			                                  "cd_referencia=? WHERE cd_contrato=?");
			if(objeto.getCdCorretora()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCorretora());
			pstmt.setString(2,objeto.getNrApolice());
			pstmt.setString(3,objeto.getNrProposta());
			pstmt.setFloat(4,objeto.getVlFranquia());
			pstmt.setString(5,objeto.getNrClasseBonus());
			pstmt.setFloat(6,objeto.getVlCoberturaCasco());
			pstmt.setFloat(7,objeto.getVlCoberturaDm());
			pstmt.setFloat(8,objeto.getVlCoberturaDp());
			pstmt.setString(9,objeto.getTxtObservacao());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdReferencia());
			pstmt.setInt(11,objeto.getCdContrato());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SeguroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SeguroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato) {
		return delete(cdContrato, null);
	}

	public static int delete(int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM bpm_seguro WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SeguroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SeguroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Seguro get(int cdContrato) {
		return get(cdContrato, null);
	}

	public static Seguro get(int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_seguro WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Seguro(rs.getInt("cd_contrato"),
						rs.getInt("cd_corretora"),
						rs.getString("nr_apolice"),
						rs.getString("nr_proposta"),
						rs.getFloat("vl_franquia"),
						rs.getString("nr_classe_bonus"),
						rs.getFloat("vl_cobertura_casco"),
						rs.getFloat("vl_cobertura_dm"),
						rs.getFloat("vl_cobertura_dp"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_referencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SeguroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SeguroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bpm_seguro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SeguroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SeguroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM bpm_seguro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
