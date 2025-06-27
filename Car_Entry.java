import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Car_Entry {
    HashMap<String, VehicleEntry> vehicleList = new HashMap<>();
    ArrayList<VehicleEntry> vehicleEntriesList = new ArrayList<>();

    JLabel vehicleNoLabel, vehicleTypeLabel, entryTimeLabel, slotAvailableLabel, slotValueLabel;
    JTextField vehicleNoField, entryTimeField;
    JComboBox<String> vehicleTypeDropdown;

    JButton allotButton, cancelButton, displayButton, exitButton;

    static int slot = 100;

    public Car_Entry() {
        JFrame carForm = new JFrame("Car Parking System");
        JPanel panel = new JPanel();
        panel.setLayout(null);

        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 13);

        vehicleNoLabel = new JLabel("Vehicle Number:");
        vehicleNoLabel.setFont(labelFont);
        vehicleNoLabel.setBounds(50, 30, 120, 25);
        panel.add(vehicleNoLabel);

        vehicleNoField = new JTextField();
        vehicleNoField.setFont(fieldFont);
        vehicleNoField.setBounds(180, 30, 180, 25);
        panel.add(vehicleNoField);

        vehicleTypeLabel = new JLabel("Vehicle Type:");
        vehicleTypeLabel.setFont(labelFont);
        vehicleTypeLabel.setBounds(50, 70, 120, 25);
        panel.add(vehicleTypeLabel);

        String[] vehicleTypes = { "Car", "Bike", "Truck", "Bus", "Van" };
        vehicleTypeDropdown = new JComboBox<>(vehicleTypes);
        vehicleTypeDropdown.setFont(fieldFont);
        vehicleTypeDropdown.setBounds(180, 70, 180, 25);
        panel.add(vehicleTypeDropdown);

        entryTimeLabel = new JLabel("Entry Time (HH:mm):");
        entryTimeLabel.setFont(labelFont);
        entryTimeLabel.setBounds(50, 110, 140, 25);
        panel.add(entryTimeLabel);

        entryTimeField = new JTextField();
        entryTimeField.setFont(fieldFont);
        entryTimeField.setBounds(180, 110, 180, 25);
        panel.add(entryTimeField);

        slotAvailableLabel = new JLabel("Slot Available:");
        slotAvailableLabel.setFont(labelFont);
        slotAvailableLabel.setBounds(50, 150, 120, 25);
        panel.add(slotAvailableLabel);

        slotValueLabel = new JLabel(String.valueOf(slot));
        slotValueLabel.setFont(labelFont);
        slotValueLabel.setBounds(180, 150, 180, 25);
        panel.add(slotValueLabel);

        allotButton = new JButton("Allot Vehicle");
        allotButton.setBounds(20, 200, 120, 30);
        panel.add(allotButton);
        allotButton.addActionListener(ae -> allotVehicle());

        cancelButton = new JButton("Clear");
        cancelButton.setBounds(150, 200, 100, 30);
        panel.add(cancelButton);
        cancelButton.addActionListener(e -> clearFields());

        displayButton = new JButton("Display All");
        displayButton.setBounds(260, 200, 110, 30);
        panel.add(displayButton);
        displayButton.addActionListener(e -> displayAllVehicles());

        exitButton = new JButton("Exit Vehicle");
        exitButton.setBounds(150, 240, 130, 30);
        panel.add(exitButton);
        exitButton.addActionListener(e -> openExitWindow());

        carForm.setSize(480, 330);
        carForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        carForm.setLocationRelativeTo(null);
        carForm.add(panel);
        carForm.setVisible(true);
    }

    int decrementSlot() {
        return --slot;
    }

    int incrementSlot() {
        return ++slot;
    }

    void allotVehicle() {
        String vno = vehicleNoField.getText().trim();
        String vtype = vehicleTypeDropdown.getSelectedItem().toString();
        String timeText = entryTimeField.getText().trim();

        if (vno.isEmpty() || timeText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter all fields.");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime entryTime = LocalTime.parse(timeText, formatter);

            if (vehicleList.containsKey(vno)) {
                JOptionPane.showMessageDialog(null, "Vehicle already parked.");
                return;
            }

            int assignedSlot = decrementSlot();
            VehicleEntry ve = new VehicleEntry(vno, vtype, entryTime, assignedSlot);
            vehicleList.put(vno, ve);
            vehicleEntriesList.add(ve);

            JOptionPane.showMessageDialog(null, "Vehicle Allotted at Slot " + assignedSlot);
            slotValueLabel.setText(String.valueOf(slot));
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid time format. Use HH:mm (e.g., 14:30)");
        }
    }

    void clearFields() {
        vehicleNoField.setText("");
        entryTimeField.setText("");
        vehicleTypeDropdown.setSelectedIndex(0);
    }

    void displayAllVehicles() {
        if (vehicleEntriesList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No vehicles parked.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-20s %-15s %-10s\n", "Slot", "Vehicle Number", "Vehicle Type", "Entry Time"));
        sb.append("--------------------------------------------------------------\n");

        for (VehicleEntry ve : vehicleEntriesList) {
            sb.append(String.format("%-10d %-20s %-15s %-10s\n",
                    ve.slot, ve.vehicleNumber, ve.vehicleType, ve.entryHour));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 250));

        JOptionPane.showMessageDialog(null, scrollPane, "Parked Vehicles", JOptionPane.INFORMATION_MESSAGE);
    }

    void openExitWindow() {
        if (vehicleList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No vehicles parked to exit.");
            return;
        }

        JFrame exitFrame = new JFrame("Exit Vehicle");
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel selectVehicleLabel = new JLabel("Select Vehicle:");
        selectVehicleLabel.setBounds(30, 30, 100, 25);
        panel.add(selectVehicleLabel);

        JComboBox<String> vehicleDropdown = new JComboBox<>();
        for (String vno : vehicleList.keySet()) {
            vehicleDropdown.addItem(vno);
        }
        vehicleDropdown.setBounds(140, 30, 160, 25);
        panel.add(vehicleDropdown);

        JLabel exitTimeLabel = new JLabel("Exit Time (HH:mm):");
        exitTimeLabel.setBounds(30, 70, 120, 25);
        panel.add(exitTimeLabel);

        JTextField exitTimeField = new JTextField();
        exitTimeField.setBounds(160, 70, 140, 25);
        panel.add(exitTimeField);

        JButton okButton = new JButton("OK");
        okButton.setBounds(100, 110, 100, 30);
        panel.add(okButton);

        okButton.addActionListener(e -> {
            String selectedVehicle = vehicleDropdown.getSelectedItem().toString();
            String exitTimeStr = exitTimeField.getText().trim();

            if (exitTimeStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter exit time.");
                return;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime exitTime = LocalTime.parse(exitTimeStr, formatter);
                VehicleEntry ve = vehicleList.remove(selectedVehicle);
                vehicleEntriesList.remove(ve);
                incrementSlot();
                slotValueLabel.setText(String.valueOf(slot));

                long hours = Duration.between(ve.entryHour, exitTime).toHours();
                if (hours <= 0) hours = 1; // Minimum 1 hour billing

                int rate = getRate(ve.vehicleType);
                long total = rate * hours;

                JOptionPane.showMessageDialog(null,
                        "Vehicle: " + ve.vehicleNumber +
                                "\nType: " + ve.vehicleType +
                                "\nSlot Freed: " + ve.slot +
                                "\nEntry Time: " + ve.entryHour +
                                "\nExit Time: " + exitTime +
                                "\nHours Parked: " + hours +
                                "\nRate/hr: ₹" + rate +
                                "\nTotal: ₹" + total,
                        "Bill", JOptionPane.INFORMATION_MESSAGE);

                exitFrame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid exit time format. Use HH:mm");
            }
        });

        exitFrame.setSize(360, 200);
        exitFrame.setLocationRelativeTo(null);
        exitFrame.add(panel);
        exitFrame.setVisible(true);
    }

    int getRate(String type) {
        return switch (type) {
            case "Car" -> 100;
            case "Bike" -> 30;
            case "Truck" -> 200;
            case "Bus" -> 150;
            case "Van" -> 80;
            default -> 100;
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Car_Entry::new);
    }
}

class VehicleEntry {
    String vehicleNumber;
    String vehicleType;
    LocalTime entryHour;
    int slot;

    public VehicleEntry(String vehicleNumber, String vehicleType, LocalTime entryHour, int slot) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.entryHour = entryHour;
        this.slot = slot;

        System.out.println(vehicleNumber + " | " + vehicleType + " | " + entryHour + " | Slot: " + slot);
    }
}
