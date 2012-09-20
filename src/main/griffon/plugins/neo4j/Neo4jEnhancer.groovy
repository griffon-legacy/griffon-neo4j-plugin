/*
    griffon-neo4j plugin
    Copyright (C) 2012 Andres Almiray

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package griffon.plugins.neo4j

import griffon.util.CallableWithArgs
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
final class Neo4jEnhancer {
    private static final Logger LOG = LoggerFactory.getLogger(Neo4jEnhancer)

    private Neo4jEnhancer() {}

    static void enhance(MetaClass mc, Neo4jProvider provider = DatabaseHolder.instance) {
        if(LOG.debugEnabled) LOG.debug("Enhancing $mc with $provider")
        mc.withNeo4j = {Closure closure ->
            provider.withNeo4j('default', closure)
        }
        mc.withNeo4j << {String databaseName, Closure closure ->
            provider.withNeo4j(databaseName, closure)
        }
        mc.withNeo4j << {CallableWithArgs callable ->
            provider.withNeo4j('default', callable)
        }
        mc.withNeo4j << {String databaseName, CallableWithArgs callable ->
            provider.withNeo4j(databaseName, callable)
        }
    }
}
