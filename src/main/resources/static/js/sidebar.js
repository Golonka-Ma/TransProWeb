
    /**
    * Toggles the given submenu and (optionally) flips the chevron.
    * @param {string} subMenuId - The ID of the submenu <ul>.
    * @param {string} [chevronId] - The ID of the <i> element for the arrow.
        */
        function toggleSubMenu(subMenuId, chevronId) {
            const submenu = document.getElementById(subMenuId);
            if (!submenu) return;

            submenu.classList.toggle('hidden');

            // If there's a chevron ID
            if (chevronId) {
            const chevron = document.getElementById(chevronId);
            if (chevron) {
            chevron.classList.toggle('rotate-180');
        }
        }
        }

        // Example theme toggling logic
        function toggleTheme() {
            const htmlElement = document.documentElement;
            htmlElement.classList.toggle('dark');

            const isDarkMode = htmlElement.classList.contains('dark');
            const icon = document.getElementById('theme-icon');
            const themeText = document.getElementById('theme-text');

            if (icon) {
            icon.classList.toggle('fa-sun', isDarkMode);
            icon.classList.toggle('fa-moon', !isDarkMode);
        }
            if (themeText) {
            themeText.textContent = isDarkMode ? 'Tryb jasny' : 'Tryb ciemny';
        }
            localStorage.setItem('isDarkMode', isDarkMode);
        }

        // Optional: load dark mode preference on page load
        document.addEventListener('DOMContentLoaded', () => {
            const isDarkMode = localStorage.getItem('isDarkMode') === 'true';
            if (isDarkMode) {
            document.documentElement.classList.add('dark');
            const icon = document.getElementById('theme-icon');
            if (icon) {
            icon.classList.toggle('fa-sun', true);
            icon.classList.toggle('fa-moon', false);
        }
            const themeText = document.getElementById('theme-text');
            if (themeText) {
            themeText.textContent = 'Tryb jasny';
        }
        }
        });