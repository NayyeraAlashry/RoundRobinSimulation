package roundrobin;
//shams
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.border.TitledBorder;

public class RoundRobin extends JFrame {
    private JPanel mainPanel;
    private JSpinner processCountSpinner;
    private JSpinner quantumSpinner;
    private JButton createProcessTableButton;
    private JTable processTable;
    private JButton calculateButton;
    private JTextArea resultArea;
    private JPanel ganttChartPanel;
    private JScrollPane tableScrollPane;
    
    private ProcessTableModel tableModel;
    private List<Process> processes;
    private List<GanttEntry> ganttChart;
    
    // Colors
    private final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel blue
    private final Color ACCENT_COLOR = new Color(34, 139, 34); // Forest green
    private final Color BACKGROUND_COLOR = new Color(240, 248, 255); // Alice blue
    private final Color TEXT_COLOR = new Color(50, 50, 50);
    
    public RoundRobin() {
        setTitle("Round Robin Scheduling Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750); 
        setLocationRelativeTo(null);
        
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/cpu_icon.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            
        }
        
        initializeComponents();
        setupLayout();
        addEventListeners();
        
        setVisible(true);
    }
    
    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel inputPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Simulation Parameters",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                PRIMARY_COLOR));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setOpaque(true);
        
        // Number of Processes
        JLabel processCountLabel = new JLabel("Number of Processes:");
        processCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(processCountLabel);
        
        processCountSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));
        styleSpinner(processCountSpinner);
        
        inputPanel.add(processCountSpinner);
        
        // Time Quantum
        JLabel quantumLabel = new JLabel("Time Quantum:");
        quantumLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputPanel.add(quantumLabel);
        
        quantumSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        styleSpinner(quantumSpinner);
        inputPanel.add(quantumSpinner);
        
        // Process Table
        createProcessTableButton = new JButton("Create Process Table");
        styleButton(createProcessTableButton, PRIMARY_COLOR);
        
        // table
        tableModel = new ProcessTableModel();
        processTable = new JTable(tableModel);
        processTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        processTable.setRowHeight(25);
        processTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        processTable.setGridColor(new Color(200, 200, 200));
        processTable.setShowGrid(true);
        
        tableScrollPane = new JScrollPane(processTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Process Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                PRIMARY_COLOR));
        tableScrollPane.setPreferredSize(new Dimension(450, 180));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        calculateButton = new JButton("Run Round Robin Simulation");
        styleButton(calculateButton, ACCENT_COLOR);
        buttonPanel.add(calculateButton);
        
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setForeground(TEXT_COLOR);
        resultArea.setBackground(new Color(253, 253, 253));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Simulation Results",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                PRIMARY_COLOR));
        
        // Gantt chart panel
        ganttChartPanel = new JPanel();
        ganttChartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Gantt Chart",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                PRIMARY_COLOR));
        ganttChartPanel.setPreferredSize(new Dimension(850, 120));
        ganttChartPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel topPanel = new JPanel(new BorderLayout(15, 15));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(createProcessTableButton, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(15, 15));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(resultScrollPane, BorderLayout.CENTER);
        bottomPanel.add(ganttChartPanel, BorderLayout.SOUTH);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JFormattedTextField tf = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        tf.setBackground(Color.white);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.black);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private void setupLayout() {
        
    }
    
    private void addEventListeners() {
        createProcessTableButton.addActionListener(e -> createProcessTable());
        calculateButton.addActionListener(e -> runSimulation());
    }
    
    private void createProcessTable() {
        int processCount = (Integer) processCountSpinner.getValue();
        tableModel.setRowCount(processCount);
    }
    
    ///////////////////////////////////////////////
    private void runSimulation() {

        if (!validateInputs()) {
            return;
        }
        
        int quantum = (Integer) quantumSpinner.getValue();
        
        processes = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String processName = (String) tableModel.getValueAt(i, 0);
            int arrivalTime = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
            int burstTime = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
            
            processes.add(new Process(processName, arrivalTime, burstTime, quantum));
        }
        
        roundRobin(quantum);
        
        displayResults();
        
        drawGanttChart();
    }
    
    private boolean validateInputs() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String processName = (String) tableModel.getValueAt(i, 0);
            if (processName == null || processName.trim().isEmpty()) {
                showErrorDialog("Process name cannot be empty for process " + (i + 1));
                return false;
            }
            
            try {
                int arrivalTime = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                if (arrivalTime < 0) {
                    showErrorDialog("Arrival time cannot be negative for process " + (i + 1));
                    return false;
                }
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid arrival time for process " + (i + 1));
                return false;
            }
            
            try {
                int burstTime = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                if (burstTime <= 0) {
                    showErrorDialog("Burst time must be positive for process " + (i + 1));
                    return false;
                }
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid burst time for process " + (i + 1));
                return false;
            }
        }
        
        return true;
    }
    
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, 
                "<html><body><p style='width: 200px;'>" + message + "</p></body></html>",
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
    }
    
    private void roundRobin(int quantum) {
        ganttChart = new ArrayList<>();
        Queue<Process> readyQueue = new LinkedList<>();
        int currentTime = 0;
        int remainingProcesses = processes.size();
        
        int[] remainingBurst = new int[processes.size()];
        for (int i = 0; i < processes.size(); i++) {
            remainingBurst[i] = processes.get(i).burstTime;
        }
        
        for (Process p : processes) {
            p.responseTime = -1;
        }
        
        /////////////////////////////////////////////////////
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(i).arrivalTime == currentTime) {
                readyQueue.add(processes.get(i));
            }
        }
        
        
        /////////////////////////////////////////////////////////////
        while (remainingProcesses > 0) {
            if (readyQueue.isEmpty()) {
                int nextArrival = findNextArrival(currentTime);
                if (nextArrival == Integer.MAX_VALUE) break;
                
                ganttChart.add(new GanttEntry("IDLE", currentTime, nextArrival - currentTime));
                currentTime = nextArrival;
                
                for (int i = 0; i < processes.size(); i++) {
                    if (processes.get(i).arrivalTime == currentTime) {
                        readyQueue.add(processes.get(i));
                    }
                }
                continue;
            }
            
            Process currentProcess = readyQueue.poll();
            int processIndex = processes.indexOf(currentProcess);
            
            if (currentProcess.responseTime == -1) {
                currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
            }
            
            int executionTime = Math.min(quantum, remainingBurst[processIndex]);
            
            ganttChart.add(new GanttEntry(currentProcess.name, currentTime, executionTime));
            
            remainingBurst[processIndex] -= executionTime;
            
            currentTime += executionTime;
            
            for (int i = 0; i < processes.size(); i++) {
                Process p = processes.get(i);
                if (p.arrivalTime > currentTime - executionTime && p.arrivalTime <= currentTime) {
                    readyQueue.add(p);
                }
            }
            
            if (remainingBurst[processIndex] > 0) {
                readyQueue.add(currentProcess);
            } else {
                remainingProcesses--;
                currentProcess.completionTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            }
        }
    }
    
    private int findNextArrival(int currentTime) {
        int minArrival = Integer.MAX_VALUE;
        for (Process p : processes) {
            if (p.arrivalTime > currentTime && p.arrivalTime < minArrival) {
                minArrival = p.arrivalTime;
            }
        }
        return minArrival;
    }
    
    private void displayResults() {
        StringBuilder sb = new StringBuilder();
        sb.append("Round Robin Scheduling Results (Quantum = ").append(quantumSpinner.getValue()).append(")\n\n");
        
        double avgWaiting = 0, avgTurnaround = 0, avgResponse = 0;
        
        sb.append(String.format("%-10s %-12s %-12s %-15s %-15s %-15s %-15s\n", 
                "Process", "Arrival", "Burst", "Completion", "Turnaround", "Waiting", "Response"));
        
        for (Process p : processes) {
            p.turnaroundTime();
            p.waitingTime();
            
            sb.append(String.format("%-10s %-12d %-12d %-15d %-15d %-15d %-15d\n", 
                    p.name, p.arrivalTime, p.burstTime, p.completionTime, 
                    p.turnaroundTime, p.waitingTime, p.responseTime));
            
            avgWaiting += p.waitingTime;
            avgTurnaround += p.turnaroundTime;
            avgResponse += p.responseTime;
        }
        
        avgWaiting /= processes.size();
        avgTurnaround /= processes.size();
        avgResponse /= processes.size();
        
        sb.append("\nAverage Waiting Time: ").append(String.format("%.2f", avgWaiting)).append("\n");
        sb.append("Average Turnaround Time: ").append(String.format("%.2f", avgTurnaround)).append("\n");
        sb.append("Average Response Time: ").append(String.format("%.2f", avgResponse)).append("\n");
        
        resultArea.setText(sb.toString());
    }
    
    private void drawGanttChart() {
        ganttChartPanel.removeAll();
        ganttChartPanel.setLayout(new BoxLayout(ganttChartPanel, BoxLayout.X_AXIS));
        
        if (ganttChart == null || ganttChart.isEmpty()) {
            JLabel noDataLabel = new JLabel("No Gantt chart data available");
            noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            noDataLabel.setForeground(Color.GRAY);
            ganttChartPanel.add(noDataLabel);
            ganttChartPanel.revalidate();
            ganttChartPanel.repaint();
            return;
        }
        
        int totalTime = 0;
        for (GanttEntry entry : ganttChart) {
            totalTime = Math.max(totalTime, entry.startTime + entry.duration);
        }
        
        int barHeight = 40;
        int panelHeight = 100;
        
        int scaleFactor = Math.max(1, (int) Math.ceil(totalTime / 50.0));
        int barWidthUnit = 30; 
        
        for (GanttEntry entry : ganttChart) {
            JPanel entryPanel = new JPanel(new BorderLayout());
            entryPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            
            JLabel barLabel = new JLabel(entry.processName, SwingConstants.CENTER);
            barLabel.setOpaque(true);
            barLabel.setBackground(getProcessColor(entry.processName));
            barLabel.setForeground(Color.WHITE);
            barLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            
            int width = entry.duration * barWidthUnit / scaleFactor;
            barLabel.setPreferredSize(new Dimension(width, barHeight));
            
            barLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            barLabel.setToolTipText(String.format("%s: %d-%d (%d units)", 
                    entry.processName, entry.startTime, 
                    entry.startTime + entry.duration, entry.duration));
            
            entryPanel.add(barLabel, BorderLayout.CENTER);
            
            JPanel timePanel = new JPanel(new GridLayout(1, 2));
            timePanel.setBackground(BACKGROUND_COLOR);
            
            JLabel startLabel = new JLabel(Integer.toString(entry.startTime), SwingConstants.LEFT);
            startLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            startLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            
            JLabel endLabel = new JLabel(Integer.toString(entry.startTime + entry.duration), SwingConstants.RIGHT);
            endLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            endLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            
            timePanel.add(startLabel);
            timePanel.add(endLabel);
            
            entryPanel.add(timePanel, BorderLayout.SOUTH);
            ganttChartPanel.add(entryPanel);
            
            ganttChartPanel.add(Box.createRigidArea(new Dimension(2, 0)));
        }
        
        ganttChartPanel.add(Box.createVerticalStrut(panelHeight));
        
        ganttChartPanel.revalidate();
        ganttChartPanel.repaint();
    }
    
    private Color getProcessColor(String processName) {
        if (processName.equals("IDLE")) {
            return new Color(220, 220, 220);
        }
        
        int hash = processName.hashCode();
        return new Color(
            Math.abs(hash % 56) + 150,  
            Math.abs((hash >> 8) % 56) + 150,  
            Math.abs((hash >> 16) % 56) + 150   
        );
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 12));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 12));
            UIManager.put("TitledBorder.font", new Font("Segoe UI", Font.BOLD, 12));
        } catch (Exception e) {
            
        }
        
        SwingUtilities.invokeLater(() -> {
            RoundRobin simulator = new RoundRobin();
            
            simulator.getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            simulator.getRootPane().putClientProperty("Window.shadow", Boolean.TRUE);
        });
    }
    
    
    ///////////////////////////////////////////////////
    class Process {
        String name;
        int arrivalTime;
        int burstTime;
        int remainingburstTime;
        int completionTime;
        int turnaroundTime;
        int waitingTime;
        int responseTime = -1;
        int quantumTime;

        
        
        Process(String name, int arrivalTime, int burstTime, int quantumTime) {
            this.name = name;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.remainingburstTime = burstTime;
            this.quantumTime = quantumTime;
        }
        
        
        void updateComplationTime (int currentTime) {
            int timeUsed = 0;
            if ( remainingburstTime > 0 ){
                if ( responseTime == -1 ){
                    responseTime = currentTime - arrivalTime;
                }
                
                timeUsed = Math.min(quantumTime, remainingburstTime);
                remainingburstTime -= timeUsed;
            }
            
            if ( remainingburstTime == 0 ){
                completionTime = currentTime + timeUsed;
            }
        }
        
        void turnaroundTime (){
            turnaroundTime = completionTime - arrivalTime;
        }
        
        void waitingTime () {
            waitingTime = turnaroundTime - burstTime;
        }
                            
    }
    /////////////////////////////////////////////////////////////
    
    class GanttEntry {
        String processName;
        int startTime;
        int duration;
        
        GanttEntry(String processName, int startTime, int duration) {
            this.processName = processName;
            this.startTime = startTime;
            this.duration = duration;
        }
    }
    
    class ProcessTableModel extends javax.swing.table.AbstractTableModel {
        private String[] columnNames = {"Process Name", "Arrival Time", "Burst Time"};
        private Object[][] data = {};
        
        @Override
        public int getRowCount() {
            return data.length;
        }
        
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
        
        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }
        
        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
        
        @Override
        public Class<?> getColumnClass(int c) {
            if (getRowCount() > 0) {
                return getValueAt(0, c).getClass();
            }
            return Object.class;
        }
        
        @Override
        public boolean isCellEditable(int row, int col) {
            return true;
        }
        
        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
        
        public void setRowCount(int rowCount) {
            Object[][] newData = new Object[rowCount][columnNames.length];
            
            int rowsToCopy = Math.min(rowCount, data.length);
            for (int i = 0; i < rowsToCopy; i++) {
                System.arraycopy(data[i], 0, newData[i], 0, columnNames.length);
            }
            
            for (int i = rowsToCopy; i < rowCount; i++) {
                newData[i][0] = "P" + (i + 1);
                newData[i][1] = 0;
                newData[i][2] = 1;
            }
            
            data = newData;
            fireTableDataChanged();
        }
    }
}