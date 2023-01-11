package dslExample

fun main(){
    // Some example of a cloud DSL
    cloud {
        project(name = "Mein Project") {
            description = "Beschreibung"
            environment = ProjectEnvironment.Development
            database("db1", DatabaseEngine.PostgreSQL){
                var startValue = 1
                startValue += 10
                println(this)
                println(startValue)
            }
            database("db2", DatabaseEngine.MySQL){
                println(this)
                println(12)
            }
        }
    }
}
class CloudDSL{
    internal val projects = mutableListOf<Project>()

    fun project(name: String, init: Project.() -> Unit): Project {
        val project = Project(name).apply(init)
        projects += project
        return project
    }
}

fun cloud(init: CloudDSL.() -> Unit): CloudDSL {
    return CloudDSL().apply(init)
}


class Project(internal val name: String) {
    var description: String? = null
    var environment: ProjectEnvironment? = null

    internal val databases = mutableListOf<Database>()

    fun database(name: String,
                 engine: DatabaseEngine,
                 init: Database.() -> Unit): Database{
        val database = Database(name, engine).apply(init)
        databases += database
        databases.add(database)
        return database
    }
}

enum class ProjectEnvironment {
    Development, Staging, Production
}
class Database(
    internal val name: String,
    internal val engine: DatabaseEngine
) : Taggable {
    var nodes: Int = 1
    var size: String = "2vpcu-20gb"
    var region: String = "nyc1"
    var version: String? = null
    override var tags: Set<String>? = null
    override fun toString(): String {
        return "Database(name='$name', engine=$engine, nodes=$nodes, size='$size', region='$region', version=$version, tags=$tags)"
    }


}

enum class DatabaseEngine(internal val engineName: String) {
    PostgreSQL("pg"), MySQL("mysql"), Redis("redis"), MongoDB("mongoDB")
}

interface Taggable{
    var tags: Set<String>?
}

