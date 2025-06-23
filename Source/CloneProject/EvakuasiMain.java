import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class EvakuasiMain extends JFrame {
    private Graph graph;
    private int start = 0;
    private int goal = 5;
    private int[] pathToGoal;
    private int pathIndex = 0;
    private javax.swing.Timer timer;

    private JLabel labelJarak;
    private JLabel labelWaktu;
    private JTextArea statusArea;
    private PetaPanel canvas;
    private JComboBox<String> startCombo;
    private JComboBox<String> goalCombo;
    private JSlider speedSlider;

    public EvakuasiMain() {
        graph = new Graph();
        setTitle("Simulasi Evakuasi Bencana - Dijkstra");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initGraph();

        canvas = new PetaPanel(graph);
        canvas.setPreferredSize(new Dimension(700, 600));
        add(canvas, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel nodePanel = new JPanel(new GridLayout(2, 2));
        nodePanel.add(new JLabel("Titik Awal:"));
        startCombo = new JComboBox<>();
        nodePanel.add(startCombo);
        nodePanel.add(new JLabel("Titik Tujuan:"));
        goalCombo = new JComboBox<>();
        nodePanel.add(goalCombo);
        rightPanel.add(nodePanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        labelJarak = new JLabel("Jarak: -", SwingConstants.CENTER);
        labelWaktu = new JLabel("Waktu Tempuh: -", SwingConstants.CENTER);
        infoPanel.add(labelJarak);
        infoPanel.add(labelWaktu);
        rightPanel.add(infoPanel, BorderLayout.CENTER);

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(statusArea);
        scroll.setPreferredSize(new Dimension(280, 200));
        rightPanel.add(scroll, BorderLayout.SOUTH);
        rightPanel.setPreferredSize(new Dimension(300, 600));
        add(rightPanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        JButton startBtn = new JButton("Start");
        JButton disturbBtn = new JButton("Gangguan Acak");
        JButton clearBtn = new JButton("Bersihkan");
        JButton exitBtn = new JButton("Exit");
        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setBorder(BorderFactory.createTitledBorder("Kecepatan Animasi"));

        buttonPanel.add(startBtn);
        buttonPanel.add(disturbBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(exitBtn);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(speedSlider, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        startBtn.addActionListener(e -> mulaiAnimasi());
        disturbBtn.addActionListener(e -> {
            graph.randomGanggu();
            pathToGoal = null;
            labelJarak.setText("Jarak: -");
            labelWaktu.setText("Waktu Tempuh: -");
            updateStatusJalan();
            canvas.repaint();
        });
        clearBtn.addActionListener(e -> {
            graph.clearGangguan();
            pathToGoal = null;
            labelJarak.setText("Jarak: -");
            labelWaktu.setText("Waktu Tempuh: -");
            updateStatusJalan();
            canvas.repaint();
        });
        exitBtn.addActionListener(e -> System.exit(0));

        updateNodeCombos();
    }

    private void updateNodeCombos() {
        startCombo.removeAllItems();
        goalCombo.removeAllItems();
        for (Node node : graph.nodes) {
            startCombo.addItem(node.name + " (" + node.id + ")");
            goalCombo.addItem(node.name + " (" + node.id + ")");
        }
        startCombo.setSelectedIndex(start);
        goalCombo.setSelectedIndex(goal);
    }

    private void mulaiAnimasi() {
        start = startCombo.getSelectedIndex();
        goal = goalCombo.getSelectedIndex();

        int[] prev = new int[graph.n];
        Arrays.fill(prev, -1);
        int[] jarak = graph.dijkstra(start, prev);

        if (jarak[goal] == Integer.MAX_VALUE) {
            JOptionPane.showMessageDialog(this, "Tidak ada jalur yang tersedia ke tujuan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pathToGoal = graph.rekonstruksiJalur(prev, goal).stream().mapToInt(i -> i).toArray();
        labelJarak.setText("Jarak: " + jarak[goal] + " km");
        double waktuTempuh = jarak[goal] / 40.0;
        labelWaktu.setText(String.format("Waktu Tempuh: %.1f jam", waktuTempuh));
        updateStatusJalan();
        pathIndex = 0;

        if (timer != null && timer.isRunning()) timer.stop();
        int delay = 1100 - (speedSlider.getValue() * 100);
        timer = new javax.swing.Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pathIndex++;
                if (pathIndex >= pathToGoal.length) timer.stop();
                canvas.setPath(pathToGoal, pathIndex);
                canvas.repaint();
            }
        });
        timer.start();
    }

    private void updateStatusJalan() {
        List<String> list = graph.daftarStatusJalan();
        statusArea.setText(String.join("\n", list));
    }

    private void initGraph() {
    graph.addNode(0, "A", 100, 300, 500, 1);
    graph.addNode(1, "B", 200, 200, 300, 2);
    graph.addNode(2, "C", 300, 300, 400, 1);
    graph.addNode(3, "D", 400, 200, 200, 3);
    graph.addNode(4, "E", 500, 300, 300, 2);
    graph.addNode(5, "F", 600, 200, 400, 1);
    graph.addNode(6, "G", 700, 300, 500, 1);
    graph.addNode(7, "H", 800, 200, 200, 3);  

    graph.tambahEdge(0, 1, 4, 2, 1);    
    graph.tambahEdge(0, 2, 2, 1, 2);     
    graph.tambahEdge(1, 2, 1, 2, 1);     
    graph.tambahEdge(1, 3, 5, 3, 2);     
    graph.tambahEdge(2, 3, 8, 2, 2);     
    graph.tambahEdge(3, 4, 6, 1, 2);     
    graph.tambahEdge(4, 5, 3, 2, 1);     
    graph.tambahEdge(2, 5, 10, 1, 3);    
    graph.tambahEdge(5, 6, 7, 2, 1);     
    graph.tambahEdge(4, 6, 4, 2, 1);     
    graph.tambahEdge(6, 7, 5, 1, 2);
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EvakuasiMain().setVisible(true));
    }
}

