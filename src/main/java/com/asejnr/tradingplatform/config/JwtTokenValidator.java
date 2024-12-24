package com.asejnr.tradingplatform.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String token = request.getHeader(JwtConstant.JWT_HEADER);

      if (token != null) {
          token = token.substring(7);

          try{
              Key key = Keys.hmacShaKeyFor(JwtConstant.JWT_SECRET.getBytes());
              Claims claims = Jwts.parserBuilder()
                      .setSigningKey(key)
                      .build()
                      .parseClaimsJws(token)
                      .getBody();

              String authorities = String.valueOf(claims.get("authorities"));
              String email = String.valueOf(claims.get("email"));

              List<GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
              Authentication auth = new UsernamePasswordAuthenticationToken(email, null, authoritiesList);
              SecurityContextHolder.getContext().setAuthentication(auth);

          }catch (io.jsonwebtoken.ExpiredJwtException e) {
              System.err.println("Token has expired: " + e.getMessage());
          } catch (io.jsonwebtoken.MalformedJwtException e) {
              System.err.println("Invalid token format: " + e.getMessage());
          } catch (io.jsonwebtoken.security.SignatureException e) {
              System.err.println("Invalid token signature: " + e.getMessage());
          } catch (Exception e) {
              System.err.println("Token processing error: " + e.getMessage());
          }
      }

      filterChain.doFilter(request, response);
    }
}
