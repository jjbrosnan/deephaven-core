//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
// ****** AUTO-GENERATED CLASS - DO NOT EDIT MANUALLY
// ****** Run ReplicateTypedHashers or ./gradlew replicateTypedHashers to regenerate
//
// @formatter:off
package io.deephaven.engine.table.impl.naturaljoin.typed.staticopen.gen;

import static io.deephaven.util.compare.DoubleComparisons.eq;

import io.deephaven.api.NaturalJoinType;
import io.deephaven.base.verify.Assert;
import io.deephaven.chunk.Chunk;
import io.deephaven.chunk.DoubleChunk;
import io.deephaven.chunk.LongChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.chunk.util.hashing.DoubleChunkHasher;
import io.deephaven.engine.rowset.RowSequence;
import io.deephaven.engine.rowset.RowSet;
import io.deephaven.engine.rowset.chunkattributes.OrderedRowKeys;
import io.deephaven.engine.table.ColumnSource;
import io.deephaven.engine.table.impl.naturaljoin.DuplicateRightRowDecorationException;
import io.deephaven.engine.table.impl.naturaljoin.StaticNaturalJoinStateManagerTypedBase;
import io.deephaven.engine.table.impl.sources.IntegerArraySource;
import io.deephaven.engine.table.impl.sources.LongArraySource;
import io.deephaven.engine.table.impl.sources.immutable.ImmutableDoubleArraySource;

final class StaticNaturalJoinHasherDouble extends StaticNaturalJoinStateManagerTypedBase {
    private final ImmutableDoubleArraySource mainKeySource0;

    public StaticNaturalJoinHasherDouble(ColumnSource[] tableKeySources,
            ColumnSource[] originalTableKeySources, int tableSize, double maximumLoadFactor,
            double targetLoadFactor, NaturalJoinType joinType, boolean addOnly) {
        super(tableKeySources, originalTableKeySources, tableSize, maximumLoadFactor, joinType, addOnly);
        this.mainKeySource0 = (ImmutableDoubleArraySource) super.mainKeySources[0];
        this.mainKeySource0.ensureCapacity(tableSize);
    }

    private int nextTableLocation(int tableLocation) {
        return (tableLocation + 1) & (tableSize - 1);
    }

    protected void buildFromLeftSide(RowSequence rowSequence, Chunk[] sourceKeyChunks,
            IntegerArraySource leftHashSlots, long hashSlotOffset) {
        final DoubleChunk<Values> keyChunk0 = sourceKeyChunks[0].asDoubleChunk();
        final int chunkSize = keyChunk0.size();
        for (int chunkPosition = 0; chunkPosition < chunkSize; ++chunkPosition) {
            final double k0 = keyChunk0.get(chunkPosition);
            final int hash = hash(k0);
            final int firstTableLocation = hashToTableLocation(hash);
            int tableLocation = firstTableLocation;
            while (true) {
                long rightSideSentinel = mainRightRowKey.getUnsafe(tableLocation);
                if (isStateEmpty(rightSideSentinel)) {
                    numEntries++;
                    mainKeySource0.set(tableLocation, k0);
                    mainRightRowKey.set(tableLocation, NO_RIGHT_STATE_VALUE);
                    leftHashSlots.set(hashSlotOffset++, tableLocation);
                    break;
                } else if (eq(mainKeySource0.getUnsafe(tableLocation), k0)) {
                    leftHashSlots.set(hashSlotOffset++, tableLocation);
                    break;
                } else {
                    tableLocation = nextTableLocation(tableLocation);
                    Assert.neq(tableLocation, "tableLocation", firstTableLocation, "firstTableLocation");
                }
            }
        }
    }

    protected void buildFromRightSide(RowSequence rowSequence, Chunk[] sourceKeyChunks) {
        final DoubleChunk<Values> keyChunk0 = sourceKeyChunks[0].asDoubleChunk();
        final int chunkSize = keyChunk0.size();
        final LongChunk<OrderedRowKeys> rowKeyChunk = rowSequence.asRowKeyChunk();
        for (int chunkPosition = 0; chunkPosition < chunkSize; ++chunkPosition) {
            final double k0 = keyChunk0.get(chunkPosition);
            final int hash = hash(k0);
            final int firstTableLocation = hashToTableLocation(hash);
            int tableLocation = firstTableLocation;
            while (true) {
                long rightSideSentinel = mainRightRowKey.getUnsafe(tableLocation);
                if (isStateEmpty(rightSideSentinel)) {
                    numEntries++;
                    mainKeySource0.set(tableLocation, k0);
                    final long rightRowKeyToInsert = rowKeyChunk.get(chunkPosition);
                    mainRightRowKey.set(tableLocation, rightRowKeyToInsert);
                    break;
                } else if (eq(mainKeySource0.getUnsafe(tableLocation), k0)) {
                    if (joinType == NaturalJoinType.FIRST_MATCH) {
                        // no-op, we already have the first match;
                    } else if (joinType == NaturalJoinType.LAST_MATCH) {
                        // we are processing sequentially so this is the latest;
                        mainRightRowKey.set(tableLocation, rowKeyChunk.get(chunkPosition));
                    } else {
                        mainRightRowKey.set(tableLocation, DUPLICATE_RIGHT_STATE);
                    }
                    break;
                } else {
                    tableLocation = nextTableLocation(tableLocation);
                    Assert.neq(tableLocation, "tableLocation", firstTableLocation, "firstTableLocation");
                }
            }
        }
    }

