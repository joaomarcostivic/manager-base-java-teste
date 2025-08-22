package com.tivic.manager.ctb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PessoaCentroCustoDAO{

	public static int insert(PessoaCentroCusto objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaCentroCusto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("CTB_PESSOA_CENTRO_CUSTO", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPessoaCentroCusto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO CTB_PESSOA_CENTRO_CUSTO (CD_PESSOA_CENTRO_CUSTO,"+
			                                  "CD_PESSOA,"+
			                                  "CD_CENTRO_CUSTO,"+
			                                  "CD_AREA_DIREITO) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCentroCusto());
			pstmt.setInt(4,objeto.getCdAreaDireito());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaCentroCusto objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PessoaCentroCusto objeto, int cdPessoaCentroCustoOld) {
		return update(objeto, cdPessoaCentroCustoOld, null);
	}

	public static int update(PessoaCentroCusto objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PessoaCentroCusto objeto, int cdPessoaCentroCustoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE CTB_PESSOA_CENTRO_CUSTO SET CD_PESSOA_CENTRO_CUSTO=?,"+
												      		   "CD_PESSOA=?,"+
												      		   "CD_CENTRO_CUSTO=?,"+
												      		   "CD_AREA_DIREITO=? WHERE CD_PESSOA_CENTRO_CUSTO=?");
			pstmt.setInt(1,objeto.getCdPessoaCentroCusto());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCentroCusto());
			pstmt.setInt(4,objeto.getCdAreaDireito());
			pstmt.setInt(5, cdPessoaCentroCustoOld!=0 ? cdPessoaCentroCustoOld : objeto.getCdPessoaCentroCusto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoaCentroCusto) {
		return delete(cdPessoaCentroCusto, null);
	}

	public static int delete(int cdPessoaCentroCusto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM CTB_PESSOA_CENTRO_CUSTO WHERE CD_PESSOA_CENTRO_CUSTO=?");
			pstmt.setInt(1, cdPessoaCentroCusto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaCentroCusto get(int cdPessoaCentroCusto) {
		return get(cdPessoaCentroCusto, null);
	}

	public static PessoaCentroCusto get(int cdPessoaCentroCusto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM CTB_PESSOA_CENTRO_CUSTO WHERE CD_PESSOA_CENTRO_CUSTO=?");
			pstmt.setInt(1, cdPessoaCentroCusto);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaCentroCusto(rs.getInt("CD_PESSOA_CENTRO_CUSTO"),
						rs.getInt("CD_PESSOA"),
						rs.getInt("CD_CENTRO_CUSTO"),
						rs.getInt("CD_AREA_DIREITO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM CTB_PESSOA_CENTRO_CUSTO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PessoaCentroCusto> getList() {
		return getList(null);
	}

	public static ArrayList<PessoaCentroCusto> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PessoaCentroCusto> list = new ArrayList<PessoaCentroCusto>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PessoaCentroCusto obj = PessoaCentroCustoDAO.get(rsm.getInt("CD_PESSOA_CENTRO_CUSTO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaCentroCustoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM CTB_PESSOA_CENTRO_CUSTO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
