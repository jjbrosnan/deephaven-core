//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.chunk.util.hashing;

import io.deephaven.chunk.attributes.Any;
import io.deephaven.chunk.CharChunk;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.LongChunk;
import io.deephaven.chunk.WritableLongChunk;

/**
 * Cast the values in the input chunk to an long and add the specified offset.
 *
 * @param <T> the chunk's attribute
 */
public class CharToLongCastWithOffset<T extends Any> implements ToLongFunctor<T> {
    private final WritableLongChunk<T> result;
    private final long offset;

    CharToLongCastWithOffset(int size, long offset) {
        result = WritableLongChunk.makeWritableChunk(size);
        this.offset = offset;
    }

    @Override
    public LongChunk<T> apply(Chunk<T> input) {
        return castWithOffset(input.asCharChunk());
    }

    private LongChunk<T> castWithOffset(CharChunk<T> input) {
        castInto(input, result, offset);
        return result;
    }

    public static <T2 extends Any> void castInto(CharChunk<T2> input, WritableLongChunk<T2> result, long offset) {
        for (int ii = 0; ii < input.size(); ++ii) {
            result.set(ii, (long) input.get(ii) + offset);
        }
        result.setSize(input.size());
    }

    @Override
    public void close() {
        result.close();
    }
}
