import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/*
 * ==========================================================
 *             GYM MANAGEMENT SYSTEM
 * ==========================================================
 *
 * Concepts Covered:
 * 1. Variables, constants, command-line arguments
 * 2. if-else-if, for loop, break, continue
 * 3. Arrays and String methods
 * 4. Inheritance, Method Overriding
 * 5. Encapsulation, Constructors, this
 * 6. Interface
 * 7. Packages concept shown through classes in one file
 * 8. public, private, protected
 * 9. try-catch-finally
 * 10. User-defined exception
 * 11. ArrayList and HashMap
 * 12. Generics and Wildcards
 * 13. Multithreading, Runnable, Thread, sleep(), synchronized
 * 14. FileWriter, BufferedWriter
 * 15. Serialization
 * 16. BufferedReader and Scanner
 * 17. JFrame, JList, JRadioButton, JButton
 * 18. ActionListener
 *
 * No JDBC / DBMS used.
 */

// ==========================================================
// INTERFACE
// ==========================================================

interface Payable {
    double calculateFee();
}


// ==========================================================
// USER-DEFINED EXCEPTION
// ==========================================================

class InvalidMemberException extends Exception {
    public InvalidMemberException(String message) {
        super(message);
    }
}


// ==========================================================
// BASE CLASS - INHERITANCE
// ==========================================================

class Person implements Serializable {

    protected String name;
    protected int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
    }
}


// ==========================================================
// MEMBER CLASS
// INHERITANCE + ENCAPSULATION + CONSTRUCTOR
// ==========================================================

class Member extends Person implements Payable, Serializable {

    private int memberId;
    private String phone;
    private String plan;

    // Constant
    public static final double MONTHLY_FEE = 1000.0;

    public Member(int memberId, String name, int age,
                  String phone, String plan) {

        super(name, age);

        this.memberId = memberId;
        this.phone = phone;
        this.plan = plan;
    }

    // Getters
    public int getMemberId() {
        return memberId;
    }//hi this changes

    public String getName() {
        System.out.println("Getting name: " + name);
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPlan() {
        return plan;
    }

    // Setter
    public void setPlan(String plan) {
        this.plan = plan;
    }

    // Method overriding
    @Override
    public void display() {

        System.out.println("----------------------------");
        System.out.println("Member ID : " + memberId);
        System.out.println("Name      : " + name);
        System.out.println("Age       : " + age);
        System.out.println("Phone     : " + phone);
        System.out.println("Plan      : " + plan);
        System.out.println("Fee       : Rs." + calculateFee());
        System.out.println("----------------------------");
    }

    // Interface method
    @Override
    public double calculateFee() {

        if (plan.equalsIgnoreCase("Monthly")) {
            return 1000;
        }
        else if (plan.equalsIgnoreCase("Quarterly")) {
            return 2700;
        }
        else if (plan.equalsIgnoreCase("Yearly")) {
            return 10000;
        }

        return 0;
    }

    @Override
    public String toString() {
        return memberId + " - " + name + " - " + plan;
    }
}


// ==========================================================
// TRAINER CLASS
// INHERITANCE + METHOD OVERRIDING
// ==========================================================

class Trainer extends Person implements Serializable {

    private int trainerId;
    private String specialization;

    public Trainer(int trainerId, String name, int age,
                   String specialization) {

        super(name, age);

        this.trainerId = trainerId;
        this.specialization = specialization;
    }

    @Override
    public void display() {

        System.out.println("----------------------------");
        System.out.println("Trainer ID       : " + trainerId);
        System.out.println("Trainer Name     : " + name);
        System.out.println("Age              : " + age);
        System.out.println("Specialization   : " + specialization);
        System.out.println("----------------------------");
    }

    @Override
    public String toString() {
        return trainerId + " - " + name +
               " - " + specialization;
    }
}


// ==========================================================
// GENERIC CLASS
// ==========================================================

class GymBox<T> {

    private T data;

    public GymBox(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void displayData() {
        System.out.println("Generic Data: " + data);
    }
}


// ==========================================================
// FILE MANAGER
// FILE HANDLING + SERIALIZATION
// ==========================================================

class FileManager {

    private static final String FILE_NAME = "gym_members.dat";
    private static final String LOG_FILE = "gym_log.txt";

    // Serialization
    public static synchronized void saveMembers(
            ArrayList<Member> members) {

        try {

            ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream(FILE_NAME));

            out.writeObject(members);

            out.close();

            System.out.println("Members saved successfully.");

        }
        catch (IOException e) {

            System.out.println(
                    "Error while saving members: "
                    + e.getMessage());
        }
    }

