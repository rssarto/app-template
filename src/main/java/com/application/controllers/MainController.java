package com.application.controllers;

import com.application.model.Model;
import com.application.services.ModelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/main-controller")
public class MainController {

    private final ModelService modelService;

    @PostMapping("/model")
    public ResponseEntity<Model> create(@RequestBody final Model model){
        log.info("received model: {}", model);
        return new ResponseEntity<>(this.modelService.create(model), HttpStatus.OK);
    }

    @GetMapping("/model/{id}")
    public ResponseEntity<Model> findById(@PathVariable  final UUID id){
        return new ResponseEntity<>(this.modelService.findById(id), HttpStatus.OK);
    }

}
