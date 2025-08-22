package com.tivic.manager.mob.radar;

import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.log.console.ConsoleLogger;
import com.tivic.manager.mob.MedicaoTrafegoServices;
import com.tivic.manager.mob.ViaServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;


public class TrafegoMonitor {

	private static ArrayList<MedicaoTrafego> medicoes = null;
	private static int nrMinutosMedicao = 0;
	
	public static void start() {
		start(0);
	}
	
	public static void start(int minMedicao) {
		System.out.println("\tIniciando Monitor de Tráfego...");
		medicoes = new ArrayList<MedicaoTrafego>();
		nrMinutosMedicao = minMedicao;
	}
	
	public static boolean started() {
		return medicoes!=null;
			
	}
	
	public static void addMedicaoTrafego(MedicaoTrafego medicao) {
		
		if(!started())
			TrafegoMonitor.start();
				
		if(nrMinutosMedicao>0) { //medicoes agrupadas a cada 'nrMinutosMedicao'
			GregorianCalendar hrInicial = (GregorianCalendar)medicao.getDtInicialMedicao().clone();
			GregorianCalendar hrFinal = (GregorianCalendar)medicao.getDtInicialMedicao().clone();
			
			int gpMin = (int)Math.ceil(medicao.getDtInicialMedicao().get(GregorianCalendar.MINUTE) / nrMinutosMedicao);
			int vlMin = nrMinutosMedicao * gpMin;
			
			hrInicial.set(GregorianCalendar.MINUTE, vlMin);
			hrInicial.set(GregorianCalendar.SECOND, 0);
			hrInicial.set(GregorianCalendar.MILLISECOND, 0);
			
			hrFinal.set(GregorianCalendar.MINUTE, vlMin+nrMinutosMedicao);
			hrFinal.set(GregorianCalendar.SECOND, 0);
			hrFinal.set(GregorianCalendar.MILLISECOND, 0);
			
			medicao.setDtInicialMedicao(hrInicial);
			medicao.setDtFinalMedicao(hrFinal);
			
			
			boolean found = false;
			for (MedicaoTrafego m : medicoes) {
				if( m.getIdEquipamento().equals(medicao.getIdEquipamento()) && //mesmo Equipamento
					m.getNmVia().equals(medicao.getNmVia()) && //mesma via
					m.getNrPista() == medicao.getNrPista() && //mesma pista
					m.getTpVeiculo() == medicao.getTpVeiculo() && //mesmo tipo de veiculo
					m.getDtInicialMedicao().getTimeInMillis() == medicao.getDtInicialMedicao().getTimeInMillis() ) {  //mesma faixa de horario 
					
					found = true;
					m.setQtVeiculos(m.getQtVeiculos()+medicao.getQtVeiculos());
					break;
				}
			}
			
			if(!found) {
				medicoes.add(medicao);
			}
			
		}
		else {
			medicoes.add(medicao);
		}
	}
	
	synchronized public static String getJSON() {
		
		String json = "[";
		
		for (MedicaoTrafego m : medicoes) {
			json += "{";
			
				json += "ID: '" + m.getIdEquipamento() + "', ";
				json += "VIA: '" + m.getNmVia() + "', ";
				json += "PISTA: '" + m.getNrPista() + "', ";
				json += "TIPO_VEICULO: '" + m.getTpVeiculo() + "', ";
				json += "DT_INICIAL: '" + Util.formatDate(m.getDtInicialMedicao(), "dd/MM/yyyy HH:mm") +"', ";
				json += "DT_FINAL: '" + Util.formatDate(m.getDtFinalMedicao(), "dd/MM/yyyy HH:mm") +"', ";
				json += "QT_VEICULOS: " + m.getQtVeiculos();
				
			json += "}";
			
			if(medicoes.indexOf(m) < medicoes.size()-1)
				json += ", ";
		}
		
		json += "]";
		return json;
		
		//return new JSONObject(medicoes).toString();
	}
	
	public static void print() {
		System.out.println(getJSON());
	}
	
