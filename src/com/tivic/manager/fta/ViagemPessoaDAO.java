package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ViagemPessoaDAO{

	public static int insert(ViagemPessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(ViagemPessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_viagem_pessoa (cd_pessoa,"+
			                                  "cd_viagem,"+
			                                  "lg_motorista," +
			                                  "tp_viagem_pessoa) VALUES (?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdViagem());
			pstmt.setInt(3,objeto.getLgMotorista());
			pstmt.setInt(4,objeto.getTpViagemPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ViagemPessoa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ViagemPessoa objeto, int cdPessoaOld, int cdViagemOld) {
		return update(objeto, cdPessoaOld, cdViagemOld, null);
	}

	public static int update(ViagemPessoa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ViagemPessoa objeto, int cdPessoaOld, int cdViagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_viagem_pessoa SET cd_pessoa=?,"+
												      		   "cd_viagem=?,"+
												      		   "lg_motorista=?," +
												      		   "tp_viagem_pessoa=? WHERE cd_pessoa=? AND cd_viagem=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdViagem());
			pstmt.setInt(3,objeto.getLgMotorista());
			pstmt.setInt(4,objeto.getTpViagemPessoa());
			pstmt.setInt(5, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(6, cdViagemOld!=0 ? cdViagemOld : objeto.getCdViagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdViagem) {
		return delete(cdPessoa, cdViagem, null);
	}

	public static int delete(int cdPessoa, int cdViagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_viagem_pessoa WHERE cd_pessoa=? AND cd_viagem=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdViagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ViagemPessoa get(int cdPessoa, int cdViagem) {
		return get(cdPessoa, cdViagem, null);
	}

	public static ViagemPessoa get(int cdPessoa, int cdViagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_viagem_pessoa WHERE cd_pessoa=? AND cd_viagem=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdViagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ViagemPessoa(rs.getInt("cd_pessoa"),
						rs.getInt("cd_viagem"),
						rs.getInt("lg_motorista"),
						rs.getInt("tp_viagem_pessoa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_viagem_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_viagem_pessoa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
