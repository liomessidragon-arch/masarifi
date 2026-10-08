# مصاريفي - مشروع APK (Capacitor)

## 1) رفع الملفات
ارفع محتويات هذا المجلد كلها إلى الـ repo (مع مجلد `.github` المخفي). التطبيق نفسه في `www/index.html`، وأي تعديل عليه يتم هناك.

## 2) إعداد Firebase (مرة واحدة)
في Firebase Console ← Authentication ← Settings ← Authorized domains ← أضف: `localhost`
بدونها لن يعمل تسجيل الدخول داخل التطبيق.

## 3) بناء APK
تبويب Actions ← "Build Android APK" ← Run workflow. بعد انتهائه نزّل الملف من Artifacts (masarifi-apk).
بدون أسرار التوقيع ينتج APK تجريبي (debug) قابل للتثبيت مباشرة.

## 4) التوقيع (للنسخة النهائية)
أنشئ مفتاحاً مرة واحدة واحفظه في مكان آمن (إن ضاع لن تستطيع تحديث التطبيق):
`keytool -genkeypair -v -keystore masarifi.jks -alias masarifi -keyalg RSA -keysize 2048 -validity 10000`
ثم حوّله: `base64 -w0 masarifi.jks` وأضف في Settings ← Secrets ← Actions:
`KEYSTORE_BASE64` و `KS_PASS` و `KEY_ALIAS` و `KEY_PASS`.
بعدها يخرج الناتج موقّعاً باسم masarifi-release.apk.

## ما هو أصلي في هذه النسخة
- زر الرجوع الأصلي مع لوحة تأكيد الخروج.
- تصدير النسخ الاحتياطية عبر مشاركة أندرويد.
- تذكير يومي 9 مساءً (من الإعدادات، يظهر داخل التطبيق فقط).
- أيقونة وشاشة بداية بهوية مصاريفي.

معرّف التطبيق `com.aboqais.masarifi` ثابت بعد النشر، فغيّره في capacitor.config.json قبل أول نشر إذا أردت.
