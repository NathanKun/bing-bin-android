-keep public class com.google.android.gms.* { public *; }
-keep class com.google.android.gms.vision.** { *; }
-keep interface com.google.android.gms.vision.** { *; }
-keep interface com.google.android.gms.common.** { *; }

-dontwarn com.google.android.gms.**

# dagger
-dontwarn com.google.errorprone.annotations.*

# okio
-dontwarn okio.**