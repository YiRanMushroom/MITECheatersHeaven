package com.yiranmushroom.mixin_helper

interface IFlySpeedInfo {
    fun modifyFlySpeed(speed: Float): Float
}

class FlySpeedInfoOneFourth : IFlySpeedInfo {
    override fun modifyFlySpeed(speed: Float): Float {
        return speed / 4
    }
}

class FlySpeedInfoOneHalf : IFlySpeedInfo {
    override fun modifyFlySpeed(speed: Float): Float {
        return speed / 2
    }
}

class FlySpeedInfoNormal : IFlySpeedInfo {
    override fun modifyFlySpeed(speed: Float): Float {
        return speed
    }
}

class FlySpeedInfoDouble : IFlySpeedInfo {
    override fun modifyFlySpeed(speed: Float): Float {
        return speed * 2
    }
}

class FlySpeedInfoQuadruple : IFlySpeedInfo {
    override fun modifyFlySpeed(speed: Float): Float {
        return speed * 4
    }
}

class FlySpeedInfoOctuple : IFlySpeedInfo {
    override fun modifyFlySpeed(speed: Float): Float {
        return speed * 8
    }
}

class FlySpeedInfoQuintuple : IFlySpeedInfo {
    override fun modifyFlySpeed(speed: Float): Float {
        return speed * 16
    }
}

class FlySpeedInfoCustom(modifier: Float.() -> Float) : IFlySpeedInfo {
    private val modifierFunction = modifier
    override fun modifyFlySpeed(speed: Float): Float {
        return speed.modifierFunction()
    }
}

object EntityClientPlayerFlySpeedMixinHelper {
    var customMultiplier = 32f

    var flySpeedInfos = listOf(
        FlySpeedInfoOneFourth(),
        FlySpeedInfoOneHalf(),
        FlySpeedInfoNormal(),
        FlySpeedInfoDouble(),
        FlySpeedInfoQuadruple(),
        FlySpeedInfoOctuple(),
        FlySpeedInfoQuintuple(),
        FlySpeedInfoCustom { this * customMultiplier }
    )

    var currentIndex = 2 // Default to Normal speed

    @JvmStatic
    fun modifyFlySpeed(speed: Float): Float {
        return flySpeedInfos[currentIndex].modifyFlySpeed(speed)
    }

    @JvmStatic
    fun roundRobinSpeed() {
        currentIndex = (currentIndex + 1) % flySpeedInfos.size
    }

    @JvmStatic
    fun setSpeedIndex(index: Int) {
        currentIndex = index.coerceIn(0, flySpeedInfos.size - 1)
    }
}