package com.tivic.manager.mob.ait;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.ait.relatorios.PageResponseRelatorioAIT;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitDTO;

public class RelatorioAitDTOListBuilder {
	
    private int total;
    private List<RelatorioAitDTO> aits;
    private double totalValor;
    
    public RelatorioAitDTOListBuilder(List<RelatorioAitDTO> listAit, int total, double totalValor) throws SQLException {
        this.aits = new ArrayList<RelatorioAitDTO>(listAit);
        this.total = total;
        this.totalValor = totalValor;
    }
    
    public PageResponseRelatorioAIT<RelatorioAitDTO> build() throws IllegalArgumentException, Exception {        
        return new PageResponseRelatorioAIT<RelatorioAitDTO>(aits, this.total, this.totalValor);	
    }
    
}

