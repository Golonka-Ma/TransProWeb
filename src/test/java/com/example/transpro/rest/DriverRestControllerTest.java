package com.example.transpro.rest;

import com.example.transpro.model.Driver;
import com.example.transpro.service.DriverService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DriverRestController.class)
public class DriverRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    @Test
    public void testGetAllDrivers() throws Exception {
        Driver driver1 = Driver.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .licenseNumber("12345")
                .licenseExpiryDate(LocalDate.now().plusYears(2))
                .tachoCardNumber("T12345")
                .tachoCardExpiryDate(LocalDate.now().plusYears(2))
                .build();

        Driver driver2 = Driver.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .licenseNumber("54321")
                .licenseExpiryDate(LocalDate.now().plusYears(1))
                .tachoCardNumber("T54321")
                .tachoCardExpiryDate(LocalDate.now().plusYears(1))
                .build();

        when(driverService.findAllDrivers()).thenReturn(Arrays.asList(driver1, driver2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/drivers")
                        .header("Authorization", "transprokey")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));

        verify(driverService, times(1)).findAllDrivers();
    }

    @Test
    public void testGetDriverById_Success() throws Exception {
        Driver driver = Driver.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .licenseNumber("12345")
                .licenseExpiryDate(LocalDate.now().plusYears(2))
                .tachoCardNumber("T12345")
                .tachoCardExpiryDate(LocalDate.now().plusYears(2))
                .build();

        when(driverService.findDriverById(1L)).thenReturn(Optional.of(driver));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/drivers/1")
                        .header("Authorization", "transprokey")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(driverService, times(1)).findDriverById(1L);
    }

    @Test
    public void testGetDriverById_NotFound() throws Exception {
        when(driverService.findDriverById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/drivers/1")
                        .header("Authorization", "transprokey")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(driverService, times(1)).findDriverById(1L);
    }

    @Test
    public void testAddDriver_Success() throws Exception {
        Driver driver = Driver.builder()
                .firstName("John")
                .lastName("Doe")
                .licenseNumber("12345")
                .licenseExpiryDate(LocalDate.now().plusYears(2))
                .tachoCardNumber("T12345")
                .tachoCardExpiryDate(LocalDate.now().plusYears(2))
                .build();

        when(driverService.existsByLicenseNumber("12345")).thenReturn(false);
        when(driverService.existsByTachoCardNumber("T12345")).thenReturn(false);
        doNothing().when(driverService).saveDriver(any(Driver.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/drivers/add")
                        .header("Authorization", "transprokey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"licenseNumber\":\"12345\",\"licenseExpiryDate\":\"2026-01-01\",\"tachoCardNumber\":\"T12345\",\"tachoCardExpiryDate\":\"2026-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dane kierowcy zostały dodane"));

        verify(driverService, times(1)).saveDriver(any(Driver.class));
    }


    @Test
    public void testAddDriver_Conflict() throws Exception {
        when(driverService.existsByLicenseNumber("12345")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/drivers/add")
                        .header("Authorization", "transprokey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"licenseNumber\":\"12345\",\"licenseExpiryDate\":\"2026-01-01\",\"tachoCardNumber\":\"T12345\",\"tachoCardExpiryDate\":\"2026-01-01\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Kierowca o takim numerze licencji lub numerze tachografu już istnieje"));

        verify(driverService, never()).saveDriver(any(Driver.class));
    }

    @Test
    public void testEditDriver_Success() throws Exception {
        Driver existingDriver = Driver.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .licenseNumber("12345")
                .licenseExpiryDate(LocalDate.now().plusYears(2))
                .tachoCardNumber("T12345")
                .tachoCardExpiryDate(LocalDate.now().plusYears(2))
                .build();

        Driver updatedDriver = Driver.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .licenseNumber("12345")
                .licenseExpiryDate(LocalDate.now().plusYears(2))
                .tachoCardNumber("T12345")
                .tachoCardExpiryDate(LocalDate.now().plusYears(2))
                .build();

        when(driverService.findDriverById(1L)).thenReturn(Optional.of(existingDriver));
        doNothing().when(driverService).saveDriver(existingDriver);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/drivers/edit/1")
                        .header("Authorization", "transprokey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Smith\",\"licenseNumber\":\"12345\",\"licenseExpiryDate\":\"2026-01-01\",\"tachoCardNumber\":\"T12345\",\"tachoCardExpiryDate\":\"2026-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dane kierowcy zostały zaktualizowane"));

        verify(driverService, times(1)).saveDriver(any(Driver.class));
    }


    @Test
    public void testEditDriver_NotFound() throws Exception {
        when(driverService.findDriverById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/drivers/edit/1")
                        .header("Authorization", "transprokey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Smith\",\"licenseNumber\":\"12345\",\"licenseExpiryDate\":\"2026-01-01\",\"tachoCardNumber\":\"T12345\",\"tachoCardExpiryDate\":\"2026-01-01\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Driver with ID 1 not found"));

        verify(driverService, never()).saveDriver(any(Driver.class));
    }

    @Test
    public void testDeleteDriver_Success() throws Exception {
        when(driverService.findDriverById(1L)).thenReturn(Optional.of(new Driver()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/drivers/delete/1")
                        .header("Authorization", "transprokey")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Dane kierowcy zostały usunięte"));

        verify(driverService, times(1)).deleteDriver(1L);
    }

    @Test
    public void testDeleteDriver_NotFound() throws Exception {
        when(driverService.findDriverById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/drivers/delete/1")
                        .header("Authorization", "transprokey")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(driverService, never()).deleteDriver(anyLong());
    }
}