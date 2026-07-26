package org.gameflix.catalog.service;

import org.gameflix.catalog.model.Game;
import org.gameflix.catalog.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {

    private final GameRepository gameRepository;

    public CatalogService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Transactional(readOnly = true)
    public List<Game> listPublicGames() {
        return gameRepository.findByActiveTrueOrderByTitleAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Game> findPublicGame(Long id) {
        return gameRepository.findByIdAndActiveTrue(id);
    }
}