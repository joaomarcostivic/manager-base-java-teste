package com.tivic.manager.geo;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ReferenciaDAO{

	public static int insert(Referencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(Referencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("geo_referencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdReferencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO geo_referencia (cd_referencia,"+
			                                  "nm_referencia,"+
			                                  "tp_referencia,"+
			                                  "tp_representacao,"+
			                                  "txt_observacao,"+
			                                  "img_referencia,"+
			                                  "vl_cor) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmReferencia());
			pstmt.setInt(3,objeto.getTpReferencia());
			pstmt.setInt(4,objeto.getTpRepresentacao());
			pstmt.setString(5,objeto.getTxtObservacao());
			if(objeto.getImgReferencia()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getImgReferencia());
			pstmt.setInt(7,objeto.getVlCor());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Referencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Referencia objeto, int cdReferenciaOld) {
		return update(objeto, cdReferenciaOld, null);
	}

	public static int update(Referencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Referencia objeto, int cdReferenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE geo_referencia SET cd_referencia=?,"+
												      		   "nm_referencia=?,"+
												      		   "tp_referencia=?,"+
												      		   "tp_representacao=?,"+
												      		   "txt_observacao=?,"+
												      		   "img_referencia=?,"+
												      		   "vl_cor=? WHERE cd_referencia=?");
			pstmt.setInt(1,objeto.getCdReferencia());
			pstmt.setString(2,objeto.getNmReferencia());
			pstmt.setInt(3,objeto.getTpReferencia());
			pstmt.setInt(4,objeto.getTpRepresentacao());
			pstmt.setString(5,objeto.getTxtObservacao());
			if(objeto.getImgReferencia()==null)
				pstmt.setNull(6, Types.BINARY);
			else
				pstmt.setBytes(6,objeto.getImgReferencia());
			pstmt.setInt(7,objeto.getVlCor());
			pstmt.setInt(8, cdReferenciaOld!=0 ? cdReferenciaOld : objeto.getCdReferencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdReferencia) {
		return delete(cdReferencia, null);
	}

	public static int delete(int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM geo_referencia WHERE cd_referencia=?");
			pstmt.setInt(1, cdReferencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Referencia get(int cdReferencia) {
		return get(cdReferencia, null);
	}

	public static Referencia get(int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM geo_referencia WHERE cd_referencia=?");
			pstmt.setInt(1, cdReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Referencia(rs.getInt("cd_referencia"),
						rs.getString("nm_referencia"),
						rs.getInt("tp_referencia"),
						rs.getInt("tp_representacao"),
						rs.getString("txt_observacao"),
						rs.getBytes("img_referencia")==null?null:rs.getBytes("img_referencia"),
						rs.getInt("vl_cor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM geo_referencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Referencia> getList() {
		return getList(null);
	}

	public static ArrayList<Referencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Referencia> list = new ArrayList<Referencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Referencia obj = ReferenciaDAO.get(rsm.getInt("cd_referencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM geo_referencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
