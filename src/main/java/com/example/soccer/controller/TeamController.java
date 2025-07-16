package com.example.soccer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.soccer.PlayerRepository;
import com.example.soccer.TeamRepository;
import com.example.soccer.model.Team;

import jakarta.validation.Valid;

@Controller
public class TeamController {

    private final PlayerRepository playerRepository;

	private final TeamRepository teamRepository;

	public TeamController(TeamRepository teamRepository, PlayerRepository playerRepository) {

		this.teamRepository = teamRepository;

		this.playerRepository = playerRepository;
	}

	@GetMapping("/team")
	public String index(Model model) {
		model.addAttribute("team", new Team());
		model.addAttribute("teams", teamRepository.findAll());
		return "new-team";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("team") Team team, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("teams", teamRepository.findAll());
			return "new-team";
		}
		teamRepository.save(team);
		return "redirect:/team";

	}

	@PostMapping("/team/delete/{id}")
	public String deleteTeam(@PathVariable Long id) {
		teamRepository.deleteById(id);
		return "redirect:/team";
	}
	
	@GetMapping("/team/edit/{id}")
	public String editTeam(@PathVariable Long id, Model model) {
		Team team = teamRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Time não encontrado" + id));
		model.addAttribute("team", team);
		model.addAttribute("teams", teamRepository.findAll());
		return "new-team";
	}
	
	@PostMapping("/team/update/{id}")
	public String updateTeam(@PathVariable Long id, @Valid @ModelAttribute("team") Team team, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			model.addAttribute("teams", teamRepository.findAll());
			return "new-team";
		}
		
		team.setId(id);
		teamRepository.save(team);
		return "redirect:/team";
	}
} 