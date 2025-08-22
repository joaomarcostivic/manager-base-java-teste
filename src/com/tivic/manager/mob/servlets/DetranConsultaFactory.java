package com.tivic.manager.mob.servlets;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.conf.ManagerConf;

public class DetranConsultaFactory {

	public static List<DetranConsulta> createServerPriority() {
		List<DetranConsulta> lista = new ArrayList<DetranConsulta>();
		lista.add(new DetranConsultaBahia());
		lista.add(new DetranConsultaMinas());
		lista.add(new DetranConsultaBdv());
		return lista;
	}
	
	public static List<DetranConsulta> createLocalPriority() {
		List<DetranConsulta> lista = new ArrayList<DetranConsulta>();
		lista.add(new DetranConsultaBdv());
		lista.add(new DetranConsultaBahia());
		lista.add(new DetranConsultaMinas());
		return lista;
	}
	
}
