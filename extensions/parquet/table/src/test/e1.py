import pandas as pd
import numpy as np

df = pd.DataFrame(
    {
        "a": list("abc"),
        "b": list(range(1, 4)),
        "c": np.arange(3, 6).astype("u1"),
        "d": np.arange(4.0, 7.0, dtype="float64"),
        "e": [True, False, True],
        # TODO(deephaven-core#976): Unable to read parquet TimestampLogicalTypeAnnotation that is not adjusted to UTC
        # "f": pd.date_range("20130101", periods=3),
        "g": pd.date_range("20130101", periods=3, tz="US/Eastern"),
        "h": pd.Categorical(list("abc")),
        "i": pd.Categorical(list("abc"), ordered=True),
    }
)

df.to_parquet("resources/e1/uncompressed.parquet", compression=None)
df.to_parquet("resources/e1/brotli.parquet", compression="brotli")
df.to_parquet("resources/e1/gzip.parquet", compression="gzip")
df.to_parquet("resources/e1/lz4.parquet", compression="lz4")
df.to_parquet("resources/e1/snappy.parquet", compression="snappy")
df.to_parquet("resources/e1/zstd.parquet", compression="zstd")
