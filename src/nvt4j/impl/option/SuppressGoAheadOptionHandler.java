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

public class SuppressGoAheadOptionHandler extends AbstractOptionHandler {

    public SuppressGoAheadOptionHandler() {
        super(TelnetOption.SUPPRESS_GO_AHEAD);
    }

    public void start(TelnetOutputStream telnetOutputStream) throws IOException {
        super.start(telnetOutputStream);
        do_();
    }

    public synchronized void onWILL() throws IOException {
        if (!on) {
            do_();
            on = true;
            ready = true;
        }
    }

    public synchronized void onWONT() throws IOException {
        ready = false;
    }

    public synchronized void onDO() throws IOException {
        if (!on) {
            will();
            on = true;
            ready = true;
        }
    }

    public synchronized void onDONT() throws IOException {
        ready = false;
    }

}
