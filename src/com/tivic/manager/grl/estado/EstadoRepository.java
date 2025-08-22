package com.tivic.manager.grl.estado;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Estado;
import com.tivic.sol.connection.CustomConnection;

import sol.dao.ItemComparator;

public interface EstadoRepository {
    public Estado insert(Estado estado, CustomConnection customConnection) throws Exception;
    public Estado update(Estado estado, CustomConnection customConnection) throws Exception;
    public Estado get(int cdEstado) throws Exception;
    public Estado get(int cdEstado, CustomConnection customConnection) throws Exception;
    public List<Estado> getAll() throws Exception;
    public List<Estado> getAll(CustomConnection customConnection) throws Exception;
    public List<Estado> find(ArrayList<ItemComparator> criterios) throws Exception;
    public List<Estado> find(ArrayList<ItemComparator> criterios, CustomConnection customConnection) throws Exception;
}