    // Deserialization
    @SuppressWarnings("unchecked")
    public static ArrayList<Member> loadMembers() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {

            ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream(FILE_NAME));

            ArrayList<Member> members =
                    (ArrayList<Member>) in.readObject();

            in.close();

            return members;

        }
        catch (Exception e) {

            System.out.println(
                    "Unable to load saved data.");

            return new ArrayList<>();
        }
    }

    // FileWriter + BufferedWriter
    public static synchronized void writeLog(
            String message) {

        try {

            FileWriter fw =
                    new FileWriter(LOG_FILE, true);

            BufferedWriter bw =
                    new BufferedWriter(fw);

            bw.write(message);
            bw.newLine();

            bw.close();
            fw.close();

        }
        catch (IOException e) {

            System.out.println(
                    "Log error: " + e.getMessage());
        }
    }

    // BufferedReader
    public static void readLog() {

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(LOG_FILE));

            String line;

            System.out.println("\n===== GYM LOG =====");

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            br.close();

        }
        catch (IOException e) {

            System.out.println("No log file found.");
        }
    }
}


// ==========================================================
// MULTITHREADING
// Runnable + Thread + sleep()
// ==========================================================

class BackupThread implements Runnable {

    private ArrayList<Member> members;

    public BackupThread(ArrayList<Member> members) {
        this.members = members;
    }

    @Override
    public void run() {

        try {

            System.out.println(
                    "\nBackup thread started...");

            Thread.sleep(2000);

            FileManager.saveMembers(members);

            System.out.println(
                    "Backup completed.");

        }
        catch (InterruptedException e) {

            System.out.println(
                    "Backup interrupted.");
        }
    }
}


// ==========================================================
// GYM SERVICE
// COLLECTIONS + GENERICS + WILDCARDS
// ==========================================================

class GymService {

    private ArrayList<Member> members;

    // HashMap
    private HashMap<Integer, Member> memberMap;

    public GymService() {

        members = FileManager.loadMembers();

        memberMap =
                new HashMap<>();

        for (Member m : members) {
            memberMap.put(m.getMemberId(), m);
        }
    }

    // Add member
    public synchronized void addMember(
            Member member)
            throws InvalidMemberException {

        // String method
        if (member.getName() == null ||
            member.getName().trim().isEmpty()) {

            throw new InvalidMemberException(
                    "Member name cannot be empty.");
        }

        if (member.getPhone().length() != 10) {

            throw new InvalidMemberException(
                    "Phone number must contain 10 digits.");
        }

        if (memberMap.containsKey(
                member.getMemberId())) {

            throw new InvalidMemberException(
                    "Member ID already exists.");
        }

        members.add(member);

        memberMap.put(
                member.getMemberId(),
                member);

        FileManager.saveMembers(members);

        FileManager.writeLog(
                "Added member: " +
                member.getName());
    }

    // Display members
    public void displayMembers() {

        if (members.isEmpty()) {

            System.out.println(
                    "No members found.");

            return;
        }

        for (Member m : members) {
            m.display();
        }
    }

    // Search
    public Member searchMember(int id) {

        return memberMap.get(id);
    }

    // Delete
    public synchronized boolean deleteMember(
            int id) {

        Member member =
                memberMap.get(id);

        if (member == null) {
            return false;
        }

        members.remove(member);

        memberMap.remove(id);

        FileManager.saveMembers(members);

        FileManager.writeLog(
                "Deleted member: " +
                member.getName());

        return true;
    }

    // Generic wildcard method
    public void displayList(
            List<? extends Person> list) {

        System.out.println(
                "\n===== PERSON LIST =====");

        for (Person p : list) {
            p.display();
        }
    }

    // Get members
    public ArrayList<Member> getMembers() {
        return members;
    }

    // Backup
    public void startBackup() {

        Thread t =
                new Thread(
                        new BackupThread(members));

        t.start();
    }
}


// ==========================================================
// GUI
// JFrame + JList + JRadioButton + JButton
// ActionListener
// ==========================================================

class GymGUI extends JFrame implements ActionListener {

    private GymService service;

    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField phoneField;

    private JRadioButton monthly;
    private JRadioButton quarterly;
    private JRadioButton yearly;

    private JButton addButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton backupButton;

    private JList<String> memberList;

    private DefaultListModel<String> listModel;

