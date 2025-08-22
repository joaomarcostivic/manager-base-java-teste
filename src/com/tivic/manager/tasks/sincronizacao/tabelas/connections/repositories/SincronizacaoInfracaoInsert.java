package com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class SincronizacaoInfracaoInsert implements ISincronizacaoInsert<Infracao> {
	
	private ManagerLog managerLog;

    public SincronizacaoInfracaoInsert() throws Exception {
         this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
    }

	@Override
	public void insert(List<Infracao> objects, CustomConnection connection) throws Exception {
		try {
            connection.initConnection(true);
            int inserts = 0;
            int updates = 0;
            InfracaoDAO infracaoRepositoryDAO = new InfracaoDAO();
            for (Infracao objeto : objects) {
            	Infracao existingInfracao = infracaoRepositoryDAO.get(objeto.getCdInfracao());
                if (existingInfracao == null) {
                    infracaoRepositoryDAO.insert(objeto, connection.getConnection());
                    inserts++;
                    managerLog.info("Infração inseridas --> " + " CdInfracao: " + objeto.getCdInfracao() + " / " + "NrArtigo: " + objeto.getNrArtigo(), new GregorianCalendar().getTime().toString());
                } else {             
                    if (!existingInfracao.equals(objeto)) {
                        infracaoRepositoryDAO.update(objeto, connection.getConnection());
                        updates++;
                        managerLog.info("Infração atualizada --> " + " CdInfracao: " + objeto.getCdInfracao() + " / " + "NrArtigo: " + objeto.getNrArtigo(), new GregorianCalendar().getTime().toString());
                    }
                }
            }
            connection.finishConnection();
            managerLog.info("Total de Infrações inseridas: " + inserts, new GregorianCalendar().getTime().toString());
            managerLog.info("Total de Infrações atualizadas: " + updates, new GregorianCalendar().getTime().toString());
        } finally {
            connection.closeConnection();
        }
		
	}
}
