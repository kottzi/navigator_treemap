package treemap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeMap<K extends Comparable<K>, V> implements Iterable<TreeMap<K, V>.Node<K, V>> {
    enum Color {RED, BLACK}

    class Node<K, V> {
        public K key;
        public V value;
        public Node<K, V> leftChild;
        public Node<K, V> rightChild;
        public Node<K, V> parent;
        public Color color;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.color = Color.RED;
        }
    }

    private Node<K, V> root;
    private int size;


    public void insert(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        if (root == null) {
            root = node;
            root.color = Color.BLACK;
            size++;
        } else {
            insertNode(node);
            fixAfterInsert(node);
            size++;
        }
    }

    private void insertNode(Node<K, V> newNode) {
        Node<K, V> current = root;
        Node<K, V> parent = null;

        while (current != null) {
            parent = current;
            if (newNode.key.compareTo(current.key) < 0) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
        }

        newNode.parent = parent;

        if (parent == null) {
            root = newNode;
        } else if (newNode.key.compareTo(parent.key) < 0) {
            parent.leftChild = newNode;
        } else {
            parent.rightChild = newNode;
        }
    }

    private void fixAfterInsert(Node<K, V> node) {
        while (node.parent != null && node.parent.color == Color.RED) {
            if (node.parent == node.parent.parent.leftChild) {
                Node<K, V> uncle = node.parent.parent.rightChild;
                if (uncle != null && uncle.color == Color.RED) {
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.rightChild) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                Node<K, V> uncle = node.parent.parent.leftChild;
                if (uncle != null && uncle.color == Color.RED) {
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.leftChild) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    rotateLeft(node.parent.parent);
                }
            }
            root.color = Color.BLACK;
        }
    }

    private void rotateLeft(Node<K, V> node) {
        Node<K, V> n = node.rightChild;
        node.rightChild = n.leftChild;
        if (n.leftChild != null) {
            n.leftChild.parent = node;
        }
        n.parent = node.parent;
        if (node.parent == null) {
            root = n;
        } else if (node == node.parent.leftChild) {
            node.parent.leftChild = n;
        } else {
            node.parent.rightChild = n;
        }
        n.leftChild = node;
        node.parent = n;
    }

    private void rotateRight(Node<K, V> node) {
        Node<K, V> n = node.leftChild;
        node.leftChild = n.rightChild;
        if (n.rightChild != null) {
            n.rightChild.parent = node;
        }
        n.parent = node.parent;
        if (node.parent == null) {
            root = n;
        } else if (node == node.parent.leftChild) {
            node.parent.leftChild = n;
        } else {
            node.parent.rightChild = n;
        }
        n.rightChild = node;
        node.parent = n;
    }

    public void remove(K key) {
        Node<K, V> node = binarySearch(key);
        if (node != null) {
            root = removeNode(root, key);
            size--;
        }
    }

    private Node<K, V> removeNode(Node<K, V> node, K key) {
        if (node == null) return null;

        int compareResult = key.compareTo(node.key);

        if (compareResult < 0) {
            node.leftChild = removeNode(node.leftChild, key);
        } else if (compareResult > 0) {
            node.rightChild = removeNode(node.rightChild, key);
        } else {
            if (node.leftChild == null) {
                return node.rightChild;
            } else if (node.rightChild == null) {
                return node.leftChild;
            }

            node.key = minValue(node.rightChild).key;
            node.rightChild = removeNode(node.rightChild, node.key);
        }

        if (node != null) {
            fixAfterRemove(node);
        }

        return node;
    }

    private Node<K, V> minValue(Node<K, V> node) {
        Node<K, V> current = node;
        while (current.leftChild != null) {
            current = current.leftChild;
        }
        return current;
    }

    private void fixAfterRemove(Node<K, V> node) {
        while (node != root && colorOf(node) == Color.BLACK) {
            if (node == leftOf(parentOf(node))) {
                Node<K, V> sibling = rightOf(parentOf(node));
                if (colorOf(sibling) == Color.RED) {
                    setColor(sibling, Color.BLACK);
                    setColor(parentOf(node), Color.RED);
                    rotateLeft(parentOf(node));
                    sibling = rightOf(parentOf(node));
                }

                if (colorOf(leftOf(sibling)) == Color.BLACK && colorOf(rightOf(sibling)) == Color.BLACK) {
                    setColor(sibling, Color.RED);
                    node = parentOf(node);
                } else {
                    if (colorOf(rightOf(sibling)) == Color.BLACK) {
                        setColor(leftOf(sibling), Color.BLACK);
                        setColor(sibling, Color.RED);
                        rotateRight(sibling);
                        sibling = rightOf(parentOf(node));
                    }

                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), Color.BLACK);
                    setColor(rightOf(sibling), Color.BLACK);
                    rotateLeft(parentOf(node));
                    node = root;
                }
            } else {
                Node<K, V> sibling = leftOf(parentOf(node));
                if (colorOf(sibling) == Color.RED) {
                    setColor(sibling, Color.BLACK);
                    setColor(parentOf(node), Color.RED);
                    rotateLeft(parentOf(node));
                    sibling = leftOf(parentOf(node));
                }
                if (colorOf(rightOf(sibling)) == Color.BLACK && colorOf(leftOf(sibling)) == Color.BLACK) {
                    setColor(sibling, Color.RED);
                    node = parentOf(node);
                } else {
                    if (colorOf(leftOf(sibling)) == Color.BLACK) {
                        setColor(rightOf(sibling), Color.BLACK);
                        setColor(sibling, Color.RED);
                        rotateLeft(sibling);
                        sibling = leftOf(parentOf(node));
                    }
                    setColor(sibling, colorOf(parentOf(node)));
                    setColor(parentOf(node), Color.BLACK);
                    setColor(leftOf(sibling), Color.BLACK);
                    rotateRight(parentOf(node));
                    node = root;
                }
            }
        }
        setColor(node, Color.BLACK);
    }


    private void setColor(Node<K, V> node, Color color) {
        if (node != null) {
            node.color = color;
        }
    }

    private Color colorOf(Node<K, V> node) {
        return (node == null) ? Color.BLACK : node.color;
    }

    private Node<K, V> parentOf(Node<K, V> node) {
        return (node == null) ? null : node.parent;
    }

    private Node<K, V> leftOf(Node<K, V> node) {
        return (node == null) ? null : node.leftChild;
    }

    private Node<K, V> rightOf(Node<K, V> node) {
        return (node == null) ? null : node.rightChild;
    }

    public boolean containsKey(K key) {
        return binarySearch(key) != null;
    }

    public boolean containsValue(V value) {
        for (V v : values()) {
            if (v.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public V get(K key) {
        Node<K, V> node = binarySearch(key);
        return (node != null) ? node.value : null;
    }

    @Override
    public Iterator<Node<K, V>> iterator() {
        Iterable<Node<K, V>> list = preOrder();
        return list.iterator();
    }

    public Iterable<K> keys() {
        List<K> keysList = new ArrayList<>();
        preOrderKeys(root, keysList);
        return keysList;
    }

    public Iterable<V> values() {
        List<V> valuesList = new ArrayList<>();
        preOrderValues(root, valuesList);
        return valuesList;
    }

    public Iterable<Node<K, V>> preOrder() {
        List<Node<K, V>> result = new ArrayList<>();
        preOrder(root, result);
        return result;
    }

    private void preOrder(Node<K, V> node, List<Node<K, V>> result) {
        if (node != null) {
            result.add(node);
            preOrder(node.leftChild, result);
            preOrder(node.rightChild, result);
        }
    }

    private void preOrderKeys(Node<K, V> node, List<K> keysList) {
        if (node != null) {
            keysList.add(node.key);
            preOrderKeys(node.leftChild, keysList);
            preOrderKeys(node.rightChild, keysList);
        }
    }

    private void preOrderValues(Node<K, V> node, List<V> valuesList) {
        if (node != null) {
            valuesList.add(node.value);
            preOrderValues(node.leftChild, valuesList);
            preOrderValues(node.rightChild, valuesList);
        }
    }

    private Node<K, V> binarySearch(K key) {
        Node<K, V> compareNode = root;
        while (compareNode != null) {
            int compareResult = key.compareTo(compareNode.key);
            if (compareResult == 0) {
                return compareNode;
            } else if (compareResult < 0) {
                compareNode = compareNode.leftChild;
            } else {
                compareNode = compareNode.rightChild;
            }
        }
        return null;
    }

    public int size() {
        return this.size;
    }
}