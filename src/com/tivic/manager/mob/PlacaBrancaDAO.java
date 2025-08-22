package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class PlacaBrancaDAO{

	public static int insert(PlacaBranca objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlacaBranca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_placa_branca", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlaca(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_placa_branca (cd_placa,"+
			                                  "nr_placa,"+
			                                  "dt_cadastro,"+
			                                  "dt_atualizacao,"+
			                                  "st_placa,"+
			                                  "txt_observacao,"+
			                                  "cd_usuario,"+
			                                  "cd_orgao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNrPlaca());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStPlaca());
			pstmt.setString(6,objeto.getTxtObservacao());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUsuario());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOrgao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlacaBranca objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PlacaBranca objeto, int cdPlacaOld) {
		return update(objeto, cdPlacaOld, null);
	}

	public static int update(PlacaBranca objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PlacaBranca objeto, int cdPlacaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_placa_branca SET cd_placa=?,"+
												      		   "nr_placa=?,"+
												      		   "dt_cadastro=?,"+
												      		   "dt_atualizacao=?,"+
												      		   "st_placa=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_orgao=? WHERE cd_placa=?");
			pstmt.setInt(1,objeto.getCdPlaca());
			pstmt.setString(2,objeto.getNrPlaca());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStPlaca());
			pstmt.setString(6,objeto.getTxtObservacao());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUsuario());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdOrgao());
			pstmt.setInt(9, cdPlacaOld!=0 ? cdPlacaOld : objeto.getCdPlaca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlaca) {
		return delete(cdPlaca, null);
	}

	public static int delete(int cdPlaca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_placa_branca WHERE cd_placa=?");
			pstmt.setInt(1, cdPlaca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlacaBranca get(int cdPlaca) {
		return get(cdPlaca, null);
	}

	public static PlacaBranca get(int cdPlaca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_placa_branca WHERE cd_placa=?");
			pstmt.setInt(1, cdPlaca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlacaBranca(rs.getInt("cd_placa"),
						rs.getString("nr_placa"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()),
						rs.getInt("st_placa"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_orgao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_placa_branca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlacaBranca> getList() {
		return getList(null);
	}

	public static ArrayList<PlacaBranca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlacaBranca> list = new ArrayList<PlacaBranca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlacaBranca obj = PlacaBrancaDAO.get(rsm.getInt("cd_placa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_placa_branca", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
