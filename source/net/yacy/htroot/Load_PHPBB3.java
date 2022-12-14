// Load_PHPBB3.java
// (C) 2009 by Michael Peter Christen; mc@yacy.net, Frankfurt a. M., Germany
// first published 09.06.2009 as IndexCreate_p.java on http://yacy.net
//
// This is a part of YaCy, a peer-to-peer based web search engine
//
// $LastChangedDate$
// $LastChangedRevision$
// $LastChangedBy$
//
// LICENSE
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package net.yacy.htroot;

import java.util.Set;

import net.yacy.cora.protocol.RequestHeader;
import net.yacy.search.Switchboard;
import net.yacy.search.SwitchboardConstants;
import net.yacy.server.serverObjects;
import net.yacy.server.serverSwitch;

public class Load_PHPBB3 {

    public static serverObjects respond(@SuppressWarnings("unused") final RequestHeader header, final serverObjects post, final serverSwitch env) {
        // return variable that accumulates replacements
        final Switchboard sb = (Switchboard) env;
        final serverObjects prop = new serverObjects();

        // define visible variables
        String myUrl;
        final Set<String> myIps = sb.peers.mySeed().getIPs();
        if (myIps.isEmpty()) {
            myUrl = "http://localhost:" + sb.getLocalPort();
        } else {
            myUrl = sb.peers.mySeed().getPublicURL(myIps.iterator().next(), true);
        }
        final boolean intranet = sb.getConfig(SwitchboardConstants.NETWORK_NAME, "").equals("intranet");
        prop.put("starturl", (intranet) ? myUrl : "http://");
        prop.put("address", myUrl);

        // hidden form param : clean up search events cache ?
        if (post != null && post.containsKey("cleanSearchCache")) {
        	prop.put("cleanSearchCacheChecked", post.getBoolean("cleanSearchCache"));
        } else {
			/*
			 * no parameter passed : no search event cache clean-up
			 * when JavaScript search resort is enabled, as it heavily relies on search events cache
			 */
			prop.put("cleanSearchCacheChecked", !sb.getConfigBool(SwitchboardConstants.SEARCH_JS_RESORT,
					SwitchboardConstants.SEARCH_JS_RESORT_DEFAULT));
        }

        // return rewrite properties
        return prop;
    }
}
