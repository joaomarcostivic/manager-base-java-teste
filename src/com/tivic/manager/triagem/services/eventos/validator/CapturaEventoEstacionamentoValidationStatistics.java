package com.tivic.manager.triagem.services.eventos.validator;

public class CapturaEventoEstacionamentoValidationStatistics {
	
    private int totalProcessed = 0;
    private int successfullyPersisted = 0;
    private int failedByExistingEvent = 0;
    private int failedByMissingImages = 0;
    private int failedByMissingAddress = 0;
    
    public void incrementTotalProcessed() {
        totalProcessed++;
    }
    
    public void incrementSuccessfullyPersisted() {
        successfullyPersisted++;
    }
    
    public void incrementFailedByExistingEvent() {
        failedByExistingEvent++;
    }
    
    public void incrementFailedByMissingImages() {
        failedByMissingImages++;
    }
    
    public void incrementFailedByMissingAddress() {
        failedByMissingAddress++;
    }
    
    public String generateReport() {
        return String.format(
            "\nTotal de notificações processadas: %d" +
            "\nPersistidas com sucesso: %d" +
            "\nRejeitadas (evento existente): %d" +
            "\nRejeitadas (sem imagens): %d" +
            "\nRejeitadas (sem endereço): %d",
            totalProcessed, successfullyPersisted, 
            failedByExistingEvent, failedByMissingImages, failedByMissingAddress
        );
    }
}
