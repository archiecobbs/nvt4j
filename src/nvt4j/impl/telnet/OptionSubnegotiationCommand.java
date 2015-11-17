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

public class OptionSubnegotiationCommand extends OptionCommand {

    private byte[] data;

    public byte[] getData() { return data; }

    public OptionSubnegotiationCommand(int position, TelnetOption option, byte[] data) {
        super(position, option);
        this.data = data;
    }

    public void execute(TelnetOptionHandler optionHandler) throws IOException {
        optionHandler.onSubnegotiation(data, 0, data.length);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(toString(IAC));
        buf.append(' ');
        buf.append(toString(SB));
        buf.append(' ');
        buf.append(option);
        for (int i = 0; i < data.length; i++) {
            buf.append(' ');
            buf.append(data[i]);
        }
        buf.append(' ');
        buf.append(toString(IAC));
        buf.append(' ');
        buf.append(toString(SE));
        buf.append(' ');
        return buf.toString();
    }

}
