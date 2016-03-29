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

package org.dellroad.nvt4j.impl.option;

import java.io.IOException;
import org.dellroad.nvt4j.impl.Terminal;
import org.dellroad.nvt4j.impl.telnet.AbstractOptionHandler;
import org.dellroad.nvt4j.impl.telnet.TelnetOption;
import org.dellroad.nvt4j.impl.telnet.TelnetOutputStream;

public class NawsOptionHandler extends AbstractOptionHandler {

    private Terminal terminal;

    public NawsOptionHandler(Terminal terminal) {
        super(TelnetOption.NAWS);
        this.terminal = terminal;
    }

    public void start(TelnetOutputStream telnetOutputStream) throws IOException {
        super.start(telnetOutputStream);
        do_();
    }

    public synchronized void onWILL() throws IOException {
        if (!on) {
            do_();
            on = true;
        }
    }

    public synchronized void onWONT() throws IOException {
        ready = false;
    }

    public synchronized void onDO() throws IOException {
        wont();
    }

    public synchronized void onDONT() throws IOException {
        //ignore
    }

    public synchronized void onSubnegotiation(byte[] buf, int off, int len) throws IOException {
        if (len != 4) {
            throw new IOException("NAWS subnegotiation must have 4 bytes");
        }
        int c1 = 0xFF & buf[off];
        int c2 = 0xFF & buf[off + 1];
        int r1 = 0xFF & buf[off + 2];
        int r2 = 0xFF & buf[off + 3];
        int columns = (c1 << 8) | c2;
        int rows = (r1 << 8) | r2;
        terminal.resize(rows, columns);
        ready = true;
    }

}
