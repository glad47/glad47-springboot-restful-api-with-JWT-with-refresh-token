package com.example.demo.Listener;

import com.example.demo.Security.JwtUtil;
import com.example.demo.Service.UserRepositoryUserDetailsService;
import com.example.demo.Util.CachedBodyHttpServletRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepositoryUserDetailsService userRepositoryUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ObjectMapper objectMapper;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURL = request.getRequestURL().toString();


        if (Pattern.compile("http://localhost:8080/h2-console/[a-zA-Z///.]*").matcher(requestURL).matches()) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println(requestURL);
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest =
                new CachedBodyHttpServletRequest(request);

        if ("http://localhost:8080/authenticate/login".equals(requestURL)) {
            filterChain.doFilter(cachedBodyHttpServletRequest, response);
            return;
        }
        String username=null;
        String jwt=null;


        final String authHeader=request.getHeader("Authorization");
            try{
                if(authHeader !=null && authHeader.startsWith("Bearer ")) {
                    jwt = authHeader.substring(7);
                    username = this.jwtUtil.getUsernameFromToken(jwt);

                }
                System.out.println("72 jwtfilter jwt: "+jwt);
                System.out.println("73 jwtfilter username: "+username);


                if(jwtUtil.validateToken(jwt)){
                    UserDetails userDetails = userRepositoryUserDetailsService.loadUserByUsername(username);
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
                //the jwt expired and we will executed the following to resolve the problem
                int i;
                char c;
                InputStream inputStream = cachedBodyHttpServletRequest.getInputStream();
                int data = inputStream.read();
                String outstr="";
                while(data != -1) {
                    c = (char)data;
                    outstr += c + "" ;
                    data = inputStream.read();
                }

                System.out.println("here" + outstr);
                String refreshToken=null;
                if(!outstr.equals("")){
                    System.out.println("bas sign");
                    Map<String, String> map
                            = this.objectMapper.readValue(outstr, new TypeReference<Map<String,String>>(){});

                    String jwtt = map.get("jwt");
                    refreshToken = map.get("RefreshToken");
                    System.out.println("refresh token "+ refreshToken);
                }



                if(refreshToken == null || refreshToken.equals("") ) {
                    //the url that their jwt expaied and they dont have refershToken
                    //send 401 and from angular platform the system will receive 401 and
                    //hence accordingly the refershToken path will be called from their
                    //with the same jwt which is expired and then the else statment of this
                    //if else statement will be executed
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
                    return;

                }else {

                    //the angular system called this path and then so we want to perform refresh
                    //on the server side but we must be careful about
                    //1) one the refersh token is not expired
                    //if it is then send 403 forbidden
                    //2)the refreshToken is not
                    //
                    try {
                        System.out.println("128    finally in");
                        username = jwtUtil.getUsernameFromToken(refreshToken);
                        System.out.println("130    username: "+username);
//                    System.out.println("line 84"+username);
                        UserDetails userDetails = userRepositoryUserDetailsService.loadUserByUsername(username);
                        if (jwtUtil.validateToken(refreshToken)) {
                            System.out.println("134    just before");
                            allowForRefreshToken(cachedBodyHttpServletRequest);
                            cachedBodyHttpServletRequest.setAttribute("RefreshToken",refreshToken);
                        }
                        } catch (Exception e) {
                        //check if the token is not vaild either expired or malformed(wrongwritten)
                        //to do this we just catch the exception nouserfound returned from
                        //loadUserbyUsername passing the username we get from the refreshToken
                         ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "The token is not valid.");

                        return;
                        }


                }

            }catch(MalformedJwtException ex){
                //check weather the jwt is malformed or not
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "The token is not valid.");

                return;
            }catch(UsernameNotFoundException ex){
                //jwt is vaild and the user is not in the database
                if(jwt != null){
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "The token is not valid.");

                    return;
                }

            }
            catch (BadCredentialsException ex) {
                request.setAttribute("exception", ex);


            } catch (Exception ex) {
                //we will check weather the url contain jwt or not
                //if not then dont do anything here
                //actually this is neccessary for the login to be processed
                //what about if other url is not contain jwt ?
                //we have three scinario here the first one is
                //1)we can use our spring secuirty setting to not forwarding it futher
                //2)usignn the guard at the view framework to not raandering the view
                //hence the view will not be able to send any request using other url
                //3)we can write or logic here to not allow any other url to be processed
                //by sending a 403 back to the view

                //i can see now how much is important to understand the whole logic of
                //the server and the view , depending on view implementation the server
                //logic can be varied to its greatest extent , the more we are able to
                //look including the whole image the more we are more logical about the
                //we choose to commuincate between the two framework, the more the system
                //will be stable the more we become happy about seeing our code not just
                //compiling successfully but see it running the way we need not we want!!
                System.out.println(ex);

                }


        filterChain.doFilter(cachedBodyHttpServletRequest, response);
    }

    private void allowForRefreshToken( HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

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