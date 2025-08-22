package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;

public class ConcessionarioPessoaDAO{

	public static int insert(ConcessionarioPessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(ConcessionarioPessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_concessionario_pessoa", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConcessionarioPessoa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_concessionario_pessoa (cd_concessionario_pessoa,"+
			                                  "cd_concessionario,"+
			                                  "cd_pessoa,"+
			                                  "tp_vinculo,"+
			                                  "st_concessionario_pessoa,"+
			                                  "txt_observacao,"+
			                                  "dt_ativacao,"+
			                                  "dt_inativacao,"+
			                                  "dt_pendencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConcessionario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessionario());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			pstmt.setInt(4,objeto.getTpVinculo());
			if(objeto.getStConcessionarioPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getStConcessionarioPessoa());
			if(objeto.getTxtObservacao()==null)
				pstmt.setNull(6, Types.VARCHAR);
			else
				pstmt.setString(6,objeto.getTxtObservacao());
			if(objeto.getDtAtivacao()==null)
				pstmt.setNull(7,Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtAtivacao().getTimeInMillis()));
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(8,Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			if(objeto.getDtPendencia()==null)
				pstmt.setNull(9,Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPendencia().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ConcessionarioPessoa objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ConcessionarioPessoa objeto, int cdConcessionarioPessoaOld) {
		return update(objeto, cdConcessionarioPessoaOld, null);
	}

	public static int update(ConcessionarioPessoa objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ConcessionarioPessoa objeto, int cdConcessionarioPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_concessionario_pessoa SET cd_concessionario_pessoa=?,"+
												      		   "cd_concessionario=?,"+
												      		   "cd_pessoa=?,"+
												      		   "tp_vinculo=?,"+
												      		   "st_concessionario_pessoa=?,"+
							                                   "txt_observacao=?,"+
							                                   "dt_ativacao=?,"+
							                                   "dt_inativacao=?,"+
							                                   "dt_pendencia=? WHERE cd_concessionario_pessoa=?");
			pstmt.setInt(1,objeto.getCdConcessionarioPessoa());
			if(objeto.getCdConcessionario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessionario());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			pstmt.setInt(4,objeto.getTpVinculo());
			if(objeto.getStConcessionarioPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getStConcessionarioPessoa());
			if(objeto.getTxtObservacao()==null)
				pstmt.setNull(6, Types.VARCHAR);
			else
				pstmt.setString(6,objeto.getTxtObservacao());
			if(objeto.getDtAtivacao()==null)
				pstmt.setNull(7,Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtAtivacao().getTimeInMillis()));
			if(objeto.getDtInativacao()==null)
				pstmt.setNull(8,Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInativacao().getTimeInMillis()));
			if(objeto.getDtPendencia()==null)
				pstmt.setNull(9,Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPendencia().getTimeInMillis()));
			pstmt.setInt(10, cdConcessionarioPessoaOld!=0 ? cdConcessionarioPessoaOld : objeto.getCdConcessionarioPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessionarioPessoa) {
		return delete(cdConcessionarioPessoa, null);
	}

	public static int delete(int cdConcessionarioPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_concessionario_pessoa WHERE cd_concessionario_pessoa=?");
			pstmt.setInt(1, cdConcessionarioPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ConcessionarioPessoa get(int cdConcessionarioPessoa) {
		return get(cdConcessionarioPessoa, null);
	}

	public static ConcessionarioPessoa get(int cdConcessionarioPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessionario_pessoa WHERE cd_concessionario_pessoa=?");
			pstmt.setInt(1, cdConcessionarioPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ConcessionarioPessoa(rs.getInt("cd_concessionario_pessoa"),
						rs.getInt("cd_concessionario"),
						rs.getInt("cd_pessoa"),
						rs.getInt("tp_vinculo"),
						rs.getInt("cd_concessionario_pessoa"),
						rs.getString("txt_observacao"),
						(rs.getTimestamp("dt_ativacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ativacao").getTime()),
						(rs.getTimestamp("dt_inativacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inativacao").getTime()),
						(rs.getTimestamp("dt_pendencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_pendencia").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessionario_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ConcessionarioPessoa> getList() {
		return getList(null);
	}

	public static ArrayList<ConcessionarioPessoa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ConcessionarioPessoa> list = new ArrayList<ConcessionarioPessoa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ConcessionarioPessoa obj = ConcessionarioPessoaDAO.get(rsm.getInt("cd_concessionario_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.getList: " + e);
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

	@SuppressWarnings("deprecation")
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_concessionario_pessoa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
