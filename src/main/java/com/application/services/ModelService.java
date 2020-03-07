package com.application.services;

import com.application.model.Model;

import java.util.UUID;

public interface ModelService {

    Model create(Model model);

    Model findById(UUID id);
}
