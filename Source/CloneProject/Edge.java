public class Edge {
    public int from, to;
    public int jarak;
    public int kondisi;   // 1: normal, 2: rusak ringan, 3: rusak sedang
    public boolean terganggu;

    public Edge(int from, int to, int jarak, int kondisi, boolean terganggu) {
        this.from = from;
        this.to = to;
        this.jarak = jarak;
        this.kondisi = kondisi;
        this.terganggu = terganggu;
    }
}
 