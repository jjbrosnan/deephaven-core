//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.engine.table.impl.by;

import io.deephaven.engine.table.Table;
import io.deephaven.engine.table.impl.MatchPair;
import io.deephaven.util.compare.CharComparisons;
import io.deephaven.engine.table.impl.sources.CharacterArraySource;
import io.deephaven.chunk.attributes.ChunkLengths;
import io.deephaven.chunk.attributes.ChunkPositions;
import io.deephaven.engine.rowset.chunkattributes.RowKeys;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.chunk.*;
import org.jetbrains.annotations.NotNull;

import static io.deephaven.util.QueryConstants.NULL_LONG;

/**
 * Chunked aggregation operator for sorted first/last-by using a char sort-column on add-only tables.
 */
public class CharAddOnlySortedFirstOrLastChunkedOperator extends BaseAddOnlyFirstOrLastChunkedOperator {

    private final CharacterArraySource sortColumnValues;

    CharAddOnlySortedFirstOrLastChunkedOperator(
            final boolean isFirst,
            @NotNull final MatchPair[] resultPairs,
            @NotNull final Table originalTable,
            final String exposeRedirectionAs) {
        super(isFirst, resultPairs, originalTable, exposeRedirectionAs);
        // region sortColumnValues initialization
        sortColumnValues = new CharacterArraySource();
        // endregion sortColumnValues initialization
    }

    @Override
    public void ensureCapacity(final long tableSize) {
        super.ensureCapacity(tableSize);
        sortColumnValues.ensureCapacity(tableSize, false);
    }

    @Override
    public void addChunk(final BucketedContext bucketedContext, // Unused
            @NotNull final Chunk<? extends Values> values,
            @NotNull final LongChunk<? extends RowKeys> inputRowKeys,
            @NotNull final IntChunk<RowKeys> destinations,
            @NotNull final IntChunk<ChunkPositions> startPositions,
            @NotNull final IntChunk<ChunkLengths> length,
            @NotNull final WritableBooleanChunk<Values> stateModified) {
        final CharChunk<? extends Values> typedValues = values.asCharChunk();
        for (int ii = 0; ii < startPositions.size(); ++ii) {
            final int startPosition = startPositions.get(ii);
            final int runLength = length.get(ii);
            final long destination = destinations.get(startPosition);
            stateModified.set(ii, addChunk(typedValues, inputRowKeys, startPosition, runLength, destination));
        }
    }

    @Override
    public boolean addChunk(final SingletonContext singletonContext, // Unused
            final int chunkSize,
            @NotNull final Chunk<? extends Values> values,
            @NotNull final LongChunk<? extends RowKeys> inputRowKeys,
            final long destination) {
        return addChunk(values.asCharChunk(), inputRowKeys, 0, inputRowKeys.size(), destination);
    }

    private boolean addChunk(@NotNull final CharChunk<? extends Values> values,
            @NotNull final LongChunk<? extends RowKeys> indices,
            final int start,
            final int length,
            final long destination) {
        if (length == 0) {
            return false;
        }
        final long initialIndex = redirections.getUnsafe(destination);
        final boolean newDestination = initialIndex == NULL_LONG;

        long bestIndex;
        char bestValue;
        if (newDestination) {
            bestIndex = indices.get(start);
            bestValue = values.get(start);
        } else {
            bestIndex = initialIndex;
            bestValue = sortColumnValues.getUnsafe(destination);
        }
        for (int ii = newDestination ? 1 : 0; ii < length; ++ii) {
            final long index = indices.get(start + ii);
            final char value = values.get(start + ii);
            final boolean better;
            if (isFirst) {
                better = index < bestIndex
                        ? CharComparisons.leq(value, bestValue)
                        : CharComparisons.lt(value, bestValue);
            } else {
                better = index > bestIndex
                        ? CharComparisons.geq(value, bestValue)
                        : CharComparisons.gt(value, bestValue);
            }
            if (better) {
                bestIndex = index;
                bestValue = value;
            }
        }
        if (bestIndex == initialIndex) {
            return false;
        }
        redirections.set(destination, bestIndex);
        sortColumnValues.set(destination, bestValue);
        return true;
    }
}
