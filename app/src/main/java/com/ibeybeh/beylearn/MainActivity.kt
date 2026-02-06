package com.ibeybeh.beylearn

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ibeybeh.beylearn.core_navigation.navigator.NavigationEffects
import com.ibeybeh.beylearn.core_navigation.navigator.NavigationManager
import com.ibeybeh.beylearn.core_navigation.routes.HomeNavGraph
import com.ibeybeh.beylearn.core_navigation.routes.homeNavGraph
import com.ibeybeh.beylearn.core_navigation.routes.loginNavGraph
import com.ibeybeh.beylearn.ui.theme.BeyLearnTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeyLearnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
//                    TracingLetterScreen()

                    val navController = rememberNavController()

                    NavigationEffects(navigationManager, navController)

                    AppNavigation(
                        navController = navController,
                        navigationManager = navigationManager
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    navigationManager: NavigationManager
) {
    NavHost(
        navController = navController,
        startDestination = HomeNavGraph::class
    ) {
        homeNavGraph(navigationManager)
        loginNavGraph(navigationManager)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BeyLearnTheme {
        Greeting("Android")
    }
}

@Composable
fun TracingLetterScreen(letterToTrace: String = "A") {
    // 1. State untuk menyimpan Bitmap
    // userDrawingBitmap: Tempat menyimpan coretan tinta pengguna
    var userDrawingBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    // maskBitmap: Tempat menyimpan bentuk solid huruf 'A' untuk kliping
    var maskBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // Canvas internal untuk menggambar ke bitmap di memori
    var drawingCanvas by remember { mutableStateOf<androidx.compose.ui.graphics.Canvas?>(null) }

    // State untuk memicu redraw ringan
    var invalidationTick by remember { mutableStateOf(0) }

    val density = LocalDensity.current

    // Paint untuk menggambar garis tinta pengguna (hitam tebal)
    val inkPaint = remember {
        Paint().apply {
            color = android.graphics.Color.BLACK
            strokeWidth = with(density) { 12.dp.toPx() } // Garis cukup tebal
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }
    }

    // Paint KHUSUS untuk masking.
    // BlendMode.DstIn adalah kuncinya: Hanya mempertahankan gambar tujuan
    // di mana gambar sumber (masker) tidak transparan.
    val maskingPaint = remember {
        androidx.compose.ui.graphics.Paint().apply {
            blendMode = BlendMode.DstIn
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tebalkan Huruf Berikut", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(300.dp) // Ukuran area tracing
                .background(Color.White)
                .onSizeChanged { size ->
                    // Inisialisasi Bitmap saat ukuran diketahui
                    if (size.width > 0 && size.height > 0) {
                        // A. Siapkan Bitmap untuk gambar pengguna
                        val newDrawingBmp =
                            ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888)
                        userDrawingBitmap = newDrawingBmp
                        drawingCanvas = androidx.compose.ui.graphics.Canvas(newDrawingBmp)

                        // B. Siapkan Bitmap MASKER (Bentuk solid huruf 'A')
                        val newMaskBmp =
                            ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888)
                        val maskCanvasNative = android.graphics.Canvas(newMaskBmp.asAndroidBitmap())

                        // Cat untuk menggambar teks masker (harus solid, warna tidak penting)
                        val textMaskPaint = Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = size.height * 0.8f // Ukuran huruf besar proporsional
                            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                            textAlign = Paint.Align.CENTER
                            isAntiAlias = true
                        }

                        // Hitung posisi tengah untuk menggambar teks
                        val xPos = (size.width / 2).toFloat()
                        val yPos =
                            (size.height / 2 - (textMaskPaint.descent() + textMaskPaint.ascent()) / 2)

                        // Gambar huruf solid ke bitmap masker
                        maskCanvasNative.drawText(letterToTrace, xPos, yPos, textMaskPaint)
                        maskBitmap = newMaskBmp
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { drawingCanvas?.save() },
                        onDragEnd = { drawingCanvas?.restore() },
                        onDrag = { change, _ ->
                            change.consume()
                            val start = change.previousPosition
                            val end = change.position

                            drawingCanvas?.let { canvas ->
                                // 1. Gambar garis normal (tinta pengguna)
                                canvas.nativeCanvas.drawLine(
                                    start.x,
                                    start.y,
                                    end.x,
                                    end.y,
                                    inkPaint
                                )

                                // 2. MAGIC STEP: Terapkan Masking
                                // Timpa seluruh kanvas gambar dengan bitmap masker menggunakan mode DstIn.
                                // Ini akan memotong tinta yang baru saja digambar jika keluar dari area masker.
                                maskBitmap?.let { mask ->
                                    canvas.drawImage(mask, Offset.Zero, maskingPaint)
                                }
                            }
                            // Trigger redraw tampilan
                            invalidationTick++
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // --- LAYER 1 (Bawah): Panduan Visual (Shadow) ---
            // Ini hanya teks biasa berwarna abu-abu agar user tahu harus menggambar apa.
            // Ukurannya kita buat sedikit lebih kecil dari maskernya agar garis user menutupi panduan.
            Text(
                text = letterToTrace,
                fontSize = 200.sp, // Ukuran disesuaikan manual atau dikalkulasi agar pas
                fontWeight = FontWeight.Bold,
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            // --- LAYER 2 (Atas): Hasil Gambar Pengguna yang sudah di-masking ---
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Baca tick agar redraw terpanggil
                val tick = invalidationTick
                userDrawingBitmap?.let { bmp ->
                    drawImage(bmp, topLeft = Offset.Zero)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Reset
        Button(onClick = {
            drawingCanvas?.let { canvas ->
                // Bersihkan bitmap gambar pengguna dengan warna transparan
                canvas.nativeCanvas.drawColor(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.PorterDuff.Mode.CLEAR
                )
                invalidationTick++
            }
        }) {
            Text("Ulangi")
        }
    }
}

//@Composable
//fun SignatureScreenOptimized() {
//    // State untuk menyimpan hasil gambar dalam bentuk Bitmap
//    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
//
//    // Canvas internal untuk menggambar ke Bitmap (bukan ke layar langsung)
//    var internalCanvas by remember { mutableStateOf<androidx.compose.ui.graphics.Canvas?>(null) }
//
//    // State sederhana hanya untuk memicu redraw tanpa recomposition berat
//    var invalidationTick by remember { mutableStateOf(0) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Tanda Tangan (No Lag)", style = MaterialTheme.typography.titleMedium)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp)
//                .clip(RoundedCornerShape(8.dp))
//                .background(Color.White)
//                .onSizeChanged { size ->
//                    // 1. Inisialisasi Bitmap saat ukuran komponen diketahui
//                    if (size.width > 0 && size.height > 0) {
//                        val newBitmap = ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888)
//                        val canvas = androidx.compose.ui.graphics.Canvas(newBitmap)
//
//                        // Isi background putih (default transparan) agar hasil export bagus
//                        val paint = Paint().apply { color = Color.White }
//                        canvas.drawRect(androidx.compose.ui.geometry.Rect(0f, 0f, size.width.toFloat(), size.height.toFloat()), paint)
//
//                        bitmap = newBitmap
//                        internalCanvas = canvas
//                    }
//                }
//                .pointerInput(Unit) {
//                    detectDragGestures(
//                        onDragStart = { offset ->
//                            // Simpan titik awal
//                            // Kita bisa tambahkan logic path.moveTo di sini jika pakai Path
//                            // Tapi untuk direct bitmap, kita simpan last position saja
//                            internalCanvas?.let {
//                                // Opsional: gambar titik (dot) jika cuma tap
//                            }
//                        },
//                        onDrag = { change, dragAmount ->
//                            change.consume()
//
//                            val start = change.previousPosition
//                            val end = change.position
//
//                            // 2. Gambar LANGSUNG ke Bitmap (Off-screen)
//                            internalCanvas?.let { canvas ->
//                                val paint = Paint().apply {
//                                    color = Color.Black
//                                    strokeWidth = 5f
//                                    strokeCap = StrokeCap.Round
//                                    isAntiAlias = true
//                                }
//                                canvas.drawLine(start, end, paint)
//                            }
//
//                            // 3. Trigger Redraw ringan
//                            invalidationTick++
//                        }
//                    )
//                }
//        ) {
//            // 4. Canvas Layar HANYA menampilkan Bitmap yang sudah jadi
//            // Ini sangat cepat karena hanya merender 1 gambar, bukan ribuan garis
//            Canvas(modifier = Modifier.fillMaxSize()) {
//                // Baca state ini agar block draw terpanggil ulang saat tick berubah
//                val tick = invalidationTick
//
//                bitmap?.let { img ->
//                    drawImage(img, topLeft = Offset.Zero)
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//            Button(onClick = {
//                // Clear Logic: Timpa bitmap dengan warna putih
//                internalCanvas?.let { canvas ->
//                    val paint = Paint().apply { color = Color.White }
//                    bitmap?.let { bmp ->
//                        canvas.drawRect(androidx.compose.ui.geometry.Rect(0f, 0f, bmp.width.toFloat(), bmp.height.toFloat()), paint)
//                    }
//                    invalidationTick++
//                }
//            }) {
//                Text("Hapus")
//            }
//
//            Button(onClick = {
//                // Logic Simpan
//                bitmap?.let { bmp ->
//                    val androidBitmap = bmp.asAndroidBitmap()
//                    // Kirim androidBitmap ke ViewModel / Repository
//                    println("Bitmap siap disimpan: ${androidBitmap.width}x${androidBitmap.height}")
//                }
//            }) {
//                Text("Simpan")
//            }
//        }
//    }
//}