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

@SuppressWarnings("serial")
public class TelnetCommandException extends IOException {

    private TelnetCommand telnetCommand;

    public TelnetCommand getTelnetCommand() { return telnetCommand; }

    public TelnetCommandException(TelnetCommand telnetCommand) {
        this.telnetCommand = telnetCommand;
    }

    public String toString() {
        return telnetCommand.toString();
    }

}