    public GymGUI(GymService service) {

        this.service = service;

        setTitle("Gym Management System");

        setSize(650, 600);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        setLayout(
                new BorderLayout());

        // ==============================================
        // FORM PANEL
        // ==============================================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(6, 2, 10, 10));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10));

        formPanel.add(
                new JLabel("Member ID:"));

        idField =
                new JTextField();

        formPanel.add(idField);


        formPanel.add(
                new JLabel("Name:"));

        nameField =
                new JTextField();

        formPanel.add(nameField);


        formPanel.add(
                new JLabel("Age:"));

        ageField =
                new JTextField();

        formPanel.add(ageField);


        formPanel.add(
                new JLabel("Phone:"));

        phoneField =
                new JTextField();

        formPanel.add(phoneField);


        // Radio buttons

        formPanel.add(
                new JLabel("Plan:"));

        JPanel radioPanel =
                new JPanel();

        monthly =
                new JRadioButton("Monthly");

        quarterly =
                new JRadioButton("Quarterly");

        yearly =
                new JRadioButton("Yearly");

        ButtonGroup group =
                new ButtonGroup();

        group.add(monthly);
        group.add(quarterly);
        group.add(yearly);

        monthly.setSelected(true);

        radioPanel.add(monthly);
        radioPanel.add(quarterly);
        radioPanel.add(yearly);

        formPanel.add(radioPanel);


        // Add button

        addButton =
                new JButton("Add Member");

        addButton.addActionListener(this);

        formPanel.add(addButton);


        // Delete button

        deleteButton =
                new JButton("Delete");

        deleteButton.addActionListener(this);

        formPanel.add(deleteButton);


        add(formPanel,
            BorderLayout.NORTH);


        // ==============================================
        // JLIST
        // ==============================================

        listModel =
                new DefaultListModel<>();

        memberList =
                new JList<>(listModel);

        JScrollPane scrollPane =
                new JScrollPane(memberList);

        add(scrollPane,
            BorderLayout.CENTER);


        // ==============================================
        // BUTTON PANEL
        // ==============================================

        JPanel buttonPanel =
                new JPanel();

        refreshButton =
                new JButton("Refresh");

        backupButton =
                new JButton("Backup");

        refreshButton.addActionListener(this);

        backupButton.addActionListener(this);

        buttonPanel.add(refreshButton);

        buttonPanel.add(backupButton);

        add(buttonPanel,
            BorderLayout.SOUTH);


        refreshList();

        setLocationRelativeTo(null);

        setVisible(true);
    }


    // ==============================================
    // ACTION LISTENER
    // ==============================================

    @Override
    public void actionPerformed(
            ActionEvent e) {

        // ADD MEMBER

        if (e.getSource() == addButton) {

            try {

                int id =
                        Integer.parseInt(
                                idField.getText());

                int age =
                        Integer.parseInt(
                                ageField.getText());

                String name =
                        nameField.getText();

                String phone =
                        phoneField.getText();

                String plan;

                if (monthly.isSelected()) {
                    plan = "Monthly";
                }
                else if (quarterly.isSelected()) {
                    plan = "Quarterly";
                }
                else {
                    plan = "Yearly";
                }

                Member member =
                        new Member(
                                id,
                                name,
                                age,
                                phone,
                                plan);

                service.addMember(member);

                JOptionPane.showMessageDialog(
                        this,
                        "Member added successfully!");

                clearFields();

                refreshList();

            }
            catch (
                    NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Enter valid numbers.");
            }
            catch (
                    InvalidMemberException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage());
            }
            finally {

                System.out.println(
                        "Add operation completed.");
            }
        }


        // DELETE MEMBER

        else if (
                e.getSource() == deleteButton) {

            try {

                int id =
                        Integer.parseInt(
                                idField.getText());

                boolean result =
                        service.deleteMember(id);

                if (result) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Member deleted.");

                }
                else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Member not found.");
                }

                refreshList();

            }
            catch (
                    NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Enter a valid Member ID.");
            }
        }


        // REFRESH

        else if (
                e.getSource() == refreshButton) {

            refreshList();
        }


        // BACKUP

        else if (
                e.getSource() == backupButton) {

            service.startBackup();

            JOptionPane.showMessageDialog(
                    this,
                    "Backup started in background.");
        }
    }


    // Refresh JList

    private void refreshList() {

        listModel.clear();

        for (Member m :
                service.getMembers()) {

            listModel.addElement(
                    m.toString());
        }
    }


    // Clear input fields

    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        phoneField.setText("");
    }
}


// ==========================================================
// MAIN CLASS
// ==========================================================

public class GymManagementSystem {

