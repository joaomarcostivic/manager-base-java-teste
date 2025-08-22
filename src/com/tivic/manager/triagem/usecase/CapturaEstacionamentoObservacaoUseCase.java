package com.tivic.manager.triagem.usecase;

import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.InfracaoObservacao;
import com.tivic.manager.mob.infracao.IInfracaoService;
import com.tivic.manager.mob.infracao.observacao.InfracaoObservacaoRepository;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.triagem.dtos.InfracaoObservacaoDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CapturaEstacionamentoObservacaoUseCase {

    private final ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
    private final InfracaoObservacaoRepository infracaoObservacaoRepository;
    private final IParametroRepository parametroRepository;
    private final IInfracaoService infracaoService;

    public CapturaEstacionamentoObservacaoUseCase() throws Exception {
        conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
        infracaoService = (IInfracaoService) BeansFactory.get(IInfracaoService.class);
        infracaoObservacaoRepository = conversorBaseAntigaNovaFactory.getInfracaoObservacaoRepository();
        parametroRepository = conversorBaseAntigaNovaFactory.getParametroRepository();
    }
    
    public List<InfracaoObservacaoDTO> execute() throws Exception {
        CustomConnection customConnection = new CustomConnection();
        try {
            customConnection.initConnection(true);
            List<InfracaoObservacaoDTO> observacaoDTOList = findObservacaoDTOList(customConnection);
            customConnection.finishConnection();
            return observacaoDTOList;
        } finally {
            customConnection.closeConnection();
        }        
    }
    
    public List<InfracaoObservacaoDTO> findObservacaoDTOList(CustomConnection customConnection) throws Exception {
        int codDetran = parametroRepository.getValorOfParametroAsInt("MOB_NR_INFRACAO_ESTACIONAMENTO_DIGITAL", customConnection);
        int cdInfracao = infracaoService.getByCodDetran(codDetran, customConnection).getCdInfracao();
        
        List<InfracaoObservacao> observacaoList = infracaoObservacaoRepository.getObservacaoByCdInfracao(cdInfracao, customConnection);
        
        return observacaoList.stream()
                .map(this::convertToDTO)
                .peek(dto -> dto.setNrCodDetran(String.valueOf(codDetran)))
                .collect(Collectors.toList());
    }
    
    private InfracaoObservacaoDTO convertToDTO(InfracaoObservacao observacao) {
        InfracaoObservacaoDTO dto = new InfracaoObservacaoDTO();
        dto.setCdInfracao(observacao.getCdInfracao());
        dto.setCdObservacao(observacao.getCdObservacao());
        dto.setNrObservacao(observacao.getNrObservacao());
        dto.setNmObservacao(observacao.getNmObservacao());
        dto.setTxtObservacao(observacao.getTxtObservacao());
        return dto;
    }
}