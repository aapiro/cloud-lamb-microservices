package com.cloudlamb.homework.ordenes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RefreshScope
@RestController
public class OrdenesController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/ordenes")
    public Orden getOrdenes(){

        // Puede que estos valores se obtengan dede una base de datos
        Orden orden = new Orden();
        orden.setId(4321L);
        orden.setNombre("Nombre de orden");
        orden.setTipo("Tipo de orden");

        // Preparamos las configuraciones para hacer la llamada HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Pago> entity = new HttpEntity<Pago>(headers);

        // Con este resttemplate podemos obtener un recurso conectandonos a otro microservicio
        // por HTTP
        Pago pago = restTemplate.exchange("http://localhost:8010/pagos",
                HttpMethod.GET,entity,Pago.class).getBody();


        orden.setPago(pago);

        return orden;

    }
}
