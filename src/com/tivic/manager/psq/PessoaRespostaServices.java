package com.tivic.manager.psq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

@DestinationConfig(enabled = false)
public class PessoaRespostaServices {
	public static PessoaResposta getByAplicacao(int cdPessoa, int cdQuestaoValor, int cdQuestao, int cdQuestionario, int cdAplicacao, int cdResultado) {
		return getByAplicacao(cdPessoa, cdQuestaoValor, cdQuestao, cdQuestionario, cdAplicacao, cdResultado, null);
	}

	public static PessoaResposta getByAplicacao(int cdPessoa, int cdQuestaoValor, int cdQuestao, int cdQuestionario, int cdAplicacao, int cdResultado, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM psq_pessoa_resposta WHERE cd_pessoa=? AND cd_questao_valor=? AND cd_questao=? AND cd_questionario=? AND cd_aplicacao=? AND cd_resultado=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdQuestaoValor);
			pstmt.setInt(3, cdQuestao);
			pstmt.setInt(4, cdQuestionario);
			pstmt.setInt(5, cdAplicacao);
			pstmt.setInt(6, cdResultado);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaResposta(rs.getInt("cd_pessoa"),
						rs.getInt("cd_questionario"),
						rs.getInt("cd_questao_valor"),
						rs.getInt("cd_questao"),
						rs.getInt("cd_resposta"),
						rs.getInt("cd_digitador"),
						rs.getInt("cd_empresa_digitador"),
						rs.getInt("cd_vinculo_digitador"),
						(rs.getTimestamp("dt_resposta")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resposta").getTime()),
						rs.getBytes("blb_resposta")==null?null:rs.getBytes("blb_resposta"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_aplicacao"),
						rs.getInt("cd_resultado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaServices.getByAplicacao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaServices.getByAplicacao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteByAplicacao(int cdPessoa, int cdQuestao, int cdQuestionario, int cdAplicacao, int cdResultado) {
		return deleteByAplicacao(cdPessoa, cdQuestao, cdQuestionario, cdAplicacao, cdResultado, null);
	}

	public static int deleteByAplicacao(int cdPessoa, int cdQuestao, int cdQuestionario, int cdAplicacao, int cdResultado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM psq_pessoa_resposta WHERE cd_pessoa=? AND cd_questao=? AND cd_questionario=? AND cd_aplicacao=? AND cd_resultado=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdQuestao);
			pstmt.setInt(3, cdQuestionario);
			pstmt.setInt(4, cdAplicacao);
			pstmt.setInt(5, cdResultado);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaServices.deleteByAplicacao: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaServices.deleteByAplicacao: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/* RESPOSTA ÚNICA */
	public static PessoaResposta getQuestaoResposta(int cdPessoa,int cdQuestao, int cdQuestionario, int cdAplicacao, int cdResultado) {
		return getQuestaoResposta(cdPessoa, cdQuestao, cdQuestionario, cdAplicacao, cdResultado, null);
	}

	public static PessoaResposta getQuestaoResposta(int cdPessoa, int cdQuestao, int cdQuestionario, int cdAplicacao, int cdResultado, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM psq_pessoa_resposta WHERE cd_pessoa=? AND cd_questao=? AND cd_questionario=? AND cd_aplicacao=? AND cd_resultado=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdQuestao);
			pstmt.setInt(3, cdQuestionario);
			pstmt.setInt(4, cdAplicacao);
			pstmt.setInt(5, cdResultado);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaResposta(rs.getInt("cd_pessoa"),
						rs.getInt("cd_questionario"),
						rs.getInt("cd_questao_valor"),
						rs.getInt("cd_questao"),
						rs.getInt("cd_resposta"),
						rs.getInt("cd_digitador"),
						rs.getInt("cd_empresa_digitador"),
						rs.getInt("cd_vinculo_digitador"),
						(rs.getTimestamp("dt_resposta")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_resposta").getTime()),
						rs.getBytes("blb_resposta")==null?null:rs.getBytes("blb_resposta"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_aplicacao"),
						rs.getInt("cd_resultado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaServices.getQuestaoResposta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaRespostaServices.getQuestaoResposta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}