package com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.CidadeRepositoryDAO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class SincronizacaoCidadeInsert implements ISincronizacaoInsert<Cidade> {


    private ManagerLog managerLog;

    public SincronizacaoCidadeInsert() throws Exception {
         this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
    }

    @Override
    public void insert(List<Cidade> objects, CustomConnection connection) throws Exception {
         try {
                connection.initConnection(true);
                int inserts = 0;
                int updates = 0;
                CidadeRepositoryDAO cidadeRepositoryDAO = new CidadeRepositoryDAO();
                for (Cidade objeto : objects) {
                    Cidade existingCidade = cidadeRepositoryDAO.get(objeto.getCdCidade());
                    if (existingCidade == null) {
                        cidadeRepositoryDAO.insert(objeto, connection);
                        inserts++;
                        managerLog.info("Cidade inserida --> " + " cdCidade: " + objeto.getCdCidade() + " / " + "nmCidade: " + objeto.getNmCidade(), new GregorianCalendar().getTime().toString());
                    } else {             
                        if (!existingCidade.equals(objeto)) {
                            cidadeRepositoryDAO.update(objeto, connection);
                            updates++;
                            managerLog.info("Cidade atualizada --> " + " cdCidade: " + objeto.getCdCidade() + " / " + "nmCidade: " + objeto.getNmCidade(), new GregorianCalendar().getTime().toString());
                        }
                    }
                }
                connection.finishConnection();
                managerLog.info("Total de cidades inseridas: " + inserts, new GregorianCalendar().getTime().toString());
                managerLog.info("Total de cidades atualizadas: " + updates, new GregorianCalendar().getTime().toString());
            } finally {
                connection.closeConnection();
            }
    }
}
