package com.tivic.manager.adapter.base.antiga.ocorrencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class OcorrenciaOldDAO {
	
	public static int insert(OcorrenciaOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(OcorrenciaOld objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ocorrencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCodOcorrencia()<=0)
				objeto.setCodOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ocorrencia (cod_ocorrencia,"+
			                                  "cod_ocorrencia,"+
			                                  "ds_ocorrencia) VALUES (?, ?)");
			pstmt.setInt(1, objeto.getCodOcorrencia());
			pstmt.setString(2,objeto.getDsOcorrencia());
			pstmt.executeUpdate();
			return objeto.getCodOcorrencia();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOldDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OcorrenciaOld objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OcorrenciaOld objeto, int codOcorrenciaOld) {
		return update(objeto, codOcorrenciaOld, null);
	}

	public static int update(OcorrenciaOld objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OcorrenciaOld objeto, int codOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ocorrencia SET cod_ocorrencia=?,"+
												      		   "ds_ocorrencia=? WHERE cod_ocorrencia=?");
			pstmt.setInt(1, objeto.getCodOcorrencia());
			pstmt.setString(2,objeto.getDsOcorrencia());
			pstmt.setInt(3, codOcorrenciaOld!=0 ? codOcorrenciaOld : objeto.getCodOcorrencia());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOldDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int codOcorrencia) {
		return delete(codOcorrencia, null);
	}

	public static int delete(int codOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ocorrencia WHERE cod_ocorrencia=?");
			pstmt.setInt(1, codOcorrencia);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOldDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OcorrenciaOld get(int codOcorrencia) {
		return get(codOcorrencia, null);
	}

	public static OcorrenciaOld get(int codOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ocorrencia WHERE cod_ocorrencia=?");
			pstmt.setInt(1, codOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OcorrenciaOld(rs.getInt("cod_ocorrencia"),
						rs.getString("ds_ocorrencia"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOldDAO.get: " + e);
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
		return Search.find("SELECT * FROM ocorrencia ", " ORDER BY cod_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
