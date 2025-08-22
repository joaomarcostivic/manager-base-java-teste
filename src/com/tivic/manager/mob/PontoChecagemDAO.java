package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class PontoChecagemDAO{

	public static int insert(PontoChecagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(PontoChecagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_ponto_checagem", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPontoChecagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ponto_checagem (cd_ponto_checagem,"+
			                                  "cd_referencia,"+
			                                  "cd_pessoa,"+
			                                  "nm_ponto_checagem,"+
			                                  "tp_ponto_checagem,"+
			                                  "st_ponto_checagem,"+
			                                  "dt_inicial,"+
			                                  "dt_final) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdReferencia());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			pstmt.setString(4,objeto.getNmPontoChecagem());
			pstmt.setInt(5,objeto.getTpPontoChecagem());
			pstmt.setInt(6,objeto.getStPontoChecagem());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PontoChecagem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PontoChecagem objeto, int cdPontoChecagemOld) {
		return update(objeto, cdPontoChecagemOld, null);
	}

	public static int update(PontoChecagem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PontoChecagem objeto, int cdPontoChecagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ponto_checagem SET cd_ponto_checagem=?,"+
												      		   "cd_referencia=?,"+
												      		   "cd_pessoa=?,"+
												      		   "nm_ponto_checagem=?,"+
												      		   "tp_ponto_checagem=?,"+
												      		   "st_ponto_checagem=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=? WHERE cd_ponto_checagem=?");
			pstmt.setInt(1,objeto.getCdPontoChecagem());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdReferencia());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			pstmt.setString(4,objeto.getNmPontoChecagem());
			pstmt.setInt(5,objeto.getTpPontoChecagem());
			pstmt.setInt(6,objeto.getStPontoChecagem());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(9, cdPontoChecagemOld!=0 ? cdPontoChecagemOld : objeto.getCdPontoChecagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPontoChecagem) {
		return delete(cdPontoChecagem, null);
	}

	public static int delete(int cdPontoChecagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ponto_checagem WHERE cd_ponto_checagem=?");
			pstmt.setInt(1, cdPontoChecagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PontoChecagem get(int cdPontoChecagem) {
		return get(cdPontoChecagem, null);
	}

	public static PontoChecagem get(int cdPontoChecagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ponto_checagem WHERE cd_ponto_checagem=?");
			pstmt.setInt(1, cdPontoChecagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PontoChecagem(rs.getInt("cd_ponto_checagem"),
						rs.getInt("cd_referencia"),
						rs.getInt("cd_pessoa"),
						rs.getString("nm_ponto_checagem"),
						rs.getInt("tp_ponto_checagem"),
						rs.getInt("st_ponto_checagem"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ponto_checagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PontoChecagem> getList() {
		return getList(null);
	}

	public static ArrayList<PontoChecagem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PontoChecagem> list = new ArrayList<PontoChecagem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PontoChecagem obj = PontoChecagemDAO.get(rsm.getInt("cd_ponto_checagem"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ponto_checagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}