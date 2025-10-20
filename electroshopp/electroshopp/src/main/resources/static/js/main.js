document.addEventListener('DOMContentLoaded', () => {

    // --- 1. FUNCIÓN DE COLAPSADO DEL FOOTER (ACORDEÓN) ---
    // Esta lógica maneja la apertura/cierre de las listas del footer en móvil
    const footerToggles = document.querySelectorAll('.footer-toggle');

    footerToggles.forEach(toggle => {
        toggle.addEventListener('click', () => {

            // Solo activar la funcionalidad cuando sea vista móvil (<= 769px)
            if (window.innerWidth <= 769) {

                const targetId = toggle.getAttribute('data-target');
                const targetContent = document.getElementById(targetId);

                if (targetContent) {

                    // Lógica para cerrar todos los demás acordeones abiertos
                    document.querySelectorAll('.footer-content.is-active').forEach(openContent => {
                        if (openContent.id !== targetId) {
                            openContent.classList.remove('is-active');
                            // También quitamos la clase 'is-active' del título para que cambie el signo/flecha
                            const relatedToggle = document.querySelector(`[data-target="${openContent.id}"]`);
                            if (relatedToggle) {
                                relatedToggle.classList.remove('is-active');
                            }
                        }
                    });

                    // Alternar la clase 'is-active' en el contenido (para mostrar/ocultar)
                    targetContent.classList.toggle('is-active');

                    // Alternar la clase 'is-active' en el título (para rotar la flecha)
                    toggle.classList.toggle('is-active');
                }
            }
        });
    });

    // ------------------------------------------------------------------
    // --- 2. FUNCIÓN DE MENÚ HAMBURGUESA DESLIZABLE ---
    // ------------------------------------------------------------------
    const menuToggle = document.getElementById('menu-toggle');
    const mainMenuContainer = document.getElementById('main-menu');

    if (menuToggle && mainMenuContainer) {
        menuToggle.addEventListener('click', () => {
            mainMenuContainer.classList.toggle('is-open');
        });

        // Cierra el menú deslizable si el usuario hace clic en un enlace (navega)
        document.querySelectorAll('.main-nav a').forEach(link => {
            link.addEventListener('click', () => {
                if (window.innerWidth <= 769) {
                    mainMenuContainer.classList.remove('is-open');
                }
            });
        });
    }

    // --- 3. INTERACTIVIDAD VISUAL EN BARRA DE BÚSQUEDA ---
    const searchInput = document.querySelector('.search-bar input');
    const searchBarContainer = document.querySelector('.search-bar');

    if (searchInput && searchBarContainer) {
        searchInput.addEventListener('focus', () => {
            searchBarContainer.classList.add('is-focused');
        });

        searchInput.addEventListener('blur', () => {
            searchBarContainer.classList.remove('is-focused');
        });
    }
});