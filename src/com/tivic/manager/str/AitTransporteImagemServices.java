package com.tivic.manager.str;

import java.sql.Connection;
import java.util.ArrayList;

import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AitTransporteImagemServices {
	public static Result save(int cdAit, ArrayList<AitTransporteImagem> imagens) {
		return save(cdAit, imagens, null);
	}

	public static Result save(int cdAit, ArrayList<AitTransporteImagem> imagens, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int retorno = 0;
			for (AitTransporteImagem imagem: imagens) {
				imagem.setCdAit(cdAit);
				System.out.println(imagem);
				retorno = AitTransporteImagemDAO.insert(imagem, connect);
				
				if(retorno<=0)
					break;
			}
			
			if(retorno>0)
				return new Result(1, "Imagem(s) incluídas com sucesso no AIT de Transporte.");
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
