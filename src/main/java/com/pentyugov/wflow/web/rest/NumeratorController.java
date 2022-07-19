package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.service.NumeratorService;
import com.pentyugov.wflow.web.payload.response.NumeratorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/numerators")
public class NumeratorController extends AbstractController {

    private final NumeratorService numeratorService;

    public NumeratorController(NumeratorService numeratorService) {
        this.numeratorService = numeratorService;
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam(name = "type") String type,
                                                @RequestParam(name = "order") String order) {
        String nextNumber = "";
        if (NumeratorService.ORDER_NEXT.equals(order.toUpperCase())) {
            nextNumber = numeratorService.getNextNumber(type);
        }
        return new ResponseEntity<>(new NumeratorResponse(nextNumber), HttpStatus.OK);
    }
}
