package com.example.soccer.controller;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.soccer.PlayerRepository;
import com.example.soccer.TeamRepository;
import com.example.soccer.model.Player;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@Controller
public class PlayerController {

	private final PlayerRepository playerRepository;
	private final TeamRepository teamRepository;

	public PlayerController(PlayerRepository playerRepository, TeamRepository teamRepository) {

		this.playerRepository = playerRepository;
		this.teamRepository = teamRepository;
	}

	@GetMapping("/player")
	public String listPlayer(org.springframework.ui.Model model) {
		model.addAttribute("player", new Player());
		model.addAttribute("players", playerRepository.findAll());
		model.addAttribute("teams", teamRepository.findAll());

		try {
			ObjectMapper mapper = new ObjectMapper();
			InputStream is = new ClassPathResource("positions.json").getInputStream();
			List<String> positions = mapper.readValue(is, new TypeReference<List<String>>() {
			});
			model.addAttribute("positions", positions);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "new-player";
	}

	@PostMapping("/player/save")
	public String save(@Valid @ModelAttribute("player") Player player, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("players", playerRepository.findAll());
			model.addAttribute("teams", teamRepository.findAll());

			try {
				ObjectMapper mapper = new ObjectMapper();
				InputStream is = new ClassPathResource("positions.json").getInputStream();
				List<String> positions = mapper.readValue(is, new TypeReference<List<String>>() {
				});
				model.addAttribute("positions", positions);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "new-player";
		}

		playerRepository.save(player);
		return "redirect:/player";
	}

	@GetMapping("/player/edit/{id}")
	public String editPlayer(@PathVariable Long id, Model model) {
		Player player = playerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Jogador não encontrado: " + id));
		model.addAttribute("player", player);
		model.addAttribute("teams", teamRepository.findAll());
		model.addAttribute("players", playerRepository.findAll());

		try {
			ObjectMapper mapper = new ObjectMapper();
			InputStream is = new ClassPathResource("positions.json").getInputStream();
			List<String> positions = mapper.readValue(is, new TypeReference<List<String>>() {
			});
			model.addAttribute("positions", positions);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "new-player";

	}

	@PostMapping("/player/delete/{id}")
	public String deletePlayer(@PathVariable Long id) {
		playerRepository.deleteById(id);
		return "redirect:/player";
	}

	@GetMapping("/player/search")
	public String searchPlayers(Model model, @RequestParam(required = false) String searchTerm) {
		List<Player> filteredPlayers;

		if (searchTerm != null && !searchTerm.isEmpty()) {
			filteredPlayers = playerRepository.findAll().stream()
					.filter(p -> p.getName().toLowerCase().contains(searchTerm.toLowerCase()))
					.collect(Collectors.toList());
		} else {
			filteredPlayers = playerRepository.findAll();
		}

		model.addAttribute("players", filteredPlayers);
		model.addAttribute("teams", teamRepository.findAll());
		model.addAttribute("player", new Player());
		model.addAttribute("searchTerm", searchTerm);
		return "new-player";
	}

	@GetMapping("/player/filter")
	public String filterPlayers(Model model, @RequestParam(required = false) List<Long> teamIds,
			@RequestParam(required = false) List<String> feet, @RequestParam(required = false) String order) {
		List<Player> filtered = playerRepository.findAll();

		if (teamIds != null && !teamIds.isEmpty()) {
			filtered = filtered.stream().filter(p -> p.getTeam() != null && teamIds.contains(p.getTeam().getId()))
					.collect(Collectors.toList());
		}

		if (feet != null && !feet.isEmpty()) {
			filtered = filtered.stream().filter(p -> feet.contains(p.getFoot())).collect(Collectors.toList());
		}

		if ("asc".equalsIgnoreCase(order)) {
			filtered.sort(Comparator.comparingInt(Player::getAge));

		} else if ("desc".equalsIgnoreCase(order)) {
			filtered.sort(Comparator.comparingInt(Player::getAge).reversed());
		}

		model.addAttribute("players", filtered);
		model.addAttribute("teams", teamRepository.findAll());
		model.addAttribute("player", new Player());

		return "new-player";
	}

	@GetMapping("/player/suggestions")
	@ResponseBody
	public List<String> getSuggestions(@RequestParam("term") String term) {
		return playerRepository.findAll().stream()
				.filter(player -> player.getName().toLowerCase().contains(term.toLowerCase())).map(Player::getName)
				.collect(Collectors.toList());
	}

}