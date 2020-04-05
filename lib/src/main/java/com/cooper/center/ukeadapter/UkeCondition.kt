package com.cooper.center.ukeadapter

interface UkeCondition {

    fun accept(`object`: Any, position: Int, totalCount: Int): Boolean

    companion object {

        fun clz(clz: Class<*>): UkeCondition {
            return object: UkeCondition {
                override fun accept(`object`: Any, position: Int, totalCount: Int): Boolean {
                    return clz.isInstance(`object`)
                }
            }
        }

    }

}
