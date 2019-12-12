package com.cloudlamb.homework.pagos;

import com.netflix.client.http.HttpHeaders;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class PagosController {

    @Value("${message:HOLA LOCALHOST}")
    private String message;

    @RequestMapping("/pagos")
    public Pago getPagos() {

        Pago pago = new Pago();
        pago.setId(1234L);
        pago.setNombre("Nombre Pago");
        pago.setTipo("POS");


        return pago;

    }


}

