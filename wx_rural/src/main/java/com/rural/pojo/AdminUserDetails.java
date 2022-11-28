package com.rural.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xh
 * @create 2022-10-14  15:48
 */
@Data
@NoArgsConstructor
public class AdminUserDetails implements UserDetails {
    private AdminUser adminUser;
    private List<String> permsList;

    @JSONField(serialize = false)
    private List<GrantedAuthority> authority;

    public AdminUserDetails(AdminUser adminUser, List<String> permsList) {
        this.adminUser = adminUser;
        this.permsList = permsList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authority != null) return authority;
        authority = permsList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authority;
    }

    @Override
    public String getPassword() {
        return adminUser.getPassword();
    }

    @Override
    public String getUsername() {
        return adminUser.getUsername();
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

    @Override
    public boolean isEnabled() {
        return adminUser.getStatus()==0?true:false;
    }

}
