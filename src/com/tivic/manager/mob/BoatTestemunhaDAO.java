package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class BoatTestemunhaDAO{

	public static int insert(BoatTestemunha objeto) {
		return insert(objeto, null);
	}

	public static int insert(BoatTestemunha objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_boat_testemunha (cd_boat,"+
			                                  "cd_pessoa," +
			                                  "nr_rg," + 
			                                  "sg_orgao_rg," + 
			                                  "txt_endereco) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdBoat()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdBoat());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			
			pstmt.setString(3,objeto.getNrRg());
			pstmt.setString(4,objeto.getSgOrgaoRg());
			pstmt.setString(5,objeto.getTxtEndereco());
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BoatTestemunha objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(BoatTestemunha objeto, int cdBoatOld, int cdPessoaOld) {
		return update(objeto, cdBoatOld, cdPessoaOld, null);
	}

	public static int update(BoatTestemunha objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(BoatTestemunha objeto, int cdBoatOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_boat_testemunha SET cd_boat=?,"+
												      		   "cd_pessoa=?, nr_rg=?, sg_orgao_rg=?, txt_endereco=? WHERE cd_boat=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdBoat());
			pstmt.setInt(2,objeto.getCdPessoa());
			
			pstmt.setString(3,objeto.getNrRg());
			pstmt.setString(4,objeto.getSgOrgaoRg());
			pstmt.setString(5,objeto.getTxtEndereco());
			
			pstmt.setInt(6, cdBoatOld!=0 ? cdBoatOld : objeto.getCdBoat());
			pstmt.setInt(7, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBoat, int cdPessoa) {
		return delete(cdBoat, cdPessoa, null);
	}

	public static int delete(int cdBoat, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_boat_testemunha WHERE cd_boat=? AND cd_pessoa=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BoatTestemunha get(int cdBoat, int cdPessoa) {
		return get(cdBoat, cdPessoa, null);
	}

	public static BoatTestemunha get(int cdBoat, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_testemunha WHERE cd_boat=? AND cd_pessoa=?");
			pstmt.setInt(1, cdBoat);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BoatTestemunha(rs.getInt("cd_boat"),
						rs.getInt("cd_pessoa"),
						rs.getString("nr_rg"),
						rs.getString("sg_orgao_rg"),
						rs.getString("txt_endereco"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_testemunha");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<BoatTestemunha> getList() {
		return getList(null);
	}

	public static ArrayList<BoatTestemunha> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<BoatTestemunha> list = new ArrayList<BoatTestemunha>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				BoatTestemunha obj = BoatTestemunhaDAO.get(rsm.getInt("cd_boat"), rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatTestemunhaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_boat_testemunha", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}