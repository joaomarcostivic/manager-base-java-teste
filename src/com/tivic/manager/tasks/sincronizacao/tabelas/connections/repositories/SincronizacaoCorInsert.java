package com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.cor.CorRepositoryDAO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class SincronizacaoCorInsert implements ISincronizacaoInsert<Cor>{

	private ManagerLog managerLog;
	
	public SincronizacaoCorInsert() throws Exception {
		 this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@Override
	public void insert(List<Cor> objects, CustomConnection connection) throws Exception {
		 try {
		        connection.initConnection(true);
		        int inserts = 0;
		        int updates = 0;
		        CorRepositoryDAO corRepositoryDAO = new CorRepositoryDAO();
		        for (Cor objeto : objects) {
		            Cor existingCor = corRepositoryDAO.get(objeto.getCdCor());
		            if (existingCor == null) {
		                corRepositoryDAO.insert(objeto, connection);
		                inserts++;
		                managerLog.info("Cor inserida --> " + " cdCor: " + objeto.getCdCor() + " / " + "nmCor: " + objeto.getNmCor(), new GregorianCalendar().getTime().toString());
		            } else {
		                if (!existingCor.equals(objeto)) {
		                    corRepositoryDAO.update(objeto, connection);
		                    updates++;
		                    managerLog.info("Cor atualizada --> " + " cdCor: " + objeto.getCdCor() + " / " + "nmCor: " + objeto.getNmCor(), new GregorianCalendar().getTime().toString());
		                }
		            }
		        }
		        connection.finishConnection();
		        managerLog.info("Total de cores inseridas: " + inserts, new GregorianCalendar().getTime().toString());
		        managerLog.info("Total de cores atualizadas: " + updates, new GregorianCalendar().getTime().toString());
		    } finally {
		        connection.closeConnection();
		    }
	}

}
