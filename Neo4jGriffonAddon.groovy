/*
    griffon-neo4j plugin
    Copyright (C) 2010-2012 Andres Almiray

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

import griffon.core.GriffonClass
import griffon.core.GriffonApplication
import griffon.plugins.neo4j.Neo4jConnector
import griffon.plugins.neo4j.Neo4jEnhancer

/*
import org.neo4j.graphdb.Node
import org.neo4j.graphdb.Relationship
import org.neo4j.graphdb.RelationshipType
*/

/**
 * @author Andres Almiray
 */
class Neo4jGriffonAddon {
    void addonInit(GriffonApplication app) {
        /*
        [Node.metaClass, Relationship.metaClass].each { mc ->
            mc.getAt = {String propertyName ->
                delegate.getProperty(propertyName)
            }
            mc.putAt = {String propertyName, Object value ->
                delegate.setProperty(propertyName, value) 
            }
        }
        Node.metaClass.relate = {RelationshipType relType, Node other ->
            return delegate.createRelationshipTo(other, relType)
        }
        */
        ConfigObject config = Neo4jConnector.instance.createConfig(app)
        Neo4jConnector.instance.connect(app, config)
    }

    void addonPostInit(GriffonApplication app) {
        def types = app.config.griffon?.neo4j?.injectInto ?: ['controller']
        for(String type : types) {
            for(GriffonClass gc : app.artifactManager.getClassesOfType(type)) {
                Neo4jEnhancer.enhance(gc.metaClass)
            }
        }
    }

    Map events = [
        ShutdownStart: { app ->
            ConfigObject config = Neo4jConnector.instance.createConfig(app)
            Neo4jConnector.instance.disconnect(app, config)
        }
    ]
}
