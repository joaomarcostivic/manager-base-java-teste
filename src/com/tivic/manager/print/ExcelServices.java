package com.tivic.manager.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.VeiculoServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.FiltroCampo;
import com.tivic.manager.grl.FiltroCampoDAO;
import com.tivic.manager.grl.FiltroCampoServices;
import com.tivic.manager.grl.FiltroRelatorio;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.mob.Concessao;
import com.tivic.manager.mob.ConcessaoServices;
import com.tivic.manager.mob.ConcessaoVeiculoServices;
import com.tivic.manager.mob.VistoriaServices;
import com.tivic.manager.prc.ProcessoAndamento;
import com.tivic.manager.prc.ProcessoAndamentoServices;
import com.tivic.manager.prc.ProcessoServices;
import com.tivic.manager.prc.TipoPrazoServices;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import net.sf.jasperreports.engine.export.oasis.CellStyle;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class ExcelServices {
	
	public static final short EXCEL_COLUMN_WIDTH_FACTOR = 256;
	public static final int UNIT_OFFSET_LENGTH = 7;
	public static final int[] UNIT_OFFSET_MAP = new int[] { 0, 36, 73, 109, 146, 182, 219 };
	
	/**
	 * @author Alvaro
	 * @param rsm
	 * @tpConta Contas a Pagar - 1 e Contas a Receber = 0
	 * @return
	 */
	public static Result gerarRelatorioDetalhamentoContaXls(ResultSetMap rsm, int tpConta){
		ArrayList<Object[]> dados = new ArrayList<Object[]>();
		DecimalFormat df = new DecimalFormat("########0.00"); 
		
		dados.add(new Object[] {"Situação", (tpConta==1)?"Favorecido":"Sacado", "Processo", "Conta", "Tipo de Documento", "N° Documento", "Emissão", "Vencimento",
								(tpConta==1)?"Paga em":"Recebida em","Valor Conta(R$)",(tpConta==1)?"Desconto":"Abatimento", "Acréscimo", (tpConta==1)?"Pago":"Recebido",(tpConta==1)?"A Pagar":"A Receber",
								"Histórico","Cód. Categoria", "Classificação", "Valor Cat(R$)", "Centro de Custo"});
		while(rsm.next()){
			dados.add(new Object[]{
					(rsm.getString("NM_ST_CONTA")!= null)?rsm.getString("NM_ST_CONTA"):"",
					(rsm.getString("NM_PESSOA")!= null)?rsm.getString("NM_PESSOA"):"",
					(rsm.getString("NR_PROCESSO")!= null)?rsm.getString("NR_PROCESSO"):"",
					(rsm.getString("NM_CONTA")!= null)?rsm.getString("NM_CONTA"):"",
					(rsm.getString("NM_TIPO_DOCUMENTO")!= null)?rsm.getString("NM_TIPO_DOCUMENTO"):"",
					(rsm.getString("NR_DOCUMENTO")!= null)?rsm.getString("NR_DOCUMENTO"):"",
					(rsm.getString("DT_EMISSAO")!= null)?rsm.getString("DT_EMISSAO"):"",
					(rsm.getString("DT_VENCIMENTO")!= null)?rsm.getString("DT_VENCIMENTO"):"",
					(tpConta==1)?rsm.getString("DT_PAGAMENTO"):rsm.getString("DT_RECEBIMENTO"),
					Double.parseDouble(df.format(rsm.getDouble("VL_CONTA")).replace(",", ".")),
					Double.parseDouble(df.format(rsm.getDouble("VL_ABATIMENTO")).replace(",", ".")),
					Double.parseDouble(df.format(rsm.getDouble("VL_ACRESCIMO")).replace(",", ".")),
					Double.parseDouble(df.format((tpConta==1)?rsm.getDouble("VL_PAGO"):rsm.getDouble("VL_RECEBIDO")).replace(",", ".")),
					Double.parseDouble(df.format((tpConta==1)?rsm.getDouble("VL_APAGAR"):rsm.getDouble("VL_ARECEBER")).replace(",", ".")),
					(rsm.getString("DS_HISTORICO")!= null)?rsm.getString("DS_HISTORICO"):"",
					(rsm.getString("CD_CATEGORIA_ECONOMICA")!= null)?rsm.getString("CD_CATEGORIA_ECONOMICA"):"",
					(rsm.getString("DS_CATEGORIA_ECONOMICA")!= null)?rsm.getString("DS_CATEGORIA_ECONOMICA"):"",
					Double.parseDouble(df.format(rsm.getDouble("VL_CONTA_CATEGORIA")).replace(",", ".")),
					(rsm.getString("NM_CENTRO_CUSTO")!= null)?rsm.getString("NM_CENTRO_CUSTO").trim():""
				}
			);
		}
		Integer[] columnsWidths = new Integer[] {80, 200, 120, 200, 150, 80, 90, 90, 80, 100, 80, 80, 80, 100, 80, 80, 200, 80, 200};
		String titulo ="Relatório de Detalhamento de Contas a "+( (tpConta==1)?"Pagar":"Receber" );
		String fileName ="DETALHAMENTO_CONTA_"+((tpConta==1)?"PAGAR":"RECEBER" )+ Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
		return gerarBasicXls(dados, columnsWidths, titulo, fileName);
	}
	

	/**
	 * Gera um relatório de acordo com o tipo de agenda
	 * 
	 * @param rsm
	 * @param empresa
	 * @param tpAgendaItem
	 * @return
	 */
	public static Result gerarAgendaXls(ResultSetMap rsm, Empresa empresa, int tpAgendaItem) {
		switch (tpAgendaItem) {
			case TipoPrazoServices.TP_AUDIENCIA:
				return gerarAudienciaXls(rsm, empresa);
			case TipoPrazoServices.TP_PRAZO:
				return gerarPrazoXls(rsm, empresa);
			case TipoPrazoServices.TP_TAREFA:
				return gerarTarefaXls(rsm, empresa);
			case TipoPrazoServices.TP_DILIGENCIA:
				return gerarDiligenciaXls(rsm, empresa);
			default:
				return gerarAgendaXls(rsm, empresa);
		}
	}
	
	public static Result gerarAudienciaXls(ResultSetMap rsm, Empresa empresa) {
		ArrayList<Object[]> dados = new ArrayList<Object[]>();
		dados.add(new Object[] {"Data", "Hora", "Compromisso", "Cliente", "Adverso", "Situação", "Prazo", "Juízo", "Comarca", "UF",
				"Processo Nº", "Grupo de Trabalho", "Pasta Nº", "Grupo de Processo", "Valor(R$) da Causa", "F/E", "Observação", "Responsável"});
		
		while(rsm.next()) {
			
			String nmSituacao = "";
			switch(rsm.getInt("ST_AGENDA_ITEM")){
				case 0 : nmSituacao = (rsm.getInt("TP_AGENDA_ITEM")==3?"Não repassado":"A cumprir"); break;
				case 1 : nmSituacao = "Cumprido"; break;
				case 2 : nmSituacao = "Cancelado"; break;
				case 3 : nmSituacao = "Vencido"; break;
				case 4 : nmSituacao = "A auditar"; break;
				case 5 : nmSituacao = "Repassado"; break;
			}
			
			GregorianCalendar dtInicial = new GregorianCalendar();
			if(rsm.getObject("DT_INICIAL")!=null)
				dtInicial.setTimeInMillis(((Date)rsm.getObject("DT_INICIAL")).getTime());
			String dsData = (rsm.getObject("DT_INICIAL")!=null) ? Util.formatDate(dtInicial, "dd/MM/yyyy").toUpperCase() : "";
			String dsHora = Util.formatDate(dtInicial, "HH:mm").toUpperCase();
			
			GregorianCalendar dtCumprimento = new GregorianCalendar();
			if(rsm.getObject("DT_REALIZACAO")!=null)
				dtCumprimento.setTimeInMillis(((Date)rsm.getObject("DT_REALIZACAO")).getTime());
			
			dados.add(new Object[] {
				dsData, dsHora, //1, 2
				rsm.getString("NM_TIPO_PRAZO"), //3
				rsm.getString("NM_CLIENTE"), //4
				rsm.getString("NM_ADVERSO"), //5
				nmSituacao, //6
				(dtCumprimento!=null ? Util.getQuantidadeDias(dtCumprimento, rsm.getGregorianCalendar("DT_INICIAL"))+" DIAS" : ""),
				(rsm.getString("NR_JUIZO")!=null && !rsm.getString("NR_JUIZO").equals("") ? rsm.getString("NR_JUIZO")+" " : "" ) +
				 (rsm.getString("SG_JUIZO")!=null && !rsm.getString("SG_JUIZO").equals("") ? rsm.getString("SG_JUIZO") : (rsm.getString("NM_JUIZO")!=null && !rsm.getString("NM_JUIZO").equals("") ? rsm.getString("NM_JUIZO") : "") ),
				(rsm.getString("NM_CIDADE")!=null && !rsm.getString("NM_CIDADE").equals("") ? rsm.getString("NM_CIDADE") : "" ), 
				(rsm.getString("SG_ESTADO")!=null && !rsm.getString("SG_ESTADO").equals("") ? rsm.getString("SG_ESTADO") : "" ), 
				rsm.getString("NR_PROCESSO"), //10
				rsm.getString("NM_GRUPO_TRABALHO"), 
				rsm.getString("NM_CONTEINER3"), //11
				rsm.getString("NM_GRUPO_PROCESSO"), //12
				Util.formatNumber(rsm.getDouble("VL_PROCESSO")),
				rsm.getInt("TP_AUTOS") == 0 ? "F" : "E", //13
				rsm.getString("DS_DETALHE"), //14
				rsm.getString("NM_RESPONSAVEL")//15
			});
		}
		
		String titulo = "Pauta de Compromissos - " + empresa.getNmPessoa();
		String fileName = "PAUTA_COMPROMISSO_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
		Integer[] columnsWidths = new Integer[] {
				51, 33, 100, 100, 100, 
				100, 100, 100, 100, 100, 
				100, 100, 100, 100, 100, 
				100, 100, 100};
		
		return gerarBasicXls(dados, columnsWidths, titulo, fileName);
	}
	
	public static Result gerarPrazoXls(ResultSetMap rsm, Empresa empresa) {
		return gerarAudienciaXls(rsm, empresa);
	}
	
	public static Result gerarTarefaXls(ResultSetMap rsm, Empresa empresa) {
		return gerarAudienciaXls(rsm, empresa);
	}
	
	public static Result gerarDiligenciaXls(ResultSetMap rsm, Empresa empresa) {
		ArrayList<Object[]> dados = new ArrayList<Object[]>();
		dados.add(new Object[] {"Dia", "Data", "Hora", "Compromisso", "Cliente", "Adverso", "Juizo", "Comarca", "UF",
				"Processo Nº", "Grupo de Trabalho", "Valor(R$) do Serviço", "F/E", "Responsável", "Preposto", "Observação"});
		
		while(rsm.next()) {
			
			GregorianCalendar dtInicial = new GregorianCalendar();
			if(rsm.getObject("DT_INICIAL")!=null)
				dtInicial.setTimeInMillis(((Date)rsm.getObject("DT_INICIAL")).getTime());

			GregorianCalendar dtCumprimento = new GregorianCalendar();
			if(rsm.getObject("DT_REALIZACAO")!=null)
				dtCumprimento.setTimeInMillis(((Date)rsm.getObject("DT_REALIZACAO")).getTime());
			
			String dsDia = Util.formatDate(dtInicial, "EEE").toUpperCase();
			String dsData = (rsm.getObject("DT_INICIAL")!=null) ? Util.formatDate(dtInicial, "dd/MM/yyyy").toUpperCase() : "";
			String dsHora = Util.formatDate(dtInicial, "HH:mm").toUpperCase();
			
			dados.add(new Object[] {
				dsDia, dsData, dsHora, //1, 2, 3
				rsm.getString("NM_TIPO_PRAZO"), //4
				rsm.getString("NM_CLIENTE"), //5
				rsm.getString("NM_ADVERSO"), //6
				(rsm.getString("NR_JUIZO")!=null && !rsm.getString("NR_JUIZO").equals("") ? rsm.getString("NR_JUIZO")+" " : "" ) +
				 (rsm.getString("SG_JUIZO")!=null && !rsm.getString("SG_JUIZO").equals("") ? rsm.getString("SG_JUIZO") : (rsm.getString("NM_JUIZO")!=null && !rsm.getString("NM_JUIZO").equals("") ? rsm.getString("NM_JUIZO") : "") ),
				(rsm.getString("NM_CIDADE")!=null && !rsm.getString("NM_CIDADE").equals("") ? rsm.getString("NM_CIDADE") : "" ), //8
				(rsm.getString("SG_ESTADO")!=null && !rsm.getString("SG_ESTADO").equals("") ? rsm.getString("SG_ESTADO") : "" ), //9
				rsm.getString("NR_PROCESSO"), //10
				rsm.getString("NM_GRUPO_TRABALHO"),
				Util.formatNumber(rsm.getDouble("VL_SERVICO")), //11
				rsm.getInt("TP_AUTOS") == 0 ? "F" : "E", //12
				rsm.getString("NM_RESPONSAVEL"), //13
				(rsm.getInt("LG_PREPOSTO")==0?"Não":"Sim"), //14
				rsm.getString("DS_DETALHE")
			});
		}
		
		String titulo = "Pauta de Compromissos - " + empresa.getNmPessoa();
		String fileName = "PAUTA_COMPROMISSO_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
		Integer[] columnsWidths = new Integer[] {
			25, 51, 33, 103, 120, 
			120, 90, 120, 51, 138, 
			80, 40, 117, 117, 100, 
			26, 117, 61, 100, 60
		};
		
		return gerarBasicXls(dados, columnsWidths, titulo, fileName);
	}
	
	public static Result gerarAgendaXls(ResultSetMap rsm, Empresa empresa) {
		
		ArrayList<Object[]> dados = new ArrayList<Object[]>();
		dados.add(new Object[] {"Dia", "Data", "Hora", "Compromisso", "Posição", "Cliente", "Adverso", "Situação", "Dt. Cumprimento", "Prazo", "Juízo", "Comarca", "UF",
				"Processo Nº", "Pasta Nº", "Instância", "Encerramento", "Grupo de Processo", "Valor(R$)", "F/E", "Observação", "Responsável", 
				"Preposto", "Tp. Contratação", "Tipo de Ação", "Grupo de Trabalho", "Cumprido por", "Ocorrências"});
		
		while(rsm.next()) {
			
			String nmInstancia = "";
			switch(rsm.getInt("TP_INSTANCIA")){
				case 0 : nmInstancia = "Não Informada"; break;
				case 1 : nmInstancia = "Primeiro Grau"; break;
				case 2 : nmInstancia = "Segundo Grau"; break;
				case 3 : nmInstancia = "Juizados Cíveis"; break;
				case 4 : nmInstancia = "Juizados Criminais"; break;
				case 5 : nmInstancia = "Turma Recursal"; break;
				case 6 : nmInstancia = "Execução Penal"; break;
				case 7 : nmInstancia = "Superiores"; break;
			}
			
			String nmSituacao = "";
			switch(rsm.getInt("ST_AGENDA_ITEM")){
				case 0 : nmSituacao = (rsm.getInt("TP_AGENDA_ITEM")==3?"Não repassado":"A cumprir"); break;
				case 1 : nmSituacao = "Cumprido"; break;
				case 2 : nmSituacao = "Cancelado"; break;
				case 3 : nmSituacao = "Vencido"; break;
				case 4 : nmSituacao = "A auditar"; break;
				case 5 : nmSituacao = "Repassado"; break;
			}
			
			String nmTpSentenca="";
			switch(rsm.getInt("TP_SENTENCA")) {
				case 0: nmTpSentenca = "Não sentenciado"; break;
				case 1: nmTpSentenca = "Procedente"; break;
				case 2: nmTpSentenca = "Improcedente"; break;
				case 3: nmTpSentenca = "Parc. Procedente"; break;
				case 4: nmTpSentenca = "Acordo"; break;
				case 5: nmTpSentenca = "Extinto sem Mérito"; break;
				case 6: nmTpSentenca = "Acordo após condenação"; break;
				case 7: nmTpSentenca = "Acordo antes da instrução"; break;
				case 8: nmTpSentenca = "Desistência"; break;
				case 9: nmTpSentenca = "Contumácia"; break; 
			}
			
			GregorianCalendar dtInicial = new GregorianCalendar();
			if(rsm.getObject("DT_INICIAL")!=null)
				dtInicial.setTimeInMillis(((Date)rsm.getObject("DT_INICIAL")).getTime());
//			GregorianCalendar dtFinal = new GregorianCalendar();
//			if(rsm.getObject("DT_FINAL")!=null)
//				dtFinal.setTimeInMillis(((Date)rsm.getObject("DT_FINAL")).getTime());
			
			GregorianCalendar dtCumprimento = new GregorianCalendar();
			if(rsm.getObject("DT_REALIZACAO")!=null)
				dtCumprimento.setTimeInMillis(((Date)rsm.getObject("DT_REALIZACAO")).getTime());
			
			String dsDia = Util.formatDate(dtInicial, "EEE").toUpperCase();
			String dsData = (rsm.getObject("DT_INICIAL")!=null) ? Util.formatDate(dtInicial, "dd/MM/yyyy").toUpperCase() : "";
			String dsHora = Util.formatDate(dtInicial, "HH:mm").toUpperCase();
			
			String dsDtCumprimento = (rsm.getObject("DT_REALIZACAO")!=null) ? Util.formatDate(dtCumprimento, "dd/MM/yyyy").toUpperCase() : "";
			
			String nmTipoContratacao = (rsm.getInt("TP_CONTRATACAO")==0) ? "Mensalista" : "Avulso";
			
			dados.add(new Object[] {
				dsDia, dsData, dsHora, //1, 2, 3
				rsm.getString("NM_TIPO_PRAZO"), //4
				rsm.getInt("LG_CLIENTE_AUTOR")==0 ? "REQUERIDO" : "AUTOR", //5
				rsm.getString("NM_CLIENTE"), //6
				rsm.getString("NM_ADVERSO"), //7
				nmSituacao, //8
				dsDtCumprimento, //9
				(dtCumprimento!=null ? Util.getQuantidadeDias(dtCumprimento, rsm.getGregorianCalendar("DT_INICIAL"))+" DIAS" : ""),
				// 10
				(rsm.getString("NR_JUIZO")!=null && !rsm.getString("NR_JUIZO").equals("") ? rsm.getString("NR_JUIZO")+" " : "" ) +
				 (rsm.getString("SG_JUIZO")!=null && !rsm.getString("SG_JUIZO").equals("") ? rsm.getString("SG_JUIZO") : (rsm.getString("NM_JUIZO")!=null && !rsm.getString("NM_JUIZO").equals("") ? rsm.getString("NM_JUIZO") : "") ), //11
				(rsm.getString("NM_CIDADE")!=null && !rsm.getString("NM_CIDADE").equals("") ? rsm.getString("NM_CIDADE") : "" ), //12
				(rsm.getString("SG_ESTADO")!=null && !rsm.getString("SG_ESTADO").equals("") ? rsm.getString("SG_ESTADO") : "" ), //13
				rsm.getString("NR_PROCESSO"), //14
				rsm.getString("NM_CONTEINER3"), //15
				nmInstancia, //16
				nmTpSentenca, //17
				rsm.getString("NM_GRUPO_PROCESSO"), //18
				Util.formatNumber(rsm.getDouble("VL_PROCESSO")), //19
				rsm.getInt("TP_AUTOS") == 0 ? "F" : "E", //20
				rsm.getString("DS_DETALHE"), //21
				rsm.getString("NM_RESPONSAVEL"), //22
				(rsm.getInt("LG_PREPOSTO")==0?"Não":"Sim"), //23
				nmTipoContratacao, //24
				rsm.getString("NM_TIPO_PROCESSO"), //25
				rsm.getString("NM_GRUPO"), //26
				rsm.getString("NM_USUARIO_CUMPRIMENTO"), //27
				rsm.getString("DS_OCORRENCIA") //28
				});
		}
		
		String titulo = "Pauta de Compromissos - " + empresa.getNmPessoa();
		String fileName = "PAUTA_COMPROMISSO_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
		Integer[] columnsWidths = new Integer[] {25, 51, 33, 103, 61, 
												61, 138, 89, 51, 138,
												75,	40, 117, 117, 26,
												117, 61, 80, 60, 40,
												120, 80, 40, 70, 80,
												80, 80, 150};
		
		return gerarBasicXls(dados, columnsWidths, titulo, fileName);
	}
	
	@Deprecated
	public static Result gerarPautaCompromissosFaturamento(ResultSetMap rsm, Empresa empresa) {
		
		ArrayList<Object[]> dados = new ArrayList<Object[]>();
		dados.add(new Object[] {"Dia", "Data", "Hora", "Compromisso", "Cliente", "Adverso", "Situação", "Dt. Cumprimento", "Juízo", "Comarca", "UF", 
				"Processo Nº", "Encerramento", "Corresp. Responsável", "Preposto", "Grupo de Trabalho", "Valor (R$)"});
		
		double vlTotal = 0;
		
		while(rsm.next()) {
			
			String nmSituacao = "";
			switch(rsm.getInt("ST_AGENDA_ITEM")){
				case 0 : nmSituacao = (rsm.getInt("TP_AGENDA_ITEM")==3?"Não repassado":"A cumprir"); break;
				case 1 : nmSituacao = "Cumprido"; break;
				case 2 : nmSituacao = "Cancelado"; break;
				case 3 : nmSituacao = "Vencido"; break;
				case 4 : nmSituacao = "A auditar"; break;
				case 5 : nmSituacao = "Repassado"; break;
			}
			
			String nmTpSentenca = "";
			switch(rsm.getInt("TP_SENTENCA")){
				case 0 : nmTpSentenca = "Não sentenciado"; break;
				case 1 : nmTpSentenca = "Procedente"; break;
				case 2 : nmTpSentenca = "Improcedente"; break;
				case 3 : nmTpSentenca = "Parc. Procedente"; break;
				case 4 : nmTpSentenca = "Acordo"; break;
				case 5 : nmTpSentenca = "Extinto sem Mérito"; break;
				case 6 : nmTpSentenca = "Acordo após condenação"; break;
				case 7 : nmTpSentenca = "Acordo antes da instrução"; break;
			}
			
			GregorianCalendar dtInicial = new GregorianCalendar();
			if(rsm.getObject("DT_INICIAL")!=null)
				dtInicial.setTimeInMillis(((Date)rsm.getObject("DT_INICIAL")).getTime());
			
			GregorianCalendar dtCumprimento = new GregorianCalendar();
			if(rsm.getObject("DT_REALIZACAO")!=null)
				dtCumprimento.setTimeInMillis(((Date)rsm.getObject("DT_REALIZACAO")).getTime());
			
			String dsDia = Util.formatDate(dtInicial, "EEE").toUpperCase();
			String dsData = (rsm.getObject("DT_INICIAL")!=null) ? Util.formatDate(dtInicial, "dd/MM/yyyy").toUpperCase() : "";
			String dsHora = Util.formatDate(dtInicial, "HH:mm").toUpperCase();
			
			String dsDtCumprimento = (rsm.getObject("DT_REALIZACAO")!=null) ? Util.formatDate(dtCumprimento, "dd/MM/yyyy").toUpperCase() : "";
			
			dados.add(new Object[] {
					dsDia, dsData, dsHora,
					rsm.getString("NM_TIPO_PRAZO"),
					rsm.getString("NM_CLIENTE"),
					rsm.getString("NM_ADVERSO"),
					nmSituacao,
					dsDtCumprimento,
					(rsm.getString("NR_JUIZO")!=null && !rsm.getString("NR_JUIZO").equals("") ? rsm.getString("NR_JUIZO")+" " : "" ) +
					 	(rsm.getString("SG_JUIZO")!=null && !rsm.getString("SG_JUIZO").equals("") ? rsm.getString("SG_JUIZO") : "" ),
					(rsm.getString("NM_CIDADE")!=null && !rsm.getString("NM_CIDADE").equals("") ? rsm.getString("NM_CIDADE") : "" ),
				 	rsm.getString("SG_ESTADO"),
					rsm.getString("NR_PROCESSO"),
					nmTpSentenca,
					rsm.getString("NM_PESSOA"),
					(rsm.getInt("LG_PREPOSTO")==0?"Não":"Sim"),
					rsm.getString("NM_GRUPO"), 
					(rsm.getString("NM_CATEGORIA_RECEITA")!=null ? rsm.getString("NM_CATEGORIA_RECEITA") : rsm.getString("NM_CATEGORIA_DESPESA")),
					Util.formatNumber(rsm.getDouble("VL_EVENTO_FINANCEIRO"))
				});
			
			vlTotal += rsm.getDouble("VL_EVENTO_FINANCEIRO");
			
			LogUtils.debug("NM_CATEGORIA_RECEITA: "+rsm.getString("NM_CATEGORIA_RECEITA"));
			LogUtils.debug("NM_CATEGORIA_DESPESA: "+rsm.getString("NM_CATEGORIA_DESPESA"));
		}
		dados.add(new Object[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""});
		dados.add(new Object[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "TOTAL (R$)", Util.formatNumber(vlTotal)});
		
		String titulo = "Pauta de Compromissos - " + empresa.getNmPessoa();
		String fileName = "PAUTA_COMPROMISSO_FATURAMENTO_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
		Integer[] columnsWidths = new Integer[] {25, 51, 33, 240, 100, 170, 60, 100, 90, 125, 80, 120, 150, 140, 110, 115, 80};
		
		return gerarBasicXls(dados, columnsWidths, titulo, fileName);
	}
	
	public static Result gerarPautaCompromissosFaturamento2(ResultSetMap rsm, Empresa empresa) {
		
		ArrayList<Object[]> dados = new ArrayList<Object[]>();
		dados.add(new Object[] {"Data", "Hora", "Compromisso", "Cliente", "Adverso", "Dt. Cumprimento", "Comarca", "UF", 
				"Processo Nº", "Grupo de Processo", "Corresp. Responsável", "Grupo de Trabalho", /*"Categoria",*/ "Serviço", "Valor (R$)"});
		
		double vlTotal = 0;
		
		while(rsm.next()) {
			
			GregorianCalendar dtInicial = new GregorianCalendar();
			if(rsm.getObject("DT_INICIAL")!=null)
				dtInicial.setTimeInMillis(((Date)rsm.getObject("DT_INICIAL")).getTime());
			
			GregorianCalendar dtCumprimento = new GregorianCalendar();
			if(rsm.getObject("DT_REALIZACAO")!=null)
				dtCumprimento.setTimeInMillis(((Date)rsm.getObject("DT_REALIZACAO")).getTime());
			
			String dsData = (rsm.getObject("DT_INICIAL")!=null) ? Util.formatDate(dtInicial, "dd/MM/yyyy").toUpperCase() : "";
			String dsHora = Util.formatDate(dtInicial, "HH:mm").toUpperCase();
			
			String dsDtCumprimento = (rsm.getObject("DT_REALIZACAO")!=null) ? Util.formatDate(dtCumprimento, "dd/MM/yyyy").toUpperCase() : "";
			
			dados.add(new Object[] {
					dsData, dsHora,
					rsm.getString("NM_TIPO_PRAZO"),
					rsm.getString("NM_CLIENTE"),
					rsm.getString("NM_ADVERSO"),
					dsDtCumprimento,
					(rsm.getString("NM_CIDADE")!=null && !rsm.getString("NM_CIDADE").equals("") ? rsm.getString("NM_CIDADE") : "" ),
				 	rsm.getString("SG_ESTADO"),
					rsm.getString("NR_PROCESSO"),
					rsm.getString("NM_GRUPO_PROCESSO"),
					rsm.getString("NM_PESSOA"),
					rsm.getString("NM_GRUPO_TRABALHO"), 
					//(rsm.getString("NM_CATEGORIA_RECEITA")!=null ? rsm.getString("NM_CATEGORIA_RECEITA") : rsm.getString("NM_CATEGORIA_DESPESA")),
					rsm.getString("NM_PRODUTO_SERVICO"),
					Util.formatNumber(rsm.getDouble("VL_EVENTO_FINANCEIRO"))
				});
			
			vlTotal += rsm.getDouble("VL_EVENTO_FINANCEIRO");
			
			LogUtils.debug("NM_CATEGORIA_RECEITA: "+rsm.getString("NM_CATEGORIA_RECEITA"));
			LogUtils.debug("NM_CATEGORIA_DESPESA: "+rsm.getString("NM_CATEGORIA_DESPESA"));
		}
		dados.add(new Object[]{"", "", "", "", "", "", "", "", "", "", "", "", "", ""});
		dados.add(new Object[]{"", "", "", "", "", "", "", "", "", "", "", "", "TOTAL (R$)", Util.formatNumber(vlTotal)});
		
		String titulo = "Pauta de Compromissos - " + empresa.getNmPessoa();
		String fileName = "PAUTA_COMPROMISSO_FATURAMENTO_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
		Integer[] columnsWidths = new Integer[] {51, 33, 240, 100, 170, 100, 90, 125, 125, 120, 110, 115, 220, 80};
		
		return gerarBasicXls(dados, columnsWidths, titulo, fileName);
	}
	
	public static Result gerarFaturamento(ResultSetMap rsm, Empresa empresa) {
		
		ArrayList<Object[]> dados = new ArrayList<Object[]>();
		dados.add(new Object[] {"Natureza", "Situação", "Serviço", "Categoria", "Ref.", "Dt. Serviço", "Lançamento", "Valor", "Prestador/Tomador", "Preposto", "Dt. Emissão",
						"Nº Fatura", "Dt. Vencimento", "Dt. Quitação", "Total da Fatura", "Dt. Repasse", "Nº Processo", "Grupo de Processo", "Pasta", "Situação do Processo", "Cliente", "Adverso", "Comarca", "UF", 
						"Encerramento", "Grupo de Trabalho", "Dt. Cumprimento", "Tipo de Compromisso/Prazo"});
		
		double vlTotal = 0;
		String nmNatureza = "";
		
		while(rsm.next()) {
			
			String dsDtEmissao = "";
			String dsNrDocumento = "";
			String dsDtVencimento = "";
			String dsDtQitacao = "";
			double vlTotalFatura = 0;
			String nmCategoria = "";
			//int stFatura = 0;
			switch(rsm.getInt("TP_NATUREZA_EVENTO")){
				case 0 : 
					nmNatureza = "Receita"; 
					
					dsDtEmissao = Util.formatDate(rsm.getGregorianCalendar("DT_EMISSAO_RECEBER"), "dd/MM/yyyy");
					dsDtVencimento = Util.formatDate(rsm.getGregorianCalendar("DT_VENCIMENTO_RECEBER"), "dd/MM/yyyy");
					dsDtQitacao = Util.formatDate(rsm.getGregorianCalendar("DT_RECEBIMENTO"), "dd/MM/yyyy");
					
					dsNrDocumento = rsm.getString("NR_DOCUMENTO_RECEBER");
					
					vlTotalFatura = rsm.getDouble("VL_CONTA_RECEBER");
					
					nmCategoria = rsm.getString("NM_CATEGORIA_RECEITA");
					
					break;
				case 1 : 
					nmNatureza = "Despesa"; 
					
					dsDtEmissao = Util.formatDate(rsm.getGregorianCalendar("DT_EMISSAO_PAGAR"), "dd/MM/yyyy");
					dsDtVencimento = Util.formatDate(rsm.getGregorianCalendar("DT_VENCIMENTO_PAGAR"), "dd/MM/yyyy");
					dsDtQitacao = Util.formatDate(rsm.getGregorianCalendar("DT_PAGAMENTO"), "dd/MM/yyyy");
					
					dsNrDocumento = rsm.getString("NR_DOCUMENTO_PAGAR");

					vlTotalFatura = rsm.getDouble("VL_CONTA_PAGAR");
					
					nmCategoria = rsm.getString("NM_CATEGORIA_DESPESA");
										
					break;
			}
			
			String nmStProcesso="";
			switch(rsm.getInt("ST_PROCESSO")) {
			case 0 : nmStProcesso = "Inativo"; break;
			case 1 : nmStProcesso = "Ativo"; break;
			case 2 : nmStProcesso = "Suspenso"; break;
			case 3 : nmStProcesso = "Descontratado"; break;
			}
			
			String nmTpSentenca = "";
			switch(rsm.getInt("TP_SENTENCA")){
				case 0 : nmTpSentenca = "Não sentenciado"; break;
				case 1 : nmTpSentenca = "Procedente"; break;
				case 2 : nmTpSentenca = "Improcedente"; break;
				case 3 : nmTpSentenca = "Parc. Procedente"; break;
				case 4 : nmTpSentenca = "Acordo"; break;
				case 5 : nmTpSentenca = "Extinto sem Mérito"; break;
				case 6 : nmTpSentenca = "Acordo após condenação"; break;
				case 7 : nmTpSentenca = "Acordo antes da instrução"; break;
			}
				
			String dsLgPreposto = (rsm.getInt("LG_PREPOSTO")==0 ? "Não" : "Sim");	
			
			dados.add(new Object[] {
					nmNatureza, //cl0
					rsm.getString("NM_ST_FATURA"),//nmStFatura,
					rsm.getString("NM_PRODUTO_SERVICO"),
					nmCategoria,
					rsm.getString("NR_REFERENCIA"),
					Util.formatDate(rsm.getGregorianCalendar("DT_EVENTO_FINANCEIRO"), "dd/MM/yyyy"), //cl04
					Util.formatDate(rsm.getGregorianCalendar("DT_LANCAMENTO"), "dd/MM/yyyy"),
					Util.arredondar(rsm.getDouble("VL_EVENTO_FINANCEIRO"), 2),
					rsm.getString("NM_PESSOA"),
					dsLgPreposto,
					dsDtEmissao, //cl9
					dsNrDocumento, 
					dsDtVencimento,
					dsDtQitacao,
					Util.arredondar(vlTotalFatura, 2),
					Util.formatDate(rsm.getGregorianCalendar("DT_REPASSE"), "dd/MM/yyyy"),
					rsm.getString("NR_PROCESSO"), //cl14
					rsm.getString("NM_GRUPO_PROCESSO"),
					rsm.getString("NM_PASTA"),
					nmStProcesso,
					rsm.getString("NM_CLIENTE"), 
					rsm.getString("NM_PARTE_CONTRARIA"),
					rsm.getString("NM_CIDADE"),
					rsm.getString("SG_ESTADO"),
					nmTpSentenca, 
					rsm.getString("NM_GRUPO_TRABALHO"),
					Util.formatDate(rsm.getGregorianCalendar("DT_REALIZACAO"), "dd/MM/yyyy"),
					rsm.getString("NM_TIPO_PRAZO") //cl19
			});
			
			vlTotal += rsm.getDouble("VL_EVENTO_FINANCEIRO");
		}
		dados.add(new Object[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""});
		dados.add(new Object[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "TOTAL (R$)", Util.formatNumber(vlTotal)});
		
		String titulo = "FATURAMENTO ("+nmNatureza+") - " + empresa.getNmPessoa();
		String fileName = "FATURAMENTO_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
		Integer[] columnsWidths = new Integer[] {75, 150, 220, 150, 50, 
												80, 80, 50, 230, 50, 
												80, 100, 80, 80, 90, 
												120, 100, 100, 100, 150, 
												290, 130, 50, 100, 120, 
												80, 100};
		
		return gerarBasicXls(dados, columnsWidths, titulo, fileName);
	}
	
	public static Result gerarListaProcessoXls(ResultSetMap rsm, Empresa empresa, boolean exportarAndamentos) {
		return gerarListaProcessoXls(rsm, empresa, exportarAndamentos, null, null);
	}
	
	public static Result gerarListaProcessoXls(ResultSetMap rsm, Empresa empresa, boolean exportarAndamentos, String dsFiltroPeriodo) {
		return gerarListaProcessoXls(rsm, empresa, exportarAndamentos, dsFiltroPeriodo, null);
	}
	
	public static Result gerarListaProcessoXls(ResultSetMap rsm, Empresa empresa, boolean exportarAndamentos, String dsFiltroPeriodo, ArrayList<Integer> visibilidadeAndamento) {
		ArrayList<Object[]> dados = new ArrayList<Object[]>();
		ArrayList<String> tituloColunas = new ArrayList<String>();
		
		Connection connection = Conexao.conectar();
		
		tituloColunas.add(" "); 
		tituloColunas.add("Processo nº"); 
		tituloColunas.add("Status"); 
		tituloColunas.add("Distribuição");  
		tituloColunas.add("Inativação");
		tituloColunas.add("Cadastro");
		tituloColunas.add("Repasse");
		tituloColunas.add("Adverso");  
		tituloColunas.add("CPF Adverso"); 
		tituloColunas.add("Nº Contrato");  
		tituloColunas.add("Posição");
		tituloColunas.add("Cliente");
		tituloColunas.add("Grupo de Trabalho");
		tituloColunas.add("Adv. Responsável");  
		tituloColunas.add("Comarca");  
		tituloColunas.add("UF");  
		tituloColunas.add("Instância");  
		tituloColunas.add("Juízo"); 
		tituloColunas.add("F/E");
		tituloColunas.add("Grupo de Processo");
		tituloColunas.add("Valor(R$)");  
		tituloColunas.add("Encerramento");  
		tituloColunas.add("Data");  
		tituloColunas.add("Prob. Perda");  
		tituloColunas.add("Adv. Adverso");  
		tituloColunas.add("Tipo de Ação");  
		tituloColunas.add("Fase");  
		tituloColunas.add("Rito");  
		tituloColunas.add("Repasse");  
		tituloColunas.add("Cód. Processo");
		tituloColunas.add("Orgão Judicial");
		tituloColunas.add("Pasta");
		tituloColunas.add("Objeto do Processo");
		tituloColunas.add("Valor Total da Sentença");
		//tituloColunas.add("Valor do Acordo");
		tituloColunas.add("Pedido de Liminar");
		tituloColunas.add("Observações");
		tituloColunas.add("Dias sem Andamento");
		
		if(exportarAndamentos) {
			tituloColunas.add("Data");  
			tituloColunas.add("Penúltimo Andamento");
			tituloColunas.add("Data");  
			tituloColunas.add("Último Andamento");
		}
				
		dados.add(tituloColunas.toArray());
					
		while(rsm.next()) {
			
			String nmInstancia = "";
			switch(rsm.getInt("TP_INSTANCIA")){
				case 0 : nmInstancia = "Não Informada"; break;
				case 1 : nmInstancia = "Primeiro Grau"; break;
				case 2 : nmInstancia = "Segundo Grau"; break;
				case 3 : nmInstancia = "Juizados Cíveis"; break;
				case 4 : nmInstancia = "Juizados Criminais"; break;
				case 5 : nmInstancia = "Turma Recursal"; break;
				case 6 : nmInstancia = "Execução Penal"; break;
				case 7 : nmInstancia = "Superiores"; break;
			}
			
			String nmLiminar = "";
			switch(rsm.getInt("ST_LIMINAR")){
				case 0 : nmLiminar = "Sem pedido"; break;
				case 1 : nmLiminar = "Pendente"; break;
				case 2 : nmLiminar = "Pedido Deferido"; break;
				case 3 : nmLiminar = "Pedido Indeferido"; break;
			}
			
			String nmTpRepasse = "";
			switch(rsm.getInt("TP_REPASSE")) {
				case 0 : nmTpRepasse = "Inicial"; break;
				case 1 : nmTpRepasse = "Migração"; break;
			}
			
			String nmTpRito = "";
			switch(rsm.getInt("TP_RITO")) {
				case 0 : nmTpRito = "Rito Comum"; break;
				case 1 : nmTpRito = "Rito Especial"; break;
				case 2 : nmTpRito = "Rito dos Juizados Especial"; break;
			}
			
			GregorianCalendar dtDistribuicao = null;
			if(rsm.getObject("DT_DISTRIBUICAO")!=null) { 
				dtDistribuicao = new GregorianCalendar();
				dtDistribuicao.setTimeInMillis(((Date)rsm.getObject("DT_DISTRIBUICAO")).getTime());
			}
			
			GregorianCalendar dtInativacao = null;
			if(rsm.getObject("DT_INATIVACAO")!=null) { 
				dtInativacao = new GregorianCalendar();
				dtInativacao.setTimeInMillis(((Date)rsm.getObject("DT_INATIVACAO")).getTime());
			}
			
			GregorianCalendar dtSentenca  = null;
			if(rsm.getObject("DT_SENTENCA")!=null) { 
				if(rsm.getObject("DT_SENTENCA") instanceof GregorianCalendar) {
					try {
						dtSentenca = rsm.getGregorianCalendar("DT_SENTENCA");
					} catch(Exception e) {
						dtSentenca = new GregorianCalendar();
						dtSentenca.setTimeInMillis(((Date)rsm.getObject("DT_SENTENCA")).getTime());					
					}
				} else if (rsm.getObject("DT_SENTENCA") instanceof Timestamp) {
					dtSentenca = Util.convTimestampToCalendar(rsm.getTimestamp("DT_SENTENCA"));
				}
				
//				dtSentenca = new GregorianCalendar();
//				dtSentenca.setTimeInMillis(((Date)rsm.getObject("DT_SENTENCA")).getTime());
			}
			
			GregorianCalendar dtCadastro = null;
			if(rsm.getObject("DT_CADASTRO")!=null) { 
				dtCadastro = new GregorianCalendar();
				dtCadastro.setTimeInMillis(((Date)rsm.getObject("DT_CADASTRO")).getTime());
			}
			
			GregorianCalendar dtRepasse = null;
			if(rsm.getObject("DT_REPASSE")!=null) { 
				dtRepasse = new GregorianCalendar();
				dtRepasse.setTimeInMillis(((Date)rsm.getObject("DT_REPASSE")).getTime());
			}
			
			ArrayList<Object> reg = new ArrayList<Object>();
			reg.add(rsm.getInt("LG_IMPORTANTE", 0)==0 ? "" : "«importante»");
			reg.add(rsm.getString("NR_PROCESSO"));
			reg.add(rsm.getInt("ST_PROCESSO") >= 0 ? ProcessoServices.situacaoProcesso[rsm.getInt("ST_PROCESSO")] : "");
			reg.add(dtDistribuicao!=null? Util.formatDate(dtDistribuicao, "dd/MM/yyyy") : "");
			reg.add(dtInativacao!=null? Util.formatDate(dtInativacao, "dd/MM/yyyy") : "");
			reg.add(dtCadastro!=null? Util.formatDate(dtCadastro, "dd/MM/yyyy") : "");
			reg.add(dtRepasse!=null? Util.formatDate(dtRepasse, "dd/MM/yyyy") : "");
			reg.add(rsm.getString("NM_ADVERSO1"));
			reg.add(rsm.getString("NR_CPF_ADVERSO1"));
			reg.add(rsm.getString("NR_CONTRATO"));
			reg.add(rsm.getInt("LG_CLIENTE_AUTOR")==0 ? "REQUERIDO" : "AUTOR");
			reg.add(rsm.getString("NM_CLIENTE"));
			reg.add(rsm.getString("NM_GRUPO_TRABALHO") != null && !rsm.getString("NM_GRUPO_TRABALHO").equals("") ? rsm.getString("NM_GRUPO_TRABALHO") : "Sem informação");
			reg.add(rsm.getString("NM_ADVOGADO"));
			reg.add(rsm.getString("NM_CIDADE")!=null && !rsm.getString("NM_CIDADE").equals("") ? rsm.getString("NM_CIDADE") : "");
			reg.add(rsm.getString("SG_ESTADO")!=null && !rsm.getString("SG_ESTADO").equals("") ? rsm.getString("SG_ESTADO") : "");
			reg.add(rsm.getString("TP_INSTANCIA")!=null && !rsm.getString("TP_INSTANCIA").equals("") ? nmInstancia : "");
			reg.add((rsm.getString("NR_JUIZO")!=null && !rsm.getString("NR_JUIZO").equals("") ? rsm.getString("NR_JUIZO")+" " : "" ) +
				 (rsm.getString("SG_JUIZO")!=null && !rsm.getString("SG_JUIZO").equals("") ? rsm.getString("SG_JUIZO") : "" ));
			reg.add(rsm.getInt("TP_AUTOS")==0 ? "F" : "E");
			reg.add(rsm.getString("NM_GRUPO_PROCESSO"));
			reg.add(rsm.getDouble("VL_PROCESSO"));//Util.formatNumber(rsm.getDouble("VL_PROCESSO")));
			reg.add(rsm.getInt("TP_SENTENCA") >= 0 ? ProcessoServices.tipoSentenca[rsm.getInt("TP_SENTENCA")] : "");
			reg.add(dtSentenca!=null ? Util.formatDate(dtSentenca, "dd/MM/yyyy") : "");
			reg.add(rsm.getInt("TP_PERDA") >= 0 ? ProcessoServices.tipoPerda[rsm.getInt("TP_PERDA")] : "");
			reg.add(rsm.getString("NM_ADVOGADO_CONTRARIO"));
			reg.add(rsm.getString("NM_TIPO_PROCESSO"));
			reg.add(rsm.getString("NM_TIPO_SITUACAO"));
			reg.add(nmTpRito);
			reg.add(nmTpRepasse);
			reg.add(rsm.getString("ID_PROCESSO"));
			reg.add(rsm.getString("NM_ORGAO_JUDICIAL"));
			reg.add(rsm.getString("NM_CONTEINER3"));
			reg.add(rsm.getString("NM_TIPO_OBJETO"));
			reg.add(rsm.getDouble("VL_TOTAL_SENTENCA"));
			//reg.add(rsm.getDouble("VL_ACORDO"));
			reg.add(rsm.getString("ST_LIMINAR")!=null && !rsm.getString("ST_LIMINAR").equals("") ? nmLiminar : "");
			reg.add(rsm.getString("TXT_OBSERVACAO"));
			
			ProcessoAndamento ultimoAndamento = ProcessoAndamentoServices.getUltimoAndamento(rsm.getInt("CD_PROCESSO"), connection);			
			if(ultimoAndamento!=null) {
				reg.add(Integer.toString(Util.getQuantidadeDias(ultimoAndamento.getDtAndamento(), new GregorianCalendar())));
			} else {
				reg.add("«nenhuma movimentação»");
			}
			
			if(exportarAndamentos) {
				
				ResultSetMap rsmAndamentos = ProcessoAndamentoServices
						.getAndamentos(rsm.getInt("CD_PROCESSO"), 2, visibilidadeAndamento, connection);
				
				String dtPenultimoAndamento = "";
				String nmPenultimoAndamento = "";
				String nmVisibilidadePenultimoAndamento = "";
				
				String dtUltimoAndamento = "";
				String nmUltimoAndamento = "";
				String nmVisibilidadeUltimoAndamento = "";
				
				if(rsmAndamentos.next()) {
					GregorianCalendar dtAndamento = null;
					if(rsmAndamentos.getGregorianCalendar("DT_ANDAMENTO")!=null) 
						dtAndamento = rsmAndamentos.getGregorianCalendar("DT_ANDAMENTO");
					
					dtUltimoAndamento = (dtAndamento!=null ? Util.formatDate(dtAndamento, "dd/MM/yyyy") : ""); 
					nmUltimoAndamento = (rsmAndamentos.getString("NM_TIPO_ANDAMENTO"));
					nmVisibilidadeUltimoAndamento = " ("+ProcessoAndamentoServices.tipoVisibilidade[rsmAndamentos.getInt("TP_VISIBILIDADE")]+")";
				}
				
				if(rsmAndamentos.next()) {
					GregorianCalendar dtAndamento = null;
					if(rsmAndamentos.getGregorianCalendar("DT_ANDAMENTO")!=null) 
						dtAndamento = rsmAndamentos.getGregorianCalendar("DT_ANDAMENTO");
					
					dtPenultimoAndamento = (dtAndamento!=null ? Util.formatDate(dtAndamento, "dd/MM/yyyy") : ""); 
					nmPenultimoAndamento = (rsmAndamentos.getString("NM_TIPO_ANDAMENTO"));
					nmVisibilidadePenultimoAndamento = " ("+ProcessoAndamentoServices.tipoVisibilidade[rsmAndamentos.getInt("TP_VISIBILIDADE")]+")";
				}

				reg.add(dtPenultimoAndamento);
				reg.add(nmPenultimoAndamento + nmVisibilidadePenultimoAndamento);
				reg.add(dtUltimoAndamento);
				reg.add(nmUltimoAndamento + nmVisibilidadeUltimoAndamento);
			}			

			dados.add(reg.toArray());
		}
		
		String titulo = "Relatório de Processos - " + empresa.getNmPessoa() 
						+ (dsFiltroPeriodo!=null ? " \n("+dsFiltroPeriodo+")" : "");
		String fileName = "RELATORIO_PROCESSOS_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
		
		@SuppressWarnings("unchecked")
		ArrayList<Integer>  columnsWidths = new ArrayList<Integer>(Arrays.asList(new Integer[]{60, 117, 33, 70, 70, 70, 70, 75, 75, 89, 75, 26, 89, 61, 82, 54, 80, 89, 138, 138, 138, 89, 138, 90, 89, 89, 138, 89, 70}));
		
		if(exportarAndamentos) {
			columnsWidths.add(54);  
			columnsWidths.add(150);
			columnsWidths.add(54);  
			columnsWidths.add(150);
		}
		
		return gerarBasicXls(dados, columnsWidths.toArray(), titulo, fileName);
	}
	
	private static Result gerarBasicXls(ArrayList<Object[]> dados, Object[] columnsWidths, String titulo, String fileName) {
		return gerarBasicXls(dados, columnsWidths, titulo, fileName, null);
	}
	 
	private static Result gerarBasicXls(ArrayList<Object[]> dados, Object[] columnsWidths, String titulo, String fileName, String[] rodape) {
		 try {
			LogUtils.debug("ExcelServices.gerarBasicXls");
			LogUtils.createTimer("EXPORT_XLS_TIMER");
			 
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
				
			/*
			 * ESTILOS
			 */
			//@comment 
			HSSFCellStyle sTitulo = workbook.createCellStyle();
		    sTitulo.setAlignment(HorizontalAlignment.CENTER);//Alignment(HSSFCellStyle.ALIGN_CENTER);
		    sTitulo.setVerticalAlignment(VerticalAlignment.BOTTOM);//HSSFCellStyle.VERTICAL_BOTTOM);
		    sTitulo.setBorderTop(BorderStyle.THICK);
		    sTitulo.setBorderBottom(BorderStyle.THICK);
		    sTitulo.setBorderLeft(BorderStyle.THICK);
		    sTitulo.setBorderRight(BorderStyle.THICK);
		    sTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);//HSSFCellStyle.SOLID_FOREGROUND);
		    sTitulo.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		    
			HSSFFont f1 = workbook.createFont();
			f1.setFontName(HSSFFont.FONT_ARIAL);
			f1.setFontHeightInPoints((short) 12);
	        f1.setBold(true); //Boldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        sTitulo.setFont(f1);
	        
			HSSFCellStyle sDetalhe = workbook.createCellStyle();
			sDetalhe.setBorderTop(BorderStyle.THIN); //BorderTop((short) 1);
			sDetalhe.setBorderBottom(BorderStyle.THIN);//(short) 1);
			sDetalhe.setBorderLeft(BorderStyle.THIN);
			sDetalhe.setBorderRight(BorderStyle.THIN);
		    
			HSSFFont f2 = workbook.createFont();
			f2.setFontName(HSSFFont.FONT_ARIAL);
			f2.setFontHeightInPoints((short) 7);
	        sDetalhe.setFont(f2);
	        
			
	        HSSFCellStyle sTituloColuna = workbook.createCellStyle();
	        sTituloColuna.setBorderTop(BorderStyle.THIN);
	        sTituloColuna.setBorderBottom(BorderStyle.THIN);
	        sTituloColuna.setBorderLeft(BorderStyle.THIN);
	        sTituloColuna.setBorderRight(BorderStyle.THIN);
	        sTituloColuna.setFillPattern(FillPatternType.SOLID_FOREGROUND);//HSSFCellStyle.SOLID_FOREGROUND);
	        sTituloColuna.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	        sTituloColuna.setAlignment(HorizontalAlignment.CENTER); //(HSSFCellStyle.ALIGN_CENTER);
	        sTituloColuna.setVerticalAlignment(VerticalAlignment.BOTTOM);//HSSFCellStyle.VERTICAL_BOTTOM);
	        
	        HSSFCellStyle sNumericCell = workbook.createCellStyle();
	        sNumericCell.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##"));
		    
			HSSFFont f3 = workbook.createFont();
			f3.setFontName(HSSFFont.FONT_ARIAL);
			f3.setFontHeightInPoints((short) 8.5);
	        f3.setBold(true); //Boldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        sTituloColuna.setFont(f3);


	        HSSFCellStyle sRodape = workbook.createCellStyle();
	        sRodape.setAlignment(HorizontalAlignment.CENTER);
	        sRodape.setVerticalAlignment(VerticalAlignment.BOTTOM);
	        HSSFFont f4 = workbook.createFont();
	        f4.setFontName(HSSFFont.FONT_ARIAL);
			f4.setFontHeightInPoints((short) 8);
			sRodape.setFont(f4);
			
			//titulo
			Row r = sheet.createRow(0);
			r.setHeight((short)350);
			Cell c = r.createCell(0);
			c.setCellValue(titulo);
			c.setCellStyle(sTitulo);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnsWidths.length-1)); 
			
			LogUtils.debug("ExcelServices.gerarBasicXls: inicio da criação das linhas");
			LogUtils.logTimer("EXPORT_XLS_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
						
			int rownum = 1;
			for (Object[] objArr : dados) {
			    Row row = sheet.createRow(rownum++);
			    int cellnum = 0;
			    for (Object obj : objArr) {
			        Cell cell = row.createCell(cellnum++);
			        if(obj instanceof Date) 
			            cell.setCellValue((Date)obj);
			        else if(obj instanceof Boolean)
			            cell.setCellValue((Boolean)obj);
			        else if(obj instanceof String)
			            cell.setCellValue((String)obj);
			        else if(obj instanceof Double) {
			        	cell.setCellStyle(sNumericCell);
			        	cell.setCellType(CellType.NUMERIC);
			        	cell.setCellValue((Double)obj);
			        }
			        
			        cell.setCellStyle(rownum==2 ? sTituloColuna : sDetalhe);
			    }
			}
			
			//rodapé
			if(rodape!=null) {
				for (String line : rodape) {
					rownum++;
					Row rodapeRow = sheet.createRow(rownum);
					rodapeRow.setHeight((short)350);
					Cell rodapeCell = rodapeRow.createCell(0);
					rodapeCell.setCellValue(line);
					rodapeCell.setCellStyle(sRodape);
					sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 0, columnsWidths.length-1));
				} 
			}
			
			
			for (int i = 0; i < columnsWidths.length; i++) {
				int width = (Integer)columnsWidths[i];
				sheet.setColumnWidth(i, pixel2WidthUnits(width));
			}
			
			//sheet.setMargin(HSSFSheet.BottomMargin, 1.9);
			//sheet.setMargin(HSSFSheet.TopMargin, 1.9);
			sheet.setMargin(HSSFSheet.LeftMargin, 0);
			sheet.setMargin(HSSFSheet.RightMargin, 0);
			
			HSSFPrintSetup printSetup = sheet.getPrintSetup();
		    //sheet.getPrintSetup().setFitWidth((short) 1);
		    //sheet.getPrintSetup().setFitHeight((short) 0);
		    //sheet.setAutobreaks(true);
		    printSetup.setLandscape(true);
		    
		    LogUtils.debug("ExcelServices.gerarBasicXls: inicio da gravação do arquivo");
			LogUtils.logTimer("EXPORT_XLS_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			
			//XXX:
			System.out.println(fileName);
			System.out.println(fileName.replaceAll("/", ""));
			
			File file = new File(fileName.replaceAll("/", ""));
		    FileOutputStream out = new FileOutputStream(file);
		    workbook.write(out);
		    out.close();
		    
		    LogUtils.debug("ExcelServices.gerarBasicXls: final da gravação do arquivo. retorno");
			LogUtils.logTimer("EXPORT_XLS_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.destroyTimer("EXPORT_XLS_TIMER");
		     
		    return new Result(1, "Arquivo Excel gerado com sucesso!", "fileName", file.getName());
		} 
		catch (FileNotFoundException e) {
		    e.printStackTrace();
		    return new Result(-1, "Erro ao gerar arquivo de Excel");
		} 
		catch (IOException e) {
		    e.printStackTrace();
		    return new Result(-1, "Erro ao gerar arquivo de Excel");
		}
	}


	/**
	  * pixel units to excel width units(units of 1/256th of a character width)
	  * @param pxs
	  * @return
	  */
	 public static short pixel2WidthUnits(int pxs) {
	   short widthUnits = (short) (EXCEL_COLUMN_WIDTH_FACTOR * (pxs / UNIT_OFFSET_LENGTH));

	   widthUnits += UNIT_OFFSET_MAP[(pxs % UNIT_OFFSET_LENGTH)];

	   return widthUnits;
	 }
	 
	 /**
	  * excel width units(units of 1/256th of a character width) to pixel units 
	  * @param widthUnits
	  * @return
	  */
	 public static int widthUnits2Pixel(short widthUnits) {
	   int pixels = (widthUnits / EXCEL_COLUMN_WIDTH_FACTOR) * UNIT_OFFSET_LENGTH;
	 
	   int offsetWidthUnits = widthUnits % EXCEL_COLUMN_WIDTH_FACTOR;
	   pixels += Math.round((float) offsetWidthUnits / ((float) EXCEL_COLUMN_WIDTH_FACTOR / UNIT_OFFSET_LENGTH));
	 
	   return pixels;
	 }
	 
	 public static Result gerarAfericaoXls(ResultSetMap rsm){					
			ArrayList<Object[]> dados = new ArrayList<Object[]>();			 
			
			dados.add(new Object[] {"Prefixo", "Catraca", "Hodômetro", "Data da Leitura", "Observação/Motivos"});
			while(rsm.next()){
				dados.add(new Object[]{
						rsm.getString("NR_PREFIXO"),
						(Double.valueOf(rsm.getString("QT_AFERIDO").trim()) > 0 ? rsm.getString("QT_AFERIDO") : ""),
						(Double.valueOf(rsm.getString("QT_HODOMETRO").trim()) > 0 ? rsm.getString("QT_HODOMETRO") : ""),						
						rsm.getDateFormat("DT_AFERICAO", "dd/MM/yyyy HH:mm"),
						(rsm.getString("LISTA_MOTIVOS").length() > 1 ? 
								(rsm.getString("TXT_OBSERVACAO").length() > 1 ? rsm.getString("TXT_OBSERVACAO")+", " +rsm.getString("LISTA_MOTIVOS") : rsm.getString("LISTA_MOTIVOS")) 
								                                                                                                                     : rsm.getString("TXT_OBSERVACAO"))
					}
				);
			}
			Integer[] columnsWidths = new Integer[] {80, 120, 120, 150, 300};
			String titulo ="Relatório de Aferição de Leituras";
			String fileName ="/tivic/RELATORIO_AFERICAO_LEITURAS_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
			return gerarBasicXls(dados, columnsWidths, titulo, fileName);
		}
	 
	 public static Result gerarRelVistoriaXls(ResultSetMap rsm){					
			ArrayList<Object[]> dados = new ArrayList<Object[]>();			 
			
			dados.add(new Object[] {"Situação", "Prefixo", "Placa", "Selo", "Vistoriado", "Agendada", "Aplicada", "Retorno", "Vistoriador", "Condutor", "Observações"});
			while(rsm.next()){
				dados.add(new Object[]{
						VistoriaServices.situacaoVistoria[rsm.getInt("ST_VISTORIA")],
						(Integer.valueOf(rsm.getString("NR_PREFIXO").trim()) > 0 ? rsm.getString("NR_PREFIXO") : ""),						
						Util.format(rsm.getString("NR_PLACA"), "###-####", false),
						rsm.getString("ID_SELO"),
						rsm.getString("NM_VISTORIADO"),						
						rsm.getDateFormat("DT_VISTORIA", "dd/MM/yyyy HH:mm"),
						rsm.getDateFormat("DT_APLICACAO", "dd/MM/yyyy HH:mm"),
						rsm.getDateFormat("DT_RETORNO", "dd/MM/yyyy HH:mm"),
						rsm.getString("NM_VISTORIADOR"),
						rsm.getString("NM_CONDUTOR"),
						rsm.getString("DS_OBSERVACAO")
					}
				);
			}
			Integer[] columnsWidths = new Integer[] {100, 70, 70, 70, 200, 90, 90, 90, 200, 200, 300};
			String titulo ="Relatório de Vistorias";
			String fileName ="/tivic/RELATORIO_VISTORIA" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
			return gerarBasicXls(dados, columnsWidths, titulo, fileName);
		}
	 
	 public static Result gerarRelatorioOSXls(ResultSetMap rsm){
			ArrayList<Object[]> dados = new ArrayList<Object[]>();
			
			Double vlTotal = new Double(0);
			
			dados.add(new Object[] {"Nº OS", "Solicitante", "Solicitação", "Entrada", "Situação do Serviço", "Conclusão - Laudo", "Finalizado", "Valor (R$)"});
			while(rsm.next()){
				dados.add(new Object[]{
						(rsm.getString("NR_ORDEM_SERVICO")!= null)?rsm.getString("NR_ORDEM_SERVICO"):"",
						(rsm.getString("NM_PESSOA")!= null)?rsm.getString("NM_PESSOA"):"",
						(rsm.getString("TXT_SOLICITACAO")!= null)?rsm.getString("TXT_SOLICITACAO"):"",
						(rsm.getString("DT_ENTRADA")!= null)?rsm.getDateFormat("DT_ENTRADA", "dd/MM/yyyy"):"",
						(rsm.getString("NM_SITUACAO_SERVICO")!= null)?rsm.getString("NM_SITUACAO_SERVICO"):"",
						(rsm.getString("TXT_LAUDO")!= null)?rsm.getString("TXT_LAUDO"):"",
						(rsm.getString("DT_LAUDO")!= null)?rsm.getDateFormat("DT_LAUDO", "dd/MM/yyyy"):"",
						Util.formatNumber(rsm.getDouble("VL_ORDEM_SERVICO"))
					}
				);
				
				vlTotal += rsm.getDouble("VL_ORDEM_SERVICO");
			}
			dados.add(new Object[] {"", "", "", "", "", "", "TOTAL", Util.formatNumber(vlTotal)});
			
			Integer[] columnsWidths = new Integer[] {80, 300, 400, 80, 100, 400, 80, 70};
			String titulo ="Relatório Geral de OS";
			String fileName = "RELATORIO_GERAL_OS_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
			return gerarBasicXls(dados, columnsWidths, titulo, fileName);
		}
	 
	 	public static Result gerarRelatorioVeiculosXls(ResultSetMap rsm){
			ArrayList<Object[]> dados = new ArrayList<Object[]>();
			
			dados.add(new Object[] {"Proprietário", "Situação", "Prefixo", "Placa", "Chassi", "Fabricado", "CRLV", "Cidade", "UF", "Adaptado", "Vinculação", "Desvinculação"});
			while(rsm.next()){
				dados.add(new Object[]{
						(rsm.getString("NM_PROPRIETARIO")!= null)?rsm.getString("NM_PROPRIETARIO"):"",
						(rsm.getString("ST_CONCESSAO_VEICULO")!= null) ?ConcessaoVeiculoServices.situacaoConcessaoVeiculo[rsm.getInt("ST_CONCESSAO_VEICULO")]:"",
						(rsm.getString("NR_PREFIXO")!= null)?rsm.getString("NR_PREFIXO"):"",
						(rsm.getString("NR_PLACA")!= null)?rsm.getString("NR_PLACA"):"",
						(rsm.getString("NR_CHASSI")!= null)?rsm.getString("NR_CHASSI"):"",
						(rsm.getString("NR_ANO_FABRICACAO")!= null)?rsm.getString("NR_ANO_FABRICACAO"):"",
						(rsm.getString("NR_ANO_LICENCIAMENTO")!= null)?rsm.getString("NR_ANO_LICENCIAMENTO"):"",
						(rsm.getString("NM_CIDADE")!= null)?rsm.getString("NM_CIDADE"):"",
						(rsm.getString("SG_ESTADO")!= null)?rsm.getString("SG_ESTADO"):"",
						(rsm.getString("TP_ADAPTACAO")!= null)?VeiculoServices.tipoAdaptado[rsm.getInt("TP_ADAPTACAO")]:"",
						(rsm.getString("DT_INICIO_OPERACAO")!= null)?rsm.getDateFormat("DT_INICIO_OPERACAO", "dd/MM/yyyy"):"",
						(rsm.getString("DT_FINAL_OPERACAO")!= null)?rsm.getDateFormat("DT_FINAL_OPERACAO", "dd/MM/yyyy"):""
												
					}
				);
				
			}
			
			Integer[] columnsWidths = new Integer[] {280, 60, 60, 60, 100, 60, 60, 120, 30, 80, 80, 80};
			String titulo ="Relatório de Veículos ";
			String fileName = "RELATORIO_GERAL_VEICULOS_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
			return gerarBasicXls(dados, columnsWidths, titulo, fileName);
		}
	 	
	 	public static Result gerarRelatorioDinamico(ResultSetMap rsm, FiltroRelatorio relatorio, Empresa empresa, Pessoa pessoa, String txtAssinatura){
			try {
				ArrayList<Object[]> dados = new ArrayList<Object[]>();
				
				JSONArray jsonCampos = new JSONArray(relatorio.getJsonFiltroRelatorio());
				
				String[] columns 		= new String[jsonCampos.length()];
				String[] fieldId 		= new String[jsonCampos.length()];
				Integer[] columnsWidths = new Integer[jsonCampos.length()];
				int[] fieldType 		= new int[jsonCampos.length()];
				
				for (int i=0; i<jsonCampos.length(); i++) {
					JSONObject obj = jsonCampos.getJSONObject(i);
					
					columns[i]   	 = obj.getString("NM_CAMPO");
					fieldId[i] 	 	 = obj.getString("ID_CAMPO");
					fieldType[i] 	 = obj.getInt("TP_CAMPO");
					columnsWidths[i] = obj.getInt("VL_COLUNA");
				}
				dados.add(columns);

				while(rsm.next()){
					Object[] line = new Object[jsonCampos.length()];
					
					for (int i=0; i<fieldId.length; i++) {
						Object value = null;
						switch (fieldType[i]) {
							case FiltroCampoServices.TP_CAMPO_DATE:
								value = rsm.getDateFormat(fieldId[i], "dd/MM/yyyy");
								break;
							case FiltroCampoServices.TP_CAMPO_DOUBLE:
								value = rsm.getDouble(fieldId[i]);
								break;
							case FiltroCampoServices.TP_CAMPO_FLOAT:
								value = rsm.getFloat(fieldId[i]);
								break;
							case FiltroCampoServices.TP_CAMPO_INTEGER:
								value = rsm.getInt(fieldId[i]);
								break;
							case FiltroCampoServices.TP_CAMPO_STRING:
								value = rsm.getString(fieldId[i]);
								break;
							default:
								value = rsm.getObject(fieldId[i]);
								break;
						}

						line[i] = value;					
						
					}
					
					dados.add(line);
				} 
				
				String[] rodape = new String[5];
				Cidade cidade = CidadeDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_CIDADE_END_DEFAULT", 0, empresa.getCdEmpresa(), null));
				rodape[0] = (cidade!=null? cidade.getNmCidade()+", " : "")+Util.formatDate(new GregorianCalendar(), "d 'de' MMMM 'de' yyyy");
				rodape[3] = txtAssinatura;
				if(pessoa!=null) {				
					rodape[4] = "Emitido por "+pessoa.getNmPessoa()+" em "+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy 'às' HH:mm");
				}
				
				String titulo = empresa.getNmPessoa()+" \n"+relatorio.getNmRelatorio();
				String fileName = relatorio.getNmRelatorio().replaceAll(" ", "_") + "_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
				return gerarBasicXls(dados, columnsWidths, titulo, fileName, rodape);
			}
			catch(Exception e) {
				return null;
			}
		}
	 	
	 	public static Result gerarRelatorioArquivoXls(ResultSetMap rsm){
	 		
			ArrayList<Object[]> dados = new ArrayList<Object[]>();
			dados.add(new Object[] {
					"Arquivado em", 
					"Nome do Doc.", 
					"Arquivo", 
					"Nº do Processo", 
					"Cliente", 
					"Adverso", 
					"Tipo de Processo", 
					"Dt. Andamento", 
					"Tipo de Andamento"}
			);
			
			while(rsm.next()){
				dados.add(new Object[]{
						(rsm.getObject("DT_ARQUIVAMENTO")!= null)?rsm.getDateFormat("DT_ARQUIVAMENTO", "dd/MM/yyyy"):"",
						(rsm.getString("NM_DOCUMENTO")!= null)?rsm.getString("NM_DOCUMENTO"):"",
						(rsm.getString("NM_ARQUIVO")!= null)?rsm.getString("NM_ARQUIVO"):"",
						(rsm.getString("NR_PROCESSO")!= null)?rsm.getString("NR_PROCESSO"):"",
						(rsm.getString("NM_REPRESENTADO")!= null)?rsm.getString("NM_REPRESENTADO"):"",
						(rsm.getString("NM_ADVERSO")!= null)?rsm.getString("NM_ADVERSO"):"",
						(rsm.getString("NM_TIPO_PROCESSO")!= null)?rsm.getString("NM_TIPO_PROCESSO"):"",
						(rsm.getObject("DT_ANDAMENTO")!= null)?rsm.getDateFormat("DT_ANDAMENTO", "dd/MM/yyyy"):"",
						(rsm.getString("NM_TIPO_ANDAMENTO")!= null)?rsm.getString("NM_TIPO_ANDAMENTO"):""
					}
				);
			}
			
			Integer[] columnsWidths = new Integer[] {80, 200, 250, 120, 200, 200, 150, 80, 150};
			String titulo ="Lista de Arquivos";
			String fileName = "RELATORIO_ARQUIVOS_" + Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss")+".xls";
			return gerarBasicXls(dados, columnsWidths, titulo, fileName);
		}
}
