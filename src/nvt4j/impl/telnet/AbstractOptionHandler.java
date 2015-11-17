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

import java.io.IOException;

public abstract class AbstractOptionHandler implements TelnetOptionHandler {

    protected TelnetOption option;
    protected TelnetOutputStream telnetOutputStream;
    protected boolean on;
    protected boolean ready;

    public TelnetOption getOption() { return option; }
    public boolean isReady() { return ready; }

    protected AbstractOptionHandler(TelnetOption option) {
	this.option = option;
    }
    
    public void start(TelnetOutputStream telnetOutputStream) throws IOException {
	this.telnetOutputStream = telnetOutputStream;	
    }

    public abstract void onWILL() throws IOException;
    public abstract void onWONT() throws IOException;
    public abstract void onDO() throws IOException;
    public abstract void onDONT() throws IOException;

    public void onSubnegotiation(byte[] data, int off, int len) throws IOException {
	if (option.isComplex()) {
	    throw new RuntimeException("User failed to implement subnegotiation " +
				       " for option: " +
				       option);
	} else {
	    throw new IOException("Received subnegotiation for simple option: " +
				  option);
	}
    }

    protected void will() throws IOException {
	telnetOutputStream.writeWILL(option);
    }

    protected void wont() throws IOException {
	telnetOutputStream.writeWONT(option);
    }

    protected void do_() throws IOException {
	telnetOutputStream.writeDO(option);
    }

    protected void dont() throws IOException {
	telnetOutputStream.writeDONT(option);
    }

    protected void sub(byte[] buf) throws IOException {
	sub(buf, 0, buf.length);
    }

    protected void sub(byte[] buf, int off, int len) throws IOException {
	telnetOutputStream.writeOptionSubnegotiation(option, buf, off, len);
    }

}
