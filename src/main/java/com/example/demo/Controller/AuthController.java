package com.example.demo.Controller;

import com.example.demo.Model.JwtRequest;
import com.example.demo.Model.JwtResponse;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Service.UserRepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/authenticate",produces = "application/json")
@CrossOrigin(origins = "*")
public class AuthController {
    private UserRepositoryUserDetailsService userRepositoryUserDetailsService;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserRepositoryUserDetailsService userRepositoryUserDetailsService){
        this.userRepositoryUserDetailsService=userRepositoryUserDetailsService;
    }

    @PostMapping(path ="/login",consumes = "application/json")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) throws Exception{
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    jwtRequest.getUsername(),jwtRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails= this.userRepositoryUserDetailsService
                .loadUserByUsername(jwtRequest.getUsername());
        final String jwt=this.jwtTokenUtil.generateToken(userDetails);
        String refreshToken = this.jwtTokenUtil.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(jwt,refreshToken));
    }

    @PostMapping(path ="/refreshtoken",consumes = "application/json")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {

        //       Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
//        String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        String ref=request.getAttribute("RefreshToken").toString();
        System.out.println("55 authcontroler  refreshtoken" + ref );
        String username=this.jwtTokenUtil.getUsernameFromToken( ref);
        final UserDetails userDetails= this.userRepositoryUserDetailsService
                .loadUserByUsername(username);
        final String jwt=this.jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(jwt,ref));
    }

//    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
//        Map<String, Object> expectedMap = new HashMap<String, Object>();
//        for (Map.Entry<String, Object> entry : claims.entrySet()) {
//            expectedMap.put(entry.getKey(), entry.getValue());
//        }
//        return expectedMap;
//    }
}

