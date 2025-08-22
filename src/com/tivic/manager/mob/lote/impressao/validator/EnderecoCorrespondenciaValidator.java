package com.tivic.manager.mob.lote.impressao.validator;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoException;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class EnderecoCorrespondenciaValidator implements Validator<Ait> {
	
	private InconsistenciaRepository inconsistenciaRepository;
	private IParametroRepository parametroRepository;
	
	public EnderecoCorrespondenciaValidator() throws Exception {
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
    @Override
    public void validate(Ait ait, CustomConnection customConnection) throws Exception {
        StringBuilder erros = new StringBuilder();
        
        if (ait.getCdBairro() <= 0) {
            erros.append("Bairro do proprietário não informado.\n");
        }
        if (ait.getDsLogradouro() == null || ait.getDsLogradouro().trim().equals("")) {
            erros.append("Logradouro do proprietário não informado.\n");
        }
        if (ait.getNrCep() == null || ait.getNrCep().trim().equals("")) {
            erros.append("CEP do proprietário não informado.\n");
        }
        if (ait.getCdCidadeProprietario() <= 0) {
            erros.append("Cidade do proprietário não informada.\n");
        }
        if (erros.length() > 0) {
        	int tpSemEnderecoCorrespondencia = parametroRepository.getValorOfParametroAsInt("MOB_ENDERECO_NOTIFICACAO");
			Inconsistencia inconsistencia = inconsistenciaRepository.get(tpSemEnderecoCorrespondencia);
            throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), "O AIT " + ait.getIdAit() + " apresenta os seguintes problemas:\n" + erros.toString());
        }
    }
}
