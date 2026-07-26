package org.gameflix.catalog.repository;

import org.gameflix.catalog.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByActiveTrueOrderByTitleAsc();

    Optional<Game> findByIdAndActiveTrue(Long id);
}