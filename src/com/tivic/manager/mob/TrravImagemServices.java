package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TrravImagemServices {

	public static Result save(int cdTrrav, ArrayList<TrravImagem> imagens) {
		return save(cdTrrav, imagens, null);
	}

	public static Result save(int cdTrrav, ArrayList<TrravImagem> imagens, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int retorno = 0;
			for (TrravImagem imagem: imagens) {
				imagem.setCdTrrav(cdTrrav);
				retorno = TrravImagemDAO.insert(imagem, connect);
				
				if(retorno<=0)
					break;
			}
			
			if(retorno>0)
				return new Result(1, "Imagem(s) incluídas com sucesso.");
			else
				return new Result(-1, "Erro ao incluir imagem(s).");
				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao incluir imagem(s).");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}