package com.tivic.manager.tasks.sincronizacao.tabelas.factories;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories.MarcaModeloBaseAntigaInsert;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories.MarcaModeloFtaInsert;
import com.tivic.manager.tasks.sincronizacao.tabelas.connections.repositories.MarcaModeloStrInsert;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class SincronizacaoMarcaModeloFactory {
    
    private static final Map<Class<?>, Class<? extends ISincronizacaoMarcaModelo>> map = new HashMap<>();

    static {
        map.put(com.tivic.manager.str.MarcaModeloStrRepositoryDAO.class, MarcaModeloStrInsert.class);
        map.put(com.tivic.manager.str.MarcaModeloOldRepositoryDAO.class, MarcaModeloBaseAntigaInsert.class);
        map.put(com.tivic.manager.fta.marcamodelo.MarcaModeloRepositoryDAO.class, MarcaModeloFtaInsert.class);
    }

    public ISincronizacaoMarcaModelo getStrategy(Object repository) throws ValidacaoException, Exception {
        Class<? extends ISincronizacaoMarcaModelo> classe = map.get(repository.getClass());
        
        if (classe == null) {
            throw new ValidacaoException("Tipo de repositório desconhecido: " + repository.getClass());
        }
        return classe.newInstance();
    }
}
