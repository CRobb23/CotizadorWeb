package com.digitalgeko.servicebus.incoming.soap;

import com.digitalgeko.servicebus.service.AutoInspectionsBus;
import corpbi.UpdateInspectionStateRequest;
import corpbi.UpdateInspectionStateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class AutoInspectionServiceSoapInbound {

    private static final String NAMESPACE_URI = "CorpBI";
    private static final String DUMMY_MESSAGE = "<ActualizacionAutoInspeccion><NoCaso>20181102001</NoCaso><Estado>Finalizada</Estado><NoInspeccion>001</NoInspeccion><FechaInspeccion>03072018</FechaInspeccion><DanoExistente>XXXXX</DanoExistente><EquipoEspecial>XXXXX</EquipoEspecial><Placa>P-123BDC</Placa><Motor>2606E2018</Motor><Chassis>231T321321A</Chassis><Observacion>Pruebas</Observacion></ActualizacionAutoInspeccion>";
    private static final String DUMMY_RESPONSE = "486<ActualizacionAutoInspeccion><Mensaje>SATISFACTORIO</Mensaje></ActualizacionAutoInspeccion>";

    @Autowired
    private AutoInspectionsBus autoInspectionsBus;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateInspectionStateRequest")
    @ResponsePayload
    public UpdateInspectionStateResponse updateInspectionState(@RequestPayload UpdateInspectionStateRequest request) {
        String responseMessage = autoInspectionsBus.updateInspectionState(request.getMessage());
        //String responseMessage = DUMMY_RESPONSE;
        UpdateInspectionStateResponse response = new UpdateInspectionStateResponse();
        response.setMessage(responseMessage);
        return response;
    }
}
