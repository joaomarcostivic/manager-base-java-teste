package com.tivic.manager.mob.v2.ait;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitProprietario;

public class AitProprietarioFactory {

	public static AitProprietario factory(Ait ait) {
		AitProprietario aitProprietario = new AitProprietario();
		aitProprietario.setNmProprietario(ait.getNmProprietario());
		return aitProprietario;
	}
}
