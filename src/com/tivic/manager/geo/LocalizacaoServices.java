package com.tivic.manager.geo;

import java.sql.Connection;

import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class LocalizacaoServices {
	
	public static final int TP_LOCALIZACAO_POSICAO = 0;
	public static final int  TP_LOCALIZACAO_ABRANGENCIA = 1;
	
	public static final String[] tiposLocalizacao = {"Posiçao", "Abrangência"};
	
	public static Result save(Localizacao localizacao){
		return save(localizacao, null);
	}

	public static Result save(Localizacao localizacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(localizacao==null)
				return new Result(-1, "Erro ao salvar. Localizacao é nulo");

			int retorno;
			if(localizacao.getCdLocalizacao()==0){
				retorno = LocalizacaoDAO.insert(localizacao, connect);
				localizacao.setCdLocalizacao(retorno);
			}
			else {
				retorno = LocalizacaoDAO.update(localizacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOCALIZACAO", localizacao);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
