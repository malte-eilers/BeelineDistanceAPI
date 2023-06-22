package com.example.distanceapi.model.types

enum class TrainStationType(val stationType: String) {
    FV("FV"),
    RV("RV"),
    DPN("DPN"),
    OTHERS("others");

    companion object {
        public fun createFromValue(value: String): TrainStationType {
            return try {
                val type = when (value) {
                    "nur DPN" -> DPN
                    else -> valueOf(value)
                }
                type
            } catch (exception: IllegalArgumentException) {
                OTHERS
            }
        }
    }

}