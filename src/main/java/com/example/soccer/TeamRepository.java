package com.example.soccer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.soccer.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
