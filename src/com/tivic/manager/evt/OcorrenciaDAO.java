package com.tivic.manager.evt;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class OcorrenciaDAO{

	public static int insert(Ocorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(Ocorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int code = Conexao.getSequenceCode("evt_ocorrencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO evt_ocorrencia (cd_ocorrencia,"+
			                                  "cd_pessoa,"+
			                                  "cd_evento,"+
			                                  "tp_ocorrencia,"+
			                                  "txt_ocorrencia,"+
			                                  "dt_ocorrencia) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdEvento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEvento());
			pstmt.setInt(4,objeto.getTpOcorrencia());
			pstmt.setString(5,objeto.getTxtOcorrencia());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Ocorrencia objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(Ocorrencia objeto, float cdOcorrenciaOld, int cdPessoaOld, int cdEventoOld) {
		return update(objeto, cdOcorrenciaOld, cdPessoaOld, cdEventoOld, null);
	}

	public static int update(Ocorrencia objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(Ocorrencia objeto, float cdOcorrenciaOld, int cdPessoaOld, int cdEventoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE evt_ocorrencia SET cd_ocorrencia=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_evento=?,"+
												      		   "tp_ocorrencia=?,"+
												      		   "txt_ocorrencia=?,"+
												      		   "dt_ocorrencia=? WHERE cd_ocorrencia=? AND cd_pessoa=? AND cd_evento=?");
			pstmt.setFloat(1,objeto.getCdOcorrencia());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getCdEvento());
			pstmt.setInt(4,objeto.getTpOcorrencia());
			pstmt.setString(5,objeto.getTxtOcorrencia());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setFloat(7, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			pstmt.setInt(8, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(9, cdEventoOld!=0 ? cdEventoOld : objeto.getCdEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(float cdOcorrencia, int cdPessoa, int cdEvento) {
		return delete(cdOcorrencia, cdPessoa, cdEvento, null);
	}

	public static int delete(float cdOcorrencia, int cdPessoa, int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM evt_ocorrencia WHERE cd_ocorrencia=? AND cd_pessoa=? AND cd_evento=?");
			pstmt.setFloat(1, cdOcorrencia);
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Ocorrencia get(float cdOcorrencia, int cdPessoa, int cdEvento) {
		return get(cdOcorrencia, cdPessoa, cdEvento, null);
	}

	public static Ocorrencia get(float cdOcorrencia, int cdPessoa, int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM evt_ocorrencia WHERE cd_ocorrencia=? AND cd_pessoa=? AND cd_evento=?");
			pstmt.setFloat(1, cdOcorrencia);
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Ocorrencia(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_evento"),
						rs.getInt("tp_ocorrencia"),
						rs.getString("txt_ocorrencia"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM evt_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Ocorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<Ocorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Ocorrencia> list = new ArrayList<Ocorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Ocorrencia obj = OcorrenciaDAO.get(rsm.getInt("cd_ocorrencia"), rsm.getInt("cd_pessoa"), rsm.getInt("cd_evento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM evt_ocorrencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
