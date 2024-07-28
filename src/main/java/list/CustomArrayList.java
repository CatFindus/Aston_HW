package list;

import java.nio.file.ClosedWatchServiceException;
import java.util.*;

public class CustomArrayList<T> extends AbstractList<T> implements List<T> {
    private int size;
    int arrayCapacity;
    public Object[] array;
    private ArrayList<T> aa;

    public CustomArrayList() {
        size=0;
        arrayCapacity=16;
        array=new Object[arrayCapacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        if (index>=size) throw new IndexOutOfBoundsException();
        return (T) array[index];
    }

    @Override
    public void clear() {
        size=0;
    }

    @Override
    public boolean add(T t) {
        checkCapacity(1);
        array[size]=t;
        size++;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index<0||index>=size) throw new IndexOutOfBoundsException();
        checkCapacity(c.size());
        Object[] endArray = Arrays.copyOfRange(array,index, size-1);
        int i = index;
        for (T t : c) {
            array[i]=t;
            i++;
        }
        for (Object o : endArray) array[++i] = o;
        size=size+c.size();
        return true;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public T remove(int index) {
        if (index<0||index>=size) throw new IndexOutOfBoundsException();
        T result = (T) array[index];
        for (int i = index; i < (size-1); i++) array[i]=array[i+1];
        size--;
        return result;
    }

    @Override
    public int indexOf(Object o) {
        if (o!=null) {
            for (int i = 0; i < size; i++) {
                if (array[i].equals(o)) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (array[i]==null) return i;
            }
        }
        return -1;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index==-1) return false;
        remove(index);
        return true;
    }

    @Override
    public void sort(Comparator<? super T> c) {
        mergeSort(c, array, size);
//        quickSort(c, array, 0, size-1);
    }

    private void mergeSort(Comparator<? super T> c, Object[] inputArray, int size) {
        int center = size/2;
        Object[] left = Arrays.copyOfRange(inputArray, 0, center);
        Object[] right = Arrays.copyOfRange(inputArray, center, size);
        if (left.length>1) {
            mergeSort(c, left, center);
        }
        if (right.length>1) {
            mergeSort(c, right, size-center);
        }
        int leftIndex = 0;
        int rightIndex = 0;
        for (int elementNumber=0; elementNumber<size; elementNumber++) {
                int compare;
                if (leftIndex==left.length) compare=1;
                else if (rightIndex==right.length) compare=-1;
                else compare = c.compare((T) left[leftIndex], (T) right[rightIndex]);
                if (compare<=0) {
                    inputArray[elementNumber]=left[leftIndex];
                    leftIndex++;
                } else {
                    inputArray[elementNumber]=right[rightIndex];
                    rightIndex++;
                }
        }
    }

    private void quickSort(Comparator<? super T> c, Object[] inputArray, int min, int max) {
        if (min<max) {
            int partiton = getPartition(c,inputArray, min, max);
            quickSort(c, inputArray, min, partiton-1);
            quickSort(c, inputArray, partiton+1, max);
        }
    }

    private int getPartition(Comparator<? super T> c, Object[] inputArray, int min, int max) {
        int center = min + (max-min)/2;
        Object pivot = inputArray[center];
        Object temp = inputArray[center];

        inputArray[center]=inputArray[max];
        inputArray[max]=temp;
        int pivotIndex = max;
        for (int i = min; i < pivotIndex; i++) {
            if (c.compare((T) inputArray[i], (T) pivot)>=0) {
                temp = inputArray[pivotIndex-1];
                inputArray[pivotIndex-1]=inputArray[i];
                inputArray[i]=temp;
                inputArray[pivotIndex]=inputArray[pivotIndex-1];
                inputArray[pivotIndex-1]=pivot;
                pivotIndex--;
                i--;
            }
        }
        return pivotIndex;
    }

    private void checkCapacity(int insertSize) {
        if ((size+insertSize)>arrayCapacity) {
            arrayCapacity = Math.max((arrayCapacity << 1), (size + insertSize));
            array = Arrays.copyOf(array, arrayCapacity);
        }
    }

}
