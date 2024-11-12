package com.dicoding.myaqidahmobile.core.firebasemodel

data class StepHistory(
    val date: String,
    var steps: Int,
    var calories: Int
) {
    companion object {
        var stepTarget: Int = 0
    }

    fun isTargetMet(): Boolean {
        return steps >= stepTarget
    }
}
