package com.tivic.manager.crm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

@DestinationConfig(enabled = false)
public class MailingPlanejamentoServices {
	public static final int ST_PENDENTE = 0;
	public static final int ST_ENVIADO = 1;
	
	public static final String[] situacaoPlanejamento = {"Pendente de Envio", "Enviado"};
	
	public static int save(MailingPlanejamento planejamento){
		return save(planejamento, null);
	}
	public static int save(MailingPlanejamento planejamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno;
			if(planejamento.getCdPlanejamento()==0){
				planejamento.setDtPlanejamento(new GregorianCalendar());
				retorno = MailingPlanejamentoDAO.insert(planejamento, connect);
			}
			else{
				retorno = MailingPlanejamentoDAO.update(planejamento, connect);
				retorno = retorno>0?planejamento.getCdPlanejamento():retorno;
			}
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MailingPlanejamentoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
