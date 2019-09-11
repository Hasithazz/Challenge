package challenge.messages;

import challenge.Tickets.Ticket;
import challenge.organizations.Organization;
import challenge.users.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Message {
    Scanner input = new Scanner(System.in);
    User user = new User();
    Organization organization = new Organization();
    Ticket ticket = new Ticket();

    public void displayMainMenu() {
        System.out.println("Type 'quit' to exit at any time, Press 'Enter' to continue");
        System.out.println("\n");
        System.out.println("\t" + "Select search options:");
        System.out.println("\t" + "\u2022 Press 1 to search");
        System.out.println("\t" + "\u2022 Press 2 to view a list of searchable fields");
        System.out.println("\t" + "\u2022 Type 'quit' to exit");
        validateSearchType();
    }

    public void validateSearchType() {
        String searchType = input.nextLine();
        switch (searchType) {
            case "1":
                displaySearchOptions();
                break;
            case "2":
                displaySearchableFields();
                break;
            case "quit":
                System.exit(0);
            default:
                System.out.println("Unknown input please try again:");
                validateSearchType();
        }
    }

    public void displaySearchableFields() {
        String line = "----------------------------------------------------";
        System.out.println(line + "\n" + "Search users with");
        user.getSearchableFields().forEach((item) -> System.out.println(item));
        System.out.println(line + "\n" + "Search tickets with");
        ticket.getSearchableFields().forEach((item) -> System.out.println(item));
        System.out.println(line + "\n" + "Search organizations with");
        organization.getSearchableFields().forEach((item) -> System.out.println(item));

    }

    public void displaySearchOptions() {
        System.out.println("Select 1) Users or 2) Tickets or 3) Organizations");
        switch (input.nextLine()) {
            case "1":
                searchByUsers();
                break;
            case "2":
                searchByTickets();
                break;
            case "3":
                searchByOrganization();
                break;
            case "quit":
                System.exit(0);
            default:
                System.out.println("Unknown input please try again:");
                displaySearchOptions();
        }
    }

    public void searchByOrganization() {
        System.out.println("Enter search term");
        String term = input.nextLine();
        if (organization.validateSearchTerm(term)) {
            System.out.println("Enter search value");
            String value = input.nextLine();
            ArrayList<LinkedHashMap<String, String>> arrayList = organization.search(term, value);
            if (arrayList.isEmpty()) {
                System.out.println("Searching organization for " + term + " with a value of " + value + "\nNo results found");
            } else {
                printDetails(arrayList);
            }
        } else {
            System.out.println("Unknown term!!! please use correct term to search.");
            searchByOrganization();
        }
    }


    public void searchByTickets() {
        System.out.println("Enter search term");
        String term = input.nextLine();
        if (ticket.validateSearchTerm(term)) {
            System.out.println("Enter search value");
            String value = input.nextLine();
            ArrayList<LinkedHashMap<String, String>> arrayList = ticket.search(term, value);
            if (arrayList.isEmpty()) {
                System.out.println("Searching ticket for " + term + " with a value of " + value + "\nNo results found");
            } else {
                printDetails(arrayList);
            }
        } else {
            System.out.println("Unknown term!!! please use correct term to search.");
            searchByTickets();
        }
    }

    public void searchByUsers() {
        System.out.println("Enter search term");
        String term = input.nextLine();
        if (user.validateSearchTerm(term)) {
            System.out.println("Enter search value");
            String value = input.nextLine();
            ArrayList<LinkedHashMap<String, String>> arrayList = user.search(term, value);
            if (arrayList.isEmpty()) {
                System.out.println("Searching users for " + term + " with a value of " + value + "\nNo results found");
            } else {
                printDetails(arrayList);
            }
        } else {
            System.out.println("Unknown term!!! please use correct term to search.");
            searchByUsers();
        }
    }

    public void printDetails(ArrayList<LinkedHashMap<String, String>> list) {
//        list.forEach((item) -> item.forEach((k, v) -> System.out.println(k + " \t\t\t\t\t\t\t " + v)));
        list.forEach((item) -> print(item));
    }

    private void print(LinkedHashMap<String, String> item) {
        item.forEach((k, v) -> System.out.println(k + " \t\t\t\t\t\t\t " + v));
        System.out.println("\n");
    }


}

