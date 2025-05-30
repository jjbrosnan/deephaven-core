//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.client.impl;

import io.deephaven.qst.array.BooleanArray;
import org.apache.arrow.vector.BigIntVector;
import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.Float4Vector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.SmallIntVector;
import org.apache.arrow.vector.TimeStampNanoTZVector;
import org.apache.arrow.vector.TinyIntVector;
import org.apache.arrow.vector.UInt2Vector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VarBinaryVector;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;

/**
 * Helpers to fill various {@link org.apache.arrow.vector.FieldVector field vectors} from arrays or collections.
 */
public class VectorHelper {

    public static void fill(TinyIntVector vector, byte[] array, int offset, int len) {
        vector.allocateNew(len);
        for (int i = 0; i < len; i++) {
            vector.set(i, array[offset + i]);
        }
        vector.setValueCount(len);
    }

    public static void fill(TinyIntVector vector, Collection<Byte> values) {
        vector.allocateNew(values.size());
        int i = 0;
        for (Byte value : values) {
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.set(i, value);
            }
            ++i;
        }
        vector.setValueCount(values.size());
    }

    public static void fill(BitVector vector, BooleanArray array, int offset, int len) {
        vector.allocateNew(len);
        for (int i = 0; i < len; i++) {
            Boolean value = array.value(offset + i);
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.set(i, value ? 1 : 0);
            }
        }
        vector.setValueCount(len);
    }

    public static void fill(BitVector vector, Collection<Boolean> values) {
        vector.allocateNew(values.size());
        int i = 0;
        for (Boolean value : values) {
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.set(i, value ? 1 : 0);
            }
            ++i;
        }
        vector.setValueCount(values.size());
    }

    public static void fill(UInt2Vector vector, char[] array, int offset, int len) {
        vector.allocateNew(len);
        for (int i = 0; i < len; i++) {
            vector.set(i, array[offset + i]);
        }
        vector.setValueCount(len);
    }

    public static void fill(UInt2Vector vector, Collection<Character> values) {
        vector.allocateNew(values.size());
        int i = 0;
        for (Character value : values) {
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.set(i, value);
            }
            ++i;
        }
        vector.setValueCount(values.size());
    }

    public static void fill(SmallIntVector vector, short[] array, int offset, int len) {
        vector.allocateNew(len);
        for (int i = 0; i < len; i++) {
            vector.set(i, array[offset + i]);
        }
        vector.setValueCount(len);
    }

    public static void fill(SmallIntVector vector, Collection<Short> values) {
        vector.allocateNew(values.size());
        int i = 0;
        for (Short value : values) {
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.set(i, value);
            }
            ++i;
        }
        vector.setValueCount(values.size());
    }

    public static void fill(IntVector vector, int[] array, int offset, int len) {
        vector.allocateNew(len);
        for (int i = 0; i < len; i++) {
            vector.set(i, array[offset + i]);
        }
        vector.setValueCount(len);
    }

    public static void fill(IntVector vector, Collection<Integer> values) {
        vector.allocateNew(values.size());
        int i = 0;
        for (Integer value : values) {
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.set(i, value);
            }
            ++i;
        }
        vector.setValueCount(values.size());
    }

    public static void fill(BigIntVector vector, long[] array, int offset, int len) {
        vector.allocateNew(len);
        for (int i = 0; i < len; i++) {
            vector.set(i, array[offset + i]);
        }
        vector.setValueCount(len);
    }

    public static void fill(BigIntVector vector, Collection<Long> values) {
        vector.allocateNew(values.size());
        int i = 0;
        for (Long value : values) {
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.set(i, value);
            }
            ++i;
        }
        vector.setValueCount(values.size());
    }

    public static void fill(Float4Vector vector, float[] array, int offset, int len) {
        vector.allocateNew(len);
        for (int i = 0; i < len; i++) {
            vector.set(i, array[offset + i]);
        }
        vector.setValueCount(len);
    }

    public static void fill(Float4Vector vector, Collection<Float> values) {
        vector.allocateNew(values.size());
        int i = 0;
        for (Float value : values) {
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.set(i, value);
            }
            ++i;
        }
        vector.setValueCount(values.size());
    }

    public static void fill(Float8Vector vector, double[] array, int offset, int len) {
        vector.allocateNew(len);
        for (int i = 0; i < len; i++) {
            vector.set(i, array[offset + i]);
        }
        vector.setValueCount(len);
    }

    public static void fill(Float8Vector vector, Collection<Double> values) {
        vector.allocateNew(values.size());
        int i = 0;
        for (Double value : values) {
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.set(i, value);
            }
            ++i;
        }
        vector.setValueCount(values.size());
    }

    public static void fill(VarCharVector vector, Collection<String> array) {
        vector.allocateNew(array.size());
        int i = 0;
        for (String value : array) {
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.setSafe(i, value.getBytes(StandardCharsets.UTF_8));
            }
            ++i;
        }
        vector.setValueCount(array.size());
    }

    public static void fill(VarBinaryVector vector, Collection<byte[]> array) {
        vector.allocateNew(array.size());
        int i = 0;
        for (byte[] value : array) {
            if (value == null) {
                vector.setNull(i);
            } else {
                vector.setSafe(i, value);
            }
            ++i;
        }
        vector.setValueCount(array.size());
    }

    public static void fill(TimeStampNanoTZVector vector, Collection<Instant> array) {
        vector.allocateNew(array.size());
        int i = 0;
        for (Instant value : array) {
            if (value == null) {
                vector.set(i, Long.MIN_VALUE);
            } else {
                final long epochSecond = value.getEpochSecond();
                final int nano = value.getNano();
                final long epochNano = Math.addExact(Math.multiplyExact(epochSecond, 1_000_000_000L), nano);
                vector.set(i, epochNano);
            }
            ++i;
        }
        vector.setValueCount(array.size());
    }
}
