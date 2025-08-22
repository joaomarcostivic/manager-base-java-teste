package com.tivic.manager.triagem.webclients.estacionamento_digital.apis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.triagem.builders.NotificacaoEstacionamentoDigitalBuilder;
import com.tivic.manager.triagem.dtos.ImagemNotificacaoDTO;
import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;
import com.tivic.manager.triagem.webclients.estacionamento_digital.requestes.EstacionamentoDigitalApiRequest;
import com.tivic.manager.triagem.webclients.estacionamento_digital.responses.Imagens;
import com.tivic.manager.triagem.webclients.estacionamento_digital.responses.NotificacaoPareAzulResponse;

import io.jsonwebtoken.io.IOException;

public class PareAzulAPI implements IEstacionamentoDigitalAPI {
	
    public List<NotificacaoEstacionamentoDigitalDTO> buscarNotificacoes() throws Exception {
        return buscarNotificacoes(new HashMap<>());
    }

    public List<NotificacaoEstacionamentoDigitalDTO> buscarNotificacoes(HashMap<String, String> params) throws Exception {
            EstacionamentoDigitalApiRequest<NotificacaoPareAzulResponse> request = 
            		new EstacionamentoDigitalApiRequest<NotificacaoPareAzulResponse>();
            request.setEndPoint("v4/veiculos/avisos-irregularidades");
    		request.setParams(params);
    		List<NotificacaoPareAzulResponse> notificacaoResponseList = request.getNotificacoes();
    		return montarListaNotificacao(notificacaoResponseList);
    }

    private List<NotificacaoEstacionamentoDigitalDTO> montarListaNotificacao(List<NotificacaoPareAzulResponse> notificacaoResponseList) throws IOException, java.io.IOException {
		if (notificacaoResponseList.isEmpty()) {
			throw new NoContentException("Nenhuma notificação encontrada.");
		}
		List<NotificacaoEstacionamentoDigitalDTO> notificacaoEstacionamentoDigitalList = new ArrayList<NotificacaoEstacionamentoDigitalDTO>();
		for (NotificacaoPareAzulResponse notificacaoPareAzulResponse: notificacaoResponseList) {
			NotificacaoEstacionamentoDigitalDTO notificacaoEstacionamentoDigitalDTO = new NotificacaoEstacionamentoDigitalBuilder()
	                .nrNotificacao(String.valueOf(notificacaoPareAzulResponse.getNotificacaoId()))
	                .dtNotificacao(notificacaoPareAzulResponse.getDataCriacao())
	                .dsNotificacao(notificacaoPareAzulResponse.getMotivoNotificacao())
	                .nrPlaca(notificacaoPareAzulResponse.getVeiculoPlaca())
	                .nmRuaNotificacao(notificacaoPareAzulResponse.getEnderecoLogradouro())
	                .nrImovelReferencia(notificacaoPareAzulResponse.getEnderecoNumero())
	                .imagemNotificacaoDTOList(pegarImagens(notificacaoPareAzulResponse.getImagens()))
					.build();
			notificacaoEstacionamentoDigitalList.add(notificacaoEstacionamentoDigitalDTO);
		}
        return notificacaoEstacionamentoDigitalList;
    }
    
    private List<ImagemNotificacaoDTO> pegarImagens(List<Imagens> imagenList) throws IOException, java.io.IOException {
    	if (imagenList.isEmpty()) {
    		return new ArrayList<ImagemNotificacaoDTO>();
    	}
        EstacionamentoDigitalApiRequest<NotificacaoPareAzulResponse> request = 
        		new EstacionamentoDigitalApiRequest<NotificacaoPareAzulResponse>();
        List<ImagemNotificacaoDTO> imagemNotificacaList =  new ArrayList<ImagemNotificacaoDTO>();
        for(Imagens img : imagenList) {
        	if (img.getUrlImagem() != null) {
        		ImagemNotificacaoDTO imagemNotificacaoDTO = new ImagemNotificacaoDTO();
        		imagemNotificacaoDTO.setBlbImagem(request.getImagem(img.getUrlImagem()));
        		imagemNotificacaList.add(imagemNotificacaoDTO);
        	}
        }
    	return imagemNotificacaList;
    }

}
