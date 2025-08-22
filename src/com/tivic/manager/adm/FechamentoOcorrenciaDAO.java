package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class FechamentoOcorrenciaDAO{

	public static int insert(FechamentoOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(FechamentoOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_fechamento_ocorrencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFechamentoOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_fechamento_ocorrencia (cd_fechamento_ocorrencia,"+
			                                  "cd_usuario,"+
			                                  "cd_fechamento,"+
			                                  "cd_conta,"+
			                                  "ds_ocorrencia,"+
			                                  "dt_ocorrencia,"+
			                                  "cd_movimento_conta) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			if(objeto.getCdFechamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFechamento());
			if(objeto.getCdConta()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdConta());
			pstmt.setString(5,objeto.getDsOcorrencia());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdMovimentoConta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FechamentoOcorrencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FechamentoOcorrencia objeto, int cdFechamentoOcorrenciaOld) {
		return update(objeto, cdFechamentoOcorrenciaOld, null);
	}

	public static int update(FechamentoOcorrencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FechamentoOcorrencia objeto, int cdFechamentoOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_fechamento_ocorrencia SET cd_fechamento_ocorrencia=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_fechamento=?,"+
												      		   "cd_conta=?,"+
												      		   "ds_ocorrencia=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "cd_movimento_conta=? WHERE cd_fechamento_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdFechamentoOcorrencia());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			if(objeto.getCdFechamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFechamento());
			if(objeto.getCdConta()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdConta());
			pstmt.setString(5,objeto.getDsOcorrencia());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdMovimentoConta());
			pstmt.setInt(8, cdFechamentoOcorrenciaOld!=0 ? cdFechamentoOcorrenciaOld : objeto.getCdFechamentoOcorrencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFechamentoOcorrencia) {
		return delete(cdFechamentoOcorrencia, null);
	}

	public static int delete(int cdFechamentoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_fechamento_ocorrencia WHERE cd_fechamento_ocorrencia=?");
			pstmt.setInt(1, cdFechamentoOcorrencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FechamentoOcorrencia get(int cdFechamentoOcorrencia) {
		return get(cdFechamentoOcorrencia, null);
	}

	public static FechamentoOcorrencia get(int cdFechamentoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_fechamento_ocorrencia WHERE cd_fechamento_ocorrencia=?");
			pstmt.setInt(1, cdFechamentoOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FechamentoOcorrencia(rs.getInt("cd_fechamento_ocorrencia"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_fechamento"),
						rs.getInt("cd_conta"),
						rs.getString("ds_ocorrencia"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getInt("cd_movimento_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_fechamento_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FechamentoOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<FechamentoOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FechamentoOcorrencia> list = new ArrayList<FechamentoOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FechamentoOcorrencia obj = FechamentoOcorrenciaDAO.get(rsm.getInt("cd_fechamento_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_fechamento_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}