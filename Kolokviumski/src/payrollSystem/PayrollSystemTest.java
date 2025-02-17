package payrollSystem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;


class PayrollSystem {

    Map<String, Double> hourlyRateByLevel;
    Map<String, Double> ticketRateByLevel;
    List<Employee> employees;

    PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.employees = new ArrayList<>();
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
    }

    public void setHourlyRateByLevel(Map<String, Double> hourlyRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
    }

    public void setTicketRateByLevel(Map<String, Double> ticketRateByLevel) {
        this.ticketRateByLevel = ticketRateByLevel;
    }

    public void readEmployees(InputStream in) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        List<String> list = bufferedReader.lines().collect(Collectors.toList());

        for (String s : list) {

            String[] parts = s.split(";");

            String ID = parts[1];
            String level = parts[2];

            if (s.startsWith("H")) {
                double hours = Double.parseDouble(parts[3]);

                HourlyEmployee employee = new HourlyEmployee(ID, level, hours, hourlyRateByLevel.get(level));
                employees.add(employee);
            }


            if (s.startsWith("F")) {

                List<Integer> tickets = new ArrayList<>();

                for (int j = 3; j < parts.length; j++) {
                    tickets.add(Integer.valueOf(parts[j]));
                }

                FreelanceEmployee employee = new FreelanceEmployee(ID, level, tickets, ticketRateByLevel.get(level));
                employees.add(employee);
            }
        }


    }

    Map<String, Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {

        Map<String, Set<Employee>> mapEmployees = new LinkedHashMap<>();

        levels.forEach(l->{
            employees.stream()
                    .filter(e->e.level.equals(l))
                    .forEach(e->{
                        mapEmployees.putIfAbsent(l,new TreeSet<>(Employee.EMPLOYEE_COMPARATOR));
                        mapEmployees.get(l).add(e);
                    });
        });

        return mapEmployees;
    }
}

abstract class Employee {

    String ID;
    String level;
    public static final Comparator<Employee> EMPLOYEE_COMPARATOR=
            Comparator.comparing(Employee::getSalary).reversed()
                    .thenComparing(Employee::getLevel);

    public double getSalary() {
        return 0;
    }

    public Employee(String ID, String level) {
        this.ID = ID;
        this.level = level;
    }

    public String getID() {
        return ID;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f",ID,level,getSalary());
    }
}

class HourlyEmployee extends Employee {
    double hours;
    double hourLevelRate;

    @Override
    public double getSalary() {
        double regular;
        double overtime;
       if(hours<=40){
           regular=hours;
           overtime=0;
       }

       else{
           regular=40;
           overtime=hours-40;
       }

       return regular*hourLevelRate+overtime*hourLevelRate*1.5;
    }

    public HourlyEmployee(String ID, String level, double hours, double hourLevelRate) {
        super(ID, level);
        this.hours = hours;
        this.hourLevelRate = hourLevelRate;
    }



    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();

        sb.append(super.toString());

        if(hours>40.00){
            sb.append(String.format(" Regular hours: 40.00 Overtime hours: %.2f",hours-40.00));
        }
        else{
            sb.append(String.format(" Regular hours: %.2f Overtime hours: 0.00",hours));
        }

        return sb.toString();
    }
}

class FreelanceEmployee extends Employee {
    List<Integer> tickets;
    double ticketLevelRate;

    @Override
    public double getSalary() {
        return tickets.stream().mapToDouble(i -> i).sum()*ticketLevelRate;
    }

    public FreelanceEmployee(String ID, String level, List<Integer> tickets, double ticketLevelRate) {
        super(ID, level);
        this.tickets = tickets;
        this.ticketLevelRate = ticketLevelRate;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();

        sb.append(super.toString());

        sb.append(String.format( " Tickets count: %d Tickets points: %d",tickets.size(),tickets.stream().mapToInt(Integer::intValue).sum()));

        return sb.toString();
    }

    //    public FreelanceEmployee(String line, double ticketLevelRate){
//        tickets=new ArrayList<>();
//        this.ticketLevelRate=ticketLevelRate;
//        String [] parts=line.split(";");
//
//        ID=parts[1];
//        level=parts[2];
//
//        for (int i = 3; i < parts.length-3; i++) {
//            tickets.add(Integer.valueOf(parts[i]));
//        }
//
//    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        levels.add("level10");
        for (int i=5;i<=9;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}
