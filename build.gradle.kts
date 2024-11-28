import com.android.builder.files.classpathToRelativeFileSet

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.android.library") version "7.1.1" apply  false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.firebase.appdistribution") version "5.0.0" apply false
}