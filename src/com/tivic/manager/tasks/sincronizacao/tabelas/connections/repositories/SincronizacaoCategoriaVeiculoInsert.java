package com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.categoriaveiculo.CategoriaVeiculoRepositoryDAO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class SincronizacaoCategoriaVeiculoInsert implements ISincronizacaoInsert<CategoriaVeiculo> {

	 private ManagerLog managerLog;

	    public SincronizacaoCategoriaVeiculoInsert() throws Exception {
	         this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	    }

	    @Override
	    public void insert(List<CategoriaVeiculo> objects, CustomConnection connection) throws Exception {
	         try {
	                connection.initConnection(true);
	                int inserts = 0;
	                int updates = 0;
	                CategoriaVeiculoRepositoryDAO categoriaVeiculoRepositoryDAO = new CategoriaVeiculoRepositoryDAO();
	                for (CategoriaVeiculo objeto : objects) {
	                	CategoriaVeiculo existingCategoriaVeiculo = categoriaVeiculoRepositoryDAO.get(objeto.getCdCategoria());
	                    if (existingCategoriaVeiculo == null) {
	                        categoriaVeiculoRepositoryDAO.insert(objeto, connection);
	                        inserts++;
	                        managerLog.info("Categoria de Veículo inserida --> " + " cdCategoriaVeiculo: " + objeto.getCdCategoria() + " / " + "nmCategoria: " + objeto.getNmCategoria(), new GregorianCalendar().getTime().toString());
	                    } else {
	                        if (!existingCategoriaVeiculo.equals(objeto)) {
	                            categoriaVeiculoRepositoryDAO.update(objeto, connection);
	                            updates++;
	                            managerLog.info("Categoria de Veículo atualizada --> " + " cdCategoriaVeiculo: " + objeto.getCdCategoria() + " / " + "nmCategoria: " + objeto.getNmCategoria(), new GregorianCalendar().getTime().toString());
	                        }
	                    }
	                }
	                connection.finishConnection();
	                managerLog.info("Total de categorias de veículo inseridas: " + inserts, new GregorianCalendar().getTime().toString());
	                managerLog.info("Total de categorias de veículo atualizadas: " + updates, new GregorianCalendar().getTime().toString());
	            } finally {
	                connection.closeConnection();
	            }
	    }

}
