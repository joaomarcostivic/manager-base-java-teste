package com.tivic.manager.grl;

import java.util.List;

public class FormularioDTO extends Formulario {
	
	public FormularioDTO(Formulario form) {
		super(form.getCdFormulario(), form.getNmFormulario(), form.getIdFormulario(), form.getDtVersao(), form.getStFormulario(), 
				form.getDsFormulario(), form.getNmLinkFormulario(), form.getDtInicioFormulario(), form.getDtFimFormulario());
	}

	List<FormularioAtributo> atributos;

	public List<FormularioAtributo> getAtributos() {
		return atributos;
	}

	public void setAtributos(List<FormularioAtributo> atributos) {
		this.atributos = atributos;
	}
	
}
