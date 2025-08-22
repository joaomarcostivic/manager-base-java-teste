package com.tivic.manager.blb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class ExemplarOcorrenciaDAO{

	public static int insert(ExemplarOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(ExemplarOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ocorrencia");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_exemplar");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdExemplar()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_publicacao");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdPublicacao()));
			int code = Conexao.getSequenceCode("blb_exemplar_ocorrencia", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_exemplar_ocorrencia (cd_ocorrencia,"+
			                                  "cd_exemplar,"+
			                                  "cd_publicacao,"+
			                                  "cd_operador,"+
			                                  "cd_pessoa,"+
			                                  "tp_ocorrencia,"+
			                                  "dt_ocorrencia,"+
			                                  "dt_limite,"+
			                                  "dt_final,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdExemplar()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdExemplar());
			if(objeto.getCdPublicacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPublicacao());
			if(objeto.getCdOperador()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdOperador());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			pstmt.setInt(6,objeto.getTpOcorrencia());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getDtLimite()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtLimite().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setString(10,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ExemplarOcorrencia objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ExemplarOcorrencia objeto, int cdOcorrenciaOld, int cdExemplarOld, int cdPublicacaoOld) {
		return update(objeto, cdOcorrenciaOld, cdExemplarOld, cdPublicacaoOld, null);
	}

	public static int update(ExemplarOcorrencia objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ExemplarOcorrencia objeto, int cdOcorrenciaOld, int cdExemplarOld, int cdPublicacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_exemplar_ocorrencia SET cd_ocorrencia=?,"+
												      		   "cd_exemplar=?,"+
												      		   "cd_publicacao=?,"+
												      		   "cd_operador=?,"+
												      		   "cd_pessoa=?,"+
												      		   "tp_ocorrencia=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "dt_limite=?,"+
												      		   "dt_final=?,"+
												      		   "txt_observacao=? WHERE cd_ocorrencia=? AND cd_exemplar=? AND cd_publicacao=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			pstmt.setInt(2,objeto.getCdExemplar());
			pstmt.setInt(3,objeto.getCdPublicacao());
			if(objeto.getCdOperador()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdOperador());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			pstmt.setInt(6,objeto.getTpOcorrencia());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getDtLimite()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtLimite().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setString(10,objeto.getTxtObservacao());
			pstmt.setInt(11, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			pstmt.setInt(12, cdExemplarOld!=0 ? cdExemplarOld : objeto.getCdExemplar());
			pstmt.setInt(13, cdPublicacaoOld!=0 ? cdPublicacaoOld : objeto.getCdPublicacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOcorrencia, int cdExemplar, int cdPublicacao) {
		return delete(cdOcorrencia, cdExemplar, cdPublicacao, null);
	}

	public static int delete(int cdOcorrencia, int cdExemplar, int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_exemplar_ocorrencia WHERE cd_ocorrencia=? AND cd_exemplar=? AND cd_publicacao=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.setInt(2, cdExemplar);
			pstmt.setInt(3, cdPublicacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ExemplarOcorrencia get(int cdOcorrencia, int cdExemplar, int cdPublicacao) {
		return get(cdOcorrencia, cdExemplar, cdPublicacao, null);
	}

	public static ExemplarOcorrencia get(int cdOcorrencia, int cdExemplar, int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_exemplar_ocorrencia WHERE cd_ocorrencia=? AND cd_exemplar=? AND cd_publicacao=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.setInt(2, cdExemplar);
			pstmt.setInt(3, cdPublicacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ExemplarOcorrencia(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_exemplar"),
						rs.getInt("cd_publicacao"),
						rs.getInt("cd_operador"),
						rs.getInt("cd_pessoa"),
						rs.getInt("tp_ocorrencia"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						(rs.getTimestamp("dt_limite")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_limite").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_exemplar_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ExemplarOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<ExemplarOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ExemplarOcorrencia> list = new ArrayList<ExemplarOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ExemplarOcorrencia obj = ExemplarOcorrenciaDAO.get(rsm.getInt("cd_ocorrencia"), rsm.getInt("cd_exemplar"), rsm.getInt("cd_publicacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ExemplarOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_exemplar_ocorrencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
