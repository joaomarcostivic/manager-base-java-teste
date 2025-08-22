package com.tivic.manager.mob.processamento;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.concurrent.status.ProcessorStatus;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ProcessamentoResultDTO {
	private final int MAX_ERRORS_LIST_SIZE = 1000;
	private final int TIMER_DURATION = 20 * 60;
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledFuture;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtUltimoProcessamento;
	private List<EventoErroDTO> eventoErros;
	private ProcessorStatus processorStatus;
	
	public GregorianCalendar getDtUltimoProcessamento() {
		return dtUltimoProcessamento;
	}
	
	public void setDtUltimoProcessamento(GregorianCalendar dtUltimoProcessamento) {
		this.dtUltimoProcessamento = dtUltimoProcessamento;
	}
	
	public List<EventoErroDTO> getEventoErros() {
		return eventoErros;
	}

	public void setEventoErros(List<EventoErroDTO> eventoErros) {
		this.eventoErros = eventoErros;
	}

	public ProcessorStatus getProcessorStatus() {
		return processorStatus;
	}

	public void setProcessorStatus(ProcessorStatus processorStatus) {
		this.processorStatus = processorStatus;
	}

	public void addEventoErro(Integer cdEvento, String mensagem) {
		if(eventoErros.size() >= MAX_ERRORS_LIST_SIZE) {
			return;
		}
		
		boolean alreadyExists = eventoErros.stream().anyMatch(evento -> evento.getCdEvento() == cdEvento);
		if(!alreadyExists) {
			this.eventoErros.add(new EventoErroDTO(cdEvento, mensagem));
		}
	}
	
	public void startProcessamento() {
		this.dtUltimoProcessamento = new GregorianCalendar();
		this.eventoErros = new ArrayList<>();
		this.processorStatus = new ProcessorStatus();
		
		if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
        }

        scheduledFuture = scheduler.schedule(() -> resetState(), TIMER_DURATION, TimeUnit.SECONDS);
	}
	
    private void resetState() {
        this.dtUltimoProcessamento = null;
        this.eventoErros = null;
        this.processorStatus = null;
    }
	
	public void endProcessamento(int qtdTotal) {
		this.dtUltimoProcessamento = new GregorianCalendar();
		this.processorStatus.setQtdTotal(qtdTotal);
		this.processorStatus.setQtdProcessados(new AtomicInteger(qtdTotal - eventoErros.size()));
		this.processorStatus.setQtdNaoProcessados(new AtomicInteger(eventoErros.size()));
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
