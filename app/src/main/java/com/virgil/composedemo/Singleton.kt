package com.virgil.composedemo

/**
 * 双重检查锁式单例
 */
class Singleton private constructor(){

    companion object{
        @Volatile
        private var instance: Singleton?=null

        fun getInstance(): Singleton {
            if (instance == null){
                synchronized(Singleton::class.java){
                    if (instance == null){
                        instance = Singleton()
                    }
                }
            }
            return instance!!
        }
    }

    fun doSomthing(){

    }
}