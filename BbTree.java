
import java.util.*;

public class BbTree {
    static class Node {
        ArrayList<Integer> keys = new ArrayList<>();
        ArrayList<Node> child = new ArrayList<>();
        boolean leaf = true;
    }

    static Node root = new Node();
    static int t = 2;

    static void split(Node p, int i) {
        Node a = p.child.get(i);
        Node b = new Node();
        b.leaf = a.leaf;

        int mid = a.keys.get(t - 1);

        while (a.keys.size() > t)
            b.keys.add(a.keys.remove(t));

        a.keys.remove(t - 1);

        if (!a.leaf)
            while (a.child.size() > t)
                b.child.add(a.child.remove(t));

        p.keys.add(i, mid);
        p.child.add(i + 1, b);
    }

    static void insertNonFull(Node n, int k) {
        int i = 0;
        while (i < n.keys.size() && k > n.keys.get(i))
            i++;

        if (n.leaf) {
            n.keys.add(i, k);
        } else {
            if (n.child.get(i).keys.size() == 2*t-1) {
                split(n, i);
                if (k > n.keys.get(i)) i++;
            }
            insertNonFull(n.child.get(i), k);
        }
    }

    static boolean search(Node n, int k) {
        int i = 0;
        while (i < n.keys.size() && k > n.keys.get(i))
            i++;

        if (i < n.keys.size() && k == n.keys.get(i))
            return true;

        return !n.leaf && search(n.child.get(i), k);
    }

    static void insert(int k) {
        if (search(root, k)) return;

        if (root.keys.size() == 2*t-1) {
            Node r = new Node();
            r.leaf = false;
            r.child.add(root);
            split(r, 0);
            root = r;
        }
        insertNonFull(root, k);
    }

    static void collect(Node n, ArrayList<Integer> a) {
        for (int i = 0; i < n.keys.size(); i++) {
            if (!n.leaf) collect(n.child.get(i), a);
            a.add(n.keys.get(i));
        }
        if (!n.leaf)
            collect(n.child.get(n.keys.size()), a);
    }

    static void delete(int k) {
        ArrayList<Integer> a = new ArrayList<>();
        collect(root, a);

        if (!a.remove(Integer.valueOf(k))) {
            System.out.println("Key not found");
            return;
        }

        root = new Node();
        for (int x : a) insert(x);
        System.out.println("Deleted");
    }

    static void display(Node n, int level) {
        System.out.println("  ".repeat(level) + n.keys);
        if (!n.leaf)
            for (Node c : n.child)
                display(c, level + 1);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1.Insert 2.Delete 3.Display 4.Exit");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Key: ");
                    insert(sc.nextInt());
                    break;
                case 2:
                    System.out.print("Key: ");
                    delete(sc.nextInt());
                    break;
                case 3:
                    display(root, 0);
                    break;
                case 4:
                    return;
            }
        }
    }
}
