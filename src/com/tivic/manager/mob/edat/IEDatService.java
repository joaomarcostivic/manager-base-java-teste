package com.tivic.manager.mob.edat;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.boat.Boat;
import com.tivic.manager.mob.boat.dto.BoatEstatisticaCategoriaVeiculoDTO;
import com.tivic.manager.mob.boat.dto.BoatEstatisticaCidadeDTO;
import com.tivic.manager.mob.boat.dto.BoatEstatisticaEspecieVeiculoDTO;
import com.tivic.manager.mob.boat.dto.BoatEstatisticaGeneroDTO;
import com.tivic.manager.mob.boat.dto.BoatEstatisticaIdadeDTO;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaCategoriaVeiculoDTO;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaCidadeDTO;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaTipoAcidenteDTO;
import com.tivic.manager.mob.edat.dto.EDatEstatisticaTipoVeiculoDTO;
import com.tivic.manager.mob.edat.dto.EdatEstatisticaEspecieVeiculoDTO;
import com.tivic.manager.mob.edat.dto.EdatEstatisticaGeneroDTO;
import com.tivic.manager.mob.edat.dto.EdatEstatisticaIdadeDTO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IEDatService {
	public Boat insert(Boat boat) throws BadRequestException, Exception;
	public Boat insert(Boat boat, CustomConnection customConnection) throws BadRequestException, Exception;
	public Boat update(Boat boat) throws Exception;
	public Boat update(Boat boat, CustomConnection customConnection) throws Exception;
	public Boat get(int cdBoat) throws Exception;
	public Boat get(int cdBoat, CustomConnection customConnection) throws Exception;
	public List<Boat> find(SearchCriterios searchCriterios) throws Exception;
	public List<Boat> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	

	public List<EdatEstatisticaGeneroDTO> getEstatisticaPorGenero(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<EdatEstatisticaIdadeDTO> getEstatisticaPorIdade(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<EDatEstatisticaCidadeDTO> getEstatisticaPorCidade(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

	public List<EdatEstatisticaEspecieVeiculoDTO> getEstatisticaEspecieVeiculo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<EDatEstatisticaCategoriaVeiculoDTO> getEstatisticaCategoriaVeiculo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

	public List<EDatEstatisticaTipoVeiculoDTO> getEstatisticaTipoVeiculo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<EDatEstatisticaTipoAcidenteDTO> getEstatisticaTipoAcidente(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