    // Constants
    public static final String GYM_NAME =
            "FITNESS ZONE GYM";

    public static void main(String[] args) {

        System.out.println(
                "====================================");

        System.out.println(
                "       " + GYM_NAME);

        System.out.println(
                "====================================");


        // ==============================================
        // COMMAND-LINE ARGUMENT
        // ==============================================

        if (args.length > 0) {

            System.out.println(
                    "Welcome " + args[0] + "!");
        }
        else {

            System.out.println(
                    "Welcome to the Gym Management System!");
        }


        // ==============================================
        // CREATE SERVICE
        // ==============================================

        GymService service =
                new GymService();


        // ==============================================
        // GENERIC CLASS EXAMPLE
        // ==============================================

        GymBox<String> gymBox =
                new GymBox<>(
                        "Gym Management System");

        gymBox.displayData();


        // ==============================================
        // COMMAND LINE MENU
        // ==============================================

        Scanner scanner =
                new Scanner(System.in);

        boolean running = true;


        while (running) {

            System.out.println(
                    "\n========= MENU =========");

            System.out.println(
                    "1. Add Member");

            System.out.println(
                    "2. Display Members");

            System.out.println(
                    "3. Search Member");

            System.out.println(
                    "4. Delete Member");

            System.out.println(
                    "5. Start Backup");

            System.out.println(
                    "6. Read Log");

            System.out.println(
                    "7. Open GUI");

            System.out.println(
                    "8. Exit");

            System.out.print(
                    "Enter choice: ");


            try {

                int choice =
                        scanner.nextInt();

                switch (choice) {

                    case 1:

                        try {

                            System.out.print(
                                    "Enter Member ID: ");

                            int id =
                                    scanner.nextInt();

                            scanner.nextLine();

                            System.out.print(
                                    "Enter Name: ");

                            String name =
                                    scanner.nextLine();

                            System.out.print(
                                    "Enter Age: ");

                            int age =
                                    scanner.nextInt();

                            scanner.nextLine();

                            System.out.print(
                                    "Enter Phone: ");

                            String phone =
                                    scanner.nextLine();

                            System.out.println(
                                    "\nSelect Plan:");

                            System.out.println(
                                    "1. Monthly - Rs.1000");

                            System.out.println(
                                    "2. Quarterly - Rs.2700");

                            System.out.println(
                                    "3. Yearly - Rs.10000");

                            System.out.print(
                                    "Choice: ");

                            int planChoice =
                                    scanner.nextInt();

                            String plan;

                            if (planChoice == 1) {
                                plan = "Monthly";
                            }
                            else if (planChoice == 2) {
                                plan = "Quarterly";
                            }
                            else if (planChoice == 3) {
                                plan = "Yearly";
                            }
                            else {

                                System.out.println(
                                        "Invalid plan.");

                                continue;
                            }


                            Member member =
                                    new Member(
                                            id,
                                            name,
                                            age,
                                            phone,
                                            plan);

                            service.addMember(member);

                            System.out.println(
                                    "Member added successfully!");

                        }
                        catch (
                                InvalidMemberException e) {

                            System.out.println(
                                    "Error: "
                                    + e.getMessage());
                        }

                        break;


                    case 2:

                        service.displayMembers();

                        break;


                    case 3:

                        System.out.print(
                                "Enter Member ID: ");

                        int searchId =
                                scanner.nextInt();

                        Member found =
                                service.searchMember(
                                        searchId);

                        if (found != null) {

                            found.display();

                        }
                        else {

                            System.out.println(
                                    "Member not found.");
                        }

                        break;


                    case 4:

                        System.out.print(
                                "Enter Member ID: ");

                        int deleteId =
                                scanner.nextInt();

                        if (service.deleteMember(
                                deleteId)) {

                            System.out.println(
                                    "Member deleted.");

                        }
                        else {

                            System.out.println(
                                    "Member not found.");
                        }

                        break;


                    case 5:

                        service.startBackup();

                        break;


                    case 6:

                        FileManager.readLog();

                        break;


                    case 7:

                        SwingUtilities.invokeLater(
                                () -> {

                                    new GymGUI(service);

                                });

                        break;


                    case 8:

                        running = false;

                        System.out.println(
                                "Thank you for using "
                                + GYM_NAME);

                        break;


                    default:

                        System.out.println(
                                "Invalid choice.");

                        break;
                }

            }
            catch (
                    InputMismatchException e) {

                System.out.println(
                        "Please enter a valid number.");

                scanner.nextLine();
            }
        }

        scanner.close();
    }
}