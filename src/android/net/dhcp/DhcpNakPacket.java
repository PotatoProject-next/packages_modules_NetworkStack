/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.net.dhcp;

import java.net.Inet4Address;
import java.nio.ByteBuffer;

/**
 * This class implements the DHCP-NAK packet.
 */
public class DhcpNakPacket extends DhcpPacket {
    /**
     * Generates a NAK packet with the specified parameters.
     */
    DhcpNakPacket(int transId, short secs, Inet4Address relayIp, byte[] clientMac,
            boolean broadcast) {
        super(transId, secs, INADDR_ANY /* clientIp */, INADDR_ANY /* yourIp */,
                INADDR_ANY /* nextIp */, relayIp, clientMac, broadcast);
    }

    public String toString() {
        String s = super.toString();
        return s + " NAK, reason " + (mMessage == null ? "(none)" : mMessage);
    }

    /**
     * Fills in a packet with the requested NAK attributes.
     */
    public ByteBuffer buildPacket(int encap, short destUdp, short srcUdp) {
        ByteBuffer result = ByteBuffer.allocate(MAX_LENGTH);
        // Constructor does not set values for layers <= 3: use empty values
        Inet4Address destIp = INADDR_ANY;
        Inet4Address srcIp = INADDR_ANY;

        fillInPacket(encap, destIp, srcIp, destUdp, srcUdp, result, DHCP_BOOTREPLY, mBroadcast);
        result.flip();
        return result;
    }

    /**
     * Adds the optional parameters to the client-generated NAK packet.
     */
    void finishPacket(ByteBuffer buffer) {
        addTlv(buffer, DHCP_MESSAGE_TYPE, DHCP_MESSAGE_TYPE_NAK);
        addTlv(buffer, DHCP_SERVER_IDENTIFIER, mServerIdentifier);
        addTlv(buffer, DHCP_MESSAGE, mMessage);
        addTlvEnd(buffer);
    }
}