	synchronized public static Result save() {

		List<MedicaoTrafego> list = (ArrayList<MedicaoTrafego>) medicoes.clone();
		
		try {
			int cdVia = 0;
			
			System.out.println("================================================================================");
			System.out.println("PERSISTINDO TrafegoMonitor\t"+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy hh:mm:ss"));
			System.out.println(list.size() + " itens...");
			System.out.println("================================================================================");
			
			medicoes = new ArrayList<MedicaoTrafego>();
						
			ArrayList<com.tivic.manager.mob.MedicaoTrafego> parsedList = new ArrayList<com.tivic.manager.mob.MedicaoTrafego>();
			
			for (MedicaoTrafego item : list) {
				Equipamento equipamento = EquipamentoServices.getByIdEquipamento(item.getIdEquipamento());			
				ResultSetMap rsm = ViaServices.find(new Criterios("A.nm_via", item.getNmVia(), ItemComparator.LIKE, Types.VARCHAR));
								
				if(rsm.next()) {
					cdVia = rsm.getInt("CD_VIA");
				}
				
				parsedList.add(new com.tivic.manager.mob.MedicaoTrafego(0, 
						equipamento.getCdEquipamento(), //cdEquipamento, 
						cdVia, //cdVia, 
						item.getNrPista(), //cdFaixa, 
						item.getDtInicialMedicao(), //dtInicial, 
						item.getDtFinalMedicao(), //dtFinal, 
						item.getTpVeiculo(), //tpVeiculo, 
						item.getQtVeiculos(), //qtVeiculos, 
						0.0d, //vlVelocidadeConsiderada, 
						0.0d, //vlVelocidadeLimite, 
						0.0d, //vlVelocidadeMedida, 
						0.0d, //vlVelocidadeTolerada, 
						0.0d //vlComprimentoVeiculo
					));
			}
			
			Result r = MedicaoTrafegoServices.save(parsedList);
						
			return r;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			medicoes.addAll(0, list);
			return null;
		}
		
	}
	
}

class MedicaoTrafego {

	private GregorianCalendar dtInicialMedicao;
	private GregorianCalendar dtFinalMedicao;
	private String idEquipamento;
	private String nmVia;
	private int nrPista;
	private int tpVeiculo;
	private int qtVeiculos;
	
	public MedicaoTrafego() { }

	public MedicaoTrafego(GregorianCalendar dtInicialMedicao, 
								GregorianCalendar dtFinalMedicao,
								String idEquipamento, 
								String nmVia, 
								int nrPista, 
								int tpVeiculo,
								int qtVeiculos) {
		setDtInicialMedicao(dtInicialMedicao);
		setDtFinalMedicao(dtFinalMedicao);
		setIdEquipamento(idEquipamento);
		setNmVia(nmVia);
		setNrPista(nrPista);
		setTpVeiculo(tpVeiculo);
		setQtVeiculos(qtVeiculos);
	}

	public GregorianCalendar getDtInicialMedicao() {
		return dtInicialMedicao;
	}

	public void setDtInicialMedicao(GregorianCalendar dtInicialMedicao) {
		this.dtInicialMedicao = dtInicialMedicao;
	}

	public GregorianCalendar getDtFinalMedicao() {
		return dtFinalMedicao;
	}

	public void setDtFinalMedicao(GregorianCalendar dtFinalMedicao) {
		this.dtFinalMedicao = dtFinalMedicao;
	}

	public String getIdEquipamento() {
		return idEquipamento;
	}

	public void setIdEquipamento(String idEquipamento) {
		this.idEquipamento = idEquipamento;
	}

	public String getNmVia() {
		return nmVia;
	}

	public void setNmVia(String nmVia) {
		this.nmVia = nmVia;
	}

	public int getNrPista() {
		return nrPista;
	}

	public void setNrPista(int nrPista) {
		this.nrPista = nrPista;
	}

	public int getTpVeiculo() {
		return tpVeiculo;
	}

	public void setTpVeiculo(int tpVeiculo) {
		this.tpVeiculo = tpVeiculo;
	}

	public int getQtVeiculos() {
		return qtVeiculos;
	}

	public void setQtVeiculos(int qtVeiculos) {
		this.qtVeiculos = qtVeiculos;
	}
}
