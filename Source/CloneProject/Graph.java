import java.util.*;

public class Graph {
    public List<Node> nodes = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();
    public int n = 0;

    public void addNode(int id, String name, int x, int y, int kapasitas, int risiko) {
        nodes.add(new Node(id, name, x, y, kapasitas, risiko));
        n = Math.max(n, id + 1);
    }

    public void tambahEdge(int from, int to, int jarak, int kondisi, int risikoGangguan) {
        boolean terganggu = risikoGangguan > 2; // contoh logika gangguan
        edges.add(new Edge(from, to, jarak, kondisi, terganggu));
    }

    public Node getNode(int id) {
        return nodes.get(id);
    }

    // Dummy implementasi untuk daftar status
    public List<String> daftarStatusJalan() {
        List<String> status = new ArrayList<>();
        for (Edge e : edges) {
            String kondisi = e.terganggu ? "Terhalang" :
                             e.kondisi == 3 ? "Rusak Sedang" :
                             e.kondisi == 2 ? "Rusak Ringan" : "Normal";
            status.add("Jalan dari " + getNode(e.from).name + " ke " + getNode(e.to).name + ": " + kondisi);
        }
        return status;
    }

    // Dummy implementasi Dijkstra (jika diperlukan)
    public int[] dijkstra(int start, int[] prev) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{start, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            int d = curr[1];
            if (d > dist[u]) continue;

            for (Edge e : edges) {
                if (e.from == u && !e.terganggu) {
                    int v = e.to;
                    int newDist = dist[u] + e.jarak;
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        prev[v] = u;
                        pq.add(new int[]{v, newDist});
                    }
                }
            }
        }

        return dist;
    }

    public List<Integer> rekonstruksiJalur(int[] prev, int goal) {
        List<Integer> path = new ArrayList<>();
        for (int at = goal; at != -1; at = prev[at]) {
            path.add(0, at);
        }
        return path;
    }

    public void randomGanggu() {
        Random rand = new Random();
        for (Edge e : edges) {
            e.terganggu = rand.nextInt(5) == 0; // 20% gangguan
        }
    }

    public void clearGangguan() {
        for (Edge e : edges) {
            e.terganggu = false;
        }
    }
}
 