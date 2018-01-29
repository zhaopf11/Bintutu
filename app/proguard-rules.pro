# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.** {*;}
-keep class com.tencent.mm.sdk.** {*;}
-keep class com.android.volley.** {*;}
-keep class com.sina.weibo.** {*;}
-keep class com.google.zxing.** {*;}
-keep class okhttp3.** {*;}
-keep class javax.net.ssl.SSLSocket.** {*;}
-keep class com.jude.rollviewpager.** {*;}

#spots-dialog
-keep class dmax.** {*;}

## Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

# luban 图片压缩 rxjava rxandroid
-keep class rx.** {*;}
#-keep class com.zhurui.bunnymall.utils.ImageCompressUtils. { *; }


-keep class com.zhurui.bunnymall.cart.model.** { *; }# 保持项目中的bean类不被混淆
-keep class com.zhurui.bunnymall.common.model.** { *; }
-keep class com.zhurui.bunnymall.classify.bean.** { *; }
-keep class com.zhurui.bunnymall.home.bean.** { *; }
-keep class com.zhurui.bunnymall.mine.bean.** { *; }
-keep class com.zhurui.bunnymall.viewutils.bean.** { *; }
-keepclassmembers class * implements java.io.Serializable {
static final long serialVersionUID;
private static final java.io.ObjectStreamField[] serialPersistentFields;
private void writeObject(java.io.ObjectOutputStream);
 private void readObject(java.io.ObjectInputStream);
 java.lang.Object writeReplace();
java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}



#butterknife
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# 极光推送
-keep class cn.jpush.** { *; }
-keep class cn.jiguang.** { *; }

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keep public class * extends android.support.v7.app.AppCompatActivity      # 保持哪些类不被混淆
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
 #保持 Serializable 不被混淆
 -keepnames class * implements java.io.Serializable


#小能混淆
-dontwarn cn.xiaoneng.**
-dontwarn android.support.v4xn.**
-dontwarn org.fusesource.**
-dontwarn net.sf.retrotranslator.**
-dontwarn edu.emory.mathcs.backport.java.util.**
-keep class cn.xiaoneng.** {*;}
-keep class android.support.v4xn.** {*;}
-keep class org.fusesource.** {*;}
-keep class net.sf.retrotranslator.** {*;}
-keep class edu.emory.mathcs.backport.java.util.** {*;}
-ignorewarnings

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.** { *; }

#umeng
-dontwarn com.umeng.**
-keep class com.umeng.**{*;}
-keep class u.aly.**{*;}


#baidu
-keep class com.baidu.** { *; }
-dontwarn com.baidu.**

#alipay
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.ut.*

-dontwarn android.net.**
-keep class android.net.** { *; }

 #如果有引用v4包可以添加下面这行
 -keep public class * extends android.support.v4.app.Fragment

 #如果引用了v4或者v7包
 -dontwarn android.support.**
 -keep public class * extends android.view.View {
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
     public void set*(...);
 }

 #保留crash日志的行号 以及源文件信息
 -renamesourcefileattribute SourceFile
 -keepattributes SourceFile,LineNumberTable
