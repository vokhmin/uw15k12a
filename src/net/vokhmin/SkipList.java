package net.vokhmin;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

/**
 * This class implements SkipList algorithms.
 * Reference: http://www.sable.mcgill.ca/%7Edbelan2/cs251/skip_lists.html
 *
 * @param <Key>
 * @param <Value>
 */
public class SkipList<Key extends Comparable<? super Key>, Value> {

    public static final int MAX_LEVEL = 5;

    final private Node nil;
    final private Node head;
    final private Random random = new Random(System.currentTimeMillis());

    private int size;
    private int level;

    public SkipList(Key maxKey) {
        head = new Node(null, null, MAX_LEVEL);
        nil = new Node(maxKey, null, MAX_LEVEL);
        clear();
    }

    public void insert(Key k, Value v) {
        Node[] update = newNodeArray(MAX_LEVEL);
        Node x = searchByKey(k, update);
        if (x.key.compareTo(k) == 0) {
            x.val = v;
            return;
        }
        int newLevel = randomLevel();
        x = new Node(k, v, newLevel);
        System.out.printf("Try to insert the element %s\n", x);
        size++;
        if (newLevel > level) {
            for (int i = level; i < newLevel; i++) {
                update[i] = head;
            }
            level = newLevel;
        }
        for (int i = 0; i < newLevel; i++ ) {
            x.forward[i] = update[i].forward[i];
            update[i].forward[i] = x;
        }
    }

    public void remove(Key k) {
        Node[] update = newNodeArray(level);
        Node x = searchByKey(k, update);
//        x = x.forward[0];
        if (x.key.compareTo(k) != 0) {
            return;
        }
        for (int i = 0; i < x.level(); i++) {
            if (update[i].forward[i] != x) {
                break;
            }
            update[i].forward[i] = x.forward[i];
        }
        x = null;
        size--;
        for (int i = level - 1; i >= 0 && head.forward[i] == nil; i-- ) {
            head.forward[i] = nil;
            level--;
        }
    }

    public Value find(Key k) {
        Node x = head;
        for (int i = level - 1; i >= 0; i--) {
            while (x.forward[i].key.compareTo(k) < 0) {
                x = x.forward[i];
            }
        }
        x = x.forward[0];
        return (x.key.compareTo(k) == 0) ? x.val : null;
    }

    public void clear() {
        size = 0;
        level = 1;
        Arrays.fill(head.forward, nil);
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SkipList{");
        sb.append("nil=").append(nil);
        sb.append(", head=").append(head);
        sb.append(", random=").append(random);
        sb.append(", size=").append(size);
        sb.append(", level=").append(level);
        sb.append('}');
        return sb.toString();
    }

    private int randomLevel() {
        return random.nextInt(MAX_LEVEL) + 1;
    }

    private Node[] newNodeArray(int level) {
        return (Node[]) Array.newInstance(Node.class, level);
    }

    private Node searchByKey(Key k, Node[] update) {
        Node x = head;
        for (int i = level - 1; i >= 0; i--) {
            while (x.forward[i].key.compareTo(k) < 0) {
                x = x.forward[i];
            }
            update[i] = x;
        }
        return x.forward[0];
    }

    private class Node {
        Key key;
        Value val;
        final Node[] forward;

        public Node(Key key, Value val, int level) {
            this.key = key;
            this.val = val;
            this.forward = newNodeArray(level);
        }

        public int level() {
            return forward.length;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Node{");
            sb.append("key=").append(key);
            sb.append(", val=").append(val);
            sb.append(", forward=").append(Arrays.toString(forward));
            sb.append('}');
            return sb.toString();
        }
    }
}
