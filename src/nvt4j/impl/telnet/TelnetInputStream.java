/**
 *  Copyright 2006 Guglielmo Lichtner (lichtner_at_bway_dot_net)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package nvt4j.impl.telnet;

import java.io.InputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Implements an input stream which can decode telnet commands,
 * as per specifications in RFC 854 and 855. Telnet options are
 * returned to the user as TelnetCommandExceptions thrown by the
 * read methods. Since the position of a telnet command in the
 * stream can affect how the input following it is processed, the
 * read methods throw TelnetCommandException at exactly the right
 * point in the stream, and when they do they read no data from the
 * stream.
 */
public class TelnetInputStream extends InputStream {

    public static final int DEFAULT_BUFFER_SIZE = 1500;

    private InputStream in;
    private byte[] buf;
    private byte[] byteBuf;
    private int off;
    private int len;
    private LinkedList commands;
    private TelnetDecoder decoder;
    private int pos;

    public TelnetInputStream(InputStream in) {
        this(in, DEFAULT_BUFFER_SIZE);
    }

    public TelnetInputStream(InputStream in, int size) {
        this.in = in;
        buf = new byte[size];
        byteBuf = new byte[1];
        commands = new LinkedList();
        decoder = new TelnetDecoder(commands);
    }

    private void advance(int n) throws IOException {
        if (n > len) {
            throw new RuntimeException("bug: advancing too far");
        }
        off = (off + n) % buf.length;
        len = len - n;
        //FIXME fix design to handle infinite streams
        //we need to figure out when we can reset the
        //position to zero
        pos = pos + n;
    }

    public int read() throws IOException {
        int r;
        while ((r = read(byteBuf, 0, 1)) == 0);
        if (r != -1) {
            r = 0xFF & byteBuf[0];
        }
        return r;
    }

    public int read(byte[] buf) throws IOException {
        return read(buf, 0, buf.length);
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        int r;
        if (this.len != -1) {
            if (len > this.len && commands.size() == 0) {
                fill();
            }
            if (commands.size() > 0) {
                TelnetCommand telnetCommand = (TelnetCommand) commands.get(0);
                int nextPos = telnetCommand.getPosition();
                if (pos < nextPos) {
                    if (pos + len >= nextPos) {
                        len = nextPos - pos;
                    }
                } else if (pos == nextPos) {
                    commands.remove(0);
                    throw new TelnetCommandException(telnetCommand);
                } else {
                    throw new RuntimeException("bug: read past next command" +
                                               " pos = " + pos +
                                               " nextPos = " + nextPos +
                                               " telnetCommand = " + telnetCommand);
                }
            }
            if (this.len > 0) {
                if (this.len < len) {
                    len = this.len;
                }
                int n = this.buf.length - this.off;
                if (n > len) {
                    n = len;
                }
                System.arraycopy(this.buf, this.off, buf, off, n);
                off = off + n;
                advance(n);
                n = len - n;
                if (n > 0) {
                    System.arraycopy(this.buf, this.off, buf, off, n);
                    advance(n);
                }
                r = len;
            } else if (this.len == -1) {
                r = -1;
            } else {
                r = 0;
            }
        } else {
            r = -1;
        }
        return r;
    }

    private void fill() throws IOException {
        int free = (off + len) % buf.length;
        int room = buf.length - len;
        if (room > 0) {
            int n;
            if (free >= off) {
                n = buf.length - free;
                if (room < n) {
                    n = room;
                }
            } else {
                n = room;
            }
            int r = in.read(buf, free, n);
            if (r != -1) {
                r = decoder.decode(buf, free, r);
                len = len + r;
                room = room - r;
                free = (free + r) % buf.length;
            } else {
                if (len == 0) {
                    len = -1;
                }
            }
        }
    }

    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    public int available() throws IOException {
        return len + in.available();
    }

    public void close() throws IOException {
        len = -1;
        in.close();
    }

    public void mark(int readLimit) {
        in.mark(readLimit);
    }

    public void reset() throws IOException {
        in.reset();
    }

    public boolean markSupported() {
        return false;
    }

}
