/*
    griffon-neo4j plugin
    Copyright (C) 2010 Andres Almiray

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

/**
 * @author Andres Almiray
 */

// check to see if we already have a Neo4jGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'Neo4jGriffonAddon' == builder
    }
}

if (addonIsSet1) {
    println 'Removing Neo4jGriffonAddon from Builder.groovy'
    builderConfigFile.text = builderConfigFile.text - "root.'Neo4jGriffonAddon'.addon=true\n"
}
