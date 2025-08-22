package com.tivic.manager.fta;

import java.sql.Connection;

import com.tivic.manager.bpm.ComponenteReferenciaServices;
import com.tivic.manager.bpm.ManutencaoBemServices;
import com.tivic.sol.connection.Conexao;


public class ReboqueServices {
	public static int delete(int cdReboque) {
		return delete(cdReboque, null);
	}

	public static int delete(int cdReboque, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//deletando manutencoes
			int retorno = ManutencaoBemServices.deleteByReferencia(cdReboque, connect);
			
			//deletando pneus
			if(retorno>0)
				retorno = PneuServices.deleteByVeiculo(cdReboque, connect);
			
			//deletando componentes
			if(retorno>0)
				retorno = ComponenteReferenciaServices.deleteByReferencia(cdReboque, connect);
			
			//deletando reboque
			if(retorno>0)
				retorno = ReboqueDAO.delete(cdReboque, connect);
		
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReboqueServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}