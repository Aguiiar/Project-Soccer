package com.example.soccer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Nome é obrigatório")
	private String name;

	@NotNull(message = "Idade é obrigatório")
	@Min(value = 1, message = "Idade deve ser maior que 0")
	@Max(value = 50, message = "Idade deve ser no máximo 50")
	private Integer age;

	@NotBlank(message = "Posição é obrigatória")
	private String position;

	@NotBlank(message = "Pé é obrigatória")
	private String foot;

	@ManyToOne
	@NotNull(message = "Time é obrigatório")
	private Team team;

	public Player() {
		super();
	}

	public Player(Long id, String name, Integer age, String position, String foot, Team team) {

		this.id = id;
		this.name = name;
		this.age = age;
		this.position = position;
		this.foot = foot;
		this.team = team;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getFoot() {
		return foot;
	}

	public void setFoot(String foot) {
		this.foot = foot;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

} 