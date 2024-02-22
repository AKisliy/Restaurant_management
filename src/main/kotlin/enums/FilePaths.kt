package enums
/**
 * FilePaths - перечисление, хранящее пути к файлам с сериализованной информацией
 */
enum class FilePaths(val path: String) {
    ADMINS_FILE("admins.json"),
    USERS_FILE("users.json"),
    ORDERS_FILE("orders.json"),
    MENU_FILE("menu.json")
}