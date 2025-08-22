package com.tivic.manager.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;


import sol.dao.ResultSetMap;

@DestinationConfig(enabled = false)
public class FidelidadeServices {

	public static int save(Fidelidade fidelidade){
		return save(fidelidade, null);
	}
	public static int save(Fidelidade fidelidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno;
			if(fidelidade.getCdFidelidade()==0){
				retorno = FidelidadeDAO.insert(fidelidade, connect);
			}
			else{
				retorno = FidelidadeDAO.update(fidelidade, connect);
				retorno = retorno>0?fidelidade.getCdFidelidade():retorno;
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFidelidade) {
		return delete(cdFidelidade, null);
	}

	public static int delete(int cdFidelidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdFidelidade<=0)
				return -1;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}


			//fidelidade/movimento
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_fidelidade_movimento WHERE cd_fidelidade=?");
			pstmt.setInt(1, cdFidelidade);
			pstmt.executeUpdate();

			//fidelidade/pessoa
			pstmt = connect.prepareStatement("DELETE FROM crm_fidelidade_pessoa WHERE cd_fidelidade=?");
			pstmt.setInt(1, cdFidelidade);
			pstmt.executeUpdate();

			if(FidelidadeDAO.delete(cdFidelidade, connect)<=0){
				Conexao.rollback(connect);
				return -2;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -3;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int addParticipantes(int cdFidelidade, ArrayList<Integer> cdPessoas){
		return addParticipantes(cdFidelidade, cdPessoas, null);
	}
	public static int addParticipantes(int cdFidelidade, ArrayList<Integer> cdPessoas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdFidelidade<=0)
				return -1;
			if(cdPessoas==null)
				return -2;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 0;

			for(int i=0; i<cdPessoas.size(); i++){
				int cdPessoa = cdPessoas.get(i).intValue();

				if(cdPessoa<=0)
					return -3;

				FidelidadePessoa participante = new FidelidadePessoa(cdPessoa, //cdPessoa
																	 cdFidelidade, //cdFidelidade
																	 new GregorianCalendar(), //dtCadastro
																	 1, //stCadastro,
																	 null, //txtObservacao,
																	 null); //nrMatricula

				retorno = addParticipante(cdFidelidade, participante, connect);

				if(retorno<0)
					break;
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.addParticipantes: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.addParticipantes: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int addParticipante(int cdFidelidade, FidelidadePessoa participante){
		return addParticipante(cdFidelidade, participante, null);
	}
	public static int addParticipante(int cdFidelidade, FidelidadePessoa participante, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdFidelidade<=0)
				return -1;
			if(participante==null)
				return -2;
			if(participante.getCdPessoa()<=0)
				return -3;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			//sem matricula indicada
			if(participante.getNrMatricula()==null ||
			   participante.getNrMatricula().equals("")){
				/*
				 * nrMatricula formato PP.MMAA.SSSSSS.D
				 * FF = código do plano de fidelidade com 2 digitos
				 * PPPPPP = código da pessoa com 6 digitos
				 * MM = mes com 2 digitos
				 * AA = ano com 2 digitos
				 * D = dígito verificador
				 */
				GregorianCalendar hoje = new GregorianCalendar();
				String nrMatricula = new DecimalFormat("00").format(cdFidelidade) +
									Util.formatDate(hoje, "MM") +
									Util.formatDate(hoje, "yy")+
									new DecimalFormat("000000").format(participante.getCdPessoa());
				nrMatricula += Util.getDvMod10(nrMatricula);

				participante.setNrMatricula(nrMatricula);
			}

			int retorno = FidelidadePessoaDAO.insert(participante, connect);

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.addParticipante: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.addParticipante: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteParticipante(int cdFidelidade, int cdPessoa) {
		return deleteParticipante(cdFidelidade, cdPessoa, null);
	}

	public static int deleteParticipante(int cdFidelidade, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdFidelidade<=0)
				return -1;
			if(cdPessoa<=0)
				return -2;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			//fidelidade/movimento
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_fidelidade_movimento WHERE cd_fidelidade=? AND cd_pessoa=?");
			pstmt.setInt(1, cdFidelidade);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();

			if(FidelidadePessoaDAO.delete(cdPessoa, cdFidelidade, connect)<=0){
				Conexao.rollback(connect);
				return -3;
			}

			if(isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.deleteParticipante: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.deleteParticipante: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -4;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getParticipantes(int cdFidelidade) {
		return getParticipantes(cdFidelidade, null);
	}

	public static ResultSetMap getParticipantes(int cdFidelidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa, B.nm_email, " +
					  "(SELECT SUM(C.vl_movimento) FROM crm_fidelidade_movimento C WHERE C.cd_fidelidade=A.cd_fidelidade AND C.cd_pessoa=A.cd_pessoa) as vl_saldo " +
					  "FROM crm_fidelidade_pessoa A " +
					  "LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) "+
					  "WHERE A.cd_fidelidade = ? " +
				      "ORDER BY B.nm_pessoa " );
			pstmt.setInt(1, cdFidelidade);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeServices.getParticipantes: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
