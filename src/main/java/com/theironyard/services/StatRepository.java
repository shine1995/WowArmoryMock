package com.theironyard.services;

import com.theironyard.entities.Stat;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Blake on 10/18/16.
 */
public interface StatRepository extends CrudRepository<Stat, Integer> {

    Stat findByStatName (String statName);
}
