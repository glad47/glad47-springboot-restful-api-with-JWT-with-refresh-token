package com.example.demo;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private  UserRepositoryUserDetailsService userRepositoryUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String username=null;
        String jwt=null;
        final String authHeader=request.getHeader("Authorization");
//        String jwtToken = extractJwtFromRequest(request);

        //if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){


            try{
                if(authHeader !=null && authHeader.startsWith("Bearer ")){
                    jwt=authHeader.substring(7);
                    username=jwtUtil.getUsernameFromToken(jwt);

                }
                UserDetails userDetails = userRepositoryUserDetailsService.loadUserByUsername(username);
                if(jwtUtil.validateToken(jwt,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                        new UsernamePasswordAuthenticationToken(userDetails,null,
                                userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);

            }else{
                    System.out.println("Cannot set the Security Context");
                }
            }catch(ExpiredJwtException ex){
                //you have to make check for the refresh token
                System.out.println("i am hear fucker");
                String requestURL = request.getRequestURL().toString();
                System.out.println(requestURL);
                //here is the problem care about it
                String refreshToken = request.getParameter("RefreshToken");
                System.out.println(refreshToken);

                username=jwtUtil.getUsernameFromToken(refreshToken);
                UserDetails userDetails = userRepositoryUserDetailsService.loadUserByUsername(username);
                if(jwtUtil.validateToken(refreshToken,userDetails) && requestURL.contains("refreshtoken")) {
                    allowForRefreshToken(request);
                }else
                    request.setAttribute("exception", ex);

            } catch (BadCredentialsException ex) {
                request.setAttribute("exception", ex);
            } catch (Exception ex) {
                System.out.println(ex);
            }


        filterChain.doFilter(request, response);
    }

    private void allowForRefreshToken( HttpServletRequest request) {

        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        //maybe we dont need this any more
       // request.setAttribute("claims", ex.getClaims());

    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
    }
//        final String authHeader=request.getHeader("Authorization");
//        String username=null;
//        String jwt=null;
//        if(authHeader !=null && authHeader.startsWith("Bearer ")){
//            jwt=authHeader.substring(7);
//            username=jwtUtil.getUsernameFromToken(jwt);
//
//        }
//        System.out.println("i am here fucker3");
//        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
//            UserDetails userDetails = userRepositoryUserDetailsService.loadUserByUsername(username);
//
//            System.out.println("i am here fucker");
//            if(jwtUtil.validateToken(jwt,userDetails)){
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
//                        new UsernamePasswordAuthenticationToken(userDetails,null,
//                                userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken.setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext()
//                        .setAuthentication(usernamePasswordAuthenticationToken);
//
//            }
//        }
//
//        System.out.println("i am here fucker2");
//        filterChain.doFilter(request,response);
//    }






 //       }
//        try {
//            // JWT Token is in the form "Bearer token". Remove Bearer word and
//            // get only the Token
//
//            if (StringUtils.hasText(jwtToken) && jwtUtil.validateToken(jwtToken)) {
//                UserDetails userDetails = new User(jwtUtil.getUsernameFromToken(jwtToken), "",
//                        jwtUtil.getRolesFromToken(jwtToken));
//
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                // After setting the Authentication in the context, we specify
//                // that the current user is authenticated. So it passes the
//                // Spring Security Configurations successfully.
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            } else {
//                System.out.println("Cannot set the Security Context");
//            }
//        } catch (JwtException ex) {
//
//            String isRefreshToken = request.getHeader("isRefreshToken");
//            String requestURL = request.getRequestURL().toString();
//            // allow for Refresh Token creation if following conditions are true.
//            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
//                allowForRefreshToken(ex, request);
//            } else
//                request.setAttribute("exception", ex);
//
//        } catch (BadCredentialsException ex) {
//            request.setAttribute("exception", ex);
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }