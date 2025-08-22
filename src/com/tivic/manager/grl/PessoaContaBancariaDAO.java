package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class PessoaContaBancariaDAO{

	public static int insert(PessoaContaBancaria objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaContaBancaria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_conta_bancaria");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_pessoa");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			int code = Conexao.getSequenceCode("grl_pessoa_conta_bancaria", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaBancaria(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_conta_bancaria (cd_conta_bancaria,"+
			                                  "cd_pessoa,"+
			                                  "cd_banco,"+
			                                  "nr_conta,"+
			                                  "nr_dv,"+
			                                  "nr_agencia,"+
			                                  "tp_operacao,"+
			                                  "nr_cpf_cnpj,"+
			                                  "nm_titular,"+
			                                  "st_conta,"+
			                                  "dt_abertura,"+
			                                  "lg_principal,"+
			                                  "tp_conta,"+
			                                  "lg_conta_conjunta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdBanco()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdBanco());
			pstmt.setString(4,objeto.getNrConta());
			pstmt.setString(5,objeto.getNrDv());
			pstmt.setString(6,objeto.getNrAgencia());
			pstmt.setInt(7,objeto.getTpOperacao());
			pstmt.setString(8,objeto.getNrCpfCnpj());
			pstmt.setString(9,objeto.getNmTitular());
			pstmt.setInt(10,objeto.getStConta());
			if(objeto.getDtAbertura()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtAbertura().getTimeInMillis()));
			pstmt.setInt(12,objeto.getLgPrincipal());
			pstmt.setInt(13,objeto.getTpConta());
			pstmt.setInt(14,objeto.getLgContaConjunta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaContaBancaria objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PessoaContaBancaria objeto, int cdContaBancariaOld, int cdPessoaOld) {
		return update(objeto, cdContaBancariaOld, cdPessoaOld, null);
	}

	public static int update(PessoaContaBancaria objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PessoaContaBancaria objeto, int cdContaBancariaOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_conta_bancaria SET cd_conta_bancaria=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_banco=?,"+
												      		   "nr_conta=?,"+
												      		   "nr_dv=?,"+
												      		   "nr_agencia=?,"+
												      		   "tp_operacao=?,"+
												      		   "nr_cpf_cnpj=?,"+
												      		   "nm_titular=?,"+
												      		   "st_conta=?,"+
												      		   "dt_abertura=?,"+
												      		   "lg_principal=?,"+
												      		   "tp_conta=?,"+
												      		   "lg_conta_conjunta=? WHERE cd_conta_bancaria=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdContaBancaria());
			pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdBanco()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdBanco());
			pstmt.setString(4,objeto.getNrConta());
			pstmt.setString(5,objeto.getNrDv());
			pstmt.setString(6,objeto.getNrAgencia());
			pstmt.setInt(7,objeto.getTpOperacao());
			pstmt.setString(8,objeto.getNrCpfCnpj());
			pstmt.setString(9,objeto.getNmTitular());
			pstmt.setInt(10,objeto.getStConta());
			if(objeto.getDtAbertura()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtAbertura().getTimeInMillis()));
			pstmt.setInt(12,objeto.getLgPrincipal());
			pstmt.setInt(13,objeto.getTpConta());
			pstmt.setInt(14,objeto.getLgContaConjunta());
			pstmt.setInt(15, cdContaBancariaOld!=0 ? cdContaBancariaOld : objeto.getCdContaBancaria());
			pstmt.setInt(16, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaBancaria, int cdPessoa) {
		return delete(cdContaBancaria, cdPessoa, null);
	}

	public static int delete(int cdContaBancaria, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_conta_bancaria WHERE cd_conta_bancaria=? AND cd_pessoa=?");
			pstmt.setInt(1, cdContaBancaria);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaContaBancaria get(int cdContaBancaria, int cdPessoa) {
		return get(cdContaBancaria, cdPessoa, null);
	}

	public static PessoaContaBancaria get(int cdContaBancaria, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_conta_bancaria WHERE cd_conta_bancaria=? AND cd_pessoa=?");
			pstmt.setInt(1, cdContaBancaria);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaContaBancaria(rs.getInt("cd_conta_bancaria"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_banco"),
						rs.getString("nr_conta"),
						rs.getString("nr_dv"),
						rs.getString("nr_agencia"),
						rs.getInt("tp_operacao"),
						rs.getString("nr_cpf_cnpj"),
						rs.getString("nm_titular"),
						rs.getInt("st_conta"),
						(rs.getTimestamp("dt_abertura")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_abertura").getTime()),
						rs.getInt("lg_principal"),
						rs.getInt("tp_conta"),
						rs.getInt("lg_conta_conjunta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_conta_bancaria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PessoaContaBancaria> getList() {
		return getList(null);
	}

	public static ArrayList<PessoaContaBancaria> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PessoaContaBancaria> list = new ArrayList<PessoaContaBancaria>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PessoaContaBancaria obj = PessoaContaBancariaDAO.get(rsm.getInt("cd_conta_bancaria"), rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_conta_bancaria", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}