package com.application.services;

import com.application.model.Model;
import com.application.repositories.ModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;

    @Transactional
    @Override
    public Model create(Model model) {
        return modelRepository.save(model);
    }

    @Override
    public Model findById(UUID id) {
        final Optional<Model> optModel = this.modelRepository.findById(id);
        final Model model = optModel.orElseThrow(() -> new EntityNotFoundException(String.format("No Model found with id: %s", id)));
        return model;
    }
}
