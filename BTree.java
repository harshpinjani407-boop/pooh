
import java.util.Scanner;

public class BTree {

    static class Node {
        int[] keys;
        Node[] child;
        int n;
        boolean leaf;

        Node(int t, boolean leaf) {
            this.leaf = leaf;
            keys = new int[2 * t - 1];
            child = new Node[2 * t];
            n = 0;
        }
    }

    int t;
    Node root;

    BTree(int t) {
        this.t = t;
        root = null;
    }

    // Search
    boolean search(Node x, int k) {
        if (x == null) return false;

        int i = 0;
        while (i < x.n && k > x.keys[i])
            i++;

        if (i < x.n && x.keys[i] == k)
            return true;

        if (x.leaf) return false;

        return search(x.child[i], k);
    }

    // Insertion
    void insert(int k) {
        if (search(root, k)) {
            System.out.println("Duplicate key!");
            return;
        }

        if (root == null) {
            root = new Node(t, true);
            root.keys[0] = k;
            root.n = 1;
            return;
        }

        if (root.n == 2 * t - 1) {
            Node s = new Node(t, false);
            s.child[0] = root;
            splitChild(s, 0);

            int i = (k > s.keys[0]) ? 1 : 0;
            insertNonFull(s.child[i], k);
            root = s;
        } else {
            insertNonFull(root, k);
        }
    }

