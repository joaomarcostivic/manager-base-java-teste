package com.tivic.manager.bdv;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class ProprietarioDAO{

	public static int insert(Proprietario objeto) {
		return insert(objeto, null);
	}

	public static int insert(Proprietario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_proprietario");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_veiculo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdVeiculo()));
			int code = Conexao.getSequenceCode("bdv_proprietario", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProprietario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bdv_proprietario (cd_proprietario,"+
			                                  "cd_veiculo,"+
			                                  "nm_proprietario,"+
			                                  "nr_cpf,"+
			                                  "nr_cnh,"+
			                                  "sg_uf_cnh,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "st_proprietario,"+
			                                  "tp_proprietario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			pstmt.setString(3,objeto.getNmProprietario());
			pstmt.setString(4,objeto.getNrCpf());
			pstmt.setString(5,objeto.getNrCnh());
			pstmt.setString(6,objeto.getSgUfCnh());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(9,objeto.getStProprietario());
			pstmt.setInt(10,objeto.getTpProprietario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Proprietario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Proprietario objeto, int cdProprietarioOld, int cdVeiculoOld) {
		return update(objeto, cdProprietarioOld, cdVeiculoOld, null);
	}

	public static int update(Proprietario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Proprietario objeto, int cdProprietarioOld, int cdVeiculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bdv_proprietario SET cd_proprietario=?,"+
												      		   "cd_veiculo=?,"+
												      		   "nm_proprietario=?,"+
												      		   "nr_cpf=?,"+
												      		   "nr_cnh=?,"+
												      		   "sg_uf_cnh=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "st_proprietario=?,"+
												      		   "tp_proprietario=? WHERE cd_proprietario=? AND cd_veiculo=?");
			pstmt.setInt(1,objeto.getCdProprietario());
			pstmt.setInt(2,objeto.getCdVeiculo());
			pstmt.setString(3,objeto.getNmProprietario());
			pstmt.setString(4,objeto.getNrCpf());
			pstmt.setString(5,objeto.getNrCnh());
			pstmt.setString(6,objeto.getSgUfCnh());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(9,objeto.getStProprietario());
			pstmt.setInt(10,objeto.getTpProprietario());
			pstmt.setInt(11, cdProprietarioOld!=0 ? cdProprietarioOld : objeto.getCdProprietario());
			pstmt.setInt(12, cdVeiculoOld!=0 ? cdVeiculoOld : objeto.getCdVeiculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProprietario, int cdVeiculo) {
		return delete(cdProprietario, cdVeiculo, null);
	}

	public static int delete(int cdProprietario, int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bdv_proprietario WHERE cd_proprietario=? AND cd_veiculo=?");
			pstmt.setInt(1, cdProprietario);
			pstmt.setInt(2, cdVeiculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Proprietario get(int cdProprietario, int cdVeiculo) {
		return get(cdProprietario, cdVeiculo, null);
	}

	public static Proprietario get(int cdProprietario, int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bdv_proprietario WHERE cd_proprietario=? AND cd_veiculo=?");
			pstmt.setInt(1, cdProprietario);
			pstmt.setInt(2, cdVeiculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Proprietario(rs.getInt("cd_proprietario"),
						rs.getInt("cd_veiculo"),
						rs.getString("nm_proprietario"),
						rs.getString("nr_cpf"),
						rs.getString("nr_cnh"),
						rs.getString("sg_uf_cnh"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("st_proprietario"),
						rs.getInt("tp_proprietario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bdv_proprietario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Proprietario> getList() {
		return getList(null);
	}

	public static ArrayList<Proprietario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Proprietario> list = new ArrayList<Proprietario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Proprietario obj = ProprietarioDAO.get(rsm.getInt("cd_proprietario"), rsm.getInt("cd_veiculo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProprietarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM bdv_proprietario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
