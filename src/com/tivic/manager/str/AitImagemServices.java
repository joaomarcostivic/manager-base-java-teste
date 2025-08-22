package com.tivic.manager.str;

import java.sql.Connection;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class AitImagemServices {
	public static Result save(int cdAit, ArrayList<AitImagem> imagens) {
		return save(cdAit, imagens, null);
	}

	public static Result save(int cdAit, ArrayList<AitImagem> imagens, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int retorno = 0;
			for (AitImagem imagem: imagens) {
				imagem.setCdAit(cdAit);
				retorno = AitImagemDAO.insert(imagem, connect);
				
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


	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		int qtLimite            = 0;
		boolean lgBlob          = true;
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
			if (criterios.get(i).getColumn().equalsIgnoreCase("lgBlob"))
				lgBlob = Boolean.parseBoolean(criterios.get(i).getValue());
			else
				crt.add(criterios.get(i));
		}

		String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
		return Search.findAndLog(
				"SELECT "+sqlLimit[0]+ (lgBlob?" * ":" cd_ait, cd_imagem, lg_impressao, tp_imagem ") +" FROM "+(Util.isStrBaseAntiga()?"str":"mob")+"_ait_imagem WHERE 1=1 ", sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
}
