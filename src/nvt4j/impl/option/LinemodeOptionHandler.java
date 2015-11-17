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

package nvt4j.impl.option;

import java.io.IOException;
import nvt4j.impl.telnet.AbstractOptionHandler;
import nvt4j.impl.telnet.TelnetOption;
import nvt4j.impl.telnet.TelnetOutputStream;

public class LinemodeOptionHandler extends AbstractOptionHandler {

    private static final int MODE = 1;
    private static final int FORWARDMASK = 2;
    private static final int SLC = 3;

    private static final int EDIT = 1;
    private static final int TRAPSIG = 2;
    private static final int MODE_ACK = 4;
    private static final int SOFT_TAB = 8;
    private static final int LIT_ECHO = 16;

    private int mask;

    public LinemodeOptionHandler() {
	super(TelnetOption.LINEMODE);
    }

    public void start(TelnetOutputStream telnetOutputStream) throws IOException {
	super.start(telnetOutputStream);
	//mask = ~EDIT | ~TRAPSIG | ~SOFT_TAB | ~LIT_ECHO;
	mask = 0;
	do_();
    }

    public synchronized void onWILL() throws IOException {
	if (!on) {
	    do_();
	    on = true;
	    writeMask();
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

    private synchronized void writeMask() throws IOException {
	byte[] buf= new byte[] { (byte) MODE, (byte) this.mask };
	telnetOutputStream.writeOptionSubnegotiation(option, buf);
	ready = true;
    }

    public synchronized void onSubnegotiation(byte[] buf, int off, int len) throws IOException {
	int subOption = 0xFF & buf[off];
	switch (subOption) {
	case MODE:
	    int mask = 0xFF & buf[off + 1];
	    if (mask != this.mask) {
		ready = false;
	    }
	    break;
	case FORWARDMASK:
	case SLC:
	default:
	    writeMask();
	    break;
	}
    }

}
