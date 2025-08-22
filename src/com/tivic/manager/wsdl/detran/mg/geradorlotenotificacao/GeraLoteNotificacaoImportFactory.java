package com.tivic.manager.wsdl.detran.mg.geradorlotenotificacao;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.sol.connection.CustomConnection;

public class GeraLoteNotificacaoImportFactory {

	public IGeraLoteNotificacaoImport getStrategy(int tpStatus, Ait ait, CustomConnection customConnection) throws Exception {
		if (tpStatus == TipoStatusEnum.NAI_ENVIADO.getKey()) {
			return new GeraLoteNotificacaoImport(ait, tpStatus, customConnection);
		} 
		else if (tpStatus == TipoStatusEnum.NIP_ENVIADA.getKey()) {
			return new GeraLoteNotificacaoImport(ait, tpStatus, customConnection);
		}

		return new IGeraLoteNotificacaoImport() {
			@Override
			public void build() throws Exception {}
		};
	}
	
}
