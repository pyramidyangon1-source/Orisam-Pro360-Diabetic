Orisam Pro360 Diabetic - Android Studio Project
ဗားရှင်း 1.0 | မြန်မာဘာသာ | Type 1 Diabetes

ပါဝင်သောအင်္ဂါရပ်များ
- သွေးချိုတန်ဖိုး (mg/dL) မှတ်တမ်းတင်ခြင်း
- အချိန်အလိုက် မှတ်တမ်းများကြည့်ခြင်း
- အနိမ့်ဆုံး၊ အမြင့်ဆုံး၊ ပျမ်းမျှတန်ဖိုးတွက်ခြင်း
- အင်ဆူလင်ဆိုင်ရာ ကိုယ်ပိုင်မှတ်စု
- ဆရာဝန်ပြရန် မှတ်စု
- ဖုန်းအတွင်း SharedPreferences ဖြင့် ဒေတာသိမ်းခြင်း

APK တည်ဆောက်ရန်
1. ကွန်ပျူတာတွင် Android Studio ထည့်သွင်းပါ။
2. ဒီ ZIP ကို Extract လုပ်ပါ။
3. Android Studio > Open > OrisamPro360Android folder ကိုရွေးပါ။
4. Gradle Sync ပြီးစီးသည်အထိ စောင့်ပါ။
5. Build > Build Bundle(s) / APK(s) > Build APK(s) ကိုနှိပ်ပါ။
6. app/build/outputs/apk/debug/app-debug.apk ကို ဖုန်းသို့ကူးပြီး install လုပ်ပါ။

မှတ်ချက်
ဤ ZIP သည် Android Studio source project ဖြစ်ပြီး APK ဖိုင်ကို compile လုပ်ပြီးသားမဟုတ်ပါ။
ဤအခြေခံဗားရှင်းတွင် cloud backup၊ ဆေးသောက် notification၊ insulin dose calculator၊
Bluetooth glucose meter ချိတ်ဆက်မှုနှင့် PDF export မပါဝင်သေးပါ။

ဘေးကင်းရေး
ဤ App သည် မှတ်တမ်းတင်ရန်သာဖြစ်ပြီး ဆေးဘက်ဆိုင်ရာ ဆုံးဖြတ်ချက်ချရန် မဟုတ်ပါ။
အင်ဆူလင်ပမာဏကို App ၏တွက်ချက်မှုအပေါ် မူတည်၍ မပြောင်းလဲပါနှင့်။
သွေးချိုနိမ့်/မြင့်ခြင်းအတွက် မိမိဆရာဝန်ပေးထားသော အစီအစဉ်ကို လိုက်နာပါ။


Cloud Build workflow
.github/workflows/build-apk.yml ကို ထည့်ထားသည်။ GitHub repository ၏ main branch သို့ project ဖိုင်များတင်ပြီးနောက် Actions tab > Build Orisam Pro360 APK > Run workflow ကိုနှိပ်ပါ။ Build အောင်မြင်လျှင် workflow run အောက်ရှိ Artifacts မှ Orisam-Pro360-Diabetic-APK ကို download လုပ်ပါ။
