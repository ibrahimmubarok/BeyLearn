package com.ibeybeh.beylearn.navigation.processor.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class BeyNavigationProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    companion object {
        private const val ANNOTATION_QNAME =
            "com.ibeybeh.beylearn.core_navigation.annotation.BeyDestination"
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(ANNOTATION_QNAME)
        val validFunctions = symbols.filterIsInstance<KSFunctionDeclaration>()
            .filter { it.validate() }

        if (!validFunctions.iterator().hasNext()) return emptyList()

        try {
            // 3. GROUPING: Kelompokkan fungsi berdasarkan Parent Graph-nya
            // Map<ClassName, List<KSFunctionDeclaration>>
            val graphGroups = validFunctions.groupBy { func ->
                val parentGraphType = getParentGraphType(func)
                // Mengubah KSType menjadi ClassName (KotlinPoet) untuk jadi Key Map
                parentGraphType.toClassName()
            }

            // 4. GENERATE: Loop setiap grup dan buat filenya
            graphGroups.forEach { (graphClassName, routes) ->
                logger.warn("Generating graph extension for: ${graphClassName.simpleName}")

                generateGraphExtension(
                    resolver = resolver,
                    graphClassName = graphClassName,
                    routes = routes
                )
            }

        } catch (e: Exception) {
            logger.error("Error processing navigation: ${e.message}")
        }

        // Return simbol yang tidak valid untuk diproses di round berikutnya
        return symbols.filterNot { it.validate() }.toList()
    }

    private fun generateGraphExtension(
        resolver: Resolver,
        graphClassName: ClassName,
        routes: List<KSFunctionDeclaration>
    ) {
        // A. Setup Class & Member Names (Agar Import Otomatis)
        val navGraphBuilderClass = ClassName("androidx.navigation", "NavGraphBuilder")
        val navManagerClass = ClassName(
            "com.ibeybeh.beylearn.core_navigation.navigator",
            "NavigationManager"
        ) // Sesuaikan package

        val navComposeNavigation = MemberName("androidx.navigation.compose", "navigation")
        val navComposeComposable = MemberName("androidx.navigation.compose", "composable")

        // B. Nama Fungsi: HomeNavGraph -> homeNavGraph
        val graphName = graphClassName.simpleName.replaceFirstChar { it.lowercase() }

        val startRoute = routes.find { func -> isStartDestination(func) }
        // Fallback: Jika tidak ada yang true, ambil route pertama dalam list
            ?: routes.firstOrNull()
            ?: return

        val startRouteClass = getRouteType(startRoute)

//        // C. Cari Start Destination
//        // Logika: Cari yang punya anotasi @StartDestination, atau ambil yang pertama
//        val startRoute = routes.find { func ->
//            // Sesuaikan nama anotasi StartDestination Anda jika ada
//            func.annotations.any { it.shortName.asString() == "isStartDestination" }
//        } ?: routes.firstOrNull()
//        ?: return
//
//        val startRouteClass = getRouteType(startRoute)

        // D. Buat Code Block
        val codeBlock = CodeBlock.builder().apply {
            // navigation<Graph>(startDestination = ...)
            beginControlFlow(
                "%M<%T>(startDestination = %T::class)",
                navComposeNavigation,
                graphClassName,
                startRouteClass
            )

            routes.forEach { func ->
                val routeType = getRouteType(func)
                val screenFunction =
                    MemberName(func.packageName.asString(), func.simpleName.asString())

                // composable<Route> {
                beginControlFlow("%M<%T>", navComposeComposable, routeType)

                // Screen(navigationManager = navigationManager)
                addStatement("%M(navigationManager = navigationManager)", screenFunction)

                endControlFlow()
            }
            endControlFlow()
        }.build()

        // E. Buat Fungsi Extension
        val functionSpec = FunSpec.builder(graphName)
            .addModifiers(KModifier.PUBLIC)
            .receiver(navGraphBuilderClass) // Extension fun NavGraphBuilder.nama()
            .addParameter("navigationManager", navManagerClass)
            .addCode(codeBlock)
            .build()

        // F. Buat File
        val fileSpec =
            FileSpec.builder(graphClassName.packageName, "${graphClassName.simpleName}Ext")
                .addFunction(functionSpec)
                .build()

        // G. Tulis File (Menggunakan mapNotNull agar aman dari !!)
        val dependencies = Dependencies(
            aggregating = true,
            sources = routes.mapNotNull { it.containingFile }.toTypedArray()
        )

        fileSpec.writeTo(codeGenerator, dependencies)
    }

    // --- HELPER 1: Mengambil Tipe Parent Graph dari Anotasi ---
    private fun getParentGraphType(func: KSFunctionDeclaration): KSType {
        val annotation = func.annotations.find { it.shortName.asString() == "BeyDestination" }!!

        // Cari argumen 'parentGraph' (bisa via nama atau posisi)
        val arg = annotation.arguments.find { it.name?.asString() == "parentGraph" }
            ?: annotation.arguments.getOrNull(1) // Fallback ke index 1 jika positional

        return arg?.value as? KSType
            ?: throw IllegalStateException("parentGraph tidak ditemukan di ${func.simpleName.asString()}")
    }

    // --- HELPER 2: Mengambil Tipe Route dari Anotasi ---
    // Digunakan di dalam generateGraphExtension
    private fun getRouteType(func: KSFunctionDeclaration): ClassName {
        val annotation = func.annotations.find { it.shortName.asString() == "BeyDestination" }!!

        // Cari argumen 'route' (biasanya index 0)
        val arg = annotation.arguments.find { it.name?.asString() == "route" }
            ?: annotation.arguments.getOrNull(0) // Fallback index 0

        val type = arg?.value as? KSType
            ?: throw IllegalStateException("Route class tidak ditemukan di ${func.simpleName.asString()}")

        return type.toClassName()
    }

    private fun isStartDestination(func: KSFunctionDeclaration): Boolean {
        val annotation = func.annotations.find { it.shortName.asString() == "BeyDestination" }
            ?: return false

        // 1. Cari argumen berdasarkan nama "isStartDestination"
        val arg = annotation.arguments.find { it.name?.asString() == "isStartDestination" }

        // 2. Jika tidak ketemu nama, coba cek berdasarkan posisi (index ke-2)
        // Urutan: [0] route, [1] parentGraph, [2] isStartDestination
        val value = arg?.value ?: annotation.arguments.getOrNull(2)?.value

        // 3. Kembalikan nilai boolean (default false jika null/tidak di-set user)
        return value as? Boolean ?: false
    }

    // Helper untuk mengambil KClass dari Annotation di KSP
    private fun getKClassType(func: KSFunctionDeclaration, paramName: String): KSType {
        val annotation = func.annotations.first { it.shortName.asString() == "BeyDestination" }
        val arg = annotation.arguments.first { it.name?.asString() == paramName }
        return arg.value as KSType
    }
}