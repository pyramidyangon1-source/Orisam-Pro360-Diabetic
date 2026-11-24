package com.orisam.pro360

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Blue = Color(0xFF1479C9)
private val Pale = Color(0xFFEAF4FC)

data class Reading(val value: String, val timing: String, val date: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { OrisamApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrisamApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("orisam_data", Context.MODE_PRIVATE) }
    var tab by remember { mutableStateOf(0) }
    var readings by remember { mutableStateOf(loadReadings(prefs)) }
    var insulin by remember { mutableStateOf(prefs.getString("insulin", "") ?: "") }
    var notes by remember { mutableStateOf(prefs.getString("notes", "") ?: "") }

    MaterialTheme(colorScheme = lightColorScheme(primary = Blue, secondary = Color(0xFF27A69A), background = Color(0xFFF5F8FC))) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("Orisam Pro360", fontWeight = FontWeight.Bold)
                            Text("ဆီးချိုစောင့်ကြည့်ရေး · Type 1", style = MaterialTheme.typography.labelSmall)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(selected = tab == 0, onClick = { tab = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("ပင်မ") })
                    NavigationBarItem(selected = tab == 1, onClick = { tab = 1 }, icon = { Icon(Icons.Default.AddCircle, null) }, label = { Text("မှတ်တမ်း") })
                    NavigationBarItem(selected = tab == 2, onClick = { tab = 2 }, icon = { Icon(Icons.Default.Medication, null) }, label = { Text("အင်ဆူလင်") })
                    NavigationBarItem(selected = tab == 3, onClick = { tab = 3 }, icon = { Icon(Icons.Default.Assessment, null) }, label = { Text("မှတ်တမ်းများ") })
                }
            },
            containerColor = Color(0xFFF5F8FC)
        ) { padding ->
            when (tab) {
                0 -> HomeScreen(readings, Modifier.padding(padding), onAdd = { tab = 1 })
                1 -> AddReadingScreen(Modifier.padding(padding)) { value, timing ->
                    val stamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
                    val updated = listOf(Reading(value, timing, stamp)) + readings
                    readings = updated
                    saveReadings(prefs, updated)
                    tab = 0
                }
                2 -> InsulinScreen(Modifier.padding(padding), insulin, onSave = {
                    insulin = it
                    prefs.edit().putString("insulin", it).apply()
                })
                else -> HistoryScreen(Modifier.padding(padding), readings, notes, onNotes = {
                    notes = it
                    prefs.edit().putString("notes", it).apply()
                })
            }
        }
    }
}

@Composable
fun HomeScreen(readings: List<Reading>, modifier: Modifier, onAdd: () -> Unit) {
    val latest = readings.firstOrNull()?.value ?: "—"
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Blue), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text("ယနေ့ သွေးချိုအခြေအနေ", color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text(latest, color = Color.White, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
                    Text(if (latest == "—") "တိုင်းတာချက် မထည့်ရသေးပါ" else "နောက်ဆုံးမှတ်တမ်း · ${readings.first().timing}", color = Color.White)
                    Spacer(Modifier.height(14.dp))
                    Button(onClick = onAdd, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Blue)) {
                        Icon(Icons.Default.Add, null); Spacer(Modifier.width(6.dp)); Text("သွေးချိုထည့်ရန်")
                    }
                }
            }
        }
        item { Text("အရေးကြီးသော သတိပြုရန်", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium) }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4E5))) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, null, tint = Color(0xFF9A5B00))
                    Spacer(Modifier.width(10.dp))
                    Text("Type 1 ဆီးချိုတွင် အင်ဆူလင်ကို ဆရာဝန်ညွှန်ကြားချက်အတိုင်းသာ အသုံးပြုပါ။ App မှ ဆေးပမာဏကို မညွှန်ကြားပါ။")
                }
            }
        }
        item { Text("နောက်ဆုံးမှတ်တမ်းများ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium) }
        items(readings.take(5)) { r ->
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                ListItem(
                    headlineContent = { Text("${r.value} mg/dL", fontWeight = FontWeight.Bold) },
                    supportingContent = { Text("${r.timing} · ${r.date}") },
                    leadingContent = { Icon(Icons.Default.Bloodtype, null, tint = Blue) }
                )
            }
        }
        if (readings.isEmpty()) item { Text("စတင်ရန် သွေးချိုတန်ဖိုးတစ်ခု မှတ်တမ်းတင်ပါ။") }
    }
}

