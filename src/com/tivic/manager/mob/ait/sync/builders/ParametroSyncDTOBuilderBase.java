package com.tivic.manager.mob.ait.sync.builders;

import java.util.List;
import com.tivic.manager.mob.ait.sync.entities.ParametroSyncDTO;
import java.util.ArrayList;

public abstract class ParametroSyncDTOBuilderBase {

    protected List<ParametroSyncDTO> parametroSyncList;

    public ParametroSyncDTOBuilderBase() throws Exception {
        this.parametroSyncList = new ArrayList<>();
    }

    protected abstract void setParametroSync() throws Exception;

    public List<ParametroSyncDTO> build() throws Exception {
    	setParametroSync();
        return parametroSyncList;
    }
}
