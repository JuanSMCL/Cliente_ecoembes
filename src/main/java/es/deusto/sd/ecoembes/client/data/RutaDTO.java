package es.deusto.sd.ecoembes.client.data;

import java.util.List;

public record RutaDTO(

    List<Long> idsContenedores,

    Long idCamion,

    String idPlantaReciclaje
){}
