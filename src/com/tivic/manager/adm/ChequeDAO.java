package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ChequeDAO{

	public static int insert(Cheque objeto) {
		return insert(objeto, null);
	}

	public static int insert(Cheque objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_cheque", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCheque(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_cheque (cd_cheque,"+
			                                  "cd_conta,"+
			                                  "nr_cheque,"+
			                                  "dt_emissao,"+
			                                  "dt_liberacao,"+
			                                  "dt_impressao,"+
			                                  "id_talao,"+
			                                  "st_cheque,"+
			                                  "ds_observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getCdConta());
			pstmt.setString(3,objeto.getNrCheque());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtLiberacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtLiberacao().getTimeInMillis()));
			if(objeto.getDtImpressao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtImpressao().getTimeInMillis()));
			pstmt.setString(7,objeto.getIdTalao());
			pstmt.setInt(8,objeto.getStCheque());
			pstmt.setString(9,objeto.getDsObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Cheque objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Cheque objeto, int cdChequeOld) {
		return update(objeto, cdChequeOld, null);
	}

	public static int update(Cheque objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Cheque objeto, int cdChequeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_cheque SET cd_cheque=?,"+
												      		   "cd_conta=?,"+
												      		   "nr_cheque=?,"+
												      		   "dt_emissao=?,"+
												      		   "dt_liberacao=?,"+
												      		   "dt_impressao=?,"+
												      		   "id_talao=?,"+
												      		   "st_cheque=?,"+
												      		   "ds_observacao=? WHERE cd_cheque=?");
			pstmt.setInt(1,objeto.getCdCheque());
			pstmt.setInt(2,objeto.getCdConta());
			pstmt.setString(3,objeto.getNrCheque());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtLiberacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtLiberacao().getTimeInMillis()));
			if(objeto.getDtImpressao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtImpressao().getTimeInMillis()));
			pstmt.setString(7,objeto.getIdTalao());
			pstmt.setInt(8,objeto.getStCheque());
			pstmt.setString(9,objeto.getDsObservacao());
			pstmt.setInt(10, cdChequeOld!=0 ? cdChequeOld : objeto.getCdCheque());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCheque) {
		return delete(cdCheque, null);
	}

	public static int delete(int cdCheque, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_cheque WHERE cd_cheque=?");
			pstmt.setInt(1, cdCheque);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Cheque get(int cdCheque) {
		return get(cdCheque, null);
	}

	public static Cheque get(int cdCheque, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_cheque WHERE cd_cheque=?");
			pstmt.setInt(1, cdCheque);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Cheque(rs.getInt("cd_cheque"),
						rs.getInt("cd_conta"),
						rs.getString("nr_cheque"),
						(rs.getTimestamp("dt_emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao").getTime()),
						(rs.getTimestamp("dt_liberacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_liberacao").getTime()),
						(rs.getTimestamp("dt_impressao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_impressao").getTime()),
						rs.getString("id_talao"),
						rs.getInt("st_cheque"),
						rs.getString("ds_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_cheque");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Cheque> getList() {
		return getList(null);
	}

	public static ArrayList<Cheque> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Cheque> list = new ArrayList<Cheque>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Cheque obj = ChequeDAO.get(rsm.getInt("cd_cheque"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_cheque", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
