package com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.EspecieVeiculoDAO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class SincronizadorEspecieVeiculoInsert implements ISincronizacaoInsert<EspecieVeiculo> {
	
	private ManagerLog managerLog;
	
	public SincronizadorEspecieVeiculoInsert() throws Exception {
		 this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@Override
	public void insert(List<EspecieVeiculo> objects, CustomConnection connection) throws Exception {
		try {
			connection.initConnection(true);
	        int inserts = 0;
	        int updates = 0;
	        EspecieVeiculoDAO especieVeiculoDAO = new EspecieVeiculoDAO();
	        for (EspecieVeiculo objeto : objects) {
	        	EspecieVeiculo existingVeiculo = especieVeiculoDAO.get(objeto.getCdEspecie());
	            if (existingVeiculo == null) {
	                especieVeiculoDAO.insert(objeto, connection.getConnection());
	                inserts++;
	                managerLog.info("Especie Veiculo inserida --> " + " cdEspecie: " + objeto.getCdEspecie() + " / " + "dsEspecie: " + objeto.getDsEspecie(), new GregorianCalendar().getTime().toString());
	            } else {
	                if (!existingVeiculo.equals(objeto)) {
	                    especieVeiculoDAO.update(objeto, connection.getConnection());
	                    updates++;
	                    managerLog.info("Especie Veiculo atualizada --> " + " cdEspecie: " + objeto.getCdEspecie() + " / " + "dsEspecie: " + objeto.getDsEspecie(), new GregorianCalendar().getTime().toString());
	                }
	            }
	        }
	        connection.finishConnection();
	        managerLog.info("Total de Especies Veiculos inseridas: " + inserts, new GregorianCalendar().getTime().toString());
	        managerLog.info("Total de Especies Veiculos  atualizadas: " + updates, new GregorianCalendar().getTime().toString());
	    } finally {
	        connection.closeConnection();
	    }
	}

}
