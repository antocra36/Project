package com.cognixia.jump.jdbc.ems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EMSRunner {

	public static DepartmentDAOClass departmentDB = new DepartmentDAOClass();
	public static EmployeeDAOClass employeeDB = new EmployeeDAOClass();
	public static AddressDAOClass addressDB = new AddressDAOClass();
	
	public static void main(String[] args) {
		promptUser();
	}

	public static void promptUser() {
		
		
		Scanner scan = new Scanner(System.in);
		boolean running = true;
		
		while (running) {
			System.out.println("\n[Employee Management System]\n");
			System.out.println("Available commands are: \n(1) add \n(2) remove \n(3) update \n(4) list \n(5) exit");
			System.out.print("Please enter a command: ");
			// Read Command, run through exception
			String command = scan.nextLine();
			
			int id = 0;
			String lastName = "";
			try {
				switch (command) {
				case "add":
				case "1":
					runAddCommand(scan);
					break;
					
				case "remove":
				case "2":
					runRemoveCommand(scan);					
					break;
					
				case "update":
				case "3":
					runUpdateCommand(scan);
					break;
					
				case "list":
				case "4": 
					runListCommand(scan);
					break;
					
				case "exit":
				case "5":
					running = false;
					break;
				default:
					System.out.println("Incorrect command. Please enter the numeric value or string corresponding with the available commands.");
				}
			}catch (InputMismatchException e) {
					System.out.println("Incorrect input");
				}
			}		
		scan.close();	
	}
	public static void runAddCommand(Scanner scan) {
		System.out.println("\n[Employee Management System]\n");
		System.out.println("Adding new employee...");
		String firstName = "";
		String lastName = "";
		String email = "";
		String department = "";
		
		System.out.print("Enter first name: ");
		firstName = scan.nextLine();
		
		System.out.print("Enter last name: ");
		lastName = scan.nextLine();
		
		System.out.print("Enter employee email: ");
		while (true) {
			email = scan.nextLine();
			if (isValidEmail(email)) {
				break;
			} else {
				System.out.println("Invalid email. Try again.");
			}
		}
		
		while(true) {
			System.out.println("Here are our departments:");
			printDepartments();

			System.out.print("Enter department name: ");

			department = scan.nextLine();
			if (isDepartment(department)) {
				break;
			} else {
				System.out.println("Invalid department. Try again.");
			}
		}
		

		
		int deptId = departmentDB.getDepartmentByName(department).getId();
		
		Employee newEmployee = new Employee(-1, firstName, lastName, email, 1, deptId); // edit
		
		if (employeeDB.addEmployee(newEmployee)) {
			System.out.println("Employee successfully added.");
		} else {
			System.out.println("Employee could not be added.");
		}
		
	}
	
	public static void runRemoveCommand(Scanner scan) {
		System.out.println("\n[Employee Management System]\n");
		System.out.println("Removing employee...");
		System.out.print("Enter Employee ID:");
		int id = 0;
		
		while (true) {
			try {
				id = scan.nextInt();
				scan.nextLine();
				break;
			} catch (InputMismatchException e) {
				scan.nextLine();
				System.out.println("Incorrect input. Try again.");
			}
		}

		if (employeeDB.deleteEmployee(id)) {
			System.out.println("Employee successfully removed from the system. \n");
		} else {
			System.out.println("Employee removal failed.\n");
		}
	}
	
	public static void runUpdateCommand(Scanner scan) {
		System.out.println("\n[Employee Management System]\n");
		System.out.println("Updating employee...");
		System.out.print("Enter Employee ID: ");
		int id = 0;
		
		while (true) {
			try {
				id = scan.nextInt();
				scan.nextLine();
				break;
			} catch (InputMismatchException e) {
				scan.nextLine();
				System.out.println("Incorrect input. Try again.");
			}
		}
		
		Employee employee = employeeDB.getEmployeeById(id);
		
		if (employee == null) {
			runUpdateCommand(scan);
		}

		String field = "";
		Employee updatedEmployee = null;	
		boolean prompting = true;
		while (prompting) {
			System.out.println("\nField options: " + 
					"\n(1) first name\n(2) last name \n(3) email \n(4) department");
			System.out.print("Enter field to update: ");
			field = scan.nextLine();			
	
			System.out.println("Please enter the new value:");
			String value = scan.nextLine();
			switch(field) {
				case "first name":
				case "1":
					updatedEmployee = new Employee(
								employee.getEmployeeId(),
								value,
								employee.getEmployeeLastName(),
								employee.getEmployeeEmail(),
								employee.getAddressId(),
								employee.getDepartmentId()
							);
					if (employeeDB.updateEmployee(updatedEmployee)) {
						System.out.println("Employee successfully updated.");
					} else {
						System.out.println("Employee update failed.");
					}
					prompting = false;
					break;
				case "last name":
				case "2":
					updatedEmployee = new Employee(
							employee.getEmployeeId(),
							employee.getEmployeeLastName(),
							value,
							employee.getEmployeeEmail(),
							employee.getAddressId(),
							employee.getDepartmentId()
						);
					if (employeeDB.updateEmployee(updatedEmployee)) {
						System.out.println("Employee successfully updated.");
					} else {
						System.out.println("Employee update failed.");
					}
					prompting = false;
					break;
				case "email":
				case "3":
					
					if (!isValidEmail(value)) {
						System.out.println("Invalid email format. Please try again.");
						break;
					}
					
					updatedEmployee = new Employee(
							employee.getEmployeeId(),
							employee.getEmployeeLastName(),
							employee.getEmployeeLastName(),
							value,
							employee.getAddressId(),
							employee.getDepartmentId()
						);
					if (employeeDB.updateEmployee(updatedEmployee)) {
						System.out.println("Employee successfully updated.");
					} else {
						System.out.println("Employee update failed.");
					}
					
					prompting = false;
					break;
				case "department":
				case "4":
					// TODO: get id from from department
					
					if (!isDepartment(value)) {
						System.out.println("Invalid department. These are the available departments:");
						printDepartments();
						break;
					}
					
					int departmentId = departmentDB.getDepartmentByName(value).getId();
					
					// TODO: verify if valid department
					updatedEmployee = new Employee(
							employee.getEmployeeId(),
							employee.getEmployeeLastName(),
							employee.getEmployeeLastName(),
							employee.getEmployeeEmail(),
							employee.getAddressId(),
							departmentId
						);
					if (employeeDB.updateEmployee(updatedEmployee)) {
						System.out.println("Employee successfully updated.");
					} else {
						System.out.println("Employee update failed.");
					}
					prompting = false;
					break;
				default:
					break;
			}
		}
		
	}
	
	public static void runListCommand(Scanner scan) {
		System.out.println("\n[Employee Management System]\n");
		System.out.println("Listing employee...");
		System.out.println("available commands are: \n(1) list all \n(2) list employee by id \n(3) list employees by name \n(4) list employees by department");
		String command = scan.nextLine();
			
		switch (command) {
		case "list all":
		case "1":
			
			List<Employee> allEmployees = employeeDB.getAllEmployees();
			printEmployeeList(allEmployees);
			
			break;
		case "list employee by id":
		case "2":
			
			System.out.print("Enter Employee ID:");
			int id = 0;
			
			while (true ) {
				try {
					id = scan.nextInt();
					scan.nextLine();
					break;
				} catch (InputMismatchException e) {
					scan.nextLine();
					System.out.println("Incorrect input. Try again.");
				}
			}

			Employee employeeById = employeeDB.getEmployeeById(id);
			
			if (employeeById == null) {
				System.out.println("Employee not found.");
			} else {
				printEmployee(employeeById);
			}

			break;
			
		case "list employees by name":
		case "3":
			
			System.out.print("Enter Employee Last Name: ");
			String lastName = scan.nextLine();
			
			List<Employee> employeesByLastName = employeeDB.getEmployeesByLastName(lastName);
			printEmployeeList(employeesByLastName);
			break;
		case "list employees by department":
		case "4":
			System.out.println("These are the available departments:");
			printDepartments();
			System.out.println("Enter Department Name");
			String departmentName = scan.nextLine();
			
			List<Employee> employeesByDepartmentName= employeeDB.getEmployeesByDepartmentName(departmentName);
			printEmployeeList(employeesByDepartmentName);
			break;
		default:
			System.out.println("Command not recognized. Please try again.");
			runListCommand(scan);
		}
	}
	
	public static void printEmployeeList(List<Employee> employeeList) {
		@SuppressWarnings("unchecked")
		ArrayList<Employee> employees = (ArrayList) employeeList;
		
		if (employees.isEmpty()) {
			System.out.println("No employees in the database.");
		} else {
			for (int i = 0; i < employees.size(); i++) {
				System.out.println("-----------------");
				printEmployee(employees.get(i));
			}
			System.out.println("-----------------");
		}
		
	}
	
	public static void printEmployee(Employee employee) {
		System.out.printf("%-13s \t %-15s\n","Id: ",employee.getEmployeeId());
		System.out.printf("%-13s \t %-15s\n","First Name: ",employee.getEmployeeFirstName());
		System.out.printf("%-13s \t %-15s\n", "Last Name: ", employee.getEmployeeLastName());
		System.out.printf("%-13s \t %-15s\n", "Email: ", employee.getEmployeeEmail());
		
		Address address = addressDB.getAddressById(employee.getAddressId());
		System.out.printf("%-13s \t %d %s %s, %s %d\n", "Address: ", address.getStreetNum(), address.getStreetName(), address.getCity(), address.getState(), address.getZipCode());
		
		Department dept =  departmentDB.getDepartmentById(employee.getDepartmentId());
		
		System.out.printf("%-13s \t %-15s\n", "Department: ", dept.getName());
		
		
	}
	
	public static boolean isDepartment(String deptName) {
		// TODO: Get all departments from departmentDatabase, and check
		List<Department> departments = departmentDB.getAllDepartments();

		Optional<Department> department = departments.stream()
				.filter(e -> e.getName().equals(deptName))
				.findFirst();

		if (department.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	
	public static void printDepartments() {
		List<Department> departments = departmentDB.getAllDepartments();

		if (departments.isEmpty()) {
			System.out.println("No employees in the database.");
		} else {
			for (int i = 0; i < departments.size(); i++) {
				System.out.printf("Id: %d   Name: %-15s \n", departments.get(i).getId(), departments.get(i).getName());
			}
		}
		
	}
	
	public static boolean isValidEmail(String email) {
		String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
	

}
