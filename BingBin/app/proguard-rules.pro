-keep public class com.google.android.gms.* { public *; }
-keep class com.google.android.gms.vision.** { *; }
-keep interface com.google.android.gms.vision.** { *; }
-keep interface com.google.android.gms.common.** { *; }

-dontwarn com.google.android.gms.**

# dagger
-dontwarn com.google.errorprone.annotations.*

# okio
-dontwarn okio.**

-keep class android.support.v7.app.AppCompatViewInflater {*;}

# BottomNavigationViewEx
#-keep class io.bingbin.bingbinandroid.views.BottomNavigationViewEx {*;}
-keep public class android.support.design.widget.BottomNavigationView { *; }
-keep public class android.support.design.internal.BottomNavigationMenuView { *; }
-keep public class android.support.design.internal.BottomNavigationPresenter { *; }
-keep public class android.support.design.internal.BottomNavigationItemView { *; }