    protected void decorateLeftSide(RowSequence rowSequence, Chunk[] sourceKeyChunks,
            LongArraySource leftRedirections, long redirectionOffset) {
        final DoubleChunk<Values> keyChunk0 = sourceKeyChunks[0].asDoubleChunk();
        final int chunkSize = keyChunk0.size();
        for (int chunkPosition = 0; chunkPosition < chunkSize; ++chunkPosition) {
            final double k0 = keyChunk0.get(chunkPosition);
            final int hash = hash(k0);
            final int firstTableLocation = hashToTableLocation(hash);
            boolean found = false;
            int tableLocation = firstTableLocation;
            long rightRowKey;
            while (!isStateEmpty(rightRowKey = mainRightRowKey.getUnsafe(tableLocation))) {
                if (eq(mainKeySource0.getUnsafe(tableLocation), k0)) {
                    if (rightRowKey == DUPLICATE_RIGHT_STATE) {
                        final LongChunk<OrderedRowKeys> rowKeyChunk = rowSequence.asRowKeyChunk();
                        throw new IllegalStateException("Natural Join found duplicate right key for " + extractKeyStringFromSourceTable(rowKeyChunk.get(chunkPosition)));
                    }
                    leftRedirections.set(redirectionOffset++, rightRowKey);
                    found = true;
                    break;
                }
                tableLocation = nextTableLocation(tableLocation);
                Assert.neq(tableLocation, "tableLocation", firstTableLocation, "firstTableLocation");
            }
            if (!found) {
                leftRedirections.set(redirectionOffset++, RowSet.NULL_ROW_KEY);
            }
        }
    }

    protected void decorateWithRightSide(RowSequence rowSequence, Chunk[] sourceKeyChunks) {
        final DoubleChunk<Values> keyChunk0 = sourceKeyChunks[0].asDoubleChunk();
        final LongChunk<OrderedRowKeys> rowKeyChunk = rowSequence.asRowKeyChunk();
        final int chunkSize = keyChunk0.size();
        for (int chunkPosition = 0; chunkPosition < chunkSize; ++chunkPosition) {
            final double k0 = keyChunk0.get(chunkPosition);
            final int hash = hash(k0);
            final int firstTableLocation = hashToTableLocation(hash);
            int tableLocation = firstTableLocation;
            long existingStateValue;
            while (!isStateEmpty(existingStateValue = mainRightRowKey.getUnsafe(tableLocation))) {
                if (eq(mainKeySource0.getUnsafe(tableLocation), k0)) {
                    if (existingStateValue != NO_RIGHT_STATE_VALUE) {
                        if (joinType == NaturalJoinType.FIRST_MATCH) {
                            // no-op, we already have the first match;
                        } else if (joinType == NaturalJoinType.LAST_MATCH) {
                            // we are processing sequentially so this is the latest;
                            mainRightRowKey.set(tableLocation, rowKeyChunk.get(chunkPosition));
                        } else {
                            mainRightRowKey.set(tableLocation, DUPLICATE_RIGHT_STATE);
                            throw new DuplicateRightRowDecorationException(tableLocation);
                        }
                    } else {
                        final long rightRowKeyToInsert = rowKeyChunk.get(chunkPosition);
                        mainRightRowKey.set(tableLocation, rightRowKeyToInsert);
                    }
                    break;
                }
                tableLocation = nextTableLocation(tableLocation);
                Assert.neq(tableLocation, "tableLocation", firstTableLocation, "firstTableLocation");
            }
        }
    }

    private static int hash(double k0) {
        int hash = DoubleChunkHasher.hashInitialSingle(k0);
        return hash;
    }

    private static boolean isStateEmpty(long state) {
        return state == EMPTY_RIGHT_STATE;
    }
}
