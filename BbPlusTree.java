
import java.util.*;

public class BbPlusTree {
    static class Node {
        ArrayList<Integer> keys = new ArrayList<>();
        ArrayList<Node> child = new ArrayList<>();
        boolean leaf = true;
    }

    static Node root = new Node();
    static final int MAX = 3;

    static int insert(Node n, int k) {
        if (n.leaf) {
            int i = 0;
            while (i < n.keys.size() && n.keys.get(i) < k)
                i++;
            n.keys.add(i, k);
        } else {
            int i = 0;
            while (i < n.keys.size() && k >= n.keys.get(i))
                i++;

            int mid = insert(n.child.get(i), k);

            if (mid != -1) {
                Node right = splitNode;
                n.keys.add(i, mid);
                n.child.add(i + 1, right);
            }
        }

        if (n.keys.size() <= MAX) return -1;

        Node right = new Node();
        right.leaf = n.leaf;

        int mid = n.keys.size() / 2;
        int promote;

        if (n.leaf) {
            while (n.keys.size() > mid)
                right.keys.add(n.keys.remove(mid));
            promote = right.keys.get(0);
        } else {
            promote = n.keys.get(mid);
            while (n.keys.size() > mid + 1)
                right.keys.add(n.keys.remove(mid + 1));
            n.keys.remove(mid);

            while (n.child.size() > mid + 1)
                right.child.add(n.child.remove(mid + 1));
        }

        splitNode = right;
        return promote;
    }

    static Node splitNode;

    static void add(int k) {
        ArrayList<Integer> a = new ArrayList<>();
        collect(root, a);
        if (a.contains(k)) return;

        int mid = insert(root, k);

        if (mid != -1) {
            Node r = new Node();
            r.leaf = false;
            r.keys.add(mid);
            r.child.add(root);
            r.child.add(splitNode);
            root = r;
        }
    }

    static void collect(Node n, ArrayList<Integer> a) {
        if (n.leaf)
            a.addAll(n.keys);
        else
            for (Node c : n.child)
                collect(c, a);
    }

    static void delete(int k) {
        ArrayList<Integer> a = new ArrayList<>();
        collect(root, a);

        if (!a.remove(Integer.valueOf(k))) {
            System.out.println("Key not found");
            return;
        }

        root = new Node();
        for (int x : a) add(x);
        System.out.println("Deleted");
    }

    static void display(Node n, int level) {
        System.out.println("  ".repeat(level) +
            (n.leaf ? "Leaf " : "Index ") + n.keys);

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
                    add(sc.nextInt());
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
