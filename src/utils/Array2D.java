package utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class Array2D<E> implements Iterable<E> {

    private final Dimension dimension;
    private final Object[][] elementData;


    /**
     * Private constructor used only by ModifiableView and UnmodifiableView.
     */
    private Array2D() {
        dimension = null;
        elementData = null;
    }


    /**
     * Constructs this Array2D as a shallow copy of the given Array2D (both contain the same elements
     * upon copying, but changes to one Array2D will not affect the other).
     */
    public Array2D(Array2D<E> toCopy) {
        this(toCopy.getDimension());
        for (int y = 0; y < dimension.getHeight(); y++) {
            for (int x = 0; x < dimension.getWidth(); x++) {
                put(toCopy.get(x, y), x, y);
            }
        }
    }


    /**
     * Constructs this Array2D with all values set to null.
     */
    public Array2D(Dimension dimension) {
        this.dimension = dimension;
        elementData = new Object[dimension.getHeight()][dimension.getWidth()];
    }


    /**
     * Constructs this Array2D with all values set to {@code defaultValue}.
     */
    public Array2D(Dimension dimension, E defaultValue) {
        this.dimension = dimension;
        elementData = new Object[dimension.getHeight()][dimension.getWidth()];
        setAll(defaultValue);
    }


    /**
     * Retrieves the element at the given index.
     */
    @SuppressWarnings("unchecked")
    public E get(int x, int y) {
        return (E) elementData[y][x];
    }


    /**
     * Assigns an element to the given index. Returns the element that was already at the given index,
     * or null if there was none.
     */
    public E put(E object, int x, int y) {
        final E replaced = get(x, y);
        elementData[y][x] = object;
        return replaced;
    }


    /**
     * Sets every object in the Array2D to the given element.
     */
    public Array2D setAll(E object) {
        for (Object[] subArray : elementData) {
            Arrays.setAll(subArray, i -> object);
        }
        return this;
    }


    /**
     * Returns the size of this Array2D.
     */
    public Dimension getDimension() {
        return dimension;
    }


    /**
     * Returns a view of the specified portion of this Array2D. The view is backed by this one, so
     * changes in either will affect the other, although the new view can only be modified if this
     * Array2D is modifiable.
     *
     * @param viewport The size of the new Array2D.
     * @param offX     Where the left edge of the viewport should start on the backing array.
     * @param offY     Where the top edge of the viewport should start on the backing array.
     */
    @SuppressWarnings("unchecked")
    public Array2D<E> view(Dimension viewport, int offX, int offY) {
        rangeCheckView(viewport, offX, offY);
        return new ModifiableView(viewport, offX, offY);
    }


    /**
     * Returns a view of the specified portion of this Array2D. The view is backed by this one, so
     * changes to the original will affect the new, but attempts to change the new will throw {@code
     * UnsupportedOperationException}.
     *
     * @param viewport The size of the new Array2D.
     * @param offX     Where the left edge of the viewport should start on the backing array.
     * @param offY     Where the top edge of the viewport should start on the backing array.
     */
    @SuppressWarnings("unchecked")
    public Array2D<E> unmodifiableView(Dimension viewport, int offX, int offY) {
        rangeCheckView(viewport, offX, offY);
        return new UnmodifiableView(viewport, offX, offY);
    }


    /**
     * Ensures no view is created that would extend past the boundaries of its parent Array2D.
     */
    private void rangeCheckView(Dimension viewport, int offX, int offY) {
        final int limitX = viewport.getWidth() - 1 + offX;
        final int limitY = viewport.getHeight() - 1 + offY;
        if (!getDimension().getCoordinateIsWithinBounds(limitX, limitY)) {
            throw new IndexOutOfBoundsException("View would extend past backing array boundaries.");
        }
    }


    /**
     * Returns a set containing all of the elements stored in this Array2D.
     */
    @SuppressWarnings("unchecked")
    public Set<E> toSet() {
        final HashSet<E> set = new HashSet<>();
        for (final Object[] subArray : elementData) {
            for (final Object element : subArray) {
                set.add((E) element);
            }
        }
        return set;
    }


    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }


    @Override
    public void forEach(Consumer<? super E> action) {
        for (int y = 0; y < getDimension().getHeight(); y++) {
            for (int x = 0; x < getDimension().getWidth(); x++) {
                action.accept(get(x, y));
            }
        }
    }


    /**
     * Returns a new Array2D that is the result of applying the given function to every element in
     * this one.
     *
     * @param mapper A function that takes in the type of this Array2D and returns type {@code <R>}.
     * @param <R>    The type returned by the mapper.
     */
    public <R> Array2D<R> map(Function<E, R> mapper) {
        Objects.requireNonNull(mapper);
        final Array2D<R> mapped = new Array2D<>(getDimension());
        for (int y = 0; y < getDimension().getHeight(); y++) {
            for (int x = 0; x < getDimension().getWidth(); x++) {
                mapped.put(mapper.apply(get(x, y)), x, y);
            }
        }
        return mapped;
    }


    private class ModifiableView extends Array2D<E> {

        final Dimension viewport;
        final int offX;
        final int offY;


        public ModifiableView(Dimension viewport, int offX, int offY) {
            this.viewport = viewport;
            this.offX = offX;
            this.offY = offY;
        }


        /**
         * Ensures that operations within this view cannot reach backing array elements that are outside
         * its boundary.
         */
        private void rangeCheck(int x, int y) {
            if (!viewport.getCoordinateIsWithinBounds(x, y)) {
                throw new IndexOutOfBoundsException();
            }
        }


        @Override
        public E get(int x, int y) {
            rangeCheck(x, y);
            return Array2D.this.get(x + offX, y + offY);
        }


        @Override
        public E put(E object, int x, int y) {
            rangeCheck(x, y);
            return Array2D.this.put(object, x + offX, y + offY);
        }


        @Override
        public Array2D<E> setAll(E object) {
            for (int y = 0; y < viewport.getHeight(); y++) {
                for (int x = 0; x < viewport.getWidth(); x++) {
                    put(object, x, y);
                }
            }
            return this;
        }


        @Override
        public Dimension getDimension() {
            return viewport;
        }


        @Override
        public Set<E> toSet() {
            final HashSet<E> set = new HashSet<>();
            for (int y = 0; y < viewport.getHeight(); y++) {
                for (int x = 0; x < viewport.getWidth(); x++) {
                    set.add(get(x, y));
                }
            }
            return set;
        }
    }

    private class UnmodifiableView extends ModifiableView {

        public UnmodifiableView(Dimension viewport, int offX, int offY) {
            super(viewport, offX, offY);
        }


        @Override
        public E put(E object, int x, int y) {
            throw new UnsupportedOperationException("This Array2D is unmodifiable.");
        }


        @Override
        public Array2D<E> setAll(E object) {
            throw new UnsupportedOperationException("This Array2D is unmodifiable.");
        }


        @Override
        public Array2D<E> view(Dimension viewport, int offX, int offY) {
            return unmodifiableView(viewport, offX, offY);
        }
    }

    private class Itr implements Iterator<E> {

        private int x = 0;
        private int y = 0;


        @Override
        public boolean hasNext() {
            return (x < getDimension().getWidth() || y < getDimension().getHeight());
        }


        @Override
        public E next() {
            x++;
            if (x >= getDimension().getWidth()) {
                x = 0;
                y++;
            }
            return get(x, y);
        }
    }
}