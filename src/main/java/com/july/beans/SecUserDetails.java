package com.july.beans;

import com.july.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by kuangjun on 2016/5/7.
 */
public class SecUserDetails extends User implements UserDetails {

    public SecUserDetails(User user) {
        if (user != null) {
            this.setId(user.getId());
            this.setEmail(user.getEmail());
            this.setPassword(user.getPassword());
            this.setNickname(user.getNickname());
            this.setEnabled(user.isEnabled());
            this.setAvatarAddress(user.getAvatarAddress());
            this.setFacebookAccount(user.getFacebookAccount());
            this.setGithubAccount(user.getGithubAccount());
            this.setTimeline(user.getTimeline());
            this.setFollowers(this.getFollowers());
            this.setFollowings(this.getFollowings());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
