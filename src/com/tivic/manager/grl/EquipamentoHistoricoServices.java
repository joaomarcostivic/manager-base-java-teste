package com.tivic.manager.grl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class EquipamentoHistoricoServices {
	public static Result sync(ArrayList<EquipamentoHistorico> historicos) {
		return sync(historicos, null);
	}

	public static Result sync(ArrayList<EquipamentoHistorico> historicos, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			System.out.println("["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Sincronizando " + historicos.size() + " historicos.");
			ArrayList<EquipamentoHistorico> historicosRetorno = new ArrayList<EquipamentoHistorico>();
			int retorno = 0;
			for (EquipamentoHistorico historico: historicos) {
				retorno = EquipamentoHistoricoDAO.insert(historico, connect);
				
				if(retorno > 0) {
					historico.setCdHistorico(retorno);
					//historicosRetorno.add(historico);
				}
				else
					break;
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			System.out.println("Historicos sincronizados.");
			return new Result(retorno, retorno>0 ? "Sincronizado " + (historicos.size() == historicosRetorno.size() ? " com sucesso." : " parcialmente.") : "Erro ao sincronizar Historicos.", "HISTORICOS", historicosRetorno);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
