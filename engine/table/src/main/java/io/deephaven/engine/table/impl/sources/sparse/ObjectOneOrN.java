//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Edit CharOneOrN and run "./gradlew replicateSourcesAndChunks" to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.sources.sparse;

import io.deephaven.util.SoftRecycler;

import java.util.function.LongConsumer;

import static io.deephaven.engine.table.impl.sources.sparse.SparseConstants.*;

public final class ObjectOneOrN {
    private static final int ONE_UNINITIALIZED = -1;
    private static final int HAVE_MORE_THAN_ONE = -2;

    private ObjectOneOrN() {
        throw new IllegalStateException("ObjectOneOrN class should never be constructed.");
    }

    // region Block0
    static public final class Block0<T> {
        int oneIndex = ONE_UNINITIALIZED;
        Block1<T> oneValue;
        Block1<T> [] array;

        public void ensureIndex(int idx, SoftRecycler<Block1<T>[]> recycler) {
            if (oneIndex == idx || oneIndex == HAVE_MORE_THAN_ONE) {
                return;
            }
            if (oneIndex == ONE_UNINITIALIZED) {
                oneIndex = idx;
            } else {
                if (recycler == null) {
                    array = new Block1[BLOCK0_SIZE];
                } else {
                    array = recycler.borrowItem();
                }
                array[oneIndex] = oneValue;
                oneIndex = HAVE_MORE_THAN_ONE;
                oneValue = null;
            }
        }

        public T [] getInnermostBlockByKeyOrNull(long key) {
            final int block0 = (int) (key >> BLOCK0_SHIFT) & BLOCK0_MASK;
            final int block1 = (int) (key >> BLOCK1_SHIFT) & BLOCK1_MASK;
            final int block2 = (int) (key >> BLOCK2_SHIFT) & BLOCK2_MASK;

            final Block1<T> blocks0 = get(block0);
            if (blocks0 == null) {
                return null;
            }

            final Block2<T> blocks1 = blocks0.get(block1);
            if (blocks1 == null) {
                return null;
            }
            return blocks1.get(block2);
        }

        public Block1<T> get(int idx) {
            if (oneIndex == idx) {
                return oneValue;
            }
            if (array == null) {
                return null;
            }
            return array[idx];
        }

        public void set(int idx, Block1<T> value) {
            if (oneIndex == idx) {
                oneValue = value;
            } else {
                array[idx] = value;
            }
        }

        public void maybeRecycle(SoftRecycler<Block1<T> []> recycler) {
            if (array != null) {
                recycler.returnItem(array);
            }
        }

        public void enumerate(Object nullValue, LongConsumer consumer) {
            if (oneIndex == ONE_UNINITIALIZED) {
                return;
            }
            if (oneIndex != HAVE_MORE_THAN_ONE) {
                if (oneValue != null) {
                    oneValue.enumerate(nullValue, consumer, (long) oneIndex << BLOCK0_SHIFT);
                }
                return;
            }
            for (int ii = 0; ii < BLOCK0_SIZE; ++ii) {
                if (array[ii] != null) {
                    array[ii].enumerate(nullValue, consumer, (long)ii << BLOCK0_SHIFT);
                }
            }
        }
    }
    // endregion Block0

    // region Block1
    static public final class Block1<T> {
        int oneIndex = ONE_UNINITIALIZED;
        Block2<T> oneValue;
        Block2<T> [] array;

        public void ensureIndex(int idx, SoftRecycler<Block2<T>[]> recycler) {
            if (oneIndex == idx || oneIndex == HAVE_MORE_THAN_ONE) {
                return;
            }
            if (oneIndex == ONE_UNINITIALIZED) {
                oneIndex = idx;
            } else {
                if (recycler == null) {
                    array = new Block2[BLOCK1_SIZE];
                } else {
                    array = recycler.borrowItem();
                }
                array[oneIndex] = oneValue;
                oneIndex = HAVE_MORE_THAN_ONE;
                oneValue = null;
            }
        }

        public Block2<T> get(int idx) {
            if (oneIndex == idx) {
                return oneValue;
            }
            if (array == null) {
                return null;
            }
            return array[idx];
        }

        public void set(int idx, Block2<T> value) {
            if (oneIndex == idx) {
                oneValue = value;
            } else {
                array[idx] = value;
            }
        }

        public void maybeRecycle(SoftRecycler<Block2<T>[]> recycler) {
            if (array != null) {
                recycler.returnItem(array);
            }
        }

        void enumerate(Object nullValue, LongConsumer consumer, long offset) {
            if (oneIndex == ONE_UNINITIALIZED) {
                return;
            }
            if (oneIndex != HAVE_MORE_THAN_ONE) {
                if (oneValue != null) {
                    oneValue.enumerate(nullValue, consumer, offset + ((long)oneIndex << BLOCK1_SHIFT));
                }
                return;
            }
            for (int ii = 0; ii < BLOCK1_SIZE; ++ii) {
                if (array[ii] != null) {
                    array[ii].enumerate(nullValue, consumer, offset + ((long)ii << BLOCK1_SHIFT));
                }
            }
        }
    }
    // endregion Block1

    // region Block2
    static public final class Block2<T> {
        int oneIndex = ONE_UNINITIALIZED;
        T [] oneValue;
        T [][] array;

        public void ensureIndex(int idx, SoftRecycler<T [][]> recycler) {
            if (oneIndex == idx || oneIndex == HAVE_MORE_THAN_ONE) {
                return;
            }
            if (oneIndex == ONE_UNINITIALIZED) {
                oneIndex = idx;
            } else {
                if (recycler == null) {
                    array = (T[][])new Object[BLOCK2_SIZE][];
                } else {
                    array = recycler.borrowItem();
                }
                array[oneIndex] = oneValue;
                oneIndex = HAVE_MORE_THAN_ONE;
                oneValue = null;
            }
        }

        public T [] get(int idx) {
            if (oneIndex == idx) {
                return oneValue;
            }
            if (array == null) {
                return null;
            }
            return array[idx];
        }

        public void set(int idx, T [] value) {
            if (oneIndex == idx) {
                oneValue = value;
            } else {
                array[idx] = value;
            }
        }

        public void maybeRecycle(SoftRecycler<T [][]> recycler) {
            if (array != null) {
                recycler.returnItem(array);
            }
        }

        void enumerate(Object nullValue, LongConsumer consumer, long offset) {
            if (oneIndex == ONE_UNINITIALIZED) {
                return;
            }
            if (oneIndex != HAVE_MORE_THAN_ONE) {
                if (oneValue != null) {
                    enumerateInner(nullValue, oneValue, consumer, offset + ((long)oneIndex << BLOCK2_SHIFT));
                }
                return;
            }
            for (int ii = 0; ii < BLOCK2_SIZE; ++ii) {
                if (array[ii] != null) {
                    enumerateInner(nullValue, array[ii], consumer, offset + ((long)ii << BLOCK2_SHIFT));
                }
            }
        }

        private static void enumerateInner(Object nullValue, Object[] inner, LongConsumer consumer, long offset) {
            for (int ii = 0; ii < inner.length; ++ii) {
                if (inner[ii] != nullValue) {
                    consumer.accept(offset + ii);
                }
            }
        }
    }
    // endregion Block2
}
