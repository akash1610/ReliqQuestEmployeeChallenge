package com.reliaquest.api.Integration;

import com.reliaquest.api.constants.AppConstants;
import com.reliaquest.api.dao.EmployeeDao;
import com.reliaquest.api.models.Employee;
import com.reliaquest.api.models.EmployeeRequest;
import com.reliaquest.api.service.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeDao employeeDao;

    @Autowired
    private EmployeeServiceImpl employeeService;

    private EmployeeRequest employeeRequest;
    private Employee mockEmployee;
    private List<Employee> mockEmployees;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        employeeRequest = new EmployeeRequest("Akash Ghuge", 100000, "Software Engineer", 35);
        mockEmployee = new Employee(UUID.randomUUID(), "Akash Ghuge", 100000, 35, "Software Engineer", "akash.ghuge@gmail.com");
        mockEmployees = Arrays.asList(mockEmployee,
                new Employee(UUID.randomUUID(), "Rajeev Goyal", 1100000, 40, "Senior Engineer", "rajeev.goyal@gmail.com"));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        when(employeeDao.addEmployee(any(EmployeeRequest.class))).thenReturn(mockEmployee);

        mockMvc.perform(post(Strings.EMPTY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name").value("Akash Ghuge"))
                .andExpect(jsonPath("$.employee_salary").value(100000))
                .andExpect(jsonPath("$.employee_title").value("Software Engineer"))
                .andExpect(jsonPath("$.employee_age").value(35))
                .andDo(print());

        verify(employeeDao, times(1)).addEmployee(any(EmployeeRequest.class));
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        when(employeeDao.getListOfAllEmployees()).thenReturn(mockEmployees);

        mockMvc.perform(get(Strings.EMPTY)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].employee_name").value("Akash Ghuge"))
                .andExpect(jsonPath("$[1].employee_name").value("Rajeev Goyal"))
                .andDo(print());

        verify(employeeDao, times(1)).getListOfAllEmployees();
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        String employeeId = mockEmployee.getId().toString();
        when(employeeDao.getEmployeeById(eq(employeeId))).thenReturn(mockEmployee);

        mockMvc.perform(get(AppConstants.FORWARD_SLASH+"{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name").value("Akash Ghuge"))
                .andExpect(jsonPath("$.employee_salary").value(100000))
                .andDo(print());

        verify(employeeDao, times(1)).getEmployeeById(employeeId);
    }

    @Test
    public void testGetEmployeesByNameSearch() throws Exception {
        String searchString = "Ghuge";
        when(employeeDao.getListOfAllEmployees()).thenReturn(mockEmployees);

        mockMvc.perform(get(AppConstants.FORWARD_SLASH + "search/{searchString}", searchString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employee_name").value("Akash Ghuge"))
                .andDo(print());

        verify(employeeDao, times(1)).getListOfAllEmployees();
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws Exception {
        when(employeeDao.getListOfAllEmployees()).thenReturn(mockEmployees);

        mockMvc.perform(get(AppConstants.FORWARD_SLASH + "highestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1100000))
                .andDo(print());

        verify(employeeDao, times(1)).getListOfAllEmployees();
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        when(employeeDao.getListOfAllEmployees()).thenReturn(mockEmployees);

        mockMvc.perform(get(AppConstants.FORWARD_SLASH + "topTenHighestEarningEmployeeNames")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("Rajeev Goyal"))
                .andExpect(jsonPath("$[1]").value("Akash Ghuge"))
                .andDo(print());

        verify(employeeDao, times(1)).getListOfAllEmployees();
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {
        String employeeId = mockEmployee.getId().toString();
        when(employeeDao.deleteEmployeeByName(eq(employeeId))).thenReturn("Akash Ghuge");

        mockMvc.perform(delete(AppConstants.FORWARD_SLASH + "{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Akash Ghuge"))
                .andDo(print());

        verify(employeeDao, times(1)).deleteEmployeeByName(employeeId);
    }

    @Test
    public void testCreateEmployeeThrowsExceptionWhenNameMissing() throws Exception {
        EmployeeRequest invalidRequest = new EmployeeRequest("", 100000, "Engineer", 30);

        mockMvc.perform(post(Strings.EMPTY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Employee name must not be empty"))
                .andDo(print());
    }



}
