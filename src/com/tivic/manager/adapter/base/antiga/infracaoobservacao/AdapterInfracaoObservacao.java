package com.tivic.manager.adapter.base.antiga.infracaoobservacao;

import java.io.IOException;
import javax.swing.text.BadLocationException;
import com.tivic.manager.mob.InfracaoObservacao;

public class AdapterInfracaoObservacao {
    
    public InfracaoObservacaoOld toBaseAntiga(InfracaoObservacao infracaoObservacao) {
        return new InfracaoObservacaoOldBuilder()
                .setCdObservacao(infracaoObservacao.getCdObservacao())
                .setCodInfracao(infracaoObservacao.getCdInfracao())
                .setNrObservacao(infracaoObservacao.getNrObservacao())
                .setNmObservacao(infracaoObservacao.getNmObservacao())
                .setTxtObservacao(infracaoObservacao.getTxtObservacao())
                .setStObservacao(infracaoObservacao.getStObservacao())
                .build();
    }
    
    public InfracaoObservacao toBaseNova(InfracaoObservacaoOld infracaoObservacaoOld) 
            throws IOException, BadLocationException {
        return new InfracaoObservacaoBuilder()
                .setCdObservacao(infracaoObservacaoOld.getCdObservacao())
                .setCdInfracao(infracaoObservacaoOld.getCodInfracao())
                .setNrObservacao(infracaoObservacaoOld.getNrObservacao())
                .setNmObservacao(infracaoObservacaoOld.getNmObservacao())
                .setTxtObservacao(infracaoObservacaoOld.getTxtObservacao())
                .setStObservacao(infracaoObservacaoOld.getStObservacao())
                .build();
    }
}
