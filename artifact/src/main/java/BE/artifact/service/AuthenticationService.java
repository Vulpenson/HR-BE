package BE.artifact.service;

import BE.artifact.payload.request.SignUpRequest;
import BE.artifact.payload.request.SignInRequest;
import BE.artifact.payload.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);

    JwtAuthenticationResponse deleteUser(Long id);
}
