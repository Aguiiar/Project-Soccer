package com.example.soccer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.soccer.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
