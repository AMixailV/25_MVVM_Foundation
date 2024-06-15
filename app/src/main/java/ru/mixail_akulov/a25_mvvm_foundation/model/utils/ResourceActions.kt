package ru.mixail_akulov.a25_mvvm_foundation.model.utils

typealias ResourceAction<T> = (T) -> Unit

/**
 * Очередь действий, где действия выполняются только при наличии ресурса.
 * Если это не так, действие добавляется в очередь и ожидает, пока ресурс не станет доступным.
 */
class ResourceActions<T> {

    var resource: T? = null
        set(newValue) {
            field = newValue
            if (newValue != null) {  // если ресурс (MainActivity в нашем случае) не нуль
                actions.forEach { it(newValue) } // то выполняем действия
                actions.clear()
            }
        }

    private val actions = mutableListOf<ResourceAction<T>>()

    /**
     * Вызывайте действие только тогда, когда [resource] существует (не нуль).
     * В противном случае действие откладывается до тех пор,
     * пока [resource] не будет присвоено какое-либо ненулевое значение.
     */

    // данный метод позволяет прямо на переменной whenActivityActive без дополнительного указывания метода,
    // в котором выполняется данный код. Для сокращения кода.
    operator fun invoke(action: ResourceAction<T>) {
        val resource = this.resource
        if (resource == null) {
            actions += action
        } else {
            action(resource)
        }
    }

    fun clear() {
        actions.clear()
    }
}