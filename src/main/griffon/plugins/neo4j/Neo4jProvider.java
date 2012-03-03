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

package griffon.plugins.neo4j;

import groovy.lang.Closure;
import griffon.util.CallableWithArgs;

/**
 * @author Andres Almiray
 */
public interface Neo4jProvider {
    Object withNeo4j(Closure closure);

    Object withNeo4j(String databaseName, Closure closure);

    <T> T withNeo4j(CallableWithArgs<T> callable);

    <T> T withNeo4j(String databaseName, CallableWithArgs<T> callable);
}
