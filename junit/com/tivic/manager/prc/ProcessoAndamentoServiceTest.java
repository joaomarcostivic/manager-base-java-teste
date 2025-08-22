package com.tivic.manager.prc;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemServices;

import junit.framework.TestCase;
import sol.util.Result;

public class ProcessoAndamentoServiceTest extends TestCase {
	
	private static final int CD_PROCESSO = 100;
	private static final int CD_ANDAMENTO = 0;
	private static final int CD_TIPO_ANDAMENTO = 12;
	
	private static final int CD_AGENDA_ITEM = 0;	
	private static final int CD_TIPO_PRAZO_AUDIENCIA = 166;
	private static final int CD_TIPO_PRAZO = 99;
	
	private static final int CD_PESSOA = 10414;
	private static final int CD_USUARIO = 10414;
	private static final int CD_EMPRESA = 3;
	
	//save
	private ProcessoAndamento andamento = null;
	private ProcessoArquivo arquivo = null;
	private AgendaItem audiencia = null;
	private ArrayList<AgendaItem> prazos = null;
	
	protected void setUp() {
		andamento = new ProcessoAndamento();
		andamento.setCdAndamento(CD_ANDAMENTO);
		andamento.setCdProcesso(CD_PROCESSO);
		andamento.setCdTipoAndamento(CD_TIPO_ANDAMENTO);
		andamento.setTpVisibilidade(ProcessoAndamentoServices.TP_VISIBILIDADE_PUBLICO);
		andamento.setDtLancamento(new GregorianCalendar());
		andamento.setDtAndamento(new GregorianCalendar());
		
		arquivo = new ProcessoArquivo(0, CD_PROCESSO, 0, "processoArquivo"+System.currentTimeMillis()+".txt", 
				"processoArquivo"+System.currentTimeMillis(), new GregorianCalendar(), 
				null, 0, 1, 0, null, null, 0, null, 0);
		
		audiencia = new AgendaItem();
		audiencia.setCdAgendaItem(CD_AGENDA_ITEM);
		audiencia.setCdProcesso(CD_PROCESSO);
		audiencia.setCdTipoPrazo(CD_TIPO_PRAZO_AUDIENCIA);
		audiencia.setCdPessoa(CD_PESSOA);
		audiencia.setCdUsuario(CD_USUARIO);
		audiencia.setCdEmpresa(CD_EMPRESA);
		audiencia.setDtInicial(new GregorianCalendar());
		audiencia.setDtFinal(new GregorianCalendar());
		audiencia.setDtLancamento(new GregorianCalendar());
		audiencia.setStAgendaItem(AgendaItemServices.ST_AGENDA_A_CUMPRIR);
		audiencia.setDsDetalhe("Test JUnit");
			
		prazos = new ArrayList<>();
		for(int i=0; i<3; i++) {
			AgendaItem prazo = new AgendaItem();
			prazo.setCdAgendaItem(0);
			prazo.setCdProcesso(CD_PROCESSO);
			prazo.setCdTipoPrazo(CD_TIPO_PRAZO);
			prazo.setCdPessoa(CD_PESSOA);
			prazo.setCdUsuario(CD_USUARIO);
			prazo.setCdEmpresa(CD_EMPRESA);
			prazo.setDtInicial(new GregorianCalendar());
			prazo.setDtFinal(new GregorianCalendar());
			prazo.setDtLancamento(new GregorianCalendar());
			prazo.setStAgendaItem(AgendaItemServices.ST_AGENDA_A_CUMPRIR);
			prazo.setDsDetalhe("Test JUnit");
			prazos.add(prazo);
		}
	
	}
	
	public void testSaveProcessoAndamento() {
		Result r = ProcessoAndamentoServices.save(andamento, null, null, null, null);
		assertEquals(1, r.getCode());
	}
	
	public void testSaveProcessoAndamentoProcessoArquivo() {
		Result r = ProcessoAndamentoServices.save(andamento, arquivo, null, null);
		assertEquals(1, r.getCode());
	}
	
	public void testSaveProcessoAndamentoProcessoArquivoAgendaItem() {
		Result r = ProcessoAndamentoServices.save(andamento, arquivo, audiencia, null);
		assertEquals(1, r.getCode());
	}

	public void testSaveProcessoAndamentoProcessoArquivoAgendaItemArrayListOfAgendaItem() {
		Result r = ProcessoAndamentoServices.save(andamento, arquivo, audiencia, prazos);
		assertEquals(1, r.getCode());
	}
}
