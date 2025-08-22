package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PessoaReferenciaDAO{

	public static int insert(PessoaReferencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaReferencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_pessoa_referencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPessoaReferencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_referencia (cd_pessoa_referencia,"+
			                                  "cd_pessoa,"+
			                                  "nm_pessoa_referencia,"+
			                                  "nr_telefone,"+
			                                  "dt_informacao,"+
			                                  "tp_referencia) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getNmPessoaReferencia());
			pstmt.setString(4,objeto.getNrTelefone());
			if(objeto.getDtInformacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInformacao().getTimeInMillis()));
			pstmt.setInt(6,objeto.getTpReferencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaReferencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PessoaReferencia objeto, int cdPessoaReferenciaOld) {
		return update(objeto, cdPessoaReferenciaOld, null);
	}

	public static int update(PessoaReferencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PessoaReferencia objeto, int cdPessoaReferenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_referencia SET cd_pessoa_referencia=?,"+
												      		   "cd_pessoa=?,"+
												      		   "nm_pessoa_referencia=?,"+
												      		   "nr_telefone=?,"+
												      		   "dt_informacao=?,"+
												      		   "tp_referencia=? WHERE cd_pessoa_referencia=?");
			pstmt.setInt(1,objeto.getCdPessoaReferencia());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getNmPessoaReferencia());
			pstmt.setString(4,objeto.getNrTelefone());
			if(objeto.getDtInformacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtInformacao().getTimeInMillis()));
			pstmt.setInt(6,objeto.getTpReferencia());
			pstmt.setInt(7, cdPessoaReferenciaOld!=0 ? cdPessoaReferenciaOld : objeto.getCdPessoaReferencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoaReferencia) {
		return delete(cdPessoaReferencia, null);
	}

	public static int delete(int cdPessoaReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_referencia WHERE cd_pessoa_referencia=?");
			pstmt.setInt(1, cdPessoaReferencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaReferencia get(int cdPessoaReferencia) {
		return get(cdPessoaReferencia, null);
	}

	public static PessoaReferencia get(int cdPessoaReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_referencia WHERE cd_pessoa_referencia=?");
			pstmt.setInt(1, cdPessoaReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaReferencia(rs.getInt("cd_pessoa_referencia"),
						rs.getInt("cd_pessoa"),
						rs.getString("nm_pessoa_referencia"),
						rs.getString("nr_telefone"),
						(rs.getTimestamp("dt_informacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_informacao").getTime()),
						rs.getInt("tp_referencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_referencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaReferenciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_referencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
