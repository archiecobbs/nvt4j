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

public class OptionNegotiationCommand extends OptionCommand {

    private int type;

    public int getType() { return type; }

    public OptionNegotiationCommand(int position, int type, TelnetOption option) {
        super(position, option);
        switch (type) {
        case WILL:
        case WONT:
        case DO:
        case DONT:
            this.type = type;
            break;
        default:
            throw new IllegalArgumentException("type " + toString(type) + " not valid");
        }
    }

    public void execute(TelnetOptionHandler optionHandler) throws IOException {
        switch (type) {
        case WILL:
            optionHandler.onWILL();
            break;
        case WONT:
            optionHandler.onWONT();
            break;
        case DO:
            optionHandler.onDO();
            break;
        case DONT:
            optionHandler.onDONT();
            break;
        default:
            throw new RuntimeException("bug: unknown type: " + type);
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(toString(IAC));
        buf.append(' ');
        buf.append(toString(type));
        buf.append(' ');
        buf.append(option);
        return buf.toString();
    }

}
