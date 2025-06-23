import javax.swing.*;
import java.awt.*;

public class PetaPanel extends JPanel {
    private Graph graph;
    private int[] path;
    private int pathIndex;

    public PetaPanel(Graph graph) {
        this.graph = graph;
    }

    public void setPath(int[] path, int pathIndex) {
        this.path = path;
        this.pathIndex = pathIndex;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Aktifkan anti-aliasing untuk hasil lebih halus
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // ====== Gambar semua edge dengan warna dan ketebalan ======
        for (Edge edge : graph.edges) {
            Node from = graph.getNode(edge.from);
            Node to = graph.getNode(edge.to);

            g2.setColor(getEdgeColor(edge));
            g2.setStroke(new BasicStroke(5)); // Ganti ketebalan garis edge di sini
            g2.drawLine(from.x, from.y, to.x, to.y);

            // Tampilkan label jarak di tengah edge
            int mx = (from.x + to.x) / 2;
            int my = (from.y + to.y) / 2;
            g2.setColor(Color.BLACK);
            g2.drawString(edge.jarak + " km", mx + 5, my - 5);
        }

        // ====== Gambar jalur hasil Dijkstra ======
        if (path != null && path.length > 1) {
            g2.setColor(new Color(0, 180, 0, 180)); // Hijau transparan
            g2.setStroke(new BasicStroke(6)); // Jalur lebih tebal
            for (int i = 0; i < Math.min(pathIndex - 1, path.length - 1); i++) {
                Node from = graph.nodes.get(path[i]);
                Node to = graph.nodes.get(path[i + 1]);
                g2.drawLine(from.x, from.y, to.x, to.y);
            }

            // Gambar kendaraan evakuasi (mobil)
            int idx = Math.min(pathIndex, path.length - 1);
            Node mobil = graph.nodes.get(path[idx]);
            g.setColor(Color.PINK);
            g.fillRect(mobil.x - 15, mobil.y - 30, 30, 20);
            g.setColor(Color.BLACK);
            g.fillRect(mobil.x - 10, mobil.y - 25, 20, 5);
            g.fillRect(mobil.x - 5, mobil.y - 35, 10, 5);
        }

        // ====== Gambar semua node ======
        for (Node node : graph.nodes) {
            g.setColor(getNodeColor(node));
            g.fillOval(node.x - 15, node.y - 15, 30, 30); // Lebih besar dari default
            g.setColor(Color.WHITE);
            g.drawString(node.name, node.x - 10, node.y + 5);
            g.setColor(Color.BLACK);
            g.drawString("Kap: " + node.kapasitas, node.x - 15, node.y + 25);
        }

        // ====== Gambar legenda ======
        drawLegend(g);
    }

    private Color getEdgeColor(Edge e) {
        if (e.terganggu) return Color.RED;
        return switch (e.kondisi) {
            case 2 -> Color.YELLOW;
            case 3 -> Color.ORANGE;
            default -> Color.LIGHT_GRAY;
        };
    }

    private Color getNodeColor(Node node) {
        return switch (node.risiko) {
            case 2 -> Color.ORANGE;
            case 3 -> Color.RED;
            default -> Color.BLUE;
        };
    }

    private void drawLegend(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Legenda:", 20, 20);

        g.setColor(Color.BLUE);
        g.fillRect(20, 35, 15, 15);
        g.setColor(Color.BLACK);
        g.drawString("Risiko Rendah", 45, 47);

        g.setColor(Color.ORANGE);
        g.fillRect(20, 55, 15, 15);
        g.drawString("Risiko Sedang", 45, 67);

        g.setColor(Color.RED);
        g.fillRect(20, 75, 15, 15);
        g.drawString("Risiko Tinggi", 45, 87);

        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(20, 105, 50, 105);
        g.drawString("Jalan Normal", 55, 110);

        g.setColor(Color.YELLOW);
        g.drawLine(20, 125, 50, 125);
        g.drawString("Jalan Rusak Ringan", 55, 130);

        g.setColor(Color.ORANGE);
        g.drawLine(20, 145, 50, 145);
        g.drawString("Jalan Rusak Sedang", 55, 150);

        g.setColor(Color.RED);
        g.drawLine(20, 165, 50, 165);
        g.drawString("Jalan Terhalang", 55, 170);

        g.setColor(Color.ORANGE);
        g.fillRect(20, 185, 20, 15);
        g.setColor(Color.BLACK);
        g.drawString("Kendaraan Evakuasi", 45, 197);
    }
}
