
import java.util.*;

public class BPlusTree {
    static final int MAX = 3;

    static class Node {
        boolean leaf;
        ArrayList<Integer> keys = new ArrayList<>();
        ArrayList<Node> children = new ArrayList<>();
        Node next;

        Node(boolean leaf) {
            this.leaf = leaf;
        }
    }

    Node root = new Node(true);

    // First key in subtree
    int first(Node n) {
        while (!n.leaf)
            n = n.children.get(0);
        return n.keys.get(0);
    }

    // Update index keys
    void refresh(Node n) {
        if (n.leaf) return;

        n.keys.clear();
        for (int i = 1; i < n.children.size(); i++)
            n.keys.add(first(n.children.get(i)));
    }

    int index(Node n, int key) {
        int i = 0;
        while (i < n.keys.size() && key >= n.keys.get(i))
            i++;
        return i;
    }

    // Search
    boolean search(int key) {
        Node n = root;

        while (!n.leaf)
            n = n.children.get(index(n, key));

        return n.keys.contains(key);
    }

    // Insertion
    void insert(int key) {
        if (search(key)) {
            System.out.println("Duplicate key");
            return;
        }

        Node right = insert(root, key);

        if (right != null) {
            Node parent = new Node(false);
            parent.children.add(root);
            parent.children.add(right);
            refresh(parent);
            root = parent;
        }
    }

    Node insert(Node n, int key) {
        if (n.leaf) {
            int i = 0;
            while (i < n.keys.size() &&
                   n.keys.get(i) < key)
                i++;

            n.keys.add(i, key);

            if (n.keys.size() <= MAX)
                return null;

            // Leaf splitting
            Node right = new Node(true);
            int mid = (n.keys.size() + 1) / 2;

            while (n.keys.size() > mid)
                right.keys.add(n.keys.remove(mid));

            right.next = n.next;
            n.next = right;
            return right;
        }

        int i = index(n, key);
        Node right = insert(n.children.get(i), key);

        if (right != null)
            n.children.add(i + 1, right);

        refresh(n);

        if (n.keys.size() <= MAX)
            return null;

        // Internal node splitting
        Node sibling = new Node(false);
        int leftCount = (n.children.size() + 1) / 2;

        while (n.children.size() > leftCount)
            sibling.children.add(
                n.children.remove(leftCount)
            );

        refresh(n);
        refresh(sibling);
        return sibling;
    }

    // Deletion
    void delete(int key) {
        if (!search(key)) {
            System.out.println("Key not found");
            return;
        }

        delete(root, key);

        // Root adjustment
        if (!root.leaf && root.children.size() == 1)
            root = root.children.get(0);

        System.out.println("Deleted");
    }

    void delete(Node n, int key) {
        if (n.leaf) {
            n.keys.remove(Integer.valueOf(key));
            return;
        }

        int i = index(n, key);
        Node child = n.children.get(i);

        delete(child, key);

        int min = child.leaf
                ? (MAX + 1) / 2
                : (MAX + 2) / 2 - 1;

        if (child.keys.size() < min) {
            Node left = i > 0
                ? n.children.get(i - 1) : null;

            Node right = i + 1 < n.children.size()
                ? n.children.get(i + 1) : null;

            // Borrow from left sibling
            if (left != null && left.keys.size() > min) {
                if (child.leaf)
                    child.keys.add(0,
                        left.keys.remove(left.keys.size() - 1));
                else
                    child.children.add(0,
                        left.children.remove(left.children.size() - 1));

                refresh(left);
                refresh(child);
            }

            // Borrow from right sibling
            else if (right != null && right.keys.size() > min) {
                if (child.leaf)
                    child.keys.add(right.keys.remove(0));
                else
                    child.children.add(
                        right.children.remove(0));

                refresh(right);
                refresh(child);
            }

            // Merge with left
            else if (left != null) {
                merge(left, child);
                n.children.remove(i);
            }

            // Merge with right
            else if (right != null) {
                merge(child, right);
                n.children.remove(i + 1);
            }
        }

        refresh(n);
    }

    // Merge nodes
    void merge(Node a, Node b) {
        if (a.leaf) {
            a.keys.addAll(b.keys);
            a.next = b.next;
        } else {
            a.children.addAll(b.children);
            refresh(a);
        }
    }

    // Display tree
    void display(Node n, int level) {
        System.out.println(
            "  ".repeat(level) +
            (n.leaf ? "Leaf " : "Index ") + n.keys
        );

        if (!n.leaf)
            for (Node c : n.children)
                display(c, level + 1);
    }

    // Display linked leaves
    void leaves() {
        Node n = root;

        while (!n.leaf)
            n = n.children.get(0);

        while (n != null) {
            System.out.print(n.keys + " -> ");
            n = n.next;
        }

        System.out.println("null");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BPlusTree tree = new BPlusTree();

        while (true) {
            System.out.println(
                "\n1.Insert 2.Delete 3.Display 4.Search 5.Exit"
            );

            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Key: ");
                    tree.insert(sc.nextInt());
                    break;

                case 2:
                    System.out.print("Key: ");
                    tree.delete(sc.nextInt());
                    break;

                case 3:
                    tree.display(tree.root, 0);
                    tree.leaves();
                    break;

                case 4:
                    System.out.print("Key: ");
                    System.out.println(
                        tree.search(sc.nextInt())
                    );
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
