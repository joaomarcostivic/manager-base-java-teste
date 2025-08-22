package com.tivic.manager.srh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;

import sol.dao.ResultSetMap;

public class DependenteServices {

	public static final String[] tipoParentesco = {"Cônjuge (1º Grau)",
												   "Filho(a) (1º Grau)",
												   "Pai/Mãe (1º Grau)",
												   "Irmã(ão) (1º Grau)",
												   "Avô/Avó (2º Grau)",
												   "Neto(a) (2º Grau)",
												   "Tio(a) (2º Grau)",
												   "Sobrinho(a) (2º Grau)",
												   "Primo(a) (3º Grau)",
												   "Sogro(a) (Afinidade)",
		  										   "Cunhado(a) (Afinidade)"};

	public static final String[] tipoCalculoPensao = {"% Bruto",
												   	  "% Líquido",
												   	  "Valor Fixo",
												   	  "% Sal. Mínimo",
												   	  "% Sal. Base",
												   	  "% Base Cálculo"};

	public static final String[] tipoPagamento = {"Tipo 01",
	     									      "Tipo 02"};

	public static final String[] tipoOperacao = {"Débito",
		   									     "Crédito"};

	public static int insert(PessoaFisica pessoa, Dependente dependente){
		return insert(pessoa, dependente, null);
	}

	public static int insert(PessoaFisica pessoa, Dependente dependente, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdDependente = PessoaFisicaDAO.insert(pessoa, connection);

			if (cdDependente <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			dependente.setCdDependente(cdDependente);
			if (DependenteDAO.insert(dependente, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			if (isConnectionNull)
				connection.commit();
			return cdDependente;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DependenteServices.insert: " + sqlExpt);
			Conexao.rollback(connection);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DependenteServices.insert: " +  e);
			Conexao.rollback(connection);
			return -1;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}

	public static int update(PessoaFisica pessoa, Dependente dependente){
		return update(pessoa, dependente, null);
	}

	public static int update(PessoaFisica pessoa, Dependente dependente, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			if(PessoaFisicaDAO.update(pessoa) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			if (DependenteDAO.update(dependente) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DependenteServices.update: " + sqlExpt);
			Conexao.rollback(connection);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DependenteServices.update: " +  e);
			Conexao.rollback(connection);
			return -1;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}


	public static int delete(int cdPessoa, int cdDependente) {
		return delete(cdPessoa, cdDependente, null);
	}

	public static int delete(int cdPessoa, int cdDependente, Connection connect){
		boolean isConnectionNull = connect==null;
		int resultado = -1;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			connect.setAutoCommit(false);

			if(DependenteDAO.delete(cdPessoa, cdDependente, connect) > 0) 	{
				resultado = PessoaFisicaDAO.delete(cdDependente, connect);
			}
			else	{
				connect.rollback();
			}
			if (resultado > 0)
				connect.commit();
			return resultado;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.delete: " + sqlExpt);
			Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DependenteDAO.delete: " +  e);
			Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(int cdPessoa) {
		return find(cdPessoa, null);
	}

	public static ResultSetMap find(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, " +
 				"	B.dt_nascimento, B.nr_rg, B.cd_estado_rg, B.sg_orgao_rg, B.dt_emissao_rg, B.tp_sexo, B.st_estado_civil, B.cd_escolaridade, " +
				"	C.nm_pessoa, C.dt_cadastro, C.id_pessoa, " +
				"	D.nm_banco, D.nr_banco, D.id_banco " +
				"FROM grl_pessoa_fisica B, grl_pessoa C, srh_dependente A " +
				"	LEFT OUTER JOIN grl_banco D ON (D.cd_banco = A.cd_banco) " +
				"WHERE A.cd_dependente = B.cd_pessoa " +
				"   AND B.cd_pessoa = C.cd_pessoa" +
				"   AND A.cd_pessoa = ? ");
			pstmt.setInt(1, cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}
