package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class DependenteDAO{

	public static int insert(Dependente objeto) {
		return insert(objeto, null);
	}

	public static int insert(Dependente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_dependente (cd_pessoa,"+
			                                  "cd_dependente,"+
			                                  "tp_parentesco,"+
			                                  "lg_ir,"+
			                                  "lg_salario_familia,"+
			                                  "lg_pensionista,"+
			                                  "tp_pagamento,"+
			                                  "tp_calculo_pensao,"+
			                                  "vl_aplicacao,"+
			                                  "nr_conta,"+
			                                  "tp_operacao,"+
			                                  "nr_agencia,"+
			                                  "cd_banco) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdDependente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDependente());
			pstmt.setInt(3,objeto.getTpParentesco());
			pstmt.setInt(4,objeto.getLgIr());
			pstmt.setInt(5,objeto.getLgSalarioFamilia());
			pstmt.setInt(6,objeto.getLgPensionista());
			pstmt.setInt(7,objeto.getTpPagamento());
			pstmt.setInt(8,objeto.getTpCalculoPensao());
			pstmt.setFloat(9,objeto.getVlAplicacao());
			pstmt.setString(10,objeto.getNrConta());
			pstmt.setInt(11,objeto.getTpOperacao());
			pstmt.setString(12,objeto.getNrAgencia());
			if(objeto.getCdBanco()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdBanco());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Dependente objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Dependente objeto, int cdPessoaOld, int cdDependenteOld) {
		return update(objeto, cdPessoaOld, cdDependenteOld, null);
	}

	public static int update(Dependente objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Dependente objeto, int cdPessoaOld, int cdDependenteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_dependente SET cd_pessoa=?,"+
												      		   "cd_dependente=?,"+
												      		   "tp_parentesco=?,"+
												      		   "lg_ir=?,"+
												      		   "lg_salario_familia=?,"+
												      		   "lg_pensionista=?,"+
												      		   "tp_pagamento=?,"+
												      		   "tp_calculo_pensao=?,"+
												      		   "vl_aplicacao=?,"+
												      		   "nr_conta=?,"+
												      		   "tp_operacao=?,"+
												      		   "nr_agencia=?,"+
												      		   "cd_banco=? WHERE cd_pessoa=? AND cd_dependente=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdDependente());
			pstmt.setInt(3,objeto.getTpParentesco());
			pstmt.setInt(4,objeto.getLgIr());
			pstmt.setInt(5,objeto.getLgSalarioFamilia());
			pstmt.setInt(6,objeto.getLgPensionista());
			pstmt.setInt(7,objeto.getTpPagamento());
			pstmt.setInt(8,objeto.getTpCalculoPensao());
			pstmt.setFloat(9,objeto.getVlAplicacao());
			pstmt.setString(10,objeto.getNrConta());
			pstmt.setInt(11,objeto.getTpOperacao());
			pstmt.setString(12,objeto.getNrAgencia());
			if(objeto.getCdBanco()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdBanco());
			pstmt.setInt(14, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(15, cdDependenteOld!=0 ? cdDependenteOld : objeto.getCdDependente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdDependente) {
		return delete(cdPessoa, cdDependente, null);
	}

	public static int delete(int cdPessoa, int cdDependente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_dependente WHERE cd_pessoa=? AND cd_dependente=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdDependente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Dependente get(int cdPessoa, int cdDependente) {
		return get(cdPessoa, cdDependente, null);
	}

	public static Dependente get(int cdPessoa, int cdDependente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_dependente WHERE cd_pessoa=? AND cd_dependente=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdDependente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Dependente(rs.getInt("cd_pessoa"),
						rs.getInt("cd_dependente"),
						rs.getInt("tp_parentesco"),
						rs.getInt("lg_ir"),
						rs.getInt("lg_salario_familia"),
						rs.getInt("lg_pensionista"),
						rs.getInt("tp_pagamento"),
						rs.getInt("tp_calculo_pensao"),
						rs.getFloat("vl_aplicacao"),
						rs.getString("nr_conta"),
						rs.getInt("tp_operacao"),
						rs.getString("nr_agencia"),
						rs.getInt("cd_banco"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_dependente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_dependente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