    void insertNonFull(Node x, int k) {
        int i = x.n - 1;

        if (x.leaf) {
            while (i >= 0 && k < x.keys[i]) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }
            x.keys[i + 1] = k;
            x.n++;
        } else {
            while (i >= 0 && k < x.keys[i])
                i--;

            i++;

            if (x.child[i].n == 2 * t - 1) {
                splitChild(x, i);
                if (k > x.keys[i])
                    i++;
            }

            insertNonFull(x.child[i], k);
        }
    }

    void splitChild(Node x, int i) {
        Node y = x.child[i];
        Node z = new Node(t, y.leaf);
        z.n = t - 1;

        for (int j = 0; j < t - 1; j++)
            z.keys[j] = y.keys[j + t];

        if (!y.leaf) {
            for (int j = 0; j < t; j++)
                z.child[j] = y.child[j + t];
        }

        y.n = t - 1;

        for (int j = x.n; j >= i + 1; j--)
            x.child[j + 1] = x.child[j];

        x.child[i + 1] = z;

        for (int j = x.n - 1; j >= i; j--)
            x.keys[j + 1] = x.keys[j];

        x.keys[i] = y.keys[t - 1];
        x.n++;
    }

    // Deletion
    void delete(int k) {
        if (root == null || !search(root, k)) {
            System.out.println("Key not found!");
            return;
        }

        remove(root, k);

        if (root.n == 0) {
            if (root.leaf)
                root = null;
            else
                root = root.child[0];
        }
    }

    void remove(Node x, int k) {
        int idx = 0;

        while (idx < x.n && x.keys[idx] < k)
            idx++;

        if (idx < x.n && x.keys[idx] == k) {
            if (x.leaf)
                removeFromLeaf(x, idx);
            else
                removeFromNonLeaf(x, idx);
        } else {
            if (x.leaf) return;

            boolean last = (idx == x.n);

            if (x.child[idx].n < t)
                fill(x, idx);

            if (last && idx > x.n)
                remove(x.child[idx - 1], k);
            else
                remove(x.child[idx], k);
        }
    }

    void removeFromLeaf(Node x, int idx) {
        for (int i = idx + 1; i < x.n; i++)
            x.keys[i - 1] = x.keys[i];

        x.n--;
    }

    void removeFromNonLeaf(Node x, int idx) {
        int k = x.keys[idx];

        if (x.child[idx].n >= t) {
            int pred = getPred(x, idx);
            x.keys[idx] = pred;
            remove(x.child[idx], pred);
        } else if (x.child[idx + 1].n >= t) {
            int succ = getSucc(x, idx);
            x.keys[idx] = succ;
            remove(x.child[idx + 1], succ);
        } else {
            merge(x, idx);
            remove(x.child[idx], k);
        }
    }

    int getPred(Node x, int idx) {
        Node cur = x.child[idx];

        while (!cur.leaf)
            cur = cur.child[cur.n];

        return cur.keys[cur.n - 1];
    }

    int getSucc(Node x, int idx) {
        Node cur = x.child[idx + 1];

        while (!cur.leaf)
            cur = cur.child[0];

        return cur.keys[0];
    }

    void fill(Node x, int idx) {
        if (idx > 0 && x.child[idx - 1].n >= t)
            borrowFromPrev(x, idx);
        else if (idx < x.n && x.child[idx + 1].n >= t)
            borrowFromNext(x, idx);
        else {
            if (idx < x.n)
                merge(x, idx);
            else
                merge(x, idx - 1);
        }
    }

    void borrowFromPrev(Node x, int idx) {
        Node c = x.child[idx];
        Node s = x.child[idx - 1];

        for (int i = c.n - 1; i >= 0; i--)
            c.keys[i + 1] = c.keys[i];

        if (!c.leaf) {
            for (int i = c.n; i >= 0; i--)
                c.child[i + 1] = c.child[i];

            c.child[0] = s.child[s.n];
        }

        c.keys[0] = x.keys[idx - 1];
        x.keys[idx - 1] = s.keys[s.n - 1];

        c.n++;
        s.n--;
    }

    void borrowFromNext(Node x, int idx) {
        Node c = x.child[idx];
        Node s = x.child[idx + 1];

        c.keys[c.n] = x.keys[idx];

        if (!c.leaf)
            c.child[c.n + 1] = s.child[0];

        x.keys[idx] = s.keys[0];

        for (int i = 1; i < s.n; i++)
            s.keys[i - 1] = s.keys[i];

        if (!s.leaf) {
            for (int i = 1; i <= s.n; i++)
                s.child[i - 1] = s.child[i];
        }

        c.n++;
        s.n--;
    }

    void merge(Node x, int idx) {
        Node c = x.child[idx];
        Node s = x.child[idx + 1];

        c.keys[t - 1] = x.keys[idx];

        for (int i = 0; i < s.n; i++)
            c.keys[i + t] = s.keys[i];

        if (!c.leaf) {
            for (int i = 0; i <= s.n; i++)
                c.child[i + t] = s.child[i];
        }

        for (int i = idx + 1; i < x.n; i++)
            x.keys[i - 1] = x.keys[i];

        for (int i = idx + 2; i <= x.n; i++)
            x.child[i - 1] = x.child[i];

        c.n += s.n + 1;
        x.n--;
    }

    // Traversal
    void traverse(Node x) {
        if (x == null) return;

        int i;
        for (i = 0; i < x.n; i++) {
            if (!x.leaf)
                traverse(x.child[i]);

            System.out.print(x.keys[i] + " ");
        }

        if (!x.leaf)
            traverse(x.child[i]);
    }


    // Display B-Tree structure
    void display(Node x, int level) {
        if (x == null) {
            if (level == 0)
                System.out.println("B-Tree is empty!");
            return;
        }

        for (int i = 0; i < level; i++)
            System.out.print("    ");

        System.out.print("Level " + level + ": [");

        for (int i = 0; i < x.n; i++) {
            System.out.print(x.keys[i]);

            if (i < x.n - 1)
                System.out.print(" | ");
        }

        System.out.println("]");

        if (!x.leaf) {
            for (int i = 0; i <= x.n; i++)
                display(x.child[i], level + 1);
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter minimum degree (t >= 2): ");
        int t = sc.nextInt();

        if (t < 2) {
            System.out.println("Invalid minimum degree!");
            sc.close();
            return;
        }

        BTree tree = new BTree(t);

        while (true) {
            System.out.println("\n1. Insert");
            System.out.println("2. Delete");
            System.out.println("3. Search");
            System.out.println("4. Display");
            System.out.println("5. B-Tree Structure");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter key: ");
                    tree.insert(sc.nextInt());
                    break;

                case 2:
                    System.out.print("Enter key: ");
                    tree.delete(sc.nextInt());
                    break;

                case 3:
                    System.out.print("Enter key: ");
                    int k = sc.nextInt();
                    System.out.println(
                        tree.search(tree.root, k)
                        ? "Key Found" : "Key Not Found"
                    );
                    break;

                case 4:
                    System.out.print("B-Tree: ");
                    tree.traverse(tree.root);
                    System.out.println();
                    break;

                case 5:
                    System.out.println("\nB-Tree Structure:");
                    tree.display(tree.root, 0);

                    System.out.print("Sorted Traversal: ");
                    tree.traverse(tree.root);
                    System.out.println();
                    break;

                case 6:
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
