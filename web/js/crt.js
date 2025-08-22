function miMotivoOnClick() {
	FormFactory.createQuickForm('jMotivo', //janela
	                                     {caption: 'Motivos de Cancelamento', width: 600, height: 400, unitSize: '%',
										  //quickForm
										  id: "crt_motivo", classDAO: 'com.tivic.manager.crt.MotivoDAO', keysFields: ['cd_motivo'],
										  constructorFields: [{reference: 'cd_motivo', type: 'int'},
															  {reference: 'nm_motivo', type: 'java.lang.String'}],
										  gridOptions: {columns: [{reference: 'nm_motivo', label: 'Motivo de Cancelamento'}],
														strippedLines: true, columnSeparator: false, lineSeparator: false},
										  lines: [[{reference: 'nm_motivo', label: 'Motivo do Cancelamento', width:100, maxLength:50}]]});
}

