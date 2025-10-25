package com.ecosphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EcoSphereApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoSphereApp() {
    val nav = rememberNavController()
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF059669),
            secondary = Color(0xFF065F46)
        )
    ) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("EcoSphere") }) },
            bottomBar = { BottomBar(nav) }
        ) { inner ->
            NavHost(
                navController = nav,
                startDestination = "home",
                modifier = Modifier.padding(inner)
            ) {
                composable("home") { HomeScreen(nav) }
                composable("dashboard") { CorporateDashboardScreen() }
                composable("supplier") { SupplierDashboardScreen() }
                composable("retrofits") { RetrofitsScreen() }
                composable("about") { AboutScreen() }
            }
        }
    }
}

/* ---------------- Bottom Bar (simplified & safe) ---------------- */

@Composable
fun BottomBar(nav: NavHostController) {
    val backStackEntry by androidx.navigation.compose.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { nav.navigate("home") { launchSingleTop = true; popUpTo(nav.graph.startDestinationId) { saveState = true }; restoreState = true } },
            label = { Text("Home") },
            icon = { Canvas(Modifier.size(24.dp)) { drawRect(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) } }
        )
        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = { nav.navigate("dashboard") { launchSingleTop = true; popUpTo(nav.graph.startDestinationId) { saveState = true }; restoreState = true } },
            label = { Text("Corporate") },
            icon = { Canvas(Modifier.size(24.dp)) { drawRect(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) } }
        )
        NavigationBarItem(
            selected = currentRoute == "supplier",
            onClick = { nav.navigate("supplier") { launchSingleTop = true; popUpTo(nav.graph.startDestinationId) { saveState = true }; restoreState = true } },
            label = { Text("Supplier") },
            icon = { Canvas(Modifier.size(24.dp)) { drawRect(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) } }
        )
        NavigationBarItem(
            selected = currentRoute == "retrofits",
            onClick = { nav.navigate("retrofits") { launchSingleTop = true; popUpTo(nav.graph.startDestinationId) { saveState = true }; restoreState = true } },
            label = { Text("Retrofits") },
            icon = { Canvas(Modifier.size(24.dp)) { drawRect(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) } }
        )
        NavigationBarItem(
            selected = currentRoute == "about",
            onClick = { nav.navigate("about") { launchSingleTop = true; popUpTo(nav.graph.startDestinationId) { saveState = true }; restoreState = true } },
            label = { Text("About") },
            icon = { Canvas(Modifier.size(24.dp)) { drawRect(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) } }
        )
    }
}


/* ---------------- Home ---------------- */

@Composable
fun HomeScreen(nav: NavHostController) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text(
                "Powering Net-Zero Supply Chains",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
        item { Spacer(Modifier.height(6.dp)) }
        item { Text("Measure, optimize and finance Scope-3 decarbonization by activating MSME suppliers.") }
        item { Spacer(Modifier.height(12.dp)) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { nav.navigate("dashboard") }) { Text("Corporate Dashboard") }
                OutlinedButton(onClick = { nav.navigate("supplier") }) { Text("Supplier Dashboard") }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
        item { Text("Solution: Capture → Optimize → Enable → Finance", fontWeight = FontWeight.SemiBold) }
        item {
            Text(
                "• Capture & validate data from invoices/meters\n" +
                        "• Optimize with hotspot insights & ROI\n" +
                        "• Enable via marketplace & tools\n" +
                        "• Finance upgrades with transaction-linked models"
            )
        }
    }
}

/* ---------------- Corporate Dashboard ---------------- */

