package dev.haguel.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IntNode implements Node<Integer, Integer> {
    private Integer key;
    private Integer value;

    @Override
    public int compareTo(Node<Integer, Integer> o) {
        return Integer.compare(key, o.getKey());
    }
}
