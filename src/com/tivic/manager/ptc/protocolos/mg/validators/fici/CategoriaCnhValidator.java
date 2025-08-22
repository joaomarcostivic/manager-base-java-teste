package com.tivic.manager.ptc.protocolos.mg.validators.fici;

import java.util.Arrays;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.TipoVeiculoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.sol.connection.CustomConnection;

public class CategoriaCnhValidator implements IValidator<DadosProtocoloDTO>{	
	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {	
		ApresentacaoCondutor fici = obj.getApresentacaoCondutor();
		
		if (checkValidacaoNecessaria(obj)) {
			return;
		}
		
		if(fici.getTpCategoriaCnh() < 0) {
			throw new ValidationException("É necessário informar a categoria da CNH.");
		}
		TipoVeiculo tipoVeiculo = TipoVeiculoDAO.get(obj.getAit().getCdTipoVeiculo(), connection.getConnection());
		
		if (tipoVeiculo.getTxtCnhRequerida() == null) {
			throw new ValidationException("Categoria da CNH não informada no tipo de veículo.");
		}
		
		String[] categoriasPermitidas = tipoVeiculo.getTxtCnhRequerida().split(",");

		if(Arrays.asList(categoriasPermitidas).stream().anyMatch(categoria -> categoria.equals(fici.getTpCategoriaCnh()))) {
            return;
        }
		
		throw new ValidationException("Categoria da CNH informada não é compatível com a categoria do veículo.");
	}
	
	private boolean checkValidacaoNecessaria(DadosProtocoloDTO obj) {
		int cdIndeferida = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INDEFERIDA",0,0);
		return obj.getCdFase() == cdIndeferida || obj.getApresentacaoCondutor().getTpModeloCnh() == TipoModeloCnhEnum.NAO_INFORMADO.getKey();
	}

}