@Composable
fun CorporateDashboardScreen() {
    val emissions = listOf(1120,1080,1040,980,960,940,910,900,880,860,840,820)
    val savings   = listOf(6.2,6.8,7.1,8.0,8.6,9.2,9.8,10.2,10.7,11.3,11.8,12.4)
    val target    = listOf(6.0,6.5,7.0,7.5,8.0,8.5,9.0,9.5,10.0,10.5,11.0,11.5)

    val m = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)
    val e = emissions[m]; val ePrev = emissions[(m-1).coerceAtLeast(0)]
    val eDelta = ((ePrev - e).toFloat() / ePrev * 100).let { String.format("%.1f", it) }
    val s = savings[m]; val sPrev = savings[(m-1).coerceAtLeast(0)]
    val sDelta = ((s - sPrev) / sPrev * 100).let { String.format("%.1f", it) }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Corporate Portal — Overview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KPI("Monthly Emissions (tCO₂e)", "% ,d".format(e), "↓ $eDelta% vs last month", true, Modifier.weight(1f))
                KPI("Savings This Month (₹)", "₹ %,d".format((s * 1e7).toInt()), "↑ $sDelta% MoM", true, Modifier.weight(1f))
            }
        }
        item { Spacer(Modifier.height(12.dp)) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KPI("Retrofit Requests — In Progress", "2", "Across priority suppliers", null, Modifier.weight(1f))
                KPI("Renewable Energy Adoption", "16%", "On track", true, Modifier.weight(1f))
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Monthly Emissions Trend (tCO₂e)", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    SimpleLineChart(values = emissions.map { it.toFloat() })
                }
            }
        }
        item { Spacer(Modifier.height(12.dp)) }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Savings vs Target (₹ crore)", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    SimpleLineChart(values = target.map { it.toFloat() }, values2 = savings.map { it.toFloat() })
                }
            }
        }
    }
}

@Composable
fun KPI(
    title: String,
    value: String,
    delta: String,
    good: Boolean?,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(Modifier.padding(12.dp)) {
            Text(title, color = Color(0xFF64748B), style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(4.dp))
            val chipColor = when (good) {
                true -> Color(0xFFECFDF5)
                false -> Color(0xFFFEE2E2)
                else -> Color(0xFFF1F5F9)
            }
            AssistChip(
                onClick = {},
                label = { Text(delta) },
                colors = AssistChipDefaults.assistChipColors(containerColor = chipColor)
            )
        }
    }
}

@Composable
fun SimpleLineChart(values: List<Float>, values2: List<Float>? = null) {
    val max = (values + (values2 ?: emptyList())).maxOrNull()?.coerceAtLeast(1f) ?: 1f
    val min = (values + (values2 ?: emptyList())).minOrNull() ?: 0f
    val padX = 12f; val padY = 12f
    Canvas(Modifier.fillMaxWidth().height(180.dp)) {
        val w = size.width; val h = size.height
        fun pathFor(list: List<Float>): Path {
            val step = (w - 2 * padX) / (list.size - 1)
            val p = Path()
            for (i in list.indices) {
                val v = list[i]
                val x = padX + i * step
                val y = padY + (h - 2 * padY) * (1 - (v - min) / (max - min))
                if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        return p
}

        drawPath(pathFor(values), color = MaterialTheme.colorScheme.primary, alpha = 0.9f)
        values2?.let { drawPath(pathFor(it), color = Color(0xFF94A3B8), alpha = 0.9f) }
    }
}

/* ---------------- Supplier Dashboard ---------------- */

data class SupplierRow(val name: String, val score: Int, val data: Int, val status: String)

@Composable
fun SupplierDashboardScreen() {
    var filter by remember { mutableStateOf("All") }
    val rows = remember {
        listOf(
            SupplierRow("Alpha Coils", 72, 90, "Active"),
            SupplierRow("Beta Ltd", 54, 60, "Onboarding"),
            SupplierRow("Gamma Gears", 81, 100, "Active"),
            SupplierRow("Delta Castings", 65, 80, "Active"),
            SupplierRow("Epsilon Plastics", 43, 40, "At Risk")
        )
    }
    val active = rows.count { it.status == "Active" }
    val coverage = (rows.map { it.data }.average()).toInt()
    val avgScore = (rows.map { it.score }.average()).toInt()
    val openTasks = rows.count { it.data < 80 }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Supplier Portal — Overview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KPI("Active Suppliers", active.toString(), "Total in program", null, Modifier.weight(1f))
                KPI("Verified Data Coverage", "$coverage%", "Avg completeness", true, Modifier.weight(1f))
            }
        }
        item { Spacer(Modifier.height(12.dp)) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KPI("Avg Decarb Score", "$avgScore", "Onboarded suppliers", true, Modifier.weight(1f))
                KPI("Open Improvement Tasks", openTasks.toString(), "Below 80% data", null, Modifier.weight(1f))
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Suppliers", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip("All", filter) { filter = "All" }
                        FilterChip("Active", filter) { filter = "Active" }
                        FilterChip("Onboarding", filter) { filter = "Onboarding" }
                        FilterChip("At Risk", filter) { filter = "At Risk" }
                    }
                    Spacer(Modifier.height(8.dp))

                    val filteredList = rows.filter { filter == "All" || it.status == filter }

                    // Nested LazyColumn keeps each row in a clean composable scope
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filteredList) { supplier ->
                            SupplierCard(supplier)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(text: String, current: String, onClick: () -> Unit) {
    val selected = text == current
    AssistChip(
        onClick = onClick,
        label = { Text(text) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color(0xFFF1F5F9)
        )
    )
}

@Composable
fun SupplierCard(r: SupplierRow) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(r.name, fontWeight = FontWeight.SemiBold)
                Text("${r.status} • Data completeness ${r.data}%", color = Color(0xFF64748B))
            }
            Box(Modifier.size(56.dp).background(Color(0xFFEFFDF5))) {
                Text("${r.score}", modifier = Modifier.align(Alignment.Center), fontWeight = FontWeight.Bold)
            }
        }
    }
}

