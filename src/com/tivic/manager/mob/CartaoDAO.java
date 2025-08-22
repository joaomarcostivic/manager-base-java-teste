package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class CartaoDAO{

	public static int insert(Cartao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Cartao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_cartao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCartao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_cartao (cd_cartao,"+
			                                  "cd_pessoa,"+
			                                  "st_cartao,"+
			                                  "tp_cartao,"+
			                                  "nr_via,"+
			                                  "id_cartao,"+
			                                  "dt_emissao,"+
			                                  "dt_validade,"+
			                                  "lg_acompanhante,"+
			                                  "tp_vigencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getStCartao());
			pstmt.setInt(4,objeto.getTpCartao());
			pstmt.setInt(5,objeto.getNrVia());
			pstmt.setString(6,objeto.getIdCartao());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtValidade()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtValidade().getTimeInMillis()));

			pstmt.setInt(9,objeto.getLgAcompanhante());
			pstmt.setInt(10,objeto.getTpVigencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Cartao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Cartao objeto, int cdCartaoOld) {
		return update(objeto, cdCartaoOld, null);
	}

	public static int update(Cartao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Cartao objeto, int cdCartaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_cartao SET cd_cartao=?,"+
												      		   "cd_pessoa=?,"+
												      		   "st_cartao=?,"+
												      		   "tp_cartao=?,"+
												      		   "nr_via=?,"+
												      		   "id_cartao=?,"+
												      		   "dt_emissao=?,"+
												      		   "dt_validade=?," +
												      		   "lg_acompanhante=?,"+
												      		   "tp_vigencia=? WHERE cd_cartao=?");
			pstmt.setInt(1,objeto.getCdCartao());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getStCartao());
			pstmt.setInt(4,objeto.getTpCartao());
			pstmt.setInt(5,objeto.getNrVia());
			pstmt.setString(6,objeto.getIdCartao());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtValidade()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			pstmt.setInt(9,objeto.getLgAcompanhante());
			pstmt.setInt(10,objeto.getTpVigencia());
			pstmt.setInt(11, cdCartaoOld!=0 ? cdCartaoOld : objeto.getCdCartao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCartao) {
		return delete(cdCartao, null);
	}

	public static int delete(int cdCartao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_cartao WHERE cd_cartao=?");
			pstmt.setInt(1, cdCartao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Cartao get(int cdCartao) {
		return get(cdCartao, null);
	}

	public static Cartao get(int cdCartao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao WHERE cd_cartao=?");
			pstmt.setInt(1, cdCartao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Cartao(rs.getInt("cd_cartao"),
						rs.getInt("cd_pessoa"),
						rs.getInt("st_cartao"),
						rs.getInt("tp_cartao"),
						rs.getInt("nr_via"),
						rs.getString("id_cartao"),
						(rs.getTimestamp("dt_emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao").getTime()),
						(rs.getTimestamp("dt_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade").getTime()),
						rs.getInt("lg_acompanhante"),
						rs.getInt("tp_vigencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Cartao> getList() {
		return getList(null);
	}

	public static ArrayList<Cartao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Cartao> list = new ArrayList<Cartao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Cartao obj = CartaoDAO.get(rsm.getInt("cd_cartao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_cartao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
