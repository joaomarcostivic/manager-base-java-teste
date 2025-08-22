package com.tivic.manager.mob.mobile;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.mob.mobile.dto.QrCodeResponseDTO;
import com.tivic.manager.mob.mobile.dto.VinculacaoEquipamentoDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IEquipamentoTalonarioService {

	public VinculacaoEquipamentoDTO insert(Equipamento equipamento) throws BadRequestException, Exception;
	public VinculacaoEquipamentoDTO insert(Equipamento equipamento, CustomConnection customConnection) throws BadRequestException, Exception;
	public QrCodeResponseDTO gerarQrCodeComParametros(int cdEquipamento) throws Exception, ValidacaoException;
}
