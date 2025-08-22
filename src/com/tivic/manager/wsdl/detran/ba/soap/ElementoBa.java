package com.tivic.manager.wsdl.detran.ba.soap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;
import com.tivic.manager.wsdl.interfaces.Elemento;

public class ElementoBa implements Elemento {

	SOAPElement element;
	
	List<Elemento> childs;
	
	public ElementoBa(SOAPElement element, String texto) throws SOAPException{
		childs = new ArrayList<Elemento>();
		this.element = element;
		this.element.addTextNode(texto != null ? texto : "");
	}
		
	@Override
	public Elemento addChild(String campo, DadosItem dadosItem){
		try {
			
			if(permitirInsercao(dadosItem)){
				ElementoBa elemento = new ElementoBa(element.addChildElement(campo), dadosItem.getValor());
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
