package com.tivic.manager.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;

@DestinationConfig(enabled = false)
public class TipoOcorrenciaServices {

	public static String[] tipoAcao = {"Admissão", "Atribuição", "Transferência", "Previsão",
									   "Relevância", "Conclusão", "Reabertura", "Agendamento",
									   "Outros", "Arquivo"};

	public static final int ADMISSAO = 0;
	public static final int ATRIBUICAO = 1;
	public static final int TRANSFERENCIA = 2;
	public static final int PREVISAO = 3;
	public static final int RELEVANCIA = 4;
	public static final int CONCLUSAO = 5;
	public static final int REABERTURA = 6;
	public static final int AGENDAMENTO = 7;
	public static final int OUTROS = 8;
	public static final int ARQUIVO = 9;

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * " +
					"FROM crm_tipo_ocorrencia A, grl_tipo_ocorrencia B " +
					"WHERE A.cd_tipo_ocorrencia=B.cd_tipo_ocorrencia " +
					"ORDER BY nm_tipo_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getOutrosTiposOcorrencia() {
		return getOutrosTiposOcorrencia(null);
	}

	public static ResultSetMap getOutrosTiposOcorrencia(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_ocorrencia A, grl_tipo_ocorrencia B " +
					                         "WHERE A.cd_tipo_ocorrencia=B.cd_tipo_ocorrencia " +
					                         "  AND tp_acao = "+OUTROS+
					                         "ORDER BY nm_tipo_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaServices.getOutrosTiposOcorrencia: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getTiposOcorrenciaByTpAcao(int tpAcao) {
		return getTiposOcorrenciaByTpAcao(tpAcao, null);
	}

	public static ResultSetMap getTiposOcorrenciaByTpAcao(int tpAcao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_ocorrencia A, grl_tipo_ocorrencia B WHERE A.cd_tipo_ocorrencia=B.cd_tipo_ocorrencia AND tp_acao = ?");
			pstmt.setInt(1, tpAcao);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaServices.getTiposOcorrenciaByTpAcao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaServices.getTiposOcorrenciaByTpAcao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoOcorrencia getByTpAcao(int tpAcao) {
		return getByTpAcao(tpAcao, null);
	}

	public static TipoOcorrencia getByTpAcao(int tpAcao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_ocorrencia A, grl_tipo_ocorrencia B WHERE A.cd_tipo_ocorrencia=B.cd_tipo_ocorrencia AND A.tp_acao=?");
			pstmt.setInt(1, tpAcao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoOcorrencia(rs.getInt("cd_tipo_ocorrencia"),
						rs.getString("nm_tipo_ocorrencia"),
						rs.getInt("tp_acao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaServices.getByTpAcao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaServices.getByTpAcao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
