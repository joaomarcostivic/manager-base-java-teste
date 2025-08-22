package com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.TipoVeiculoDAO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class SincronizacaoTipoVeiculoInsert implements ISincronizacaoInsert<TipoVeiculo> {
	
	private ManagerLog managerLog;

    public SincronizacaoTipoVeiculoInsert() throws Exception {
         this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
    }

	@Override
	public void insert(List<TipoVeiculo> objects, CustomConnection connection) throws Exception {
		try {
            connection.initConnection(true);
            int inserts = 0;
            int updates = 0;
            TipoVeiculoDAO tipoVeiculoRepositoryDAO = new TipoVeiculoDAO();
            for (TipoVeiculo objeto : objects) {
            	TipoVeiculo existingTipoVeiculo = tipoVeiculoRepositoryDAO.get(objeto.getCdTipoVeiculo());
                if (existingTipoVeiculo == null) {
                    tipoVeiculoRepositoryDAO.insert(objeto, connection.getConnection());
                    inserts++;
                    managerLog.info("Tipo de Veículos inserido --> " + " CdTipoVeiculo: " + objeto.getCdTipoVeiculo() + " / " + "nmTipoVeiculo: " + objeto.getNmTipoVeiculo(), new GregorianCalendar().getTime().toString());
                } else {             
                    if (!existingTipoVeiculo.equals(objeto)) {
                        tipoVeiculoRepositoryDAO.update(objeto, connection.getConnection());
                        updates++;
                        managerLog.info("Tipo de Veículos atualizada --> " + " CdTipoVeiculo: " + objeto.getCdTipoVeiculo() + " / " + "nmTipoVeiculo: " + objeto.getNmTipoVeiculo(), new GregorianCalendar().getTime().toString());
                    }
                }
            }
            connection.finishConnection();
            managerLog.info("Total de Tipo de Veículos inseridos: " + inserts, new GregorianCalendar().getTime().toString());
            managerLog.info("Total de Tipo de Veículos atualizados: " + updates, new GregorianCalendar().getTime().toString());
        } finally {
            connection.closeConnection();
        }
		
	}

}
