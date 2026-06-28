plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "org.example"
version = providers.environmentVariable("VERSION").getOrElse("1.1.3")

labyMod {
    defaultPackageName = "com.rappytv.betterfriends"

    addonInfo {
        namespace = "betterfriends"
        displayName = "BetterFriends"
        author = "RappyTV, JarDateien"
        description = "All-in-one addon packed with advanced features for your LabyChat friends."
        minecraftVersion = "*"
        version = rootProject.version.toString()

        addon("voicechat", true)
    }

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    devLogin = true
                }
            }
        }
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version

    extensions.findByType(JavaPluginExtension::class.java)?.apply {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}