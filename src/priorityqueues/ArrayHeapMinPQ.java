package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T extends Comparable<T>> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    Map<T, Integer> map;
    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        map = new HashMap<>();
    }
    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }

    private int getParent(int curIndex) {
        return (curIndex - 1) / 2;
    }

    private int getLeftChild(int curIndex) {
        return 2 * curIndex + 1;
    }

    private int getRightChild(int curIndex) {
        return 2 * curIndex + 2;
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < items.size();
    }

    private boolean shouldSwapWithChild(PriorityNode<T> cur, PriorityNode<T> child) {
        if (child == null) {
            return false;
        }
        return cur.getPriority() > child.getPriority();
    }

    public void percolateDown(PriorityNode<T> cur, int curIndex) {
        if (cur != null) {
            int lci = getLeftChild(curIndex);
            int rci = getRightChild(curIndex);
            PriorityNode<T> leftChild = isValidIndex(lci) ? items.get(lci) : null;
            PriorityNode<T> rightChild = isValidIndex(rci) ? items.get(rci) : null;
            if (leftChild != null && rightChild != null) {
                if (leftChild.getPriority() < rightChild.getPriority()
                    && (shouldSwapWithChild(cur, leftChild))) {
                    swap(curIndex, lci);
                    percolateDown(cur, lci);
                } else if (shouldSwapWithChild(cur, rightChild)) {
                    swap(curIndex, rci);
                    percolateDown(cur, rci);
                }
            } else if (leftChild != null) {
                if (shouldSwapWithChild(cur, leftChild)) {
                    swap(curIndex, lci);
                    percolateDown(cur, lci);
                }
            } else if (rightChild != null) {
                if (shouldSwapWithChild(cur, rightChild)) {
                    swap(curIndex, rci);
                    percolateDown(cur, rci);
                }
            }
        }
    }

    @Override
    public void add(T item, double priority) {
        if (item == null || this.contains(item)) {
            throw new IllegalArgumentException();
        }
        PriorityNode<T> currNode = new PriorityNode<>(item, priority);
        this.items.add(currNode);
        map.put(item, percolateUp(currNode, items.size() - 1));
    }

    public int percolateUp(PriorityNode<T> currNode, int currIndex) {
        if (currNode != null) {
            int parentIndex = getParent(currIndex);
            PriorityNode<T> parentNode = items.get(parentIndex);
            if (parentNode != null && parentNode.getPriority() > currNode.getPriority()) {
                swap(parentIndex, currIndex);
                return percolateUp(currNode, parentIndex);
            }
        }
        return currIndex;
    }

    @Override
    public boolean contains(T item) {
        for (PriorityNode<T> i : items) {
            if (i.getItem().equals(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T peekMin() {
        if (size() > 0) {
            return items.get(0).getItem();
        } else {
            return null;
        }
    }

    @Override
    public T removeMin() {
        if (items.size() <= 0) {
            throw new NoSuchElementException("PQ is empty.");
        } else if (items.size() == 1) {
            return items.remove(0).getItem();
        } else {
            PriorityNode<T> result = items.get(0);
            items.set(0, items.remove(items.size() - 1));
            percolateDown(items.get(0), 0);
            return result.getItem();
        }
    }

    @Override
    public void changePriority(T item, double priority) {
        int index = findIndex(item);
        if (isValidIndex(index)) {
            int lastChild = getLastChild(index);
            swap(index, lastChild);
            items.remove(lastChild);
            if (isValidIndex(index)) {
                percolateDown(items.get(index), index);
            }
            add(item, priority);
        }
    }

    private int getLastChild(int curIndex) {
        int left = getLeftChild(curIndex);
        int right = getRightChild(curIndex);
        if (isValidIndex(right)) {
            return getLastChild(right);
        } else if (isValidIndex(left)) {
            return getLastChild(left);
        } else {
            return curIndex;
        }
    }

    private int findIndex(T item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItem().equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return items.size();
    }
}
