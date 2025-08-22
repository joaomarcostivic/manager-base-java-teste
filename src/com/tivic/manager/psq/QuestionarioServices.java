package com.tivic.manager.psq;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

@DestinationConfig(enabled = false)
public class QuestionarioServices {

	public static ResultSetMap getQuestoes(int cdQuestionario) {
		return getQuestoes(cdQuestionario, null);
	}

	public static ResultSetMap getQuestoes(int cdQuestionario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					  "FROM psq_questao "+
				      "WHERE cd_questionario = ?");
			pstmt.setInt(1, cdQuestionario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioServices.getQuestoes: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getTemas(int cdQuestionario) {
		return getTemas(cdQuestionario, null);
	}

	public static ResultSetMap getTemas(int cdQuestionario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					  "FROM psq_tema "+
				      "WHERE cd_questionario = ?");
			pstmt.setInt(1, cdQuestionario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioServices.getTemas: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getQuestoesByTemas(int cdQuestionario, int cdTema) {
		return getQuestoesByTemas(cdQuestionario, cdTema, null);
	}

	public static ResultSetMap getQuestoesByTemas(int cdQuestionario, int cdTema, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					  "FROM psq_tema A " +
					  "JOIN psq_questao B ON (A.cd_questionario = B.cd_questionario AND A.cd_tema = B.cd_tema) "+
				      "WHERE A.cd_questionario = ? "+
				      ((cdTema!=0)?" AND B.cd_tema = "+cdTema:"")+
				      " ORDER BY A.cd_tema, B.nr_ordem");

			pstmt.setInt(1, cdQuestionario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! QuestionarioServices.getTemas: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}