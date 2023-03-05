package com.example.waiter.OtherTests;

import com.example.waiter.Entities.Staff;
import com.example.waiter.Enums.Role;
import com.example.waiter.Security.CustomAuthenticationSuccessHandler;
import com.example.waiter.Security.MyStaffDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
@ExtendWith(MockitoExtension.class)
public class MyStaffDetailsTest {
    @InjectMocks
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Test
    public void testGetAuthorities() {
        Staff staff = new Staff();
        staff.setRole(Role.COOK);
        MyStaffDetails userDetails = new MyStaffDetails(staff);
        userDetails.isAccountNonExpired();
        userDetails.isAccountNonExpired();
        userDetails.isAccountNonLocked();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Assertions.assertEquals(1, authorities.size());
        SimpleGrantedAuthority authority = (SimpleGrantedAuthority) authorities.iterator().next();
        Assertions.assertEquals(Role.COOK.toString(), authority.getAuthority());
    }

}
