package com.tivic.manager.seg;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="acoes")
public class Acoes {
	
	List<Acao> acoes;
	
	public Acoes() {
		acoes = new ArrayList<Acao>();
	}

	@XmlElement(name="acao")
	public void setAcoes(List<Acao> acoes) {
		this.acoes = acoes;
	}
	
	public List<Acao> getAcoes() {
		return acoes;
	}
	
	@Override
	public String toString() {
		
		String acoes = "{";
		
		for(Acao objetoAcao : this.acoes){
			acoes += objetoAcao + ", ";
		}
		
		if(acoes.length() > 2)
			acoes = acoes.substring(0, acoes.length()-2);
		
		acoes += "}";	
			
		return acoes;
	}
}
