package com.example.waiter;

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
    StaffDetailsServiceImpl staffDetailsService;
    @Test
    public void testLoadUserByUsername() {
        Staff staff = new Staff();
        staff.setUsername("we");
        staff.setPassword("password");
        staff.setEnabled(true);
        when(staffRepository.getStaffByUsername("we")).thenReturn(staff);
        UserDetails userDetails = staffDetailsService.loadUserByUsername("we");
        assertEquals(userDetails.getUsername(), staff.getUsername());
        assertEquals(userDetails.getPassword(), staff.getPassword());
        assertEquals(userDetails.isEnabled(), staff.isEnabled());
    }
    @Test
    public void testLoadUserByUsername_InvalidUsername() {
        // Configure the mock repository to return null
        when(staffRepository.getStaffByUsername("johndoe")).thenReturn(null);

        // Call the loadUserByUsername method with an invalid username using assertThrows
        Exception exception = assertThrows(
                UsernameNotFoundException.class,
                () -> staffDetailsService.loadUserByUsername("johndoe")
        );

        // Verify that the exception message is correct
        String expectedMessage = "Could not find user";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