/* ---------------- Retrofits ---------------- */

data class RetrofitItem(val supplier: String, val type: String, val abatement: Float, val status: String, val updated: String)

@Composable
fun RetrofitsScreen() {
    var retrofitItems by remember {
        mutableStateOf(
            listOf(
                RetrofitItem("Alpha Coils", "VFD for Motors", 120f, "In Progress", "2025-10-10"),
                RetrofitItem("Beta Ltd", "Solar Rooftop", 240f, "Pending", "2025-10-18"),
                RetrofitItem("Gamma Gears", "LED Lighting", 35f, "Completed", "2025-09-28"),
                RetrofitItem("Delta Castings", "Heat Pump", 180f, "In Progress", "2025-10-22")
            )
        )
    }
    var show by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Retrofit Requests", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Button(onClick = { show = true }) { Text("+ Request Retrofit") }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(retrofitItems) { r -> RetrofitRow(r) }
        }
    }

    if (show) NewRetrofitDialog(
        onDismiss = { show = false },
        onSubmit = { supplier, type, abate ->
            retrofitItems = listOf(
                RetrofitItem(supplier, type, abate, "Pending", java.time.LocalDate.now().toString())
            ) + retrofitItems
            show = false
        }
    )
}

@Composable
fun RetrofitRow(r: RetrofitItem) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(r.supplier, fontWeight = FontWeight.SemiBold)
                Text(r.type, color = Color(0xFF64748B))
            }
            Text("${r.abatement.toInt()} tCO₂e/yr", Modifier.padding(end = 12.dp))
            AssistChip(onClick = {}, label = { Text(r.status) })
        }
    }
}

@Composable
fun NewRetrofitDialog(onDismiss: () -> Unit, onSubmit: (String, String, Float) -> Unit) {
    var supplier by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Solar Rooftop") }
    var abate by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val a = abate.toFloatOrNull() ?: 0f
                if (supplier.isNotBlank() && a > 0) onSubmit(supplier, type, a)
            }) { Text("Submit") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Request New Retrofit") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = supplier, onValueChange = { supplier = it }, label = { Text("Supplier Name") })
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Retrofit Type") })
                OutlinedTextField(value = abate, onValueChange = { abate = it }, label = { Text("Estimated Abatement (tCO₂e/yr)") })
            }
        }
    )
}

/* ---------------- About ---------------- */

@Composable
fun AboutScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("About EcoSphere", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("SaaS for MSME supply chains. Includes Home, Corporate & Supplier dashboards, plus a Retrofits module.")
    }
}
