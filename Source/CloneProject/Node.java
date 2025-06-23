public class Node {
    public int id;
    public String name;
    public int x, y;
    public int kapasitas; // untuk label "Kap:"
    public int risiko;    // 1: rendah, 2: sedang, 3: tinggi

    public Node(int id, String name, int x, int y, int kapasitas, int risiko) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.kapasitas = kapasitas;
        this.risiko = risiko;
    }
}
