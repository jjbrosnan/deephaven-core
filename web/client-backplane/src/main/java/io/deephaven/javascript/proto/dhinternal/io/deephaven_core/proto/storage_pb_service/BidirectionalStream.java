//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.javascript.proto.dhinternal.io.deephaven_core.proto.storage_pb_service;

import elemental2.core.Function;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(
        isNative = true,
        name = "dhinternal.io.deephaven_core.proto.storage_pb_service.BidirectionalStream",
        namespace = JsPackage.GLOBAL)
public interface BidirectionalStream<ReqT, ResT> {
    void cancel();

    void end();

    BidirectionalStream on(String type, Function handler);

    BidirectionalStream write(ReqT message);
}
