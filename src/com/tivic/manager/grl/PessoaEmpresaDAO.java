package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PessoaEmpresaDAO{

	public static int insert(PessoaEmpresa objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaEmpresa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_empresa (cd_empresa,"+
			                                  "cd_pessoa,"+
			                                  "cd_vinculo,"+
			                                  "dt_vinculo,"+
			                                  "st_vinculo) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdVinculo() < 0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVinculo());
			if(objeto.getDtVinculo()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtVinculo().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStVinculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaEmpresa objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(PessoaEmpresa objeto, int cdEmpresaOld, int cdPessoaOld, int cdVinculoOld) {
		return update(objeto, cdEmpresaOld, cdPessoaOld, cdVinculoOld, null);
	}

	public static int update(PessoaEmpresa objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(PessoaEmpresa objeto, int cdEmpresaOld, int cdPessoaOld, int cdVinculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_empresa SET cd_empresa=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_vinculo=?,"+
												      		   "dt_vinculo=?,"+
												      		   "st_vinculo=? WHERE cd_empresa=? AND cd_pessoa=? AND cd_vinculo=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getCdVinculo());
			if(objeto.getDtVinculo()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtVinculo().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStVinculo());
			pstmt.setInt(6, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(7, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(8, cdVinculoOld!=0 ? cdVinculoOld : objeto.getCdVinculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa, int cdPessoa, int cdVinculo) {
		return delete(cdEmpresa, cdPessoa, cdVinculo, null);
	}

	public static int delete(int cdEmpresa, int cdPessoa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_empresa WHERE cd_empresa=? AND cd_pessoa=? AND cd_vinculo=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdVinculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaEmpresa get(int cdEmpresa, int cdPessoa, int cdVinculo) {
		return get(cdEmpresa, cdPessoa, cdVinculo, null);
	}

	public static PessoaEmpresa get(int cdEmpresa, int cdPessoa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_empresa WHERE cd_empresa=? AND cd_pessoa=? AND cd_vinculo=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdVinculo);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaEmpresa(rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_vinculo"),
						(rs.getTimestamp("dt_vinculo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vinculo").getTime()),
						rs.getInt("st_vinculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static PessoaEmpresa get(int cdEmpresa, int cdPessoa) {
		return get(cdEmpresa, cdPessoa, null);
	}

	public static PessoaEmpresa get(int cdEmpresa, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_empresa WHERE cd_empresa=? AND cd_pessoa=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdPessoa);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaEmpresa(rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_vinculo"),
						(rs.getTimestamp("dt_vinculo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vinculo").getTime()),
						rs.getInt("st_vinculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
