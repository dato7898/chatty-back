package com.dato.chatty.util

import java.io.*

class CopyUtil {

    companion object {
        fun <T : Serializable> deepCopy(obj: T?): T? {
            if (obj == null) return null
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            oos.writeObject(obj)
            oos.close()
            val bais = ByteArrayInputStream(baos.toByteArray())
            val ois = ObjectInputStream(bais)
            return ois.readObject() as T
        }
    }

}