package phoneBookOptimized;

import java.util.*;


class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String number) {
        super(String.format("Duplicate number: %s", number));
    }
}

class PhoneBook {

    Map<String, Set<Contact>> contactsByName;
    Map<String, Contact> contactsByNumber;

    public PhoneBook() {
        contactsByName = new TreeMap<>();
        contactsByNumber = new TreeMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        Contact contact = new Contact(name, number);

        if (contactsByNumber.containsKey(number)) {
            throw new DuplicateNumberException(number);
        } else {
            contactsByNumber.put(number, contact);

            contactsByName
                    .computeIfAbsent(name, k->new TreeSet<>())
                    .add(contact);
        }
    }

    public void contactsByNumber(String part) {

        List<Contact> matchedContacts = new ArrayList<>();

        for (Map.Entry<String, Contact> entry : contactsByNumber.entrySet()) {
            String number = entry.getKey();
            if (number.contains(part)) {
                matchedContacts.add(entry.getValue());
            }
        }

        if (!matchedContacts.isEmpty()) {

            Collections.sort(matchedContacts);
            for (Contact contact : matchedContacts) {
                System.out.println(contact);
            }
        }else{
            System.out.println("NOT FOUND");
        }

    }



    public void contactsByName(String part) {
        boolean flag = true;

        for (Map.Entry<String, Set<Contact>> entry : contactsByName.entrySet()) {
            String name = entry.getKey();
            if (name.equals(part)) {
                for (Contact contact : entry.getValue()) {
                    System.out.println(contact);
                    flag = false;
                }
            }
        }

        if (flag)
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
    public int compareTo(Contact o) {
        int res=this.name.compareTo(o.name);

        if(res==0)
            return this.number.compareTo(o.number);

        return res;
    }

    public String getName(){
        return name;
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

// Вашиот код овде



