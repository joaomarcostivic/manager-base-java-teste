package com.tivic.manager.wsdl.detran.mg.soap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;
import com.tivic.manager.wsdl.interfaces.Elemento;

public class ElementoMg implements Elemento {

	SOAPElement element;
	
	List<Elemento> childs;
	
	public ElementoMg(SOAPElement element, String texto) throws SOAPException{
		childs = new ArrayList<Elemento>();
		this.element = element;
		this.element.addTextNode(texto != null ? texto : "");
	}
		
	@Override
	public Elemento addChild(String campo, DadosItem dadosItem){
		try {
			
			if(permitirInsercao(dadosItem)){
				ElementoMg elemento = new ElementoMg(element.addChildElement(campo), dadosItem.getValor());
				childs.add(elemento);
				return elemento;
			}
			
			return null;
			
		} catch (SOAPException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private boolean permitirInsercao(DadosItem dadosItem){
		return dadosItem.isObrigatorio() || dadosItem.getValor() != null;
	}

}
