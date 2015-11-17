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

import java.io.OutputStream;
import java.io.IOException;

public class TelnetEncoder {

    public static final int CR = 13;
    public static final int LF = 10;

    private OutputStream out;
    private byte[] buf;
    private int last;

    public TelnetEncoder(OutputStream out) {
	buf = new byte[512];
	this.out = out;
    }

    public void encode(byte[] buf, int off, int len) throws IOException {
	int p = 0;
	for (int i = off; i < off + len; i++) {
	    int c = 0xFF & buf[i];
	    if (last == CR) {
		if (c != LF) {
		    //CR -> CR 0
		    this.buf[p++] = (byte) 0;
		}
	    } else {
		if (c == LF) {
		    //LF -> CR LF
		    this.buf[p++] = (byte) CR;
		} else if (c == TelnetCommand.IAC) {
		    //IAC -> IAC IAC
		    this.buf[p++] = (byte) TelnetCommand.IAC;
		}
	    }
	    this.buf[p++] = (byte) c;
	    last = c;
	    if (p > this.buf.length - 2) {
		out.write(this.buf, 0, p);
		p = 0;
	    }
	}
	if (p > 0) {
	    out.write(this.buf, 0, p);
	}
    }

    public void encodeFunction(int function) throws IOException {
	switch (function) {
	case TelnetCommand.NOP:
	case TelnetCommand.DM:
	case TelnetCommand.BRK:
	case TelnetCommand.IP:
	case TelnetCommand.AO:
	case TelnetCommand.AYT:
	case TelnetCommand.EC:
	case TelnetCommand.EL:
	case TelnetCommand.GA:
	    break;
	default:
	    throw new IllegalArgumentException("Invalid function: " +
					       TelnetCommand.toString(function));
	}
	out.write(TelnetCommand.IAC);
	out.write(function);
	out.flush();
    }

    public void encodeOptionNegotiation(int type, TelnetOption option) throws IOException {
	switch (type) {
	case TelnetCommand.WILL:
	case TelnetCommand.WONT:
	case TelnetCommand.DO:
	case TelnetCommand.DONT:
	    break;
	default:
	    throw new IllegalArgumentException("type " +
					       TelnetCommand.toString(type) +
					       " not valid");
	}
	out.write(TelnetCommand.IAC);
	out.write(type);
	out.write(option.getCode());
	out.flush();
    }

    public void encodeOptionSubnegotiation(TelnetOption option, byte[] buf, int off, int len)
	throws IOException {
	out.write(TelnetCommand.IAC);
	out.write(TelnetCommand.SB);
	out.write(option.getCode());
	out.write(buf, off, len);
	out.write(TelnetCommand.IAC);
	out.write(TelnetCommand.SE);
	out.flush();
    }

}
