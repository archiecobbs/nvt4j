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

package org.dellroad.nvt4j.impl.telnet;

import java.io.OutputStream;
import java.io.IOException;

public class TelnetOutputStream extends OutputStream {

    private OutputStream out;
    private byte[] byteBuf;
    private TelnetEncoder encoder;

    public TelnetOutputStream(OutputStream out) {
        this.out = out;
        byteBuf = new byte[1];
        encoder = new TelnetEncoder(out);
    }

    public void write(int b) throws IOException {
        byteBuf[0] = (byte) b;
        write(byteBuf, 0, 1);
    }

    public void write(byte[] buf) throws IOException {
        write(buf, 0, buf.length);
    }

    public void write(byte[] buf, int off, int len) throws IOException {
        encoder.encode(buf, off, len);
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void close() throws IOException {
        out.close();
    }

    public void writeNOP() throws IOException {
        encoder.encodeFunction(TelnetCommand.NOP);
    }

    public void writeDM() throws IOException {
        encoder.encodeFunction(TelnetCommand.DM);
    }

    public void writeBRK() throws IOException {
        encoder.encodeFunction(TelnetCommand.BRK);
    }

    public void writeIP() throws IOException {
        encoder.encodeFunction(TelnetCommand.IP);
    }

    public void writeAO() throws IOException {
        encoder.encodeFunction(TelnetCommand.AO);
    }

    public void writeAYT() throws IOException {
        encoder.encodeFunction(TelnetCommand.AYT);
    }

    public void writeEC() throws IOException {
        encoder.encodeFunction(TelnetCommand.EC);
    }

    public void writeEL() throws IOException {
        encoder.encodeFunction(TelnetCommand.EL);
    }

    public void writeGA() throws IOException {
        encoder.encodeFunction(TelnetCommand.GA);
    }

    public void writeWILL(TelnetOption option) throws IOException {
        encoder.encodeOptionNegotiation(TelnetCommand.WILL, option);
    }

    public void writeWONT(TelnetOption option) throws IOException {
        encoder.encodeOptionNegotiation(TelnetCommand.WONT, option);
    }

    public void writeDO(TelnetOption option) throws IOException {
        encoder.encodeOptionNegotiation(TelnetCommand.DO, option);
    }

    public void writeDONT(TelnetOption option) throws IOException {
        encoder.encodeOptionNegotiation(TelnetCommand.DONT, option);
    }

    public void writeOptionSubnegotiation(TelnetOption option, byte[] buf) throws IOException {
        writeOptionSubnegotiation(option, buf, 0, buf.length);
    }

    public void writeOptionSubnegotiation(TelnetOption option, byte[] buf, int off, int len)
        throws IOException {
        encoder.encodeOptionSubnegotiation(option, buf, off, len);
    }

}
