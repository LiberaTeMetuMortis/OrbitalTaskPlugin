package me.metumortis.orbitaltask

import java.sql.Connection
import java.sql.DriverManager

fun connect(database: String): Connection {
    return DriverManager.getConnection("jdbc:sqlite:$database");
}