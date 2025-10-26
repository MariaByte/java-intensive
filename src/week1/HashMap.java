package week1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class HashMap<K, V> {
    private final int capacity;

    private static class Node<K, V> {
        K key;
        V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private LinkedList<Node<K, V>>[] buckets;

    public HashMap() {
        this(16);
    }

    public HashMap(int initialCapacity) {
        this.capacity = initialCapacity;
        buckets = new LinkedList[initialCapacity];
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    public V put(K key, V value) {
        int index = getBucketIndex(key);

        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }

        for (Node<K,V> node : buckets[index]) {
            if (Objects.equals(key, node.key)) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }
        buckets[index].add(new Node<>(key,value));
        return null;
    }

    public V get(K key) {
        int index = getBucketIndex(key);

        LinkedList<Node<K, V>> bucket = buckets[index];
        if (bucket != null) {
            for (Node<K, V> node : bucket) {
                if (Objects.equals(key, node.key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    public V remove(K key) {
        int index = getBucketIndex(key);
        LinkedList<Node<K, V>> bucket = buckets[index];
        if (bucket != null) {
            for (Node<K, V> node : bucket) {
                if (Objects.equals(key, node.key)) {
                    V oldValue = node.value;
                    bucket.remove(node);
                    return oldValue;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        for (LinkedList<Node<K,V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    list.add(node.key + "=" + node.value);
                }
            }
        }
        return list.toString();
    }

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("one",1);
        map.put("three",3 );
        map.put("six",6);

        System.out.println(map.get("three"));
        map.put("six", 7);
        System.out.println(map.get("six"));

        map.remove("one");
        System.out.println(map.get("one"));

        System.out.println(map);
    }
}

