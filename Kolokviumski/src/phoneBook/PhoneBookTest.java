package phoneBook;

import java.util.*;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String number) {
        super(String.format("Duplicate number: %s", number));
    }
}

class PhoneBook {

    Set<Contact> contacts;

    public PhoneBook() {
        contacts = new TreeSet<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        Contact contact = new Contact(name, number);

        if (contacts.contains(contact)) {
            throw new DuplicateNumberException(number);
        } else {
            contacts.add(contact);
        }
    }

    public void contactsByNumber(String part) {
        boolean flag=true;
        for (Contact contact : contacts) {
            String number = contact.number;
            if (number.contains(part)) {
                System.out.println(contact);
                flag=false;
            }
        }

        if(flag)
            System.out.println("NOT FOUND");

    }

    public void contactsByName(String part) {
        boolean flag=true;

        for (Contact contact:contacts){
            String name=contact.name;

            if(name.equals(part)){
                System.out.println(contact);
                flag=false;
            }
        }

        if(flag)
            System.out.println("NOT FOUND");
    }
}

class Contact implements Comparable<Contact>{

    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return name + " " + number;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(number, contact.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public int compareTo(Contact o) {
        int res=this.name.compareTo(o.name);

        if(res==0)
            return this.number.compareTo(o.number);

        return res;
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}