@Composable
fun AddReadingScreen(modifier: Modifier, onSave: (String, String) -> Unit) {
    var value by remember { mutableStateOf("") }
    var timing by remember { mutableStateOf("မနက်စာမစားမီ") }
    var error by remember { mutableStateOf(false) }
    Column(modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("သွေးချိုမှတ်တမ်းအသစ်", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("သွေးချိုစက်တွင် ပြထားသော တန်ဖိုးကို ထည့်ပါ။")
        OutlinedTextField(value, { value = it.filter(Char::isDigit) }, label = { Text("သွေးချိုတန်ဖိုး (mg/dL)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), singleLine = true)
        Text("ဘယ်အချိန်တိုင်းတာသလဲ")
        listOf("မနက်စာမစားမီ", "အစာစားပြီး ၂ နာရီ", "နေ့လယ်", "ညစာမစားမီ", "အိပ်ရာမဝင်မီ", "အခြား").forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = timing == it, onClick = { timing = it })
                Text(it)
            }
        }
        if (error) Text("မှန်ကန်သော တန်ဖိုး ထည့်ပေးပါ။", color = MaterialTheme.colorScheme.error)
        Button(onClick = {
            if (value.isNotBlank() && value.toIntOrNull() != null && value.toInt() in 1..1500) onSave(value, timing) else error = true
        }, modifier = Modifier.fillMaxWidth()) { Text("မှတ်တမ်းသိမ်းမည်") }
        Text("မှတ်ချက် - သွေးချိုတန်ဖိုး၏ ယူနစ်ကို မိမိစက်တွင် စစ်ဆေးပါ။ mmol/L အသုံးပြုပါက မထည့်မီ mg/dL သို့ ပြောင်းလဲရန်လိုနိုင်သည်။", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
fun InsulinScreen(modifier: Modifier, saved: String, onSave: (String) -> Unit) {
    var text by remember(saved) { mutableStateOf(saved) }
    Column(modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("အင်ဆူလင်မှတ်တမ်း", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Card(colors = CardDefaults.cardColors(containerColor = Pale)) {
            Text("အင်ဆူလင်အမျိုးအစား၊ ဆရာဝန်ညွှန်ကြားထားသော အချိန်နှင့် ကိုယ်တိုင်မှတ်သားလိုသော အချက်များကိုသာ ရေးပါ။", Modifier.padding(16.dp))
        }
        OutlinedTextField(text, { text = it }, label = { Text("ကိုယ်ပိုင်မှတ်စု") }, modifier = Modifier.fillMaxWidth().height(160.dp))
        Button(onClick = { onSave(text) }, modifier = Modifier.fillMaxWidth()) { Text("သိမ်းမည်") }
        Text("သတိပေးချက် - အင်ဆူလင်ပမာဏကို ကိုယ်တိုင်မပြောင်းလဲပါနှင့်။ သွေးချိုနိမ့်ခြင်း/မြင့်ခြင်းအတွက် ဆရာဝန်ပေးထားသော အရေးပေါ်အစီအစဉ်ကို လိုက်နာပါ။", color = Color(0xFF9A5B00), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun HistoryScreen(modifier: Modifier, readings: List<Reading>, savedNotes: String, onNotes: (String) -> Unit) {
    var notes by remember(savedNotes) { mutableStateOf(savedNotes) }
    Column(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("မှတ်တမ်းနှင့် အစီရင်ခံစာ", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("စုစုပေါင်း တိုင်းတာချက် - ${readings.size}")
        val nums = readings.mapNotNull { it.value.toIntOrNull() }
        if (nums.isNotEmpty()) {
            Text("ပျမ်းမျှ - ${nums.average().toInt()} mg/dL")
            Text("အနိမ့်ဆုံး - ${nums.minOrNull()} mg/dL     အမြင့်ဆုံး - ${nums.maxOrNull()} mg/dL")
        }
        OutlinedTextField(notes, { notes = it; onNotes(it) }, label = { Text("ဆရာဝန်ကို ပြောပြရန် မှတ်စု") }, modifier = Modifier.fillMaxWidth())
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(readings) { r ->
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    ListItem(headlineContent = { Text("${r.value} mg/dL") }, supportingContent = { Text("${r.timing} · ${r.date}") })
                }
            }
        }
    }
}

private fun loadReadings(prefs: android.content.SharedPreferences): List<Reading> {
    return try {
        val arr = JSONArray(prefs.getString("readings", "[]"))
        (0 until arr.length()).map { i ->
            val o = arr.getJSONObject(i)
            Reading(o.getString("value"), o.getString("timing"), o.getString("date"))
        }
    } catch (_: Exception) { emptyList() }
}

private fun saveReadings(prefs: android.content.SharedPreferences, list: List<Reading>) {
    val arr = JSONArray()
    list.forEach {
        arr.put(JSONObject().put("value", it.value).put("timing", it.timing).put("date", it.date))
    }
    prefs.edit().putString("readings", arr.toString()).apply()
}
