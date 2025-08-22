package com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.marcamodelo.MarcaModeloRepository;
import com.tivic.manager.str.MarcaModeloOldRepository;
import com.tivic.manager.str.MarcaModeloStrRepository;
import com.tivic.manager.tasks.sincronizacao.tabelas.factories.ISincronizacaoMarcaModelo;
import com.tivic.manager.tasks.sincronizacao.tabelas.factories.SincronizacaoMarcaModeloFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class SincronizacaoMarcaModeloInsert implements ISincronizacaoInsert<MarcaModelo> {
    boolean lgBaseAntiga;
    private ManagerLog managerLog;
    private MarcaModeloOldRepository marcaModeloOldRepository;
    private MarcaModeloStrRepository marcaModeloStrRepository;
    private MarcaModeloRepository marcaModeloRepository;

    public SincronizacaoMarcaModeloInsert() throws Exception {
        this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
        this.lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
        this.marcaModeloOldRepository = (MarcaModeloOldRepository) BeansFactory.get(MarcaModeloOldRepository.class);
        this.marcaModeloStrRepository = (MarcaModeloStrRepository) BeansFactory.get(MarcaModeloStrRepository.class);
        this.marcaModeloRepository = (MarcaModeloRepository) BeansFactory.get(MarcaModeloRepository.class);
    }

    @Override
    public void insert(List<MarcaModelo> marcaModeloList, CustomConnection customConnection) throws Exception {
        if (this.lgBaseAntiga) {
            managerLog.info("Iniciando inserção em Marca Modelo (FTA)", new GregorianCalendar().getTime().toString());
            insertMarcaModelo(marcaModeloList, customConnection, marcaModeloRepository);

            managerLog.info("Iniciando inserção em Marca Modelo (STR)", new GregorianCalendar().getTime().toString());
            insertMarcaModelo(marcaModeloList, customConnection, marcaModeloStrRepository);

            managerLog.info("Iniciando inserção em Marca Modelo (Base Antiga)", new GregorianCalendar().getTime().toString());
            insertMarcaModelo(marcaModeloList, customConnection, marcaModeloOldRepository);
            return;
        }

        managerLog.info("Iniciando inserção em Marca Modelo (FTA)", new GregorianCalendar().getTime().toString());
        insertMarcaModelo(marcaModeloList, customConnection, marcaModeloRepository);
    }

    private void insertMarcaModelo(List<MarcaModelo> marcaModeloList, CustomConnection customConnection, Object repository) throws Exception {
        ISincronizacaoMarcaModelo sincronizacao = new SincronizacaoMarcaModeloFactory().getStrategy(repository);
        try {
            customConnection.initConnection(true);
            int inserts = 0;
            int updates = 0;
            for (MarcaModelo marcaModelo : marcaModeloList) {
                MarcaModelo existingMarcaModelo = sincronizacao.get(marcaModelo.getCdMarca(), customConnection);
                if (existingMarcaModelo == null) {
                    sincronizacao.insert(marcaModelo, customConnection);
                    inserts++;
                    managerLog.info("Marca Modelo inserida --> cdMarca: " + marcaModelo.getCdMarca() + " / nmMarca: " + marcaModelo.getNmMarca(), new GregorianCalendar().getTime().toString());
                } else if (!existingMarcaModelo.equals(marcaModelo)) {
                        sincronizacao.update(marcaModelo, customConnection);
                        updates++;
                        managerLog.info("Marca Modelo atualizada --> cdMarca: " + marcaModelo.getCdMarca() + " / nmMarca: " + marcaModelo.getNmMarca(), new GregorianCalendar().getTime().toString());
                }
            }
            customConnection.finishConnection();
            managerLog.info("Total de MarcaModelos inseridas: " + inserts, new GregorianCalendar().getTime().toString());
            managerLog.info("Total de MarcaModelos atualizadas: " + updates, new GregorianCalendar().getTime().toString());
        } finally {
            customConnection.closeConnection();
        }
    }
    
}
