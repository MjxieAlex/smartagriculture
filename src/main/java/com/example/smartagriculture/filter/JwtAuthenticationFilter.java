package com.example.smartagriculture.filter;

import com.example.smartagriculture.service.UserService;
import com.example.smartagriculture.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.*;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: MjXie
 * @Date: 2025/05/12/10:43
 * @Description:
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {
        String hdr = req.getHeader("Authorization");
        if (hdr != null && hdr.startsWith("Bearer ")) {
            String token = hdr.substring(7);
            if (jwtUtil.validate(token)) {
                String username = jwtUtil.getUsername(token);
                // 验证单点登出
                String cached = (String) redisTemplate.opsForValue().get("token:" + username);
                if (cached != null && cached.equals(token)) {
                    UserDetails ud = userService.loadUserByUsername(username);
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            ud, null, ud.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                // 如果 token 验证失败或者已登出，则不设置 Authentication，但依然放行给后续去返回 401
            }
        }
        // 无论是否带了 Header，都要继续调用链
        chain.doFilter(req, res);
    }

}
