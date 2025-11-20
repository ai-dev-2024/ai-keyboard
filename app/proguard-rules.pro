# Add project specific ProGuard rules here.
-keep class com.microsoft.onnxruntime.** { *; }
-keep class com.alphacephei.vosk.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn com.microsoft.onnxruntime.**
-dontwarn com.alphacephei.vosk.**

