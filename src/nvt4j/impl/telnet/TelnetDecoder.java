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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class TelnetDecoder {

    private static final int STATE_DATA = 1;
    private static final int STATE_IAC = 2;
    private static final int STATE_WILL = 3;
    private static final int STATE_WONT = 4;
    private static final int STATE_DO = 5;
    private static final int STATE_DONT = 6;
    private static final int STATE_SB = 7;
    private static final int STATE_SB_DATA = 8;
    private static final int STATE_SB_DATA_IAC = 9 ;

    private int state;
    private ByteArrayOutputStream sbData;
    private int sbOption;
    private LinkedList commands;
    private int pos;

    public TelnetDecoder(LinkedList commands) {
	this.commands = commands;
	state = STATE_DATA;
    }

    /*
     * Decodes r bytes of a circular buffer starting from off, in place.
     */
    public int decode(byte[] buf, int off, int r) throws IOException {
	int p = 0;
	for (int i = 0; i < r; i++) {
	    byte b = buf[(off + i) % buf.length];
	    int c = b & 0xFF;
	    switch (state) {
	    case STATE_DATA:
		switch (c) {
		case TelnetCommand.IAC:
		    state = STATE_IAC;
		    break;
		default:
		    buf[(off + p++) % buf.length] = b;
		    break;
		}
		break;
	    case STATE_IAC:
		switch (c) {
		case TelnetCommand.NOP:
		case TelnetCommand.DM:
		case TelnetCommand.BRK:
		case TelnetCommand.IP:
		case TelnetCommand.AO:
		case TelnetCommand.AYT:
		case TelnetCommand.EC:
		case TelnetCommand.EL:
		case TelnetCommand.GA:
		    commands.add(new FunctionCommand(pos + p, c));
		    state = STATE_DATA;
		    break;
		case TelnetCommand.SB:
		    state = STATE_SB;
		    break;
		case TelnetCommand.SE:
		    //FIXME must get IAC SE in the SB_DATA state
		    throw new IOException("Protocol violation: SE outside of subnegotiation");
		case TelnetCommand.WILL:
		    state = STATE_WILL;
		    break;
		case TelnetCommand.WONT:
		    state = STATE_WONT;
		    break;
		case TelnetCommand.DO:
		    state = STATE_DO;
		    break;
		case TelnetCommand.DONT:
		    state = STATE_DONT;
		    break;
		case TelnetCommand.IAC:
		default:
		    buf[(off + p++) % buf.length] = b;
		    state = STATE_DATA;
		    break;
		}
		break;
	    case STATE_WILL:
		commands.add(new OptionNegotiationCommand(pos + p, TelnetCommand.WILL, TelnetOption.getOption(c)));
		state = STATE_DATA;
		break;
	    case STATE_WONT:
		commands.add(new OptionNegotiationCommand(pos + p, TelnetCommand.WONT, TelnetOption.getOption(c)));
		state = STATE_DATA;
		break;
	    case STATE_DO:
		commands.add(new OptionNegotiationCommand(pos + p, TelnetCommand.DO, TelnetOption.getOption(c)));
		state = STATE_DATA;
		break;
	    case STATE_DONT:
		commands.add(new OptionNegotiationCommand(pos + p, TelnetCommand.DONT, TelnetOption.getOption(c)));
		state = STATE_DATA;
		break;
	    case STATE_SB:
		sbOption = c;
		sbData = new ByteArrayOutputStream();
		state = STATE_SB_DATA;
		break;
	    case STATE_SB_DATA:
		switch (c) {
		case TelnetCommand.IAC:
		    state = STATE_SB_DATA_IAC;
		    break;
		default:
		    sbData.write(c);
		    break;
		}
		break;
	    case STATE_SB_DATA_IAC:
		switch (c) {
		case TelnetCommand.SE:
		    byte[] data = sbData.toByteArray();
		    commands.add(new OptionSubnegotiationCommand(pos + p, TelnetOption.getOption(sbOption), data));
		    state = STATE_DATA;
		    break;
		default:
		    sbData.write(c);
		    state = STATE_SB_DATA;
		    break;
		}
		break;
	    default:
		break;
	    }
	}
	pos = pos + p;
	return p;
    }

}
