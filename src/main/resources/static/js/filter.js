document.addEventListener("DOMContentLoaded", function() {
	const btnToggle = document.getElementById("btnToggleFilter");
	const filterMenu = document.getElementById("MenuFilter");

	if (btnToggle && filterMenu) {
		btnToggle.addEventListener("click", () => {
			filterMenu.classList.toggle("hidden");
		});

		document.addEventListener("click", (event) => {
			if (!filterMenu.contains(event.target) && event.target !== btnToggle) {
				filterMenu.classList.add("hidden");
			}
		})
	}
})

document.addEventListener("DOMContentLoaded", function() {

	const searchInput = document.getElementById("searchPlayer");
	const suggestionsBox = document.getElementById("suggestions");

	function getSuggestions(query) {


		if (query.length <= 2) {
			suggestionsBox.style.display = 'none';
			suggestionsBox.innerHTML = '';
			return;

		}

		fetch(`/player/suggestions?term=${encodeURIComponent(query)}`).then(response => {
			if (!response.ok) throw new Error(`Error HTTP! status: ${response.status}`);
			return response.json();
		}).then(data => {
			suggestionsBox.innerHTML = '';
			if (data.length === 0) {
				suggestionsBox.style.display = 'none';
				return;
			}

			data.forEach(name => {
				const div = document.createElement('div');
				div.textContent = name;
				div.addEventListener('click', () => {
					searchInput.value = name;
					suggestionsBox.style.display = 'none';
				});
				suggestionsBox.appendChild(div);
			})
			suggestionsBox.style.display = 'block';

		}).catch(error => {
			console.error('Suhhestions invalid', error);
			suggestionsBox.style.display = 'none';
		});
	}

	searchInput.addEventListener('input', function() {
		getSuggestions(this.value);

	});

	document.addEventListener('click', function(e) {
		if (!suggestionsBox.contains(e.target) && e.target !== searchInput) {
			suggestionsBox.style.display = 'none';
		}
	});

});

let btnMobile = document.getElementById('btnMobile');

function toggleMenu() {
	let nav = document.getElementById('menuMobile');
	nav.classList.toggle('active');
}


btnMobile.addEventListener('click', toggleMenu);