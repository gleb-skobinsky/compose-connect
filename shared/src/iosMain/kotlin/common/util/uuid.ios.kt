package common.util

import platform.Foundation.NSUUID

actual fun uuid(): String {
    return NSUUID().UUIDString().lowercase()
}