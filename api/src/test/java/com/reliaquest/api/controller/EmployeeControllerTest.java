package com.reliaquest.api.controller;

import com.reliaquest.api.constants.AppConstants;
import com.reliaquest.api.models.Employee;
import com.reliaquest.api.models.EmployeeRequest;
import com.reliaquest.api.service.IEmployeeService;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = EmployeeControllerImpl.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEmployeeService employeeService;



    private Employee mockEmployee;
    private EmployeeRequest employeeRequest;
    private UUID employeeId;

    @BeforeEach
    public void setUp() {
        employeeId = UUID.randomUUID();
        mockEmployee = new Employee(employeeId, "Akash Ghuge", 50000, 30, "Engineer", "akash.ghuge@gmail.com");
        employeeRequest = new EmployeeRequest("Akash Ghuge", 50000, "Engineer", 30);
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        List<Employee> employees = List.of(mockEmployee);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get(Strings.EMPTY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].employee_name").value("Akash Ghuge"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    public void testGetEmployeesByNameSearch() throws Exception {
        List<Employee> employees = List.of(mockEmployee);
        when(employeeService.getEmployeesByNameSearch("Ghuge")).thenReturn(employees);

        mockMvc.perform(get(AppConstants.FORWARD_SLASH+"search"+AppConstants.FORWARD_SLASH+"Ghuge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].employee_name").value("Akash Ghuge"));

        verify(employeeService, times(1)).getEmployeesByNameSearch("Ghuge");
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(employeeId.toString())).thenReturn(mockEmployee);

        mockMvc.perform(get(AppConstants.FORWARD_SLASH + employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name").value("Akash Ghuge"))
                .andExpect(jsonPath("$.employee_salary").value(50000));

        verify(employeeService, times(1)).getEmployeeById(employeeId.toString());
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws Exception {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(50000);

        mockMvc.perform(get(AppConstants.FORWARD_SLASH+"highestSalary"))
                .andExpect(status().isOk())
                .andExpect(content().string("50000"));

        verify(employeeService, times(1)).getHighestSalaryOfEmployees();
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> topEarners = List.of("Akash Ghuge");
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(topEarners);

        mockMvc.perform(get(AppConstants.FORWARD_SLASH+"topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0]").value("Akash Ghuge"));

        verify(employeeService, times(1)).getTopTenHighestEarningEmployeeNames();
    }

    @Test
    public void testCreateEmployee() throws Exception {
        when(employeeService.createEmployee(employeeRequest)).thenReturn(mockEmployee);

        mockMvc.perform(post(Strings.EMPTY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Akash Ghuge\", \"salary\":50000, \"title\":\"Engineer\", \"age\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name").value("Akash Ghuge"))
                .andExpect(jsonPath("$.employee_salary").value(50000));

        verify(employeeService, times(1)).createEmployee(any(EmployeeRequest.class));
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {
        when(employeeService.deleteEmployeeById(employeeId.toString())).thenReturn("Akash Ghuge");

        mockMvc.perform(delete(AppConstants.FORWARD_SLASH + employeeId))
                .andExpect(status().isOk())
                .andExpect(content().string("Akash Ghuge"));

        verify(employeeService, times(1)).deleteEmployeeById(employeeId.toString());
    }
}