package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PessoaOfertaDAO{

	public static int insert(PessoaOferta objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaOferta objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_pessoa_oferta (cd_pessoa,"+
			                                  "cd_oferta,"+
			                                  "cd_funcao,"+
			                                  "st_pessoa_oferta) VALUES (?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOferta());
			if(objeto.getCdFuncao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFuncao());
			pstmt.setInt(4,objeto.getStPessoaOferta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaOferta objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(PessoaOferta objeto, int cdPessoaOld, int cdOfertaOld, int cdFuncaoOld) {
		return update(objeto, cdPessoaOld, cdOfertaOld, cdFuncaoOld, null);
	}

	public static int update(PessoaOferta objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(PessoaOferta objeto, int cdPessoaOld, int cdOfertaOld, int cdFuncaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_pessoa_oferta SET cd_pessoa=?,"+
												      		   "cd_oferta=?,"+
												      		   "cd_funcao=?,"+
												      		   "st_pessoa_oferta=? WHERE cd_pessoa=? AND cd_oferta=? AND cd_funcao=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdOferta());
			pstmt.setInt(3,objeto.getCdFuncao());
			pstmt.setInt(4,objeto.getStPessoaOferta());
			pstmt.setInt(5, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(6, cdOfertaOld!=0 ? cdOfertaOld : objeto.getCdOferta());
			pstmt.setInt(7, cdFuncaoOld!=0 ? cdFuncaoOld : objeto.getCdFuncao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdOferta, int cdFuncao) {
		return delete(cdPessoa, cdOferta, cdFuncao, null);
	}

	public static int delete(int cdPessoa, int cdOferta, int cdFuncao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_pessoa_oferta WHERE cd_pessoa=? AND cd_oferta=? AND cd_funcao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdOferta);
			pstmt.setInt(3, cdFuncao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaOferta get(int cdPessoa, int cdOferta, int cdFuncao) {
		return get(cdPessoa, cdOferta, cdFuncao, null);
	}

	public static PessoaOferta get(int cdPessoa, int cdOferta, int cdFuncao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_pessoa_oferta WHERE cd_pessoa=? AND cd_oferta=? AND cd_funcao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdOferta);
			pstmt.setInt(3, cdFuncao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaOferta(rs.getInt("cd_pessoa"),
						rs.getInt("cd_oferta"),
						rs.getInt("cd_funcao"),
						rs.getInt("st_pessoa_oferta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_pessoa_oferta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PessoaOferta> getList() {
		return getList(null);
	}

	public static ArrayList<PessoaOferta> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PessoaOferta> list = new ArrayList<PessoaOferta>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PessoaOferta obj = PessoaOfertaDAO.get(rsm.getInt("cd_pessoa"), rsm.getInt("cd_oferta"), rsm.getInt("cd_funcao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_pessoa_oferta", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
