/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.common.io;

import org.gwtproject.nio.Numbers;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * An implementation of {@link DataOutput} that uses little-endian byte ordering for writing {@code
 * char}, {@code short}, {@code int}, {@code float}, {@code double}, and {@code long} values.
 *
 * <p><b>Note:</b> This class intentionally violates the specification of its supertype {@code
 * DataOutput}, which explicitly requires big-endian byte order.
 *
 * @author Chris Nokleberg
 * @author Keith Bottner
 * @since 8.0
 */
public final class LittleEndianDataOutputStream extends FilterOutputStream implements DataOutput {

    /**
     * Creates a {@code LittleEndianDataOutputStream} that wraps the given stream.
     *
     * @param out the stream to delegate to
     */
    public LittleEndianDataOutputStream(OutputStream out) {
        super(new DataOutputStream(Objects.requireNonNull(out)));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        // Override slow FilterOutputStream impl
        out.write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        ((DataOutputStream) out).writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        ((DataOutputStream) out).writeByte(v);
    }

    /**
     * @deprecated The semantics of {@code writeBytes(String s)} are considered dangerous. Please use
     *     {@link #writeUTF(String s)}, {@link #writeChars(String s)} or another write method instead.
     */
    @Deprecated
    @Override
    public void writeBytes(String s) throws IOException {
        ((DataOutputStream) out).writeBytes(s);
    }

    /**
     * Writes a char as specified by {@link DataOutputStream#writeChar(int)}, except using
     * little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeChar(int v) throws IOException {
        writeShort(v);
    }

    /**
     * Writes a {@code String} as specified by {@link DataOutputStream#writeChars(String)}, except
     * each character is written using little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeChars(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            writeChar(s.charAt(i));
        }
    }

    /**
     * Writes a {@code double} as specified by {@link DataOutputStream#writeDouble(double)}, except
     * using little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    /**
     * Writes a {@code float} as specified by {@link DataOutputStream#writeFloat(float)}, except using
     * little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeFloat(float v) throws IOException {
        writeInt(Numbers.floatToIntBits(v));
    }

    /**
     * Writes an {@code int} as specified by {@link DataOutputStream#writeInt(int)}, except using
     * little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeInt(int v) throws IOException {
        out.write(0xFF & v);
        out.write(0xFF & (v >> 8));
        out.write(0xFF & (v >> 16));
        out.write(0xFF & (v >> 24));
    }

    /**
     * Writes a {@code long} as specified by {@link DataOutputStream#writeLong(long)}, except using
     * little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeLong(long v) throws IOException {
        out.write(0xFF & (int) v);
        out.write(0xFF & (int) (v >> 8));
        out.write(0xFF & (int) (v >> 16));
        out.write(0xFF & (int) (v >> 24));
        out.write(0xFF & (int) (v >> 32));
        out.write(0xFF & (int) (v >> 40));
        out.write(0xFF & (int) (v >> 48));
        out.write(0xFF & (int) (v >> 56));
    }

    /**
     * Writes a {@code short} as specified by {@link DataOutputStream#writeShort(int)}, except using
     * little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeShort(int v) throws IOException {
        out.write(0xFF & v);
        out.write(0xFF & (v >> 8));
    }

    @Override
    public void writeUTF(String s) throws IOException {
        throw new UnsupportedOperationException("modified utf-8");
    }
}
