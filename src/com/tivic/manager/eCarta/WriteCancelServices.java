package com.tivic.manager.eCarta;

import java.sql.Connection;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.mob.grafica.LoteImpressaoServices;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;

import sol.util.Result;

public class WriteCancelServices {
	
	public static Result writeCancelTransit(String numLot)
	{
		return writeCancelTransit(numLot, null);
	}
	
	public static Result writeCancelTransit(String numLot, Connection connect)
	{
		int numLote = Integer.parseInt(numLot);
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull) 
			connect = Conexao.conectar();
		
		LoteImpressao lote = LoteImpressaoDAO.get(numLote, connect);
		updateLot(lote, connect);
		
		if (isConnectionNull)
			Conexao.desconectar(connect);
		
		Result r = new Result(-1);
		r.setCode(1);
		r.setMessage("Cancelamento solicitado!");
		r.addObject("STATUS", "Cancelamento solicitado!");
		
		if (isConnectionNull)
			Conexao.desconectar(connect);
		
		return r;
	}
	
	private static void updateLot (LoteImpressao lote, Connection connect)
	{
		if (lote.getStLoteImpressao() == LoteImpressaoSituacao.ECARTAS_LOTE_AUTORIZADO)
		{
			lote.setStLoteImpressao(LoteImpressaoSituacao.ECARTAS_SOLICITAR_CANCELAMENTO);
			LoteImpressaoServices.save(lote, connect);
		}
	}
}
