package com.pmrodrigues.rpg.repositories;

import com.pmrodrigues.rpg.entities.Rule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RuleRepository extends CrudRepository<Rule, UUID> {
}
