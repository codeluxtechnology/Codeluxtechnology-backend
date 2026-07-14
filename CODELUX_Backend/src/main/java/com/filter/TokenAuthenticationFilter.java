package com.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.entity.AdminUser;
import com.service.AdminService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private final AdminService adminService;

	public TokenAuthenticationFilter(AdminService adminService) {
		this.adminService = adminService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = resolveToken(request);
		if (StringUtils.hasText(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
			adminService.findByToken(token).ifPresent(admin -> authenticate(request, admin));
		}
		filterChain.doFilter(request, response);
	}

	private void authenticate(HttpServletRequest request, AdminUser admin) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(admin, null,
				List.of(new SimpleGrantedAuthority("ROLE_" + admin.getRole().name())));
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return request.getHeader("X-Admin-Token");
	}
}
