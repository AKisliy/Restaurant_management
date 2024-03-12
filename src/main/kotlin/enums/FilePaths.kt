package enums

/**
 * FilePaths - stores paths to the files with serialized data
 */
enum class FilePaths(val path: String) {
    ADMINS_FILE("database/admins.json"),
    USERS_FILE("database/users.json"),
    ORDERS_FILE("database/orders.json"),
    MENU_FILE("database/menu.json"),
    RESTAURANT_FILE("database/restaurant.json")
}