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

import java.io.IOException;

public class DefaultOptionHandler extends AbstractOptionHandler {

    public DefaultOptionHandler(TelnetOption option) {
        super(option);
    }

    public void start(TelnetOutputStream telnetOutputStream) throws IOException {
        super.start(telnetOutputStream);
    }

    public synchronized void onWILL() throws IOException {
        dont();
    }

    public synchronized void onWONT() throws IOException {
        //do nothing
    }

    public synchronized void onDO() throws IOException {
        wont();
    }

    public synchronized void onDONT() throws IOException {
        //do nothing
        on = false;
    }

    public synchronized void onSubnegotiation(byte[] data, int off, int len)
        throws IOException {
        dont();
        on = false;
    }

}
