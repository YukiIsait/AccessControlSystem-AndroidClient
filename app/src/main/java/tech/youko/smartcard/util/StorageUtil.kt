package tech.youko.smartcard.util

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

fun Context.writeObjectToFile(fileName: String, obj: Any?) {
    ObjectOutputStream(openFileOutput(fileName, Context.MODE_PRIVATE)).use {
        it.writeObject(obj)
    }
}

fun Context.readOriginalObjectFromFile(fileName: String): Any? {
    return try {
        ObjectInputStream(openFileInput(fileName)).use {
            it.readObject()
        }
    } catch (_: FileNotFoundException) {
        null
    }
}

inline fun <reified T> Context.readObjectFromFile(fileName: String): T? {
    return readOriginalObjectFromFile(fileName) as T?
}

inline fun <reified T> MutableStateFlow<T?>.initializeStorage(
    coroutineScope: CoroutineScope,
    context: Context,
    fileName: String
) {
    value = context.readObjectFromFile<T>(fileName)
    coroutineScope.launch {
        collect {
            context.writeObjectToFile(fileName, it)
        }
    }
}
