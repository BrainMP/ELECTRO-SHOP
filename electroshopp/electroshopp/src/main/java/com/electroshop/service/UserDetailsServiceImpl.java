package com.electroshop.service;

import com.electroshop.model.Usuario;
import com.electroshop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Este método es llamado automáticamente por Spring Security cuando
     * alguien intenta iniciar sesión.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Buscamos al usuario en nuestra base de datos MySQL por su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));

        // 2. Preparamos los roles/autoridades (ej. "ROLE_USER")
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(usuario.getRole()));

        // 3. Creamos y devolvemos el objeto UserDetails que Spring Security entiende
        // Spring Security usará esto para comparar la contraseña encriptada
        return new User(usuario.getEmail(), usuario.getPassword(), authorities);
    }
}
