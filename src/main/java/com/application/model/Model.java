package com.application.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table(name = "MODEL")
@Entity
public class Model {

    public Model(final String name){
        this.name = name;
    }

    @Id
    private UUID id = UUID.randomUUID();

    private String name;

}
