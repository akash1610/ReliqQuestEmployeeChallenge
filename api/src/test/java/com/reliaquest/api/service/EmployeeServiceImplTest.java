package com.reliaquest.api.service;

import com.reliaquest.api.Exception.EmployeeException;
import com.reliaquest.api.constants.AppConstants;
import com.reliaquest.api.dao.EmployeeDao;
import com.reliaquest.api.models.Employee;
import com.reliaquest.api.models.EmployeeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockEmployee1;
    private Employee mockEmployee2;
    private EmployeeRequest employeeRequest;
    private UUID employeeId;

    @BeforeEach
    public void setUp() {
        employeeId = UUID.randomUUID();
        mockEmployee1 = new Employee(employeeId, "Akash Ghuge", 100000, 35, "Software Engineer", "akash.ghuge@gmail.com");
        mockEmployee2 = new Employee(UUID.randomUUID(), "Rajeev Goyal", 150000, 40, "Senior Software", "rajeev.goyal@gmail.com");
        employeeRequest = new EmployeeRequest("Ragini Kale", 100000, "Software Engineer", 30);
    }

    @Test
    public void testGetAllEmployeesWithSuccessResponse() {
        List<Employee> mockEmployees = Arrays.asList(mockEmployee1, mockEmployee2);

        when(employeeDao.getListOfAllEmployees()).thenReturn(mockEmployees);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("Akash Ghuge", result.get(0).getName());
        verify(employeeDao, times(1)).getListOfAllEmployees();
    }

    @Test
    public void testGetEmployeeByIdWithValidId() {
        when(employeeDao.getEmployeeById(employeeId.toString())).thenReturn(mockEmployee1);

        Employee result = employeeService.getEmployeeById(employeeId.toString());

        assertNotNull(result);
        assertEquals("Akash Ghuge", result.getName());
        assertEquals(employeeId, result.getId());
        verify(employeeDao, times(1)).getEmployeeById(employeeId.toString());
    }

    @Test
    public void testGetEmployeeByIdWithInvalidId() {
        String invalidId = UUID.randomUUID().toString();
        when(employeeDao.getEmployeeById(invalidId)).thenThrow(new EmployeeException("Employee not Found", AppConstants.ID));

        Exception exception = assertThrows(EmployeeException.class, () -> employeeService.getEmployeeById(invalidId));

        assertEquals("Employee not Found", exception.getMessage());
        verify(employeeDao, times(1)).getEmployeeById(invalidId);
    }

    @Test
    public void testGetEmployeesByNameSearch() {
        List<Employee> mockEmployees = Arrays.asList(mockEmployee1, mockEmployee2);

        when(employeeDao.getListOfAllEmployees()).thenReturn(mockEmployees);

        List<Employee> result = employeeService.getEmployeesByNameSearch("Ghuge");

        assertEquals(1, result.size());
        verify(employeeDao, times(1)).getListOfAllEmployees();
    }
    @Test
    public void testGetEmployeesByNameSearch_NoMatchFound() {
        List<Employee> mockEmployees = Arrays.asList(mockEmployee1, mockEmployee2);
        when(employeeDao.getListOfAllEmployees()).thenReturn(mockEmployees);

        String searchTerm = "Omkar";

        EmployeeException exception = assertThrows(EmployeeException.class, () -> {
            employeeService.getEmployeesByNameSearch(searchTerm);
        });

        assertEquals("No employees found with name: Omkar", exception.getMessage());
        verify(employeeDao, times(1)).getListOfAllEmployees();
    }


    @Test
    public void testGetHighestSalaryOfEmployees() {
        List<Employee> mockEmployees = Arrays.asList(mockEmployee1, mockEmployee2);

        when(employeeDao.getListOfAllEmployees()).thenReturn(mockEmployees);

        Integer result = employeeService.getHighestSalaryOfEmployees();

        assertEquals(150000, result);
        verify(employeeDao, times(1)).getListOfAllEmployees();
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() {
        List<Employee> mockEmployees = Arrays.asList(mockEmployee1, mockEmployee2);

        when(employeeDao.getListOfAllEmployees()).thenReturn(mockEmployees);

        List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();

        assertEquals(2, result.size());
        assertEquals("Rajeev Goyal", result.get(0));
        verify(employeeDao, times(1)).getListOfAllEmployees();
    }

    @Test
    public void testCreateEmployee() {
        Employee createdEmployee = new Employee(UUID.randomUUID(), "Ragini Kale", 100000, 30, "Software Engineer", "ragini.kale@gmail.com");

        when(employeeDao.addEmployee(employeeRequest)).thenReturn(createdEmployee);

        Employee result = employeeService.createEmployee(employeeRequest);

        assertNotNull(result);
        assertEquals("Ragini Kale", result.getName());
        assertEquals("Software Engineer", result.getTitle());
        assertEquals(100000, result.getSalary());
        assertEquals(30, result.getAge());
        assertEquals("ragini.kale@gmail.com", result.getEmail());
        verify(employeeDao, times(1)).addEmployee(employeeRequest);
    }

    @Test
    public void testDeleteEmployeeById() {
        when(employeeDao.deleteEmployeeByName(employeeId.toString())).thenReturn("Akash Ghuge");

        String result = employeeService.deleteEmployeeById(employeeId.toString());

        assertEquals("Akash Ghuge", result);
        verify(employeeDao, times(1)).deleteEmployeeByName(employeeId.toString());
    }
}