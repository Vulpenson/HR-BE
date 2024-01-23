package BE.artifact.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Collection;

@Entity
public class User implements UserDetails {

    @Id
    private String username;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // return the authorities granted to the user
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // change according to your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // change according to your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // change according to your requirements
    }

    @Override
    public boolean isEnabled() {
        return true; // change according to your requirements
    }
}
