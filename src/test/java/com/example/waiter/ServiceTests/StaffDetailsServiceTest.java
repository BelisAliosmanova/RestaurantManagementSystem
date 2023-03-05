package com.example.waiter.ServiceTests;

import com.example.waiter.Entities.Staff;
import com.example.waiter.Repositories.StaffRepository;
import com.example.waiter.Services.StaffDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StaffDetailsServiceTest {
    @Mock
    StaffRepository staffRepository;
    @InjectMocks
    StaffDetailsServiceImpl staffDetailsServiceImpl;

    @Test
    public void TestLoadUserByUserName() {
        Staff staff = new Staff();
        staff.setUsername("belis");
        staff.setPassword("b1234");
        staff.setEnabled(true);
        when(staffRepository.getStaffByUsername("belis")).thenReturn(staff);
        UserDetails userDetails = staffDetailsServiceImpl.loadUserByUsername("belis");
        assertEquals(staff.getUsername(), userDetails.getUsername());
        assertEquals(staff.getPassword(), userDetails.getPassword());
        assertEquals(staff.isEnabled(), userDetails.isEnabled());
    }

    @Test
    public void testLoadUserByUsername_InvalidUsername() {
        when(staffRepository.getStaffByUsername("johndoe")).thenReturn(null);
        Exception exception = assertThrows(
                UsernameNotFoundException.class,
                () -> staffDetailsServiceImpl.loadUserByUsername("johndoe")
        );
        String expectedMessage = "Could not find user";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
