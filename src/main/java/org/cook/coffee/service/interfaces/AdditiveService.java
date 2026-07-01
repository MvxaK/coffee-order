package org.cook.coffee.service.interfaces;

import org.cook.coffee.model.Additive;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdditiveService {

    Additive getById(Long id);
    List<Additive> getAll();
    Additive create(Additive additive);
    Additive update(Long id, Additive additive);
    void delete(Long id);

}
