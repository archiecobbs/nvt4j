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

public class FunctionCommand extends TelnetCommand {
    
    private int function;

    public int getFunction() { return function; }

    public FunctionCommand(int position, int function) {
	super(position);
	switch (function) {
	case NOP:
	case DM:
	case BRK:
	case IP:
	case AO:
	case AYT:
	case EC:
	case EL:
	case GA:
	    this.function = function;
	    break;
	default:
	    throw new IllegalArgumentException("Invalid function: " + toString(function));
	}
    }

    public String toString() {
	StringBuffer buf = new StringBuffer();
	buf.append(toString(IAC));
	buf.append(' ');
	buf.append(toString(function));
	return buf.toString();
    }

}
