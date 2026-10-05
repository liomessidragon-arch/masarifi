# مصاريفي V3.5.1 — Android Native Notifications

هذه النسخة تضيف طبقة Android أصلية للإشعارات فوق واجهة Masarifi V3.5.

## ما تم إصلاحه
- إذن `POST_NOTIFICATIONS` لأندرويد 13+
- Notification Channel باسم «تنبيهات مصاريفي»
- JavaScript bridge باسم `MasarifiAndroid`
- زر «تفعيل التنبيهات» داخل التطبيق يطلب إذن Android الحقيقي
- تنبيهات الميزانية تستخدم إشعارات Android الأصلية داخل APK
- الإشعارات تبقى Toast fallback في الويب فقط
- زر الرجوع يحاول إغلاق الواجهة المفتوحة قبل الخروج

## البناء
افتح المشروع في Android Studio أو شغّل GitHub Actions المرفق.

Debug APK:
`app/build/outputs/apk/debug/app-debug.apk`

ملاحظة: ملف `app/src/main/assets/index.html` هو نسخة Masarifi V3.5.1 المعدلة.
