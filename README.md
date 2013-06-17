
Neo4j support
-------------

Plugin page: [http://artifacts.griffon-framework.org/plugin/neo4j](http://artifacts.griffon-framework.org/plugin/neo4j)


The Neo4j plugin enables lightweight access to [Neo4j][1] databases.
This plugin does NOT provide domain classes nor dynamic finders like GORM does.

Usage
-----
Upon installation the plugin will generate the following artifacts in `$appdir/griffon-app/conf`:

 * Neo4jConfig.groovy - contains the database definitions.
 * BootstrapNeo4j.groovy - defines init/destroy hooks for data to be manipulated during app startup/shutdown.

A new dynamic method named `withNeo4j` will be injected into all controllers,
giving you access to a `org.neo4j.graphdb.GraphDatabaseService` object, with which you'll be able
to make calls to the database. Remember to make all database calls off the EDT
otherwise your application may appear unresponsive when doing long computations
inside the EDT.

This method is aware of multiple databases. If no databaseName is specified when calling
it then the default database will be selected. Here are two example usages, the first
queries against the default database while the second queries a database whose name has
been configured as 'internal'

    package sample
    class SampleController {
        def queryAllDatabases = {
            withNeo4j { databaseName, database -> ... }
            withNeo4j('internal') { databaseName, database -> ... }
        }
    }

This method is also accessible to any component through the singleton `griffon.plugins.neo4j.Neo4jConnector`.
You can inject these methods to non-artifacts via metaclasses. Simply grab hold of a particular metaclass and call
`Neo4jEnhancer.enhance(metaClassInstance, neo4jProviderInstance)`.

Configuration
-------------
### Dynamic method injection

The `withNeo4j()` dynamic method will be added to controllers by default. You can
change this setting by adding a configuration flag in `griffon-app/conf/Config.groovy`

    griffon.neo4j.injectInto = ['controller', 'service']

### Events

The following events will be triggered by this addon

 * Neo4jConnectStart[config, databaseName] - triggered before connecting to the database
 * Neo4jConnectEnd[databaseName, database] - triggered after connecting to the database
 * Neo4jDisconnectStart[config, databaseName, database] - triggered before disconnecting from the database
 * Neo4jDisconnectEnd[config, databaseName] - triggered after disconnecting from the database

### Multiple Stores

The config file `Neo4jConfig.groovy` defines a default database block. As the name
implies this is the database used by default, however you can configure named databases
by adding a new config block. For example connecting to a database whose name is 'internal'
can be done in this way

    databases {
        internal {
            params = [:]
            storeDir = 'neo4j/internal'
        }
    }

This block can be used inside the `environments()` block in the same way as the
default database block is used.

### Example

A trivial sample application can be found at [https://github.com/aalmiray/griffon_sample_apps/tree/master/persistence/neo4j][2]

Testing
-------
The `withNeo4j()` dynamic method will not be automatically injected during unit testing, because addons are simply not initialized
for this kind of tests. However you can use `Neo4jEnhancer.enhance(metaClassInstance, neo4jProviderInstance)` where 
`neo4jProviderInstance` is of type `griffon.plugins.neo4j.Neo4jProvider`. The contract for this interface looks like this

    public interface Neo4jProvider {
        Object withNeo4j(Closure closure);
        Object withNeo4j(String databaseName, Closure closure);
        <T> T withNeo4j(CallableWithArgs<T> callable);
        <T> T withNeo4j(String databaseName, CallableWithArgs<T> callable);
    }

It's up to you define how these methods need to be implemented for your tests. For example, here's an implementation that never
fails regardless of the arguments it receives

    class MyNeo4jProvider implements Neo4jProvider {
        Object withNeo4j(String databaseName = 'default', Closure closure) { null }
        public <T> T withNeo4j(String databaseName = 'default', CallableWithArgs<T> callable) { null }
    }

This implementation may be used in the following way

    class MyServiceTests extends GriffonUnitTestCase {
        void testSmokeAndMirrors() {
            MyService service = new MyService()
            Neo4jEnhancer.enhance(service.metaClass, new MyNeo4jProvider())
            // exercise service methods
        }
    }


[1]: http://neo4j.org/
[2]: https://github.com/aalmiray/griffon_sample_apps/tree/master/persistence/neo4j

