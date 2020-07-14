package ru.otus;

import java.util.*;

public class DiyArrayList<T> implements List<T> {
    private static final String unsupportedOperationMessage = "This operation is not supported.";
    private Object[] elements;
    private int index = 0;

    public DiyArrayList() {
        this(10);
    }

    public DiyArrayList(int capacity) {
        elements = new Object[capacity];
    }

    @Override
    public int size() {
        return index;
    }


    @Override
    public T get(int index) {
        checkIndexExist(index);
        return (T) elements[index];

    }

    @Override
    public T set(int index, T element) {
        if (index == this.index) {
            add(element);
            return null;
        } else {
            checkIndexExist(index);
            T oldElement = get(index);
            setElement(index, element);
            return oldElement;
        }
    }


    private void checkIndexExist(int index) {
        if (index >= this.index) {
            throw new IndexOutOfBoundsException(String.format("Index %d is unreachable", index));
        }
    }

    @Override
    public boolean add(T element) {
        if (index == elements.length) {
            reInitArrayWithNewLength();
        }
        elements[index] = element;
        index++;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::add);
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < size(); i++) {
            s.append(elements[i].toString()).append(" ");
        }
        return s.toString();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size());

    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIterator<T>() {

            int cursorPosition = -1;

            @Override
            public T next() {
                cursorPosition++;
                return (T) elements[cursorPosition];
            }

            @Override
            public T previous() {
                if ((cursorPosition - 1) < 0) {
                    cursorPosition = 0;
                }
                return (T) elements[cursorPosition];
            }

            @Override
            public boolean hasNext() {
                return cursorPosition + 1 != index;
            }

            @Override
            public boolean hasPrevious() {
                return cursorPosition > 0;
            }

            @Override
            public int nextIndex() {
                return cursorPosition + 1;
            }

            @Override
            public int previousIndex() {
                return cursorPosition - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException(unsupportedOperationMessage);
            }

            @Override
            public void add(T t) {
                throw new UnsupportedOperationException(unsupportedOperationMessage);
            }

            @Override
            public void set(T t) {
                DiyArrayList.this.set(cursorPosition, t);
            }
        };
    }

    private void reInitArrayWithNewLength() {
        int newLength = elements.length * 2;
        elements = Arrays.copyOf(elements, newLength);
    }

    private void setElement(int index, T element) {
        checkIndexExist(index);
        elements[index] = element;
    }

    @Override
    public T remove(int index) {
        checkIndexExist(index);
        T removedElement = get(index);
        //Копирование (this.index - index) - количество элементов, которые остались после index, начиная с позиции, следующей за той, которую
        //хотим убрать (index+1), в этот же самый массив(чтобы оставить всё то, что есть до index-элемента)
        System.arraycopy(elements, index + 1, elements, index, this.index - index);
        this.index--;
        return removedElement;
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException(unsupportedOperationMessage);
    }

}
