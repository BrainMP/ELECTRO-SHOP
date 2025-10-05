document.addEventListener('DOMContentLoaded', () => {

    // --- 1. SIMULACIÓN DE CARRITO DE COMPRAS ---
    const botonesCarrito = document.querySelectorAll('.btn-comprar');
    const contadorCarritoLink = document.querySelector('.cart-link');
    let totalItems = 0;

    botonesCarrito.forEach(boton => {
        boton.addEventListener('click', (evento) => {

            // Incrementa y actualiza el contador
            totalItems++;
            if (contadorCarritoLink) {
                contadorCarritoLink.textContent = `🛒 Mi carrito (${totalItems})`;
            }

            // Notificación visual de éxito
            evento.target.textContent = 'Añadido!';
            evento.target.style.backgroundColor = '#28a745'; // Color verde
            setTimeout(() => {
                evento.target.textContent = 'Añadir al Carrito';
                evento.target.style.backgroundColor = '#ff6600'; // Vuelve al color naranja
            }, 800);
        });
    });

    // --- 2. INTERACTIVIDAD VISUAL EN BARRA DE BÚSQUEDA ---
    const searchInput = document.querySelector('.search-bar input');
    const searchBarContainer = document.querySelector('.search-bar');

    if (searchInput && searchBarContainer) {
        // Añade la clase CSS 'is-focused' al hacer clic
        searchInput.addEventListener('focus', () => {
            searchBarContainer.classList.add('is-focused');
        });

        // Remueve la clase al perder el foco
        searchInput.addEventListener('blur', () => {
            searchBarContainer.classList.remove('is-focused');
        });
    }

});