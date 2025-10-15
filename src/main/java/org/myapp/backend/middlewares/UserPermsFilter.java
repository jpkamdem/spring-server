package org.myapp.backend.middlewares;

import java.io.IOException;
import java.util.UUID;

import org.myapp.backend.auth.JWTService;
import org.myapp.backend.users.Role;
import org.myapp.backend.users.User;
import org.myapp.backend.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserPermsFilter implements Filter {
  private final JWTService jwtService;
  private final UserRepository userRepository;

  @Autowired
  public UserPermsFilter(JWTService jwtService, UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String cookieValue = jwtService.tokenValueFromHttp(request);
    if (cookieValue == null) {
      throw new IOException("Vous n'êtes pas connecté.e");
    }

    Claims decodedToken = jwtService.extractClaims(cookieValue);
    UUID jti = UUID.fromString(decodedToken.getSubject());
    User user = userRepository.findById(jti).get();
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String uri = httpServletRequest.getRequestURI();
    String pathId = uri.substring(uri.lastIndexOf("/")).substring(1);
    Boolean isAdmin = !user.getRole().equals(Role.user);

    if (pathId.length() == 0) {
      if (!isAdmin) {
        throw new IOException("Accès non autorisé");
      }

      chain.doFilter(request, response);
      return;
    }

    Boolean sameUserFromJwtCookie = user.getId().equals(UUID.fromString(pathId));
    if (!sameUserFromJwtCookie) {

      if (!isAdmin) {
        throw new IOException("Accès non autorisé");
      }

    }

    chain.doFilter(request, response);
  }

}