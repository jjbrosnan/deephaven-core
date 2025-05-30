/*
 * Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
 */
#pragma once

#include <cstdint>
#include <cstddef>
#include <memory>
#include "deephaven/dhcore/types.h"
#include "deephaven/dhcore/column/column_source.h"

namespace deephaven::dhcore::utility {
class CythonSupport {
  using ColumnSource = deephaven::dhcore::column::ColumnSource;
public:
  static std::shared_ptr<ColumnSource> CreateBooleanColumnSource(const uint8_t *data_begin,
      const uint8_t *data_end, const uint8_t *validity_begin, const uint8_t *validity_end,
      size_t num_elements);
  static std::shared_ptr<ColumnSource> CreateStringColumnSource(const char *text_begin,
      const char *text_end, const uint32_t *offsets_begin, const uint32_t *offsets_end,
      const uint8_t *validity_begin, const uint8_t *validity_end, size_t num_elements);
  static std::shared_ptr<ColumnSource> CreateDateTimeColumnSource(const int64_t *data_begin, const int64_t *data_end,
      const uint8_t *validity_begin, const uint8_t *validity_end, size_t num_elements);
  static std::shared_ptr<ColumnSource> CreateLocalDateColumnSource(const int64_t *data_begin, const int64_t *data_end,
      const uint8_t *validity_begin, const uint8_t *validity_end, size_t num_elements);
  static std::shared_ptr<ColumnSource> CreateLocalTimeColumnSource(const int64_t *data_begin, const int64_t *data_end,
      const uint8_t *validity_begin, const uint8_t *validity_end, size_t num_elements);

  /**
   * For backwards compatibility. Will be removed when Cython is updated.
   */
  static ElementTypeId::Enum GetElementTypeId(const ColumnSource &column_source);
};
}  // namespace deephaven::dhcore::utility
